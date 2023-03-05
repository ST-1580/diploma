package com.st1580.diploma.collector.service;

import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.api.DeltaCollectorController;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.repository.DeltaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.PolicyType;
import org.springframework.stereotype.Service;

@Service
public class DeltaCollectorService extends AbstractCollectorService implements DeltaCollectorController {

    private final DeltaRepository deltaRepository;

    @Inject
    public DeltaCollectorService(DeltaRepository deltaRepository) {
        super(deltaRepository);
        this.deltaRepository = deltaRepository;
    }

    @Override
    public GraphDto collectGraph(long entityId, PolicyType policyType, boolean isLinksLight, boolean isEntitiesLight) {
        final Entity startEntity = new LightEntity(EntityType.DELTA, entityId);
        return getGraphByPolicy(startEntity, policyType, isLinksLight, isEntitiesLight);
    }

    @Override
    public List<GraphLinkDto> collectEntityNeighbors(long entityId, boolean isLinksLight) {
        final Entity startEntity = new LightEntity(EntityType.DELTA, entityId);
        return getEntityNeighbors(startEntity, isLinksLight);
    }
}
