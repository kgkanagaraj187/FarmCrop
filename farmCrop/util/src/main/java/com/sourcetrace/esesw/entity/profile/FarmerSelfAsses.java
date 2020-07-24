package com.sourcetrace.esesw.entity.profile;

import java.io.Serializable;

public class FarmerSelfAsses implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Farmer farmer;
	private String activity;
	private String value;
	private String remark;
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

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getRevsionNo() {
		return revsionNo;
	}

	public void setRevsionNo(Long revsionNo) {
		this.revsionNo = revsionNo;
	}

}
