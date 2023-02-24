package com.st1580.diploma.collector.graph.links;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.entities.AlphaEntity;
import com.st1580.diploma.collector.graph.entities.GammaEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;

public class GammaToAlphaLink extends AbstractLink {
    public GammaToAlphaLink(long gammaId, long alphaId) {
        super(new GammaEntity(gammaId), new AlphaEntity(alphaId));
    }

    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(getFrom().convertToDto(), getTo().convertToDto(), null);
    }
}
