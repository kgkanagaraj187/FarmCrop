package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

public class GMO {
	private Long id;
	private Double noOfSamples;
	private Double noOfPositive;
	private Double contaminationPercentage;
	private String seasonCode;
	private String branchId;
	private Date createdDate;
	private String type;
	private Integer typeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getNoOfSamples() {
		return noOfSamples;
	}

	public void setNoOfSamples(Double noOfSamples) {
		this.noOfSamples = noOfSamples;
	}

	public Double getContaminationPercentage() {
		return contaminationPercentage;
	}

	public void setContaminationPercentage(Double contaminationPercentage) {
		this.contaminationPercentage = contaminationPercentage;
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
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

	public Double getNoOfPositive() {
		return noOfPositive;
	}

	public void setNoOfPositive(Double noOfPositive) {
		this.noOfPositive = noOfPositive;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	

}
