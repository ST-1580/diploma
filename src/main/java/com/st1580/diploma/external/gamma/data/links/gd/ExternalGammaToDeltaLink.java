package com.st1580.diploma.external.gamma.data.links.gd;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalGammaToDeltaLink {
    private final long gammaId;
    private final long deltaId;
    private final boolean isActive;

    public ExternalGammaToDeltaLink(long gammaId, long deltaId, boolean isActive) {
        this.gammaId = gammaId;
        this.deltaId = deltaId;
        this.isActive = isActive;
    }

    public long getGammaId() {
        return gammaId;
    }

    public long getDeltaId() {
        return deltaId;
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
        ExternalGammaToDeltaLink that = (ExternalGammaToDeltaLink) o;
        return gammaId == that.gammaId && deltaId == that.deltaId && isActive == that.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gammaId, deltaId, isActive);
    }
}
