/*
 * Account.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;


/**
 * The Class Account.
 * @author $Author: boopalan $
 * @version $Rev: 110 $, $Date: 2009-08-26 20:37:15 +0530 (Wed, 26 Aug 2009) $
 */
public class Account {
    

    public static final int SAVING = 1;
    public static final int CURRENT = 2;
    public static final int LOAN = 3;
    public static final int CREDIT = 4;
    public static final int DEPOSIT = 5;
	public static final String ACC_TYPE_AHO="AHO";
	public static final String ACC_TYPE_CTE="CTE";
	public static final String ACC_TYPE_TP="TPE";
	public static final String ACC_TYPE_PRE="PRE";

	private int type;
    private String accountNumber;
    private String accountName;
    private String accountType;
    private String validFrom;
    private String validThrough;
    private double balance;
    private String status;
    private String currency;
    private String other;
    private double limit;
    private double minimumPaymentAmount;
    private double paymentAmount;
    private String dueDate;
    private double accumulatedPaymentAmount;
    private String lastPaymentDate;


    /**
     * Instantiates a new account.
     * @param type the type
     */
    public Account(int type) {
        
        this.type = type;
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
     * Gets the account number.
     * @return the account number
     */
    public String getAccountNumber() {
        return (accountNumber != null) ? accountNumber : " ";
    }

    /**
     * Sets the account number.
     * @param accountNumber the new account number
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Gets the balance.
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the balance.
     * @param balance the new balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Gets the account name.
     * @return the account name
     */
    public String getAccountName() {
        if(accountName == null) {
        	int index = (accountNumber != null) ? ((accountNumber.length() > 4) ? accountNumber.length() -4 : 0) : -1;
        	accountName = ((accountType != null) ? accountType : "")
        		+ ((index > -1) ? accountNumber.substring(index) : "");

        }

        return accountName;
    }

    /**
     * Sets the account name.
     * @param accountName the new account name
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * Gets the account type.
     * @return the account type
     */
    public String getAccountType() {
        return (accountType != null) ? accountType : " ";
    }

    /**
     * Sets the account type.
     * @param accounttype the new account type
     */
    public void setAccountType(String accounttype) {
        this.accountType = accounttype;
    }

    /**
     * Gets the valid from.
     * @return the valid from
     */
    public String getValidFrom() {
        return (validFrom != null) ? validFrom : " ";
    }

    /**
     * Sets the valid from.
     * @param validFrom the new valid from
     */
    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    /**
     * Gets the valid through.
     * @return the valid through
     */
    public String getValidThrough() {
        return (validThrough != null) ? validThrough : " ";
    }

    /**
     * Sets the valid through.
     * @param validThrough the new valid through
     */
    public void setValidThrough(String validThrough) {
        this.validThrough = validThrough;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public String getStatus() {
        return (status != null) ? status : " ";
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the currency.
     * @return the currency
     */
    public String getCurrency() {
        return (currency != null) ? currency : " ";
    }

    /**
     * Sets the currency.
     * @param currency the new currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Gets the other.
     * @return the other
     */
    public String getOther() {
    	return (other != null) ? other : " ";
    }

    /**
     * Sets the other.
     * @param other the new other
     */
    public void setOther(String other) {
        this.other = other;
    }

    /**
     * Gets the limit.
     * @return the limit
     */
    public double getLimit() {
    
        return limit;
    }

    /**
     * Sets the limit.
     * @param limit the new limit
     */
    public void setLimit(double limit) {
    
        this.limit = limit;
    }

    /**
     * Gets the minimum payment amount.
     * @return the minimum payment amount
     */
    public double getMinimumPaymentAmount() {
    
        return minimumPaymentAmount;
    }

    /**
     * Sets the minimum payment amount.
     * @param minimumPaymentAmount the new minimum payment amount
     */
    public void setMinimumPaymentAmount(double minimumPaymentAmount) {
    
        this.minimumPaymentAmount = minimumPaymentAmount;
    }

    /**
     * Gets the payment amount.
     * @return the payment amount
     */
    public double getPaymentAmount() {
    
        return paymentAmount;
    }

    /**
     * Sets the payment amount.
     * @param paymentAmount the new payment amount
     */
    public void setPaymentAmount(double paymentAmount) {
    
        this.paymentAmount = paymentAmount;
    }

    /**
     * Gets the due date.
     * @return the due date
     */
    public String getDueDate() {
    
        return (dueDate != null) ? dueDate : " ";
    }

    /**
     * Sets the due date.
     * @param dueDate the new due date
     */
    public void setDueDate(String dueDate) {
    
        this.dueDate = dueDate;
    }

    /**
     * Gets the accumulated payment amount.
     * @return the accumulated payment amount
     */
    public double getAccumulatedPaymentAmount() {
    
        return accumulatedPaymentAmount;
    }

    /**
     * Sets the accumulated payment amount.
     * @param accumulatedPaymentAmount the new accumulated payment amount
     */
    public void setAccumulatedPaymentAmount(double accumulatedPaymentAmount) {
    
        this.accumulatedPaymentAmount = accumulatedPaymentAmount;
    }

    /**
     * Gets the last payment date.
     * @return the last payment date
     */
    public String getLastPaymentDate() {
    
        return (lastPaymentDate != null) ? lastPaymentDate : " ";
    }

    /**
     * Sets the last payment date.
     * @param lastPaymentDate the new last payment date
     */
    public void setLastPaymentDate(String lastPaymentDate) {
    
        this.lastPaymentDate = lastPaymentDate;
    }
}
