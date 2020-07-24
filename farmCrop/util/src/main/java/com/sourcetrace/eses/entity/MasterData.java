package com.sourcetrace.eses.entity;

import org.hibernate.validator.constraints.NotEmpty;

public class MasterData 
{
	private Long id;
	private String code;
	private String masterType;
	private String name;
	
	private String contactPersonName;
	private String mobileNo;
	private String landlineNo;
	private String emailAddress;
	private String address;
	private long revisionNo;
	
	protected static final String MANDI_TRADER = "MANDI TRADER"; 
	protected static final String MANDI_AGGREGATOR = "MANDI AGGREGATOR"; 
	protected static final String FARM_AGGREGATOR = "FARM AGGREGATOR";
	protected static final String PRODUCE_IMPORTER = "PRODUCE IMPORTER";
	
	public static final String CODE_PREFIX="M";
	
	public static enum masterTypes {
		MANDI_TRADER, MANDI_AGGREGATOR, FARM_AGGREGATOR, FPO, CIG, FIG, PRODUCE_IMPORTER,FPC,IMPORTER,AGRICULTURE_COMPANY,FARMER_GROUP_FG,CUSTOMER,CASH_PURCHASE
	};
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@NotEmpty(message = "empty.masterType")
	public String getMasterType() {
		return masterType;
	}
	public void setMasterType(String masterType) {
		this.masterType = masterType;
	}
	
	@NotEmpty(message = "empty.name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactPersonName() {
		return contactPersonName;
	}
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getLandlineNo() {
		return landlineNo;
	}
	public void setLandlineNo(String landlineNo) {
		this.landlineNo = landlineNo;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getRevisionNo() {
		return revisionNo;
	}
	public void setRevisionNo(long revisionNo) {
		this.revisionNo = revisionNo;
	}
}
