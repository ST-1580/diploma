package com.st1580.diploma.collector.service;

import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.api.BetaCollectorController;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.repository.BetaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.PolicyType;
import org.springframework.stereotype.Service;

@Service
public class BetaCollectorService extends AbstractCollectorService implements BetaCollectorController {

    private final BetaRepository betaRepository;

    @Inject
    public BetaCollectorService(BetaRepository betaRepository) {
        super(betaRepository);
        this.betaRepository = betaRepository;
    }

    @Override
    public GraphDto collectGraph(long entityId, long ts,  PolicyType policyType, boolean isLinksLight, boolean isEntitiesLight) {
        final Entity startEntity = new LightEntity(EntityType.BETA, entityId);
        return getGraphByPolicy(startEntity, ts, policyType, isLinksLight, isEntitiesLight);
    }
}
