package com.st1580.diploma.collector.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.st1580.diploma.collector.graph.links.AlphaToBetaLink;
import com.st1580.diploma.updater.events.AlphaToBetaEvent;

public interface AlphaToBetaRepository {
    Map<Long, List<Long>> getConnectedBetaEntitiesIdsByAlphaIds(Collection<Long> alphaIds, long ts);

    Map<Long, List<AlphaToBetaLink>> getConnectedBetaEntitiesByAlphaIds(Collection<Long> alphaIds, long ts);

    Map<Long, List<Long>> getConnectedAlphaEntitiesIdsByBetaIds(Collection<Long> betaIds, long ts);

    Map<Long, List<AlphaToBetaLink>> getConnectedAlphaEntitiesByBetaIds(Collection<Long> betaIds, long ts);

    void batchInsertNewEvents(List<AlphaToBetaEvent> events);
}
