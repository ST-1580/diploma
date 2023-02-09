package com.st1580.diploma.collector.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.repository.AlphaRepository;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class DbAlphaRepository implements AlphaRepository {

    DSLContext context = DSL.using(SQLDialect.POSTGRES);
    @Inject
    AlphaToBetaRepository alphaToBetaRepository;
    @Inject
    GammaToAlphaRepository gammaToAlphaRepository;

    @Override
    public List<Entity> collectAllNeighbors(long id) {
        List<Entity> res = new ArrayList<>();

        context.transaction(ctx -> {
            res.addAll(alphaToBetaRepository.getConnectedBetaEntitiesByAlpha(id));
            res.addAll(gammaToAlphaRepository.getConnectedGammaEntitiesByAlpha(id));
        });

        return res;
    }
}
