/*
 * Distribution.java
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
import java.util.List;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;

public class Distribution implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FARMER = "farmer";
	public static final String AGENT = "agent";

	public static final String DISTRIBUTION_AMOUNT = "DISTRIBUTION PAYMENT";
	public static final String PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION = "PRODUCT DISTRIBUTION TO FARMER";
	public static final String PRODUCT_DISTRIBUTION_TO_FIELDSTAFF_DESCRIPTION = "PRODUCT DISTRIBUTION TO FIELDSTAFF";
	public static final String PRODUCT_RETURN_FROM_FARMER_DESCRIPTION = "PRODUCT RETURN FROM FARMER";
	public static final String PRODUCT_RETURN_FROM_FIELDSTAFF_DESCRIPTION = "PRODUCT RETURN FROM FIELDSTAFF";
	public static final String DISTRIBUTION_PAYMENT_AMOUNT = "DISTRIBUTION PAYMENT AMOUNT";
	
	public static final String DISTRIBUTION_DELETE_FARMER = "DISTRIBUTION DELETE TO FARMER";
	
	public static final String DISTRIBUTION_DELETE_FIELDSTAFF = "DISTRIBUTION DELETE TO FIELDSTAFF";
	
	public static final int APPROVED=1;
	public static final String PRODUCT_DISTRIBUTION_TO_FARMER = "314";
	public static final String PRODUCT_RETURN_FROM_FARMER = "344";
	public static final String PRODUCT_DISTRIBUTION_TO_FIELDSTAFF = "514";
	public static final String PRODUCT_RETURN_FROM_FIELDSTAFF = "345";
	public static final int NOT_APPROVED=0;
	public static final int DELETE_STATUS=2;
	public static final String PRODUCT_DISTRIBUTION_FARMER_BALANCE = "395";
	
	private long id;
	private Village village;
	private AgroTransaction agroTransaction;
	private Season season;
	private Set<DistributionDetail> distributionDetails;
	private String referenceNo;
	private String serialNo;
	private double paymentAmount;
	private String mobileNumber;
	private double costPrice;
	private double sellingPrice;
	private Double totalAmount;
	private Double tax;
	private Double finalAmount;
	private String paymentMode;
	private String receiptNumber;
	private double intBalance;
	private double txnAmount;
	private String farmerId;
	private String agentId;
	private String servicePointId;
	private String freeDistribution;
	private String farmerName;
	private String agentName;
	private String servicePointName;
	private Date txnTime;
	private String villageName;
	private double balAmount;
	private String samithiName;
	private String txnType;
	private String productStock;
	private String isFreeDistribution;
	private String branchId;
	private String seasonCode;
	private HarvestSeason harvestSeason;
	private String updatedUserName;
	private Date updateTime;
	private Set<PMTImageDetails> pmtImageDetail;
	private String warehouseCode;
	private String warehouseName;
	private String samithiId;
	private String latitude;
	private String Longitude;
	/* Transient Variable */

	
	private int status;
	private String userName;
	private String transactionTime;
	private String farmerType;
	private String ProductCode;
	private String enableApproved;
	private long productId;
	private String tenantId;
	private String stockType;
	
	private String stateName;
    private String fpo;
    private String icsName;
    private long groupId;

	/* Query Field */
	private String fatherName;
	
	/**
	 * Transient variable
	 */
	private List<String> branchesList;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */

	public long getId() {

		return id;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Double finalAmount) {
		this.finalAmount = finalAmount;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
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
	 * Sets the distribution details.
	 * 
	 * @param distributionDetails
	 *            the new distribution details
	 */
	public void setDistributionDetails(Set<DistributionDetail> distributionDetails) {

		this.distributionDetails = distributionDetails;
	}

	/**
	 * Gets the distribution details.
	 * 
	 * @return the distribution details
	 */
	public Set<DistributionDetail> getDistributionDetails() {

		return distributionDetails;
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
	 * Sets the season.
	 * 
	 * @param season
	 *            the new season
	 */
	public void setSeason(Season season) {

		this.season = season;
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
	 * Sets the reference no.
	 * 
	 * @param referenceNo
	 *            the new reference no
	 */
	public void setReferenceNo(String referenceNo) {

		this.referenceNo = referenceNo;
	}

	/**
	 * Gets the reference no.
	 * 
	 * @return the reference no
	 */
	public String getReferenceNo() {

		return referenceNo;
	}

	/**
	 * Sets the serial no.
	 * 
	 * @param serialNo
	 *            the new serial no
	 */
	public void setSerialNo(String serialNo) {

		this.serialNo = serialNo;
	}

	/**
	 * Gets the serial no.
	 * 
	 * @return the serial no
	 */
	public String getSerialNo() {

		return serialNo;
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
	 * Gets the payment amount.
	 * 
	 * @return the payment amount
	 */
	public double getPaymentAmount() {

		return paymentAmount;
	}

	/**
	 * Gets the txn type.
	 * 
	 * @return the txn type
	 */
	public String getTxnType() {

		return txnType;
	}

	/**
	 * Sets the txn type.
	 * 
	 * @param txnType
	 *            the new txn type
	 */
	public void setTxnType(String txnType) {

		this.txnType = txnType;
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

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public double getIntBalance() {
		return intBalance;
	}

	public void setIntBalance(double intBalance) {
		this.intBalance = intBalance;
	}

	public double getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(double txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getServicePointId() {
		return servicePointId;
	}

	public void setServicePointId(String servicePointId) {
		this.servicePointId = servicePointId;
	}

	public String getFreeDistribution() {

		return freeDistribution;
	}

	public void setFreeDistribution(String freeDistribution) {

		this.freeDistribution = freeDistribution;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getServicePointName() {
		return servicePointName;
	}

	public void setServicePointName(String servicePointName) {
		this.servicePointName = servicePointName;
	}

	public Date getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(Date txnTime) {
		this.txnTime = txnTime;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public double getBalAmount() {
		return balAmount;
	}

	public void setBalAmount(double balAmount) {
		this.balAmount = balAmount;
	}

	public String getSamithiName() {
		return samithiName;
	}

	public void setSamithiName(String samithiName) {
		this.samithiName = samithiName;
	}

	public String getProductStock() {
		return productStock;
	}

	public void setProductStock(String productStock) {
		this.productStock = productStock;
	}

	public String getIsFreeDistribution() {
		return isFreeDistribution;
	}

	public void setIsFreeDistribution(String isFreeDistribution) {
		this.isFreeDistribution = isFreeDistribution;
	}

	public String getProductCode() {
		return ProductCode;
	}

	public void setProductCode(String productCode) {
		ProductCode = productCode;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFatherName() {

		return fatherName;
	}

	public void setFatherName(String fatherName) {

		this.fatherName = fatherName;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

    public HarvestSeason getHarvestSeason() {
    
        return harvestSeason;
    }

    public void setHarvestSeason(HarvestSeason harvestSeason) {
    
        this.harvestSeason = harvestSeason;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getFarmerType() {
		return farmerType;
	}

	public void setFarmerType(String farmerType) {
		this.farmerType = farmerType;
	}

	public String getUpdatedUserName() {
		return updatedUserName;
	}

	public void setUpdatedUserName(String updatedUserName) {
		this.updatedUserName = updatedUserName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getEnableApproved() {
		return enableApproved;
	}

	public void setEnableApproved(String enableApproved) {
		this.enableApproved = enableApproved;
	}

    public long getProductId() {
    
        return productId;
    }

    public void setProductId(long productId) {
    
        this.productId = productId;
    }

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

    public String getTenantId() {
    
        return tenantId;
    }

    public void setTenantId(String tenantId) {
    
        this.tenantId = tenantId;
    }

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getFpo() {
		return fpo;
	}

	public void setFpo(String fpo) {
		this.fpo = fpo;
	}

	public String getIcsName() {
		return icsName;
	}

	public void setIcsName(String icsName) {
		this.icsName = icsName;
	}

	public Set<PMTImageDetails> getPmtImageDetail() {
		return pmtImageDetail;
	}

	public void setPmtImageDetail(Set<PMTImageDetails> pmtImageDetail) {
		this.pmtImageDetail = pmtImageDetail;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getSamithiId() {
		return samithiId;
	}

	public void setSamithiId(String samithiId) {
		this.samithiId = samithiId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
    
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
   
}
