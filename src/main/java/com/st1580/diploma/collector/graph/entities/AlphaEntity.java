package com.st1580.diploma.collector.graph.entities;

import java.util.Map;
import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;
import com.st1580.diploma.external.alpha.data.ExternalAlphaEntity;

public class AlphaEntity extends AbstractEntity {
    private final String name;

    public AlphaEntity(long id, String name) {
        super(EntityType.ALPHA, id);
        this.name = name;
    }

    public AlphaEntity(ExternalAlphaEntity externalAlphaEntity) {
        super(EntityType.ALPHA, externalAlphaEntity.getId());
        this.name = externalAlphaEntity.getName();
    }

    public String getName() {
        return name;
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(
                GraphEntityType.ALPHA,
                getId(),
                Map.of("name", name)
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
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
