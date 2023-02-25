package com.st1580.diploma.collector.service;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.BfsStage;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Graph;
import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.policy.Policy;
import com.st1580.diploma.collector.repository.CollectorRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;

public abstract class AbstractCollectorService {
    private final CollectorRepository collectorRepository;

    @Inject
    private GraphConstructorService graphConstructorService;

    public AbstractCollectorService(CollectorRepository collectorRepository) {
        this.collectorRepository = collectorRepository;
    }

    private Graph constructGraph(Entity startEntity,
                                 Policy policy,
                                 boolean isLightConstructor) {
        Graph graph = new Graph(policy);

        Queue<BfsStage> queue = new ArrayDeque<>();
        queue.add(new BfsStage(startEntity.getType(), Set.of(startEntity.getId())));

        while (!queue.isEmpty()) {
            BfsStage currStage = queue.poll();
            List<Long> notFinishEntitiesIds =
                    currStage.getEntitiesIds().stream()
                            .filter(id -> !graph.canExtendFromEntityByPolicy(currStage.getType(), id))
                            .collect(Collectors.toList());

            if (isLightConstructor) {
                Map<EntityType, Map<Long, List<Long>>> currStageNeighbors =
                        graphConstructorService.getEntitiesNeighborsIds(currStage.getType(), notFinishEntitiesIds);

                currStageNeighbors.forEach((nextStageType, linkedEntitiesIds) -> {
                    Set<Long> nextStageEntitiesIds = new HashSet<>();

                    for (long entityId : linkedEntitiesIds.keySet()) {
                        List<Long> neighbors = linkedEntitiesIds.get(entityId);

                        graph.addLightNeighbors(
                                currStage.getType(),
                                entityId,
                                Map.of(nextStageType, neighbors)
                        );

                        nextStageEntitiesIds.addAll(neighbors);
                    }

                    queue.add(new BfsStage(nextStageType, nextStageEntitiesIds));
                });
            } else {
                Map<EntityType, Map<Long, List<? extends Link>>> currStageNeighbors =
                        graphConstructorService.getEntitiesNeighbors(currStage.getType(), notFinishEntitiesIds);

                currStageNeighbors.forEach((nextStageType, links) -> {
                    Set<Long> nextStageEntitiesIds = new HashSet<>();

                    for (long entityId : links.keySet()) {
                        List<? extends Link> neighbors = links.get(entityId);
                        graph.addNeighbors(
                                currStage.getType(),
                                entityId,
                                Map.of(nextStageType, neighbors)
                        );

                        List<Long> neighborsIds = neighbors
                                .stream()
                                .map(link -> link.getEntityWithType(nextStageType).getId())
                                .collect(Collectors.toList());
                        nextStageEntitiesIds.addAll(neighborsIds);
                    }

                    queue.add(new BfsStage(nextStageType, nextStageEntitiesIds));
                });
            }
        }

        return graph;
    }

    public GraphDto getLightGraphWithPolicy(Entity startEntity, Policy policy) {
        Graph g = constructGraph(startEntity, policy, true);
        return g.convertToDto();
    }

    public GraphDto getGraphWithPolicy(Entity startEntity, Policy policy) {
        Graph g = constructGraph(startEntity, policy, false);

        Map<EntityType, Set<Long>> lightEntities = g.getGraphEntities()
                .stream()
                .collect(Collectors.groupingBy(
                        Entity::getType,
                        Collectors.mapping(Entity::getId, Collectors.toSet()))
                );

        Set<Entity> heavyEntities = new HashSet<>();
        for (EntityType type : lightEntities.keySet()) {
            Map<Long, ? extends Entity> heavyEntityById =
                    graphConstructorService.getEntitiesByIds(type, lightEntities.get(type));
            heavyEntities.addAll(heavyEntityById.values());
        }

        Graph heavyGraph = new Graph(heavyEntities, g.getGraphLinks());
        return heavyGraph.convertToDto();
    }

    public List<GraphLinkDto> getEntityNeighbors(Entity startEntity) {
        Graph g = new Graph();
        Map<EntityType, List<? extends Link>> neighbors = collectorRepository.collectAllNeighbors(startEntity.getId());
        g.addNeighbors(startEntity.convertToLight(), neighbors);

        return g.getGraphLinks().stream().map(Link::convertToDto).collect(Collectors.toList());
    }


    public List<GraphLinkDto> getEntityLightNeighbors(Entity startEntity) {
        Graph g = new Graph();
        Map<EntityType, List<Long>> neighbors = collectorRepository.collectAllNeighborsIds(startEntity.getId());
        g.addLightNeighbors(startEntity.convertToLight(), neighbors);

        return g.getGraphLinks().stream().map(Link::convertToDto).collect(Collectors.toList());
    }

}
