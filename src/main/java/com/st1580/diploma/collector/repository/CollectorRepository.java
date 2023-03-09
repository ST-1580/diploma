package com.st1580.diploma.collector.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;

public interface CollectorRepository {
    Map<Long, ? extends Entity> collectAllEntitiesByIds(Collection<Long> ids, long ts);

    default Map<EntityType, List<Long>> collectAllNeighborsIds(long id, long ts) {
        Map<EntityType, Map<Long, List<Long>>> res = collectAllNeighborsIdsByEntities(List.of(id), ts);
        return res.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(id)));
    }

    Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids, long ts);

    default Map<EntityType, List<? extends Link>> collectAllNeighbors(long id, long ts) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = collectAllNeighborsByEntities(List.of(id), ts);
        return res.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(id)));
    }

    Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids, long ts);
}
