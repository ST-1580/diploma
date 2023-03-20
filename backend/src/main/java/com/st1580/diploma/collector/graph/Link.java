package com.st1580.diploma.collector.graph;

import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;

public interface Link {
    LightEntity getFrom();

    LightEntity getTo();

    GraphLinkDto convertToDto();

    LightEntity getEntityWithType(EntityType type);
}
