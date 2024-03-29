package com.st1580.diploma.external.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.st1580.diploma.repository.types.EntityActiveType;
import com.st1580.diploma.db.tables.Alpha;
import com.st1580.diploma.db.tables.AlphaToBeta;
import com.st1580.diploma.db.tables.Beta;
import com.st1580.diploma.db.tables.Delta;
import com.st1580.diploma.db.tables.Gamma;
import com.st1580.diploma.db.tables.GammaToAlpha;
import com.st1580.diploma.db.tables.GammaToDelta;
import com.st1580.diploma.external.alpha.data.entity.ExternalAlphaEntity;
import com.st1580.diploma.external.alpha.data.link.ExternalAlphaToBetaLink;
import com.st1580.diploma.external.beta.data.ExternalBetaEntity;
import com.st1580.diploma.external.delta.data.ExternalDeltaEntity;
import com.st1580.diploma.external.gamma.data.entity.ExternalGammaEntity;
import com.st1580.diploma.external.gamma.data.links.ga.ExternalGammaToAlphaLink;
import com.st1580.diploma.external.gamma.data.links.gd.ExternalGammaToDeltaLink;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.st1580.diploma.db.Tables.ALPHA;
import static com.st1580.diploma.db.Tables.ALPHA_TO_BETA;
import static com.st1580.diploma.db.Tables.BETA;
import static com.st1580.diploma.db.Tables.DELTA;
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
                                .groupBy(LOW_LVL_ALPHA.ID)
                                .having(LOW_LVL_ALPHA.ID.eq(TOP_LVL_ALPHA.ID)
                                        .and(max(LOW_LVL_ALPHA.CREATED_TS).eq(TOP_LVL_ALPHA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(record -> new ExternalAlphaEntity(
                        record.getId(),
                        record.getName(),
                        record.getActiveStatus().endsWith("TRUE")
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
    public List<ExternalBetaEntity> getAllBetaEntities() {
        Beta TOP_LVL_BETA = BETA.as("top_lvl");
        Beta LOW_LVL_BETA = BETA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_BETA)
                .whereExists(
                        context.select(LOW_LVL_BETA.ID, max(LOW_LVL_BETA.CREATED_TS))
                                .from(LOW_LVL_BETA)
                                .groupBy(LOW_LVL_BETA.ID)
                                .having(LOW_LVL_BETA.ID.eq(TOP_LVL_BETA.ID)
                                        .and(max(LOW_LVL_BETA.CREATED_TS).eq(TOP_LVL_BETA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(record -> new ExternalBetaEntity(
                        record.getId(),
                        record.getEpoch()
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
                                .groupBy(LOW_LVL_GAMMA.ID)
                                .having(LOW_LVL_GAMMA.ID.eq(TOP_LVL_GAMMA.ID)
                                        .and(max(LOW_LVL_GAMMA.CREATED_TS).eq(TOP_LVL_GAMMA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(record -> new ExternalGammaEntity(
                        record.getId(),
                        record.getIsMaster(),
                        record.getActiveStatus().endsWith("TRUE")
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

    @Override
    public List<ExternalDeltaEntity> getAllDeltaEntities() {
        Delta TOP_LVL_DELTA = DELTA.as("top_lvl");
        Delta LOW_LVL_DELTA = DELTA.as("low_lvl");

        return context
                .selectFrom(TOP_LVL_DELTA)
                .whereExists(
                        context.select(LOW_LVL_DELTA.ID, max(LOW_LVL_DELTA.CREATED_TS))
                                .from(LOW_LVL_DELTA)
                                .groupBy(LOW_LVL_DELTA.ID)
                                .having(LOW_LVL_DELTA.ID.eq(TOP_LVL_DELTA.ID)
                                        .and(max(LOW_LVL_DELTA.CREATED_TS).eq(TOP_LVL_DELTA.CREATED_TS)))
                )
                .fetch()
                .stream()
                .map(record -> new ExternalDeltaEntity(
                        record.getId(),
                        record.getName()
                ))
                .collect(Collectors.toList());
    }
}
