/*
 * SupplierWarehouseDetail.java
 * Copyright (c) 2017-2018, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class SupplierWarehouseDetail {

    public enum TYPE {
        PROCUREMENT, PROCUREMENT_MTNT, PROCUREMENT_MTNR
    }

    private long id;
    private Date date;
    private int type;
    private long referenceId;
    private long previousNumberOfBags;
    private double previousGrossWeight;
    private long txnNumberOfBags;
    private double txnGrossWeight;
    private long totalNumberOfBags;
    private double totalGrossWeight;
    private String description;
    private SupplierWarehouse supplierWarehouse;
    private ProcurementProduct procurementProduct;
    private ProcurementGrade procurementGrade;
    private String batchNo;
    private Farmer farmer;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}
	public long getPreviousNumberOfBags() {
		return previousNumberOfBags;
	}
	public void setPreviousNumberOfBags(long previousNumberOfBags) {
		this.previousNumberOfBags = previousNumberOfBags;
	}
	public double getPreviousGrossWeight() {
		return previousGrossWeight;
	}
	public void setPreviousGrossWeight(double previousGrossWeight) {
		this.previousGrossWeight = previousGrossWeight;
	}
	public long getTxnNumberOfBags() {
		return txnNumberOfBags;
	}
	public void setTxnNumberOfBags(long txnNumberOfBags) {
		this.txnNumberOfBags = txnNumberOfBags;
	}
	public double getTxnGrossWeight() {
		return txnGrossWeight;
	}
	public void setTxnGrossWeight(double txnGrossWeight) {
		this.txnGrossWeight = txnGrossWeight;
	}
	public long getTotalNumberOfBags() {
		return totalNumberOfBags;
	}
	public void setTotalNumberOfBags(long totalNumberOfBags) {
		this.totalNumberOfBags = totalNumberOfBags;
	}
	public double getTotalGrossWeight() {
		return totalGrossWeight;
	}
	public void setTotalGrossWeight(double totalGrossWeight) {
		this.totalGrossWeight = totalGrossWeight;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public SupplierWarehouse getSupplierWarehouse() {
		return supplierWarehouse;
	}
	public void setSupplierWarehouse(SupplierWarehouse supplierWarehouse) {
		this.supplierWarehouse = supplierWarehouse;
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
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Farmer getFarmer() {
		return farmer;
	}
	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

    

}
