package com.st1580.diploma.collector.graph;

import java.util.Objects;

import com.st1580.diploma.collector.service.dto.GraphEntityDto;

public abstract class Entity {
    private final EntityType type;

    private final long id;

    public Entity(EntityType type, long id) {
        this.type = type;
        this.id = id;
    }

    public abstract GraphEntityDto convertToDto();

    public EntityType getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entity entity = (Entity) o;
        return id == entity.id && type == entity.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
