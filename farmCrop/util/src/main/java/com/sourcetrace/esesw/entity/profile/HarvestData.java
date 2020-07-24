/*
 * HarvestData.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

// TODO: Auto-generated Javadoc

public class HarvestData {

    public static final String HARVESTED_PATTERN = "(?:\\d*\\d+|\\d*)";
    public static final int MAX_LENGTH_NAME = 35;

    private long id;
    private FarmCrops farmCrops;
    private String harvested;// harvested quantity
    private Date harvestedDate;
    private String buyerName;
    private String harvestedAmount;
    private byte[] harvestDataImage;

    // transient variables
    private String harvestQtyNumber;
    private String harvestQtyDecimal;
    private String harvestAmountNumber;
    private String harvestAmountDecimal;
    private HarvestSeason harvestSeason;

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
     * Gets the farm crops.
     * @return the farm crops
     */
    public FarmCrops getFarmCrops() {

        return farmCrops;
    }

    /**
     * Sets the farm crops.
     * @param farmCrops the new farm crops
     */
    public void setFarmCrops(FarmCrops farmCrops) {

        this.farmCrops = farmCrops;
    }

    /**
     * Gets the harvested date.
     * @return the harvested date
     */
    public Date getHarvestedDate() {

        return harvestedDate;
    }

    /**
     * Sets the harvested date.
     * @param harvestedDate the new harvested date
     */
    public void setHarvestedDate(Date harvestedDate) {

        this.harvestedDate = harvestedDate;
    }

    /**
     * Sets the harvested.
     * @param harvested the new harvested
     */
    public void setHarvested(String harvested) {

        this.harvested = harvested;
    }

    /**
     * Gets the harvested.
     * @return the harvested
     */
    public String getHarvested() {

        return harvested;
    }

    /**
     * Gets the buyer name.
     * @return the buyer name
     */
    public String getBuyerName() {

        return buyerName;
    }

    /**
     * Sets the buyer name.
     * @param buyerName the new buyer name
     */
    public void setBuyerName(String buyerName) {

        this.buyerName = buyerName;
    }

    /**
     * Gets the harvested amount.
     * @return the harvested amount
     */
    public String getHarvestedAmount() {

        return harvestedAmount;
    }

    /**
     * Sets the harvested amount.
     * @param harvestedAmount the new harvested amount
     */

    public void setHarvestedAmount(String harvestedAmount) {

        this.harvestedAmount = harvestedAmount;
    }

    /**
     * Gets the harvest data image.
     * @return the harvest data image
     */
    public byte[] getHarvestDataImage() {

        return harvestDataImage;
    }

    /**
     * Sets the harvest data image.
     * @param harvestDataImage the new harvest data image
     */
    public void setHarvestDataImage(byte[] harvestDataImage) {

        this.harvestDataImage = harvestDataImage;
    }

    /**
     * Gets the harvest qty number.
     * @return the harvest qty number
     */
    public String getHarvestQtyNumber() {

        return harvestQtyNumber;
    }

    /**
     * Sets the harvest qty number.
     * @param harvestQtyNumber the new harvest qty number
     */
    public void setHarvestQtyNumber(String harvestQtyNumber) {

        this.harvestQtyNumber = harvestQtyNumber;
    }

    /**
     * Gets the harvest qty decimal.
     * @return the harvest qty decimal
     */
    public String getHarvestQtyDecimal() {

        return harvestQtyDecimal;
    }

    /**
     * Sets the harvest qty decimal.
     * @param harvestQtyDecimal the new harvest qty decimal
     */
    public void setHarvestQtyDecimal(String harvestQtyDecimal) {

        this.harvestQtyDecimal = harvestQtyDecimal;
    }

    /**
     * Gets the harvest amount number.
     * @return the harvest amount number
     */
    public String getHarvestAmountNumber() {

        return harvestAmountNumber;
    }

    /**
     * Sets the harvest amount number.
     * @param harvestAmountNumber the new harvest amount number
     */
    public void setHarvestAmountNumber(String harvestAmountNumber) {

        this.harvestAmountNumber = harvestAmountNumber;
    }

    /**
     * Gets the harvest amount decimal.
     * @return the harvest amount decimal
     */
    public String getHarvestAmountDecimal() {

        return harvestAmountDecimal;
    }

    /**
     * Sets the harvest amount decimal.
     * @param harvestAmountDecimal the new harvest amount decimal
     */
    public void setHarvestAmountDecimal(String harvestAmountDecimal) {

        this.harvestAmountDecimal = harvestAmountDecimal;
    }

	public HarvestSeason getHarvestSeason() {
		return harvestSeason;
	}

	public void setHarvestSeason(HarvestSeason harvestSeason) {
		this.harvestSeason = harvestSeason;
	}

}
