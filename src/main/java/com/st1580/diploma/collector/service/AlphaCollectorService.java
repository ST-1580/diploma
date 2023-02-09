package com.st1580.diploma.collector.service;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.api.AlphaCollectorController;
import com.st1580.diploma.collector.repository.AlphaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.entites.AlphaEntityDto;
import org.springframework.stereotype.Service;

@Service
public class AlphaCollectorService extends AbstractCollectorService implements AlphaCollectorController {

    private final AlphaRepository alphaRepository;

    @Inject
    public AlphaCollectorService(AlphaRepository alphaRepository) {
        super(alphaRepository);
        this.alphaRepository = alphaRepository;
    }

    @Override
    public GraphDto collectAll(long alphaId) {
        return null;
    }

    @Override
    public List<GraphLinkDto> collectEntityNeighbors(long alphaId) {
        final GraphEntityDto startEntity = new AlphaEntityDto(alphaId, new HashMap<>());
        return getEntityNeighbors(startEntity);
    }
}
