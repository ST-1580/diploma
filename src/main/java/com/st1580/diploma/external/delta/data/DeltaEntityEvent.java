package com.st1580.diploma.external.delta.data;

import java.util.Objects;

import static java.lang.System.currentTimeMillis;

public class DeltaEntityEvent {
    private final DeltaEventType type;
    private final long eventTs;
    private final long deltaId;
    private final String name;

    public DeltaEntityEvent(DeltaEventType type, long deltaId, String name) {
        this.type = type;
        this.eventTs = currentTimeMillis();
        this.deltaId = deltaId;
        this.name = name;
    }

    public DeltaEventType getType() {
        return type;
    }

    public long getEventTs() {
        return eventTs;
    }

    public long getDeltaId() {
        return deltaId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeltaEntityEvent that = (DeltaEntityEvent) o;
        return eventTs == that.eventTs && deltaId == that.deltaId && type == that.type && Objects.equals(name,
                that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventTs, deltaId, name);
    }
}
