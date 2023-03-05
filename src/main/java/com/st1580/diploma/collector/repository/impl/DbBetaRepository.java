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
import com.st1580.diploma.collector.repository.BetaRepository;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.db.tables.records.BetaRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.BETA;

@Repository
public class DbBetaRepository implements BetaRepository {
    @Autowired
    private DSLContext context;

    @Inject
    AlphaToBetaRepository alphaToBetaRepository;


    @Override
    public Map<Long, BetaEntity> collectAllEntitiesByIds(Collection<Long> ids) {
        return context
                .selectFrom(BETA)
                .where(BETA.ID.in(ids))
                .fetch()
                .stream()
                .map(this::convertToBetaEntity)
                .collect(Collectors.toMap(
                        BetaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, alphaToBetaRepository.getConnectedAlphaEntitiesIdsByBetaIds(ids));
        });

        return res;
    }

    @Override
    public Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, new HashMap<>(alphaToBetaRepository.getConnectedAlphaEntitiesByBetaIds(ids)));
        });

        return res;
    }

    private BetaEntity convertToBetaEntity(BetaRecord record) {
        return new BetaEntity(
                record.getId(),
                record.getProperty_1(),
                record.getProperty_2()
        );
    }
}
