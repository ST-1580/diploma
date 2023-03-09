package com.st1580.diploma.collector.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.st1580.diploma.collector.graph.links.GammaToDeltaLink;

public interface GammaToDeltaRepository {
    Map<Long, List<Long>> getConnectedGammaEntitiesIdsByDeltaIds(Collection<Long> deltaIds, long ts);

    Map<Long, List<GammaToDeltaLink>> getConnectedGammaEntitiesByDeltaIds(Collection<Long> deltaIds, long ts);

    Map<Long, List<Long>> getConnectedDeltaEntitiesIdsByGammaIds(Collection<Long> gammaIds, long ts);

    Map<Long, List<GammaToDeltaLink>> getConnectedDeltaEntitiesByGammaIds(Collection<Long> gammaIds, long ts);
}
