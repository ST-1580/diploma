package com.st1580.diploma.updater.events;

import java.util.Objects;

import com.st1580.diploma.repository.types.EntityActiveType;
import com.st1580.diploma.external.gamma.data.GammaEventType;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntityEvent;

public class GammaEvent implements EntityEvent {
    private final long gammaId;
    private final boolean isMaster;
    private final EntityActiveType type;
    private final long createdTs;

    public GammaEvent(long gammaId, boolean isMaster, EntityActiveType type, long createdTs) {
        this.gammaId = gammaId;
        this.isMaster = isMaster;
        this.type = type;
        this.createdTs = createdTs;
    }

    public GammaEvent(ExternalGammaEntityEvent event) {
        this.gammaId = event.getEntity().getId();
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

    public long getEntityId() {
        return gammaId;
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
        return gammaId == that.gammaId && isMaster == that.isMaster && createdTs == that.createdTs && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gammaId, isMaster, type, createdTs);
    }
}
