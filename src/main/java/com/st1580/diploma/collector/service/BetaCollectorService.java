package com.st1580.diploma.collector.service;

import java.util.HashMap;
import java.util.List;

import com.st1580.diploma.collector.api.BetaCollectorController;
import com.st1580.diploma.collector.repository.BetaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.entites.BetaEntityDto;
import org.springframework.stereotype.Service;

@Service
public class BetaCollectorService extends AbstractCollectorService implements BetaCollectorController {

    private final BetaRepository betaRepository;

    public BetaCollectorService(BetaRepository betaRepository) {
        super(betaRepository);
        this.betaRepository = betaRepository;
    }

    @Override
    public GraphDto collectAll(long betaId) {
        return null;
    }

    @Override
    public List<GraphLinkDto> collectEntityNeighbors(long betaId) {
        final GraphEntityDto startEntity = new BetaEntityDto(betaId);
        return getEntityNeighbors(startEntity);
    }
}
