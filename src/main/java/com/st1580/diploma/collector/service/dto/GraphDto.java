package com.st1580.diploma.collector.service.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.policy.Policy;
import jakarta.annotation.Nonnull;

public class GraphDto {
    private final Set<GraphEntityDto> graphEntities;

    private final Set<GraphLinkDto> graphLinks;

    public GraphDto(Set<GraphEntityDto> graphEntities, Set<GraphLinkDto> graphLinkDtos) {
        this.graphEntities = graphEntities;
        this.graphLinks = graphLinkDtos;
    }

    public GraphDto(GraphEntityDto graphEntityDto) {
        this.graphEntities = Set.of(graphEntityDto);
        this.graphLinks = new HashSet<>();
    }

    public Set<GraphEntityDto> getEntities() {
        return graphEntities;
    }

    public Set<GraphLinkDto> getLinks() {
        return graphLinks;
    }

    public boolean checkPolicy(Policy policy, GraphEntityDto graphEntityDto) {
        return policy.canExtendFromEntity(this, graphEntityDto);
    }
}
