/*
 * TxnBulkImport.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.txn;

import java.util.Date;

/**
 * @author Panneer
 */
public class TxnBulkImport {
    
    private long id;
    private String acctType;
    private String acctNumber;
    private double txnAmount;
    private String txnNarration;
    private Date importTime;
    private String creditType;
    private String currencyType;
    private String paymentMode;
    private String txnType;
    private String txnId;
    private int statusCode;
    private String statusMsg;
    private String batchNo;
    
    // transient variable
    private boolean errorDate;

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
     * Gets the acct type.
     * @return the acct type
     */
    public String getAcctType() {

        return acctType;
    }

    /**
     * Sets the acct type.
     * @param acctType the new acct type
     */
    public void setAcctType(String acctType) {

        this.acctType = acctType;
    }

    /**
     * Gets the acct number.
     * @return the acct number
     */
    public String getAcctNumber() {

        return acctNumber;
    }

    /**
     * Sets the acct number.
     * @param acctNumber the new acct number
     */
    public void setAcctNumber(String acctNumber) {

        this.acctNumber = acctNumber;
    }

    /**
     * Gets the txn amount.
     * @return the txn amount
     */
    public double getTxnAmount() {

        return txnAmount;
    }

    /**
     * Sets the txn amount.
     * @param txnAmount the new txn amount
     */
    public void setTxnAmount(double txnAmount) {

        this.txnAmount = txnAmount;
    }

    /**
     * Gets the txn narration.
     * @return the txn narration
     */
    public String getTxnNarration() {

        return txnNarration;
    }

    /**
     * Sets the txn narration.
     * @param txnNarration the new txn narration
     */
    public void setTxnNarration(String txnNarration) {

        this.txnNarration = txnNarration;
    }

    /**
     * Gets the import time.
     * @return the import time
     */
    public Date getImportTime() {

        return importTime;
    }

    /**
     * Sets the import time.
     * @param importTime the new import time
     */
    public void setImportTime(Date importTime) {

        this.importTime = importTime;
    }

    /**
     * Gets the credit type.
     * @return the credit type
     */
    public String getCreditType() {

        return creditType;
    }

    /**
     * Sets the credit type.
     * @param creditType the new credit type
     */
    public void setCreditType(String creditType) {

        this.creditType = creditType;
    }

    /**
     * Gets the currency type.
     * @return the currency type
     */
    public String getCurrencyType() {

        return currencyType;
    }

    /**
     * Sets the currency type.
     * @param currencyType the new currency type
     */
    public void setCurrencyType(String currencyType) {

        this.currencyType = currencyType;
    }

    /**
     * Gets the payment mode.
     * @return the payment mode
     */
    public String getPaymentMode() {

        return paymentMode;
    }

    /**
     * Sets the payment mode.
     * @param paymentMode the new payment mode
     */
    public void setPaymentMode(String paymentMode) {

        this.paymentMode = paymentMode;
    }

    /**
     * Gets the txn type.
     * @return the txn type
     */
    public String getTxnType() {

        return txnType;
    }

    /**
     * Sets the txn type.
     * @param txnType the new txn type
     */
    public void setTxnType(String txnType) {

        this.txnType = txnType;
    }

    /**
     * Gets the txn id.
     * @return the txn id
     */
    public String getTxnId() {

        return txnId;
    }

    /**
     * Sets the txn id.
     * @param txnId the new txn id
     */
    public void setTxnId(String txnId) {

        this.txnId = txnId;
    }

    /**
     * Gets the status code.
     * @return the status code
     */
    public int getStatusCode() {

        return statusCode;
    }

    /**
     * Sets the status code.
     * @param statusCode the new status code
     */
    public void setStatusCode(int statusCode) {

        this.statusCode = statusCode;
    }

    /**
     * Gets the status msg.
     * @return the status msg
     */
    public String getStatusMsg() {

        return statusMsg;
    }

    /**
     * Sets the status msg.
     * @param statusMsg the new status msg
     */
    public void setStatusMsg(String statusMsg) {

        this.statusMsg = statusMsg;
    }

    /**
     * Gets the batch no.
     * @return the batch no
     */
    public String getBatchNo() {

        return batchNo;
    }

    /**
     * Sets the batch no.
     * @param batchNo the new batch no
     */
    public void setBatchNo(String batchNo) {

        this.batchNo = batchNo;
    }

	/**
	 * @param errorDate the errorDate to set
	 */
	public void setErrorDate(boolean errorDate) {
		this.errorDate = errorDate;
	}

	/**
	 * @return the errorDate
	 */
	public boolean isErrorDate() {
		return errorDate;
	}

}
