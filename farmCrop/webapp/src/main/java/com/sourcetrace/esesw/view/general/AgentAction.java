/*
 * AgentAction.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.util.ESESystem;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICardService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IMailService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IReportService;
import com.sourcetrace.eses.service.IServiceLocationService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.service.IUserService;
import com.sourcetrace.eses.umgmt.entity.ContactInfo;
import com.sourcetrace.eses.umgmt.entity.IdentityType;
import com.sourcetrace.eses.umgmt.entity.PersonalInfo;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.Base64Util;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.MailUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.crypto.ICryptoUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.ESECard;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ServiceLocation;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.view.ProcessStockTransferImpl;
import com.sourcetrace.esesw.view.SwitchValidatorAction;
import com.sourcetrace.util.QRCodeGenerator;

/**
 * The Class AgentAction.
 */
@SuppressWarnings("serial")
public class AgentAction extends SwitchValidatorAction {

	private static final Logger logger = Logger.getLogger(AgentAction.class);
	protected static final String WAREHOUSE_MAPPING = "warehouseMapping";

	private String selectedStatus;
	private String id;
	private String profileId;
	private String dateOfBirth;
	private String selectedLocality;
	private String selectedMunicipality;
	private String selectedIdentity;
	private String postalCode;
	private String selectedCountry;
	private String selectedState;
	private String selectedCity;
	private String selectedServicePoint;
	private String selectedSex;
	private String servicePointName;
	private String serviceLocationName = "";
	private String agentImageByteString;
	private String requestFor;
	private String warehouseNames = "";
	private String qrid;
	private String loginUserName;
	private String type;
	private String selectedCooperative;
	private String cooperativeName;
	private String warehouseCooperative;
	private boolean pendingMTNTExists = false;
	private String selectedProcurementCenter;
	private String selectedGinningCenter;
	private String selectedSpinner;
	private Agent agent;
	private ESEAccount account;
	private ESECard card;
	private ProcessStockTransferImpl processStockTransfer;
	private InputStream fileInputStream;
	DateFormat df = new SimpleDateFormat(getESEDateFormat());
	private String selectedAgentType;
	private Set<ServiceLocation> selectedLocation;
	private List<Agent> agents;
	private List<String> availableWarehouse = new ArrayList<String>();
	private List<String> selectedWarehouse = new ArrayList<String>();
	List<Locality> localities = new ArrayList<Locality>();
	List<State> stats = new ArrayList<State>();
	List<Municipality> cities = new ArrayList<Municipality>();
	List<String> availableServiceLocation = new ArrayList<String>();
	List<String> selectedServiceLocation = new ArrayList<String>();
	List<String> selectedName = new ArrayList<String>();
	private List<String> availableName = new ArrayList<String>();

	Map<String, String> genderType = new LinkedHashMap<String, String>();
	Map<Integer, String> cardRewriteList = new LinkedHashMap<Integer, String>();
	Map<Integer, String> accountStatusList = new LinkedHashMap<Integer, String>();
	Map<Integer, String> cardStatusList = new LinkedHashMap<Integer, String>();
	Map<Integer, String> txnMode = new LinkedHashMap<Integer, String>();
	Map<Integer, String> statuses = new LinkedHashMap<Integer, String>();
	Map<Integer, String> bodStatus = new LinkedHashMap<Integer, String>();
	Map<String, String> identityTypeList = new LinkedHashMap<String, String>();

	private IAgentService agentService;
	private IAccountService accountService;
	private ICardService cardService;
	private IUniqueIDGenerator idGenerator;
	private ILocationService locationService;
	private IReportService reportService;
	private IUserService userService;
	private IServicePointService servicePointService;
	private IServiceLocationService serviceLocationService;
	private IMailService mailService;
	private IPreferencesService preferncesService;
	private IProductDistributionService productDistributionService;
	private ICryptoUtil cryptoUtil;
	private String loginUserRole;
	private String selectedtrainings;
	private String warehouseName;
	private File agentImage;

	@Autowired
	private ITrainingService trainingService;
	/**
	 * Gets the gender type.
	 * 
	 * @return the gender type
	 */
	public Map<String, String> getGenderType() {

		return genderType;
	}

	/**
	 * Sets the gender type.
	 * 
	 * @param genderType
	 *            the gender type
	 */
	public void setGenderType(Map<String, String> genderType) {

		this.genderType = genderType;
	}

	/**
	 * Sets the identity type list.
	 * 
	 * @param identityTypeList
	 *            the identity type list
	 */
	public void setIdentityTypeList(Map<String, String> identityTypeList) {

		this.identityTypeList = identityTypeList;
	}

	/**
	 * Gets the txn mode.
	 * 
	 * @return the txn mode
	 */
	public Map<Integer, String> getTxnMode() {

		return txnMode;
	}

	/**
	 * Sets the statuses.
	 * 
	 * @param statuses
	 *            the statuses
	 */
	public void setStatuses(Map<Integer, String> statuses) {

		this.statuses = statuses;
	}

	/**
	 * Gets the card status list.
	 * 
	 * @return the card status list
	 */
	public Map<Integer, String> getCardStatusList() {

		return cardStatusList;
	}

	/**
	 * Sets the txn mode.
	 * 
	 * @param txnMode
	 *            the txn mode
	 */
	public void setTxnMode(Map<Integer, String> txnMode) {

		this.txnMode = txnMode;
	}

	/**
	 * Gets the service location service.
	 * 
	 * @return the service location service
	 */
	public IServiceLocationService getServiceLocationService() {

		return serviceLocationService;
	}

	/**
	 * Sets the service location service.
	 * 
	 * @param serviceLocationService
	 *            the new service location service
	 */
	public void setServiceLocationService(IServiceLocationService serviceLocationService) {

		this.serviceLocationService = serviceLocationService;
	}

	/**
	 * Gets the selected location.
	 * 
	 * @return the selected location
	 */
	public Set<ServiceLocation> getSelectedLocation() {

		return selectedLocation;
	}

	/**
	 * Sets the selected location.
	 * 
	 * @param selectedLocation
	 *            the new selected location
	 */
	public void setSelectedLocation(Set<ServiceLocation> selectedLocation) {

		this.selectedLocation = selectedLocation;
	}

	/**
	 * Gets the selected name.
	 * 
	 * @return the selected name
	 */
	public List<String> getSelectedName() {

		return selectedName;
	}

	/**
	 * Sets the selected name.
	 * 
	 * @param selectedName
	 *            the new selected name
	 */
	public void setSelectedName(List<String> selectedName) {

		this.selectedName = selectedName;
	}

	/**
	 * Gets the available service location.
	 * 
	 * @return the available service location
	 */
	public List<String> getAvailableServiceLocation() {

		return availableServiceLocation;
	}

	/**
	 * Sets the available service location.
	 * 
	 * @param availableServiceLocation
	 *            the new available service location
	 */
	public void setAvailableServiceLocation(List<String> availableServiceLocation) {

		this.availableServiceLocation = availableServiceLocation;
	}

	/**
	 * Gets the selected service location.
	 * 
	 * @return the selected service location
	 */
	public List<String> getSelectedServiceLocation() {

		return selectedServiceLocation;
	}

	/**
	 * Sets the selected service location.
	 * 
	 * @param selectedServiceLocation
	 *            the new selected service location
	 */
	public void setSelectedServiceLocation(List<String> selectedServiceLocation) {

		this.selectedServiceLocation = selectedServiceLocation;
	}

	/**
	 * Gets the agent.
	 * 
	 * @return the agent
	 */

	public Agent getAgent() {

		return agent;
	}

	/**
	 * Sets the agent.
	 * 
	 * @param agent
	 *            the new agent
	 */
	public void setAgent(Agent agent) {

		this.agent = agent;
	}

	/**
	 * Gets the account.
	 * 
	 * @return the account
	 */
	public ESEAccount getAccount() {

		return account;
	}

	/**
	 * Gets the account status list.
	 * 
	 * @return the account status list
	 */
	public Map<Integer, String> getAccountStatusList() {

		return accountStatusList;
	}

	/**
	 * Sets the card status list.
	 * 
	 * @param cardStatusList
	 *            the card status list
	 */
	public void setCardStatusList(Map<Integer, String> cardStatusList) {

		this.cardStatusList = cardStatusList;
	}

	/**
	 * Sets the account.
	 * 
	 * @param account
	 *            the new account
	 */
	public void setAccount(ESEAccount account) {

		this.account = account;
	}

	/**
	 * Gets the card.
	 * 
	 * @return the card
	 */
	public ESECard getCard() {

		return card;
	}

	/**
	 * Sets the service location name.
	 * 
	 * @param serviceLocationName
	 *            the new service location name
	 */
	public void setServiceLocationName(String serviceLocationName) {

		this.serviceLocationName = serviceLocationName;
	}

	/**
	 * Gets the service location name.
	 * 
	 * @return the service location name
	 */
	public String getServiceLocationName() {

		return serviceLocationName;
	}

	/**
	 * Sets the account service.
	 * 
	 * @param accountService
	 *            the new account service
	 */
	public void setAccountService(IAccountService accountService) {

		this.accountService = accountService;
	}

	/**
	 * Sets the service point name.
	 * 
	 * @param servicePointName
	 *            the new service point name
	 */
	public void setServicePointName(String servicePointName) {

		this.servicePointName = servicePointName;
	}

	/**
	 * Gets the service point name.
	 * 
	 * @return the service point name
	 */
	public String getServicePointName() {

		return servicePointName;
	}

	/**
	 * Gets the profile id.
	 * 
	 * @return the profile id
	 */
	public String getProfileId() {

		return profileId;
	}

	/**
	 * Sets the profile id.
	 * 
	 * @param profileId
	 *            the new profile id
	 */
	public void setProfileId(String profileId) {

		this.profileId = profileId;
	}

	/**
	 * Gets the date of birth.
	 * 
	 * @return the date of birth
	 */
	public String getDateOfBirth() {

		return dateOfBirth;
	}

	/**
	 * Sets the date of birth.
	 * 
	 * @param dateOfBirth
	 *            the new date of birth
	 */
	public void setDateOfBirth(String dateOfBirth) {

		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Gets the selected locality.
	 * 
	 * @return the selected locality
	 */
	public String getSelectedLocality() {

		return selectedLocality;
	}

	/**
	 * Sets the selected locality.
	 * 
	 * @param selectedLocality
	 *            the new selected locality
	 */
	public void setSelectedLocality(String selectedLocality) {

		this.selectedLocality = selectedLocality;
	}

	/**
	 * Gets the selected municipality.
	 * 
	 * @return the selected municipality
	 */
	public String getSelectedMunicipality() {

		return selectedMunicipality;
	}

	/**
	 * Sets the selected municipality.
	 * 
	 * @param selectedMunicipality
	 *            the new selected municipality
	 */
	public void setSelectedMunicipality(String selectedMunicipality) {

		this.selectedMunicipality = selectedMunicipality;
	}

	/**
	 * Gets the postal code.
	 * 
	 * @return the postal code
	 */
	public String getPostalCode() {

		return postalCode;
	}

	/**
	 * Sets the postal code.
	 * 
	 * @param postalCode
	 *            the new postal code
	 */
	public void setPostalCode(String postalCode) {

		this.postalCode = postalCode;
	}

	/**
	 * Gets the df.
	 * 
	 * @return the df
	 */
	public DateFormat getDf() {

		return df;
	}

	/**
	 * Sets the df.
	 * 
	 * @param df
	 *            the new df
	 */
	public void setDf(DateFormat df) {

		this.df = df;
	}

	/**
	 * Gets the agent service.
	 * 
	 * @return the agent service
	 */
	public IAgentService getAgentService() {

		return agentService;
	}

	/**
	 * Sets the account status list.
	 * 
	 * @param accountStatusList
	 *            the account status list
	 */
	public void setAccountStatusList(Map<Integer, String> accountStatusList) {

		this.accountStatusList = accountStatusList;
	}

	/**
	 * Sets the agent service.
	 * 
	 * @param agentService
	 *            the new agent service
	 */
	public void setAgentService(IAgentService agentService) {

		this.agentService = agentService;
	}

	/**
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
	}

	/**
	 * Sets the location service.
	 * 
	 * @param locationService
	 *            the new location service
	 */
	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {

		this.id = id;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {

		return id;
	}

	/**
	 * Sets the selected country.
	 * 
	 * @param selectedCountry
	 *            the new selected country
	 */
	public void setSelectedCountry(String selectedCountry) {

		this.selectedCountry = selectedCountry;
	}

	/**
	 * Gets the selected country.
	 * 
	 * @return the selected country
	 */
	public String getSelectedCountry() {

		return selectedCountry;
	}

	/**
	 * Sets the selected state.
	 * 
	 * @param selectedState
	 *            the new selected state
	 */
	public void setSelectedState(String selectedState) {

		this.selectedState = selectedState;
	}

	/**
	 * Gets the selected state.
	 * 
	 * @return the selected state
	 */
	public String getSelectedState() {

		return selectedState;
	}

	/**
	 * Sets the selected city.
	 * 
	 * @param selectedCity
	 *            the new selected city
	 */
	public void setSelectedCity(String selectedCity) {

		this.selectedCity = selectedCity;
	}

	/**
	 * Gets the selected city.
	 * 
	 * @return the selected city
	 */
	public String getSelectedCity() {

		return selectedCity;
	}

	/**
	 * Gets the report service.
	 * 
	 * @return the report service
	 */
	public IReportService getReportService() {

		return reportService;
	}

	/**
	 * Sets the report service.
	 * 
	 * @param reportService
	 *            the report service
	 * @see com.sourcetrace.esesw.view.SwitchAction#setReportService(com.ese.service.report.IReportService)
	 */
	@Override
	public void setReportService(IReportService reportService) {

		this.reportService = reportService;
	}

	/**
	 * Gets the service point service.
	 * 
	 * @return the service point service
	 */

	/**
	 * Gets the service point service.
	 * 
	 * @return the service point service
	 */
	public IServicePointService getServicePointService() {

		return servicePointService;
	}

	/**
	 * Sets the service point service.
	 * 
	 * @param servicePointService
	 *            the new service point service
	 */
	public void setServicePointService(IServicePointService servicePointService) {

		this.servicePointService = servicePointService;
	}

	/**
	 * Sets the product distribution service.
	 * 
	 * @param productDistributionService
	 *            the new product distribution service
	 */
	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	/**
	 * Gets the product distribution service.
	 * 
	 * @return the product distribution service
	 */
	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	/**
	 * Gets the selected sex.
	 * 
	 * @return the selected sex
	 */
	public String getSelectedSex() {

		return selectedSex;
	}

	/**
	 * Sets the selected sex.
	 * 
	 * @param selectedSex
	 *            the new selected sex
	 */
	public void setSelectedSex(String selectedSex) {

		this.selectedSex = selectedSex;
	}

	/**
	 * Sets the selected status.
	 * 
	 * @param selectedStatus
	 *            the new selected status
	 */
	public void setSelectedStatus(String selectedStatus) {

		this.selectedStatus = selectedStatus;
	}

	/**
	 * Gets the selected status.
	 * 
	 * @return the selected status
	 */
	public String getSelectedStatus() {

		return selectedStatus;
	}

	/**
	 * Sets the card.
	 * 
	 * @param card
	 *            the new card
	 */
	public void setCard(ESECard card) {

		this.card = card;
	}

	/**
	 * Gets the account service.
	 * 
	 * @return the account service
	 */
	public IAccountService getAccountService() {

		return accountService;
	}

	/**
	 * Sets the card service.
	 * 
	 * @param cardService
	 *            the new card service
	 */
	public void setCardService(ICardService cardService) {

		this.cardService = cardService;
	}

	/**
	 * Gets the card service.
	 * 
	 * @return the card service
	 */
	public ICardService getCardService() {

		return cardService;
	}

	/**
	 * Sets the id generator.
	 * 
	 * @param idGenerator
	 *            the new id generator
	 */
	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
	}

	/**
	 * Gets the id generator.
	 * 
	 * @return the id generator
	 */
	public IUniqueIDGenerator getIdGenerator() {

		return idGenerator;
	}

	/**
	 * Gets the card rewrite list.
	 * 
	 * @return the card rewrite list
	 */
	public Map<Integer, String> getCardRewriteList() {

		return cardRewriteList;
	}

	/**
	 * Sets the card rewrite list.
	 * 
	 * @param cardRewriteList
	 *            the card rewrite list
	 */
	public void setCardRewriteList(Map<Integer, String> cardRewriteList) {

		this.cardRewriteList = cardRewriteList;
	}

	/**
	 * Gets the identity type list.
	 * 
	 * @return the identity type list
	 */
	public Map<String, String> getIdentityTypeList() {

		return identityTypeList;
	}

	/**
	 * Gets the selected identity.
	 * 
	 * @return the selected identity
	 */
	public String getSelectedIdentity() {

		return selectedIdentity;
	}

	/**
	 * Sets the selected identity.
	 * 
	 * @param selectedIdentity
	 *            the new selected identity
	 */
	public void setSelectedIdentity(String selectedIdentity) {

		this.selectedIdentity = selectedIdentity;
	}

	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	public IUserService getUserService() {

		return userService;
	}

	/**
	 * Sets the user service.
	 * 
	 * @param userService
	 *            the user service
	 * @see com.sourcetrace.esesw.view.SwitchAction#setUserService(com.ese.service.user.IUserService)
	 */
	@Override
	public void setUserService(IUserService userService) {

		this.userService = userService;
	}

	/**
	 * Sets the selected service point.
	 * 
	 * @param selectedServicePoint
	 *            the new selected service point
	 */
	public void setSelectedServicePoint(String selectedServicePoint) {

		this.selectedServicePoint = selectedServicePoint;
	}

	/**
	 * Gets the selected service point.
	 * 
	 * @return the selected service point
	 */
	public String getSelectedServicePoint() {

		return selectedServicePoint;
	}

	/**
	 * Gets the mail service.
	 * 
	 * @return the mail service
	 */
	public IMailService getMailService() {

		return mailService;
	}

	/**
	 * Sets the mail service.
	 * 
	 * @param mailService
	 *            the new mail service
	 */
	public void setMailService(IMailService mailService) {

		this.mailService = mailService;
	}

	/**
	 * Gets the prefernces service.
	 * 
	 * @return the prefernces service
	 */
	public IPreferencesService getPreferncesService() {

		return preferncesService;
	}

	/**
	 * Sets the prefernces service.
	 * 
	 * @param preferncesService
	 *            the new prefernces service
	 */
	public void setPreferncesService(IPreferencesService preferncesService) {

		this.preferncesService = preferncesService;
	}

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {

		// To get Card Rewritable Status
		cardRewriteList.put(ESECard.IS_REWRITABLE_NO, getText("cardRewrite0"));
		cardRewriteList.put(ESECard.IS_REWRITABLE_YES, getText("cardRewrite1"));

		// To get Account Status List
		accountStatusList.put(ESEAccount.ACTIVE, getText("account1"));
		accountStatusList.put(ESEAccount.INACTIVE, getText("account0"));

		// To get Card Status List
		cardStatusList.put(ESECard.ACTIVE, getText("card1"));
		cardStatusList.put(ESECard.INACTIVE, getText("card0"));

		// To get Txn Mode
		txnMode.put(ESETxn.ONLINE_MODE, getText("mode1"));
		txnMode.put(ESETxn.OFFLINE_MODE, getText("mode2"));
		txnMode.put(ESETxn.BOTH, getText("mode3"));

		// To get Agent Status
		statuses.put(Profile.ACTIVE, getText("agent1"));
		statuses.put(Profile.INACTIVE, getText("agent0"));

		// To get BOD Status
		bodStatus.put(Profile.ONLINE, getText("bodStatus1"));
		bodStatus.put(Profile.OFFLINE, getText("bodStatus0"));

		// To get Identity type List
		List<IdentityType> listIdentityType = userService.listIdentityType();
		if (!ObjectUtil.isListEmpty(listIdentityType)) {
			for (IdentityType identityType : listIdentityType) {
				identityTypeList.put(identityType.getCode(), getText(identityType.getName()));
			}
		}

		// To get Agent Gender
		genderType.put(PersonalInfo.SEX_MALE, getText("Male"));
		genderType.put(PersonalInfo.SEX_FEMALE, getText("Female"));

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		if (agent != null) {
			try {
				if (!StringUtil.isEmpty(dateOfBirth)) {
					agent.getPersonalInfo().setDateOfBirth(df.parse(dateOfBirth));
				} else {
					agent.getPersonalInfo().setDateOfBirth(null);
				}
				// To set the values for selected country,state and district
				/*
				 * Country country = new Country();
				 * country.setName(getSelectedCountry()); State state = new
				 * State(); state.setName(getSelectedState());
				 * state.setCountry(country); Locality locality = new
				 * Locality(); locality.setName(getSelectedLocality());
				 * locality.setState(state);
				 */

				/* agent.getContactInfo().getCity().setLocality(locality); */
				agent.setTxnMode(agent.getTxnMode());
			    agent.setCommand(getCommand());
				if (!ObjectUtil.isEmpty(agent.getAgentType()) && agent.getAgentType().getId() > 0 && !StringUtil.isEmpty(selectedAgentType)) {
					agent.setAgentType(agentService.findAgentTypeById(Long.valueOf(selectedAgentType)));
				}
				if(getCurrentTenantId().equalsIgnoreCase("chetna") || getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
					agent.setProfileType(!ObjectUtil.isEmpty(agent.getAgentType()) && !StringUtil.isEmpty(agent.getAgentType().getCode()) ? agent.getAgentType().getCode() :"01");
				}else{
					agent.setProfileType("01");
				}
				if ("cooperativeManager".equalsIgnoreCase(type)) {
					if (!StringUtil.isEmpty(selectedCooperative)) {
						Warehouse warehouse = locationService.findWarehouseByName(selectedCooperative);
						Set<Warehouse> coopWarehouses = new HashSet<Warehouse>();
						coopWarehouses.add(warehouse);
						agent.setWareHouses(coopWarehouses);
					}
				} else {
					Set<Warehouse> wareHouses = new HashSet<Warehouse>();
					
					if(getCurrentTenantId().equalsIgnoreCase("chetna")|| getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
						if(!ObjectUtil.isEmpty(agent.getAgentType()) &&  !StringUtil.isEmpty(agent.getAgentType().getCode())
								&& agent.getAgentType().getCode().equalsIgnoreCase("02")){
							
							for (String warehouseName : getSelectedName()) {
								Warehouse warehouse = locationService.findSamithiByName(warehouseName);
								if (!ObjectUtil.isEmpty(warehouse))
									wareHouses.add(warehouse);
							}
							agent.setWareHouses(wareHouses);
							
							agent.setProcurementCenter(null);
						}else if(!ObjectUtil.isEmpty(agent.getAgentType()) &&  !StringUtil.isEmpty(agent.getAgentType().getCode())
								&& agent.getAgentType().getCode().equalsIgnoreCase("03")){
							
							for (String warehouseName : getSelectedName()) {
								Warehouse warehouse = locationService.findSamithiByName(warehouseName);
								if (!ObjectUtil.isEmpty(warehouse))
									wareHouses.add(warehouse);
							}
							agent.setWareHouses(wareHouses);
							if(!StringUtil.isEmpty(selectedProcurementCenter)){
								Warehouse warehouse = locationService.findProcurementWarehouseById(Long.valueOf(selectedProcurementCenter));
								agent.setProcurementCenter(warehouse);
							}
							
						}else if(!ObjectUtil.isEmpty(agent.getAgentType()) && !StringUtil.isEmpty(agent.getAgentType().getCode()) 
								&& agent.getAgentType().getCode().equalsIgnoreCase("04")){
						if(!StringUtil.isEmpty(selectedGinningCenter)){
							Warehouse warehouse = locationService.findProcurementWarehouseById(Long.valueOf(selectedGinningCenter));
							agent.setProcurementCenter(warehouse);
						}
							
						}else if(!ObjectUtil.isEmpty(agent.getAgentType()) && !StringUtil.isEmpty(agent.getAgentType().getCode()) 
								&& agent.getAgentType().getCode().equalsIgnoreCase("05")){
							if(!StringUtil.isEmpty(selectedSpinner)){
								Warehouse warehouse = locationService.findProcurementWarehouseById(Long.valueOf(selectedSpinner));
								agent.setProcurementCenter(warehouse);
							}
						}
					}else if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
						for (String warehouseName : getSelectedName()) {
							Warehouse warehouse = locationService.findSamithiByName(warehouseName);
							if (!ObjectUtil.isEmpty(warehouse))
								wareHouses.add(warehouse);
						}
						agent.setWareHouses(wareHouses);
						if(!StringUtil.isEmpty(warehouseName)){
							if(warehouseName.contains(",")){
							agent.setProcurementCenter(locationService.findWarehouseById(Long.parseLong(warehouseName.split(",")[0].trim())));
							}
							else{
								agent.setProcurementCenter(locationService.findWarehouseById(Long.parseLong(warehouseName)));
							}
						}
					}
					else{
						for (String warehouseName : getSelectedName()) {
							Warehouse warehouse = locationService.findSamithiByName(warehouseName);
							if (!ObjectUtil.isEmpty(warehouse))
								wareHouses.add(warehouse);
						}
						agent.setWareHouses(wareHouses);
						agent.setProcurementCenter(null);
					}
					
					
					
				}

				availableWarehouse = getAvailableName();
				selectedWarehouse = getSelectedName();

			} catch (ParseException e) {
				e.printStackTrace();

			}
		}

		return agent;
	}

	/**
	 * Gets the statuses.
	 * 
	 * @return the statuses
	 */
	public Map<Integer, String> getStatuses() {

		return statuses;
	}

	/**
	 * Data.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	@Override
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		Agent filter = new Agent();

		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
		if (!getIsMultiBranch().equalsIgnoreCase("1")) {
			List<String> branchList = new ArrayList<>();
			branchList.add(searchRecord.get("branchId").trim());
			filter.setBranchesList(branchList);
		} else {
			List<String> branchList = new ArrayList<>();
			List<BranchMaster> branches = clientService.listChildBranchIds(searchRecord.get("branchId").trim());
			branchList.add(searchRecord.get("branchId").trim());
			branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
				branchList.add(branch.getBranchId());
			});
			filter.setBranchesList(branchList);
		}
	}
	
	
	 if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
        filter.setBranchId(searchRecord.get("subBranchId").trim());
    }
	

		if (!StringUtil.isEmpty(searchRecord.get("profileId"))) {
			filter.setProfileId(searchRecord.get("profileId").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("pI.firstName"))
				|| !StringUtil.isEmpty(searchRecord.get("pI.lastName"))) {
			PersonalInfo personalInfo = new PersonalInfo();
			if (!StringUtil.isEmpty(searchRecord.get("pI.firstName")))
				personalInfo.setFirstName(searchRecord.get("pI.firstName").trim());
			if (!StringUtil.isEmpty(searchRecord.get("pI.lastName")))
				personalInfo.setLastName(searchRecord.get("pI.lastName").trim());
			filter.setPersonalInfo(personalInfo);
		}

		filter.setBodStatus(-1);
		if (!StringUtil.isEmpty(searchRecord.get("bodStatus"))) {
			filter.setBodStatus(Integer.parseInt(searchRecord.get("bodStatus")));
		}

		if (!StringUtil.isEmpty(searchRecord.get("cI.mobileNumber"))) {
			ContactInfo contactInfo = new ContactInfo();
			contactInfo.setMobileNumber(searchRecord.get("cI.mobileNumber").trim());
			filter.setContactInfo(contactInfo);
		}

		if (!StringUtil.isEmpty(searchRecord.get("w.name"))) {
			Set<Warehouse> cooperative = new HashSet<Warehouse>();
			Warehouse warehouse = new Warehouse();
			warehouse.setName(searchRecord.get("w.name").trim());
			cooperative.add(warehouse);
			filter.setWareHouses(cooperative);
		}
		if(!getCurrentTenantId().equalsIgnoreCase("chetna") && !getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) && !getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
			AgentType agentType = new AgentType();
			if ("cooperativeManager".equalsIgnoreCase(type))
				agentType.setId(Long.valueOf(1));
			else
				agentType.setId(Long.valueOf(2));
			filter.setAgentType(agentType);
		}
		if (!StringUtil.isEmpty(searchRecord.get("agentType")) && getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
			AgentType agentType = new AgentType();
			agentType.setCode(searchRecord.get("agentType"));
			filter.setAgentType(agentType);
		}
		

		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	/**
	 * To json.
	 * 
	 * @param obj
	 *            the obj
	 * @return the JSON object
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		Agent agent = (Agent) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();

		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(agent.getBranchId())))
						? getBranchesMap().get(getParentBranchMap().get(agent.getBranchId()))
						: getBranchesMap().get(agent.getBranchId()));
			}
			rows.add(getBranchesMap().get(agent.getBranchId()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(agent.getBranchId()));
			}
		}
		rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + agent.getProfileId() + "</font>");
		rows.add(agent.getPersonalInfo().getFirstName());
		rows.add(agent.getPersonalInfo().getLastName());
		rows.add(agent.getContactInfo().getMobileNumber() == null ? "" : agent.getContactInfo().getMobileNumber());
		if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
			if(agent.getCreateTime() != null){
			String txnTime = agent.getCreateTime().toString();
			String res = txnTime.substring(0, 19);
			rows.add(res);
          }
          else{
        	  rows.add("NA");
          }
			// rows.add(agent.getCreateTime());
		rows.add(agent.getVersion());
		if(agent.getUpdateTime() != null){
		String txnTime1 = agent.getUpdateTime().toString();
		String res1 = txnTime1.substring(0, 19);
		rows.add(res1);
		 }
         else{
       	  rows.add("NA");
         }
		rows.add(agent.getAgentType().getName());
		rows.add(agent.getProcurementCenter()!=null && !ObjectUtil.isEmpty(agent.getProcurementCenter())&& agent.getProcurementCenter().getName()!=null && !StringUtil.isEmpty(agent.getProcurementCenter().getName())?agent.getProcurementCenter().getName():"");
		}
		rows.add(getText("bodStatus" + agent.getBodStatus()));
		/*
		 * if (!ObjectUtil.isListEmpty(agent.getWareHouses()) &&
		 * "cooperativeManager".equalsIgnoreCase(type)) { for (Warehouse
		 * warehouse : agent.getWareHouses()) {
		 * rows.add(!ObjectUtil.isEmpty(warehouse) ? warehouse.getName() : "");
		 * } } else rows.add("");
		 */

		jsonObject.put("id", agent.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/**
	 * Creates the.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String create() throws Exception {
		String result = "";
		String cc="";
		String apkUrl="";
		if (agent == null) {
			command = CREATE;
			request.setAttribute(HEADING, getText("Agentcreate.page" + type));
			agent=new Agent();
			agent.setStatus(Profile.ACTIVE);
			return INPUT;
		} else {

			if (!ObjectUtil.isEmpty(agent.getAgentType()) && !StringUtil.isEmpty(selectedAgentType)) {
				AgentType agentType = agentService.findAgentTypeById(Long.valueOf(selectedAgentType));
				if (!ObjectUtil.isEmpty(agentType)) {
					agent.setAgentType(agentType);
				}
			}

			// agent.setProfileId(idGenerator.createAgentId());
			/*
			 * Municipality city =
			 * locationService.findMunicipalityByName(agent.getContactInfo()
			 * .getCity().getName()); agent.getContactInfo().setCity(city);
			 */
			//agent.getAgentType().setId(agent.getAgentType().getId());
			if(getCurrentTenantId().equalsIgnoreCase("chetna") || getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)|| getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
				agent.setProfileType(!ObjectUtil.isEmpty(agent.getAgentType()) && !StringUtil.isEmpty(agent.getAgentType().getCode()) ? agent.getAgentType().getCode() :"01");
			}
			else{
				agent.setProfileType("01");
			}
			
			
			agent.getPersonalInfo().setIdentityType(agent.getPersonalInfo().getIdentityType());
			ServicePoint servicePoint = servicePointService.findServicePointByCode(getText("defaultServicePointCode"));
			agent.setServicePoint(servicePoint);
			if ("cooperativeManager".equalsIgnoreCase(type)) {
				if (!StringUtil.isEmpty(selectedCooperative)) {
					Warehouse warehouse = locationService.findWarehouseByName(selectedCooperative);
					Set<Warehouse> coopWarehouses = new HashSet<Warehouse>();
					coopWarehouses.add(warehouse);
					agent.setWareHouses(coopWarehouses);
				}
			} else {
				Set<Warehouse> wareHouses = new HashSet<Warehouse>();
				
				if(getCurrentTenantId().equalsIgnoreCase("chetna")|| getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
					if(!ObjectUtil.isEmpty(agent.getAgentType()) &&  !StringUtil.isEmpty(agent.getAgentType().getCode())
							&& agent.getAgentType().getCode().equalsIgnoreCase("02")){
						
						for (String warehouseName : getSelectedName()) {
							Warehouse warehouse = locationService.findSamithiByName(warehouseName);
							if (!ObjectUtil.isEmpty(warehouse))
								wareHouses.add(warehouse);
						}
						agent.setWareHouses(wareHouses);
						
						agent.setProcurementCenter(null);
					}else if(!ObjectUtil.isEmpty(agent.getAgentType()) &&  !StringUtil.isEmpty(agent.getAgentType().getCode())
							&& agent.getAgentType().getCode().equalsIgnoreCase("03")){
						
						for (String warehouseName : getSelectedName()) {
							Warehouse warehouse = locationService.findSamithiByName(warehouseName);
							if (!ObjectUtil.isEmpty(warehouse))
								wareHouses.add(warehouse);
						}
						agent.setWareHouses(wareHouses);
						Warehouse warehouse = locationService.findProcurementWarehouseById(Long.valueOf(selectedProcurementCenter));
						agent.setProcurementCenter(warehouse);
						
						
					}else if(!ObjectUtil.isEmpty(agent.getAgentType()) && !StringUtil.isEmpty(agent.getAgentType().getCode()) 
							&& agent.getAgentType().getCode().equalsIgnoreCase("04")){
						Warehouse warehouse = locationService.findProcurementWarehouseById(Long.valueOf(selectedGinningCenter));
						agent.setProcurementCenter(warehouse);
					
						
					}else if(!ObjectUtil.isEmpty(agent.getAgentType()) && !StringUtil.isEmpty(agent.getAgentType().getCode()) 
							&& agent.getAgentType().getCode().equalsIgnoreCase("05")){
						Warehouse warehouse = locationService.findProcurementWarehouseById(Long.valueOf(selectedSpinner));
						agent.setProcurementCenter(warehouse);
					
					}
				}else if(getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
					for (String warehouseName : getSelectedName()) {
						Warehouse warehouse = locationService.findSamithiByName(warehouseName);
						if (!ObjectUtil.isEmpty(warehouse))
							wareHouses.add(warehouse);
					}
					agent.setWareHouses(wareHouses);
					if(!StringUtil.isEmpty(warehouseName)){
						Warehouse warehouse=null;
						if(warehouseName.contains(","))
						 warehouse = locationService.findProcurementWarehouseById(Long.valueOf(warehouseName.split(",")[0].trim()));
						else
							 warehouse = locationService.findProcurementWarehouseById(Long.valueOf(warehouseName));
						agent.setProcurementCenter(warehouse);
					}
					
				}
				else{
					for (String warehouseName : getSelectedName()) {
						Warehouse warehouse = locationService.findSamithiByName(warehouseName);
						if (!ObjectUtil.isEmpty(warehouse))
							wareHouses.add(warehouse);
					}
					agent.setWareHouses(wareHouses);
					agent.setProcurementCenter(null);
				}
				
				
				
			}

			if (Profile.ACTIVE == agent.getStatus()) {
				if (StringUtil.isEmpty(agent.getPassword())) {
					addActionError(getText("empty.password"));
					request.setAttribute(HEADING, getText("Agentcreate.page" + type));
					return INPUT;
				} else {
					String oldPasswordToken = cryptoUtil
							.encrypt(StringUtil.getMulipleOfEight(agent.getProfileId() + agent.getPassword()));
					agent.setPassword(oldPasswordToken);
				}
			}
			agent.setTxnMode(agent.getTxnMode());
			agent.getPersonalInfo().setSex(agent.getPersonalInfo().getSex());
			// agent.setStatus(Profile.INACTIVE);
			agent.setBodStatus(ESETxn.EOD);
			agent.setInternalIdSequence(idGenerator.createAgentInternalIdSequence());
			agent.setBranchId(getBranchId());
			agent.setCreatedDate(new Date());
			agent.setUpdatedDate(new Date());
			agent.setCreatedUser(getUsername());
			agent.setUpdatedUser(getUsername());
			if(this.getAgentImage()!=null){
			agent.getPersonalInfo().setImage(FileUtil.getBinaryFileContent(this.getAgentImage()));
			}
		/*	
			String []idArr=agent.getSelectedtrainings().split(",");
			FarmerTraining farmerTraining;
			Set<FarmerTraining>farmerTrainings=new HashSet<FarmerTraining>();
			if(agent.isTrainingExists()){
			for(String id:idArr){
				farmerTraining=trainingService.findFarmerTrainingById(Long.valueOf(id.trim()));
				farmerTrainings.add(farmerTraining);
				}	
			}
			agent.setTrainingTemplates(farmerTrainings);
			*/
			agentService.createAgent(agent);

			/*if (!StringUtil.isEmpty(agent.getAccountBalance())) {
				agent.setAccountBalance(agent.getAccountBalance());
			} else*/
				agent.setAccountBalance(0.00);

			/** SAVING ACCOUNT INFORMATION **/
			agentService.createAgentESEAccount(agent.getProfileId(), idGenerator.createAgentAccountNoSequence(),
					new Date(), ESEAccount.AGENT_ACCOUNT, agent,getBranchId());

			/** SAVING CARD INFORMATION **/
			agentService.createESECard(agent.getProfileId(), idGenerator.createAgentCardIdSequence(), new Date(),
					ESECard.FARMER_CARD);
			
			try {
				if (!ObjectUtil.isEmpty(agent)) {
					if (!ObjectUtil.isEmpty(agent.getContactInfo())
							&& !StringUtil.isEmpty(agent.getContactInfo().getEmail())) {
						StringBuffer msg = new StringBuffer();
						String password = StringUtil.getRandomNumber();

						BranchMaster dm = clientService.findBranchMasterByBranchId(agent.getBranchId());
						ESESystem preference = preferncesService.findPrefernceById(ESESystem.SYSTEM_ESE);
						if (!ObjectUtil.isEmpty(preference)) {
							apkUrl = preference.getPreferences().get(ESESystem.APK_URL);
							cc = preference.getPreferences().get(ESESystem.CC_EMAIL);
						}
						msg.append("\n\tUser Name\t:\t");
						msg.append(agent.getProfileId());
						msg.append("\n");
						msg.append("\tPassword\t:\t");
						msg.append(password);
						msg.append("\n");
						if (dm != null) {
							msg.append("\tOrganisation\t:\t");
							msg.append(dm.getName());
							msg.append("\n");
						}
						msg.append("\n\tFirst Name\t:\t");
						msg.append(agent.getPersonalInfo().getFirstName());
						msg.append("\n");
						msg.append("\n\tLast Name\t:\t");
						msg.append(agent.getPersonalInfo().getLastName());
						msg.append("\n");
						msg.append("\n\tPlease Download the following file for Mobile Application\t:\t");
						msg.append(apkUrl);
						msg.append("\n");
						
						MailUtil.sendWithCC(agent.getContactInfo().getEmail(), "SourceTrace Web Console Sign in Details",
								agent.getPersonalInfo().getName(), msg.toString(),cc);
						result = getText("successMsg");
					} else {
						result = getText("notExist.email");
					}
				} 
			} catch (Exception e) {
				result = "";
			}
			return REDIRECT;
		}
	}

	/**
	 * Populate municipality.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateMunicipality() throws Exception {

		List<Municipality> municipalities = null;
		if (postalCode != null && !postalCode.equalsIgnoreCase("null")) {
			municipalities = locationService.findMunicipalitiesByPostalCode(postalCode);
		}
		sendResponse(municipalities);
		return null;
	}

	/**
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String detail() throws Exception {

		if (id != null && !id.equals(EMPTY)) {
			agent = agentService.findAgent(Long.valueOf(id));
			card = cardService.findCardByProfileId(agent.getProfileId());
			account = accountService.findAccountByProfileIdAndProfileType(agent.getProfileId(),
					ESEAccount.AGENT_ACCOUNT);
			if (!StringUtil.isEmpty(agent.getPersonalInfo().getDateOfBirth())) {
				setDateOfBirth(df.format(agent.getPersonalInfo().getDateOfBirth()));
			}
			setCurrentPage(getCurrentPage());
			if (agent == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			// getting agent photo
			if (agent.getImageInfo() != null && agent.getImageInfo().getPhoto() != null
					&& agent.getImageInfo().getPhoto().getImage() != null) {
				agentImageByteString = Base64Util.encoder(agent.getImageInfo().getPhoto().getImage());
			}
			if(getCurrentTenantId().equalsIgnoreCase("chetna") || getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)|| getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
				if(!ObjectUtil.isEmpty(agent.getAgentType())){
					setSelectedAgentType(agent.getAgentType().getName());
				}
				if(!ObjectUtil.isEmpty(agent.getProcurementCenter())){
					setSelectedProcurementCenter(agent.getProcurementCenter().getName());
					//agent.setProcurementCenter(agent.getProcurementCenter());
				}
				if(agent.getAgentType().getCode().equalsIgnoreCase("03")){
					setSelectedProcurementCenter(!ObjectUtil.isEmpty(agent)&& !ObjectUtil.isEmpty(agent.getProcurementCenter()) ? agent.getProcurementCenter().getName() : "");
				}else if(agent.getAgentType().getCode().equalsIgnoreCase("04")){
					setSelectedGinningCenter(!ObjectUtil.isEmpty(agent)&& !ObjectUtil.isEmpty(agent.getProcurementCenter()) ? agent.getProcurementCenter().getName() : "");
				}else if(agent.getAgentType().getCode().equalsIgnoreCase("05")){
					setSelectedSpinner(!ObjectUtil.isEmpty(agent)&& !ObjectUtil.isEmpty(agent.getProcurementCenter()) ? agent.getProcurementCenter().getName() : "");
				}
			}
			if(getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID) && !ObjectUtil.isEmpty(agent.getProcurementCenter())){
				setWarehouseName(agent.getProcurementCenter().getName());
			}
	/*		if(agent.isTrainingExists()){
				String trainings="";
				Set<FarmerTraining>farmerTrainings=agent.getTrainingTemplates();
				for(FarmerTraining farmerTraining:farmerTrainings){
					trainings=trainings.concat(!ObjectUtil.isEmpty(farmerTraining.getTrainingTopic())?String.valueOf(farmerTraining.getTrainingTopic().getName()).concat(","):"");
				}
				
					setSelectedtrainings(StringUtil.removeLastComma(trainings.trim()));
				selectedtrainings=StringUtil.removeLastComma(trainings.trim());
				}*/

			if (!ObjectUtil.isListEmpty(agent.getWareHouses())) {
				String sl = "";
				for (Warehouse temp : agent.getWareHouses()) {
					sl = sl + temp.getName() + ", ";
					if (!ObjectUtil.isEmpty(temp.getRefCooperative())) {
						warehouseCooperative = temp.getRefCooperative().getName();
					}
				}
				if (sl != null && sl.length() > 2) {
					warehouseNames = sl.substring(0, sl.length() - 2);
				}
			}
			account = accountService.findAccountByProfileIdAndProfileType(agent.getProfileId(),
					ESEAccount.AGENT_ACCOUNT);
			if (!ObjectUtil.isEmpty(account)) {
				String sufx=account.getCashBalance()!=0?account.getCashBalance()<0?"Dr":"Cr":"";
				String[] agentAcc = String.valueOf(Math.abs(account.getCashBalance())).split("\\.");
				agent.setAccountRupee(agentAcc[0]);
				agent.setAccountPaise(agentAcc[1]+sufx);
			}

			User user = userService.findUserByUserName(getUsername());
			/*
			 * if(!ObjectUtil.isEmpty(user)&&!StringUtil.isEmpty(user.
			 * getIsAdminUser())&&user.
			 * getIsAdminUser().equalsIgnoreCase("true")){
			 * setLoginUserRole("Admin"); }
			 */
			if (!ObjectUtil.isEmpty(user) && !StringUtil.isEmpty(user.getRole())
					&& !StringUtil.isEmpty(user.getRole().getIsAdminUser())
					&& user.getRole().getIsAdminUser().equalsIgnoreCase("true")) {
				setLoginUserRole("Admin");
			}
			setLoginUserName(request.getSession().getAttribute("user").toString());
			command = DETAIL;
			request.setAttribute(HEADING, getText(command + type));

		} else {
			request.setAttribute(HEADING, getText(LIST + type));
			return LIST;
		}
		return command;
	}

	/**
	 * Update.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */

	public String update() throws Exception {

		int tmpAccStat = -1; // temp storage of account status. -1 is initial
								// value.
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		if (id != null && !id.equals("")) {
			agent = agentService.findAgent(Long.valueOf(id));
			card = cardService.findCardByProfileId(agent.getProfileId());
			account = accountService.findAccountByProfileIdAndProfileType(agent.getProfileId(),
					ESEAccount.AGENT_ACCOUNT);
			setCurrentPage(getCurrentPage());
			if (agent == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			// setSelectedCountry(agent.getContactInfo().getCity().getLocality().getState()
			// .getCountry().getName());
			// setSelectedState(agent.getContactInfo().getCity().getLocality().getState().getName());
			// setSelectedLocality(agent.getContactInfo().getCity().getLocality().getName());
			if (agent.getStatus() == 0) {
				setSelectedStatus("0");
			} else {
				setSelectedStatus("1");
			}
			if (!StringUtil.isEmpty(agent.getPersonalInfo().getDateOfBirth())) {
				dateOfBirth = df.format(agent.getPersonalInfo().getDateOfBirth());
			}
			if(getCurrentTenantId().equalsIgnoreCase("chetna") || getCurrentTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)|| getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
				if(!ObjectUtil.isEmpty(agent.getAgentType())){
					setSelectedAgentType(String.valueOf(agent.getAgentType().getId()));
				}
				if(agent.getAgentType().getCode().equalsIgnoreCase("03")){
					setSelectedProcurementCenter(!ObjectUtil.isEmpty(agent)&& !ObjectUtil.isEmpty(agent.getProcurementCenter()) ? String.valueOf(agent.getProcurementCenter().getId()) : "");
				}else if(agent.getAgentType().getCode().equalsIgnoreCase("04")){
					setSelectedGinningCenter(!ObjectUtil.isEmpty(agent)&& !ObjectUtil.isEmpty(agent.getProcurementCenter()) ? String.valueOf(agent.getProcurementCenter().getId()) : "");
				}else if(agent.getAgentType().getCode().equalsIgnoreCase("05")){
					setSelectedSpinner(!ObjectUtil.isEmpty(agent)&& !ObjectUtil.isEmpty(agent.getProcurementCenter()) ? String.valueOf(agent.getProcurementCenter().getId()) : "");
				}
				
			}
			if(getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
				setWarehouseName(!ObjectUtil.isEmpty(agent) && !ObjectUtil.isEmpty(agent.getProcurementCenter()) ?String.valueOf(agent.getProcurementCenter().getId()):"");
			}
			// account =
			// accountService.findAccountByProfileIdAndProfileType(agent.getProfileId(),
			// ESEAccount.AGENT_ACCOUNT);
			if (!StringUtil.isEmpty(account)) {
				String[] agentAcc = String.valueOf(account.getCashBalance()).split("\\.");
				agent.setAccountRupee(String.valueOf(agentAcc[0]));
				agent.setAccountPaise(String.valueOf(agentAcc[1]));
			} else {
				agent.setAccountRupee("0");
				agent.setAccountPaise("00");
			}
			// Load warehouse details
			for (Warehouse warehouse : agent.getWareHouses()) {
				if (ObjectUtil.isEmpty(warehouse.getRefCooperative())) {
					if(getCurrentTenantId().equalsIgnoreCase("chetna")|| getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
						if(!ObjectUtil.isEmpty(agent.getAgentType()) &&  !StringUtil.isEmpty(agent.getAgentType().getCode())
								&& agent.getAgentType().getCode().equalsIgnoreCase("01")|| agent.getAgentType().getCode().equalsIgnoreCase("02")
								){
							setSelectedCooperative(warehouse.getName());
							populateSamithiByAgentId(id);
							}else if(!ObjectUtil.isEmpty(agent.getAgentType()) &&  !StringUtil.isEmpty(agent.getAgentType().getCode())
									&& agent.getAgentType().getCode().equalsIgnoreCase("03")){
								setSelectedCooperative(warehouse.getName());
								populateSamithiByAgentId(id);
								//Warehouse procurementCenter = agentService.listSelectedWarehouseById(agent.getId());
								agent.setProcurementCenter(agent.getProcurementCenter());
							}else if(!ObjectUtil.isEmpty(agent.getAgentType()) &&  !StringUtil.isEmpty(agent.getAgentType().getCode())
									&& agent.getAgentType().getCode().equalsIgnoreCase("04")){
								
								//Warehouse procurementCenter = agentService.listSelectedWarehouseById(agent.getId());
								agent.setProcurementCenter(agent.getProcurementCenter());
							}else if(!ObjectUtil.isEmpty(agent.getAgentType()) &&  !StringUtil.isEmpty(agent.getAgentType().getCode())
									&& agent.getAgentType().getCode().equalsIgnoreCase("05")){
								
								//Warehouse procurementCenter = agentService.listSelectedWarehouseById(agent.getId());
								agent.setProcurementCenter(agent.getProcurementCenter());
							}
					}else{
						setSelectedCooperative(warehouse.getName());
						populateSamithiByAgentId(id);
					}
					
					
				} else {
					setSelectedCooperative(warehouse.getRefCooperative().getName());
					populateSamithiByAgentId(id);
					// loadSamithiByCooperative(id, agent.getCooperative());
				}
			}
			if (agent.getPersonalInfo().getImage() != null) {
				setAgentImageByteString(Base64Util.encoder(agent.getPersonalInfo().getImage()));
			}
			// loadWarehouseByServicePoint(id, agent.getServicePoint());
			if (!StringUtil.isEmpty(agent.getPassword())) {
				String plainText = cryptoUtil.decrypt(agent.getPassword());
				if (!StringUtil.isEmpty(plainText)) {
					plainText = plainText.trim();
					plainText = plainText.substring(agent.getProfileId().length(), plainText.length());
					agent.setPassword(plainText.trim());
					agent.setConfirmPassword(plainText.trim());
				} else {
					agent.setPassword(null);
				}
			} else {
				agent.setPassword(null);
			}
			/*if(agent.isTrainingExists()){
				String trainings="";
				Set<FarmerTraining>farmerTrainings=agent.getTrainingTemplates();
				for(FarmerTraining farmerTraining:farmerTrainings){
					trainings=trainings.concat(String.valueOf(farmerTraining.getId())).concat(",");
				}
					agent.setSelectedtrainings(StringUtil.removeLastComma(trainings.trim()));
				agent.setSelectedtrainings(StringUtil.removeLastComma(trainings.trim()));
				}
			*/
			
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText("Agentupdate.page" + type));
		} else {

			if (agent != null) {
				if (agent.getBodStatus() == ESETxn.BOD) {
					addActionError(getText("cannotUpdateAgent"));
					request.setAttribute(HEADING, getText("Agentupdate.page" + type));
					return INPUT;
				}

				Agent temp = agentService.findAgent(agent.getId());
				if (temp == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}

				if (!ObjectUtil.isEmpty(agent.getAgentType())) {
					AgentType agentType = agentService.findAgentTypeById(agent.getAgentType().getId());
					if (!ObjectUtil.isEmpty(agentType)) {
						temp.setAgentType(agentType);
					}
				}

				temp.setProfileId(agent.getProfileId());
				temp.getContactInfo().setAddress1(agent.getContactInfo().getAddress1());
				temp.getContactInfo().setAddress2(agent.getContactInfo().getAddress2());
				temp.getContactInfo().setZipCode(agent.getContactInfo().getZipCode());
				temp.getContactInfo().setPhoneNumber(agent.getContactInfo().getPhoneNumber());
				temp.getContactInfo().setMobileNumber(agent.getContactInfo().getMobileNumber());
				temp.getContactInfo().setEmail(agent.getContactInfo().getEmail());
				temp.setAccountBalance(agent.getAccountBalance());
				temp.setRevisionNumber(DateUtil.getRevisionNumber());

				/*
				 * temp.getContactInfo().setCity(
				 * locationService.findMunicipalityByName(agent.getContactInfo()
				 * .getCity() .getName()));
				 */
				temp.getPersonalInfo().setFirstName(agent.getPersonalInfo().getFirstName());
				temp.getPersonalInfo().setMiddleName(agent.getPersonalInfo().getMiddleName());
				temp.getPersonalInfo().setLastName(agent.getPersonalInfo().getLastName());
				temp.getPersonalInfo().setStatus(agent.getBodStatus());
				temp.getContactInfo().setMobileNumber(agent.getContactInfo().getMobileNumber());
				// Commented, second last name will not be used in future said
				// by loganathan on 1st
				// release on 17th jan 2013 4:53 pm
				// temp.getPersonalInfo().setSecondLastName(
				// agent.getPersonalInfo().getSecondLastName());
				temp.getPersonalInfo().setIdentityType(agent.getPersonalInfo().getIdentityType());
				temp.getPersonalInfo().setIdentityNumber(agent.getPersonalInfo().getIdentityNumber());
				temp.getPersonalInfo().setSex(agent.getPersonalInfo().getSex());
				temp.getPersonalInfo().setDateOfBirth(agent.getPersonalInfo().getDateOfBirth());
				temp.setTxnMode(agent.getTxnMode());
				temp.setStatus(agent.getStatus());
				temp.setUpdatedDate(new Date());
				temp.setUpdatedUser(getUsername());
			/*	
				temp.setTrainingExists(agent.isTrainingExists());
				Set<FarmerTraining>farmerTrainings=new HashSet<FarmerTraining>();
				FarmerTraining farmerTraining;
				String []idArr=agent.getSelectedtrainings().split(",");
				if(agent.isTrainingExists()){
					if(idArr.length>0){
				for(String id:idArr){
					farmerTraining=trainingService.findFarmerTrainingById(Long.valueOf(id.trim()));
					farmerTrainings.add(farmerTraining);
					}	
				}
				}
				temp.setTrainingTemplates(farmerTrainings);
				agent.setTrainingTemplates(farmerTrainings);
				*/
				if (Profile.ACTIVE == agent.getStatus()) {
					if (StringUtil.isEmpty(agent.getPassword())) {
						addActionError(getText("empty.password"));
						request.setAttribute(HEADING, getText("Agentupdate.page" + type));
						return INPUT;
					} else {
						String oldPasswordToken = cryptoUtil
								.encrypt(StringUtil.getMulipleOfEight(agent.getProfileId() + agent.getPassword()));
						temp.setPassword(oldPasswordToken);
					}
				}
				setCurrentPage(getCurrentPage());

				if (!ObjectUtil.isListEmpty(temp.getServiceLocations())) {
					// agentService.removeAgentServiceLocationMapping(temp.getId());
					// temp.getServiceLocations().clear();
					agentService.removeAgentWarehouseMappingByAgentId(temp.getId());
				}
				if (this.getAgentImage() != null) {
					temp.getPersonalInfo().setImage(FileUtil.getBinaryFileContent(this.getAgentImage()));
				}
				// Loading warehouse details
				if ("cooperativeManager".equalsIgnoreCase(type)) {
					if (!StringUtil.isEmpty(selectedCooperative)) {
						Warehouse warehouse = locationService.findWarehouseByName(selectedCooperative);
						Set<Warehouse> coopWarehouses = new HashSet<Warehouse>();
						coopWarehouses.add(warehouse);
						temp.setWareHouses(coopWarehouses);
					}
				} else {
					
					boolean changed = false;
					Set<Warehouse> wareHouses = new HashSet<Warehouse>();
					if(getCurrentTenantId().equalsIgnoreCase("chetna")|| getCurrentTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
						if(!ObjectUtil.isEmpty(temp.getAgentType()) && !StringUtil.isEmpty(temp.getAgentType().getCode()) && temp.getAgentType().getCode().equalsIgnoreCase("02")){
							for (String warehouseName : getSelectedName()) {
								Warehouse warehouse = locationService.findSamithiByName(warehouseName);
								if (!ObjectUtil.isEmpty(warehouse)) {
									if (!temp.getWareHouses().contains(warehouse))
										changed = true;
									wareHouses.add(warehouse);
								}
							}temp.setWareHouses(wareHouses);
							}else if(!ObjectUtil.isEmpty(temp.getAgentType()) && !StringUtil.isEmpty(temp.getAgentType().getCode()) 
									&& temp.getAgentType().getCode().equalsIgnoreCase("03")){
								for (String warehouseName : getSelectedName()) {
									Warehouse warehouse = locationService.findSamithiByName(warehouseName);
									if (!ObjectUtil.isEmpty(warehouse)) {
										if (!temp.getWareHouses().contains(warehouse))
											changed = true;
										wareHouses.add(warehouse);
									}
								}temp.setWareHouses(wareHouses);
								Warehouse warehouse = locationService.findProcurementWarehouseById(Long.valueOf(selectedProcurementCenter));
								temp.setProcurementCenter(warehouse);
							//temp.setWarehouseId(selectedProcurementCenter);
							}else if(!ObjectUtil.isEmpty(temp.getAgentType()) && !StringUtil.isEmpty(temp.getAgentType().getCode()) 
									&& temp.getAgentType().getCode().equalsIgnoreCase("04")){
								
								Warehouse warehouse = locationService.findProcurementWarehouseById(Long.valueOf(selectedGinningCenter));
								temp.setProcurementCenter(warehouse);
							}else if(!ObjectUtil.isEmpty(temp.getAgentType()) && !StringUtil.isEmpty(temp.getAgentType().getCode()) 
									&& temp.getAgentType().getCode().equalsIgnoreCase("05")){
								Warehouse warehouse = locationService.findProcurementWarehouseById(Long.valueOf(selectedSpinner));
								temp.setProcurementCenter(warehouse);
							//temp.setWarehouseId();
							}
					}else if(getCurrentTenantId().equals(ESESystem.PRATIBHA_TENANT_ID)){
						if(!temp.getAgentType().getCode().equalsIgnoreCase("06")){
						Warehouse warehouse = locationService.findProcurementWarehouseById(Long.valueOf(warehouseName));
						temp.setProcurementCenter(warehouse);
						}

						for (String warehouseName : getSelectedName()) {
							Warehouse Samithi = locationService.findSamithiByName(warehouseName);
							if (!ObjectUtil.isEmpty(Samithi)) {
								if (!temp.getWareHouses().contains(Samithi))
									changed = true;
								wareHouses.add(Samithi);
							}
						}
						//
						if (temp.getWareHouses().size() != wareHouses.size() || changed) {
							temp.setRevisionNumber(DateUtil.getRevisionNumber());
						}
						temp.setWareHouses(wareHouses);

					
					}else{						
						for (String warehouseName : getSelectedName()) {
							Warehouse warehouse = locationService.findSamithiByName(warehouseName);
							if (!ObjectUtil.isEmpty(warehouse)) {
								if (!temp.getWareHouses().contains(warehouse))
									changed = true;
								wareHouses.add(warehouse);
							}
						}
						if (temp.getWareHouses().size() != wareHouses.size() || changed) {
							temp.setRevisionNumber(DateUtil.getRevisionNumber());
						}
						temp.setWareHouses(wareHouses);					
					}
					
					
					//
					if (temp.getWareHouses().size() != wareHouses.size() || changed) {
						temp.setRevisionNumber(DateUtil.getRevisionNumber());
					}
					

				}

				if (!ObjectUtil.isEmpty(account)) {
					tmpAccStat = account.getStatus();
				}

				account = accountService.findAccountByProfileIdAndProfileType(agent.getProfileId(),
						ESEAccount.AGENT_ACCOUNT);
				if (account != null) {
					account.setCashBalance(agent.getAccountBalance());
					accountService.update(account);
				}
				agentService.editAgentProfile(temp);
				if (!ObjectUtil.isEmpty(card))
					cardService.updateCardStatus(temp.getProfileId(), card.getStatus(), card.getCardRewritable());
				if (!ObjectUtil.isEmpty(account)) {
					if ("cooperativeManager".equalsIgnoreCase(type))
						accountService.updateAccountStatus(agent.getProfileId(), account.ACTIVE,
								ESEAccount.AGENT_ACCOUNT);
					else {
						if (tmpAccStat != -1) { // check tmpAccStat reassigned
												// value.
							account.setStatus(tmpAccStat);
						}
						accountService.updateAccountStatus(agent.getProfileId(), account.getStatus(),
								ESEAccount.AGENT_ACCOUNT);
					}

				}

			}

			request.setAttribute(HEADING, getText(LIST + type));
			return LIST;
		}
		return super.execute();
	}

	/**
	 * Delete.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String delete() throws Exception {

		setCurrentPage(getCurrentPage());
		if (this.getId() != null && !(this.getId().equals(EMPTY))) {
			agent = agentService.findAgent(Long.valueOf(id));
			setCurrentPage(getCurrentPage());
			if (agent == null) {
				addActionError(NO_RECORD);
				return list();
			}
			if (agent != null) {
				boolean agentId = agentService.findAgentIdFromEseTxn(agent.getProfileId());
				if (agentId) {
					addActionError(getText("cannotDeleteAgentHasTxn"));
					request.setAttribute(HEADING, getText(DETAIL));
					return DETAIL;
				} else {
					String errorMsg = null;
					if ("fieldStaff".equalsIgnoreCase(type)) {
						boolean agentDeviceMap = agentService.findAgentIdFromDevice(agent.getId());
						if (agentDeviceMap) {
							errorMsg = "cannotDeleteAgentHasDeviceMapping";
							detail();
						} else if (agent.getBodStatus() == ESETxn.BOD) {
							errorMsg = "cannotDeleteAgentBODstatus";
						} else if (productDistributionService.isAgentWarehouseProductStockExist(agent.getProfileId())) {
							errorMsg = "cannotDeleteAgentstockExist";
							detail();
						}
						else if(productDistributionService.isAgentDistributionExist(agent.getProfileId())){
							errorMsg = "cannotDeleteAgentMobileUserExist";
							detail();
						}
					}

					if (StringUtil.isEmpty(errorMsg)) {
						// delete constrain for procurement stock availability
						List<CityWarehouse> procurementProductStock = productDistributionService
								.listProcurementStocksForAgent(agent.getProfileId());
						if (!ObjectUtil.isListEmpty(procurementProductStock)) {
							for (CityWarehouse cityWarehouse : procurementProductStock) {
								errorMsg = !ObjectUtil.isEmpty(cityWarehouse.getCoOperative())
										? "cannotDeleteCOMProcurementstockExist"
										: "cannotDeleteFSProcurementstockExist";
								detail();
								break;
							}
						}
					}

					if (StringUtil.isEmpty(errorMsg)) {
						productDistributionService.removeAgentWarehouseProduct(agent.getId());
						cardService.removeCardByProfileId(agent.getProfileId());
						accountService.removeAccountByProfileIdAndProfileType(agent.getProfileId(),
								ESEAccount.AGENT_ACCOUNT);
						agentService.removeAgent(agent);
					}

					if (!StringUtil.isEmpty(errorMsg)) {
						addActionError(getText(errorMsg));
						request.setAttribute(HEADING, getText(DETAIL));
						return DETAIL;
					}
				}
			}
		}
		return list();
	}

	/**
	 * Process stock transfer.
	 * 
	 * @return the string
	 */
	public String processStockTransfer() {

		String response = null;
		if ("removeStock".equalsIgnoreCase(this.requestFor)) {
			this.processStockTransfer.removePendingMTNTStock(id);
			printAjaxResponse("1", CONTENT_TYPE_TEXT_HTML);
		} else if ("getAgents".equalsIgnoreCase(this.requestFor)) {
			this.agents = this.processStockTransfer.getAgentsFromServicePoint(id);
			response = "ajaxagents";
		} else if ("moveStocks".equalsIgnoreCase(this.requestFor)) {
			printAjaxResponse(this.processStockTransfer.moveStocksToAnotherAgent(id, profileId),
					CONTENT_TYPE_TEXT_HTML);
		}
		return response;
	}

	/**
	 * Gets the countries.
	 * 
	 * @return the countries
	 */
	public List<Country> getCountries() {

		List<Country> returnValue = locationService.listCountries();
		return returnValue;
	}

	/**
	 * Gets the states.
	 * 
	 * @return the states
	 */
	public List<State> getStates() {

		if (!StringUtil.isEmpty(selectedCountry)) {
			stats = locationService.listStates(selectedCountry);
		}
		return stats;
	}

	/**
	 * Gets the localities.
	 * 
	 * @return the localities
	 */
	public List<Locality> getLocalities() {

		if (!StringUtil.isEmpty(selectedState)) {
			localities = locationService.listLocalities(selectedState);
		}
		return localities;
	}

	/**
	 * Gets the cities.
	 * 
	 * @return the cities
	 */
	public List<Municipality> getCities() {

		if (!StringUtil.isEmpty(selectedLocality)) {
			cities = locationService.listMunicipalities(selectedLocality);
		}
		return cities;
	}

	/**
	 * Populate locality.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateLocality() throws Exception {

		if (!selectedState.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedState))) {
			localities = locationService.listLocalities(selectedState);
		}
		sendResponse(localities);
		return null;
	}

	/**
	 * Populate city.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateCity() throws Exception {

		if (!selectedLocality.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedLocality))) {
			cities = locationService.listMunicipalities(selectedLocality);
		}
		sendResponse(cities);
		return null;
	}

	/**
	 * Populate state.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateState() throws Exception {

		if (!selectedCountry.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCountry))) {
			stats = locationService.listStates(selectedCountry);
		}
		sendResponse(stats);
		return null;
	}

	/**
	 * Gets the service point list.
	 * 
	 * @return the service point list
	 */
	public List<ServicePoint> getServicePointList() {

		List<ServicePoint> servicePoint = servicePointService.listServicePoints();
		return servicePoint;
	}

	/**
	 * Execute.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {

		if (id != null && !id.equals("")) {
			agent = agentService.findAgent(id);

			id = null;
			command = "update";
			request.setAttribute("heading", getText("update" + type));
		}
		return super.execute();
	}

	/**
	 * Populate service location.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateServiceLocation() throws Exception {

		ServicePoint service = servicePointService.findServicePointByName(servicePointName);
		if (!ObjectUtil.isEmpty(service)) {
			availableServiceLocation = serviceLocationService
					.findServiceLocationBasedOnServicePointAndAgent(service.getId());
		}
		if (ObjectUtil.isEmpty(agent)) {
			agent = agentService.findAgent(Long.parseLong(id));
		}

		if (agent.getServicePoint().getName().equalsIgnoreCase(service.getName())) {
			if (agent.getServiceLocations().size() != 0) {
				selectedLocation = agent.getServiceLocations();
				for (ServiceLocation location : selectedLocation) {
					String selected = location.getName();
					selectedServiceLocation.add(selected);
				}
			}
		} else {
			selectedServiceLocation.clear();
		}

		command = UPDATE;
		request.setAttribute(HEADING, getText("Agentupdate.page" + type));

		return MAPPING;
	}

	/**
	 * Populate ware house.
	 * 
	 * @return the string
	 */
	public String populateWareHouse() {

		// Load warehouse details
		/*
		 * loadWarehouseByServicePoint(id, servicePointService
		 * .findServicePointByName(servicePointName));
		 */

		Warehouse cooperative = (Warehouse) locationService.findCooperativeByName(cooperativeName);
		// loadSamithiByCooperative(id, cooperative);
		return WAREHOUSE_MAPPING;
	}

	/**
	 * Load samithi by cooperative.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param warehouse
	 *            the warehouse
	 */
	private void loadSamithiByCooperative(String agentId, Warehouse warehouse) {

		if (!ObjectUtil.isEmpty(warehouse)) {
			if (!StringUtil.isEmpty(agentId)) {
				selectedWarehouse = agentService.listSelectedSamithiByAgentId(Long.valueOf(agentId), warehouse.getId());
				availableWarehouse = agentService.listAvailableSamithiByAgentId(Long.valueOf(agentId),
						warehouse.getId());

			} else
				availableWarehouse = agentService.listSamithiByCooperativeId(warehouse.getId());
		}
	}

	private void populateSamithiByAgentId(String agentId) {

		if (!StringUtil.isEmpty(agentId)) {
			selectedWarehouse = agentService.listSelectedSamithiById(Long.valueOf(agentId));
			availableWarehouse = agentService.listAvailableSamithi();

			Iterator itr = selectedWarehouse.iterator();
			while (itr.hasNext()) {
				availableWarehouse.remove(itr.next());
			}
			setAvailableWarehouse(availableWarehouse);
			System.out.println(selectedWarehouse + "" + availableWarehouse);
		}
	}

	/**
	 * Load warehouse by service point.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param servicePoint
	 *            the service point
	 */
	private void loadWarehouseByServicePoint(String agentId, ServicePoint servicePoint) {

		if (!ObjectUtil.isEmpty(servicePoint)) {
			availableWarehouse = agentService.listWarehouseNameByServicePointId(servicePoint.getId());
			selectedWarehouse = agentService.listWarehouseNameByAgentIdAndServicePointId(Long.valueOf(agentId),
					servicePoint.getId());
		}
	}

	/**
	 * Sets the agent image byte string.
	 * 
	 * @param agentImageByteString
	 *            the new agent image byte string
	 */
	public void setAgentImageByteString(String agentImageByteString) {

		this.agentImageByteString = agentImageByteString;
	}

	/**
	 * Gets the agent image byte string.
	 * 
	 * @return the agent image byte string
	 */
	public String getAgentImageByteString() {

		return agentImageByteString;
	}

	/**
	 * Populate qr code.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String populateQRCode() throws Exception {

		byte[] result = QRCodeGenerator.generatorQRCode(getQrid());
		fileInputStream = new ByteArrayInputStream(result);
		return "code";
	}

	/**
	 * Sets the file input stream.
	 * 
	 * @param fileInputStream
	 *            the new file input stream
	 */
	public void setFileInputStream(InputStream fileInputStream) {

		this.fileInputStream = fileInputStream;
	}

	/**
	 * Gets the file input stream.
	 * 
	 * @return the file input stream
	 */
	public InputStream getFileInputStream() {

		return fileInputStream;
	}

	/**
	 * Gets the code name.
	 * 
	 * @return the code name
	 */
	public String getCodeName() {

		return getQrid();
	}

	/**
	 * Sets the qrid.
	 * 
	 * @param qrid
	 *            the new qrid
	 */
	public void setQrid(String qrid) {

		this.qrid = qrid;
	}

	/**
	 * Gets the qrid.
	 * 
	 * @return the qrid
	 */
	public String getQrid() {

		return qrid;
	}

	/**
	 * Gets the available warehouse.
	 * 
	 * @return the available warehouse
	 */
	public List<String> getAvailableWarehouse() {

		List<Warehouse> samithiList = locationService.listOfSamithi();
		availableWarehouse = new ArrayList<String>();
		if (!ObjectUtil.isListEmpty(samithiList)) {
			for (Warehouse samithi : samithiList) {
				availableWarehouse.add(samithi.getName());
			}
		}
		Iterator itr = selectedWarehouse.iterator();
		while (itr.hasNext()) {
			availableWarehouse.remove(itr.next());
		}

		return availableWarehouse;
	}

	/**
	 * Sets the available warehouse.
	 * 
	 * @param availableWarehouse
	 *            the new available warehouse
	 */
	public void setAvailableWarehouse(List<String> availableWarehouse) {

		this.availableWarehouse = availableWarehouse;
	}

	/**
	 * Gets the selected warehouse.
	 * 
	 * @return the selected warehouse
	 */
	public List<String> getSelectedWarehouse() {

		return selectedWarehouse;
	}

	/**
	 * Sets the selected warehouse.
	 * 
	 * @param selectedWarehouse
	 *            the new selected warehouse
	 */
	public void setSelectedWarehouse(List<String> selectedWarehouse) {

		this.selectedWarehouse = selectedWarehouse;
	}

	/**
	 * Gets the warehouse names.
	 * 
	 * @return the warehouse names
	 */
	public String getWarehouseNames() {

		return warehouseNames;
	}

	/**
	 * Sets the warehouse names.
	 * 
	 * @param warehouseNames
	 *            the new warehouse names
	 */
	public void setWarehouseNames(String warehouseNames) {

		this.warehouseNames = warehouseNames;
	}

	/**
	 * Gets the pending mtnt exists.
	 * 
	 * @return the pending mtnt exists
	 */
	public boolean getPendingMTNTExists() {

		return pendingMTNTExists;
	}

	/**
	 * Sets the pending mtnt exists.
	 * 
	 * @param pendingMTNTExists
	 *            the new pending mtnt exists
	 */
	public void setPendingMTNTExists(boolean pendingMTNTExists) {

		this.pendingMTNTExists = pendingMTNTExists;
	}

	/**
	 * Sets the agents.
	 * 
	 * @param agents
	 *            the new agents
	 */
	public void setAgents(List<Agent> agents) {

		this.agents = agents;
	}

	/**
	 * Sets the login user name.
	 * 
	 * @param loginUserName
	 *            the new login user name
	 */
	public void setLoginUserName(String loginUserName) {

		this.loginUserName = loginUserName;
	}

	/**
	 * Gets the login user name.
	 * 
	 * @return the login user name
	 */
	public String getLoginUserName() {

		return loginUserName;
	}

	/**
	 * Gets the agents.
	 * 
	 * @return the agents
	 */
	public List<Agent> getAgents() {

		return agents;
	}

	/**
	 * Sets the process stock transfer.
	 * 
	 * @param processStockTransfer
	 *            the new process stock transfer
	 */
	public void setProcessStockTransfer(ProcessStockTransferImpl processStockTransfer) {

		this.processStockTransfer = processStockTransfer;
	}

	/**
	 * Sets the request for.
	 * 
	 * @param requestFor
	 *            the new request for
	 */
	public void setRequestFor(String requestFor) {

		this.requestFor = requestFor;
	}

	/**
	 * Gets the available name.
	 * 
	 * @return the available name
	 */
	public List<String> getAvailableName() {

		return availableName;
	}

	/**
	 * Sets the available name.
	 * 
	 * @param availableName
	 *            the new available name
	 */
	public void setAvailableName(List<String> availableName) {

		this.availableName = availableName;
	}

	/**
	 * Reset.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String reset() throws Exception {

		if (this.getId() != null && !(this.getId().equals(EMPTY))) {
			agent = agentService.findAgent(Long.valueOf(id));
			setCurrentPage(getCurrentPage());
			if (agent == null) {
				addActionError(NO_RECORD);
				return null;
			}
			int txnStatus = 0;
			String text = "success.reset.BOD";
			/*
			 * if (agent.getBodStatus() == ESETxn.EOD) { txnStatus = 1; text =
			 * "success.reset.EOD"; }
			 */
			agentService.updateAgentBODStatus(agent.getProfileId(), txnStatus);
			loginUserName = request.getSession().getAttribute("user").toString();
			agent.setBodStatus(txnStatus);
			addActionMessage(getText(text));
			request.setAttribute(HEADING, getText(DETAIL + type));
			detail();
		}
		return DETAIL;
	}

	/**
	 * Update id seq.
	 * 
	 * @return the string
	 */
	public String updateIdSeq() {

		agentService.updateIdSeq();
		return DETAIL;
	}

	/**
	 * Gets the agent types.
	 * 
	 * @return the agent types
	 */
	public List<AgentType> getAgentTypes() {

		return agentService.listAgentType();
	}

	/**
	 * Gets the filter agent types.
	 * 
	 * @return the filter agent types
	 */
	public String getFilterAgentTypes() {

		List<AgentType> agentTypes = getAgentTypes();
		String filterAgentTypes = "0:All;";
		if (!ObjectUtil.isEmpty(agentTypes)) {
			for (AgentType agentType : agentTypes) {
				filterAgentTypes += agentType.getId() + ":" + agentType.getName() + ";";
			}
		}
		return filterAgentTypes.substring(0, filterAgentTypes.length() - 1);
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
	 * Sets the selected cooperative.
	 * 
	 * @param selectedCooperative
	 *            the new selected cooperative
	 */
	public void setSelectedCooperative(String selectedCooperative) {

		this.selectedCooperative = selectedCooperative;
	}

	/**
	 * Gets the selected cooperative.
	 * 
	 * @return the selected cooperative
	 */
	public String getSelectedCooperative() {

		return selectedCooperative;
	}

	/**
	 * Gets the cooperative list.
	 * 
	 * @return the cooperative list
	 */
	public List<Warehouse> getCooperativeList() {

		List<Warehouse> returnValueList = locationService.listOfCooperatives();

		return returnValueList;
	}

	public List<Warehouse> getSamithiList() {

		List<Warehouse> returnValueList = locationService.listOfSamithi();
		Iterator itr = selectedWarehouse.iterator();
		while (itr.hasNext()) {
			availableWarehouse.remove(itr.next());
			// returnValueList.remove(itr.next());
		}
		setAvailableWarehouse(availableWarehouse);
		return returnValueList;
	}

	/**
	 * Prepare.
	 * 
	 * @throws Exception
	 *             the exception
	 * @see com.sourcetrace.esesw.view.SwitchAction#prepare()
	 */
	public void prepare() throws Exception {

		type = request.getParameter("type");
		if (!StringUtil.isEmpty(type)) {
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB+type);
			if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB+type))) {
				content = super.getText(BreadCrumb.BREADCRUMB+type, "");
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
			
		}else{
			String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
					.getSimpleName();
			String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
			if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
				content = super.getText(BreadCrumb.BREADCRUMB, "");
			}else{
				
			}
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content));
		}
	}

	/**
	 * Gets the cooperative name.
	 * 
	 * @return the cooperative name
	 */
	public String getCooperativeName() {

		return cooperativeName;
	}

	/**
	 * Sets the cooperative name.
	 * 
	 * @param cooperativeName
	 *            the new cooperative name
	 */
	public void setCooperativeName(String cooperativeName) {

		this.cooperativeName = cooperativeName;
	}

	/**
	 * Sets the warehouse cooperative.
	 * 
	 * @param warehouseCooperative
	 *            the new warehouse cooperative
	 */
	public void setWarehouseCooperative(String warehouseCooperative) {

		this.warehouseCooperative = warehouseCooperative;
	}

	/**
	 * Gets the warehouse cooperative.
	 * 
	 * @return the warehouse cooperative
	 */
	public String getWarehouseCooperative() {

		return warehouseCooperative;
	}

	public Map<Integer, String> getBodStatus() {

		return bodStatus;
	}

	public void setBodStatus(Map<Integer, String> bodStatus) {

		this.bodStatus = bodStatus;
	}

	/**
	 * List.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 * @see com.sourcetrace.esesw.view.SwitchAction#list()
	 */
	public String list() throws Exception {

		if (getCurrentPage() != null) {
			setCurrentPage(getCurrentPage());
		}
		request.setAttribute(HEADING, getText(LIST + type));
		return LIST;
	}

	/**
	 * Gets the crypto util.
	 * 
	 * @return the crypto util
	 */
	public ICryptoUtil getCryptoUtil() {

		return cryptoUtil;
	}

	/**
	 * Sets the crypto util.
	 * 
	 * @param cryptoUtil
	 *            the new crypto util
	 */
	public void setCryptoUtil(ICryptoUtil cryptoUtil) {

		this.cryptoUtil = cryptoUtil;
	}

	public int getStatusDefaultValue() {

		return (ObjectUtil.isEmpty(this.agent) ? Profile.INACTIVE : this.agent.getStatus());
	}

	public String getLoginUserRole() {

		return loginUserRole;
	}

	public void setLoginUserRole(String loginUserRole) {

		this.loginUserRole = loginUserRole;
	}

	public String getSelectedtrainings() {
		return selectedtrainings;
	}

	public void setSelectedtrainings(String selectedtrainings) {
		this.selectedtrainings = selectedtrainings;
	}
	public Map<Long,String>getTrainingList(){
		Map<Long,String>trainingMap=new LinkedHashMap<>();
		List<FarmerTraining>farmerTrainings=trainingService.listFarmerTraining();
		if(!ObjectUtil.isListEmpty(farmerTrainings)){
			for(FarmerTraining farmerTraining:farmerTrainings){
			trainingMap.put(farmerTraining.getId(), farmerTraining.getTrainingTopic().getName());
			}
			//farmerTrainings.stream().collect(Collectors.toMap(FarmerTraining->getName(), FarmerTraining->traini));
		}
		return trainingMap;
		
	}
	public Map<Long, String> getListProcurementCenter() {

		List<Warehouse> procurementCenterList = locationService.listProcurementCenter();

		Map<Long, String> procurementCenterDropDownList = new LinkedHashMap<Long, String>();
		for (Warehouse warehouse : procurementCenterList) {
			procurementCenterDropDownList.put(warehouse.getId(), warehouse.getName());
		}
		return procurementCenterDropDownList;
	}
	public Map<Long, String> getWarehouseList(){
		List<Warehouse> warehouseList=locationService.listWarehouse();
		Map<Long,String> warehouses=new LinkedHashMap<Long, String>();
		if(!ObjectUtil.isListEmpty(warehouseList)){
		warehouseList.stream().forEach( w->{
			warehouses.put(w.getId(), w.getName());
		});
		}
		return warehouses;
	}
	public Map<Long, String> getGinnerCenterList() {
		return locationService.listCoOperativeAndSamithiByRevisionNo(0L, Warehouse.WarehouseTypes.GINNER.ordinal())
				.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
	}

	public String getSelectedProcurementCenter() {
		return selectedProcurementCenter;
	}

	public void setSelectedProcurementCenter(String selectedProcurementCenter) {
		this.selectedProcurementCenter = selectedProcurementCenter;
	}

	public String getSelectedGinningCenter() {
		return selectedGinningCenter;
	}

	public void setSelectedGinningCenter(String selectedGinningCenter) {
		this.selectedGinningCenter = selectedGinningCenter;
	}

	public String getSelectedAgentType() {
		return selectedAgentType;
	}

	public void setSelectedAgentType(String selectedAgentType) {
		this.selectedAgentType = selectedAgentType;
	}
	public Map<Long, String> getSpinnerList() {
		return locationService.listCoOperativeAndSamithiByRevisionNo(0L, Warehouse.WarehouseTypes.SPINNER.ordinal())
				.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getName));
	}

	public String getSelectedSpinner() {
		return selectedSpinner;
	}

	public void setSelectedSpinner(String selectedSpinner) {
		this.selectedSpinner = selectedSpinner;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public File getAgentImage() {
		return agentImage;
	}

	public void setAgentImage(File agentImage) {
		this.agentImage = agentImage;
	}
	
	public String populateImage() {

		try {
			// setImgId(imgId);
			Agent pmtImg = null;
			if (!StringUtil.isEmpty(id) && !StringUtil.isEmpty(type) && type != null)
				pmtImg = agentService.findAgent(Long.valueOf(id));
			byte[] image = null;
			if (type.equals("1") && !ObjectUtil.isEmpty(pmtImg)
					&& pmtImg.getPersonalInfo().getImage() != null) {
				image = pmtImg.getPersonalInfo().getImage();
			}
			response.setContentType("multipart/form-data");
			OutputStream out = response.getOutputStream();
			out.write(image);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
}
