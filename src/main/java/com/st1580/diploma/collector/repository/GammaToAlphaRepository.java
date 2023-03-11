package com.st1580.diploma.collector.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.st1580.diploma.collector.graph.links.GammaToAlphaLink;
import com.st1580.diploma.updater.events.GammaToAlphaEvent;

public interface GammaToAlphaRepository {
    Map<Long, List<Long>> getConnectedGammaEntitiesIdsByAlphaIds(Collection<Long> alphaIds, long ts);

    Map<Long, List<GammaToAlphaLink>> getConnectedGammaEntitiesByAlphaIds(Collection<Long> alphaIds, long ts);

    Map<Long, List<Long>> getConnectedAlphaEntitiesIdsByGammaIds(Collection<Long> gammaIds, long ts);

    Map<Long, List<GammaToAlphaLink>> getConnectedAlphaEntitiesByGammaIds(Collection<Long> gammaIds, long ts);

    void batchInsertNewEvents(List<GammaToAlphaEvent> events);
}
