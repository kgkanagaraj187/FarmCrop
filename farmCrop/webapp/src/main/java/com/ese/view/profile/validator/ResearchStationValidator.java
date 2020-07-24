/*
 * CustomerValidator.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile.validator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IClientDAO;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.ResearchStation;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomerValidator.
 */
public class ResearchStationValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(ResearchStationValidator.class);
    private IFarmerDAO farmerDAO;
    private Pattern pattern;  
    private Matcher matcher;  
    /**
     * Validate.
     * @param object the object
     * @return the map< string, string>
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator researchStationValidator = new ClassValidator(ResearchStation.class);
        ResearchStation researchStation = (ResearchStation) object;
        String STRING_PATTERN = "/^[a-zA-Z]*$/";  
        
        
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();

        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + researchStation.toString());
        }
        InvalidValue[] values = null;

        values = researchStationValidator.getInvalidValues(researchStation, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        values = researchStationValidator.getInvalidValues(researchStation, "researchStationAddress");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
       /* values = researchStationValidator.getInvalidValues(researchStation, "landLine");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }*/
        values = researchStationValidator.getInvalidValues(researchStation, "pointPerson");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        values = researchStationValidator.getInvalidValues(researchStation, "division");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        values = researchStationValidator.getInvalidValues(researchStation, "designation");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        if (!StringUtil.isEmpty( researchStation.getName())) {
            if (!ValidationUtil.isPatternMaches( researchStation.getName(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.researchStationName", "pattern.researchStationName");
            }
        }else{
            errorCodes.put("empty.researchStationName","empty.researchStationName");
        }
        
      
        
        if (!StringUtil.isEmpty( researchStation.getPointPerson())) {
            if (!ValidationUtil.isPatternMaches( researchStation.getPointPerson(),ValidationUtil.NAME_PATTERN)) {
                errorCodes.put("pointPerson.incorrect", "pointPerson.incorrect");
            }
        }
        
       /* if (!StringUtil.isEmpty( researchStation.getDivision())) {
            if (!ValidationUtil.isPatternMaches( researchStation.getDivision(),ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.mobileNumber", "pattern.mobileNumber");
            }
        }
        
        if (!StringUtil.isEmpty( researchStation.getDesignation())) {
            if (!ValidationUtil.isPatternMaches( researchStation.getDesignation(),ValidationUtil.EMAIL_PATTERN)) {
                errorCodes.put("pattern.email", "pattern.email");
            }
        }*/
        
        if (!StringUtil.isEmpty( researchStation.getResearchStationAddress())) {
            if (!ValidationUtil.isPatternMaches( researchStation.getResearchStationAddress(),ValidationUtil.SPECIAL_CHARACTER)) {
                errorCodes.put("pattern.address", "pattern.address");
            }
        }
        
        
        
        return errorCodes;
    }
    
    

    


    public IFarmerDAO getFarmerDAO() {

        return farmerDAO;
    }



    public void setFarmerDAO(IFarmerDAO farmerDAO) {

        this.farmerDAO = farmerDAO;
    }

}
