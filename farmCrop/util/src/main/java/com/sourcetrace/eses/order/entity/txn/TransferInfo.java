/*
 * TransferInfo.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

public class TransferInfo {

    private long id;
    private String agentId;
    private String agentName;
    private String servicePointId;
    private String servicePointName;
    private String deviceId;
    private String deviceName;
    private int operationType;
    private Date txnTime;
    private String agentType;

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
     * Gets the agent name.
     * @return the agent name
     */
    public String getAgentName() {

        return agentName;
    }

    /**
     * Sets the agent name.
     * @param agentName the new agent name
     */
    public void setAgentName(String agentName) {

        this.agentName = agentName;
    }

    /**
     * Gets the service point id.
     * @return the service point id
     */
    public String getServicePointId() {

        return servicePointId;
    }

    /**
     * Sets the service point id.
     * @param servicePointId the new service point id
     */
    public void setServicePointId(String servicePointId) {

        this.servicePointId = servicePointId;
    }

    /**
     * Gets the service point name.
     * @return the service point name
     */
    public String getServicePointName() {

        return servicePointName;
    }

    /**
     * Sets the service point name.
     * @param servicePointName the new service point name
     */
    public void setServicePointName(String servicePointName) {

        this.servicePointName = servicePointName;
    }

    /**
     * Gets the device id.
     * @return the device id
     */
    public String getDeviceId() {

        return deviceId;
    }

    /**
     * Sets the device id.
     * @param deviceId the new device id
     */
    public void setDeviceId(String deviceId) {

        this.deviceId = deviceId;
    }

    /**
     * Gets the device name.
     * @return the device name
     */
    public String getDeviceName() {

        return deviceName;
    }

    /**
     * Sets the device name.
     * @param deviceName the new device name
     */
    public void setDeviceName(String deviceName) {

        this.deviceName = deviceName;
    }

    /**
     * Gets the operation type.
     * @return the operation type
     */
    public int getOperationType() {

        return operationType;
    }

    /**
     * Sets the operation type.
     * @param operationType the new operation type
     */
    public void setOperationType(int operationType) {

        this.operationType = operationType;
    }

    /**
     * Gets the txn time.
     * @return the txn time
     */
    public Date getTxnTime() {

        return txnTime;
    }

    /**
     * Sets the txn time.
     * @param txnTime the new txn time
     */
    public void setTxnTime(Date txnTime) {

        this.txnTime = txnTime;
    }

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

}
