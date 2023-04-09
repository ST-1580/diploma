/*
 * This file is generated by jOOQ.
 */
package com.st1580.diploma.db.tables;


import com.st1580.diploma.db.Indexes;
import com.st1580.diploma.db.Public;
import com.st1580.diploma.db.tables.records.LastSyncRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LastSync extends TableImpl<LastSyncRecord> {

    private static final long serialVersionUID = 227278457;

    /**
     * The reference instance of <code>public.last_sync</code>
     */
    public static final LastSync LAST_SYNC = new LastSync();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<LastSyncRecord> getRecordType() {
        return LastSyncRecord.class;
    }

    /**
     * The column <code>public.last_sync.name</code>.
     */
    public final TableField<LastSyncRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.last_sync.last_sync_ts</code>.
     */
    public final TableField<LastSyncRecord, Long> LAST_SYNC_TS = createField(DSL.name("last_sync_ts"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * Create a <code>public.last_sync</code> table reference
     */
    public LastSync() {
        this(DSL.name("last_sync"), null);
    }

    /**
     * Create an aliased <code>public.last_sync</code> table reference
     */
    public LastSync(String alias) {
        this(DSL.name(alias), LAST_SYNC);
    }

    /**
     * Create an aliased <code>public.last_sync</code> table reference
     */
    public LastSync(Name alias) {
        this(alias, LAST_SYNC);
    }

    private LastSync(Name alias, Table<LastSyncRecord> aliased) {
        this(alias, aliased, null);
    }

    private LastSync(Name alias, Table<LastSyncRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> LastSync(Table<O> child, ForeignKey<O, LastSyncRecord> key) {
        super(child, key, LAST_SYNC);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.LAST_SYNC_NAME__INDEX);
    }

    @Override
    public LastSync as(String alias) {
        return new LastSync(DSL.name(alias), this);
    }

    @Override
    public LastSync as(Name alias) {
        return new LastSync(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public LastSync rename(String name) {
        return new LastSync(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public LastSync rename(Name name) {
        return new LastSync(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}