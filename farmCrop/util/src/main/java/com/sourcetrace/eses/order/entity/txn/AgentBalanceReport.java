/*
 * AgentBalanceReport.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

public class AgentBalanceReport {

    private long id;
    private String receiptNo;
    private String agentId;
    private String agentName;
    private String farmerId;
    private String farmerName;
    private String servicePointName;
    private String txnType;
    private double initialBalance;
    private double txnAmount;
    private double balanceAmount;
    private Date txnTime;
    private String txnDesc;
    private String accountNumber;
    private String accountType;
    private String profType;

    // transient variable
    private String profileId;
    private double finalBalance;
    private int balanceType;

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the receipt no.
     * @param receiptNo the new receipt no
     */
    public void setReceiptNo(String receiptNo) {

        this.receiptNo = receiptNo;
    }

    /**
     * Gets the receipt no.
     * @return the receipt no
     */
    public String getReceiptNo() {

        return receiptNo;
    }

    /**
     * Sets the agent id.
     * @param agentId the new agent id
     */
    public void setAgentId(String agentId) {

        this.agentId = agentId;
    }

    /**
     * Gets the agent id.
     * @return the agent id
     */
    public String getAgentId() {

        return agentId;
    }

    /**
     * Sets the agent name.
     * @param agentName the new agent name
     */
    public void setAgentName(String agentName) {

        this.agentName = agentName;
    }

    /**
     * Gets the agent name.
     * @return the agent name
     */
    public String getAgentName() {

        return agentName;
    }

    /**
     * Sets the farmer id.
     * @param farmerId the new farmer id
     */
    public void setFarmerId(String farmerId) {

        this.farmerId = farmerId;
    }

    /**
     * Gets the farmer id.
     * @return the farmer id
     */
    public String getFarmerId() {

        return farmerId;
    }

    /**
     * Sets the farmer name.
     * @param farmerName the new farmer name
     */
    public void setFarmerName(String farmerName) {

        this.farmerName = farmerName;
    }

    /**
     * Gets the farmer name.
     * @return the farmer name
     */
    public String getFarmerName() {

        return farmerName;
    }

    /**
     * Sets the service point name.
     * @param servicePointName the new service point name
     */
    public void setServicePointName(String servicePointName) {

        this.servicePointName = servicePointName;
    }

    /**
     * Gets the service point name.
     * @return the service point name
     */
    public String getServicePointName() {

        return servicePointName;
    }

    /**
     * Sets the txn type.
     * @param txnType the new txn type
     */
    public void setTxnType(String txnType) {

        this.txnType = txnType;
    }

    /**
     * Gets the txn type.
     * @return the txn type
     */
    public String getTxnType() {

        return txnType;
    }

    /**
     * Sets the initial balance.
     * @param initialBalance the new initial balance
     */
    public void setInitialBalance(double initialBalance) {

        this.initialBalance = initialBalance;
    }

    /**
     * Gets the initial balance.
     * @return the initial balance
     */
    public double getInitialBalance() {

        return initialBalance;
    }

    /**
     * Sets the txn amount.
     * @param txnAmount the new txn amount
     */
    public void setTxnAmount(double txnAmount) {

        this.txnAmount = txnAmount;
    }

    /**
     * Gets the txn amount.
     * @return the txn amount
     */
    public double getTxnAmount() {

        return txnAmount;
    }

    /**
     * Sets the balance amount.
     * @param balanceAmount the new balance amount
     */
    public void setBalanceAmount(double balanceAmount) {

        this.balanceAmount = balanceAmount;
    }

    /**
     * Gets the balance amount.
     * @return the balance amount
     */
    public double getBalanceAmount() {

        return balanceAmount;
    }

    /**
     * Sets the txn time.
     * @param txnTime the new txn time
     */
    public void setTxnTime(Date txnTime) {

        this.txnTime = txnTime;
    }

    /**
     * Gets the txn time.
     * @return the txn time
     */
    public Date getTxnTime() {

        return txnTime;
    }

    /**
     * Sets the txn desc.
     * @param txnDesc the new txn desc
     */
    public void setTxnDesc(String txnDesc) {

        this.txnDesc = txnDesc;
    }

    /**
     * Gets the txn desc.
     * @return the txn desc
     */
    public String getTxnDesc() {

        return txnDesc;
    }

    /**
     * Sets the account number.
     * @param accountNumber the new account number
     */
    public void setAccountNumber(String accountNumber) {

        this.accountNumber = accountNumber;
    }

    /**
     * Gets the account number.
     * @return the account number
     */
    public String getAccountNumber() {

        return accountNumber;
    }

    /**
     * Sets the account type.
     * @param accountType the new account type
     */
    public void setAccountType(String accountType) {

        this.accountType = accountType;
    }

    /**
     * Gets the account type.
     * @return the account type
     */
    public String getAccountType() {

        return accountType;
    }

    /**
     * Sets the profile id.
     * @param profileId the new profile id
     */
    public void setProfileId(String profileId) {

        this.profileId = profileId;
    }

    /**
     * Gets the profile id.
     * @return the profile id
     */
    public String getProfileId() {

        return profileId;
    }

    /**
     * Sets the final balance.
     * @param finalBalance the new final balance
     */
    public void setFinalBalance(double finalBalance) {

        this.finalBalance = finalBalance;
    }

    /**
     * Gets the final balance.
     * @return the final balance
     */
    public double getFinalBalance() {

        return finalBalance;
    }

    /**
     * Sets the balance type.
     * @param balanceType the new balance type
     */
    public void setBalanceType(int balanceType) {

        this.balanceType = balanceType;
    }

    /**
     * Gets the balance type.
     * @return the balance type
     */
    public int getBalanceType() {

        return balanceType;
    }

    /**
     * @param profType the profType to set
     */
    public void setProfType(String profType) {

        this.profType = profType;
    }

    /**
     * @return the profType
     */
    public String getProfType() {

        return profType;
    }

}
