package com.st1580.diploma.collector.policy;

import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphDto;

public interface Policy {
    boolean canExtendFromEntity(GraphDto g, GraphEntityDto e);
}
