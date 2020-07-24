/*
 * FarmerTrainingSelectionValidator.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general.validator;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.txn.training.TrainingTopic;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ITrainingDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class FarmerTrainingSelectionValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(FarmerTrainingSelectionValidator.class
            .getName());
    private static final String SELECT = "-1";

    private ITrainingDAO trainingDAO;

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator<FarmerTraining> farmerTrainingValidator = new ClassValidator<FarmerTraining>(
                FarmerTraining.class);
        FarmerTraining aFarmerTraining = (FarmerTraining) object;

        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aFarmerTraining.toString());
        }

        InvalidValue[] values = null;
        // code
        values = farmerTrainingValidator.getInvalidValues(aFarmerTraining, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        if (values == null || values.length == 0) {
        	
            FarmerTraining eFarmerTraining = trainingDAO.findFarmerTrainingByCode(aFarmerTraining
                    .getCode());
            if(StringUtil.isEmpty(aFarmerTraining.getCode())){
              	 errorCodes.put("code", "empty.trainingcode");
              }
            else if (eFarmerTraining != null && aFarmerTraining.getId() != eFarmerTraining.getId()) {
                errorCodes.put("code", "unique.code");
            }
        }

        // training topic
        /*if (SELECT.equals(aFarmerTraining.getTrainingTopic().getName())) {
            errorCodes.put("emptyTrainingTopic", "empty.trainingTopic");
        } */
        
        if(StringUtil.isEmpty(aFarmerTraining.getTrainingTopic())){
        	errorCodes.put("emptyTrainingTopic", "empty.trainingTopic");
        }
        else {
            TrainingTopic aTrainingTopic = trainingDAO.findTrainingTopicByName(aFarmerTraining
                    .getTrainingTopic().getName());
            if (ObjectUtil.isEmpty(aTrainingTopic)) {
                errorCodes.put("invalidTrainingTopic", "invalid.trainingTopic");
            }
        }
        // training activity
        if (StringUtil.isEmpty(aFarmerTraining.getSelectedTopicId())
                || aFarmerTraining.getSelectedTopicId().length() <= 0) {
            errorCodes.put("emptyTrainingActivity", "empty.trainingActivity");
        } else {
            Set<Long> ids = buildTopicSet(aFarmerTraining.getSelectedTopicId());
            if (ids.size() <= 0) {
                errorCodes.put("emptyTrainingActivity", "empty.trainingActivity");
            }
        }
        // target group
      /*  if (StringUtil.isEmpty(aFarmerTraining.getSelectedTargetId())
                || aFarmerTraining.getSelectedTargetId().length() <= 0) {
            errorCodes.put("emptyTargetGroup", "empty.targetGroup");
        }*/
        // training method
        if (StringUtil.isEmpty(aFarmerTraining.getSelectedMethodId())
                || aFarmerTraining.getSelectedMethodId().length() <= 0) {
            errorCodes.put("emptyTrainingMethod", "empty.trainingMethod");
        }
        
        if (StringUtil.isEmpty(aFarmerTraining.getSelectedTrainingMaterialId())
                || aFarmerTraining.getSelectedTrainingMaterialId().length() <= 0) {
            errorCodes.put("emptyTrainingMaterial", "empty.trainingMaterial");
        }
        
        if (StringUtil.isEmpty(aFarmerTraining.getSelectedObservationsId())
                || aFarmerTraining.getSelectedObservationsId().length() <= 0) {
            errorCodes.put("emptyTrainingObervation", "empty.trainingObservations");
        }

        return errorCodes;
    }

    public Set<Long> buildTopicSet(String selectedTopic) {

        Set<Long> topics = new HashSet<Long>();
        String[] topicIds = selectedTopic.split("\\,");
        for (String topicId : topicIds) {
            if (StringUtil.isLong(topicId.trim())) {
                topics.add(Long.parseLong(topicId.trim()));
            }
        }
        return topics;
    }

    /**
     * Sets the training dao.
     * @param trainingDAO the trainingDAO to set
     */
    public void setTrainingDAO(ITrainingDAO trainingDAO) {

        this.trainingDAO = trainingDAO;
    }

    /**
     * Gets the training dao.
     * @return the trainingDAO
     */
    public ITrainingDAO getTrainingDAO() {

        return trainingDAO;
    }

}
