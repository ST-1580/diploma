package com.st1580.diploma.external.beta.data;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import static java.lang.System.currentTimeMillis;

public class ExternalBetaEntity {
    private final long id;
    private final int epoch;

    public ExternalBetaEntity(@JsonProperty("id") long id, @JsonProperty("epoch") int epoch) {
        this.id = id;
        this.epoch = epoch;
    }

    public long getId() {
        return id;
    }

    public int getEpoch() {
        return epoch;
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
        return id == that.id && epoch == that.epoch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, epoch);
    }
}
