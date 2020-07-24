/*
 * OfflinesupplierProcurementDetail.java
 * Copyright (c) 2017-2018, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;

public class OfflineSupplierProcurementDetail implements Serializable {

	private static final long serialVersionUID = -5478727008052783904L;
	private long id;
	private String quality;
	private String productCode;
	private String numberOfBags;
	private String grossWeight;	
	private String netWeight;
	private String ratePerUnit;
	private String subTotal;
	private OfflineSupplierProcurement offlineSupplierProcurement;
	private String batchNo;
	private String unit;
	private String farmerId;
	private String farmerName;
	private String isRegFarmer;
	private String farmerMobileNumber;
	private String villageCode;
	private String cropType;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getNumberOfBags() {
		return numberOfBags;
	}
	public void setNumberOfBags(String numberOfBags) {
		this.numberOfBags = numberOfBags;
	}
	public String getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}
	public String getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
	}
	public String getRatePerUnit() {
		return ratePerUnit;
	}
	public void setRatePerUnit(String ratePerUnit) {
		this.ratePerUnit = ratePerUnit;
	}
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public OfflineSupplierProcurement getOfflineSupplierProcurement() {
		return offlineSupplierProcurement;
	}
	public void setOfflineSupplierProcurement(OfflineSupplierProcurement offlineSupplierProcurement) {
		this.offlineSupplierProcurement = offlineSupplierProcurement;
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
	public String getFarmerId() {
		return farmerId;
	}
	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	
	public String getIsRegFarmer() {
		return isRegFarmer;
	}
	public void setIsRegFarmer(String isRegFarmer) {
		this.isRegFarmer = isRegFarmer;
	}
	public String getFarmerMobileNumber() {
		return farmerMobileNumber;
	}
	public void setFarmerMobileNumber(String farmerMobileNumber) {
		this.farmerMobileNumber = farmerMobileNumber;
	}
	public String getVillageCode() {
		return villageCode;
	}
	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}
	public String getCropType() {
		return cropType;
	}
	public void setCropType(String cropType) {
		this.cropType = cropType;
	}
	public String getFarmerName() {
		return farmerName;
	}
	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	
	
	
}
