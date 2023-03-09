package com.st1580.diploma.collector.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.st1580.diploma.collector.graph.links.AlphaToBetaLink;

public interface AlphaToBetaRepository {
    Map<Long, List<Long>> getConnectedBetaEntitiesIdsByAlphaIds(Collection<Long> alphaIds, long ts);

    Map<Long, List<AlphaToBetaLink>> getConnectedBetaEntitiesByAlphaIds(Collection<Long> alphaIds, long ts);

    Map<Long, List<Long>> getConnectedAlphaEntitiesIdsByBetaIds(Collection<Long> betaIds, long ts);

    Map<Long, List<AlphaToBetaLink>> getConnectedAlphaEntitiesByBetaIds(Collection<Long> betaIds, long ts);
}
