package com.st1580.diploma.repository.impl;

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
import com.st1580.diploma.collector.graph.entities.AlphaEntity;
import com.st1580.diploma.repository.AlphaRepository;
import com.st1580.diploma.repository.AlphaToBetaRepository;
import com.st1580.diploma.repository.GammaToAlphaRepository;
import com.st1580.diploma.repository.types.EntityActiveType;
import com.st1580.diploma.db.tables.Alpha;
import com.st1580.diploma.db.tables.records.AlphaRecord;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.EntityEventIdAndTs;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA;
import static com.st1580.diploma.repository.impl.RepositoryHelper.getBatches;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.noCondition;

@Repository
public class DbAlphaRepository implements AlphaRepository {
    @Autowired
    private DSLContext context;
    @Inject
    AlphaToBetaRepository alphaToBetaRepository;
    @Inject
    GammaToAlphaRepository gammaToAlphaRepository;
    private final Alpha TOP_LVL_ALPHA = ALPHA.as("top_lvl");
    private final Alpha LOW_LVL_ALPHA = ALPHA.as("low_lvl");

    @Override
    public Map<Long, AlphaEntity> collectAllActiveEntitiesByIds(Collection<Long> ids, long ts) {
        Condition condition = LOW_LVL_ALPHA.ID.in(ids)
                .and(LOW_LVL_ALPHA.CREATED_TS.lessOrEqual(ts));

        return getAlphaRecordByCondition(condition)
                .stream()
                .filter(record -> EntityActiveType.trueEntityActiveTypes.contains(record.getActiveStatus()))
                .map(this::convertToAlphaEntity)
                .collect(Collectors.toMap(
                        AlphaEntity::getId,
                        Function.identity()
                ));
    }

    @Override
    public Map<EntityType, Map<Long, List<Long>>> collectAllNeighborsIdsByEntities(Collection<Long> ids, long ts) {
        Map<EntityType, Map<Long, List<Long>>> res = new HashMap<>();

        res.put(EntityType.BETA, alphaToBetaRepository.getConnectedToEntitiesIdsByFromEntitiesIds(ids, ts));
        res.put(EntityType.GAMMA, gammaToAlphaRepository.getConnectedFromEntitiesIdsByToEntitiesIds(ids, ts));

        return res;
    }

    @Override
    public void batchInsertNewEvents(List<AlphaEvent> events) {
        List<AlphaRecord> records = events.stream().map(this::covertToAlphaRecord).collect(Collectors.toList());
        context.insertInto(ALPHA, ALPHA.ID, ALPHA.NAME, ALPHA.ACTIVE_STATUS, ALPHA.CREATED_TS)
                .valuesOfRecords(records)
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public List<Set<AlphaEvent>> getActiveStatusChangedEventsInRange(long tsFrom, long tsTo) {
        Map<Long, List<AlphaEvent>> alphaEventsById = context
                .selectFrom(ALPHA)
                .where(ALPHA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(ALPHA.CREATED_TS.lessThan(tsTo))
                        .and(ALPHA.ACTIVE_STATUS.in(EntityActiveType.changedEntityActiveTypes))
                )
                .fetchInto(AlphaEvent.class)
                .stream()
                .collect(Collectors.groupingBy(AlphaEvent::getEntityId));

        return getBatches(alphaEventsById);
    }

    @Override
    public void correctDependentLinks(long tsFrom, long tsTo) {
        List<EntityEventIdAndTs> alphaToBetaUndefinedDependencies =
                alphaToBetaRepository.getUndefinedFromStateInRange(tsFrom, tsTo);
        List<EntityEventIdAndTs> gammaToAlphaUndefinedDependencies =
                gammaToAlphaRepository.getUndefinedToStateInRange(tsFrom, tsTo);

        Map<Long, List<EntityEventIdAndTs>> alphaDependenciesById = Stream
                .concat(alphaToBetaUndefinedDependencies.stream(), gammaToAlphaUndefinedDependencies.stream())
                .collect(Collectors.groupingBy(EntityEventIdAndTs::getEntityId));

        Map<EntityEventIdAndTs, Boolean> actualAlphaState = new HashMap<>();
        for (Set<EntityEventIdAndTs> batch : getBatches(alphaDependenciesById)) {
            Map<Long, EntityEventIdAndTs> eventById = new HashMap<>();

            Condition condition = noCondition();
            for (EntityEventIdAndTs event : batch) {
                condition = condition.or(LOW_LVL_ALPHA.ID.eq(event.getEntityId())
                        .and(LOW_LVL_ALPHA.CREATED_TS.lessOrEqual(event.getCreatedTs())));
                eventById.put(event.getEntityId(), event);
            }

            actualAlphaState.putAll(getAlphaRecordByCondition(condition)
                    .stream()
                    .map(this::covertToAlphaEvent)
                    .collect(Collectors.toMap(
                            event -> eventById.get(event.getEntityId()),
                            event -> EntityActiveType.trueEntityActiveTypes.contains(event.getType().name())
                    ))
            );
        }

        alphaToBetaRepository.batchUpdateLinksDependentOnFromEntity(actualAlphaState);
        gammaToAlphaRepository.batchUpdateLinksDependentOnToEntity(actualAlphaState);
    }

    private Result<AlphaRecord> getAlphaRecordByCondition(Condition condition) {
        return context
                .selectFrom(TOP_LVL_ALPHA)
                .whereExists(
                        context.select(LOW_LVL_ALPHA.ID, max(LOW_LVL_ALPHA.CREATED_TS))
                                .from(LOW_LVL_ALPHA)
                                .where(condition)
                                .groupBy(LOW_LVL_ALPHA.ID)
                                .having(LOW_LVL_ALPHA.ID.eq(TOP_LVL_ALPHA.ID)
                                        .and(max(LOW_LVL_ALPHA.CREATED_TS).eq(TOP_LVL_ALPHA.CREATED_TS)))
                )
                .fetch();
    }

    private AlphaEntity convertToAlphaEntity(AlphaRecord record) {
        return new AlphaEntity(
                record.getId(),
                record.getName()
        );
    }

    private AlphaRecord covertToAlphaRecord(AlphaEvent event) {
        return new AlphaRecord(
                event.getEntityId(),
                event.getName(),
                event.getType().name(),
                event.getCreatedTs()
        );
    }

    private AlphaEvent covertToAlphaEvent(AlphaRecord record) {
        return new AlphaEvent(
                record.getId(),
                record.getName(),
                EntityActiveType.valueOf(record.getActiveStatus()),
                record.getCreatedTs()
        );
    }
}
