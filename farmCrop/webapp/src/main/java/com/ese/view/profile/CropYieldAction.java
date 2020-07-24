/*
 * CropYieldAction.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.txn.agrocert.CropYieldDetail;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.view.SwitchValidatorAction;

@SuppressWarnings("serial")
public class CropYieldAction extends SwitchValidatorAction {

    private String id;
    private static final DateFormat CROP_YIELD_DATE_FORMAT = new SimpleDateFormat(
            DateUtil.TXN_TIME_FORMAT);
    private static final Logger LOGGER = Logger.getLogger(CropYieldAction.class);

    /**
     * @see com.sourcetrace.esesw.view.SwitchAction#data()
     */
    @SuppressWarnings("unchecked")
    public String data() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam();
        CropYield filter = new CropYield();
        Season season = new Season();
        Farm farm = new Farm();
        filter.setFarm(farm);
        if (!StringUtil.isEmpty(id)) {
            try {
                farm.setId(Long.valueOf(id));
            } catch (Exception e) {
                LOGGER.info("Exception while conversion of farmid " + id);
            }
        }
        filter.setSeason(season);
        if (!StringUtil.isEmpty(searchRecord.get("s.name"))) {
            filter.getSeason().setName(searchRecord.get("s.name").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("landHolding"))) {
            filter.setLandHolding(searchRecord.get("landHolding").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("latitude"))) {
            filter.setLatitude(searchRecord.get("latitude").trim());
        }
        if (!StringUtil.isEmpty(searchRecord.get("longitude"))) {
            filter.setLongitude(searchRecord.get("longitude").trim());
        }
        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());
        return sendJQGridJSONResponse(data);
    }

    /**
     * Sub grid detail.
     * @return the string
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public String subGridDetail() throws Exception {

        Map<String, String> searchRecord = getJQGridRequestParam();
        CropYieldDetail filter = new CropYieldDetail();
        filter.setCropYield(new CropYield());
        try {
            filter.getCropYield().setId(Long.valueOf(id));
        } catch (Exception e) {
            LOGGER.info("Exception while conversion of Crop Yield " + id);
        }
        Map data = reportService.listWithEntityFiltering(getDir(), getSort(), getStartIndex(),
                getResults(), filter, getPage());
        return sendJQGridJSONResponse(data);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JSONObject toJSON(Object record) {

        JSONObject jsonObject = new JSONObject();
        JSONArray rows = new JSONArray();
        if (record instanceof CropYield) {
            CropYield cropYield = (CropYield) record;
            rows.add(CROP_YIELD_DATE_FORMAT.format(cropYield.getCropYieldDate()));
            rows.add(ObjectUtil.isEmpty(cropYield.getSeason()) ? "" : cropYield.getSeason()
                    .getName());
            rows.add(cropYield.getLandHolding());
            rows.add(cropYield.getLatitude());
            rows.add(cropYield.getLongitude());
            jsonObject.put("id", cropYield.getId());
        } else {
            CropYieldDetail cropYieldDetail = (CropYieldDetail) record;
            /*rows.add(!ObjectUtil.isEmpty(cropYieldDetail.getFarmCropsMaster()) ? !StringUtil
                    .isEmpty(cropYieldDetail.getFarmCropsMaster().getCode()) ? cropYieldDetail
                    .getFarmCropsMaster().getCode() : "" : "");
            rows.add(!ObjectUtil.isEmpty(cropYieldDetail.getFarmCropsMaster()) ? !StringUtil
                    .isEmpty(cropYieldDetail.getFarmCropsMaster().getName()) ? cropYieldDetail
                    .getFarmCropsMaster().getName() : "" : "");
            rows.add(cropYieldDetail.getYield());*/
           
            rows.add(!ObjectUtil.isEmpty(cropYieldDetail.getProcurementProduct())?(!StringUtil.isEmpty(cropYieldDetail.getProcurementProduct().getCode())?cropYieldDetail.getProcurementProduct().getCode():""):"");
            rows.add(!ObjectUtil.isEmpty(cropYieldDetail.getProcurementProduct())?(!StringUtil.isEmpty(cropYieldDetail.getProcurementProduct().getName())?cropYieldDetail.getProcurementProduct().getName():""):"");
            rows.add(cropYieldDetail.getYield());
            
            jsonObject.put("id", cropYieldDetail.getCropYield().getId() + "_"
                    + cropYieldDetail.getId());
        }
        jsonObject.put("cell", rows);
        return jsonObject;
    }

    /**
     * @see com.sourcetrace.esesw.view.SwitchValidatorAction#getData()
     */
    @Override
    public Object getData() {

        return null;
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

}
