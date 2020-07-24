/*
 * TransactionHeader.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.entity;

/**
 * @author Saravanan
 */
public class TransactionHeader {

    private long id;
    private String serialNo;
    private String versionNo;
    private String agentId;
    private String servPointId;

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
     * Gets the serial no.
     * @return the serial no
     */
    public String getSerialNo() {

        return serialNo;
    }

    /**
     * Sets the serial no.
     * @param serialNo the new serial no
     */
    public void setSerialNo(String serialNo) {

        this.serialNo = serialNo;
    }

    public String getVersionNo() {

        return versionNo;
    }

    public void setVersionNo(String versionNo) {

        this.versionNo = versionNo;
    }

    /**
     * Gets the agent id.
     * @return the agent id
     */
    public String getAgentId() {

        return agentId;
    }

    /**
     * Sets the agent id.
     * @param agentId the new agent id
     */
    public void setAgentId(String agentId) {

        this.agentId = agentId;
    }

    /**
     * Gets the serv point id.
     * @return the serv point id
     */
    public String getServPointId() {

        return servPointId;
    }

    /**
     * Sets the serv point id.
     * @param servPointId the new serv point id
     */
    public void setServPointId(String servPointId) {

        this.servPointId = servPointId;
    }

}
