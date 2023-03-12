package com.st1580.diploma.collector.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.entities.AlphaEntity;
import com.st1580.diploma.collector.repository.AlphaRepository;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.db.tables.Alpha;
import com.st1580.diploma.db.tables.records.AlphaRecord;
import com.st1580.diploma.updater.events.AlphaEvent;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA;
import static org.jooq.impl.DSL.max;

@Repository
public class DbAlphaRepository implements AlphaRepository {
    @Autowired
    private DSLContext context;
    @Inject
    AlphaToBetaRepository alphaToBetaRepository;
    @Inject
    GammaToAlphaRepository gammaToAlphaRepository;

    @Override
    public Map<Long, AlphaEntity> collectAllEntitiesByIds(Collection<Long> ids, long ts) {
        Alpha TOP_LVL_ALPHA = ALPHA.as("top_lvl");
        Alpha LOW_LVL_ALPHA = ALPHA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_ALPHA)
                .whereExists(
                        context.select(LOW_LVL_ALPHA.ID, max(LOW_LVL_ALPHA.CREATED_TS))
                                .from(LOW_LVL_ALPHA)
                                .where(LOW_LVL_ALPHA.ID.in(ids)
                                        .and(LOW_LVL_ALPHA.ACTIVE_STATUS.in(EntityActiveType.trueEntityActiveTypes))
                                        .and(LOW_LVL_ALPHA.CREATED_TS.lessOrEqual(ts)))
                                .groupBy(LOW_LVL_ALPHA.ID)
                                .having(LOW_LVL_ALPHA.ID.eq(TOP_LVL_ALPHA.ID)
                                        .and(max(LOW_LVL_ALPHA.CREATED_TS).eq(TOP_LVL_ALPHA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(this::convertToAlphaEntity)
                .collect(Collectors.toMap(
                        AlphaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids, long ts) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.BETA, alphaToBetaRepository.getConnectedBetaEntitiesIdsByAlphaIds(ids, ts));
            res.put(EntityType.GAMMA, gammaToAlphaRepository.getConnectedGammaEntitiesIdsByAlphaIds(ids, ts));
        });

        return res;
    }

    @Override
    public Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids,
                                                                                          long ts) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.BETA, new HashMap<>(alphaToBetaRepository.getConnectedBetaEntitiesByAlphaIds(ids, ts)));
            res.put(EntityType.GAMMA, new HashMap<>(gammaToAlphaRepository.getConnectedGammaEntitiesByAlphaIds(ids,
                    ts)));
        });

        return res;
    }

    @Override
    public void batchInsertNewEvents(List<AlphaEvent> events) {
        List<AlphaRecord> records = events.stream().map(this::covertToAlphaRecord).collect(Collectors.toList());
        context.insertInto(ALPHA, ALPHA.ID, ALPHA.NAME, ALPHA.ACTIVE_STATUS, ALPHA.CREATED_TS)
                .valuesOfRecords(records)
                .onDuplicateKeyIgnore()
                .execute();
    }

    private AlphaRecord covertToAlphaRecord(AlphaEvent event) {
        return new AlphaRecord(
                event.getId(),
                event.getName(),
                event.getType().name(),
                event.getCreatedTs()
        );
    }

    private AlphaEntity convertToAlphaEntity(AlphaRecord record) {
        return new AlphaEntity(
                record.getId(),
                record.getName()
        );
    }


}
