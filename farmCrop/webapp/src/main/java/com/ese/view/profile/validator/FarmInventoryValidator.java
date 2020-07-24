/*
 * FarmInventoryValidator.java
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

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.FarmInventory;

public class FarmInventoryValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(FarmInventoryValidator.class);
    public static final int SELECT = -1;

    private IFarmerDAO farmerDAO;

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator farmInventoryValidator = new ClassValidator(FarmInventory.class);
        FarmInventory aFarmInventory = (FarmInventory) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();

        InvalidValue[] values = null;

//        if (aFarmInventory.getInventoryItem() == SELECT) {
//            errorCodes.put("inventoryItem", "empty.inventoryItem");
//        }
//
//        if (aFarmInventory.getInventoryItem() == FarmInventory.INVENTORY_OTHER) {
//            
//            values = farmInventoryValidator.getInvalidValues(aFarmInventory, "otherInventoryItem");
//            for (InvalidValue value : values) {
//                errorCodes.put(value.getPropertyName(), value.getMessage());
//            }
//        }

 /*       values = farmInventoryValidator.getInvalidValues(aFarmInventory, "itemCount");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
 */       
        if (!StringUtil.isEmpty(aFarmInventory.getItemCount())) {
          	 if (!ValidationUtil.isPatternMaches(aFarmInventory.getItemCount(),ValidationUtil.NUMBER_PATTERN)) {
          	 errorCodes.put("pattern.farmName", "pattern.farmName");      
          	 }                    
          }else{
          	errorCodes.put("empty.itemCount", "empty.itemCount");
          }
       
      /* if (!StringUtil.isEmpty(aFarmInventory.getItemCount())) {
            values = farmInventoryValidator.getInvalidValues(aFarmInventory, "itemCount");
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }*/

        return errorCodes;
    }

    /**
     * Gets the farmer dao.
     * @return the farmer dao
     */
    public IFarmerDAO getFarmerDAO() {

        return farmerDAO;
    }

    /**
     * Sets the farmer dao.
     * @param farmerDAO the new farmer dao
     */
    public void setFarmerDAO(IFarmerDAO farmerDAO) {

        this.farmerDAO = farmerDAO;
    }

}
