package com.st1580.diploma.collector.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.graph.links.LightLink;
import com.st1580.diploma.collector.graph.links.LinkType;
import com.st1580.diploma.collector.policy.Policy;
import com.st1580.diploma.collector.policy.NonCyclicPolicy;
import com.st1580.diploma.collector.service.dto.GraphDto;

import static com.st1580.diploma.collector.graph.links.LinkType.possibleLinks;

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


    public Set<Entity> getGraphEntities() {
        return graphEntities;
    }

    public Set<Link> getGraphLinks() {
        return graphLinks;
    }

    public Policy getPolicy() {
        return policy;
    }

    public GraphDto convertToDto(long ts) {
        return new GraphDto(
                ts,
                graphEntities.stream().map(Entity::convertToDto).collect(Collectors.toSet()),
                graphLinks.stream().map(Link::convertToDto).collect(Collectors.toSet())
        );
    }

    public void addEntity(Entity entity) {
        graphEntities.add(entity);
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

        this.graphEntities.add(start);
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

        this.graphEntities.add(start);
        this.graphLinks.addAll(createLightLinks(start, connectedEntities));
    }

    private List<LightEntity> createLightEntities(EntityType type, List<Long> ids) {
        return ids.stream()
                .map(id -> new LightEntity(type, id))
                .collect(Collectors.toList());
    }

    private List<LightLink> createLightLinks(LightEntity centralEntity, List<LightEntity> connectedEntities) {
        if (connectedEntities.isEmpty()) {
            return new ArrayList<>();
        }

        LinkType normal = new LinkType(centralEntity.getType(), connectedEntities.get(0).getType());
        LinkType rev = new LinkType(connectedEntities.get(0).getType(), centralEntity.getType());
        boolean isRev = possibleLinks.contains(rev);
        if (!isRev && !possibleLinks.contains(normal)) {
            throw new IllegalArgumentException("Wrong types of link ends: " + normal.getTypeFrom() + ", " + normal.getTypeTo());
        }

        return connectedEntities.stream()
                .map(entity -> isRev ? new LightLink(entity, centralEntity) : new LightLink(centralEntity, entity))
                .collect(Collectors.toList());
    }



    public boolean canExtendFromEntityByPolicy(EntityType type, long id) {
        LightEntity entity = new LightEntity(type, id);
        return policy.canExtendFromLightEntity(this, entity) && !this.graphEntities.contains(entity);
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
