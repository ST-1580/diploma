package com.st1580.diploma.collector.service;

import java.util.HashMap;
import java.util.List;

import com.st1580.diploma.collector.api.DeltaCollectorController;
import com.st1580.diploma.collector.repository.DeltaRepository;
import com.st1580.diploma.collector.service.dto.GraphDto;
import com.st1580.diploma.collector.service.dto.GraphEntityDto;
import com.st1580.diploma.collector.service.dto.GraphLinkDto;
import com.st1580.diploma.collector.service.dto.entites.DeltaEntityDto;
import org.springframework.stereotype.Service;

@Service
public class DeltaCollectorService extends AbstractCollectorService implements DeltaCollectorController {

    private final DeltaRepository deltaRepository;

    public DeltaCollectorService(DeltaRepository deltaRepository) {
        super(deltaRepository);
        this.deltaRepository = deltaRepository;
    }

    @Override
    public GraphDto collectAll(long deltaId) {
        return null;
    }

    @Override
    public List<GraphLinkDto> collectEntityNeighbors(long deltaId) {
        final GraphEntityDto startEntity = new DeltaEntityDto(deltaId, new HashMap<>());
        return getEntityNeighbors(startEntity);
    }
}
