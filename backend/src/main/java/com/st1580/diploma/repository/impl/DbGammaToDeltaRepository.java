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
import com.st1580.diploma.collector.graph.links.GammaToDeltaLink;
import com.st1580.diploma.collector.graph.links.LightLink;
import com.st1580.diploma.collector.graph.links.LinkEndIds;
import com.st1580.diploma.db.tables.records.GammaToAlphaRecord;
import com.st1580.diploma.repository.GammaToDeltaRepository;
import com.st1580.diploma.repository.types.EntityActiveType;
import com.st1580.diploma.repository.types.LinkEndActivityType;
import com.st1580.diploma.db.tables.GammaToDelta;
import com.st1580.diploma.db.tables.records.GammaToDeltaRecord;
import com.st1580.diploma.updater.events.DeltaEvent;
import com.st1580.diploma.updater.events.EntityEvent;
import com.st1580.diploma.updater.events.EntityEventIdAndTs;
import com.st1580.diploma.updater.events.GammaEvent;
import com.st1580.diploma.updater.events.GammaToDeltaEvent;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Row4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA_TO_DELTA;
import static com.st1580.diploma.repository.impl.RepositoryHelper.fillConnectedEntities;
import static com.st1580.diploma.repository.impl.RepositoryHelper.isActiveLinkByEndStatus;
import static com.st1580.diploma.repository.types.LinkEndActivityType.getTypeByEndStatus;
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
    public Map<Long, List<Long>> getConnectedFromEntitiesIdsByToEntitiesIds(Collection<Long> deltaIds, long ts) {
        Condition condition = GAMMA_TO_DELTA.DELTA_ID.in(deltaIds)
                .and(GAMMA_TO_DELTA.CREATED_TS.lessOrEqual(ts));

        Map<Long, List<Long>> existingLinks = getActiveLinks(condition)
                .collect(Collectors.groupingBy(GammaToDeltaLink::getDeltaId,
                        Collectors.mapping(GammaToDeltaLink::getGammaId, Collectors.toList())));

        return fillConnectedEntities(deltaIds, existingLinks);
    }

    @Override
    public Map<Long, List<Long>> getConnectedToEntitiesIdsByFromEntitiesIds(Collection<Long> gammaIds, long ts) {
        Condition condition = GAMMA_TO_DELTA.GAMMA_ID.in(gammaIds)
                .and(GAMMA_TO_DELTA.CREATED_TS.lessOrEqual(ts));

        Map<Long, List<Long>> existingLinks = getActiveLinks(condition)
                .collect(Collectors.groupingBy(GammaToDeltaLink::getGammaId,
                        Collectors.mapping(GammaToDeltaLink::getDeltaId, Collectors.toList())));

        return fillConnectedEntities(gammaIds, existingLinks);
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
    public Map<LightLink, ? extends Link> collectAllActiveLinksByEnds(List<LinkEndIds> linkEndIds, long ts) {
        Condition condition = noCondition();
        for (LinkEndIds link : linkEndIds) {
            condition = condition.or(
                    LOW_LVL_GD.GAMMA_ID.eq(link.getFromEntityId())
                    .and(LOW_LVL_GD.DELTA_ID.eq(link.getToEntityId()))
                    .and(LOW_LVL_GD.CREATED_TS.lessOrEqual(ts))
            );
        }

        return getGDRecordByCondition(condition)
                .stream()
                .map(this::convertToLink)
                .collect(Collectors.toMap(
                        link -> new LightLink(
                                new LightEntity(EntityType.GAMMA, link.getGammaId()),
                                new LightEntity(EntityType.DELTA, link.getDeltaId())),
                        Function.identity()
                ));
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
        records.addAll(convertLinksByEntityToRecords(GDLinksByGammaEntity, false));
        records.addAll(convertLinksByEntityToRecords(GDLinksByDeltaEntity, true));

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
                condition = condition.or(LOW_LVL_GD.GAMMA_ID.eq(event.getEntityId())
                        .and(LOW_LVL_GD.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getEntityId(), event);
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
                condition = condition.or(LOW_LVL_GD.DELTA_ID.eq(event.getEntityId())
                        .and(LOW_LVL_GD.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getEntityId(), event);
            }

            res.putAll(getGDRecordByCondition(condition)
                    .stream()
                    .collect(Collectors.groupingBy(link -> eventById.get(link.getDeltaId())))
            );
        }

        return res;
    }

    @Override
    public List<EntityEventIdAndTs> getUndefinedFromStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(GAMMA_TO_DELTA.CREATED_TS.lessThan(tsTo))
                        .and(GAMMA_TO_DELTA.IS_ACTIVE_GAMMA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEventIdAndTs(record.getGammaId(), record.getCreatedTs()));
    }

    @Override
    public List<EntityEventIdAndTs> getUndefinedToStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(GAMMA_TO_DELTA.CREATED_TS.lessThan(tsTo))
                        .and(GAMMA_TO_DELTA.IS_ACTIVE_DELTA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEventIdAndTs(record.getDeltaId(), record.getCreatedTs()));
    }

    @Override
    public void deleteUndefinedLinks(long tsFrom, long tsTo) {
        context.batchDelete(
                context.selectFrom(GAMMA_TO_DELTA)
                        .where(GAMMA_TO_DELTA.CREATED_TS.greaterOrEqual(tsFrom)
                                .and(GAMMA_TO_DELTA.CREATED_TS.lessThan(tsTo))
                                .and(GAMMA_TO_DELTA.IS_ACTIVE_GAMMA.eq(LinkEndActivityType.UNDEFINED.name())
                                        .or(GAMMA_TO_DELTA.IS_ACTIVE_DELTA.eq(LinkEndActivityType.UNDEFINED.name())))
                        )
                        .fetch()
        ).execute();
    }

    @Override
    public void batchUpdateLinksDependentOnFromEntity(Map<EntityEventIdAndTs, Boolean> gammaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEventIdAndTs event : gammaActiveStatusByEvent.keySet()) {
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
    public void batchUpdateLinksDependentOnToEntity(Map<EntityEventIdAndTs, Boolean> deltaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEventIdAndTs event : deltaActiveStatusByEvent.keySet()) {
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

    private List<GammaToDeltaRecord> convertLinksByEntityToRecords(
            Map<? extends EntityEvent, List<GammaToDeltaRecord>> linksByEntity,
            boolean setFromStatusToUndefined) {

        return linksByEntity.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(link -> new GammaToDeltaRecord(
                                link.getGammaId(),
                                link.getDeltaId(),
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

    private Row4<Long, Long, Boolean, Long> covertToRow(GammaToDeltaEvent event) {
        return row(event.getFromId(), event.getToId(), event.isActive(), event.getCreatedTs());
    }

    private GammaToDeltaLink convertToLink(GammaToDeltaRecord record) {
        return new GammaToDeltaLink(
                record.getGammaId(),
                record.getDeltaId()
        );
    }
}
