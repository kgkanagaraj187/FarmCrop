/*
 * FarmerFamilyValidator.java
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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.FarmerFamily;

public class FarmerFamilyValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(FarmerFamilyValidator.class);

    private IFarmerDAO farmerDAO;
    protected HttpServletRequest request;

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

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> validate(Object object) {

        ClassValidator farmerFamilyValidator = new ClassValidator(FarmerFamily.class);

        FarmerFamily farmerFamily = (FarmerFamily) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + farmerFamily.toString());
        }
        InvalidValue[] values = null;

        /*values = farmerFamilyValidator.getInvalidValues(farmerFamily, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
*/
      if (farmerFamily.getRelation() ==null || StringUtil.isEmpty(farmerFamily.getRelation())) {
            errorCodes.put("emptyrelation", "empty.relation");
        }
      	if(farmerFamily.getDisability()==1){
      		if(StringUtil.isEmpty(farmerFamily.getDisableDetail()) || farmerFamily.getDisableDetail()==null){
      			errorCodes.put("emptyDisableDetail","empty.disableDetail");
      		}
      	}
     
     
        if (StringUtil.isEmpty(farmerFamily.getGender())) {
            errorCodes.put("emptygender", "empty.gender");
       }
        if(farmerFamily.getAge() < 1 ){
            errorCodes.put("age", "invalid.age");
        }
        
        FarmerFamily existing = null;

        if (!(StringUtil.isEmpty(farmerFamily.getName()))
                && !(StringUtil.isEmpty(farmerFamily.getFarmer().getId()))) {

            existing = farmerDAO.findFarmerFamilyByName(farmerFamily.getName(), farmerFamily
                    .getFarmer().getId());
            if (existing != null && farmerFamily.getId() == 0L) {
                errorCodes.put("unique", "unique.create");
            } else if (existing != null && existing.getId() != (farmerFamily.getId())) {
                errorCodes.put("unique", "unique.update");
            }
        }else if((StringUtil.isEmpty(farmerFamily.getName()))){
        	 errorCodes.put("name", "empty.name");
        }

        return errorCodes;
    }
}
