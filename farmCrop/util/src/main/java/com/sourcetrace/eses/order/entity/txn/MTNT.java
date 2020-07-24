/*
 * MTNT.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class MTNT implements Serializable {

    private static final long serialVersionUID = 6270383412066658056L;

    public static enum Type {
        MTNT, MTNR
    }

    private long id;
    private String receiptNo;
    private Date mtntDate;
    private String warehouseCode;
    private String warehouseName;
    private String agentId;
    private String agentName;
    private String deviceId;
    private String deviceName;
    private String servicePointId;
    private String servicePointName;
    private int operationType;
    private long totalNumberOfBags;
    private double totalGrossWeight;
    private double totalTareWeight;
    private double totalNetWeight;
    private int type;
    private String truckId;
    private String driverId;
    private Set<MTNTDetail> mtntDetails;
    private String seasonCode;

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Gets the mtnt details.
     * @return the mtnt details
     */
    public Set<MTNTDetail> getMtntDetails() {

        return mtntDetails;
    }

    /**
     * Sets the mtnt details.
     * @param mtntDetails the new mtnt details
     */
    public void setMtntDetails(Set<MTNTDetail> mtntDetails) {

        this.mtntDetails = mtntDetails;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the receipt no.
     * @return the receipt no
     */
    public String getReceiptNo() {

        return receiptNo;
    }

    /**
     * Sets the receipt no.
     * @param receiptNo the new receipt no
     */
    public void setReceiptNo(String receiptNo) {

        this.receiptNo = receiptNo;
    }

    /**
     * Gets the mtnt date.
     * @return the mtnt date
     */
    public Date getMtntDate() {

        return mtntDate;
    }

    /**
     * Sets the mtnt date.
     * @param mtntDate the new mtnt date
     */
    public void setMtntDate(Date mtntDate) {

        this.mtntDate = mtntDate;
    }

    /**
     * Gets the warehouse code.
     * @return the warehouse code
     */
    public String getWarehouseCode() {

        return warehouseCode;
    }

    /**
     * Sets the warehouse code.
     * @param warehouseCode the new warehouse code
     */
    public void setWarehouseCode(String warehouseCode) {

        this.warehouseCode = warehouseCode;
    }

    /**
     * Sets the warehouse name.
     * @param warehouseName the new warehouse name
     */
    public void setWarehouseName(String warehouseName) {

        this.warehouseName = warehouseName;
    }

    /**
     * Gets the warehouse name.
     * @return the warehouse name
     */
    public String getWarehouseName() {

        return warehouseName;
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
     * @param operationType the operationType to set
     */
    public void setOperationType(int operationType) {

        this.operationType = operationType;
    }

    /**
     * @return the operationType
     */
    public int getOperationType() {

        return operationType;
    }

    /**
     * Gets the total number of bags.
     * @return the total number of bags
     */
    public long getTotalNumberOfBags() {

        return totalNumberOfBags;
    }

    /**
     * Sets the total number of bags.
     * @param totalNumberOfBags the new total number of bags
     */
    public void setTotalNumberOfBags(long totalNumberOfBags) {

        this.totalNumberOfBags = totalNumberOfBags;
    }

    /**
     * Gets the total gross weight.
     * @return the total gross weight
     */
    public double getTotalGrossWeight() {

        return totalGrossWeight;
    }

    /**
     * Sets the total gross weight.
     * @param totalGrossWeight the new total gross weight
     */
    public void setTotalGrossWeight(double totalGrossWeight) {

        this.totalGrossWeight = totalGrossWeight;
    }

    /**
     * Gets the total tare weight.
     * @return the total tare weight
     */
    public double getTotalTareWeight() {

        return totalTareWeight;
    }

    /**
     * Sets the total tare weight.
     * @param totalTareWeight the new total tare weight
     */
    public void setTotalTareWeight(double totalTareWeight) {

        this.totalTareWeight = totalTareWeight;
    }

    /**
     * Gets the total net weight.
     * @return the total net weight
     */
    public double getTotalNetWeight() {

        return totalNetWeight;
    }

    /**
     * Sets the total net weight.
     * @param totalNetWeight the new total net weight
     */
    public void setTotalNetWeight(double totalNetWeight) {

        this.totalNetWeight = totalNetWeight;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(int type) {

        this.type = type;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public int getType() {

        return type;
    }

    public void setTruckId(String truckId) {

        this.truckId = truckId;
    }

    public String getTruckId() {

        return truckId;
    }

    public void setDriverId(String driverId) {

        this.driverId = driverId;
    }

    public String getDriverId() {

        return driverId;
    }

    public String getSeasonCode() {
    
        return seasonCode;
    }

    public void setSeasonCode(String seasonCode) {
    
        this.seasonCode = seasonCode;
    }
    
    

}
