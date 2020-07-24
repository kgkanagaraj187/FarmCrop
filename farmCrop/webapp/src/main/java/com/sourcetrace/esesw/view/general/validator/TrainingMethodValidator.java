/*
 * TrainingMethodValidator.java
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

import com.ese.entity.txn.training.TrainingMethod;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ITrainingDAO;
import com.sourcetrace.eses.util.StringUtil;

public class TrainingMethodValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(TrainingMethodValidator.class);

    private ITrainingDAO trainingDAO;

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator traingMethodValidator = new ClassValidator(TrainingMethod.class);
        TrainingMethod aTrainingMethod = (TrainingMethod) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aTrainingMethod.toString());
        }
        InvalidValue[] values = null;

        values = traingMethodValidator.getInvalidValues(aTrainingMethod, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

      /*  if (values == null || values.length == 0) {
            TrainingMethod eTrainingMethod = trainingDAO.findTrainingMethodByCode(aTrainingMethod
                    .getCode());
            if (eTrainingMethod != null && aTrainingMethod.getId() != eTrainingMethod.getId()) {
                errorCodes.put("code", "unique.trainingMethodCode");
            }
        }*/

        values = traingMethodValidator.getInvalidValues(aTrainingMethod, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        if(StringUtil.isEmpty(aTrainingMethod.getName())){
        	errorCodes.put("name","empty.trainingMethodName");
        }

        if (values == null || values.length == 0) {
            TrainingMethod eTrainingMethod = trainingDAO.findTrainingMethodByName(aTrainingMethod
                    .getName());
            if (eTrainingMethod != null && aTrainingMethod.getId() != eTrainingMethod.getId()) {
                errorCodes.put("name", "unique.trainingMethodName");
            }
        }

        return errorCodes;
    }

    /**
     * Sets the training dao.
     * @param trainingDAO the new training dao
     */
    public void setTrainingDAO(ITrainingDAO trainingDAO) {

        this.trainingDAO = trainingDAO;
    }

    /**
     * Gets the training dao.
     * @return the training dao
     */
    public ITrainingDAO getTrainingDAO() {

        return trainingDAO;
    }

}
