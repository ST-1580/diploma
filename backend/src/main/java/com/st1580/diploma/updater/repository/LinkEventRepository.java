package com.st1580.diploma.updater.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.st1580.diploma.updater.events.EntityEvent;
import com.st1580.diploma.updater.events.EntityEventIdAndTs;
import com.st1580.diploma.updater.events.LinkEvent;

public interface LinkEventRepository<
        LINK_EVENT extends LinkEvent,
        FROM_EVENT extends EntityEvent,
        TO_EVENT extends EntityEvent> {
    void batchInsertNewEvents(List<LINK_EVENT> events);

    void addLinkEventsTriggeredByEntitiesUpdate(List<Set<FROM_EVENT>> fromEvents, List<Set<TO_EVENT>> toEvents);

    List<EntityEventIdAndTs> getUndefinedFromStateInRange(long tsFrom, long tsTo);

    List<EntityEventIdAndTs> getUndefinedToStateInRange(long tsFrom, long tsTo);

    void deleteUndefinedLinks(long tsFrom, long tsTo);

    void batchUpdateLinksDependentOnFromEntity(Map<EntityEventIdAndTs, Boolean> fromActiveStatusByEvent);

    void batchUpdateLinksDependentOnToEntity(Map<EntityEventIdAndTs, Boolean> toActiveStatusByEvent);
}
