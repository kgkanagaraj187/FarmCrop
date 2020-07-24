/*
 * GramPanchayatValidator.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.view.general.validator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.entity.util.ESESystem;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.GramPanchayat;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.State;

public class GramPanchayatValidator implements IValidator {

	private static final Logger logger = Logger.getLogger(GramPanchayatValidator.class);
	private static final String COUNTRY_EMPTY = "emptyCountry";
	private static final String COUNTRY_EMPTY_PROPERTY = "empty.country";
	private static final String STATE_EMPTY = "emptyState";
	private static final String STATE_EMPTY_PROPERTY = "empty.state";
	private static final String DISTRICT_EMPTY = "emptyDistrict";
	private static final String DISTRICT_EMPTY_PROPERTY = "empty.district";
	private static final String COUNTRY_NOT_FOUND = "countryNotFound";
	private static final String COUNTRY_NOT_FOUND_PROPERTY = "country.notfound";
	private static final String STATE_NOT_FOUND = "stateNotFound";
	private static final String STATE_NOT_FOUND_PROPERTY = "state.notfound";
	private static final String DISTRICT_NOT_FOUND = "districtNotFound";
	private static final String DISTRICT_NOT_FOUND_PROPERTY = "district.notfound";
	private static final String CITY_NOT_FOUND = "cityNotFound";
	private static final String CITY_NOT_FOUND_PROPERTY = "city.notfound";
	private static final String CITY_EMPTY = "emptyCity";
	private static final String CITY_EMPTY_PROPERTY = "empty.city";
	private static final String NAME_EMPTY = "emptyname";
	private static final String NAME_EMPTY_PROPERTY = "empty.name";
	private static final String CODE_EMPTY = "emptycode";
	private static final String CODE_EMPTY_PROPERTY = "empty.grampanchayat.code";

	private ILocationDAO locationDAO;
	private IFarmerDAO farmerDAO;

	/**
	 * Validate.
	 * 
	 * @param object
	 *            the object
	 * @return the map< string, string>
	 * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
	 */
	@Override
	public Map<String, String> validate(Object object) {

		HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID
				: "";

		if (!ObjectUtil.isEmpty(request)) {
			tenantId = !StringUtil.isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
					? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID) : "";
		}

		ClassValidator gramPanchayatValidator = new ClassValidator(GramPanchayat.class);
		ClassValidator cityValidator = new ClassValidator(Municipality.class);

		GramPanchayat aGramPanchayat = (GramPanchayat) object;
		// Village aVillage = (Village) object;

		Map<String, String> errorCodes = new LinkedHashMap<String, String>();
		if (logger.isInfoEnabled()) {
			logger.info("validate(Object) " + aGramPanchayat.toString());
		}

		InvalidValue[] values = null;

		if (StringUtil.isEmpty(aGramPanchayat.getName())) {
			errorCodes.put(NAME_EMPTY, NAME_EMPTY_PROPERTY);
		}

		if (tenantId.equalsIgnoreCase(ESESystem.AWI_TENANT_ID)) {
			if (StringUtil.isEmpty(aGramPanchayat.getCode())) {
				errorCodes.put(CODE_EMPTY, CODE_EMPTY_PROPERTY);
			} else {
				if (!ValidationUtil.isPatternMaches(aGramPanchayat.getCode(), ValidationUtil.ALPHANUMERIC_PATTERN))
					errorCodes.put("pattern.code", "pattern.code");
			}
		}
		if (StringUtil.isEmpty(aGramPanchayat.getCity().getLocality().getState().getCountry().getName())) {
			errorCodes.put(COUNTRY_EMPTY, COUNTRY_EMPTY_PROPERTY);
		} else {
			// Check whether country name is exists
			Country country = locationDAO
					.findCountryByName(aGramPanchayat.getCity().getLocality().getState().getCountry().getName());
			if (ObjectUtil.isEmpty(country))
				errorCodes.put(COUNTRY_NOT_FOUND, COUNTRY_NOT_FOUND_PROPERTY);
		}

		// State Validation
		if (StringUtil.isEmpty(aGramPanchayat.getCity().getLocality().getState().getCode())) {
			errorCodes.put(STATE_EMPTY, STATE_EMPTY_PROPERTY);
		} else {
			// Check whether state name is exists
			State state = locationDAO.findStateByCode(aGramPanchayat.getCity().getLocality().getState().getCode());
			if (ObjectUtil.isEmpty(state))
				errorCodes.put(STATE_NOT_FOUND, STATE_NOT_FOUND_PROPERTY);
		}

		// District Validation
		if (StringUtil.isEmpty(aGramPanchayat.getCity().getLocality().getCode())) {
			errorCodes.put(DISTRICT_EMPTY, DISTRICT_EMPTY_PROPERTY);
		} else {
			// Check whether district name is exists
			Locality locality = locationDAO.findLocalityByCode(aGramPanchayat.getCity().getLocality().getCode());
			if (ObjectUtil.isEmpty(locality))
				errorCodes.put(DISTRICT_NOT_FOUND, DISTRICT_NOT_FOUND_PROPERTY);
		}
		Municipality city = null;
		// city Validation
		if (StringUtil.isEmpty(aGramPanchayat.getCity().getCode())) {
			errorCodes.put(CITY_EMPTY, CITY_EMPTY_PROPERTY);
		} else {
			// Check whether city name is exists
			city = locationDAO.findMunicipalityByCode(aGramPanchayat.getCity().getCode());
			if (ObjectUtil.isEmpty(city))
				errorCodes.put(CITY_NOT_FOUND, CITY_EMPTY_PROPERTY);
		}

		values = cityValidator.getInvalidValues(aGramPanchayat.getCity(), "city");
		for (InvalidValue value : values)
			errorCodes.put(value.getPropertyName(), value.getMessage());

		values = gramPanchayatValidator.getInvalidValues(aGramPanchayat, "code");
		for (InvalidValue value : values) {
			errorCodes.put(value.getPropertyName(), value.getMessage());
		}
		if (values == null || values.length == 0) {
			if (tenantId.equalsIgnoreCase(ESESystem.AWI_TENANT_ID)) {
				GramPanchayat egramPanchayat = locationDAO.findGramPanchayatByCode(aGramPanchayat.getCode());
				if (egramPanchayat != null) {
					if (aGramPanchayat.getId() != egramPanchayat.getId()) {
						errorCodes.put("code", "unique.gramPanchayatCode");
					} else {
						if (!ObjectUtil.isEmpty(city) && egramPanchayat.getCity().getId() != city.getId()) {
							List<Farmer> farmers = farmerDAO.listFarmerByCityId(egramPanchayat.getCity().getId());
							if (!ObjectUtil.isListEmpty(farmers)) {
								errorCodes.put("farmerExists", "farmer.exists");
							}
						}
					}
				}
			}
		}

		values = gramPanchayatValidator.getInvalidValues(aGramPanchayat, "name");
		for (InvalidValue value : values) {
			errorCodes.put(value.getPropertyName(), value.getMessage());
		}
		
		 //code hidden as req changed to allow duplicate names
		if(!StringUtil.isEmpty(aGramPanchayat.getCity().getCode())){
			Municipality municipality=locationDAO.findMunicipalityByCode(aGramPanchayat.getCity().getCode());
			if(!ObjectUtil.isEmpty(municipality)){
			GramPanchayat gramPanchayatExist=locationDAO.findDuplicateGramPanchayatName(municipality.getId(),aGramPanchayat.getName());
		if (values == null || values.length == 0) {

		//	GramPanchayat egramPanchayat = locationDAO.findGramPanchayatByCode((aGramPanchayat.getCode()));

			if (gramPanchayatExist != null && aGramPanchayat.getId() != gramPanchayatExist.getId()) {
				errorCodes.put("name", "unique.gramPanchayatName");
			}
			
		}
		}
		}
		return errorCodes;
	}

	/**
	 * Gets the location dao.
	 * 
	 * @return the location dao
	 */
	public ILocationDAO getLocationDAO() {

		return locationDAO;
	}

	/**
	 * Sets the location dao.
	 * 
	 * @param locationDAO
	 *            the new location dao
	 */
	public void setLocationDAO(ILocationDAO locationDAO) {

		this.locationDAO = locationDAO;
	}

	/**
	 * Gets the farmer dao.
	 * 
	 * @return the farmer dao
	 */
	public IFarmerDAO getFarmerDAO() {

		return farmerDAO;
	}

	/**
	 * Sets the farmer dao.
	 * 
	 * @param farmerDAO
	 *            the new farmer dao
	 */
	public void setFarmerDAO(IFarmerDAO farmerDAO) {

		this.farmerDAO = farmerDAO;
	}

}
