package com.st1580.diploma.collector.graph;

import java.util.HashMap;

import com.st1580.diploma.collector.service.dto.entites.DeltaEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;

public class DeltaEntity extends Entity {

    public DeltaEntity(long id) {
        super(EntityType.DELTA, id);
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new DeltaEntityDto(getId(), new HashMap<>());
    }
}
