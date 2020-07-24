/*
 * Coordinates.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.ArrayList;
import java.util.List;

public class Coordinates implements Comparable<Coordinates> {

    private long id;
    private Farm farm;
    private String latitude;
    private String longitude;
    private long orderNo;
    private String farmId;
    private CoordinatesMap coordinatesMap;

     private String title;
	 private String description;
	 private int type;
	 private String lat;
	 private String lng;
	 private List<FarmCropsCoordinates> nearByLandDetails = new ArrayList<FarmCropsCoordinates>();
    
    public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}
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
     * Sets the farm.
     * @param farm the new farm
     */
    public void setFarm(Farm farm) {

        this.farm = farm;
    }

    /**
     * Gets the farm.
     * @return the farm
     */
    public Farm getFarm() {

        return farm;
    }

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public int compareTo(Coordinates coord) {
		
		long compareQuantity = ((Coordinates) coord).getOrderNo(); 
		
		//ascending order
		return (int) (this.orderNo - compareQuantity);
		
	}

	public CoordinatesMap getCoordinatesMap() {
		return coordinatesMap;
	}

	public void setCoordinatesMap(CoordinatesMap coordinatesMap) {
		this.coordinatesMap = coordinatesMap;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public List<FarmCropsCoordinates> getNearByLandDetails() {
		return nearByLandDetails;
	}

	public void setNearByLandDetails(List<FarmCropsCoordinates> nearByLandDetails) {
		this.nearByLandDetails = nearByLandDetails;
	}
	

	
}
