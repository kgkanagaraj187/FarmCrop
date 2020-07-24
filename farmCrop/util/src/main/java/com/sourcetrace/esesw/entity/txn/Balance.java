/*
 * Balance.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.txn;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.sourcetrace.esesw.entity.profile.Currency;

/**
 * The Class CreditBalance.
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
public class Balance {
    
    public static int BALANCE_ALL=1;
    public static int BALANCE_NEGATIVE=2;
    public static int BALANCE_POSITIVE=3;

	private static final int NAME_MAX_LENGTH = 10;
	private long id;
    private String accountNumber;
    private Date registrationDate;
    private double prepaymentAmount;
    private double creditAmount;
    private double trustAmount;
    private double accumulatedBalance;
    private double creditBalance;
    private Currency currency;
    private Date lastModifiedTime;

    // Transient properties
    private double authorizedBalance;// Getter alone-used in report for showing authorizedBalance
    private int creditBalanceType;//To list the affiliate balances WRT type which could be +,-,Both
    /**
     * @return the authorizedBalance which is the sum of prepaymentAmount,creditAmount,trustAmount
     */
    public double getAuthorizedBalance() {

        authorizedBalance = (prepaymentAmount + creditAmount + trustAmount);
        return authorizedBalance;
    }
    /**
     * @return the creditBalanceType
     */
    public int getCreditBalanceType() {
    
        return creditBalanceType;
    }

    /**
     * @param creditBalanceType the creditBalanceType to set
     */
    public void setCreditBalanceType(int creditBalanceType) {
    
        this.creditBalanceType = creditBalanceType;
    }

    /**
     * Gets the currency.
     * @return the currency
     */

    @NotNull(message = "empty.currency")
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
     * Gets the prepayment amount.
     * @return the prepayment amount
     */
    @Range(max = 10000000, min = 1, message = "prepaymentamount.range")
    public double getPrepaymentAmount() {

        return prepaymentAmount;
    }

    /**
     * Sets the prepayment amount.
     * @param prepaymentAmount the new prepayment amount
     */
    public void setPrepaymentAmount(double prepaymentAmount) {

        this.prepaymentAmount = prepaymentAmount;
    }

    /**
     * Gets the credit amount.
     * @return the credit amount
     */
    @Range(max = 10000000, min = 1, message = "creditamount.range")
    public double getCreditAmount() {

        return creditAmount;
    }

    /**
     * Sets the credit amount.
     * @param creditAmount the new credit amount
     */
    public void setCreditAmount(double creditAmount) {

        this.creditAmount = creditAmount;
    }

    /**
     * Gets the trust amount.
     * @return the trust amount
     */
    @Range(max = 10000000, min = 1, message = "trustamount.range")
    public double getTrustAmount() {

        return trustAmount;
    }

    /**
     * Sets the trust amount.
     * @param trustAmount the new trust amount
     */
    public void setTrustAmount(double trustAmount) {

        this.trustAmount = trustAmount;
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
     * Gets the last modified time.
     * @return the last modified time
     */
    public Date getLastModifiedTime() {

        return lastModifiedTime;
    }

    /**
     * Sets the last modified time.
     * @param lastModifiedTime the new last modified time
     */
    public void setLastModifiedTime(Date lastModifiedTime) {

        this.lastModifiedTime = lastModifiedTime;
    }

    /**
     * Gets the account number.
     * @return the account number
     */

    
    @Length(max = 15, message = "length.account.number")
    @Pattern(regexp="[0-9^-]+", message = "pattern.account.number")
    @NotEmpty(message = "empty.account.number")
    public String getAccountNumber() {

        return accountNumber;
    }

    /**
     * Sets the account number.
     * @param accountNumber the new account number
     */
    public void setAccountNumber(String accountNumber) {

        this.accountNumber = accountNumber;
    }

    /**
     * Gets the registration date.
     * @return the registration date
     */
    @NotNull(message = "empty.registrationDate")
    public Date getRegistrationDate() {

        return registrationDate;
    }

    /**
     * Sets the registration date.
     * @param registrationDate the new registration date
     */
    public void setRegistrationDate(Date registrationDate) {

        this.registrationDate = registrationDate;
    }
}
