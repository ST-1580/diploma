package com.st1580.diploma.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.st1580.diploma.collector.graph.links.GammaToAlphaLink;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.EntityEvent;
import com.st1580.diploma.updater.events.GammaEvent;
import com.st1580.diploma.updater.events.GammaToAlphaEvent;

public interface GammaToAlphaRepository extends LinkCollectorRepository {
    Map<Long, List<Long>> getConnectedGammaEntitiesIdsByAlphaIds(Collection<Long> alphaIds, long ts);

    Map<Long, List<Long>> getConnectedAlphaEntitiesIdsByGammaIds(Collection<Long> gammaIds, long ts);

    void batchInsertNewEvents(List<GammaToAlphaEvent> events);

    void addLinkEventsTriggeredByEntitiesUpdate(List<Set<GammaEvent>> gammaEvents, List<Set<AlphaEvent>> alphaEvents);

    List<EntityEvent> getUndefinedGammaStateInRange(long tsFrom, long tsTo);

    List<EntityEvent> getUndefinedAlphaStateInRange(long tsFrom, long tsTo);

    void deleteUndefinedLinks(long tsFrom, long tsTo);

    void batchUpdateLinksDependentOnGamma(Map<EntityEvent, Boolean> gammaActiveStatusByEvent);

    void batchUpdateLinksDependentOnAlpha(Map<EntityEvent, Boolean> alphaActiveStatusByEvent);
}
