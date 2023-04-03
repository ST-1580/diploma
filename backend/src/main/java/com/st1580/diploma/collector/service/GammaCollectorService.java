package com.st1580.diploma.collector.service;

import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.api.GammaCollectorController;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.repository.GammaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.PolicyType;
import org.springframework.stereotype.Service;

@Service
public class GammaCollectorService extends AbstractCollectorService implements GammaCollectorController {

    private final GammaRepository gammaRepository;

    @Inject
    public GammaCollectorService(GammaRepository gammaRepository) {
        super(gammaRepository);
        this.gammaRepository = gammaRepository;
    }

    @Override
    public GraphDto collectGraph(long entityId, long ts, PolicyType policyType, boolean isLinksLight, boolean isEntitiesLight) {
        final Entity startEntity = new LightEntity(EntityType.GAMMA, entityId);
        return getGraphByPolicy(startEntity, ts, policyType, isLinksLight, isEntitiesLight);
    }
}
