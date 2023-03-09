package com.st1580.diploma.collector.repository.types;

import java.util.List;

public enum EntityActiveType {
    TRUE, FALSE, CHANGED_TO_TRUE, CHANGED_TO_FALSE;

    public static final List<String> trueEntityActiveTypes =
            List.of(EntityActiveType.TRUE.toString(), EntityActiveType.CHANGED_TO_TRUE.toString());
}
