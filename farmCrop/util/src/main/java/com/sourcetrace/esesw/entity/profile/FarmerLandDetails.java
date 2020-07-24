package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

public class FarmerLandDetails
{
	private Long id;
	private Double irrigatedTotLand;
	private String irrigatedIfsPractices;
	private Double rainfedTotLand;
	private String ranifedIfsPractices;
	private String seasonCode;
	private Farm farmId;
	private String year;
	private String crops;
	private String seedlings;
	private Double estimatedAcreage;
	private String noOfTrees;
	private String pestdiseases;
	private String pestdiseasesControl;
	private String fertilizationMethod;
	private String inputs;
	private String withBuffer;
	private String withoutBuffer;
	private Date date;
	private String branchId;
	
	//transient variables
		
	private String irrigatedLand;
	private String irrigatedFarmingLand;
	private String fedtotaland;
	private String fedtotalics;
	private String tempSeasonCode;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSeasonCode() {
		return seasonCode;
	}
	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode;
	}

	public Double getRainfedTotLand() {
		return rainfedTotLand;
	}
	public void setRainfedTotLand(Double rainfedTotLand) {
		this.rainfedTotLand = rainfedTotLand;
	}
	public Double getIrrigatedTotLand() {
		return irrigatedTotLand;
	}
	public void setIrrigatedTotLand(Double irrigatedTotLand) {
		this.irrigatedTotLand = irrigatedTotLand;
	}
	public String getIrrigatedIfsPractices() {
		return irrigatedIfsPractices;
	}
	public void setIrrigatedIfsPractices(String irrigatedIfsPractices) {
		this.irrigatedIfsPractices = irrigatedIfsPractices;
	}
	public String getRanifedIfsPractices() {
		return ranifedIfsPractices;
	}
	public void setRanifedIfsPractices(String ranifedIfsPractices) {
		this.ranifedIfsPractices = ranifedIfsPractices;
	}

	
	public String getIrrigatedFarmingLand() {
		return irrigatedFarmingLand;
	}
	public void setIrrigatedFarmingLand(String irrigatedFarmingLand) {
		this.irrigatedFarmingLand = irrigatedFarmingLand;
	}
	
	public String getFedtotalics() {
		return fedtotalics;
	}
	public void setFedtotalics(String fedtotalics) {
		this.fedtotalics = fedtotalics;
	}
	public Farm getFarmId() {
		return farmId;
	}
	public void setFarmId(Farm farmId) {
		this.farmId = farmId;
	}
	public String getTempSeasonCode() {
		return tempSeasonCode;
	}
	public void setTempSeasonCode(String tempSeasonCode) {
		this.tempSeasonCode = tempSeasonCode;
	}
	public String getIrrigatedLand() {
		return irrigatedLand;
	}
	public void setIrrigatedLand(String irrigatedLand) {
		this.irrigatedLand = irrigatedLand;
	}
	public String getFedtotaland() {
		return fedtotaland;
	}
	public void setFedtotaland(String fedtotaland) {
		this.fedtotaland = fedtotaland;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCrops() {
		return crops;
	}
	public void setCrops(String crops) {
		this.crops = crops;
	}
	public String getSeedlings() {
		return seedlings;
	}
	public void setSeedlings(String seedlings) {
		this.seedlings = seedlings;
	}
	public Double getEstimatedAcreage() {
		return estimatedAcreage;
	}
	public void setEstimatedAcreage(Double estimatedAcreage) {
		this.estimatedAcreage = estimatedAcreage;
	}
	public String getNoOfTrees() {
		return noOfTrees;
	}
	public void setNoOfTrees(String noOfTrees) {
		this.noOfTrees = noOfTrees;
	}
	public String getPestdiseases() {
		return pestdiseases;
	}
	public void setPestdiseases(String pestdiseases) {
		this.pestdiseases = pestdiseases;
	}
	public String getPestdiseasesControl() {
		return pestdiseasesControl;
	}
	public void setPestdiseasesControl(String pestdiseasesControl) {
		this.pestdiseasesControl = pestdiseasesControl;
	}
	public String getFertilizationMethod() {
		return fertilizationMethod;
	}
	public void setFertilizationMethod(String fertilizationMethod) {
		this.fertilizationMethod = fertilizationMethod;
	}
	public String getInputs() {
		return inputs;
	}
	public void setInputs(String inputs) {
		this.inputs = inputs;
	}
	public String getWithBuffer() {
		return withBuffer;
	}
	public void setWithBuffer(String withBuffer) {
		this.withBuffer = withBuffer;
	}
	public String getWithoutBuffer() {
		return withoutBuffer;
	}
	public void setWithoutBuffer(String withoutBuffer) {
		this.withoutBuffer = withoutBuffer;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	
    


}
