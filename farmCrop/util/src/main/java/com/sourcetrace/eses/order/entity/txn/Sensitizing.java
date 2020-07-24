package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.Set;

public class Sensitizing {
	private Long id;
	private String groupId;
	private String farmerCount;
	private String remarks;
	private String latitude;
	private String longitude;
	private Long revisionNo;
	private String status;
	private Date createdDt;
	private String branchId;
	private String groupName;
	private String village;

	private Set<SensitizingImg> sentizingImages;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getFarmerCount() {
		return farmerCount;
	}

	public void setFarmerCount(String farmerCount) {
		this.farmerCount = farmerCount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Long getRevisionNo() {
		return revisionNo;
	}

	public void setRevisionNo(Long revisionNo) {
		this.revisionNo = revisionNo;
	}

	public Set<SensitizingImg> getSentizingImages() {
		return sentizingImages;
	}

	public void setSentizingImages(Set<SensitizingImg> sentizingImages) {
		this.sentizingImages = sentizingImages;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	
}
