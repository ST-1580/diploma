package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.links.GammaToAlphaLink;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA_TO_ALPHA;

@Repository
public class DbGammaToAlphaRepository implements GammaToAlphaRepository {
    @Autowired
    private DSLContext context;

    @Override
    public Map<Long, List<Long>> getConnectedGammaEntitiesIdsByAlphaIds(Collection<Long> alphaIds) {
        return context
                .selectFrom(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.ALPHA_ID.in(alphaIds))
                .fetchGroups(GAMMA_TO_ALPHA.ALPHA_ID, GAMMA_TO_ALPHA.GAMMA_ID);
    }

    @Override
    public Map<Long, List<GammaToAlphaLink>> getConnectedGammaEntitiesByAlphaIds(Collection<Long> alphaIds) {
        return context
                .selectFrom(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.ALPHA_ID.in(alphaIds))
                .fetch()
                .stream()
                .map(GammaToAlphaLink::new)
                .collect(Collectors.groupingBy(GammaToAlphaLink::getAlphaId));
    }

    @Override
    public Map<Long, List<Long>> getConnectedAlphaEntitiesIdsByGammaIds(Collection<Long> gammaIds) {
        return context
                .selectFrom(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.GAMMA_ID.in(gammaIds))
                .fetchGroups(GAMMA_TO_ALPHA.GAMMA_ID, GAMMA_TO_ALPHA.ALPHA_ID);
    }

    @Override
    public Map<Long, List<GammaToAlphaLink>> getConnectedAlphaEntitiesByGammaIds(Collection<Long> gammaIds) {
        return context
                .selectFrom(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.GAMMA_ID.in(gammaIds))
                .fetch()
                .stream()
                .map(GammaToAlphaLink::new)
                .collect(Collectors.groupingBy(GammaToAlphaLink::getGammaId));
    }
}
