/*
 * This file is generated by jOOQ.
 */
package com.st1580.diploma.db.tables;


import com.st1580.diploma.db.Indexes;
import com.st1580.diploma.db.Keys;
import com.st1580.diploma.db.Public;
import com.st1580.diploma.db.tables.records.AlphaRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
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
public class Alpha extends TableImpl<AlphaRecord> {

    private static final long serialVersionUID = 1121680033;

    /**
     * The reference instance of <code>public.alpha</code>
     */
    public static final Alpha ALPHA = new Alpha();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AlphaRecord> getRecordType() {
        return AlphaRecord.class;
    }

    /**
     * The column <code>public.alpha.id</code>.
     */
    public final TableField<AlphaRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.alpha.name</code>.
     */
    public final TableField<AlphaRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.alpha.active_status</code>.
     */
    public final TableField<AlphaRecord, String> ACTIVE_STATUS = createField(DSL.name("active_status"), org.jooq.impl.SQLDataType.VARCHAR(24).nullable(false), this, "");

    /**
     * The column <code>public.alpha.created_ts</code>.
     */
    public final TableField<AlphaRecord, Long> CREATED_TS = createField(DSL.name("created_ts"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * Create a <code>public.alpha</code> table reference
     */
    public Alpha() {
        this(DSL.name("alpha"), null);
    }

    /**
     * Create an aliased <code>public.alpha</code> table reference
     */
    public Alpha(String alias) {
        this(DSL.name(alias), ALPHA);
    }

    /**
     * Create an aliased <code>public.alpha</code> table reference
     */
    public Alpha(Name alias) {
        this(alias, ALPHA);
    }

    private Alpha(Name alias, Table<AlphaRecord> aliased) {
        this(alias, aliased, null);
    }

    private Alpha(Name alias, Table<AlphaRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Alpha(Table<O> child, ForeignKey<O, AlphaRecord> key) {
        super(child, key, ALPHA);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.ALPHA_ACTIVE__INDEX);
    }

    @Override
    public UniqueKey<AlphaRecord> getPrimaryKey() {
        return Keys.ALPHA__PKEY;
    }

    @Override
    public List<UniqueKey<AlphaRecord>> getKeys() {
        return Arrays.<UniqueKey<AlphaRecord>>asList(Keys.ALPHA__PKEY);
    }

    @Override
    public Alpha as(String alias) {
        return new Alpha(DSL.name(alias), this);
    }

    @Override
    public Alpha as(Name alias) {
        return new Alpha(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Alpha rename(String name) {
        return new Alpha(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Alpha rename(Name name) {
        return new Alpha(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, String, String, Long> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
