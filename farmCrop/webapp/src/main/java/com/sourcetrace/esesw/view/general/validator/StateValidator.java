/**
 * StateValidator.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
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
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.State;

public class StateValidator implements IValidator {

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(StateValidator.class);
   
    /** The location dao. */
    private ILocationDAO locationDAO;

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

        ClassValidator stateValidator = new ClassValidator(State.class);
        State aState = (State) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aState.toString());
        }
        InvalidValue[] values = null;
        
        if (ObjectUtil.isEmpty(aState.getCountry())) {
            errorCodes.put("country", "empty.country");
        }

        /*values = stateValidator.getInvalidValues(aState, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        

        if (values == null || values.length == 0) {
            State eState = locationDAO.findStateByCode(aState.getCode());
            if (eState != null && aState.getId() != eState.getId()) {
                errorCodes.put("code", "unique.stateCode");
            }

        }
*/
        values = stateValidator.getInvalidValues(aState, "name");

        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        if(!StringUtil.isEmpty(aState.getName())){
        	if(!ValidationUtil.isPatternMaches(aState.getName(), ValidationUtil.NAME_PATTERN)){
        		errorCodes.put("name","pattern.name");
        	}
        }else{
        	errorCodes.put("name","empty.name");
        }
      

        if (values == null || values.length == 0) {
            State eState = locationDAO.findStateByName(aState.getName());
            if (eState != null && aState.getId() != eState.getId()) {
                errorCodes.put("name", "unique.stateName");
            }

        }

        return errorCodes;
    }

}
