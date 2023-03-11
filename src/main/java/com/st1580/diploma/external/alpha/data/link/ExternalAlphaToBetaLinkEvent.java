package com.st1580.diploma.external.alpha.data.link;

import java.util.Objects;

import com.st1580.diploma.external.alpha.data.AlphaEventType;

import static java.lang.System.currentTimeMillis;

public class ExternalAlphaToBetaLinkEvent {
    private final AlphaEventType type;
    private final long eventTs;
    private final ExternalAlphaToBetaLink link;

    public ExternalAlphaToBetaLinkEvent(AlphaEventType type, ExternalAlphaToBetaLink link) {
        this.type = type;
        this.eventTs = currentTimeMillis();
        this.link = link;
    }

    public AlphaEventType getType() {
        return type;
    }

    public long getEventTs() {
        return eventTs;
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
        ExternalAlphaToBetaLinkEvent that = (ExternalAlphaToBetaLinkEvent) o;
        return eventTs == that.eventTs && type == that.type && Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventTs, link);
    }
}
