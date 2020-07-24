/*
 * ProductServiceValidator.java
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
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ICategoryDAO;
import com.sourcetrace.eses.dao.IProductDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.eses.util.profile.Product;

/**
 * The Class ProductServiceValidator.
 */
public class ProductServiceValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(ProductServiceValidator.class);
    private static final String SELECT = "-1";
    private IProductDAO productDAO;
    private ICategoryDAO categoryDAO;
    private static final String CATEGORY_NOT_FOUND = "subCategoryNotFound";
    private static final String SCATEGORY_NOT_FOUND_PROPERTY = "subCategory.notfound";

    /**
     * Validate.
     * @param object the object
     * @return the map< string, string>
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator productValidator = new ClassValidator(Product.class);
        Product aProduct = (Product) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aProduct.toString());
        }

        InvalidValue[] values = null;

        values = productValidator.getInvalidValues(aProduct, "code");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if (values == null || values.length == 0) {
            Product eProduct = productDAO.findByProductCode(aProduct.getCode());
            if (eProduct != null && aProduct.getId() != eProduct.getId()) {
                errorCodes.put("code", "unique.ProductCode");
            }
        }

        values = productValidator.getInvalidValues(aProduct, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if (values == null || values.length == 0) {
            Product eProduct = productDAO.findByProductName(aProduct.getName());
            if (eProduct != null && aProduct.getId() != eProduct.getId()) {
                errorCodes.put("name", "unique.ProductName");
            }
        }

        if (StringUtil.isEmpty(aProduct.getPrice())) {
            try {
                if (getPriceDoubleValue(aProduct) <= 0.0) {
                    errorCodes.put("price", "empty.price");
                }
            } catch (Exception e) {
                errorCodes.put("price", "invalid.price");
            }
        }
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

       /* if (!StringUtil.isEmpty(aProduct.getCode())) {
            if (!ValidationUtil.isPatternMaches(aProduct.getCode(), ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.code", "pattern.code");
            }
        } 
        else {
            errorCodes.put("empty.code", "empty.code");
        }*/

        if (!StringUtil.isEmpty(aProduct.getName())) {
            if (!ValidationUtil.isPatternMaches(aProduct.getName(), ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.name", "pattern.name");
            }
        }
        else {
            errorCodes.put("empty.name", "empty.name");
        }
        
        /*if(!StringUtil.isEmpty(aProduct.getUnit())){   //NOTE: THIS BLOCK COMMENTED TO FIX BUG 720.
            if (!ValidationUtil.isPatternMaches(aProduct.getUnit(), ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.unit", "pattern.unit");
            }            
        }
        else{
            errorCodes.put("empty.unit", "empty.unit");
        }*/
        
        if(!ObjectUtil.isEmpty(aProduct.getType())){
          if(aProduct.getType().getCode().equals("-1")){
              errorCodes.put("pattern.unit", "pattern.unit");
          }
        }

        return errorCodes;
    }

    public double getPriceDoubleValue(Product aProduct) {

        double price = 0.0;
        String priceString = "";
        if (!StringUtil.isEmpty(aProduct.getPricePrefix()))
            priceString = aProduct.getPricePrefix();
        if (!StringUtil.isEmpty(aProduct.getPriceSuffix()))
            priceString = priceString + "." + aProduct.getPriceSuffix();
        if (!StringUtil.isEmpty(priceString))
            price = Double.valueOf(priceString);

        return price;
    }

    /**
     * Sets the product dao.
     * @param productDAO the new product dao
     */
    public void setProductDAO(IProductDAO productDAO) {

        this.productDAO = productDAO;
    }

    /**
     * Gets the product dao.
     * @return the product dao
     */
    public IProductDAO getProductDAO() {

        return productDAO;
    }

    /**
     * Sets the category dao.
     * @param categoryDAO the new category dao
     */
    public void setCategoryDAO(ICategoryDAO categoryDAO) {

        this.categoryDAO = categoryDAO;
    }

    /**
     * Gets the category dao.
     * @return the category dao
     */
    public ICategoryDAO getCategoryDAO() {

        return categoryDAO;
    }

}
