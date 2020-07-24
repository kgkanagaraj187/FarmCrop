/*
 * OfflineProcurement.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
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

import com.sourcetrace.esesw.entity.profile.Customer;

public class OfflineProcurement implements Serializable {

	private static final long serialVersionUID = -5076127821638541492L;
	public static final String GOODS_PRODUCT_TYPE = "0";
	public static final String CARRIER_PRODUCT_TYPE = "1";

	private long id;
	private String farmerId;
	private String procurementDate;
	private String receiptNo;
	private String paymentAmt;
	private String type;
	private String totalAmount;
	private String agentId;
	private String deviceId;
	private String servicePointId;
	private String samithiCode;
	private String villageCode;
	private String cityCode;
	private String chartNo;
	private String driverName;
	private String vehicleNo;
	private String poNumber;
	private String mobileNumber;
	private int statusCode;
	private String statusMsg;
	private Set<OfflineProcurementDetail> offlineProcurementDetails;
	private String procMasterType;
	private String procMasterTypeId;
	private String paymentMode;
	private String branchId;
	private String seasonCode;
	private String createdUser;
	private String updatedUser;
	private Date createdDate;
	private Date updatedDate;
	private String roadMapCode;
	private String substituteName;
	private String farmerAttnce;
	private String farmId;
	private String latitude;
	private String longitude;
	private Double totalLabourCost;
	private Double transportCost;
	private Double invoiceValue;
	private String warehouseId;
	private String cropType;
	private String invoiceNo;
	private String isReg;
	private Customer buyer;
	private String vehicleType; 
	private Double loanInterest;
	private Double loanRepaymentAmt;
	private Double finalPayAmt;
	
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
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Gets the farmer id.
	 * 
	 * @return the farmer id
	 */
	public String getFarmerId() {

		return farmerId;
	}

	/**
	 * Sets the farmer id.
	 * 
	 * @param farmerId
	 *            the new farmer id
	 */
	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	/**
	 * Gets the procurement date.
	 * 
	 * @return the procurement date
	 */
	public String getProcurementDate() {

		return procurementDate;
	}

	/**
	 * Sets the procurement date.
	 * 
	 * @param procurementDate
	 *            the new procurement date
	 */
	public void setProcurementDate(String procurementDate) {

		this.procurementDate = procurementDate;
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
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {

		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {

		this.type = type;
	}

	/**
	 * Gets the total amount.
	 * 
	 * @return the total amount
	 */
	public String getTotalAmount() {

		return totalAmount;
	}

	/**
	 * Sets the total amount.
	 * 
	 * @param totalAmount
	 *            the new total amount
	 */
	public void setTotalAmount(String totalAmount) {

		this.totalAmount = totalAmount;
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
	 * Gets the samithi code.
	 * 
	 * @return the samithi code
	 */
	public String getSamithiCode() {

		return samithiCode;
	}

	/**
	 * Sets the samithi code.
	 * 
	 * @param samithiCode
	 *            the new samithi code
	 */
	public void setSamithiCode(String samithiCode) {

		this.samithiCode = samithiCode;
	}

	/**
	 * Gets the village code.
	 * 
	 * @return the village code
	 */
	public String getVillageCode() {

		return villageCode;
	}

	/**
	 * Sets the village code.
	 * 
	 * @param villageCode
	 *            the new village code
	 */
	public void setVillageCode(String villageCode) {

		this.villageCode = villageCode;
	}

	/**
	 * Gets the city code.
	 * 
	 * @return the city code
	 */
	public String getCityCode() {

		return cityCode;
	}

	/**
	 * Sets the city code.
	 * 
	 * @param cityCode
	 *            the new city code
	 */
	public void setCityCode(String cityCode) {

		this.cityCode = cityCode;
	}

	/**
	 * Gets the chart no.
	 * 
	 * @return the chart no
	 */
	public String getChartNo() {

		return chartNo;
	}

	/**
	 * Sets the chart no.
	 * 
	 * @param chartNo
	 *            the new chart no
	 */
	public void setChartNo(String chartNo) {

		this.chartNo = chartNo;
	}

	/**
	 * Gets the driver name.
	 * 
	 * @return the driver name
	 */
	public String getDriverName() {

		return driverName;
	}

	/**
	 * Sets the driver name.
	 * 
	 * @param driverName
	 *            the new driver name
	 */
	public void setDriverName(String driverName) {

		this.driverName = driverName;
	}

	/**
	 * Gets the vehicle no.
	 * 
	 * @return the vehicle no
	 */
	public String getVehicleNo() {

		return vehicleNo;
	}

	/**
	 * Sets the vehicle no.
	 * 
	 * @param vehicleNo
	 *            the new vehicle no
	 */
	public void setVehicleNo(String vehicleNo) {

		this.vehicleNo = vehicleNo;
	}

	/**
	 * Gets the po number.
	 * 
	 * @return the po number
	 */
	public String getPoNumber() {

		return poNumber;
	}

	/**
	 * Sets the po number.
	 * 
	 * @param poNumber
	 *            the new po number
	 */
	public void setPoNumber(String poNumber) {

		this.poNumber = poNumber;
	}

	/**
	 * Sets the mobile number.
	 * 
	 * @param mobileNumber
	 *            the new mobile number
	 */
	public void setMobileNumber(String mobileNumber) {

		this.mobileNumber = mobileNumber;
	}

	/**
	 * Gets the mobile number.
	 * 
	 * @return the mobile number
	 */
	public String getMobileNumber() {

		return mobileNumber;
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
	 * Gets the status code.
	 * 
	 * @return the status code
	 */
	public int getStatusCode() {

		return statusCode;
	}

	/**
	 * Gets the status msg.
	 * 
	 * @return the status msg
	 */
	public String getStatusMsg() {

		return statusMsg;
	}

	/**
	 * Sets the status msg.
	 * 
	 * @param statusMsg
	 *            the new status msg
	 */
	public void setStatusMsg(String statusMsg) {

		this.statusMsg = statusMsg;
	}

	/**
	 * Sets the offline procurement details.
	 * 
	 * @param offlineProcurementDetails
	 *            the new offline procurement details
	 */
	public void setOfflineProcurementDetails(Set<OfflineProcurementDetail> offlineProcurementDetails) {

		this.offlineProcurementDetails = offlineProcurementDetails;
	}

	/**
	 * Gets the offline procurement details.
	 * 
	 * @return the offline procurement details
	 */
	public Set<OfflineProcurementDetail> getOfflineProcurementDetails() {

		return offlineProcurementDetails;
	}

	/**
	 * Sets the payment amt.
	 * 
	 * @param paymentAmt
	 *            the new payment amt
	 */
	public void setPaymentAmt(String paymentAmt) {

		this.paymentAmt = paymentAmt;
	}

	/**
	 * Gets the payment amt.
	 * 
	 * @return the payment amt
	 */
	public String getPaymentAmt() {

		return paymentAmt;
	}

	public String getProcMasterType() {
		return procMasterType;
	}

	public void setProcMasterType(String procMasterType) {
		this.procMasterType = procMasterType;
	}

	public String getProcMasterTypeId() {
		return procMasterTypeId;
	}

	public void setProcMasterTypeId(String procMasterTypeId) {
		this.procMasterTypeId = procMasterTypeId;
	}

	public String getPaymentMode() {

		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {

		this.paymentMode = paymentMode;
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

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getRoadMapCode() {

		return roadMapCode;
	}

	public void setRoadMapCode(String roadMapCode) {

		this.roadMapCode = roadMapCode;
	}

	public String getSubstituteName() {

		return substituteName;
	}

	public void setSubstituteName(String substituteName) {

		this.substituteName = substituteName;
	}

	public String getFarmerAttnce() {

		return farmerAttnce;
	}

	public void setFarmerAttnce(String farmerAttnce) {

		this.farmerAttnce = farmerAttnce;
	}

	public String getFarmId() {

		return farmId;
	}

	public void setFarmId(String farmId) {

		this.farmId = farmId;
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

	public Double getInvoiceValue() {
		return invoiceValue;
	}

	public void setInvoiceValue(Double invoiceValue) {
		this.invoiceValue = invoiceValue;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getCropType() {
		return cropType;
	}

	public void setCropType(String cropType) {
		this.cropType = cropType;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getIsReg() {
		return isReg;
	}

	public void setIsReg(String isReg) {
		this.isReg = isReg;
	}

	public Customer getBuyer() {
		return buyer;
	}

	public void setBuyer(Customer buyer) {
		this.buyer = buyer;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Double getLoanInterest() {
		return loanInterest;
	}

	public void setLoanInterest(Double loanInterest) {
		this.loanInterest = loanInterest;
	}

	public Double getLoanRepaymentAmt() {
		return loanRepaymentAmt;
	}

	public void setLoanRepaymentAmt(Double loanRepaymentAmt) {
		this.loanRepaymentAmt = loanRepaymentAmt;
	}

	public Double getFinalPayAmt() {
		return finalPayAmt;
	}

	public void setFinalPayAmt(Double finalPayAmt) {
		this.finalPayAmt = finalPayAmt;
	}
	
	
	

}
