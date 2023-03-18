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


/**
 * Convenience access to all tables in public
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.alpha</code>.
     */
    public static final Alpha ALPHA = Alpha.ALPHA;

    /**
     * The table <code>public.alpha_to_beta</code>.
     */
    public static final AlphaToBeta ALPHA_TO_BETA = AlphaToBeta.ALPHA_TO_BETA;

    /**
     * The table <code>public.beta</code>.
     */
    public static final Beta BETA = Beta.BETA;

    /**
     * The table <code>public.delta</code>.
     */
    public static final Delta DELTA = Delta.DELTA;

    /**
     * The table <code>public.flyway_schema_history</code>.
     */
    public static final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>public.gamma</code>.
     */
    public static final Gamma GAMMA = Gamma.GAMMA;

    /**
     * The table <code>public.gamma_to_alpha</code>.
     */
    public static final GammaToAlpha GAMMA_TO_ALPHA = GammaToAlpha.GAMMA_TO_ALPHA;

    /**
     * The table <code>public.gamma_to_delta</code>.
     */
    public static final GammaToDelta GAMMA_TO_DELTA = GammaToDelta.GAMMA_TO_DELTA;

    /**
     * The table <code>public.last_sync</code>.
     */
    public static final LastSync LAST_SYNC = LastSync.LAST_SYNC;
}
