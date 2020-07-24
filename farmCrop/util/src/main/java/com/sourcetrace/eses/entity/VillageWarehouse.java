/*
 * VillageWarehouse.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.entity;

import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.Village;

public class VillageWarehouse {

    public static final int DELETED = 1;
    public static final int NOT_DELETED = 0;

    private long id;
    private Village village;
    private ProcurementProduct procurementProduct;
    private long numberOfBags;
    private double grossWeight;
    private String agentId;
    private int isDelete;
    private String quality;
    
    //Transient Varaiable
    private String areaId;

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
     * Gets the quality code.
     * @return the quality code
     */
    public String getQuality() {

        return quality;
    }

    /**
     * Sets the quality code.
     * @param quality the new quality code
     */
    public void setQuality(String quality) {

        this.quality = quality;
    }

    public void setAreaId(String areaId) {

        this.areaId = areaId;
    }

    public String getAreaId() {

        return areaId;
    }

}
