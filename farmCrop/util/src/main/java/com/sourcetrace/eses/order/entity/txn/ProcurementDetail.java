/*
 * ProcurementDetail.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;

import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

// TODO: Auto-generated Javadoc
public class ProcurementDetail implements Serializable {

	private static final long serialVersionUID = -2291644640015331299L;

	public static enum qualityType {
		HIGH, MEDIUM, LOW
	}

	private long id;
	private ProcurementGrade procurementGrade;
	private long numberOfBags;
	private String numberOfFruitBags;
	private double grossWeight;
	private double tareWeight;
	private double NetWeight;
	private double ratePerUnit;
	private double subTotal;
	private Procurement procurement;
	private Double dryLoss;
	private Double gradingLoss;
	private String quality;
	private double totalLoss;
	private ProcurementProduct procurementProduct;
	private String batchNo;
	private String unit;
	/* Transient Variable */
	private String tenantId;
	private String IndividulalFinalBal;
	private String item;
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
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
	 * Gets the procurement product.
	 * 
	 * @return the procurement product
	 */
	public ProcurementProduct getProcurementProduct() {

		return procurementProduct;
	}

	/**
	 * Sets the procurement product.
	 * 
	 * @param procurementProduct
	 *            the new procurement product
	 */
	public void setProcurementProduct(ProcurementProduct procurementProduct) {

		this.procurementProduct = procurementProduct;
	}

	/**
	 * Gets the number of bags.
	 * 
	 * @return the number of bags
	 */
	public long getNumberOfBags() {

		return numberOfBags;
	}

	/**
	 * Sets the number of bags.
	 * 
	 * @param numberOfBags
	 *            the new number of bags
	 */
	public void setNumberOfBags(long numberOfBags) {

		this.numberOfBags = numberOfBags;
	}

	/**
	 * Gets the gross weight.
	 * 
	 * @return the gross weight
	 */
	public double getGrossWeight() {

		return grossWeight;
	}

	/**
	 * Sets the gross weight.
	 * 
	 * @param grossWeight
	 *            the new gross weight
	 */
	public void setGrossWeight(double grossWeight) {

		this.grossWeight = grossWeight;
	}

	/**
	 * Gets the tare weight.
	 * 
	 * @return the tare weight
	 */
	public double getTareWeight() {

		return tareWeight;
	}

	/**
	 * Sets the tare weight.
	 * 
	 * @param tareWeight
	 *            the new tare weight
	 */
	public void setTareWeight(double tareWeight) {

		this.tareWeight = tareWeight;
	}

	/**
	 * Gets the net weight.
	 * 
	 * @return the net weight
	 */
	public double getNetWeight() {

		return NetWeight;
	}

	/**
	 * Sets the net weight.
	 * 
	 * @param netWeight
	 *            the new net weight
	 */
	public void setNetWeight(double netWeight) {

		NetWeight = netWeight;
	}

	/**
	 * Gets the rate per unit.
	 * 
	 * @return the rate per unit
	 */
	public double getRatePerUnit() {

		return ratePerUnit;
	}

	/**
	 * Sets the rate per unit.
	 * 
	 * @param ratePerUnit
	 *            the new rate per unit
	 */
	public void setRatePerUnit(double ratePerUnit) {

		this.ratePerUnit = ratePerUnit;
	}

	/**
	 * Sets the sub total.
	 * 
	 * @param subTotal
	 *            the new sub total
	 */
	public void setSubTotal(double subTotal) {

		this.subTotal = subTotal;
	}

	/**
	 * Gets the sub total.
	 * 
	 * @return the sub total
	 */
	public double getSubTotal() {

		return subTotal;
	}

	/**
	 * Gets the procurement.
	 * 
	 * @return the procurement
	 */
	public Procurement getProcurement() {

		return procurement;
	}

	/**
	 * Sets the procurement.
	 * 
	 * @param procurement
	 *            the new procurement
	 */
	public void setProcurement(Procurement procurement) {

		this.procurement = procurement;
	}

	/**
	 * Gets the procurement grade.
	 * 
	 * @return the procurement grade
	 */
	public ProcurementGrade getProcurementGrade() {

		return procurementGrade;
	}

	/**
	 * Sets the procurement grade.
	 * 
	 * @param procurementGrade
	 *            the new procurement grade
	 */
	public void setProcurementGrade(ProcurementGrade procurementGrade) {

		this.procurementGrade = procurementGrade;
	}	

	

    public Double getDryLoss() {
    
        return dryLoss;
    }

    public void setDryLoss(Double dryLoss) {
    
        this.dryLoss = dryLoss;
    }

    public Double getGradingLoss() {
    
        return gradingLoss;
    }

    public void setGradingLoss(Double gradingLoss) {
    
        this.gradingLoss = gradingLoss;
    }

    public double getTotalLoss() {

		return totalLoss;
	}

	public void setTotalLoss(double totalLoss) {

		this.totalLoss = totalLoss;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getIndividulalFinalBal() {
		return IndividulalFinalBal;
	}

	public void setIndividulalFinalBal(String individulalFinalBal) {
		IndividulalFinalBal = individulalFinalBal;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
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
