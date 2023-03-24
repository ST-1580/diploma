package com.st1580.diploma.collector.graph.links;

import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.LinkEndDto;
import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLink;

public class GammaToDeltaLink extends AbstractLink {
    private final long gammaId;
    private final long deltaId;

    public GammaToDeltaLink(long gammaId, long deltaId) {
        super(new LightEntity(EntityType.GAMMA, gammaId), new LightEntity(EntityType.DELTA, deltaId));
        this.gammaId = gammaId;
        this.deltaId = deltaId;
    }

    public GammaToDeltaLink(ExternalGammaToDeltaLink externalGammaToDeltaLink) {
        super(new LightEntity(EntityType.GAMMA, externalGammaToDeltaLink.getGammaId()),
                new LightEntity(EntityType.DELTA, externalGammaToDeltaLink.getDeltaId()));
        this.gammaId = externalGammaToDeltaLink.getGammaId();
        this.deltaId = externalGammaToDeltaLink.getDeltaId();
    }

    public long getGammaId() {
        return gammaId;
    }

    public long getDeltaId() {
        return deltaId;
    }

    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(
                new LinkEndDto(getFrom().convertToDto().getType(), getFrom().getId()),
                new LinkEndDto(getTo().convertToDto().getType(), getTo().getId()),
                null);
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
        GammaToDeltaLink that = (GammaToDeltaLink) o;
        return gammaId == that.gammaId && deltaId == that.deltaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gammaId, deltaId);
    }
}
