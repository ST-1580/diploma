package com.st1580.diploma.updater.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.entities.DeltaEntity;
import com.st1580.diploma.collector.graph.entities.GammaEntity;
import com.st1580.diploma.collector.repository.DeltaRepository;
import com.st1580.diploma.external.delta.data.DeltaEntityEvent;
import com.st1580.diploma.external.delta.data.DeltaEventType;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntity;
import com.st1580.diploma.updater.caller.DeltaCaller;
import com.st1580.diploma.updater.service.DeltaUpdateService;
import org.springframework.stereotype.Service;

@Service
public class DeltaUpdateServiceImpl implements DeltaUpdateService {
    @Inject
    private DeltaCaller deltaCaller;
    @Inject
    private DeltaRepository deltaRepository;

    @Override
    public void updateDeltaEntity(long fromTs, long toTs) {
//        Map<DeltaEventType, List<DeltaEntityEvent>> events = deltaCaller.getAllDeltaEvents(fromTs, toTs);
//
//        for (DeltaEventType eventType : events.keySet()) {
//            List<DeltaEntity> entities = events.get(eventType).stream()
//                    .map(event -> new DeltaEntity(event.getDeltaId(), event.getName()))
//                    .collect(Collectors.toList());
//
//            switch (eventType) {
//                case CREATE:
//                    deltaRepository.insert(entities);
//                    break;
//                case UPDATE:
//                    entities.forEach(entity -> deltaRepository.update(entity));
//                    break;
//                case DELETE:
//                    List<Long> ids = entities.stream().map(DeltaEntity::getId).collect(Collectors.toList());
//                    deltaRepository.delete(ids);
//            }
//        }
    }
}
