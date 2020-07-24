package com.ese.entity.txn.mfi;

import java.util.Date;

public class InterestCalcConsolidated {

	private long id;
	private String farmerProfileId;
	private String farmerAccountRef;
	private double accumulatedPrincipalAmount;
	private double currentRateOfInterest;
	private double accumulatedIntAmount;
	private double accumulatedPrincipalAmount2;
	private double currentRateOfInterest2;
	private double accumulatedIntAmount2;
	private Date lastCalcDate;
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

	public String getFarmerProfileId() {
		return farmerProfileId;
	}

	public void setFarmerProfileId(String farmerProfileId) {
		this.farmerProfileId = farmerProfileId;
	}

	public String getFarmerAccountRef() {
		return farmerAccountRef;
	}

	public void setFarmerAccountRef(String farmerAccountRef) {
		this.farmerAccountRef = farmerAccountRef;
	}

	public double getAccumulatedPrincipalAmount() {
		return accumulatedPrincipalAmount;
	}

	public void setAccumulatedPrincipalAmount(double accumulatedPrincipalAmount) {
		this.accumulatedPrincipalAmount = accumulatedPrincipalAmount;
	}

	public double getCurrentRateOfInterest() {
		return currentRateOfInterest;
	}

	public void setCurrentRateOfInterest(double currentRateOfInterest) {
		this.currentRateOfInterest = currentRateOfInterest;
	}

	public double getAccumulatedIntAmount() {
		return accumulatedIntAmount;
	}

	public void setAccumulatedIntAmount(double accumulatedIntAmount) {
		this.accumulatedIntAmount = accumulatedIntAmount;
	}

	public Date getLastCalcDate() {
		return lastCalcDate;
	}

	public void setLastCalcDate(Date lastCalcDate) {
		this.lastCalcDate = lastCalcDate;
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

	public long getRevisionNo() {
		return revisionNo;
	}

	public void setRevisionNo(long revisionNo) {
		this.revisionNo = revisionNo;
	}

	public double getAccumulatedPrincipalAmount2() {
		return accumulatedPrincipalAmount2;
	}

	public void setAccumulatedPrincipalAmount2(double accumulatedPrincipalAmount2) {
		this.accumulatedPrincipalAmount2 = accumulatedPrincipalAmount2;
	}

	public double getCurrentRateOfInterest2() {
		return currentRateOfInterest2;
	}

	public void setCurrentRateOfInterest2(double currentRateOfInterest2) {
		this.currentRateOfInterest2 = currentRateOfInterest2;
	}

	public double getAccumulatedIntAmount2() {
		return accumulatedIntAmount2;
	}

	public void setAccumulatedIntAmount2(double accumulatedIntAmount2) {
		this.accumulatedIntAmount2 = accumulatedIntAmount2;
	}

}
