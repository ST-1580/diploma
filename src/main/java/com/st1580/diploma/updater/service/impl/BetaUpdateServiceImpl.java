package com.st1580.diploma.updater.service.impl;

import javax.inject.Inject;

import com.st1580.diploma.collector.repository.BetaRepository;
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
//        List<BetaEntityEvent> events = betaCaller.getBetaEntityEvents(tsFrom, tsTo);
//
//        for (BetaEntityEvent event : events) {
//            switch (event.getType()) {
//                case 'c':
//                    betaRepository.insert(new BetaEntity(event.getEntity()));
//                    break;
//                case 'u':
//                    betaRepository.update(new BetaEntity(event.getEntity()));
//                    break;
//                case 'd':
//                    betaRepository.delete(event.getEntityId());
//                    break;
//                default:
//                    throw new IllegalArgumentException("Beta event type must be 'c', 'u' or 'd'");
//            }
//        }
    }
}
