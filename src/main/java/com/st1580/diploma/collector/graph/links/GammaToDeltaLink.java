package com.st1580.diploma.collector.graph.links;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.entities.DeltaEntity;
import com.st1580.diploma.collector.graph.entities.GammaEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.db.tables.records.GammaToDeltaRecord;

public class GammaToDeltaLink extends AbstractLink {
    private final long gammaId;
    private final long deltaId;
    public GammaToDeltaLink(long gammaId, long deltaId) {
        super(new GammaEntity(gammaId), new DeltaEntity(deltaId));
        this.gammaId = gammaId;
        this.deltaId = deltaId;
    }

    public GammaToDeltaLink(GammaToDeltaRecord record) {
        super(new GammaEntity(record.getGammaId()), new DeltaEntity(record.getDeltaId()));
        this.gammaId = record.getGammaId();
        this.deltaId = record.getDeltaId();
    }

    public long getGammaId() {
        return gammaId;
    }

    public long getDeltaId() {
        return deltaId;
    }

    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(getFrom().convertToDto(), getTo().convertToDto(), null);
    }
}
