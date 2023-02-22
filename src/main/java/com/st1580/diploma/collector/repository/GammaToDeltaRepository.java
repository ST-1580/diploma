package com.st1580.diploma.collector.repository;

import java.util.List;

public interface GammaToDeltaRepository {
    List<Long> getConnectedGammaEntitiesByDelta(long deltaId);

    List<Long> getConnectedDeltaEntitiesByGamma(long gammaId);
}
