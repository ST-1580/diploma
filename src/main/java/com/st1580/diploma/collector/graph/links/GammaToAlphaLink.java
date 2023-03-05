package com.st1580.diploma.collector.graph.links;

import java.util.Map;
import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.db.tables.records.GammaToAlphaRecord;

public class GammaToAlphaLink extends AbstractLink {
    private final long gammaId;
    private final long alphaId;
    private final Long property_1;
    private final String property_2;

    public GammaToAlphaLink(long gammaId, long alphaId, Long property_1, String property_2) {
        super(new LightEntity(EntityType.GAMMA, gammaId), new LightEntity(EntityType.ALPHA, alphaId));
        this.alphaId = alphaId;
        this.gammaId = gammaId;
        this.property_1 = property_1;
        this.property_2 = property_2;
    }

    public long getGammaId() {
        return gammaId;
    }

    public long getAlphaId() {
        return alphaId;
    }

    public Long getProperty_1() {
        return property_1;
    }

    public String getProperty_2() {
        return property_2;
    }

    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(
                getFrom().convertToDto(),
                getTo().convertToDto(),
                Map.of("property_1", property_1.toString(),
                        "property_2", property_2)
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
        GammaToAlphaLink that = (GammaToAlphaLink) o;
        return gammaId == that.gammaId && alphaId == that.alphaId && Objects.equals(property_1, that.property_1) && Objects.equals(property_2, that.property_2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gammaId, alphaId, property_1, property_2);
    }
}
