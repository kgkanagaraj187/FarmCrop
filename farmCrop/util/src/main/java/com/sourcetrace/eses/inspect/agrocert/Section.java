/*
 * Section.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.inspect.agrocert;

import java.util.Set;

/**
 * The Class Section.
 */
public class Section {

    private long id;
    private String code;
    private String serialNo;
    private String name;
    private String sectionType;
    private Set<Question> questions;
    private CertificateCategory certificateCategory;
    private long revisionNumber;

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
     * Gets the code.
     * @return the code
     */
    public String getCode() {

        return code;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Gets the serial no.
     * @return the serial no
     */
    public String getSerialNo() {

        return serialNo;
    }

    /**
     * Sets the serial no.
     * @param serialNo the new serial no
     */
    public void setSerialNo(String serialNo) {

        this.serialNo = serialNo;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the section type.
     * @return the section type
     */
    public String getSectionType() {

        return sectionType;
    }

    /**
     * Sets the section type.
     * @param sectionType the new section type
     */
    public void setSectionType(String sectionType) {

        this.sectionType = sectionType;
    }

    /**
     * Gets the questions.
     * @return the questions
     */
    public Set<Question> getQuestions() {

        return questions;
    }

    /**
     * Sets the questions.
     * @param questions the new questions
     */
    public void setQuestions(Set<Question> questions) {

        this.questions = questions;
    }

    /**
     * Gets the certificate category.
     * @return the certificate category
     */
    public CertificateCategory getCertificateCategory() {

        return certificateCategory;
    }

    /**
     * Sets the certificate category.
     * @param certificateCategory the new certificate category
     */
    public void setCertificateCategory(CertificateCategory certificateCategory) {

        this.certificateCategory = certificateCategory;
    }

    /**
     * Gets the revision number.
     * @return the revision number
     */
    public long getRevisionNumber() {

        return revisionNumber;
    }

    /**
     * Sets the revision number.
     * @param revisionNumber the new revision number
     */
    public void setRevisionNumber(long revisionNumber) {

        this.revisionNumber = revisionNumber;
    }

}
