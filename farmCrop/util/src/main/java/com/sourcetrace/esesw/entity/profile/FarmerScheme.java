package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

public class FarmerScheme {
	private Long id;
	private Long farmId;
	private String benefitDetails;
	private Integer noOfKgs;
	private String departmentName;
	private Date receivedDate;
	private Double receivedAmt;
	private Double contributionAmt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBenefitDetails() {
		return benefitDetails;
	}
	public void setBenefitDetails(String benefitDetails) {
		this.benefitDetails = benefitDetails;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public Double getReceivedAmt() {
		return receivedAmt;
	}
	public void setReceivedAmt(Double receivedAmt) {
		this.receivedAmt = receivedAmt;
	}
	public Double getContributionAmt() {
		return contributionAmt;
	}
	public void setContributionAmt(Double contributionAmt) {
		this.contributionAmt = contributionAmt;
	}
	
	public Integer getNoOfKgs() {
		return noOfKgs;
	}
	public void setNoOfKgs(Integer noOfKgs) {
		this.noOfKgs = noOfKgs;
	}
	public Long getFarmId() {
		return farmId;
	}
	public void setFarmId(Long farmId) {
		this.farmId = farmId;
	}
	

}
