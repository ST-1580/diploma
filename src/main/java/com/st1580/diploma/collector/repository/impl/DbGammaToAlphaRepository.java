package com.st1580.diploma.collector.repository.impl;

import java.util.List;

import com.st1580.diploma.collector.graph.AlphaEntity;
import com.st1580.diploma.collector.graph.GammaEntity;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbGammaToAlphaRepository implements GammaToAlphaRepository {
    @Override
    public List<GammaEntity> getConnectedGammaEntitiesByAlpha(long alphaId) {
        return null;
    }

    @Override
    public List<AlphaEntity> getConnectedAlphaEntitiesByGamma(long gammaId) {
        return null;
    }
}
