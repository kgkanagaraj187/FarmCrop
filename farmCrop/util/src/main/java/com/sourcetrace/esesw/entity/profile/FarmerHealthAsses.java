package com.sourcetrace.esesw.entity.profile;

import java.io.Serializable;

public class FarmerHealthAsses implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Farmer farmer;
	private String diabilityType;
	private String origin;
	private String remark;
	private String consultationStatus;
	private String consulatationDetail;
	private Long revsionNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Farmer getFarmer() {
		return farmer;
	}

	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	public String getDiabilityType() {
		return diabilityType;
	}

	public void setDiabilityType(String diabilityType) {
		this.diabilityType = diabilityType;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getConsultationStatus() {
		return consultationStatus;
	}

	public void setConsultationStatus(String consultationStatus) {
		this.consultationStatus = consultationStatus;
	}

	public String getConsulatationDetail() {
		return consulatationDetail;
	}

	public void setConsulatationDetail(String consulatationDetail) {
		this.consulatationDetail = consulatationDetail;
	}

	public Long getRevsionNo() {
		return revsionNo;
	}

	public void setRevsionNo(Long revsionNo) {
		this.revsionNo = revsionNo;
	}

}
