/*
 * AbstractValidator.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.validator;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.sourcetrace.eses.util.ObjectUtil;

public abstract class AbstractValidator {

    private static ValidatorFactory factory = null;
    private static Validator validator = null;

    static {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Gets the validation errors.
     * @param object the object
     * @return the validation errors
     */
    public Map<String, String> getValidationErrors(Object object) {

        return validate(object);
    }

    @SuppressWarnings("rawtypes")
    protected Set<String> validateProperty(Object entity, String propertyName) {

        Set<String> errors = new LinkedHashSet<String>();
        Set<ConstraintViolation<Object>> constraintViolation = validator.validateProperty(entity,
                propertyName);

        Iterator<ConstraintViolation<Object>> violationIterator = constraintViolation.iterator();
        while (violationIterator.hasNext()) {
            ConstraintViolation cv = violationIterator.next();
            errors.add(cv.getMessage());
        }
        return errors;
    }

    protected void addErrors(Set<String> errorSet, Map<String, String> errors, String errorKey) {

        if (!ObjectUtil.isListEmpty(errorSet)) {
            for (String error : errorSet) {
                errors.put(errorKey, error);
            }
        }
    }

    protected abstract Map<String, String> validate(Object object);

}
