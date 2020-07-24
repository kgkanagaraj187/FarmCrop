package com.ese.entity.txn.mfi;

import java.util.Date;

public class InterestRateHistory {
	private long id;
	private double rateOfInterest;
	private Date roiEffFrom;
	private int affectExistingFarmerBal;
	private int isActive;
	private Date createDt;
	private String createUserName;
	private Date lastUpdateDt;
	private String updateUserName;
	private long revisionNo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getRateOfInterest() {
		return rateOfInterest;
	}

	public void setRateOfInterest(double rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}

	public Date getRoiEffFrom() {
		return roiEffFrom;
	}

	public void setRoiEffFrom(Date roiEffFrom) {
		this.roiEffFrom = roiEffFrom;
	}

	public int getAffectExistingFarmerBal() {
		return affectExistingFarmerBal;
	}

	public void setAffectExistingFarmerBal(int affectExistingFarmerBal) {
		this.affectExistingFarmerBal = affectExistingFarmerBal;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public long getRevisionNo() {
		return revisionNo;
	}

	public void setRevisionNo(long revisionNo) {
		this.revisionNo = revisionNo;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getLastUpdateDt() {
		return lastUpdateDt;
	}

	public void setLastUpdateDt(Date lastUpdateDt) {
		this.lastUpdateDt = lastUpdateDt;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

}
