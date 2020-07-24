/*
 * TrainingCriteriaCategoryValidator.java
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

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.entity.txn.training.TopicCategory;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ITrainingDAO;
import com.sourcetrace.eses.util.StringUtil;

public class TrainingCriteriaCategoryValidator implements IValidator {

    private ITrainingDAO trainingDAO;

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator topicCategoryValidator = new ClassValidator(TopicCategory.class);
        TopicCategory aTopicCategory = (TopicCategory) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();

        InvalidValue[] values = null;

        values = topicCategoryValidator.getInvalidValues(aTopicCategory, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

       /* if (values == null || values.length == 0) {
            TopicCategory eTopicCategory = trainingDAO.findTopicCategoryByCode(aTopicCategory
                    .getCode());
            if (eTopicCategory != null && aTopicCategory.getId() != eTopicCategory.getId()) {
                errorCodes.put("code", "unique.CriteriaCategoryCode");
            }
        }*/

        values = topicCategoryValidator.getInvalidValues(aTopicCategory, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        if (values == null || values.length == 0) {
            TopicCategory eTopicCategory = trainingDAO.findTopicCategoryByName(aTopicCategory.getName()); 
            if (eTopicCategory != null && aTopicCategory.getId() != eTopicCategory.getId()) {
               errorCodes.put("name", "unique.CriteriaCategoryName");
            }
        }
        if(StringUtil.isEmpty(aTopicCategory.getName())){
        	errorCodes.put("name","empty.name");
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
