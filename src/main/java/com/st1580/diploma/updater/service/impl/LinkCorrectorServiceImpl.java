package com.st1580.diploma.updater.service.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.st1580.diploma.collector.repository.AlphaRepository;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.collector.repository.BetaRepository;
import com.st1580.diploma.collector.repository.DeltaRepository;
import com.st1580.diploma.collector.repository.GammaRepository;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.BetaEvent;
import com.st1580.diploma.updater.events.DeltaEvent;
import com.st1580.diploma.updater.events.GammaEvent;
import com.st1580.diploma.updater.service.LinkCorrectorService;
import org.springframework.stereotype.Service;

@Service
public class LinkCorrectorServiceImpl implements LinkCorrectorService {
    @Inject
    private AlphaRepository alphaRepository;
    @Inject
    private BetaRepository betaRepository;
    @Inject
    private GammaRepository gammaRepository;
    @Inject
    private DeltaRepository deltaRepository;
    @Inject
    private AlphaToBetaRepository alphaToBetaRepository;
    @Inject
    private GammaToAlphaRepository gammaToAlphaRepository;
    @Inject
    private GammaToDeltaRepository gammaToDeltaRepository;

    @Override
    public void addLinkEventsByEntityUpdate(long tsFrom, long tsTo) {
        List<List<AlphaEvent>> batchedAlphaEventsInRange = alphaRepository.getActiveStatusChangedEventsInRange(tsFrom, tsTo);
        List<List<BetaEvent>> batchedBetaEventsInRange = betaRepository.getActiveStatusChangedEventsInRange(tsFrom, tsTo);
        List<List<GammaEvent>> batchedGammaEventsInRange = gammaRepository.getActiveStatusChangedEventsInRange(tsFrom, tsTo);
        List<List<DeltaEvent>> batchedDeltaEventsInRange = deltaRepository.getActiveStatusChangedEventsInRange(tsFrom, tsTo);

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
}
