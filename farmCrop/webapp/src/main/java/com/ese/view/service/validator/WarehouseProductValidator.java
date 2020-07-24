/*
 * WarehouseProductValidator.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.service.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

public class WarehouseProductValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(WarehouseProductValidator.class);

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    public Map<String, String> validate(Object object) {

        ClassValidator productValidator = new ClassValidator(WarehouseProductValidator.class);
        WarehouseProduct aWarehouseProduct = (WarehouseProduct) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aWarehouseProduct.toString());
        }

        if (ObjectUtil.isEmpty(aWarehouseProduct.getWarehouse())) {
            errorCodes.put("emptywarehouse", "empty.warehouse");
        }
        if (StringUtil.isEmpty(aWarehouseProduct.getProduct().getSubcategory().getCategory()
                .getName())) {
            errorCodes.put("emptyCategory", "empty.category");
        }
        if (StringUtil.isEmpty(aWarehouseProduct.getProduct().getSubcategory().getName())) {
            errorCodes.put("emptySubCategor", "empty.subCategory");
        }
        if (aWarehouseProduct.getProduct().getId() <= 0){
            errorCodes.put("emptyproduct", "empty.product");
        }
        if (aWarehouseProduct.getStock() > 500000) {
            errorCodes.put("emptystock", "max.stock");
        }

        return errorCodes;
    }

}
