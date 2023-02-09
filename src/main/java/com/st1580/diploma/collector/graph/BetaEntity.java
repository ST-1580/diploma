package com.st1580.diploma.collector.graph;

import java.util.HashMap;

import com.st1580.diploma.collector.service.dto.entites.BetaEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;

public class BetaEntity extends Entity {

    public BetaEntity(long id) {
        super(EntityType.BETA, id);
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new BetaEntityDto(getId(), new HashMap<>());
    }
}
