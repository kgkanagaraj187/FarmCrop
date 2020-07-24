/*
 * Farm.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.util.StringUtil;

// TODO: Auto-generated Javadoc
public class CoordinatesMap {
    
	public static enum Status {
		INACTIVE, ACTIVE, DELETED
	}
	 private long id;
	 private Date date;
	 private String agentId;
	 private int status;
	 private Farm farm;
	 private FarmCrops farmCrops;
	 private String area; 
	 private String midLatitude;
	 private String midLongitude;
	 private Set<Coordinates> farmCoordinates;
	 private Set<FarmCropsCoordinates> farmCropsCoordinates; 
	 
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Farm getFarm() {
		return farm;
	}
	public void setFarm(Farm farm) {
		this.farm = farm;
	}
	public FarmCrops getFarmCrops() {
		return farmCrops;
	}
	public void setFarmCrops(FarmCrops farmCrops) {
		this.farmCrops = farmCrops;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getMidLatitude() {
		return midLatitude;
	}
	public void setMidLatitude(String midLatitude) {
		this.midLatitude = midLatitude;
	}
	public String getMidLongitude() {
		return midLongitude;
	}
	public void setMidLongitude(String midLongitude) {
		this.midLongitude = midLongitude;
	}
	public Set<Coordinates> getFarmCoordinates() {
		return farmCoordinates;
	}
	public void setFarmCoordinates(Set<Coordinates> farmCoordinates) {
		this.farmCoordinates = farmCoordinates;
	}
	public Set<FarmCropsCoordinates> getFarmCropsCoordinates() {
		return farmCropsCoordinates;
	}
	public void setFarmCropsCoordinates(Set<FarmCropsCoordinates> farmCropsCoordinates) {
		this.farmCropsCoordinates = farmCropsCoordinates;
	}
    
}
