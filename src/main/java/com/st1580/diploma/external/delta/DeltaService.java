package com.st1580.diploma.external.delta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.external.beta.data.BetaEntityEvent;
import com.st1580.diploma.external.delta.data.DeltaEntityEvent;
import com.st1580.diploma.external.delta.data.DeltaEventType;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntity;
import org.springframework.stereotype.Service;

@Service
public class DeltaService implements DeltaServiceApi {
    private final List<DeltaEntityEvent> entityEvents;
    private final Set<Long> idState;

    @Inject
    public DeltaService(DeltaInitHelper helper) {
        this.entityEvents = new ArrayList<>();
        this.idState = new HashSet<>(helper.getAllDeltaEntities());
    }

    @Override
    public String createEntity(ExternalDeltaEntity newEntity) {
        if (idState.contains(newEntity.getId())) {
            return "Delta entity with id " + newEntity.getId() + " is already exist";
        }
        idState.add(newEntity.getId());

        entityEvents.add(
                new DeltaEntityEvent(
                        DeltaEventType.CREATE,
                        newEntity.getId(),
                        newEntity.getName()
                ));
        return "done";
    }

    @Override
    public String patchEntity(ExternalDeltaEntity deltaEntity) {
        if (!idState.contains(deltaEntity.getId())) {
            return "Delta entity with id " + deltaEntity.getId() + " does not exist";
        }

        entityEvents.add(
                new DeltaEntityEvent(
                        DeltaEventType.UPDATE,
                        deltaEntity.getId(),
                        deltaEntity.getName()
                ));
        return "done";
    }

    @Override
    public String deleteEntity(long entityId) {
        if (!idState.contains(entityId)) {
            return "Delta entity with id " + entityId + " does not exist";
        }
        idState.remove(entityId);

        entityEvents.add(
                new DeltaEntityEvent(
                        DeltaEventType.DELETE,
                        entityId,
                        null
                ));
        return "done";
    }

    @Override
    public Map<DeltaEventType, List<DeltaEntityEvent>> getDeltaEntityEvents(long tsFrom, long tsTo) {
        return entityEvents.stream()
                .filter(event -> tsFrom <= event.getEventTs() && event.getEventTs() < tsTo)
                .sorted(Comparator.comparingLong(DeltaEntityEvent::getEventTs))
                .collect(Collectors.groupingBy(DeltaEntityEvent::getType));
    }
}
