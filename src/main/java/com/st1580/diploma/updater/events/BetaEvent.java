package com.st1580.diploma.updater.events;

import java.util.Objects;

import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.external.beta.data.ExternalBetaEntityEvent;

public class BetaEvent {
    private final long id;
    private final int epoch;
    private final EntityActiveType type;
    private final long createdTs;

    public BetaEvent(long id, int epoch, EntityActiveType type, long createdTs) {
        this.id = id;
        this.epoch = epoch;
        this.type = type;
        this.createdTs = createdTs;
    }

    public BetaEvent(ExternalBetaEntityEvent event) {
        this.id = event.getEntity().getId();
        this.epoch = event.getEntity().getEpoch();
        this.type = parseTypeFromExternalEvent(event.getType());
        this.createdTs = event.getEventTs();
    }

    private EntityActiveType parseTypeFromExternalEvent(char type) {
        switch (type) {
            case 'c':
            case 'u':
                return EntityActiveType.TRUE;
            case 'd':
                return EntityActiveType.DELETED;
        }

        throw new IllegalArgumentException("Wrong beta event type");
    }

    public long getId() {
        return id;
    }

    public int getEpoch() {
        return epoch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BetaEvent betaEvent = (BetaEvent) o;
        return id == betaEvent.id && epoch == betaEvent.epoch && createdTs == betaEvent.createdTs && type == betaEvent.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, epoch, type, createdTs);
    }

    public EntityActiveType getType() {
        return type;
    }

    public long getCreatedTs() {
        return createdTs;
    }

}