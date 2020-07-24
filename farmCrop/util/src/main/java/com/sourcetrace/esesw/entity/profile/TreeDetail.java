package com.sourcetrace.esesw.entity.profile;

public class TreeDetail {

	private long id;
	private Farm farm;
	private String years;
    private String noOfTrees;
	private String variety;
	private String prodStatus;
	
	/*
	 * trans
	 */
	private String yearsId;
	private String varietyId;
	private String prodStatusId;
	
	
	
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public String getNoOfTrees() {
		return noOfTrees;
	}
	public void setNoOfTrees(String noOfTrees) {
		this.noOfTrees = noOfTrees;
	}
	public String getVariety() {
		return variety;
	}
	public void setVariety(String variety) {
		this.variety = variety;
	}

	
	public String getProdStatus() {
		return prodStatus;
	}

	public void setProdStatus(String prodStatus) {
		this.prodStatus = prodStatus;
	}

	public Farm getFarm() {
		return farm;
	}

	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	public String getYearsId() {
		return yearsId;
	}

	public void setYearsId(String yearsId) {
		this.yearsId = yearsId;
	}

	public String getVarietyId() {
		return varietyId;
	}

	public void setVarietyId(String varietyId) {
		this.varietyId = varietyId;
	}

	public String getProdStatusId() {
		return prodStatusId;
	}

	public void setProdStatusId(String prodStatusId) {
		this.prodStatusId = prodStatusId;
	}
	
	
	
	
}
