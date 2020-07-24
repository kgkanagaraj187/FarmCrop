package com.sourcetrace.esesw.entity.profile;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class CropCalendar
{
  private long id;
  private Set<CropCalendarDetail> cropCalendarDetail;
  private String name;
  private ProcurementProduct procurementProduct;
  private String cropCode;
  private String varietyCode;
  private ProcurementVariety procurementVariety;
  private String seasonCode;
  private String createdUser;
  private Date createdDate;
  private String updatedUser;
  private Date updatedDate;
  private String branchId;
  private int activityType;
  private Date txnDate;
  private Map<String, String> filterData;
  private String revisionNo;
  
  
  
  
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}

public Set<CropCalendarDetail> getCropCalendarDetail() {
	return cropCalendarDetail;
}
public void setCropCalendarDetail(Set<CropCalendarDetail> cropCalendarDetail) {
	this.cropCalendarDetail = cropCalendarDetail;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public ProcurementProduct getProcurementProduct() {
	return procurementProduct;
}
public void setProcurementProduct(ProcurementProduct procurementProduct) {
	this.procurementProduct = procurementProduct;
}
public String getCropCode() {
	return cropCode;
}
public void setCropCode(String cropCode) {
	this.cropCode = cropCode;
}
public String getVarietyCode() {
	return varietyCode;
}
public void setVarietyCode(String varietyCode) {
	this.varietyCode = varietyCode;
}
public ProcurementVariety getProcurementVariety() {
	return procurementVariety;
}
public void setProcurementVariety(ProcurementVariety procurementVariety) {
	this.procurementVariety = procurementVariety;
}
public String getSeasonCode() {
	return seasonCode;
}
public void setSeasonCode(String seasonCode) {
	this.seasonCode = seasonCode;
}
public String getCreatedUser() {
	return createdUser;
}
public void setCreatedUser(String createdUser) {
	this.createdUser = createdUser;
}
public Date getCreatedDate() {
	return createdDate;
}
public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}
public String getUpdatedUser() {
	return updatedUser;
}
public void setUpdatedUser(String updatedUser) {
	this.updatedUser = updatedUser;
}
public Date getUpdatedDate() {
	return updatedDate;
}
public void setUpdatedDate(Date updatedDate) {
	this.updatedDate = updatedDate;
}
public String getBranchId() {
	return branchId;
}
public void setBranchId(String branchId) {
	this.branchId = branchId;
}
public int getActivityType() {
	return activityType;
}
public void setActivityType(int activityType) {
	this.activityType = activityType;
}
public Date getTxnDate() {
	return txnDate;
}
public void setTxnDate(Date txnDate) {
	this.txnDate = txnDate;
}
public Map<String, String> getFilterData() {
	return filterData;
}
public void setFilterData(Map<String, String> filterData) {
	this.filterData = filterData;
}
public String getRevisionNo() {
	return revisionNo;
}
public void setRevisionNo(String revisionNo) {
	this.revisionNo = revisionNo;
}
  
  
  
}