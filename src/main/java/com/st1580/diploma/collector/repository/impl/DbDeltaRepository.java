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
import com.st1580.diploma.db.tables.records.DeltaRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.DELTA;

@Repository
public class DbDeltaRepository implements DeltaRepository {
    @Autowired
    private DSLContext context;

    @Inject
    GammaToDeltaRepository gammaToDeltaRepository;

    @Override
    public Map<Long, DeltaEntity> collectAllEntitiesByIds(Collection<Long> ids) {
        return context
                .selectFrom(DELTA)
                .where(DELTA.ID.in(ids))
                .fetch()
                .stream()
                .map(this::convertToDeltaEntity)
                .collect(Collectors.toMap(
                        DeltaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.GAMMA, gammaToDeltaRepository.getConnectedGammaEntitiesIdsByDeltaIds(ids));
        });

        return res;
    }

    @Override
    public Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.GAMMA, new HashMap<>(gammaToDeltaRepository.getConnectedGammaEntitiesByDeltaIds(ids)));
        });

        return res;
    }

    private DeltaEntity convertToDeltaEntity(DeltaRecord record) {
        return new DeltaEntity(
                record.getId(),
                record.getProperty_1()
        );
    }
}
