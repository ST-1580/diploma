package com.st1580.diploma.collector.graph.entities;

import java.util.Map;
import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;
import com.st1580.diploma.db.tables.records.GammaRecord;

public class GammaEntity extends AbstractEntity {
    private final Long property_1;
    private final String property_2;
    private final Boolean property_3;

    public GammaEntity(long id, Long property_1, String property_2, Boolean property_3) {
        super(EntityType.GAMMA, id);
        this.property_1 = property_1;
        this.property_2 = property_2;
        this.property_3 = property_3;
    }

    public Long getProperty_1() {
        return property_1;
    }

    public String getProperty_2() {
        return property_2;
    }

    public Boolean getProperty_3() {
        return property_3;
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(
                GraphEntityType.DELTA,
                getId(),
                Map.of("property_1", property_1.toString(),
                        "property_2", property_2,
                        "property_3", property_3.toString())
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
        GammaEntity that = (GammaEntity) o;
        return Objects.equals(property_1, that.property_1) && Objects.equals(property_2, that.property_2) && Objects.equals(property_3, that.property_3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), property_1, property_2, property_3);
    }
}