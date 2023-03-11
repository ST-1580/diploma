package com.st1580.diploma.updater.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.repository.AlphaRepository;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntityEvent;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLinkEvent;
import com.st1580.diploma.updater.caller.AlphaCaller;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.AlphaToBetaEvent;
import com.st1580.diploma.updater.service.AlphaUpdateService;
import org.springframework.stereotype.Service;

@Service
public class AlphaUpdateServiceImpl implements AlphaUpdateService {
    @Inject
    private AlphaCaller alphaCaller;
    @Inject
    private AlphaRepository alphaRepository;
    @Inject
    private AlphaToBetaRepository alphaToBetaRepository;

    @Override
    public void updateAlphaEntity(long tsFrom, long tsTo) {
        List<ExternalAlphaEntityEvent> events = alphaCaller.getAlphaEntityEvents(tsFrom, tsTo);
        List<AlphaEvent> parsedAlphaEvents = events.stream().map(AlphaEvent::new).collect(Collectors.toList());

        alphaRepository.batchInsertNewEvents(parsedAlphaEvents);
    }

    @Override
    public void updateAlphaToBetaLink(long tsFrom, long tsTo) {
        List<ExternalAlphaToBetaLinkEvent> events = alphaCaller.getAlphaToBetaLinkEvents(tsFrom, tsTo);
        List<AlphaToBetaEvent> parsedAlphaToBetaEvents = events.stream().map(AlphaToBetaEvent::new).collect(Collectors.toList());

        alphaToBetaRepository.batchInsertNewEvents(parsedAlphaToBetaEvents);
    }
}
