package com.st1580.diploma.collector.repository.types;

public enum LinkEndActivityType {
    TRUE, FALSE, UNDEFINED;

    public static LinkEndActivityType parseBoolean(Boolean bool) {
        return bool ? TRUE : FALSE;
    }
}
