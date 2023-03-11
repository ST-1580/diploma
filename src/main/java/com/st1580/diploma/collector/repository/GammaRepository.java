package com.st1580.diploma.collector.repository;

import java.util.List;

import com.st1580.diploma.collector.graph.entities.GammaEntity;
import com.st1580.diploma.updater.events.GammaEvent;

public interface GammaRepository extends CollectorRepository {
    void batchInsertNewEvents(List<GammaEvent> events);
}
