package com.st1580.diploma.collector.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.st1580.diploma.collector.graph.links.GammaToDeltaLink;
import com.st1580.diploma.updater.events.DeltaEvent;
import com.st1580.diploma.updater.events.GammaEvent;
import com.st1580.diploma.updater.events.GammaToDeltaEvent;

public interface GammaToDeltaRepository {
    Map<Long, List<Long>> getConnectedGammaEntitiesIdsByDeltaIds(Collection<Long> deltaIds, long ts);

    Map<Long, List<GammaToDeltaLink>> getConnectedGammaEntitiesByDeltaIds(Collection<Long> deltaIds, long ts);

    Map<Long, List<Long>> getConnectedDeltaEntitiesIdsByGammaIds(Collection<Long> gammaIds, long ts);

    Map<Long, List<GammaToDeltaLink>> getConnectedDeltaEntitiesByGammaIds(Collection<Long> gammaIds, long ts);

    void batchInsertNewEvents(List<GammaToDeltaEvent> events);

    void addLinkEventsTriggeredByEntitiesUpdate(List<List<GammaEvent>> gammaEvents, List<List<DeltaEvent>> deltaEvents);
}
