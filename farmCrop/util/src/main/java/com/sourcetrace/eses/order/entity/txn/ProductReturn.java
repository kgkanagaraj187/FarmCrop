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
import java.util.Map;
import java.util.Set;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;

public class ProductReturn implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FARMER = "farmer";
	public static final String AGENT = "agent";

	public static final String PRODUCT_RETURN_AMOUNT = "PRODUCT RETURN PAYMENT";
	
	public static final String PRODUCT_RETURN_FROM_FARMER_DESCRIPTION = "PRODUCT RETURN FROM FARMER";
	public static final String PRODUCT_RETURN_FROM_FIELDSTAFF_DESCRIPTION = "PRODUCT RETURN FROM FIELDSTAFF";
	
	public static final String PRODUCT_RETURN_PAYMENT_AMOUNT = "PRODUCT RETURN PAYMENT AMOUNT";

	public static final String PRODUCT_DISTRIBUTION_TO_FARMER = "314";
	public static final String PRODUCT_DISTRIBUTION_TO_FIELDSTAFF = "514";
	public static final String PRODUCT_RETURN_FROM_FARMER = "344";
	public static final String PRODUCT_RETURN_FROM_FIELDSTAFF = "345";
	public static final int NOT_APPROVED=0;
	public static final int APPROVED=1;
	
	public static final String WAREHOUSE_STOCK="WarehouseStock";
	public static final String MOBILE_STOCK="MobileUserStock";
	
	public static final String FARMER_REGISTERED="Registered";
	public static final String FARMER_UN_REGISTERD="Unregistered";
	
	private long id;
	private Village village;
	private AgroTransaction agroTransaction;
	private Season season;
	private Set<ProductReturnDetail> productReturnDetail;
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
	private Map<String, String> filterData;
	private Set<PMTImageDetails> pmtImageDetail;

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

	/* Query Field */
	private String fatherName;
	private Warehouse warehouse;
	private Agent agent;
	
	/**
	 * Transient variable
	 */
	private List<String> branchesList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Set<ProductReturnDetail> getProductReturnDetail() {
		return productReturnDetail;
	}

	public void setProductReturnDetail(Set<ProductReturnDetail> productReturnDetail) {
		this.productReturnDetail = productReturnDetail;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
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

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
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

	public HarvestSeason getHarvestSeason() {
		return harvestSeason;
	}

	public void setHarvestSeason(HarvestSeason harvestSeason) {
		this.harvestSeason = harvestSeason;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getProductCode() {
		return ProductCode;
	}

	public void setProductCode(String productCode) {
		ProductCode = productCode;
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

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	
	public boolean isPaymentAvailable() {

		return (this.paymentAmount > 0d);
	}

	public Map<String, String> getFilterData() {
		return filterData;
	}

	public void setFilterData(Map<String, String> filterData) {
		this.filterData = filterData;
	}

	public Set<PMTImageDetails> getPmtImageDetail() {
		return pmtImageDetail;
	}

	public void setPmtImageDetail(Set<PMTImageDetails> pmtImageDetail) {
		this.pmtImageDetail = pmtImageDetail;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}


	
}
