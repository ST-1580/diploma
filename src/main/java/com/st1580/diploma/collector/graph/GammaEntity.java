package com.st1580.diploma.collector.graph;

import java.util.HashMap;

import com.st1580.diploma.collector.service.dto.entites.GammaEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;

public class GammaEntity extends Entity {

    public GammaEntity(long id) {
        super(EntityType.GAMMA, id);
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GammaEntityDto(getId(), new HashMap<>());
    }
}