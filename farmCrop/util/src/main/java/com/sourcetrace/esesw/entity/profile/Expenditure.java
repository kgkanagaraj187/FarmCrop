package com.sourcetrace.esesw.entity.profile;

public class Expenditure {

	private long id;
	private String name;
	private String branchId;
	private long revisionNo;
	private double value;
	private Farm farm;
	
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
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public long getRevisionNo() {
		return revisionNo;
	}
	public void setRevisionNo(long revisionNo) {
		this.revisionNo = revisionNo;
	}
	public Farm getFarm() {
		return farm;
	}
	public void setFarm(Farm farm) {
		this.farm = farm;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	
}
