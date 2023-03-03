package com.st1580.diploma.collector.service.dto;

import java.util.HashSet;
import java.util.Set;

public class GraphDto {
    private final Set<GraphEntityDto> graphEntities;

    private final Set<GraphLinkDto> graphLinks;

    public GraphDto(Set<GraphEntityDto> graphEntities, Set<GraphLinkDto> graphLinkDtos) {
        this.graphEntities = graphEntities;
        this.graphLinks = graphLinkDtos;
    }

    public Set<GraphEntityDto> getEntities() {
        return graphEntities;
    }

    public Set<GraphLinkDto> getLinks() {
        return graphLinks;
    }
}
