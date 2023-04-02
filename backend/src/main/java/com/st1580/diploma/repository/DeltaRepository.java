package com.st1580.diploma.repository;

import java.util.List;
import java.util.Set;

import com.st1580.diploma.updater.events.DeltaEvent;

public interface DeltaRepository extends EntityCollectorRepository {
    void batchInsertNewEvents(List<DeltaEvent> events);

    List<Set<DeltaEvent>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo);

    void correctDependentLinks(long tsFrom ,long tsTo);
}
