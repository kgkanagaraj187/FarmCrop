/*
 * Section.java
 * Copyright (c) 2016-2017, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.inspect.agrocert;

import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.DataLevel;

/**
 * The Class Section.
 */
public class SurveySection {

      public static final String CODE_PREFIX = "S";

    private long id;
    private String code;
    private String serialNo;
    private String name;
    private Set<SurveyQuestion> questions;
    private CertificateCategory certificateCategory;
    private Set<LanguagePreferences> languagePreferences;
    private boolean reservedSection;
    private long revisionNumber;
     // transient
    private List<LanguagePreferences> languagePreferenceList;
    private int passScore;
    private int totalScore;

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
    //@NotEmpty(message = "empty.code")
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
    //@NotEmpty(message = "empty.name")
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
     * Gets the questions.
     * @return the questions
     */
    public Set<SurveyQuestion> getQuestions() {

        return questions;
    }

    /**
     * Sets the questions.
     * @param questions the new questions
     */
    public void setQuestions(Set<SurveyQuestion> questions) {

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

   /* *//**
     * Gets the data level.
     * @return the data level
     *//*
    public DataLevel getDataLevel() {

        return dataLevel;
    }

    *//**
     * Sets the data level.
     * @param dataLevel the new data level
     *//*
    public void setDataLevel(DataLevel dataLevel) {

        this.dataLevel = dataLevel;
    }*/

  
    /**
     * Gets the language preferences.
     * @return the language preferences
     */
    public Set<LanguagePreferences> getLanguagePreferences() {

        return languagePreferences;
    }

    /**
     * Sets the language preferences.
     * @param languagePreferences the new language preferences
     */
    public void setLanguagePreferences(Set<LanguagePreferences> languagePreferences) {

        this.languagePreferences = languagePreferences;
    }

    /**
     * Gets the reserved section.
     * @return the reserved section
     */
    public boolean getReservedSection() {

        return reservedSection;
    }

    /**
     * Sets the reserved section.
     * @param reservedSection the new reserved section
     */
    public void setReservedSection(boolean reservedSection) {

        this.reservedSection = reservedSection;
    }

    /**
     * Gets the language preference list.
     * @return the language preference list
     */
    public List<LanguagePreferences> getLanguagePreferenceList() {

        return languagePreferenceList;
    }

    /**
     * Sets the language preference list.
     * @param languagePreferenceList the new language preference list
     */
    public void setLanguagePreferenceList(List<LanguagePreferences> languagePreferenceList) {

        this.languagePreferenceList = languagePreferenceList;
    }

    /**
     * Gets the pass score.
     * @return the pass score
     */
    public int getPassScore() {

        return passScore;
    }

    /**
     * Sets the pass score.
     * @param passScore the new pass score
     */
    public void setPassScore(int passScore) {

        this.passScore = passScore;
    }

    /**
     * Gets the total score.
     * @return the total score
     */
    public int getTotalScore() {

        return totalScore;
    }

    /**
     * Sets the total score.
     * @param totalScore the new total score
     */
    public void setTotalScore(int totalScore) {

        this.totalScore = totalScore;
    }

	public long getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(long revisionNumber) {
		this.revisionNumber = revisionNumber;
	}
    
    

}
