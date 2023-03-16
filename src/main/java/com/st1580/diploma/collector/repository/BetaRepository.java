package com.st1580.diploma.collector.repository;

import java.util.List;
import java.util.Set;

import com.st1580.diploma.updater.events.BetaEvent;

public interface BetaRepository extends CollectorRepository {
    void batchInsertNewEvents(List<BetaEvent> events);

    List<Set<BetaEvent>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo);

    void correctDependentLinks(long tsFrom ,long tsTo);
}
