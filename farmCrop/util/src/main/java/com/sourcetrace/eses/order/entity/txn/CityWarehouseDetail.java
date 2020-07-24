/*
 * CityWarehouseDetail.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class CityWarehouseDetail {

    public enum TYPE {
        PROCUREMENT, PROCUREMENT_MTNT, PROCUREMENT_MTNR, SUPPLIER_PROCUREMENT,PROCUREMENT_TRACEABILITY,COLD_STORAGE,COLD_STORAGE_STOCK_TRANSFER
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
    private CityWarehouse cityWarehouse;
    private ProcurementProduct procurementProduct;
    private ProcurementGrade procurementGrade;
    private String batchNo;
    private String blockName;
    private String floorName;
    private String bayNumber;

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the date.
     * @return the date
     */
    public Date getDate() {

        return date;
    }

    /**
     * Sets the date.
     * @param date the new date
     */
    public void setDate(Date date) {

        this.date = date;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public int getType() {

        return type;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(int type) {

        this.type = type;
    }

    /**
     * Gets the reference id.
     * @return the reference id
     */
    public long getReferenceId() {

        return referenceId;
    }

    /**
     * Sets the reference id.
     * @param referenceId the new reference id
     */
    public void setReferenceId(long referenceId) {

        this.referenceId = referenceId;
    }

    /**
     * Gets the previous number of bags.
     * @return the previous number of bags
     */
    public long getPreviousNumberOfBags() {

        return previousNumberOfBags;
    }

    /**
     * Sets the previous number of bags.
     * @param previousNumberOfBags the new previous number of bags
     */
    public void setPreviousNumberOfBags(long previousNumberOfBags) {

        this.previousNumberOfBags = previousNumberOfBags;
    }

    /**
     * Gets the previous gross weight.
     * @return the previous gross weight
     */
    public double getPreviousGrossWeight() {

        return previousGrossWeight;
    }

    /**
     * Sets the previous gross weight.
     * @param previousGrossWeight the new previous gross weight
     */
    public void setPreviousGrossWeight(double previousGrossWeight) {

        this.previousGrossWeight = previousGrossWeight;
    }

    /**
     * Gets the txn number of bags.
     * @return the txn number of bags
     */
    public long getTxnNumberOfBags() {

        return txnNumberOfBags;
    }

    /**
     * Sets the txn number of bags.
     * @param txnNumberOfBags the new txn number of bags
     */
    public void setTxnNumberOfBags(long txnNumberOfBags) {

        this.txnNumberOfBags = txnNumberOfBags;
    }

    /**
     * Gets the txn gross weight.
     * @return the txn gross weight
     */
    public double getTxnGrossWeight() {

        return txnGrossWeight;
    }

    /**
     * Sets the txn gross weight.
     * @param txnGrossWeight the new txn gross weight
     */
    public void setTxnGrossWeight(double txnGrossWeight) {

        this.txnGrossWeight = txnGrossWeight;
    }

    /**
     * Gets the total number of bags.
     * @return the total number of bags
     */
    public long getTotalNumberOfBags() {

        return totalNumberOfBags;
    }

    /**
     * Sets the total number of bags.
     * @param totalNumberOfBags the new total number of bags
     */
    public void setTotalNumberOfBags(long totalNumberOfBags) {

        this.totalNumberOfBags = totalNumberOfBags;
    }

    /**
     * Gets the total gross weight.
     * @return the total gross weight
     */
    public double getTotalGrossWeight() {

        return totalGrossWeight;
    }

    /**
     * Sets the total gross weight.
     * @param totalGrossWeight the new total gross weight
     */
    public void setTotalGrossWeight(double totalGrossWeight) {

        this.totalGrossWeight = totalGrossWeight;
    }

    /**
     * Gets the description.
     * @return the description
     */
    public String getDescription() {

        return description;
    }

    /**
     * Sets the description.
     * @param description the new description
     */
    public void setDescription(String description) {

        this.description = description;
    }

    /**
     * Gets the city warehouse.
     * @return the city warehouse
     */
    public CityWarehouse getCityWarehouse() {

        return cityWarehouse;
    }

    /**
     * Sets the city warehouse.
     * @param cityWarehouse the new city warehouse
     */
    public void setCityWarehouse(CityWarehouse cityWarehouse) {

        this.cityWarehouse = cityWarehouse;
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
    

}
