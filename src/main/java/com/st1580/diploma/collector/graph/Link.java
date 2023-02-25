package com.st1580.diploma.collector.graph;

import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;

public interface Link {
    GraphLinkDto convertToDto();
    LightEntity getFrom();
    LightEntity getTo();
    LightEntity getEntityWithType(EntityType type);
}
