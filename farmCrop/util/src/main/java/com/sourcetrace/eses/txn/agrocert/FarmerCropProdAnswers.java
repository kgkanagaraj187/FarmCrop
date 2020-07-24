/*
 * FarmerCropProdAnswers.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.agrocert;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import com.sourcetrace.esesw.entity.profile.Season;

/**
 * The Class FarmerCropProdAnswers.
 */
public class FarmerCropProdAnswers {

	private long id;
	private Date answeredDate;
	private String farmerId;
	private String farmId;
	private String categoryCode;
	private String categoryName;
	private SortedSet<FarmersSectionAnswers> farmersSectionAnswers;
	private Set<InspectionStandard> inspectionStandards;
	private Season season;
	private String branchId;
	private double acresOwned;
	private double acresFarmed;
	private double acresOrganic;
	private double acresTransition;
	private double acresEligibleNext;
	private double acresReqIns;
	private int certificationStatus;
	   private String farmerName;
	    private String farmerCode;
	    private String farmName;
	    private String farmerTraceId;
	    private String farmerTraceNet;
	    private String farmCode;
	    private String latitude;
	    private String longitude;


	/**
	 * Transient variable
	 */
	private List<String> branchesList;
	
    private List<FarmersSectionAnswers> farmersSectionAnswersList;

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
	 * Gets the answered date.
	 * 
	 * @return the answered date
	 */
	public Date getAnsweredDate() {

		return answeredDate;
	}

	/**
	 * Sets the answered date.
	 * 
	 * @param answeredDate
	 *            the new answered date
	 */
	public void setAnsweredDate(Date answeredDate) {

		this.answeredDate = answeredDate;
	}

	/**
	 * Gets the farmer id.
	 * 
	 * @return the farmer id
	 */
	public String getFarmerId() {

		return farmerId;
	}

	/**
	 * Sets the farmer id.
	 * 
	 * @param farmerId
	 *            the new farmer id
	 */
	public void setFarmerId(String farmerId) {

		this.farmerId = farmerId;
	}

	/**
	 * @param farmId
	 *            the farmId to set
	 */
	public void setFarmId(String farmId) {

		this.farmId = farmId;
	}

	/**
	 * @return the farmId
	 */
	public String getFarmId() {

		return farmId;
	}

	/**
	 * Gets the category code.
	 * 
	 * @return the category code
	 */
	public String getCategoryCode() {

		return categoryCode;
	}

	/**
	 * Sets the category code.
	 * 
	 * @param categoryCode
	 *            the new category code
	 */
	public void setCategoryCode(String categoryCode) {

		this.categoryCode = categoryCode;
	}

	/**
	 * Gets the category name.
	 * 
	 * @return the category name
	 */
	public String getCategoryName() {

		return categoryName;
	}

	/**
	 * Sets the category name.
	 * 
	 * @param categoryName
	 *            the new category name
	 */
	public void setCategoryName(String categoryName) {

		this.categoryName = categoryName;
	}

	/**
	 * Gets the farmers section answers.
	 * 
	 * @return the farmers section answers
	 */
	public SortedSet<FarmersSectionAnswers> getFarmersSectionAnswers() {

		return farmersSectionAnswers;
	}

	/**
	 * Sets the farmers section answers.
	 * 
	 * @param farmersSectionAnswers
	 *            the new farmers section answers
	 */
	public void setFarmersSectionAnswers(SortedSet<FarmersSectionAnswers> farmersSectionAnswers) {

		this.farmersSectionAnswers = farmersSectionAnswers;
	}

	/**
	 * Gets the inspection standards.
	 * 
	 * @return the inspection standards
	 */
	public Set<InspectionStandard> getInspectionStandards() {

		return inspectionStandards;
	}

	/**
	 * Sets the inspection standards.
	 * 
	 * @param inspectionStandards
	 *            the new inspection standards
	 */
	public void setInspectionStandards(Set<InspectionStandard> inspectionStandards) {

		this.inspectionStandards = inspectionStandards;
	}

	/**
	 * Gets the season.
	 * 
	 * @return the season
	 */
	public Season getSeason() {

		return season;
	}

	/**
	 * Sets the season.
	 * 
	 * @param season
	 *            the new season
	 */
	public void setSeason(Season season) {

		this.season = season;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public double getAcresOwned() {
		return acresOwned;
	}

	public void setAcresOwned(double acresOwned) {
		this.acresOwned = acresOwned;
	}

	public double getAcresFarmed() {
		return acresFarmed;
	}

	public void setAcresFarmed(double acresFarmed) {
		this.acresFarmed = acresFarmed;
	}

	public double getAcresOrganic() {
		return acresOrganic;
	}

	public void setAcresOrganic(double acresOrganic) {
		this.acresOrganic = acresOrganic;
	}

	public double getAcresTransition() {
		return acresTransition;
	}

	public void setAcresTransition(double acresTransition) {
		this.acresTransition = acresTransition;
	}

	public double getAcresEligibleNext() {
		return acresEligibleNext;
	}

	public void setAcresEligibleNext(double acresEligibleNext) {
		this.acresEligibleNext = acresEligibleNext;
	}

	public double getAcresReqIns() {
		return acresReqIns;
	}

	public void setAcresReqIns(double acresReqIns) {
		this.acresReqIns = acresReqIns;
	}

	public int getCertificationStatus() {
		return certificationStatus;
	}

	public void setCertificationStatus(int certificationStatus) {
		this.certificationStatus = certificationStatus;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public List<FarmersSectionAnswers> getFarmersSectionAnswersList() {
		return farmersSectionAnswersList;
	}

	public void setFarmersSectionAnswersList(List<FarmersSectionAnswers> farmersSectionAnswersList) {
		this.farmersSectionAnswersList = farmersSectionAnswersList;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public String getFarmerCode() {
		return farmerCode;
	}

	public void setFarmerCode(String farmerCode) {
		this.farmerCode = farmerCode;
	}

	public String getFarmName() {
		return farmName;
	}

	public void setFarmName(String farmName) {
		this.farmName = farmName;
	}

	public String getFarmerTraceId() {
		return farmerTraceId;
	}

	public void setFarmerTraceId(String farmerTraceId) {
		this.farmerTraceId = farmerTraceId;
	}

	public String getFarmerTraceNet() {
		return farmerTraceNet;
	}

	public void setFarmerTraceNet(String farmerTraceNet) {
		this.farmerTraceNet = farmerTraceNet;
	}

	public String getFarmCode() {
		return farmCode;
	}

	public void setFarmCode(String farmCode) {
		this.farmCode = farmCode;
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
