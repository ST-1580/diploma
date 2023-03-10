/*
 * This file is generated by jOOQ.
 */
package com.st1580.diploma.db.tables;


import com.st1580.diploma.db.Indexes;
import com.st1580.diploma.db.Keys;
import com.st1580.diploma.db.Public;
import com.st1580.diploma.db.tables.records.GammaToDeltaRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
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
public class GammaToDelta extends TableImpl<GammaToDeltaRecord> {

    private static final long serialVersionUID = -1586229490;

    /**
     * The reference instance of <code>public.gamma_to_delta</code>
     */
    public static final GammaToDelta GAMMA_TO_DELTA = new GammaToDelta();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GammaToDeltaRecord> getRecordType() {
        return GammaToDeltaRecord.class;
    }

    /**
     * The column <code>public.gamma_to_delta.gamma_id</code>.
     */
    public final TableField<GammaToDeltaRecord, Long> GAMMA_ID = createField(DSL.name("gamma_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.gamma_to_delta.delta_id</code>.
     */
    public final TableField<GammaToDeltaRecord, Long> DELTA_ID = createField(DSL.name("delta_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.gamma_to_delta.is_active</code>.
     */
    public final TableField<GammaToDeltaRecord, Boolean> IS_ACTIVE = createField(DSL.name("is_active"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.gamma_to_delta.is_active_gamma</code>.
     */
    public final TableField<GammaToDeltaRecord, String> IS_ACTIVE_GAMMA = createField(DSL.name("is_active_gamma"), org.jooq.impl.SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>public.gamma_to_delta.is_active_delta</code>.
     */
    public final TableField<GammaToDeltaRecord, String> IS_ACTIVE_DELTA = createField(DSL.name("is_active_delta"), org.jooq.impl.SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>public.gamma_to_delta.can_use</code>.
     */
    public final TableField<GammaToDeltaRecord, Boolean> CAN_USE = createField(DSL.name("can_use"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.gamma_to_delta.created_ts</code>.
     */
    public final TableField<GammaToDeltaRecord, Long> CREATED_TS = createField(DSL.name("created_ts"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * Create a <code>public.gamma_to_delta</code> table reference
     */
    public GammaToDelta() {
        this(DSL.name("gamma_to_delta"), null);
    }

    /**
     * Create an aliased <code>public.gamma_to_delta</code> table reference
     */
    public GammaToDelta(String alias) {
        this(DSL.name(alias), GAMMA_TO_DELTA);
    }

    /**
     * Create an aliased <code>public.gamma_to_delta</code> table reference
     */
    public GammaToDelta(Name alias) {
        this(alias, GAMMA_TO_DELTA);
    }

    private GammaToDelta(Name alias, Table<GammaToDeltaRecord> aliased) {
        this(alias, aliased, null);
    }

    private GammaToDelta(Name alias, Table<GammaToDeltaRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> GammaToDelta(Table<O> child, ForeignKey<O, GammaToDeltaRecord> key) {
        super(child, key, GAMMA_TO_DELTA);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.GD_DELTA_CAN_USE__INDEX, Indexes.GD_GAMMA_CAN_USE__INDEX);
    }

    @Override
    public UniqueKey<GammaToDeltaRecord> getPrimaryKey() {
        return Keys.GAMMA_TO_DELTA__PKEY;
    }

    @Override
    public List<UniqueKey<GammaToDeltaRecord>> getKeys() {
        return Arrays.<UniqueKey<GammaToDeltaRecord>>asList(Keys.GAMMA_TO_DELTA__PKEY);
    }

    @Override
    public GammaToDelta as(String alias) {
        return new GammaToDelta(DSL.name(alias), this);
    }

    @Override
    public GammaToDelta as(Name alias) {
        return new GammaToDelta(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public GammaToDelta rename(String name) {
        return new GammaToDelta(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public GammaToDelta rename(Name name) {
        return new GammaToDelta(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, Boolean, String, String, Boolean, Long> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
