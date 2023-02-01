/*
 * This file is generated by jOOQ.
 */
package db.tables.records;


import db.tables.AlphaToBeta;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AlphaToBetaRecord extends UpdatableRecordImpl<AlphaToBetaRecord> implements Record2<Long, Long> {

    private static final long serialVersionUID = 213065783;

    /**
     * Setter for <code>public.alpha_to_beta.alpha_id</code>.
     */
    public void setAlphaId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.alpha_to_beta.alpha_id</code>.
     */
    public Long getAlphaId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.alpha_to_beta.beta_id</code>.
     */
    public void setBetaId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.alpha_to_beta.beta_id</code>.
     */
    public Long getBetaId() {
        return (Long) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<Long, Long> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Long, Long> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return AlphaToBeta.ALPHA_TO_BETA.ALPHA_ID;
    }

    @Override
    public Field<Long> field2() {
        return AlphaToBeta.ALPHA_TO_BETA.BETA_ID;
    }

    @Override
    public Long component1() {
        return getAlphaId();
    }

    @Override
    public Long component2() {
        return getBetaId();
    }

    @Override
    public Long value1() {
        return getAlphaId();
    }

    @Override
    public Long value2() {
        return getBetaId();
    }

    @Override
    public AlphaToBetaRecord value1(Long value) {
        setAlphaId(value);
        return this;
    }

    @Override
    public AlphaToBetaRecord value2(Long value) {
        setBetaId(value);
        return this;
    }

    @Override
    public AlphaToBetaRecord values(Long value1, Long value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AlphaToBetaRecord
     */
    public AlphaToBetaRecord() {
        super(AlphaToBeta.ALPHA_TO_BETA);
    }

    /**
     * Create a detached, initialised AlphaToBetaRecord
     */
    public AlphaToBetaRecord(Long alphaId, Long betaId) {
        super(AlphaToBeta.ALPHA_TO_BETA);

        set(0, alphaId);
        set(1, betaId);
    }
}
