/*
 * Procurement.java
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
import java.util.Map;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;

public class FarmCropCalendar implements Serializable {

	private static final long serialVersionUID = -691522973746677689L;

	public static final String TXN_TYPE = "398";
	
	private long id;
	private Farm farm;
	private ProcurementVariety variety;
	private String seasonCode;
	private String createdUser;
	private String updatedUser;
	private Date createdDate;
	private Date updatedDate;
	private String branchId;	
	private Set<FarmCropCalendarDetail> farmCropCalendarDetail;
	private String agentId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Farm getFarm() {
		return farm;
	}
	public void setFarm(Farm farm) {
		this.farm = farm;
	}
	public ProcurementVariety getVariety() {
		return variety;
	}
	public void setVariety(ProcurementVariety variety) {
		this.variety = variety;
	}
	
	public String getSeasonCode() {
		return seasonCode;
	}
	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	
	public Set<FarmCropCalendarDetail> getFarmCropCalendarDetail() {
		return farmCropCalendarDetail;
	}
	public void setFarmCropCalendarDetail(Set<FarmCropCalendarDetail> farmCropCalendarDetail) {
		this.farmCropCalendarDetail = farmCropCalendarDetail;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	


}
