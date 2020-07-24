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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;
import com.sourcetrace.eses.BreadCrumb;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.JsonDataObject;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmInventory;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

// TODO: Auto-generated Javadoc
/**
 * The Class FarmInventoryAction.
 */
public class FarmInventoryAction extends SwitchValidatorAction {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(FarmInventoryAction.class);

    /** The Constant SELECT. */
    public static final int SELECT = -1;

    /** The Constant OTHER. */
    public static final int OTHER = 99;

    /** The id. */
    private String id;

    /** The farm id. */
    private String farmId;

    /** The farm name. */
    private String farmName;

    /** The selected farm inventory item detail. */
    private String selectedFarmInventoryItemDetail;

    /** The tab index. */
    private String tabIndex = "#tabs-1";

    /** The tab index z. */
    private String tabIndexZ = "#tabs-2";

    /** The selected farm inventory item. */
    private int selectedFarmInventoryItem;

    /** The inventory items list. */
    private Map<Integer, String> inventoryItemsList = new LinkedHashMap<Integer, String>();

    /** The farm. */
    private Farm farm;

    /** The farm inventory. */
    private FarmInventory farmInventory;

    /** The farmer service. */
    private IFarmerService farmerService;

    /** The farmer id. */
    private String farmerId;

    /** The farmer name. */
    private String farmerName;

    /** The catalogue list. */
    List<FarmCatalogue> FarmCatalogueList = new ArrayList<FarmCatalogue>();

    /** The farm inventory list. */
    List<FarmInventory> farmInventoryList = new ArrayList<FarmInventory>();

    /** The farm inven json string. */
    private String farmInvenJsonString = "";

    /** The json data object. */
    private JsonDataObject jsonDataObject = new JsonDataObject();

    /** The gson. */
    private Gson gson = new Gson();
    @Autowired
    private IUniqueIDGenerator idGenerator;

    /** The json object. */
    JSONObject jsonObject = new JSONObject();
    private String templateId;
    @Autowired
    private ICatalogueService catalogueService;
    
    private String otherInventoryItem;
    private String farmerIdId;

    /**
     * Gets the data.
     * @return the data
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

//        if (!ObjectUtil.isEmpty(farmInventory)) {
//            farmInventory
//                    .setInventoryItem((selectedFarmInventoryItem != SELECT) ? selectedFarmInventoryItem
//                            : SELECT);
//
//        }
        return farmInventory;
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
        FarmInventory filter = new FarmInventory();
        //filter.setFarm(farmObj);

        Map<String, String> searchRecord = getJQGridRequestParam();

        if (!StringUtil.isEmpty(searchRecord.get("itemCount"))) {
            filter.setItemCount(searchRecord.get("itemCount"));
        }

        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());

        return sendJQGridJSONResponse(data);

    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#toJSON(java.lang.Object)
     */
    public JSONObject toJSON(Object obj) {

        FarmInventory farmInventory = (FarmInventory) obj;
        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        /*
         * rows.add("<font color=\"#0000FF\" style=\"cursor:pointer;\">" + farmInventory.getId() +
         * "</font>");
         */

        rows.add((farmInventory.getInventoryItem() == null) ? "" : (farmInventory
                .getInventoryItem().getName()));
        rows.add(farmInventory.getItemCount());
        /*
         * rows.add("<button class='ic-del' onclick='deleteFarmInventory(" + farmInventory.getId() +
         * ")'>Save</button"+"  "+"<button class='ic-del' onclick='deleteFarmInventory(" +
         * farmInventory.getId() + ")'>Delete</button");
         */
        rows.add("");
        jsonObject.put("id", farmInventory.getId());
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

//        if (farmInventory == null) {
//            command = "create";
//            request.setAttribute(HEADING, getText(CREATE));
//            setSelectedFarmInventoryItem(SELECT);
//            return INPUT;
//        } else {
//            Farm farm = farmerService.findFarmById(Long.valueOf(getFarmId()));
//            farmInventory.setFarm(farm);
//            Catalogue cat = catalogueService.findCatalogueById(selectedFarmInventoryItem);
//            farmInventory
//                    .setInventoryItem(cat);
//            FarmInventory existingFarmInventory = farmerService.findFarmInventoryItem(farmInventory
//                    .getFarm().getId(), farmInventory.getInventoryItem().geti);
//            if (!ObjectUtil.isEmpty(existingFarmInventory)
//                    && existingFarmInventory.getInventoryItem() == farmInventory.getInventoryItem()
//                    && StringUtil.isLong(existingFarmInventory.getItemCount())
//                    && StringUtil.isLong(farmInventory.getItemCount())) {
//                int itemcount = Integer.valueOf(existingFarmInventory.getItemCount())
//                        + Integer.valueOf(farmInventory.getItemCount());
//                existingFarmInventory.setItemCount(String.valueOf(itemcount));
//                existingFarmInventory.setOtherInventoryItem(farmInventory.getOtherInventoryItem());
//                farmerService.editFarmInventory(existingFarmInventory);
//            } else
//                farmerService.addFarmInventory(farmInventory);

            return "farmDetail";
        }
//    }

    public String saveInventory() throws Exception {

        return null;

    }

    /**
     * Update.
     * @return the string
     * @throws Exception the exception
     */
    public String update() throws Exception {

        setFarmIdToRequestAttribute();
        if (id != null && !id.equals("")) {
            farmInventory = farmerService.findFarmInventoryById(id);

            if (farmInventory == null) {
                addActionError(NO_RECORD);
                return REDIRECT;
            }

//            selectedFarmInventoryItemDetail = (farmInventory.getInventoryItem() == SELECT) ? ""
//                    : getInventoryItemsList().get(farmInventory.getInventoryItem());
//
//            selectedFarmInventoryItem = (farmInventory.getInventoryItem() == SELECT) ? SELECT
//                    : farmInventory.getInventoryItem();

            setCurrentPage(getCurrentPage());
            id = null;
            command = UPDATE;
            request.setAttribute(HEADING, getText(UPDATE));
        } else {
            if (farmInventory != null) {
                FarmInventory temp = farmerService.findFarmInventoryById(farmInventory.getId());
                if (temp == null) {
                    addActionError(NO_RECORD);
                    return REDIRECT;
                }
                setCurrentPage(getCurrentPage());
//                temp
//                        .setInventoryItem((selectedFarmInventoryItem != SELECT) ? selectedFarmInventoryItem
//                                : SELECT);
                /*
                 * temp.setInventoryItem(farmService
                 * .findSelectedInventoryItems(selectedFarmInventoryItem));
                 */
                temp.setItemCount(farmInventory.getItemCount());
                temp.setOtherInventoryItem(farmInventory.getOtherInventoryItem());

                farmerService.editFarmInventory(temp);
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
            farmInventory = farmerService.findFarmInventoryById((id));
            if (farmInventory == null) {
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
            farmInventory = farmerService.findFarmInventoryById((id));
            if (farmInventory == null) {
                addActionError(NO_RECORD);
                return "farmDetail";
            }
            setCurrentPage(getCurrentPage());

            farmerService.removeFarmInventory(farmInventory);
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
     * Gets the farm inventory.
     * @return the farm inventory
     */
    public FarmInventory getFarmInventory() {

        return farmInventory;
    }

    /**
     * Sets the farm inventory.
     * @param farmInventory the new farm inventory
     */
    public void setFarmInventory(FarmInventory farmInventory) {

        this.farmInventory = farmInventory;
    }

    /**
     * Gets the farm name.
     * @return the farm name
     */
    public String getFarmName() {

        return farmName;
    }

    /**
     * Sets the farm name.
     * @param farmName the new farm name
     */
    public void setFarmName(String farmName) {

        this.farmName = farmName;
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
     * Gets the selected farm inventory item.
     * @return the selected farm inventory item
     */
    public int getSelectedFarmInventoryItem() {

        return selectedFarmInventoryItem;
    }

    /**
     * Sets the selected farm inventory item.
     * @param selectedFarmInventoryItem the new selected farm inventory item
     */
    public void setSelectedFarmInventoryItem(int selectedFarmInventoryItem) {

        this.selectedFarmInventoryItem = selectedFarmInventoryItem;
    }

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
     * Sets the inventory items list.
     * @param inventoryItemsList the inventory items list
     */
    public void setInventoryItemsList(Map<Integer, String> inventoryItemsList) {

        this.inventoryItemsList = inventoryItemsList;
    }

    /**
     * Gets the inventory items list.
     * @return the inventory items list
     */
    public Map<Integer, String> getInventoryItemsList() {

        if (inventoryItemsList.size() == 0) {
            inventoryItemsList = getPropertyData("inventoryItemsList");
            return inventoryItemsList;
        }
        return inventoryItemsList;
    }

    /**
     * Sets the selected farm inventory item detail.
     * @param selectedFarmInventoryItemDetail the new selected farm inventory item detail
     */
    public void setSelectedFarmInventoryItemDetail(String selectedFarmInventoryItemDetail) {

        this.selectedFarmInventoryItemDetail = selectedFarmInventoryItemDetail;
    }

    /**
     * Gets the selected farm inventory item detail.
     * @return the selected farm inventory item detail
     */
    public String getSelectedFarmInventoryItemDetail() {

        return selectedFarmInventoryItemDetail;
    }

    /**
     * Gets the farmer id.
     * @return the farmer id
     */
    public String getFarmerId() {

        return farmerId;
    }

    /**
     * Sets the farmer id.
     * @param farmerId the new farmer id
     */
    public void setFarmerId(String farmerId) {

        this.farmerId = farmerId;
    }

    /**
     * Gets the farmer name.
     * @return the farmer name
     */
    public String getFarmerName() {

        return farmerName;
    }

    /**
     * Sets the farmer name.
     * @param farmerName the new farmer name
     */
    public void setFarmerName(String farmerName) {

        this.farmerName = farmerName;
    }

    /**
     * Gets the tab index z.
     * @return the tab index z
     */
    public String getTabIndexZ() {

        return tabIndexZ;
    }

    /**
     * Sets the tab index z.
     * @param tabIndexZ the new tab index z
     */
    public void setTabIndexZ(String tabIndexZ) {

        this.tabIndexZ = tabIndexZ;
    }

    public void prepare() throws Exception {

        String farmUniqueId = (String) request.getParameter("farmId");
    	String actionClassName = ActionContext.getContext().getActionInvocation().getAction().getClass()
				.getSimpleName();
		String content = getLocaleProperty(actionClassName + "." + BreadCrumb.BREADCRUMB);
		if (StringUtil.isEmpty(content) || (content.equalsIgnoreCase(actionClassName + "." + BreadCrumb.BREADCRUMB))) {
			content = super.getText(BreadCrumb.BREADCRUMB, "");
		}
		if (!StringUtil.isEmpty(farmUniqueId)) {

			request.setAttribute(BreadCrumb.BREADCRUMB,
					BreadCrumb.getBreadCrumb(content) + farmUniqueId + "&" + getTabIndexZ());

		} else {
			request.setAttribute(BreadCrumb.BREADCRUMB, BreadCrumb.getBreadCrumb(content) + "&" + getTabIndexZ());
		}
    }

    /**
     * Gets the catalogue list.
     * @return the catalogue list
     */
    public List<FarmCatalogue> getFarmCatalogueList() {

        FarmCatalogueList = farmerService.listFarmEquipmentBasedOnType();

        return FarmCatalogueList;
    }

    /**
     * Sets the catalogue list.
     * @param catalogueList the new catalogue list
     */
    public void setFarmCatalogueList(List<FarmCatalogue> FarmCatalogueList) {

        this.FarmCatalogueList = FarmCatalogueList;
    }

    /**
     * Gets the farm inventory list.
     * @return the farm inventory list
     */
    public List<FarmInventory> getFarmInventoryList() {

        return farmInventoryList;
    }

    /**
     * Sets the farm inventory list.
     * @param farmInventoryList the new farm inventory list
     */
    public void setFarmInventoryList(List<FarmInventory> farmInventoryList) {

        this.farmInventoryList = farmInventoryList;
    }

    public void populateInventry() throws Exception {

        String msg = "";
        Type listType = new TypeToken<List<FarmInventory>>() {
        }.getType();
        List<FarmInventory> farmInventoryList = new Gson().fromJson(farmInvenJsonString, listType);
        if (!ObjectUtil.isEmpty(farmInventoryList)) {
            for (int j = 0; j < farmInventoryList.size(); j++) {
                FarmInventory Inventory = new FarmInventory();
                Farmer farmer = farmerService.findFarmerByFarmerId(farmerIdId);
                FarmInventory fm = farmerService.findFarmInventoryItem(Long.valueOf(farmerId), farmInventoryList.get(j).getInvItem());
               if(fm!=null && !ObjectUtil.isEmpty(fm)){
                   String val = String.valueOf(Integer.valueOf(fm.getItemCount())+Integer.valueOf(farmInventoryList.get(j).getItemCount()));
                   fm.setItemCount(val);
                   farmerService.updateFarmInventory(fm);
               }else{
                String invString = farmInventoryList.get(j).getInvItem();
                FarmCatalogue cat = catalogueService.findCatalogueByCode(invString);
                Inventory.setInventoryItem(cat);
                Inventory.setItemCount(farmInventoryList.get(j).getItemCount());
                Inventory.setFarmer(farmer);
                farmerService.addFarmInventory(Inventory);
               }
                // inventryArr.add(getJSONObject(state.getId(), state.getName()));
            }
            jsonObject.put("msg", "success");
        }

        sendAjaxResponse(jsonObject);
    }
public void populateFramCatalogue(){
	JSONArray jsonArray=new JSONArray();
	if(!StringUtil.isEmpty(otherInventoryItem)){
		
		FarmCatalogue eCatalogue = catalogueService.findCatalogueByName(otherInventoryItem.trim());
        if (eCatalogue != null ) {
        	JSONObject jsonObject=new JSONObject();
			jsonObject.put("error", getText("unique.equipmentName"));
		
			jsonArray.add(jsonObject);
           
        } else{
     
	FarmCatalogue farmCatalogue=new FarmCatalogue();
	farmCatalogue.setName(otherInventoryItem);
	farmCatalogue.setTypez(FarmCatalogue.FARM_EQUIPMENT);
	farmCatalogue.setBranchId(getBranchId());
	farmCatalogue.setCode(idGenerator.getCatalogueIdSeq());
	farmCatalogue.setRevisionNo(DateUtil.getRevisionNumber());
	farmCatalogue.setStatus(FarmCatalogue.ACTIVE);
	catalogueService.addCatalogue(farmCatalogue);
   	
        }
        
	}
if(jsonArray.isEmpty()){
		List<Object[]>farmCatalogueCodeAndName=catalogueService.listFramEquipments();
	if(!ObjectUtil.isEmpty(farmCatalogueCodeAndName)){
	
		for(Object[] obj:farmCatalogueCodeAndName){
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("code", obj[0]);
			jsonObject.put("name", obj[1]);
			jsonArray.add(jsonObject);
		}
		
	}
}
	sendAjaxResponse(jsonArray);
}

    /**
     * Gets the farm inven json string.
     * @return the farm inven json string
     */
    public String getFarmInvenJsonString() {

        return farmInvenJsonString;
    }

    /**
     * Sets the farm inven json string.
     * @param farmInvenJsonString the new farm inven json string
     */
    public void setFarmInvenJsonString(String farmInvenJsonString) {

        this.farmInvenJsonString = farmInvenJsonString;
    }

    @SuppressWarnings("unchecked")
	public String populateInventryList() throws Exception {

        JSONArray array = new JSONArray();

        List<FarmInventory> invList = farmerService.listFarmInventryList(Long.valueOf(id));

        for (FarmInventory fm : invList) {
           // Catalogue cat =  catalogueService.findCatalogueById(Long.valueOf(fm.getInventoryItem()));
            JSONObject obj = new JSONObject();
            obj.put("id", fm.getId());
            obj.put("item",fm.getInventoryItem().getName());
            obj.put("ct", fm.getItemCount());
            //obj.put("farmName", fm.getFarm().getFarmName());
            array.add(obj);
        }
        JSONObject mainObj = new JSONObject();
        mainObj.put("data", array);
        sendResponse(mainObj);
        return null;
    }

    /**
     * Gets the json data object.
     * @return the json data object
     */
    public JsonDataObject getJsonDataObject() {

        return jsonDataObject;
    }

    /**
     * Sets the json data object.
     * @param jsonDataObject the new json data object
     */
    public void setJsonDataObject(JsonDataObject jsonDataObject) {

        this.jsonDataObject = jsonDataObject;
    }

    /**
     * Gets the gson.
     * @return the gson
     */
    public Gson getGson() {

        return gson;
    }

    /**
     * Sets the gson.
     * @param gson the new gson
     */
    public void setGson(Gson gson) {

        this.gson = gson;
    }

    public String deleteInventry() {

        FarmInventory inventry = farmerService.findFarmInventoryById(templateId);
        farmerService.removeFarmInventry(inventry);
        try {
            JSONObject js = new JSONObject();
            js.put("msg", getText("msg.removed"));
            sendAjaxResponse(js);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTemplateId() {

        return templateId;
    }

    public void setTemplateId(String templateId) {

        this.templateId = templateId;
    }

    public ICatalogueService getCatalogueService() {
    
        return catalogueService;
    }

    public void setCatalogueService(ICatalogueService catalogueService) {
    
        this.catalogueService = catalogueService;
    }

	public String getOtherInventoryItem() {
		return otherInventoryItem;
	}

	public void setOtherInventoryItem(String otherInventoryItem) {
		this.otherInventoryItem = otherInventoryItem;
	}

	public String getFarmerIdId() {
		return farmerIdId;
	}

	public void setFarmerIdId(String farmerIdId) {
		this.farmerIdId = farmerIdId;
	}

	
	
	

}
