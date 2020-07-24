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

public class WarehouseStorageMap implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String coldStorageName;
	private Double maxBayHold;	
	private String branchId;
	private Warehouse warehouse;
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
	
	
	
}
