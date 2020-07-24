/*
 * AgentMovement.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Set;

import com.sourcetrace.esesw.entity.profile.Farm;

public class AgentMovement {

    private long id;
    private String purpose;
    private String remarks;
    private Farm farm;
    private AgroTransaction agroTransaction;
    private Set<AgentMovementLocation> agentMovementLocations;

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
     * Gets the farm.
     * @return the farm
     */
    public Farm getFarm() {

        return farm;
    }

    /**
     * Sets the farm.
     * @param farm the new farm
     */
    public void setFarm(Farm farm) {

        this.farm = farm;
    }

    /**
     * Gets the agro transaction.
     * @return the agro transaction
     */
    public AgroTransaction getAgroTransaction() {

        return agroTransaction;
    }

    /**
     * Sets the agro transaction.
     * @param agroTransaction the new agro transaction
     */
    public void setAgroTransaction(AgroTransaction agroTransaction) {

        this.agroTransaction = agroTransaction;
    }

    /**
     * Gets the purpose.
     * @return the purpose
     */
    public String getPurpose() {

        return purpose;
    }

    /**
     * Sets the purpose.
     * @param purpose the new purpose
     */
    public void setPurpose(String purpose) {

        this.purpose = purpose;
    }

    /**
     * Gets the remarks.
     * @return the remarks
     */
    public String getRemarks() {

        return remarks;
    }

    /**
     * Sets the remarks.
     * @param remarks the new remarks
     */
    public void setRemarks(String remarks) {

        this.remarks = remarks;
    }

    /**
     * Sets the agent movement locations.
     * @param agentMovementLocations the new agent movement locations
     */
    public void setAgentMovementLocations(Set<AgentMovementLocation> agentMovementLocations) {

        this.agentMovementLocations = agentMovementLocations;
    }

    /**
     * Gets the agent movement locations.
     * @return the agent movement locations
     */
    public Set<AgentMovementLocation> getAgentMovementLocations() {

        return agentMovementLocations;
    }

}
