package com.st1580.diploma.collector.service.dto.entites;

import java.util.Map;

import com.st1580.diploma.collector.graph.AlphaEntity;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;

public class AlphaEntityDto extends GraphEntityDto {

    public AlphaEntityDto(long id) {
        super(GraphEntityType.ALPHA, id);
    }

    @Override
    public Entity convertToModel() {
        return new AlphaEntity(getId());
    }
}
