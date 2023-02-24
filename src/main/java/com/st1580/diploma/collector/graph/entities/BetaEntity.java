package com.st1580.diploma.collector.graph.entities;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;
import com.st1580.diploma.db.tables.records.BetaRecord;

public class BetaEntity extends AbstractEntity {

    public BetaEntity(long id) {
        super(EntityType.BETA, id);
    }

    public BetaEntity(BetaRecord record) {
        super(EntityType.BETA, record.getId());
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(GraphEntityType.BETA, getId(), null);
    }
}
