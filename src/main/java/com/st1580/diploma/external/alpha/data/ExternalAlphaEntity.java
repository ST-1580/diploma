package com.st1580.diploma.external.alpha.data;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import static java.lang.System.currentTimeMillis;

public class ExternalAlphaEntity {
    private final long id;
    private final String name;
    private final Long createdTs;
    private final Long stat;

    public ExternalAlphaEntity(@JsonProperty("id") long id,
                               @JsonProperty("name") String name,
                               @JsonProperty("stat") Long stat) {
        this.id = id;
        this.name = name;
        this.createdTs = currentTimeMillis();
        this.stat = stat;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCreatedTs() {
        return createdTs;
    }

    public Long getStat() {
        return stat;
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
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(createdTs, that.createdTs) && Objects.equals(stat, that.stat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdTs, stat);
    }
}
