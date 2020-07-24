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
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;

public class ColdStorageDetail implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private ProcurementGrade procurementGrade;
	private String blockName;
	private String floorName;
	private String bayNumber;
	private long noOfBags;
	private Double prevQty;
	private Double txnQty;
	private Double finalQty;
	private ColdStorage coldStorage;
	private ProcurementVariety procurementVariety;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public ProcurementGrade getProcurementGrade() {
		return procurementGrade;
	}
	public void setProcurementGrade(ProcurementGrade procurementGrade) {
		this.procurementGrade = procurementGrade;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public String getFloorName() {
		return floorName;
	}
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	public String getBayNumber() {
		return bayNumber;
	}
	public void setBayNumber(String bayNumber) {
		this.bayNumber = bayNumber;
	}
	public long getNoOfBags() {
		return noOfBags;
	}
	public void setNoOfBags(long noOfBags) {
		this.noOfBags = noOfBags;
	}
	public Double getPrevQty() {
		return prevQty;
	}
	public void setPrevQty(Double prevQty) {
		this.prevQty = prevQty;
	}
	public Double getTxnQty() {
		return txnQty;
	}
	public void setTxnQty(Double txnQty) {
		this.txnQty = txnQty;
	}
	public Double getFinalQty() {
		return finalQty;
	}
	public void setFinalQty(Double finalQty) {
		this.finalQty = finalQty;
	}
	public ColdStorage getColdStorage() {
		return coldStorage;
	}
	public void setColdStorage(ColdStorage coldStorage) {
		this.coldStorage = coldStorage;
	}
	public ProcurementVariety getProcurementVariety() {
		return procurementVariety;
	}
	public void setProcurementVariety(ProcurementVariety procurementVariety) {
		this.procurementVariety = procurementVariety;
	}
	
	
	
	
}
