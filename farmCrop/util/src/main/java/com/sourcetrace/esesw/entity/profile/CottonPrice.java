package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

public class CottonPrice {

	private Long id;
	private String msp;
	private String seasonCode;
	private String branchId;
	private String stapleLength;
	private Date createdDate;
	private Date updatedDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getStapleLength() {
		return stapleLength;
	}

	public void setStapleLength(String stapleLength) {
		this.stapleLength = stapleLength;
	}

	public String getMsp() {
		return msp;
	}

	public void setMsp(String msp) {
		this.msp = msp;
	}

	

}
