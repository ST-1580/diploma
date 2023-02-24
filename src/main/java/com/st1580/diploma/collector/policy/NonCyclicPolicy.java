package com.st1580.diploma.collector.policy;

import com.st1580.diploma.collector.graph.Graph;
import com.st1580.diploma.collector.graph.entities.LightEntity;

public class NonCyclicPolicy implements Policy {
    @Override
    public boolean canExtendFromLightEntity(Graph g, LightEntity e) {
        return g.getGraphEntities().contains(e);
    }
}
