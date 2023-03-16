package com.st1580.diploma.collector.graph.links;

import java.util.Objects;
import java.util.Set;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;

public class LightLink extends AbstractLink {
    public LightLink(LightEntity from, LightEntity to) {
        super(from, to);
    }

    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(getFrom().convertToDto(), getTo().convertToDto(), null);
    }
}
