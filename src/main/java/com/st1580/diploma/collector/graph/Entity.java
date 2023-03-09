package com.st1580.diploma.collector.graph;

import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;

public interface Entity {
    EntityType getType();

    long getId();

    LightEntity convertToLight();

    GraphEntityDto convertToDto();
}
