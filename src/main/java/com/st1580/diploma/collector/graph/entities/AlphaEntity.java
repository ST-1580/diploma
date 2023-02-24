package com.st1580.diploma.collector.graph.entities;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;
import com.st1580.diploma.db.tables.records.AlphaRecord;

public class AlphaEntity extends AbstractEntity {

    public AlphaEntity(long id) {
        super(EntityType.ALPHA, id);
    }

    public AlphaEntity(AlphaRecord record) {
        super(EntityType.ALPHA, record.getId());
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(GraphEntityType.ALPHA, getId(), null);
    }
}
