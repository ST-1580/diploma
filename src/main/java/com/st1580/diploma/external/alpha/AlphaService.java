package com.st1580.diploma.external.alpha;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.external.alpha.data.AlphaBetaId;
import com.st1580.diploma.external.alpha.data.AlphaEntityEvent;
import com.st1580.diploma.external.alpha.data.AlphaEventType;
import com.st1580.diploma.external.alpha.data.AlphaToBetaLinkEvent;
import com.st1580.diploma.external.alpha.data.ExternalAlphaEntity;
import com.st1580.diploma.external.alpha.data.ExternalAlphaToBetaLink;
import org.springframework.stereotype.Service;

@Service
public class AlphaService implements AlphaServiceApi {
    private final List<AlphaEntityEvent> entityEvents;
    private final List<AlphaToBetaLinkEvent> alphaToBetaLinkEvents;
    private final Set<Long> idState;
    private final Set<AlphaBetaId> linkState;

    @Inject
    public AlphaService(AlphaInitHelper helper) {
        this.entityEvents = new ArrayList<>();
        this.alphaToBetaLinkEvents = new ArrayList<>();
        this.idState = new HashSet<>(helper.getAllEntityIds());
        this.linkState = new HashSet<>(helper.getAllLinksIds());
    }

    @Override
    public String createEntity(ExternalAlphaEntity newEntity) {
        if (idState.contains(newEntity.getId())) {
            return "Alpha entity with id " + newEntity.getId() + " is already exists";
        }
        idState.add(newEntity.getId());

        entityEvents.add(
                new AlphaEntityEvent(
                    AlphaEventType.CREATE,
                    newEntity.getId(),
                    newEntity
                ));
        return "done";
    }

    @Override
    public String patchEntity(ExternalAlphaEntity alphaEntity) {
        if (!idState.contains(alphaEntity.getId())) {
            return "Alpha entity with id " + alphaEntity.getId() + " is not exists";
        }

        entityEvents.add(
                new AlphaEntityEvent(
                        AlphaEventType.UPDATE,
                        alphaEntity.getId(),
                        alphaEntity
                ));
        return "done";
    }

    @Override
    public String deleteEntity(long entityId) {
        if (!idState.contains(entityId)) {
            return "Alpha entity with id " + entityId + " is not exists";
        }
        idState.remove(entityId);

        entityEvents.add(
                new AlphaEntityEvent(
                        AlphaEventType.REMOVE,
                        entityId,
                        null
                ));
        return "done";
    }

    @Override
    public String createAlphaToBetaLink(ExternalAlphaToBetaLink newLink) {
        AlphaBetaId ab = new AlphaBetaId(newLink.getAlphaId(), newLink.getBetaId());
        if (linkState.contains(ab)) {
            return "Link from alpha entity " + ab.getAlphaId() +
                    " to beta entity " + ab.getBetaId() + " is already exists";
        }
        linkState.add(ab);

        alphaToBetaLinkEvents.add(
                new AlphaToBetaLinkEvent(
                        AlphaEventType.CREATE,
                        newLink.getAlphaId(),
                        newLink.getBetaId(),
                        newLink
                )
        );
        return "done";
    }

    @Override
    public String patchAlphaToBetaLink(ExternalAlphaToBetaLink alphaToBetaLink) {
        AlphaBetaId ab = new AlphaBetaId(alphaToBetaLink.getAlphaId(), alphaToBetaLink.getBetaId());
        if (!linkState.contains(ab)) {
            return "Link from alpha entity " + ab.getAlphaId() +
                    " to beta entity " + ab.getBetaId() + " is not exists";
        }

        alphaToBetaLinkEvents.add(
                new AlphaToBetaLinkEvent(
                        AlphaEventType.UPDATE,
                        alphaToBetaLink.getAlphaId(),
                        alphaToBetaLink.getBetaId(),
                        alphaToBetaLink
                )
        );
        return "done";
    }

    @Override
    public String deleteAlphaToBetaLink(long alphaId, long betaId) {
        AlphaBetaId ab = new AlphaBetaId(alphaId, betaId);
        if (!linkState.contains(ab)) {
            return "Link from alpha entity " + ab.getAlphaId() +
                    " to beta entity " + ab.getBetaId() + " is not exists";
        }
        linkState.remove(ab);

        alphaToBetaLinkEvents.add(
                new AlphaToBetaLinkEvent(
                        AlphaEventType.REMOVE,
                        alphaId,
                        betaId,
                        null
                )
        );
        return "done";
    }

    @Override
    public List<AlphaEntityEvent> getAlphaEntityEvents(long tsFrom, long tsTo) {
        return entityEvents.stream()
                .filter(event -> tsFrom <= event.getEventTs() && event.getEventTs() < tsTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlphaToBetaLinkEvent> getAlphaToBetaLinkEvents(long tsFrom, long tsTo) {
        return alphaToBetaLinkEvents.stream()
                .filter(event -> tsFrom <= event.getEventTs() && event.getEventTs() < tsTo)
                .collect(Collectors.toList());
    }
}
