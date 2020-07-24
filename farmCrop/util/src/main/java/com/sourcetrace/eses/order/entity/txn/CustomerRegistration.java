/*
 * CustomerRegistration.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.order.entity.txn;

import java.util.Date;

public class CustomerRegistration {

	private long id;
	private String customerId;
	private String firstName;
	private String lastName;
	private String storeName;
	private String address;
	private String location;
	private String pinCode;
	private String contactNumber;
	private Date enrolledDate;
	private String enrolledAgentId;
	private String enrolledAgentName;
	private String enrolledDeviceId;
	private String enrolledDeviceName;
	private String enrolledBranchId;
	private String enrolledBranchName;

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	/**
	 * Gets the customer name.
	 * 
	 * @return the customer name
	 */
	public String getCustomerName() {

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
	 * Gets the customer id.
	 * 
	 * @return the customer id
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * Sets the customer id.
	 * 
	 * @param customerId
	 *            the new customer id
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	 * Sets the first name.
	 * 
	 * @param firstName
	 *            the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 * 
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 * 
	 * @param lastName
	 *            the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 * 
	 * @param address
	 *            the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location
	 *            the new location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Gets the pin code.
	 * 
	 * @return the pin code
	 */
	public String getPinCode() {
		return pinCode;
	}

	/**
	 * Sets the pin code.
	 * 
	 * @param pinCode
	 *            the new pin code
	 */
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	/**
	 * Gets the contact number.
	 * 
	 * @return the contact number
	 */
	public String getContactNumber() {
		return contactNumber;
	}

	/**
	 * Sets the contact number.
	 * 
	 * @param contactNumber
	 *            the new contact number
	 */
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	/**
	 * Gets the enrolled date.
	 * 
	 * @return the enrolled date
	 */
	public Date getEnrolledDate() {
		return enrolledDate;
	}

	/**
	 * Sets the enrolled date.
	 * 
	 * @param enrolledDate
	 *            the new enrolled date
	 */
	public void setEnrolledDate(Date enrolledDate) {
		this.enrolledDate = enrolledDate;
	}

	/**
	 * Sets the enrolled agent id.
	 * 
	 * @param enrolledAgentId
	 *            the new enrolled agent id
	 */
	public void setEnrolledAgentId(String enrolledAgentId) {
		this.enrolledAgentId = enrolledAgentId;
	}

	/**
	 * Gets the enrolled agent id.
	 * 
	 * @return the enrolled agent id
	 */
	public String getEnrolledAgentId() {
		return enrolledAgentId;
	}

	/**
	 * Gets the enrolled agent name.
	 * 
	 * @return the enrolled agent name
	 */
	public String getEnrolledAgentName() {
		return enrolledAgentName;
	}

	/**
	 * Sets the enrolled agent name.
	 * 
	 * @param enrolledAgentName
	 *            the new enrolled agent name
	 */
	public void setEnrolledAgentName(String enrolledAgentName) {
		this.enrolledAgentName = enrolledAgentName;
	}

	/**
	 * Gets the enrolled device id.
	 * 
	 * @return the enrolled device id
	 */
	public String getEnrolledDeviceId() {
		return enrolledDeviceId;
	}

	/**
	 * Sets the enrolled device id.
	 * 
	 * @param enrolledDeviceId
	 *            the new enrolled device id
	 */
	public void setEnrolledDeviceId(String enrolledDeviceId) {
		this.enrolledDeviceId = enrolledDeviceId;
	}

	/**
	 * Gets the enrolled device name.
	 * 
	 * @return the enrolled device name
	 */
	public String getEnrolledDeviceName() {
		return enrolledDeviceName;
	}

	/**
	 * Sets the enrolled device name.
	 * 
	 * @param enrolledDeviceName
	 *            the new enrolled device name
	 */
	public void setEnrolledDeviceName(String enrolledDeviceName) {
		this.enrolledDeviceName = enrolledDeviceName;
	}

	/**
	 * Gets the enrolled branch id.
	 * 
	 * @return the enrolled branch id
	 */
	public String getEnrolledBranchId() {
		return enrolledBranchId;
	}

	/**
	 * Sets the enrolled branch id.
	 * 
	 * @param enrolledBranchId
	 *            the new enrolled branch id
	 */
	public void setEnrolledBranchId(String enrolledBranchId) {
		this.enrolledBranchId = enrolledBranchId;
	}

	/**
	 * Gets the enrolled branch name.
	 * 
	 * @return the enrolled branch name
	 */
	public String getEnrolledBranchName() {
		return enrolledBranchName;
	}

	/**
	 * Sets the enrolled branch name.
	 * 
	 * @param enrolledBranchName
	 *            the new enrolled branch name
	 */
	public void setEnrolledBranchName(String enrolledBranchName) {
		this.enrolledBranchName = enrolledBranchName;
	}

}
