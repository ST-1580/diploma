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
import com.st1580.diploma.db.tables.LastSync;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables of the <code>public</code> schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index ALPHA_ACTIVE__INDEX = Indexes0.ALPHA_ACTIVE__INDEX;
    public static final Index AB_ALPHA_CAN_USE__INDEX = Indexes0.AB_ALPHA_CAN_USE__INDEX;
    public static final Index AB_ALPHA_IS_ACTIVE__INDEX = Indexes0.AB_ALPHA_IS_ACTIVE__INDEX;
    public static final Index AB_BETA_CAN_USE__INDEX = Indexes0.AB_BETA_CAN_USE__INDEX;
    public static final Index AB_BETA_IS_ACTIVE__INDEX = Indexes0.AB_BETA_IS_ACTIVE__INDEX;
    public static final Index BETA_ACTIVE__INDEX = Indexes0.BETA_ACTIVE__INDEX;
    public static final Index DELTA_ACTIVE__INDEX = Indexes0.DELTA_ACTIVE__INDEX;
    public static final Index FLYWAY_SCHEMA_HISTORY_S_IDX = Indexes0.FLYWAY_SCHEMA_HISTORY_S_IDX;
    public static final Index GAMMA_ACTIVE__INDEX = Indexes0.GAMMA_ACTIVE__INDEX;
    public static final Index GA_ALPHA_CAN_USE__INDEX = Indexes0.GA_ALPHA_CAN_USE__INDEX;
    public static final Index GA_ALPHA_IS_ACTIVE__INDEX = Indexes0.GA_ALPHA_IS_ACTIVE__INDEX;
    public static final Index GA_GAMMA_CAN_USE__INDEX = Indexes0.GA_GAMMA_CAN_USE__INDEX;
    public static final Index GA_GAMMA_IS_ACTIVE__INDEX = Indexes0.GA_GAMMA_IS_ACTIVE__INDEX;
    public static final Index GD_DELTA_CAN_USE__INDEX = Indexes0.GD_DELTA_CAN_USE__INDEX;
    public static final Index GD_DELTA_IS_ACTIVE__INDEX = Indexes0.GD_DELTA_IS_ACTIVE__INDEX;
    public static final Index GD_GAMMA_CAN_USE__INDEX = Indexes0.GD_GAMMA_CAN_USE__INDEX;
    public static final Index GD_GAMMA_IS_ACTIVE__INDEX = Indexes0.GD_GAMMA_IS_ACTIVE__INDEX;
    public static final Index LAST_SYNC_NAME__INDEX = Indexes0.LAST_SYNC_NAME__INDEX;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index ALPHA_ACTIVE__INDEX = Internal.createIndex("alpha_active__index", Alpha.ALPHA, new OrderField[] { Alpha.ALPHA.ID, Alpha.ALPHA.CREATED_TS, Alpha.ALPHA.ACTIVE_STATUS }, true);
        public static Index AB_ALPHA_CAN_USE__INDEX = Internal.createIndex("ab_alpha_can_use__index", AlphaToBeta.ALPHA_TO_BETA, new OrderField[] { AlphaToBeta.ALPHA_TO_BETA.ALPHA_ID, AlphaToBeta.ALPHA_TO_BETA.CREATED_TS }, false);
        public static Index AB_ALPHA_IS_ACTIVE__INDEX = Internal.createIndex("ab_alpha_is_active__index", AlphaToBeta.ALPHA_TO_BETA, new OrderField[] { AlphaToBeta.ALPHA_TO_BETA.CREATED_TS, AlphaToBeta.ALPHA_TO_BETA.IS_ACTIVE_ALPHA }, false);
        public static Index AB_BETA_CAN_USE__INDEX = Internal.createIndex("ab_beta_can_use__index", AlphaToBeta.ALPHA_TO_BETA, new OrderField[] { AlphaToBeta.ALPHA_TO_BETA.BETA_ID, AlphaToBeta.ALPHA_TO_BETA.CREATED_TS }, false);
        public static Index AB_BETA_IS_ACTIVE__INDEX = Internal.createIndex("ab_beta_is_active__index", AlphaToBeta.ALPHA_TO_BETA, new OrderField[] { AlphaToBeta.ALPHA_TO_BETA.CREATED_TS, AlphaToBeta.ALPHA_TO_BETA.IS_ACTIVE_BETA }, false);
        public static Index BETA_ACTIVE__INDEX = Internal.createIndex("beta_active__index", Beta.BETA, new OrderField[] { Beta.BETA.ID, Beta.BETA.CREATED_TS, Beta.BETA.ACTIVE_STATUS }, true);
        public static Index DELTA_ACTIVE__INDEX = Internal.createIndex("delta_active__index", Delta.DELTA, new OrderField[] { Delta.DELTA.ID, Delta.DELTA.CREATED_TS, Delta.DELTA.ACTIVE_STATUS }, true);
        public static Index FLYWAY_SCHEMA_HISTORY_S_IDX = Internal.createIndex("flyway_schema_history_s_idx", FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, new OrderField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.SUCCESS }, false);
        public static Index GAMMA_ACTIVE__INDEX = Internal.createIndex("gamma_active__index", Gamma.GAMMA, new OrderField[] { Gamma.GAMMA.ID, Gamma.GAMMA.CREATED_TS, Gamma.GAMMA.ACTIVE_STATUS }, true);
        public static Index GA_ALPHA_CAN_USE__INDEX = Internal.createIndex("ga_alpha_can_use__index", GammaToAlpha.GAMMA_TO_ALPHA, new OrderField[] { GammaToAlpha.GAMMA_TO_ALPHA.ALPHA_ID, GammaToAlpha.GAMMA_TO_ALPHA.CREATED_TS }, false);
        public static Index GA_ALPHA_IS_ACTIVE__INDEX = Internal.createIndex("ga_alpha_is_active__index", GammaToAlpha.GAMMA_TO_ALPHA, new OrderField[] { GammaToAlpha.GAMMA_TO_ALPHA.CREATED_TS, GammaToAlpha.GAMMA_TO_ALPHA.IS_ACTIVE_ALPHA }, false);
        public static Index GA_GAMMA_CAN_USE__INDEX = Internal.createIndex("ga_gamma_can_use__index", GammaToAlpha.GAMMA_TO_ALPHA, new OrderField[] { GammaToAlpha.GAMMA_TO_ALPHA.GAMMA_ID, GammaToAlpha.GAMMA_TO_ALPHA.CREATED_TS }, false);
        public static Index GA_GAMMA_IS_ACTIVE__INDEX = Internal.createIndex("ga_gamma_is_active__index", GammaToAlpha.GAMMA_TO_ALPHA, new OrderField[] { GammaToAlpha.GAMMA_TO_ALPHA.CREATED_TS, GammaToAlpha.GAMMA_TO_ALPHA.IS_ACTIVE_GAMMA }, false);
        public static Index GD_DELTA_CAN_USE__INDEX = Internal.createIndex("gd_delta_can_use__index", GammaToDelta.GAMMA_TO_DELTA, new OrderField[] { GammaToDelta.GAMMA_TO_DELTA.DELTA_ID, GammaToDelta.GAMMA_TO_DELTA.CREATED_TS }, false);
        public static Index GD_DELTA_IS_ACTIVE__INDEX = Internal.createIndex("gd_delta_is_active__index", GammaToDelta.GAMMA_TO_DELTA, new OrderField[] { GammaToDelta.GAMMA_TO_DELTA.CREATED_TS, GammaToDelta.GAMMA_TO_DELTA.IS_ACTIVE_DELTA }, false);
        public static Index GD_GAMMA_CAN_USE__INDEX = Internal.createIndex("gd_gamma_can_use__index", GammaToDelta.GAMMA_TO_DELTA, new OrderField[] { GammaToDelta.GAMMA_TO_DELTA.GAMMA_ID, GammaToDelta.GAMMA_TO_DELTA.CREATED_TS }, false);
        public static Index GD_GAMMA_IS_ACTIVE__INDEX = Internal.createIndex("gd_gamma_is_active__index", GammaToDelta.GAMMA_TO_DELTA, new OrderField[] { GammaToDelta.GAMMA_TO_DELTA.CREATED_TS, GammaToDelta.GAMMA_TO_DELTA.IS_ACTIVE_GAMMA }, false);
        public static Index LAST_SYNC_NAME__INDEX = Internal.createIndex("last_sync_name__index", LastSync.LAST_SYNC, new OrderField[] { LastSync.LAST_SYNC.NAME }, false);
    }
}
