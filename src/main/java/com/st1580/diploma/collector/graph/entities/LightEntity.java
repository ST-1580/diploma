package com.st1580.diploma.collector.graph.entities;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;

public class LightEntity extends AbstractEntity {
    public LightEntity(EntityType type, long id) {
        super(type, id);
    }

    @Override
    public GraphEntityDto convertToDto() {
        switch (getType()) {
            case ALPHA:
                return new GraphEntityDto(GraphEntityType.ALPHA, getId(), null);

            case BETA:
                return new GraphEntityDto(GraphEntityType.BETA, getId(), null);

            case GAMMA:
                return new GraphEntityDto(GraphEntityType.GAMMA, getId(), null);

            case DELTA:
                return new GraphEntityDto(GraphEntityType.DELTA, getId(), null);
        }

        throw new IllegalArgumentException("Wrong type parameter " + getType());
    }
}
