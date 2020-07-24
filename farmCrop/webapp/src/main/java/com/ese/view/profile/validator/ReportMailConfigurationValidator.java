/*
 * ReportMailConfigurationValidator.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile.validator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.IMailConfigurationDAO;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.ReportMailConfiguration;

public class ReportMailConfigurationValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(ReportMailConfigurationValidator.class);

    private IMailConfigurationDAO mailConfigurationDAO;

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator<ReportMailConfiguration> reportMailConfigurationValidator = new ClassValidator<ReportMailConfiguration>(
                ReportMailConfiguration.class);
        ReportMailConfiguration aReportMailConfiguration = (ReportMailConfiguration) object;

        Map<String, String> errorCodes = new LinkedHashMap<String, String>();
        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + aReportMailConfiguration.toString());
        }

        InvalidValue[] values = null;

        
        if(!StringUtil.isEmpty(aReportMailConfiguration.getName())){
        	if(!ValidationUtil.isPatternMaches(aReportMailConfiguration.getName(), ValidationUtil.NAME_PATTERN)){
        		errorCodes.put("name","reportpattern.name");
        	}
        }else{
        	errorCodes.put("name","empty.name");
        }
        
        if(!StringUtil.isEmpty(aReportMailConfiguration.getMailId())){
        	if(!ValidationUtil.isPatternMaches(aReportMailConfiguration.getMailId(), ValidationUtil.EMAIL_PATTERN)){
        		errorCodes.put("mailId","pattern.mailId");
        	}
        }else{
        	errorCodes.put("mailId","empty.email");
        }
        
        
        if(StringUtil.isListEmpty(aReportMailConfiguration.getDailyReport()) 
        		&& StringUtil.isListEmpty(aReportMailConfiguration.getConsolidatedReport())){
        	errorCodes.put("reports","empty.reports");
        }
        
       /* 
        values = reportMailConfigurationValidator
                .getInvalidValues(aReportMailConfiguration, "name");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }

        values = reportMailConfigurationValidator.getInvalidValues(aReportMailConfiguration,
                "mailId");
        for (InvalidValue value : values) {
            errorCodes.put(value.getPropertyName(), value.getMessage());
        }*/

        if (values == null || values.length == 0) {
            ReportMailConfiguration existing = mailConfigurationDAO
                    .findUniqueMailIdByName(aReportMailConfiguration.getMailId());
            if (existing != null && existing.getId() != aReportMailConfiguration.getId()) {
                errorCodes.put("email", "unique.emailId");
            }
        }

        return errorCodes;
    }

    /**
     * Sets the mail configuration dao.
     * @param mailConfigurationDAO the new mail configuration dao
     */
    public void setMailConfigurationDAO(IMailConfigurationDAO mailConfigurationDAO) {

        this.mailConfigurationDAO = mailConfigurationDAO;
    }

    /**
     * Gets the mail configuration dao.
     * @return the mail configuration dao
     */
    public IMailConfigurationDAO getMailConfigurationDAO() {

        return mailConfigurationDAO;
    }

}
