package com.st1580.diploma.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.graph.links.AlphaToBetaLink;
import com.st1580.diploma.collector.graph.links.LightLink;
import com.st1580.diploma.collector.graph.links.LinkEndIds;
import com.st1580.diploma.repository.AlphaToBetaRepository;
import com.st1580.diploma.repository.types.EntityActiveType;
import com.st1580.diploma.repository.types.LinkEndActivityType;
import com.st1580.diploma.db.tables.AlphaToBeta;
import com.st1580.diploma.db.tables.records.AlphaToBetaRecord;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.AlphaToBetaEvent;
import com.st1580.diploma.updater.events.BetaEvent;
import com.st1580.diploma.updater.events.EntityEvent;
import com.st1580.diploma.updater.events.EntityEventIdAndTs;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Row5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA_TO_BETA;
import static com.st1580.diploma.repository.impl.RepositoryHelper.fillConnectedEntities;
import static com.st1580.diploma.repository.impl.RepositoryHelper.isActiveLinkByEndStatus;
import static com.st1580.diploma.repository.types.LinkEndActivityType.getTypeByEndStatus;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.row;

@Repository
public class DbAlphaToBetaRepository implements AlphaToBetaRepository {
    @Autowired
    private DSLContext context;
    private final AlphaToBeta TOP_LVL_AB = ALPHA_TO_BETA.as("top_lvl");
    private final AlphaToBeta LOW_LVL_AB = ALPHA_TO_BETA.as("low_lvl");

    @Override
    public Map<Long, List<Long>> getConnectedToEntitiesIdsByFromEntitiesIds(Collection<Long> alphaIds, long ts) {
        Condition condition = ALPHA_TO_BETA.ALPHA_ID.in(alphaIds)
                .and(ALPHA_TO_BETA.CREATED_TS.lessOrEqual(ts));

        Map<Long, List<Long>> existingLinks = getActualLinks(condition)
                .collect(Collectors.groupingBy(AlphaToBetaLink::getAlphaId,
                        Collectors.mapping(AlphaToBetaLink::getBetaId, Collectors.toList())));

        return fillConnectedEntities(alphaIds, existingLinks);
    }

    @Override
    public Map<Long, List<Long>> getConnectedFromEntitiesIdsByToEntitiesIds(Collection<Long> betaIds, long ts) {
        Condition condition = ALPHA_TO_BETA.BETA_ID.in(betaIds)
                .and(ALPHA_TO_BETA.CREATED_TS.lessOrEqual(ts));

        Map<Long, List<Long>> existingLinks = getActualLinks(condition)
                .collect(Collectors.groupingBy(AlphaToBetaLink::getBetaId,
                Collectors.mapping(AlphaToBetaLink::getAlphaId, Collectors.toList())));

        return fillConnectedEntities(betaIds, existingLinks);
    }

    private Stream<AlphaToBetaLink> getActualLinks(Condition condition) {
        Map<AlphaToBetaLink, Boolean> usabilityHelper = new HashMap<>();

        context.select(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID,
                        ALPHA_TO_BETA.CAN_USE, max(ALPHA_TO_BETA.CREATED_TS))
                .from(ALPHA_TO_BETA)
                .where(condition)
                .groupBy(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID, ALPHA_TO_BETA.CAN_USE)
                .orderBy(max(ALPHA_TO_BETA.CREATED_TS))
                .fetch()
                .forEach(record -> {
                    AlphaToBetaLink currIds = new AlphaToBetaLink(
                            record.get(0, Long.class),
                            record.get(1, Long.class),
                            null);
                    usabilityHelper.put(currIds, record.get(2, Boolean.class));
                });

        return usabilityHelper.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey);
    }

    @Override
    public Map<LightLink, ? extends Link> collectAllActiveLinksByEnds(List<LinkEndIds> linkEndIds, long ts) {
        Condition condition = noCondition();
        for (LinkEndIds link : linkEndIds) {
            condition = condition.or(
                    LOW_LVL_AB.ALPHA_ID.eq(link.getFromEntityId())
                    .and(LOW_LVL_AB.BETA_ID.eq(link.getToEntityId()))
                    .and(LOW_LVL_AB.CREATED_TS.lessOrEqual(ts))
            );
        }

        return getABRecordByCondition(condition)
                .stream()
                .map(this::convertToLink)
                .collect(Collectors.toMap(
                        link -> new LightLink(
                                new LightEntity(EntityType.ALPHA, link.getAlphaId()),
                                new LightEntity(EntityType.BETA, link.getBetaId())),
                        Function.identity()
                ));
    }

    @Override
    public void batchInsertNewEvents(List<AlphaToBetaEvent> events) {
        List<Row5<Long, Long, String, Boolean, Long>> rows =
                events.stream().map(this::covertToRecord).collect(Collectors.toList());
        context.insertInto(ALPHA_TO_BETA, ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID,
                        ALPHA_TO_BETA.HASH, ALPHA_TO_BETA.IS_ACTIVE, ALPHA_TO_BETA.CREATED_TS)
                .valuesOfRows(rows)
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public void addLinkEventsTriggeredByEntitiesUpdate(List<Set<AlphaEvent>> batchedAlphaEvents,
                                                       List<Set<BetaEvent>> batchedBetaEvents) {
        Map<AlphaEvent, List<AlphaToBetaRecord>> ABLinksByAlphaEntity = getActualLinksForAlpha(batchedAlphaEvents);
        Map<BetaEvent, List<AlphaToBetaRecord>> ABLinksByBetaEntity = getActualLinksForBeta(batchedBetaEvents);

        List<AlphaToBetaRecord> records = new ArrayList<>();
        records.addAll(convertLinksByEntityToRecords(ABLinksByAlphaEntity, false));
        records.addAll(convertLinksByEntityToRecords(ABLinksByBetaEntity, true));

        context.insertInto(ALPHA_TO_BETA, ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID, ALPHA_TO_BETA.HASH,
                        ALPHA_TO_BETA.IS_ACTIVE, ALPHA_TO_BETA.IS_ACTIVE_ALPHA, ALPHA_TO_BETA.IS_ACTIVE_BETA,
                        ALPHA_TO_BETA.CAN_USE, ALPHA_TO_BETA.CREATED_TS)
                .valuesOfRecords(records)
                .onDuplicateKeyIgnore()
                .execute();
    }

    private Map<AlphaEvent, List<AlphaToBetaRecord>> getActualLinksForAlpha(List<Set<AlphaEvent>> batchedAlphaEvents) {
        Map<AlphaEvent, List<AlphaToBetaRecord>> res = new HashMap<>();

        for (Set<AlphaEvent> batch : batchedAlphaEvents) {
            Map<Long, AlphaEvent> eventById = new HashMap<>();
            Condition condition = noCondition();
            for (AlphaEvent event : batch) {
                condition = condition.or(LOW_LVL_AB.ALPHA_ID.eq(event.getEntityId())
                        .and(LOW_LVL_AB.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getEntityId(), event);
            }

            res.putAll(getABRecordByCondition(condition)
                    .stream()
                    .collect(Collectors.groupingBy(link -> eventById.get(link.getAlphaId())))
            );
        }

        return res;
    }

    private Map<BetaEvent, List<AlphaToBetaRecord>> getActualLinksForBeta(List<Set<BetaEvent>> batchedBetaEvents) {
        Map<BetaEvent, List<AlphaToBetaRecord>> res = new HashMap<>();

        for (Set<BetaEvent> batch : batchedBetaEvents) {
            Map<Long, BetaEvent> eventById = new HashMap<>();
            Condition condition = noCondition();
            for (BetaEvent event : batch) {
                condition = condition.or(LOW_LVL_AB.BETA_ID.eq(event.getEntityId())
                        .and(LOW_LVL_AB.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getEntityId(), event);
            }

            res.putAll(getABRecordByCondition(condition)
                    .stream()
                    .collect(Collectors.groupingBy(link -> eventById.get(link.getBetaId())))
            );
        }

        return res;
    }

    @Override
    public List<EntityEventIdAndTs> getUndefinedFromStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(ALPHA_TO_BETA.CREATED_TS.lessThan(tsTo))
                        .and(ALPHA_TO_BETA.IS_ACTIVE_ALPHA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEventIdAndTs(record.getAlphaId(), record.getCreatedTs()));
    }

    @Override
    public List<EntityEventIdAndTs> getUndefinedToStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(ALPHA_TO_BETA.CREATED_TS.lessThan(tsTo))
                        .and(ALPHA_TO_BETA.IS_ACTIVE_BETA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEventIdAndTs(record.getBetaId(), record.getCreatedTs()));
    }

    @Override
    public void deleteUndefinedLinks(long tsFrom, long tsTo) {
        context.batchDelete(
                context.selectFrom(ALPHA_TO_BETA)
                        .where(ALPHA_TO_BETA.CREATED_TS.greaterOrEqual(tsFrom)
                                .and(ALPHA_TO_BETA.CREATED_TS.lessThan(tsTo))
                                .and(ALPHA_TO_BETA.IS_ACTIVE_ALPHA.eq(LinkEndActivityType.UNDEFINED.name())
                                        .or(ALPHA_TO_BETA.IS_ACTIVE_BETA.eq(LinkEndActivityType.UNDEFINED.name())))
                        )
                        .fetch()
        ).execute();
    }

    @Override
    public void batchUpdateLinksDependentOnFromEntity(Map<EntityEventIdAndTs, Boolean> alphaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEventIdAndTs event : alphaActiveStatusByEvent.keySet()) {
                ctx.dsl().update(ALPHA_TO_BETA)
                        .set(ALPHA_TO_BETA.IS_ACTIVE_ALPHA,
                                LinkEndActivityType.parseBoolean(alphaActiveStatusByEvent.get(event)).name())
                        .where(ALPHA_TO_BETA.ALPHA_ID.eq(event.getEntityId())
                                .and(ALPHA_TO_BETA.CREATED_TS.eq(event.getCreatedTs())))
                        .execute();
            }
        });
    }

    @Override
    public void batchUpdateLinksDependentOnToEntity(Map<EntityEventIdAndTs, Boolean> betaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEventIdAndTs event : betaActiveStatusByEvent.keySet()) {
                ctx.dsl().update(ALPHA_TO_BETA)
                        .set(ALPHA_TO_BETA.IS_ACTIVE_BETA,
                                LinkEndActivityType.parseBoolean(betaActiveStatusByEvent.get(event)).name())
                        .where(ALPHA_TO_BETA.BETA_ID.eq(event.getEntityId())
                                .and(ALPHA_TO_BETA.CREATED_TS.eq(event.getCreatedTs())))
                        .execute();
            }
        });
    }

    private Result<AlphaToBetaRecord> getABRecordByCondition(Condition condition) {
        return context
                .selectFrom(TOP_LVL_AB)
                .whereExists(context
                        .select(LOW_LVL_AB.ALPHA_ID, LOW_LVL_AB.BETA_ID, max(LOW_LVL_AB.CREATED_TS))
                        .from(LOW_LVL_AB)
                        .where(condition)
                        .groupBy(LOW_LVL_AB.ALPHA_ID, LOW_LVL_AB.BETA_ID)
                        .having(LOW_LVL_AB.ALPHA_ID.eq(TOP_LVL_AB.ALPHA_ID)
                                .and(LOW_LVL_AB.BETA_ID.eq(TOP_LVL_AB.BETA_ID))
                                .and(max(LOW_LVL_AB.CREATED_TS).eq(TOP_LVL_AB.CREATED_TS)))
                )
                .fetch();
    }

    private List<AlphaToBetaRecord> convertLinksByEntityToRecords(
            Map<? extends EntityEvent, List<AlphaToBetaRecord>> linksByEntity,
            boolean setFromStatusToUndefined) {

        return linksByEntity.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(link -> new AlphaToBetaRecord(
                                link.getAlphaId(),
                                link.getBetaId(),
                                link.getHash(),
                                isActiveLinkByEndStatus(entry.getKey().getType(), link.getIsActive()),
                                setFromStatusToUndefined ?
                                        LinkEndActivityType.UNDEFINED.name() :
                                        getTypeByEndStatus(entry.getKey().getType()).name(),
                                setFromStatusToUndefined ?
                                        getTypeByEndStatus(entry.getKey().getType()).name() :
                                        LinkEndActivityType.UNDEFINED.name(),
                                null,
                                entry.getKey().getCreatedTs()
                        ))
                )
                .collect(Collectors.toList());
    }

    private Row5<Long, Long, String, Boolean, Long> covertToRecord(AlphaToBetaEvent event) {
        return row(
                event.getFromId(),
                event.getToId(),
                event.getHash(),
                event.isActive(),
                event.getCreatedTs()
        );
    }

    private AlphaToBetaLink convertToLink(AlphaToBetaRecord record) {
        return new AlphaToBetaLink(
                record.getAlphaId(),
                record.getBetaId(),
                record.getHash()
        );
    }
}
