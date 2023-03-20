package com.st1580.diploma.external.beta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.external.beta.data.ExternalBetaEntityEvent;
import com.st1580.diploma.external.beta.data.ExternalBetaEntity;
import com.st1580.diploma.external.delta.data.DeltaEventType;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntity;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntityEvent;
import com.st1580.diploma.external.repository.ExternalServicesRepository;
import org.springframework.stereotype.Service;

@Service
public class BetaService implements BetaServiceApi {
    private final List<ExternalBetaEntityEvent> entityEvents;
    private final Set<Long> activeBetaEntities;
    private final Map<Long, Integer> lastEpoch;

    @Inject
    public BetaService(ExternalServicesRepository externalServicesRepository) {
        this.entityEvents = new ArrayList<>();

        List<ExternalBetaEntity> entities = externalServicesRepository.getAllBetaEntities();
        this.activeBetaEntities = entities.stream()
                .map(ExternalBetaEntity::getId)
                .collect(Collectors.toSet());
        this.lastEpoch = entities.stream().collect(Collectors.toMap(
                ExternalBetaEntity::getId,
                ExternalBetaEntity::getEpoch
        ));
    }

    @Override
    public String createEntity(ExternalBetaEntity newEntity) {
        long newEntityId = newEntity.getId();
        if (activeBetaEntities.contains(newEntityId)) {
            return "Beta entity id " + newEntityId + " is already exist";
        }
        activeBetaEntities.add(newEntityId);
        lastEpoch.put(newEntityId, newEntity.getEpoch());

        entityEvents.add(
                new ExternalBetaEntityEvent(
                        'c',
                        new ExternalBetaEntity(newEntityId, newEntity.getEpoch())
                ));
        return "done";
    }

    @Override
    public String patchEntity(ExternalBetaEntity betaEntity) {
        if (!activeBetaEntities.contains(betaEntity.getId())) {
            return "Beta entity with id " + betaEntity.getId() + " does not exist";
        }
        lastEpoch.put(betaEntity.getId(), betaEntity.getEpoch());

        entityEvents.add(
                new ExternalBetaEntityEvent(
                        'u',
                        new ExternalBetaEntity(betaEntity.getId(), betaEntity.getEpoch())
                ));
        return "done";
    }

    @Override
    public String deleteEntity(long entityId) {
        if (!activeBetaEntities.contains(entityId)) {
            return "Beta entity with id " + entityId + " does not exist";
        }
        activeBetaEntities.remove(entityId);

        entityEvents.add(
                new ExternalBetaEntityEvent(
                        'd',
                        new ExternalBetaEntity(entityId, lastEpoch.get(entityId))
                ));
        lastEpoch.remove(entityId);

        return "done";
    }

    @Override
    public List<ExternalBetaEntityEvent> getBetaEntityEvents(long tsFrom, long tsTo) {
        return entityEvents.stream()
                .filter(event -> tsFrom <= event.getEventTs() && event.getEventTs() < tsTo)
                .sorted(Comparator.comparingLong(ExternalBetaEntityEvent::getEventTs))
                .collect(Collectors.toList());
    }
}
