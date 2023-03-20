package com.st1580.diploma.external.alpha.data.link;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalAlphaToBetaLinkDto {
    private final long alphaId;
    private final long betaId;
    private final String hash;

    public ExternalAlphaToBetaLinkDto(@JsonProperty("alphaId") long alphaId,
                                      @JsonProperty("betaId") long betaId,
                                      @JsonProperty("hash") String hash) {
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
        ExternalAlphaToBetaLinkDto that = (ExternalAlphaToBetaLinkDto) o;
        return alphaId == that.alphaId && betaId == that.betaId && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alphaId, betaId, hash);
    }
}
