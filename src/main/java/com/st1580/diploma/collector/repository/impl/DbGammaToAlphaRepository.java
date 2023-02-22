package com.st1580.diploma.collector.repository.impl;

import java.util.List;

import com.st1580.diploma.collector.repository.GammaToAlphaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA_TO_ALPHA;

@Repository
public class DbGammaToAlphaRepository implements GammaToAlphaRepository {
    DSLContext context = DSL.using(SQLDialect.POSTGRES);

    @Override
    public List<Long> getConnectedGammaEntitiesByAlpha(long alphaId) {
        return context
                .select(GAMMA_TO_ALPHA.GAMMA_ID)
                .from(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.ALPHA_ID.eq(alphaId))
                .fetchInto(Long.class);
    }

    @Override
    public List<Long> getConnectedAlphaEntitiesByGamma(long gammaId) {
        return context
                .select(GAMMA_TO_ALPHA.ALPHA_ID)
                .from(GAMMA_TO_ALPHA)
                .where(GAMMA_TO_ALPHA.GAMMA_ID.eq(gammaId))
                .fetchInto(Long.class);
    }
}
