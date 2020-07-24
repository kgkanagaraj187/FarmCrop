/*
 * Procurement.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;

public class ColdStorage implements Serializable {

	
	/**
	 * 
	 */
	
	public static enum BONDSTATUS {
		NA,AWAITING,APPROVED,REJECTED
	}
	
	private static final long serialVersionUID = 1L;
	private long id;
	private String coldStorageName;
	private Double maxBayHold;
	private String createdUser;
	private String updatedUser;
	private Date createdDate;
	private Date updatedDate;	
	private String branchId;
	private Warehouse warehouse;
	private String batchNo;
	private String bondNo;
	private double totalQty;
	private String revisionNo;
	private int totalBags;
	private Set<ColdStorageDetail> coldStorageDetail;
	private Farmer farmer;
	private String roundOfHarvesting;
	private String bondStatus;
	private byte[] img;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getColdStorageName() {
		return coldStorageName;
	}
	public void setColdStorageName(String coldStorageName) {
		this.coldStorageName = coldStorageName;
	}
	public Double getMaxBayHold() {
		return maxBayHold;
	}
	public void setMaxBayHold(Double maxBayHold) {
		this.maxBayHold = maxBayHold;
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
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getBondNo() {
		return bondNo;
	}
	public void setBondNo(String bondNo) {
		this.bondNo = bondNo;
	}
	public double getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(double totalQty) {
		this.totalQty = totalQty;
	}
	public String getRevisionNo() {
		return revisionNo;
	}
	public void setRevisionNo(String revisionNo) {
		this.revisionNo = revisionNo;
	}
	public int getTotalBags() {
		return totalBags;
	}
	public void setTotalBags(int totalBags) {
		this.totalBags = totalBags;
	}
	public Set<ColdStorageDetail> getColdStorageDetail() {
		return coldStorageDetail;
	}
	public void setColdStorageDetail(Set<ColdStorageDetail> coldStorageDetail) {
		this.coldStorageDetail = coldStorageDetail;
	}
	public Farmer getFarmer() {
		return farmer;
	}
	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}
	public String getRoundOfHarvesting() {
		return roundOfHarvesting;
	}
	public void setRoundOfHarvesting(String roundOfHarvesting) {
		this.roundOfHarvesting = roundOfHarvesting;
	}
	public String getBondStatus() {
		return bondStatus;
	}
	public void setBondStatus(String bondStatus) {
		this.bondStatus = bondStatus;
	}
	public byte[] getImg() {
		return img;
	}
	public void setImg(byte[] img) {
		this.img = img;
	}
	

	
}
