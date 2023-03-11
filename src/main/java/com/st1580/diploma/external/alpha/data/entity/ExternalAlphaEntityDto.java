package com.st1580.diploma.external.alpha.data.entity;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalAlphaEntityDto {
    private final long id;
    private final String name;

    public ExternalAlphaEntityDto(@JsonProperty("id") long id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
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
        ExternalAlphaEntityDto that = (ExternalAlphaEntityDto) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
