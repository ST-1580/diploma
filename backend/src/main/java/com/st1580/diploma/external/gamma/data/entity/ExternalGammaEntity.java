package com.st1580.diploma.external.gamma.data.entity;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalGammaEntity {
    private final long id;
    private final boolean isMaster;
    private final boolean isActive;

    public ExternalGammaEntity(long id, boolean isMaster, boolean isActive) {
        this.id = id;
        this.isMaster = isMaster;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalGammaEntity that = (ExternalGammaEntity) o;
        return id == that.id && isMaster == that.isMaster && isActive == that.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isMaster, isActive);
    }
}
