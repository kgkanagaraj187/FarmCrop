package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

public class GlobalReport {
private long id;
private Date txnTime;
private String farmerId;
private String farmerName;
private double distributionAmount;
private double procurmentAmount;
private String seasonCode;



public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public Date getTxnTime() {
	return txnTime;
}
public void setTxnTime(Date txnTime) {
	this.txnTime = txnTime;
}
public String getFarmerId() {
	return farmerId;
}
public void setFarmerId(String farmerId) {
	this.farmerId = farmerId;
}
public String getFarmerName() {
	return farmerName;
}
public void setFarmerName(String farmerName) {
	this.farmerName = farmerName;
}
public double getDistributionAmount() {
	return distributionAmount;
}
public void setDistributionAmount(double distributionAmount) {
	this.distributionAmount = distributionAmount;
}
public double getProcurmentAmount() {
	return procurmentAmount;
}
public void setProcurmentAmount(double procurmentAmount) {
	this.procurmentAmount = procurmentAmount;
}
public String getSeasonCode() {
	return seasonCode;
}
public void setSeasonCode(String seasonCode) {
	this.seasonCode = seasonCode;
}

}
