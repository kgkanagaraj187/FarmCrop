/*
 * CustomerProjectValidator.java
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

import com.ese.view.validator.IValidator;
import com.sourcetrace.eses.dao.ICertificationDAO;
import com.sourcetrace.eses.dao.IClientDAO;
import com.sourcetrace.eses.inspect.agrocert.CertificateCategory;
import com.sourcetrace.eses.inspect.agrocert.CertificateStandard;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.CustomerProject;

public class CustomerProjectValidator implements IValidator {

    private static final Logger logger = Logger.getLogger(CustomerValidator.class);
    private IClientDAO clientDAO;
    private ICertificationDAO certificationDAO;

    /**
     * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> validate(Object object) {

        ClassValidator customerProjectValidator = new ClassValidator(CustomerProject.class);
        CustomerProject customerProject = (CustomerProject) object;
        Map<String, String> errorCodes = new LinkedHashMap<String, String>();

        if (logger.isInfoEnabled()) {
            logger.info("validate(Object) " + customerProject.toString());
        }
       
        if (!StringUtil.isEmpty(customerProject.getNameOfProject())) {
            if (!ValidationUtil.isPatternMaches(customerProject.getNameOfProject(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.nameOfProject", "pattern.nameOfProject");
            }
        }else{
            errorCodes.put("empty.nameOfProject","empty.nameOfProject");
        }
        
        if (!StringUtil.isEmpty(customerProject.getNumberOfProjects())) {
            if (!ValidationUtil.isPatternMaches(customerProject.getNumberOfProjects(),ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.numberOfProjects", "pattern.numberOfProjects");
            }
        }else{
            errorCodes.put("empty.numberOfProjects","empty.numberOfProjects");
        }
        
        if (!StringUtil.isEmpty(customerProject.getReportNo())) {
            if (!ValidationUtil.isPatternMaches(customerProject.getReportNo(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.reportNo", "pattern.reportNo");
            }
        }else{
            errorCodes.put("empty.reportNo","empty.reportNo");
        }
        
        if (!StringUtil.isEmpty(customerProject.getUnitNo())) {
            if (!ValidationUtil.isPatternMaches(customerProject.getUnitNo(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.unitNo", "pattern.unitNo");
            }
        }else{
            errorCodes.put("empty.unitNo","empty.unitNo");
        }
        
        if (!StringUtil.isEmpty(customerProject.getNameOfUnit())) {
            if (!ValidationUtil.isPatternMaches(customerProject.getNameOfUnit(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.nameOfUnit", "pattern.nameOfUnit");
            }
        }else{
            errorCodes.put("empty.nameOfUnit","empty.nameOfUnit");
        }
        
        if (!StringUtil.isEmpty(customerProject.getLocationOfUnit())) {
            if (!ValidationUtil.isPatternMaches(customerProject.getLocationOfUnit(),ValidationUtil.ALPHANUMERIC_PATTERN)) {
                errorCodes.put("pattern.locationOfUnit", "pattern.locationOfUnit");
            }
        }else{
            errorCodes.put("empty.locationOfUnit","empty.locationOfUnit");
        }
        
        if (!ObjectUtil.isEmpty(customerProject.getCertificateStandard())
                && !ObjectUtil.isEmpty(customerProject.getCertificateStandard().getCategory())
                && customerProject.getCertificateStandard().getId() > -1
                && customerProject.getCertificateStandard().getCategory().getId() > -1) {
            CertificateCategory certificateCategory = certificationDAO
                    .findCertificateCategoryById(customerProject.getCertificateStandard()
                            .getCategory().getId());
            CertificateStandard certificateStandard = certificationDAO
                    .findCertificateStandardById(customerProject.getCertificateStandard().getId());
            if (ObjectUtil.isEmpty(certificateCategory)) {
                errorCodes.put("customerProject.certificateCategory",
                        "certificateCategory.not.exists");
            }
            if (ObjectUtil.isEmpty(certificateStandard)) {
                errorCodes.put("customerProject.certificateStandard",
                        "certificateStandard.not.exists");
            }
            if (!ObjectUtil.isEmpty(certificateCategory)
                    && !ObjectUtil.isEmpty(certificateStandard)) {
                if (customerProject.getCertificateStandard().getCategory().getId() != certificateStandard
                        .getCategory().getId()) {
                    errorCodes.put("customerProject.certificateStandard",
                            "certificateStandard.certificateCategory.mapping.not.exists");
                }
            }
        }else{
            
            if (!ObjectUtil.isEmpty(customerProject.getCertificateStandard())){
               
                if (!ObjectUtil.isEmpty(customerProject.getCertificateStandard().getCategory()) &&
                        customerProject.getCertificateStandard().getCategory().getId()<=-1){
                    errorCodes.put("emptyCertificateCategory", "empty.certificatCategory");
                }
                
                if (customerProject.getCertificateStandard().getId() <= -1){
                    errorCodes.put("emptyCertificateStandard", "empty.certificatStandard");
                }
                
            }else{
                errorCodes.put("emptyCertificateStandard", "empty.certificatStandard");
                errorCodes.put("emptyCertificateCategory", "empty.certificatCategory");
            }
        }

        if (customerProject.getInspection() == -1) {
            errorCodes.put("emptyInspection", "empty.inspection");
        }

        if (customerProject.getIcsStatus() == -1) {
            errorCodes.put("emptyIcsStatus", "empty.icsStatus");
        }
        
        if (!StringUtil.isEmpty(customerProject.getNumberOfFarmers())) {
            if (!ValidationUtil.isPatternMaches(customerProject.getNumberOfFarmers(),ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.numberOfFarmers", "pattern.numberOfFarmers");
            }
        }else{
            errorCodes.put("empty.numberOfFarmers","empty.numberOfFarmers");
        }
        
        if (!StringUtil.isEmpty(customerProject.getArea())) {
            if (!ValidationUtil.isPatternMaches(customerProject.getArea(),ValidationUtil.NUMBER_PATTERN)) {
                errorCodes.put("pattern.area", "pattern.area");
            }
        }else{
            errorCodes.put("empty.area","empty.area");
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

    /**
     * Gets the certification dao.
     * @return the certification dao
     */
    public ICertificationDAO getCertificationDAO() {

        return certificationDAO;
    }

    /**
     * Sets the certification dao.
     * @param certificationDAO the new certification dao
     */
    public void setCertificationDAO(ICertificationDAO certificationDAO) {

        this.certificationDAO = certificationDAO;
    }

}
