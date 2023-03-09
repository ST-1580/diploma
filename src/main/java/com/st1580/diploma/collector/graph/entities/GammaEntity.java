package com.st1580.diploma.collector.graph.entities;

import java.util.Map;
import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;
import com.st1580.diploma.external.gamma.data.ExternalGammaEntity;

public class GammaEntity extends AbstractEntity {
    private final boolean isMaster;

    public GammaEntity(long id, boolean isMaster) {
        super(EntityType.GAMMA, id);
        this.isMaster = isMaster;
    }

    public GammaEntity(ExternalGammaEntity externalGammaEntity) {
        super(EntityType.GAMMA, externalGammaEntity.getId());
        this.isMaster = externalGammaEntity.isMaster();
    }

    public boolean getMaster() {
        return isMaster;
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(
                GraphEntityType.DELTA,
                getId(),
                Map.of("isMaster", Boolean.toString(isMaster))
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
        return isMaster == that.isMaster;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isMaster);
    }
}