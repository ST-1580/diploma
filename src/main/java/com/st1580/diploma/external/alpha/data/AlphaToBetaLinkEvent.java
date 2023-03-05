package com.st1580.diploma.external.alpha.data;

import java.util.Objects;

import jakarta.annotation.Nullable;

import static java.lang.System.currentTimeMillis;

public class AlphaToBetaLinkEvent {
    private final AlphaEventType type;

    private final long eventTs;

    @Nullable
    private final Long alphaId;

    @Nullable
    private final Long betaId;

    private final ExternalAlphaToBetaLink link;

    public AlphaToBetaLinkEvent(AlphaEventType type, Long alphaId, Long betaId, ExternalAlphaToBetaLink link) {
        this.type = type;
        this.eventTs = currentTimeMillis();
        this.alphaId = alphaId;
        this.betaId = betaId;
        this.link = link;
    }

    public AlphaEventType getType() {
        return type;
    }

    public long getEventTs() {
        return eventTs;
    }

    public Long getAlphaId() {
        return alphaId;
    }

    public Long getBetaId() {
        return betaId;
    }

    public ExternalAlphaToBetaLink getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AlphaToBetaLinkEvent that = (AlphaToBetaLinkEvent) o;
        return eventTs == that.eventTs && type == that.type && Objects.equals(alphaId, that.alphaId) && Objects.equals(betaId, that.betaId) && Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventTs, alphaId, betaId, link);
    }
}
