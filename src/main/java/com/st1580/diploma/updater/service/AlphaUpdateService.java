package com.st1580.diploma.updater.service;

public interface AlphaUpdateService {
    void updateAlphaEntity(long tsFrom, long tsTo);

    void updateAlphaToBetaLink(long tsFrom, long tsTo);
}
