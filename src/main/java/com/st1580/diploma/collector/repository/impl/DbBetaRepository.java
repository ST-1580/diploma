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
import com.st1580.diploma.collector.graph.entities.BetaEntity;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.collector.repository.BetaRepository;
import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.db.tables.Beta;
import com.st1580.diploma.db.tables.records.BetaRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.BETA;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.row;

@Repository
public class DbBetaRepository implements BetaRepository {
    @Autowired
    private DSLContext context;
    @Inject
    AlphaToBetaRepository alphaToBetaRepository;

    @Override
    public Map<Long, BetaEntity> collectAllEntitiesByIds(Collection<Long> ids, long ts) {
        Beta TOP_LVL_BETA = BETA.as("top_lvl");
        Beta LOW_LVL_BETA = BETA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_BETA)
                .whereExists(
                        context.select(LOW_LVL_BETA.ID, max(LOW_LVL_BETA.CREATED_TS))
                                .from(LOW_LVL_BETA)
                                .where(LOW_LVL_BETA.ID.in(ids)
                                        .and(LOW_LVL_BETA.IS_ACTIVE.in(EntityActiveType.trueEntityActiveTypes))
                                        .and(LOW_LVL_BETA.CREATED_TS.lessOrEqual(ts)))
                                .groupBy(LOW_LVL_BETA.ID)
                                .having(LOW_LVL_BETA.ID.eq(TOP_LVL_BETA.ID)
                                        .and(max(LOW_LVL_BETA.CREATED_TS).eq(TOP_LVL_BETA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(this::convertToBetaEntity)
                .collect(Collectors.toMap(
                        BetaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids, long ts) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, alphaToBetaRepository.getConnectedAlphaEntitiesIdsByBetaIds(ids, ts));
        });

        return res;
    }

    @Override
    public Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids, long ts) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, new HashMap<>(alphaToBetaRepository.getConnectedAlphaEntitiesByBetaIds(ids, ts)));
        });

        return res;
    }

    private BetaEntity convertToBetaEntity(BetaRecord record) {
        return new BetaEntity(
                record.getId(),
                record.getEpoch()
        );
    }
}
