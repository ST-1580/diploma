package com.st1580.diploma.collector.graph;

import java.util.HashMap;

import com.st1580.diploma.collector.service.dto.entites.AlphaEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;

public class AlphaEntity extends Entity {

    public AlphaEntity(long id) {
        super(EntityType.ALPHA, id);
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new AlphaEntityDto(getId());
    }
}
