package com.st1580.diploma.collector.service.dto;

import java.util.Objects;

public class LinkEndDto {
    private final GraphEntityType type;
    private final long id;

    public LinkEndDto(GraphEntityType type, long id) {
        this.type = type;
        this.id = id;
    }

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
        LinkEndDto that = (LinkEndDto) o;
        return id == that.id && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
