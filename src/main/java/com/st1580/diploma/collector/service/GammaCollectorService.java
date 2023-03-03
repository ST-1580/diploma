package com.st1580.diploma.collector.service;

import java.util.List;

import com.st1580.diploma.collector.api.GammaCollectorController;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.policy.StartEntityPolicy;
import com.st1580.diploma.collector.repository.GammaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.PolicyType;
import org.springframework.stereotype.Service;

@Service
public class GammaCollectorService extends AbstractCollectorService implements GammaCollectorController {

    private final GammaRepository gammaRepository;

    public GammaCollectorService(GammaRepository gammaRepository) {
        super(gammaRepository);
        this.gammaRepository = gammaRepository;
    }

    @Override
    public GraphDto collectGraph(long entityId, PolicyType policyType, boolean isLinksLight, boolean isEntitiesLight) {
        final Entity startEntity = new LightEntity(EntityType.GAMMA, entityId);
        return getGraphByPolicy(startEntity, policyType, isLinksLight, isEntitiesLight);
    }

    @Override
    public List<GraphLinkDto> collectEntityNeighbors(long entityId, boolean isLinksLight) {
        final Entity startEntity = new LightEntity(EntityType.GAMMA, entityId);
        return getEntityNeighbors(startEntity, isLinksLight);
    }
}
