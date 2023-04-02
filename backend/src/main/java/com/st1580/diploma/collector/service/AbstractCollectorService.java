package com.st1580.diploma.collector.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
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
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.graph.links.LightLink;
import com.st1580.diploma.collector.graph.links.LinkType;
import com.st1580.diploma.collector.policy.NonCyclicPolicy;
import com.st1580.diploma.collector.policy.Policy;
import com.st1580.diploma.collector.policy.StartEntityPolicy;
import com.st1580.diploma.repository.EntityCollectorRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.PolicyType;
import com.st1580.diploma.repository.LastSyncRepository;

public abstract class AbstractCollectorService {
    private final EntityCollectorRepository collectorRepository;

    @Inject
    private GraphConstructorService graphConstructorService;

    @Inject
    private LastSyncRepository lastSyncRepository;

    public AbstractCollectorService(EntityCollectorRepository entityCollectorRepository) {
        this.collectorRepository = entityCollectorRepository;
    }

    public GraphDto getGraphByPolicy(Entity startEntity, long ts, PolicyType policyType,
                                     boolean isLinksLight, boolean isEntitiesLight) {
        long actualTs = lastSyncRepository.getCorrectorLastSync();
        long constructedTs = ts != -1 && ts < actualTs ? ts : actualTs;
        if (!isEntityExist(startEntity, constructedTs)) {
            return new GraphDto(constructedTs, new HashSet<>(), new HashSet<>());
        }

        Policy policy = createPolicy(policyType, startEntity);
        Graph g = constructGraph(startEntity, policy, constructedTs);
        Graph gWithCorrectLinks = isLinksLight ? g : constructGraphWithHeavyLinks(g, constructedTs);
        return isEntitiesLight ?
                gWithCorrectLinks.convertToDto(constructedTs) :
                constructHeavyGraph(gWithCorrectLinks, constructedTs).convertToDto(constructedTs);
    }

    private Graph constructGraph(Entity startEntity,
                                 Policy policy,
                                 long ts) {
        Graph graph = new Graph(policy);
        Queue<BfsStage> queue = new ArrayDeque<>();
        queue.add(new BfsStage(startEntity.getType(), Set.of(startEntity.getId())));

        while (!queue.isEmpty()) {
            BfsStage currStage = queue.poll();
            List<Long> notFinishEntitiesIds = new ArrayList<>();
            for (long id : currStage.getEntitiesIds()) {
                if (graph.canExtendFromEntityByPolicy(currStage.getType(), id)) {
                    notFinishEntitiesIds.add(id);
                } else {
                    graph.addEntity(new LightEntity(currStage.getType(), id));
                }
            }

            Map<EntityType, Map<Long, List<Long>>> currStageNeighbors =
                    graphConstructorService.getEntitiesNeighborsIds(currStage.getType(), notFinishEntitiesIds, ts);

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

                if (!nextStageEntitiesIds.isEmpty()) {
                    queue.add(new BfsStage(nextStageType, nextStageEntitiesIds));
                }
            });
        }

        return graph;
    }

    private Graph constructGraphWithHeavyLinks(Graph lightGraph, long ts) {
        Map<LinkType, Set<LightLink>> lightLinks = lightGraph.getGraphLinks()
                .stream()
                .collect(Collectors.groupingBy(
                        link -> new LinkType(link.getFrom().getType(), link.getTo().getType()),
                        Collectors.mapping(
                                link -> new LightLink(link.getFrom(), link.getTo()),
                                Collectors.toSet())
                        )
                );

        Set<Link> heavyLinks = new HashSet<>();
        for (LinkType type : lightLinks.keySet()) {
            Map<LightLink, ? extends Link> heavyLinkById =
                    graphConstructorService.getLinksByEnds(type, lightLinks.get(type), ts);
            heavyLinks.addAll(heavyLinkById.values());
        }

        return new Graph(lightGraph.getGraphEntities(), heavyLinks);
    }

    private Graph constructHeavyGraph(Graph lightGraph, long ts) {
        Map<EntityType, Set<Long>> lightEntities = lightGraph.getGraphEntities()
                .stream()
                .collect(Collectors.groupingBy(
                        Entity::getType,
                        Collectors.mapping(Entity::getId, Collectors.toSet()))
                );

        Set<Entity> heavyEntities = new HashSet<>();
        for (EntityType type : lightEntities.keySet()) {
            Map<Long, ? extends Entity> heavyEntityById =
                    graphConstructorService.getEntitiesByIds(type, lightEntities.get(type), ts);
            heavyEntities.addAll(heavyEntityById.values());
        }

        return new Graph(heavyEntities, lightGraph.getGraphLinks());
    }


    private Policy createPolicy(PolicyType policyType, Entity startEntity) {
        switch (policyType) {
            case START:
                return new StartEntityPolicy(startEntity.getType());

            case NON_CYCLIC:
                return new NonCyclicPolicy();
        }

        throw new IllegalArgumentException("Wrong policy type parameter " + policyType);
    }

    public List<GraphLinkDto> getEntityNeighbors(Entity startEntity, long ts, boolean isLinksLight) {
        long actualTs = lastSyncRepository.getCorrectorLastSync();
        long constructedTs = ts != -1 && ts < actualTs ? ts : actualTs;
        if (!isEntityExist(startEntity, constructedTs)) {
            return new ArrayList<>();
        }

        Graph g = new Graph();

        if (isLinksLight) {
            Map<EntityType, List<Long>> neighbors = collectorRepository.collectAllNeighborsIds(startEntity.getId(), constructedTs);
            g.addLightNeighbors(startEntity.convertToLight(), neighbors);
        } else {
            Map<EntityType, List<? extends Link>> neighbors =
                    collectorRepository.collectAllNeighbors(startEntity.getId(), constructedTs);
            g.addNeighbors(startEntity.convertToLight(), neighbors);
        }

        return g.getGraphLinks().stream().map(Link::convertToDto).collect(Collectors.toList());
    }

    private boolean isEntityExist(Entity entity, long ts) {
        return !collectorRepository.collectAllActiveEntitiesByIds(List.of(entity.getId()), ts).isEmpty();
    }

}
