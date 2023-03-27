/*
 * This file is generated by jOOQ.
 */
package com.st1580.diploma.db;


import com.st1580.diploma.db.tables.Alpha;
import com.st1580.diploma.db.tables.AlphaToBeta;
import com.st1580.diploma.db.tables.Beta;
import com.st1580.diploma.db.tables.Delta;
import com.st1580.diploma.db.tables.FlywaySchemaHistory;
import com.st1580.diploma.db.tables.Gamma;
import com.st1580.diploma.db.tables.GammaToAlpha;
import com.st1580.diploma.db.tables.GammaToDelta;
import com.st1580.diploma.db.tables.records.AlphaRecord;
import com.st1580.diploma.db.tables.records.AlphaToBetaRecord;
import com.st1580.diploma.db.tables.records.BetaRecord;
import com.st1580.diploma.db.tables.records.DeltaRecord;
import com.st1580.diploma.db.tables.records.FlywaySchemaHistoryRecord;
import com.st1580.diploma.db.tables.records.GammaRecord;
import com.st1580.diploma.db.tables.records.GammaToAlphaRecord;
import com.st1580.diploma.db.tables.records.GammaToDeltaRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AlphaRecord> ALPHA__PKEY = UniqueKeys0.ALPHA__PKEY;
    public static final UniqueKey<AlphaToBetaRecord> ALPHA_TO_BETA__PKEY = UniqueKeys0.ALPHA_TO_BETA__PKEY;
    public static final UniqueKey<BetaRecord> BETA__PKEY = UniqueKeys0.BETA__PKEY;
    public static final UniqueKey<DeltaRecord> DELTA__PKEY = UniqueKeys0.DELTA__PKEY;
    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = UniqueKeys0.FLYWAY_SCHEMA_HISTORY_PK;
    public static final UniqueKey<GammaRecord> GAMMA__PKEY = UniqueKeys0.GAMMA__PKEY;
    public static final UniqueKey<GammaToAlphaRecord> GAMMA_TO_ALPHA__PKEY = UniqueKeys0.GAMMA_TO_ALPHA__PKEY;
    public static final UniqueKey<GammaToDeltaRecord> GAMMA_TO_DELTA__PKEY = UniqueKeys0.GAMMA_TO_DELTA__PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 {
        public static final UniqueKey<AlphaRecord> ALPHA__PKEY = Internal.createUniqueKey(Alpha.ALPHA, "alpha__pkey", new TableField[] { Alpha.ALPHA.ID, Alpha.ALPHA.CREATED_TS }, true);
        public static final UniqueKey<AlphaToBetaRecord> ALPHA_TO_BETA__PKEY = Internal.createUniqueKey(AlphaToBeta.ALPHA_TO_BETA, "alpha_to_beta__pkey", new TableField[] { AlphaToBeta.ALPHA_TO_BETA.ALPHA_ID, AlphaToBeta.ALPHA_TO_BETA.BETA_ID, AlphaToBeta.ALPHA_TO_BETA.CREATED_TS }, true);
        public static final UniqueKey<BetaRecord> BETA__PKEY = Internal.createUniqueKey(Beta.BETA, "beta__pkey", new TableField[] { Beta.BETA.ID, Beta.BETA.CREATED_TS }, true);
        public static final UniqueKey<DeltaRecord> DELTA__PKEY = Internal.createUniqueKey(Delta.DELTA, "delta__pkey", new TableField[] { Delta.DELTA.ID, Delta.DELTA.CREATED_TS }, true);
        public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, "flyway_schema_history_pk", new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
        public static final UniqueKey<GammaRecord> GAMMA__PKEY = Internal.createUniqueKey(Gamma.GAMMA, "gamma__pkey", new TableField[] { Gamma.GAMMA.ID, Gamma.GAMMA.CREATED_TS }, true);
        public static final UniqueKey<GammaToAlphaRecord> GAMMA_TO_ALPHA__PKEY = Internal.createUniqueKey(GammaToAlpha.GAMMA_TO_ALPHA, "gamma_to_alpha__pkey", new TableField[] { GammaToAlpha.GAMMA_TO_ALPHA.GAMMA_ID, GammaToAlpha.GAMMA_TO_ALPHA.ALPHA_ID, GammaToAlpha.GAMMA_TO_ALPHA.CREATED_TS }, true);
        public static final UniqueKey<GammaToDeltaRecord> GAMMA_TO_DELTA__PKEY = Internal.createUniqueKey(GammaToDelta.GAMMA_TO_DELTA, "gamma_to_delta__pkey", new TableField[] { GammaToDelta.GAMMA_TO_DELTA.GAMMA_ID, GammaToDelta.GAMMA_TO_DELTA.DELTA_ID, GammaToDelta.GAMMA_TO_DELTA.CREATED_TS }, true);
    }
}