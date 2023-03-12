package com.st1580.diploma.collector.repository;

import java.util.List;

import com.st1580.diploma.collector.graph.entities.DeltaEntity;
import com.st1580.diploma.updater.events.DeltaEvent;

public interface DeltaRepository extends CollectorRepository {
    void batchInsertNewEvents(List<DeltaEvent> events);
}
