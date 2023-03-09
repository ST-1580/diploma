/*
 * This file is generated by jOOQ.
 */
package com.st1580.diploma.db.tables;


import com.st1580.diploma.db.Keys;
import com.st1580.diploma.db.Public;
import com.st1580.diploma.db.tables.records.AlphaToBetaRecord;

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
public class AlphaToBeta extends TableImpl<AlphaToBetaRecord> {

    private static final long serialVersionUID = 2062995660;

    /**
     * The reference instance of <code>public.alpha_to_beta</code>
     */
    public static final AlphaToBeta ALPHA_TO_BETA = new AlphaToBeta();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AlphaToBetaRecord> getRecordType() {
        return AlphaToBetaRecord.class;
    }

    /**
     * The column <code>public.alpha_to_beta.alpha_id</code>.
     */
    public final TableField<AlphaToBetaRecord, Long> ALPHA_ID = createField(DSL.name("alpha_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.alpha_to_beta.beta_id</code>.
     */
    public final TableField<AlphaToBetaRecord, Long> BETA_ID = createField(DSL.name("beta_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.alpha_to_beta.hash</code>.
     */
    public final TableField<AlphaToBetaRecord, String> HASH = createField(DSL.name("hash"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false).defaultValue(org.jooq.impl.DSL.field("'hash'::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.alpha_to_beta.is_active</code>.
     */
    public final TableField<AlphaToBetaRecord, Boolean> IS_ACTIVE = createField(DSL.name("is_active"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.alpha_to_beta.is_active_alpha</code>.
     */
    public final TableField<AlphaToBetaRecord, String> IS_ACTIVE_ALPHA = createField(DSL.name("is_active_alpha"), org.jooq.impl.SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>public.alpha_to_beta.is_active_beta</code>.
     */
    public final TableField<AlphaToBetaRecord, String> IS_ACTIVE_BETA = createField(DSL.name("is_active_beta"), org.jooq.impl.SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>public.alpha_to_beta.created_ts</code>.
     */
    public final TableField<AlphaToBetaRecord, Long> CREATED_TS = createField(DSL.name("created_ts"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * Create a <code>public.alpha_to_beta</code> table reference
     */
    public AlphaToBeta() {
        this(DSL.name("alpha_to_beta"), null);
    }

    /**
     * Create an aliased <code>public.alpha_to_beta</code> table reference
     */
    public AlphaToBeta(String alias) {
        this(DSL.name(alias), ALPHA_TO_BETA);
    }

    /**
     * Create an aliased <code>public.alpha_to_beta</code> table reference
     */
    public AlphaToBeta(Name alias) {
        this(alias, ALPHA_TO_BETA);
    }

    private AlphaToBeta(Name alias, Table<AlphaToBetaRecord> aliased) {
        this(alias, aliased, null);
    }

    private AlphaToBeta(Name alias, Table<AlphaToBetaRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> AlphaToBeta(Table<O> child, ForeignKey<O, AlphaToBetaRecord> key) {
        super(child, key, ALPHA_TO_BETA);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<AlphaToBetaRecord> getPrimaryKey() {
        return Keys.ALPHA_TO_BETA__PKEY;
    }

    @Override
    public List<UniqueKey<AlphaToBetaRecord>> getKeys() {
        return Arrays.<UniqueKey<AlphaToBetaRecord>>asList(Keys.ALPHA_TO_BETA__PKEY);
    }

    @Override
    public AlphaToBeta as(String alias) {
        return new AlphaToBeta(DSL.name(alias), this);
    }

    @Override
    public AlphaToBeta as(Name alias) {
        return new AlphaToBeta(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public AlphaToBeta rename(String name) {
        return new AlphaToBeta(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AlphaToBeta rename(Name name) {
        return new AlphaToBeta(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, String, Boolean, String, String, Long> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
