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
import com.st1580.diploma.collector.graph.entities.AlphaEntity;
import com.st1580.diploma.collector.repository.AlphaRepository;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA;

@Repository
public class DbAlphaRepository implements AlphaRepository {

    DSLContext context = DSL.using(SQLDialect.POSTGRES);
    @Inject
    AlphaToBetaRepository alphaToBetaRepository;
    @Inject
    GammaToAlphaRepository gammaToAlphaRepository;

    @Override
    public Map<Long, AlphaEntity> collectAllEntitiesByIds(Collection<Long> ids) {
        return context
                .selectFrom(ALPHA)
                .where(ALPHA.ID.in(ids))
                .fetch()
                .stream()
                .map(AlphaEntity::new)
                .collect(Collectors.toMap(
                        AlphaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.BETA, alphaToBetaRepository.getConnectedBetaEntitiesIdsByAlphaIds(ids));
            res.put(EntityType.GAMMA, gammaToAlphaRepository.getConnectedGammaEntitiesIdsByAlphaIds(ids));
        });

        return res;
    }

    @Override
    public Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.BETA, new HashMap<>(alphaToBetaRepository.getConnectedBetaEntitiesByAlphaIds(ids)));
            res.put(EntityType.GAMMA, new HashMap<>(gammaToAlphaRepository.getConnectedGammaEntitiesByAlphaIds(ids)));
        });

        return res;
    }
}
