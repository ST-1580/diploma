package com.st1580.diploma.updater.service.impl;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.entities.AlphaEntity;
import com.st1580.diploma.collector.graph.entities.BetaEntity;
import com.st1580.diploma.collector.repository.BetaRepository;
import com.st1580.diploma.db.tables.Beta;
import com.st1580.diploma.external.alpha.data.AlphaEntityEvent;
import com.st1580.diploma.external.beta.data.BetaEntityEvent;
import com.st1580.diploma.updater.caller.BetaCaller;
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
        List<BetaEntityEvent> events = betaCaller.getBetaEntityEvents(tsFrom, tsTo);
        events.sort(Comparator.comparingLong(BetaEntityEvent::getEventTs));

        for (BetaEntityEvent event : events) {
            switch (event.getType()) {
                case 'c':
                    betaRepository.insert(new BetaEntity(event.getEntity()));
                    break;
                case 'u':
                    betaRepository.update(event.getEntityId(), new BetaEntity(event.getEntity()));
                    break;
                case 'd':
                    betaRepository.delete(event.getEntityId());
                    break;
                default:
                    throw new IllegalArgumentException("Beta event type must be 'c', 'u' or 'd'");
            }
        }
    }
}
