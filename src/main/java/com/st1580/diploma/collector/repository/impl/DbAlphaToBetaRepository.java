package com.st1580.diploma.collector.repository.impl;

import java.util.List;

import com.st1580.diploma.collector.graph.AlphaEntity;
import com.st1580.diploma.collector.graph.BetaEntity;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbAlphaToBetaRepository implements AlphaToBetaRepository {
    @Override
    public List<BetaEntity> getConnectedBetaEntitiesByAlpha(long alphaId) {
        return null;
    }

    @Override
    public List<AlphaEntity> getConnectedAlphaEntitiesByBeta(long betaId) {
        return null;
    }
}
