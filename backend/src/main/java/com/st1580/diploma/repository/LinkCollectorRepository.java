package com.st1580.diploma.repository;

import java.util.Collection;
import java.util.Map;

import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.links.LightLink;

public interface LinkCollectorRepository {
    Map<LightLink, ? extends Link> collectAllActiveLinksByEnds(Collection<Long> fromIds, Collection<Long> toIds, long ts);
}
