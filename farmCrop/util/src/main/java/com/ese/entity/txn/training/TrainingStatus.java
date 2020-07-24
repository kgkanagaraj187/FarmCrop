/*
 * TrainingStatus.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.entity.txn.training;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.Village;

public class TrainingStatus {

	private long id;
	private Date trainingDate;
	private String receiptNo;
	private Season season;
	private Village village;
	private Warehouse learningGroup;
	private FarmerTraining farmerTraining;
	private Set<Topic> topics;
	private int farmerAttended;
	private String remarks;
	private TransferInfo transferInfo;
	private Set<TrainingStatusLocation> trainingStatusLocations;
	private String branchId;
	private String farmerIds;
	private Set<Village> villages;
	private Set<TrainingMaterial> trainingMaterials;
	private Set<TrainingMethod> trainingMethods;
	private Set<Observations> observations;
	private String trainingAssistName;
	private String timeTakenForTraining;
	private String trainingCode;

	private Set<TopicCategory> topicCategory;

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
	 *            the id to set
	 */
	public void setId(long id) {

		this.id = id;
	}

	/**
	 * Gets the training date.
	 * 
	 * @return the trainingDate
	 */
	public Date getTrainingDate() {

		return trainingDate;
	}

	/**
	 * Sets the training date.
	 * 
	 * @param trainingDate
	 *            the trainingDate to set
	 */
	public void setTrainingDate(Date trainingDate) {

		this.trainingDate = trainingDate;
	}

	/**
	 * Gets the receipt no.
	 * 
	 * @return the receiptNo
	 */
	public String getReceiptNo() {

		return receiptNo;
	}

	/**
	 * Sets the receipt no.
	 * 
	 * @param receiptNo
	 *            the receiptNo to set
	 */
	public void setReceiptNo(String receiptNo) {

		this.receiptNo = receiptNo;
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
	 *            the season to set
	 */
	public void setSeason(Season season) {

		this.season = season;
	}

	/**
	 * Gets the village.
	 * 
	 * @return the village
	 */
	public Village getVillage() {

		return village;
	}

	/**
	 * Sets the village.
	 * 
	 * @param village
	 *            the village to set
	 */
	public void setVillage(Village village) {

		this.village = village;
	}

	/**
	 * Gets the learning group.
	 * 
	 * @return the learningGroup
	 */
	public Warehouse getLearningGroup() {

		return learningGroup;
	}

	/**
	 * Sets the learning group.
	 * 
	 * @param learningGroup
	 *            the learningGroup to set
	 */
	public void setLearningGroup(Warehouse learningGroup) {

		this.learningGroup = learningGroup;
	}

	/**
	 * Gets the farmer training.
	 * 
	 * @return the farmerTraining
	 */
	public FarmerTraining getFarmerTraining() {

		return farmerTraining;
	}

	/**
	 * Sets the farmer training.
	 * 
	 * @param farmerTraining
	 *            the farmerTraining to set
	 */
	public void setFarmerTraining(FarmerTraining farmerTraining) {

		this.farmerTraining = farmerTraining;
	}

	/**
	 * Gets the topics.
	 * 
	 * @return the topics
	 */
	public Set<Topic> getTopics() {

		return topics;
	}

	/**
	 * Sets the topics.
	 * 
	 * @param topics
	 *            the topics to set
	 */
	public void setTopics(Set<Topic> topics) {

		this.topics = topics;
	}

	/**
	 * Gets the farmer attended.
	 * 
	 * @return the farmerAttended
	 */
	public int getFarmerAttended() {

		return farmerAttended;
	}

	/**
	 * Sets the farmer attended.
	 * 
	 * @param farmerAttended
	 *            the farmerAttended to set
	 */
	public void setFarmerAttended(int farmerAttended) {

		this.farmerAttended = farmerAttended;
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
	 *            the remarks to set
	 */
	public void setRemarks(String remarks) {

		this.remarks = remarks;
	}

	/**
	 * Gets the transfer info.
	 * 
	 * @return the transferInfo
	 */
	public TransferInfo getTransferInfo() {

		return transferInfo;
	}

	/**
	 * Sets the transfer info.
	 * 
	 * @param transferInfo
	 *            the transferInfo to set
	 */
	public void setTransferInfo(TransferInfo transferInfo) {

		this.transferInfo = transferInfo;
	}

	/**
	 * Gets the training status locations.
	 * 
	 * @return the training status locations
	 */
	public Set<TrainingStatusLocation> getTrainingStatusLocations() {

		return trainingStatusLocations;
	}

	/**
	 * Sets the training status locations.
	 * 
	 * @param trainingStatusLocations
	 *            the new training status locations
	 */
	public void setTrainingStatusLocations(Set<TrainingStatusLocation> trainingStatusLocations) {

		this.trainingStatusLocations = trainingStatusLocations;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public String getFarmerIds() {
		return farmerIds;
	}

	public void setFarmerIds(String farmerIds) {
		this.farmerIds = farmerIds;
	}

	public Set<Village> getVillages() {
		return villages;
	}

	public void setVillages(Set<Village> villages) {
		this.villages = villages;
	}

	public Set<TrainingMaterial> getTrainingMaterials() {
		return trainingMaterials;
	}

	public void setTrainingMaterials(Set<TrainingMaterial> trainingMaterials) {
		this.trainingMaterials = trainingMaterials;
	}

	public Set<TrainingMethod> getTrainingMethods() {
		return trainingMethods;
	}

	public void setTrainingMethods(Set<TrainingMethod> trainingMethods) {
		this.trainingMethods = trainingMethods;
	}

	public Set<Observations> getObservations() {
		return observations;
	}

	public void setObservations(Set<Observations> observations) {
		this.observations = observations;
	}

	public String getTrainingAssistName() {
		return trainingAssistName;
	}

	public void setTrainingAssistName(String trainingAssistName) {
		this.trainingAssistName = trainingAssistName;
	}

	public String getTimeTakenForTraining() {
		return timeTakenForTraining;
	}

	public void setTimeTakenForTraining(String timeTakenForTraining) {
		this.timeTakenForTraining = timeTakenForTraining;
	}

	public String getTrainingCode() {
		return trainingCode;
	}

	public void setTrainingCode(String trainingCode) {
		this.trainingCode = trainingCode;
	}

	public Set<TopicCategory> getTopicCategory() {
		return topicCategory;
	}

	public void setTopicCategory(Set<TopicCategory> topicCategory) {
		this.topicCategory = topicCategory;
	}

}
