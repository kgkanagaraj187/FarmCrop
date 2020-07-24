/*
 * ContactInfo.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.umgmt.entity;

import javax.validation.GroupSequence;

import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.Second;
import com.sourcetrace.esesw.entity.profile.Municipality;

@GroupSequence({ ContactInfo.class, First.class, Second.class })
public class ContactInfo {

	public static final int CURRENT_ADDRESS = 1;
	public static final int PERMANENT_ADDRESS = 2;
	public static final int OFFICE_ADDRESS = 3;

	private long id;
	private int type;
	private String address1;
	private String address2;
	private Municipality city;
	private String zipCode;
	private String email;
	private String phoneNumber;
	private String mobileNumber;

	/**
	 * Gets the mobile.
	 * 
	 * @return the mobile
	 */
	
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * Sets the mobile.
	 * 
	 * @param mobile the new mobile
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Gets the address1.
	 * 
	 * @return the address1
	 */


	public String getAddress1() {
		return address1;
	}

	/**
	 * Sets the address1.
	 * 
	 * @param address1 the new address1
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * Gets the address2.
	 * 
	 * @return the address2
	 */

	public String getAddress2() {
		return address2;
	}

	/**
	 * Sets the address2.
	 * 
	 * @param address2 the new address2
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
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
	 * @param type the new type
	 */
	public void setType(int type) {
		this.type = type;
	}

	
	/**
	 * Gets the zipcode.
	 * 
	 * @return the zipcode
	 */

	public String getZipCode() {
		return zipCode;
	}

	/**
	 * Sets the zipcode.
	 * 
	 * @param zipcode the new zipcode
	 */
	public void setZipCode(String zipcode) {
		this.zipCode = zipcode;
	}

	
	/**
	 * Gets the phone.
	 * 
	 * @return the phone
	 */
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the phone.
	 * 
	 * @param phone the new phone
	 */
	public void setPhoneNumber(String phone) {
		this.phoneNumber = phone;
	}

	/**
	 * Gets the email.
	 * 
	 * @return the email
	 */
	
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Sets the city.
	 * 
	 * @param city the new city
	 */
	public void setCity(Municipality city) {
		this.city = city;
	}

	/**
	 * Gets the city.
	 * 
	 * @return the city
	 */
	
	public Municipality getCity() {
		return city;
	}
}
