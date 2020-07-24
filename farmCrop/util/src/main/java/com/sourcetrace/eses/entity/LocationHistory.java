/*
 * LocationHistory.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.Farm;

public class LocationHistory {
	private long id;
	private Date txnTime;
	private String serialNumber;
	private String agentId;
	private Date createdTime;
	private String branchId;
	private Date startTime;
	private Date endTime;
	private Set<LocationHistoryDetail> locationHistoryDetails;
	private String agentName;
	private List<String> branchesList;
	private String longitude;
	private String latitude;
	private String endlatitude;
	private String endlongitude;
	private String startAddress;
	private String endAddress;

	// Read Only Properties
	private Profile profile;
	private Device device;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(Date txnTime) {
		this.txnTime = txnTime;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public List<String> getBranchesList() {
		return branchesList;
	}
	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public Set<LocationHistoryDetail> getLocationHistoryDetails() {
		return locationHistoryDetails;
	}
	public void setLocationHistoryDetails(Set<LocationHistoryDetail> locationHistoryDetails) {
		this.locationHistoryDetails = locationHistoryDetails;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getEndlatitude() {
		return endlatitude;
	}
	public void setEndlatitude(String endlatitude) {
		this.endlatitude = endlatitude;
	}
	public String getEndlongitude() {
		return endlongitude;
	}
	public void setEndlongitude(String endlongitude) {
		this.endlongitude = endlongitude;
	}
	public String getStartAddress() {
		return startAddress;
	}
	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}
	public String getEndAddress() {
		return endAddress;
	}
	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}

}
