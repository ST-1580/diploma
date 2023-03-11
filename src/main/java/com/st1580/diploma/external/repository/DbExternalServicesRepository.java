package com.st1580.diploma.external.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.st1580.diploma.collector.repository.types.EntityActiveType;
import com.st1580.diploma.db.tables.Alpha;
import com.st1580.diploma.db.tables.AlphaToBeta;
import com.st1580.diploma.db.tables.Gamma;
import com.st1580.diploma.db.tables.GammaToAlpha;
import com.st1580.diploma.db.tables.GammaToDelta;
import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntity;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLink;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntity;
import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLink;
import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLink;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA;
import static com.st1580.diploma.db.Tables.ALPHA_TO_BETA;
import static com.st1580.diploma.db.Tables.GAMMA;
import static com.st1580.diploma.db.Tables.GAMMA_TO_ALPHA;
import static com.st1580.diploma.db.Tables.GAMMA_TO_DELTA;
import static org.jooq.impl.DSL.max;

@Repository
public class DbExternalServicesRepository implements ExternalServicesRepository {
    @Autowired
    private DSLContext context;

    @Override
    public List<ExternalAlphaEntity> getAllAlphaEntities() {
        Alpha TOP_LVL_ALPHA = ALPHA.as("top_lvl");
        Alpha LOW_LVL_ALPHA = ALPHA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_ALPHA)
                .whereExists(
                        context.select(LOW_LVL_ALPHA.ID, max(LOW_LVL_ALPHA.CREATED_TS))
                                .from(LOW_LVL_ALPHA)
                                .where(LOW_LVL_ALPHA.IS_ACTIVE.in(EntityActiveType.trueEntityActiveTypes))
                                .groupBy(LOW_LVL_ALPHA.ID)
                                .having(LOW_LVL_ALPHA.ID.eq(TOP_LVL_ALPHA.ID)
                                        .and(max(LOW_LVL_ALPHA.CREATED_TS).eq(TOP_LVL_ALPHA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(record -> new ExternalAlphaEntity(
                        record.getId(),
                        record.getName(),
                        record.getIsActive().endsWith("TRUE")
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExternalAlphaToBetaLink> getAllAlphaToBetaLinks() {
        AlphaToBeta TOP_LVL_AB = ALPHA_TO_BETA.as("top_lvl");
        AlphaToBeta LOW_LVL_AB = ALPHA_TO_BETA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_AB)
                .whereExists(context
                        .select(LOW_LVL_AB.ALPHA_ID, LOW_LVL_AB.BETA_ID, max(LOW_LVL_AB.CREATED_TS))
                        .from(LOW_LVL_AB)
                        .where(LOW_LVL_AB.CAN_USE)
                        .groupBy(LOW_LVL_AB.ALPHA_ID, LOW_LVL_AB.BETA_ID)
                        .having(LOW_LVL_AB.ALPHA_ID.eq(TOP_LVL_AB.ALPHA_ID)
                                .and(LOW_LVL_AB.BETA_ID.eq(TOP_LVL_AB.BETA_ID))
                                .and(max(LOW_LVL_AB.CREATED_TS).eq(TOP_LVL_AB.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(record -> new ExternalAlphaToBetaLink(
                        record.getAlphaId(),
                        record.getBetaId(),
                        record.getHash(),
                        record.getIsActive()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExternalGammaEntity> getAllGammaEntities() {
        Gamma TOP_LVL_GAMMA = GAMMA.as("top_lvl");
        Gamma LOW_LVL_GAMMA = GAMMA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_GAMMA)
                .whereExists(
                        context.select(LOW_LVL_GAMMA.ID, max(LOW_LVL_GAMMA.CREATED_TS))
                                .from(LOW_LVL_GAMMA)
                                .where(LOW_LVL_GAMMA.IS_ACTIVE.in(EntityActiveType.trueEntityActiveTypes))
                                .groupBy(LOW_LVL_GAMMA.ID)
                                .having(LOW_LVL_GAMMA.ID.eq(TOP_LVL_GAMMA.ID)
                                        .and(max(LOW_LVL_GAMMA.CREATED_TS).eq(TOP_LVL_GAMMA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(record -> new ExternalGammaEntity(
                        record.getId(),
                        record.getIsMaster(),
                        "random string number" + (int) (Math.random() * 10),
                        record.getIsActive().endsWith("TRUE")
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExternalGammaToAlphaLink> getAllGammaToAlphaLinks() {
        GammaToAlpha TOP_LVL_GA = GAMMA_TO_ALPHA.as("top_lvl");
        GammaToAlpha LOW_LVL_GA = GAMMA_TO_ALPHA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_GA)
                .whereExists(context
                        .select(LOW_LVL_GA.GAMMA_ID, LOW_LVL_GA.ALPHA_ID, max(LOW_LVL_GA.CREATED_TS))
                        .from(LOW_LVL_GA)
                        .where(LOW_LVL_GA.CAN_USE)
                        .groupBy(LOW_LVL_GA.GAMMA_ID, LOW_LVL_GA.ALPHA_ID)
                        .having(LOW_LVL_GA.GAMMA_ID.eq(TOP_LVL_GA.GAMMA_ID)
                                .and(LOW_LVL_GA.ALPHA_ID.eq(TOP_LVL_GA.ALPHA_ID))
                                .and(max(LOW_LVL_GA.CREATED_TS).eq(TOP_LVL_GA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(record -> new ExternalGammaToAlphaLink(
                        record.getGammaId(),
                        record.getAlphaId(),
                        record.getWeight(),
                        record.getIsActive()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExternalGammaToDeltaLink> getAllGammaToDeltaLinks() {
        GammaToDelta TOP_LVL_GD = GAMMA_TO_DELTA.as("top_lvl");
        GammaToDelta LOW_LVL_GD = GAMMA_TO_DELTA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_GD)
                .whereExists(context
                        .select(LOW_LVL_GD.GAMMA_ID, LOW_LVL_GD.DELTA_ID, max(LOW_LVL_GD.CREATED_TS))
                        .from(LOW_LVL_GD)
                        .where(LOW_LVL_GD.CAN_USE)
                        .groupBy(LOW_LVL_GD.GAMMA_ID, LOW_LVL_GD.DELTA_ID)
                        .having(LOW_LVL_GD.GAMMA_ID.eq(TOP_LVL_GD.GAMMA_ID)
                                .and(LOW_LVL_GD.DELTA_ID.eq(TOP_LVL_GD.DELTA_ID))
                                .and(max(LOW_LVL_GD.CREATED_TS).eq(TOP_LVL_GD.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(record -> new ExternalGammaToDeltaLink(
                        record.getGammaId(),
                        record.getDeltaId(),
                        record.getIsActive()
                ))
                .collect(Collectors.toList());
    }
}
