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
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;

public class FarmCropCalendarDetail implements Serializable {

	private static final long serialVersionUID = -691522973746677689L;


	private long id;
	private String activityMethod;
	private Integer status;
	private String date;
	private String remarks;
	private FarmCropCalendar farmCropCalendar;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getActivityMethod() {
		return activityMethod;
	}
	public void setActivityMethod(String activityMethod) {
		this.activityMethod = activityMethod;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public FarmCropCalendar getFarmCropCalendar() {
		return farmCropCalendar;
	}
	public void setFarmCropCalendar(FarmCropCalendar farmCropCalendar) {
		this.farmCropCalendar = farmCropCalendar;
	}
	
	
	

}
