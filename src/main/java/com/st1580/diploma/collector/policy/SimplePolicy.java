package com.st1580.diploma.collector.policy;

import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphDto;

public class SimplePolicy implements Policy {
    @Override
    public boolean canExtendFromEntity(GraphDto g, GraphEntityDto e) {
        return g.getEntities().contains(e);
    }
}
