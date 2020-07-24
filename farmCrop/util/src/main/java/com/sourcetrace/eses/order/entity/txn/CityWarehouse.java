/*
 * CityWarehouse.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
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

public class CityWarehouse {

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
    private String unit;
    private Set<CityWarehouseDetail> cityWarehouseDetails;

    private Village village;
    private Warehouse coOperative;
    private long revisionNo;   
    private Farmer farmer;
    private String batchNo;
    private String coldStorageName;
    // Transient Varaiable
    private String areaId;
    private String branchId;

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
     * Gets the city.
     * @return the city
     */
    public Municipality getCity() {

        return city;
    }

    /**
     * Sets the city.
     * @param city the new city
     */
    public void setCity(Municipality city) {

        this.city = city;
    }

    /**
     * Gets the procurement product.
     * @return the procurement product
     */
    public ProcurementProduct getProcurementProduct() {

        return procurementProduct;
    }

    /**
     * Sets the procurement product.
     * @param procurementProduct the new procurement product
     */
    public void setProcurementProduct(ProcurementProduct procurementProduct) {

        this.procurementProduct = procurementProduct;
    }

    /**
     * Gets the number of bags.
     * @return the number of bags
     */
    public long getNumberOfBags() {

        return numberOfBags;
    }

    /**
     * Sets the number of bags.
     * @param numberOfBags the new number of bags
     */
    public void setNumberOfBags(long numberOfBags) {

        this.numberOfBags = numberOfBags;
    }

    /**
     * Gets the gross weight.
     * @return the gross weight
     */
    public double getGrossWeight() {

        return grossWeight;
    }

    /**
     * Sets the gross weight.
     * @param grossWeight the new gross weight
     */
    public void setGrossWeight(double grossWeight) {

        this.grossWeight = grossWeight;
    }

    /**
     * Sets the agent id.
     * @param agentId the new agent id
     */
    public void setAgentId(String agentId) {

        this.agentId = agentId;
    }

    /**
     * Gets the agent id.
     * @return the agent id
     */
    public String getAgentId() {

        return agentId;
    }

    /**
     * Gets the checks if is delete.
     * @return the checks if is delete
     */
    public int getIsDelete() {

        return isDelete;
    }

    /**
     * Sets the checks if is delete.
     * @param isDelete the new checks if is delete
     */
    public void setIsDelete(int isDelete) {

        this.isDelete = isDelete;
    }

    /**
     * Gets the quality.
     * @return the quality
     */
    public String getQuality() {

        return quality;
    }

    /**
     * Sets the quality.
     * @param quality the new quality
     */
    public void setQuality(String quality) {

        this.quality = quality;
    }

    /**
     * Sets the area id.
     * @param areaId the new area id
     */
    public void setAreaId(String areaId) {

        this.areaId = areaId;
    }

    /**
     * Gets the area id.
     * @return the area id
     */
    public String getAreaId() {

        return areaId;
    }

    /**
     * Gets the city warehouse details.
     * @return the city warehouse details
     */
    public Set<CityWarehouseDetail> getCityWarehouseDetails() {

        return cityWarehouseDetails;
    }

    /**
     * Sets the city warehouse details.
     * @param cityWarehouseDetails the new city warehouse details
     */
    public void setCityWarehouseDetails(Set<CityWarehouseDetail> cityWarehouseDetails) {

        this.cityWarehouseDetails = cityWarehouseDetails;
    }

    /**
     * Gets the village.
     * @return the village
     */
    public Village getVillage() {

        return village;
    }

    /**
     * Sets the village.
     * @param village the new village
     */
    public void setVillage(Village village) {

        this.village = village;
    }

    /**
     * Gets the co operative.
     * @return the co operative
     */
    public Warehouse getCoOperative() {

        return coOperative;
    }

    /**
     * Sets the co operative.
     * @param coOperative the new co operative
     */
    public void setCoOperative(Warehouse coOperative) {

        this.coOperative = coOperative;
    }

    /**
     * Sets the revision no.
     * @param revisionNo the new revision no
     */
    public void setRevisionNo(long revisionNo) {

        this.revisionNo = revisionNo;
    }

    /**
     * Gets the revision no.
     * @return the revision no
     */
    public long getRevisionNo() {

        return revisionNo;
    }

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	
    public Farmer getFarmer() {
    
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
    
        this.farmer = farmer;
    }

    public String getBatchNo() {
    
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
    
        this.batchNo = batchNo;
    }

	public String getColdStorageName() {
		return coldStorageName;
	}

	public void setColdStorageName(String coldStorageName) {
		this.coldStorageName = coldStorageName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

   
    
    

}
