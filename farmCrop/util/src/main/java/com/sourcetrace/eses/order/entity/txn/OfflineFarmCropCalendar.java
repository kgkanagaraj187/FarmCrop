/*
 * OfflineProcurement.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.Customer;

public class OfflineFarmCropCalendar implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String farmerId;
	private String farmCode;
	private String varietyId;
	private String seasonCode;
	private String agentId;
	private String deviceId;
	private int statusCode;
	private String statusMsg;
	private Set<OfflineFarmCropCalendarDetail> offlineFarmCropCalendarDetail;	
	private String branchId;	
	private String createdUser;
	private String updatedUser;
	private String createdDate;
	private String updatedDate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFarmerId() {
		return farmerId;
	}
	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}	
	public String getFarmCode() {
		return farmCode;
	}
	public void setFarmCode(String farmCode) {
		this.farmCode = farmCode;
	}
	public String getVarietyId() {
		return varietyId;
	}
	public void setVarietyId(String varietyId) {
		this.varietyId = varietyId;
	}
	public String getSeasonCode() {
		return seasonCode;
	}
	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public Set<OfflineFarmCropCalendarDetail> getOfflineFarmCropCalendarDetail() {
		return offlineFarmCropCalendarDetail;
	}
	public void setOfflineFarmCropCalendarDetail(Set<OfflineFarmCropCalendarDetail> offlineFarmCropCalendarDetail) {
		this.offlineFarmCropCalendarDetail = offlineFarmCropCalendarDetail;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getUpdatedUser() {
		return updatedUser;
	}
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
	
	

}
