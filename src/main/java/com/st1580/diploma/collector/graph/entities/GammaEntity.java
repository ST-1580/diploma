package com.st1580.diploma.collector.graph.entities;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;
import com.st1580.diploma.db.tables.records.GammaRecord;

public class GammaEntity extends AbstractEntity {

    public GammaEntity(long id) {
        super(EntityType.GAMMA, id);
    }

    public GammaEntity(GammaRecord record) {
        super(EntityType.GAMMA, record.getId());
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(GraphEntityType.DELTA, getId(), null);
    }
}