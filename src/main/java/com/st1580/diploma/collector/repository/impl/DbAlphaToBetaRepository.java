package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.links.AlphaToBetaLink;
import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import com.st1580.diploma.db.tables.AlphaToBeta;
import com.st1580.diploma.db.tables.records.AlphaToBetaRecord;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.SelectHavingStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA_TO_BETA;
import static org.jooq.impl.DSL.max;

@Repository
public class DbAlphaToBetaRepository implements AlphaToBetaRepository {
    @Autowired
    private DSLContext context;

    @Override
    public Map<Long, List<Long>> getConnectedBetaEntitiesIdsByAlphaIds(Collection<Long> alphaIds, long ts) {
        return context
                .select(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID, max(ALPHA_TO_BETA.CREATED_TS))
                .from(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.ALPHA_ID.in(alphaIds)
                        .and(ALPHA_TO_BETA.IS_ACTIVE)
                        .and(ALPHA_TO_BETA.IS_ACTIVE_ALPHA.eq("TRUE"))
                        .and(ALPHA_TO_BETA.IS_ACTIVE_BETA.eq("TRUE"))
                        .and(ALPHA_TO_BETA.CREATED_TS.lessOrEqual(ts)))
                .groupBy(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID)
                .fetchGroups(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID);
    }

    @Override
    public Map<Long, List<AlphaToBetaLink>> getConnectedBetaEntitiesByAlphaIds(Collection<Long> alphaIds, long ts) {
        AlphaToBeta TOP_LVL_AB = ALPHA_TO_BETA.as("top_lvl");
        AlphaToBeta LOW_LVL_AB = ALPHA_TO_BETA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_AB)
                .whereExists(context
                        .select(LOW_LVL_AB.ALPHA_ID, LOW_LVL_AB.BETA_ID, max(LOW_LVL_AB.CREATED_TS))
                        .where(LOW_LVL_AB.ALPHA_ID.in(alphaIds)
                                .and(LOW_LVL_AB.IS_ACTIVE)
                                .and(LOW_LVL_AB.IS_ACTIVE_ALPHA.eq("TRUE"))
                                .and(LOW_LVL_AB.IS_ACTIVE_BETA.eq("TRUE"))
                                .and(LOW_LVL_AB.CREATED_TS.lessOrEqual(ts)))
                        .groupBy(LOW_LVL_AB.ALPHA_ID, LOW_LVL_AB.BETA_ID)
                        .having(LOW_LVL_AB.ALPHA_ID.eq(TOP_LVL_AB.ALPHA_ID)
                                .and(LOW_LVL_AB.BETA_ID.eq(TOP_LVL_AB.BETA_ID))
                                .and(max(LOW_LVL_AB.CREATED_TS).eq(TOP_LVL_AB.CREATED_TS)))
                )
                .fetch()
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
                        .and(ALPHA_TO_BETA.IS_ACTIVE)
                        .and(ALPHA_TO_BETA.IS_ACTIVE_ALPHA.eq("TRUE"))
                        .and(ALPHA_TO_BETA.IS_ACTIVE_BETA.eq("TRUE"))
                        .and(ALPHA_TO_BETA.CREATED_TS.lessOrEqual(ts)))
                .groupBy(ALPHA_TO_BETA.ALPHA_ID, ALPHA_TO_BETA.BETA_ID)
                .fetchGroups(ALPHA_TO_BETA.BETA_ID, ALPHA_TO_BETA.ALPHA_ID);
    }

    @Override
    public Map<Long, List<AlphaToBetaLink>> getConnectedAlphaEntitiesByBetaIds(Collection<Long> betaIds, long ts) {
        AlphaToBeta TOP_LVL_AB = ALPHA_TO_BETA.as("top_lvl");
        AlphaToBeta LOW_LVL_AB = ALPHA_TO_BETA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_AB)
                .whereExists(context
                        .select(LOW_LVL_AB.ALPHA_ID, LOW_LVL_AB.BETA_ID, max(LOW_LVL_AB.CREATED_TS))
                        .where(LOW_LVL_AB.BETA_ID.in(betaIds)
                                .and(LOW_LVL_AB.IS_ACTIVE)
                                .and(LOW_LVL_AB.IS_ACTIVE_ALPHA.eq("TRUE"))
                                .and(LOW_LVL_AB.IS_ACTIVE_BETA.eq("TRUE"))
                                .and(LOW_LVL_AB.CREATED_TS.lessOrEqual(ts)))
                        .groupBy(LOW_LVL_AB.ALPHA_ID, LOW_LVL_AB.BETA_ID)
                        .having(LOW_LVL_AB.ALPHA_ID.eq(TOP_LVL_AB.ALPHA_ID)
                                .and(LOW_LVL_AB.BETA_ID.eq(TOP_LVL_AB.BETA_ID))
                                .and(max(LOW_LVL_AB.CREATED_TS).eq(TOP_LVL_AB.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(AlphaToBetaLink::getBetaId));
    }

    private AlphaToBetaLink convertToLink(AlphaToBetaRecord record) {
        return new AlphaToBetaLink(
                record.getAlphaId(),
                record.getBetaId(),
                record.getHash()
        );
    }
}
