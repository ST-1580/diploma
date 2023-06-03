package com.st1580.diploma.collector.graph.links;

import java.util.Objects;

public class LinkEndIds {
    final Long fromEntityId;
    final Long toEntityId;

    public LinkEndIds(Long fromEntityId, Long toEntityId) {
        this.fromEntityId = fromEntityId;
        this.toEntityId = toEntityId;
    }

    public Long getFromEntityId() {
        return fromEntityId;
    }

    public Long getToEntityId() {
        return toEntityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkEndIds linkEndIds = (LinkEndIds) o;
        return Objects.equals(fromEntityId, linkEndIds.fromEntityId) && Objects.equals(toEntityId, linkEndIds.toEntityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromEntityId, toEntityId);
    }
}
