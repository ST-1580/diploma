package com.st1580.diploma.collector.service;

import java.util.List;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.repository.CollectorRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;

public abstract class AbstractCollectorService {
    private final CollectorRepository collectorRepository;

    public AbstractCollectorService(CollectorRepository collectorRepository) {
        this.collectorRepository = collectorRepository;
    }

    public GraphDto getAllWithPolicy(GraphEntityDto startEntity) {
        GraphDto g = new GraphDto(startEntity);

        return g;
    }

    public List<GraphLinkDto> getEntityNeighbors(GraphEntityDto startEntity) {
        List<Entity> neighbors = collectorRepository.collectAllNeighbors(startEntity.getId());
        List<GraphEntityDto> connectedEntities =
                neighbors.stream().map(Entity::convertToDto).collect(Collectors.toList());

        return createLinks(startEntity, connectedEntities);
    }

    private List<GraphLinkDto> createLinks(GraphEntityDto centralEntity, List<GraphEntityDto> connectedEntities) {
        return connectedEntities.stream().map(entity -> new GraphLinkDto(centralEntity, entity)).collect(Collectors.toList());
    }
}
