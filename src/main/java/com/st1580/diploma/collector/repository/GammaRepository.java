package com.st1580.diploma.collector.repository;

import java.util.List;
import java.util.Set;

import com.st1580.diploma.updater.events.GammaEvent;

public interface GammaRepository extends CollectorRepository {
    void batchInsertNewEvents(List<GammaEvent> events);

    List<Set<GammaEvent>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo);

    void correctDependentLinks(long tsFrom ,long tsTo);
}
