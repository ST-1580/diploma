package com.st1580.diploma.collector.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.repository.GammaRepository;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class DbGammaRepository implements GammaRepository {
    DSLContext context = DSL.using(SQLDialect.POSTGRES);
    @Inject
    GammaToAlphaRepository gammaToAlphaRepository;
    @Inject
    GammaToDeltaRepository gammaToDeltaRepository;

    @Override
    public List<Entity> collectAllNeighbors(long id) {
        List<Entity> res = new ArrayList<>();

        context.transaction(ctx -> {
            res.addAll(gammaToAlphaRepository.getConnectedAlphaEntitiesByGamma(id));
            res.addAll(gammaToDeltaRepository.getConnectedDeltaEntitiesByGamma(id));
        });

        return res;
    }
}
