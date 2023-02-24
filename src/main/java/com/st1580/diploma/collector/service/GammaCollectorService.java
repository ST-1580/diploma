package com.st1580.diploma.collector.service;

import java.util.List;

import com.st1580.diploma.collector.api.GammaCollectorController;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.GammaEntity;
import com.st1580.diploma.collector.policy.StartEntityPolicy;
import com.st1580.diploma.collector.repository.GammaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import org.springframework.stereotype.Service;

@Service
public class GammaCollectorService extends AbstractCollectorService implements GammaCollectorController {

    private final GammaRepository gammaRepository;

    public GammaCollectorService(GammaRepository gammaRepository) {
        super(gammaRepository);
        this.gammaRepository = gammaRepository;
    }

    @Override
    public GraphDto collectLightGraph(long gammaId) {
        final Entity startEntity = new GammaEntity(gammaId);
        return getLightGraphWithPolicy(startEntity, new StartEntityPolicy(EntityType.GAMMA));
    }

    @Override
    public List<GraphLinkDto> collectEntityLightNeighbors(long gammaId) {
        final Entity startEntity = new GammaEntity(gammaId);
        return getEntityLightNeighbors(startEntity);
    }
}
