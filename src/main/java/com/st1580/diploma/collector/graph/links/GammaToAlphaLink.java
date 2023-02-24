package com.st1580.diploma.collector.graph.links;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.entities.AlphaEntity;
import com.st1580.diploma.collector.graph.entities.GammaEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.db.tables.records.GammaToAlphaRecord;

public class GammaToAlphaLink extends AbstractLink {
    private final long gammaId;
    private final long alphaId;

    public GammaToAlphaLink(long gammaId, long alphaId) {
        super(new GammaEntity(gammaId), new AlphaEntity(alphaId));
        this.alphaId = alphaId;
        this.gammaId = gammaId;
    }

    public GammaToAlphaLink(GammaToAlphaRecord record) {
        super(new GammaEntity(record.getGammaId()), new AlphaEntity(record.getAlphaId()));
        this.alphaId = record.getAlphaId();
        this.gammaId = record.getGammaId();
    }

    public long getGammaId() {
        return gammaId;
    }

    public long getAlphaId() {
        return alphaId;
    }

    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(getFrom().convertToDto(), getTo().convertToDto(), null);
    }
}
