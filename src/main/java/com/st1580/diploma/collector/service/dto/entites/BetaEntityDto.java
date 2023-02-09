package com.st1580.diploma.collector.service.dto.entites;

import java.util.Map;

import com.st1580.diploma.collector.graph.BetaEntity;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;

public class BetaEntityDto extends GraphEntityDto {
    public BetaEntityDto(long id, Map<String, String> payload) {
        super(GraphEntityType.BETA, id, payload);
    }

    @Override
    public Entity convertToModel() {
        return new BetaEntity(getId());
    }
}
