package com.st1580.diploma.external.alpha.data;

import java.util.Objects;

public class ExternalAlphaToBetaLink {
    private final long alphaId;
    private final long betaId;
    private final String hash;

    public ExternalAlphaToBetaLink(long alphaId, long betaId, String hash) {
        this.alphaId = alphaId;
        this.betaId = betaId;
        this.hash = hash;
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
        ExternalAlphaToBetaLink that = (ExternalAlphaToBetaLink) o;
        return alphaId == that.alphaId && betaId == that.betaId && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alphaId, betaId, hash);
    }
}
