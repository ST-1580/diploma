package com.st1580.diploma.collector.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.links.AlphaToBetaLink;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.collector.repository.types.LinkEndActivityType;
import com.st1580.diploma.db.tables.AlphaToBeta;
import com.st1580.diploma.db.tables.records.AlphaToBetaRecord;
import com.st1580.diploma.updater.events.AlphaEvent;
import com.st1580.diploma.updater.events.AlphaToBetaEvent;
import com.st1580.diploma.updater.events.BetaEvent;
import com.st1580.diploma.updater.events.EntityEvent;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Row5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA;
import static com.st1580.diploma.db.Tables.ALPHA_TO_BETA;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.update;

@Repository
public class DbAlphaToBetaRepository implements AlphaToBetaRepository {
    @Autowired
    private DSLContext context;
    private final AlphaToBeta TOP_LVL_AB = ALPHA_TO_BETA.as("top_lvl");
    private final AlphaToBeta LOW_LVL_AB = ALPHA_TO_BETA.as("low_lvl");

    @Override
    public Map<Long, List<Long>> getConnectedBetaEntitiesIdsByAlphaIds(Collection<Long> alphaIds, long ts) {
        return context
                .select(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID, max(ALPHA_TO_BETA.CREATED_TS))
                .from(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.ALPHA_ID.in(alphaIds)
                        .and(ALPHA_TO_BETA.CREATED_TS.lessOrEqual(ts))
                        .and(ALPHA_TO_BETA.CAN_USE))
                .groupBy(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID)
                .fetchGroups(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID);
    }

    @Override
    public Map<Long, List<AlphaToBetaLink>> getConnectedBetaEntitiesByAlphaIds(Collection<Long> alphaIds, long ts) {
        Condition condition = LOW_LVL_AB.ALPHA_ID.in(alphaIds)
                .and(LOW_LVL_AB.CREATED_TS.lessOrEqual(ts)
                .and(LOW_LVL_AB.CAN_USE));

        return getABRecordByCondition(condition)
                .stream()
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(AlphaToBetaLink::getAlphaId));
    }

    @Override
    public Map<Long, List<Long>> getConnectedAlphaEntitiesIdsByBetaIds(Collection<Long> betaIds, long ts) {
        return context
                .select(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID, max(ALPHA_TO_BETA.CREATED_TS))
                .from(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.BETA_ID.in(betaIds)
                        .and(ALPHA_TO_BETA.CREATED_TS.lessOrEqual(ts))
                        .and(ALPHA_TO_BETA.CAN_USE))
                .groupBy(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID)
                .fetchGroups(ALPHA_TO_BETA.BETA_ID, ALPHA_TO_BETA.ALPHA_ID);
    }

    @Override
    public Map<Long, List<AlphaToBetaLink>> getConnectedAlphaEntitiesByBetaIds(Collection<Long> betaIds, long ts) {
        Condition condition = LOW_LVL_AB.BETA_ID.in(betaIds)
                .and(LOW_LVL_AB.CREATED_TS.lessOrEqual(ts)
                .and(LOW_LVL_AB.CAN_USE));

        return getABRecordByCondition(condition)
                .stream()
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(AlphaToBetaLink::getBetaId));
    }

    @Override
    public void batchInsertNewEvents(List<AlphaToBetaEvent> events) {
        List<Row5<Long, Long, String, Boolean, Long>> rows =
                events.stream().map(this::covertToRow).collect(Collectors.toList());
        context.insertInto(ALPHA_TO_BETA, ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID,
                        ALPHA_TO_BETA.HASH, ALPHA_TO_BETA.IS_ACTIVE, ALPHA_TO_BETA.CREATED_TS)
                .valuesOfRows(rows)
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public void addLinkEventsTriggeredByEntitiesUpdate(List<List<AlphaEvent>> batchedAlphaEvents,
                                                       List<List<BetaEvent>> batchedBetaEvents) {
        Map<AlphaEvent, List<AlphaToBetaRecord>> ABLinksByAlphaEntity = getActualLinksForAlpha(batchedAlphaEvents);
        Map<BetaEvent, List<AlphaToBetaRecord>> ABLinksByBetaEntity = getActualLinksForBeta(batchedBetaEvents);

        List<AlphaToBetaRecord> records = new ArrayList<>();

        records.addAll(ABLinksByAlphaEntity.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(link -> new AlphaToBetaRecord(
                                link.getAlphaId(),
                                link.getBetaId(),
                                link.getHash(),
                                entry.getKey().getType() == EntityActiveType.DELETED ? false : link.getIsActive(),
                                EntityActiveType.trueEntityActiveTypes.contains(entry.getKey().getType().name()) ?
                                        LinkEndActivityType.TRUE.name() : LinkEndActivityType.FALSE.name(),
                                link.getIsActiveBeta(),
                                null,
                                entry.getKey().getCreatedTs()
                        ))
                )
                .collect(Collectors.toList())
        );

        records.addAll(ABLinksByBetaEntity.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(link -> new AlphaToBetaRecord(
                                link.getAlphaId(),
                                link.getBetaId(),
                                link.getHash(),
                                entry.getKey().getType() == EntityActiveType.DELETED ? false : link.getIsActive(),
                                link.getIsActiveAlpha(),
                                EntityActiveType.trueEntityActiveTypes.contains(entry.getKey().getType().name()) ?
                                        LinkEndActivityType.TRUE.name() : LinkEndActivityType.FALSE.name(),
                                null,
                                entry.getKey().getCreatedTs()
                        ))
                )
                .collect(Collectors.toList())
        );

        context.insertInto(ALPHA_TO_BETA, ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID, ALPHA_TO_BETA.HASH,
                        ALPHA_TO_BETA.IS_ACTIVE, ALPHA_TO_BETA.IS_ACTIVE_ALPHA, ALPHA_TO_BETA.IS_ACTIVE_BETA,
                        ALPHA_TO_BETA.CAN_USE, ALPHA_TO_BETA.CREATED_TS)
                .valuesOfRecords(records)
                .onDuplicateKeyIgnore()
                .execute();
    }

    private Map<AlphaEvent, List<AlphaToBetaRecord>> getActualLinksForAlpha(List<List<AlphaEvent>> batchedAlphaEvents) {
        Map<AlphaEvent, List<AlphaToBetaRecord>> res = new HashMap<>();

        for (List<AlphaEvent> batch : batchedAlphaEvents) {
            Map<Long, AlphaEvent> eventById = new HashMap<>();
            Condition condition = noCondition();
            for (AlphaEvent event : batch) {
                condition = condition.or(LOW_LVL_AB.ALPHA_ID.eq(event.getAlphaId())
                        .and(LOW_LVL_AB.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getAlphaId(), event);
            }

            res.putAll(getABRecordByCondition(condition)
                    .stream()
                    .collect(Collectors.groupingBy(link -> eventById.get(link.getAlphaId())))
            );
        }

        return res;
    }

    private Map<BetaEvent, List<AlphaToBetaRecord>> getActualLinksForBeta(List<List<BetaEvent>> batchedBetaEvents) {
        Map<BetaEvent, List<AlphaToBetaRecord>> res = new HashMap<>();

        for (List<BetaEvent> batch : batchedBetaEvents) {
            Map<Long, BetaEvent> eventById = new HashMap<>();
            Condition condition = noCondition();
            for (BetaEvent event : batch) {
                condition = condition.or(LOW_LVL_AB.BETA_ID.eq(event.getBetaId())
                        .and(LOW_LVL_AB.CREATED_TS.lessThan(event.getCreatedTs())));
                eventById.put(event.getBetaId(), event);
            }

            res.putAll(getABRecordByCondition(condition)
                    .stream()
                    .collect(Collectors.groupingBy(link -> eventById.get(link.getBetaId())))
            );
        }

        return res;
    }

    @Override
    public List<EntityEvent> getUndefinedAlphaStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(ALPHA_TO_BETA.CREATED_TS.lessThan(tsTo))
                        .and(ALPHA_TO_BETA.IS_ACTIVE_ALPHA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEvent(record.getAlphaId(), record.getCreatedTs()));
    }

    @Override
    public List<EntityEvent> getUndefinedBetaStateInRange(long tsFrom, long tsTo) {
        return context
                .selectFrom(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.CREATED_TS.greaterOrEqual(tsFrom)
                        .and(ALPHA_TO_BETA.CREATED_TS.lessThan(tsTo))
                        .and(ALPHA_TO_BETA.IS_ACTIVE_BETA.eq(LinkEndActivityType.UNDEFINED.name())))
                .fetch()
                .map(record -> new EntityEvent(record.getBetaId(), record.getCreatedTs()));
    }

    @Override
    public void batchUpdateLinksDependentOnAlpha(Map<EntityEvent, Boolean> alphaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEvent event : alphaActiveStatusByEvent.keySet()) {
                ctx.dsl().update(ALPHA_TO_BETA)
                        .set(ALPHA_TO_BETA.IS_ACTIVE_ALPHA,
                                LinkEndActivityType.parseBoolean(alphaActiveStatusByEvent.get(event)).name())
                        .where(ALPHA_TO_BETA.ALPHA_ID.eq(event.getEntityId())
                                .and(ALPHA_TO_BETA.CREATED_TS.eq(event.getCreatedTs())))
                        .execute();
            }
        });
    }

    @Override
    public void batchUpdateLinksDependentOnBeta(Map<EntityEvent, Boolean> betaActiveStatusByEvent) {
        context.batched(ctx -> {
            for (EntityEvent event : betaActiveStatusByEvent.keySet()) {
                ctx.dsl().update(ALPHA_TO_BETA)
                        .set(ALPHA_TO_BETA.IS_ACTIVE_BETA,
                                LinkEndActivityType.parseBoolean(betaActiveStatusByEvent.get(event)).name())
                        .where(ALPHA_TO_BETA.BETA_ID.eq(event.getEntityId())
                                .and(ALPHA_TO_BETA.CREATED_TS.eq(event.getCreatedTs())))
                        .execute();
            }
        });
    }

    private Result<AlphaToBetaRecord> getABRecordByCondition(Condition condition) {
        return context
                .selectFrom(TOP_LVL_AB)
                .whereExists(context
                        .select(LOW_LVL_AB.ALPHA_ID, LOW_LVL_AB.BETA_ID, max(LOW_LVL_AB.CREATED_TS))
                        .from(LOW_LVL_AB)
                        .where(condition)
                        .groupBy(LOW_LVL_AB.ALPHA_ID, LOW_LVL_AB.BETA_ID)
                        .having(LOW_LVL_AB.ALPHA_ID.eq(TOP_LVL_AB.ALPHA_ID)
                                .and(LOW_LVL_AB.BETA_ID.eq(TOP_LVL_AB.BETA_ID))
                                .and(max(LOW_LVL_AB.CREATED_TS).eq(TOP_LVL_AB.CREATED_TS)))
                )
                .fetch();
    }

    private Row5<Long, Long, String, Boolean, Long> covertToRow(AlphaToBetaEvent event) {
        return row(event.getAlphaId(), event.getBetaId(), event.getHash(), event.isActive(), event.getCreatedTs());
    }

    private AlphaToBetaLink convertToLink(AlphaToBetaRecord record) {
        return new AlphaToBetaLink(
                record.getAlphaId(),
                record.getBetaId(),
                record.getHash()
        );
    }
}
