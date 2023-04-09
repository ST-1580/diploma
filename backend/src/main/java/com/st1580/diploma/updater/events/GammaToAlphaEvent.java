package com.st1580.diploma.updater.events;

import java.util.Objects;

import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLinkEvent;

public class GammaToAlphaEvent implements LinkEvent {
    private final long gammaId;
    private final long alphaId;
    private final long weight;
    private final boolean isActive;
    private final long createdTs;

    public GammaToAlphaEvent(long gammaId, long alphaId, long weight, boolean isActive, long createdTs) {
        this.gammaId = gammaId;
        this.alphaId = alphaId;
        this.weight = weight;
        this.isActive = isActive;
        this.createdTs = createdTs;
    }

    public GammaToAlphaEvent(ExternalGammaToAlphaLinkEvent event) {
        this.gammaId = event.getLink().getGammaId();
        this.alphaId = event.getLink().getAlphaId();
        this.weight = event.getLink().getWeight();
        this.isActive = event.getLink().isActive();
        this.createdTs = event.getEventTs();
    }

    public long getFromId() {
        return gammaId;
    }

    public long getToId() {
        return alphaId;
    }

    public long getWeight() {
        return weight;
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
        GammaToAlphaEvent that = (GammaToAlphaEvent) o;
        return gammaId == that.gammaId && alphaId == that.alphaId && weight == that.weight && isActive == that.isActive && createdTs == that.createdTs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gammaId, alphaId, weight, isActive, createdTs);
    }
}
