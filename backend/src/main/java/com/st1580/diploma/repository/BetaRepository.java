package com.st1580.diploma.repository;

import java.util.List;
import java.util.Set;

import com.st1580.diploma.updater.events.BetaEvent;

public interface BetaRepository extends EntityCollectorRepository {
    void batchInsertNewEvents(List<BetaEvent> events);

    List<Set<BetaEvent>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo);

    void correctDependentLinks(long tsFrom ,long tsTo);
}
