/*
 * PersonalInfo.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.umgmt.entity;

import java.io.File;
import java.util.Date;

import javax.validation.GroupSequence;

import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.Second;

@GroupSequence({ PersonalInfo.class, First.class, Second.class })
public class PersonalInfo {

	public static final String MARITAL_STATUS_SINGLE = "SINGLE";
	public static final String MARITAL_STATUS_MARRIED = "MARRIED";
	public static final String SEX_MALE = "MALE";
	public static final String SEX_FEMALE = "FEMALE";
	private long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String secondLastName;
	private String identityType;
	private String identityNumber;
	private String sex;
	private Date dateOfBirth;
	private String placeOfBirth;
	private String maritalStatus;
	private byte[] image;
	//Transient variable
	private String filterStatus;
	private int status;
	private File userImage;
	private String userImageString;

	

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {

		StringBuffer name = new StringBuffer();
		if (firstName != null) {
			name.append(firstName);
			name.append(" ");
		}
		if (lastName != null) {
			name.append(lastName);
		}
		if (middleName != null) {
			name.append(middleName);
			name.append(" ");
		}
		return name.toString();
	}

	/**
	 * Gets the agent name.
	 * 
	 * @return the agent name
	 */
	public String getAgentName() {

		StringBuffer name = new StringBuffer();
		if (firstName != null) {
			name.append(firstName);
			name.append(" ");
		}
		if (lastName != null) {
			name.append(lastName);
		}
		return name.toString();
	}

	/**
	 * Gets the date of birth.
	 * 
	 * @return the date of birth
	 */
	public Date getDateOfBirth() {

		return dateOfBirth;
	}

	/**
	 * Gets the first name.
	 * 
	 * @return the first name
	 */
	public String getFirstName() {

		return firstName;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Gets the identity type.
	 * 
	 * @return the identity type
	 */

	public String getIdentityType() {

		return identityType;
	}

	/**
	 * Gets the last name.
	 * 
	 * @return the last name
	 */

	/**
	 * Gets the last nname.
	 * 
	 * @return the last nname
	 */
	public String getLastName() {

		return lastName;
	}

	/**
	 * Gets the marital status.
	 * 
	 * @return the marital status
	 */
	public String getMaritalStatus() {

		return maritalStatus;
	}

	/**
	 * Gets the middle name.
	 * 
	 * @return the middle name
	 */
	public String getMiddleName() {

		return middleName;
	}

	/**
	 * Gets the place of birth.
	 * 
	 * @return the place of birth
	 */
	public String getPlaceOfBirth() {

		return placeOfBirth;
	}

	/**
	 * Gets the second last name.
	 * 
	 * @return the second last name
	 */
	public String getSecondLastName() {

		return secondLastName;
	}

	/**
	 * Gets the sex.
	 * 
	 * @return the sex
	 */
	public String getSex() {

		return sex;
	}

	/**
	 * Sets the date of birth.
	 * 
	 * @param dateOfBirth
	 *            the new date of birth
	 */
	public void setDateOfBirth(Date dateOfBirth) {

		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Sets the first name.
	 * 
	 * @param firstName
	 *            the new first name
	 */
	public void setFirstName(String firstName) {

		this.firstName = firstName;
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
	 * Sets the identity type.
	 * 
	 * @param identityType
	 *            the new identity type
	 */
	public void setIdentityType(String identityType) {

		this.identityType = identityType;
	}

	/**
	 * Sets the last name.
	 * 
	 * @param lastName
	 *            the new last name
	 */

	/**
	 * Sets the last nname.
	 * 
	 * @param lastName
	 *            the new last nname
	 */
	public void setLastName(String lastName) {

		this.lastName = lastName;
	}

	/**
	 * Sets the marital status.
	 * 
	 * @param maritalStatus
	 *            the new marital status
	 */
	public void setMaritalStatus(String maritalStatus) {

		this.maritalStatus = maritalStatus;
	}

	/**
	 * Sets the middle name.
	 * 
	 * @param middleName
	 *            the new middle name
	 */
	public void setMiddleName(String middleName) {

		this.middleName = middleName;
	}

	/**
	 * Sets the place of birth.
	 * 
	 * @param placeOfBirth
	 *            the new place of birth
	 */
	public void setPlaceOfBirth(String placeOfBirth) {

		this.placeOfBirth = placeOfBirth;
	}

	/**
	 * Sets the second last name.
	 * 
	 * @param secondlastName
	 *            the new second last name
	 */
	public void setSecondLastName(String secondlastName) {

		this.secondLastName = secondlastName;
	}

	/**
	 * Sets the sex.
	 * 
	 * @param sex
	 *            the new sex
	 */
	public void setSex(String sex) {

		this.sex = sex;
	}

	/**
	 * Sets the identity number.
	 * 
	 * @param identityNumber
	 *            the new identity number
	 */
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	/**
	 * Gets the identity number.
	 * 
	 * @return the identity number
	 */
	public String getIdentityNumber() {
		return identityNumber;
	}

	public String getFilterStatus() {
		return filterStatus;
	}

	public void setFilterStatus(String filterStatus) {
		this.filterStatus = filterStatus;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public File getUserImage() {
		return userImage;
	}

	public void setUserImage(File userImage) {
		this.userImage = userImage;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public String getUserImageString() {
		return userImageString;
	}

	public void setUserImageString(String userImageString) {
		this.userImageString = userImageString;
	}
}
