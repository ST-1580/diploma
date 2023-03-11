package com.st1580.diploma.updater.events;

import java.util.Objects;

import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLinkEvent;

public class AlphaToBetaEvent {
    private final long alphaId;
    private final long betaId;
    private final String hash;
    private final boolean isActive;
    private final long createdTs;

    public AlphaToBetaEvent(long alphaId, long betaId, String hash, boolean isActive, long createdTs) {
        this.alphaId = alphaId;
        this.betaId = betaId;
        this.hash = hash;
        this.isActive = isActive;
        this.createdTs = createdTs;
    }

    public AlphaToBetaEvent(ExternalAlphaToBetaLinkEvent event) {
        this.alphaId = event.getLink().getAlphaId();
        this.betaId = event.getLink().getBetaId();
        this.hash = event.getLink().getHash();
        this.isActive = event.getLink().isActive();
        this.createdTs = event.getEventTs();
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

    public boolean isActive() {
        return isActive;
    }

    public long getCreatedTs() {
        return createdTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AlphaToBetaEvent that = (AlphaToBetaEvent) o;
        return alphaId == that.alphaId && betaId == that.betaId && isActive == that.isActive && createdTs == that.createdTs && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alphaId, betaId, hash, isActive, createdTs);
    }
}
