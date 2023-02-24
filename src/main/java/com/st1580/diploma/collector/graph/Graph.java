package com.st1580.diploma.collector.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.graph.links.LightLink;
import com.st1580.diploma.collector.policy.Policy;
import com.st1580.diploma.collector.policy.NonCyclicPolicy;
import com.st1580.diploma.collector.service.dto.GraphDto;

public class Graph {
    private final Set<Entity> graphEntities;

    private final Set<Link> graphLinks;

    private final Policy policy;

    public Graph() {
        this.graphEntities = new HashSet<>();
        this.graphLinks = new HashSet<>();
        this.policy = new NonCyclicPolicy();
    }

    public Graph(Policy policy) {
        this.graphEntities = new HashSet<>();
        this.graphLinks = new HashSet<>();
        this.policy = policy;
    }

    public Graph(Set<Entity> graphEntities, Set<Link> graphLinks) {
        this.graphEntities = graphEntities;
        this.graphLinks = graphLinks;
        this.policy = new NonCyclicPolicy();
    }

    public Graph(Set<Entity> graphEntities, Set<Link> graphLinks, Policy policy) {
        this.graphEntities = graphEntities;
        this.graphLinks = graphLinks;
        this.policy = policy;
    }


    public Set<Entity> getGraphEntities() {
        return graphEntities;
    }

    public Set<Link> getGraphLinks() {
        return graphLinks;
    }

    public Policy getPolicy() {
        return policy;
    }

    public GraphDto convertToDto() {
        return new GraphDto(
                graphEntities.stream().map(Entity::convertToDto).collect(Collectors.toSet()),
                graphLinks.stream().map(Link::convertToDto).collect(Collectors.toSet())
        );
    }

    public void addNeighbors(EntityType startEntityType,
                             long startEntityId,
                             Map<EntityType, List<? extends Link>> neighborsLinks) {
        LightEntity startEntity = new LightEntity(startEntityType, startEntityId);
        addNeighbors(startEntity, neighborsLinks);
    }

    public void addNeighbors(LightEntity start, Map<EntityType, List<? extends Link>> neighborsLinks) {
        List<LightEntity> connectedEntities = neighborsLinks.entrySet()
                .stream()
                .flatMap(entry -> createLightEntityFromLinks(entry.getKey(), entry.getValue()).stream())
                .collect(Collectors.toList());

        Set<Link> newLinks = neighborsLinks.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        this.graphEntities.addAll(connectedEntities);
        this.graphLinks.addAll(newLinks);
    }

    private List<LightEntity> createLightEntityFromLinks(EntityType toType, List<? extends Link> links) {
        return links.stream()
                .map(link -> new LightEntity(toType, link.getEntityWithType(toType).getId()))
                .collect(Collectors.toList());
    }

    public void addLightNeighbors(EntityType startEntityType,
                                  long startEntityId,
                                  Map<EntityType, List<Long>> neighbors) {
        LightEntity createdEntity = new LightEntity(startEntityType, startEntityId);
        addLightNeighbors(createdEntity, neighbors);
    }

    public void addLightNeighbors(LightEntity start, Map<EntityType, List<Long>> neighbors) {
        List<LightEntity> connectedEntities = neighbors.entrySet()
                .stream()
                .flatMap(entry -> createLightEntities(entry.getKey(), entry.getValue()).stream())
                .collect(Collectors.toList());

        this.graphEntities.addAll(connectedEntities);
        this.graphLinks.addAll(createLightLinks(start, connectedEntities));
    }

    private List<LightEntity> createLightEntities(EntityType type, List<Long> ids) {
        return ids.stream()
                .map(id -> new LightEntity(type, id))
                .collect(Collectors.toList());
    }

    private List<LightLink> createLightLinks(LightEntity centralEntity, List<LightEntity> connectedEntities) {
        return connectedEntities.stream().map(entity -> new LightLink(centralEntity, entity)).collect(Collectors.toList());
    }

    public boolean canExtendFromEntityByPolicy(EntityType type, long id) {
        return policy.canExtendFromLightEntity(this, new LightEntity(type, id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Graph graph = (Graph) o;
        return Objects.equals(graphEntities, graph.graphEntities) && Objects.equals(graphLinks, graph.graphLinks) && Objects.equals(policy, graph.policy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graphEntities, graphLinks, policy);
    }
}
