package com.st1580.diploma.collector.repository;


import java.util.List;

import com.st1580.diploma.updater.events.AlphaEvent;

public interface AlphaRepository extends CollectorRepository {
    void batchInsertNewEvents(List<AlphaEvent> events);

    // in one batch unique alphaIds
    List<List<AlphaEvent>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo);

    void correctDependentLinks(long tsFrom ,long tsTo);
}
