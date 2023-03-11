package com.st1580.diploma.updater.events;

import java.util.Objects;

import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.external.gamma.data.GammaEventType;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntityEvent;

public class GammaEvent {
    private final long id;
    private final boolean isMaster;
    private final EntityActiveType type;
    private final long createdTs;

    public GammaEvent(long id, boolean isMaster, EntityActiveType type, long createdTs) {
        this.id = id;
        this.isMaster = isMaster;
        this.type = type;
        this.createdTs = createdTs;
    }

    public GammaEvent(ExternalGammaEntityEvent event) {
        this.id = event.getEntity().getId();
        this.isMaster = event.getEntity().isMaster();
        this.type = parseTypeFromExternalEvent(event.getType());
        this.createdTs = event.getEventTs();
    }

    private EntityActiveType parseTypeFromExternalEvent(GammaEventType type) {
        switch (type) {
            case INSERT:
            case UPDATE:
                return EntityActiveType.TRUE;
            case TURN_ON:
                return EntityActiveType.CHANGED_TO_TRUE;
            case TURN_OFF:
                return EntityActiveType.CHANGED_TO_FALSE;
        }

        throw new IllegalArgumentException("Wrong GammaEventType");
    }

    public long getId() {
        return id;
    }

    public boolean isMaster() {
        return isMaster;
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
        GammaEvent that = (GammaEvent) o;
        return id == that.id && isMaster == that.isMaster && createdTs == that.createdTs && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isMaster, type, createdTs);
    }
}
