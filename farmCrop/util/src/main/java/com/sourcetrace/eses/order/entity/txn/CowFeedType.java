package com.sourcetrace.eses.order.entity.txn;

public class CowFeedType
{
	private long id;
	private String feedType;
	private double amount;
	private long cowInspectionId;
	public long getId() {
		return id;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCowInspectionId() {
		return cowInspectionId;
	}
	public void setCowInspectionId(long cowInspectionId) {
		this.cowInspectionId = cowInspectionId;
	}

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

}
