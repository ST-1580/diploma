package com.st1580.diploma.collector.graph.links;

import java.util.Map;
import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.LinkEndDto;
import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLink;

public class GammaToAlphaLink extends AbstractLink {
    private final long gammaId;
    private final long alphaId;
    private final long weight;

    public GammaToAlphaLink(long gammaId, long alphaId, long weight) {
        super(new LightEntity(EntityType.GAMMA, gammaId), new LightEntity(EntityType.ALPHA, alphaId));
        this.alphaId = alphaId;
        this.gammaId = gammaId;
        this.weight = weight;
    }

    public long getGammaId() {
        return gammaId;
    }

    public long getAlphaId() {
        return alphaId;
    }

    public long getWeight() {
        return weight;
    }
    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(
                new LinkEndDto(getFrom().convertToDto().getType(), getFrom().getId()),
                new LinkEndDto(getTo().convertToDto().getType(), getTo().getId()),
                Map.of("weight", Long.toString(weight))
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
        return gammaId == that.gammaId && alphaId == that.alphaId && weight == that.weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gammaId, alphaId, weight);
    }


}
