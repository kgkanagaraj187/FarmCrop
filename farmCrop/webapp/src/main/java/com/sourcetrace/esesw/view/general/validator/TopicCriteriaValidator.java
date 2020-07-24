package com.sourcetrace.esesw.view.general.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.entity.txn.training.Topic;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ITrainingDAO;
import com.sourcetrace.eses.util.StringUtil;

public class TopicCriteriaValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(FarmerTrainingSelectionValidator.class
            .getName());
    private static final String SELECT = "-1";

    private ITrainingDAO trainingDAO;
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        Topic aTopic = (Topic) object;
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aTopic.toString());
        }

        ClassValidator<Topic> topicValidator = new ClassValidator<Topic>(Topic.class);
        
        InvalidValue[] values = null;
        
        values = topicValidator.getInvalidValues(aTopic, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
       /* if (values == null || values.length == 0) {
            Topic eTopic = trainingDAO.findTopicByCode(aTopic.getCode());
            if (eTopic != null && aTopic.getId() != eTopic.getId()) {
                errorCodes.put("code", "unique.code");
            }
        }*/
        
        values = topicValidator.getInvalidValues(aTopic, "principle");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        values = topicValidator.getInvalidValues(aTopic, "des");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        values = topicValidator.getInvalidValues(aTopic, "topicCategory");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        if(StringUtil.isEmpty(aTopic.getPrinciple())){
        	errorCodes.put("principle","empty.principle");
        }
        if(StringUtil.isEmpty(aTopic.getDes())){
        	errorCodes.put("des","empty.des");
        }
        if(StringUtil.isEmpty(aTopic.getTopicCategory())){
        	errorCodes.put("topicCategory","empty.topicCategory");
        }
        

        return errorCodes;
    }

    public void setTrainingDAO(ITrainingDAO trainingDAO) {

        this.trainingDAO = trainingDAO;
    }

}
