package com.st1580.diploma.updater.events;

import java.util.Objects;

import com.st1580.diploma.repository.types.EntityActiveType;
import com.st1580.diploma.external.delta.data.DeltaEventType;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntityEvent;

public class DeltaEvent {
    private final long deltaId;
    private final String name;
    private final EntityActiveType type;
    private final long createdTs;

    public DeltaEvent(long deltaId, String name, EntityActiveType type, long createdTs) {
        this.deltaId = deltaId;
        this.name = name;
        this.type = type;
        this.createdTs = createdTs;
    }

    public DeltaEvent(ExternalDeltaEntityEvent event) {
        this.deltaId = event.getEntity().getId();
        this.name = event.getEntity().getName();
        this.type = parseTypeFromExternalEvent(event.getType());
        this.createdTs = event.getEventTs();
    }

    private EntityActiveType parseTypeFromExternalEvent(DeltaEventType type) {
        switch (type) {
            case CREATE:
            case UPDATE:
                return EntityActiveType.TRUE;
            case DELETE:
                return EntityActiveType.DELETED;
        }

        throw new IllegalArgumentException("Wrong DeltaEventType");
    }


    public long getDeltaId() {
        return deltaId;
    }

    public String getName() {
        return name;
    }

    public EntityActiveType getType() {
        return type;
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
        DeltaEvent that = (DeltaEvent) o;
        return deltaId == that.deltaId && createdTs == that.createdTs && Objects.equals(name, that.name) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deltaId, name, type, createdTs);
    }
}
