package com.st1580.diploma.collector.repository;

import java.util.List;

import com.st1580.diploma.collector.graph.AlphaEntity;
import com.st1580.diploma.collector.graph.GammaEntity;

public interface GammaToAlphaRepository {
    List<GammaEntity> getConnectedGammaEntitiesByAlpha(long alphaId);

    List<AlphaEntity> getConnectedAlphaEntitiesByGamma(long gammaId);
}
