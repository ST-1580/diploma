/*
 * This file is generated by jOOQ.
 */
package com.st1580.diploma.db.tables.records;


import com.st1580.diploma.db.tables.LastSync;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LastSyncRecord extends TableRecordImpl<LastSyncRecord> implements Record2<String, Long> {

    private static final long serialVersionUID = -2076909892;

    /**
     * Setter for <code>public.last_sync.name</code>.
     */
    public void setName(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.last_sync.name</code>.
     */
    public String getName() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.last_sync.last_sync_ts</code>.
     */
    public void setLastSyncTs(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.last_sync.last_sync_ts</code>.
     */
    public Long getLastSyncTs() {
        return (Long) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<String, Long> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return LastSync.LAST_SYNC.NAME;
    }

    @Override
    public Field<Long> field2() {
        return LastSync.LAST_SYNC.LAST_SYNC_TS;
    }

    @Override
    public String component1() {
        return getName();
    }

    @Override
    public Long component2() {
        return getLastSyncTs();
    }

    @Override
    public String value1() {
        return getName();
    }

    @Override
    public Long value2() {
        return getLastSyncTs();
    }

    @Override
    public LastSyncRecord value1(String value) {
        setName(value);
        return this;
    }

    @Override
    public LastSyncRecord value2(Long value) {
        setLastSyncTs(value);
        return this;
    }

    @Override
    public LastSyncRecord values(String value1, Long value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LastSyncRecord
     */
    public LastSyncRecord() {
        super(LastSync.LAST_SYNC);
    }

    /**
     * Create a detached, initialised LastSyncRecord
     */
    public LastSyncRecord(String name, Long lastSyncTs) {
        super(LastSync.LAST_SYNC);

        set(0, name);
        set(1, lastSyncTs);
    }
}
