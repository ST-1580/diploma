package com.st1580.diploma.collector.repository;

import java.util.List;

import com.st1580.diploma.updater.events.BetaEvent;

public interface BetaRepository extends CollectorRepository {
    void batchInsertNewEvents(List<BetaEvent> events);

    List<List<BetaEvent>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo);

    void correctDependentLinks(long tsFrom ,long tsTo);
}
