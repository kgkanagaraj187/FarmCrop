/*
 * CropYield.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.agrocert;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Season;

public class CropYield {

    private long id;
    private Farm farm;
    private Season season;
    private String landHolding;
    private String latitude;
    private String longitude;
    private Date cropYieldDate;
    private Set<CropYieldDetail> cropYieldDetails;
    private String moleculeName;
	private byte[] image;
	private int status;
	private String statusMsg;
	private String type;
	private String branchId;
	private List<String> branchesList;
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
     * Gets the farm.
     * @return the farm
     */
    public Farm getFarm() {

        return farm;
    }

    /**
     * Sets the farm.
     * @param farm the new farm
     */
    public void setFarm(Farm farm) {

        this.farm = farm;
    }

    /**
     * Gets the season.
     * @return the season
     */
    public Season getSeason() {

        return season;
    }

    /**
     * Sets the season.
     * @param season the new season
     */
    public void setSeason(Season season) {

        this.season = season;
    }

    /**
     * Gets the land holding.
     * @return the land holding
     */
    public String getLandHolding() {

        return landHolding;
    }

    /**
     * Sets the land holding.
     * @param landHolding the new land holding
     */
    public void setLandHolding(String landHolding) {

        this.landHolding = landHolding;
    }

    /**
     * Gets the latitude.
     * @return the latitude
     */
    public String getLatitude() {

        return latitude;
    }

    /**
     * Sets the latitude.
     * @param latitude the new latitude
     */
    public void setLatitude(String latitude) {

        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     * @return the longitude
     */
    public String getLongitude() {

        return longitude;
    }

    /**
     * Sets the longitude.
     * @param longitude the new longitude
     */
    public void setLongitude(String longitude) {

        this.longitude = longitude;
    }

    /**
     * Gets the crop yield date.
     * @return the crop yield date
     */
    public Date getCropYieldDate() {

        return cropYieldDate;
    }

    /**
     * Sets the crop yield date.
     * @param cropYieldDate the new crop yield date
     */
    public void setCropYieldDate(Date cropYieldDate) {

        this.cropYieldDate = cropYieldDate;
    }

    /**
     * Gets the crop yield details.
     * @return the crop yield details
     */
    public Set<CropYieldDetail> getCropYieldDetails() {

        return cropYieldDetails;
    }

    /**
     * Sets the crop yield details.
     * @param cropYieldDetails the new crop yield details
     */
    public void setCropYieldDetails(Set<CropYieldDetail> cropYieldDetails) {

        this.cropYieldDetails = cropYieldDetails;
    }

	public String getMoleculeName() {
		return moleculeName;
	}

	public void setMoleculeName(String moleculeName) {
		this.moleculeName = moleculeName;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
