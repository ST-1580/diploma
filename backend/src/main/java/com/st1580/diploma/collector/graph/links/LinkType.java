package com.st1580.diploma.collector.graph.links;

import java.util.Objects;
import java.util.Set;

import com.st1580.diploma.collector.graph.EntityType;

public class LinkType {
    private final EntityType typeFrom;
    private final EntityType typeTo;

    public static final Set<LinkType> possibleLinks = Set.of(
            new LinkType(EntityType.ALPHA, EntityType.BETA),
            new LinkType(EntityType.GAMMA, EntityType.ALPHA),
            new LinkType(EntityType.GAMMA, EntityType.DELTA)
    );

    public LinkType(EntityType typeFrom, EntityType typeTo) {
        this.typeFrom = typeFrom;
        this.typeTo = typeTo;
    }

    public EntityType getTypeFrom() {
        return typeFrom;
    }

    public EntityType getTypeTo() {
        return typeTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkType that = (LinkType) o;
        return typeFrom == that.typeFrom && typeTo == that.typeTo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeFrom, typeTo);
    }
}
