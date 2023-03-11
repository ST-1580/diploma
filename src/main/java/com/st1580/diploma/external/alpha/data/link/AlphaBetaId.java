package com.st1580.diploma.external.alpha.data.link;

import java.util.Objects;

public class AlphaBetaId {
    private final long alphaId;
    private final long betaId;

    public AlphaBetaId(long alphaId, long betaId) {
        this.alphaId = alphaId;
        this.betaId = betaId;
    }

    public long getAlphaId() {
        return alphaId;
    }

    public long getBetaId() {
        return betaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AlphaBetaId that = (AlphaBetaId) o;
        return alphaId == that.alphaId && betaId == that.betaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alphaId, betaId);
    }
}
