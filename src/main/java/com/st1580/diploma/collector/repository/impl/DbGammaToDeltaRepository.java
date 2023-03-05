package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.links.GammaToDeltaLink;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import com.st1580.diploma.db.tables.records.GammaToDeltaRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA_TO_DELTA;

@Repository
public class DbGammaToDeltaRepository implements GammaToDeltaRepository {
    @Autowired
    private DSLContext context;

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
                .map(this::convertToLink)
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
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(GammaToDeltaLink::getGammaId));
    }

    private GammaToDeltaLink convertToLink(GammaToDeltaRecord record) {
        return new GammaToDeltaLink(
                record.getGammaId(),
                record.getDeltaId()
        );
    }
}
