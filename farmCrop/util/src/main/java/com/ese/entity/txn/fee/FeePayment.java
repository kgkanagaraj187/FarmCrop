/**
 * FeePayment.java
 * Copyright (c) 2008-2009, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.ese.entity.txn.fee;

import java.util.Date;


/**
 * The Class FeePayment.
 * @author $Author: aravind $
 * @version $Rev: 59 $ $Date: 2009-08-04 17:10:51 +0530 (Tue, 04 Aug 2009) $
 */
public class FeePayment {

    private long id;
    private String txnCode;
    private Date txnDate = new Date();
    private String accountType;
    private String accountNumber;
    private String commerceId;
    private String commerceName;
    private double amount;
    private String currency;
    private String transferType;
    private String reverse = "N";
    private String detail;
    private String authCode;
	private double balance;
    private String status = "1";
    private String message;
    private String refernceId;
    private boolean retry;

    
    /**
	 * Gets the auth code.
	 * @return the auth code
	 */
    public String getAuthCode() {
		return authCode;
	}

	/**
	 * Sets the auth code.
	 * @param authCode the new auth code
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
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
	 * Gets the txn code.
	 * @return the txn code
	 */
    public String getTxnCode() {
        return txnCode;
    }

    /**
	 * Sets the txn code.
	 * @param txnCode the new txn code
	 */
    public void setTxnCode(String txnCode) {
        this.txnCode = txnCode;
    }

    /**
	 * Gets the txn date.
	 * @return the txn date
	 */
    public Date getTxnDate() {
        return txnDate;
    }

    /**
	 * Sets the txn date.
	 * @param txnDate the new txn date
	 */
    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
    }

    /**
	 * Gets the account type.
	 * @return the account type
	 */
    public String getAccountType() {
        return accountType;
    }

    /**
	 * Sets the account type.
	 * @param accountType the new account type
	 */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
	 * Gets the account number.
	 * @return the account number
	 */
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
	 * Gets the commerce id.
	 * @return the commerce id
	 */
    public String getCommerceId() {
        return commerceId;
    }

    /**
	 * Sets the commerce id.
	 * @param commerceId the new commerce id
	 */
    public void setCommerceId(String commerceId) {
        this.commerceId = commerceId;
    }

    /**
	 * Gets the commerce name.
	 * @return the commerce name
	 */
    public String getCommerceName() {
        return commerceName;
    }

    /**
	 * Sets the commerce name.
	 * @param commerceName the new commerce name
	 */
    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    /**
	 * Gets the amount.
	 * @return the amount
	 */
    public double getAmount() {
        return amount;
    }

    /**
	 * Sets the amount.
	 * @param amount the new amount
	 */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
	 * Gets the currency.
	 * @return the currency
	 */
    public String getCurrency() {
        return currency;
    }

    /**
	 * Sets the currency.
	 * @param currency the new currency
	 */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
	 * Gets the transfer type.
	 * @return the transfer type
	 */
    public String getTransferType() {
        return transferType;
    }

    /**
	 * Sets the transfer type.
	 * @param transferType the new transfer type
	 */
    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    /**
	 * Gets the reverse.
	 * @return the reverse
	 */
    public String getReverse() {
        return reverse;
    }

    /**
	 * Sets the reverse.
	 * @param reverse the new reverse
	 */
    public void setReverse(String reverse) {
        this.reverse = reverse;
    }

    /**
	 * Gets the reference.
	 * @return the reference
	 */
    public String getDetail() {
        return detail;
    }

    /**
	 * Sets the reference.
	 * @param reference the new reference
	 */
    public void setDetail(String reference) {
        this.detail = reference;
    }

    /**
	 * Gets the status.
	 * @return the status
	 */
    public String getStatus() {
        return status;
    }

    /**
	 * Sets the status.
	 * @param status the new status
	 */
    public void setStatus(String status) {
        this.status = status;
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
	 * Gets the payment refernce id.
	 * @return the payment refernce id
	 */
    public String getRefernceId() {
        return refernceId;
    }

    /**
	 * Sets the payment refernce id.
	 * @param paymentRefernceId the new payment refernce id
	 */
    public void setRefernceId(String paymentRefernceId) {
        this.refernceId = paymentRefernceId;
    }

    /**
	 * Checks if is retry.
	 * @return true, if is retry
	 */
    public boolean isRetry() {
        return retry;
    }

    /**
	 * Sets the retry.
	 * @param retry the new retry
	 */
    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    /**
	 * Gets the mis message.
	 * @return the mis message
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * Sets the mis message.
	 * @param misMessage the new mis message
	 */
    public void setMessage(String misMessage) {
        this.message = misMessage;
    }    
}
