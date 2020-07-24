/*
 * OfflineFarmCropsEnrollment.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

public class OfflineFarmCropsEnrollment {

    private long id;
    private String cropCode;
    private String cropArea;
    private String productionPerYear;
    private String farmCode;
    private OfflineFarmerEnrollment farmer;

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
     * Gets the crop code.
     * @return the crop code
     */
    public String getCropCode() {

        return cropCode;
    }

    /**
     * Sets the crop code.
     * @param cropCode the new crop code
     */
    public void setCropCode(String cropCode) {

        this.cropCode = cropCode;
    }

    /**
     * Gets the crop area.
     * @return the crop area
     */
    public String getCropArea() {

        return cropArea;
    }

    /**
     * Sets the crop area.
     * @param cropArea the new crop area
     */
    public void setCropArea(String cropArea) {

        this.cropArea = cropArea;
    }

    /**
     * Gets the production per year.
     * @return the production per year
     */
    public String getProductionPerYear() {

        return productionPerYear;
    }

    /**
     * Sets the production per year.
     * @param productionPerYear the new production per year
     */
    public void setProductionPerYear(String productionPerYear) {

        this.productionPerYear = productionPerYear;
    }

    /**
     * Gets the farm code.
     * @return the farm code
     */
    public String getFarmCode() {

        return farmCode;
    }

    /**
     * Sets the farm code.
     * @param farmCode the new farm code
     */
    public void setFarmCode(String farmCode) {

        this.farmCode = farmCode;
    }

    /**
     * Gets the farmer.
     * @return the farmer
     */
    public OfflineFarmerEnrollment getFarmer() {

        return farmer;
    }

    /**
     * Sets the farmer.
     * @param farmer the new farmer
     */
    public void setFarmer(OfflineFarmerEnrollment farmer) {

        this.farmer = farmer;
    }

}
