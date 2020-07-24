
/*
 * ClientAction.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.ese.view.profile;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.profile.CropSupply;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ResearchStation;
import com.sourcetrace.esesw.entity.profile.State;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class CustomerAction.
 */
@SuppressWarnings("serial")
public class ResearchStationAction extends SwitchValidatorAction {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(ResearchStationAction.class);

    protected static final String CREATE = "create";
    protected static final String DETAIL = "detail";
    protected static final String UPDATE = "update";
    protected static final String MAPPING = "mapping";
    protected static final String DELETE = "delete";
    protected static final String LIST = "list";
    protected static final String TITLE_PREFIX = "title.";
    protected static final String HEADING = "heading";

    public static final String COUNTRY_ID_SEQ = "COUNTRY_ID_SEQ";
    public static final String STATE_ID_SEQ = "STATE_ID_SEQ";
    public static final String DISTRICT_ID_SEQ = "DISTRICT_ID_SEQ";
    public static final String MANDAL_ID_SEQ = "MANDAL_ID_SEQ";
    public static final String VILLAGE_ID_SEQ = "VILLAGE_ID_SEQ";

    public static final String DEVICE_ID_SEQ = "DEVICE_ID_SEQ";
    public static final String GROUP_ID_SEQ = "GROUP_ID_SEQ";
    public static final String WAREHOUSE_ID_SEQ = "WAREHOUSE_ID_SEQ";

    public static final String SUBCATEGORY_ID_SEQ = "SUBCATEGORY_ID_SEQ";

    public static final String PRODUCT_ID_SEQ = "PRODUCT_ID_SEQ";

    private IClientService clientService;
    private IUniqueIDGenerator idGenerator;
    

   
    private String id;
    private ResearchStation researchStation;
    private ResearchStation filter;

    private String selectedHoldingType;

    private Map<String, String> holdingTypeList = new LinkedHashMap<String, String>();
    private String tabIndex = "#tabs-1";
    private IAccountService accountService;
    @Autowired
    private IProductService productService;
    @Autowired
    private ILocationService locationService;
    @Autowired
    private IFarmerService farmerService;
    
   
    List<Locality> localities = new ArrayList<Locality>();
    List<Municipality>municipalities =new ArrayList<Municipality>();
    List<State> states = new ArrayList<State>();
    private String selectedCountry;
    private String selectedState;
    private Municipality municipality;
    private String selectedDistrict;
    /**
     * Data.
     * 
     * @return the string
     * @throws Exception
     *             the exception
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam(); // get the
                                                                    // search
                                                                    // parameter
                                                                    // with

        ResearchStation filter = new ResearchStation();

        if (!StringUtil.isEmpty(searchRecord.get("branchId"))) {
            filter.setBranchId(searchRecord.get("branchId").trim());
        }
        
        if (!StringUtil.isEmpty(searchRecord.get("researchStationId"))) {
            filter.setResearchStationId(searchRecord.get("researchStationId").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("name"))) {
            filter.setName(searchRecord.get("name").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("pointPerson"))) {
            filter.setPointPerson(searchRecord.get("pointPerson").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("division"))) {
            filter.setDivision(searchRecord.get("division").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("designation"))) {
            filter.setDesignation(searchRecord.get("designation").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("researchStationAddress"))) {
            filter.setResearchStationAddress(searchRecord.get("researchStationAddress").trim());
        }

        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(), getResults(), filter,
                getPage());

        return sendJQGridJSONResponse(data);
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
            researchStation = farmerService.findResearchStation(Long.valueOf(id));
            if (researchStation == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
               if(researchStation.getMunicipality()!=null){
                    researchStation.getMunicipality().setName(researchStation.getMunicipality().getName());
                }
            command = UPDATE;
            if(researchStation.getCity()!=null){
                setSelectedCountry(researchStation.getCity().getState().getCountry().getName());
                setSelectedState(researchStation.getCity().getState().getName());
                }
            view = DETAIL;
            request.setAttribute(HEADING, getText("researchStationdetail"));
        } else {
            request.setAttribute(HEADING, getText("researchStationlist"));
            return LIST;
        }
        return view;
    }

    /**
     * Creates the.
     * 
     * @return the string
     * @throws Exception
     *             the exception
     */
    public String create() throws Exception {

        if (researchStation == null) {
            command = "create";
            request.setAttribute(HEADING, getText("researchStationcreate"));
            return INPUT;
        } else {
            if(researchStation.getCity()!=null){
                Locality locality = locationService.findLocalityByName(researchStation.getCity().getName());
                researchStation.setCity(locality);
                }
            if(researchStation.getMunicipality()!=null){
            	
                Municipality municipality = locationService.findMunicipalityByName(researchStation.getMunicipality().getName());
                researchStation.setMunicipality(municipality);
                }
            String researchStationIdSeq = idGenerator.createResearchStationId();
            researchStation.setResearchStationId(researchStationIdSeq);
            researchStation.setBranchId(getBranchId());
            researchStation.setStatus(ResearchStation.STATUS_ACTIVE);
            farmerService.addResearchStation(researchStation);
            return REDIRECT;
        }
        
        
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
            researchStation = farmerService.findResearchStation(Long.valueOf(id));
            if (researchStation == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            if(researchStation.getCity()!=null){
                setSelectedCountry(researchStation.getCity().getState().getCountry().getName());
                setSelectedState(researchStation.getCity().getState().getName());
                }
            
            if(researchStation.getMunicipality()!=null){
                researchStation.getMunicipality().setName(researchStation.getMunicipality().getName());
            }
            id = null;
            command = UPDATE;
            request.setAttribute(HEADING, getText("researchStationupdate"));
        } else {
            if (researchStation != null) {
                ResearchStation tempResearchStation = farmerService.findResearchStation(Long.valueOf(researchStation.getId()));
                if (tempResearchStation == null) {
                    addActionError(NO_RECORD);
                    return REDIRECT;
                }
                setCurrentPage(getCurrentPage());
                if(researchStation.getCity()!=null){
                    Locality locality = locationService.findLocalityByName(researchStation.getCity().getName());
                    researchStation.setCity(locality);
                    }
                    if(researchStation.getMunicipality()!=null){
                        Municipality municipality = locationService.findMunicipalityByName(researchStation.getMunicipality().getName());
                        researchStation.setMunicipality(municipality);
                        }
                tempResearchStation.setName(researchStation.getName());
                tempResearchStation.setResearchStationAddress(researchStation.getResearchStationAddress());
                tempResearchStation.setDivision(researchStation.getDivision());
                tempResearchStation.setPointPerson(researchStation.getPointPerson());
                tempResearchStation.setDesignation(researchStation.getDesignation());
                tempResearchStation.setStatus(ResearchStation.STATUS_ACTIVE);
                farmerService.editResearchStation(tempResearchStation);
            }
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
    public String delete() throws Exception {
        
        
        if (this.getId() != null && !(this.getId().equals(EMPTY))) {
            researchStation = farmerService.findResearchStation(Long.valueOf(getId()));
            if (researchStation == null) {
                addActionError(NO_RECORD);
                return null;
            }
            if (!ObjectUtil.isEmpty(researchStation)) {

                boolean isCustomerMappedWithFS = false;/*
                                                         * clientService
                                                         * .isCustomerWithMappedFieldStaff
                                                         * (customer.getId());
                                                         */
                List <CropSupply> cs = productService.findCropSupplyByCustomerId(researchStation.getId());
                if(cs.size()>0){
                    addActionError(getText("buyercannotDeleteCustomerMappedCropSupply"));
                    request.setAttribute(HEADING, getText(DETAIL));
                    return DETAIL;
                }

                  
                else {
                  //  ESEAccount account = accountService.findAccountByProfileId(researchStation.getResearchStationId());
                    /*if (account != null && !(ObjectUtil.isEmpty(account))) {
                        accountService.removeAccountByProfileIdAndProfileType(account.getProfileId(),
                                account.getType());
                        
                    }*/
                    farmerService.removeResearchStation(researchStation);
                }

            }
        }
        request.setAttribute(HEADING, getText(LIST));
        return LIST;

    }

    /**
     * To json.
     * 
     * @param record
     *            the record
     * @return the JSON object
     * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object record) {

        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        if (record instanceof ResearchStation) {
            ResearchStation researchStation = (ResearchStation) record;
            JSONArray rows = new JSONArray();
            if (StringUtil.isEmpty(branchIdValue)) {
                rows.add(branchesMap.get(researchStation.getBranchId()));
            }
            rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + researchStation.getResearchStationId() + "</font>");
            rows.add(researchStation.getName());
            rows.add(researchStation.getPointPerson());
            rows.add(researchStation.getDesignation());
           // rows.add(researchStation.getDivision());
            jsonObject.put("id", researchStation.getId());
            jsonObject.put("cell", rows);
            return jsonObject;

        }
        return jsonObject;
    }

    /**
     * Populate state.
     * 
     * @param populateResponce
     *            the populate responce
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
                localtiesArr.add(getJSONObject(locality.getName(), locality.getName()));
            }
        }
        sendAjaxResponse(localtiesArr);
    }
    public void populateState() throws Exception {
        if (!selectedCountry.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedCountry))) {
            states = locationService.listStates(selectedCountry);
        }
        JSONArray stateArr = new JSONArray();
        if (!ObjectUtil.isEmpty(states)) {
            for (State state : states) {
                stateArr.add(getJSONObject(state.getName(), state.getName()));
            }
        }
        sendAjaxResponse(stateArr);
    }
    
    public void populateTaluks(){
        if (!selectedDistrict.equalsIgnoreCase("null") && (!StringUtil.isEmpty(selectedDistrict))) {
            municipalities = locationService.listMunicipalities(selectedDistrict);
        }
        JSONArray stateArr = new JSONArray();
        if (!ObjectUtil.isEmpty(municipalities)) {
            for (Municipality municipality  : municipalities) {
                stateArr.add(getJSONObject(municipality.getName(), municipality.getName()));
            }
        }
        sendAjaxResponse(stateArr);
    }
    @SuppressWarnings("unchecked")
    protected JSONObject getJSONObject(Object id, Object name) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        return jsonObject;
    }
    protected String sendResponse(List<?> populateResponce) throws Exception {

        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(populateResponce);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets the data.
     * 
     * @return the data
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {
        if (ObjectUtil.isEmpty(researchStation)) {
            return null;
        } else {
            return researchStation;
        }
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
     * Gets the id generator.
     * 
     * @return the id generator
     */
    public IUniqueIDGenerator getIdGenerator() {

        return idGenerator;
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
     * Sets the filter.
     * 
     * @param filter
     *            the new filter
     */
    public void setFilter(ResearchStation filter) {

        this.filter = filter;
    }

 
       /**
     * Gets the filter.
     * 
     * @return the filter
     */
    public ResearchStation getFilter() {

        return filter;
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
     * Gets the holding type list.
     * 
     * @return the holding type list
     */
    public Map<String, String> getHoldingTypeList() {

        return holdingTypeList;
    }

    /**
     * Sets the holding type list.
     * 
     * @param holdingTypeList
     *            the holding type list
     */
    public void setHoldingTypeList(Map<String, String> holdingTypeList) {

        this.holdingTypeList = holdingTypeList;
    }

    /**
     * Gets the selected holding type.
     * 
     * @return the selected holding type
     */
    public String getSelectedHoldingType() {

        return selectedHoldingType;
    }

    /**
     * Sets the selected holding type.
     * 
     * @param selectedHoldingType
     *            the new selected holding type
     */
    public void setSelectedHoldingType(String selectedHoldingType) {

        this.selectedHoldingType = selectedHoldingType;
    }

    public String getTabIndex() {

        return URLDecoder.decode(tabIndex);
    }

    public void setTabIndex(String tabIndex) {

        this.tabIndex = tabIndex;
    }

    public IAccountService getAccountService() {

        return accountService;
    }

    public void setAccountService(IAccountService accountService) {

        this.accountService = accountService;
    }

   
    public List<Locality> getLocalities() {
        if (!StringUtil.isEmpty(selectedState)) {
            localities = locationService.listLocalities(selectedState);
        }
        return localities;
        
    }

    public void setLocalities(List<Locality> localities) {
        this.localities = localities;
    }

    public List<State> getStates() {
        if (!StringUtil.isEmpty(selectedCountry)) {
            states = locationService.listStates(selectedCountry);
        }
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public String getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(String selectedState) {
        this.selectedState = selectedState;
    }
    public List<Country> getCountries() {

        List<Country> returnValue = locationService.listCountries();
        return returnValue;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

  public List<Municipality> getMunicipalities() {
        if (!ObjectUtil.isEmpty(researchStation) && researchStation.getCity() != null &&!StringUtil.isEmpty(researchStation.getCity().getName())) {
            municipalities = locationService.listMunicipalities(researchStation.getCity().getName());
        }
        return municipalities;
    }

    public void setMunicipalities(List<Municipality> municipalities) {
        this.municipalities = municipalities;
    }

    public String getSelectedDistrict() {
        return selectedDistrict;
    }

    public void setSelectedDistrict(String selectedDistrict) {
        this.selectedDistrict = selectedDistrict;
    }
    
    public ResearchStation getResearchStation() {
        
        return researchStation;
    }

    public void setResearchStation(ResearchStation researchStation) {
    
        this.researchStation = researchStation;
    }
    
    
}
