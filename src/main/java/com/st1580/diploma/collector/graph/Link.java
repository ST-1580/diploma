package com.st1580.diploma.collector.graph;

import com.st1580.diploma.collector.service.dto.GraphLinkDto;

public interface Link {
    GraphLinkDto convertToDto();
    Entity getFrom();
    Entity getTo();
    Entity getEntityWithType(EntityType type);
}
