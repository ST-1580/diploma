package com.st1580.diploma.collector.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.entities.AlphaEntity;
import com.st1580.diploma.collector.graph.links.AlphaToBetaLink;

public interface AlphaToBetaRepository {
    Map<Long, List<Long>> getConnectedBetaEntitiesIdsByAlphaIds(Collection<Long> alphaIds);
    Map<Long, List<AlphaToBetaLink>> getConnectedBetaEntitiesByAlphaIds(Collection<Long> alphaIds);
    Map<Long, List<Long>> getConnectedAlphaEntitiesIdsByBetaIds(Collection<Long> betaIds);
    Map<Long, List<AlphaToBetaLink>> getConnectedAlphaEntitiesByBetaIds(Collection<Long> betaIds);
    void insert(AlphaToBetaLink link);
    void update(long alphaId, long betaId, AlphaToBetaLink link);
    void delete(long alphaId, long betaId);
    List<AlphaToBetaLink> getAllLinks();
}
