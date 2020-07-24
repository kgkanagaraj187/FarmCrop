/*
 * PMTDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Village;

// TODO: Auto-generated Javadoc
public class ColdStorageStockTransferDetail {

	private long id;	
	private ProcurementProduct procurementProduct;
	private ProcurementGrade procurementGrade;
	private long noOfBags;	
	private double qty;
	private String blockName;
	private String floorName;
	private String bayNumber;
	private ColdStorageStockTransfer coldStorageStockTransfer;
	private Farmer farmer;
	private ProcurementVariety procurementVariety;
	private String batchNo;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public ProcurementProduct getProcurementProduct() {
		return procurementProduct;
	}
	public void setProcurementProduct(ProcurementProduct procurementProduct) {
		this.procurementProduct = procurementProduct;
	}
	public ProcurementGrade getProcurementGrade() {
		return procurementGrade;
	}
	public void setProcurementGrade(ProcurementGrade procurementGrade) {
		this.procurementGrade = procurementGrade;
	}
	public long getNoOfBags() {
		return noOfBags;
	}
	public void setNoOfBags(long noOfBags) {
		this.noOfBags = noOfBags;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
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
	public ColdStorageStockTransfer getColdStorageStockTransfer() {
		return coldStorageStockTransfer;
	}
	public void setColdStorageStockTransfer(ColdStorageStockTransfer coldStorageStockTransfer) {
		this.coldStorageStockTransfer = coldStorageStockTransfer;
	}
	public Farmer getFarmer() {
		return farmer;
	}
	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}
	public ProcurementVariety getProcurementVariety() {
		return procurementVariety;
	}
	public void setProcurementVariety(ProcurementVariety procurementVariety) {
		this.procurementVariety = procurementVariety;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	
	

}
