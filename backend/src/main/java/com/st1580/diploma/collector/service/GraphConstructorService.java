package com.st1580.diploma.collector.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.Entity;
import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.links.LightLink;
import com.st1580.diploma.collector.graph.links.LinkEndIds;
import com.st1580.diploma.collector.graph.links.LinkType;
import com.st1580.diploma.repository.AlphaRepository;
import com.st1580.diploma.repository.AlphaToBetaRepository;
import com.st1580.diploma.repository.BetaRepository;
import com.st1580.diploma.collector.repository.EntityCollectorRepository;
import com.st1580.diploma.repository.DeltaRepository;
import com.st1580.diploma.repository.GammaRepository;
import com.st1580.diploma.repository.GammaToAlphaRepository;
import com.st1580.diploma.repository.GammaToDeltaRepository;
import com.st1580.diploma.collector.repository.LinkCollectorRepository;
import org.springframework.stereotype.Service;

@Service
public class GraphConstructorService {
    private final AlphaRepository alphaRepository;

    private final BetaRepository betaRepository;

    private final GammaRepository gammaRepository;

    private final DeltaRepository deltaRepository;

    private final AlphaToBetaRepository alphaToBetaRepository;

    private final GammaToAlphaRepository gammaToAlphaRepository;

    private final GammaToDeltaRepository gammaToDeltaRepository;

    @Inject
    public GraphConstructorService(AlphaRepository alphaRepository, BetaRepository betaRepository,
                                   GammaRepository gammaRepository, DeltaRepository deltaRepository,
                                   AlphaToBetaRepository alphaToBetaRepository,
                                   GammaToAlphaRepository gammaToAlphaRepository,
                                   GammaToDeltaRepository gammaToDeltaRepository) {
        this.alphaRepository = alphaRepository;
        this.betaRepository = betaRepository;
        this.gammaRepository = gammaRepository;
        this.deltaRepository = deltaRepository;
        this.alphaToBetaRepository = alphaToBetaRepository;
        this.gammaToAlphaRepository = gammaToAlphaRepository;
        this.gammaToDeltaRepository = gammaToDeltaRepository;
    }

    private EntityCollectorRepository matchEntityRepository(EntityType type) {
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

    private LinkCollectorRepository matchLinkRepository(LinkType type) {
        if (new LinkType(EntityType.ALPHA, EntityType.BETA).equals(type)) {
            return alphaToBetaRepository;
        } else if (new LinkType(EntityType.GAMMA, EntityType.ALPHA).equals(type)) {
            return gammaToAlphaRepository;
        } else if (new LinkType(EntityType.GAMMA, EntityType.DELTA).equals(type)) {
            return gammaToDeltaRepository;
        }

        throw new IllegalArgumentException("Wrong type parameter " + type);
    }

    public Map<Long, ? extends Entity> getEntitiesByIds(EntityType entitiesType, Set<Long> entitiesIds, long ts) {
        EntityCollectorRepository entityCollectorRepository = matchEntityRepository(entitiesType);
        return entityCollectorRepository.collectAllActiveEntitiesByIds(entitiesIds, ts);
    }


    public Map<EntityType, Map<Long, List<Long>>> getEntitiesNeighborsIds(EntityType entitiesType,
                                                                          Collection<Long> entitiesIds,
                                                                          long ts) {
        EntityCollectorRepository entityCollectorRepository = matchEntityRepository(entitiesType);
        return entityCollectorRepository.collectAllNeighborsIdsByEntities(entitiesIds, ts);
    }


    public Map<LightLink,? extends Link> getLinksByEnds(LinkType type, Set<LightLink> lightLinks, long ts) {
        LinkCollectorRepository linkCollectorRepository = matchLinkRepository(type);
        List<LinkEndIds> linkEndIds = lightLinks.stream()
                .map(link -> new LinkEndIds(link.getFrom().getId(), link.getTo().getId()))
                .collect(Collectors.toList());
        return linkCollectorRepository.collectAllActiveLinksByEnds(linkEndIds, ts);
    }
}
