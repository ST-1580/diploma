package com.st1580.diploma.repository;


import java.util.List;
import java.util.Set;

import com.st1580.diploma.updater.events.AlphaEvent;

public interface AlphaRepository extends CollectorRepository {
    void batchInsertNewEvents(List<AlphaEvent> events);

    List<Set<AlphaEvent>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo);

    void correctDependentLinks(long tsFrom ,long tsTo);
}
