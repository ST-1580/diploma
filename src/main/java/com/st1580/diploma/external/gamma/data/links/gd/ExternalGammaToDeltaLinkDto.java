package com.st1580.diploma.external.gamma.data.links.gd;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalGammaToDeltaLinkDto {
    private final long gammaId;
    private final long deltaId;

    public ExternalGammaToDeltaLinkDto(@JsonProperty("gammaId") long gammaId, @JsonProperty("deltaId") long deltaId) {
        this.gammaId = gammaId;
        this.deltaId = deltaId;
    }

    public long getGammaId() {
        return gammaId;
    }

    public long getDeltaId() {
        return deltaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalGammaToDeltaLinkDto that = (ExternalGammaToDeltaLinkDto) o;
        return gammaId == that.gammaId && deltaId == that.deltaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gammaId, deltaId);
    }
}
