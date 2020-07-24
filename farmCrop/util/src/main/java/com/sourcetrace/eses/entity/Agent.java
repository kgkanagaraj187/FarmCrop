/*
 * Agent.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

import com.ese.entity.txn.training.FarmerTraining;
import com.sourcetrace.eses.inspect.agrocert.AgentSurveyMapping;
import com.sourcetrace.eses.inspect.agrocert.SurveyMaster;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestionMapping;
import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.Second;
import com.sourcetrace.esesw.entity.profile.ServiceLocation;

@GroupSequence({ Agent.class, First.class, Second.class })
public class Agent extends Profile {

	private String password;
	private Date passwordUDT;
	private AgentType agentType;
	private Set<ServiceLocation> serviceLocations;
	private int txnMode;
	private int bodStatus;
	private Set<Warehouse> wareHouses;
	private boolean trainingExists;
	private Set<FarmerTraining>trainingTemplates;
	
	// transient variable
	private String confirmPassword;
	private String farmerCurrentIdSeq;
	private String farmerAllotedIdSeq;
	private String farmerAllotedResIdSeq;
	private String shopDealerCurrentIdSeq;
	private String shopDealerAllotedIdSeq;
	private String shopDealerAllotedResIdSeq;
	private String farmerCardIdSequence;
	private String shopDealerCardIdSequence;
	private String internalIdSequence;
	private String receiptNumber;
	private String orderNoSeq;
	private String deliveryNoSeq;
	private String farmerAccountNoSequence;
	private String type;
	private String searchPage;
	private double accountBalance;
	private String accountRupee;
	private String accountPaise;
	private String selectedtrainings;
	private String language;
	private String version;
	private SortedSet<AgentSurveyMapping> surveys;
	
	private List<String> surveyCodes;
	private List<String> availSurveyCodes;

	/**
* Transient variable
*/
private List<String> branchesList;
private String command;
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */

	public String getPassword() {

		return password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {

		this.password = password;
	}

	/**
	 * Gets the password udt.
	 * 
	 * @return the password udt
	 */
	public Date getPasswordUDT() {

		return passwordUDT;
	}

	/**
	 * Sets the password udt.
	 * 
	 * @param passwordUDT
	 *            the new password udt
	 */
	public void setPasswordUDT(Date passwordUDT) {

		this.passwordUDT = passwordUDT;
	}

	/**
	 * Gets the agent type.
	 * 
	 * @return the agent type
	 */
	public AgentType getAgentType() {

		return agentType;
	}

	/**
	 * Sets the agent type.
	 * 
	 * @param agentType
	 *            the new agent type
	 */
	public void setAgentType(AgentType agentType) {

		this.agentType = agentType;
	}

	/**
	 * Gets the confirm password.
	 * 
	 * @return the confirm password
	 */
	public String getConfirmPassword() {

		return confirmPassword;
	}

	/**
	 * Sets the confirm password.
	 * 
	 * @param confirmPassword
	 *            the new confirm password
	 */
	public void setConfirmPassword(String confirmPassword) {

		this.confirmPassword = confirmPassword;
	}

	/**
	 * Sets the service locations.
	 * 
	 * @param serviceLocations
	 *            the new service locations
	 */
	public void setServiceLocations(Set<ServiceLocation> serviceLocations) {

		this.serviceLocations = serviceLocations;
	}

	/**
	 * Gets the service locations.
	 * 
	 * @return the service locations
	 */
	public Set<ServiceLocation> getServiceLocations() {

		return serviceLocations;
	}

	/**
	 * Sets the bod status.
	 * 
	 * @param bodStatus
	 *            the new bod status
	 */
	public void setBodStatus(int bodStatus) {

		this.bodStatus = bodStatus;
	}

	/**
	 * Gets the bod status.
	 * 
	 * @return the bod status
	 */
	public int getBodStatus() {

		return bodStatus;
	}

	/**
	 * Sets the txn mode.
	 * 
	 * @param txnMode
	 *            the new txn mode
	 */
	public void setTxnMode(int txnMode) {

		this.txnMode = txnMode;
	}

	/**
	 * Gets the txn mode.
	 * 
	 * @return the txn mode
	 */
	@NotNull(message = "empty.txnMode")
	public int getTxnMode() {

		return txnMode;
	}

	/**
	 * Gets the ware houses.
	 * 
	 * @return the ware houses
	 */
	public Set<Warehouse> getWareHouses() {

		return wareHouses;
	}

	/**
	 * Sets the ware houses.
	 * 
	 * @param wareHouses
	 *            the new ware houses
	 */
	public void setWareHouses(Set<Warehouse> wareHouses) {

		this.wareHouses = wareHouses;
	}

	/**
	 * Gets the farmer current id seq.
	 * 
	 * @return the farmer current id seq
	 */
	public String getFarmerCurrentIdSeq() {

		return farmerCurrentIdSeq;
	}

	/**
	 * Sets the farmer current id seq.
	 * 
	 * @param farmerCurrentIdSeq
	 *            the new farmer current id seq
	 */
	public void setFarmerCurrentIdSeq(String farmerCurrentIdSeq) {

		this.farmerCurrentIdSeq = farmerCurrentIdSeq;
	}

	/**
	 * Gets the farmer alloted id seq.
	 * 
	 * @return the farmer alloted id seq
	 */
	public String getFarmerAllotedIdSeq() {

		return farmerAllotedIdSeq;
	}

	/**
	 * Sets the farmer alloted id seq.
	 * 
	 * @param farmerAllotedIdSeq
	 *            the new farmer alloted id seq
	 */
	public void setFarmerAllotedIdSeq(String farmerAllotedIdSeq) {

		this.farmerAllotedIdSeq = farmerAllotedIdSeq;
	}

	/**
	 * Gets the farmer alloted res id seq.
	 * 
	 * @return the farmer alloted res id seq
	 */
	public String getFarmerAllotedResIdSeq() {

		return farmerAllotedResIdSeq;
	}

	/**
	 * Sets the farmer alloted res id seq.
	 * 
	 * @param farmerAllotedResIdSeq
	 *            the new farmer alloted res id seq
	 */
	public void setFarmerAllotedResIdSeq(String farmerAllotedResIdSeq) {

		this.farmerAllotedResIdSeq = farmerAllotedResIdSeq;
	}

	/**
	 * Gets the shop dealer current id seq.
	 * 
	 * @return the shop dealer current id seq
	 */
	public String getShopDealerCurrentIdSeq() {

		return shopDealerCurrentIdSeq;
	}

	/**
	 * Sets the shop dealer current id seq.
	 * 
	 * @param shopDealerCurrentIdSeq
	 *            the new shop dealer current id seq
	 */
	public void setShopDealerCurrentIdSeq(String shopDealerCurrentIdSeq) {

		this.shopDealerCurrentIdSeq = shopDealerCurrentIdSeq;
	}

	/**
	 * Gets the shop dealer alloted id seq.
	 * 
	 * @return the shop dealer alloted id seq
	 */
	public String getShopDealerAllotedIdSeq() {

		return shopDealerAllotedIdSeq;
	}

	/**
	 * Sets the shop dealer alloted id seq.
	 * 
	 * @param shopDealerAllotedIdSeq
	 *            the new shop dealer alloted id seq
	 */
	public void setShopDealerAllotedIdSeq(String shopDealerAllotedIdSeq) {

		this.shopDealerAllotedIdSeq = shopDealerAllotedIdSeq;
	}

	/**
	 * Gets the shop dealer alloted res id seq.
	 * 
	 * @return the shop dealer alloted res id seq
	 */
	public String getShopDealerAllotedResIdSeq() {

		return shopDealerAllotedResIdSeq;
	}

	/**
	 * Sets the shop dealer alloted res id seq.
	 * 
	 * @param shopDealerAllotedResIdSeq
	 *            the new shop dealer alloted res id seq
	 */
	public void setShopDealerAllotedResIdSeq(String shopDealerAllotedResIdSeq) {

		this.shopDealerAllotedResIdSeq = shopDealerAllotedResIdSeq;
	}

	/**
	 * Gets the farmer card id sequence.
	 * 
	 * @return the farmer card id sequence
	 */
	public String getFarmerCardIdSequence() {

		return farmerCardIdSequence;
	}

	/**
	 * Sets the farmer card id sequence.
	 * 
	 * @param farmerCardIdSequence
	 *            the new farmer card id sequence
	 */
	public void setFarmerCardIdSequence(String farmerCardIdSequence) {

		this.farmerCardIdSequence = farmerCardIdSequence;
	}

	/**
	 * Gets the shop dealer card id sequence.
	 * 
	 * @return the shop dealer card id sequence
	 */
	public String getShopDealerCardIdSequence() {

		return shopDealerCardIdSequence;
	}

	/**
	 * Sets the shop dealer card id sequence.
	 * 
	 * @param shopDealerCardIdSequence
	 *            the new shop dealer card id sequence
	 */
	public void setShopDealerCardIdSequence(String shopDealerCardIdSequence) {

		this.shopDealerCardIdSequence = shopDealerCardIdSequence;
	}

	/**
	 * Gets the internal id sequence.
	 * 
	 * @return the internal id sequence
	 */
	public String getInternalIdSequence() {

		return internalIdSequence;
	}

	/**
	 * Sets the internal id sequence.
	 * 
	 * @param internalIdSequence
	 *            the new internal id sequence
	 */
	public void setInternalIdSequence(String internalIdSequence) {

		this.internalIdSequence = internalIdSequence;
	}

	/**
	 * Sets the receipt number.
	 * 
	 * @param receiptNumber
	 *            the new receipt number
	 */
	public void setReceiptNumber(String receiptNumber) {

		this.receiptNumber = receiptNumber;
	}

	/**
	 * Gets the receipt number.
	 * 
	 * @return the receipt number
	 */
	public String getReceiptNumber() {

		return receiptNumber;
	}

	/**
	 * Sets the order no seq.
	 * 
	 * @param orderNoSeq
	 *            the new order no seq
	 */
	public void setOrderNoSeq(String orderNoSeq) {

		this.orderNoSeq = orderNoSeq;
	}

	/**
	 * Gets the order no seq.
	 * 
	 * @return the order no seq
	 */
	public String getOrderNoSeq() {

		return orderNoSeq;
	}

	/**
	 * Sets the delivery no seq.
	 * 
	 * @param deliveryNoSeq
	 *            the new delivery no seq
	 */
	public void setDeliveryNoSeq(String deliveryNoSeq) {

		this.deliveryNoSeq = deliveryNoSeq;
	}

	/**
	 * Gets the delivery no seq.
	 * 
	 * @return the delivery no seq
	 */
	public String getDeliveryNoSeq() {

		return deliveryNoSeq;
	}

	/**
	 * Sets the farmer account no sequence.
	 * 
	 * @param farmerAccountNoSequence
	 *            the new farmer account no sequence
	 */
	public void setFarmerAccountNoSequence(String farmerAccountNoSequence) {

		this.farmerAccountNoSequence = farmerAccountNoSequence;
	}

	/**
	 * Gets the farmer account no sequence.
	 * 
	 * @return the farmer account no sequence
	 */
	public String getFarmerAccountNoSequence() {

		return farmerAccountNoSequence;
	}

	/**
	 * Gets the cooperative.
	 * 
	 * @return the cooperative
	 */
	public Warehouse getCooperative() {

		if (!ObjectUtil.isListEmpty(wareHouses)
				&& !ObjectUtil.isEmpty(agentType)) {
			if (AgentType.COOPERATIVE_MANAGER.equals(agentType.getCode()))
				return wareHouses.iterator().next();
			else
				return wareHouses.iterator().next().getRefCooperative();
		}
		return null;
	}

	/**
	 * Checks if is co operative manager.
	 * 
	 * @return true, if is co operative manager
	 */
	public boolean isCoOperativeManager() {

		if (!ObjectUtil.isEmpty(agentType)) {
			if (AgentType.COOPERATIVE_MANAGER.equalsIgnoreCase(agentType
					.getCode())) {
				return true;
			}
		}
		return false;
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

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {

		return type;
	}

	/**
	 * Gets the search page.
	 * 
	 * @return the search page
	 */
	public String getSearchPage() {

		return searchPage;
	}

	/**
	 * Sets the search page.
	 * 
	 * @param searchPage
	 *            the new search page
	 */
	public void setSearchPage(String searchPage) {

		this.searchPage = searchPage;
	}

	public double getAccountBalance() {

		/*if (this.getAccountRupee().equals(null)
				|| StringUtil.isEmpty(this.getAccountRupee())) {
			setAccountRupee("0");
		}
		if (this.getAccountPaise().equals(null)
				|| StringUtil.isEmpty(this.getAccountPaise())) {
			setAccountPaise("00");
		}

		return (Double.valueOf(this.getAccountRupee() + "."
				+ this.getAccountPaise()));*/
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {

		this.accountBalance = accountBalance;
	}

	public String getAccountRupee() {
		return accountRupee;
	}

	public void setAccountRupee(String accountRupee) {
		this.accountRupee = accountRupee;
	}

	public String getAccountPaise() {
		return accountPaise;
	}

	public void setAccountPaise(String accountPaise) {
		this.accountPaise = accountPaise;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public boolean isTrainingExists() {
		return trainingExists;
	}

	public void setTrainingExists(boolean trainingExists) {
		this.trainingExists = trainingExists;
	}

	public String getSelectedtrainings() {
		return selectedtrainings;
	}

	public void setSelectedtrainings(String selectedtrainings) {
		this.selectedtrainings = selectedtrainings;
	}

	public Set<FarmerTraining> getTrainingTemplates() {
		return trainingTemplates;
	}

	public void setTrainingTemplates(Set<FarmerTraining> trainingTemplates) {
		this.trainingTemplates = trainingTemplates;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	

	public SortedSet<AgentSurveyMapping> getSurveys() {
		return surveys;
	}

	public void setSurveys(SortedSet<AgentSurveyMapping> surveys) {
		this.surveys = surveys;
	}

	public List<String> getSurveyCodes() {
		return surveyCodes;
	}

	public void setSurveyCodes(List<String> surveyCodes) {
		this.surveyCodes = surveyCodes;
	}

	public List<String> getAvailSurveyCodes() {
		return availSurveyCodes;
	}

	public void setAvailSurveyCodes(List<String> availSurveyCodes) {
		this.availSurveyCodes = availSurveyCodes;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	
}
