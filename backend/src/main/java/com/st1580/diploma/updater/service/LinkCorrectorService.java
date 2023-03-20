package com.st1580.diploma.updater.service;

public interface LinkCorrectorService {
    void addLinkEventsByEntityUpdate(long tsFrom, long tsTo);

    void correctLinks(long tsFrom, long tsTo);
}
