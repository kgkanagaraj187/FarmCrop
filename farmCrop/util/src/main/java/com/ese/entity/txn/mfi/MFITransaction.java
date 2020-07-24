/*
 * MFITransaction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.entity.txn.mfi;

import java.util.Date;

/**
 * @author Panneer
 */
public class MFITransaction {

    public static final int TRANSACTION_MODE_ONLINE = 1;
    public static final int TRANSACTION_MODE_OFFLINE = 2;
    public static final int TRANSACTION_MODE_BACK_END = 3;

    public static final int PAYMENT_MODE_ONLINE = 1;
    public static final int PAYMENT_MODE_OFFLINE = 2;
    public static final int PAYMENT_MODE_BACK_END = 3;

    public static final int BACK_END_TRANSACTION = 1;
    public static final int BACK_END_TRANSACTION_PROCESS_DONE = 2;

    public static final String SUCCESS_TXN = "0";
    public static final String FAILED_TXN = "1";
    public static final String PENDING_TXN = "2";
    public static final String VOID_TXN = "3";

    public static final int EXPORT_PENDING = 0;
    public static final int EXPORT_DONE = 1;
    
    public static final int CASH_PAYMENT = 1;
    public static final int CHEQUE_PAYMENT = 2;
    public static final int DEMAND_DRAFT_PAYMENT = 3;
    
    public static final String DEPOSIT_TXN_NARRATION="DEPOSIT";
    public static final String WITHDRAW_TXN_NARRATION="WITHDRAW";
    
    public static final String CREDIT_TYPE="CR";
    public static final String DEBIT_TYPE="DR";
    
    public static final String CURRENCY_TYPE="INR";
    public static final String SUCCESS = "SUCCESS";
    public static final String PENDING = "PENDING";

    public static final String CREDIT = "CR";
    public static final String DEBIT = "DR";

    private long id;
    private String profileId;
    private String profileType;
    private String acctType;
    private String acctNum;
    private double intBalance;
    private double txnAmount;
    private double balAmount;
    private String statusCode;
    private String statusMsg;
    private String aprCode;
    private String txnCode;
    private String txnNarration;
    private String creditType;
    private int pymntMode;
    private String pymntRefNo;
    private String currencyType;
    private String txnType;
    private int txnMode;
    private Date createDateTime;
    private Date updateDateTime;
    private int backTxnStatus;
    private MFITransaction refernceMFITxn;
    private int txnExportStatus;
    private String servicePlaceCode;

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
     * Gets the profile id.
     * @return the profile id
     */
    public String getProfileId() {

        return profileId;
    }

    /**
     * Sets the profile id.
     * @param profileId the new profile id
     */
    public void setProfileId(String profileId) {

        this.profileId = profileId;
    }

    /**
     * Gets the profile type.
     * @return the profile type
     */
    public String getProfileType() {

        return profileType;
    }

    /**
     * Sets the profile type.
     * @param profileType the new profile type
     */
    public void setProfileType(String profileType) {

        this.profileType = profileType;
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
     * Gets the acct num.
     * @return the acct num
     */
    public String getAcctNum() {

        return acctNum;
    }

    /**
     * Sets the acct num.
     * @param acctNum the new acct num
     */
    public void setAcctNum(String acctNum) {

        this.acctNum = acctNum;
    }

    /**
     * Gets the int balance.
     * @return the int balance
     */
    public double getIntBalance() {

        return intBalance;
    }

    /**
     * Sets the int balance.
     * @param intBalance the new int balance
     */
    public void setIntBalance(double intBalance) {

        this.intBalance = intBalance;
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
     * Gets the bal amount.
     * @return the bal amount
     */
    public double getBalAmount() {

        return balAmount;
    }

    /**
     * Sets the bal amount.
     * @param balAmount the new bal amount
     */
    public void setBalAmount(double balAmount) {

        this.balAmount = balAmount;
    }

    /**
     * Gets the status code.
     * @return the status code
     */
    public String getStatusCode() {

        return statusCode;
    }

    /**
     * Sets the status code.
     * @param statusCode the new status code
     */
    public void setStatusCode(String statusCode) {

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
     * Gets the apr code.
     * @return the apr code
     */
    public String getAprCode() {

        return aprCode;
    }

    /**
     * Sets the apr code.
     * @param aprCode the new apr code
     */
    public void setAprCode(String aprCode) {

        this.aprCode = aprCode;
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
     * Gets the pymnt mode.
     * @return the pymnt mode
     */
    public int getPymntMode() {

        return pymntMode;
    }

    /**
     * Sets the pymnt mode.
     * @param pymntMode the new pymnt mode
     */
    public void setPymntMode(int pymntMode) {

        this.pymntMode = pymntMode;
    }

    /**
     * Gets the pymnt ref no.
     * @return the pymnt ref no
     */
    public String getPymntRefNo() {

        return pymntRefNo;
    }

    /**
     * Sets the pymnt ref no.
     * @param pymntRefNo the new pymnt ref no
     */
    public void setPymntRefNo(String pymntRefNo) {

        this.pymntRefNo = pymntRefNo;
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
     * Gets the txn mode.
     * @return the txn mode
     */
    public int getTxnMode() {

        return txnMode;
    }

    /**
     * Sets the txn mode.
     * @param txnMode the new txn mode
     */
    public void setTxnMode(int txnMode) {

        this.txnMode = txnMode;
    }

    /**
     * Gets the creates the date time.
     * @return the creates the date time
     */
    public Date getCreateDateTime() {

        return createDateTime;
    }

    /**
     * Sets the creates the date time.
     * @param createDateTime the new creates the date time
     */
    public void setCreateDateTime(Date createDateTime) {

        this.createDateTime = createDateTime;
    }

    /**
     * Gets the update date time.
     * @return the update date time
     */
    public Date getUpdateDateTime() {

        return updateDateTime;
    }

    /**
     * Sets the update date time.
     * @param updateDateTime the new update date time
     */
    public void setUpdateDateTime(Date updateDateTime) {

        this.updateDateTime = updateDateTime;
    }

    /**
     * Gets the back txn status.
     * @return the back txn status
     */
    public int getBackTxnStatus() {

        return backTxnStatus;
    }

    /**
     * Sets the back txn status.
     * @param backTxnStatus the new back txn status
     */
    public void setBackTxnStatus(int backTxnStatus) {

        this.backTxnStatus = backTxnStatus;
    }

    /**
     * Gets the refernce mfi txn.
     * @return the refernce mfi txn
     */
    public MFITransaction getRefernceMFITxn() {

        return refernceMFITxn;
    }

    /**
     * Sets the refernce mfi txn.
     * @param refernceMFITxn the new refernce mfi txn
     */
    public void setRefernceMFITxn(MFITransaction refernceMFITxn) {

        this.refernceMFITxn = refernceMFITxn;
    }

    /**
     * Gets the txn export status.
     * @return the txn export status
     */
    public int getTxnExportStatus() {

        return txnExportStatus;
    }

    /**
     * Sets the txn export status.
     * @param txnExportStatus the new txn export status
     */
    public void setTxnExportStatus(int txnExportStatus) {

        this.txnExportStatus = txnExportStatus;
    }

    /**
     * Gets the service place code.
     * @return the service place code
     */
    public String getServicePlaceCode() {

        return servicePlaceCode;
    }

    /**
     * Sets the service place code.
     * @param servicePlaceCode the new service place code
     */
    public void setServicePlaceCode(String servicePlaceCode) {

        this.servicePlaceCode = servicePlaceCode;
    }

}
