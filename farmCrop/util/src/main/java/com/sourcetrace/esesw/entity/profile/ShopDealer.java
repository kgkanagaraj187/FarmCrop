/*
 * ShopDealer.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.util.entity.ImageInfo;

public class ShopDealer {

	public static final String SAME_AS_SHOP_DEALER = "1";
	public static final String NOT_SAME_AS_SHOP_DEALER="0";
	public static final String SEX_MALE = "MALE";
	public static final String SEX_FEMALE = "FEMALE";
	
	public static final int SHOP_DEALER_SEQ_RANGE = 1000;
    public static final int SHOP_DEALER_RESERVE_INDEX = 100;
    public static final int SHOP_DEALER_ID_LENGTH = 6;
    public static final int SHOP_DEALER_ID_MAX_RANGE = 999999;
    public static final String SHOP_DEALER_PREFIX = "S";

	private long id;
	private String shopDealerId;
	private Date dateOfBirth;
	private String firstName;
	private String lastName;
	private String gender;
	private Municipality city;
	private ImageInfo imageInfo;
	private String address;
	private String email;
	private String mobileNumber;
	private String phoneNumber;
	private String pinCode;
	private String shopName;
	private boolean contactPerson;
	private String contactPersonFirstName;
	private String contactPersonLastName;
	private long revisionNo;

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
	 * Sets the shop dealer id.
	 * 
	 * @param shopDealerId
	 *            the new shop dealer id
	 */
	public void setShopDealerId(String shopDealerId) {
		this.shopDealerId = shopDealerId;
	}

	/**
	 * Gets the shop dealer id.
	 * 
	 * @return the shop dealer id
	 */
	@Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.shopDealerId")
	@NotEmpty(message = "empty.shopDealerId")
	public String getShopDealerId() {
		return shopDealerId;
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
	 * Sets the date of birth.
	 * 
	 * @param dateOfBirth
	 *            the new date of birth
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Gets the first name.
	 * 
	 * @return the first name
	 */
	@Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.firstName")
	@NotEmpty(message = "empty.firstName")
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
	@Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.lastName")
	@NotEmpty(message = "empty.lastName")
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
	 * Gets the gender.
	 * 
	 * @return the gender
	 */
	@NotEmpty(message = "empty.gender")
	public String getGender() {
		return gender;
	}

	/**
	 * Sets the gender.
	 * 
	 * @param gender
	 *            the new gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Sets the city.
	 * 
	 * @param city
	 *            the new city
	 */
	public void setCity(Municipality city) {
		this.city = city;
	}

	/**
	 * Gets the city.
	 * 
	 * @return the city
	 */
	@NotNull(message = "empty.city")
	public Municipality getCity() {
		return city;
	}

	/**
	 * Gets the image info.
	 * 
	 * @return the image info
	 */
	public ImageInfo getImageInfo() {
		return imageInfo;
	}

	/**
	 * Sets the image info.
	 * 
	 * @param imageInfo
	 *            the new image info
	 */
	public void setImageInfo(ImageInfo imageInfo) {
		this.imageInfo = imageInfo;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	@Pattern(regexp = "[^@#!$%^&*_]+$", message = "pattern.address")
	@NotEmpty(message = "empty.address")
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
	 * Gets the email.
	 * 
	 * @return the email
	 */
	@Email(message = "pattern.email")
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email
	 *            the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the mobile number.
	 * 
	 * @return the mobile number
	 */
	@Pattern(regexp = "[0-9]+$", message = "pattern.mobileNumber")
	@NotEmpty(message = "empty.mobileNumber")
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * Sets the mobile number.
	 * 
	 * @param mobileNumber
	 *            the new mobile number
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Gets the phone number.
	 * 
	 * @return the phone number
	 */
	@Pattern(regexp = "[0-9]*", message = "pattern.phoneNumber")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the phone number.
	 * 
	 * @param phoneNumber
	 *            the new phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Gets the pin code.
	 * 
	 * @return the pin code
	 */
	@Pattern(regexp = "[0-9]+$", message = "pattern.pinCode")
	@NotEmpty(message = "empty.pinCode")
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
	 * Gets the shop name.
	 * 
	 * @return the shop name
	 */
	@Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.shopName")
	@NotEmpty(message = "empty.shopName")
	public String getShopName() {
		return shopName;
	}

	/**
	 * Sets the shop name.
	 * 
	 * @param shopName
	 *            the new shop name
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void setRevisionNo(long revisionNo) {
		this.revisionNo = revisionNo;
	}

	public long getRevisionNo() {
		return revisionNo;
	}

	/**
	 * Checks if is contact person.
	 * 
	 * @return true, if is contact person
	 */
	public boolean isContactPerson() {
		return contactPerson;
	}

	/**
	 * Sets the contact person.
	 * 
	 * @param contactPerson
	 *            the new contact person
	 */
	public void setContactPerson(boolean contactPerson) {
		this.contactPerson = contactPerson;
	}

	/**
	 * Sets the contact person first name.
	 * 
	 * @param contactPersonFirstName
	 *            the new contact person first name
	 */
	public void setContactPersonFirstName(String contactPersonFirstName) {
		this.contactPersonFirstName = contactPersonFirstName;
	}

	/**
	 * Gets the contact person first name.
	 * 
	 * @return the contact person first name
	 */
	@Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.contactPersonFirstName")
	@NotEmpty(message = "empty.contactPersonFirstName")
	public String getContactPersonFirstName() {
		return contactPersonFirstName;
	}

	/**
	 * Sets the contact person last name.
	 * 
	 * @param contactPersonLastName
	 *            the new contact person last name
	 */
	public void setContactPersonLastName(String contactPersonLastName) {
		this.contactPersonLastName = contactPersonLastName;
	}

	/**
	 * Gets the contact person last name.
	 * 
	 * @return the contact person last name
	 */
	@Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.contactPersonLastName")
	@NotEmpty(message = "empty.contactPersonLastName")
	public String getContactPersonLastName() {
		return contactPersonLastName;
	}

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

		return name.toString();
	}
}
