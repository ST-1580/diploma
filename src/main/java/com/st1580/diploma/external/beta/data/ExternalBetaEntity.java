package com.st1580.diploma.external.beta.data;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import static java.lang.System.currentTimeMillis;

public class ExternalBetaEntity {
    private final long id;
    private final long createdTs;

    public ExternalBetaEntity(@JsonProperty("id") long id) {
        this.id = id;
        this.createdTs = currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public long getCreatedTs() {
        return createdTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalBetaEntity that = (ExternalBetaEntity) o;
        return id == that.id && createdTs == that.createdTs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdTs);
    }
}
