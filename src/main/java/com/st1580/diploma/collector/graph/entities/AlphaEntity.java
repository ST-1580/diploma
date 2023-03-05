package com.st1580.diploma.collector.graph.entities;

import java.util.Map;
import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;
import com.st1580.diploma.external.alpha.data.ExternalAlphaEntity;

public class AlphaEntity extends AbstractEntity {
    private final String property_1;
    private final Long property_2;
    private final Long property_3;

    public AlphaEntity(long id, String property_1, Long property_2, Long property_3) {
        super(EntityType.ALPHA, id);
        this.property_1 = property_1;
        this.property_2 = property_2;
        this.property_3 = property_3;
    }

    public AlphaEntity(ExternalAlphaEntity externalAlphaEntity) {
        super(EntityType.ALPHA, externalAlphaEntity.getId());
        this.property_1 = externalAlphaEntity.getName();
        this.property_2 = externalAlphaEntity.getCreatedTs();
        this.property_3 = externalAlphaEntity.getStat();
    }

    public String getProperty_1() {
        return property_1;
    }

    public Long getProperty_2() {
        return property_2;
    }

    public Long getProperty_3() {
        return property_3;
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(
                GraphEntityType.ALPHA,
                getId(),
                Map.of("property_1", property_1,
                        "property_2", property_2.toString(),
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
        AlphaEntity that = (AlphaEntity) o;
        return Objects.equals(property_1, that.property_1) && Objects.equals(property_2, that.property_2) && Objects.equals(property_3, that.property_3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), property_1, property_2, property_3);
    }
}
