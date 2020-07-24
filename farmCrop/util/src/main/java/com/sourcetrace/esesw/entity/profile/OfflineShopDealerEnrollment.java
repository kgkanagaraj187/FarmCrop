/*
 * OfflineShopDealerEnrollment.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

public class OfflineShopDealerEnrollment {

    public static final String SHOP_DEALER_ENROLLMENT = "307";
    public static final String SHOP_DEALER_BIOMETRIC_UPLOAD = "323";

    private long id;
    private String shopDealerId;
    private String agentId;
    private String dateOfBirth;
    private String firstName;
    private String lastName;
    private String gender;
    private String cityCode;
    private String address;
    private String email;
    private String mobileNumber;
    private String phoneNumber;
    private String pinCode;
    private String shopName;
    private String contactPerson;
    private String contactPersonFirstName;
    private String contactPersonLastName;
    private String cardId;
    private String enrolledDate;
    private String transactionType;
    private String statusMsg;
    private int statusCode;
    private byte[] photo;
    private byte[] fingerPrint;

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
     * Gets the shop dealer id.
     * @return the shop dealer id
     */
    public String getShopDealerId() {

        return shopDealerId;
    }

    /**
     * Sets the shop dealer id.
     * @param shopDealerId the new shop dealer id
     */
    public void setShopDealerId(String shopDealerId) {

        this.shopDealerId = shopDealerId;
    }

    /**
     * Gets the date of birth.
     * @return the date of birth
     */
    public String getDateOfBirth() {

        return dateOfBirth;
    }

    /**
     * Sets the date of birth.
     * @param dateOfBirth the new date of birth
     */
    public void setDateOfBirth(String dateOfBirth) {

        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the first name.
     * @return the first name
     */
    public String getFirstName() {

        return firstName;
    }

    /**
     * Sets the first name.
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     * @return the last name
     */
    public String getLastName() {

        return lastName;
    }

    /**
     * Sets the last name.
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    /**
     * Gets the gender.
     * @return the gender
     */
    public String getGender() {

        return gender;
    }

    /**
     * Sets the gender.
     * @param gender the new gender
     */
    public void setGender(String gender) {

        this.gender = gender;
    }

    /**
     * Gets the city code.
     * @return the city code
     */
    public String getCityCode() {

        return cityCode;
    }

    /**
     * Sets the city code.
     * @param cityCode the new city code
     */
    public void setCityCode(String cityCode) {

        this.cityCode = cityCode;
    }

    /**
     * Gets the address.
     * @return the address
     */
    public String getAddress() {

        return address;
    }

    /**
     * Sets the address.
     * @param address the new address
     */
    public void setAddress(String address) {

        this.address = address;
    }

    /**
     * Gets the email.
     * @return the email
     */
    public String getEmail() {

        return email;
    }

    /**
     * Sets the email.
     * @param email the new email
     */
    public void setEmail(String email) {

        this.email = email;
    }

    /**
     * Gets the mobile number.
     * @return the mobile number
     */
    public String getMobileNumber() {

        return mobileNumber;
    }

    /**
     * Sets the mobile number.
     * @param mobileNumber the new mobile number
     */
    public void setMobileNumber(String mobileNumber) {

        this.mobileNumber = mobileNumber;
    }

    /**
     * Gets the phone number.
     * @return the phone number
     */
    public String getPhoneNumber() {

        return phoneNumber;
    }

    /**
     * Sets the phone number.
     * @param phoneNumber the new phone number
     */
    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the pin code.
     * @return the pin code
     */
    public String getPinCode() {

        return pinCode;
    }

    /**
     * Sets the pin code.
     * @param pinCode the new pin code
     */
    public void setPinCode(String pinCode) {

        this.pinCode = pinCode;
    }

    /**
     * Gets the shop name.
     * @return the shop name
     */
    public String getShopName() {

        return shopName;
    }

    /**
     * Sets the shop name.
     * @param shopName the new shop name
     */
    public void setShopName(String shopName) {

        this.shopName = shopName;
    }

    /**
     * Gets the contact person.
     * @return the contact person
     */
    public String getContactPerson() {

        return contactPerson;
    }

    /**
     * Sets the contact person.
     * @param contactPerson the new contact person
     */
    public void setContactPerson(String contactPerson) {

        this.contactPerson = contactPerson;
    }

    /**
     * Gets the contact person first name.
     * @return the contact person first name
     */
    public String getContactPersonFirstName() {

        return contactPersonFirstName;
    }

    /**
     * Sets the contact person first name.
     * @param contactPersonFirstName the new contact person first name
     */
    public void setContactPersonFirstName(String contactPersonFirstName) {

        this.contactPersonFirstName = contactPersonFirstName;
    }

    /**
     * Gets the contact person last name.
     * @return the contact person last name
     */
    public String getContactPersonLastName() {

        return contactPersonLastName;
    }

    /**
     * Sets the contact person last name.
     * @param contactPersonLastName the new contact person last name
     */
    public void setContactPersonLastName(String contactPersonLastName) {

        this.contactPersonLastName = contactPersonLastName;
    }

    /**
     * Gets the card id.
     * @return the card id
     */
    public String getCardId() {

        return cardId;
    }

    /**
     * Sets the card id.
     * @param cardId the new card id
     */
    public void setCardId(String cardId) {

        this.cardId = cardId;
    }

    /**
     * Gets the status code.
     * @return the status code
     */
    public int getStatusCode() {

        return statusCode;
    }

    /**
     * Sets the status code.
     * @param statusCode the new status code
     */
    public void setStatusCode(int statusCode) {

        this.statusCode = statusCode;
    }

    /**
     * Gets the status msg.
     * @return the status msg
     */
    public String getStatusMsg() {

        return statusMsg;
    }

    /**
     * Sets the status msg.
     * @param statusMsg the new status msg
     */
    public void setStatusMsg(String statusMsg) {

        this.statusMsg = statusMsg;
    }

    /**
     * Gets the photo.
     * @return the photo
     */
    public byte[] getPhoto() {

        return photo;
    }

    /**
     * Sets the photo.
     * @param photo the new photo
     */
    public void setPhoto(byte[] photo) {

        this.photo = photo;
    }

    /**
     * Gets the finger print.
     * @return the finger print
     */
    public byte[] getFingerPrint() {

        return fingerPrint;
    }

    /**
     * Sets the finger print.
     * @param fingerPrint the new finger print
     */
    public void setFingerPrint(byte[] fingerPrint) {

        this.fingerPrint = fingerPrint;
    }

    /**
     * Sets the enrolled date.
     * @param enrolledDate the new enrolled date
     */
    public void setEnrolledDate(String enrolledDate) {

        this.enrolledDate = enrolledDate;
    }

    /**
     * Gets the enrolled date.
     * @return the enrolled date
     */
    public String getEnrolledDate() {

        return enrolledDate;
    }

    /**
     * Sets the agent id.
     * @param agentId the new agent id
     */
    public void setAgentId(String agentId) {

        this.agentId = agentId;
    }

    /**
     * Gets the agent id.
     * @return the agent id
     */
    public String getAgentId() {

        return agentId;
    }

    /**
     * Sets the transaction type.
     * @param transactionType the new transaction type
     */
    public void setTransactionType(String transactionType) {

        this.transactionType = transactionType;
    }

    /**
     * Gets the transaction type.
     * @return the transaction type
     */
    public String getTransactionType() {

        return transactionType;
    }

}
