/*
 * SupplierProcurementDetail.java
 * Copyright (c) 2017-2018, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;

import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;

// TODO: Auto-generated Javadoc
public class SupplierProcurementDetail implements Serializable {

	private static final long serialVersionUID = -2291644640015331299L;

	public static enum qualityType {
		HIGH, MEDIUM, LOW
	}

	private long id;
	private ProcurementGrade procurementGrade;
	private long numberOfBags;
	private double grossWeight;	
	private double NetWeight;
	private double ratePerUnit;
	private double subTotal;
	private SupplierProcurement supplierProcurement;	
	private String quality;	
	private ProcurementProduct procurementProduct;
	private String batchNo;
	private String unit;
	private Farmer farmer;
	private String farmerName;
	private String isReg;
	private String farmerMobileNumber;
	private Village village;
	private String cropType;
	/* Transient Variable */
	private String tenantId;
	private String IndividulalFinalBal;
	private String item;
	private String villageCode;
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
	public long getNumberOfBags() {
		return numberOfBags;
	}
	public void setNumberOfBags(long numberOfBags) {
		this.numberOfBags = numberOfBags;
	}
	public double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public double getNetWeight() {
		return NetWeight;
	}
	public void setNetWeight(double netWeight) {
		NetWeight = netWeight;
	}
	public double getRatePerUnit() {
		return ratePerUnit;
	}
	public void setRatePerUnit(double ratePerUnit) {
		this.ratePerUnit = ratePerUnit;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public SupplierProcurement getSupplierProcurement() {
		return supplierProcurement;
	}
	public void setSupplierProcurement(SupplierProcurement supplierProcurement) {
		this.supplierProcurement = supplierProcurement;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public ProcurementProduct getProcurementProduct() {
		return procurementProduct;
	}
	public void setProcurementProduct(ProcurementProduct procurementProduct) {
		this.procurementProduct = procurementProduct;
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
	public Farmer getFarmer() {
		return farmer;
	}
	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}
	
	public String getIsReg() {
		return isReg;
	}
	public void setIsReg(String isReg) {
		this.isReg = isReg;
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
	public String getFarmerMobileNumber() {
		return farmerMobileNumber;
	}
	public void setFarmerMobileNumber(String farmerMobileNumber) {
		this.farmerMobileNumber = farmerMobileNumber;
	}
	public String getCropType() {
		return cropType;
	}
	public void setCropType(String cropType) {
		this.cropType = cropType;
	}
	public Village getVillage() {
		return village;
	}
	public void setVillage(Village village) {
		this.village = village;
	}
	public String getVillageCode() {
		return villageCode;
	}
	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}
	public String getFarmerName() {
		return farmerName;
	}
	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	
	
	
}
