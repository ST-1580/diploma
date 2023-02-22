package com.st1580.diploma.collector.repository;

import java.util.List;
import java.util.Map;

import com.st1580.diploma.collector.graph.EntityType;

public interface CollectorRepository {
    Map<EntityType, List<Long>> collectAllNeighbors(long id);
}
