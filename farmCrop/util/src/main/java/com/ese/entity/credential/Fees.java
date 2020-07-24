/**
 * Fees.java
 * Copyright (c) 2008-2009, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.ese.entity.credential;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.TransactionType;
import com.sourcetrace.esesw.entity.profile.Currency;

/**
 * The Class Fees.
 * @author $Author:
 * @version $Rev:
 */
public class Fees {

    private long id;
    private Profile profile;
    private TransactionType transactionType;
    private String accountType;
    private Currency currency;
    private double amount;

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public Profile getProfile() {

        return profile;
    }

    public void setProfile(Profile profile) {

        this.profile = profile;
    }

    public TransactionType getTransactionType() {

        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {

        this.transactionType = transactionType;
    }

    public String getAccountType() {

        return accountType;
    }

    public void setAccountType(String accountType) {

        this.accountType = accountType;
    }

    public Currency getCurrency() {

        return currency;
    }

    public void setCurrency(Currency currency) {

        this.currency = currency;
    }

    @Min(value = 0, message = "min.amount")
    @Max(value = 100, message = "amount.max")
    public double getAmount() {

        return amount;
    }

    public void setAmount(double amount) {

        this.amount = amount;
    }

    public String toString() {

        return getAccountType() + "_" + getTransactionType().getId() + "_"
                + getCurrency().getCurrencyCode();
    }

}
