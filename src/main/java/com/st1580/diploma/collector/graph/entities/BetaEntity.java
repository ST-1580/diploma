package com.st1580.diploma.collector.graph.entities;

import java.util.Map;
import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;
import com.st1580.diploma.db.tables.records.BetaRecord;

public class BetaEntity extends AbstractEntity {
    private final Long property_1;
    private final Boolean property_2;

    public BetaEntity(long id, Long property_1, Boolean property_2) {
        super(EntityType.BETA, id);
        this.property_1 = property_1;
        this.property_2 = property_2;
    }

    public BetaEntity(BetaRecord record) {
        super(EntityType.BETA, record.getId());
        this.property_1 = record.getProperty_1();
        this.property_2 = record.getProperty_2();
    }

    public Long getProperty_1() {
        return property_1;
    }

    public Boolean getProperty_2() {
        return property_2;
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(
                GraphEntityType.BETA,
                getId(),
                Map.of("property_1", property_1.toString(),
                        "property_2", property_2.toString())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BetaEntity that = (BetaEntity) o;
        return Objects.equals(property_1, that.property_1) && Objects.equals(property_2, that.property_2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), property_1, property_2);
    }
}
