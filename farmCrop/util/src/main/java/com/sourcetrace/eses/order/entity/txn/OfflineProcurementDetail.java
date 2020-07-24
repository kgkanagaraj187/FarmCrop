/*
 * OfflineProcurementDetail.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;

public class OfflineProcurementDetail implements Serializable {

	private static final long serialVersionUID = -5478727008052783904L;
	private long id;
	private String quality;
	private String productCode;
	private String numberOfBags;
	private String numberOfFruitBags;
	private String grossWeight;
	private String tareWeight;
	private String netWeight;
	private String ratePerUnit;
	private String subTotal;
	private OfflineProcurement offlineProcurement;
	private String batchNo;
	private String unit;
	
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
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Gets the quality.
	 * 
	 * @return the quality
	 */
	public String getQuality() {
		return quality;
	}

	/**
	 * Sets the quality.
	 * 
	 * @param quality
	 *            the new quality
	 */
	public void setQuality(String quality) {
		this.quality = quality;
	}

	/**
	 * Gets the product code.
	 * 
	 * @return the product code
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * Sets the product code.
	 * 
	 * @param productCode
	 *            the new product code
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	/**
	 * Gets the number of bags.
	 * 
	 * @return the number of bags
	 */
	public String getNumberOfBags() {
		return numberOfBags;
	}

	/**
	 * Sets the number of bags.
	 * 
	 * @param numberOfBags
	 *            the new number of bags
	 */
	public void setNumberOfBags(String numberOfBags) {
		this.numberOfBags = numberOfBags;
	}

	/**
	 * Gets the gross weight.
	 * 
	 * @return the gross weight
	 */
	public String getGrossWeight() {
		return grossWeight;
	}

	/**
	 * Sets the gross weight.
	 * 
	 * @param grossWeight
	 *            the new gross weight
	 */
	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}

	/**
	 * Gets the tare weight.
	 * 
	 * @return the tare weight
	 */
	public String getTareWeight() {
		return tareWeight;
	}

	/**
	 * Sets the tare weight.
	 * 
	 * @param tareWeight
	 *            the new tare weight
	 */
	public void setTareWeight(String tareWeight) {
		this.tareWeight = tareWeight;
	}

	/**
	 * Gets the net weight.
	 * 
	 * @return the net weight
	 */
	public String getNetWeight() {
		return netWeight;
	}

	/**
	 * Sets the net weight.
	 * 
	 * @param netWeight
	 *            the new net weight
	 */
	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
	}

	/**
	 * Gets the rate per unit.
	 * 
	 * @return the rate per unit
	 */
	public String getRatePerUnit() {
		return ratePerUnit;
	}

	/**
	 * Sets the rate per unit.
	 * 
	 * @param ratePerUnit
	 *            the new rate per unit
	 */
	public void setRatePerUnit(String ratePerUnit) {
		this.ratePerUnit = ratePerUnit;
	}

	/**
	 * Gets the sub total.
	 * 
	 * @return the sub total
	 */
	public String getSubTotal() {
		return subTotal;
	}

	/**
	 * Sets the sub total.
	 * 
	 * @param subTotal
	 *            the new sub total
	 */
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	/**
	 * Gets the offline procurement.
	 * 
	 * @return the offline procurement
	 */
	public OfflineProcurement getOfflineProcurement() {
		return offlineProcurement;
	}

	/**
	 * Sets the offline procurement.
	 * 
	 * @param offlineProcurement
	 *            the new offline procurement
	 */
	public void setOfflineProcurement(OfflineProcurement offlineProcurement) {
		this.offlineProcurement = offlineProcurement;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getNumberOfFruitBags() {
		return numberOfFruitBags;
	}

	public void setNumberOfFruitBags(String numberOfFruitBags) {
		this.numberOfFruitBags = numberOfFruitBags;
	}

	
}
