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
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;

public class DynamicMenuValidator implements IValidator {

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(DynamicMenuValidator.class);
   
    /** The farmer dao. */
    private IFarmerDAO farmerDAO;

   

    /**
     * Validate.
     * @param object the object
     * @return the map< string, string>
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator dynamicValidator = new ClassValidator(DynamicFeildMenuConfig.class);
        DynamicFeildMenuConfig aDynamicFeild = (DynamicFeildMenuConfig) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aDynamicFeild.toString());
        }
        InvalidValue[] values = null;
        
       if(!StringUtil.isEmpty(aDynamicFeild.getName())){
    	   if (!ValidationUtil.isPatternMaches(aDynamicFeild.getName(), ValidationUtil.ALPHANUMERIC_PATTERN)) {
				errorCodes.put("pattern.menuName", "pattern.menuName");
			}
       }else{
    	   errorCodes.put("empty.menuName", "empty.menuName");
       }
      
      
        /*   if (ObjectUtil.isEmpty(aDynamicFeild.getDynamicSectionConfigs()) || aDynamicFeild.getDynamicSectionConfigs() == null) {
               errorCodes.put("selectedSections", "empty.selectedSections");
           }*/

       
       
       

        return errorCodes;
    }



	public IFarmerDAO getFarmerDAO() {
		return farmerDAO;
	}



	public void setFarmerDAO(IFarmerDAO farmerDAO) {
		this.farmerDAO = farmerDAO;
	}

}
