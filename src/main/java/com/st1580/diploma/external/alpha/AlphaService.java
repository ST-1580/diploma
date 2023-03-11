package com.st1580.diploma.external.alpha;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.external.alpha.data.link.AlphaBetaId;
import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntityEvent;
import com.st1580.diploma.external.alpha.data.AlphaEventType;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLinkEvent;
import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntity;
import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntityDto;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLink;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLinkDto;
import com.st1580.diploma.external.repository.ExternalServicesRepository;
import org.springframework.stereotype.Service;

@Service
public class AlphaService implements AlphaServiceApi {
    private final List<ExternalAlphaEntityEvent> entityEvents;
    private final List<ExternalAlphaToBetaLinkEvent> alphaToBetaLinkEvents;
    private final Set<Long> activeAlphaEntities;
    private final Set<Long> disableAlphaEntities;
    private final Map<Long, String> lastName;
    private final Set<AlphaBetaId> activeAlphaToBetaLinks;
    private final Set<AlphaBetaId> disableAlphaToBetaLinks;
    private final Map<AlphaBetaId, String> lastHash;

    @Inject
    public AlphaService(ExternalServicesRepository externalServicesRepository) {
        this.entityEvents = new ArrayList<>();
        this.alphaToBetaLinkEvents = new ArrayList<>();

        List<ExternalAlphaEntity> entities = externalServicesRepository.getAllAlphaEntities();
        this.activeAlphaEntities = entities.stream()
                .filter(ExternalAlphaEntity::isActive)
                .map(ExternalAlphaEntity::getId)
                .collect(Collectors.toSet());
        this.disableAlphaEntities = entities.stream()
                .filter(Predicate.not(ExternalAlphaEntity::isActive))
                .map(ExternalAlphaEntity::getId)
                .collect(Collectors.toSet());
        this.lastName = entities.stream().collect(Collectors.toMap(
                ExternalAlphaEntity::getId,
                ExternalAlphaEntity::getName
        ));

        List<ExternalAlphaToBetaLink> links = externalServicesRepository.getAllAlphaToBetaLinks();
        this.activeAlphaToBetaLinks = links.stream()
                .filter(ExternalAlphaToBetaLink::isActive)
                .map(link -> new AlphaBetaId(link.getAlphaId(), link.getBetaId()))
                .collect(Collectors.toSet());
        this.disableAlphaToBetaLinks = links.stream()
                .filter(Predicate.not(ExternalAlphaToBetaLink::isActive))
                .map(link -> new AlphaBetaId(link.getAlphaId(), link.getBetaId()))
                .collect(Collectors.toSet());
        this.lastHash = links.stream().collect(Collectors.toMap(
                link -> new AlphaBetaId(link.getAlphaId(), link.getBetaId()),
                ExternalAlphaToBetaLink::getHash
        ));
    }

    @Override
    public String createEntity(ExternalAlphaEntityDto newEntity) {
        long newEntityId = newEntity.getId();
        if (activeAlphaEntities.contains(newEntityId) || disableAlphaEntities.contains(newEntityId)) {
            return "Alpha entity id " + newEntityId + " is already exist";
        }
        activeAlphaEntities.add(newEntityId);
        lastName.put(newEntityId, newEntity.getName());

        entityEvents.add(
                new ExternalAlphaEntityEvent(
                    AlphaEventType.CREATE,
                    new ExternalAlphaEntity(newEntityId, newEntity.getName(), true)
                ));
        return "done";
    }

    @Override
    public String patchEntity(ExternalAlphaEntityDto alphaEntity) {
        if (!activeAlphaEntities.contains(alphaEntity.getId())) {
            return "Alpha entity with id " + alphaEntity.getId() + " does not exist or does not active";
        }
        lastName.put(alphaEntity.getId(), alphaEntity.getName());

        entityEvents.add(
                new ExternalAlphaEntityEvent(
                        AlphaEventType.UPDATE,
                        new ExternalAlphaEntity(alphaEntity.getId(), alphaEntity.getName(), true)
                ));
        return "done";
    }

    @Override
    public String switchEntity(long entityId) {
        if (activeAlphaEntities.contains(entityId)) {
            activeAlphaEntities.remove(entityId);
            disableAlphaEntities.add(entityId);

            entityEvents.add(
                    new ExternalAlphaEntityEvent(
                            AlphaEventType.DISABLE,
                            new ExternalAlphaEntity(entityId, lastName.get(entityId), false)
                    ));
        } else if (disableAlphaEntities.contains(entityId)) {
            activeAlphaEntities.add(entityId);
            disableAlphaEntities.remove(entityId);

            entityEvents.add(
                    new ExternalAlphaEntityEvent(
                            AlphaEventType.ACTIVE,
                            new ExternalAlphaEntity(entityId, lastName.get(entityId), true)
                    ));
        } else {
            return "Alpha entity with id " + entityId + " does not exist";
        }

        return "done";
    }

    @Override
    public String createAlphaToBetaLink(ExternalAlphaToBetaLinkDto newLink) {
        AlphaBetaId ab = new AlphaBetaId(newLink.getAlphaId(), newLink.getBetaId());
        if (!activeAlphaEntities.contains(ab.getAlphaId())) {
            return "Can't create link because of alpha entity with id "
                    + ab.getAlphaId() + " does not exist or does not active";
        }
        if (activeAlphaToBetaLinks.contains(ab) || disableAlphaToBetaLinks.contains(ab)) {
            return "Link from alpha entity " + ab.getAlphaId() +
                    " to beta entity " + ab.getBetaId() + " is already exist";
        }
        activeAlphaToBetaLinks.add(ab);
        lastHash.put(ab, newLink.getHash());

        alphaToBetaLinkEvents.add(
                new ExternalAlphaToBetaLinkEvent(
                        AlphaEventType.CREATE,
                        new ExternalAlphaToBetaLink(
                                newLink.getAlphaId(),
                                newLink.getBetaId(),
                                newLink.getHash(),
                                true)
                )
        );
        return "done";
    }

    @Override
    public String patchAlphaToBetaLink(ExternalAlphaToBetaLinkDto alphaToBetaLink) {
        AlphaBetaId ab = new AlphaBetaId(alphaToBetaLink.getAlphaId(), alphaToBetaLink.getBetaId());
        if (!activeAlphaToBetaLinks.contains(ab)) {
            return "Link from alpha entity " + ab.getAlphaId() +
                    " to beta entity " + ab.getBetaId() + " does not exist or does not active";
        }
        lastHash.put(ab, alphaToBetaLink.getHash());

        alphaToBetaLinkEvents.add(
                new ExternalAlphaToBetaLinkEvent(
                        AlphaEventType.UPDATE,
                        new ExternalAlphaToBetaLink(
                                alphaToBetaLink.getAlphaId(),
                                alphaToBetaLink.getBetaId(),
                                alphaToBetaLink.getHash(),
                                true)
                )
        );
        return "done";
    }

    @Override
    public String switchAlphaToBetaLink(long alphaId, long betaId) {
        AlphaBetaId ab = new AlphaBetaId(alphaId, betaId);
        if (activeAlphaToBetaLinks.contains(ab)) {
            activeAlphaToBetaLinks.remove(ab);
            disableAlphaToBetaLinks.add(ab);

            alphaToBetaLinkEvents.add(
                    new ExternalAlphaToBetaLinkEvent(
                            AlphaEventType.DISABLE,
                            new ExternalAlphaToBetaLink(
                                    alphaId,
                                    betaId,
                                    lastHash.get(new AlphaBetaId(alphaId, betaId)),
                                    false)
                    ));
        } else if (disableAlphaToBetaLinks.contains(ab)) {
            activeAlphaToBetaLinks.add(ab);
            disableAlphaToBetaLinks.remove(ab);

            alphaToBetaLinkEvents.add(
                    new ExternalAlphaToBetaLinkEvent(
                            AlphaEventType.ACTIVE,
                            new ExternalAlphaToBetaLink(
                                    alphaId,
                                    betaId,
                                    lastHash.get(new AlphaBetaId(alphaId, betaId)),
                                    true)
                    ));
        } else {
            return "Link from alpha entity " + ab.getAlphaId() +
                    " to beta entity " + ab.getBetaId() + " does not exist";
        }

        return "done";
    }

    @Override
    public List<ExternalAlphaEntityEvent> getAlphaEntityEvents(long tsFrom, long tsTo) {
        return entityEvents.stream()
                .filter(event -> tsFrom <= event.getEventTs() && event.getEventTs() < tsTo)
                .sorted(Comparator.comparingLong(ExternalAlphaEntityEvent::getEventTs))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExternalAlphaToBetaLinkEvent> getAlphaToBetaLinkEvents(long tsFrom, long tsTo) {
        return alphaToBetaLinkEvents.stream()
                .filter(event -> tsFrom <= event.getEventTs() && event.getEventTs() < tsTo)
                .sorted(Comparator.comparingLong(ExternalAlphaToBetaLinkEvent::getEventTs))
                .collect(Collectors.toList());
    }
}
