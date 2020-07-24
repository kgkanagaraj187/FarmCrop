/*
 * OfflineDistribution.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Set;

/**
 * The Class OfflineDistribution.
 */
public class OfflineDistribution {

    private long id;
    private String receiptNo;
    private String distributionDate;
    private String farmerId;
    private String warehouseCode;
    private String villageCode;
    private String seasonCode;
    private String agentId;
    private String deviceId;
    private String servicePointId;
    private String paymentAmt;
    private String mobileNumber;
    private int statusCode;
    private String statusMsg;
    private String txnType;
    private double tax;
    private String paymentMode;
    private String freeDistribution;
    private double totalAmount;
    private Set<OfflineDistributionDetail> offlineDistributionDetails;
    private String branchId;
    private String currentSeasonCode;
    private Set<PMTImageDetails> pmtImageDetail;
   private String latitude;
   private String longitude;
    
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

	private String tenantId;
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
     * Sets the distribution date.
     * @param distributionDate the new distribution date
     */
    public void setDistributionDate(String distributionDate) {

        this.distributionDate = distributionDate;
    }

    /**
     * Gets the distribution date.
     * @return the distribution date
     */
    public String getDistributionDate() {

        return distributionDate;
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
     * Sets the warehouse code.
     * @param warehouseCode the new warehouse code
     */
    public void setWarehouseCode(String warehouseCode) {

        this.warehouseCode = warehouseCode;
    }

    /**
     * Gets the warehouse code.
     * @return the warehouse code
     */
    public String getWarehouseCode() {

        return warehouseCode;
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
     * Sets the device id.
     * @param deviceId the new device id
     */
    public void setDeviceId(String deviceId) {

        this.deviceId = deviceId;
    }

    /**
     * Gets the device id.
     * @return the device id
     */
    public String getDeviceId() {

        return deviceId;
    }

    /**
     * Sets the service point id.
     * @param servicePointId the new service point id
     */
    public void setServicePointId(String servicePointId) {

        this.servicePointId = servicePointId;
    }

    /**
     * Gets the service point id.
     * @return the service point id
     */
    public String getServicePointId() {

        return servicePointId;
    }

    /**
     * Sets the payment amt.
     * @param paymentAmt the new payment amt
     */
    public void setPaymentAmt(String paymentAmt) {

        this.paymentAmt = paymentAmt;
    }

    /**
     * Gets the payment amt.
     * @return the payment amt
     */
    public String getPaymentAmt() {

        return paymentAmt;
    }

    /**
     * Sets the mobile number.
     * @param mobileNumber the new mobile number
     */
    public void setMobileNumber(String mobileNumber) {

        this.mobileNumber = mobileNumber;
    }

    /**
     * Gets the mobile number.
     * @return the mobile number
     */
    public String getMobileNumber() {

        return mobileNumber;
    }

    /**
     * Sets the status msg.
     * @param statusMsg the new status msg
     */
    public void setStatusMsg(String statusMsg) {

        this.statusMsg = statusMsg;
    }

    /**
     * Gets the status msg.
     * @return the status msg
     */
    public String getStatusMsg() {

        return statusMsg;
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
     * Sets the offline distribution details.
     * @param offlineDistributionDetails the new offline distribution details
     */
    public void setOfflineDistributionDetails(
            Set<OfflineDistributionDetail> offlineDistributionDetails) {

        this.offlineDistributionDetails = offlineDistributionDetails;
    }

    /**
     * Gets the offline distribution details.
     * @return the offline distribution details
     */
    public Set<OfflineDistributionDetail> getOfflineDistributionDetails() {

        return offlineDistributionDetails;
    }

    /**
     * Sets the status code.
     * @param statusCode the new status code
     */
    public void setStatusCode(int statusCode) {

        this.statusCode = statusCode;
    }

    /**
     * Gets the status code.
     * @return the status code
     */
    public int getStatusCode() {

        return statusCode;
    }

    /**
     * Sets the season code.
     * @param seasonCode the new season code
     */
    public void setSeasonCode(String seasonCode) {

        this.seasonCode = seasonCode;
    }

    /**
     * Gets the season code.
     * @return the season code
     */
    public String getSeasonCode() {

        return seasonCode;
    }

    /**
     * Sets the village code.
     * @param villageCode the new village code
     */
    public void setVillageCode(String villageCode) {

        this.villageCode = villageCode;
    }

    /**
     * Gets the village code.
     * @return the village code
     */
    public String getVillageCode() {

        return villageCode;
    }

    public double getTax() {

        return tax;
    }

    public void setTax(double tax) {

        this.tax = tax;
    }

    public String getPaymentMode() {

        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {

        this.paymentMode = paymentMode;
    }

    public String getFreeDistribution() {

        return freeDistribution;
    }

    public void setFreeDistribution(String freeDistribution) {

        this.freeDistribution = freeDistribution;
    }

    public double getTotalAmount() {

        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {

        this.totalAmount = totalAmount;
    }

    public String getBranchId() {

        return branchId;
    }

    public void setBranchId(String branchId) {

        this.branchId = branchId;
    }

    public String getCurrentSeasonCode() {

        return currentSeasonCode;
    }

    public void setCurrentSeasonCode(String currentSeasonCode) {

        this.currentSeasonCode = currentSeasonCode;
    }

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Set<PMTImageDetails> getPmtImageDetail() {
		return pmtImageDetail;
	}

	public void setPmtImageDetail(Set<PMTImageDetails> pmtImageDetail) {
		this.pmtImageDetail = pmtImageDetail;
	}
    
    
}
