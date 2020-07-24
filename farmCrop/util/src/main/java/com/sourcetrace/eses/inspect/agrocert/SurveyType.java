/*
 * SurveyType.java
 * Copyright (c) 2010-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.inspect.agrocert;

import java.util.Date;

public class SurveyType {

	public static final long GENERAL = 1;
	public static final long CERTIFICATION = 2;
	public static final long TRAINING = 3;
	public static final long INTERNAL_PROGRAMS = 4;
	public static final long YIELD_MEASUREMENT = 5;
	public static final long FAMILY_PROFILE = 6;
	public static final long SOIL_PROFILE = 7;
	public static final long DIAGNOSTIC = 8;
	public static final long FARMER_FAMILY_MEMBER = 9;
	private long id;
	private String name;
	private Date createdDate;
	private Date updatedDate;
	private String createdUserName;
	private String updatedUserName;
	private long revisionNumber;

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

}
