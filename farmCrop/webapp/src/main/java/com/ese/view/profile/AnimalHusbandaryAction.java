/*
 * FarmInventoryAction.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

/**
 * The Class AnimalHusbandaryAction.
 */
public class AnimalHusbandaryAction extends SwitchValidatorAction {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(AnimalHusbandaryAction.class);
    public static final int SELECT = -1;
    public static final int OTHER = 99;


    private String id;
    private String farmId;
    private String selectedFarmAnimalDetail;
    private String tabIndex = "#tabs-1";
    private String tabIndexZ = "#tabs-3";
    private int selectedFarmAnimal;
    private int selectedFeedingUsed;
    private int selectedAnimalHousing;
    @Autowired
    private IFarmerService farmerService;
    private Map<Integer, String> farmAnimalList = new LinkedHashMap<Integer, String>();
    private Map<Integer, String> feedingUsedList = new LinkedHashMap<Integer, String>();
    private Map<Integer, String> animalHousingList = new LinkedHashMap<Integer, String>();
    private Farm farm;
    private AnimalHusbandary animalHusbandary;
    private Map<Integer,String> fooderList = new LinkedHashMap<Integer, String>();
    private Map<Integer,String> revenueList = new LinkedHashMap<Integer, String>();
    private String farmName;
    private String farmerId;
    private String farmerName;
    JSONObject jsonObject = new JSONObject();
    private String animalInvenJsonString = "";
    private String templateId;
    @Autowired
    private ICatalogueService catalogueService;
    
    /**
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

//        if (!ObjectUtil.isEmpty(animalHusbandary)) {
//            animalHusbandary.setFarmAnimal((selectedFarmAnimal != SELECT) ? animalHusbandary
//                    .getFarmAnimal() : SELECT);
//            animalHusbandary.setFeedingUsed((selectedFeedingUsed != SELECT) ? animalHusbandary
//                    .getFeedingUsed() : SELECT);
//            animalHusbandary.setAnimalHousing((selectedAnimalHousing != SELECT) ? animalHusbandary
//                    .getAnimalHousing() : SELECT);
//        }
        return animalHusbandary;
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#list()
     */
    public String list() throws Exception {

        setFarmIdToRequestAttribute();
        if (getCurrentPage() != null) {
            setCurrentPage(getCurrentPage());
        }
        request.setAttribute(HEADING, getText(LIST));
        setFarmIdToRequestAttribute();
        return LIST;
    }

    /**
     * Sets the farm id to request attribute.
     */
    private void setFarmIdToRequestAttribute() {

        if (!StringUtil.isEmpty(getFarmId())) {
            request.getSession().setAttribute("farmId", getFarmId());
        } else if (!StringUtil.isEmpty(request.getSession().getAttribute("farmId"))) {
            request.getSession()
                    .setAttribute("farmId", request.getSession().getAttribute("farmId"));
            setFarmId(String.valueOf(request.getSession().getAttribute("farmId")));
        }
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    public String data() throws Exception {

        Farm farmObj = farmerService.findFarmById(Long.valueOf(getFarmId()));
        AnimalHusbandary filter = new AnimalHusbandary();
       // filter.setFarm(farmObj);

        Map<String, String> searchRecord = getJQGridRequestParam();

//        if (!StringUtil.isEmpty(searchRecord.get("farmAnimal"))) {
//            filter.setFarmAnimal(Integer.valueOf(Integer.valueOf(searchRecord.get("farmAnimal"))));
//        } else {
//            filter.setFarmAnimal(-1);
//        }

        if (!StringUtil.isEmpty(searchRecord.get("animalCount"))) {
            filter.setAnimalCount(searchRecord.get("animalCount").trim());
        }

        if (!StringUtil.isEmpty(searchRecord.get("feedingUsed"))) {
            filter
                    .setFeedingUsed(Integer.valueOf(Integer
                            .valueOf(searchRecord.get("feedingUsed"))));
        } else {
            filter.setFeedingUsed(-1);
        }

//        if (!StringUtil.isEmpty(searchRecord.get("animalHousing"))) {
//            filter.setAnimalHousing(Integer.valueOf(Integer.valueOf(searchRecord
//                    .get("animalHousing"))));
//        } else {
//            filter.setAnimalHousing(-1);
//        }

        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());

        return sendJQGridJSONResponse(data);

    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj) {

        AnimalHusbandary animalHusbandary = (AnimalHusbandary) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
//        rows.add((animalHusbandary.getFarmAnimal() == SELECT) ? "" : (animalHusbandary.getFarmAnimal() == OTHER)?getFarmAnimalList().get(
//                animalHusbandary.getFarmAnimal())+" - "+(animalHusbandary.getOtherFarmAnimal()):getFarmAnimalList().get(animalHusbandary.getFarmAnimal()));
//       
        rows.add(animalHusbandary.getAnimalCount());
       /* rows.add((animalHusbandary.getFeedingUsed() == SELECT) ? "" : getFeedingUsedList().get(
                animalHusbandary.getFeedingUsed()));*/
//        rows.add((animalHusbandary.getAnimalHousing() == SELECT) ? "" :(animalHusbandary.getAnimalHousing() == OTHER)? getAnimalHousingList().get(
//                animalHusbandary.getAnimalHousing())+" - "+(animalHusbandary.getOtherAnimalHousing()):getAnimalHousingList().get(animalHusbandary.getAnimalHousing()));
        rows.add("<button class='ic-del' onclick='deleteAnimalHusbandry("
                + animalHusbandary.getId() + ")'>Delete</button");
        jsonObject.put("id", animalHusbandary.getId());
        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * Creates the.
     * @return the string
     * @throws Exception the exception
     */
    public String create() throws Exception {

        setFarmIdToRequestAttribute();
        if (animalHusbandary == null) {
            command = "create";
            request.setAttribute(HEADING, getText(CREATE));
            setSelectedFarmAnimal(SELECT);
            setSelectedFeedingUsed(SELECT);
            setSelectedAnimalHousing(SELECT);
        
            return INPUT;
        } else {
// set Farmer Id
            /*Farm farm = farmerService.findFarmById(Long.valueOf(getFarmId()));
            animalHusbandary.setFarmer(farmer);*/
//            animalHusbandary.setFarmAnimal((selectedFarmAnimal != SELECT) ? selectedFarmAnimal
//                    : SELECT);
 //           animalHusbandary.setFeedingUsed((selectedFeedingUsed != SELECT) ? selectedFeedingUsed
//                    : SELECT);
//            animalHusbandary
//                    .setAnimalHousing((selectedAnimalHousing != SELECT) ? selectedAnimalHousing
//                            : SELECT);
           
            farmerService.addAnimalHusbandary(animalHusbandary);

            return "farmDetail";
        }
    }

    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public String update() throws Exception {

        setFarmIdToRequestAttribute();
        if (id != null && !id.equals("")) {
            animalHusbandary = farmerService.findAnimalHusbandaryById(id);

            if (animalHusbandary == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }

//            selectedFarmAnimalDetail = (animalHusbandary.getFarmAnimal() == SELECT) ? ""
//                    : getFarmAnimalList().get(animalHusbandary.getFarmAnimal());
//
//            selectedFarmAnimal = (animalHusbandary.getFarmAnimal() == SELECT) ? SELECT
//                    : animalHusbandary.getFarmAnimal();
//            selectedFeedingUsed = (animalHusbandary.getFeedingUsed() == SELECT) ? SELECT
//                    : animalHusbandary.getFeedingUsed();
//            selectedAnimalHousing = (animalHusbandary.getAnimalHousing() == SELECT) ? SELECT
//                    : animalHusbandary.getAnimalHousing();
           
            setCurrentPage(getCurrentPage());
            id = null;
            command = UPDATE;
            request.setAttribute(HEADING, getText(UPDATE));
        } else {
            if (animalHusbandary != null) {
                AnimalHusbandary temp = farmerService.findAnimalHusbandaryById(animalHusbandary
                        .getId());
                if (temp == null) {
                    addActionError(NO_RECORD);
                    return REDIRECT;
                }
                setCurrentPage(getCurrentPage());
               // temp.setFarmAnimal((selectedFarmAnimal != SELECT) ? selectedFarmAnimal : SELECT);
                temp.setOtherFarmAnimal(animalHusbandary.getOtherFarmAnimal());
                temp.setAnimalCount(animalHusbandary.getAnimalCount());
                //temp.setFeedingUsed((selectedFeedingUsed != SELECT) ? selectedFeedingUsed : SELECT);
                temp.setFodder(animalHusbandary.getFodder());
                temp.setOtherFodder(animalHusbandary.getOtherFodder());
              //  temp.setAnimalHousing((selectedAnimalHousing != SELECT) ? selectedAnimalHousing
             //           : SELECT);
                temp.setOtherAnimalHousing(animalHusbandary.getOtherAnimalHousing());
                temp.setProduction(animalHusbandary.getProduction());
                temp.setRevenue(animalHusbandary.getRevenue());
                temp.setOtherRevenue(animalHusbandary.getOtherRevenue());
                temp.setFymEstimatedOutput(animalHusbandary.getFymEstimatedOutput());
                temp.setManureCollected(animalHusbandary.getManureCollected());
                temp.setUrineCollected(animalHusbandary.getUrineCollected());
                farmerService.editAnimalHusbandary(temp);
            }
            request.setAttribute(HEADING, getText(LIST));
            return "farmDetail";
        }
        return super.execute();
    }

    /**
     * Detail.
     * @return the string
     * @throws Exception the exception
     */
    public String detail() throws Exception {

        setFarmIdToRequestAttribute();
        String view = "";
        if (id != null && !id.equals("")) {
            // animalHusbandary = farmService.findAnimalHusbandaryById((id));
            if (animalHusbandary == null) {
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
     * Delete.
     * @return the string
     * @throws Exception the exception
     */
    public String delete() throws Exception {

        setFarmIdToRequestAttribute();
        if (this.getId() != null && !(this.getId().equals(EMPTY))) {
            animalHusbandary = farmerService.findAnimalHusbandaryById(id);
            if (animalHusbandary == null) {
                addActionError(NO_RECORD);
                return "farmDetail";
            }
            setCurrentPage(getCurrentPage());
            farmerService.removeAnimalHusbandary(animalHusbandary);
        }

        request.setAttribute(HEADING, getText(LIST));
        return "farmDetail";

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
     * Gets the farm.
     * @return the farm
     */
    public Farm getFarm() {

        return farm;
    }

    /**
     * Sets the farm.
     * @param farm the new farm
     */
    public void setFarm(Farm farm) {

        this.farm = farm;
    }

    /**
     * Gets the farm id.
     * @return the farm id
     */
    public String getFarmId() {

        return farmId;
    }

    /**
     * Sets the farm id.
     * @param farmId the new farm id
     */
    public void setFarmId(String farmId) {

        this.farmId = farmId;
    }

    /**
     * Gets the animal husbandary.
     * @return the animal husbandary
     */
    public AnimalHusbandary getAnimalHusbandary() {

        return animalHusbandary;
    }

    /**
     * Sets the animal husbandary.
     * @param animalHusbandary the new animal husbandary
     */
    public void setAnimalHusbandary(AnimalHusbandary animalHusbandary) {

        this.animalHusbandary = animalHusbandary;
    }

    /**
     * Gets the selected farm animal.
     * @return the selected farm animal
     */
    public int getSelectedFarmAnimal() {

        return selectedFarmAnimal;
    }

    /**
     * Sets the selected farm animal.
     * @param selectedFarmAnimal the new selected farm animal
     */
    public void setSelectedFarmAnimal(int selectedFarmAnimal) {

        this.selectedFarmAnimal = selectedFarmAnimal;
    }

    /**
     * Gets the selected feeding used.
     * @return the selected feeding used
     */
    public int getSelectedFeedingUsed() {

        return selectedFeedingUsed;
    }

    /**
     * Sets the selected feeding used.
     * @param selectedFeedingUsed the new selected feeding used
     */
    public void setSelectedFeedingUsed(int selectedFeedingUsed) {

        this.selectedFeedingUsed = selectedFeedingUsed;
    }

    /**
     * Gets the selected animal housing.
     * @return the selected animal housing
     */
    public int getSelectedAnimalHousing() {

        return selectedAnimalHousing;
    }

    /**
     * Sets the selected animal housing.
     * @param selectedAnimalHousing the new selected animal housing
     */
    public void setSelectedAnimalHousing(int selectedAnimalHousing) {

        this.selectedAnimalHousing = selectedAnimalHousing;
    }

    /**
     * Sets the tab index.
     * @param tabIndex the new tab index
     */
    public void setTabIndex(String tabIndex) {

        this.tabIndex = tabIndex;
    }

    /**
     * Gets the tab index.
     * @return the tab index
     */
    public String getTabIndex() {

        return tabIndex;
    }

    /**
     * Gets the farm detail params.
     * @return the farm detail params
     */
    public String getFarmDetailParams() {

        return "tabIndex=" + URLEncoder.encode(tabIndex) + "&id=" + getFarmId() + "&" + tabIndex;
    }

    /**
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
    }

    /**
     * Gets the farmer service.
     * @return the farmer service
     */
    public IFarmerService getFarmerService() {

        return farmerService;
    }

    /**
     * Sets the farm animal list.
     * @param farmAnimalList the farm animal list
     */
    public void setFarmAnimalList(Map<Integer, String> farmAnimalList) {

        this.farmAnimalList = farmAnimalList;
    }

    /**
     * Gets the farm animal list.
     * @return the farm animal list
     */
    public Map<Integer, String> getFarmAnimalList() {
        if (farmAnimalList.size() == 0) {
            farmAnimalList = getPropertyData("farmAnimalList");       
          }
        return farmAnimalList;
    }

    /**
     * Sets the feeding used list.
     * @param feedingUsedList the feeding used list
     */
    public void setFeedingUsedList(Map<Integer, String> feedingUsedList) {

        this.feedingUsedList = feedingUsedList;
    }

    /**
     * Gets the feeding used list.
     * @return the feeding used list
     */
    public Map<Integer, String> getFeedingUsedList() {
        if (feedingUsedList.size() == 0) {
            feedingUsedList = getPropertyData("feedingUsedList");       
          }
        return feedingUsedList;
    }

    /**
     * Sets the animal housing list.
     * @param animalHousingList the animal housing list
     */
    public void setAnimalHousingList(Map<Integer, String> animalHousingList) {

        this.animalHousingList = animalHousingList;
    }

    /**
     * Gets the animal housing list.
     * @return the animal housing list
     */
    public Map<Integer, String> getAnimalHousingList() {
        if (animalHousingList.size() == 0) {
            animalHousingList = getPropertyData("animalHousingList");       
          }
        return animalHousingList;
    }

    /**
     * Sets the selected farm animal detail.
     * @param selectedFarmAnimalDetail the new selected farm animal detail
     */
    public void setSelectedFarmAnimalDetail(String selectedFarmAnimalDetail) {

        this.selectedFarmAnimalDetail = selectedFarmAnimalDetail;
    }

    /**
     * Gets the selected farm animal detail.
     * @return the selected farm animal detail
     */
    public String getSelectedFarmAnimalDetail() {

        return selectedFarmAnimalDetail;
    }

    public Map<Integer, String> getFooderList() {
        if (fooderList.size() == 0) {
            fooderList = getPropertyData("fooderList");       
        }
      return fooderList;
    }

    public void setFooderList(Map<Integer, String> fooderList) {
    
        this.fooderList = fooderList;
    }

    public Map<Integer, String> getRevenueList() {
      if (revenueList.size() == 0) {
                revenueList = getPropertyData("revenueList");       
               }
        return revenueList;
    }

    public void setRevenueList(Map<Integer, String> revenueList) {
    
        this.revenueList = revenueList;
    }

    public String getFarmerId() {
    
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
    
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
    
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
    
        this.farmerName = farmerName;
    }

    public String getFarmName() {
    
        return farmName;
    }

    public void setFarmName(String farmName) {
    
        this.farmName = farmName;
    }

    public String getTabIndexZ() {
    
        return tabIndexZ;
    }

    public void setTabIndexZ(String tabIndexZ) {
    
        this.tabIndexZ = tabIndexZ;
    }
    
    public void prepare() throws Exception {
        String farmUniqueId = (String)request.getParameter("farmId");
        String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
				.getSimpleName();
		String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
		if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
			content = super.getText(BreadCrumb.BREADCRUMB, "");
		}else{
			
		}
		request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content+farmUniqueId+"&"+getTabIndexZ()));
       
    }
    @SuppressWarnings("unchecked")
	public String populateAnimalInventryList() throws Exception {
        //String fodderValue="";
        JSONArray array = new JSONArray();

        List<AnimalHusbandary> invList1 = farmerService.listAnimalHusbandaryList(Long.valueOf(id));

        for (AnimalHusbandary ah : invList1) {
           // Catalogue cat =  catalogueService.findCatalogueById(Long.valueOf(fm.getInventoryItem()));
            JSONObject obj = new JSONObject();
            obj.put("id", ah.getId());
            obj.put("item1",ah.getFarmAnimal().getName());            
            obj.put("cout", ah.getAnimalCount());
            //obj.put("farmerName", ah.getFarmer());
            if(ah.getFodder()!=null)
            {
            	String fodderValue="";
            	FarmCatalogue fodder = new FarmCatalogue();
                String fodderArr[] = ah.getFodder().split("\\,");                             
                for (int i = 0; i < fodderArr.length; i++) {
                    String fodderTrim = fodderArr[i].replaceAll("\\s+", "");
                    if(!StringUtil.isEmpty(fodderTrim)){
                    fodder  = catalogueService.findCatalogueById(Long.valueOf(fodderTrim));
                    if(!ObjectUtil.isEmpty(fodder)){
                        fodderValue += fodder.getName() +",";
                        fodderValue = fodderValue.substring(0, fodderValue.length() - 1) +",";
                    }else{
                        fodderValue +="";
                    }
                }
                 
                }   
                if (fodderValue.endsWith(",")) {
                	fodderValue = fodderValue.substring(0, fodderValue.length() - 1);
                }
                obj.put("item2",fodderValue);
            }else{
                obj.put("item2","");
            }
            if(ah.getAnimalHousing()!=null){
            obj.put("item3",ah.getAnimalHousing().getName());
            }else{
                obj.put("item3","");
            }
            if(ah.getRevenue()!=null){
            //obj.put("item4",ah.getRevenue().getName());
            	obj.put("item4",ah.getRevenue());
            }else{
                obj.put("item4","");
            }
            
            if(ah.getBreed()==null){
            	 obj.put("item23","");
                }else{
                	obj.put("item23",ah.getBreed());
                }
          
            obj.put("manureCollected", ah.getManureCollected());
            obj.put("urineCollected", ah.getUrineCollected());
                
            array.add(obj);
        }
        JSONObject mainObj = new JSONObject();
        mainObj.put("data", array);
        sendResponse(mainObj);
        return null;
    }
    public void populateAnimalInventry() throws Exception {
       
        String msg = "";
        String fodderValue="";
       Type listType1 = new TypeToken<List<AnimalHusbandary>>() {}.getType();
       System.out.println("animalInvenJsonString"+animalInvenJsonString);
      // System.out.println("animalInvenJsonString"+animalInvenJsonString);
       List<AnimalHusbandary> animalHusbandaryList = new Gson().fromJson(animalInvenJsonString, listType1);
       // List<AnimalHusbandary> invList1 = farmerService.listAnimalHusbandaryList(Long.valueOf(farmId));
        
       if (!ObjectUtil.isEmpty(animalHusbandaryList)) {
            for (int j = 0; j < animalHusbandaryList.size(); j++) {
                
                FarmCatalogue farmAnimal=catalogueService.findCatalogueByCode(animalHusbandaryList.get(j).getAnimalStr());
                FarmCatalogue housingStr=catalogueService.findCatalogueByCode(animalHusbandaryList.get(j).getHousStr());
                    AnimalHusbandary Inventory1 = new AnimalHusbandary();    
                    AnimalHusbandary  farmInve;
                  if(getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)){
                	 farmInve = (AnimalHusbandary)farmerService.findAnimalHusbandaryByFarmerIdAndId(Long.valueOf(farmerId),Long.valueOf(farmAnimal.getId()));
                  }else{
                     farmInve = (AnimalHusbandary)farmerService.findAnimalHusbandaryByFarmAndOtherItems(Long.valueOf(farmerId),Long.valueOf(farmAnimal.getId()),animalHusbandaryList.get(j).getRevenueStr(),Long.valueOf(housingStr.getId()));
                  }
                     if(farmInve != null && !ObjectUtil.isEmpty(farmInve) ){
                         Integer valeu = Integer.valueOf(farmInve.getAnimalCount())+Integer.valueOf(animalHusbandaryList.get(j).getAnimalCount());
                         farmInve.setAnimalCount(String.valueOf(valeu));
                         farmerService.updateAnimalInventory(farmInve);
                         
                    }
                    else{

                        Farmer farmer = farmerService.findFarmerById(Long.valueOf(farmerId));
                        if(getCurrentTenantId().equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)){
                        	if(!StringUtil.isEmpty(animalHusbandaryList.get(j).getAnimalStr())&&!StringUtil.isEmpty(animalHusbandaryList.get(j).getAnimalCount())){
                        		 Inventory1.setFarmAnimal(catalogueService.findCatalogueByCode(animalHusbandaryList.get(j).getAnimalStr()));
                        		 Inventory1.setAnimalCount(animalHusbandaryList.get(j).getAnimalCount());
                        		 Inventory1.setFarmer(farmer);
     	                        farmerService.addAnimalInventory(Inventory1);
                        	}
                        }else{
                        if(!StringUtil.isEmpty(animalHusbandaryList.get(j).getAnimalStr())&&!StringUtil.isEmpty(animalHusbandaryList.get(j).getFodderStr())
                        &&!StringUtil.isEmpty(animalHusbandaryList.get(j).getHousStr())&&!StringUtil.isEmpty(animalHusbandaryList.get(j).getRevenueStr())
                        &&!StringUtil.isEmpty(animalHusbandaryList.get(j).getAnimalCount())){
	                        Inventory1.setFarmAnimal(catalogueService.findCatalogueByCode(animalHusbandaryList.get(j).getAnimalStr()));
	                        if (!StringUtil.isEmpty(animalHusbandaryList.get(j).getFodderStr())) {
	                            FarmCatalogue fodder = new FarmCatalogue();
	                            String fodderArr[] = animalHusbandaryList.get(j).getFodderStr().split("\\,");                             
	                            for (int i = 0; i < fodderArr.length; i++) {
	                                String fodderTrim = fodderArr[i].replaceAll("\\s+", "");
	                                fodder  = catalogueService.findCatalogueByCode(fodderTrim);
	                                
	                                fodderValue += fodder.getId() +",";
	                             
	                            }   
	                            fodderValue = fodderValue.substring(0, fodderValue.length() - 1);
	                        }
	                        Inventory1.setFodder(fodderValue);
	                        fodderValue="";
	                       // Inventory1.setFodder(catalogueService.findCatalogueByCode(animalHusbandaryList.get(j).getFodderStr()));
	                        Inventory1.setAnimalHousing(catalogueService.findCatalogueByCode(animalHusbandaryList.get(j).getHousStr()));
	                        //Inventory1.setRevenue(catalogueService.findCatalogueByCode(animalHusbandaryList.get(j).getRevenueStr()));
	                        Inventory1.setRevenue(animalHusbandaryList.get(j).getRevenueStr());
	                        Inventory1.setAnimalCount(animalHusbandaryList.get(j).getAnimalCount());
	                        Inventory1.setBreed(!StringUtil.isEmpty(animalHusbandaryList.get(j).getBreedStr())?animalHusbandaryList.get(j).getBreedStr():"");
	                        if(getCurrentTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
   	                        	Inventory1.setManureCollected(animalHusbandaryList.get(j).getManureCollected());
   	                        	Inventory1.setUrineCollected(animalHusbandaryList.get(j).getUrineCollected());
   	                        }
	                        Inventory1.setFarmer(farmer);
	                        farmerService.addAnimalInventory(Inventory1);          
                        }
                        }
                    }
                   
                
              
                // inventryArr.add(getJSONObject(state.getId(), state.getName()));
            }
         
            jsonObject.put("msg", "success");
        }

        sendAjaxResponse(jsonObject);
    }

    public String deleteAnimalInventry() {

        AnimalHusbandary inventry = farmerService.findAnimalHusbandaryById(templateId);
        farmerService.removeAnimalHusbandary(inventry);
        try {
            JSONObject js = new JSONObject();
            js.put("msg", getText("msg.removed"));
            sendAjaxResponse(js);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getAnimalInvenJsonString() {
    
        return animalInvenJsonString;
    }

    public void setAnimalInvenJsonString(String animalInvenJsonString) {
    
        this.animalInvenJsonString = animalInvenJsonString;
    }

    public ICatalogueService getCatalogueService() {
    
        return catalogueService;
    }

    public void setCatalogueService(ICatalogueService catalogueService) {
    
        this.catalogueService = catalogueService;
    }

    public String getTemplateId() {
    
        return templateId;
    }

    public void setTemplateId(String templateId) {
    
        this.templateId = templateId;
    }

}
