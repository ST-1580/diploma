package com.st1580.diploma.repository.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.DeltaEntity;
import com.st1580.diploma.db.tables.Delta;
import com.st1580.diploma.db.tables.records.DeltaRecord;
import com.st1580.diploma.repository.DeltaRepository;
import com.st1580.diploma.repository.GammaToDeltaRepository;
import com.st1580.diploma.repository.types.EntityActiveType;
import com.st1580.diploma.updater.events.DeltaEvent;
import com.st1580.diploma.updater.events.EntityEventIdAndTs;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.DELTA;
import static com.st1580.diploma.repository.impl.RepositoryHelper.getBatches;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.noCondition;

@Repository
public class DbDeltaRepository implements DeltaRepository {
    @Autowired
    private DSLContext context;
    @Inject
    GammaToDeltaRepository gammaToDeltaRepository;
    private final Delta TOP_LVL_DELTA = DELTA.as("top_lvl");
    private final Delta LOW_LVL_DELTA = DELTA.as("low_lvl");

    @Override
    public Map<Long, DeltaEntity> collectAllActiveEntitiesByIds(Collection<Long> ids, long ts) {
        Condition condition = LOW_LVL_DELTA.ID.in(ids)
                .and(LOW_LVL_DELTA.CREATED_TS.lessOrEqual(ts));

        return getDeltaRecordByCondition(condition)
                .stream()
                .filter(record -> EntityActiveType.trueEntityActiveTypes.contains(record.getActiveStatus()))
                .map(this::convertToDeltaEntity)
                .collect(Collectors.toMap(
                        DeltaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids, long ts) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();
        res.put(EntityType.GAMMA, gammaToDeltaRepository.getConnectedFromEntitiesIdsByToEntitiesIds(ids, ts));
        return res;
    }

    @Override
    public void batchInsertNewEvents(List<DeltaEvent> events) {
        List<DeltaRecord> records = events.stream().map(this::covertToDeltaRecord).collect(Collectors.toList());
        context.insertInto(DELTA, DELTA.ID, DELTA.NAME, DELTA.ACTIVE_STATUS, DELTA.CREATED_TS)
                .valuesOfRecords(records)
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public List<Set<DeltaEvent>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo) {
        Map<Long, List<DeltaEvent>> deltaEventsById = context
                .selectFrom(DELTA)
                .where(DELTA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(DELTA.CREATED_TS.lessThan(tsTo))
                        .and(DELTA.ACTIVE_STATUS.in(EntityActiveType.changedEntityActiveTypes))
                )
                .fetchInto(DeltaEvent.class)
                .stream()
                .collect(Collectors.groupingBy(DeltaEvent::getEntityId));

        return getBatches(deltaEventsById);
    }

    @Override
    public void correctDependentLinks(long tsFrom, long tsTo) {
        List<EntityEventIdAndTs> gammaToDeltaUndefinedDependencies =
                gammaToDeltaRepository.getUndefinedToStateInRange(tsFrom, tsTo);

        Map<Long, List<EntityEventIdAndTs>> deltaDependenciesById =
                gammaToDeltaUndefinedDependencies.stream()
                        .collect(Collectors.groupingBy(EntityEventIdAndTs::getEntityId));

        Map<EntityEventIdAndTs, Boolean> actualDeltaState = new HashMap<>();
        for (Set<EntityEventIdAndTs> batch : getBatches(deltaDependenciesById)) {
            Map<Long, EntityEventIdAndTs> eventById = new HashMap<>();

            Condition condition = noCondition();
            for (EntityEventIdAndTs event : batch) {
                condition = condition.or(LOW_LVL_DELTA.ID.eq(event.getEntityId())
                        .and(LOW_LVL_DELTA.CREATED_TS.lessOrEqual(event.getCreatedTs())));
                eventById.put(event.getEntityId(), event);
            }

            actualDeltaState.putAll(getDeltaRecordByCondition(condition)
                    .stream()
                    .map(this::convertToDeltaEvent)
                    .collect(Collectors.toMap(
                            event -> eventById.get(event.getEntityId()),
                            event -> EntityActiveType.trueEntityActiveTypes.contains(event.getType().name())
                    ))
            );
        }

        gammaToDeltaRepository.batchUpdateLinksDependentOnToEntity(actualDeltaState);
    }

    private Result<DeltaRecord> getDeltaRecordByCondition(Condition condition) {
        return context
                .selectFrom(TOP_LVL_DELTA)
                .whereExists(
                        context.select(LOW_LVL_DELTA.ID, max(LOW_LVL_DELTA.CREATED_TS))
                                .from(LOW_LVL_DELTA)
                                .where(condition)
                                .groupBy(LOW_LVL_DELTA.ID)
                                .having(LOW_LVL_DELTA.ID.eq(TOP_LVL_DELTA.ID)
                                        .and(max(LOW_LVL_DELTA.CREATED_TS).eq(TOP_LVL_DELTA.CREATED_TS)))
                )
                .fetch();
    }

    private DeltaRecord covertToDeltaRecord(DeltaEvent event) {
        return new DeltaRecord(
                event.getEntityId(),
                event.getName(),
                event.getType().name(),
                event.getCreatedTs()
        );
    }

    private DeltaEntity convertToDeltaEntity(DeltaRecord record) {
        return new DeltaEntity(
                record.getId(),
                record.getName()
        );
    }

    private DeltaEvent convertToDeltaEvent(DeltaRecord record) {
        return new DeltaEvent(
                record.getId(),
                record.getName(),
                EntityActiveType.valueOf(record.getActiveStatus()),
                record.getCreatedTs()
        );
    }
}
