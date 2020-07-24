/*
 * OfflineProcurementDetail.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;

public class OfflineFarmCropCalendarDetail implements Serializable {

	private static final long serialVersionUID = -5478727008052783904L;
	private long id;
	private String activityMethod;
	private String status;
	private String date;
	private String remarks;
	private OfflineFarmCropCalendar offlineFarmCropCalendar;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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
	public OfflineFarmCropCalendar getOfflineFarmCropCalendar() {
		return offlineFarmCropCalendar;
	}
	public void setOfflineFarmCropCalendar(OfflineFarmCropCalendar offlineFarmCropCalendar) {
		this.offlineFarmCropCalendar = offlineFarmCropCalendar;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}
