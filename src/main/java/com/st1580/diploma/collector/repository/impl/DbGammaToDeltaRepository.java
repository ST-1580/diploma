package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.links.GammaToDeltaLink;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA_TO_DELTA;

@Repository
public class DbGammaToDeltaRepository implements GammaToDeltaRepository {
    DSLContext context = DSL.using(SQLDialect.POSTGRES);

    @Override
    public Map<Long, List<Long>> getConnectedGammaEntitiesIdsByDeltaIds(Collection<Long> deltaIds) {
        return context
                .selectFrom(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.DELTA_ID.in(deltaIds))
                .fetchGroups(GAMMA_TO_DELTA.DELTA_ID, GAMMA_TO_DELTA.GAMMA_ID);
    }

    @Override
    public Map<Long, List<GammaToDeltaLink>> getConnectedGammaEntitiesByDeltaIds(Collection<Long> deltaIds) {
        return context
                .selectFrom(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.DELTA_ID.in(deltaIds))
                .fetch()
                .stream()
                .map(GammaToDeltaLink::new)
                .collect(Collectors.groupingBy(GammaToDeltaLink::getDeltaId));
    }

    @Override
    public Map<Long, List<Long>> getConnectedDeltaEntitiesIdsByGammaIds(Collection<Long> gammaIds) {
        return context
                .selectFrom(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.GAMMA_ID.in(gammaIds))
                .fetchGroups(GAMMA_TO_DELTA.GAMMA_ID, GAMMA_TO_DELTA.DELTA_ID);
    }

    @Override
    public Map<Long, List<GammaToDeltaLink>> getConnectedDeltaEntitiesByGammaIds(Collection<Long> gammaIds) {
        return context
                .selectFrom(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.GAMMA_ID.in(gammaIds))
                .fetch()
                .stream()
                .map(GammaToDeltaLink::new)
                .collect(Collectors.groupingBy(GammaToDeltaLink::getGammaId));
    }
}
