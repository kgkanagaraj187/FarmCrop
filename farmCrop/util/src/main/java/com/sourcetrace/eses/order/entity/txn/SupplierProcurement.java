/*
 * SupplierProcurement.java
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
import java.util.Map;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;

public class SupplierProcurement implements Serializable {

	private static final long serialVersionUID = -691522973746677689L;

	public static enum productType {
		GOODS, CARRIER
	}

	public static final String PROCUREMENT_AMOUNT = "PROCUREMENT AMOUNT";
	public static final String PROCUREMENT_PAYMENT = "PROCUREMENT PAYMENT";
	public static final String PROCUREMENT_PAYMENT_AMOUNT = "PROCUREMENT PAYMENT AMOUNT";
	public static final String CASH_WITHDRAW = "CASH WITHDRAW";
	public static final String TXN_TYPE = "378";
	public static final int DEFAULT_FARMER_ATTENDENCE = 0;
	public static final String PROCURMEMENT = "SUPPLIER PROCUREMENT";

	private long id;
	private int type;
	private Village village;
	private AgroTransaction agroTransaction;	
	private Season season;
	private Set<SupplierProcurementDetail> supplierProcurementDetails;
	private double paymentAmount;
	private String mobileNumber;
	private String procMasterType;
	private String procMasterTypeId;
	private String paymentMode;
	private double totalProVal;
	private String seasonCode;
	private String createdUser;
	private String updatedUser;
	private Date createdDate;
	private Date updatedDate;	
	private String latitude;
	private String longitude;	
	private int isRegSupplier;
	private String trader;
	private Double totalLabourCost;
	private Double transportCost;
	private Double invoiceValue;
	private Double taxAmt;
	private Double otherCost;
	
	
	/* Transient Variable */
	private String branchId;
	private String warehouseName;
	private Map<String, String> filterData;
	private String ffc;	
	private String invoiceNo;
	private String warehouseId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Village getVillage() {
		return village;
	}
	public void setVillage(Village village) {
		this.village = village;
	}
	public AgroTransaction getAgroTransaction() {
		return agroTransaction;
	}
	public void setAgroTransaction(AgroTransaction agroTransaction) {
		this.agroTransaction = agroTransaction;
	}
	public Season getSeason() {
		return season;
	}
	public void setSeason(Season season) {
		this.season = season;
	}
	public Set<SupplierProcurementDetail> getSupplierProcurementDetails() {
		return supplierProcurementDetails;
	}
	public void setSupplierProcurementDetails(Set<SupplierProcurementDetail> supplierProcurementDetails) {
		this.supplierProcurementDetails = supplierProcurementDetails;
	}
	public double getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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
	public double getTotalProVal() {
		return totalProVal;
	}
	public void setTotalProVal(double totalProVal) {
		this.totalProVal = totalProVal;
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
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public Map<String, String> getFilterData() {
		return filterData;
	}
	public void setFilterData(Map<String, String> filterData) {
		this.filterData = filterData;
	}
	public String getFfc() {
		return ffc;
	}
	public void setFfc(String ffc) {
		this.ffc = ffc;
	}		
	public int getIsRegSupplier() {
		return isRegSupplier;
	}
	public void setIsRegSupplier(int isRegSupplier) {
		this.isRegSupplier = isRegSupplier;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getTrader() {
		return trader;
	}
	public void setTrader(String trader) {
		this.trader = trader;
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
