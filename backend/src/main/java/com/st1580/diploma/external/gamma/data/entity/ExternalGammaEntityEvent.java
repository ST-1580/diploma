package com.st1580.diploma.external.gamma.data.entity;

import java.util.Objects;

import com.st1580.diploma.external.gamma.data.GammaEventType;

import static java.lang.System.currentTimeMillis;

public class ExternalGammaEntityEvent {
    private final GammaEventType type;
    private final long eventTs;
    private final ExternalGammaEntity entity;

    public ExternalGammaEntityEvent(GammaEventType type, ExternalGammaEntity entity) {
        this.type = type;
        this.eventTs = currentTimeMillis();
        this.entity = entity;
    }

    public GammaEventType getType() {
        return type;
    }

    public long getEventTs() {
        return eventTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalGammaEntityEvent that = (ExternalGammaEntityEvent) o;
        return eventTs == that.eventTs && type == that.type && Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventTs, entity);
    }

    public ExternalGammaEntity getEntity() {
        return entity;
    }

}
