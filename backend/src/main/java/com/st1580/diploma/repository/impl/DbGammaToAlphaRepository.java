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
import com.st1580.diploma.collector.graph.links.GammaToAlphaLink;
import com.st1580.diploma.collector.graph.links.LightLink;
import com.st1580.diploma.db.tables.records.AlphaToBetaRecord;
import com.st1580.diploma.repository.GammaToAlphaRepository;
import com.st1580.diploma.repository.types.EntityActiveType;
import com.st1580.diploma.repository.types.LinkEndActivityType;
import com.st1580.diploma.db.tables.GammaToAlpha;
import com.st1580.diploma.db.tables.records.GammaToAlphaRecord;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.EntityEvent;
import com.st1580.diploma.updater.events.EntityEventIdAndTs;
import com.st1580.diploma.updater.events.GammaEvent;
import com.st1580.diploma.updater.events.GammaToAlphaEvent;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Row5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA_TO_ALPHA;
import static com.st1580.diploma.repository.impl.RepositoryHelper.fillConnectedEntities;
import static com.st1580.diploma.repository.impl.RepositoryHelper.isActiveLinkByEndStatus;
import static com.st1580.diploma.repository.types.LinkEndActivityType.getTypeByEndStatus;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.row;

@Repository
public class DbGammaToAlphaRepository implements GammaToAlphaRepository {
    @Autowired
    private DSLContext context;
    private final GammaToAlpha TOP_LVL_GA = GAMMA_TO_ALPHA.as("top_lvl");
    private final GammaToAlpha LOW_LVL_GA = GAMMA_TO_ALPHA.as("low_lvl");


    @Override
    public Map<Long, List<Long>> getConnectedFromEntitiesIdsByToEntitiesIds(Collection<Long> alphaIds, long ts) {
        Condition condition = GAMMA_TO_ALPHA.ALPHA_ID.in(alphaIds)
                .and(GAMMA_TO_ALPHA.CREATED_TS.lessOrEqual(ts));

        Map<Long, List<Long>> existingLinks = getActualLinks(condition)
                .collect(Collectors.groupingBy(GammaToAlphaLink::getAlphaId,
                        Collectors.mapping(GammaToAlphaLink::getGammaId, Collectors.toList())));

        return fillConnectedEntities(alphaIds, existingLinks);
    }

    @Override
    public Map<Long, List<Long>> getConnectedToEntitiesIdsByFromEntitiesIds(Collection<Long> gammaIds, long ts) {
        Condition condition = GAMMA_TO_ALPHA.GAMMA_ID.in(gammaIds)
                .and(GAMMA_TO_ALPHA.CREATED_TS.lessOrEqual(ts));

        Map<Long, List<Long>> existingLinks = getActualLinks(condition)
                .collect(Collectors.groupingBy(GammaToAlphaLink::getGammaId,
                        Collectors.mapping(GammaToAlphaLink::getAlphaId, Collectors.toList())));

        return fillConnectedEntities(gammaIds, existingLinks);
    }

    private Stream<GammaToAlphaLink> getActualLinks(Condition condition) {
        Map<GammaToAlphaLink, Boolean> usabilityHelper = new HashMap<>();

        context.select(GAMMA_TO_ALPHA.GAMMA_ID, GAMMA_TO_ALPHA.ALPHA_ID,
                        GAMMA_TO_ALPHA.CAN_USE, max(GAMMA_TO_ALPHA.CREATED_TS))
                .from(GAMMA_TO_ALPHA)
                .where(condition)
                .groupBy(GAMMA_TO_ALPHA.GAMMA_ID, GAMMA_TO_ALPHA.ALPHA_ID, GAMMA_TO_ALPHA.CAN_USE)
                .orderBy(max(GAMMA_TO_ALPHA.CREATED_TS))
                .fetch()
                .forEach(record -> {
                    GammaToAlphaLink currIds = new GammaToAlphaLink(
                            record.get(0, Long.class),
                            record.get(1, Long.class),
                            0);
                    usabilityHelper.put(currIds, record.get(2, Boolean.class));
                });

        return usabilityHelper.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey);
    }

    @Override
    public Map<LightLink, ? extends Link> collectAllActiveLinksByEnds(Map<Long, Long> linkEndIds, long ts) {
        Condition condition = noCondition();
        linkEndIds.forEach((gammaId, alphaId) -> condition.or(
                        LOW_LVL_GA.GAMMA_ID.in(gammaId)
                        .and(LOW_LVL_GA.ALPHA_ID.in(alphaId))
                        .and(LOW_LVL_GA.CREATED_TS.lessOrEqual(ts))
                )
        );

        return getGARecordByCondition(condition)
                .stream()
                .map(this::convertToLink)
                .collect(Collectors.toMap(
                        link -> new LightLink(
                                new LightEntity(EntityType.GAMMA, link.getGammaId()),
                                new LightEntity(EntityType.ALPHA, link.getAlphaId())),
                        Function.identity()
                ));
    }

    @Override
    public void batchInsertNewEvents(List<GammaToAlphaEvent> events) {
        List<Row5<Long, Long, Long, Boolean, Long>> rows =
                events.stream().map(this::covertToRow).collect(Collectors.toList());
        context.insertInto(GAMMA_TO_ALPHA, GAMMA_TO_ALPHA.GAMMA_ID, GAMMA_TO_ALPHA.ALPHA_ID,
                        GAMMA_TO_ALPHA.WEIGHT, GAMMA_TO_ALPHA.IS_ACTIVE, GAMMA_TO_ALPHA.CREATED_TS)
                .valuesOfRows(rows)
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public void addLinkEventsTriggeredByEntitiesUpdate(List<Set<GammaEvent>> gammaEvents, List<Set<AlphaEvent>> alphaEvents) {
        Map<GammaEvent, List<GammaToAlphaRecord>> GALinksByGammaEntity = getActualLinksForGamma(gammaEvents);
        Map<AlphaEvent, List<GammaToAlphaRecord>> GALinksByAlphaEntity = getActualLinksForAlpha(alphaEvents);

        List<GammaToAlphaRecord> records = new ArrayList<>();
        records.addAll(convertLinksByEntityToRecords(GALinksByGammaEntity, false));
        records.addAll(convertLinksByEntityToRecords(GALinksByAlphaEntity, true));

        context.insertInto(GAMMA_TO_ALPHA, GAMMA_TO_ALPHA.GAMMA_ID, GAMMA_TO_ALPHA.ALPHA_ID, GAMMA_TO_ALPHA.WEIGHT,
                        GAMMA_TO_ALPHA.IS_ACTIVE, GAMMA_TO_ALPHA.IS_ACTIVE_GAMMA, GAMMA_TO_ALPHA.IS_ACTIVE_ALPHA,
                        GAMMA_TO_ALPHA.CAN_USE, GAMMA_TO_ALPHA.CREATED_TS)
                .valuesOfRecords(records)
                .onDuplicateKeyIgnore()
                .execute();
    }

    private Map<GammaEvent, List<GammaToAlphaRecord>> getActualLinksForGamma(List<Set<GammaEvent>> batchedGammaEvents) {
        Map<GammaEvent, List<GammaToAlphaRecord>> res = new HashMap<>();

        for (Set<GammaEvent> batch : batchedGammaEvents) {
            Map<Long, GammaEvent> eventById = new HashMap<>();
            Condition condition = noCondition();
            for (GammaEvent event : batch) {
                condition = condition.or(LOW_LVL_GA.GAMMA_ID.eq(event.getEntityId())
                        .and(LOW_LVL_GA.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getEntityId(), event);
            }

            res.putAll(getGARecordByCondition(condition)
                    .stream()
                    .collect(Collectors.groupingBy(link -> eventById.get(link.getGammaId())))
            );
        }

        return res;
    }

    private Map<AlphaEvent, List<GammaToAlphaRecord>> getActualLinksForAlpha(List<Set<AlphaEvent>> batchedAlphaEvents) {
        Map<AlphaEvent, List<GammaToAlphaRecord>> res = new HashMap<>();

        for (Set<AlphaEvent> batch : batchedAlphaEvents) {
            Map<Long, AlphaEvent> eventById = new HashMap<>();
            Condition condition = noCondition();
            for (AlphaEvent event : batch) {
                condition = condition.or(LOW_LVL_GA.ALPHA_ID.eq(event.getEntityId())
                        .and(LOW_LVL_GA.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getEntityId(), event);
            }

            res.putAll(getGARecordByCondition(condition)
                    .stream()
                    .collect(Collectors.groupingBy(link -> eventById.get(link.getAlphaId())))
            );
        }

        return res;
    }

    @Override
    public List<EntityEventIdAndTs> getUndefinedFromStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(GAMMA_TO_ALPHA.CREATED_TS.lessThan(tsTo))
                        .and(GAMMA_TO_ALPHA.IS_ACTIVE_GAMMA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEventIdAndTs(record.getGammaId(), record.getCreatedTs()));
    }

    @Override
    public List<EntityEventIdAndTs> getUndefinedToStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(GAMMA_TO_ALPHA.CREATED_TS.lessThan(tsTo))
                        .and(GAMMA_TO_ALPHA.IS_ACTIVE_ALPHA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEventIdAndTs(record.getAlphaId(), record.getCreatedTs()));
    }

    @Override
    public void deleteUndefinedLinks(long tsFrom, long tsTo) {
        context.batchDelete(
                context.selectFrom(GAMMA_TO_ALPHA)
                        .where(GAMMA_TO_ALPHA.CREATED_TS.greaterOrEqual(tsFrom)
                                .and(GAMMA_TO_ALPHA.CREATED_TS.lessThan(tsTo))
                                .and(GAMMA_TO_ALPHA.IS_ACTIVE_GAMMA.eq(LinkEndActivityType.UNDEFINED.name())
                                        .or(GAMMA_TO_ALPHA.IS_ACTIVE_ALPHA.eq(LinkEndActivityType.UNDEFINED.name())))
                        )
                        .fetch()
        ).execute();
    }

    @Override
    public void batchUpdateLinksDependentOnFromEntity(Map<EntityEventIdAndTs, Boolean> gammaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEventIdAndTs event : gammaActiveStatusByEvent.keySet()) {
                ctx.dsl().update(GAMMA_TO_ALPHA)
                        .set(GAMMA_TO_ALPHA.IS_ACTIVE_GAMMA,
                                LinkEndActivityType.parseBoolean(gammaActiveStatusByEvent.get(event)).name())
                        .where(GAMMA_TO_ALPHA.GAMMA_ID.eq(event.getEntityId())
                                .and(GAMMA_TO_ALPHA.CREATED_TS.eq(event.getCreatedTs())))
                        .execute();
            }
        });
    }

    @Override
    public void batchUpdateLinksDependentOnToEntity(Map<EntityEventIdAndTs, Boolean> alphaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEventIdAndTs event : alphaActiveStatusByEvent.keySet()) {
                ctx.dsl().update(GAMMA_TO_ALPHA)
                        .set(GAMMA_TO_ALPHA.IS_ACTIVE_ALPHA,
                                LinkEndActivityType.parseBoolean(alphaActiveStatusByEvent.get(event)).name())
                        .where(GAMMA_TO_ALPHA.ALPHA_ID.eq(event.getEntityId())
                                .and(GAMMA_TO_ALPHA.CREATED_TS.eq(event.getCreatedTs())))
                        .execute();
            }
        });
    }

    private Result<GammaToAlphaRecord> getGARecordByCondition(Condition condition) {
        return context
                .selectFrom(TOP_LVL_GA)
                .whereExists(context
                        .select(LOW_LVL_GA.GAMMA_ID, LOW_LVL_GA.ALPHA_ID, max(LOW_LVL_GA.CREATED_TS))
                        .from(LOW_LVL_GA)
                        .where(condition)
                        .groupBy(LOW_LVL_GA.GAMMA_ID, LOW_LVL_GA.ALPHA_ID)
                        .having(LOW_LVL_GA.GAMMA_ID.eq(TOP_LVL_GA.GAMMA_ID)
                                .and(LOW_LVL_GA.ALPHA_ID.eq(TOP_LVL_GA.ALPHA_ID))
                                .and(max(LOW_LVL_GA.CREATED_TS).eq(TOP_LVL_GA.CREATED_TS)))
                )
                .fetch();
    }

    private List<GammaToAlphaRecord> convertLinksByEntityToRecords(
            Map<? extends EntityEvent, List<GammaToAlphaRecord>> linksByEntity,
            boolean setFromStatusToUndefined) {

        return linksByEntity.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(link -> new GammaToAlphaRecord(
                                link.getGammaId(),
                                link.getAlphaId(),
                                link.getWeight(),
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

    private Row5<Long, Long, Long, Boolean, Long> covertToRow(GammaToAlphaEvent event) {
        return row(
                event.getFromId(),
                event.getToId(),
                event.getWeight(),
                event.isActive(),
                event.getCreatedTs()
        );
    }

    private GammaToAlphaLink convertToLink(GammaToAlphaRecord record) {
        return new GammaToAlphaLink(
                record.getGammaId(),
                record.getAlphaId(),
                record.getWeight()
        );
    }

}
