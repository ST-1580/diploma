package com.st1580.diploma.collector.graph;

import java.util.Objects;

import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;

public abstract class AbstractLink implements Link {
    private final LightEntity from;
    private final LightEntity to;

    public AbstractLink(LightEntity from, LightEntity to) {
        this.from = from;
        this.to = to;
    }

    public LightEntity getFrom() {
        return from;
    }

    public LightEntity getTo() {
        return to;
    }

    public LightEntity getEntityWithType(EntityType type) {
        if (type.equals(from.getType())) {
            return from;
        } else if (type.equals(to.getType())) {
            return to;
        }

        throw new IllegalArgumentException("Link has not end with type " + type);
    }

    public abstract GraphLinkDto convertToDto();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractLink link = (AbstractLink) o;
        return Objects.equals(from, link.from) && Objects.equals(to, link.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
