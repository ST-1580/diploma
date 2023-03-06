package com.st1580.diploma.external.beta.data;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalBetaEntity {
    private final long id;
    private final long data;
    private final boolean isDeprecated;
    private final String unimportantString;

    public ExternalBetaEntity(@JsonProperty("id") long id,
                              @JsonProperty("data") long data,
                              @JsonProperty("isDeprecated") boolean isDeprecated,
                              @JsonProperty("unimportant") String unimportantString) {
        this.id = id;
        this.data = data;
        this.isDeprecated = isDeprecated;
        this.unimportantString = unimportantString;
    }

    public long getId() {
        return id;
    }

    public long getData() {
        return data;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public String getUnimportantString() {
        return unimportantString;
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
        return id == that.id && data == that.data && isDeprecated == that.isDeprecated && Objects.equals(unimportantString, that.unimportantString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data, isDeprecated, unimportantString);
    }
}
