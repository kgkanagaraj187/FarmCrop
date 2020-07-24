package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.Set;

public class EventReport {
	private long id;
	private String eventId;
	private String farmerId;
	private String status;
	private String output;
	private String remark;
	private Date captureTime;
	private String latitude;
	private String longitude;
	private String imageByteString;
	private String branchId;
	private byte[] photo;
	private String agentId;
	private String agentName;
	private Date createdDate;
	private String group;
	private String farmerName;
	private String cordinates;
	
	private String eventType;
	private Date startDate;
	private Date endDate;
	private String startTime;
	private String endTime;
	private String purpose;
	private String warehouseName;
	private String eventTypeCatalogueName;
	private String statusCatalogueName;
	private String agentPersInfoName;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getFarmerId() {
		return farmerId;
	}
	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCaptureTime() {
		return captureTime;
	}
	public void setCaptureTime(Date captureTime) {
		this.captureTime = captureTime;
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
	public String getImageByteString() {
		return imageByteString;
	}
	public void setImageByteString(String imageByteString) {
		this.imageByteString = imageByteString;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getFarmerName() {
		return farmerName;
	}
	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	public String getCordinates() {
		return cordinates;
	}
	public void setCordinates(String cordinates) {
		this.cordinates = cordinates;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getEventTypeCatalogueName() {
		return eventTypeCatalogueName;
	}
	public void setEventTypeCatalogueName(String eventTypeCatalogueName) {
		this.eventTypeCatalogueName = eventTypeCatalogueName;
	}
	public String getStatusCatalogueName() {
		return statusCatalogueName;
	}
	public void setStatusCatalogueName(String statusCatalogueName) {
		this.statusCatalogueName = statusCatalogueName;
	}
	public String getAgentPersInfoName() {
		return agentPersInfoName;
	}
	public void setAgentPersInfoName(String agentPersInfoName) {
		this.agentPersInfoName = agentPersInfoName;
	}

	
	
	}
