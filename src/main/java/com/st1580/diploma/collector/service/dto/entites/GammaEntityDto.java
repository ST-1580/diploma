package com.st1580.diploma.collector.service.dto.entites;

import java.util.Map;

import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.GammaEntity;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;

public class GammaEntityDto extends GraphEntityDto {

    public GammaEntityDto(long id) {
        super(GraphEntityType.GAMMA, id);
    }

    @Override
    public Entity convertToModel() {
        return new GammaEntity(getId());
    }
}
