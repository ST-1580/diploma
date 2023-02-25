package com.st1580.diploma.collector.service;

import java.util.List;

import com.st1580.diploma.collector.api.DeltaCollectorController;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.DeltaEntity;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.policy.StartEntityPolicy;
import com.st1580.diploma.collector.repository.DeltaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import org.springframework.stereotype.Service;

@Service
public class DeltaCollectorService extends AbstractCollectorService implements DeltaCollectorController {

    private final DeltaRepository deltaRepository;

    public DeltaCollectorService(DeltaRepository deltaRepository) {
        super(deltaRepository);
        this.deltaRepository = deltaRepository;
    }

    @Override
    public GraphDto collectLightGraph(long deltaId) {
        final Entity startEntity = new LightEntity(EntityType.DELTA, deltaId);
        return getLightGraphWithPolicy(startEntity, new StartEntityPolicy(EntityType.DELTA));
    }

    @Override
    public List<GraphLinkDto> collectEntityLightNeighbors(long deltaId) {
        final Entity startEntity = new LightEntity(EntityType.DELTA, deltaId);
        return getEntityLightNeighbors(startEntity);
    }
}
