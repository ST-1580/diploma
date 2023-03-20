package com.st1580.diploma.updater.events;

import java.util.Objects;

import com.st1580.diploma.repository.types.EntityActiveType;
import com.st1580.diploma.external.alpha.data.AlphaEventType;
import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntityEvent;

public class AlphaEvent {
    private final long alphaId;
    private final String name;
    private final EntityActiveType type;
    private final long createdTs;

    public AlphaEvent(long alphaId, String name, EntityActiveType type, long createdTs) {
        this.alphaId = alphaId;
        this.name = name;
        this.type = type;
        this.createdTs = createdTs;
    }

    public AlphaEvent(ExternalAlphaEntityEvent event) {
        this.alphaId = event.getEntity().getId();
        this.name = event.getEntity().getName();
        this.type = parseTypeFromExternalEvent(event.getType());
        this.createdTs = event.getEventTs();
    }

    private EntityActiveType parseTypeFromExternalEvent(AlphaEventType type) {
        switch (type) {
            case CREATE:
            case UPDATE:
                return EntityActiveType.TRUE;
            case ACTIVE:
                return EntityActiveType.CHANGED_TO_TRUE;
            case DISABLE:
                return EntityActiveType.CHANGED_TO_FALSE;
        }

        throw new IllegalArgumentException("Wrong AlphaEventType");
    }

    public long getAlphaId() {
        return alphaId;
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
        AlphaEvent that = (AlphaEvent) o;
        return alphaId == that.alphaId && createdTs == that.createdTs && Objects.equals(name, that.name) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alphaId, name, type, createdTs);
    }
}
