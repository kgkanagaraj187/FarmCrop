/**
 * TxnCommissionFee.java
 * Copyright (c) 2008-2009, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.ese.entity.txn.fee;

import java.util.Currency;
import java.util.Date;

import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.TransactionType;

// TODO: Auto-generated Javadoc
/**
 * The Class TxnCommissionFee.
 * @author $Author: aravind $
 * @version $Rev: 59 $, $Date: 2009-08-04 17:10:51 +0530 (Tue, 04 Aug 2009) $
 */
public class TxnCommissionFee {

    public static final int AGENT = 0;
    public static final int CLIENT = 1;

    public static final int CURRENT = 0;
    public static final int PENDING = 1;
    public static final int READY_TO_POST = 2;
    public static final int SETTLED = 3;

    private long id;
    private int type;
    private long paymentRefernceId;
    private long txnCount;
    private double commRate;
    private double commAmount;
    private Currency currency;
    private int status;
    private Date commDate;
    private Profile profile;
    private TransactionType txnType;
    private CommissionPayment payment;

    /**
     * Gets the payment.
     * @return the payment
     */
    public CommissionPayment getPayment() {

        return payment;
    }

    /**
     * Sets the payment.
     * @param payment the new payment
     */
    public void setPayment(CommissionPayment payment) {

        this.payment = payment;
    }

    /**
     * Gets the profile.
     * @return the profile
     */
    public Profile getProfile() {

        return profile;
    }

    /**
     * Sets the profile.
     * @param profile the new profile
     */
    public void setProfile(Profile profile) {

        this.profile = profile;
    }

    /**
     * Gets the txn type.
     * @return the txn type
     */
    public TransactionType getTxnType() {

        return txnType;
    }

    /**
     * Sets the txn type.
     * @param txnType the new txn type
     */
    public void setTxnType(TransactionType txnType) {

        this.txnType = txnType;
    }

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
     * Gets the txn count.
     * @return the txn count
     */
    public long getTxnCount() {

        return txnCount;
    }

    /**
     * Sets the txn count.
     * @param txnCount the new txn count
     */
    public void setTxnCount(long txnCount) {

        this.txnCount = txnCount;
    }

    /**
     * Gets the comm rate.
     * @return the comm rate
     */
    public double getCommRate() {

        return commRate;
    }

    /**
     * Sets the comm rate.
     * @param commRate the new comm rate
     */
    public void setCommRate(double commRate) {

        this.commRate = commRate;
    }

    /**
     * Gets the comm amount.
     * @return the comm amount
     */
    public double getCommAmount() {

        return commAmount;
    }

    /**
     * Sets the comm amount.
     * @param commAmount the new comm amount
     */
    public void setCommAmount(double commAmount) {

        this.commAmount = commAmount;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public int getStatus() {

        return status;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(int status) {

        this.status = status;
    }

    /**
     * Gets the comm date.
     * @return the comm date
     */
    public Date getCommDate() {

        return commDate;
    }

    /**
     * Sets the comm date.
     * @param commDate the new comm date
     */
    public void setCommDate(Date commDate) {

        this.commDate = commDate;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public int getType() {

        return type;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(int type) {

        this.type = type;
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
     * Gets the payment refernce id.
     * @return the payment refernce id
     */
    public long getPaymentRefernceId() {

        return paymentRefernceId;
    }

    /**
     * Sets the payment refernce id.
     * @param paymentRefernceId the new payment refernce id
     */
    public void setPaymentRefernceId(long paymentRefernceId) {

        this.paymentRefernceId = paymentRefernceId;
    }

}
