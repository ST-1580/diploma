package com.st1580.diploma.external.delta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.external.delta.data.ExternalDeltaEntityEvent;
import com.st1580.diploma.external.delta.data.DeltaEventType;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntity;
import com.st1580.diploma.external.repository.ExternalServicesRepository;
import org.springframework.stereotype.Service;

@Service
public class DeltaService implements DeltaServiceApi {
    private final List<ExternalDeltaEntityEvent> entityEvents;
    private final Set<Long> activeDeltaEntities;
    private final Map<Long, String> lastName;

    @Inject
    public DeltaService(ExternalServicesRepository externalServicesRepository) {
        this.entityEvents = new ArrayList<>();

        List<ExternalDeltaEntity> entities = externalServicesRepository.getAllDeltaEntities();
        this.activeDeltaEntities = entities.stream()
                .map(ExternalDeltaEntity::getId)
                .collect(Collectors.toSet());
        this.lastName = entities.stream().collect(Collectors.toMap(
                ExternalDeltaEntity::getId,
                ExternalDeltaEntity::getName
        ));
    }

    @Override
    public String createEntity(ExternalDeltaEntity newEntity) {
        long newEntityId = newEntity.getId();
        if (activeDeltaEntities.contains(newEntityId)) {
            return "Delta entity id " + newEntityId + " is already exist";
        }
        activeDeltaEntities.add(newEntityId);
        lastName.put(newEntityId, newEntity.getName());

        entityEvents.add(
                new ExternalDeltaEntityEvent(
                        DeltaEventType.CREATE,
                        new ExternalDeltaEntity(newEntityId, newEntity.getName())
                ));
        return "done";
    }

    @Override
    public String patchEntity(ExternalDeltaEntity deltaEntity) {
        if (!activeDeltaEntities.contains(deltaEntity.getId())) {
            return "Delta entity with id " + deltaEntity.getId() + " does not exist";
        }
        lastName.put(deltaEntity.getId(), deltaEntity.getName());

        entityEvents.add(
                new ExternalDeltaEntityEvent(
                        DeltaEventType.UPDATE,
                        new ExternalDeltaEntity(deltaEntity.getId(), deltaEntity.getName())
                ));
        return "done";
    }

    @Override
    public String deleteEntity(long entityId) {
        if (!activeDeltaEntities.contains(entityId)) {
            return "Delta entity with id " + entityId + " does not exist";
        }
        activeDeltaEntities.remove(entityId);

        entityEvents.add(
                new ExternalDeltaEntityEvent(
                        DeltaEventType.DELETE,
                        new ExternalDeltaEntity(entityId, lastName.get(entityId))
                ));
        lastName.remove(entityId);

        return "done";
    }

    @Override
    public List<ExternalDeltaEntityEvent> getDeltaEntityEvents(long tsFrom, long tsTo) {
        return entityEvents.stream()
                .filter(event -> tsFrom <= event.getEventTs() && event.getEventTs() < tsTo)
                .sorted(Comparator.comparingLong(ExternalDeltaEntityEvent::getEventTs))
                .collect(Collectors.toList());
    }
}
