package com.st1580.diploma.collector.service;

import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.api.AlphaCollectorController;
import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.repository.AlphaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.PolicyType;
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
    public GraphDto collectGraph(long entityId, long ts, PolicyType policyType, boolean isLinksLight, boolean isEntitiesLight) {
        final Entity startEntity = new LightEntity(EntityType.ALPHA, entityId);
        return getGraphByPolicy(startEntity, ts, policyType, isLinksLight, isEntitiesLight);
    }
}
