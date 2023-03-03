package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.entities.DeltaEntity;
import com.st1580.diploma.collector.graph.entities.GammaEntity;
import com.st1580.diploma.collector.repository.GammaRepository;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA;

@Repository
public class DbGammaRepository implements GammaRepository {
    @Autowired
    private DSLContext context;
    @Inject
    GammaToAlphaRepository gammaToAlphaRepository;
    @Inject
    GammaToDeltaRepository gammaToDeltaRepository;

    @Override
    public Map<Long, GammaEntity> collectAllEntitiesByIds(Collection<Long> ids) {
        return context
                .selectFrom(GAMMA)
                .where(GAMMA.ID.in(ids))
                .fetch()
                .stream()
                .map(GammaEntity::new)
                .collect(Collectors.toMap(
                        GammaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, gammaToAlphaRepository.getConnectedAlphaEntitiesIdsByGammaIds(ids));
            res.put(EntityType.DELTA, gammaToDeltaRepository.getConnectedDeltaEntitiesIdsByGammaIds(ids));
        });

        return res;
    }

    @Override
    public Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, new HashMap<>(gammaToAlphaRepository.getConnectedAlphaEntitiesByGammaIds(ids)));
            res.put(EntityType.DELTA, new HashMap<>(gammaToDeltaRepository.getConnectedDeltaEntitiesByGammaIds(ids)));
        });

        return res;
    }
}
