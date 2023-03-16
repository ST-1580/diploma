package com.st1580.diploma.collector.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.st1580.diploma.collector.graph.links.AlphaToBetaLink;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.AlphaToBetaEvent;
import com.st1580.diploma.updater.events.BetaEvent;
import com.st1580.diploma.updater.events.EntityEvent;

public interface AlphaToBetaRepository {
    Map<Long, List<Long>> getConnectedBetaEntitiesIdsByAlphaIds(Collection<Long> alphaIds, long ts);

    Map<Long, List<Long>> getConnectedAlphaEntitiesIdsByBetaIds(Collection<Long> betaIds, long ts);

    Map<Long, List<AlphaToBetaLink>> getConnectedBetaEntitiesByAlphaIds(Collection<Long> alphaIds, long ts);

    Map<Long, List<AlphaToBetaLink>> getConnectedAlphaEntitiesByBetaIds(Collection<Long> betaIds, long ts);

    void batchInsertNewEvents(List<AlphaToBetaEvent> events);

    void addLinkEventsTriggeredByEntitiesUpdate(List<Set<AlphaEvent>> alphaEvents, List<Set<BetaEvent>> betaEvents);

    List<EntityEvent> getUndefinedAlphaStateInRange(long tsFrom, long tsTo);

    List<EntityEvent> getUndefinedBetaStateInRange(long tsFrom, long tsTo);

    void batchUpdateLinksDependentOnAlpha(Map<EntityEvent, Boolean> alphaActiveStatusByEvent);

    void batchUpdateLinksDependentOnBeta(Map<EntityEvent, Boolean> betaActiveStatusByEvent);
}
