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
import com.st1580.diploma.collector.graph.entities.GammaEntity;
import com.st1580.diploma.collector.repository.GammaRepository;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.db.tables.Gamma;
import com.st1580.diploma.db.tables.records.GammaRecord;
import com.st1580.diploma.updater.events.GammaEvent;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA;
import static org.jooq.impl.DSL.max;

@Repository
public class DbGammaRepository implements GammaRepository {
    @Autowired
    private DSLContext context;
    @Inject
    GammaToAlphaRepository gammaToAlphaRepository;
    @Inject
    GammaToDeltaRepository gammaToDeltaRepository;

    @Override
    public Map<Long, GammaEntity> collectAllEntitiesByIds(Collection<Long> ids, long ts) {
        Gamma TOP_LVL_GAMMA = GAMMA.as("top_lvl");
        Gamma LOW_LVL_GAMMA = GAMMA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_GAMMA)
                .whereExists(
                        context.select(LOW_LVL_GAMMA.ID, max(LOW_LVL_GAMMA.CREATED_TS))
                                .from(LOW_LVL_GAMMA)
                                .where(LOW_LVL_GAMMA.ID.in(ids)
                                        .and(LOW_LVL_GAMMA.IS_ACTIVE.in(EntityActiveType.trueEntityActiveTypes))
                                        .and(LOW_LVL_GAMMA.CREATED_TS.lessOrEqual(ts)))
                                .groupBy(LOW_LVL_GAMMA.ID)
                                .having(LOW_LVL_GAMMA.ID.eq(TOP_LVL_GAMMA.ID)
                                        .and(max(LOW_LVL_GAMMA.CREATED_TS).eq(TOP_LVL_GAMMA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(this::convertToGammaEntity)
                .collect(Collectors.toMap(
                        GammaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids, long ts) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, gammaToAlphaRepository.getConnectedAlphaEntitiesIdsByGammaIds(ids, ts));
            res.put(EntityType.DELTA, gammaToDeltaRepository.getConnectedDeltaEntitiesIdsByGammaIds(ids, ts));
        });

        return res;
    }

    @Override
    public Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids,
                                                                                          long ts) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, new HashMap<>(gammaToAlphaRepository.getConnectedAlphaEntitiesByGammaIds(ids,
                    ts)));
            res.put(EntityType.DELTA, new HashMap<>(gammaToDeltaRepository.getConnectedDeltaEntitiesByGammaIds(ids,
                    ts)));
        });

        return res;
    }

    @Override
    public void batchInsertNewEvents(List<GammaEvent> events) {
        List<GammaRecord> records = events.stream().map(this::convertToGammaRecord).collect(Collectors.toList());
        context.insertInto(GAMMA, GAMMA.ID, GAMMA.IS_MASTER, GAMMA.IS_ACTIVE, GAMMA.CREATED_TS)
                .valuesOfRecords(records)
                .onDuplicateKeyIgnore()
                .execute();
    }

    private GammaRecord convertToGammaRecord(GammaEvent event) {
        return new GammaRecord(
                event.getId(),
                event.isMaster(),
                event.getType().name(),
                event.getCreatedTs()
        );
    }

    private GammaEntity convertToGammaEntity(GammaRecord record) {
        return new GammaEntity(
                record.getId(),
                record.getIsMaster()
        );
    }
}
