package com.st1580.diploma.collector.graph.entities;

import java.util.Map;
import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractEntity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphEntityType;
import com.st1580.diploma.external.beta.data.ExternalBetaEntity;

public class BetaEntity extends AbstractEntity {
    private final long epoch;

    public BetaEntity(long id, long epoch) {
        super(EntityType.BETA, id);
        this.epoch = epoch;
    }

    public long getEpoch() {
        return epoch;
    }

    @Override
    public GraphEntityDto convertToDto() {
        return new GraphEntityDto(
                GraphEntityType.BETA,
                getId(),
                Map.of("epoch", Long.toString(epoch))
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
        return epoch == that.epoch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epoch);
    }
}
