package com.st1580.diploma.collector.service;

import java.util.List;

import com.st1580.diploma.collector.api.BetaCollectorController;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.BetaEntity;
import com.st1580.diploma.collector.policy.StartEntityPolicy;
import com.st1580.diploma.collector.repository.BetaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import org.springframework.stereotype.Service;

@Service
public class BetaCollectorService extends AbstractCollectorService implements BetaCollectorController {

    private final BetaRepository betaRepository;

    public BetaCollectorService(BetaRepository betaRepository) {
        super(betaRepository);
        this.betaRepository = betaRepository;
    }

    @Override
    public GraphDto collectLightGraph(long betaId) {
        final Entity startEntity = new BetaEntity(betaId);
        return getLightGraphWithPolicy(startEntity, new StartEntityPolicy(EntityType.BETA));
    }

    @Override
    public List<GraphLinkDto> collectEntityLightNeighbors(long betaId) {
        final Entity startEntity = new BetaEntity(betaId);
        return getEntityLightNeighbors(startEntity);
    }
}
