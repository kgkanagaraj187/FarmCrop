/* * VillageAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.GramPanchayat;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

public class VillageAction extends SwitchValidatorAction {

	private static final long serialVersionUID = -3703638399195601859L;

	private static final Logger LOGGER = Logger.getLogger(VillageAction.class);

	private ILocationService locationService;

	private Village village;
	private String selectedCountry;
	private String selectedState;
	private String selectedDistrict;
	private String selectedCity;
	private String selectedGramPanchayat;
	private String id;
	private String gramPanchayatEnable;
	private String locality;
	List<Locality> localities = new ArrayList<Locality>();
	List<State> states = new ArrayList<State>();
	List<Municipality> cities = new ArrayList<Municipality>();
	List<GramPanchayat> gramPanchayats = new ArrayList<GramPanchayat>();
	private String name;
	private String code;

	private IUniqueIDGenerator idGenerator;
	private IPreferencesService preferncesService;

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

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

		Village filter = new Village();

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

		if (!StringUtil.isEmpty(searchRecord.get("code"))) {
			filter.setCode(searchRecord.get("code").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("name"))) {
			filter.setName(searchRecord.get("name").trim());
		}

		if (!StringUtil.isEmpty(searchRecord.get("selectedGramPanchayat"))) {
			GramPanchayat gramPanchayat = new GramPanchayat();
			gramPanchayat.setName(searchRecord.get("selectedGramPanchayat").trim());
			filter.setGramPanchayat(gramPanchayat);
		}

		if (!StringUtil.isEmpty(searchRecord.get("selectedCity"))) {
			Municipality city = new Municipality();
			city.setName(searchRecord.get("selectedCity").trim());
			filter.setCity(city);
		}

		if (!StringUtil.isEmpty(searchRecord.get("selectedDistrict"))) {
			Municipality city = new Municipality();
			Locality locality = new Locality();
			locality.setName(searchRecord.get("selectedDistrict").trim());
			city.setLocality(locality);
			filter.setCity(city);
		}

		if (!StringUtil.isEmpty(searchRecord.get("selectedState"))) {
			Municipality city = new Municipality();
			Locality locality = new Locality();
			State state = new State();
			state.setName(searchRecord.get("selectedState").trim());
			locality.setState(state);
			city.setLocality(locality);
			filter.setCity(city);
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("selectedCountry"))) {
			Municipality city = new Municipality();
			Locality locality = new Locality();
			State state = new State();
			Country country = new Country();
			country.setName(searchRecord.get("selectedCountry").trim());
			state.setCountry(country);
			locality.setState(state);
			city.setLocality(locality);
			filter.setCity(city);
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
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
		}
		Village village = (Village) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(village.getBranchId())))
						? getBranchesMap().get(getParentBranchMap().get(village.getBranchId()))
						: getBranchesMap().get(village.getBranchId()));
			}
			rows.add(getBranchesMap().get(village.getBranchId()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(village.getBranchId()));
			}
		}
		if(!getCurrentTenantId().equalsIgnoreCase("livelihood")){
		rows.add(village.getCode());
		}
		
		String name = getLanguagePref(getLoggedInUserLanguage(), village.getCode().trim().toString());
		if(!StringUtil.isEmpty(name) && name != null){
			rows.add(name);
		}else{
			rows.add(village.getName());
		}
		

		if (getGramPanchayatEnable().equalsIgnoreCase("1")) {
			String gramName = getLanguagePref(getLoggedInUserLanguage(), village.getGramPanchayat().getCode().trim().toString());
			if(!StringUtil.isEmpty(gramName) && gramName != null){
				rows.add(gramName);
			}else{
				rows.add(village.getGramPanchayat() == null ? "  " :  village.getGramPanchayat().getName());
			}
		}
		
		
		String cityName = getLanguagePref(getLoggedInUserLanguage(), village.getCity().getCode().trim().toString());
		if(!StringUtil.isEmpty(cityName) && cityName != null){
			rows.add(cityName);
		}else{
			rows.add(village.getCity() == null ? "  " : village.getCity().getName());
		}
		
		
		String localityName = getLanguagePref(getLoggedInUserLanguage(), village.getCity().getLocality().getCode().trim().toString());
		if(!StringUtil.isEmpty(localityName) && localityName != null){
			rows.add(localityName);
		}else{
			rows.add(village.getCity().getLocality() == null ? " " : village.getCity().getLocality().getName());
		}
		
		String stateName = getLanguagePref(getLoggedInUserLanguage(), village.getCity().getLocality().getState().getCode().trim().toString());
		if(!StringUtil.isEmpty(stateName) && stateName != null){
			rows.add(stateName);
		}else{
			rows.add(village.getCity().getLocality().getState() == null ? " "
					: village.getCity().getLocality().getState().getName());
		}
		
		
		String countryName = getLanguagePref(getLoggedInUserLanguage(), village.getCity().getLocality().getState().getCountry().getCode().trim().toString());
		if(!StringUtil.isEmpty(countryName) && countryName != null){
			rows.add(countryName);
		}else{
			rows.add(village.getCity().getLocality().getState().getCountry() == null ? " "
					: village.getCity().getLocality().getState().getCountry().getName());
		}
		
		
		jsonObject.put("id", village.getId());
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
		String codeGenType = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
			codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);

		}
		if (village == null) {

			command = CREATE;
			request.setAttribute(HEADING, getText(CREATE));
			return LIST;
		} else {

			Municipality city = locationService.findMunicipalityByCode(village.getCity().getCode());
			village.setCity(city);
			village.setBranchId(getBranchId());
			if (gramPanchayatEnable != null) {
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					GramPanchayat gramPanchayat = locationService
							.findGrampanchaythByCode(village.getGramPanchayat().getCode());
					village.setGramPanchayat(gramPanchayat);

					if (codeGenType.equals("1") ) {
						String villageCode = idGenerator.getVillageHHIdSeq(gramPanchayat.getCode());
						BigInteger codeSeq = new BigInteger(villageCode);
						String maxCode = codeSeq.subtract(new BigInteger("1")).toString();
						if (Integer.valueOf(maxCode.substring(4, 6)) >= 99) {
							addActionError(getText("error.villageExceed"));
							return INPUT;
						} else {
							village.setCode(idGenerator.getVillageHHIdSeq(gramPanchayat.getCode()));
						}
					} else {
						village.setCode(idGenerator.getVillageIdSeq());
					}

				} else {
					village.setCode(idGenerator.getVillageIdSeq());
				}

			} else {
				village.setGramPanchayat(null);
			}
			locationService.addVillage(village);
			return REDIRECT;
		}
	}

/*	@SuppressWarnings("unchecked")
	public String populateSaveVillage() {
		village = new Village();
		String codeGenType = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
			codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);
		}

		if (!StringUtil.isEmpty(selectedCity)) {
			village.setName(getName());
			Village tempVillage=locationService.findVillageByName(getName());
			if(ObjectUtil.isEmpty(tempVillage) || !ObjectUtil.isEmpty(tempVillage) && !ObjectUtil.isEmpty(tempVillage.getCity()) )
			{
				
		
			Municipality city = locationService.findMunicipalityByCode(selectedCity);
			village.setCity(city);
			village.setBranchId(getBranchId());
			if (gramPanchayatEnable != null) {
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					GramPanchayat gramPanchayat = locationService.findGrampanchaythByCode(getSelectedGramPanchayat());
					village.setGramPanchayat(gramPanchayat);

					village.setCode(idGenerator.getVillageIdSeq());

				} else {
					village.setCode(idGenerator.getVillageIdSeq());
				}

			}
			locationService.addVillage(village);
			getJsonObject().put("msg", getText("msg.added"));
			getJsonObject().put("title", getText("title.success"));
			
			}
			else
			{
				
				getJsonObject().put("status", "0");
				getJsonObject().put("msg", getLocaleProperty("msg.stateDuplicateError"));  
			     getJsonObject().put("title", getText("title.error"));
			}
		} else {
			addActionError(getText("empty.city"));

		}

		
		sendAjaxResponse(getJsonObject());
		return null;
	}*/
	
	@SuppressWarnings("unchecked")
	public String populateSaveVillage() {
		village = new Village();
		String codeGenType = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
			codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);
		}

		if (!StringUtil.isEmpty(selectedCity)) {
			village.setName(getName());
			Municipality city = locationService.findMunicipalityByCode(selectedCity);
			village.setCity(city);
			village.setBranchId(getBranchId());
			if (gramPanchayatEnable != null) {
				if (gramPanchayatEnable.equalsIgnoreCase("1")) {
					GramPanchayat gramPanchayat = locationService.findGrampanchaythByCode(getSelectedGramPanchayat());
					village.setGramPanchayat(gramPanchayat);

					village.setCode(idGenerator.getVillageIdSeq());

				} else {
					if(getCurrentTenantId().equalsIgnoreCase("symrise") || getCurrentTenantId().equalsIgnoreCase(ESESystem.AVT) ){
						village.setCode(getCode());
						if(getCurrentTenantId().equalsIgnoreCase(ESESystem.AVT)){
							village.setSeq("1");
						}
						else{
						village.setSeq("100");
						}
					}else{
					village.setCode(idGenerator.getVillageIdSeq());
					if(getCurrentTenantId().equalsIgnoreCase("griffith")){
						village.setSeq("1");
					}
					}
				}

			}
			
			if(getCurrentTenantId().equalsIgnoreCase("symrise") || getCurrentTenantId().equalsIgnoreCase(ESESystem.AVT)){
				Village villageExist=locationService.findVillageByCode(village.getCode());
				Village v = locationService.findVillageByName(village.getName());
				if(village.getName()==null || StringUtil.isEmpty(village.getName())){
					getJsonObject().put("status", "0"); 
					getJsonObject().put("msg", getText("msg.nameEmpty"));  
				    getJsonObject().put("title", getText("title.error"));
				}else if(village.getCode()==null || StringUtil.isEmpty(village.getCode())){
					getJsonObject().put("status", "0"); 
					getJsonObject().put("msg", getText("msg.codeEmpty"));  
				    getJsonObject().put("title", getText("title.error"));
				}
				else if(ObjectUtil.isEmpty(v)&&ObjectUtil.isEmpty(villageExist)){
					locationService.addVillage(village);
						getJsonObject().put("status", "1"); 
						getJsonObject().put("msg", getText("msg.added"));  
					    getJsonObject().put("title", getText("title.success"));
					}
				else{
					getJsonObject().put("status", "0");
					getJsonObject().put("msg", getLocaleProperty("msg.villageDuplicateError"));  
				    getJsonObject().put("title", getText("title.error"));
				}
			}else{
			Village v = locationService.findVillageByNameAndCity(village.getName(), village.getCity().getName());
			if(village.getName()==null || StringUtil.isEmpty(village.getName())){
				getJsonObject().put("status", "0"); 
				getJsonObject().put("msg", getText("msg.nameEmpty"));  
			    getJsonObject().put("title", getText("title.error"));
			}else if(ObjectUtil.isEmpty(v)){
				locationService.addVillage(village);
					getJsonObject().put("status", "1"); 
					getJsonObject().put("msg", getText("msg.added"));  
				    getJsonObject().put("title", getText("title.success"));
				}
			else{
				getJsonObject().put("status", "0");
				getJsonObject().put("msg", getLocaleProperty("msg.villageDuplicateError"));  
			    getJsonObject().put("title", getText("title.error"));
			}
			
		} 
		}
		sendAjaxResponse(getJsonObject());
		return null;
	}

	/**
	 * Update.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	public String update() throws Exception {

		if (id != null && !id.equals("")) {
			village = locationService.findVillageById(Long.parseLong(id));
			if (village == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}

			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
			}

			/*
			 * setSelectedCountry(village.getCity().getLocality().getState().
			 * getCountry().getName());
			 * setSelectedState(village.getCity().getLocality().getState().
			 * getName());
			 * setSelectedDistrict(village.getCity().getLocality().getName());
			 * setSelectedCity(village.getCity().getName());
			 */
			if (getGramPanchayatEnable().equalsIgnoreCase("1") && !StringUtil.isEmpty(village.getGramPanchayat())) {
				setSelectedCountry(
						village.getGramPanchayat().getCity().getLocality().getState().getCountry().getName());
				setSelectedState(village.getGramPanchayat().getCity().getLocality().getState().getCode());
				setSelectedDistrict(village.getGramPanchayat().getCity().getLocality().getCode());
				setSelectedCity(village.getGramPanchayat().getCity().getCode());
				setSelectedGramPanchayat(village.getGramPanchayat().getCode());
			} else {
				setSelectedCountry(village.getCity().getLocality().getState().getCountry().getName());
				setSelectedState(village.getCity().getLocality().getState().getCode());
				setSelectedDistrict(village.getCity().getLocality().getCode());
				setSelectedCity(village.getCity().getCode());
			}

			setCurrentPage(getCurrentPage());
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getText(UPDATE));
		} else {
			ESESystem preferences = preferncesService.findPrefernceById("1");
			String codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);
			if (village != null) {
				Village existing = locationService.findVillageById(village.getId());

				if (existing == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				setCurrentPage(getCurrentPage());
				existing.setName(village.getName());

				Municipality city = locationService.findMunicipalityByCode(village.getCity().getCode());
				existing.setCity(city);
				if (gramPanchayatEnable != null) {
					if (gramPanchayatEnable.equalsIgnoreCase("1")) {

						if (codeGenType.equals("1")) {
							if (!village.getGramPanchayat().getName()
									.equalsIgnoreCase(existing.getGramPanchayat().getName())) {
								String villageCodeSeq = idGenerator
										.getVillageHHIdSeq(existing.getGramPanchayat().getCode());
								existing.setCode(villageCodeSeq);
							}
						}

						GramPanchayat gramPanchayat = locationService
								.findGrampanchaythByCode(village.getGramPanchayat().getCode());
						existing.setGramPanchayat(gramPanchayat);

					}
				}
				locationService.editVillage(existing);

				getJsonObject().put("msg", getText("msg.updated"));
				getJsonObject().put("title", getText("title.success"));

				sendAjaxResponse(getJsonObject());
			}
			command = UPDATE;
			request.setAttribute(HEADING, getText(LIST));
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
	@SuppressWarnings("unchecked")
	public String delete() throws Exception {

		if (id != null && !id.equals("")) {
			village = locationService.findVillageById(Long.parseLong(id));
			if (ObjectUtil.isEmpty(village)) {
				addActionError(NO_RECORD);
				
			}

			String flag = locationService.isVillageMappingExist(village.getId());

			if (!StringUtil.isEmpty(flag)) {
				// addActionError(getLocaleProperty(flag));
				getJsonObject().put("msg", getLocaleProperty(flag));
				getJsonObject().put("title", getText("title.success"));
				sendAjaxResponse(getJsonObject());
				return null;
			}

			boolean isCooperativeMappingExist = locationService.isVillageMappedWithCooperative(village.getId());

			if (isCooperativeMappingExist) {
				// addActionError(getLocaleProperty("cooperative.mapped"));
				getJsonObject().put("msg", getLocaleProperty("cooperative.mapped"));
				getJsonObject().put("title", getText("title.success"));
				sendAjaxResponse(getJsonObject());
				return null;
			}

			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
			}

			if (getGramPanchayatEnable().equalsIgnoreCase("1") && !StringUtil.isEmpty(village.getGramPanchayat())) {

				if (village.getGramPanchayat().getVillages().contains(village)) {
					village.getGramPanchayat().getVillages().remove(village);
				}
				locationService.editGramPanchayat(village.getGramPanchayat());

				if (village.getCity() != null && !ObjectUtil.isEmpty(village.getCity())) {
					if (village.getCity().getVillages().contains(village)) {
						village.getCity().getVillages().remove(village);
					}
				}
				locationService.editMunicipality(village.getCity());

			} else {
				if (village.getCity() != null && !ObjectUtil.isEmpty(village.getCity())) {
					if (village.getCity().getVillages().contains(village)) {
						village.getCity().getVillages().remove(village);
					}
				}
				locationService.editMunicipality(village.getCity());
			}

			locationService.removeVillage(village);
			if (getJsonObject() != null && !StringUtil.isEmpty(getJsonObject().get("msg"))) {
				getJsonObject().put("msg", getText("msg.deleted"));
				getJsonObject().put("title", getText("title.success"));
			}

			sendAjaxResponse(getJsonObject());
		}
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

		String view = "";
		if (id != null && !id.equals("")) {
			village = locationService.findVillageById(Long.parseLong(id));
			ESESystem preferences = preferncesService.findPrefernceById("1");
			if (!StringUtil.isEmpty(preferences)) {
				setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
			}
			if (village == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getText(DETAIL));
		} else {
			request.setAttribute(HEADING, getText(LIST));
			return LIST;
		}
		return view;
	}

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {

		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			setGramPanchayatEnable(preferences.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
		}

		if (ObjectUtil.isEmpty(village)) {
			return null;
		} else {
			Country country = new Country();
			country.setName(this.selectedCountry);

			State state = new State();
			state.setCode(this.selectedState);
			state.setCountry(country);

			Locality locality = new Locality();
			locality.setCode(this.selectedDistrict);
			locality.setState(state);

			Municipality city = new Municipality();
			city.setCode(this.selectedCity);
			city.setLocality(locality);

			GramPanchayat gramPanchayat = new GramPanchayat();
			if (gramPanchayatEnable != null) {
				if (getGramPanchayatEnable().equalsIgnoreCase("1")) {
					gramPanchayat.setCode(village.getGramPanchayat().getCode());
					gramPanchayat.setCity(city);
					village.setGramPanchayat(gramPanchayat);
				}

			}
			// city.setName(village.getCity().getName());
			// city.setLocality(locality);
			village.setCity(city);
			return village;
		}

	}

	@SuppressWarnings("unchecked")
	public void populateVillageUpdate() {
		if (!StringUtil.isEmpty(getId())) {
			Village village = locationService.findVillageById(Long.valueOf(getId()));
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)){
				/*if(code!=null && !StringUtil.isEmpty(code)){
					Village vil=locationService.findVillageByCode(code);
					if(vil!=null && !ObjectUtil.isEmpty(vil)){
						getJsonObject().put("msg", getText("msg.codeDuplicate"));  
					     getJsonObject().put("title", getText("title.error"));
					     sendAjaxResponse(getJsonObject());
					}
					else{*/
						if (!ObjectUtil.isEmpty(village)) {
							village.setCode(code);
							village.setName(getName());
							Municipality city = locationService.findMunicipalityByCode(getSelectedCity());
							GramPanchayat gp = locationService.findGrampanchaythByCode(getSelectedGramPanchayat());
							village.setCity(city);
							village.setGramPanchayat(gp);
							village.setRevisionNo(DateUtil.getRevisionNumber());
							locationService.editVillage(village);

							getJsonObject().put("msg", getText("msg.updated"));
							getJsonObject().put("title", getText("title.success"));
							sendAjaxResponse(getJsonObject());
						/*}
					}*/
				}
			}
			else{
			if (!StringUtil.isEmpty(village)) {
				village.setName(getName());
				Municipality city = locationService.findMunicipalityByCode(getSelectedCity());
				GramPanchayat gp = locationService.findGrampanchaythByCode(getSelectedGramPanchayat());
				village.setCity(city);
				village.setGramPanchayat(gp);
				village.setRevisionNo(DateUtil.getRevisionNumber());
				locationService.editVillage(village);

				getJsonObject().put("msg", getText("msg.updated"));
				getJsonObject().put("title", getText("title.success"));
				sendAjaxResponse(getJsonObject());
			}
			}
		}
	}

	/**
	 * Gets the countries.
	 * 
	 * @return the countries
	 */
	/*
	 * public List<Country> getCountries() {
	 * 
	 * List<Country> returnValue = locationService.listCountries(); return
	 * returnValue; }
	 */
	public Map<String, String> getCountriesList() {
		Map<String, String> countryMap = new LinkedHashMap<String, String>();
		List<Country> returnValue = locationService.listCountries();
		for (Country c : returnValue) {
			countryMap.put(c.getCode(), c.getName());
		}
		return countryMap;
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
	 *            the new village
	 */
	public void setVillage(Village village) {

		this.village = village;
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
	 * Gets the selected district.
	 * 
	 * @return the selected district
	 */
	public String getSelectedDistrict() {

		return selectedDistrict;
	}

	/**
	 * Sets the selected district.
	 * 
	 * @param selectedDistrict
	 *            the new selected district
	 */
	public void setSelectedDistrict(String selectedDistrict) {

		this.selectedDistrict = selectedDistrict;
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
	 * Gets the localities.
	 * 
	 * @return the localities
	 */
	/*
	 * public List<Locality> getLocalities() {
	 * 
	 * if (!StringUtil.isEmpty(selectedState)) { localities =
	 * locationService.listLocalities(selectedState); } return localities; }
	 */

	public Map<String, String> getLocalities() {
		Map<String, String> locality = new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedState)) {
			localities = locationService.listLocalities(selectedState);
			for (Locality loc : localities) {
				locality.put(loc.getCode(), loc.getCode() + " - " + loc.getName());
			}
		}
		return locality;
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
	 * Gets the states.
	 * 
	 * @return the states
	 */
	/*
	 * public List<State> getStates() {
	 * 
	 * if (!StringUtil.isEmpty(selectedCountry)) { states =
	 * locationService.listStates(selectedCountry); }
	 * 
	 * return states; }
	 */

	public Map<String, String> getStates() {
		Map<String, String> state = new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedCountry)) {
			states = locationService.listStates(selectedCountry);
			for (State s : states) {
				state.put(s.getCode(), s.getCode() + " - " + s.getName());
			}
		}
		return state;
	}

	/**
	 * Sets the states.
	 * 
	 * @param states
	 *            the new states
	 */
	public void setStates(List<State> states) {

		this.states = states;
	}

	/**
	 * Gets the cities.
	 * 
	 * @return the cities
	 */
	/*
	 * public List<Municipality> getCities() {
	 * 
	 * if (!StringUtil.isEmpty(selectedDistrict)) { cities =
	 * locationService.listMunicipalitiesByCode(selectedDistrict); } return
	 * cities; }
	 */

	public Map<String, String> getCities() {
		Map<String, String> city = new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedDistrict)) {
			cities = locationService.listMunicipalitiesByCode(selectedDistrict);
			for (Municipality muncipality : cities) {
				city.put(muncipality.getCode(), muncipality.getCode() + " - " + muncipality.getName());
			}
		}
		return city;
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

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	public void populateCountryList() {
		JSONObject jsonObject = new JSONObject();

		locationService.listCountries().stream().forEach(country -> {
			jsonObject.put(country.getCode(), country.getName());
		});

		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void populateStateList() {
		JSONObject jsonObject = new JSONObject();

		locationService.listOfStates().stream().forEach(state -> {
			jsonObject.put(state.getCode(), state.getName());
		});

		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void populateLocalityList() {
		JSONObject jsonObject = new JSONObject();
		locationService.listLocalityIdCodeAndName().stream().forEach(cities -> {
			Object[] cityArr = (Object[]) cities;
			jsonObject.put(cityArr[1], cityArr[2]);
		});

		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void populateCityList() {
		JSONObject jsonObject = new JSONObject();
		locationService.listCityCodeAndName().stream().forEach(cities -> {
			Object[] cityArr = (Object[]) cities;
			jsonObject.put(cityArr[0], cityArr[1]);
		});

		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void populateGrampanchayatList() {
		JSONObject jsonObject = new JSONObject();
		locationService.listGramPanchayatIdCodeName().stream().forEach(cities -> {
			Object[] cityArr = (Object[]) cities;
			jsonObject.put(cityArr[1], cityArr[2]);
		});

		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(jsonObject.toString().replace("\"", "").replace(",", ";"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Populate state.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateState() throws Exception {

		if (!StringUtil.isEmpty(selectedCountry)) {
			states = locationService.listStatesByCode(selectedCountry.trim());
		}
		JSONArray stateArr = new JSONArray();
		if (!ObjectUtil.isEmpty(states)) {
			for (State state : states) {
				stateArr.add(getJSONObject(state.getCode(), state.getCode() + " - " + state.getName()));
			}
		}
		sendAjaxResponse(stateArr);
	}

	/**
	 * Populate locality.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateLocality() throws Exception {

		if (!selectedState.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedState))) {
			localities = locationService.listLocalities(selectedState.trim());
		}
		JSONArray localtiesArr = new JSONArray();
		if (!ObjectUtil.isEmpty(localities)) {
			for (Locality locality : localities) {
				localtiesArr.add(getJSONObject(locality.getCode(), locality.getCode() + " - " + locality.getName()));
			}
		}
		sendAjaxResponse(localtiesArr);
	}

	/**
	 * Populate city.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateCity() throws Exception {

		if ((!StringUtil.isEmpty(selectedDistrict) && !selectedDistrict.equalsIgnoreCase("null"))) {
			cities = locationService.listMunicipalitiesByCode(selectedDistrict);
		}
		JSONArray cityArray = new JSONArray();
		if (!ObjectUtil.isEmpty(cities)) {
			for (Municipality municipality : cities) {
				cityArray.add(
						getJSONObject(municipality.getCode(), municipality.getCode() + " - " + municipality.getName()));
			}
		}
		sendAjaxResponse(cityArray);
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
	 * Sets the selected city.
	 * 
	 * @param selectedCity
	 *            the new selected city
	 */
	public void setSelectedCity(String selectedCity) {

		this.selectedCity = selectedCity;
	}

	public Map<String, String> getGramPanchayats() {
		Map<String, String> gramPanchayat = new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedCity)) {
			gramPanchayats = locationService.listGramPanchayatsByCode(selectedCity);
			for (GramPanchayat gram : gramPanchayats) {
				gramPanchayat.put(gram.getCode(), gram.getCode() + " - " + gram.getName());
			}
		}
		return gramPanchayat;
	}

	/**
	 * Sets the gram panchayats.
	 * 
	 * @param gramPanchayats
	 *            the new gram panchayats
	 */
	public void setGramPanchayats(List<GramPanchayat> gramPanchayats) {

		this.gramPanchayats = gramPanchayats;
	}

	/**
	 * Populate gram panchayats.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateGramPanchayat() throws Exception {

		if ((!StringUtil.isEmpty(selectedCity) && !selectedCity.equalsIgnoreCase("null"))) {
			gramPanchayats = locationService.listGramPanchayatsByCityCode(selectedCity);
		}
		JSONArray gramPanchayatArray = new JSONArray();
		if (!ObjectUtil.isEmpty(gramPanchayats)) {
			for (GramPanchayat gramPanchayat : gramPanchayats) {
				gramPanchayatArray.add(getJSONObject(gramPanchayat.getCode(),
						gramPanchayat.getCode() + " - " + gramPanchayat.getName()));
			}
		}
		sendAjaxResponse(gramPanchayatArray);
	}

	/**
	 * Gets the selected gram panchayat.
	 * 
	 * @return the selected gram panchayat
	 */
	public String getSelectedGramPanchayat() {

		return selectedGramPanchayat;
	}

	/**
	 * Sets the selected gram panchayat.
	 * 
	 * @param selectedGramPanchayat
	 *            the new selected gram panchayat
	 */
	public void setSelectedGramPanchayat(String selectedGramPanchayat) {

		this.selectedGramPanchayat = selectedGramPanchayat;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
	}

	public String getGramPanchayatEnable() {
		return gramPanchayatEnable;
	}

	public void setGramPanchayatEnable(String gramPanchayatEnable) {
		this.gramPanchayatEnable = gramPanchayatEnable;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
