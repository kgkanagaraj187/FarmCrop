/**
 * UserAction.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.Language;
import com.ese.util.Base64Util;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IMailService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IRoleService;
import com.sourcetrace.eses.service.IUserService;
import com.sourcetrace.eses.umgmt.entity.ContactInfo;
import com.sourcetrace.eses.umgmt.entity.IdentityType;
import com.sourcetrace.eses.umgmt.entity.PersonalInfo;
import com.sourcetrace.eses.umgmt.entity.Role;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.JsonUtil;
import com.sourcetrace.eses.util.MailUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.crypto.ICryptoUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

// TODO: Auto-generated Javadoc
/**
 * The Class UserAction.
 * 
 * @author $Author: boopalan $
 * @version $Rev: 5050 $
 */
public class UserAction extends SwitchValidatorAction {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserAction.class);
	private IUserService userService;
	private ILocationService locationService;
	private IMailService mailService;
	private IPreferencesService preferncesService;
	private User user;
	private PersonalInfo personalInfo;
	private ESESystem preferences;
	private String id;
	private String dateOfBirth;
	private String selectedLocality;
	private String selectedMunicipality;
	private String postalCode;
	private String selectedCountry;
	private String selectedState;
	private String selectedCity;
	List<Locality> localities = new ArrayList<Locality>();
	List<State> stats = new ArrayList<State>();
	List<Municipality> cities = new ArrayList<Municipality>();
	DateFormat df = new SimpleDateFormat(getESEDateFormat());
	Map<Integer, String> returnContactTypes = new LinkedHashMap<Integer, String>();
	Map<String, String> returnSexes = new LinkedHashMap<String, String>();
	Map<String, String> returnMaritalStatuses = new LinkedHashMap<String, String>();
	Map<String, String> identityTypeList = new LinkedHashMap<String, String>();
	Map<String, String> languageMap = new LinkedHashMap<String, String>();

	List<Role> roles;
	private IRoleService roleService;
	private String seletedType;
	private ICryptoUtil cryptoUtil;
	private String roleName;
	private String status;
	private String hiddenRole;
	private String branchId_F;
	private File userImage;
	private String subBranchId_F;

	private String getImageFile;
	private String userImageString;
	private String userImageExist;
	/**
	 * Gets the stats.
	 * 
	 * @return the stats
	 */
	public List<State> getStats() {

		return stats;
	}

	/**
	 * Sets the stats.
	 * 
	 * @param stats
	 *            the new stats
	 */
	public void setStats(List<State> stats) {

		this.stats = stats;
	}

	/**
	 * Gets the return contact types.
	 * 
	 * @return the return contact types
	 */
	public Map<Integer, String> getReturnContactTypes() {

		return returnContactTypes;
	}

	/**
	 * Gets the logger.
	 * 
	 * @return the logger
	 */
	public static Logger getLogger() {

		return logger;
	}

	/**
	 * Gets the language.
	 * 
	 * @return the language
	 */

	public Map<String, String> getLanguageMap() {

		List<Language> languages = clientService.listLanguages();
		for (Language lm : languages) {
			languageMap.put(lm.getCode(), getLocaleProperty(lm.getName()));
		}
		return languageMap;
	}

	/**
	 * Sets the return contact types.
	 * 
	 * @param returnContactTypes
	 *            the return contact types
	 */
	public void setReturnContactTypes(Map<Integer, String> returnContactTypes) {

		this.returnContactTypes = returnContactTypes;
	}

	/**
	 * Gets the return sexes.
	 * 
	 * @return the return sexes
	 */
	public Map<String, String> getReturnSexes() {

		return returnSexes;
	}

	/**
	 * Sets the return sexes.
	 * 
	 * @param returnSexes
	 *            the return sexes
	 */
	public void setReturnSexes(Map<String, String> returnSexes) {

		this.returnSexes = returnSexes;
	}

	/**
	 * Gets the return marital statuses.
	 * 
	 * @return the return marital statuses
	 */
	public Map<String, String> getReturnMaritalStatuses() {

		return returnMaritalStatuses;
	}

	/**
	 * Sets the return marital statuses.
	 * 
	 * @param returnMaritalStatuses
	 *            the return marital statuses
	 */
	public void setReturnMaritalStatuses(Map<String, String> returnMaritalStatuses) {

		this.returnMaritalStatuses = returnMaritalStatuses;
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
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
	}

	/**
	 * Sets the localities.
	 * 
	 * @param localities
	 *            the new localities
	 */
	public void setLocalities(List<Locality> localities) {

		this.localities = localities;
	}

	/**
	 * Sets the cities.
	 * 
	 * @param cities
	 *            the new cities
	 */
	public void setCities(List<Municipality> cities) {

		this.cities = cities;
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
	 * Sets the location service.
	 * 
	 * @param locationService
	 *            the new location service
	 */
	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	/**
	 * Sets the user service.
	 * 
	 * @param userService
	 *            the user service
	 * @see com.sourcetrace.esesw.view.SwitchAction#setUserService(com.ese.service.user.IUserService)
	 */
	public void setUserService(IUserService userService) {

		this.userService = userService;
	}

	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public User getUser() {

		return user;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user
	 *            the new user
	 */
	public void setUser(User user) {

		this.user = user;
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
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {

		this.id = id;
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
	 * Gets the identity type list.
	 * 
	 * @return the identity type list
	 */
	public Map<String, String> getIdentityTypeList() {

		return identityTypeList;
	}

	/**
	 * Gets the contact types.
	 * 
	 * @return the contact types
	 */
	public Map<Integer, String> getContactTypes() {

		return returnContactTypes;
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
	 * Sets the selected country.
	 * 
	 * @param selectedCountry
	 *            the new selected country
	 */
	public void setSelectedCountry(String selectedCountry) {

		this.selectedCountry = selectedCountry;
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
	 * Sets the selected state.
	 * 
	 * @param selectedState
	 *            the new selected state
	 */
	public void setSelectedState(String selectedState) {

		this.selectedState = selectedState;
	}

	/**
	 * Gets the sexes.
	 * 
	 * @return the sexes
	 */
	public Map<String, String> getSexes() {

		return returnSexes;
	}

	/**
	 * Gets the marital statuses.
	 * 
	 * @return the marital statuses
	 */
	public Map<String, String> getMaritalStatuses() {

		return returnMaritalStatuses;
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
	 * Sets the mail service.
	 * 
	 * @param mailService
	 *            the new mail service
	 */
	public void setMailService(IMailService mailService) {

		this.mailService = mailService;
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
	 * Sets the preferences.
	 * 
	 * @param preferences
	 *            the new preferences
	 */
	public void setPreferences(ESESystem preferences) {

		this.preferences = preferences;
	}

	/**
	 * Gets the preferences.
	 * 
	 * @return the preferences
	 */
	public ESESystem getPreferences() {

		return preferences;
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
	 * Data.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		User filter = new User();

/*		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			filter.setBranchId(searchRecord.get("branchId").trim());
		}
*/
		
		if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
			if (!getIsMultiBranch().equalsIgnoreCase("1")) {
				List<String> branchList = new ArrayList<>();
				branchList.add(searchRecord.get("branchId").trim());
				filter.setBranchesList(branchList);
				filter.setBranchFiletr("1");
				filter.setBranchId(searchRecord.get("branchId").trim());
			} else {
				List<String> branchList = new ArrayList<>();
				List<BranchMaster> branches = clientService.listChildBranchIds(searchRecord.get("branchId").trim());
				branchList.add(searchRecord.get("branchId").trim());
				branches.stream().filter(branch -> !StringUtil.isEmpty(branch)).forEach(branch -> {
					branchList.add(branch.getBranchId());
				});
				filter.setBranchesList(branchList);
				filter.setBranchFiletr("1");
			}
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("subBranchId"))) {
			filter.setBranchId(searchRecord.get("subBranchId").trim());
		}
		
		String loggedUserName = (String) request.getSession().getAttribute("user");
		if (!StringUtil.isEmpty(loggedUserName) && User.ADMIN_USER_NAME.equals(loggedUserName)) {
			filter.setAdmin(true);
		}

		if (!StringUtil.isEmpty(searchRecord.get("username"))) {
			filter.setUsername(searchRecord.get("username").trim());
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

		if (!StringUtil.isEmpty(searchRecord.get("cI.mobileNumber"))) {
			ContactInfo contactInfo = new ContactInfo();
			contactInfo.setMobileNumber(searchRecord.get("cI.mobileNumber").trim());
			filter.setContactInfo(contactInfo);
		}

		if (!StringUtil.isEmpty(searchRecord.get("r.name"))) {
			Role role = new Role();
			role.setName(searchRecord.get("r.name").trim());
			filter.setRole(role);
		}

		if (!StringUtil.isEmpty(searchRecord.get("enabled"))) {
			if ("1".equals(searchRecord.get("enabled"))) {
				filter.setFilterStatus("enabled");
				filter.setEnabled(true);
			} else {
				filter.setFilterStatus("disabled");
				filter.setEnabled(false);
			}

		}

		filter.setIsMultiBranch(getIsMultiBranch());
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
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
		String webUrl="";
		if (user == null) {
			command = CREATE;
			request.setAttribute(HEADING, getText("Usercreate.page"));
			return INPUT;
		} else {

			/*
			 * Municipality cityId =
			 * locationService.findMunicipalityByName(user.getContactInfo()
			 * .getCity().getName());
			 */
			String userPass = cryptoUtil.encrypt(StringUtil.getMulipleOfEight(user.getUsername() + user.getPassword()));
			user.setPassword(userPass);
			user.getContactInfo().setCity(null);
			user.setLoginStatus(0);
			if (!ObjectUtil.isEmpty(user.getRole())) {
				user.getRole().setId(user.getRole().getId());
			}
			user.setEnabled(user.isEnabled());
			/*
			 * setSelectedCountry(user.getContactInfo().getCity().getLocality().
			 * getState() .getCountry().getName());
			 * setSelectedState(user.getContactInfo().getCity().getLocality().
			 * getState().getName());
			 * setSelectedLocality(user.getContactInfo().getCity().getLocality()
			 * .getName());
			 */

			if (getUserImage() != null) {

				user.getPersonalInfo().setImage(FileUtil.getBinaryFileContent(getUserImage()));

			}

			String bId = StringUtil.isEmpty(getBranchId())
					? ((StringUtil.isEmpty(getBranchId_F()) || getBranchId_F().equals("-1")) ? null : getBranchId_F())
					: getBranchId();
			user.setBranchId(bId);
			
			if (getIsMultiBranch().equals("1") && !StringUtil.isEmpty(getSubBranchId_F())) {
				user.setBranchId(subBranchId_F);
			}
			userService.addUser(user);
			// To send sms after user registration
			/*
			 * SMSUtil.send(getText("senderId"),
			 * user.getContactInfo().getMobileNumber(), getText("message") +
			 * user.getUsername());
			 */
			// To send mail after user registration
			
			try {
				if (!ObjectUtil.isEmpty(user)) {
					if (!ObjectUtil.isEmpty(user.getContactInfo())
							&& !StringUtil.isEmpty(user.getContactInfo().getEmail())) {
						StringBuffer msg = new StringBuffer();
						String password = StringUtil.getRandomNumber();

						BranchMaster dm = clientService.findBranchMasterByBranchId(user.getBranchId());
						ESESystem preference = preferncesService.findPrefernceById(ESESystem.SYSTEM_ESE);
						if (!ObjectUtil.isEmpty(preference)) {
							webUrl = preference.getPreferences().get(ESESystem.WEB_URL);
							cc = preference.getPreferences().get(ESESystem.CC_EMAIL);
						}
						msg.append("\n\tUser Name\t:\t");
						msg.append(user.getUsername());
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
						msg.append(user.getPersonalInfo().getFirstName());
						msg.append("\n");
						msg.append("\n\tLast Name\t:\t");
						msg.append(user.getPersonalInfo().getLastName());
						msg.append("\n");
						msg.append("\n\tWeb Console\t:\t");
						msg.append(webUrl);
						msg.append("\n");
						
						MailUtil.sendWithCC(user.getContactInfo().getEmail(), "SourceTrace Web Console Sign in Details",
								user.getPersonalInfo().getName(), msg.toString(),cc);
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
	 * Detail.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String detail() throws Exception {

		if (id != null && !id.equals(EMPTY)) {
			user = userService.findUser(Long.valueOf(id));
			if(!ObjectUtil.isEmpty(user)){
			if( user.getBranchId()!=null && !StringUtil.isEmpty( user.getBranchId())){
		        BranchMaster fm  = clientService.findBranchMasterByBranchId( user.getBranchId());
		            if(fm.getParentBranch()==null || StringUtil.isEmpty(fm.getParentBranch())){
		            	  branchId_F = user.getBranchId();
		                  user.setBranchId(null);
		                  user.setParentBranchId(branchId_F);
		            }else{
		                BranchMaster parent  = clientService.findBranchMasterByBranchIdAndDisableFilter( fm.getParentBranch());
		                branchId_F = parent.getBranchId();
		                subBranchId_F = user.getBranchId();
		                user.setParentBranchId(branchId_F);
		            }
					}
		            		
			
			user.getPersonalInfo().setSex(getSexes().get(user.getPersonalInfo().getSex()));
			user.getPersonalInfo()
					.setMaritalStatus(getMaritalStatuses().get(user.getPersonalInfo().getMaritalStatus()));
			setDateOfBirth(df.format(user.getPersonalInfo().getDateOfBirth()));
			setCurrentPage(getCurrentPage());

			if (user.isEnabled()) {
				setStatus(getText("enabled"));
			} else {
				setStatus(getText("disabled"));
			}
			if (user.getPersonalInfo().getImage() != null) {

				userImageString = Base64Util.encoder(user.getPersonalInfo().getImage());

			}

			if (user.getRole() != null)
				roleName = user.getRole().getName();
			else
				roleName = "-";

			if (user == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			id = null;
			command = DETAIL;
			request.setAttribute(HEADING, getText(command));
		}else{
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
	}else {
			request.setAttribute(HEADING, getText(LIST));
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

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		if (id != null && !id.equals("")) {
			user = userService.findUser(Long.valueOf(id));
			setCurrentPage(getCurrentPage());
			if (user == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			/*
			 * setSelectedCountry(user.getContactInfo().getCity().getLocality().
			 * getState() .getCountry().getName());
			 * setSelectedState(user.getContactInfo().getCity().getLocality().
			 * getState().getName());
			 * setSelectedLocality(user.getContactInfo().getCity().getLocality()
			 * .getName()); //
			 * setSelectedCity(user.getContactInfo().getCity().getName());
			 * user.getContactInfo().getCity().setName(user.getContactInfo().
			 * getCity().getName());
			 */if (!ObjectUtil.isEmpty(user.getPersonalInfo())
					&& !StringUtil.isEmpty(user.getPersonalInfo().getDateOfBirth())) {
				dateOfBirth = df.format(user.getPersonalInfo().getDateOfBirth());
			}

			String tempPass = user.getPassword();
			if (tempPass != null && !StringUtil.isEmpty(tempPass)) {
				user.setPassword(cryptoUtil.decrypt(tempPass));
			}
			if (!ObjectUtil.isEmpty(user.getRole())) {
				user.getRole().setId(user.getRole().getId());
			}
			if (StringUtil.isEmpty(user.getPassword()) || user.getPassword() == null) {
				user.setChangePassword(true);
			}
			if (user.getPersonalInfo().getImage() != null) {

				setUserImageString(Base64Util.encoder(user.getPersonalInfo().getImage()));
			}
		   	 if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

			if( user.getBranchId()!=null && !StringUtil.isEmpty( user.getBranchId())){
            BranchMaster fm  = clientService.findBranchMasterByBranchId( user.getBranchId());
            if(fm.getParentBranch()==null || StringUtil.isEmpty(fm.getParentBranch())){
                branchId_F = user.getBranchId();
                user.setBranchId(null);
                user.setParentBranchId(branchId_F);
            }else{
                BranchMaster parent  = clientService.findBranchMasterByBranchIdAndDisableFilter( fm.getParentBranch());
                branchId_F = parent.getBranchId();
                subBranchId_F = user.getBranchId();
                user.setParentBranchId(branchId_F);
            }
			}
            		
		   	 }
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText("Userupdate.page"));
		} else {
			if (user != null) {
				User temp = userService.findUser(user.getId());
				if(!ObjectUtil.isEmpty(temp)){
				temp.setUsername(user.getUsername());
				temp.getContactInfo().setType(user.getContactInfo().getType());
				temp.getContactInfo().setAddress1(user.getContactInfo().getAddress1());
				temp.getContactInfo().setAddress2(user.getContactInfo().getAddress2());
				temp.getContactInfo().setZipCode(user.getContactInfo().getZipCode());
				temp.getContactInfo().setPhoneNumber(user.getContactInfo().getPhoneNumber());
				temp.getContactInfo().setMobileNumber(user.getContactInfo().getMobileNumber());
				temp.getContactInfo().setEmail(user.getContactInfo().getEmail());

				/*
				 * System.out.println(user.getContactInfo().getCity().getName())
				 * ; System.out.println(locationService.findMunicipalityByName(
				 * user.getContactInfo() .getCity().getName()));
				 * temp.getContactInfo().setCity(
				 * locationService.findMunicipalityByName(user.getContactInfo().
				 * getCity() .getName()));
				 */

				temp.getContactInfo().setCity(null);
				temp.getPersonalInfo().setFirstName(user.getPersonalInfo().getFirstName());
				temp.getPersonalInfo().setMiddleName(user.getPersonalInfo().getMiddleName());
				temp.getPersonalInfo().setLastName(user.getPersonalInfo().getLastName());
				temp.getPersonalInfo().setIdentityType(user.getPersonalInfo().getIdentityType());
				temp.getPersonalInfo().setIdentityNumber(user.getPersonalInfo().getIdentityNumber());
				temp.getPersonalInfo().setSex(user.getPersonalInfo().getSex());
				temp.getPersonalInfo().setDateOfBirth(user.getPersonalInfo().getDateOfBirth());
				temp.getPersonalInfo().setPlaceOfBirth(user.getPersonalInfo().getPlaceOfBirth());
				temp.getPersonalInfo().setMaritalStatus(user.getPersonalInfo().getMaritalStatus());
				temp.setLanguage(user.getLanguage());
				if (getUserImage() != null){
					temp.getPersonalInfo().setImage(FileUtil.getBinaryFileContent(getUserImage()));

				}
				if(getUserImageExist()!=null && getUserImageExist().equals("1")){
					temp.getPersonalInfo().setImage(null);
				}
				User userCred = userService.findUser(user.getId());
				setCurrentPage(getCurrentPage());
				if (temp == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}

				userService.editUser(temp);
				user.setBranchId(temp.getBranchId());
				userService.editUserCredential(user);
			}else{
				addActionError(getText("user.deleted"));
				return INPUT;
			}
			}
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
			
		}
		return super.execute();
	}

	public void populateImage() {

		try {

			HttpServletResponse response = ServletActionContext.getResponse();
			response.reset();
			response.setContentType("multipart/form-data");
			OutputStream out = response.getOutputStream();
			byte[] imageData = new byte[] {};
			if (StringUtil.isLong(id)) {
				imageData = userService.findUserImage(Long.parseLong(id));
			}

			if (ObjectUtil.isEmpty(imageData) || imageData.length == 0) {
				String logoPath = request.getSession().getServletContext().getRealPath("/img/avatar-small.jpg");
				File pic = new File(logoPath);
				long length = pic.length();
				imageData = new byte[(int) length];
				FileInputStream picIn = new FileInputStream(pic);
				picIn.read(imageData);
			}
			out.write(imageData);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String delete() throws Exception {

		user = userService.findUser(Long.valueOf(id));
		String loggedUserName = (String) request.getSession().getAttribute("user");
		setCurrentPage(getCurrentPage());
		if (user == null) {
			addActionError(NO_RECORD);
			return list();
		}
		if (user != null) {
			if (StringUtil.isEqual(user.getUsername(), loggedUserName)) {
				addActionError(getText("cannotDeleteLoggedUser"));
				request.setAttribute(HEADING, getText(DETAIL));
				if (!ObjectUtil.isEmpty(user.getPersonalInfo())
						&& !StringUtil.isEmpty(user.getPersonalInfo().getDateOfBirth())) {
					dateOfBirth = df.format(user.getPersonalInfo().getDateOfBirth());
				}
				return DETAIL;
			} else {
				if (user.getLoginStatus() != 0) {
					addActionError(getText("cannotDeleteLoggedUser"));
					return DETAIL;
				} else
					userService.removeUser(user);

			}
		}

		return REDIRECT;

	}

	/**
	 * To json.
	 * 
	 * @param obj
	 *            the obj
	 * @return the JSON object
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		User user = (User) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1") && (getIsParentBranch().equals("1")||StringUtil.isEmpty(branchIdValue)))) {

	        
         rows.add(!StringUtil.isEmpty(
                    getBranchesMap().get(getParentBranchMap().get(user.getBranchId())))
                            ? getBranchesMap()
                                    .get(getParentBranchMap().get(user.getBranchId()))
                            : getBranchesMap().get(user.getBranchId()));
        rows.add(!StringUtil.isEmpty(
                getBranchesMap().get(getParentBranchMap().get(user.getBranchId())))
                        ? getBranchesMap().get(user.getBranchId())
                        : "");

		} else {
        if (StringUtil.isEmpty(branchIdValue)) {
            rows.add(branchesMap.get(user.getBranchId()));
        }
    	}
		rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + user.getUsername() + "</font>");
		rows.add(user.getPersonalInfo().getFirstName());
		rows.add(user.getPersonalInfo().getLastName());
		rows.add(user.getContactInfo().getMobileNumber() == null ? "" : user.getContactInfo().getMobileNumber());
		rows.add(!ObjectUtil.isEmpty(user.getRole())
				? (!StringUtil.isEmpty(user.getRole().getName()) ? user.getRole().getName() : "") : "");
		rows.add(user.isEnabled() ? getText("enabled") : getText("disabled"));
		rows.add("<font color=\"#0000FF\">delete</font>");
		jsonObject.put("id", user.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	public Object getData() {

		// To get Contact Type List
		returnContactTypes.put(1, getText("contact1"));
		returnContactTypes.put(2, getText("contact2"));
		// To get Marital Status List
		returnMaritalStatuses.put(PersonalInfo.MARITAL_STATUS_SINGLE, getText("maritalStatus.single"));
		returnMaritalStatuses.put(PersonalInfo.MARITAL_STATUS_MARRIED, getText("maritalStatus.married"));
		// To get Gender Type
		returnSexes.put(PersonalInfo.SEX_MALE, getText("sex.male"));
		returnSexes.put(PersonalInfo.SEX_FEMALE, getText("sex.female"));
		// To get Language List

		List<Language> languages = clientService.listLanguages();
		for (Language language : languages) {
			languageMap.put(language.getCode(), getLocaleProperty(language.getName()));
		}

		// To get Identity type List
		List<IdentityType> listIdentityType = userService.listIdentityType();
		if (!ObjectUtil.isListEmpty(listIdentityType)) {
			for (IdentityType identityType : listIdentityType) {
				identityTypeList.put(identityType.getCode(), getText(identityType.getName()));
			}
		}

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		if (user != null) {
			try {
				if (!StringUtil.isEmpty(dateOfBirth)) {
					user.getPersonalInfo().setDateOfBirth(df.parse(dateOfBirth));
				} else {
					user.getPersonalInfo().setDateOfBirth(null);
				}
				user.setIsMultiBranch(getIsMultiBranch());
				if(getBranchId()!=null){
				user.setParentBranchId(getBranchId());
				}else{
				    user.setParentBranchId(getBranchId_F());
				}
			if( user.getBranchId()!=null && !StringUtil.isEmpty( user.getBranchId())){
				 BranchMaster fm  = clientService.findBranchMasterByBranchId( user.getBranchId());
		            if(fm.getParentBranch()==null || StringUtil.isEmpty(fm.getParentBranch())){
		                branchId_F = user.getBranchId();
		            }else{
		                BranchMaster parent  = clientService.findBranchMasterByBranchIdAndDisableFilter( fm.getParentBranch());
		                branchId_F = parent.getBranchId();
		                subBranchId_F = user.getBranchId();
		                user.setParentBranchId(branchId_F);
		            }
			}  

				// To set the values for selected country,state and district
				/*
				 * Country country = new Country();
				 * country.setName(getSelectedCountry()); State state = new
				 * State(); state.setName(getSelectedState());
				 * state.setCountry(country); Locality locality = new
				 * Locality(); locality.setName(getSelectedLocality());
				 * locality.setState(state);
				 * user.getContactInfo().getCity().setLocality(locality);
				 */
			} catch (ParseException e) {
				e.printStackTrace();

			}
		}

		return user;
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
	 * Gets the roles.
	 * 
	 * @return the roles
	 */
	public List<Role> getRoles() {

		roles = new ArrayList<Role>();
		
		if(subBranchId_F==null || StringUtil.isEmpty(subBranchId_F)){
		if ((ObjectUtil.isEmpty(branchId_F) || branchId_F.equals("-1")) && StringUtil.isEmpty(getBranchId())) {
			roles = new ArrayList<Role>();
			roles = roleService.listRoles();
		} else if ((!ObjectUtil.isEmpty(branchId_F) && !branchId_F.equals("-1"))) {
			if (!ObjectUtil.isEmpty(user)) {
				roles = roleService.listRolesByTypeAndBranchId(roleService.listFilters().get(0).getId(), branchId_F);
			}
		} else {
			if (StringUtil.isEmpty(getSeletedType())) {
				roles = roleService.listRolesByType(roleService.listFilters().get(0).getId());
			} else {
				roles = roleService.listRolesByType(Integer.parseInt(getSeletedType()));
			}
		}
		}else{
		    
		    
	            if (!ObjectUtil.isEmpty(user)) {
	                roles = roleService.listRolesByTypeAndBranchId(roleService.listFilters().get(0).getId(), subBranchId_F);
	            }
	         
		}
		for (Role role : roles) {
			String name = role.getName();
			role.setName(getText(name));
		}
		return roles;
	}

	public void populateRoles() {

		roles = new ArrayList<Role>();
		
	if (!branchId_F.equals("-1")) {
			roles = roleService.listRolesByTypeAndBranchIdExcludeBranch(roleService.listFilters().get(0).getId(), getBranchId_F());
		}
		Map<String, String> map = ReflectUtil.buildMap(roles, new String[] { "id", "name" });
		sendAjaxResponse(JsonUtil.maptoJSONArrayMap(map));
	}

	public Map<String, String> getParentBranches() {
		return clientService.findParentBranches().stream().collect(
				Collectors.toMap(branchId -> String.valueOf(branchId[0]), branchName -> String.valueOf(branchName[1])));
	}

	public Map<String, String> getSubBranchesMap() {
	    String branchId = getBranchId() == null ? getBranchId_F() : StringUtil.isEmpty(getBranchId()) ? getBranchId_F() : getBranchId();
		return clientService.listChildBranchIds(branchId).stream()
				.filter(branch -> !ObjectUtil.isEmpty(branch))
				.collect(Collectors.toMap(BranchMaster::getBranchId, BranchMaster::getName));
	}

	public void populateSubBranches() {
		JSONArray branchArr = new JSONArray();
		if (!StringUtil.isEmpty(getBranchId_F())) {
			clientService.listChildBranchIds(branchId_F).stream().filter(branch -> !ObjectUtil.isEmpty(branch))
					.forEach(branch -> {
						branchArr.add(getJSONObject(branch.getBranchId(), branch.getName()));
					});
		} else {

		}
		sendAjaxResponse(branchArr);
	}

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	/**
	 * Sets the roles.
	 * 
	 * @param roles
	 *            the new roles
	 */
	public void setRoles(List<Role> roles) {

		this.roles = roles;
	}

	/**
	 * Gets the role service.
	 * 
	 * @return the role service
	 */
	public IRoleService getRoleService() {

		return roleService;
	}

	/**
	 * Sets the role service.
	 * 
	 * @param roleService
	 *            the new role service
	 */
	public void setRoleService(IRoleService roleService) {

		this.roleService = roleService;
	}

	/**
	 * Sets the seleted type.
	 * 
	 * @param seletedType
	 *            the new seleted type
	 */
	public void setSeletedType(String seletedType) {

		this.seletedType = seletedType;
	}

	/**
	 * Gets the seleted type.
	 * 
	 * @return the seleted type
	 */
	public String getSeletedType() {

		return seletedType;
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

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(String status) {

		this.status = status;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus() {

		return status;
	}

	/**
	 * Sets the role name.
	 * 
	 * @param roleName
	 *            the new role name
	 */
	public void setRoleName(String roleName) {

		this.roleName = roleName;
	}

	/**
	 * Gets the role name.
	 * 
	 * @return the role name
	 */
	public String getRoleName() {

		return roleName;
	}

	public String getBranchId_F() {

		return branchId_F;
	}

	public void setBranchId_F(String branchId_F) {

		this.branchId_F = branchId_F;
	}

	public String getHiddenRole() {

		return hiddenRole;
	}

	public void setHiddenRole(String hiddenRole) {

		this.hiddenRole = hiddenRole;
	}

	public File getUserImage() {

		return userImage;
	}

	public void setUserImage(File userImage) {

		this.userImage = userImage;
	}

	public PersonalInfo getPersonalInfo() {

		return personalInfo;
	}

	public void setPersonalInfo(PersonalInfo personalInfo) {

		this.personalInfo = personalInfo;
	}

	public String getGetImageFile() {

		return getImageFile;
	}

	public void setGetImageFile(String getImageFile) {

		this.getImageFile = getImageFile;
	}

	public String getUserImageString() {

		return userImageString;
	}

	public void setUserImageString(String userImageString) {

		this.userImageString = userImageString;
	}

	public String getSubBranchId_F() {
		return subBranchId_F;
	}

	public void setSubBranchId_F(String subBranchId_F) {
		this.subBranchId_F = subBranchId_F;
	}

	public String getUserImageExist() {
		return userImageExist;
	}

	public void setUserImageExist(String userImageExist) {
		this.userImageExist = userImageExist;
	}

}
