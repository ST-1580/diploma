package com.st1580.diploma.updater.events;

import java.util.Objects;

import com.st1580.diploma.repository.types.EntityActiveType;

public interface EntityEvent {
    long getEntityId();

    EntityActiveType getType();

    long getCreatedTs();
}
