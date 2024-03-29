package com.st1580.diploma.updater.service.impl;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.st1580.diploma.repository.AlphaRepository;
import com.st1580.diploma.repository.AlphaToBetaRepository;
import com.st1580.diploma.repository.BetaRepository;
import com.st1580.diploma.repository.DeltaRepository;
import com.st1580.diploma.repository.GammaRepository;
import com.st1580.diploma.repository.GammaToAlphaRepository;
import com.st1580.diploma.repository.GammaToDeltaRepository;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.BetaEvent;
import com.st1580.diploma.updater.events.DeltaEvent;
import com.st1580.diploma.updater.events.GammaEvent;
import com.st1580.diploma.updater.repository.EntityEventRepository;
import com.st1580.diploma.updater.service.LinkCorrectorService;
import org.springframework.stereotype.Service;

@Service
public class LinkCorrectorServiceImpl implements LinkCorrectorService {
    private final AlphaRepository alphaRepository;
    private final BetaRepository betaRepository;
    private final GammaRepository gammaRepository;
    private final DeltaRepository deltaRepository;
    private final AlphaToBetaRepository alphaToBetaRepository;
    private final GammaToAlphaRepository gammaToAlphaRepository;
    private final GammaToDeltaRepository gammaToDeltaRepository;

    @Inject
    public LinkCorrectorServiceImpl(AlphaRepository alphaRepository, BetaRepository betaRepository,
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

    @Override
    public void addLinkEventsByEntityUpdate(long tsFrom, long tsTo) {
        List<Set<AlphaEvent>> batchedAlphaEventsInRange = alphaRepository.getActiveStatusChangedEventsInRange(tsFrom, tsTo);
        List<Set<BetaEvent>> batchedBetaEventsInRange = betaRepository.getActiveStatusChangedEventsInRange(tsFrom, tsTo);
        List<Set<GammaEvent>> batchedGammaEventsInRange = gammaRepository.getActiveStatusChangedEventsInRange(tsFrom, tsTo);
        List<Set<DeltaEvent>> batchedDeltaEventsInRange = deltaRepository.getActiveStatusChangedEventsInRange(tsFrom, tsTo);

        alphaToBetaRepository.addLinkEventsTriggeredByEntitiesUpdate(batchedAlphaEventsInRange, batchedBetaEventsInRange);
        gammaToAlphaRepository.addLinkEventsTriggeredByEntitiesUpdate(batchedGammaEventsInRange, batchedAlphaEventsInRange);
        gammaToDeltaRepository.addLinkEventsTriggeredByEntitiesUpdate(batchedGammaEventsInRange, batchedDeltaEventsInRange);
    }

    @Override
    public void correctLinks(long tsFrom, long tsTo) {
        alphaRepository.correctDependentLinks(tsFrom, tsTo);
        betaRepository.correctDependentLinks(tsFrom, tsTo);
        gammaRepository.correctDependentLinks(tsFrom, tsTo);
        deltaRepository.correctDependentLinks(tsFrom, tsTo);
    }

    @Override
    public void deleteUndefinedLinks(long tsFrom, long tsTo) {
        alphaToBetaRepository.deleteUndefinedLinks(tsFrom, tsTo);
        gammaToAlphaRepository.deleteUndefinedLinks(tsFrom, tsTo);
        gammaToDeltaRepository.deleteUndefinedLinks(tsFrom, tsTo);
    }
}
