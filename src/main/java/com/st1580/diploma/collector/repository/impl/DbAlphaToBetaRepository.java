package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.links.AlphaToBetaLink;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA_TO_BETA;

@Repository
public class DbAlphaToBetaRepository implements AlphaToBetaRepository {
    @Autowired
    private DSLContext context;

    @Override
    public Map<Long, List<Long>> getConnectedBetaEntitiesIdsByAlphaIds(Collection<Long> alphaIds) {
        return context
                .selectFrom(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.ALPHA_ID.in(alphaIds))
                .fetchGroups(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID);
    }

    @Override
    public Map<Long, List<AlphaToBetaLink>> getConnectedBetaEntitiesByAlphaIds(Collection<Long> alphaIds) {
        return context
                .selectFrom(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.ALPHA_ID.in(alphaIds))
                .fetch()
                .stream()
                .map(AlphaToBetaLink::new)
                .collect(Collectors.groupingBy(AlphaToBetaLink::getAlphaId));
    }

    @Override
    public Map<Long, List<Long>> getConnectedAlphaEntitiesIdsByBetaIds(Collection<Long> betaIds) {
        return context
                .selectFrom(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.BETA_ID.in(betaIds))
                .fetchGroups(ALPHA_TO_BETA.BETA_ID, ALPHA_TO_BETA.ALPHA_ID);
    }

    @Override
    public Map<Long, List<AlphaToBetaLink>> getConnectedAlphaEntitiesByBetaIds(Collection<Long> betaIds) {
        return context
                .selectFrom(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.BETA_ID.in(betaIds))
                .fetch()
                .stream()
                .map(AlphaToBetaLink::new)
                .collect(Collectors.groupingBy(AlphaToBetaLink::getBetaId));
    }
}
