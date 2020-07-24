/**
 * TxnCredential.java
 * Copyright (c) 2009, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.ese.entity.credential;

import java.text.DecimalFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.sourcetrace.eses.entity.TransactionType;
import com.sourcetrace.esesw.entity.profile.Currency;

// TODO: Auto-generated Javadoc
/**
 * The Class TxnCredential.
 * @author $Author: aravind $
 * @version $Rev: 272 $
 */
public class TxnCredential {

    private long id;
    private long txnPerDay;
    private double amountPerDay;
    private long txnPerSession;
    private double amountPerSession;
    private Currency currency;
    private TransactionType transactionType;
    DecimalFormat decimal = new DecimalFormat("0.00");

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the txn per day.
     * @return the txn per day
     */
    @Min(value = 0, message = "min.txnPerDay")
    @Max(value = 1000, message = "range.txnPerDay")
    public long getTxnPerDay() {

        return txnPerDay;
    }

    /**
     * Sets the txn per day.
     * @param txnPerDay the new txn per day
     */
    public void setTxnPerDay(long txnPerDay) {

        this.txnPerDay = txnPerDay;
    }

    /**
     * Gets the amount per day.
     * @return the amount per day
     */
    @Min(value = 0, message = "min.amountPerDay")
    @Max(value = 1000000, message = "range.amountPerDay")
    public double getAmountPerDay() {

        return amountPerDay;
    }

    /**
     * Sets the amount per day.
     * @param amountPerDay the new amount per day
     */
    public void setAmountPerDay(double amountPerDay) {

        this.amountPerDay = Double.valueOf(decimal.format(amountPerDay));
    }

    /**
     * Gets the txn per session.
     * @return the txn per session
     */
    @Min(value = 0, message = "min.txnPerSession")
    @Max(value = 1000, message = "range.txnPerSession")
    public long getTxnPerSession() {

        return txnPerSession;
    }

    /**
     * Sets the txn per session.
     * @param txnPerSession the new txn per session
     */
    public void setTxnPerSession(long txnPerSession) {

        this.txnPerSession = txnPerSession;
    }

    /**
     * Gets the amount per session.
     * @return the amount per session
     */
    @Min(value = 0, message = "min.amountPerSession")
    @Max(value = 1000000, message = "range.amountPerSession")
    public double getAmountPerSession() {

        return amountPerSession;
    }

    /**
     * Sets the amount per session.
     * @param amountPerSession the new amount per session
     */
    public void setAmountPerSession(double amountPerSession) {

        this.amountPerSession = Double.valueOf(decimal.format(amountPerSession));
    }

    /**
     * Gets the currency.
     * @return the currency
     */
    public Currency getCurrency() {

        return currency;
    }

    /**
     * Sets the currency.
     * @param currency the new currency
     */
    public void setCurrency(Currency currency) {

        this.currency = currency;
    }

    /**
     * Gets the transaction type.
     * @return the transaction type
     */
    public TransactionType getTransactionType() {

        return transactionType;
    }

    /**
     * Sets the transaction type.
     * @param transactionType the new transaction type
     */
    public void setTransactionType(TransactionType transactionType) {

        this.transactionType = transactionType;
    }

}
