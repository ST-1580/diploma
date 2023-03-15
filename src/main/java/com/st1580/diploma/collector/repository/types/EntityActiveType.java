package com.st1580.diploma.collector.repository.types;

import java.util.List;

public enum EntityActiveType {
    TRUE, FALSE, CHANGED_TO_TRUE, CHANGED_TO_FALSE, DELETED;

    public static final List<String> trueEntityActiveTypes = List.of(TRUE.name(), CHANGED_TO_TRUE.name());

    public static final List<String> changedEntityActiveTypes =
            List.of(CHANGED_TO_FALSE.name(), CHANGED_TO_TRUE.name(), DELETED.name());
}
