/*
 * SurveyMaster.java
 * Copyright (c) 2008-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.inspect.agrocert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.DataLevel;

public class SurveyMaster implements Cloneable {

	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final String CODE_PREFIX = "CS";
	public static long DEFAULT_SURVEY_TYPE_ID = 1;
	public static String ALREADY_MAPPED_SCOPE = "mapped";


	private long id;
	private String code;
	private String name;
	private String description;
	private Date validFrom;
	private Date validTo;
	private DataLevel dataLevel;
	private SurveyType surveyType;
	private Set<SurveySection> sections;
	private Date createdDate;
	private Date updatedDate;
	private String createdUserName;
	private String updatedUserName;
	private long revisionNumber;
	
	private int status;
		private SortedSet<SurveyQuestionMapping> surveyQuestionMapping;
	private Set<LanguagePreferences> languagePreferences;
	private Set<SurveyQuestion> verifyQuestions;
	private String language;
	private SurveySection section;
	private List<String> sectionCodes;
		private List<LanguagePreferences> languagePreferencesList;
	private List<String> questionCodes;
	private List<String> availQuestionCodes;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {

		this.id = id;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {

		return code;
	}

	/**
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {

		this.code = code;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {

		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	//@NotEmpty(message = "empty.name")
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {

		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {

		this.description = description;
	}

	/**
	 * Gets the valid from.
	 * 
	 * @return the valid from
	 */
	public Date getValidFrom() {

		return validFrom;
	}

	/**
	 * Sets the valid from.
	 * 
	 * @param validFrom
	 *            the new valid from
	 */
	public void setValidFrom(Date validFrom) {

		this.validFrom = validFrom;
	}

	/**
	 * Gets the valid to.
	 * 
	 * @return the valid to
	 */
	public Date getValidTo() {

		return validTo;
	}

	/**
	 * Sets the valid to.
	 * 
	 * @param validTo
	 *            the new valid to
	 */
	public void setValidTo(Date validTo) {

		this.validTo = validTo;
	}

	
	/**
	 * Gets the survey type.
	 * 
	 * @return the survey type
	 */
	public SurveyType getSurveyType() {

		return surveyType;
	}

	/**
	 * Sets the survey type.
	 * 
	 * @param surveyType
	 *            the new survey type
	 */
	public void setSurveyType(SurveyType surveyType) {

		this.surveyType = surveyType;
	}

	/**
	 * Gets the created date.
	 * 
	 * @return the created date
	 */
	public Date getCreatedDate() {

		return createdDate;
	}

	/**
	 * Sets the created date.
	 * 
	 * @param createdDate
	 *            the new created date
	 */
	public void setCreatedDate(Date createdDate) {

		this.createdDate = createdDate;
	}

	/**
	 * Gets the updated date.
	 * 
	 * @return the updated date
	 */
	public Date getUpdatedDate() {

		return updatedDate;
	}

	/**
	 * Sets the updated date.
	 * 
	 * @param updatedDate
	 *            the new updated date
	 */
	public void setUpdatedDate(Date updatedDate) {

		this.updatedDate = updatedDate;
	}

	/**
	 * Gets the created user name.
	 * 
	 * @return the created user name
	 */
	public String getCreatedUserName() {

		return createdUserName;
	}

	/**
	 * Sets the created user name.
	 * 
	 * @param createdUserName
	 *            the new created user name
	 */
	public void setCreatedUserName(String createdUserName) {

		this.createdUserName = createdUserName;
	}

	/**
	 * Gets the updated user name.
	 * 
	 * @return the updated user name
	 */
	public String getUpdatedUserName() {

		return updatedUserName;
	}

	/**
	 * Sets the updated user name.
	 * 
	 * @param updatedUserName
	 *            the new updated user name
	 */
	public void setUpdatedUserName(String updatedUserName) {

		this.updatedUserName = updatedUserName;
	}

	/**
	 * Gets the revision number.
	 * 
	 * @return the revision number
	 */
	public long getRevisionNumber() {

		return revisionNumber;
	}

	/**
	 * Sets the revision number.
	 * 
	 * @param revisionNumber
	 *            the new revision number
	 */
	public void setRevisionNumber(long revisionNumber) {

		this.revisionNumber = revisionNumber;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public int getStatus() {

		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(int status) {

		this.status = status;
	}

	public SortedSet<SurveyQuestionMapping> getSurveyQuestionMapping() {
		return surveyQuestionMapping;
	}

	public void setSurveyQuestionMapping(SortedSet<SurveyQuestionMapping> surveyQuestionMapping) {
		this.surveyQuestionMapping = surveyQuestionMapping;
	}

	
	/**
	 * Gets the data level.
	 * 
	 * @return the data level
	 */
	public DataLevel getDataLevel() {

		return dataLevel;
	}

	/**
	 * Sets the data level.
	 * 
	 * @param dataLevel
	 *            the new data level
	 */
	public void setDataLevel(DataLevel dataLevel) {

		this.dataLevel = dataLevel;
	}

	
	/**
	 * Gets the language preferences.
	 * 
	 * @return the language preferences
	 */
	public Set<LanguagePreferences> getLanguagePreferences() {

		return languagePreferences;
	}

	/**
	 * Sets the language preferences.
	 * 
	 * @param languagePreferences
	 *            the new language preferences
	 */
	public void setLanguagePreferences(Set<LanguagePreferences> languagePreferences) {

		this.languagePreferences = languagePreferences;
	}

	public List<LanguagePreferences> getLanguagePreferencesList() {
		return languagePreferencesList;
	}

	public void setLanguagePreferencesList(List<LanguagePreferences> languagePreferencesList) {
		this.languagePreferencesList = languagePreferencesList;
	}

	

	public List<String> getQuestionCodes() {
		return questionCodes;
	}

	public void setQuestionCodes(List<String> questionCodes) {
		this.questionCodes = questionCodes;
	}

	public Set<SurveyQuestion> getVerifyQuestions() {
		return verifyQuestions;
	}

	public void setVerifyQuestions(Set<SurveyQuestion> verifyQuestions) {
		this.verifyQuestions = verifyQuestions;
	}

	

	public List<String> getVerifyQusIds() {
		List<String> ids = new ArrayList<String>();
		for (SurveyQuestion question : getVerifyQuestions()) {
			ids.add(String.valueOf(question.getId()));
		}
		return ids;
	}

	

	public Set<SurveySection> getSections() {
		return sections;
	}

	public void setSections(Set<SurveySection> sections) {
		this.sections = sections;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public SurveySection getSection() {
		return section;
	}

	public void setSection(SurveySection section) {
		this.section = section;
	}

	public List<String> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<String> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public List<String> getAvailQuestionCodes() {
		return availQuestionCodes;
	}

	public void setAvailQuestionCodes(List<String> availQuestionCodes) {
		this.availQuestionCodes = availQuestionCodes;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}