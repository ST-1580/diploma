package com.st1580.diploma.external.gamma.data.links.ga;

import java.util.Objects;

import com.st1580.diploma.external.gamma.data.GammaEventType;

import static java.lang.System.currentTimeMillis;

public class ExternalGammaToAlphaLinkEvent {
    private final GammaEventType type;
    private final long eventTs;
    private final ExternalGammaToAlphaLink link;

    public ExternalGammaToAlphaLinkEvent(GammaEventType type, ExternalGammaToAlphaLink link) {
        this.type = type;
        this.eventTs = currentTimeMillis();
        this.link = link;
    }

    public GammaEventType getType() {
        return type;
    }

    public long getEventTs() {
        return eventTs;
    }

    public ExternalGammaToAlphaLink getLink() {
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
        ExternalGammaToAlphaLinkEvent that = (ExternalGammaToAlphaLinkEvent) o;
        return eventTs == that.eventTs && type == that.type && Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventTs, link);
    }
}
