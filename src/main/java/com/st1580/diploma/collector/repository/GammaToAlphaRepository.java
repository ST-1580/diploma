package com.st1580.diploma.collector.repository;

import java.util.List;

public interface GammaToAlphaRepository {
    List<Long> getConnectedGammaEntitiesByAlpha(long alphaId);

    List<Long> getConnectedAlphaEntitiesByGamma(long gammaId);
}
