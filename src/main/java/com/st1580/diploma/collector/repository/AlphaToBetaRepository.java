package com.st1580.diploma.collector.repository;

import java.util.List;

public interface AlphaToBetaRepository {

    List<Long> getConnectedBetaEntitiesByAlpha(long alphaId);

    List<Long> getConnectedAlphaEntitiesByBeta(long betaId);
}
