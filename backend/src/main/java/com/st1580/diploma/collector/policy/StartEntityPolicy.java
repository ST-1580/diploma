package com.st1580.diploma.collector.policy;

import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Graph;
import com.st1580.diploma.collector.graph.entities.LightEntity;

public class StartEntityPolicy implements Policy {
    private final EntityType startedType;

    public StartEntityPolicy(EntityType startedType) {
        this.startedType = startedType;
    }

    @Override
    public boolean canExtendFromLightEntity(Graph g, LightEntity e) {
        return g.getGraphEntities().isEmpty() || e.getType() != startedType;
    }
}
