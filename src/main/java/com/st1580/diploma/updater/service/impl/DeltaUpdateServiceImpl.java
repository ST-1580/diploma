package com.st1580.diploma.updater.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.repository.DeltaRepository;
import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntityEvent;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntityEvent;
import com.st1580.diploma.updater.caller.DeltaCaller;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.DeltaEvent;
import com.st1580.diploma.updater.service.DeltaUpdateService;
import org.springframework.stereotype.Service;

@Service
public class DeltaUpdateServiceImpl implements DeltaUpdateService {
    @Inject
    private DeltaCaller deltaCaller;
    @Inject
    private DeltaRepository deltaRepository;

    @Override
    public void updateDeltaEntity(long tsFrom, long tsTo) {
        List<ExternalDeltaEntityEvent> events = deltaCaller.getAllDeltaEvents(tsFrom, tsTo);
        List<DeltaEvent> parsedDeltaEvents = events.stream().map(DeltaEvent::new).collect(Collectors.toList());

        deltaRepository.batchInsertNewEvents(parsedDeltaEvents);
    }
}
