/*
 * MunicipalityAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general;

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
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.service.IUserService;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.ServicePointType;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class MunicipalityAction.
 */
public class MunicipalityAction extends SwitchValidatorAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(MunicipalityAction.class);

	private String id;
	private String selectedCountry;
	private String selectedState;
	private String selecteddropdwon;
	private String selectedDistrict;
	private String locality;
	private ILocationService locationService;
	private IServicePointService servicePointService;
	private IAgentService agentService;
	private IClientService clientService;
	private IUserService userService;
	private IProductDistributionService productDistributionService;
	private IPreferencesService preferncesService;
	private Municipality municipality;
	List<Locality> localities = new ArrayList<Locality>();
	List<State> stats = new ArrayList<State>();
	private String code;

	private IUniqueIDGenerator idGenerator;

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
	 * @see com.sourcetrace.esesw.view.SwitchAction#setUserService(com.ese.service.user.IUserService)
	 */
	public void setUserService(IUserService userService) {

		this.userService = userService;
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
	 * Gets the agent service.
	 * 
	 * @return the agent service
	 */
	public IAgentService getAgentService() {

		return agentService;
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
	 * Gets the client service.
	 * 
	 * @return the client service
	 */
	public IClientService getClientService() {

		return clientService;
	}

	/**
	 * Sets the client service.
	 * 
	 * @param clientService
	 *            the new client service
	 */
	public void setClientService(IClientService clientService) {

		this.clientService = clientService;
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
	 * Gets the municipality.
	 * 
	 * @return the municipality
	 */
	public Municipality getMunicipality() {

		return municipality;
	}

	/**
	 * Sets the municipality.
	 * 
	 * @param municipality
	 *            the new municipality
	 */
	public void setMunicipality(Municipality municipality) {

		this.municipality = municipality;
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
	 * Gets the selecteddropdwon.
	 * 
	 * @return the selecteddropdwon
	 */
	public String getSelecteddropdwon() {

		return selecteddropdwon;
	}

	/**
	 * Sets the selecteddropdwon.
	 * 
	 * @param selecteddropdwon
	 *            the new selecteddropdwon
	 */
	public void setSelecteddropdwon(String selecteddropdwon) {

		this.selecteddropdwon = selecteddropdwon;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#data()
	 */
	public String data() throws Exception {

		Map<String, String> searchRecord = getJQGridRequestParam(); // get the
																	// search
																	// parameter
																	// with
		// value

		Municipality filter = new Municipality();

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

		/*
		 * if (!StringUtil.isEmpty(searchRecord.get("latitude"))) {
		 * filter.setLatitude(searchRecord.get("latitude").trim()); }
		 * 
		 * if (!StringUtil.isEmpty(searchRecord.get("longitude"))) {
		 * filter.setLongitude(searchRecord.get("longitude").trim()); }
		 */

		if (!StringUtil.isEmpty(searchRecord.get("selectedDistrict"))) {
			Locality locality = new Locality();
			locality.setName(searchRecord.get("selectedDistrict").trim());
			filter.setLocality(locality);
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("selectedState"))) {
			filter.setStateName(searchRecord.get("selectedState").trim());;
		}
		
		if (!StringUtil.isEmpty(searchRecord.get("selectedCountry"))) {
			filter.setCountryName(searchRecord.get("selectedCountry").trim());;
		}
		
		Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
				getPage());

		return sendJQGridJSONResponse(data);
	}

	/**
	 * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON(Object obj) {

		Municipality municipality = (Municipality) obj;
		JSONObject jsonObject = new JSONObject();
		JSONArray rows = new JSONArray();
		if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(municipality.getBranchId())))
						? getBranchesMap().get(getParentBranchMap().get(municipality.getBranchId()))
						: getBranchesMap().get(municipality.getBranchId()));
			}
			rows.add(getBranchesMap().get(municipality.getBranchId()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(municipality.getBranchId()));
			}
		}
		if(!getCurrentTenantId().equalsIgnoreCase("livelihood")){
		rows.add(municipality.getCode());
		}
		//rows.add(municipality.getName());
		
		String name = getLanguagePref(getLoggedInUserLanguage(), municipality.getCode().trim().toString());
		if(!StringUtil.isEmpty(name) && name != null){
			rows.add(name);
		}else{
			rows.add(municipality.getName());
		}
		
		
		String locatlityName = getLanguagePref(getLoggedInUserLanguage(), municipality.getLocality().getCode().trim().toString());
		if(!StringUtil.isEmpty(locatlityName) && locatlityName != null){
			rows.add(locatlityName);
		}else{
			rows.add(municipality.getLocality().getName());
		}
		
		String stateName = getLanguagePref(getLoggedInUserLanguage(), municipality.getLocality().getState().getCode().trim().toString());
		if(!StringUtil.isEmpty(stateName) && stateName != null){
			rows.add(stateName);
		}else{
			rows.add(municipality.getLocality().getState().getName());
		}
		
		String countryName = getLanguagePref(getLoggedInUserLanguage(), municipality.getLocality().getState().getCountry().getCode().trim().toString());
		if(!StringUtil.isEmpty(countryName) && countryName != null){
			rows.add(countryName);
		}else{
			rows.add(municipality.getLocality().getState().getCountry().getName());
		}
		
		
		jsonObject.put("id", municipality.getId());
		jsonObject.put("cell", rows);
		return jsonObject;
	}

	/*@SuppressWarnings("unchecked")
	public void populateSaveCity() {
		Municipality city = new Municipality();
		String codeGenType = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			
			 * setGramPanchayatEnable(preferences.getPreferences().get(ESESystem
			 * .ENABLE_GRAMPANJAYAT));
			 
			codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);
		}
		if (!StringUtil.isEmpty(selectedDistrict)) {
			Locality locality = locationService.findLocalityByCode(selectedDistrict);
			city.setName(getName());
			city.setLocality(locality);
			city.setBranchId(getBranchId());
			city.setRevisionNo(DateUtil.getRevisionNumber());

			if (codeGenType.equals("1") ) {
				String municipalityCodeSeq = idGenerator.getMunicipalityHHIdSeq(locality.getCode());
				BigInteger codeSeq = new BigInteger(municipalityCodeSeq);
				String maxCode = codeSeq.subtract(new BigInteger("1")).toString();
				if (Integer.valueOf(maxCode.substring(1, 2)) >= 9) {
					// addActionError(getLocaleProperty("error.municipalityExceed"));
					getJsonObject().put("msg", getLocaleProperty("error.municipalityExceed"));
					getJsonObject().put("title", getText("title.error"));
				} else {
					city.setCode(municipalityCodeSeq);

				}
			} else if (!getCurrentTenantId().equalsIgnoreCase("awi")) {
				city.setCode(idGenerator.getMandalIdSeq());
			}
			locationService.addMunicipality(city);
			getJsonObject().put("msg", getText("msg.added"));
			getJsonObject().put("title", getText("title.success"));
			sendAjaxResponse(getJsonObject());
		}
	}*/

	@SuppressWarnings("unchecked")
	public void populateSaveCity() {
		Municipality city = new Municipality();
		String codeGenType = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			/*
			 * setGramPanchayatEnable(preferences.getPreferences().get(ESESystem
			 * .ENABLE_GRAMPANJAYAT));
			 */
			codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);
		}
		if (!StringUtil.isEmpty(selectedDistrict)) {
			Locality locality = locationService.findLocalityByCode(selectedDistrict);
			city.setName(getName());
			city.setLocality(locality);
			city.setBranchId(getBranchId());
			city.setRevisionNo(DateUtil.getRevisionNumber());
			if (codeGenType.equals("1") ) {
				String municipalityCodeSeq = idGenerator.getMunicipalityHHIdSeq(locality.getCode());
				BigInteger codeSeq = new BigInteger(municipalityCodeSeq);
				String maxCode = codeSeq.subtract(new BigInteger("1")).toString();
				if (Integer.valueOf(maxCode.substring(1, 2)) >= 9) {
					// addActionError(getLocaleProperty("error.municipalityExceed"));
					getJsonObject().put("msg", getLocaleProperty("error.municipalityExceed"));
					getJsonObject().put("title", getText("title.error"));
				} else {
					city.setCode(municipalityCodeSeq);

				}
			}
				city.setCode(idGenerator.getMandalIdSeq());
			
				Municipality c = locationService.findMunicipalityByName(city.getName());
				if(city.getName()==null || StringUtil.isEmpty(city.getName())){
						getJsonObject().put("status", "0"); 
						getJsonObject().put("msg", getText("msg.nameEmpty"));  
					    getJsonObject().put("title", getText("title.error"));
					}
				 else if(ObjectUtil.isEmpty(c)){
					    locationService.addMunicipality(city);
						getJsonObject().put("status", "1"); 
						getJsonObject().put("msg", getText("msg.added"));  
					    getJsonObject().put("title", getText("title.success"));
					}
				else{
						getJsonObject().put("status", "0");
						getJsonObject().put("msg", getLocaleProperty("msg.cityDuplicateError"));  
					    getJsonObject().put("title", getText("title.error"));
					}
			
			sendAjaxResponse(getJsonObject());
		}
	}

	
	
	
	@SuppressWarnings("unchecked")
	public void populateCityUpdate() {
		if (!StringUtil.isEmpty(getId())) {
			// Village village =
			// locationService.findVillageById(Long.valueOf(getId()));
			Municipality city = locationService.findMunicipalityById(Long.valueOf(getId()));
			if(getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)){
		/*		if(code!=null && !StringUtil.isEmpty(code)){
					Municipality m=locationService.findMunicipalityByCode(code);	
					if(m!=null && !ObjectUtil.isEmpty(m)){
						getJsonObject().put("msg", getText("msg.codeDuplicate"));  
					     getJsonObject().put("title", getText("title.error"));
					     sendAjaxResponse(getJsonObject());
					}
					else{*/
						if (!StringUtil.isEmpty(city)) {
						city.setCode(code);
						city.setName(getName());
						Locality locality = locationService.findLocalityByCode(getSelectedDistrict());
						city.setLocality(locality);
						city.setRevisionNo(DateUtil.getRevisionNumber());
						locationService.editMunicipality(city);
						getJsonObject().put("msg", getText("msg.updated"));
						getJsonObject().put("title", getText("title.success"));
						sendAjaxResponse(getJsonObject());
						}
					/*}
				}*/
			}
			else{
			if (!StringUtil.isEmpty(city)) {
				city.setName(getName());
				Locality locality = locationService.findLocalityByCode(getSelectedDistrict());
				city.setLocality(locality);
				city.setRevisionNo(DateUtil.getRevisionNumber());
				locationService.editMunicipality(city);
				getJsonObject().put("msg", getText("msg.updated"));
				getJsonObject().put("title", getText("title.success"));
				sendAjaxResponse(getJsonObject());
			}
		}
		}
	}

	public String create() throws Exception {

		if (municipality == null) {

			command = CREATE;
			request.setAttribute(HEADING, getLocaleProperty(CREATE));
			return INPUT;
		} else {

			Locality locality = locationService.findLocalityByCode(municipality.getLocality().getCode());

			ESESystem preferences = preferncesService.findPrefernceById("1");
			String codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);
			if (codeGenType.equals("1")) {
				String municipalityCodeSeq = idGenerator.getMunicipalityHHIdSeq(locality.getCode());
				BigInteger codeSeq = new BigInteger(municipalityCodeSeq);
				String maxCode = codeSeq.subtract(new BigInteger("1")).toString();
				if (Integer.valueOf(maxCode.substring(1, 2)) >= 9) {
					addActionError(getLocaleProperty("error.municipalityExceed"));
					return INPUT;
				} else {
					municipality.setCode(municipalityCodeSeq);

				}
			} else if (!getCurrentTenantId().equalsIgnoreCase("awi")) {
				municipality.setCode(idGenerator.getMandalIdSeq());
			}

			municipality.setLocality(locality);
			municipality.setBranchId(getBranchId());
			locationService.addMunicipality(municipality);
			// Added for Adding Service Point when creating Municipatity
			// addServicePoint(municipality);
			return REDIRECT;
		}
	}

	/**
	 * Adds the warehouse.
	 * 
	 * @param municipality
	 *            the municipality
	 */
	private void addWarehouse(Municipality municipality) {

		Warehouse warehouse = new Warehouse();
		warehouse.setCode(municipality.getCode());
		warehouse.setName(municipality.getName());
		// warehouse.setCity(municipality);
		locationService.addWarehouse(warehouse);
	}

	/**
	 * Update.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String update() throws Exception {

		if (id != null && !id.equals("")) {
			municipality = locationService.findMunicipalityById(Long.valueOf(id));
			if (municipality == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setSelectedCountry(municipality.getLocality().getState().getCountry().getName());
			setSelectedState(municipality.getLocality().getState().getCode());
			localities = locationService.listLocalities(municipality.getLocality().getState().getName());
			municipality.getLocality().setName(municipality.getLocality().getName());
			setCurrentPage(getCurrentPage());
			id = null;
			command = UPDATE;
			request.setAttribute(HEADING, getLocaleProperty(UPDATE));
		} else {
			ESESystem preferences = preferncesService.findPrefernceById("1");
			String codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);
			if (municipality != null) {
				Municipality temp1 = locationService.findMunicipalityById(municipality.getId());
				if (temp1 == null) {
					addActionError(NO_RECORD);
					return REDIRECT;
				}
				setCurrentPage(getCurrentPage());

				if (codeGenType.equals("1") ) {
					if (!municipality.getLocality().getName().equalsIgnoreCase(temp1.getLocality().getName())) {
						String municipalityCodeSeq = idGenerator.getMunicipalityHHIdSeq(temp1.getLocality().getCode());
						temp1.setCode(municipalityCodeSeq);
					}
				}

				// temp1.setCode(municipality.getCode());
				temp1.setName(municipality.getName());
				Locality locality = locationService.findLocalityByCode(municipality.getLocality().getCode());

				temp1.setLocality(locality);
				temp1.setPostalCode(municipality.getPostalCode());
				// temp1.setLatitude(municipality.getLatitude());
				// temp1.setLongitude(municipality.getLongitude());
				temp1.setBranchId(getBranchId());

				locationService.editMunicipality(temp1);
				// Updating warehouse when editing Municipality
				editWarehouse(temp1);
				// Updating ServicePoint when editing Municipality
				editServicePoint(temp1);

			}
			command = UPDATE;
			request.setAttribute(HEADING, getLocaleProperty(LIST));
			return LIST;
		}
		return super.execute();
	}

	/**
	 * Edits the warehouse.
	 * 
	 * @param municipality
	 *            the municipality
	 */
	private void editWarehouse(Municipality municipality) {

		Warehouse warehouse = locationService.findWarehouseByCode(municipality.getCode());
		if (!ObjectUtil.isEmpty(warehouse)) {
			warehouse.setName(municipality.getName());
			locationService.editWarehouse(warehouse);
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

		String view = "";
		if (id != null && !id.equals("")) {
			municipality = locationService.findMunicipalityById(Long.valueOf(id));
			if (municipality == null) {
				addActionError(NO_RECORD);
				return REDIRECT;
			}
			setCurrentPage(getCurrentPage());
			command = UPDATE;
			view = DETAIL;
			request.setAttribute(HEADING, getLocaleProperty(DETAIL));
		} else {
			request.setAttribute(HEADING, getLocaleProperty(LIST));
			return LIST;
		}
		return view;
	}

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
	 * Sets the localities.
	 * 
	 * @param localities
	 *            the new localities
	 */
	public void setLocalities(List<Locality> localities) {

		this.localities = localities;
	}

	/**
	 * Delete.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */

	@SuppressWarnings("unchecked")
	public void delete() throws Exception {

		if (id != null && !id.equals("")) {
			municipality = locationService.findMunicipalityById(Long.valueOf(id));

			String flag = null;
			if (!ObjectUtil.isListEmpty(locationService.listVillagesByCityID(municipality.getId()))) {
				flag = "village.mapped.municipality";
				
			}

			/*
			 * if(!ObjectUtil.isListEmpty(municipality.getGramPanchayats())){
			 * flag="gramPanchayat.mapped"; }else{ flag =
			 * locationService.isCityMappingexist(municipality); }
			 */

			if (StringUtil.isEmpty(flag)) {

				productDistributionService.removeCityWarehouseProduct(municipality.getCode());
				if (municipality.getLocality() != null && !ObjectUtil.isEmpty(municipality.getLocality())) {
					if (municipality.getLocality().getMunicipalities().contains(municipality)) {
						municipality.getLocality().getMunicipalities().remove(municipality);

						locationService.editLocality(municipality.getLocality());
					}
				} 
				locationService.removeCity(municipality);
				getJsonObject().put("msg", getText("msg.deleted"));  
			    getJsonObject().put("title", getText("title.success"));
			}
			
			else {
				getJsonObject().put("msg", getLocaleProperty(flag));  
			    getJsonObject().put("title", getText("title.error"));
			}

			//addActionError(getLocaleProperty(flag));
			 sendAjaxResponse(getJsonObject());
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

	public Map<String, String> getCountries() {
		Map<String, String> countryMap = new LinkedHashMap<String, String>();
		List<Country> returnValue = locationService.listCountries();
		for (Country c : returnValue) {
			countryMap.put(c.getName(), c.getCode() + " - " + c.getName());
		}
		return countryMap;
	}

	/**
	 * Gets the states.
	 * 
	 * @return the states
	 */
	/*
	 * public List<State> getStates() {
	 * 
	 * if (!StringUtil.isEmpty(selectedCountry)) { stats =
	 * locationService.listStates(selectedCountry); } return stats; }
	 */

	public Map<String, String> getStates() {
		Map<String, String> state = new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedCountry)) {
			stats = locationService.listStates(selectedCountry);
			for (State s : stats) {
				state.put(s.getCode(), s.getCode() + " - " + s.getName());
			}
		}
		return state;
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

	@SuppressWarnings("unchecked")
	protected JSONObject getJSONObject(Object id, Object name) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		return jsonObject;
	}

	/**
	 * Populate state.
	 * 
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public void populateState() throws Exception {
		if (!selectedCountry.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCountry))) {
			stats = locationService.listStates(selectedCountry);
		}
		JSONArray stateArr = new JSONArray();
		if (!ObjectUtil.isEmpty(stats)) {
			for (State state : stats) {
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
			localities = locationService.listLocalities(selectedState);
		}
		JSONArray localtiesArr = new JSONArray();
		if (!ObjectUtil.isEmpty(localities)) {
			for (Locality locality : localities) {
				localtiesArr.add(getJSONObject(locality.getCode(), locality.getCode() + " - " + locality.getName()));
			}
		}
		sendAjaxResponse(localtiesArr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ese.view.ESEValidatorAction#getData()
	 */
	/**
	 * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
	 */
	@Override
	public Object getData() {

		if (ObjectUtil.isEmpty(municipality)) {
			return null;
		} else {
			Country country = new Country();
			country.setName(this.selectedCountry);

			State state = new State();
			state.setCode(this.selectedState);
			state.setCountry(country);

			Locality locality = new Locality();
			locality.setCode(municipality.getLocality().getCode());
			locality.setState(state);
			municipality.setLocality(locality);

			return municipality;
		}

	}

	/**
	 * Adds the service point.
	 * 
	 * @param municipality
	 *            the municipality
	 */
	public void addServicePoint(Municipality municipality) {

		ServicePoint servicePoint = new ServicePoint();
		servicePoint.setCode(municipality.getCode());
		servicePoint.setName(municipality.getName());
		servicePoint.setCity(municipality);
		ServicePointType spt = new ServicePointType();
		spt.setId(1);
		servicePoint.setType(spt);
		servicePointService.addServicePoint(servicePoint);
	}

	/**
	 * Edits the service point.
	 * 
	 * @param municipality
	 *            the municipality
	 */
	public void editServicePoint(Municipality municipality) {

		ServicePoint temp = servicePointService.findServicePointByCode(municipality.getCode());
		if (!ObjectUtil.isEmpty(temp)) {
			temp.setName(municipality.getName());
			temp.setCity(municipality);
			servicePointService.editServicePoint(temp);
		}

	}

	/**
	 * Delete service point.
	 * 
	 * @param municipality
	 *            the municipality
	 */
	public void deleteServicePoint(Municipality municipality) {

		ServicePoint existing = servicePointService.findServicePointByCode(municipality.getCode());
		if (!ObjectUtil.isEmpty(existing)) {
			servicePointService.removeServicePointByObject(existing);
		}

	}

	/**
	 * Delete warehouse.
	 * 
	 * @param municipality
	 *            the municipality
	 */
	private void deleteWarehouse(Municipality municipality) {

		Warehouse warehouse = locationService.findWarehouseByCode(municipality.getCode());
		if (!ObjectUtil.isEmpty(warehouse)) {
			locationService.removeWarehouse(warehouse);
		}

	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getSelectedDistrict() {
		return selectedDistrict;
	}

	public void setSelectedDistrict(String selectedDistrict) {
		this.selectedDistrict = selectedDistrict;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
