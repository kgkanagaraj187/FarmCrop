/*
 * SupplierWarehouse.java
 * Copyright (c) 2017-2018, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;

public class SupplierWarehouse {

    public static final int DELETED = 1;
    public static final int NOT_DELETED = 0;

    private long id;
    private Municipality city;
    private ProcurementProduct procurementProduct;
    private long numberOfBags;
    private double grossWeight;
    private String agentId;
    private int isDelete;
    private String quality;
    private Set<SupplierWarehouseDetail> supplierWarehouseDetails;
    private Village village;
    private Warehouse coOperative;
    private long revisionNo;  
    // Transient Varaiable
    private String areaId;
    private String branchId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Municipality getCity() {
		return city;
	}
	public void setCity(Municipality city) {
		this.city = city;
	}
	public ProcurementProduct getProcurementProduct() {
		return procurementProduct;
	}
	public void setProcurementProduct(ProcurementProduct procurementProduct) {
		this.procurementProduct = procurementProduct;
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
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public Set<SupplierWarehouseDetail> getSupplierWarehouseDetails() {
		return supplierWarehouseDetails;
	}
	public void setSupplierWarehouseDetails(Set<SupplierWarehouseDetail> supplierWarehouseDetails) {
		this.supplierWarehouseDetails = supplierWarehouseDetails;
	}
	public Village getVillage() {
		return village;
	}
	public void setVillage(Village village) {
		this.village = village;
	}
	public Warehouse getCoOperative() {
		return coOperative;
	}
	public void setCoOperative(Warehouse coOperative) {
		this.coOperative = coOperative;
	}
	public long getRevisionNo() {
		return revisionNo;
	}
	public void setRevisionNo(long revisionNo) {
		this.revisionNo = revisionNo;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}


}
