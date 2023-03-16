package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.st1580.diploma.collector.graph.EntityType;
import com.st1580.diploma.collector.graph.Link;
import com.st1580.diploma.collector.graph.entities.BetaEntity;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.collector.repository.BetaRepository;
import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.db.tables.Beta;
import com.st1580.diploma.db.tables.records.BetaRecord;
import com.st1580.diploma.updater.events.BetaEvent;
import com.st1580.diploma.updater.events.EntityEvent;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.collector.repository.impl.RepositoryHelper.getBatches;
import static com.st1580.diploma.db.Tables.BETA;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.noCondition;

@Repository
public class DbBetaRepository implements BetaRepository {
    @Autowired
    private DSLContext context;
    @Inject
    AlphaToBetaRepository alphaToBetaRepository;

    private final Beta TOP_LVL_BETA = BETA.as("top_lvl");
    private final Beta LOW_LVL_BETA = BETA.as("low_lvl");

    @Override
    public Map<Long, BetaEntity> collectAllActiveEntitiesByIds(Collection<Long> ids, long ts) {
        Condition condition = LOW_LVL_BETA.ID.in(ids)
                .and(LOW_LVL_BETA.CREATED_TS.lessOrEqual(ts));

        return getBetaRecordByCondition(condition)
                .stream()
                .filter(record -> EntityActiveType.trueEntityActiveTypes.contains(record.getActiveStatus()))
                .map(this::convertToBetaEntity)
                .collect(Collectors.toMap(
                        BetaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids, long ts) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, alphaToBetaRepository.getConnectedAlphaEntitiesIdsByBetaIds(ids, ts));
        });

        return res;
    }

    @Override
    public Map<EntityType, Map<Long, List<? extends Link>>> collectAllNeighborsByEntities(Collection<Long> ids, long ts) {
        Map<EntityType, Map<Long, List<? extends Link>>> res = new HashMap<>();

        context.transaction(ctx -> {
            res.put(EntityType.ALPHA, new HashMap<>(alphaToBetaRepository.getConnectedAlphaEntitiesByBetaIds(ids, ts)));
        });

        return res;
    }

    @Override
    public void batchInsertNewEvents(List<BetaEvent> events) {
        List<BetaRecord> records = events.stream().map(this::covertToBetaRecord).collect(Collectors.toList());
        context.insertInto(BETA, BETA.ID, BETA.EPOCH, BETA.ACTIVE_STATUS, BETA.CREATED_TS)
                .valuesOfRecords(records)
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public List<Set<BetaEvent>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo) {
        Map<Long, List<BetaEvent>> betaEventsById = context
                .selectFrom(BETA)
                .where(BETA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(BETA.CREATED_TS.lessThan(tsTo))
                        .and(BETA.ACTIVE_STATUS.in(EntityActiveType.changedEntityActiveTypes))
                )
                .fetchInto(BetaEvent.class)
                .stream()
                .collect(Collectors.groupingBy(BetaEvent::getBetaId));

        return getBatches(betaEventsById);
    }

    @Override
    public void correctDependentLinks(long tsFrom, long tsTo) {
        List<EntityEvent> alphaToBetaUndefinedDependencies =
                alphaToBetaRepository.getUndefinedBetaStateInRange(tsFrom, tsTo);

        Map<Long, List<EntityEvent>> betaDependenciesById =
                alphaToBetaUndefinedDependencies.stream()
                .collect(Collectors.groupingBy(EntityEvent::getEntityId));

        Map<EntityEvent, Boolean> actualBetaState = new HashMap<>();
        for (Set<EntityEvent> batch : getBatches(betaDependenciesById)) {
            Map<Long, EntityEvent> eventById = new HashMap<>();

            Condition condition = noCondition();
            for (EntityEvent event : batch) {
                condition = condition.or(LOW_LVL_BETA.ID.eq(event.getEntityId())
                        .and(LOW_LVL_BETA.CREATED_TS.lessOrEqual(event.getCreatedTs())));
                eventById.put(event.getEntityId(), event);
            }

            actualBetaState.putAll(getBetaRecordByCondition(condition)
                    .stream()
                    .map(this::covertToBetaEvent)
                    .collect(Collectors.toMap(
                            event -> eventById.get(event.getBetaId()),
                            event -> EntityActiveType.trueEntityActiveTypes.contains(event.getType().name())
                    ))
            );
        }

        alphaToBetaRepository.batchUpdateLinksDependentOnBeta(actualBetaState);
    }

    private Result<BetaRecord> getBetaRecordByCondition(Condition condition) {
        return context
                .selectFrom(TOP_LVL_BETA)
                .whereExists(
                        context.select(LOW_LVL_BETA.ID, max(LOW_LVL_BETA.CREATED_TS))
                                .from(LOW_LVL_BETA)
                                .where(condition)
                                .groupBy(LOW_LVL_BETA.ID)
                                .having(LOW_LVL_BETA.ID.eq(TOP_LVL_BETA.ID)
                                        .and(max(LOW_LVL_BETA.CREATED_TS).eq(TOP_LVL_BETA.CREATED_TS)))
                )
                .fetch();
    }

    private BetaRecord covertToBetaRecord(BetaEvent event) {
        return new BetaRecord(
                event.getBetaId(),
                event.getEpoch(),
                event.getType().name(),
                event.getCreatedTs()
        );
    }

    private BetaEntity convertToBetaEntity(BetaRecord record) {
        return new BetaEntity(
                record.getId(),
                record.getEpoch()
        );
    }

    private BetaEvent covertToBetaEvent(BetaRecord record) {
        return new BetaEvent(
                record.getId(),
                record.getEpoch(),
                EntityActiveType.valueOf(record.getActiveStatus()),
                record.getCreatedTs()
        );
    }
}
