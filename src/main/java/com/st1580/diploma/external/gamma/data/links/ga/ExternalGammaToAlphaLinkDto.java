package com.st1580.diploma.external.gamma.data.links.ga;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalGammaToAlphaLinkDto {
    private final long gammaId;
    private final long alphaId;
    private final long weight;

    public ExternalGammaToAlphaLinkDto(@JsonProperty("gammaId") long gammaId,
                                       @JsonProperty("alphaId") long alphaId,
                                       @JsonProperty("weight") long weight) {
        this.gammaId = gammaId;
        this.alphaId = alphaId;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalGammaToAlphaLinkDto that = (ExternalGammaToAlphaLinkDto) o;
        return gammaId == that.gammaId && alphaId == that.alphaId && weight == that.weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gammaId, alphaId, weight);
    }
}
