package com.ese.entity.txn.mfi;

import java.util.Date;

public class InterestCalcHistory {
	private long id;
	private String farmerProfileId;
	private String farmerAccountRef;
	private double principalAmount;
	private double rateOfInterest;
	private double interestAmount;
	private Date calcDate;
	private int calcStatus;
	private String calcRemarks;
	private String trxnRefId;
	private double accumulatedIntAmount;
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

	public double getPrincipalAmount() {
		return principalAmount;
	}

	public void setPrincipalAmount(double principalAmount) {
		this.principalAmount = principalAmount;
	}

	public double getRateOfInterest() {
		return rateOfInterest;
	}

	public void setRateOfInterest(double rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}

	public double getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(double interestAmount) {
		this.interestAmount = interestAmount;
	}

	public Date getCalcDate() {
		return calcDate;
	}

	public void setCalcDate(Date calcDate) {
		this.calcDate = calcDate;
	}

	public int getCalcStatus() {
		return calcStatus;
	}

	public void setCalcStatus(int calcStatus) {
		this.calcStatus = calcStatus;
	}

	public String getCalcRemarks() {
		return calcRemarks;
	}

	public void setCalcRemarks(String calcRemarks) {
		this.calcRemarks = calcRemarks;
	}

	public double getAccumulatedIntAmount() {
		return accumulatedIntAmount;
	}

	public void setAccumulatedIntAmount(double accumulatedIntAmount) {
		this.accumulatedIntAmount = accumulatedIntAmount;
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

	public String getTrxnRefId() {
		return trxnRefId;
	}

	public void setTrxnRefId(String trxnRefId) {
		this.trxnRefId = trxnRefId;
	}

}
