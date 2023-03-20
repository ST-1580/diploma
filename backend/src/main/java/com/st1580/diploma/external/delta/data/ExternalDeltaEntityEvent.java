package com.st1580.diploma.external.delta.data;

import java.util.Objects;

import static java.lang.System.currentTimeMillis;

public class ExternalDeltaEntityEvent {
    private final DeltaEventType type;
    private final long eventTs;
    private final ExternalDeltaEntity entity;

    public ExternalDeltaEntityEvent(DeltaEventType type, ExternalDeltaEntity entity) {
        this.type = type;
        this.eventTs = currentTimeMillis();
        this.entity = entity;
    }

    public DeltaEventType getType() {
        return type;
    }

    public long getEventTs() {
        return eventTs;
    }

    public ExternalDeltaEntity getEntity() {
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
        ExternalDeltaEntityEvent that = (ExternalDeltaEntityEvent) o;
        return eventTs == that.eventTs && type == that.type && Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventTs, entity);
    }
}
