/*
 * DataLevel.java
 * Copyright (c) 2008-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Set;

import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;





public class DataLevel {

	public static final String FARMER_ORGANIZATION = "D01";
	public static final String FARMER = "D02";
	public static final String FARM = "D03";
	public static final String FARM_CROPS= "D04";
		private long id;
	private String code;
	private String name;
	private String entityName;
	private String entityAbsoluteName;
	// Idendifier from Survey Object(FarmerCropProdAnswers) i.e
	// farmerId,farmId,cocoaFarmId,cooperativeCode
	private String idendifier;
	private Set<LanguagePreferences> languagePreferences;

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
	public void setName(String name) {

		this.name = name;
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

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityAbsoluteName() {
		return entityAbsoluteName;
	}

	public void setEntityAbsoluteName(String entityAbsoluteName) {
		this.entityAbsoluteName = entityAbsoluteName;
	}

	public String getIdendifier() {
		return idendifier;
	}

	public void setIdendifier(String idendifier) {
		this.idendifier = idendifier;
	}
}
