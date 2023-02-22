package com.st1580.diploma.collector.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.collector.repository.BetaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class DbBetaRepository implements BetaRepository {
    DSLContext context = DSL.using(SQLDialect.POSTGRES);

    @Inject
    AlphaToBetaRepository alphaToBetaRepository;

    @Override
    public Map<EntityType, List<Long>> collectAllNeighbors(long id) {
        Map<EntityType, List<Long>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, alphaToBetaRepository.getConnectedAlphaEntitiesByBeta(id));
        });

        return res;
    }
}
