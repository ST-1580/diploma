package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.entities.GammaEntity;
import com.st1580.diploma.collector.repository.GammaRepository;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.db.tables.Gamma;
import com.st1580.diploma.db.tables.records.GammaRecord;
import com.st1580.diploma.updater.events.EntityEvent;
import com.st1580.diploma.updater.events.GammaEvent;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.collector.repository.impl.RepositoryHelper.getBatches;
import static com.st1580.diploma.db.Tables.GAMMA;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.noCondition;

@Repository
public class DbGammaRepository implements GammaRepository {
    @Autowired
    private DSLContext context;
    @Inject
    GammaToAlphaRepository gammaToAlphaRepository;
    @Inject
    GammaToDeltaRepository gammaToDeltaRepository;
    private final Gamma TOP_LVL_GAMMA = GAMMA.as("top_lvl");
    private final Gamma LOW_LVL_GAMMA = GAMMA.as("low_lvl");

    @Override
    public Map<Long, GammaEntity> collectAllActiveEntitiesByIds(Collection<Long> ids, long ts) {
        Condition condition = LOW_LVL_GAMMA.ID.in(ids)
                .and(LOW_LVL_GAMMA.CREATED_TS.lessOrEqual(ts));

        return getGammaRecordByCondition(condition)
                .stream()
                .filter(record -> EntityActiveType.trueEntityActiveTypes.contains(record.getActiveStatus()))
                .map(this::convertToGammaEntity)
                .collect(Collectors.toMap(
                        GammaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids, long ts) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, gammaToAlphaRepository.getConnectedAlphaEntitiesIdsByGammaIds(ids, ts));
            res.put(EntityType.DELTA, gammaToDeltaRepository.getConnectedDeltaEntitiesIdsByGammaIds(ids, ts));
        });

        return res;
    }

    @Override
    public Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids,
                                                                                          long ts) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, new HashMap<>(gammaToAlphaRepository.getConnectedAlphaEntitiesByGammaIds(ids,
                    ts)));
            res.put(EntityType.DELTA, new HashMap<>(gammaToDeltaRepository.getConnectedDeltaEntitiesByGammaIds(ids,
                    ts)));
        });

        return res;
    }

    @Override
    public void batchInsertNewEvents(List<GammaEvent> events) {
        List<GammaRecord> records = events.stream().map(this::convertToGammaRecord).collect(Collectors.toList());
        context.insertInto(GAMMA, GAMMA.ID, GAMMA.IS_MASTER, GAMMA.ACTIVE_STATUS, GAMMA.CREATED_TS)
                .valuesOfRecords(records)
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public List<Set<GammaEvent>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo) {
        Map<Long, List<GammaEvent>> gammaEventsById = context
                .selectFrom(GAMMA)
                .where(GAMMA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(GAMMA.CREATED_TS.lessThan(tsTo))
                        .and(GAMMA.ACTIVE_STATUS.in(EntityActiveType.changedEntityActiveTypes))
                )
                .fetchInto(GammaEvent.class)
                .stream()
                .collect(Collectors.groupingBy(GammaEvent::getGammaId));

        return getBatches(gammaEventsById);
    }

    @Override
    public void correctDependentLinks(long tsFrom, long tsTo) {
        List<EntityEvent> gammaToAlphaUndefinedDependencies =
                gammaToAlphaRepository.getUndefinedGammaStateInRange(tsFrom, tsTo);
        List<EntityEvent> gammaToDeltaUndefinedDependencies =
                gammaToDeltaRepository.getUndefinedGammaStateInRange(tsFrom, tsTo);

        Map<Long, List<EntityEvent>> gammaDependenciesById = Stream
                .concat(gammaToAlphaUndefinedDependencies.stream(), gammaToDeltaUndefinedDependencies.stream())
                .collect(Collectors.groupingBy(EntityEvent::getEntityId));

        Map<EntityEvent, Boolean> actualGammaState = new HashMap<>();
        for (Set<EntityEvent> batch : getBatches(gammaDependenciesById)) {
            Map<Long, EntityEvent> eventById = new HashMap<>();

            Condition condition = noCondition();
            for (EntityEvent event : batch) {
                condition = condition.or(LOW_LVL_GAMMA.ID.eq(event.getEntityId())
                        .and(LOW_LVL_GAMMA.CREATED_TS.lessOrEqual(event.getCreatedTs())));
                eventById.put(event.getEntityId(), event);
            }

            actualGammaState.putAll(getGammaRecordByCondition(condition)
                    .stream()
                    .map(this::convertToGammaEvent)
                    .collect(Collectors.toMap(
                            event -> eventById.get(event.getGammaId()),
                            event -> EntityActiveType.trueEntityActiveTypes.contains(event.getType().name())
                    ))
            );
        }

        gammaToAlphaRepository.batchUpdateLinksDependentOnGamma(actualGammaState);
        gammaToDeltaRepository.batchUpdateLinksDependentOnGamma(actualGammaState);
    }

    private Result<GammaRecord> getGammaRecordByCondition(Condition condition) {
        return context
                .selectFrom(TOP_LVL_GAMMA)
                .whereExists(
                        context.select(LOW_LVL_GAMMA.ID, max(LOW_LVL_GAMMA.CREATED_TS))
                                .from(LOW_LVL_GAMMA)
                                .where(condition)
                                .groupBy(LOW_LVL_GAMMA.ID)
                                .having(LOW_LVL_GAMMA.ID.eq(TOP_LVL_GAMMA.ID)
                                        .and(max(LOW_LVL_GAMMA.CREATED_TS).eq(TOP_LVL_GAMMA.CREATED_TS)))
                )
                .fetch();
    }

    private GammaRecord convertToGammaRecord(GammaEvent event) {
        return new GammaRecord(
                event.getGammaId(),
                event.isMaster(),
                event.getType().name(),
                event.getCreatedTs()
        );
    }

    private GammaEntity convertToGammaEntity(GammaRecord record) {
        return new GammaEntity(
                record.getId(),
                record.getIsMaster()
        );
    }

    private GammaEvent convertToGammaEvent(GammaRecord record) {
        return new GammaEvent(
                record.getId(),
                record.getIsMaster(),
                EntityActiveType.valueOf(record.getActiveStatus()),
                record.getCreatedTs()
        );
    }
}
