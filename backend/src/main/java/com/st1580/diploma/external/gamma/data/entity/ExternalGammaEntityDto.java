package com.st1580.diploma.external.gamma.data.entity;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalGammaEntityDto {
    private final long id;
    private final boolean isMaster;

    public ExternalGammaEntityDto(@JsonProperty("id") long id,
                                  @JsonProperty("isMaster") boolean isMaster) {
        this.id = id;
        this.isMaster = isMaster;
    }

    public long getId() {
        return id;
    }

    @JsonProperty("isMaster")
    public boolean isMaster() {
        return isMaster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalGammaEntityDto that = (ExternalGammaEntityDto) o;
        return id == that.id && isMaster == that.isMaster;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isMaster);
    }
}
