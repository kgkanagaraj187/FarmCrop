package com.sourcetrace.eses.entity;

import java.util.Date;

import com.sourcetrace.esesw.entity.profile.Season;

public class SamithiIcs {
	 public enum ICSTypes {
	        ICS_1, ICS_2, ICS_3, ORGANIC
	    }
	private long id;
	private Warehouse warehouse;
	private String season;
	private String landHolding;
	private String type;
	private Date date;
	private byte[] image;
	private int status;
	private String branchId;
	private String icsType;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getLandHolding() {
		return landHolding;
	}
	public void setLandHolding(String landHolding) {
		this.landHolding = landHolding;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIcsType() {
		return icsType;
	}
	public void setIcsType(String icsType) {
		this.icsType = icsType;
	}
	
	
}
