package com.st1580.diploma.collector.graph.links;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.entities.DeltaEntity;
import com.st1580.diploma.collector.graph.entities.GammaEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;

public class GammaToDeltaLink extends AbstractLink {
    public GammaToDeltaLink(GammaEntity from, DeltaEntity to) {
        super(from, to);
    }

    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(getFrom().convertToDto(), getTo().convertToDto(), null);
    }
}
