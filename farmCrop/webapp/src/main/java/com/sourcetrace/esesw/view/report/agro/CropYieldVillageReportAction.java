/*
 * CropYieldVillageReportAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.report.agro;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.dao.IReportDAO;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.view.BaseReportAction;

public class CropYieldVillageReportAction extends BaseReportAction {

    private static final long serialVersionUID = -9191437334851144561L;
    public static final String FARMER_DETAIL = "farmerDetail";
    private List<String> fields = new ArrayList<String>();
    private String id;
    private FarmCrops farmCrops;
    private IFarmerService farmerService;
    private ILocationService locationService;
    private String selectedVillage;

    /**
     * Gets the farm crops.
     * @return the farm crops
     */
    public FarmCrops getFarmCrops() {

        return farmCrops;
    }

    /**
     * Sets the farm crops.
     * @param farmCrops the new farm crops
     */
    public void setFarmCrops(FarmCrops farmCrops) {

        this.farmCrops = farmCrops;
    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#list()
     */
    public String list() throws Exception {

        fields.add(getText("village"));
        request.setAttribute(HEADING, getText(LIST));
        return LIST;
    }

    /**
     * Data.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

       JSONObject gridData = new JSONObject();
       JSONArray rows = new JSONArray();

       List<Object[]> listFarmCropsObject = farmerService.listFarmCropsByVillage(selectedVillage,getBranchId());
       List<Object[]> listFarmCropsObjectLimitList = farmerService.listFarmCropsByVillage(selectedVillage, getStartIndex(), getLimit(),getBranchId());

       if (!ObjectUtil.isListEmpty(listFarmCropsObjectLimitList)) {

            for (Object[] object : listFarmCropsObjectLimitList) {
                FarmCrops farmCropsObj = new FarmCrops();
                farmCropsObj.setVillageCode(String.valueOf(object[0]));
                farmCropsObj.setVillageName(String.valueOf(object[1]));
                farmCropsObj.setTotalArea(String.valueOf(object[2]));
                farmCropsObj.setTotalProduction(String.valueOf(object[3]));
                rows.add(toJSON(farmCropsObj, IReportDAO.MAIN_GRID));
            }
        }

        gridData.put(PAGE, getPage());

        totalRecords = listFarmCropsObject.size();
        gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getLimit()))));
        gridData.put(IReportDAO.START_INDEX, getStartIndex());
        gridData.put(IReportDAO.LIMIT, listFarmCropsObject.size());
        gridData.put(IReportDAO.RECORD_COUNT, listFarmCropsObject.size());
        gridData.put(ROWS, rows);
        PrintWriter out = response.getWriter();
        out.println(gridData.toString());
        return null;
    }

    /**
     * Sub list.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings( { "unchecked", "unchecked" })
    public String subList() throws Exception {

        JSONObject gridData = new JSONObject();
        JSONArray rows = new JSONArray();

        List<Object[]> listFarmCropsObject = farmerService.listFarmCropsDetailsByVillageCode(selectedVillage,getBranchId());
        List<Object[]> listFarmCropsObjectLimitList = farmerService.listFarmCropsDetailsByVillageCode(selectedVillage, getStartIndex(), getLimit(),getBranchId());

        if (!ObjectUtil.isListEmpty(listFarmCropsObjectLimitList)) {

            for (Object[] object : listFarmCropsObjectLimitList) {
                FarmCrops farmCropsObj = new FarmCrops();
                farmCropsObj.setCropCode(String.valueOf(object[0]));
                farmCropsObj.setCropName(String.valueOf(object[1]));
                farmCropsObj.setTotalArea(String.valueOf(object[2]));
                farmCropsObj.setTotalProduction(String.valueOf(object[3]));
                rows.add(toJSON(farmCropsObj, IReportDAO.SUB_GRID));
            }
        }

        gridData.put(PAGE, getPage());

        totalRecords = listFarmCropsObject.size();
        gridData.put(TOTAL, java.lang.Math.ceil(totalRecords / Double.valueOf(Integer.toString(getLimit()))));
        gridData.put(IReportDAO.START_INDEX, getStartIndex());
        gridData.put(IReportDAO.LIMIT, listFarmCropsObject.size());
        gridData.put(IReportDAO.RECORD_COUNT, listFarmCropsObject.size());
        gridData.put(ROWS, rows);
        PrintWriter out = response.getWriter();
        out.println(gridData.toString());
        return null;
    }

    /**
     * To json.
     * @param obj the obj
     * @param grid the grid
     * @return the jSON object
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON(Object obj, String grid) {

        FarmCrops famCrops = (FarmCrops) obj;

        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        DecimalFormat df = new DecimalFormat("0.000");

        if (grid.equalsIgnoreCase(IReportDAO.MAIN_GRID)) {
            rows.add(famCrops.getVillageCode());
            rows.add(famCrops.getVillageName());
            rows.add(df.format(Double.valueOf(famCrops.getTotalArea())));
            rows.add(df.format(Double.valueOf(famCrops.getTotalProduction())));
            jsonObject.put("id", famCrops.getVillageCode());
            jsonObject.put("cell", rows);
        }

        else if (grid.equalsIgnoreCase(IReportDAO.SUB_GRID)) {
            rows.add(famCrops.getCropCode());
            rows.add(famCrops.getCropName());
            rows.add(df.format(Double.valueOf(famCrops.getTotalArea())));
            rows.add(df.format(Double.valueOf(famCrops.getTotalProduction())));
            jsonObject.put("id", famCrops.getCropCode());
            jsonObject.put("cell", rows);
        }
        return jsonObject;

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
     * @see com.sourcetrace.esesw.view.BaseReportAction#getId()
     */
    public String getId() {

        return id;
    }

    /**
     * @see com.sourcetrace.esesw.view.BaseReportAction#setId(java.lang.String)
     */
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Gets the selected village.
     * @return the selected village
     */
    public String getSelectedVillage() {

        return selectedVillage;
    }

    /**
     * Sets the selected village.
     * @param selectedVillage the new selected village
     */
    public void setSelectedVillage(String selectedVillage) {

        this.selectedVillage = selectedVillage;
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
     * Gets the village list.
     * @return the village list
     */
    public Map<String, String> getVillageList() {

        Map<String, String> villageListMap = new LinkedHashMap<String, String>();
        List<Village> villageList = locationService.listVillage();
        for (Village obj : villageList) {
            villageListMap.put(obj.getCode(), obj.getName() + " - " + obj.getCode());
        }
        return villageListMap;

    }

    /**
     * Gets the fields.
     * @return the fields
     */
    public List<String> getFields() {

        return fields;
    }

    /**
     * Sets the fields.
     * @param fields the new fields
     */
    public void setFields(List<String> fields) {

        this.fields = fields;
    }

}
