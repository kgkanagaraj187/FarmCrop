/*
 * ProcurementProductValidator.java
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

import javax.servlet.http.HttpSession;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;

public class ProcurementProductValidator implements IValidator {

    private IProductDistributionDAO productDistributionDAO;

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator productValidator = new ClassValidator(ProcurementProduct.class);
        ProcurementProduct aProcurementProduct = (ProcurementProduct) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();

        HttpSession httpSession=ReflectUtil.getCurrentHttpSession();
    	String branchId_F=(String)httpSession.getAttribute(ISecurityFilter.CURRENT_BRANCH);
    	
        InvalidValue[] values = null;

        values = productValidator.getInvalidValues(aProcurementProduct, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if (values == null || values.length == 0) {
            ProcurementProduct eProcurementProduct = productDistributionDAO
                    .findProcurementProductByCode(aProcurementProduct.getCode());
            if (eProcurementProduct != null
                    && aProcurementProduct.getId() != eProcurementProduct.getId()) {
                errorCodes.put("code", "unique.ProcurementProductCode");
            }
        }

        values = productValidator.getInvalidValues(aProcurementProduct, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if (values == null || values.length == 0) {
            ProcurementProduct eProcurementProduct = productDistributionDAO
                    .findProcurementProductByNameAndBranch(aProcurementProduct.getName(),branchId_F);
            if (eProcurementProduct != null
                    && aProcurementProduct.getId() != eProcurementProduct.getId()) {
                errorCodes.put("name", "unique.ProcurementProductName");
            }
        }
        
        /*if (!StringUtil.isEmpty(aProcurementProduct.getCode())) {
            if (!ValidationUtil.isPatternMaches(aProcurementProduct.getCode(), ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.code", "pattern.code");
            }
        } 
        else {
            errorCodes.put("empty.code", "empty.code");
        }*/
        
        if (!StringUtil.isEmpty(aProcurementProduct.getName())) {
            if (!ValidationUtil.isPatternMaches(aProcurementProduct.getName(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.name", "pattern.name");
            }
        }else{
            errorCodes.put("empty.name","empty.name");
        }
        
        /*if(StringUtil.isEmpty(aProcurementProduct.getNoOfDaysToGrow()))
        	errorCodes.put("empty.noOfDaysToGrow", "empty.noOfDaysToGrow");*/
        
        return errorCodes;
    }

    /**
     * Sets the product distribution dao.
     * @param productDistributionDAO the new product distribution dao
     */
    public void setProductDistributionDAO(IProductDistributionDAO productDistributionDAO) {

        this.productDistributionDAO = productDistributionDAO;
    }

    /**
     * Gets the product distribution dao.
     * @return the product distribution dao
     */
    public IProductDistributionDAO getProductDistributionDAO() {

        return productDistributionDAO;
    }

}
