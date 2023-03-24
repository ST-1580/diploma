package com.st1580.diploma.collector.graph.links;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.entities.LightEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.LinkEndDto;

public class LightLink extends AbstractLink {
    public LightLink(LightEntity from, LightEntity to) {
        super(from, to);
    }

    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(
                new LinkEndDto(getFrom().convertToDto().getType(), getFrom().getId()),
                new LinkEndDto(getTo().convertToDto().getType(), getTo().getId())
                , null);
    }
}
