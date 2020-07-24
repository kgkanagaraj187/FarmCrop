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
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.Customer;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomerValidator.
 */
public class CustomerValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(CustomerValidator.class);
    private IClientDAO clientDAO;
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

        ClassValidator customerValidator = new ClassValidator(Customer.class);
        Customer customer = (Customer) object;
        String STRING_PATTERN = "/^[a-zA-Z]*$/";  
        
        
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();

        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + customer.toString());
        }
        InvalidValue[] values = null;

        values = customerValidator.getInvalidValues(customer, "customerName");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        values = customerValidator.getInvalidValues(customer, "customerAddress");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        values = customerValidator.getInvalidValues(customer, "landLine");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        values = customerValidator.getInvalidValues(customer, "personName");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        values = customerValidator.getInvalidValues(customer, "mobileNumber");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        values = customerValidator.getInvalidValues(customer, "email");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        
        if (!StringUtil.isEmpty(customer.getCustomerName())) {
            if (!ValidationUtil.isPatternMaches(customer.getCustomerName(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.customerName", "pattern.customerName");
            }
        }else{
            errorCodes.put("empty.customerName","empty.customerName");
        }
        
        if (!StringUtil.isEmpty(customer.getLandLine())) {
            if (!ValidationUtil.isPatternMaches(customer.getLandLine(),ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.landLine", "pattern.landLine");
            }
        }
        
        if (!StringUtil.isEmpty(customer.getPersonName())) {
            if (!ValidationUtil.isPatternMaches(customer.getPersonName(),ValidationUtil.NAME_PATTERN)) {
                errorCodes.put("personName.incorrect", "personName.incorrect");
            }
        }
        
        if (!StringUtil.isEmpty(customer.getMobileNumber())) {
            if (!ValidationUtil.isPatternMaches(customer.getMobileNumber(),ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.mobileNumber", "pattern.mobileNumber");
            }
        }
        
        if (!StringUtil.isEmpty(customer.getEmail())) {
            if (!ValidationUtil.isPatternMaches(customer.getEmail(),ValidationUtil.EMAIL_PATTERN)) {
                errorCodes.put("pattern.email", "pattern.email");
            }
        }
        
        if (!StringUtil.isEmpty(customer.getCustomerAddress())) {
            if (!ValidationUtil.isPatternMaches(customer.getCustomerAddress(),ValidationUtil.SPECIAL_CHARACTER)) {
                errorCodes.put("pattern.address", "pattern.address");
            }
        }
        
        
        
        return errorCodes;
    }
    
    

    /**
     * Gets the client dao.
     * @return the client dao
     */
    public IClientDAO getClientDAO() {

        return clientDAO;
    }

    /**
     * Sets the client dao.
     * @param clientDAO the new client dao
     */
    public void setClientDAO(IClientDAO clientDAO) {

        this.clientDAO = clientDAO;
    }

}
