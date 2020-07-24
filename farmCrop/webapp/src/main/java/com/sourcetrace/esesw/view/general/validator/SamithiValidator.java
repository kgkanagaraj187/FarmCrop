/*
 * SamithiValidator.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;

public class SamithiValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(SamithiValidator.class);

    private ILocationDAO locationDAO;
    @Autowired
	private IPreferencesService preferncesService;

    /**
     * Sets the location dao.
     * @param locationDAO the new location dao
     */
    public void setLocationDAO(ILocationDAO locationDAO) {

        this.locationDAO = locationDAO;
    }

    /**
     * Validate.
     * @param object the object
     * @return the map< string, string>
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator samithiValidator = new ClassValidator(Warehouse.class);

        Warehouse aSamithi = (Warehouse) object;
        HttpServletRequest httpRequest=ReflectUtil.getCurrentHttpRequest();
        String tenantId = (String) httpRequest.getSession().getAttribute(ISecurityFilter.TENANT_ID);
        tenantId = StringUtil.isEmpty(tenantId) ? ISecurityFilter.DEFAULT_TENANT_ID : tenantId;
        
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aSamithi.toString());
        }
        InvalidValue[] values = null;

        /*
         * values = samithiValidator.getInvalidValues(aSamithi, "code"); for (InvalidValue value :
         * values) { errorCodes.put(value.getPropertyName(), value.getMessage()); } if (values ==
         * null || values.length == 0) { Warehouse eSamithi =
         * locationDAO.findWarehouseByCode(aSamithi.getCode()); if (eSamithi != null &&
         * aSamithi.getId() != eSamithi.getId()) { errorCodes.put("code", "unique.samithiCode"); } }
         */

        values = samithiValidator.getInvalidValues(aSamithi, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        
        
        if(!StringUtil.isEmpty(aSamithi.getName())){
        	Warehouse eSamithi = locationDAO.findWarehouseByNameAndType(aSamithi.getName(),Warehouse.WarehouseTypes.SAMITHI.ordinal());
        	if(eSamithi!=null && aSamithi.getId() != eSamithi.getId()){
        		errorCodes.put("name", "unique.samithiName");
        	}
        }else{
        	if(StringUtil.isEmpty(aSamithi.getName())){
            	errorCodes.put("name","empty.name");
            }
        }
        
        
        
        
        //code hidden as req changed to allow special characters 

       /* if (!StringUtil.isEmpty(aSamithi.getName())) {
            if (!ValidationUtil.isPatternMaches(aSamithi.getName(),
                    ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.name","pattern.name");

            } else {
               
            	Warehouse eSamithi = locationDAO.findWarehouseByNameAndType(aSamithi.getName(),Warehouse.WarehouseTypes.SAMITHI.ordinal());
                if (eSamithi != null && aSamithi.getId() != eSamithi.getId()) {
                    errorCodes.put("name", "unique.samithiName");
                }
            }
        }else{
            errorCodes.put("name", "empty.name");
        }
*/
        /*
         * if (!ObjectUtil.isEmpty(aSamithi.getRefCooperative())) { if
         * (StringUtil.isEmpty(aSamithi.getRefCooperative().getCode())) {
         * errorCodes.put("emptyCooperative", "empty.cooperative"); } }
         */
        String result="";
        ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			result = preferences.getPreferences().get(ESESystem.IS_KPF_BASED);
		}
		String isKpfBased=!StringUtil.isEmpty(result) ? result : ESESystem.IS_KPF_BASED;
       // if(tenantId.equalsIgnoreCase("kpf")||tenantId.equalsIgnoreCase("gar") || tenantId.equalsIgnoreCase("iffco")){
		if(isKpfBased.equals("1") && !tenantId.equalsIgnoreCase("gsma") && !tenantId.equalsIgnoreCase("ecoagri")){
       if (StringUtil.isEmpty(aSamithi.getGroupType())) {
            errorCodes.put("emptyMasterType", "empty.groupType");
        }
        }
        
        if(tenantId.equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
        if(!StringUtil.isEmpty(aSamithi.getPresidentName())){
        	if(!ValidationUtil.isPatternMaches(aSamithi.getPresidentName(), ValidationUtil.ALPHANUMERIC_PATTERN)){
        		errorCodes.put("pattern.name", "pattern.name");
        	}
        }else{
    		errorCodes.put("empty.name", "empty.presidentName");
    	}
        
        if(!StringUtil.isEmpty(aSamithi.getSecretaryName())){
        	if(!ValidationUtil.isPatternMaches(aSamithi.getSecretaryName(), ValidationUtil.ALPHANUMERIC_PATTERN)){
        		errorCodes.put("pattern.name", "pattern.name");
        	}
        }else{
    		errorCodes.put("empty.name", "empty.secretaryName");
    	}
        
        if(!StringUtil.isEmpty(aSamithi.getTreasurer())){
        	if(!ValidationUtil.isPatternMaches(aSamithi.getTreasurer(), ValidationUtil.ALPHANUMERIC_PATTERN)){
        		errorCodes.put("pattern.name", "pattern.name");
        	}
        }else{
    		errorCodes.put("empty.name", "empty.treasurer");
    	}
        }
        return errorCodes;
    }

}
