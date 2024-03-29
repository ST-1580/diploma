package com.st1580.diploma.collector.graph;

import java.util.Objects;

import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;

public abstract class AbstractEntity implements Entity {
    private final EntityType type;
    private final long id;

    public AbstractEntity(EntityType type, long id) {
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

    public LightEntity convertToLight() {
        return new LightEntity(type, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractEntity that = (AbstractEntity) o;
        return id == that.id && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
