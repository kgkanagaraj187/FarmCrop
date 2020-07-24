package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.Set;

import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.ResearchStation;

public class CowInspection 
{
	
	private long id;
	private String elitType;
	private ResearchStation researchStation;
	private String farmerId;
	private Farm farm;
	private String cowId;
	private String inspectionNo;
	private Date lastInspDate;
	private Date currentInspDate;
	private int intervalDays; 
	private double milkMorngPerday;
	private double milkEvngPerday;
	private double totalMilkPerDay;
	private double totalMilkPerPeriod;
	private byte[] audio;
	private String feedType;
	private String feedAmtPerPeriod;
	private String infestationPara;
	private String deworwingPlace;
	private String medicineName;
	private String diseaseNoticed;
	private String diseaseName;
	private String diseaseServices;
	private String diseaseMedicines;
	private String healthProblem;
	private String healthServices;
	private String healthMedicines;
	private String correctivMeasure;
	private String vaccinationPlace;
	private String latitude;
	private String longitude;
	private String comments;
	private String branchId;
	private String createdUserName;
	private String lastUpdatedUserName;
	private Set<CowInspectionImages> inspectionImages;
	private Set<CowFeedType> cowFeedTypes;
	private String isMilkingCow;
	private Set<CowVaccination> cowVaccinations;
	
	public static String IS_MILKING="Y";
	public static String NON_MILKING="N";
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public ResearchStation getResearchStation() {
		return researchStation;
	}
	public void setResearchStation(ResearchStation researchStation) {
		this.researchStation = researchStation;
	}
	public String getFarmerId() {
		return farmerId;
	}
	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}
	
	public String getInspectionNo() {
		return inspectionNo;
	}
	public void setInspectionNo(String inspectionNo) {
		this.inspectionNo = inspectionNo;
	}
	
	public int getIntervalDays() {
		return intervalDays;
	}
	public void setIntervalDays(int intervalDays) {
		this.intervalDays = intervalDays;
	}
	public double getMilkMorngPerday() {
		return milkMorngPerday;
	}
	public void setMilkMorngPerday(double milkMorngPerday) {
		this.milkMorngPerday = milkMorngPerday;
	}
	
	public double getTotalMilkPerDay() {
		return totalMilkPerDay;
	}
	public void setTotalMilkPerDay(double totalMilkPerDay) {
		this.totalMilkPerDay = totalMilkPerDay;
	}
	public double getTotalMilkPerPeriod() {
		return totalMilkPerPeriod;
	}
	public void setTotalMilkPerPeriod(double totalMilkPerPeriod) {
		this.totalMilkPerPeriod = totalMilkPerPeriod;
	}
	public String getFeedType() {
		return feedType;
	}
	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}
	public String getFeedAmtPerPeriod() {
		return feedAmtPerPeriod;
	}
	public void setFeedAmtPerPeriod(String feedAmtPerPeriod) {
		this.feedAmtPerPeriod = feedAmtPerPeriod;
	}
	public String getInfestationPara() {
		return infestationPara;
	}
	public void setInfestationPara(String infestationPara) {
		this.infestationPara = infestationPara;
	}
	public String getDeworwingPlace() {
		return deworwingPlace;
	}
	public void setDeworwingPlace(String deworwingPlace) {
		this.deworwingPlace = deworwingPlace;
	}
	public String getMedicineName() {
		return medicineName;
	}
	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}
	public String getDiseaseNoticed() {
		return diseaseNoticed;
	}
	public void setDiseaseNoticed(String diseaseNoticed) {
		this.diseaseNoticed = diseaseNoticed;
	}
	public String getDiseaseName() {
		return diseaseName;
	}
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	

	public String getHealthProblem() {
		return healthProblem;
	}
	public void setHealthProblem(String healthProblem) {
		this.healthProblem = healthProblem;
	}
	public String getCorrectivMeasure() {
		return correctivMeasure;
	}
	public void setCorrectivMeasure(String correctivMeasure) {
		this.correctivMeasure = correctivMeasure;
	}
	public String getVaccinationPlace() {
		return vaccinationPlace;
	}
	public void setVaccinationPlace(String vaccinationPlace) {
		this.vaccinationPlace = vaccinationPlace;
	}
	
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public Farm getFarm() {
		return farm;
	}
	public void setFarm(Farm farm) {
		this.farm = farm;
	}
	public double getMilkEvngPerday() {
		return milkEvngPerday;
	}
	public void setMilkEvngPerday(double milkEvngPerday) {
		this.milkEvngPerday = milkEvngPerday;
	}
	
	public String getElitType() {
		return elitType;
	}
	public void setElitType(String elitType) {
		this.elitType = elitType;
	}
	public Set<CowInspectionImages> getInspectionImages() {
		return inspectionImages;
	}
	public void setInspectionImages(Set<CowInspectionImages> inspectionImages) {
		this.inspectionImages = inspectionImages;
	}
	
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getDiseaseServices() {
		return diseaseServices;
	}
	public void setDiseaseServices(String diseaseServices) {
		this.diseaseServices = diseaseServices;
	}
	public String getDiseaseMedicines() {
		return diseaseMedicines;
	}
	public void setDiseaseMedicines(String diseaseMedicines) {
		this.diseaseMedicines = diseaseMedicines;
	}
	public String getHealthServices() {
		return healthServices;
	}
	public void setHealthServices(String healthServices) {
		this.healthServices = healthServices;
	}
	public String getHealthMedicines() {
		return healthMedicines;
	}
	public void setHealthMedicines(String healthMedicines) {
		this.healthMedicines = healthMedicines;
	}
	public byte[] getAudio() {
		return audio;
	}
	public void setAudio(byte[] audio) {
		this.audio = audio;
	}
	public Set<CowFeedType> getCowFeedTypes() {
		return cowFeedTypes;
	}
	public void setCowFeedTypes(Set<CowFeedType> cowFeedTypes) {
		this.cowFeedTypes = cowFeedTypes;
	}
	public Date getLastInspDate() {
		return lastInspDate;
	}
	public void setLastInspDate(Date lastInspDate) {
		this.lastInspDate = lastInspDate;
	}
	public Date getCurrentInspDate() {
		return currentInspDate;
	}
	public void setCurrentInspDate(Date currentInspDate) {
		this.currentInspDate = currentInspDate;
	}
	public String getCowId() {
		return cowId;
	}
	public void setCowId(String cowId) {
		this.cowId = cowId;
	}
	public String getCreatedUserName() {
		return createdUserName;
	}
	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}
	public String getLastUpdatedUserName() {
		return lastUpdatedUserName;
	}
	public void setLastUpdatedUserName(String lastUpdatedUserName) {
		this.lastUpdatedUserName = lastUpdatedUserName;
	}
	public String getIsMilkingCow() {
		return isMilkingCow;
	}
	public void setIsMilkingCow(String isMilkingCow) {
		this.isMilkingCow = isMilkingCow;
	}
	public Set<CowVaccination> getCowVaccinations() {
		return cowVaccinations;
	}
	public void setCowVaccinations(Set<CowVaccination> cowVaccinations) {
		this.cowVaccinations = cowVaccinations;
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
	
	
	
	

}
