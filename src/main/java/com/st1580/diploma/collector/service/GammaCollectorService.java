package com.st1580.diploma.collector.service;

import java.util.HashMap;
import java.util.List;

import com.st1580.diploma.collector.api.GammaCollectorController;
import com.st1580.diploma.collector.repository.DeltaRepository;
import com.st1580.diploma.collector.repository.GammaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.entites.DeltaEntityDto;
import com.st1580.diploma.collector.service.dto.entites.GammaEntityDto;
import org.springframework.stereotype.Service;

@Service
public class GammaCollectorService extends AbstractCollectorService implements GammaCollectorController {

    private final GammaRepository gammaRepository;

    public GammaCollectorService(GammaRepository gammaRepository) {
        super(gammaRepository);
        this.gammaRepository = gammaRepository;
    }

    @Override
    public GraphDto collectAll(long gammaId) {
        return null;
    }

    @Override
    public List<GraphLinkDto> collectEntityNeighbors(long gammaId) {
        final GraphEntityDto startEntity = new GammaEntityDto(gammaId, new HashMap<>());
        return getEntityNeighbors(startEntity);
    }
}
