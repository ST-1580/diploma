package com.st1580.diploma.updater.repository;

import java.util.List;
import java.util.Set;

import com.st1580.diploma.updater.events.EntityEvent;

public interface EntityEventRepository<T extends EntityEvent> {
    void batchInsertNewEvents(List<T> events);

    List<Set<T>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo);

    void correctDependentLinks(long tsFrom, long tsTo);
}
