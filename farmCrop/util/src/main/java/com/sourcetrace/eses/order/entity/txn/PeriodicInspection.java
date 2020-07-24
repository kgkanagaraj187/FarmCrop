/*
 * PeriodicInspection.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.profile.InspectionImageInfo;
import com.sourcetrace.eses.order.entity.profile.Symptoms;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.txn.ESETxn;

// TODO: Auto-generated Javadoc
public class PeriodicInspection {

	public static final String ACTIVITIES_CARRIED_OUT = "ACAPV";
	public static final String INTERPLOUGHING_WITH = "INTPLUG";
	public static final String MANURE_APPLIED = "MATYP";
	public static final String FERTILIZER_APPLIED = "FRTATYP";
	public static final String PEST_NAME = "PEST";
	public static final String PESTICIDE_RECOMMENTED = "PESTREC";
	public static final String DISEASE_NAME = "DISEASE";
	public static final String FUNGISIDE_RECOMMENTED = "FUNGISREC";

	public static final char ACTIVE = 'Y';
	public static final char IN_ACTIVE = 'N';
	public static final char YES = 'Y';
	public static final char NO = 'N';
	public static final String NO_No = "2";

	private long id;
	private String inspectionType;
	private Date inspectionDate;
	private String farmId;
	private String purpose;
	private String remarks;
	private byte[] farmerVoice;
	private String survivalPercentage;
	private String averageHeight;
	private double averageGirth;
	private String currentStatusOfGrowth;
	private String activitiesAfterPreVisit;
	private String interPloughingWith;
	private String typeOfManureApplied;
	private String otherTypeOfManureApplied;
	private double manureQuantityApplied;
	private String typeOfFertilizerApplied;
	private String otherTypeOfFertilizerApplied;
	private double fertilizerQuantityApplied;
	private char pestProblemNoticed;
	private String nameOfPest;
	private String otherPestName;
	private Symptoms pestSymptom;
	private String pestSymptomOtherValue;
	private char pestInfestationETL;
	private String recommendedPesticideName;
	private double pesticideQuantityApplied;
	private Date pesticidateApplicationDate;
	private char pestProblemSolved;
	private char diseaseProblemNoticed;
	private String nameOfDisease;
	private String otherNameOfDisease;
	private Symptoms diseaseSymptom;
	private String diseaseSymptomOtherValue;
	private char diseaseInfestationETL;
	private String recommendedFungicideName;
	private double fungicideQuantityApplied;
	private Date dateOfFungicideApplication;
	private char diseaseProblemSolved;
	private String farmerOpinion;
	private char interCrop;
	private String nameOfInterCrop;
	private double yieldObtained;
	private double expenditureIncurred;
	private double incomeGenerated;
	private double netProfitOrLoss;
	private InspectionImageInfo inspectionImageInfo;
	private String txnId;
	private String txnType;

	private String chemicalName;
	private String monthOfFertilizerApplied;
	private String monthOfPesticideApplication;
	private String monthOfFungicideApplication;
	private char isFertilizerApplied;
	private char isFieldSafetyProposal;

	private Set<PeriodicInspectionData> periodicInspectionData;
	private Set<PeriodicInspectionSymptom> periodicInspectionSymptoms;

	private Date createdDT;
	private String createdUserName;
	private Date lastUpdatedDT;
	private String lastUpdatedUserName;

	private String noOfPlantsReplanned;
	private Date gapPlantingDate;
	private String branchId;

	private String pestSymptomsName;
	private String diseaseSymptomsName;
	private String cropRotation;

	private String temperature;
	private String humidity;
	private String rain;
	private String windSpeed;
	private String seasonCode;
	private String isDeleted;
	private String cropCode;

	// Pratibha
	private String landpreparationCompleted;
	private String chemicalSpray;
	private String monoOrImida;
	private String singleSprayOrCocktail;
	private String repetitionOfPest;
	private String nitrogenousFert;
	private String cropSpacingLastYear;
	private String cropSpacingCurrentYear;
	private String weeding;
	private String picking;
	private byte[] soilPhoto;
	
	//AVT
	private String cropProtectionPractice;
	// Read Only Properties
	private Farm farm;
	private String audioTrue;
	private Set<PeriodicInspectionData> activitiesAfterPreVisitSet = new LinkedHashSet<PeriodicInspectionData>();
	private Set<PeriodicInspectionData> inteploughingWithSet = new LinkedHashSet<PeriodicInspectionData>();
	private Set<PeriodicInspectionData> typeOfManureSet = new LinkedHashSet<PeriodicInspectionData>();
	private Set<PeriodicInspectionData> fertilizerAppliedSet = new LinkedHashSet<PeriodicInspectionData>();
	private Set<PeriodicInspectionData> nameOfPestSet = new LinkedHashSet<PeriodicInspectionData>();
	private Set<PeriodicInspectionData> pesticideRecommentedSet = new LinkedHashSet<PeriodicInspectionData>();
	private Set<PeriodicInspectionData> nameOfDiseaseSet = new LinkedHashSet<PeriodicInspectionData>();
	private Set<PeriodicInspectionData> fungicideRecommentedSet = new LinkedHashSet<PeriodicInspectionData>();
	private Set<PeriodicInspectionSymptom> pestSymptomsSet = new LinkedHashSet<PeriodicInspectionSymptom>();
	private Set<PeriodicInspectionSymptom> diseaseSymptomsSet = new LinkedHashSet<PeriodicInspectionSymptom>();
	private ESETxn eSETxn;

	private String latitude;
	private String longitude;
	
	private String icsName;
	 // transient variable   
    private List<String> agentList;
    

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
	 * Gets the inspection type.
	 * 
	 * @return the inspection type
	 */
	public String getInspectionType() {

		return inspectionType;
	}

	/**
	 * Sets the inspection type.
	 * 
	 * @param inspectionType
	 *            the new inspection type
	 */
	public void setInspectionType(String inspectionType) {

		this.inspectionType = inspectionType;
	}

	/**
	 * Gets the inspection date.
	 * 
	 * @return the inspection date
	 */
	public Date getInspectionDate() {

		return inspectionDate;
	}

	/**
	 * Sets the inspection date.
	 * 
	 * @param inspectionDate
	 *            the new inspection date
	 */
	public void setInspectionDate(Date inspectionDate) {

		this.inspectionDate = inspectionDate;
	}

	/**
	 * Gets the farm id.
	 * 
	 * @return the farm id
	 */
	public String getFarmId() {

		return farmId;
	}

	/**
	 * Sets the farm id.
	 * 
	 * @param farmId
	 *            the new farm id
	 */
	public void setFarmId(String farmId) {

		this.farmId = farmId;
	}

	/**
	 * Gets the purpose.
	 * 
	 * @return the purpose
	 */
	public String getPurpose() {

		return purpose;
	}

	/**
	 * Sets the purpose.
	 * 
	 * @param purpose
	 *            the new purpose
	 */
	public void setPurpose(String purpose) {

		this.purpose = purpose;
	}

	/**
	 * Gets the remarks.
	 * 
	 * @return the remarks
	 */
	public String getRemarks() {

		return remarks;
	}

	/**
	 * Sets the remarks.
	 * 
	 * @param remarks
	 *            the new remarks
	 */
	public void setRemarks(String remarks) {

		this.remarks = remarks;
	}

	/**
	 * Gets the farmer voice.
	 * 
	 * @return the farmer voice
	 */
	public byte[] getFarmerVoice() {

		return farmerVoice;
	}

	/**
	 * Sets the farmer voice.
	 * 
	 * @param farmerVoice
	 *            the new farmer voice
	 */
	public void setFarmerVoice(byte[] farmerVoice) {

		this.farmerVoice = farmerVoice;
	}

	/**
	 * Gets the survival percentage.
	 * 
	 * @return the survival percentage
	 */
	public String getSurvivalPercentage() {

		return survivalPercentage;
	}

	/**
	 * Sets the survival percentage.
	 * 
	 * @param survivalPercentage
	 *            the new survival percentage
	 */
	public void setSurvivalPercentage(String survivalPercentage) {

		this.survivalPercentage = survivalPercentage;
	}

	/**
	 * Gets the average height.
	 * 
	 * @return the average height
	 */

	public String getAverageHeight() {

		return averageHeight;
	}

	/**
	 * Sets the average height.
	 * 
	 * @param averageHeight
	 *            the new average height
	 */

	public void setAverageHeight(String averageHeight) {

		this.averageHeight = averageHeight;
	}

	/**
	 * Gets the average girth.
	 * 
	 * @return the average girth
	 */
	public double getAverageGirth() {

		return averageGirth;
	}

	/**
	 * Sets the average girth.
	 * 
	 * @param averageGirth
	 *            the new average girth
	 */
	public void setAverageGirth(double averageGirth) {

		this.averageGirth = averageGirth;
	}

	/**
	 * Gets the current status of growth.
	 * 
	 * @return the current status of growth
	 */
	public String getCurrentStatusOfGrowth() {

		return currentStatusOfGrowth;
	}

	/**
	 * Sets the current status of growth.
	 * 
	 * @param currentStatusOfGrowth
	 *            the new current status of growth
	 */
	public void setCurrentStatusOfGrowth(String currentStatusOfGrowth) {

		this.currentStatusOfGrowth = currentStatusOfGrowth;
	}

	/**
	 * Gets the activities after pre visit.
	 * 
	 * @return the activities after pre visit
	 */
	public String getActivitiesAfterPreVisit() {

		return activitiesAfterPreVisit;
	}

	/**
	 * Sets the activities after pre visit.
	 * 
	 * @param activitiesAfterPreVisit
	 *            the new activities after pre visit
	 */
	public void setActivitiesAfterPreVisit(String activitiesAfterPreVisit) {

		this.activitiesAfterPreVisit = activitiesAfterPreVisit;
	}

	/**
	 * Gets the inter ploughing with.
	 * 
	 * @return the inter ploughing with
	 */
	public String getInterPloughingWith() {

		return interPloughingWith;
	}

	/**
	 * Sets the inter ploughing with.
	 * 
	 * @param interPloughingWith
	 *            the new inter ploughing with
	 */
	public void setInterPloughingWith(String interPloughingWith) {

		this.interPloughingWith = interPloughingWith;
	}

	/**
	 * Gets the type of manure applied.
	 * 
	 * @return the type of manure applied
	 */
	public String getTypeOfManureApplied() {

		return typeOfManureApplied;
	}

	/**
	 * Sets the type of manure applied.
	 * 
	 * @param typeOfManureApplied
	 *            the new type of manure applied
	 */
	public void setTypeOfManureApplied(String typeOfManureApplied) {

		this.typeOfManureApplied = typeOfManureApplied;
	}

	/**
	 * Gets the other type of manure applied.
	 * 
	 * @return the other type of manure applied
	 */
	public String getOtherTypeOfManureApplied() {

		return otherTypeOfManureApplied;
	}

	/**
	 * Sets the other type of manure applied.
	 * 
	 * @param otherTypeOfManureApplied
	 *            the new other type of manure applied
	 */
	public void setOtherTypeOfManureApplied(String otherTypeOfManureApplied) {

		this.otherTypeOfManureApplied = otherTypeOfManureApplied;
	}

	/**
	 * Gets the manure quantity applied.
	 * 
	 * @return the manure quantity applied
	 */
	public double getManureQuantityApplied() {

		return manureQuantityApplied;
	}

	/**
	 * Sets the manure quantity applied.
	 * 
	 * @param manureQuantityApplied
	 *            the new manure quantity applied
	 */
	public void setManureQuantityApplied(double manureQuantityApplied) {

		this.manureQuantityApplied = manureQuantityApplied;
	}

	/**
	 * Gets the type of fertilizer applied.
	 * 
	 * @return the type of fertilizer applied
	 */
	public String getTypeOfFertilizerApplied() {

		return typeOfFertilizerApplied;
	}

	/**
	 * Sets the type of fertilizer applied.
	 * 
	 * @param typeOfFertilizerApplied
	 *            the new type of fertilizer applied
	 */
	public void setTypeOfFertilizerApplied(String typeOfFertilizerApplied) {

		this.typeOfFertilizerApplied = typeOfFertilizerApplied;
	}

	/**
	 * Gets the other type of fertilizer applied.
	 * 
	 * @return the other type of fertilizer applied
	 */
	public String getOtherTypeOfFertilizerApplied() {

		return otherTypeOfFertilizerApplied;
	}

	/**
	 * Sets the other type of fertilizer applied.
	 * 
	 * @param otherTypeOfFertilizerApplied
	 *            the new other type of fertilizer applied
	 */
	public void setOtherTypeOfFertilizerApplied(String otherTypeOfFertilizerApplied) {

		this.otherTypeOfFertilizerApplied = otherTypeOfFertilizerApplied;
	}

	/**
	 * Gets the fertilizer quantity applied.
	 * 
	 * @return the fertilizer quantity applied
	 */
	public double getFertilizerQuantityApplied() {

		return fertilizerQuantityApplied;
	}

	/**
	 * Sets the fertilizer quantity applied.
	 * 
	 * @param fertilizerQuantityApplied
	 *            the new fertilizer quantity applied
	 */
	public void setFertilizerQuantityApplied(double fertilizerQuantityApplied) {

		this.fertilizerQuantityApplied = fertilizerQuantityApplied;
	}

	/**
	 * Gets the pest problem noticed.
	 * 
	 * @return the pest problem noticed
	 */
	public char getPestProblemNoticed() {

		return pestProblemNoticed;
	}

	/**
	 * Sets the pest problem noticed.
	 * 
	 * @param pestProblemNoticed
	 *            the new pest problem noticed
	 */
	public void setPestProblemNoticed(char pestProblemNoticed) {

		this.pestProblemNoticed = pestProblemNoticed;
	}

	/**
	 * Gets the name of pest.
	 * 
	 * @return the name of pest
	 */
	public String getNameOfPest() {

		return nameOfPest;
	}

	/**
	 * Sets the name of pest.
	 * 
	 * @param nameOfPest
	 *            the new name of pest
	 */
	public void setNameOfPest(String nameOfPest) {

		this.nameOfPest = nameOfPest;
	}

	/**
	 * Gets the other pest name.
	 * 
	 * @return the other pest name
	 */
	public String getOtherPestName() {

		return otherPestName;
	}

	/**
	 * Sets the other pest name.
	 * 
	 * @param otherPestName
	 *            the new other pest name
	 */
	public void setOtherPestName(String otherPestName) {

		this.otherPestName = otherPestName;
	}

	/**
	 * Gets the pest symptom.
	 * 
	 * @return the pest symptom
	 */
	public Symptoms getPestSymptom() {

		return pestSymptom;
	}

	/**
	 * Sets the pest symptom.
	 * 
	 * @param pestSymptom
	 *            the new pest symptom
	 */
	public void setPestSymptom(Symptoms pestSymptom) {

		this.pestSymptom = pestSymptom;
	}

	/**
	 * Gets the pest symptom other value.
	 * 
	 * @return the pest symptom other value
	 */
	public String getPestSymptomOtherValue() {

		return pestSymptomOtherValue;
	}

	/**
	 * Sets the pest symptom other value.
	 * 
	 * @param pestSymptomOtherValue
	 *            the new pest symptom other value
	 */
	public void setPestSymptomOtherValue(String pestSymptomOtherValue) {

		this.pestSymptomOtherValue = pestSymptomOtherValue;
	}

	/**
	 * Gets the pest infestation etl.
	 * 
	 * @return the pest infestation etl
	 */
	public char getPestInfestationETL() {

		return pestInfestationETL;
	}

	/**
	 * Sets the pest infestation etl.
	 * 
	 * @param pestInfestationETL
	 *            the new pest infestation etl
	 */
	public void setPestInfestationETL(char pestInfestationETL) {

		this.pestInfestationETL = pestInfestationETL;
	}

	/**
	 * Gets the recommended pesticide name.
	 * 
	 * @return the recommended pesticide name
	 */
	public String getRecommendedPesticideName() {

		return recommendedPesticideName;
	}

	/**
	 * Sets the recommended pesticide name.
	 * 
	 * @param recommendedPesticideName
	 *            the new recommended pesticide name
	 */
	public void setRecommendedPesticideName(String recommendedPesticideName) {

		this.recommendedPesticideName = recommendedPesticideName;
	}

	/**
	 * Gets the pesticide quantity applied.
	 * 
	 * @return the pesticide quantity applied
	 */
	public double getPesticideQuantityApplied() {

		return pesticideQuantityApplied;
	}

	/**
	 * Sets the pesticide quantity applied.
	 * 
	 * @param pesticideQuantityApplied
	 *            the new pesticide quantity applied
	 */
	public void setPesticideQuantityApplied(double pesticideQuantityApplied) {

		this.pesticideQuantityApplied = pesticideQuantityApplied;
	}

	/**
	 * Gets the pesticidate application date.
	 * 
	 * @return the pesticidate application date
	 */
	public Date getPesticidateApplicationDate() {

		return pesticidateApplicationDate;
	}

	/**
	 * Sets the pesticidate application date.
	 * 
	 * @param pesticidateApplicationDate
	 *            the new pesticidate application date
	 */
	public void setPesticidateApplicationDate(Date pesticidateApplicationDate) {

		this.pesticidateApplicationDate = pesticidateApplicationDate;
	}

	/**
	 * Gets the pest problem solved.
	 * 
	 * @return the pest problem solved
	 */
	public char getPestProblemSolved() {

		return pestProblemSolved;
	}

	/**
	 * Sets the pest problem solved.
	 * 
	 * @param pestProblemSolved
	 *            the new pest problem solved
	 */
	public void setPestProblemSolved(char pestProblemSolved) {

		this.pestProblemSolved = pestProblemSolved;
	}

	/**
	 * Gets the disease problem noticed.
	 * 
	 * @return the disease problem noticed
	 */
	public char getDiseaseProblemNoticed() {

		return diseaseProblemNoticed;
	}

	/**
	 * Sets the disease problem noticed.
	 * 
	 * @param diseaseProblemNoticed
	 *            the new disease problem noticed
	 */
	public void setDiseaseProblemNoticed(char diseaseProblemNoticed) {

		this.diseaseProblemNoticed = diseaseProblemNoticed;
	}

	/**
	 * Gets the name of disease.
	 * 
	 * @return the name of disease
	 */
	public String getNameOfDisease() {

		return nameOfDisease;
	}

	/**
	 * Sets the name of disease.
	 * 
	 * @param nameOfDisease
	 *            the new name of disease
	 */
	public void setNameOfDisease(String nameOfDisease) {

		this.nameOfDisease = nameOfDisease;
	}

	/**
	 * Gets the other name of disease.
	 * 
	 * @return the other name of disease
	 */
	public String getOtherNameOfDisease() {

		return otherNameOfDisease;
	}

	/**
	 * Sets the other name of disease.
	 * 
	 * @param otherNameOfDisease
	 *            the new other name of disease
	 */
	public void setOtherNameOfDisease(String otherNameOfDisease) {

		this.otherNameOfDisease = otherNameOfDisease;
	}

	/**
	 * Gets the disease symptom.
	 * 
	 * @return the disease symptom
	 */
	public Symptoms getDiseaseSymptom() {

		return diseaseSymptom;
	}

	/**
	 * Sets the disease symptom.
	 * 
	 * @param diseaseSymptom
	 *            the new disease symptom
	 */
	public void setDiseaseSymptom(Symptoms diseaseSymptom) {

		this.diseaseSymptom = diseaseSymptom;
	}

	/**
	 * Gets the disease symptom other value.
	 * 
	 * @return the disease symptom other value
	 */
	public String getDiseaseSymptomOtherValue() {

		return diseaseSymptomOtherValue;
	}

	/**
	 * Sets the disease symptom other value.
	 * 
	 * @param diseaseSymptomOtherValue
	 *            the new disease symptom other value
	 */
	public void setDiseaseSymptomOtherValue(String diseaseSymptomOtherValue) {

		this.diseaseSymptomOtherValue = diseaseSymptomOtherValue;
	}

	/**
	 * Gets the disease infestation etl.
	 * 
	 * @return the disease infestation etl
	 */
	public char getDiseaseInfestationETL() {

		return diseaseInfestationETL;
	}

	/**
	 * Sets the disease infestation etl.
	 * 
	 * @param diseaseInfestationETL
	 *            the new disease infestation etl
	 */
	public void setDiseaseInfestationETL(char diseaseInfestationETL) {

		this.diseaseInfestationETL = diseaseInfestationETL;
	}

	/**
	 * Gets the recommended fungicide name.
	 * 
	 * @return the recommended fungicide name
	 */
	public String getRecommendedFungicideName() {

		return recommendedFungicideName;
	}

	/**
	 * Sets the recommended fungicide name.
	 * 
	 * @param recommendedFungicideName
	 *            the new recommended fungicide name
	 */
	public void setRecommendedFungicideName(String recommendedFungicideName) {

		this.recommendedFungicideName = recommendedFungicideName;
	}

	/**
	 * Gets the fungicide quantity applied.
	 * 
	 * @return the fungicide quantity applied
	 */
	public double getFungicideQuantityApplied() {

		return fungicideQuantityApplied;
	}

	/**
	 * Sets the fungicide quantity applied.
	 * 
	 * @param fungicideQuantityApplied
	 *            the new fungicide quantity applied
	 */
	public void setFungicideQuantityApplied(double fungicideQuantityApplied) {

		this.fungicideQuantityApplied = fungicideQuantityApplied;
	}

	/**
	 * Gets the date of fungicide application.
	 * 
	 * @return the date of fungicide application
	 */
	public Date getDateOfFungicideApplication() {

		return dateOfFungicideApplication;
	}

	/**
	 * Sets the date of fungicide application.
	 * 
	 * @param dateOfFungicideApplication
	 *            the new date of fungicide application
	 */
	public void setDateOfFungicideApplication(Date dateOfFungicideApplication) {

		this.dateOfFungicideApplication = dateOfFungicideApplication;
	}

	/**
	 * Gets the disease problem solved.
	 * 
	 * @return the disease problem solved
	 */
	public char getDiseaseProblemSolved() {

		return diseaseProblemSolved;
	}

	/**
	 * Sets the disease problem solved.
	 * 
	 * @param diseaseProblemSolved
	 *            the new disease problem solved
	 */
	public void setDiseaseProblemSolved(char diseaseProblemSolved) {

		this.diseaseProblemSolved = diseaseProblemSolved;
	}

	/**
	 * Gets the farmer opinion.
	 * 
	 * @return the farmer opinion
	 */
	public String getFarmerOpinion() {

		return farmerOpinion;
	}

	/**
	 * Sets the farmer opinion.
	 * 
	 * @param farmerOpinion
	 *            the new farmer opinion
	 */
	public void setFarmerOpinion(String farmerOpinion) {

		this.farmerOpinion = farmerOpinion;
	}

	/**
	 * Gets the inter crop.
	 * 
	 * @return the inter crop
	 */
	public char getInterCrop() {

		return interCrop;
	}

	/**
	 * Sets the inter crop.
	 * 
	 * @param interCrop
	 *            the new inter crop
	 */
	public void setInterCrop(char interCrop) {

		this.interCrop = interCrop;
	}

	/**
	 * Gets the name of inter crop.
	 * 
	 * @return the name of inter crop
	 */
	public String getNameOfInterCrop() {

		return nameOfInterCrop;
	}

	/**
	 * Sets the name of inter crop.
	 * 
	 * @param nameOfInterCrop
	 *            the new name of inter crop
	 */
	public void setNameOfInterCrop(String nameOfInterCrop) {

		this.nameOfInterCrop = nameOfInterCrop;
	}

	/**
	 * Gets the yield obtained.
	 * 
	 * @return the yield obtained
	 */
	public double getYieldObtained() {

		return yieldObtained;
	}

	/**
	 * Sets the yield obtained.
	 * 
	 * @param yieldObtained
	 *            the new yield obtained
	 */
	public void setYieldObtained(double yieldObtained) {

		this.yieldObtained = yieldObtained;
	}

	/**
	 * Gets the expenditure incurred.
	 * 
	 * @return the expenditure incurred
	 */
	public double getExpenditureIncurred() {

		return expenditureIncurred;
	}

	/**
	 * Sets the expenditure incurred.
	 * 
	 * @param expenditureIncurred
	 *            the new expenditure incurred
	 */
	public void setExpenditureIncurred(double expenditureIncurred) {

		this.expenditureIncurred = expenditureIncurred;
	}

	/**
	 * Gets the income generated.
	 * 
	 * @return the income generated
	 */
	public double getIncomeGenerated() {

		return incomeGenerated;
	}

	/**
	 * Sets the income generated.
	 * 
	 * @param incomeGenerated
	 *            the new income generated
	 */
	public void setIncomeGenerated(double incomeGenerated) {

		this.incomeGenerated = incomeGenerated;
	}

	/**
	 * Gets the net profit or loss.
	 * 
	 * @return the net profit or loss
	 */
	public double getNetProfitOrLoss() {

		return netProfitOrLoss;
	}

	/**
	 * Sets the net profit or loss.
	 * 
	 * @param netProfitOrLoss
	 *            the new net profit or loss
	 */
	public void setNetProfitOrLoss(double netProfitOrLoss) {

		this.netProfitOrLoss = netProfitOrLoss;
	}

	/**
	 * Gets the inspection image info.
	 * 
	 * @return the inspection image info
	 */
	public InspectionImageInfo getInspectionImageInfo() {

		return inspectionImageInfo;
	}

	/**
	 * Sets the inspection image info.
	 * 
	 * @param inspectionImageInfo
	 *            the new inspection image info
	 */
	public void setInspectionImageInfo(InspectionImageInfo inspectionImageInfo) {

		this.inspectionImageInfo = inspectionImageInfo;
	}

	/**
	 * Gets the chemical name.
	 * 
	 * @return the chemical name
	 */
	public String getChemicalName() {

		return chemicalName;
	}

	/**
	 * Sets the chemical name.
	 * 
	 * @param chemicalName
	 *            the new chemical name
	 */
	public void setChemicalName(String chemicalName) {

		this.chemicalName = chemicalName;
	}

	/**
	 * Gets the month of fertilizer applied.
	 * 
	 * @return the month of fertilizer applied
	 */
	public String getMonthOfFertilizerApplied() {

		return monthOfFertilizerApplied;
	}

	/**
	 * Sets the month of fertilizer applied.
	 * 
	 * @param monthOfFertilizerApplied
	 *            the new month of fertilizer applied
	 */
	public void setMonthOfFertilizerApplied(String monthOfFertilizerApplied) {

		this.monthOfFertilizerApplied = monthOfFertilizerApplied;
	}

	/**
	 * Gets the month of pesticide application.
	 * 
	 * @return the month of pesticide application
	 */
	public String getMonthOfPesticideApplication() {

		return monthOfPesticideApplication;
	}

	/**
	 * Sets the month of pesticide application.
	 * 
	 * @param monthOfPesticideApplication
	 *            the new month of pesticide application
	 */
	public void setMonthOfPesticideApplication(String monthOfPesticideApplication) {

		this.monthOfPesticideApplication = monthOfPesticideApplication;
	}

	/**
	 * Gets the month of fungicide application.
	 * 
	 * @return the month of fungicide application
	 */
	public String getMonthOfFungicideApplication() {

		return monthOfFungicideApplication;
	}

	/**
	 * Sets the month of fungicide application.
	 * 
	 * @param monthOfFungicideApplication
	 *            the new month of fungicide application
	 */
	public void setMonthOfFungicideApplication(String monthOfFungicideApplication) {

		this.monthOfFungicideApplication = monthOfFungicideApplication;
	}

	/**
	 * Gets the is fertilizer applied.
	 * 
	 * @return the is fertilizer applied
	 */
	public char getIsFertilizerApplied() {

		return isFertilizerApplied;
	}

	/**
	 * Sets the is fertilizer applied.
	 * 
	 * @param isFertilizerApplied
	 *            the new is fertilizer applied
	 */
	public void setIsFertilizerApplied(char isFertilizerApplied) {

		this.isFertilizerApplied = isFertilizerApplied;
	}

	/**
	 * Gets the is field safety proposal.
	 * 
	 * @return the is field safety proposal
	 */
	public char getIsFieldSafetyProposal() {

		return isFieldSafetyProposal;
	}

	/**
	 * Sets the is field safety proposal.
	 * 
	 * @param isFieldSafetyProposal
	 *            the new is field safety proposal
	 */
	public void setIsFieldSafetyProposal(char isFieldSafetyProposal) {

		this.isFieldSafetyProposal = isFieldSafetyProposal;
	}

	/**
	 * Gets the periodic inspection data.
	 * 
	 * @return the periodic inspection data
	 */
	public Set<PeriodicInspectionData> getPeriodicInspectionData() {

		return periodicInspectionData;
	}

	/**
	 * Sets the periodic inspection data.
	 * 
	 * @param periodicInspectionData
	 *            the new periodic inspection data
	 */
	public void setPeriodicInspectionData(Set<PeriodicInspectionData> periodicInspectionData) {

		this.periodicInspectionData = periodicInspectionData;
	}

	/**
	 * Gets the periodic inspection symptoms.
	 * 
	 * @return the periodic inspection symptoms
	 */
	public Set<PeriodicInspectionSymptom> getPeriodicInspectionSymptoms() {

		return periodicInspectionSymptoms;
	}

	/**
	 * Sets the periodic inspection symptoms.
	 * 
	 * @param periodicInspectionSymptoms
	 *            the new periodic inspection symptoms
	 */
	public void setPeriodicInspectionSymptoms(Set<PeriodicInspectionSymptom> periodicInspectionSymptoms) {

		this.periodicInspectionSymptoms = periodicInspectionSymptoms;
	}

	/**
	 * Gets the created dt.
	 * 
	 * @return the created dt
	 */
	public Date getCreatedDT() {

		return createdDT;
	}

	/**
	 * Sets the created dt.
	 * 
	 * @param createdDT
	 *            the new created dt
	 */
	public void setCreatedDT(Date createdDT) {

		this.createdDT = createdDT;
	}

	/**
	 * Gets the created user name.
	 * 
	 * @return the created user name
	 */
	public String getCreatedUserName() {

		return createdUserName;
	}

	/**
	 * Sets the created user name.
	 * 
	 * @param createdUserName
	 *            the new created user name
	 */
	public void setCreatedUserName(String createdUserName) {

		this.createdUserName = createdUserName;
	}

	/**
	 * Gets the last updated dt.
	 * 
	 * @return the last updated dt
	 */
	public Date getLastUpdatedDT() {

		return lastUpdatedDT;
	}

	/**
	 * Sets the last updated dt.
	 * 
	 * @param lastUpdatedDT
	 *            the new last updated dt
	 */
	public void setLastUpdatedDT(Date lastUpdatedDT) {

		this.lastUpdatedDT = lastUpdatedDT;
	}

	/**
	 * Gets the last updated user name.
	 * 
	 * @return the last updated user name
	 */
	public String getLastUpdatedUserName() {

		return lastUpdatedUserName;
	}

	/**
	 * Sets the last updated user name.
	 * 
	 * @param lastUpdatedUserName
	 *            the new last updated user name
	 */
	public void setLastUpdatedUserName(String lastUpdatedUserName) {

		this.lastUpdatedUserName = lastUpdatedUserName;
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
	 * Gets the audio true.
	 * 
	 * @return the audio true
	 */
	public String getAudioTrue() {

		return audioTrue;
	}

	/**
	 * Sets the audio true.
	 * 
	 * @param audioTrue
	 *            the new audio true
	 */
	public void setAudioTrue(String audioTrue) {

		this.audioTrue = audioTrue;
	}

	/**
	 * Gets the activities after pre visit set.
	 * 
	 * @return the activities after pre visit set
	 */
	public Set<PeriodicInspectionData> getActivitiesAfterPreVisitSet() {

		return activitiesAfterPreVisitSet;
	}

	/**
	 * Sets the activities after pre visit set.
	 * 
	 * @param activitiesAfterPreVisitSet
	 *            the new activities after pre visit set
	 */
	public void setActivitiesAfterPreVisitSet(Set<PeriodicInspectionData> activitiesAfterPreVisitSet) {

		this.activitiesAfterPreVisitSet = activitiesAfterPreVisitSet;
	}

	/**
	 * Gets the inteploughing with set.
	 * 
	 * @return the inteploughing with set
	 */
	public Set<PeriodicInspectionData> getInteploughingWithSet() {

		return inteploughingWithSet;
	}

	/**
	 * Sets the inteploughing with set.
	 * 
	 * @param inteploughingWithSet
	 *            the new inteploughing with set
	 */
	public void setInteploughingWithSet(Set<PeriodicInspectionData> inteploughingWithSet) {

		this.inteploughingWithSet = inteploughingWithSet;
	}

	/**
	 * Gets the type of manure set.
	 * 
	 * @return the type of manure set
	 */
	public Set<PeriodicInspectionData> getTypeOfManureSet() {

		return typeOfManureSet;
	}

	/**
	 * Sets the type of manure set.
	 * 
	 * @param typeOfManureSet
	 *            the new type of manure set
	 */
	public void setTypeOfManureSet(Set<PeriodicInspectionData> typeOfManureSet) {

		this.typeOfManureSet = typeOfManureSet;
	}

	/**
	 * Gets the fertilizer applied set.
	 * 
	 * @return the fertilizer applied set
	 */
	public Set<PeriodicInspectionData> getFertilizerAppliedSet() {

		return fertilizerAppliedSet;
	}

	/**
	 * Sets the fertilizer applied set.
	 * 
	 * @param fertilizerAppliedSet
	 *            the new fertilizer applied set
	 */
	public void setFertilizerAppliedSet(Set<PeriodicInspectionData> fertilizerAppliedSet) {

		this.fertilizerAppliedSet = fertilizerAppliedSet;
	}

	/**
	 * Gets the name of pest set.
	 * 
	 * @return the name of pest set
	 */
	public Set<PeriodicInspectionData> getNameOfPestSet() {

		return nameOfPestSet;
	}

	/**
	 * Sets the name of pest set.
	 * 
	 * @param nameOfPestSet
	 *            the new name of pest set
	 */
	public void setNameOfPestSet(Set<PeriodicInspectionData> nameOfPestSet) {

		this.nameOfPestSet = nameOfPestSet;
	}

	/**
	 * Gets the pesticide recommented set.
	 * 
	 * @return the pesticide recommented set
	 */
	public Set<PeriodicInspectionData> getPesticideRecommentedSet() {

		return pesticideRecommentedSet;
	}

	/**
	 * Sets the pesticide recommented set.
	 * 
	 * @param pesticideRecommentedSet
	 *            the new pesticide recommented set
	 */
	public void setPesticideRecommentedSet(Set<PeriodicInspectionData> pesticideRecommentedSet) {

		this.pesticideRecommentedSet = pesticideRecommentedSet;
	}

	/**
	 * Gets the name of disease set.
	 * 
	 * @return the name of disease set
	 */
	public Set<PeriodicInspectionData> getNameOfDiseaseSet() {

		return nameOfDiseaseSet;
	}

	/**
	 * Sets the name of disease set.
	 * 
	 * @param nameOfDiseaseSet
	 *            the new name of disease set
	 */
	public void setNameOfDiseaseSet(Set<PeriodicInspectionData> nameOfDiseaseSet) {

		this.nameOfDiseaseSet = nameOfDiseaseSet;
	}

	/**
	 * Gets the fungicide recommented set.
	 * 
	 * @return the fungicide recommented set
	 */
	public Set<PeriodicInspectionData> getFungicideRecommentedSet() {

		return fungicideRecommentedSet;
	}

	/**
	 * Sets the fungicide recommented set.
	 * 
	 * @param fungicideRecommentedSet
	 *            the new fungicide recommented set
	 */
	public void setFungicideRecommentedSet(Set<PeriodicInspectionData> fungicideRecommentedSet) {

		this.fungicideRecommentedSet = fungicideRecommentedSet;
	}

	/**
	 * Gets the pest symptoms set.
	 * 
	 * @return the pest symptoms set
	 */
	public Set<PeriodicInspectionSymptom> getPestSymptomsSet() {

		return pestSymptomsSet;
	}

	/**
	 * Sets the pest symptoms set.
	 * 
	 * @param pestSymptomsSet
	 *            the new pest symptoms set
	 */
	public void setPestSymptomsSet(Set<PeriodicInspectionSymptom> pestSymptomsSet) {

		this.pestSymptomsSet = pestSymptomsSet;
	}

	/**
	 * Gets the disease symptoms set.
	 * 
	 * @return the disease symptoms set
	 */
	public Set<PeriodicInspectionSymptom> getDiseaseSymptomsSet() {

		return diseaseSymptomsSet;
	}

	/**
	 * Sets the disease symptoms set.
	 * 
	 * @param diseaseSymptomsSet
	 *            the new disease symptoms set
	 */
	public void setDiseaseSymptomsSet(Set<PeriodicInspectionSymptom> diseaseSymptomsSet) {

		this.diseaseSymptomsSet = diseaseSymptomsSet;
	}

	public String getTxnId() {

		return txnId;
	}

	public void setTxnId(String txnId) {

		this.txnId = txnId;
	}

	public ESETxn geteSETxn() {

		return eSETxn;
	}

	public void seteSETxn(ESETxn eSETxn) {

		this.eSETxn = eSETxn;
	}

	public String getNoOfPlantsReplanned() {

		return noOfPlantsReplanned;
	}

	public void setNoOfPlantsReplanned(String noOfPlantsReplanned) {

		this.noOfPlantsReplanned = noOfPlantsReplanned;
	}

	public Date getGapPlantingDate() {

		return gapPlantingDate;
	}

	public void setGapPlantingDate(Date gapPlantingDate) {

		this.gapPlantingDate = gapPlantingDate;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getPestSymptomsName() {
		return pestSymptomsName;
	}

	public void setPestSymptomsName(String pestSymptomsName) {
		this.pestSymptomsName = pestSymptomsName;
	}

	public String getDiseaseSymptomsName() {
		return diseaseSymptomsName;
	}

	public void setDiseaseSymptomsName(String diseaseSymptomsName) {
		this.diseaseSymptomsName = diseaseSymptomsName;
	}

	public String getCropRotation() {

		return cropRotation;
	}

	public void setCropRotation(String cropRotation) {

		this.cropRotation = cropRotation;
	}

	public String getTemperature() {

		return temperature;
	}

	public void setTemperature(String temperature) {

		this.temperature = temperature;
	}

	public String getHumidity() {

		return humidity;
	}

	public void setHumidity(String humidity) {

		this.humidity = humidity;
	}

	public String getRain() {

		return rain;
	}

	public void setRain(String rain) {

		this.rain = rain;
	}

	public String getWindSpeed() {

		return windSpeed;
	}

	public void setWindSpeed(String windSpeed) {

		this.windSpeed = windSpeed;
	}

	public String getSeasonCode() {

		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {

		this.seasonCode = seasonCode;
	}

	public String getIsDeleted() {

		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {

		this.isDeleted = isDeleted;
	}

	public String getLandpreparationCompleted() {

		return landpreparationCompleted;
	}

	public void setLandpreparationCompleted(String landpreparationCompleted) {

		this.landpreparationCompleted = landpreparationCompleted;
	}

	public String getChemicalSpray() {

		return chemicalSpray;
	}

	public void setChemicalSpray(String chemicalSpray) {

		this.chemicalSpray = chemicalSpray;
	}

	public String getMonoOrImida() {

		return monoOrImida;
	}

	public void setMonoOrImida(String monoOrImida) {

		this.monoOrImida = monoOrImida;
	}

	public String getSingleSprayOrCocktail() {

		return singleSprayOrCocktail;
	}

	public void setSingleSprayOrCocktail(String singleSprayOrCocktail) {

		this.singleSprayOrCocktail = singleSprayOrCocktail;
	}

	public String getRepetitionOfPest() {

		return repetitionOfPest;
	}

	public void setRepetitionOfPest(String repetitionOfPest) {

		this.repetitionOfPest = repetitionOfPest;
	}

	public String getNitrogenousFert() {

		return nitrogenousFert;
	}

	public void setNitrogenousFert(String nitrogenousFert) {

		this.nitrogenousFert = nitrogenousFert;
	}

	public String getCropSpacingLastYear() {

		return cropSpacingLastYear;
	}

	public void setCropSpacingLastYear(String cropSpacingLastYear) {

		this.cropSpacingLastYear = cropSpacingLastYear;
	}

	public String getCropSpacingCurrentYear() {

		return cropSpacingCurrentYear;
	}

	public void setCropSpacingCurrentYear(String cropSpacingCurrentYear) {

		this.cropSpacingCurrentYear = cropSpacingCurrentYear;
	}

	public String getWeeding() {

		return weeding;
	}

	public void setWeeding(String weeding) {

		this.weeding = weeding;
	}

	public String getPicking() {

		return picking;
	}

	public void setPicking(String picking) {

		this.picking = picking;
	}

	public byte[] getSoilPhoto() {

		return soilPhoto;
	}

	public void setSoilPhoto(byte[] soilPhoto) {

		this.soilPhoto = soilPhoto;
	}

	/**
	 * Builds the multiple records.
	 */

	public void buildMultipleRecords() {

		if (!ObjectUtil.isListEmpty(periodicInspectionData)) {
			for (PeriodicInspectionData periodicInspectionDataObj : periodicInspectionData) {
				if ("ACAPV".equals(periodicInspectionDataObj.getType())) {
					activitiesAfterPreVisitSet.add(periodicInspectionDataObj);
				} else if ("INTPLUG".equals(periodicInspectionDataObj.getType())) {
					inteploughingWithSet.add(periodicInspectionDataObj);
				} else if ("MATYP".equals(periodicInspectionDataObj.getType())) {
					typeOfManureSet.add(periodicInspectionDataObj);
				} else if ("FRTATYP".equals(periodicInspectionDataObj.getType())) {
					fertilizerAppliedSet.add(periodicInspectionDataObj);
				} else if ("PEST".equals(periodicInspectionDataObj.getType())) {
					nameOfPestSet.add(periodicInspectionDataObj);
				} else if ("PESTREC".equals(periodicInspectionDataObj.getType())) {
					pesticideRecommentedSet.add(periodicInspectionDataObj);
				} else if ("DISEASE".equals(periodicInspectionDataObj.getType())) {
					nameOfDiseaseSet.add(periodicInspectionDataObj);
				} else if ("FUNGISREC".equals(periodicInspectionDataObj.getType())) {
					fungicideRecommentedSet.add(periodicInspectionDataObj);
				}
			}
		}

		if (!ObjectUtil.isListEmpty(periodicInspectionSymptoms)) {
			for (PeriodicInspectionSymptom periodicInspectionSymptom : periodicInspectionSymptoms) {
				if (String.valueOf(Symptoms.PEST).equals(periodicInspectionSymptom.getType())) {
					pestSymptomsSet.add(periodicInspectionSymptom);
				} else if (String.valueOf(Symptoms.DISEASE).equals(periodicInspectionSymptom.getType())) {
					diseaseSymptomsSet.add(periodicInspectionSymptom);
				}
			}
		}

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

    public String getTxnType() {
    
        return txnType;
    }

    public void setTxnType(String txnType) {
    
        this.txnType = txnType;
    }

    public List<String> getAgentList() {
    
        return agentList;
    }

    public void setAgentList(List<String> agentList) {
    
        this.agentList = agentList;
    }

	public String getCropCode() {
		return cropCode;
	}

	public void setCropCode(String cropCode) {
		this.cropCode = cropCode;
	}

	public String getCropProtectionPractice() {
		return cropProtectionPractice;
	}

	public void setCropProtectionPractice(String cropProtectionPractice) {
		this.cropProtectionPractice = cropProtectionPractice;
	}
    
    

	
}
