/*
 * HarvestSeasonValidator.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.entity.util.ESESystem;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;

// TODO: Auto-generated Javadoc
/**
 * @author admin
 */
public class HarvestSeasonValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(HarvestSeasonValidator.class);

    private IFarmerDAO farmerDAO;

    /**
     * Gets the farmer dao.
     * @return the farmer dao
     */
    public IFarmerDAO getFarmerDAO() {

        return farmerDAO;
    }

    /**
     * Sets the farmer dao.
     * @param farmerDAO the new farmer dao
     */
    public void setFarmerDAO(IFarmerDAO farmerDAO) {

        this.farmerDAO = farmerDAO;
    }

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> validate(Object object) {

        ClassValidator harvestSeasonValidator = new ClassValidator(HarvestSeason.class);
        HarvestSeason aHarvestSeason = (HarvestSeason) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        HttpServletRequest httpRequest=ReflectUtil.getCurrentHttpRequest();
        String tenantId = (String) httpRequest.getSession().getAttribute(ISecurityFilter.TENANT_ID);
        tenantId = StringUtil.isEmpty(tenantId) ? ISecurityFilter.DEFAULT_TENANT_ID : tenantId;
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aHarvestSeason.toString());
        }
        InvalidValue[] values = null;

       /* values = harvestSeasonValidator.getInvalidValues(aHarvestSeason, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if (values == null || values.length == 0) {
            HarvestSeason eHarvestSeason = farmerDAO.findHarvestSeasonByCode(aHarvestSeason
                    .getCode());
            if (eHarvestSeason != null && aHarvestSeason.getId() != eHarvestSeason.getId()) {
                errorCodes.put("code", "unique.HarvestSeasonCode");
            }
        }
*/
        values = harvestSeasonValidator.getInvalidValues(aHarvestSeason, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if (values == null || values.length == 0) {
            HarvestSeason eHarvestSeason = farmerDAO.findHarvestSeasonByName(aHarvestSeason
                    .getName());
            if (eHarvestSeason != null && aHarvestSeason.getId() != eHarvestSeason.getId()) {
                errorCodes.put("name", "unique.HarvestSeasonName");
            }
        }
        values = harvestSeasonValidator.getInvalidValues(aHarvestSeason, "fromPeriod");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        values = harvestSeasonValidator.getInvalidValues(aHarvestSeason, "toPeriod");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        if(!ObjectUtil.isEmpty(aHarvestSeason)){
        	if(StringUtil.isEmpty(aHarvestSeason.getFromPeriod())){
        		errorCodes.put("empty.fromPeriod", "empty.fromPeriod");
        	}
        	if(StringUtil.isEmpty(aHarvestSeason.getToPeriod())){
        		errorCodes.put("empty.toPeriod", "empty.toPeriod");
        	}
        	if(StringUtil.isEmpty(aHarvestSeason.getName())){
        		errorCodes.put("empty.name", "empty.name");
        	}        	
        }
        
        /*if(aHarvestSeason.getCurrentSeason()==1){
        	boolean currentSeasonexits=farmerDAO.findCurrentSeason();
        	if(currentSeasonexits==false){
        		errorCodes.put("current.season", "current.season");
        	}
        }*/
        return errorCodes;
    }

}
