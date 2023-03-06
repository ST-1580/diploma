package com.st1580.diploma.external.beta.data;

import java.util.Objects;

import static java.lang.System.currentTimeMillis;

public class BetaEntityEvent {
    private final char type;
    private final long eventTs;
    private final long entityId;
    private final ExternalBetaEntity entity;

    public BetaEntityEvent(char type, long entityId, ExternalBetaEntity entity) {
        this.type = type;
        this.eventTs = currentTimeMillis();
        this.entityId = entityId;
        this.entity = entity;
    }

    public char getType() {
        return type;
    }

    public long getEventTs() {
        return eventTs;
    }

    public long getEntityId() {
        return entityId;
    }

    public ExternalBetaEntity getEntity() {
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
        BetaEntityEvent that = (BetaEntityEvent) o;
        return type == that.type && eventTs == that.eventTs && entityId == that.entityId && Objects.equals(entity,
                that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventTs, entityId, entity);
    }
}
