/*
 * CountryValidator.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.validator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.entity.Location;

@Component
public class LocationValidator implements IValidator {

	@Autowired
	private ILocationDAO locationDAO;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> validate(Object object) {

		Map<String, String> errors = new HashMap<String, String>();
		if (object instanceof Location) {
			Location location = (Location) object;
			addErrors(validateProperty(location, "code"), errors, "country.code");
			if (errors.size() == 0) {
				Location tempLocation = locationDAO.findLocationByCode(location.getCode());
				if (!ObjectUtil.isEmpty(tempLocation) && tempLocation.getId() != location.getId()) {
					errors.put("country.code", "duplicate.countryCode");
				}
			}
			addErrors(validateProperty(location, "name"), errors, "country.name");
			if (errors.size() == 0) {
				Location tempLocation = locationDAO.findLocationByName(location.getName());
				if (!ObjectUtil.isEmpty(tempLocation) && tempLocation.getId() != location.getId()) {
					errors.put("country.name", "duplicate.countryName");
				}
			}
		}

		return errors;
	}

	@SuppressWarnings("rawtypes")
	protected Set<String> validateProperty(Object entity, String propertyName) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<String> errors = new LinkedHashSet<String>();
		Set<ConstraintViolation<Object>> constraintViolation = validator.validateProperty(entity, propertyName);

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
}
