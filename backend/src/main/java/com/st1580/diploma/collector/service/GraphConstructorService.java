package com.st1580.diploma.collector.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.repository.AlphaRepository;
import com.st1580.diploma.repository.BetaRepository;
import com.st1580.diploma.repository.CollectorRepository;
import com.st1580.diploma.repository.DeltaRepository;
import com.st1580.diploma.repository.GammaRepository;
import org.springframework.stereotype.Service;

@Service
public class GraphConstructorService {
    private final AlphaRepository alphaRepository;

    private final BetaRepository betaRepository;

    private final GammaRepository gammaRepository;

    private final DeltaRepository deltaRepository;

    @Inject
    public GraphConstructorService(AlphaRepository alphaRepository, BetaRepository betaRepository,
                                   GammaRepository gammaRepository, DeltaRepository deltaRepository) {
        this.alphaRepository = alphaRepository;
        this.betaRepository = betaRepository;
        this.gammaRepository = gammaRepository;
        this.deltaRepository = deltaRepository;
    }

    private CollectorRepository matchRepository(EntityType type) {
        switch (type) {
            case ALPHA:
                return alphaRepository;

            case BETA:
                return betaRepository;

            case GAMMA:
                return gammaRepository;

            case DELTA:
                return deltaRepository;
        }

        throw new IllegalArgumentException("Wrong type parameter " + type);
    }

    public Map<Long, ? extends Entity> getEntitiesByIds(EntityType entitiesType, Set<Long> entitiesIds, long ts) {
        CollectorRepository collectorRepository = matchRepository(entitiesType);
        return collectorRepository.collectAllActiveEntitiesByIds(entitiesIds, ts);
    }


    public Map<EntityType, Map<Long, List<Long>>> getEntitiesNeighborsIds(EntityType entitiesType,
                                                                          Collection<Long> entitiesIds,
                                                                          long ts) {
        CollectorRepository collectorRepository = matchRepository(entitiesType);
        return collectorRepository.collectAllNeighborsIdsByEntities(entitiesIds, ts);
    }

    public Map<EntityType, Map<Long, List<? extends Link>>> getEntitiesNeighbors(EntityType entitiesType,
                                                                                 Collection<Long> entitiesIds,
                                                                                 long ts) {
        CollectorRepository collectorRepository = matchRepository(entitiesType);
        return collectorRepository.collectAllNeighborsByEntities(entitiesIds, ts);
    }


}
