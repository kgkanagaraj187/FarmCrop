/*
 * FarmDetailedInfo.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

public class FarmDetailedInfo {
    public static final String HECTARES = "HEC";
    public static final String ACRES = "ACR";
    public static final String THREE_DECIMAL_PATTERN = "(^\\d{0,12}(\\.\\d{1,3})?$)";
    public static final char BOREWELL_RECHARGEY='Y';
    public static final char BOREWELL_RECHARGEN='N';

    private long id;
    private boolean sameAddressofFarmer;
    private String farmAddress;
    private String landMeasurement;
    private String farmArea;
    private String farmProductiveArea;
    private String farmConservationArea;
    private String waterBodiesCount;
    private String fullTimeWorkersCount;
    private String partTimeWorkersCount;
    private String seasonalWorkersCount;
    private String farmOwned;
    private String areaUnderIrrigation;
    private String farmIrrigation;
    private String irrigationSource;
    private int irrigationMethod;
    private String soilType;
    private int soilFertility;
    private int boreWellRechargeStructure;
    private String methodOfIrrigation;
    private int milletCultivated;
    private String milletCropType;
    private int milletCropCount;
    private int farmerFieldSchool;
    private String isFFSBenifited;

    private String lastDateOfChemicalApplication;
    private String beginOfConversion;
    private Date internalInspectionDate;
    private String internalInspectorName;
    private String surveyNumber;
    private int landUnderICSStatus;
    private String fallowOrPastureLand;
    private String conventionalLand;
    private String conventionalCrops;
    private String conventionalEstimatedYield;
    private String nc;

    private String totalLandHolding;
    private String proposedPlantingArea;
    private String landGradient;
    private String approachRoad;
    private String regYear;
    private String irrigatedOther;
    private String soilTexture;
    private String rainFedValue;
    private String sessionYear;

    //Wilmar 
    private int processingActivity;
    private String fieldName;
    private String fieldArea;
    private String fieldCrop;
    private String quantityApplied;
    private Date lastDateOfChemical;
    private String inputApplied;
    private String inputSource;
    private String activitiesInCoconutFarming;
    private String lastDateOfChemicalString;
    /*
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Checks if is same addressof farmer.
     * @return true, if is same addressof farmer
     */
    public boolean isSameAddressofFarmer() {

        return sameAddressofFarmer;
    }

    /**
     * Sets the same addressof farmer.
     * @param sameAddressofFarmer the new same addressof farmer
     */
    public void setSameAddressofFarmer(boolean sameAddressofFarmer) {

        this.sameAddressofFarmer = sameAddressofFarmer;
    }

    /**
     * Gets the farm address.
     * @return the farm address
     */
//    @Length(max = 255, message = "length.address")
  //  @Pattern(regex = "[^@#!$%^&*_]+$", message = "pattern.address")
    public String getFarmAddress() {

        return farmAddress;
    }

    /**
     * Sets the farm address.
     * @param farmAddress the new farm address
     */
    public void setFarmAddress(String farmAddress) {

        this.farmAddress = farmAddress;
    }

    /**
     * Gets the land measurement.
     * @return the land measurement
     */
    public String getLandMeasurement() {

        return landMeasurement;
    }

    /**
     * Sets the land measurement.
     * @param landMeasurement the new land measurement
     */
    public void setLandMeasurement(String landMeasurement) {

        this.landMeasurement = landMeasurement;
    }

    /**
     * Gets the farm area.
     * @return the farm area
     */
  //  @Pattern(regex = THREE_DECIMAL_PATTERN, message = "pattern.farmArea")
    public String getFarmArea() {

        return farmArea;
    }

    /**
     * Sets the farm area.
     * @param farmArea the new farm area
     */
    public void setFarmArea(String farmArea) {

        this.farmArea = farmArea;
    }

    /**
     * Gets the farm productive area.
     * @return the farm productive area
     */
 //   @Pattern(regex = THREE_DECIMAL_PATTERN, message = "pattern.productiveArea")
    public String getFarmProductiveArea() {

        return farmProductiveArea;
    }

    /**
     * Sets the farm productive area.
     * @param farmProductiveArea the new farm productive area
     */
    public void setFarmProductiveArea(String farmProductiveArea) {

        this.farmProductiveArea = farmProductiveArea;
    }

    /**
     * Gets the farm conservation area.
     * @return the farm conservation area
     */
  //  @Pattern(regex = THREE_DECIMAL_PATTERN, message = "pattern.conservationArea")
    public String getFarmConservationArea() {

        return farmConservationArea;
    }

    /**
     * Sets the farm conservation area.
     * @param farmConservationArea the new farm conservation area
     */
    public void setFarmConservationArea(String farmConservationArea) {

        this.farmConservationArea = farmConservationArea;
    }

    /**
     * Gets the water bodies count.
     * @return the water bodies count
     */
 //   @Length(max = 2, message = "length.waterBodiesCount")
 //   @Pattern(regex = "[0-9]+$", message = "pattern.waterBodiesCount")
    public String getWaterBodiesCount() {

        return waterBodiesCount;
    }

    /**
     * Sets the water bodies count.
     * @param waterBodiesCount the new water bodies count
     */
    public void setWaterBodiesCount(String waterBodiesCount) {

        this.waterBodiesCount = waterBodiesCount;
    }

    /**
     * Gets the area under irrigation.
     * @return the area under irrigation
     */
  //  @Length(max = 4, message = "length.areaUnderIrrigation")
    public String getAreaUnderIrrigation() {

        return areaUnderIrrigation;
    }

    /**
     * Sets the area under irrigation.
     * @param areaUnderIrrigation the new area under irrigation
     */
    public void setAreaUnderIrrigation(String areaUnderIrrigation) {

        this.areaUnderIrrigation = areaUnderIrrigation;
    }

  

    /**
     * Gets the irrigation method.
     * @return the irrigation method
     */
    public int getIrrigationMethod() {

        return irrigationMethod;
    }

    /**
     * Sets the irrigation method.
     * @param irrigationMethod the new irrigation method
     */
    public void setIrrigationMethod(int irrigationMethod) {

        this.irrigationMethod = irrigationMethod;
    }

    /**
     * Gets the soil type.
     * @return the soil type
     */
    public String getSoilType() {

        return soilType;
    }

    /**
     * Sets the soil type.
     * @param soilType the new soil type
     */
    public void setSoilType(String soilType) {

        this.soilType = soilType;
    }

    /**
     * Gets the soil fertility.
     * @return the soil fertility
     */
    public int getSoilFertility() {

        return soilFertility;
    }

    /**
     * Sets the soil fertility.
     * @param soilFertility the new soil fertility
     */
    public void setSoilFertility(int soilFertility) {

        this.soilFertility = soilFertility;
    }

    /**
     * Gets the full time workers count.
     * @return the full time workers count
     */
 //   @Length(max = 4, message = "length.fullTimeWorkersCount")
 //   @Pattern(regex = "[0-9]+$", message = "pattern.fullTimeWorkersCount")
    public String getFullTimeWorkersCount() {

        return fullTimeWorkersCount;
    }

    /**
     * Sets the full time workers count.
     * @param fullTimeWorkersCount the new full time workers count
     */
    public void setFullTimeWorkersCount(String fullTimeWorkersCount) {

        this.fullTimeWorkersCount = fullTimeWorkersCount;
    }

    /**
     * Gets the part time workers count.
     * @return the part time workers count
     */
 //   @Length(max = 4, message = "length.partTimeWorkersCount")
//    @Pattern(regex = "[0-9]+$", message = "pattern.partTimeWorkersCount")
    public String getPartTimeWorkersCount() {

        return partTimeWorkersCount;
    }

    /**
     * Sets the part time workers count.
     * @param partTimeWorkersCount the new part time workers count
     */
    public void setPartTimeWorkersCount(String partTimeWorkersCount) {

        this.partTimeWorkersCount = partTimeWorkersCount;
    }

    /**
     * Gets the seasonal workers count.
     * @return the seasonal workers count
     */
//    @Length(max = 4, message = "length.seasonalWorkersCount")
//    @Pattern(regex = "[0-9]+$", message = "pattern.seasonalWorkersCount")
    public String getSeasonalWorkersCount() {

        return seasonalWorkersCount;
    }

    /**
     * Sets the seasonal workers count.
     * @param seasonalWorkersCount the new seasonal workers count
     */
    public void setSeasonalWorkersCount(String seasonalWorkersCount) {

        this.seasonalWorkersCount = seasonalWorkersCount;
    }

    /**
     * Gets the last date of chemical application.
     * @return the last date of chemical application
     */
    public String getLastDateOfChemicalApplication() {

        return lastDateOfChemicalApplication;
    }

    /**
     * Sets the last date of chemical application.
     * @param lastDateOfChemicalApplication the new last date of chemical application
     */
    public void setLastDateOfChemicalApplication(String lastDateOfChemicalApplication) {

        this.lastDateOfChemicalApplication = lastDateOfChemicalApplication;
    }

    /**
     * Gets the begin of conversion.
     * @return the begin of conversion
     */
    public String getBeginOfConversion() {

        return beginOfConversion;
    }

    /**
     * Sets the begin of conversion.
     * @param beginOfConversion the new begin of conversion
     */
    public void setBeginOfConversion(String beginOfConversion) {

        this.beginOfConversion = beginOfConversion;
    }

    /**
     * Gets the internal inspection date.
     * @return the internal inspection date
     */
    public Date getInternalInspectionDate() {

        return internalInspectionDate;
    }

    /**
     * Sets the internal inspection date.
     * @param internalInspectionDate the new internal inspection date
     */
    public void setInternalInspectionDate(Date internalInspectionDate) {

        this.internalInspectionDate = internalInspectionDate;
    }

    /**
     * Gets the internal inspector name.
     * @return the internal inspector name
     */
    public String getInternalInspectorName() {

        return internalInspectorName;
    }

    /**
     * Sets the internal inspector name.
     * @param internalInspectorName the new internal inspector name
     */
    public void setInternalInspectorName(String internalInspectorName) {

        this.internalInspectorName = internalInspectorName;
    }

    /**
     * Gets the survey number.
     * @return the survey number
     */
    public String getSurveyNumber() {

        return surveyNumber;
    }

    /**
     * Sets the survey number.
     * @param surveyNumber the new survey number
     */
    public void setSurveyNumber(String surveyNumber) {

        this.surveyNumber = surveyNumber;
    }

    /**
     * Gets the land under ics status.
     * @return the land under ics status
     */
    public int getLandUnderICSStatus() {

        return landUnderICSStatus;
    }

    /**
     * Sets the land under ics status.
     * @param landUnderICSStatus the new land under ics status
     */
    public void setLandUnderICSStatus(int landUnderICSStatus) {

        this.landUnderICSStatus = landUnderICSStatus;
    }

    /**
     * Gets the fallow or pasture land.
     * @return the fallow or pasture land
     */
  //  @Pattern(regex = THREE_DECIMAL_PATTERN, message = "pattern.pastureLand")
    public String getFallowOrPastureLand() {

        return fallowOrPastureLand;
    }

    /**
     * Sets the fallow or pasture land.
     * @param fallowOrPastureLand the new fallow or pasture land
     */
    public void setFallowOrPastureLand(String fallowOrPastureLand) {

        this.fallowOrPastureLand = fallowOrPastureLand;
    }

    /**
     * Gets the conventional land.
     * @return the conventional land
     */
//    @Pattern(regex = THREE_DECIMAL_PATTERN, message = "pattern.conventionalLand")
     public String getConventionalLand() {

        return conventionalLand;
    }

    /**
     * Sets the conventional land.
     * @param conventionalLand the new conventional land
     */
    public void setConventionalLand(String conventionalLand) {

        this.conventionalLand = conventionalLand;
    }

    /**
     * Gets the conventional crops.
     * @return the conventional crops
     */
  //  @Pattern(regex = THREE_DECIMAL_PATTERN, message = "pattern.conventionalCrops")
    public String getConventionalCrops() {

        return conventionalCrops;
    }

    /**
     * Sets the conventional crops.
     * @param conventionalCrops the new conventional crops
     */
    public void setConventionalCrops(String conventionalCrops) {

        this.conventionalCrops = conventionalCrops;
    }

    /**
     * Gets the conventional estimated yield.
     * @return the conventional estimated yield
     */
 //   @Pattern(regex = THREE_DECIMAL_PATTERN, message = "pattern.estimatedYield")
    public String getConventionalEstimatedYield() {

        return conventionalEstimatedYield;
    }

    /**
     * Sets the conventional estimated yield.
     * @param conventionalEstimatedYield the new conventional estimated yield
     */
    public void setConventionalEstimatedYield(String conventionalEstimatedYield) {

        this.conventionalEstimatedYield = conventionalEstimatedYield;
    }

    /**
     * Gets the nc.
     * @return the nc
     */
    public String getNc() {

        return nc;
    }

    /**
     * Sets the nc.
     * @param nc the new nc
     */
    public void setNc(String nc) {

        this.nc = nc;
    }

    /**
     * Gets the total land holding.
     * @return the total land holding
     */
    public String getTotalLandHolding() {

        return totalLandHolding;
    }

    /**
     * Sets the total land holding.
     * @param totalLandHolding the new total land holding
     */
    public void setTotalLandHolding(String totalLandHolding) {

        this.totalLandHolding = totalLandHolding;
    }

    /**
     * Gets the proposed planting area.
     * @return the proposed planting area
     */
    public String getProposedPlantingArea() {

        return proposedPlantingArea;
    }

    /**
     * Sets the proposed planting area.
     * @param proposedPlantingArea the new proposed planting area
     */
    public void setProposedPlantingArea(String proposedPlantingArea) {

        this.proposedPlantingArea = proposedPlantingArea;
    }

    /**
     * Gets the land gradient.
     * @return the land gradient
     */
    public String getLandGradient() {

        return landGradient;
    }

    /**
     * Sets the land gradient.
     * @param landGradient the new land gradient
     */
    public void setLandGradient(String landGradient) {

        this.landGradient = landGradient;
    }

    /**
     * Gets the approach road.
     * @return the approach road
     */
   // @Pattern(regex = "[a-zA-z0-9]+$", message = "pattern.approachRoad")
    
     public String getApproachRoad() {
		return approachRoad;
	}

	public void setApproachRoad(String approachRoad) {
		this.approachRoad = approachRoad;
	}

    /**
     * Gets the reg year.
     * @return the reg year
     */
    //@NotEmpty(message = "empty.regYear")
    public String getRegYear() {

        return regYear;
    }

   

	/**
     * Sets the reg year.
     * @param regYear the new reg year
     */
    public void setRegYear(String regYear) {

        this.regYear = regYear;
    }

    /**
     * Gets the irrigated other.
     * @return the irrigated other
     */
    public String getIrrigatedOther() {

        return irrigatedOther;
    }

    /**
     * Sets the irrigated other.
     * @param irrigatedOther the new irrigated other
     */
    public void setIrrigatedOther(String irrigatedOther) {

        this.irrigatedOther = irrigatedOther;
    }

    /**
     * Gets the soil texture.
     * @return the soil texture
     */
    public String getSoilTexture() {

        return soilTexture;
    }

    /**
     * Sets the soil texture.
     * @param soilTexture the new soil texture
     */
    public void setSoilTexture(String soilTexture) {

        this.soilTexture = soilTexture;
    }

    /**
     * Gets the rain fed value.
     * @return the rain fed value
     */
    public String getRainFedValue() {

        return rainFedValue;
    }

    /**
     * Sets the rain fed value.
     * @param rainFedValue the new rain fed value
     */
    public void setRainFedValue(String rainFedValue) {

        this.rainFedValue = rainFedValue;
    }

	public String getSessionYear() {
		return sessionYear;
	}

	public void setSessionYear(String sessionYearString) {
		this.sessionYear = sessionYearString;
	}

	
	public String getIrrigationSource() {
		return irrigationSource;
	}

	public void setIrrigationSource(String irrigationSource) {
		this.irrigationSource = irrigationSource;
	}

    public String getFarmIrrigation() {

        return farmIrrigation;
    }

    public void setFarmIrrigation(String farmIrrigation) {

        this.farmIrrigation = farmIrrigation;
    }

    public String getFarmOwned() {
    
        return farmOwned;
    }

    public void setFarmOwned(String farmOwned) {
    
        this.farmOwned = farmOwned;
    }

	public int getBoreWellRechargeStructure() {
		return boreWellRechargeStructure;
	}

	public void setBoreWellRechargeStructure(int boreWellRechargeStructure) {
		this.boreWellRechargeStructure = boreWellRechargeStructure;
	}

	public String getMethodOfIrrigation() {
		return methodOfIrrigation;
	}

	public void setMethodOfIrrigation(String methodOfIrrigation) {
		this.methodOfIrrigation = methodOfIrrigation;
	}

	public int getMilletCultivated() {
		return milletCultivated;
	}

	public void setMilletCultivated(int milletCultivated) {
		this.milletCultivated = milletCultivated;
	}

	public String getMilletCropType() {
		return milletCropType;
	}

	public void setMilletCropType(String milletCropType) {
		this.milletCropType = milletCropType;
	}

	public int getMilletCropCount() {
		return milletCropCount;
	}

	public void setMilletCropCount(int milletCropCount) {
		this.milletCropCount = milletCropCount;
	}
	public int getFarmerFieldSchool() {
		return farmerFieldSchool;
	}

	public void setFarmerFieldSchool(int farmerFieldSchool) {
		this.farmerFieldSchool = farmerFieldSchool;
	}

	public String getIsFFSBenifited() {
		return isFFSBenifited;
	}

	public void setIsFFSBenifited(String isFFSBenifited) {
		this.isFFSBenifited = isFFSBenifited;
	}

	public int getProcessingActivity() {
		return processingActivity;
	}

	public void setProcessingActivity(int processingActivity) {
		this.processingActivity = processingActivity;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldArea() {
		return fieldArea;
	}

	public void setFieldArea(String fieldArea) {
		this.fieldArea = fieldArea;
	}

	public String getFieldCrop() {
		return fieldCrop;
	}

	public void setFieldCrop(String fieldCrop) {
		this.fieldCrop = fieldCrop;
	}

	public String getQuantityApplied() {
		return quantityApplied;
	}

	public void setQuantityApplied(String quantityApplied) {
		this.quantityApplied = quantityApplied;
	}

	public Date getLastDateOfChemical() {
		return lastDateOfChemical;
	}

	public void setLastDateOfChemical(Date lastDateOfChemical) {
		this.lastDateOfChemical = lastDateOfChemical;
	}

	public String getInputApplied() {
		return inputApplied;
	}

	public void setInputApplied(String inputApplied) {
		this.inputApplied = inputApplied;
	}

	public String getInputSource() {
		return inputSource;
	}

	public void setInputSource(String inputSource) {
		this.inputSource = inputSource;
	}

	public String getActivitiesInCoconutFarming() {
		return activitiesInCoconutFarming;
	}

	public void setActivitiesInCoconutFarming(String activitiesInCoconutFarming) {
		this.activitiesInCoconutFarming = activitiesInCoconutFarming;
	}

	public String getLastDateOfChemicalString() {
		return lastDateOfChemicalString;
	}

	public void setLastDateOfChemicalString(String lastDateOfChemicalString) {
		this.lastDateOfChemicalString = lastDateOfChemicalString;
	}
	
    
}
