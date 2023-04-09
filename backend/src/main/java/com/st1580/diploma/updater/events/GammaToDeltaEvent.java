package com.st1580.diploma.updater.events;

import java.util.Objects;

import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLinkEvent;

public class GammaToDeltaEvent implements LinkEvent {
    private final long gammaId;
    private final long deltaId;
    private final boolean isActive;
    private final long createdTs;

    public GammaToDeltaEvent(long gammaId, long deltaId, boolean isActive, long createdTs) {
        this.gammaId = gammaId;
        this.deltaId = deltaId;
        this.isActive = isActive;
        this.createdTs = createdTs;
    }

    public GammaToDeltaEvent(ExternalGammaToDeltaLinkEvent event) {
        this.gammaId = event.getLink().getGammaId();
        this.deltaId = event.getLink().getDeltaId();
        this.isActive = event.getLink().isActive();
        this.createdTs = event.getEventTs();
    }

    public long getFromId() {
        return gammaId;
    }

    public long getToId() {
        return deltaId;
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
        GammaToDeltaEvent that = (GammaToDeltaEvent) o;
        return gammaId == that.gammaId && deltaId == that.deltaId && isActive == that.isActive && createdTs == that.createdTs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gammaId, deltaId, isActive, createdTs);
    }
}
