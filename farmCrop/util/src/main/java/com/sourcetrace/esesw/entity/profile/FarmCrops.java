/*
 * FarmCrops.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.esesw.entity.profile;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.util.ObjectUtil;

// TODO: Auto-generated Javadoc
public class FarmCrops {

	public static enum CROPTYPE {
		MAINCROP, INTERCROP, BORDERCROP ,COVERCROP,PLANTONBUND,TRAPCROP
	}

	public static final int MAX_LENGTH_NAME = 35;
	public static final String DECIMAL_PATTERN = "(?:\\d*\\.\\d+|\\d*)";
	public static final String THREE_DECIMAL_PATTERN = "(^\\d{0,12}(\\.\\d{1,3})?$)";
	private long id;
	private String stapleLength;
	private Double seedQtyUsed;
	private Double seedQtyCost;
	private Double estimatedYield;
	private HarvestSeason cropSeason;
	private String type;
	private Farm farm;
	private ProcurementVariety procurementVariety;
	private String unit;
	private String seedSource;
	private int cropCategory;
	private Date sowingDate;
	private Date estimatedHarvestDate;
	private String riskAssesment;
	private String riskBufferZoneDistanse;
	private String seedTreatmentDetails;
	private String otherSeedTreatmentDetails;
	private FarmCatalogue farmcatalogue;
	private String cropCategoryList;
	private String cultiArea;
	private Set<FarmCropsCoordinates> farmCropsCoordinates;
	private String latitude;
	private String longitude;
	private String otherType;
	private long revisionNo;
	private int status;
	private int cropEditStatus;
	private String gcParcelID;
	private String cropTimeline;
	
	//Canda
	private Double estimatedCotton;
	private String preSowingProd;
	private String preHarvestProd;
	private String postHarvestProd;
	private String seedCotton;
	private String lintCotton;
	private String actualSeedYield;
	private String totalCropHarv;
	private String grossIncome;
	private String interType;
	private String interAcre;
	
	//wilmar
	private String noOfTrees;
	private String prodTrees;
	private String affTrees;
	
	// Transient Variables
	private String stapleLengthPfx;
	private String stapleLengthSfx;
	private String cropCode;
	private String cropName;
	private String seedQtyCostPfx;
	private String seedQtyCostSfx;
	private String estYldPfx;
	private String estYldSfx;
	private String branchId;
	private String categoryFilter;
	private String seedQtyUsedSfx;
	private String seedQtyUsedPfx;
	private String villageCode;
	private String villageName;
	private String totalArea;
	private String totalProduction;
	private long procurementproductId;
	private long farmerId;
	private String farmerCode;
	private long cropId;
	private long season;
	private long samithiId;
	private String icsName;
	private Integer intervalDays;
	private List<Long> cropIdsList;
	private Date plotCapturingTime;
	 private Set<CoordinatesMap> coordinatesMap;
	 private  CoordinatesMap activeCoordinates;
	 
	 
	 //KenyaFPO
	 private String selectedCrop;
	

	/**
	 * Transient variable
	 */
	private List<String> branchesList;
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {

		this.id = id;
	}

	/**
	 * Gets the staple length.
	 * 
	 * @return the staple length
	 */
	public String getStapleLength() {

		return stapleLength;
	}

	/**
	 * Sets the staple length.
	 * 
	 * @param stapleLength
	 *            the new staple length
	 */
	public void setStapleLength(String stapleLength) {

		this.stapleLength = stapleLength;
	}

	/**
	 * Gets the seed qty used.
	 * 
	 * @return the seed qty used
	 */
	public Double getSeedQtyUsed() {

		return seedQtyUsed;
	}

	/**
	 * Sets the seed qty used.
	 * 
	 * @param seedQtyUsed
	 *            the new seed qty used
	 */
	public void setSeedQtyUsed(Double seedQtyUsed) {

		this.seedQtyUsed = seedQtyUsed;
	}

	/**
	 * Gets the seed qty cost.
	 * 
	 * @return the seed qty cost
	 */
	public Double getSeedQtyCost() {

		return seedQtyCost;
	}

	/**
	 * Sets the seed qty cost.
	 * 
	 * @param seedQtyCost
	 *            the new seed qty cost
	 */
	public void setSeedQtyCost(Double seedQtyCost) {

		this.seedQtyCost = seedQtyCost;
	}

	/**
	 * Gets the estimated yield.
	 * 
	 * @return the estimated yield
	 */
	public Double getEstimatedYield() {

		return estimatedYield;
	}

	/**
	 * Sets the estimated yield.
	 * 
	 * @param estimatedYield
	 *            the new estimated yield
	 */
	public void setEstimatedYield(Double estimatedYield) {

		this.estimatedYield = estimatedYield;
	}

	/**
	 * Gets the crop season.
	 * 
	 * @return the crop season
	 */
	public HarvestSeason getCropSeason() {

		return cropSeason;
	}

	/**
	 * Sets the crop season.
	 * 
	 * @param cropSeason
	 *            the new crop season
	 */
	public void setCropSeason(HarvestSeason cropSeason) {

		this.cropSeason = cropSeason;
	}

	/**
	 * Gets the farm.
	 * 
	 * @return the farm
	 */
	public Farm getFarm() {

		return farm;
	}

	/**
	 * Sets the farm.
	 * 
	 * @param farm
	 *            the new farm
	 */
	public void setFarm(Farm farm) {

		this.farm = farm;
	}

	/**
	 * Gets the procurement variety.
	 * 
	 * @return the procurement variety
	 */
	public ProcurementVariety getProcurementVariety() {

		return procurementVariety;
	}

	/**
	 * Sets the procurement variety.
	 * 
	 * @param procurementVariety
	 *            the new procurement variety
	 */
	public void setProcurementVariety(ProcurementVariety procurementVariety) {

		this.procurementVariety = procurementVariety;
	}

	public String getSeedSource() {

		return seedSource;
	}

	public void setSeedSource(String seedSource) {

		this.seedSource = seedSource;
	}

	/**
	 * Gets the crop category.
	 * 
	 * @return the crop category
	 */
	public int getCropCategory() {

		return cropCategory;
	}

	/**
	 * Sets the crop category.
	 * 
	 * @param cropCategory
	 *            the new crop category
	 */
	public void setCropCategory(int cropCategory) {

		this.cropCategory = cropCategory;
	}

	/**
	 * Gets the crop code.
	 * 
	 * @return the crop code
	 */
	public String getCropCode() {

		return cropCode;
	}

	/**
	 * Sets the crop code.
	 * 
	 * @param cropCode
	 *            the new crop code
	 */
	public void setCropCode(String cropCode) {

		this.cropCode = cropCode;
	}

	/**
	 * Gets the crop name.
	 * 
	 * @return the crop name
	 */
	public String getCropName() {

		return cropName;
	}

	/**
	 * Sets the crop name.
	 * 
	 * @param cropName
	 *            the new crop name
	 */
	public void setCropName(String cropName) {

		this.cropName = cropName;
	}

	/**
	 * Gets the seed qty cost pfx.
	 * 
	 * @return the seed qty cost pfx
	 */
	public String getSeedQtyCostPfx() {

		return seedQtyCostPfx;
	}

	/**
	 * Sets the seed qty cost pfx.
	 * 
	 * @param seedQtyCostPfx
	 *            the new seed qty cost pfx
	 */
	public void setSeedQtyCostPfx(String seedQtyCostPfx) {

		this.seedQtyCostPfx = seedQtyCostPfx;
	}

	/**
	 * Gets the seed qty cost sfx.
	 * 
	 * @return the seed qty cost sfx
	 */
	public String getSeedQtyCostSfx() {

		return seedQtyCostSfx;
	}

	/**
	 * Sets the seed qty cost sfx.
	 * 
	 * @param seedQtyCostSfx
	 *            the new seed qty cost sfx
	 */
	public void setSeedQtyCostSfx(String seedQtyCostSfx) {

		this.seedQtyCostSfx = seedQtyCostSfx;
	}

	/**
	 * Gets the est yld pfx.
	 * 
	 * @return the est yld pfx
	 */
	public String getEstYldPfx() {

		return estYldPfx;
	}

	/**
	 * Sets the est yld pfx.
	 * 
	 * @param estYldPfx
	 *            the new est yld pfx
	 */
	public void setEstYldPfx(String estYldPfx) {

		this.estYldPfx = estYldPfx;
	}

	/**
	 * Gets the est yld sfx.
	 * 
	 * @return the est yld sfx
	 */
	public String getEstYldSfx() {

		return estYldSfx;
	}

	/**
	 * Sets the est yld sfx.
	 * 
	 * @param estYldSfx
	 *            the new est yld sfx
	 */
	public void setEstYldSfx(String estYldSfx) {

		this.estYldSfx = estYldSfx;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {

		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {

		this.type = type;
	}

	public String getStapleLengthPfx() {
		return stapleLengthPfx;
	}

	public void setStapleLengthPfx(String stapleLengthPfx) {
		this.stapleLengthPfx = stapleLengthPfx;
	}

	public String getStapleLengthSfx() {
		return stapleLengthSfx;
	}

	public void setStapleLengthSfx(String stapleLengthSfx) {
		this.stapleLengthSfx = stapleLengthSfx;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public Date getSowingDate() {
		return sowingDate;
	}

	public void setSowingDate(Date sowingDate) {
		this.sowingDate = sowingDate;
	}

	public Date getEstimatedHarvestDate() {
		return estimatedHarvestDate;
	}

	public void setEstimatedHarvestDate(Date estimatedHarvestDate) {
		this.estimatedHarvestDate = estimatedHarvestDate;
	}

	public String getCategoryFilter() {
		return categoryFilter;
	}

	public void setCategoryFilter(String categoryFilter) {
		this.categoryFilter = categoryFilter;
	}

	public String getRiskBufferZoneDistanse() {

		return riskBufferZoneDistanse;
	}

	public void setRiskBufferZoneDistanse(String riskBufferZoneDistanse) {

		this.riskBufferZoneDistanse = riskBufferZoneDistanse;
	}

	public String getRiskAssesment() {

		return riskAssesment;
	}

	public void setRiskAssesment(String riskAssesment) {

		this.riskAssesment = riskAssesment;
	}

	public String getVillageCode() {
		return villageCode;
	}

	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getTotalArea() {
		return totalArea;
	}

	public void setTotalArea(String totalArea) {
		this.totalArea = totalArea;
	}

	public String getTotalProduction() {
		return totalProduction;
	}

	public void setTotalProduction(String totalProduction) {
		this.totalProduction = totalProduction;
	}

	public String getOtherSeedTreatmentDetails() {

		return otherSeedTreatmentDetails;
	}

	public void setOtherSeedTreatmentDetails(String otherSeedTreatmentDetails) {

		this.otherSeedTreatmentDetails = otherSeedTreatmentDetails;
	}

	public String getSeedTreatmentDetails() {

		return seedTreatmentDetails;
	}

	public void setSeedTreatmentDetails(String seedTreatmentDetails) {

		this.seedTreatmentDetails = seedTreatmentDetails;
	}

	public FarmCatalogue getFarmcatalogue() {

		return farmcatalogue;
	}

	public void setFarmcatalogue(FarmCatalogue farmcatalogue) {

		this.farmcatalogue = farmcatalogue;
	}

	public String getCropCategoryList() {
		return cropCategoryList;
	}

	public void setCropCategoryList(String cropCategoryList) {
		this.cropCategoryList = cropCategoryList;
	}

	public String getCultiArea() {
		return cultiArea;
	}

	public void setCultiArea(String cultiArea) {
		this.cultiArea = cultiArea;
	}

	public String getSeedQtyUsedSfx() {
		return seedQtyUsedSfx;
	}

	public void setSeedQtyUsedSfx(String seedQtyUsedSfx) {
		this.seedQtyUsedSfx = seedQtyUsedSfx;
	}

	public String getSeedQtyUsedPfx() {
		return seedQtyUsedPfx;
	}

	public void setSeedQtyUsedPfx(String seedQtyUsedPfx) {
		this.seedQtyUsedPfx = seedQtyUsedPfx;
	}

	public Set<FarmCropsCoordinates> getFarmCropsCoordinates() {
		return farmCropsCoordinates;
	}

	public void setFarmCropsCoordinates(Set<FarmCropsCoordinates> farmCropsCoordinates) {
		this.farmCropsCoordinates = farmCropsCoordinates;
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

	public String getOtherType() {

		return otherType;
	}

	public void setOtherType(String otherType) {

		this.otherType = otherType;
	}

	public long getProcurementproductId() {
		return procurementproductId;
	}

	public void setProcurementproductId(long procurementproductId) {
		this.procurementproductId = procurementproductId;
	}

	public long getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(long farmerId) {
		this.farmerId = farmerId;
	}

	public String getFarmerCode() {
		return farmerCode;
	}

	public void setFarmerCode(String farmerCode) {
		this.farmerCode = farmerCode;
	}

	public long getCropId() {
		return cropId;
	}

	public void setCropId(long cropId) {
		this.cropId = cropId;
	}

	public long getSeason() {
		return season;
	}

	public void setSeason(long season) {
		this.season = season;
	}

	public long getSamithiId() {
		return samithiId;
	}

	public void setSamithiId(long samithiId) {
		this.samithiId = samithiId;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

    public String getIcsName() {

        return icsName;
    }

    public void setIcsName(String icsName) {

        this.icsName = icsName;
    }
    
    public Integer getIntervalDays() {
		return intervalDays;
	}

	public void setIntervalDays(Integer intervalDays) {
		this.intervalDays = intervalDays;
	}

	public List<Long> getCropIdsList() {
		return cropIdsList;
	}

	public void setCropIdsList(List<Long> cropIdsList) {
		this.cropIdsList = cropIdsList;
	}

	public Double getEstimatedCotton() {
		return estimatedCotton;
	}

	public void setEstimatedCotton(Double estimatedCotton) {
		this.estimatedCotton = estimatedCotton;
	}

	public String getPreSowingProd() {
		return preSowingProd;
	}

	public void setPreSowingProd(String preSowingProd) {
		this.preSowingProd = preSowingProd;
	}

	public String getPreHarvestProd() {
		return preHarvestProd;
	}

	public void setPreHarvestProd(String preHarvestProd) {
		this.preHarvestProd = preHarvestProd;
	}

	public String getPostHarvestProd() {
		return postHarvestProd;
	}

	public void setPostHarvestProd(String postHarvestProd) {
		this.postHarvestProd = postHarvestProd;
	}

	public String getSeedCotton() {
		return seedCotton;
	}

	public void setSeedCotton(String seedCotton) {
		this.seedCotton = seedCotton;
	}

	public String getLintCotton() {
		return lintCotton;
	}

	public void setLintCotton(String lintCotton) {
		this.lintCotton = lintCotton;
	}

	public String getActualSeedYield() {
		return actualSeedYield;
	}

	public void setActualSeedYield(String actualSeedYield) {
		this.actualSeedYield = actualSeedYield;
	}

	public String getTotalCropHarv() {
		return totalCropHarv;
	}

	public void setTotalCropHarv(String totalCropHarv) {
		this.totalCropHarv = totalCropHarv;
	}

	public String getGrossIncome() {
		return grossIncome;
	}

	public void setGrossIncome(String grossIncome) {
		this.grossIncome = grossIncome;
	}

	public String getInterType() {
		return interType;
	}

	public void setInterType(String interType) {
		this.interType = interType;
	}

	public String getInterAcre() {
		return interAcre;
	}

	public void setInterAcre(String interAcre) {
		this.interAcre = interAcre;
	}

	public long getRevisionNo() {
		return revisionNo;
	}

	public void setRevisionNo(long revisionNo) {
		this.revisionNo = revisionNo;
	}

	public String getNoOfTrees() {
		return noOfTrees;
	}

	public void setNoOfTrees(String noOfTrees) {
		this.noOfTrees = noOfTrees;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getProdTrees() {
		return prodTrees;
	}

	public void setProdTrees(String prodTrees) {
		this.prodTrees = prodTrees;
	}

	public Date getPlotCapturingTime() {
		return plotCapturingTime;
	}

	public void setPlotCapturingTime(Date plotCapturingTime) {
		this.plotCapturingTime = plotCapturingTime;
	}

	public String getAffTrees() {
		return affTrees;
	}

	public void setAffTrees(String affTrees) {
		this.affTrees = affTrees;
	}

	public int getCropEditStatus() {
		return cropEditStatus;
	}

	public void setCropEditStatus(int cropEditStatus) {
		this.cropEditStatus = cropEditStatus;
	}

	public Set<CoordinatesMap> getCoordinatesMap() {
		return coordinatesMap;
	}

	public void setCoordinatesMap(Set<CoordinatesMap> coordinatesMap) {
		this.coordinatesMap = coordinatesMap;
	}

	public CoordinatesMap getActiveCoordinates() {
		CoordinatesMap coMap= this.coordinatesMap!=null && !ObjectUtil.isListEmpty(this.coordinatesMap) && this.coordinatesMap.size()>0?this.coordinatesMap.stream().filter(f-> f.getStatus()==1).findFirst().orElse(null):this.activeCoordinates;
		//return  !ObjectUtil.isListEmpty(this.coordinatesMap)  ? this.coordinatesMap.stream().filter(u -> u.getStatus()==1).findAny().get() : activeCoordinates;
		return coMap;
	}

	public void setActiveCoordinates(CoordinatesMap activeCoordinates) {
		this.activeCoordinates = activeCoordinates;
	}

	public String getGcParcelID() {
		return gcParcelID;
	}

	public void setGcParcelID(String gcParcelID) {
		this.gcParcelID = gcParcelID;
	}

	public String getCropTimeline() {
		return cropTimeline;
	}

	public void setCropTimeline(String cropTimeline) {
		this.cropTimeline = cropTimeline;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSelectedCrop() {
		return selectedCrop;
	}

	public void setSelectedCrop(String selectedCrop) {
		this.selectedCrop = selectedCrop;
	}
	
	
	
	
}
