package com.st1580.diploma.updater.service.impl;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.entities.AlphaEntity;
import com.st1580.diploma.collector.graph.links.AlphaToBetaLink;
import com.st1580.diploma.collector.repository.AlphaRepository;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.external.alpha.data.AlphaEntityEvent;
import com.st1580.diploma.external.alpha.data.AlphaToBetaLinkEvent;
import com.st1580.diploma.updater.service.AlphaUpdateService;
import com.st1580.diploma.updater.caller.AlphaCaller;
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
        List<AlphaEntityEvent> events = alphaCaller.getAlphaEntityEvents(tsFrom, tsTo);
        events.sort(Comparator.comparingLong(AlphaEntityEvent::getEventTs));

        for (AlphaEntityEvent event : events) {
            switch (event.getType()) {
                case CREATE:
                    alphaRepository.insert(new AlphaEntity(event.getEntity()));
                    break;
                case UPDATE:
                    alphaRepository.update(event.getEntityId(), new AlphaEntity(event.getEntity()));
                    break;
                case REMOVE:
                    alphaRepository.delete(event.getEntityId());
                    break;
            }
        }
    }

    @Override
    public void updateAlphaToBetaLink(long tsFrom, long tsTo) {
        List<AlphaToBetaLinkEvent> events = alphaCaller.getAlphaToBetaLinkEvents(tsFrom, tsTo);
        events.sort(Comparator.comparingLong(AlphaToBetaLinkEvent::getEventTs));

        for (AlphaToBetaLinkEvent event : events) {
            switch (event.getType()) {
                case CREATE:
                    alphaToBetaRepository.insert(new AlphaToBetaLink(event.getLink()));
                    break;
                case UPDATE:
                    alphaToBetaRepository.update(event.getAlphaId(), event.getBetaId(),
                            new AlphaToBetaLink(event.getLink()));
                    break;
                case REMOVE:
                    alphaToBetaRepository.delete(event.getAlphaId(), event.getBetaId());
                    break;
            }
        }
    }
}
