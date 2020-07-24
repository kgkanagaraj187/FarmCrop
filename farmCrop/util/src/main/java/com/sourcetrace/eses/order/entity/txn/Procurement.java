/*
 * Procurement.java
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
import java.util.Map;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;

public class Procurement implements Serializable {

	private static final long serialVersionUID = -691522973746677689L;

	public static enum productType {
		GOODS, CARRIER
	}

	public static final String PROCUREMENT_AMOUNT = "PROCUREMENT AMOUNT";
	public static final String PROCUREMENT_PAYMENT = "PROCUREMENT PAYMENT";
	public static final String PROCUREMENT_PAYMENT_AMOUNT = "PROCUREMENT PAYMENT AMOUNT";
	public static final String CASH_WITHDRAW = "CASH WITHDRAW";
	public static final String TXN_TYPE = "316";
	public static final int DEFAULT_FARMER_ATTENDENCE = 0;
	public static final String PROCURMEMENT = "PROCUREMENT";
	public static final String DELETE_STATUS = "1";
	public static final String PROCURMEMENT_DELETE = "PROCURMEMENT DELETE";
	public static final String REPAYMENT_TXN_TYPE = "702";
	public static final String REPAYMENT_AMOUNT = "PROCUREMENT PAYMENT WITH LOAN DEDUCTION";
	private long id;
	private int type;
	private Village village;
	private AgroTransaction agroTransaction;
	private TripSheet tripSheet;
	private String poNumber;
	private Season season;
	private Set<ProcurementDetail> procurementDetails;
	private String unit;
	private double paymentAmount;
	private String mobileNumber;
	private String procMasterType;
	private String procMasterTypeId;
	private String paymentMode;
	private double totalProVal;
	private String seasonCode;
	private Farmer farmer;
	private String createdUser;
	private String updatedUser;
	private Date createdDate;
	private Date updatedDate;
	private String roadMapCode;
	private String substituteName;
	private String farmerAttnce;
	private String vehicleNum;
	private String farmId;
	private String latitude;
	private String longitude;
	private String batchNo;
	private Double totalLabourCost;
	private Double transportCost;
	private Double invoiceValue;
	private int isReg;
	private Customer buyer;
	private Integer status;
	/* Transient Variable */
	private String branchId;
	private String warehouseName;
	private Map<String, String> filterData;
	private String ffc;
	private String cropType;
	private String invoiceNo;

	private String farm;
	private String warehouseId;
	private String vehicleType;
	
	
	private double actualAmount;
	private double loanInterest;
	private double finalPayAmt;
	private double loanAmount;

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
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(int type) {

		this.type = type;
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
	 * Gets the village.
	 * 
	 * @return the village
	 */
	public Village getVillage() {

		return village;
	}

	/**
	 * Sets the agro transaction.
	 * 
	 * @param agroTransaction
	 *            the new agro transaction
	 */
	public void setAgroTransaction(AgroTransaction agroTransaction) {

		this.agroTransaction = agroTransaction;
	}

	/**
	 * Gets the agro transaction.
	 * 
	 * @return the agro transaction
	 */
	public AgroTransaction getAgroTransaction() {

		return agroTransaction;
	}

	/**
	 * Sets the village.
	 * 
	 * @param village
	 *            the new village
	 */
	public void setVillage(Village village) {

		this.village = village;
	}

	/**
	 * Gets the trip sheet.
	 * 
	 * @return the trip sheet
	 */
	public TripSheet getTripSheet() {

		return tripSheet;
	}

	/**
	 * Sets the trip sheet.
	 * 
	 * @param tripSheet
	 *            the new trip sheet
	 */
	public void setTripSheet(TripSheet tripSheet) {

		this.tripSheet = tripSheet;
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
	 * Gets the season.
	 * 
	 * @return the season
	 */
	public Season getSeason() {

		return season;
	}

	/**
	 * Sets the season.
	 * 
	 * @param season
	 *            the new season
	 */
	public void setSeason(Season season) {

		this.season = season;
	}

	/**
	 * Sets the procurement details.
	 * 
	 * @param procurementDetails
	 *            the new procurement details
	 */
	public void setProcurementDetails(Set<ProcurementDetail> procurementDetails) {

		this.procurementDetails = procurementDetails;
	}

	/**
	 * Gets the procurement details.
	 * 
	 * @return the procurement details
	 */
	public Set<ProcurementDetail> getProcurementDetails() {

		return procurementDetails;
	}

	/**
	 * Gets the payment amount.
	 * 
	 * @return the payment amount
	 */
	public double getPaymentAmount() {

		return paymentAmount;
	}

	/**
	 * Sets the payment amount.
	 * 
	 * @param paymentAmount
	 *            the new payment amount
	 */
	public void setPaymentAmount(double paymentAmount) {

		this.paymentAmount = paymentAmount;
	}

	/**
	 * Checks if is payment available.
	 * 
	 * @return true, if is payment available
	 */
	public boolean isPaymentAvailable() {

		return (this.paymentAmount > 0d);
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

	public Farmer getFarmer() {

		return farmer;
	}

	public void setFarmer(Farmer farmer) {

		this.farmer = farmer;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
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

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
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

	public String getVehicleNum() {

		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {

		this.vehicleNum = vehicleNum;
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

	public Map<String, String> getFilterData() {
		return filterData;
	}

	public void setFilterData(Map<String, String> filterData) {
		this.filterData = filterData;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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

	public String getFfc() {
		return ffc;
	}

	public void setFfc(String ffc) {
		this.ffc = ffc;
	}

	public String getCropType() {
		return cropType;
	}

	public void setCropType(String cropType) {
		this.cropType = cropType;
	}

	public String getFarm() {
		return farm;
	}

	public void setFarm(String farm) {
		this.farm = farm;
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

	public int getIsReg() {
		return isReg;
	}

	public void setIsReg(int isReg) {
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(double actualAmount) {
		this.actualAmount = actualAmount;
	}

	public double getLoanInterest() {
		return loanInterest;
	}

	public void setLoanInterest(double loanInterest) {
		this.loanInterest = loanInterest;
	}

	public double getFinalPayAmt() {
		return finalPayAmt;
	}

	public void setFinalPayAmt(double finalPayAmt) {
		this.finalPayAmt = finalPayAmt;
	}

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

}
