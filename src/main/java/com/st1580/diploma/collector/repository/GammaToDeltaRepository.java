package com.st1580.diploma.collector.repository;

import java.util.List;

import com.st1580.diploma.collector.graph.DeltaEntity;
import com.st1580.diploma.collector.graph.GammaEntity;

public interface GammaToDeltaRepository {
    List<GammaEntity> getConnectedGammaEntitiesByDelta(long deltaId);

    List<DeltaEntity> getConnectedDeltaEntitiesByGamma(long gammaId);
}
