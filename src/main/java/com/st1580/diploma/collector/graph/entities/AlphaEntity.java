package com.st1580.diploma.collector.graph.entities;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;

public class AlphaEntity extends AbstractEntity {

    public AlphaEntity(long id) {
        super(EntityType.ALPHA, id);
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(GraphEntityType.ALPHA, getId(), null);
    }
}
