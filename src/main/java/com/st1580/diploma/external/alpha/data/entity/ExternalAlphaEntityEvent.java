package com.st1580.diploma.external.alpha.data.entity;

import java.util.Objects;

import com.st1580.diploma.external.alpha.data.AlphaEventType;
import jakarta.annotation.Nullable;

import static java.lang.System.currentTimeMillis;

public class ExternalAlphaEntityEvent {
    private final AlphaEventType type;
    private final long eventTs;
    @Nullable
    private final ExternalAlphaEntity entity;

    public ExternalAlphaEntityEvent(AlphaEventType type, ExternalAlphaEntity entity) {
        this.type = type;
        this.eventTs = currentTimeMillis();
        this.entity = entity;
    }

    public AlphaEventType getType() {
        return type;
    }

    public long getEventTs() {
        return eventTs;
    }

    public ExternalAlphaEntity getEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalAlphaEntityEvent that = (ExternalAlphaEntityEvent) o;
        return eventTs == that.eventTs && type == that.type && Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventTs, entity);
    }
}
