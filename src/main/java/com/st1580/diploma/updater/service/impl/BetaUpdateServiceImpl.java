package com.st1580.diploma.updater.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.repository.BetaRepository;
import com.st1580.diploma.external.beta.data.ExternalBetaEntityEvent;
import com.st1580.diploma.updater.caller.BetaCaller;
import com.st1580.diploma.updater.events.BetaEvent;
import com.st1580.diploma.updater.service.BetaUpdateService;
import org.springframework.stereotype.Service;

@Service
public class BetaUpdateServiceImpl implements BetaUpdateService {
    @Inject
    private BetaCaller betaCaller;
    @Inject
    private BetaRepository betaRepository;

    @Override
    public void updateBetaEntity(long tsFrom, long tsTo) {
        List<ExternalBetaEntityEvent> events = betaCaller.getBetaEntityEvents(tsFrom, tsTo);
        List<BetaEvent> parsedBetaEvents = events.stream().map(BetaEvent::new).collect(Collectors.toList());

        betaRepository.batchInsertNewEvents(parsedBetaEvents);
    }
}
