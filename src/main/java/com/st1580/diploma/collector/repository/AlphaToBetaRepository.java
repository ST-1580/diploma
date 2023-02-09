package com.st1580.diploma.collector.repository;
import java.util.List;

import com.st1580.diploma.collector.graph.AlphaEntity;
import com.st1580.diploma.collector.graph.BetaEntity;

public interface AlphaToBetaRepository {

    List<BetaEntity> getConnectedBetaEntitiesByAlpha(long alphaId);

    List<AlphaEntity> getConnectedAlphaEntitiesByBeta(long betaId);
}
