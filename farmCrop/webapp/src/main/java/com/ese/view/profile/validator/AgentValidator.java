/*
 * AgentValidator.java
 * Copyright (c) 2012-2013, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile.validator;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.entity.util.ESESystem;
import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IAgentDAO;
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.service.IServicePointDAO;
import com.sourcetrace.eses.umgmt.entity.ContactInfo;
import com.sourcetrace.eses.umgmt.entity.PersonalInfo;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ServicePoint;

/**
 * The Class AgentValidator.
 */
public class AgentValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(AgentValidator.class);

    private IAgentDAO agentDAO;
    private ILocationDAO locationDAO;
    private IServicePointDAO servicePointDAO;
    private IProductDistributionDAO productDistributionDAO;
    private static final String CITY_NOT_FOUND = "cityNotFound";
    private static final String CITY_NOT_FOUND_PROPERTY = "city.notfound";
    private static final String COUNTRY_EMPTY = "emptyCountry";
    private static final String COUNTRY_EMPTY_PROPERTY = "empty.country";
    private static final String COUNTRY_NOT_FOUND = "countryNotFound";
    private static final String COUNTRY_NOT_FOUND_PROPERTY = "country.notfound";
    private static final String STATE_EMPTY = "emptyState";
    private static final String STATE_EMPTY_PROPERTY = "empty.state";
    private static final String STATE_NOT_FOUND = "stateNotFound";
    private static final String STATE_NOT_FOUND_PROPERTY = "state.notfound";
    private static final String DISTRICT_EMPTY = "emptyDistrict";
    private static final String DISTRICT_EMPTY_PROPERTY = "empty.district";
    private static final String DISTRICT_NOT_FOUND = "districtNotFound";
    private static final String DISTRICT_NOT_FOUND_PROPERTY = "district.notfound";
    private static final String SERVICEPOINT_NOT_FOUND = "servicePointNotFound";
    private static final String SERVICEPOINT_NOT_FOUND_PROPERTY = "servicepoint.notfound";
    private static final String SELECT = "-1";
   
    /**
     * Gets the location dao.
     * @return the location dao
     */
    public ILocationDAO getLocationDAO() {

        return locationDAO;
    }

    /**
     * Sets the location dao.
     * @param locationDAO the new location dao
     */
    public void setLocationDAO(ILocationDAO locationDAO) {

        this.locationDAO = locationDAO;
    }

    /**
     * Gets the agent dao.
     * @return the agent dao
     */
    public IAgentDAO getAgentDAO() {

        return agentDAO;
    }

    /**
     * Gets the service point dao.
     * @return the service point dao
     */
    public IServicePointDAO getServicePointDAO() {

        return servicePointDAO;
    }

    /**
     * Sets the service point dao.
     * @param servicePointDAO the new service point dao
     */
    public void setServicePointDAO(IServicePointDAO servicePointDAO) {

        this.servicePointDAO = servicePointDAO;
    }

    /**
     * Sets the agent dao.
     * @param agentDAO the new agent dao
     */
    public void setAgentDAO(IAgentDAO agentDAO) {

        this.agentDAO = agentDAO;
    }

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @Override
    public Map<String, String> validate(Object agent) {

        ClassValidator<Agent> agentValidator = new ClassValidator<Agent>(Agent.class);
        ClassValidator contactValidator = new ClassValidator(ContactInfo.class);
        ClassValidator cityValidator = new ClassValidator(Municipality.class);
        ClassValidator personalValidator = new ClassValidator(PersonalInfo.class);
        ClassValidator servicePointValidator = new ClassValidator(ServicePoint.class);
        Agent aAgent = (Agent) agent;
        HttpServletRequest httpRequest=ReflectUtil.getCurrentHttpRequest();
        String branchId_F=(String) httpRequest.getSession().getAttribute(ISecurityFilter.CURRENT_BRANCH);
        String tenantId = (String) httpRequest.getSession().getAttribute(ISecurityFilter.TENANT_ID);
        tenantId = StringUtil.isEmpty(tenantId) ? ISecurityFilter.DEFAULT_TENANT_ID : tenantId;
        
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + agent.toString());
        }
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        InvalidValue[] values = null;

    
        values = agentValidator.getInvalidValues(aAgent, "profileId");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }        
        
        if(!StringUtil.isEmpty(aAgent.getProfileId())){
        	Agent eAgent = agentDAO.findAgentByAgentIdd(aAgent.getProfileId());
        	if(eAgent!=null && eAgent.getId()!= aAgent.getId() && eAgent.getProfileId().equalsIgnoreCase(aAgent.getProfileId())){
        		errorCodes.put("profileId", "unique.agentId");
        	}
        }
        
        if(StringUtil.isEmpty(aAgent.getAgentType().getCode()) || aAgent.getAgentType().getCode().equalsIgnoreCase("02") || aAgent.getAgentType().getCode().equalsIgnoreCase("01")){
	        if (ObjectUtil.isListEmpty(aAgent.getWareHouses())) {
	            if (!ObjectUtil.isEmpty(aAgent.getAgentType()) && aAgent.getAgentType().getId() != 1) {
	                errorCodes.put("cooperative", "empty.samithi");
	            }
	
	        }
        }
        values = personalValidator.getInvalidValues(aAgent.getPersonalInfo(), "firstName");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if (aAgent.getPersonalInfo().getMiddleName() != null
                && aAgent.getPersonalInfo().getMiddleName().length() > 0) {
            values = personalValidator.getInvalidValues(aAgent.getPersonalInfo(), "middleName");
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }

        if (aAgent.getPersonalInfo().getSecondLastName() != null
                && aAgent.getPersonalInfo().getSecondLastName().length() > 0) {
            values = personalValidator.getInvalidValues(aAgent.getPersonalInfo(), "secondLastName");
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }

        if (!StringUtil.isEmpty(aAgent.getPersonalInfo().getIdentityNumber())) {
            values = personalValidator.getInvalidValues(aAgent.getPersonalInfo(), "identityNumber");
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }
        
        if(tenantId.equalsIgnoreCase("chetna") || tenantId.equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
        	 if (!ObjectUtil.isEmpty(aAgent.getAgentType()) && aAgent.getAgentType().getCode()==null) {
                 errorCodes.put("agentType", "empty.mobileUserType");
             }
        	 if(!StringUtil.isEmpty(aAgent.getAgentType().getCode()) && aAgent.getAgentType().getCode().equalsIgnoreCase("03")){
        		 if(ObjectUtil.isListEmpty(aAgent.getWareHouses())){
        			 errorCodes.put("selectedWarehouses", "empty.shgs");
        		 }
	        	 if (ObjectUtil.isEmpty(aAgent.getProcurementCenter())) {
	                 errorCodes.put("selectedProcurementCenter", "empty.procurementCenter");
	             }
        	 
        	 }else if (!StringUtil.isEmpty(aAgent.getAgentType().getCode()) && aAgent.getAgentType().getCode().equalsIgnoreCase("04")){
        		 if (ObjectUtil.isEmpty(aAgent.getProcurementCenter())) {
                     errorCodes.put("selectedGinningCenter", "empty.ginningCenter");
                 }
        	 }else if(!StringUtil.isEmpty(aAgent.getAgentType().getCode()) && aAgent.getAgentType().getCode().equalsIgnoreCase("05")){
        		 if (ObjectUtil.isEmpty(aAgent.getProcurementCenter())) {
                     errorCodes.put("selectedSpinner", "empty.spinner");
                 }
        	 }
        	 
        }
         if(tenantId.equals(ESESystem.PRATIBHA_TENANT_ID)){
        	 if (!ObjectUtil.isEmpty(aAgent.getAgentType()) && aAgent.getAgentType().getCode()==null) {
                 errorCodes.put("agentType", "empty.mobileUserType");
             }
        	 if(aAgent.getAgentType().getCode()!=null && !aAgent.getAgentType().getCode().equalsIgnoreCase("06")){
        	 if (ObjectUtil.isEmpty(aAgent.getProcurementCenter())) {
                 errorCodes.put("warehouseName", "empty.warehouse");
             }
         }  
         }
        values = agentValidator.getInvalidValues(aAgent, "status");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }
        if (aAgent.getPassword() != null && aAgent.getPassword().length() > 0) {
            values = agentValidator.getInvalidValues(aAgent, "password");

            if (aAgent.getPassword().length() != 6) {
                errorCodes.put("agent.password", "length.passwords");
            }

        }
        if (values == null || values.length == 0) {
            Date toDay = new Date();
            Date SelectedDate = aAgent.getPersonalInfo().getDateOfBirth();
            if (SelectedDate != null && toDay.before(SelectedDate)) {
                errorCodes.put("dateofbirth", "invalid.dateofbirth");
            }
        }

        values = contactValidator.getInvalidValues(aAgent.getContactInfo(), "phoneNumber");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        if ((!(ObjectUtil.isEmpty(aAgent.getContactInfo())))
                && (!(StringUtil.isEmpty(aAgent.getContactInfo().getMobileNumber())))) {
            values = contactValidator.getInvalidValues(aAgent.getContactInfo(), "mobileNumber");
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }

        if ((!(ObjectUtil.isEmpty(aAgent.getContactInfo())))
                && (!(StringUtil.isEmpty(aAgent.getContactInfo().getEmail())))) {
            values = contactValidator.getInvalidValues(aAgent.getContactInfo(), "email");
            for (InvalidValue value : values) {
                errorCodes.put(value.getPropertyName(), value.getMessage());
            }
        }

        if (aAgent.getTxnMode() == -1) {
            errorCodes.put("txnMode", "empty.txnMode");
        }

/*        if (values == null || values.length == 0) {
            if (!StringUtil.isEmpty(aAgent.getProfileId())) {
                Agent eAgent = agentDAO.findAgentByAgentId(aAgent.getProfileId());
                if (eAgent != null && eAgent.getId() != aAgent.getId()) {
                    errorCodes.put("profileId", "unique.agentId");
                }
                
            }
        }*/
        if (aAgent.getStatus() == 1) {

            if (StringUtil.isEmpty(aAgent.getPassword())
                    || StringUtil.isEmpty(aAgent.getConfirmPassword())) {
                errorCodes.put("agent.password", "password.missing");
            } else if (!aAgent.getPassword().equals(aAgent.getConfirmPassword())) {
                errorCodes.put("agent.password", "password.missmatch");
            }

        }
        
        
        if (!StringUtil.isEmpty(aAgent.getProfileId())) {
            if (!ValidationUtil.isPatternMaches(aAgent.getProfileId(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.profileId", "pattern.profileId");
            }
        } else {
            errorCodes.put("empty.profileId", "empty.profileId");
        }

        if (!StringUtil.isEmpty(aAgent.getPersonalInfo().getFirstName())) {
            if (!ValidationUtil.isPatternMaches(aAgent.getPersonalInfo().getFirstName(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.firstName", "pattern.firstName");
            }
        } else {
            errorCodes.put("empty.firstName", "empty.firstName");
        }
        if (!StringUtil.isEmpty(aAgent.getPersonalInfo().getFirstName())) {
            if (!ValidationUtil.isPatternMaches(aAgent.getPersonalInfo().getFirstName(),
                    ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.lastName", "pattern.lastName");
            }
        }
        if (!StringUtil.isEmpty(aAgent.getPersonalInfo().getIdentityNumber())) {
            if (!ValidationUtil.isPatternMaches(aAgent.getPersonalInfo().getIdentityNumber(),
                    ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.identityNumber", "pattern.identityNumber");
            }
        }
        if (!StringUtil.isEmpty(aAgent.getContactInfo().getMobileNumber())) {
            if (!ValidationUtil.isPatternMaches(aAgent.getContactInfo().getMobileNumber(),
                    ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.mobile", "pattern.mobile");
            }
        }
        if (!StringUtil.isEmpty(aAgent.getContactInfo().getEmail())) {
            if (!ValidationUtil.isPatternMaches(aAgent.getContactInfo().getEmail(),
                    ValidationUtil.EMAIL_PATTERN)) {
                errorCodes.put("pattern.email", "pattern.email");
            }
        }
        if (!StringUtil.isEmpty(aAgent.getContactInfo().getPhoneNumber())) {
            if (!ValidationUtil.isPatternMaches(aAgent.getContactInfo().getPhoneNumber(),
                    ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.phoneNumber", "pattern.phoneNumber");
            }
        }
        
        if (aAgent.isTrainingExists()) {
            if (ObjectUtil.isEmpty(aAgent.getSelectedtrainings())||aAgent.getSelectedtrainings().length()<=0) {
                errorCodes.put("selectedtrainings", "empty.selectedtrainings");
            }

        }
        
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
