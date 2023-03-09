package com.st1580.diploma.collector.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.graph.links.GammaToAlphaLink;
import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import com.st1580.diploma.db.tables.GammaToAlpha;
import com.st1580.diploma.db.tables.records.GammaToAlphaRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA_TO_ALPHA;
import static org.jooq.impl.DSL.max;

@Repository
public class DbGammaToAlphaRepository implements GammaToAlphaRepository {
    @Autowired
    private DSLContext context;

    @Override
    public Map<Long, List<Long>> getConnectedGammaEntitiesIdsByAlphaIds(Collection<Long> alphaIds, long ts) {
        return context
                .select(GAMMA_TO_ALPHA.GAMMA_ID, GAMMA_TO_ALPHA.ALPHA_ID, max(GAMMA_TO_ALPHA.CREATED_TS))
                .from(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.ALPHA_ID.in(alphaIds)
                        .and(GAMMA_TO_ALPHA.IS_ACTIVE)
                        .and(GAMMA_TO_ALPHA.IS_ACTIVE_GAMMA.eq("TRUE"))
                        .and(GAMMA_TO_ALPHA.IS_ACTIVE_ALPHA.eq("TRUE"))
                        .and(GAMMA_TO_ALPHA.CREATED_TS.lessOrEqual(ts)))
                .groupBy(GAMMA_TO_ALPHA.GAMMA_ID, GAMMA_TO_ALPHA.ALPHA_ID)
                .fetchGroups(GAMMA_TO_ALPHA.ALPHA_ID, GAMMA_TO_ALPHA.GAMMA_ID);
    }

    @Override
    public Map<Long, List<GammaToAlphaLink>> getConnectedGammaEntitiesByAlphaIds(Collection<Long> alphaIds, long ts) {
        GammaToAlpha TOP_LVL_GA = GAMMA_TO_ALPHA.as("top_lvl");
        GammaToAlpha LOW_LVL_GA = GAMMA_TO_ALPHA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_GA)
                .whereExists(context
                        .select(LOW_LVL_GA.GAMMA_ID, LOW_LVL_GA.ALPHA_ID, max(LOW_LVL_GA.CREATED_TS))
                        .where(LOW_LVL_GA.ALPHA_ID.in(alphaIds)
                                .and(LOW_LVL_GA.IS_ACTIVE)
                                .and(LOW_LVL_GA.IS_ACTIVE_GAMMA.eq("TRUE"))
                                .and(LOW_LVL_GA.IS_ACTIVE_ALPHA.eq("TRUE"))
                                .and(LOW_LVL_GA.CREATED_TS.lessOrEqual(ts)))
                        .groupBy(LOW_LVL_GA.GAMMA_ID, LOW_LVL_GA.ALPHA_ID)
                        .having(LOW_LVL_GA.GAMMA_ID.eq(TOP_LVL_GA.GAMMA_ID)
                                .and(LOW_LVL_GA.ALPHA_ID.eq(TOP_LVL_GA.ALPHA_ID))
                                .and(max(LOW_LVL_GA.CREATED_TS).eq(TOP_LVL_GA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(GammaToAlphaLink::getAlphaId));
    }

    @Override
    public Map<Long, List<Long>> getConnectedAlphaEntitiesIdsByGammaIds(Collection<Long> gammaIds, long ts) {
        return context
                .select(GAMMA_TO_ALPHA.GAMMA_ID, GAMMA_TO_ALPHA.ALPHA_ID, max(GAMMA_TO_ALPHA.CREATED_TS))
                .from(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.GAMMA_ID.in(gammaIds)
                        .and(GAMMA_TO_ALPHA.IS_ACTIVE)
                        .and(GAMMA_TO_ALPHA.IS_ACTIVE_GAMMA.eq("TRUE"))
                        .and(GAMMA_TO_ALPHA.IS_ACTIVE_ALPHA.eq("TRUE"))
                        .and(GAMMA_TO_ALPHA.CREATED_TS.lessOrEqual(ts)))
                .groupBy(GAMMA_TO_ALPHA.GAMMA_ID, GAMMA_TO_ALPHA.ALPHA_ID)
                .fetchGroups(GAMMA_TO_ALPHA.GAMMA_ID, GAMMA_TO_ALPHA.ALPHA_ID);
    }

    @Override
    public Map<Long, List<GammaToAlphaLink>> getConnectedAlphaEntitiesByGammaIds(Collection<Long> gammaIds, long ts) {
        GammaToAlpha TOP_LVL_GA = GAMMA_TO_ALPHA.as("top_lvl");
        GammaToAlpha LOW_LVL_GA = GAMMA_TO_ALPHA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_GA)
                .whereExists(context
                        .select(LOW_LVL_GA.GAMMA_ID, LOW_LVL_GA.ALPHA_ID, max(LOW_LVL_GA.CREATED_TS))
                        .where(LOW_LVL_GA.GAMMA_ID.in(gammaIds)
                                .and(LOW_LVL_GA.IS_ACTIVE)
                                .and(LOW_LVL_GA.IS_ACTIVE_GAMMA.eq("TRUE"))
                                .and(LOW_LVL_GA.IS_ACTIVE_ALPHA.eq("TRUE"))
                                .and(LOW_LVL_GA.CREATED_TS.lessOrEqual(ts)))
                        .groupBy(LOW_LVL_GA.GAMMA_ID, LOW_LVL_GA.ALPHA_ID)
                        .having(LOW_LVL_GA.GAMMA_ID.eq(TOP_LVL_GA.GAMMA_ID)
                                .and(LOW_LVL_GA.ALPHA_ID.eq(TOP_LVL_GA.ALPHA_ID))
                                .and(max(LOW_LVL_GA.CREATED_TS).eq(TOP_LVL_GA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(this::convertToLink)
                .collect(Collectors.groupingBy(GammaToAlphaLink::getGammaId));
    }

    private GammaToAlphaLink convertToLink(GammaToAlphaRecord record) {
        return new GammaToAlphaLink(
                record.getGammaId(),
                record.getAlphaId(),
                record.getWeight()
        );
    }

}
