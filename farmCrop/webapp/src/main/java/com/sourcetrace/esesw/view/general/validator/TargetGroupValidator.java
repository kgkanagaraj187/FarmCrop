/*
 * TargetGroupValidator.java
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

import com.ese.entity.txn.training.TargetGroup;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ITrainingDAO;
import com.sourcetrace.eses.util.StringUtil;

public class TargetGroupValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(TargetGroupValidator.class);

    private ITrainingDAO trainingDAO;

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator targetGroupValidator = new ClassValidator(TargetGroup.class);
        TargetGroup aTargetGroup = (TargetGroup) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aTargetGroup.toString());
        }
        InvalidValue[] values = null;

        values = targetGroupValidator.getInvalidValues(aTargetGroup, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        /*if (values == null || values.length == 0) {
            TargetGroup eTargetGroup = trainingDAO.findTargetGroupByCode(aTargetGroup.getCode());
            if (eTargetGroup != null && aTargetGroup.getId() != eTargetGroup.getId()) {
                errorCodes.put("code", "unique.targetGroupCode");
            }
        }*/

        values = targetGroupValidator.getInvalidValues(aTargetGroup, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        if(StringUtil.isEmpty(aTargetGroup.getName())){
        	errorCodes.put("name","empty.targetGroupName");
        }

        if (values == null || values.length == 0) {
            TargetGroup eTargetGroup = trainingDAO.findTargetGroupByName(aTargetGroup.getName());
            if (eTargetGroup != null && aTargetGroup.getId() != eTargetGroup.getId()) {
                errorCodes.put("name", "unique.targetGroupName");
            }
        }

        return errorCodes;
    }

    /**
     * Gets the training dao.
     * @return the training dao
     */
    public ITrainingDAO getTrainingDAO() {

        return trainingDAO;
    }

    /**
     * Sets the training dao.
     * @param trainingDAO the new training dao
     */
    public void setTrainingDAO(ITrainingDAO trainingDAO) {

        this.trainingDAO = trainingDAO;
    }

}
