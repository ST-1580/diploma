package com.st1580.diploma.collector.graph;

import java.util.Objects;

import com.st1580.diploma.collector.service.dto.GraphLinkDto;

public abstract class AbstractLink implements Link {
    private final Entity from;
    private final Entity to;

    public AbstractLink(Entity from, Entity to) {
        this.from = from;
        this.to = to;
    }

    public Entity getFrom() {
        return from;
    }

    public Entity getTo() {
        return to;
    }

    public Entity getEntityWithType(EntityType type) {
        if (type.equals(from.getType())) {
            return from;
        } else if (type.equals(to.getType())) {
            return to;
        }

        throw new IllegalArgumentException("Links has not end with type " + type);
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
