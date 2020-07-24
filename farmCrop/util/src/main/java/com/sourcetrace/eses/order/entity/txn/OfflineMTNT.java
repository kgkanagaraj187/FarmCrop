/*
 * OfflineMTNT.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Set;

public class OfflineMTNT implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String agentId;
	private String servicePointId;
	private String deviceId;
	private String receiptNo;
	private String warehouseCode;
	private String mtntDate;
	private String totalNumberOfBags;
	private String totalGrossWeight;
	private String totalTareWeight;
	private String totalNetWeight;
	private String truckId;
	private String driverId;
	private int type;
	private int statusCode;
	private String statusMessage;
	private Set<OfflineMTNTDetail> offlineMTNTDetails;
	private String branchId;
	private String seasonCode;
	private Double totalLabourCost;
    private Double transportCost;
    private Double totalAmt;
    private String client;
    private String invoiceNo;
    private String transporter;
    private String messageNo;
    private String latitude;
	private String longitude;
    
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the agent id.
	 * 
	 * @return the agent id
	 */
	public String getAgentId() {
		return agentId;
	}

	/**
	 * Sets the agent id.
	 * 
	 * @param agentId
	 *            the new agent id
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	/**
	 * Gets the service point id.
	 * 
	 * @return the service point id
	 */
	public String getServicePointId() {
		return servicePointId;
	}

	/**
	 * Sets the service point id.
	 * 
	 * @param servicePointId
	 *            the new service point id
	 */
	public void setServicePointId(String servicePointId) {
		this.servicePointId = servicePointId;
	}

	/**
	 * Gets the device id.
	 * 
	 * @return the device id
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 * 
	 * @param deviceId
	 *            the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Gets the receipt no.
	 * 
	 * @return the receipt no
	 */
	public String getReceiptNo() {
		return receiptNo;
	}

	/**
	 * Sets the receipt no.
	 * 
	 * @param receiptNo
	 *            the new receipt no
	 */
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	/**
	 * Gets the warehouse code.
	 * 
	 * @return the warehouse code
	 */
	public String getWarehouseCode() {
		return warehouseCode;
	}

	/**
	 * Sets the warehouse code.
	 * 
	 * @param warehouseCode
	 *            the new warehouse code
	 */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/**
	 * Gets the mtnt date.
	 * 
	 * @return the mtnt date
	 */
	public String getMtntDate() {
		return mtntDate;
	}

	/**
	 * Sets the mtnt date.
	 * 
	 * @param mtntDate
	 *            the new mtnt date
	 */
	public void setMtntDate(String mtntDate) {
		this.mtntDate = mtntDate;
	}

	/**
	 * Gets the total number of bags.
	 * 
	 * @return the total number of bags
	 */
	public String getTotalNumberOfBags() {
		return totalNumberOfBags;
	}

	/**
	 * Sets the total number of bags.
	 * 
	 * @param totalNumberOfBags
	 *            the new total number of bags
	 */
	public void setTotalNumberOfBags(String totalNumberOfBags) {
		this.totalNumberOfBags = totalNumberOfBags;
	}

	/**
	 * Gets the total gross weight.
	 * 
	 * @return the total gross weight
	 */
	public String getTotalGrossWeight() {
		return totalGrossWeight;
	}

	/**
	 * Sets the total gross weight.
	 * 
	 * @param totalGrossWeight
	 *            the new total gross weight
	 */
	public void setTotalGrossWeight(String totalGrossWeight) {
		this.totalGrossWeight = totalGrossWeight;
	}

	/**
	 * Gets the total tare weight.
	 * 
	 * @return the total tare weight
	 */
	public String getTotalTareWeight() {
		return totalTareWeight;
	}

	/**
	 * Sets the total tare weight.
	 * 
	 * @param totalTareWeight
	 *            the new total tare weight
	 */
	public void setTotalTareWeight(String totalTareWeight) {
		this.totalTareWeight = totalTareWeight;
	}

	/**
	 * Gets the total net weight.
	 * 
	 * @return the total net weight
	 */
	public String getTotalNetWeight() {
		return totalNetWeight;
	}

	/**
	 * Sets the truck id.
	 * 
	 * @param truckId
	 *            the new truck id
	 */
	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}

	/**
	 * Gets the truck id.
	 * 
	 * @return the truck id
	 */
	public String getTruckId() {
		return truckId;
	}

	/**
	 * Sets the driver id.
	 * 
	 * @param driverId
	 *            the new driver id
	 */
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	/**
	 * Gets the driver id.
	 * 
	 * @return the driver id
	 */
	public String getDriverId() {
		return driverId;
	}

	/**
	 * Sets the total net weight.
	 * 
	 * @param totalNetWeight
	 *            the new total net weight
	 */
	public void setTotalNetWeight(String totalNetWeight) {
		this.totalNetWeight = totalNetWeight;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the offline mtnt details.
	 * 
	 * @return the offline mtnt details
	 */
	public Set<OfflineMTNTDetail> getOfflineMTNTDetails() {
		return offlineMTNTDetails;
	}

	/**
	 * Sets the offline mtnt details.
	 * 
	 * @param offlineMTNTDetails
	 *            the new offline mtnt details
	 */
	public void setOfflineMTNTDetails(Set<OfflineMTNTDetail> offlineMTNTDetails) {
		this.offlineMTNTDetails = offlineMTNTDetails;
	}

	/**
	 * Gets the status code.
	 * 
	 * @return the status code
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Sets the status code.
	 * 
	 * @param statusCode
	 *            the new status code
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Gets the status message.
	 * 
	 * @return the status message
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Sets the status message.
	 * 
	 * @param statusMessage
	 *            the new status message
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

    public String getSeasonCode() {
    
        return seasonCode;
    }

    public void setSeasonCode(String seasonCode) {
    
        this.seasonCode = seasonCode;
    }

	public Double getTotalLabourCost() {
		return totalLabourCost;
	}

	public void setTotalLabourCost(Double totalLabourCost) {
		this.totalLabourCost = totalLabourCost;
	}

	public Double getTransportCost() {
		return transportCost;
	}

	public void setTransportCost(Double transportCost) {
		this.transportCost = transportCost;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getMessageNo() {
		return messageNo;
	}

	public void setMessageNo(String messageNo) {
		this.messageNo = messageNo;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	
	
}
