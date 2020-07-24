/*
 * DayCreditBalance.java
 * Copyright (c) 2008-2010, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.txn;

import java.util.Date;

import com.sourcetrace.esesw.entity.profile.Affiliate;

/**
 * The Class DayCreditBalance.
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
public class DayCreditBalance {

    public static final int TALLY = 0;
    public static final int LESS = -1;
    public static final int MORE = 1;
    
    private long id;
    private Affiliate affiliate;
    private Date date;
    private double deposit;
    private double credit;
    private double trust;
    private double accumulatedBalance;
    private double creditBalance;
    private int status;
    private Date[] filterDate;

    /**
     * Gets the date.
     * @return the date
     */
    public Date getDate() {

        return date;
    }

    /**
     * Sets the date.
     * @param date the new date
     */
    public void setDate(Date date) {

        this.date = date;
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
     * Gets the affiliate.
     * @return the affiliate
     */
    public Affiliate getAffiliate() {

        return affiliate;
    }

    /**
     * Sets the affiliate.
     * @param affiliate the new affiliate
     */
    public void setAffiliate(Affiliate affiliate) {

        this.affiliate = affiliate;
    }

    /**
     * Gets the deposit.
     * @return the deposit
     */
    public double getDeposit() {

        return deposit;
    }

    /**
     * Sets the deposit.
     * @param deposit the new deposit
     */
    public void setDeposit(double deposit) {

        this.deposit = deposit;
    }

    /**
     * Gets the credit.
     * @return the credit
     */
    public double getCredit() {

        return credit;
    }

    /**
     * Sets the credit.
     * @param credit the new credit
     */
    public void setCredit(double credit) {

        this.credit = credit;
    }

    /**
     * Gets the trust.
     * @return the trust
     */
    public double getTrust() {

        return trust;
    }

    /**
     * Sets the trust.
     * @param trust the new trust
     */
    public void setTrust(double trust) {

        this.trust = trust;
    }

    /**
     * Gets the accumulated balance.
     * @return the accumulated balance
     */
    public double getAccumulatedBalance() {

        return accumulatedBalance;
    }

    /**
     * Sets the accumulated balance.
     * @param accumulatedBalance the new accumulated balance
     */
    public void setAccumulatedBalance(double accumulatedBalance) {

        this.accumulatedBalance = accumulatedBalance;
    }

    /**
     * Gets the credit balance.
     * @return the credit balance
     */
    public double getCreditBalance() {

        return creditBalance;
    }

    /**
     * Sets the credit balance.
     * @param creditBalance the new credit balance
     */
    public void setCreditBalance(double creditBalance) {

        this.creditBalance = creditBalance;
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
     * Gets the filter date.
     * @return the filter date
     */
    public Date[] getFilterDate() {

        return filterDate;
    }

    /**
     * Sets the filter date.
     * @param filterDate the new filter date
     */
    public void setFilterDate(Date[] filterDate) {

        this.filterDate = filterDate;
    }
}
