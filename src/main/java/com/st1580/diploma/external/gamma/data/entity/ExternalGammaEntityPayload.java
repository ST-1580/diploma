package com.st1580.diploma.external.gamma.data.entity;

import java.util.Objects;

public class ExternalGammaEntityPayload {
    private final boolean isMaster;
    private final String unimportantData;

    public ExternalGammaEntityPayload(boolean isMaster, String unimportantData) {
        this.isMaster = isMaster;
        this.unimportantData = unimportantData;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public String getUnimportantData() {
        return unimportantData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalGammaEntityPayload that = (ExternalGammaEntityPayload) o;
        return isMaster == that.isMaster && Objects.equals(unimportantData, that.unimportantData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isMaster, unimportantData);
    }
}
