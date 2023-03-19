package com.st1580.diploma.updater.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.repository.GammaRepository;
import com.st1580.diploma.repository.GammaToAlphaRepository;
import com.st1580.diploma.repository.GammaToDeltaRepository;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntityEvent;
import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLinkEvent;
import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLinkEvent;
import com.st1580.diploma.updater.caller.GammaCaller;
import com.st1580.diploma.updater.events.GammaEvent;
import com.st1580.diploma.updater.events.GammaToAlphaEvent;
import com.st1580.diploma.updater.events.GammaToDeltaEvent;
import com.st1580.diploma.updater.service.GammaUpdateService;
import org.springframework.stereotype.Service;

@Service
public class GammaUpdateServiceImpl implements GammaUpdateService {
    @Inject
    private GammaCaller gammaCaller;
    @Inject
    private GammaRepository gammaRepository;
    @Inject
    private GammaToAlphaRepository gammaToAlphaRepository;
    @Inject
    private GammaToDeltaRepository gammaToDeltaRepository;

    @Override
    public void updateGammaEntity(long tsFrom, long tsTo) {
        List<ExternalGammaEntityEvent> events = gammaCaller.getGammaEntityEvents(tsFrom, tsTo);
        List<GammaEvent> parsedGammaEvents = events.stream().map(GammaEvent::new).collect(Collectors.toList());

        gammaRepository.batchInsertNewEvents(parsedGammaEvents);
    }

    @Override
    public void updateGammaToAlphaLink(long tsFrom, long tsTo) {
        List<ExternalGammaToAlphaLinkEvent> events = gammaCaller.getAllGammaToAlphaLinkEvents(tsFrom, tsTo);
        List<GammaToAlphaEvent> parsedGammaToAlphaEvents = events.stream()
                .map(GammaToAlphaEvent::new)
                .collect(Collectors.toList());

        gammaToAlphaRepository.batchInsertNewEvents(parsedGammaToAlphaEvents);
    }

    @Override
    public void updateGammaToDeltaLink(long tsFrom, long tsTo) {
        List<ExternalGammaToDeltaLinkEvent> events = gammaCaller.getAllGammaToDeltaLinkEvents(tsFrom, tsTo);
        List<GammaToDeltaEvent> parsedGammaToDeltaEvents = events.stream()
                .map(GammaToDeltaEvent::new)
                .collect(Collectors.toList());

        gammaToDeltaRepository.batchInsertNewEvents(parsedGammaToDeltaEvents);
    }
}
