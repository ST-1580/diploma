package com.st1580.diploma.updater.service;

import org.springframework.web.bind.annotation.RequestParam;

public interface GammaUpdateService {

    void updateGammaEntity(long tsFrom, long tsTo);

    void updateGammaToAlphaLink(long tsFrom, long tsTo);

    void updateGammaToDeltaLink(long tsFrom, long tsTo);
}
