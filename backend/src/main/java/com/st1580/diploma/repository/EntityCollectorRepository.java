package com.st1580.diploma.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;

public interface EntityCollectorRepository {
    Map<Long, ? extends Entity> collectAllActiveEntitiesByIds(Collection<Long> ids, long ts);

    Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids, long ts);
}
