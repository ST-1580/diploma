package com.st1580.diploma.collector.graph.entities;

import java.util.Map;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;
import com.st1580.diploma.db.tables.records.DeltaRecord;

public class DeltaEntity extends AbstractEntity {
    private final String property_1;

    public DeltaEntity(long id, String property_1) {
        super(EntityType.DELTA, id);
        this.property_1 = property_1;
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(GraphEntityType.DELTA, getId(), Map.of("property_1", property_1));
    }
}
