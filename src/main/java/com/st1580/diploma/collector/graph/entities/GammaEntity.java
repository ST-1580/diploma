package com.st1580.diploma.collector.graph.entities;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;

public class GammaEntity extends AbstractEntity {

    public GammaEntity(long id) {
        super(EntityType.GAMMA, id);
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(GraphEntityType.DELTA, getId(), null);
    }
}