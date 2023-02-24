package com.st1580.diploma.collector.graph.links;

import java.util.Objects;

import com.st1580.diploma.collector.graph.AbstractLink;
import com.st1580.diploma.collector.graph.entities.AlphaEntity;
import com.st1580.diploma.collector.graph.entities.BetaEntity;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.db.tables.records.AlphaToBetaRecord;

public class AlphaToBetaLink extends AbstractLink {
    private final long alphaId;

    private final long betaId;

    public AlphaToBetaLink(long alphaId, long betaId) {
        super(new AlphaEntity(alphaId), new BetaEntity(betaId));
        this.alphaId = alphaId;
        this.betaId = betaId;
    }

    public AlphaToBetaLink(AlphaToBetaRecord record) {
        super(new AlphaEntity(record.getAlphaId()), new BetaEntity(record.getBetaId()));
        this.alphaId = record.getAlphaId();
        this.betaId = record.getBetaId();
    }

    @Override
    public GraphLinkDto convertToDto() {
        return new GraphLinkDto(getFrom().convertToDto(), getTo().convertToDto(), null);
    }

    public long getAlphaId() {
        return alphaId;
    }

    public long getBetaId() {
        return betaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AlphaToBetaLink that = (AlphaToBetaLink) o;
        return alphaId == that.alphaId && betaId == that.betaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), alphaId, betaId);
    }
}
