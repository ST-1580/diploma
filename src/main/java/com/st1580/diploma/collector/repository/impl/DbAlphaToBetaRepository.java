package com.st1580.diploma.collector.repository.impl;

import java.util.List;

import com.st1580.diploma.collector.repository.AlphaToBetaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA_TO_BETA;


@Repository
public class DbAlphaToBetaRepository implements AlphaToBetaRepository {
    DSLContext context = DSL.using(SQLDialect.POSTGRES);

    @Override
    public List<Long> getConnectedBetaEntitiesByAlpha(long alphaId) {
        return context
                .select(ALPHA_TO_BETA.BETA_ID)
                .from(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.ALPHA_ID.eq(alphaId))
                .fetchInto(Long.class);
    }

    @Override
    public List<Long> getConnectedAlphaEntitiesByBeta(long betaId) {
        return context
                .select(ALPHA_TO_BETA.ALPHA_ID)
                .from(ALPHA_TO_BETA)
                .where(ALPHA_TO_BETA.BETA_ID.eq(betaId))
                .fetchInto(Long.class);
    }
}
