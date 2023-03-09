package com.st1580.diploma.collector.service.dto;

import java.util.Set;

public class GraphDto {
    private final long constructedTs;
    private final Set<GraphEntityDto> graphEntities;
    private final Set<GraphLinkDto> graphLinks;

    public GraphDto(long constructedTs, Set<GraphEntityDto> graphEntities, Set<GraphLinkDto> graphLinkDtos) {
        this.constructedTs = constructedTs;
        this.graphEntities = graphEntities;
        this.graphLinks = graphLinkDtos;
    }

    public long getConstructedTs() {
        return constructedTs;
    }

    public Set<GraphEntityDto> getEntities() {
        return graphEntities;
    }

    public Set<GraphLinkDto> getLinks() {
        return graphLinks;
    }
}
