/**
 * MunicipalityValidator.java
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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.entity.util.ESESystem;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.Locality;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.State;

/**
 * The Class MunicipalityValidator.
 */
public class MunicipalityValidator implements IValidator {

    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(MunicipalityValidator.class);
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

    /** The location dao. */
    private ILocationDAO locationDAO;

    /**
     * Sets the location dao.
     * @param locationDAO the new location dao
     */
    public void setLocationDAO(ILocationDAO locationDAO) {

        this.locationDAO = locationDAO;
    }

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @Override
    public Map<String, String> validate(Object object) {
    	
    	HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
        String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID)
                ? ISecurityFilter.DEFAULT_TENANT_ID : "";
    	
        if (!ObjectUtil.isEmpty(request)) {
            tenantId = !StringUtil
                    .isEmpty((String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID))
                            ? (String) request.getSession().getAttribute(ISecurityFilter.TENANT_ID)
                            : "";
        }

        ClassValidator municipalityValidator = new ClassValidator(Municipality.class);
        ClassValidator localityValidator = new ClassValidator(Locality.class);
        Municipality aMunicipality = (Municipality) object;

        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aMunicipality.toString());
        }

        InvalidValue[] values = null;

        if (StringUtil.isEmpty(aMunicipality.getLocality().getState().getCountry().getName())) {
            errorCodes.put(COUNTRY_EMPTY, COUNTRY_EMPTY_PROPERTY);
        } else {
            // Check whether country name is exists
            Country country = locationDAO.findCountryByName(aMunicipality.getLocality().getState()
                    .getCountry().getName());
            if (ObjectUtil.isEmpty(country))
                errorCodes.put(COUNTRY_NOT_FOUND, COUNTRY_NOT_FOUND_PROPERTY);
        }

        // State Validation
        if (StringUtil.isEmpty(aMunicipality.getLocality().getState().getCode())) {
            errorCodes.put(STATE_EMPTY, STATE_EMPTY_PROPERTY);
        } else {
            // Check whether state name is exists
            State state = locationDAO.findStateByCode(aMunicipality.getLocality().getState()
                    .getCode());
            if (ObjectUtil.isEmpty(state))
                errorCodes.put(STATE_NOT_FOUND, STATE_NOT_FOUND_PROPERTY);
        }

        // District Validation
        if (StringUtil.isEmpty(aMunicipality.getLocality().getCode())) {
            errorCodes.put(DISTRICT_EMPTY, DISTRICT_EMPTY_PROPERTY);
        } else {
            // Check whether district name is exists
            Locality locality = locationDAO.findLocalityByCode(aMunicipality.getLocality()
                    .getCode());
            if (ObjectUtil.isEmpty(locality))
                errorCodes.put(DISTRICT_NOT_FOUND, DISTRICT_NOT_FOUND_PROPERTY);
        }

        values = localityValidator.getInvalidValues(aMunicipality.getLocality(), "locality");
        for (InvalidValue value : values)
            errorCodes.put(value.getPropertyName(), value.getMessage());

        

        values = municipalityValidator.getInvalidValues(aMunicipality, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        //code hidden as req changed to allow duplicate names
       
        	
       if (values == null || values.length == 0) {
    	   if (!StringUtil.isEmpty(aMunicipality.getLocality().getCode())){
    		   Locality locality=locationDAO.findLocalityByCode(aMunicipality.getLocality().getCode());
    		   if(!ObjectUtil.isEmpty(locality)){
    			   Municipality municipalityExist=locationDAO.findDuplicateMunicipalityName(locality.getId(),aMunicipality.getName());
          /*  Municipality eMunicipality = locationDAO.findMunicipalityByCode((aMunicipality
                    .getCode()));*/
            if (municipalityExist != null && municipalityExist.getId() != aMunicipality.getId()) {
                errorCodes.put("name", "unique.municipalityName");
            }
    		   }
        }
        }
        if(tenantId.equalsIgnoreCase(ESESystem.AWI_TENANT_ID))
        {
	        if(StringUtil.isEmpty(aMunicipality.getCode())){
	            errorCodes.put("code", "empty.muncipality.code");
	        }else{
	            if (!ValidationUtil.isPatternMaches(aMunicipality.getCode(), ValidationUtil.ALPHANUMERIC_PATTERN))
	                errorCodes.put("pattern.code", "pattern.code");
	            
	            Municipality eMunicipality = locationDAO.findMunicipalityByCode((aMunicipality
	                    .getCode()));
	            if (eMunicipality != null && aMunicipality.getId() != eMunicipality.getId()) {
	                errorCodes.put("code", "unique.cityCode");
	            }
	        }
        }
        values = municipalityValidator.getInvalidValues(aMunicipality, "postalCode");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if (!StringUtil.isEmpty(aMunicipality.getLatitude())) {
            values = municipalityValidator.getInvalidValues(aMunicipality, "latitude");
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }

        if (!StringUtil.isEmpty(aMunicipality.getLongitude())) {
            values = municipalityValidator.getInvalidValues(aMunicipality, "longitude");
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }

        if (!StringUtil.isEmpty(aMunicipality.getLatitude())
                && !StringUtil.isEmpty(aMunicipality.getLongitude())) {
            if (values == null || values.length == 0) {
                Municipality eMunicipality = locationDAO.findMunicipalityByName((aMunicipality
                        .getName()));
                eMunicipality = locationDAO.findMunicipalityByCoordinates(aMunicipality
                        .getLatitude(), aMunicipality.getLongitude());
                if (eMunicipality != null && aMunicipality.getId() != eMunicipality.getId()) {
                    errorCodes.put("coordinates", "unique.latitude.longitude");
                }
            }
        }
        
        if(!StringUtil.isEmpty(aMunicipality.getName())){
        	if(!ValidationUtil.isPatternMaches(aMunicipality.getName(), ValidationUtil.NAME_PATTERN)){
        		errorCodes.put("name","pattern.name");
        	}
        }else{
        	errorCodes.put("name","empty.name");
        }
        
        if(!StringUtil.isEmpty(aMunicipality.getPostalCode())){
        	if(!ValidationUtil.isPatternMaches(aMunicipality.getPostalCode(), ValidationUtil.NUMBER_PATTERN)){
        		errorCodes.put("name","pattern.postalcode");
        	}
        }else
        /*{
        	errorCodes.put("postalCode","empty.postalcode");
        }*/
        
        if(!StringUtil.isEmpty(aMunicipality.getLatitude())){
        	if(!ValidationUtil.isPatternMaches(aMunicipality.getLatitude(), ValidationUtil.LATITUDE_PATTERN)){
        		errorCodes.put("latitude","pattern.latitude");
        	}
        }
        
        if(!StringUtil.isEmpty(aMunicipality.getLongitude())){
        	if(!ValidationUtil.isPatternMaches(aMunicipality.getLongitude(), ValidationUtil.LONGITUDE_PATTERN)){
        		errorCodes.put("longitude","pattern.longitude");
        	}
        }
        
        return errorCodes;
    }

}
