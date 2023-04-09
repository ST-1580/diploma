package com.st1580.diploma.updater.events;

public interface LinkEvent {
    long getFromId();

    long getToId();

    boolean isActive();

    long getCreatedTs();
}
