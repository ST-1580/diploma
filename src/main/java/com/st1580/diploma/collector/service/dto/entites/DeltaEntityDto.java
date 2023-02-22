package com.st1580.diploma.collector.service.dto.entites;

import java.util.Map;

import com.st1580.diploma.collector.graph.DeltaEntity;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;

public class DeltaEntityDto extends GraphEntityDto {

    public DeltaEntityDto(long id) {
        super(GraphEntityType.DELTA, id);
    }

    @Override
    public Entity convertToModel() {
        return new DeltaEntity(getId());
    }
}
