package com.st1580.diploma.collector.repository.impl;

import java.util.List;

import com.st1580.diploma.collector.repository.GammaToDeltaRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.GAMMA_TO_DELTA;

@Repository
public class DbGammaToDeltaRepository implements GammaToDeltaRepository {
    DSLContext context = DSL.using(SQLDialect.POSTGRES);

    @Override
    public List<Long> getConnectedGammaEntitiesByDelta(long deltaId) {
        return context
                .select(GAMMA_TO_DELTA.GAMMA_ID)
                .from(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.DELTA_ID.eq(deltaId))
                .fetchInto(Long.class);
    }

    @Override
    public List<Long> getConnectedDeltaEntitiesByGamma(long gammaId) {
        return context
                .select(GAMMA_TO_DELTA.DELTA_ID)
                .from(GAMMA_TO_DELTA)
                .where(GAMMA_TO_DELTA.GAMMA_ID.eq(gammaId))
                .fetchInto(Long.class);
    }
}
