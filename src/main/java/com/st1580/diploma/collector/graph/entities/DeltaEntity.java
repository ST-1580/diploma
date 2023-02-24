package com.st1580.diploma.collector.graph.entities;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;

public class DeltaEntity extends AbstractEntity {

    public DeltaEntity(long id) {
        super(EntityType.DELTA, id);
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(GraphEntityType.DELTA, getId(), null);
    }
}
