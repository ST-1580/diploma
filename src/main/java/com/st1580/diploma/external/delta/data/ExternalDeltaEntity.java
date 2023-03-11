package com.st1580.diploma.external.delta.data;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalDeltaEntity {
    private final long id;
    private final String name;
    private final boolean isActive;

    public ExternalDeltaEntity(@JsonProperty("id") long id,
                               @JsonProperty("isActive") boolean isActive,
                               @JsonProperty("name") String name) {
        this.id = id;
        this.isActive = isActive;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
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
        ExternalDeltaEntity that = (ExternalDeltaEntity) o;
        return id == that.id && isActive == that.isActive && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isActive);
    }
}
