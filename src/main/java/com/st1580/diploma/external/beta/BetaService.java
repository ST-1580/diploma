package com.st1580.diploma.external.beta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.external.beta.data.BetaEntityEvent;
import com.st1580.diploma.external.beta.data.ExternalBetaEntity;
import org.springframework.stereotype.Service;

@Service
public class BetaService implements BetaServiceApi {
    private final List<BetaEntityEvent> entityEvents;
    private final Set<Long> idState;

    @Inject
    public BetaService(BetaInitHelper helper) {
        this.entityEvents = new ArrayList<>();
        this.idState = new HashSet<>(helper.getAllEntityIds());
    }

    @Override
    public String createEntity(ExternalBetaEntity newEntity) {
        if (idState.contains(newEntity.getId())) {
            return "Beta entity with id " + newEntity.getId() + " is already exist";
        }
        idState.add(newEntity.getId());

        entityEvents.add(
                new BetaEntityEvent(
                        'c',
                        newEntity.getId(),
                        newEntity
                ));
        return "done";
    }

    @Override
    public String patchEntity(ExternalBetaEntity betaEntity) {
        if (!idState.contains(betaEntity.getId())) {
            return "Beta entity with id " + betaEntity.getId() + " does not exist";
        }

        entityEvents.add(
                new BetaEntityEvent(
                        'u',
                        betaEntity.getId(),
                        betaEntity
                ));
        return "done";
    }

    @Override
    public String deleteEntity(long entityId) {
        if (!idState.contains(entityId)) {
            return "Beta entity with id " + entityId + " does not exist";
        }
        idState.remove(entityId);

        entityEvents.add(
                new BetaEntityEvent(
                        'd',
                        entityId,
                        null
                ));
        return "done";
    }

    @Override
    public List<BetaEntityEvent> getBetaEntityEvents(long tsFrom, long tsTo) {
        return entityEvents.stream()
                .filter(event -> tsFrom <= event.getEventTs() && event.getEventTs() < tsTo)
                .sorted(Comparator.comparingLong(BetaEntityEvent::getEventTs))
                .collect(Collectors.toList());
    }
}
