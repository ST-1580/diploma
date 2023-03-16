package com.st1580.diploma.collector.repository.types;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public enum EntityActiveType {
    TRUE, FALSE, CHANGED_TO_TRUE, CHANGED_TO_FALSE, DELETED;

    public static final Set<String> trueEntityActiveTypes = Set.of(TRUE.name(), CHANGED_TO_TRUE.name());

    public static final Set<String> changedEntityActiveTypes =
            Set.of(CHANGED_TO_FALSE.name(), CHANGED_TO_TRUE.name(), DELETED.name());
}
