package com.st1580.diploma.collector.policy;

import com.st1580.diploma.collector.graph.Graph;
import com.st1580.diploma.collector.graph.entities.LightEntity;

public interface Policy {
    boolean canExtendFromLightEntity(Graph g, LightEntity e);
}
