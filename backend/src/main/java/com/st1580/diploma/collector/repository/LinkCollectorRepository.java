package com.st1580.diploma.collector.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.links.LightLink;

public interface LinkCollectorRepository {
    Map<LightLink, ? extends Link> collectAllActiveLinksByEnds(Map<Long, Long> linkEndIds, long ts);

    Map<Long, List<Long>> getConnectedToEntitiesIdsByFromEntitiesIds(Collection<Long> fromIds, long ts);

    Map<Long, List<Long>> getConnectedFromEntitiesIdsByToEntitiesIds(Collection<Long> toIds, long ts);
}
