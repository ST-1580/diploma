package com.st1580.diploma.collector.graph.links;

import java.util.Map;
import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLink;

public class AlphaToBetaLink extends AbstractLink {
    private final long alphaId;
    private final long betaId;
    private final String hash;

    public AlphaToBetaLink(long alphaId, long betaId, String hash) {
        super(new LightEntity(EntityType.ALPHA, alphaId), new LightEntity(EntityType.BETA, betaId));
        this.alphaId = alphaId;
        this.betaId = betaId;
        this.hash = hash;
    }

    public AlphaToBetaLink(ExternalAlphaToBetaLink externalAlphaToBetaLink) {
        super(new LightEntity(EntityType.ALPHA, externalAlphaToBetaLink.getAlphaId()),
                new LightEntity(EntityType.BETA, externalAlphaToBetaLink.getBetaId()));
        this.alphaId = externalAlphaToBetaLink.getAlphaId();
        this.betaId = externalAlphaToBetaLink.getBetaId();
        this.hash = externalAlphaToBetaLink.getHash();
    }

    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(
                getFrom().convertToDto(),
                getTo().convertToDto(),
                Map.of("hash", hash)
        );
    }

    public long getAlphaId() {
        return alphaId;
    }

    public long getBetaId() {
        return betaId;
    }

    public String getHash() {
        return hash;
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
        return alphaId == that.alphaId && betaId == that.betaId && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), alphaId, betaId, hash);
    }
}
