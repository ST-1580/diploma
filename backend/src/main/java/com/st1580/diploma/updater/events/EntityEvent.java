package com.st1580.diploma.updater.events;

import java.util.Objects;

public class EntityEvent {
    private final long entityId;
    private final long createdTs;

    public EntityEvent(long entityId, long createdTs) {
        this.entityId = entityId;
        this.createdTs = createdTs;
    }

    public long getEntityId() {
        return entityId;
    }

    public long getCreatedTs() {
        return createdTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityEvent that = (EntityEvent) o;
        return entityId == that.entityId && createdTs == that.createdTs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, createdTs);
    }
}
