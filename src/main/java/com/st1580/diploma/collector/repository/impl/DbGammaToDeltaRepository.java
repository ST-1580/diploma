package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.st1580.diploma.collector.graph.links.GammaToDeltaLink;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class DbGammaToDeltaRepository implements GammaToDeltaRepository {
    DSLContext context = DSL.using(SQLDialect.POSTGRES);

    @Override
    public Map<Long, List<Long>> getConnectedGammaEntitiesIdsByDeltaIds(Collection<Long> deltaIds) {
        return null;
    }

    @Override
    public Map<Long, List<GammaToDeltaLink>> getConnectedGammaEntitiesByDeltaIds(Collection<Long> deltaIds) {
        return null;
    }

    @Override
    public Map<Long, List<Long>> getConnectedDeltaEntitiesIdsByGammaIds(Collection<Long> gammaIds) {
        return null;
    }

    @Override
    public Map<Long, List<GammaToDeltaLink>> getConnectedDeltaEntitiesByGammaIds(Collection<Long> gammaIds) {
        return null;
    }
}
