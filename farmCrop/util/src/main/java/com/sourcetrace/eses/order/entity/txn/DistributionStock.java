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
import com.sourcetrace.eses.entity.Warehouse;


public class DistributionStock implements Serializable{
	public static final String DISTRIBUTION_TRANSACTION = "DISTRIBUTION STOCK TRANSACTION";
	public static final String DISTRIBUTION_STOCK_TRANSFER = "314DT";
	public static final String DISTRIBUTION_RECEPTION = "DISTRIBUTION STOCK RECEPTION";
	public static final String DISTRIBUTION_STOCK_RECEPTION = "314DR";
	private static final long serialVersionUID = 1L;
	private long id;
	private Set<DistributionStockDetail> distributionStockDetails;
	private Date txnTime;
	private String receiptNo;
	private String branchId;
	private List<String> branchesList;
	private Warehouse senderWarehouse;
	private Warehouse receiverWarehouse;
	private String truckId;
	private String driverName;
	private String loggedUser;
	private String txnType;
	private int status;
	private String season;
	
	/* Transient Variable */
	private AgroTransaction agrotxn;
	
	 private long  productCode;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Set<DistributionStockDetail> getDistributionStockDetails() {
		return distributionStockDetails;
	}
	public void setDistributionStockDetails(Set<DistributionStockDetail> distributionStockDetails) {
		this.distributionStockDetails = distributionStockDetails;
	}
	public Date getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(Date txnTime) {
		this.txnTime = txnTime;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public List<String> getBranchesList() {
		return branchesList;
	}
	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}
	public Warehouse getSenderWarehouse() {
		return senderWarehouse;
	}
	public void setSenderWarehouse(Warehouse senderWarehouse) {
		this.senderWarehouse = senderWarehouse;
	}
	public Warehouse getReceiverWarehouse() {
		return receiverWarehouse;
	}
	public void setReceiverWarehouse(Warehouse receiverWarehouse) {
		this.receiverWarehouse = receiverWarehouse;
	}
	public String getLoggedUser() {
		return loggedUser;
	}
	public void setLoggedUser(String loggedUser) {
		this.loggedUser = loggedUser;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getTruckId() {
		return truckId;
	}
	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public AgroTransaction getAgrotxn() {
		return agrotxn;
	}
	public void setAgrotxn(AgroTransaction agrotxn) {
		this.agrotxn = agrotxn;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getProductCode() {
		return productCode;
	}
	public void setProductCode(long productCode) {
		this.productCode = productCode;
	}
	
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	
    
}
