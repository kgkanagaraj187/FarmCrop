/*
 * FarmCropsMasterValidator.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IFarmCropsDAO;
import com.sourcetrace.esesw.entity.profile.FarmCropsMaster;

public class FarmCropsMasterValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(FarmCropsMasterValidator.class);
    private IFarmCropsDAO farmCropsDAO;

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    public Map<String, String> validate(Object object) {

        ClassValidator farmCropsMasterValidator = new ClassValidator(FarmCropsMaster.class);

        FarmCropsMaster aFarmCropsMaster = (FarmCropsMaster) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aFarmCropsMaster.toString());
        }
        InvalidValue[] values = null;

        values = farmCropsMasterValidator.getInvalidValues(aFarmCropsMaster, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if (values == null || values.length == 0) {
            FarmCropsMaster eFarmCropsMaster = farmCropsDAO.findFarmCropsMasterByCode(aFarmCropsMaster
                    .getCode());
            if (eFarmCropsMaster != null && aFarmCropsMaster.getId() != eFarmCropsMaster.getId()) {
                errorCodes.put("code", "unique.code");
            }
        }

        values = farmCropsMasterValidator.getInvalidValues(aFarmCropsMaster, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        return errorCodes;
    }

    /**
     * Sets the farm crops dao.
     * 
     * @param farmCropsDAO the new farm crops dao
     */
    public void setFarmCropsDAO(IFarmCropsDAO farmCropsDAO) {

        this.farmCropsDAO = farmCropsDAO;
    }

    /**
     * Gets the farm crops dao.
     * 
     * @return the farm crops dao
     */
    public IFarmCropsDAO getFarmCropsDAO() {

        return farmCropsDAO;
    }

}
