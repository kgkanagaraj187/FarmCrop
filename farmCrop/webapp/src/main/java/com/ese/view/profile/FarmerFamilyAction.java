/*
 * FarmerFamilyAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerFamily;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class FarmerFamilyAction.
 */
public class FarmerFamilyAction extends SwitchValidatorAction {

    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(FarmerFamilyAction.class);
    public static final int SELECT = -1;

    private IFarmerService farmerService;

    private FarmerFamily farmerFamily;
    private FarmerFamily filter;
    private FarmerFamily headFamily;

    private Map<Integer, String> relations = new LinkedHashMap<Integer, String>();
    private Map<Integer, String> educations = new LinkedHashMap<Integer, String>();
    Map<String, String> genderType = new LinkedHashMap<String, String>();
    Map<String, String> disabilityType = new LinkedHashMap<String, String>();
    private Map<Integer, String> profession = new LinkedHashMap<Integer, String>();
    
    Map<String, String> catalogueRelationshipList = new LinkedHashMap<String, String>();
	Map<String, String> educationStatusList = new LinkedHashMap<String, String>();
	private Map<Integer, String> maritalSatuses = new LinkedHashMap<Integer, String>();
	private Map<Integer, String> educationList = new LinkedHashMap<Integer, String>();

    private long headOfTheFamily;
    private long currentFamily;
    private String familyDetailJsonString = "";

    private String id;
    private String farmerId;
    public boolean familyFlag;
    public boolean headOfFlag;

    private String relationHeader;
    private String educationHeader;

    private String tabIndex = "#tabs-6";
    
    private String farmerSeqId;
    private String farmerName;
    private String disability;

    /**
     * Gets the tab index.
     * @return the tab index
     */
    public String getTabIndex() {

        return tabIndex;
    }

    /**
     * Sets the tab index.
     * @param tabIndex the new tab index
     */
    public void setTabIndex(String tabIndex) {

        this.tabIndex = tabIndex;
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam(); // get the search parameter with
        // value

        FarmerFamily filter = new FarmerFamily();

        Farmer farmer = new Farmer();
        farmer.setId(Long.valueOf(id));
        filter.setFarmer(farmer);
       

        if (!StringUtil.isEmpty(searchRecord.get("famly.name"))) {
            filter.setName(searchRecord.get("famly.name").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("famly.gender"))) {
            filter.setGender(searchRecord.get("famly.gender").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("famly.age"))) {
            filter.setAge(Integer.valueOf(searchRecord.get("famly.age").trim()));
        }
        if (!StringUtil.isEmpty(searchRecord.get("famly.rel"))) {
            filter.setRelation(searchRecord.get("famly.rel").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("famly.edu"))) {
            filter.setEducation(searchRecord.get("famly.edu").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("famly.abi"))) {
            filter.setDisability(Integer.valueOf(searchRecord.get("famly.abi").trim()));
            filter.setFilterStatusDis("1");
        }
        if (!StringUtil.isEmpty(searchRecord.get("famly.marital"))) {
            filter.setMaritalStatus(searchRecord.get("famly.marital").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("famly.eduStatus"))) {
            filter.setEducationStatus(searchRecord.get("famly.eduStatus").trim());
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

        FarmerFamily farmerFamily = (FarmerFamily) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        rows.add(farmerFamily.getName());
        rows.add( farmerFamily.getGender());
        rows.add( farmerFamily.getAge());
      FarmCatalogue relation=catalogueService.findCatalogueByCode(farmerFamily.getRelation());
         FarmCatalogue edu=catalogueService.findCatalogueByCode(farmerFamily.getEducation());
         FarmCatalogue marital=catalogueService.findCatalogueByCode(farmerFamily.getMaritalStatus());
         FarmCatalogue educationbelow=catalogueService.findCatalogueByCode(farmerFamily.getEducationStatus());
        rows.add(!ObjectUtil.isEmpty(relation)? relation.getName() : "");
        rows.add(!ObjectUtil.isEmpty(edu)?edu.getName():"");
        if(farmerFamily.getDisability()==1){
        	rows.add("yes");
        }
        else{
        	rows.add("NO");
        }
        rows.add(!ObjectUtil.isEmpty(marital)?marital.getName():"");
        rows.add(!ObjectUtil.isEmpty(educationbelow)?educationbelow.getName():"");
        /*rows.add("<button class='ic-del' onclick='deleteFarmerFamily(" + farmerFamily.getId()
                + ")'>Delete</button");*/
        jsonObject.put("id", farmerFamily.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
    }
    /*public void populateFamilyDetail() throws Exception {
    	Type listType1 = new TypeToken<List<FarmerFamily>>() {}.getType();
    	List<FarmerFamily> familyDetailList = new Gson().fromJson(familyDetailJsonString, listType1);
    	  System.out.println("familyDetailJsonString"+familyDetailJsonString);
    	 if (!ObjectUtil.isEmpty(familyDetailList)) {
    		 for (int j = 0; j < familyDetailList.size(); j++) {
    			 
                 FarmerFamily farmerFamily=new FarmerFamily();
                 Farmer farmer = farmerService.findFarmerById(Long.valueOf(farmerId));
                 farmerFamily.setName(familyDetailList.get(j).getNameStr());
                 farmerFamily.setGender(familyDetailList.get(j).getGenderInt());
                 farmerFamily.setAge(Integer.parseInt(familyDetailList.get(j).getAgeInt()));
                 farmerFamily.setRelation(familyDetailList.get(j).getRelationStr());
                 farmerFamily.setFarmer(farmer);
                 farmerFamily.setEducation(familyDetailList.get(j).getEducationStr());
                 farmerFamily.setMaritalStatus(familyDetailList.get(j).getMaritalStr());
                 farmerFamily.setEducationStatus(familyDetailList.get(j).getEducationstatusStr());
                 farmerFamily.setDisability(Integer.parseInt(familyDetailList.get(j).getDisabilityInt()));
                 
                 farmerService.addFarmerFamily(farmerFamily);
    		 }
    	 }
    }*/
    
    public String getFamilyDetailJsonString() {
        
        return familyDetailJsonString;
    }

    public void setFamilyDetailJsonString(String familyDetailJsonString) {
    
        this.familyDetailJsonString = familyDetailJsonString;
    }

    /**
     * Gets the property value.
     * @param propertyKey the property key
     * @param index the index
     * @return the property value
     */
    private Object getPropertyValue(String propertyKey, int index) {

        String values = getText(propertyKey);
        Object returnObj = "";
        if (!StringUtil.isEmpty(values) && index >= 0) {
            String[] valuesArray = values.split(",");
            if (valuesArray.length > index) {
                returnObj = valuesArray[index];
            }
        }
        return returnObj;
    }

    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    public String create() throws Exception {

        setFarmerIdToSessionAttribute();
        if (farmerFamily == null) {
            command = CREATE;
            request.setAttribute(HEADING, getText(CREATE));
            // educationHeader = farmerService.headerOfCatalogValues(educationCatalog);
            // relationHeader = farmerService.headerOfCatalogValues(relationCatalog);
           // headFamily = farmerService.findHeadOfFamilyById(Long.valueOf(farmerId));
            if (headFamily != null) {
                headOfTheFamily = headFamily.getId();
            }
            return INPUT;
        } else {

            Farmer farmer = farmerService.findFarmerById(Long.valueOf(farmerId));
            farmerFamily.setFarmer(farmer);
            if(farmerFamily.isHeadOfFamily().equalsIgnoreCase("true"))
            	farmerFamily.setHeadOfFamily("1");
            else
            	farmerFamily.setHeadOfFamily("2");
            farmerService.addFarmerFamily(farmerFamily);
            return "farmerDetail";
        }

    }

    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public String update() throws Exception {

        setFarmerIdToSessionAttribute();
        if (id != null && !id.equals("")) {
            farmerFamily = farmerService.findFarmerFamilyById(Long.parseLong(id));
            if (farmerFamily == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            if(farmerFamily.isHeadOfFamily().equalsIgnoreCase("1"))
            	farmerFamily.setHeadOfFamily("true");
            else
            	farmerFamily.setHeadOfFamily("false");
            setCurrentPage(getCurrentPage());
            id = null;
            command = UPDATE;

            request.setAttribute(HEADING, getText(UPDATE));
            headFamily = farmerService.findHeadOfFamilyById(Long.valueOf(farmerId));
            if (headFamily != null) {
                headOfTheFamily = headFamily.getId();
                currentFamily = this.getFarmerFamily().getId();
            }
        } else {
            if (farmerFamily != null) {
                FarmerFamily temp = farmerService.findFarmerFamilyById(farmerFamily.getId());
                if (temp == null) {
                    addActionError(NO_RECORD);
                    return REDIRECT;
                }
                setCurrentPage(getCurrentPage());
                temp.setName(farmerFamily.getName());
         
                temp.setAge(farmerFamily.getAge());
                temp.setGender(farmerFamily.getGender());
                temp.setRelation(farmerFamily.getRelation());
                temp.setEducation(farmerFamily.getEducation());
                temp.setHeadOfFamily(farmerFamily.isHeadOfFamily());
                temp.setActivity(farmerFamily.getActivity());
                temp.setWageEarner(farmerFamily.isWageEarner());
                temp.setProfession(farmerFamily.getProfession());
                temp.setOtherProfession(farmerFamily.getOtherProfession());	
                temp.setDisability(farmerFamily.getDisability());
                temp.setEducationStatus(farmerFamily.getEducationStatus());
                if(farmerFamily.isHeadOfFamily().equalsIgnoreCase("true"))
                	temp.setHeadOfFamily("1");
                else
                	temp.setHeadOfFamily("2");
                farmerService.editFarmerFamily(temp);
                headFamily = farmerService.findHeadOfFamilyById(Long.valueOf(farmerId));
                if (headFamily != null) {
                    headOfTheFamily = headFamily.getId();
                    currentFamily = this.getFarmerFamily().getId();
                }
                setFarmerId(String.valueOf(temp.getFarmer().getId()));
            }
           
            request.setAttribute(HEADING, getText(LIST));
            return "farmerDetail";
        }
        return super.execute();
    }

    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     */
    public String detail() throws Exception {

        setFarmerIdToSessionAttribute();
        String view = "";
        if (id != null && !id.equals("")) {
            farmerFamily = farmerService.findFarmerFamilyById(Long.parseLong(id));
            
            if (farmerFamily == null) {
            	
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            farmerFamily.setRelation(getCatlogueValueByCode(farmerFamily.getRelation())!=null ? getCatlogueValueByCode(farmerFamily.getRelation()).getName() : "");
            farmerFamily.setEducation(getCatlogueValueByCode(farmerFamily.getEducation())!=null ? getCatlogueValueByCode(farmerFamily.getEducation()).getName() : "");
            farmerFamily.setEducationStatus(getCatlogueValueByCode(farmerFamily.getEducationStatus())!=null ? getCatlogueValueByCode(farmerFamily.getEducationStatus()).getName() : "");
            farmerFamily.setMaritalStatus(getCatlogueValueByCode(farmerFamily.getMaritalStatus())!=null ? getCatlogueValueByCode(farmerFamily.getMaritalStatus()).getName() : "");
            farmerFamily.setGender(farmerFamily.getGender().equals(Farmer.SEX_MALE) ? getText("MALE") : getText("FEMALE"));
            farmerFamily.setDisability(farmerFamily.getDisability());
            if(farmerFamily.isHeadOfFamily().equals("1")){
            	farmerFamily.setHeadOfFamily("Yes");
            }
            else{
            	farmerFamily.setHeadOfFamily("No");
            }
            if(StringUtil.isInteger(farmerFamily.getDisability())){
            	if(farmerFamily.getDisability()==1){
            		setDisability(getText("Yes"));
            	}else{
            		setDisability(getText("No"));
            	}
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
     * Delete.
     * @return the string
     * @throws Exception the exception
     */
    public String delete() throws Exception {

        setFarmerIdToSessionAttribute();
        if (this.getId() != null && !(this.getId().equals(EMPTY))) {
            farmerFamily = farmerService.findFarmerFamilyById(Long.parseLong(id));
            if (farmerFamily == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }
            setCurrentPage(getCurrentPage());
            farmerService.removeFarmerFamily(farmerFamily);
            setFarmerId(String.valueOf(farmerFamily.getFarmer().getId()));
        }
        return "farmerDetail";

    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object getData() {

        if (!ObjectUtil.isEmpty(farmerFamily)) {
            /*
             * CatalogValues relation = farmerService.findCatalogValuesByName(selectedRelation);
             * farmerFamily.setRelation(relation); CatalogValues education =
             * farmerService.findCatalogValuesByName(selectedEducation);
             * farmerFamily.setEducation(education);
             */
        }
        // educationHeader = farmerService.headerOfCatalogValues(educationCatalog);
        // relationHeader = farmerService.headerOfCatalogValues(relationCatalog);
        if (farmerFamily != null) {
            Farmer farmer = new Farmer();
            farmer.setId(Long.valueOf(this.getFarmerId()));
            farmerFamily.setFarmer(farmer);
        }
        relations = formMap("rel", relations);
        genderType.put(Farmer.SEX_MALE, getText("MALE"));
        genderType.put(Farmer.SEX_FEMALE, getText("FEMALE"));
        disabilityType.put(Farmer.DISABLED_YES,getText("yes"));
        disabilityType.put(Farmer.DISABLED_NO,getText("no"));
        profession = formValueMap("prof",profession);

        return farmerFamily;
    }

   
	/**
     * Form map.
     * @param keyProperty the key property
     * @param dataMap the data map
     * @return the map
     */
    @SuppressWarnings("unchecked")
    private Map formMap(String keyProperty, Map dataMap) {

        dataMap = new LinkedHashMap();
        String values = getText(keyProperty);
        if (!StringUtil.isEmpty(values)) {
            String[] valuesArray = values.split(",");
            int i = 0;
            for (String value : valuesArray) {
                dataMap.put(i++, value);
            }
        }
        return dataMap;
    }

    
    private Map<Integer, String> formValueMap(String keyProperty,
            Map<Integer, String> dataMap) {

        dataMap = new LinkedHashMap();
        String values = getText(keyProperty);
        if (!StringUtil.isEmpty(values)) {
            String[] valuesArray = values.split(",");
            int i = 1;
            for (String value : valuesArray) {
                dataMap.put(i++, value);
            }
        }
        return dataMap;
    }
    
    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#list()
     */
    public String list() throws Exception {

        setFarmerIdToSessionAttribute();

        if (getCurrentPage() != null) {
            setCurrentPage(getCurrentPage());
        }

        request.setAttribute(HEADING, getText(LIST));
        return LIST;
    }

    /**
     * Sets the farmer id to session attribute.
     */
    private void setFarmerIdToSessionAttribute() {

        if (!StringUtil.isEmpty(getFarmerId())) {
            request.getSession().setAttribute("farmerId", getFarmerId());
        } else if (!StringUtil.isEmpty(request.getSession().getAttribute("farmerId"))) {
            setFarmerId(String.valueOf(request.getSession().getAttribute("farmerId")));
        }
    }

    /**
     * Gets the farmer service.
     * @return the farmer service
     */
    public IFarmerService getFarmerService() {

        return farmerService;
    }

    /**
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
    }

    /**
     * Gets the farmer family.
     * @return the farmer family
     */
    public FarmerFamily getFarmerFamily() {

        return farmerFamily;
    }

    /**
     * Sets the farmer family.
     * @param farmerFamily the new farmer family
     */
    public void setFarmerFamily(FarmerFamily farmerFamily) {

        this.farmerFamily = farmerFamily;
    }

    /**
     * Sets the filter.
     * @param filter the new filter
     */
    public void setFilter(FarmerFamily filter) {

        this.filter = filter;
    }

    /**
     * Gets the filter.
     * @return the filter
     */
    public FarmerFamily getFilter() {

        return filter;
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
     * Sets the farmer id.
     * @param farmerId the new farmer id
     */
    public void setFarmerId(String farmerId) {

        this.farmerId = farmerId;
    }

    /**
     * Gets the farmer id.
     * @return the farmer id
     */
    public String getFarmerId() {

        return farmerId;
    }

    /**
     * Gets the family flag.
     * @return the family flag
     */
    public boolean getFamilyFlag() {

        return familyFlag;
    }

    /**
     * Sets the family flag.
     * @param familyFlag the new family flag
     */
    public void setFamilyFlag(boolean familyFlag) {

        this.familyFlag = familyFlag;
    }

    /**
     * Gets the relation header.
     * @return the relation header
     */
    public String getRelationHeader() {

        return relationHeader;
    }

    /**
     * Sets the relation header.
     * @param relationHeader the new relation header
     */
    public void setRelationHeader(String relationHeader) {

        this.relationHeader = relationHeader;
    }

    /**
     * Gets the education header.
     * @return the education header
     */
    public String getEducationHeader() {

        return educationHeader;
    }

    /**
     * Sets the education header.
     * @param educationHeader the new education header
     */
    public void setEducationHeader(String educationHeader) {

        this.educationHeader = educationHeader;
    }

    /**
     * Gets the head of flag.
     * @return the head of flag
     */
    public boolean getHeadOfFlag() {

        return headOfFlag;
    }

    /**
     * Sets the head of flag.
     * @param headOfFlag the new head of flag
     */
    public void setHeadOfFlag(boolean headOfFlag) {

        this.headOfFlag = headOfFlag;
    }

    /**
     * Gets the head family.
     * @return the head family
     */
    public FarmerFamily getHeadFamily() {

        return headFamily;
    }

    /**
     * Sets the head family.
     * @param headFamily the new head family
     */
    public void setHeadFamily(FarmerFamily headFamily) {

        this.headFamily = headFamily;
    }

    /**
     * Gets the head of the family.
     * @return the head of the family
     */
    public long getHeadOfTheFamily() {

        return headOfTheFamily;
    }

    /**
     * Sets the head of the family.
     * @param headOfTheFamily the new head of the family
     */
    public void setHeadOfTheFamily(long headOfTheFamily) {

        this.headOfTheFamily = headOfTheFamily;
    }

    /**
     * Gets the current family.
     * @return the current family
     */
    public long getCurrentFamily() {

        return currentFamily;
    }

    /**
     * Sets the current family.
     * @param currentFamily the new current family
     */
    public void setCurrentFamily(long currentFamily) {

        this.currentFamily = currentFamily;
    }

    /**
     * Gets the farmer detail params.
     * @return the farmer detail params
     */
    @SuppressWarnings("deprecation")
    public String getFarmerDetailParams() {

        return "tabIndex=" + URLEncoder.encode(tabIndex) + "&id=" + getFarmerId() + "&" + tabIndex;
    }

    /**
     * Age validate.
     * @param age the age
     * @return the string
     */
    public String ageValidate(int age) {

        if (age == 0 || age == -1)
            return "";
        else
            return String.valueOf(age);
    }

    /**
     * Sets the educations.
     * @param educations the educations
     */
    public void setEducations(Map<Integer, String> educations) {

        this.educations = educations;
    }

    /**
     * Gets the educations.
     * @return the educations
     */
    public Map<Integer, String> getEducations() {

        if (educations.size() == 0) {
            String[] education = getText("edu").split(",");
            int i=1;
            for(String eduValue :education )
                educations.put(i++, eduValue);
        }
        return educations;
    }

    /**
     * Sets the relations.
     * @param relations the relations
     */
    public void setRelations(Map<Integer, String> relations) {

        this.relations = relations;
    }

    /**
     * Gets the relations.
     * @return the relations
     */
    public Map<Integer, String> getRelations() {

        return relations;
    }

    /**
     * Gets the gender type.
     * @return the gender type
     */
    public Map<String, String> getGenderType() {

        return genderType;
    }

    /**
     * Sets the gender type.
     * @param genderType the gender type
     */
    public void setGenderType(Map<String, String> genderType) {

        this.genderType = genderType;
    }
    
    

    public Map<String, String> getDisabilityType() {
		return disabilityType;
	}

	public void setDisabilityType(Map<String, String> disabilityType) {
		this.disabilityType = disabilityType;
	}

	public Map<Integer, String> getProfession() {
    
        return profession;
    }

    public void setProfession(Map<Integer, String> profession) {
    
        this.profession = profession;
    }

    public String getFarmerSeqId() {
    
        return farmerSeqId;
    }

    public void setFarmerSeqId(String farmerSeqId) {
    
        this.farmerSeqId = farmerSeqId;
    }

    public String getFarmerName() {
    
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
    
        this.farmerName = farmerName;
    }
    
    public Map<String, String> getCatalogueRelationshipList() {

		Map<String, String> housingTypeList = new LinkedHashMap<>();
		housingTypeList = getFarmCatalougeMap(Integer.valueOf(getText("relationship")));
		return housingTypeList;

	}

	public void setCatalogueRelationshipList(Map<String, String> catalogueRelationshipList) {
		this.catalogueRelationshipList = catalogueRelationshipList;
	}

	public void setMaritalSatuses(Map<Integer, String> maritalSatuses) {

		this.maritalSatuses = maritalSatuses;
	}

	/**
	 * Gets the marital satuses.
	 * 
	 * @return the marital satuses
	 */
	public Map<String, String> getMaritalSatuses() {

		Map<String, String> maritalSatuses = new HashMap<>();
		maritalSatuses = getFarmCatalougeMap(Integer.valueOf(getText("maritalStatus")));
		return maritalSatuses;

	}

	/**
	 * Sets the education list.
	 * 
	 * @param educationList
	 *            the education list
	 */
	public void setEducationList(Map<Integer, String> educationList) {

		this.educationList = educationList;
	}

	/**
	 * Gets the education list.
	 * 
	 * @return the education list
	 */
	/*
	 * public Map<String, String> getEducationList() { Map<String, String>
	 * educationMap = new LinkedHashMap<String, String>(); List<FarmCatalogue>
	 * educationList = farmerService
	 * .listCatelogueType(getText("educationType")); for (FarmCatalogue obj :
	 * educationList) { educationMap.put(obj.getCode(), obj.getName()); } return
	 * educationMap; }
	 */

	public Map<String, String> getEducationList() {
		Map<String, String> educationList = new LinkedHashMap<>();
		educationList = getFarmCatalougeMap(Integer.valueOf(getText("educationType")));
		return educationList;

	}
	
	public Map<String, String> getEducationStatusList() {

		Map<String, String> housingTypeList = new LinkedHashMap<>();
		housingTypeList = getFarmCatalougeMap(Integer.valueOf(getText("educationstatus")));
		return housingTypeList;

	}

	public void setEducationStatusList(Map<String, String> educationStatusList) {
		this.educationStatusList = educationStatusList;
	}

	public String getDisability() {
		return disability;
	}

	public void setDisability(String disability) {
		this.disability = disability;
	}

	
	
}
