package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

import com.sourcetrace.esesw.entity.profile.Cow;

public class CostFarming 
{
	
	private long id;
	private String elitType;
	private String researchStationId;
	private String farmCode;
	private String farmerId;
	private Cow cow;
	private Date collectionDate;
	private String receiptNo;
	private double housingCost;
	private double feedCost;
	private double treatementCost;
	private double otherCost;
	private double totalExpence;
	private double incomeMilk;
	private double incomeOther;
	private double totalIncome;
	private Date createDate;
	private String branchId;
	private String createdUserName;
	private String lastUpdatedUserName;
	private String latitude;
	private String longitude;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getElitType() {
		return elitType;
	}
	public void setElitType(String elitType) {
		this.elitType = elitType;
	}
	public String getResearchStationId() {
		return researchStationId;
	}
	public void setResearchStationId(String researchStationId) {
		this.researchStationId = researchStationId;
	}
	public String getFarmCode() {
		return farmCode;
	}
	public void setFarmCode(String farmCode) {
		this.farmCode = farmCode;
	}
	public String getFarmerId() {
		return farmerId;
	}
	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	public Cow getCow() {
		return cow;
	}
	public void setCow(Cow cow) {
		this.cow = cow;
	}
	public Date getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public double getHousingCost() {
		return housingCost;
	}
	public void setHousingCost(double housingCost) {
		this.housingCost = housingCost;
	}
	public double getFeedCost() {
		return feedCost;
	}
	public void setFeedCost(double feedCost) {
		this.feedCost = feedCost;
	}
	public double getTreatementCost() {
		return treatementCost;
	}
	public void setTreatementCost(double treatementCost) {
		this.treatementCost = treatementCost;
	}
	public double getOtherCost() {
		return otherCost;
	}
	public void setOtherCost(double otherCost) {
		this.otherCost = otherCost;
	}
	public double getTotalExpence() {
		return totalExpence;
	}
	public void setTotalExpence(double totalExpence) {
		this.totalExpence = totalExpence;
	}
	public double getIncomeMilk() {
		return incomeMilk;
	}
	public void setIncomeMilk(double incomeMilk) {
		this.incomeMilk = incomeMilk;
	}
	public double getIncomeOther() {
		return incomeOther;
	}
	public void setIncomeOther(double incomeOther) {
		this.incomeOther = incomeOther;
	}
	public double getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getCreatedUserName() {
		return createdUserName;
	}
	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}
	public String getLastUpdatedUserName() {
		return lastUpdatedUserName;
	}
	public void setLastUpdatedUserName(String lastUpdatedUserName) {
		this.lastUpdatedUserName = lastUpdatedUserName;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
}
