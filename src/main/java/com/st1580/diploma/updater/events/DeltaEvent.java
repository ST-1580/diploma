package com.st1580.diploma.updater.events;

import java.util.Objects;

import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.external.delta.data.DeltaEventType;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntityEvent;

public class DeltaEvent {
    private final long id;
    private final String name;
    private final EntityActiveType type;
    private final long createdTs;

    public DeltaEvent(long id, String name, EntityActiveType type, long createdTs) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdTs = createdTs;
    }

    public DeltaEvent(ExternalDeltaEntityEvent event) {
        this.id = event.getEntity().getId();
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


    public long getId() {
        return id;
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
        return id == that.id && createdTs == that.createdTs && Objects.equals(name, that.name) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, createdTs);
    }
}
