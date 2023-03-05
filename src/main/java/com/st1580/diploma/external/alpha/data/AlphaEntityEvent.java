package com.st1580.diploma.external.alpha.data;

import java.util.Objects;

import jakarta.annotation.Nullable;

import static java.lang.System.currentTimeMillis;

public class AlphaEntityEvent {
    private final AlphaEventType type;

    private final long eventTs;

    @Nullable
    private final Long entityId;

    @Nullable
    private final ExternalAlphaEntity entity;

    public AlphaEntityEvent(AlphaEventType type, Long entityId, ExternalAlphaEntity entity) {
        this.type = type;
        this.eventTs = currentTimeMillis();
        this.entityId = entityId;
        this.entity = entity;
    }

    public AlphaEventType getType() {
        return type;
    }

    public long getEventTs() {
        return eventTs;
    }

    public Long getEntityId() {
        return entityId;
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
        AlphaEntityEvent that = (AlphaEntityEvent) o;
        return eventTs == that.eventTs && type == that.type && Objects.equals(entityId, that.entityId) && Objects.equals(entity,
                that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventTs, entityId, entity);
    }
}
