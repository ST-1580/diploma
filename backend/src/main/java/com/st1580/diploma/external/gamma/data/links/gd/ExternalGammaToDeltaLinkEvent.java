package com.st1580.diploma.external.gamma.data.links.gd;

import java.util.Objects;

import com.st1580.diploma.external.gamma.data.GammaEventType;

import static java.lang.System.currentTimeMillis;

public class ExternalGammaToDeltaLinkEvent {
    private final GammaEventType type;
    private final long eventTs;
    private final ExternalGammaToDeltaLink link;

    public ExternalGammaToDeltaLinkEvent(GammaEventType type, ExternalGammaToDeltaLink link) {
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

    public ExternalGammaToDeltaLink getLink() {
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
        ExternalGammaToDeltaLinkEvent that = (ExternalGammaToDeltaLinkEvent) o;
        return eventTs == that.eventTs && type == that.type && Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventTs, link);
    }
}
