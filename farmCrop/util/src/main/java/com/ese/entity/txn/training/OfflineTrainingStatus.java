/*
 * OfflineTrainingStatus.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.entity.txn.training;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class OfflineTrainingStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String trainingDate;
	private String receiptNo;
	private String seasonCode;
	private String villageCode;
	private String learningGroupCode;
	private String farmerTrainingCode;
	private String topics;
	private String farmerAttended;
	private String remarks;
	private String agentId;
	private String servicePointId;
	private String deviceId;
	private int statusCode;
	private String statusMessage;
	private Set<TrainingStatusLocation> trainingStatusLocations;
	private String branchId;
	private String trainingCode;
	private String criteriaCodes;
	private String trainingMaterials;
	private String trainingMethods;
	private String observations;
	private String farmerIds;
	
	private String trainingAssistName;
	private String timeTakenForTraining;
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
	 * Gets the training date.
	 * 
	 * @return the training date
	 */
	public String getTrainingDate() {

		return trainingDate;
	}

	/**
	 * Sets the training date.
	 * 
	 * @param trainingDate
	 *            the new training date
	 */
	public void setTrainingDate(String trainingDate) {

		this.trainingDate = trainingDate;
	}

	/**
	 * Gets the receipt no.
	 * 
	 * @return the receipt no
	 */
	public String getReceiptNo() {

		return receiptNo;
	}

	/**
	 * Sets the receipt no.
	 * 
	 * @param receiptNo
	 *            the new receipt no
	 */
	public void setReceiptNo(String receiptNo) {

		this.receiptNo = receiptNo;
	}

	/**
	 * Gets the season code.
	 * 
	 * @return the season code
	 */
	public String getSeasonCode() {

		return seasonCode;
	}

	/**
	 * Sets the season code.
	 * 
	 * @param seasonCode
	 *            the new season code
	 */
	public void setSeasonCode(String seasonCode) {

		this.seasonCode = seasonCode;
	}

	/**
	 * Gets the village code.
	 * 
	 * @return the village code
	 */
	public String getVillageCode() {

		return villageCode;
	}

	/**
	 * Sets the village code.
	 * 
	 * @param villageCode
	 *            the new village code
	 */
	public void setVillageCode(String villageCode) {

		this.villageCode = villageCode;
	}

	/**
	 * Gets the learning group code.
	 * 
	 * @return the learning group code
	 */
	public String getLearningGroupCode() {

		return learningGroupCode;
	}

	/**
	 * Sets the learning group code.
	 * 
	 * @param learningGroupCode
	 *            the new learning group code
	 */
	public void setLearningGroupCode(String learningGroupCode) {

		this.learningGroupCode = learningGroupCode;
	}

	/**
	 * Sets the farmer training code.
	 * 
	 * @param farmerTrainingCode
	 *            the new farmer training code
	 */
	public void setFarmerTrainingCode(String farmerTrainingCode) {

		this.farmerTrainingCode = farmerTrainingCode;
	}

	/**
	 * Gets the farmer training code.
	 * 
	 * @return the farmer training code
	 */
	public String getFarmerTrainingCode() {

		return farmerTrainingCode;
	}

	/**
	 * Sets the topics.
	 * 
	 * @param topics
	 *            the new topics
	 */
	public void setTopics(String topics) {

		this.topics = topics;
	}

	/**
	 * Gets the topics.
	 * 
	 * @return the topics
	 */
	public String getTopics() {

		return topics;
	}

	/**
	 * Gets the farmer attended.
	 * 
	 * @return the farmer attended
	 */
	public String getFarmerAttended() {

		return farmerAttended;
	}

	/**
	 * Sets the farmer attended.
	 * 
	 * @param farmerAttended
	 *            the new farmer attended
	 */
	public void setFarmerAttended(String farmerAttended) {

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
	 *            the new remarks
	 */
	public void setRemarks(String remarks) {

		this.remarks = remarks;
	}

	/**
	 * Gets the agent id.
	 * 
	 * @return the agent id
	 */
	public String getAgentId() {

		return agentId;
	}

	/**
	 * Sets the agent id.
	 * 
	 * @param agentId
	 *            the new agent id
	 */
	public void setAgentId(String agentId) {

		this.agentId = agentId;
	}

	/**
	 * Gets the service point id.
	 * 
	 * @return the service point id
	 */
	public String getServicePointId() {

		return servicePointId;
	}

	/**
	 * Sets the service point id.
	 * 
	 * @param servicePointId
	 *            the new service point id
	 */
	public void setServicePointId(String servicePointId) {

		this.servicePointId = servicePointId;
	}

	/**
	 * Gets the device id.
	 * 
	 * @return the device id
	 */
	public String getDeviceId() {

		return deviceId;
	}

	/**
	 * Sets the device id.
	 * 
	 * @param deviceId
	 *            the new device id
	 */
	public void setDeviceId(String deviceId) {

		this.deviceId = deviceId;
	}

	/**
	 * Gets the status code.
	 * 
	 * @return the status code
	 */
	public int getStatusCode() {

		return statusCode;
	}

	/**
	 * Sets the status code.
	 * 
	 * @param statusCode
	 *            the new status code
	 */
	public void setStatusCode(int statusCode) {

		this.statusCode = statusCode;
	}

	/**
	 * Gets the status message.
	 * 
	 * @return the status message
	 */
	public String getStatusMessage() {

		return statusMessage;
	}

	/**
	 * Sets the status message.
	 * 
	 * @param statusMessage
	 *            the new status message
	 */
	public void setStatusMessage(String statusMessage) {

		this.statusMessage = statusMessage;
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

	public String getTrainingCode() {
		return trainingCode;
	}

	public void setTrainingCode(String trainingCode) {
		this.trainingCode = trainingCode;
	}

	public String getCriteriaCodes() {
		return criteriaCodes;
	}

	public void setCriteriaCodes(String criteriaCodes) {
		this.criteriaCodes = criteriaCodes;
	}

	public String getTrainingMaterials() {
		return trainingMaterials;
	}

	public void setTrainingMaterials(String trainingMaterials) {
		this.trainingMaterials = trainingMaterials;
	}

	public String getTrainingMethods() {
		return trainingMethods;
	}

	public void setTrainingMethods(String trainingMethods) {
		this.trainingMethods = trainingMethods;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public String getFarmerIds() {
		return farmerIds;
	}

	public void setFarmerIds(String farmerIds) {
		this.farmerIds = farmerIds;
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

}
