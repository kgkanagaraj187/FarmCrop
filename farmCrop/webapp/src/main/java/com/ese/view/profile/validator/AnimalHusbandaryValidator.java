/*
 * AnimalHusbandaryValidator.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
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
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;

public class AnimalHusbandaryValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(AnimalHusbandaryValidator.class);
    public static final int SELECT = -1;

    private IFarmerDAO farmerDAO;

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */

    public Map<String, String> validate(Object object) {

        ClassValidator animalHusbandaryValidator = new ClassValidator(AnimalHusbandary.class);
        AnimalHusbandary aAnimalHusbandary = (AnimalHusbandary) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();

        InvalidValue[] values = null;


        /*
         * values = animalHusbandaryValidator.getInvalidValues(aAnimalHusbandary, "animalCount");
         * for (InvalidValue value : values) { errorCodes.put(value.getPropertyName(),
         * value.getMessage()); }
         */
        /*
         * if (!StringUtil.isEmpty(aAnimalHusbandary.getAnimalCount())) { values =
         * animalHusbandaryValidator.getInvalidValues(aAnimalHusbandary, "animalCount"); for
         * (InvalidValue value : values) { errorCodes.put(value.getPropertyName(),
         * value.getMessage()); } }
         */

        if (!StringUtil.isEmpty(aAnimalHusbandary.getAnimalCount())
                && (aAnimalHusbandary.getAnimalCount().trim().length() > 0)) {
              	 if (!ValidationUtil.isPatternMaches(aAnimalHusbandary.getAnimalCount(),ValidationUtil.NUMBER_PATTERN)) {
              	 errorCodes.put("pattern.farmName", "pattern.farmName");                    	                     
              }
           
        }else{
          	errorCodes.put("empty.itemCount", "empty.itemCount");
          }        

        if (aAnimalHusbandary.getFeedingUsed() == SELECT) {
            errorCodes.put("feedingUsed", "empty.feedingUsed");
        }
        /*
         * if (aAnimalHusbandary.getAnimalHousing() == SELECT) { errorCodes.put("animalHousing",
         * "empty.animalHousing"); }
         */

        return errorCodes;
    }

    /**
     * Sets the farmer dao.
     * @param farmerDAO the new farmer dao
     */
    public void setFarmerDAO(IFarmerDAO farmerDAO) {

        this.farmerDAO = farmerDAO;
    }

    /**
     * Gets the farmer dao.
     * @return the farmer dao
     */
    public IFarmerDAO getFarmerDAO() {

        return farmerDAO;
    }

}
