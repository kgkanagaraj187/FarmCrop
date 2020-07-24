/*
 * AgentCashFlow.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

public class AgentCashFlow {

    private long id;
    private String agentId;
    private String agentName;
    private String accountNo;
    private Double balance;
    private Double cashBalance;
    private Double distributionBalance;
    private String branch;
    //private long warehouseId;

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
     * Sets the account no.
     * @param accountNo the new account no
     */
    public void setAccountNo(String accountNo) {

        this.accountNo = accountNo;
    }

    /**
     * Gets the account no.
     * @return the account no
     */
    public String getAccountNo() {

        return accountNo;
    }

    /**
     * Sets the balance.
     * @param balance the new balance
     */
    public void setBalance(Double balance) {

        this.balance = balance;
    }

    /**
     * Gets the balance.
     * @return the balance
     */
    public Double getBalance() {

        return balance;
    }

    /**
     * Sets the distribution balance.
     * @param distributionBalance the new distribution balance
     */
    public void setDistributionBalance(Double distributionBalance) {
        this.distributionBalance = distributionBalance;
    }

    /**
     * Gets the distribution balance.
     * @return the distribution balance
     */
    public Double getDistributionBalance() {
        return distributionBalance;
    }

	public Double getCashBalance() {
		return cashBalance;
	}

	public void setCashBalance(Double cashBalance) {
		this.cashBalance = cashBalance;
	}
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
   
}
