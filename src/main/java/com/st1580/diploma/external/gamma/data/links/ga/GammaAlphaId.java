package com.st1580.diploma.external.gamma.data.links.ga;

import java.util.Objects;

public class GammaAlphaId {
    private final long gammaId;
    private final long alphaId;

    public GammaAlphaId(long gammaId, long alphaId) {
        this.gammaId = gammaId;
        this.alphaId = alphaId;
    }

    public long getGammaId() {
        return gammaId;
    }

    public long getAlphaId() {
        return alphaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GammaAlphaId that = (GammaAlphaId) o;
        return gammaId == that.gammaId && alphaId == that.alphaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gammaId, alphaId);
    }
}
