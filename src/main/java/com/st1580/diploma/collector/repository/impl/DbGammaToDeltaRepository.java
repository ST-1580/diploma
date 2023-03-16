package com.st1580.diploma.collector.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.st1580.diploma.collector.graph.links.GammaToDeltaLink;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.collector.repository.types.LinkEndActivityType;
import com.st1580.diploma.db.tables.GammaToDelta;
import com.st1580.diploma.db.tables.records.GammaToDeltaRecord;
import com.st1580.diploma.updater.events.DeltaEvent;
import com.st1580.diploma.updater.events.EntityEvent;
import com.st1580.diploma.updater.events.GammaEvent;
import com.st1580.diploma.updater.events.GammaToDeltaEvent;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Row4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA_TO_DELTA;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.row;

@Repository
public class DbGammaToDeltaRepository implements GammaToDeltaRepository {
    @Autowired
    private DSLContext context;
    private final GammaToDelta TOP_LVL_GD = GAMMA_TO_DELTA.as("top_lvl");
    private final GammaToDelta LOW_LVL_GD = GAMMA_TO_DELTA.as("low_lvl");

    @Override
    public Map<Long, List<Long>> getConnectedGammaEntitiesIdsByDeltaIds(Collection<Long> deltaIds, long ts) {
        Condition condition = GAMMA_TO_DELTA.DELTA_ID.in(deltaIds)
                .and(GAMMA_TO_DELTA.CREATED_TS.lessOrEqual(ts));

        return getActiveLinks(condition)
                .collect(Collectors.groupingBy(GammaToDeltaLink::getDeltaId,
                        Collectors.mapping(GammaToDeltaLink::getGammaId, Collectors.toList())));
    }

    @Override
    public Map<Long, List<Long>> getConnectedDeltaEntitiesIdsByGammaIds(Collection<Long> gammaIds, long ts) {
        Condition condition = GAMMA_TO_DELTA.GAMMA_ID.in(gammaIds)
                .and(GAMMA_TO_DELTA.CREATED_TS.lessOrEqual(ts));

        return getActiveLinks(condition)
                .collect(Collectors.groupingBy(GammaToDeltaLink::getGammaId,
                        Collectors.mapping(GammaToDeltaLink::getDeltaId, Collectors.toList())));
    }

    private Stream<GammaToDeltaLink> getActiveLinks(Condition condition) {
        Map<GammaToDeltaLink, Boolean> usabilityHelper = new HashMap<>();

        context.select(GAMMA_TO_DELTA.GAMMA_ID, GAMMA_TO_DELTA.DELTA_ID,
                        GAMMA_TO_DELTA.CAN_USE, max(GAMMA_TO_DELTA.CREATED_TS))
                .from(GAMMA_TO_DELTA)
                .where(condition)
                .groupBy(GAMMA_TO_DELTA.GAMMA_ID, GAMMA_TO_DELTA.DELTA_ID, GAMMA_TO_DELTA.CAN_USE)
                .orderBy(max(GAMMA_TO_DELTA.CREATED_TS))
                .fetch()
                .forEach(record -> {
                    GammaToDeltaLink currIds = new GammaToDeltaLink(
                            record.get(0, Long.class),
                            record.get(1, Long.class));
                    usabilityHelper.put(currIds, record.get(2, Boolean.class));
                });

        return usabilityHelper.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey);
    }

    @Override
    public Map<Long, List<GammaToDeltaLink>> getConnectedGammaEntitiesByDeltaIds(Collection<Long> deltaIds, long ts) {
        Condition condition = LOW_LVL_GD.DELTA_ID.in(deltaIds)
                .and(LOW_LVL_GD.CREATED_TS.lessOrEqual(ts));

        return getGDRecordByCondition(condition)
                .stream()
                .filter(GammaToDeltaRecord::getCanUse)
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(GammaToDeltaLink::getDeltaId));
    }

    @Override
    public Map<Long, List<GammaToDeltaLink>> getConnectedDeltaEntitiesByGammaIds(Collection<Long> gammaIds, long ts) {
        Condition condition = LOW_LVL_GD.GAMMA_ID.in(gammaIds)
                .and(LOW_LVL_GD.CREATED_TS.lessOrEqual(ts));

        return getGDRecordByCondition(condition)
                .stream()
                .filter(GammaToDeltaRecord::getCanUse)
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(GammaToDeltaLink::getGammaId));
    }

    @Override
    public void batchInsertNewEvents(List<GammaToDeltaEvent> events) {
        List<Row4<Long, Long, Boolean, Long>> rows =
                events.stream().map(this::covertToRow).collect(Collectors.toList());
        context.insertInto(GAMMA_TO_DELTA, GAMMA_TO_DELTA.GAMMA_ID, GAMMA_TO_DELTA.DELTA_ID,
                        GAMMA_TO_DELTA.IS_ACTIVE, GAMMA_TO_DELTA.CREATED_TS)
                .valuesOfRows(rows)
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public void addLinkEventsTriggeredByEntitiesUpdate(List<Set<GammaEvent>> gammaEvents,
                                                       List<Set<DeltaEvent>> deltaEvents) {
        Map<GammaEvent, List<GammaToDeltaRecord>> GDLinksByGammaEntity = getActualLinksForGamma(gammaEvents);
        Map<DeltaEvent, List<GammaToDeltaRecord>> GDLinksByDeltaEntity = getActualLinksForDelta(deltaEvents);

        List<GammaToDeltaRecord> records = new ArrayList<>();

        records.addAll(GDLinksByGammaEntity.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(link -> new GammaToDeltaRecord(
                                link.getGammaId(),
                                link.getDeltaId(),
                                entry.getKey().getType() == EntityActiveType.DELETED ? false : link.getIsActive(),
                                EntityActiveType.trueEntityActiveTypes.contains(entry.getKey().getType().name()) ?
                                        LinkEndActivityType.TRUE.name() : LinkEndActivityType.FALSE.name(),
                                link.getIsActiveDelta(),
                                null,
                                entry.getKey().getCreatedTs()
                        ))
                )
                .collect(Collectors.toList())
        );

        records.addAll(GDLinksByDeltaEntity.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(link -> new GammaToDeltaRecord(
                                link.getGammaId(),
                                link.getDeltaId(),
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

        context.insertInto(GAMMA_TO_DELTA, GAMMA_TO_DELTA.GAMMA_ID, GAMMA_TO_DELTA.DELTA_ID,
                        GAMMA_TO_DELTA.IS_ACTIVE, GAMMA_TO_DELTA.IS_ACTIVE_GAMMA, GAMMA_TO_DELTA.IS_ACTIVE_DELTA,
                        GAMMA_TO_DELTA.CAN_USE, GAMMA_TO_DELTA.CREATED_TS)
                .valuesOfRecords(records)
                .onDuplicateKeyIgnore()
                .execute();
    }

    private Map<GammaEvent, List<GammaToDeltaRecord>> getActualLinksForGamma(List<Set<GammaEvent>> batchedGammaEvents) {
        Map<GammaEvent, List<GammaToDeltaRecord>> res = new HashMap<>();

        for (Set<GammaEvent> batch : batchedGammaEvents) {
            Map<Long, GammaEvent> eventById = new HashMap<>();
            Condition condition = noCondition();
            for (GammaEvent event : batch) {
                condition = condition.or(LOW_LVL_GD.GAMMA_ID.eq(event.getGammaId())
                        .and(LOW_LVL_GD.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getGammaId(), event);
            }

            res.putAll(getGDRecordByCondition(condition)
                    .stream()
                    .collect(Collectors.groupingBy(link -> eventById.get(link.getGammaId())))
            );
        }

        return res;
    }

    private Map<DeltaEvent, List<GammaToDeltaRecord>> getActualLinksForDelta(List<Set<DeltaEvent>> batchedDeltaEvents) {
        Map<DeltaEvent, List<GammaToDeltaRecord>> res = new HashMap<>();

        for (Set<DeltaEvent> batch : batchedDeltaEvents) {
            Map<Long, DeltaEvent> eventById = new HashMap<>();
            Condition condition = noCondition();
            for (DeltaEvent event : batch) {
                condition = condition.or(LOW_LVL_GD.DELTA_ID.eq(event.getDeltaId())
                        .and(LOW_LVL_GD.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getDeltaId(), event);
            }

            res.putAll(getGDRecordByCondition(condition)
                    .stream()
                    .collect(Collectors.groupingBy(link -> eventById.get(link.getDeltaId())))
            );
        }

        return res;
    }

    @Override
    public List<EntityEvent> getUndefinedGammaStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(GAMMA_TO_DELTA.CREATED_TS.lessThan(tsTo))
                        .and(GAMMA_TO_DELTA.IS_ACTIVE_GAMMA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEvent(record.getGammaId(), record.getCreatedTs()));
    }

    @Override
    public List<EntityEvent> getUndefinedDeltaStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(GAMMA_TO_DELTA.CREATED_TS.lessThan(tsTo))
                        .and(GAMMA_TO_DELTA.IS_ACTIVE_DELTA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEvent(record.getDeltaId(), record.getCreatedTs()));
    }

    @Override
    public void batchUpdateLinksDependentOnGamma(Map<EntityEvent, Boolean> gammaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEvent event : gammaActiveStatusByEvent.keySet()) {
                ctx.dsl().update(GAMMA_TO_DELTA)
                        .set(GAMMA_TO_DELTA.IS_ACTIVE_GAMMA,
                                LinkEndActivityType.parseBoolean(gammaActiveStatusByEvent.get(event)).name())
                        .where(GAMMA_TO_DELTA.GAMMA_ID.eq(event.getEntityId())
                                .and(GAMMA_TO_DELTA.CREATED_TS.eq(event.getCreatedTs())))
                        .execute();
            }
        });
    }

    @Override
    public void batchUpdateLinksDependentOnDelta(Map<EntityEvent, Boolean> deltaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEvent event : deltaActiveStatusByEvent.keySet()) {
                ctx.dsl().update(GAMMA_TO_DELTA)
                        .set(GAMMA_TO_DELTA.IS_ACTIVE_DELTA,
                                LinkEndActivityType.parseBoolean(deltaActiveStatusByEvent.get(event)).name())
                        .where(GAMMA_TO_DELTA.DELTA_ID.eq(event.getEntityId())
                                .and(GAMMA_TO_DELTA.CREATED_TS.eq(event.getCreatedTs())))
                        .execute();
            }
        });
    }

    private Result<GammaToDeltaRecord> getGDRecordByCondition(Condition condition) {
        return context
                .selectFrom(TOP_LVL_GD)
                .whereExists(context
                        .select(LOW_LVL_GD.GAMMA_ID, LOW_LVL_GD.DELTA_ID, max(LOW_LVL_GD.CREATED_TS))
                        .from(LOW_LVL_GD)
                        .where(condition)
                        .groupBy(LOW_LVL_GD.GAMMA_ID, LOW_LVL_GD.DELTA_ID)
                        .having(LOW_LVL_GD.GAMMA_ID.eq(TOP_LVL_GD.GAMMA_ID)
                                .and(LOW_LVL_GD.DELTA_ID.eq(TOP_LVL_GD.DELTA_ID))
                                .and(max(LOW_LVL_GD.CREATED_TS).eq(TOP_LVL_GD.CREATED_TS)))
                )
                .fetch();
    }

    private Row4<Long, Long, Boolean, Long> covertToRow(GammaToDeltaEvent event) {
        return row(event.getGammaId(), event.getDeltaId(), event.isActive(), event.getCreatedTs());
    }

    private GammaToDeltaLink convertToLink(GammaToDeltaRecord record) {
        return new GammaToDeltaLink(
                record.getGammaId(),
                record.getDeltaId()
        );
    }
}
