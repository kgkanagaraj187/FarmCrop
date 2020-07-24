/*
 * CustomerProject.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import com.sourcetrace.eses.inspect.agrocert.CertificateStandard;

/**
 * The Class CustomerProject.
 */
public class CustomerProject {

    public static final String INDIVIDUAL_FARMER = "0";
    public static final String SMALL_HOLDER_GROUP = "1";

    public static final String INSPECTION_TYPE_FIRST = "I1";
    public static final String INSPECTION_TYPE_REPEAT = "I2";
    public static final String INSPECTION_TYPE_SUPPLEMENTRY = "I3";
    public static final String INSPECTION_TYPE_NEW_OR_PRODUCTION = "I4";
    public static final String INSPECTION_TYPE_CHANGED = "I5";
    public static final String INSPECTION_TYPE_NEW_FIELD = "I6";
    public static final String INSPECTION_TYPE_OTHERS = "I7";

    public static final String UNIT_COMPOSITION_TYPE_IC1 = "FS1";
    public static final String UNIT_COMPOSITION_TYPE_IC2 = "FS2";
    public static final String UNIT_COMPOSITION_TYPE_ORGANIC = "FS3";
    private static final String EXPRESSION_AREA = "^([+] ?)?[0-9]+(.[0-9]+)*$";

    private long id;
    private String codeOfProject;
    private String nameOfProject;
    private String numberOfProjects;
    private String unitNo;
    private String reportNo;
    private String nameOfUnit;
    private String locationOfUnit;
    private int typeOfHolding;
    private int inspection;
    private String numberOfFarmers;
    private String area;
    private long revisionNo;
    private CertificateStandard certificateStandard;
    private int icsStatus;
    private Customer customer;

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the code of project.
     * @return the code of project
     */
    public String getCodeOfProject() {

        return codeOfProject;
    }

    /**
     * Sets the code of project.
     * @param codeOfProject the new code of project
     */
    public void setCodeOfProject(String codeOfProject) {

        this.codeOfProject = codeOfProject;
    }

    /**
     * Gets the name of project.
     * @return the name of project
     */

    public String getNameOfProject() {

        return nameOfProject;
    }

    /**
     * Sets the name of project.
     * @param nameOfProject the new name of project
     */
    public void setNameOfProject(String nameOfProject) {

        this.nameOfProject = nameOfProject;
    }

    /**
     * Gets the number of projects.
     * @return the number of projects
     */
    public String getNumberOfProjects() {

        return numberOfProjects;
    }

    /**
     * Sets the number of projects.
     * @param numberOfProjects the new number of projects
     */
    public void setNumberOfProjects(String numberOfProjects) {

        this.numberOfProjects = numberOfProjects;
    }

    /**
     * Gets the unit no.
     * @return the unit no
     */
    public String getUnitNo() {

        return unitNo;
    }

    /**
     * Sets the unit no.
     * @param unitNo the new unit no
     */
    public void setUnitNo(String unitNo) {

        this.unitNo = unitNo;
    }

    /**
     * Gets the report no.
     * @return the report no
     */
    public String getReportNo() {

        return reportNo;
    }

    /**
     * Sets the report no.
     * @param reportNo the new report no
     */
    public void setReportNo(String reportNo) {

        this.reportNo = reportNo;
    }

    /**
     * Gets the name of unit.
     * @return the name of unit
     */
    public String getNameOfUnit() {

        return nameOfUnit;
    }

    /**
     * Sets the name of unit.
     * @param nameOfUnit the new name of unit
     */
    public void setNameOfUnit(String nameOfUnit) {

        this.nameOfUnit = nameOfUnit;
    }

    /**
     * Gets the location of unit.
     * @return the location of unit
     */
    public String getLocationOfUnit() {

        return locationOfUnit;
    }

    /**
     * Sets the location of unit.
     * @param locationOfUnit the new location of unit
     */
    public void setLocationOfUnit(String locationOfUnit) {

        this.locationOfUnit = locationOfUnit;
    }

    /**
     * Gets the type of holding.
     * @return the type of holding
     */
    public int getTypeOfHolding() {

        return typeOfHolding;
    }

    /**
     * Sets the type of holding.
     * @param typeOfHolding the new type of holding
     */
    public void setTypeOfHolding(int typeOfHolding) {

        this.typeOfHolding = typeOfHolding;
    }

    /**
     * Gets the inspection.
     * @return the inspection
     */
    public int getInspection() {

        return inspection;
    }

    /**
     * Sets the inspection.
     * @param inspection the new inspection
     */
    public void setInspection(int inspection) {

        this.inspection = inspection;
    }

    /**
     * Gets the number of farmers.
     * @return the number of farmers
     */

    public String getNumberOfFarmers() {

        return numberOfFarmers;
    }

    /**
     * Sets the number of farmers.
     * @param numberOfFarmers the new number of farmers
     */
    public void setNumberOfFarmers(String numberOfFarmers) {

        this.numberOfFarmers = numberOfFarmers;
    }

    /**
     * Gets the area.
     * @return the area
     */

    public String getArea() {

        return area;
    }

    /**
     * Sets the area.
     * @param area the new area
     */
    public void setArea(String area) {

        this.area = area;
    }

    /**
     * Gets the revision no.
     * @return the revision no
     */
    public long getRevisionNo() {

        return revisionNo;
    }

    /**
     * Sets the revision no.
     * @param revisionNo the new revision no
     */
    public void setRevisionNo(long revisionNo) {

        this.revisionNo = revisionNo;
    }

    /**
     * Gets the customer.
     * @return the customer
     */
    public Customer getCustomer() {

        return customer;
    }

    /**
     * Sets the customer.
     * @param customer the new customer
     */
    public void setCustomer(Customer customer) {

        this.customer = customer;
    }

    /**
     * Gets the certificate standard.
     * @return the certificate standard
     */
    public CertificateStandard getCertificateStandard() {

        return certificateStandard;
    }

    /**
     * Sets the certificate standard.
     * @param certificateStandard the new certificate standard
     */
    public void setCertificateStandard(CertificateStandard certificateStandard) {

        this.certificateStandard = certificateStandard;
    }

    /**
     * Gets the ics status.
     * @return the ics status
     */
    public int getIcsStatus() {

        return icsStatus;
    }

    /**
     * Sets the ics status.
     * @param icsStatus the new ics status
     */
    public void setIcsStatus(int icsStatus) {

        this.icsStatus = icsStatus;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    // this method for getting key pair value of customer project code and name
    // if u want to change this method please check with all the related files
    @Override
    public String toString() {

        return nameOfProject + "-" + codeOfProject;
    }

}
