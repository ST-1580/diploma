package com.st1580.diploma.collector.service.dto;

import java.util.Map;
import java.util.Objects;

public class GraphEntityDto {
    private final GraphEntityType type;
    private final long id;
    private final Map<String, String> payload;

    public GraphEntityDto(GraphEntityType type, long id, Map<String, String> payload) {
        this.type = type;
        this.id = id;
        this.payload = payload;
    }

    public GraphEntityType getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GraphEntityDto that = (GraphEntityDto) o;
        return id == that.id && type == that.type && Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, payload);
    }
}
