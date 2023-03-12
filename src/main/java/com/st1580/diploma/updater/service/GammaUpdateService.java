package com.st1580.diploma.updater.service;

public interface GammaUpdateService {

    void updateGammaEntity(long tsFrom, long tsTo);

    void updateGammaToAlphaLink(long tsFrom, long tsTo);

    void updateGammaToDeltaLink(long tsFrom, long tsTo);
}
