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
import com.st1580.diploma.repository.GammaToAlphaRepository;
import com.st1580.diploma.repository.types.EntityActiveType;
import com.st1580.diploma.repository.types.LinkEndActivityType;
import com.st1580.diploma.db.tables.GammaToAlpha;
import com.st1580.diploma.db.tables.records.GammaToAlphaRecord;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.EntityEvent;
import com.st1580.diploma.updater.events.GammaEvent;
import com.st1580.diploma.updater.events.GammaToAlphaEvent;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Row5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA_TO_BETA;
import static com.st1580.diploma.db.Tables.GAMMA_TO_ALPHA;
import static com.st1580.diploma.repository.impl.RepositoryHelper.fillConnectedEntities;
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
    public Map<Long, List<Long>> getConnectedGammaEntitiesIdsByAlphaIds(Collection<Long> alphaIds, long ts) {
        Condition condition = GAMMA_TO_ALPHA.ALPHA_ID.in(alphaIds)
                .and(GAMMA_TO_ALPHA.CREATED_TS.lessOrEqual(ts));

        Map<Long, List<Long>> existingLinks = getActualLinks(condition)
                .collect(Collectors.groupingBy(GammaToAlphaLink::getAlphaId,
                        Collectors.mapping(GammaToAlphaLink::getGammaId, Collectors.toList())));

        return fillConnectedEntities(alphaIds, existingLinks);
    }

    @Override
    public Map<Long, List<Long>> getConnectedAlphaEntitiesIdsByGammaIds(Collection<Long> gammaIds, long ts) {
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
    public Map<LightLink, ? extends Link> collectAllActiveLinksByEnds(Collection<Long> fromIds,
                                                                      Collection<Long> toIds, long ts) {
        Condition condition = LOW_LVL_GA.GAMMA_ID.in(fromIds)
                .and(LOW_LVL_GA.ALPHA_ID.in(toIds))
                .and(LOW_LVL_GA.CREATED_TS.lessOrEqual(ts));

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
    public Map<Long, List<GammaToAlphaLink>> getConnectedGammaEntitiesByAlphaIds(Collection<Long> alphaIds, long ts) {
        Condition condition = LOW_LVL_GA.ALPHA_ID.in(alphaIds)
                .and(LOW_LVL_GA.CREATED_TS.lessOrEqual(ts));

        Map<Long, List<GammaToAlphaLink>> existingLinks = getGARecordByCondition(condition)
                .stream()
                .filter(GammaToAlphaRecord::getCanUse)
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(GammaToAlphaLink::getAlphaId));

        return fillConnectedEntities(alphaIds, existingLinks);
    }

    @Override
    public Map<Long, List<GammaToAlphaLink>> getConnectedAlphaEntitiesByGammaIds(Collection<Long> gammaIds, long ts) {
        Condition condition = LOW_LVL_GA.GAMMA_ID.in(gammaIds)
                .and(LOW_LVL_GA.CREATED_TS.lessOrEqual(ts));

        Map<Long, List<GammaToAlphaLink>> existingLinks = getGARecordByCondition(condition)
                .stream()
                .filter(GammaToAlphaRecord::getCanUse)
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(GammaToAlphaLink::getGammaId));

        return fillConnectedEntities(gammaIds, existingLinks);
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

        records.addAll(GALinksByGammaEntity.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(link -> new GammaToAlphaRecord(
                                link.getGammaId(),
                                link.getAlphaId(),
                                link.getWeight(),
                                entry.getKey().getType() == EntityActiveType.DELETED ? false : link.getIsActive(),
                                EntityActiveType.trueEntityActiveTypes.contains(entry.getKey().getType().name()) ?
                                        LinkEndActivityType.TRUE.name() : LinkEndActivityType.FALSE.name(),
                                link.getIsActiveAlpha(),
                                null,
                                entry.getKey().getCreatedTs()
                        ))
                )
                .collect(Collectors.toList())
        );

        records.addAll(GALinksByAlphaEntity.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(link -> new GammaToAlphaRecord(
                                link.getGammaId(),
                                link.getAlphaId(),
                                link.getWeight(),
                                entry.getKey().getType() == EntityActiveType.DELETED ? false : link.getIsActive(),
                                link.getIsActiveGamma(),
                                EntityActiveType.trueEntityActiveTypes.contains(entry.getKey().getType().name()) ?
                                        LinkEndActivityType.TRUE.name() : LinkEndActivityType.FALSE.name(),
                                null,
                                entry.getKey().getCreatedTs()
                        ))
                )
                .collect(Collectors.toList())
        );

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
                condition = condition.or(LOW_LVL_GA.GAMMA_ID.eq(event.getGammaId())
                        .and(LOW_LVL_GA.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getGammaId(), event);
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
                condition = condition.or(LOW_LVL_GA.ALPHA_ID.eq(event.getAlphaId())
                        .and(LOW_LVL_GA.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getAlphaId(), event);
            }

            res.putAll(getGARecordByCondition(condition)
                    .stream()
                    .collect(Collectors.groupingBy(link -> eventById.get(link.getAlphaId())))
            );
        }

        return res;
    }

    @Override
    public List<EntityEvent> getUndefinedGammaStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(GAMMA_TO_ALPHA.CREATED_TS.lessThan(tsTo))
                        .and(GAMMA_TO_ALPHA.IS_ACTIVE_GAMMA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEvent(record.getGammaId(), record.getCreatedTs()));
    }

    @Override
    public List<EntityEvent> getUndefinedAlphaStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(GAMMA_TO_ALPHA.CREATED_TS.lessThan(tsTo))
                        .and(GAMMA_TO_ALPHA.IS_ACTIVE_ALPHA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEvent(record.getAlphaId(), record.getCreatedTs()));
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
    public void batchUpdateLinksDependentOnGamma(Map<EntityEvent, Boolean> gammaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEvent event : gammaActiveStatusByEvent.keySet()) {
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
    public void batchUpdateLinksDependentOnAlpha(Map<EntityEvent, Boolean> alphaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEvent event : alphaActiveStatusByEvent.keySet()) {
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

    private Row5<Long, Long, Long, Boolean, Long> covertToRow(GammaToAlphaEvent event) {
        return row(
                event.getGammaId(),
                event.getAlphaId(),
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
