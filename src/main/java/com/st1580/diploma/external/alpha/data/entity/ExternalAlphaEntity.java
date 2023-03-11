package com.st1580.diploma.external.alpha.data.entity;

import java.util.Objects;

public class ExternalAlphaEntity {
    private final long id;
    private boolean isActive;
    private final String name;

    public ExternalAlphaEntity(long id, String name, boolean isActive) {
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

    public void changeActive() {
        isActive = !isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalAlphaEntity that = (ExternalAlphaEntity) o;
        return id == that.id && isActive == that.isActive && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isActive, name);
    }
}
