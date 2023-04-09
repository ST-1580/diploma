package com.st1580.diploma.repository.types;

public enum LinkEndActivityType {
    TRUE, FALSE, UNDEFINED;

    public static LinkEndActivityType parseBoolean(Boolean bool) {
        return bool ? TRUE : FALSE;
    }

    public static LinkEndActivityType getTypeByEndStatus(EntityActiveType type) {
        return EntityActiveType.trueEntityActiveTypes.contains(type.name()) ?
                LinkEndActivityType.TRUE : LinkEndActivityType.FALSE;
    }
}
