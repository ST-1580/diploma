package com.st1580.diploma.collector.repository.impl;

import java.util.List;

import com.st1580.diploma.collector.graph.DeltaEntity;
import com.st1580.diploma.collector.graph.GammaEntity;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbGammaToDeltaRepository implements GammaToDeltaRepository {
    @Override
    public List<GammaEntity> getConnectedGammaEntitiesByDelta(long deltaId) {
        return null;
    }

    @Override
    public List<DeltaEntity> getConnectedDeltaEntitiesByGamma(long gammaId) {
        return null;
    }
}
