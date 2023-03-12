package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.entities.DeltaEntity;
import com.st1580.diploma.collector.repository.DeltaRepository;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.db.tables.Delta;
import com.st1580.diploma.db.tables.records.AlphaRecord;
import com.st1580.diploma.db.tables.records.DeltaRecord;
import com.st1580.diploma.updater.events.DeltaEvent;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA;
import static com.st1580.diploma.db.Tables.DELTA;
import static org.jooq.impl.DSL.max;

@Repository
public class DbDeltaRepository implements DeltaRepository {
    @Autowired
    private DSLContext context;

    @Inject
    GammaToDeltaRepository gammaToDeltaRepository;

    @Override
    public Map<Long, DeltaEntity> collectAllEntitiesByIds(Collection<Long> ids, long ts) {
        Delta TOP_LVL_DELTA = DELTA.as("top_lvl");
        Delta LOW_LVL_DELTA = DELTA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_DELTA)
                .whereExists(
                        context.select(LOW_LVL_DELTA.ID, max(LOW_LVL_DELTA.CREATED_TS))
                                .from(LOW_LVL_DELTA)
                                .where(LOW_LVL_DELTA.ID.in(ids)
                                        .and(LOW_LVL_DELTA.ACTIVE_STATUS.in(EntityActiveType.trueEntityActiveTypes))
                                        .and(LOW_LVL_DELTA.CREATED_TS.lessOrEqual(ts)))
                                .groupBy(LOW_LVL_DELTA.ID)
                                .having(LOW_LVL_DELTA.ID.eq(TOP_LVL_DELTA.ID)
                                        .and(max(LOW_LVL_DELTA.CREATED_TS).eq(TOP_LVL_DELTA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(this::convertToDeltaEntity)
                .collect(Collectors.toMap(
                        DeltaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids, long ts) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.GAMMA, gammaToDeltaRepository.getConnectedGammaEntitiesIdsByDeltaIds(ids, ts));
        });

        return res;
    }

    @Override
    public Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids,
                                                                                          long ts) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.GAMMA, new HashMap<>(gammaToDeltaRepository.getConnectedGammaEntitiesByDeltaIds(ids,
                    ts)));
        });

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

    private DeltaRecord covertToDeltaRecord(DeltaEvent event) {
        return new DeltaRecord(
                event.getId(),
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
}
