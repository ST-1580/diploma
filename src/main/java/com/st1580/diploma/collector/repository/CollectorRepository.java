package com.st1580.diploma.collector.repository;

import java.util.List;

import com.st1580.diploma.collector.graph.Entity;

public interface CollectorRepository {
    List<Entity> collectAllNeighbors(long id);
}
