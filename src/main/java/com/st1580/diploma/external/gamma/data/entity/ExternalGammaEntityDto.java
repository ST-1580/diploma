package com.st1580.diploma.external.gamma.data.entity;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalGammaEntityDto {
    private final long id;
    private final String unimportantData;
    private final boolean isMaster;

    public ExternalGammaEntityDto(@JsonProperty("id") long id,
                                  @JsonProperty("isMaster") boolean isMaster,
                                  @JsonProperty("unimportant") String unimportantData) {
        this.id = id;
        this.unimportantData = unimportantData;
        this.isMaster = isMaster;
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
        ExternalGammaEntityDto that = (ExternalGammaEntityDto) o;
        return id == that.id && isMaster == that.isMaster && Objects.equals(unimportantData, that.unimportantData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unimportantData, isMaster);
    }

    public String getUnimportantData() {
        return unimportantData;
    }

    public boolean isMaster() {
        return isMaster;
    }

}
