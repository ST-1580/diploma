package com.st1580.diploma.collector.service;

import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.api.AlphaCollectorController;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.AlphaEntity;
import com.st1580.diploma.collector.policy.StartEntityPolicy;
import com.st1580.diploma.collector.repository.AlphaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import org.springframework.stereotype.Service;

@Service
public class AlphaCollectorService extends AbstractCollectorService implements AlphaCollectorController {

    private final AlphaRepository alphaRepository;

    @Inject
    public AlphaCollectorService(AlphaRepository alphaRepository) {
        super(alphaRepository);
        this.alphaRepository = alphaRepository;
    }

    @Override
    public GraphDto collectLightGraph(long alphaId) {
        final Entity startEntity = new AlphaEntity(alphaId);
        return getLightGraphWithPolicy(startEntity, new StartEntityPolicy(EntityType.ALPHA));
    }

    @Override
    public List<GraphLinkDto> collectEntityLightNeighbors(long alphaId) {
        final Entity startEntity = new AlphaEntity(alphaId);
        return getEntityLightNeighbors(startEntity);
    }
}
