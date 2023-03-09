/*
 * This file is generated by jOOQ.
 */
package com.st1580.diploma.db.tables;


import com.st1580.diploma.db.Keys;
import com.st1580.diploma.db.Public;
import com.st1580.diploma.db.tables.records.GammaToAlphaRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GammaToAlpha extends TableImpl<GammaToAlphaRecord> {

    private static final long serialVersionUID = -589883557;

    /**
     * The reference instance of <code>public.gamma_to_alpha</code>
     */
    public static final GammaToAlpha GAMMA_TO_ALPHA = new GammaToAlpha();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GammaToAlphaRecord> getRecordType() {
        return GammaToAlphaRecord.class;
    }

    /**
     * The column <code>public.gamma_to_alpha.gamma_id</code>.
     */
    public final TableField<GammaToAlphaRecord, Long> GAMMA_ID = createField(DSL.name("gamma_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.gamma_to_alpha.alpha_id</code>.
     */
    public final TableField<GammaToAlphaRecord, Long> ALPHA_ID = createField(DSL.name("alpha_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.gamma_to_alpha.weight</code>.
     */
    public final TableField<GammaToAlphaRecord, Long> WEIGHT = createField(DSL.name("weight"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.gamma_to_alpha.is_active</code>.
     */
    public final TableField<GammaToAlphaRecord, Boolean> IS_ACTIVE = createField(DSL.name("is_active"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.gamma_to_alpha.is_active_gamma</code>.
     */
    public final TableField<GammaToAlphaRecord, String> IS_ACTIVE_GAMMA = createField(DSL.name("is_active_gamma"), org.jooq.impl.SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>public.gamma_to_alpha.is_active_alpha</code>.
     */
    public final TableField<GammaToAlphaRecord, String> IS_ACTIVE_ALPHA = createField(DSL.name("is_active_alpha"), org.jooq.impl.SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>public.gamma_to_alpha.created_ts</code>.
     */
    public final TableField<GammaToAlphaRecord, Long> CREATED_TS = createField(DSL.name("created_ts"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * Create a <code>public.gamma_to_alpha</code> table reference
     */
    public GammaToAlpha() {
        this(DSL.name("gamma_to_alpha"), null);
    }

    /**
     * Create an aliased <code>public.gamma_to_alpha</code> table reference
     */
    public GammaToAlpha(String alias) {
        this(DSL.name(alias), GAMMA_TO_ALPHA);
    }

    /**
     * Create an aliased <code>public.gamma_to_alpha</code> table reference
     */
    public GammaToAlpha(Name alias) {
        this(alias, GAMMA_TO_ALPHA);
    }

    private GammaToAlpha(Name alias, Table<GammaToAlphaRecord> aliased) {
        this(alias, aliased, null);
    }

    private GammaToAlpha(Name alias, Table<GammaToAlphaRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> GammaToAlpha(Table<O> child, ForeignKey<O, GammaToAlphaRecord> key) {
        super(child, key, GAMMA_TO_ALPHA);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<GammaToAlphaRecord> getPrimaryKey() {
        return Keys.GAMMA_TO_ALPHA__PKEY;
    }

    @Override
    public List<UniqueKey<GammaToAlphaRecord>> getKeys() {
        return Arrays.<UniqueKey<GammaToAlphaRecord>>asList(Keys.GAMMA_TO_ALPHA__PKEY);
    }

    @Override
    public GammaToAlpha as(String alias) {
        return new GammaToAlpha(DSL.name(alias), this);
    }

    @Override
    public GammaToAlpha as(Name alias) {
        return new GammaToAlpha(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public GammaToAlpha rename(String name) {
        return new GammaToAlpha(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public GammaToAlpha rename(Name name) {
        return new GammaToAlpha(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, Long, Boolean, String, String, Long> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
