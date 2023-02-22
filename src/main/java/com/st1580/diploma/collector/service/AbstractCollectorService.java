package com.st1580.diploma.collector.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.repository.CollectorRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.entites.AlphaEntityDto;
import com.st1580.diploma.collector.service.dto.entites.BetaEntityDto;
import com.st1580.diploma.collector.service.dto.entites.DeltaEntityDto;
import com.st1580.diploma.collector.service.dto.entites.GammaEntityDto;

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
        Map<EntityType, List<Long>> neighbors = collectorRepository.collectAllNeighbors(startEntity.getId());
        List<GraphEntityDto> connectedEntities =
                neighbors.entrySet()
                        .stream()
                        .flatMap(entry -> createBaseEntityByType(entry.getKey(), entry.getValue()).stream())
                        .collect(Collectors.toList());

        return createLinks(startEntity, connectedEntities);
    }

    private List<GraphEntityDto> createBaseEntityByType(EntityType type, List<Long> ids) {
        return ids.stream()
                .map(id -> {
                    switch (type) {
                        case ALPHA:
                            return new AlphaEntityDto(id);

                        case BETA:
                            return new BetaEntityDto(id);

                        case GAMMA:
                            return new GammaEntityDto(id);

                        case DELTA:
                            return new DeltaEntityDto(id);
                    }

                    throw new IllegalArgumentException("Wrong type parameter " + type);
                })
                .collect(Collectors.toList());
    }

    private List<GraphLinkDto> createLinks(GraphEntityDto centralEntity, List<GraphEntityDto> connectedEntities) {
        return connectedEntities.stream().map(entity -> new GraphLinkDto(centralEntity, entity)).collect(Collectors.toList());
    }
}
