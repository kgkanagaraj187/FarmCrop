package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

public class CowVaccination
{
	private long id;
	private String name;
	private Date date;
	private long cowInspectionId;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getCowInspectionId() {
		return cowInspectionId;
	}
	public void setCowInspectionId(long cowInspectionId) {
		this.cowInspectionId = cowInspectionId;
	}

}
