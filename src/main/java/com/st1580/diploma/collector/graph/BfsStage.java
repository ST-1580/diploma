package com.st1580.diploma.collector.graph;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BfsStage {
    private final EntityType type;

    private final Set<Long> entitiesIds;

    public BfsStage(EntityType type, Set<Long> entitiesIds) {
        this.type = type;
        this.entitiesIds = entitiesIds;
    }

    public EntityType getType() {
        return type;
    }

    public Set<Long> getEntitiesIds() {
        return entitiesIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BfsStage bfsStage = (BfsStage) o;
        return type == bfsStage.type && Objects.equals(entitiesIds, bfsStage.entitiesIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, entitiesIds);
    }
}
