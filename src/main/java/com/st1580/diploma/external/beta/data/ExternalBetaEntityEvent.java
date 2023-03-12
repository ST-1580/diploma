package com.st1580.diploma.external.beta.data;

import java.util.Objects;

import static java.lang.System.currentTimeMillis;

public class ExternalBetaEntityEvent {
    private final char type;
    private final long eventTs;
    private final ExternalBetaEntity entity;

    public ExternalBetaEntityEvent(char type, ExternalBetaEntity entity) {
        this.type = type;
        this.eventTs = currentTimeMillis();
        this.entity = entity;
    }

    public char getType() {
        return type;
    }

    public long getEventTs() {
        return eventTs;
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
        ExternalBetaEntityEvent that = (ExternalBetaEntityEvent) o;
        return type == that.type && eventTs == that.eventTs && Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventTs, entity);
    }
}
