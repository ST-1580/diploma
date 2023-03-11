package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.links.GammaToDeltaLink;
import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import com.st1580.diploma.db.tables.GammaToDelta;
import com.st1580.diploma.db.tables.records.GammaToDeltaRecord;
import com.st1580.diploma.updater.events.GammaToDeltaEvent;
import org.jooq.DSLContext;
import org.jooq.Row4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA_TO_DELTA;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.row;

@Repository
public class DbGammaToDeltaRepository implements GammaToDeltaRepository {
    @Autowired
    private DSLContext context;

    @Override
    public Map<Long, List<Long>> getConnectedGammaEntitiesIdsByDeltaIds(Collection<Long> deltaIds, long ts) {
        return context
                .select(GAMMA_TO_DELTA.GAMMA_ID, GAMMA_TO_DELTA.DELTA_ID, max(GAMMA_TO_DELTA.CREATED_TS))
                .from(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.DELTA_ID.in(deltaIds)
                        .and(GAMMA_TO_DELTA.CAN_USE)
                        .and(GAMMA_TO_DELTA.CREATED_TS.lessOrEqual(ts)))
                .groupBy(GAMMA_TO_DELTA.GAMMA_ID, GAMMA_TO_DELTA.DELTA_ID)
                .fetchGroups(GAMMA_TO_DELTA.DELTA_ID, GAMMA_TO_DELTA.GAMMA_ID);
    }

    @Override
    public Map<Long, List<GammaToDeltaLink>> getConnectedGammaEntitiesByDeltaIds(Collection<Long> deltaIds, long ts) {
        GammaToDelta TOP_LVL_GD = GAMMA_TO_DELTA.as("top_lvl");
        GammaToDelta LOW_LVL_GD = GAMMA_TO_DELTA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_GD)
                .whereExists(context
                        .select(LOW_LVL_GD.GAMMA_ID, LOW_LVL_GD.DELTA_ID, max(LOW_LVL_GD.CREATED_TS))
                        .from(LOW_LVL_GD)
                        .where(LOW_LVL_GD.DELTA_ID.in(deltaIds)
                                .and(LOW_LVL_GD.CAN_USE)
                                .and(LOW_LVL_GD.CREATED_TS.lessOrEqual(ts)))
                        .groupBy(LOW_LVL_GD.GAMMA_ID, LOW_LVL_GD.DELTA_ID)
                        .having(LOW_LVL_GD.GAMMA_ID.eq(TOP_LVL_GD.GAMMA_ID)
                                .and(LOW_LVL_GD.DELTA_ID.eq(TOP_LVL_GD.DELTA_ID))
                                .and(max(LOW_LVL_GD.CREATED_TS).eq(TOP_LVL_GD.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(GammaToDeltaLink::getDeltaId));
    }

    @Override
    public Map<Long, List<Long>> getConnectedDeltaEntitiesIdsByGammaIds(Collection<Long> gammaIds, long ts) {
        return context
                .select(GAMMA_TO_DELTA.GAMMA_ID, GAMMA_TO_DELTA.DELTA_ID, max(GAMMA_TO_DELTA.CREATED_TS))
                .from(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.GAMMA_ID.in(gammaIds)
                        .and(GAMMA_TO_DELTA.CAN_USE)
                        .and(GAMMA_TO_DELTA.CREATED_TS.lessOrEqual(ts)))
                .groupBy(GAMMA_TO_DELTA.GAMMA_ID, GAMMA_TO_DELTA.DELTA_ID)
                .fetchGroups(GAMMA_TO_DELTA.GAMMA_ID, GAMMA_TO_DELTA.DELTA_ID);
    }

    @Override
    public Map<Long, List<GammaToDeltaLink>> getConnectedDeltaEntitiesByGammaIds(Collection<Long> gammaIds, long ts) {
        GammaToDelta TOP_LVL_GD = GAMMA_TO_DELTA.as("top_lvl");
        GammaToDelta LOW_LVL_GD = GAMMA_TO_DELTA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_GD)
                .whereExists(context
                        .select(LOW_LVL_GD.GAMMA_ID, LOW_LVL_GD.DELTA_ID, max(LOW_LVL_GD.CREATED_TS))
                        .from(LOW_LVL_GD)
                        .where(LOW_LVL_GD.GAMMA_ID.in(gammaIds)
                                .and(LOW_LVL_GD.CAN_USE)
                                .and(LOW_LVL_GD.CREATED_TS.lessOrEqual(ts)))
                        .groupBy(LOW_LVL_GD.GAMMA_ID, LOW_LVL_GD.DELTA_ID)
                        .having(LOW_LVL_GD.GAMMA_ID.eq(TOP_LVL_GD.GAMMA_ID)
                                .and(LOW_LVL_GD.DELTA_ID.eq(TOP_LVL_GD.DELTA_ID))
                                .and(max(LOW_LVL_GD.CREATED_TS).eq(TOP_LVL_GD.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(GammaToDeltaLink::getGammaId));
    }

    @Override
    public void batchInsertNewEvents(List<GammaToDeltaEvent> events) {
        List<Row4<Long, Long, Boolean, Long>> rows =
                events.stream().map(this::covertToRow).collect(Collectors.toList());
        context.insertInto(GAMMA_TO_DELTA, GAMMA_TO_DELTA.GAMMA_ID, GAMMA_TO_DELTA.DELTA_ID,
                        GAMMA_TO_DELTA.IS_ACTIVE, GAMMA_TO_DELTA.CREATED_TS)
                .valuesOfRows(rows)
                .onDuplicateKeyIgnore()
                .execute();
    }

    private Row4<Long, Long, Boolean, Long> covertToRow(GammaToDeltaEvent event) {
        return row(event.getGammaId(), event.getDeltaId(), event.isActive(), event.getCreatedTs());
    }

    private GammaToDeltaLink convertToLink(GammaToDeltaRecord record) {
        return new GammaToDeltaLink(
                record.getGammaId(),
                record.getDeltaId()
        );
    }
}
