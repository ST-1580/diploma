package com.st1580.diploma.external.gamma.data.links.ga;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalGammaToAlphaLink {
    private final long gammaId;
    private final long alphaId;
    private final long weight;
    private final boolean isActive;

    public ExternalGammaToAlphaLink(long gammaId, long alphaId, long weight, boolean isActive) {
        this.gammaId = gammaId;
        this.alphaId = alphaId;
        this.weight = weight;
        this.isActive = isActive;
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

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalGammaToAlphaLink that = (ExternalGammaToAlphaLink) o;
        return gammaId == that.gammaId && alphaId == that.alphaId && weight == that.weight && isActive == that.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gammaId, alphaId, weight, isActive);
    }
}
