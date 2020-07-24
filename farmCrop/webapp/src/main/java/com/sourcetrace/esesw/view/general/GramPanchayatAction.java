/*
 * GramPanchayatAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
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
import org.springframework.beans.factory.annotation.Autowired;

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

public class GramPanchayatAction extends SwitchValidatorAction {

    private static final long serialVersionUID = -3703638399195601859L;

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(GramPanchayatAction.class);

    private ILocationService locationService;

    private GramPanchayat gramPanchayat;
    private String selectedCountry;
    private String selectedState;
    private String selectedDistrict;
    private String selectedCity;
    private String id;
    List<Locality> localities = new ArrayList<Locality>();
    List<State> states = new ArrayList<State>();
    List<Municipality> cities = new ArrayList<Municipality>();
    private IUniqueIDGenerator idGenerator;
    @Autowired
    private IPreferencesService preferncesService;
    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with

        GramPanchayat filter = new GramPanchayat();

        if (!StringUtil.isEmpty(searchRecord.get("code"))) {
            filter.setCode(searchRecord.get("code").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("name"))) {
            filter.setName(searchRecord.get("name").trim());
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

        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());

        return sendJQGridJSONResponse(data);
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) {

        GramPanchayat gramPanchayat = (GramPanchayat) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        if ((getIsMultiBranch().equalsIgnoreCase("1")
				&& (getIsParentBranch().equals("1") || StringUtil.isEmpty(branchIdValue)))) {

			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(!StringUtil.isEmpty(getBranchesMap().get(getParentBranchMap().get(gramPanchayat.getBranchId())))
						? getBranchesMap().get(getParentBranchMap().get(gramPanchayat.getBranchId()))
						: getBranchesMap().get(gramPanchayat.getBranchId()));
			}
			rows.add(getBranchesMap().get(gramPanchayat.getBranchId()));

		} else {
			if (StringUtil.isEmpty(branchIdValue)) {
				rows.add(branchesMap.get(gramPanchayat.getBranchId()));
			}
		}
        rows.add(gramPanchayat.getCode());
        rows.add(gramPanchayat.getName());
        rows.add(gramPanchayat.getCity() == null ? "  " :gramPanchayat.getCity().getName());
        rows.add(gramPanchayat.getCity().getLocality().getName());
        rows.add(gramPanchayat.getCity().getLocality().getState().getName());
        rows.add(gramPanchayat.getCity().getLocality().getState().getCountry().getName());
        jsonObject.put("id", gramPanchayat.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    public String create() throws Exception {
    	
    	ESESystem preferences = preferncesService.findPrefernceById("1");
		String codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);

        if (gramPanchayat == null) {
            command = CREATE;
            request.setAttribute(HEADING, getText(CREATE));
            return INPUT;
        } else {
            Municipality city = locationService.findMunicipalityByCode(gramPanchayat.getCity()
                    .getCode());
            gramPanchayat.setCity(city);
            gramPanchayat.setBranchId(getBranchId());
		
			
			if(!getCurrentTenantId().equalsIgnoreCase(ESESystem.AWI_TENANT_ID)){
				gramPanchayat.setCode(idGenerator.getGramPanchayatIdSeq());
			}
			
            
            locationService.addGramPanchayat(gramPanchayat);
            return REDIRECT;
        }
    }

    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public String update() throws Exception {

        if (id != null && !id.equals("")) {
            gramPanchayat = locationService.findGramPanchayatById(Long.parseLong(id));
            if (gramPanchayat == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setSelectedCountry(gramPanchayat.getCity().getLocality().getState().getCountry()
                    .getName());
            setSelectedState(gramPanchayat.getCity().getLocality().getState().getCode());
            setSelectedDistrict(gramPanchayat.getCity().getLocality().getCode());
            setCurrentPage(getCurrentPage());
            id = null;
            command = UPDATE;
            request.setAttribute(HEADING, getText(UPDATE));
        } else {
        	ESESystem preferences = preferncesService.findPrefernceById("1");
			String codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);
            if (gramPanchayat != null) {
                GramPanchayat existing = locationService.findGramPanchayatById(gramPanchayat
                        .getId());
                if (existing == null) {
                    addActionError(NO_RECORD);
                    return REDIRECT;
                }
                setCurrentPage(getCurrentPage());
                if (codeGenType.equals("1")) {
					if(!gramPanchayat.getCity().getName() .equalsIgnoreCase(existing.getCity().getName()))
					{
						String gramPanchayatCodeSeq = idGenerator.getGramPanchayatHHIdSeq(existing.getCity().getCode());
						existing.setCode(gramPanchayatCodeSeq);
					}
				}
                
                existing.setName(gramPanchayat.getName());
                Municipality city = locationService.findMunicipalityByCode(gramPanchayat.getCity()
                        .getCode());
				
                existing.setCity(city);
                existing.setBranchId(getBranchId());
                locationService.editGramPanchayat(existing);
            }
            command = UPDATE;
            request.setAttribute(HEADING, getText(LIST));
            return LIST;
        }
        return super.execute();
    }

    /**
     * Delete.
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public void delete() throws Exception {

        if (id != null && !id.equals("")) {
            gramPanchayat = locationService.findGramPanchayatById(Long.parseLong(id));
            if (ObjectUtil.isEmpty(gramPanchayat)) {
                addActionError(NO_RECORD);
            }

            String flag = null;
            if (!ObjectUtil.isListEmpty(gramPanchayat.getVillages())) {
                flag = "village.mapped.grampanchayat";
                getJsonObject().put("msg", getText(flag));  
			    getJsonObject().put("title", getText("title.error"));
            }
           
          /*  addActionError(getLocaleProperty(flag));
            request.setAttribute(HEADING, getText(DETAIL));
            return DETAIL;8*/
        
        else{
        	 if (StringUtil.isEmpty(flag)) {
                 locationService.removeGramPanchayat(gramPanchayat);
                 getJsonObject().put("msg", getText("msg.deleted"));
     			getJsonObject().put("title", getText("title.success"));
     			
             }
        }
        }
        sendAjaxResponse(getJsonObject());
    }

    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     */
    public String detail() throws Exception {

        String view = "";
        if (id != null && !id.equals("")) {
            gramPanchayat = locationService.findGramPanchayatById(Long.parseLong(id));
            if (gramPanchayat == null) {
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
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

        if (ObjectUtil.isEmpty(gramPanchayat)) {
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
            city.setCode(gramPanchayat.getCity().getCode());
            city.setLocality(locality);
            gramPanchayat.setCity(city);

            return gramPanchayat;
        }

    }

    /**
     * Gets the countries.
     * @return the countries
     */
/*    public List<Country> getCountries() {

        List<Country> returnValue = locationService.listCountries();
        return returnValue;
    }
*/
    
    public Map<String,String> getCountries(){
		Map<String, String> countryMap=new LinkedHashMap<String, String>();
		List<Country> returnValue = locationService.listCountries();
		for(Country c:returnValue){
			countryMap.put(c.getName(), c.getCode()+" - "+c.getName());
		}
		return countryMap;
	}

    /**
     * Gets the location service.
     * @return the location service
     */
    public ILocationService getLocationService() {

        return locationService;
    }

    /**
     * Sets the location service.
     * @param locationService the new location service
     */
    public void setLocationService(ILocationService locationService) {

        this.locationService = locationService;
    }

    /**
     * Gets the gram panchayat.
     * @return the gram panchayat
     */
    public GramPanchayat getGramPanchayat() {

        return gramPanchayat;
    }

    /**
     * Sets the gram panchayat.
     * @param gramPanchayat the new gram panchayat
     */
    public void setGramPanchayat(GramPanchayat gramPanchayat) {

        this.gramPanchayat = gramPanchayat;
    }

    /**
     * Gets the selected country.
     * @return the selected country
     */
    public String getSelectedCountry() {

        return selectedCountry;
    }

    /**
     * Sets the selected country.
     * @param selectedCountry the new selected country
     */
    public void setSelectedCountry(String selectedCountry) {

        this.selectedCountry = selectedCountry;
    }

    /**
     * Gets the selected state.
     * @return the selected state
     */
    public String getSelectedState() {

        return selectedState;
    }

    /**
     * Sets the selected state.
     * @param selectedState the new selected state
     */
    public void setSelectedState(String selectedState) {

        this.selectedState = selectedState;
    }

    /**
     * Gets the selected district.
     * @return the selected district
     */
    public String getSelectedDistrict() {

        return selectedDistrict;
    }

    /**
     * Sets the selected district.
     * @param selectedDistrict the new selected district
     */
    public void setSelectedDistrict(String selectedDistrict) {

        this.selectedDistrict = selectedDistrict;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public String getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Gets the localities.
     * @return the localities
     */
  /*  public List<Locality> getLocalities() {

        if (!StringUtil.isEmpty(selectedState)) {
            localities = locationService.listLocalities(selectedState);
        }
        return localities;
    }*/

    public Map<String, String> getLocalities() {
		Map<String, String> locality=new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedState)) {
			localities = locationService.listLocalities(selectedState);
			for(Locality loc:localities){
				locality.put(loc.getCode(), loc.getCode()+" - "+loc.getName());
			}
		}
		return locality;
	}

    
    /**
     * Sets the localities.
     * @param localities the new localities
     */
    public void setLocalities(List<Locality> localities) {

        this.localities = localities;
    }

    /**
     * Gets the states.
     * @return the states
     */
  /*  public List<State> getStates() {

        if (!StringUtil.isEmpty(selectedCountry)) {
            states = locationService.listStates(selectedCountry);
        }
        return states;
    }*/

    
    public Map<String, String> getStates(){
		Map<String, String> state=new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedCountry)) {
			states = locationService.listStates(selectedCountry);
			for(State s:states){
				state.put(s.getCode(), s.getCode()+" - "+s.getName());
			}
		}
		return state;
	}
    
    /**
     * Sets the states.
     * @param states the new states
     */
    public void setStates(List<State> states) {

        this.states = states;
    }
    
    public IUniqueIDGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IUniqueIDGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	/**
     * Gets the cities.
     * @return the cities
     */
   /* public List<Municipality> getCities() {

        if (!StringUtil.isEmpty(selectedDistrict)) {
            cities = locationService.listMunicipalities(selectedDistrict);
        }
        return cities;
    }*/
	
	public Map<String, String> getCities() {
		Map<String, String> city=new LinkedHashMap<String, String>();
		if (!StringUtil.isEmpty(selectedDistrict)) {
			cities = locationService.listMunicipalitiesByCode(selectedDistrict);
			for(Municipality muncipality:cities){
				city.put(muncipality.getCode(), muncipality.getCode()+" - "+muncipality.getName());
			}
		}
		return city;
	}

    /**
     * Sets the cities.
     * @param cities the new cities
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


    /**
     * Populate state.
     * @return the string
     * @throws Exception the exception
     */
    public void populateState() throws Exception {

        if (!selectedCountry.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCountry))) {
            states = locationService.listStates(selectedCountry);
        }
        JSONArray stateArr = new JSONArray();
        if (!ObjectUtil.isEmpty(states)) {
            for (State state : states) {
                stateArr.add(getJSONObject(state.getCode(), state.getCode()+" - "+state.getName()));
            }
        }
        sendAjaxResponse(stateArr);
    }

    /**
     * Populate locality.
     * @return the string
     * @throws Exception the exception
     */
    public void populateLocality() throws Exception {

    	if (!selectedState.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedState))) {
            localities = locationService.listLocalities(selectedState);
        }
        JSONArray localtiesArr = new JSONArray();
        if (!ObjectUtil.isEmpty(localities)) {
            for (Locality locality : localities) {
                localtiesArr.add(getJSONObject(locality.getCode(), locality.getCode()+" - "+locality.getName()));
            }
        }
        sendAjaxResponse(localtiesArr);
    }


    /**
     * Populate city.
     * @return the string
     * @throws Exception the exception
     */
    public void populateCity() throws Exception {

    	 if ((!StringUtil.isEmpty(selectedDistrict)&&!selectedDistrict.equalsIgnoreCase("null"))) {
             cities = locationService.listMunicipalitiesByCode(selectedDistrict);
         }
         JSONArray cityArray = new JSONArray();
         if (!ObjectUtil.isEmpty(cities)) {
             for (Municipality municipality : cities) {
                 cityArray.add(getJSONObject(municipality.getCode(),municipality.getCode()+" - "+municipality.getName()));
             }
         }
         sendAjaxResponse(cityArray);
     }
    
    @SuppressWarnings("unchecked")
	public void populateSavePanchayat() {
		GramPanchayat gramPanchayat = new GramPanchayat();
		String codeGenType = null;
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			/*
			 * setGramPanchayatEnable(preferences.getPreferences().get(ESESystem
			 * .ENABLE_GRAMPANJAYAT));
			 */
			codeGenType = preferences.getPreferences().get(ESESystem.CODE_TYPE);
		}
		if (!StringUtil.isEmpty(getSelectedCity())) {
			Municipality city = locationService.findMunicipalityByCode(getSelectedCity());
			gramPanchayat.setName(getName());
			gramPanchayat.setCity(city);
			gramPanchayat.setBranchId(getBranchId());
			gramPanchayat.setRevisionNo(DateUtil.getRevisionNumber());

			if (codeGenType.equals("1")) {
				BigInteger codeSeq=new BigInteger(idGenerator.getGramPanchayatHHIdSeq(city.getCode()));
				String maxCode=codeSeq.subtract(new BigInteger("1")).toString();
				if(Integer.valueOf(maxCode.substring(2,4))>=99){
					addActionError(getText("error.gramaPanchyatExceed"));
				}
				else
				{
					gramPanchayat.setCode(idGenerator.getGramPanchayatHHIdSeq(city.getCode()));
				}
			} 
				gramPanchayat.setCode(idGenerator.getGramPanchayatIdSeq());
				
				
				GramPanchayat gp = locationService.findGramPanchayatByName(gramPanchayat.getName());
				if(gramPanchayat.getName()==null || StringUtil.isEmpty(gramPanchayat.getName())){
					getJsonObject().put("status", "0"); 
					getJsonObject().put("msg", getText("msg.nameEmpty"));  
				    getJsonObject().put("title", getText("title.error"));
				}else if(ObjectUtil.isEmpty(gp)){
					locationService.addGramPanchayat(gramPanchayat);
					getJsonObject().put("msg", getText("msg.added"));
					getJsonObject().put("title", getText("title.success"));
					sendAjaxResponse(getJsonObject());
					}
				else{
					getJsonObject().put("status", "0");
					getJsonObject().put("msg", getLocaleProperty("msg.gramPanchayatDuplicateError"));  
				    getJsonObject().put("title", getText("title.error"));
				    sendAjaxResponse(getJsonObject());
				}
			
			
		/*	 locationService.addGramPanchayat(gramPanchayat);
			getJsonObject().put("msg", getText("msg.added"));
			getJsonObject().put("title", getText("title.success"));
			sendAjaxResponse(getJsonObject());*/
		}
    }
    
    public void populatePanchayatUpdate(){
    	if (!StringUtil.isEmpty(getId())) {
    		GramPanchayat panchayat = locationService.findGramPanchayatById(Long.valueOf(id));
    		if(!StringUtil.isEmpty(selectedCity)){
    			Municipality city = locationService.findMunicipalityByCode(getSelectedCity());
    			panchayat.setCity(city);
    			panchayat.setName(getName());
    			panchayat.setRevisionNo(DateUtil.getRevisionNumber());
    			locationService.editGramPanchayat(panchayat);
    			
    			getJsonObject().put("msg", getText("msg.updated"));
				getJsonObject().put("title", getText("title.success"));
				sendAjaxResponse(getJsonObject());
    		}
    	}
    }

	public String getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(String selectedCity) {
		this.selectedCity = selectedCity;
	}
    
    
}
