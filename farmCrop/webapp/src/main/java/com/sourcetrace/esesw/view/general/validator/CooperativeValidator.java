/*
 * CoOperativeValidator.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.FarmCrops;

public class CooperativeValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(CooperativeValidator.class);

    private ILocationDAO locationDAO;

    /**
     * Sets the location dao.
     * @param locationDAO the new location dao
     */
    public void setLocationDAO(ILocationDAO locationDAO) {

        this.locationDAO = locationDAO;
    }

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator cooperativeValidator = new ClassValidator(Warehouse.class);
        Warehouse cooperativeObject = (Warehouse) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + cooperativeObject.toString());
        }
        InvalidValue[] values = null;

        values = cooperativeValidator.getInvalidValues(cooperativeObject, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        if (values == null || values.length == 0) {
            Warehouse cooperative = locationDAO.findWarehouseByName(cooperativeObject.getName());
            if (cooperative != null && cooperativeObject.getId() != cooperative.getId()) {
                errorCodes.put("name", "unique.cooperativeName");
            }
        }

        values = cooperativeValidator.getInvalidValues(cooperativeObject, "address");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        values = cooperativeValidator.getInvalidValues(cooperativeObject, "phoneNo");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        values = cooperativeValidator.getInvalidValues(cooperativeObject, "capacityInTonnes");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        if(!StringUtil.isEmpty(cooperativeObject.getName())){
            if (!ValidationUtil.isPatternMaches(cooperativeObject.getName(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.name","pattern.name");
            }
        }else{
            errorCodes.put("empty.name","empty.name");
        }
        
        if(!StringUtil.isEmpty(cooperativeObject.getLocation())){
            if (!ValidationUtil.isPatternMaches(cooperativeObject.getLocation(),ValidationUtil.NAME_PATTERN)) {
                errorCodes.put("pattern.location","pattern.location");
            }
        }
        
        if(!StringUtil.isEmpty(cooperativeObject.getWarehouseInCharge())){
            if (!ValidationUtil.isPatternMaches(cooperativeObject.getWarehouseInCharge(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.warehouseInCharge","pattern.warehouseInCharge");
            }
        }
        
        if(!StringUtil.isEmpty(cooperativeObject.getCapacityInTonnes())){
            if (!ValidationUtil.isPatternMaches(cooperativeObject.getCapacityInTonnes(),FarmCrops.THREE_DECIMAL_PATTERN)) {
                errorCodes.put("pattern.capacityInTonnes","pattern.capacityInTonnes");
            }
        }
        
        return errorCodes;
    }

}
