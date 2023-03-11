package com.st1580.diploma.external.gamma;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.external.alpha.data.AlphaEventType;
import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntity;
import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntityEvent;
import com.st1580.diploma.external.alpha.data.link.AlphaBetaId;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLink;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLinkEvent;
import com.st1580.diploma.external.gamma.data.GammaEventType;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntity;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntityDto;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntityEvent;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntityPayload;
import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLink;
import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLinkDto;
import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLinkEvent;
import com.st1580.diploma.external.gamma.data.links.ga.GammaAlphaId;
import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLink;
import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLinkDto;
import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLinkEvent;
import com.st1580.diploma.external.gamma.data.links.gd.GammaDeltaId;
import com.st1580.diploma.external.repository.ExternalServicesRepository;
import org.springframework.stereotype.Service;

@Service
public class GammaService implements GammaServiceApi {
    private final List<ExternalGammaEntityEvent> entityEvents;
    private final List<ExternalGammaToAlphaLinkEvent> gammaToAlphaLinkEvents;
    private final List<ExternalGammaToDeltaLinkEvent> gammaToDeltaLinkEvents;
    private final Set<Long> activeGammaEntities;
    private final Set<Long> disableGammaEntities;
    private final Map<Long, ExternalGammaEntityPayload> lastPayload;
    private final Set<GammaAlphaId> activeGammaToAlphaLinks;
    private final Set<GammaAlphaId> disableGammaToAlphaLinks;
    private final Map<GammaAlphaId, Long> lastWeight;

    private final Set<GammaDeltaId> activeGammaToDeltaLinks;
    private final Set<GammaDeltaId> disableGammaToDeltaLinks;

    @Inject
    public GammaService(ExternalServicesRepository externalServicesRepository) {
        this.entityEvents = new ArrayList<>();
        this.gammaToAlphaLinkEvents = new ArrayList<>();
        this.gammaToDeltaLinkEvents = new ArrayList<>();

        List<ExternalGammaEntity> entities = externalServicesRepository.getAllGammaEntities();
        this.activeGammaEntities = entities.stream()
                .filter(ExternalGammaEntity::isActive)
                .map(ExternalGammaEntity::getId)
                .collect(Collectors.toSet());
        this.disableGammaEntities = entities.stream()
                .filter(Predicate.not(ExternalGammaEntity::isActive))
                .map(ExternalGammaEntity::getId)
                .collect(Collectors.toSet());
        this.lastPayload = entities.stream().collect(Collectors.toMap(
                ExternalGammaEntity::getId,
                entity -> new ExternalGammaEntityPayload(entity.isMaster(), entity.getUnimportantData())
        ));

        List<ExternalGammaToAlphaLink> gaLinks = externalServicesRepository.getAllGammaToAlphaLinks();
        this.activeGammaToAlphaLinks = gaLinks.stream()
                .filter(ExternalGammaToAlphaLink::isActive)
                .map(link -> new GammaAlphaId(link.getGammaId(), link.getAlphaId()))
                .collect(Collectors.toSet());
        this.disableGammaToAlphaLinks = gaLinks.stream()
                .filter(Predicate.not(ExternalGammaToAlphaLink::isActive))
                .map(link -> new GammaAlphaId(link.getGammaId(), link.getAlphaId()))
                .collect(Collectors.toSet());
        this.lastWeight = gaLinks.stream().collect(Collectors.toMap(
                link -> new GammaAlphaId(link.getGammaId(), link.getAlphaId()),
                ExternalGammaToAlphaLink::getWeight
        ));

        List<ExternalGammaToDeltaLink> gdLinks = externalServicesRepository.getAllGammaToDeltaLinks();
        this.activeGammaToDeltaLinks = gdLinks.stream()
                .filter(ExternalGammaToDeltaLink::isActive)
                .map(link -> new GammaDeltaId(link.getGammaId(), link.getDeltaId()))
                .collect(Collectors.toSet());
        this.disableGammaToDeltaLinks = gdLinks.stream()
                .filter(Predicate.not(ExternalGammaToDeltaLink::isActive))
                .map(link -> new GammaDeltaId(link.getGammaId(), link.getDeltaId()))
                .collect(Collectors.toSet());
    }

    @Override
    public String createEntity(ExternalGammaEntityDto newEntity) {
        long newEntityId = newEntity.getId();
        if (activeGammaEntities.contains(newEntityId) || disableGammaEntities.contains(newEntityId)) {
            return "Gamma entity id " + newEntityId + " is already exist";
        }
        activeGammaEntities.add(newEntityId);
        lastPayload.put(newEntityId,
                new ExternalGammaEntityPayload(newEntity.isMaster(), newEntity.getUnimportantData()));

        entityEvents.add(
                new ExternalGammaEntityEvent(
                        GammaEventType.INSERT,
                        new ExternalGammaEntity(
                                newEntityId,
                                newEntity.isMaster(),
                                newEntity.getUnimportantData(),
                                true)
                ));
        return "done";
    }

    @Override
    public String patchEntity(ExternalGammaEntityDto gammaEntity) {
        if (!activeGammaEntities.contains(gammaEntity.getId())) {
            return "Gamma entity with id " + gammaEntity.getId() + " does not exist or does not active";
        }
        lastPayload.put(gammaEntity.getId(),
                new ExternalGammaEntityPayload(gammaEntity.isMaster(), gammaEntity.getUnimportantData()));

        entityEvents.add(
                new ExternalGammaEntityEvent(
                        GammaEventType.UPDATE,
                        new ExternalGammaEntity(
                                gammaEntity.getId(),
                                gammaEntity.isMaster(),
                                gammaEntity.getUnimportantData(),
                                true)
                ));
        return "done";
    }

    @Override
    public String switchEntity(long entityId) {
        if (activeGammaEntities.contains(entityId)) {
            activeGammaEntities.remove(entityId);
            disableGammaEntities.add(entityId);

            ExternalGammaEntityPayload payload = lastPayload.get(entityId);
            entityEvents.add(
                    new ExternalGammaEntityEvent(
                            GammaEventType.TURN_OFF,
                            new ExternalGammaEntity(
                                    entityId,
                                    payload.isMaster(),
                                    payload.getUnimportantData(),
                                    false)
                    ));
        } else if (disableGammaEntities.contains(entityId)) {
            activeGammaEntities.add(entityId);
            disableGammaEntities.remove(entityId);

            ExternalGammaEntityPayload payload = lastPayload.get(entityId);
            entityEvents.add(
                    new ExternalGammaEntityEvent(
                            GammaEventType.TURN_ON,
                            new ExternalGammaEntity(
                                    entityId,
                                    payload.isMaster(),
                                    payload.getUnimportantData(),
                                    true)
                    ));
        } else {
            return "Gamma entity with id " + entityId + " does not exist";
        }

        return "done";
    }

    @Override
    public String createGammaToAlphaLink(ExternalGammaToAlphaLinkDto newLink) {
        GammaAlphaId ga = new GammaAlphaId(newLink.getGammaId(), newLink.getAlphaId());
        if (!activeGammaEntities.contains(ga.getGammaId())) {
            return "Can't create link because of gamma entity with id "
                    + ga.getGammaId() + " does not exist or does not active";
        }
        if (activeGammaToAlphaLinks.contains(ga) || disableGammaToAlphaLinks.contains(ga)) {
            return "Link from gamma entity " + ga.getGammaId() +
                    " to alpha entity " + ga.getAlphaId() + " is already exist";
        }
        activeGammaToAlphaLinks.add(ga);
        lastWeight.put(ga, newLink.getWeight());

        gammaToAlphaLinkEvents.add(
                new ExternalGammaToAlphaLinkEvent(
                        GammaEventType.INSERT,
                        new ExternalGammaToAlphaLink(
                                newLink.getGammaId(),
                                newLink.getAlphaId(),
                                newLink.getWeight(),
                                true)
                )
        );
        return "done";
    }

    @Override
    public String patchGammaToAlphaLink(ExternalGammaToAlphaLinkDto gammaToAlphaLink) {
        GammaAlphaId ga = new GammaAlphaId(gammaToAlphaLink.getGammaId(), gammaToAlphaLink.getAlphaId());
        if (!activeGammaToAlphaLinks.contains(ga)) {
            return "Link from gamma entity " + ga.getGammaId() +
                    " to alpha entity " + ga.getAlphaId() + " does not exist or does not active";
        }
        lastWeight.put(ga, gammaToAlphaLink.getWeight());

        gammaToAlphaLinkEvents.add(
                new ExternalGammaToAlphaLinkEvent(
                        GammaEventType.UPDATE,
                        new ExternalGammaToAlphaLink(
                                gammaToAlphaLink.getGammaId(),
                                gammaToAlphaLink.getAlphaId(),
                                gammaToAlphaLink.getWeight(),
                                true)
                )
        );
        return "done";
    }

    @Override
    public String switchGammaToAlphaLink(long gammaId, long alphaId) {
        GammaAlphaId ga = new GammaAlphaId(gammaId, alphaId);
        if (activeGammaToAlphaLinks.contains(ga)) {
            activeGammaToAlphaLinks.remove(ga);
            disableGammaToAlphaLinks.add(ga);

            gammaToAlphaLinkEvents.add(
                    new ExternalGammaToAlphaLinkEvent(
                            GammaEventType.TURN_OFF,
                            new ExternalGammaToAlphaLink(
                                    gammaId,
                                    alphaId,
                                    lastWeight.get(ga),
                                    false)
                    )
            );
        } else if (disableGammaToAlphaLinks.contains(ga)) {
            activeGammaToAlphaLinks.add(ga);
            disableGammaToAlphaLinks.remove(ga);

            gammaToAlphaLinkEvents.add(
                    new ExternalGammaToAlphaLinkEvent(
                            GammaEventType.TURN_ON,
                            new ExternalGammaToAlphaLink(
                                    gammaId,
                                    alphaId,
                                    lastWeight.get(ga),
                                    true)
                    )
            );
        } else {
            return "Link from gamma entity " + ga.getGammaId() +
                    " to alpha entity " + ga.getAlphaId() + " does not exist";
        }

        return "done";
    }

    @Override
    public String createGammaToDeltaLink(ExternalGammaToDeltaLinkDto newLink) {
        GammaDeltaId gd = new GammaDeltaId(newLink.getGammaId(), newLink.getDeltaId());
        if (!activeGammaEntities.contains(gd.getGammaId())) {
            return "Can't create link because of gamma entity with id "
                    + gd.getGammaId() + " does not exist or does not active";
        }
        if (activeGammaToDeltaLinks.contains(gd) || disableGammaToDeltaLinks.contains(gd)) {
            return "Link from gamma entity " + gd.getGammaId() +
                    " to delta entity " + gd.getDeltaId() + " is already exist";
        }
        activeGammaToDeltaLinks.add(gd);

        gammaToDeltaLinkEvents.add(
                new ExternalGammaToDeltaLinkEvent(
                        GammaEventType.INSERT,
                        new ExternalGammaToDeltaLink(
                                newLink.getGammaId(),
                                newLink.getDeltaId(),
                                true)
                )
        );
        return "done";
    }

    @Override
    public String patchGammaToDeltaLink(ExternalGammaToDeltaLinkDto gammaToDeltaLink) {
        GammaDeltaId gd = new GammaDeltaId(gammaToDeltaLink.getGammaId(), gammaToDeltaLink.getDeltaId());
        if (!activeGammaToDeltaLinks.contains(gd)) {
            return "Link from gamma entity " + gd.getGammaId() +
                    " to delta entity " + gd.getDeltaId() + " does not exist or does not active";
        }

        gammaToDeltaLinkEvents.add(
                new ExternalGammaToDeltaLinkEvent(
                        GammaEventType.UPDATE,
                        new ExternalGammaToDeltaLink(
                                gammaToDeltaLink.getGammaId(),
                                gammaToDeltaLink.getDeltaId(),
                                true)
                )
        );
        return "done";
    }

    @Override
    public String switchGammaToDeltaLink(long gammaId, long deltaId) {
        GammaDeltaId gd = new GammaDeltaId(gammaId, deltaId);
        if (activeGammaToDeltaLinks.contains(gd)) {
            activeGammaToDeltaLinks.remove(gd);
            disableGammaToDeltaLinks.add(gd);

            gammaToDeltaLinkEvents.add(
                    new ExternalGammaToDeltaLinkEvent(
                            GammaEventType.TURN_OFF,
                            new ExternalGammaToDeltaLink(gammaId, deltaId, false)
                    )
            );
        } else if (disableGammaToDeltaLinks.contains(gd)) {
            activeGammaToDeltaLinks.add(gd);
            disableGammaToDeltaLinks.remove(gd);

            gammaToDeltaLinkEvents.add(
                    new ExternalGammaToDeltaLinkEvent(
                            GammaEventType.TURN_ON,
                            new ExternalGammaToDeltaLink(gammaId, deltaId, true)
                    )
            );
        } else {
            return "Link from gamma entity " + gd.getGammaId() +
                    " to delta entity " + gd.getDeltaId() + " does not exist";
        }

        return "done";
    }

    @Override
    public List<ExternalGammaEntityEvent> getGammaEntityEvents(long tsFrom, long tsTo) {
        return entityEvents.stream()
                .filter(event -> tsFrom <= event.getEventTs() && event.getEventTs() < tsTo)
                .sorted(Comparator.comparingLong(ExternalGammaEntityEvent::getEventTs))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExternalGammaToAlphaLinkEvent> getGammaToAlphaLinkEvents(long tsFrom, long tsTo) {
        return gammaToAlphaLinkEvents.stream()
                .filter(event -> tsFrom <= event.getEventTs() && event.getEventTs() < tsTo)
                .sorted(Comparator.comparingLong(ExternalGammaToAlphaLinkEvent::getEventTs))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExternalGammaToDeltaLinkEvent> getGammaToDeltaLinkEvents(long tsFrom, long tsTo) {
        return gammaToDeltaLinkEvents.stream()
                .filter(event -> tsFrom <= event.getEventTs() && event.getEventTs() < tsTo)
                .sorted(Comparator.comparingLong(ExternalGammaToDeltaLinkEvent::getEventTs))
                .collect(Collectors.toList());
    }
}
