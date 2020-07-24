/*
 * Farmer.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.esesw.entity.profile;

import java.util.List;
import java.util.Set;

public class AFLExport {
	private String id;
	private String farmerCode;
	private String farmId;
	private String apedaNo;
	private String farmerName;
	private String fatherName;
	private String age;
	private String caste;
	private String address;
	private String houseNo;
	private String mobileNo;
	private String aadharNo;
	private String villageName;
	private String gpsTracking;
	private String cityName;
	private String districtCode;
	private String districtName;
	private String stateName;
	private String pinCode;
	private String landmark;
	private String totalArea;
	private String areaUnderOrganic;
	private String surveyNo;
	private String noOfPlot;
	private String landmarkLattitude;
	private String landmarkLongitude;
	private String fieldLattitude;
	private String fieldLongitude;
	private String animalHusbandry;
	private String cultivationArea;
	private String cropType;
	private String variety;
	private String noOfTrees;
	private String estimatedYield;
	private String actualYield;
	private String group;
	
	private String inspection1Date;
	private String inspector1Name;
	private String inspector1Mobile;
	private String inspection1status;

	
	private String inspection2Date;
	private String inspector2Name;
	private String inspector2Mobile;
	private String inspection2status;
	
	private String npopIcs;
	private String nopIcs;
	private String icsName;
	private String gender;
	private String dateOfReg;
	private String lastDateCheApp;
	private String branch;
	private List<String> branchesList;
	private Set<FarmIcsConversion> farmICSConversion;
	private String tracenetCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFarmerCode() {
		return farmerCode;
	}
	public void setFarmerCode(String farmerCode) {
		this.farmerCode = farmerCode;
	}
	public String getApedaNo() {
		return apedaNo;
	}
	public void setApedaNo(String apedaNo) {
		this.apedaNo = apedaNo;
	}
	public String getFarmerName() {
		return farmerName;
	}
	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getCaste() {
		return caste;
	}
	public void setCaste(String caste) {
		this.caste = caste;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getAadharNo() {
		return aadharNo;
	}
	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}
	public String getVillageName() {
		return villageName;
	}
	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}
	public String getGpsTracking() {
		return gpsTracking;
	}
	public void setGpsTracking(String gpsTracking) {
		this.gpsTracking = gpsTracking;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public String getTotalArea() {
		return totalArea;
	}
	public void setTotalArea(String totalArea) {
		this.totalArea = totalArea;
	}
	public String getAreaUnderOrganic() {
		return areaUnderOrganic;
	}
	public void setAreaUnderOrganic(String areaUnderOrganic) {
		this.areaUnderOrganic = areaUnderOrganic;
	}
	public String getSurveyNo() {
		return surveyNo;
	}
	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}
	public String getNoOfPlot() {
		return noOfPlot;
	}
	public void setNoOfPlot(String noOfPlot) {
		this.noOfPlot = noOfPlot;
	}
	public String getLandmarkLattitude() {
		return landmarkLattitude;
	}
	public void setLandmarkLattitude(String landmarkLattitude) {
		this.landmarkLattitude = landmarkLattitude;
	}
	public String getLandmarkLongitude() {
		return landmarkLongitude;
	}
	public void setLandmarkLongitude(String landmarkLongitude) {
		this.landmarkLongitude = landmarkLongitude;
	}
	public String getFieldLattitude() {
		return fieldLattitude;
	}
	public void setFieldLattitude(String fieldLattitude) {
		this.fieldLattitude = fieldLattitude;
	}
	public String getFieldLongitude() {
		return fieldLongitude;
	}
	public void setFieldLongitude(String fieldLongitude) {
		this.fieldLongitude = fieldLongitude;
	}
	public String getAnimalHusbandry() {
		return animalHusbandry;
	}
	public void setAnimalHusbandry(String animalHusbandry) {
		this.animalHusbandry = animalHusbandry;
	}
	public String getCultivationArea() {
		return cultivationArea;
	}
	public void setCultivationArea(String cultivationArea) {
		this.cultivationArea = cultivationArea;
	}
	public String getCropType() {
		return cropType;
	}
	public void setCropType(String cropType) {
		this.cropType = cropType;
	}
	public String getVariety() {
		return variety;
	}
	public void setVariety(String variety) {
		this.variety = variety;
	}
	public String getNoOfTrees() {
		return noOfTrees;
	}
	public void setNoOfTrees(String noOfTrees) {
		this.noOfTrees = noOfTrees;
	}
	public String getEstimatedYield() {
		return estimatedYield;
	}
	public void setEstimatedYield(String estimatedYield) {
		this.estimatedYield = estimatedYield;
	}
	public String getActualYield() {
		return actualYield;
	}
	public void setActualYield(String actualYield) {
		this.actualYield = actualYield;
	}
	
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	
	public String getIcsName() {
		return icsName;
	}
	public void setIcsName(String icsName) {
		this.icsName = icsName;
	}
	
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public List<String> getBranchesList() {
		return branchesList;
	}
	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	public String getDateOfReg() {
		return dateOfReg;
	}
	public void setDateOfReg(String dateOfReg) {
		this.dateOfReg = dateOfReg;
	}
	public String getLastDateCheApp() {
		return lastDateCheApp;
	}
	public void setLastDateCheApp(String lastDateCheApp) {
		this.lastDateCheApp = lastDateCheApp;
	}
	public String getInspection1Date() {
		return inspection1Date;
	}
	public void setInspection1Date(String inspection1Date) {
		this.inspection1Date = inspection1Date;
	}
	public String getInspector1Name() {
		return inspector1Name;
	}
	public void setInspector1Name(String inspector1Name) {
		this.inspector1Name = inspector1Name;
	}
	public String getInspector1Mobile() {
		return inspector1Mobile;
	}
	public void setInspector1Mobile(String inspector1Mobile) {
		this.inspector1Mobile = inspector1Mobile;
	}
	public String getInspection1status() {
		return inspection1status;
	}
	public void setInspection1status(String inspection1status) {
		this.inspection1status = inspection1status;
	}
	public String getInspection2Date() {
		return inspection2Date;
	}
	public void setInspection2Date(String inspection2Date) {
		this.inspection2Date = inspection2Date;
	}
	public String getInspector2Name() {
		return inspector2Name;
	}
	public void setInspector2Name(String inspector2Name) {
		this.inspector2Name = inspector2Name;
	}
	public String getInspector2Mobile() {
		return inspector2Mobile;
	}
	public void setInspector2Mobile(String inspector2Mobile) {
		this.inspector2Mobile = inspector2Mobile;
	}
	public String getInspection2status() {
		return inspection2status;
	}
	public void setInspection2status(String inspection2status) {
		this.inspection2status = inspection2status;
	}
	public String getNpopIcs() {
		return npopIcs;
	}
	public void setNpopIcs(String npopIcs) {
		this.npopIcs = npopIcs;
	}
	public String getNopIcs() {
		return nopIcs;
	}
	public void setNopIcs(String nopIcs) {
		this.nopIcs = nopIcs;
	}
	public Set<FarmIcsConversion> getFarmICSConversion() {
		return farmICSConversion;
	}
	public void setFarmICSConversion(Set<FarmIcsConversion> farmICSConversion) {
		this.farmICSConversion = farmICSConversion;
	}
	public String getFarmId() {
		return farmId;
	}
	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}
	public String getTracenetCode() {
		return tracenetCode;
	}
	public void setTracenetCode(String tracenetCode) {
		this.tracenetCode = tracenetCode;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	
	
}
