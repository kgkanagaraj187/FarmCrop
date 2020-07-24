/*
 * LanguagePreferences.java
 * Copyright (c) 2008-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.inspect.agrocert;

public class LanguagePreferences {

	public static enum Type {
		 CATALOGUE, FIELD,SECTION, MENU  
	};

	public static final String ENGLISH = "en";
	public static final String FRENCH = "fr";

	private long id;
	private String code;
	private String name;
	private String shortName;
	private String info;
	private String lang;
	private int type;

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
	 * Gets the short name.
	 * 
	 * @return the short name
	 */
	public String getShortName() {

		return shortName;
	}

	/**
	 * Sets the short name.
	 * 
	 * @param shortName
	 *            the new short name
	 */
	public void setShortName(String shortName) {

		this.shortName = shortName;
	}

	/**
	 * Gets the info.
	 * 
	 * @return the info
	 */
	public String getInfo() {

		return info;
	}

	/**
	 * Sets the info.
	 * 
	 * @param info
	 *            the new info
	 */
	public void setInfo(String info) {

		this.info = info;
	}

	/**
	 * Gets the lang.
	 * 
	 * @return the lang
	 */
	public String getLang() {

		return lang;
	}

	/**
	 * Sets the lang.
	 * 
	 * @param lang
	 *            the new lang
	 */
	public void setLang(String lang) {

		this.lang = lang;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public int getType() {

		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(int type) {

		this.type = type;
	}

}
