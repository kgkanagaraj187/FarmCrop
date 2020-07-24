/*
 * OfflineFarmerEnrollment.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Set;

public class OfflineFarmerEnrollment {

    public static final String FARMER_ENROLLMENT = "308";
    public static final String FARMER_BIOMETRIC_UPLOAD = "325";
    public static final Integer FAILED = 1;
    public static final Integer PENDING = 2;

    private long id;
    private String farmerId;
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
    private String noOfFamilyMembers;
    private String villageCode;
    private String cardId;
    private String acctNumber;
    private int statusCode;
    private String statusMsg;
    private byte[] photo;
    private byte[] fingerPrint;
    private Set<OfflineFarmEnrollment> farms;
    private Set<OfflineFarmCropsEnrollment> farmCrops;
    private String enrolledDate;
    private String enrolledAgentId;
    private String transactionType;
    private String latitude;
    private String longitude;
    private String photoCaptureTime;
    private String postOffice;
    private String dateOfJoining;
    private String farmerCode;
    private String samithiCode;

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
     * Gets the farmer id.
     * @return the farmer id
     */
    public String getFarmerId() {

        return farmerId;
    }

    /**
     * Sets the farmer id.
     * @param farmerId the new farmer id
     */
    public void setFarmerId(String farmerId) {

        this.farmerId = farmerId;
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
     * Gets the no of family members.
     * @return the no of family members
     */
    public String getNoOfFamilyMembers() {

        return noOfFamilyMembers;
    }

    /**
     * Sets the no of family members.
     * @param noOfFamilyMembers the new no of family members
     */
    public void setNoOfFamilyMembers(String noOfFamilyMembers) {

        this.noOfFamilyMembers = noOfFamilyMembers;
    }

    /**
     * Gets the village code.
     * @return the village code
     */
    public String getVillageCode() {

        return villageCode;
    }

    /**
     * Sets the village code.
     * @param villageCode the new village code
     */
    public void setVillageCode(String villageCode) {

        this.villageCode = villageCode;
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
     * Gets the acct number.
     * @return the acct number
     */
    public String getAcctNumber() {

        return acctNumber;
    }

    /**
     * Sets the acct number.
     * @param acctNumber the new acct number
     */
    public void setAcctNumber(String acctNumber) {

        this.acctNumber = acctNumber;
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
     * Sets the photo.
     * @param photo the new photo
     */
    public void setPhoto(byte[] photo) {

        this.photo = photo;
    }

    /**
     * Gets the photo.
     * @return the photo
     */
    public byte[] getPhoto() {

        return photo;
    }

    /**
     * Sets the finger print.
     * @param fingerPrint the new finger print
     */
    public void setFingerPrint(byte[] fingerPrint) {

        this.fingerPrint = fingerPrint;
    }

    /**
     * Gets the finger print.
     * @return the finger print
     */
    public byte[] getFingerPrint() {

        return fingerPrint;
    }

    /**
     * Gets the farms.
     * @return the farms
     */
    public Set<OfflineFarmEnrollment> getFarms() {

        return farms;
    }

    /**
     * Sets the farms.
     * @param farms the new farms
     */
    public void setFarms(Set<OfflineFarmEnrollment> farms) {

        this.farms = farms;
    }

    /**
     * Gets the farm crops.
     * @return the farm crops
     */
    public Set<OfflineFarmCropsEnrollment> getFarmCrops() {

        return farmCrops;
    }

    /**
     * Sets the farm crops.
     * @param farmCrops the new farm crops
     */
    public void setFarmCrops(Set<OfflineFarmCropsEnrollment> farmCrops) {

        this.farmCrops = farmCrops;
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
     * Gets the enrolled agent id.
     * @return the enrolled agent id
     */
    public String getEnrolledAgentId() {

        return enrolledAgentId;
    }

    /**
     * Sets the enrolled agent id.
     * @param enrolledAgentId the new enrolled agent id
     */
    public void setEnrolledAgentId(String enrolledAgentId) {

        this.enrolledAgentId = enrolledAgentId;
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

    /**
     * Gets the latitude.
     * @return the latitude
     */
    public String getLatitude() {

        return latitude;
    }

    /**
     * Sets the latitude.
     * @param latitude the new latitude
     */
    public void setLatitude(String latitude) {

        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     * @return the longitude
     */
    public String getLongitude() {

        return longitude;
    }

    /**
     * Sets the longitude.
     * @param longitude the new longitude
     */
    public void setLongitude(String longitude) {

        this.longitude = longitude;
    }

    /**
     * Gets the photo capture time.
     * @return the photo capture time
     */
    public String getPhotoCaptureTime() {

        return photoCaptureTime;
    }

    /**
     * Sets the photo capture time.
     * @param photoCaptureTime the new photo capture time
     */
    public void setPhotoCaptureTime(String photoCaptureTime) {

        this.photoCaptureTime = photoCaptureTime;
    }

    /**
     * Sets the post office.
     * @param postOffice the new post office
     */
    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }

    /**
     * Gets the post office.
     * @return the post office
     */
    public String getPostOffice() {
        return postOffice;
    }

    /**
     * Sets the date of joining.
     * @param dateOfJoining the new date of joining
     */
    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    /**
     * Gets the date of joining.
     * @return the date of joining
     */
    public String getDateOfJoining() {
        return dateOfJoining;
    }

    /**
     * Sets the farmer code.
     * @param farmerCode the new farmer code
     */
    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    /**
     * Gets the farmer code.
     * @return the farmer code
     */
    public String getFarmerCode() {
        return farmerCode;
    }

    /**
     * Sets the samithi code.
     * @param samithiCode the new samithi code
     */
    public void setSamithiCode(String samithiCode) {
        this.samithiCode = samithiCode;
    }

    /**
     * Gets the samithi code.
     * @return the samithi code
     */
    public String getSamithiCode() {
        return samithiCode;
    }

}
