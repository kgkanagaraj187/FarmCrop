package com.sourcetrace.eses.entity;


public class FarmCatalogueMaster {
	
	
	public static final Integer STATUS_SURVEY=3;
	
	private long id;
	private String name;
	private int typez;
	private long revisionNo;
	private String branchId;
	private int status;
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
	public int getTypez() {
		return typez;
	}
	public void setTypez(int typez) {
		this.typez = typez;
	}
	public long getRevisionNo() {
		return revisionNo;
	}
	public void setRevisionNo(long revisionNo) {
		this.revisionNo = revisionNo;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
