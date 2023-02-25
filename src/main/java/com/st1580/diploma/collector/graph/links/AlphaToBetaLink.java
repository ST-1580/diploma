package com.st1580.diploma.collector.graph.links;

import java.util.Map;
import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.db.tables.records.AlphaToBetaRecord;

public class AlphaToBetaLink extends AbstractLink {
    private final long alphaId;
    private final long betaId;
    private final String property_1;
    public AlphaToBetaLink(long alphaId, long betaId, String property_1) {
        super(new LightEntity(EntityType.ALPHA, alphaId), new LightEntity(EntityType.BETA, betaId));
        this.alphaId = alphaId;
        this.betaId = betaId;
        this.property_1 = property_1;
    }

    public AlphaToBetaLink(AlphaToBetaRecord record) {
        super(new LightEntity(EntityType.ALPHA, record.getAlphaId()),
                new LightEntity(EntityType.BETA, record.getBetaId()));
        this.alphaId = record.getAlphaId();
        this.betaId = record.getBetaId();
        this.property_1 = record.getProperty_1();
    }

    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(
                getFrom().convertToDto(),
                getTo().convertToDto(),
                Map.of("property_1", property_1)
        );
    }

    public long getAlphaId() {
        return alphaId;
    }

    public long getBetaId() {
        return betaId;
    }

    public String getProperty_1() {
        return property_1;
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
        AlphaToBetaLink that = (AlphaToBetaLink) o;
        return alphaId == that.alphaId && betaId == that.betaId && Objects.equals(property_1, that.property_1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), alphaId, betaId, property_1);
    }
}
