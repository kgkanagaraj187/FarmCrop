/*
 * OfflineSupplierProcurement.java
 * Copyright (c) 2017-2018, SourceTrace Systems, All Rights Reserved.
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

public class OfflineSupplierProcurement implements Serializable {

	private static final long serialVersionUID = -5076127821638541492L;
	public static final String GOODS_PRODUCT_TYPE = "0";
	public static final String CARRIER_PRODUCT_TYPE = "1";

	private long id;	
	private String procurementDate;
	private String receiptNo;
	private String paymentAmt;
	private String type;
	private String totalAmount;
	private String agentId;
	private String deviceId;
	private String servicePointId;
	private String samithiCode;	
	private String cityCode;	
	private String mobileNumber;
	private int statusCode;
	private String statusMsg;
	private Set<OfflineSupplierProcurementDetail> offlineSupplierProcurementDetails;
	private String procMasterType;
	private String procMasterTypeId;	
	private String branchId;
	private String seasonCode;
	private String createdUser;
	private String updatedUser;
	private Date createdDate;
	private Date updatedDate;	
	private String latitude;
	private String longitude;	
	private String warehouseId;	
	private String invoiceNo;
	private String isRegSupplier;
	private Double totalLabourCost;
	private Double transportCost;
	private Double invoiceValue;
	private Double taxAmt;
	private Double otherCost;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getProcurementDate() {
		return procurementDate;
	}
	public void setProcurementDate(String procurementDate) {
		this.procurementDate = procurementDate;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getPaymentAmt() {
		return paymentAmt;
	}
	public void setPaymentAmt(String paymentAmt) {
		this.paymentAmt = paymentAmt;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getServicePointId() {
		return servicePointId;
	}
	public void setServicePointId(String servicePointId) {
		this.servicePointId = servicePointId;
	}
	public String getSamithiCode() {
		return samithiCode;
	}
	public void setSamithiCode(String samithiCode) {
		this.samithiCode = samithiCode;
	}
	
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public Set<OfflineSupplierProcurementDetail> getOfflineSupplierProcurementDetails() {
		return offlineSupplierProcurementDetails;
	}
	public void setOfflineSupplierProcurementDetails(
			Set<OfflineSupplierProcurementDetail> offlineSupplierProcurementDetails) {
		this.offlineSupplierProcurementDetails = offlineSupplierProcurementDetails;
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
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getIsRegSupplier() {
		return isRegSupplier;
	}
	public void setIsRegSupplier(String isRegSupplier) {
		this.isRegSupplier = isRegSupplier;
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
	public Double getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(Double taxAmt) {
		this.taxAmt = taxAmt;
	}
	public Double getOtherCost() {
		return otherCost;
	}
	public void setOtherCost(Double otherCost) {
		this.otherCost = otherCost;
	}
	



}
