package com.st1580.diploma.collector.service.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;

public abstract class GraphEntityDto {
    private final GraphEntityType type;

    private final long id;
    public GraphEntityDto(GraphEntityType type, long id) {
        this.type = type;
        this.id = id;
    }

    public abstract Entity convertToModel();

    public GraphEntityType getType() {
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
        GraphEntityDto graphEntityDto = (GraphEntityDto) o;
        return id == graphEntityDto.id && type == graphEntityDto.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
