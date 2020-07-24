/*
 * PersonalInfo.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.esesw.entity.profile;

import java.util.Date;

import  com.sourcetrace.eses.util.log.Auditable;

/**
 * The Class PersonalInfo.
 * @author $Author: ganesh $
 * @version $Rev: 1173 $ $Date: 2010-04-26 15:59:42 +0530 (Mon, 26 Apr 2010) $
 */
public class PersonalInfo implements Auditable {

    public static final String MARITAL_STATUS_SINGLE="SINGLE";
    public static final String MARITAL_STATUS_MARRIED="MARRIED";
    public static final String SEX_MALE="MALE";
    public static final String SEX_FEMALE="FEMALE";
    private Date dateOfBirth;
    private String firstName;
    private long id;
    private String identityType;
    private String identityType2;
    private String lastName;
    private String maritalStatus;
    private String middleName;
    private String placeOfBirth;
    private String secondLastName;
    private String sex;

    /**
     * Gets the name.
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
     * Gets the date of birth.
     * @return the date of birth
     */
    public Date getDateOfBirth() {

        return dateOfBirth;
    }

    /**
     * Gets the first name.
     * @return the first name
     */

    public String getFirstName() {

        return firstName;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Gets the identity type.
     * @return the identity type
     */
    public String getIdentityType() {

        return identityType;
    }

    /**
     * Gets the identity type2.
     * @return the identity type2
     */
    public String getIdentityType2() {

        return identityType2;
    }

    /**
     * Gets the last nname.
     * @return the last nname
     */
    public String getLastName() {

        return lastName;
    }

    /**
     * Gets the marital status.
     * @return the marital status
     */
    public String getMaritalStatus() {

        return maritalStatus;
    }

    /**
     * Gets the middle name.
     * @return the middle name
     */
    public String getMiddleName() {

        return middleName;
    }

    /**
     * Gets the place of birth.
     * @return the place of birth
     */
    public String getPlaceOfBirth() {

        return placeOfBirth;
    }

    /**
     * Gets the secondlast name.
     * @return the secondlast name
     */
    public String getSecondLastName() {

        return secondLastName;
    }

    /**
     * Gets the sex.
     * @return the sex
     */
    public String getSex() {

        return sex;
    }

    /**
     * Sets the date of birth.
     * @param dateOfBirth the new date of birth
     */
    public void setDateOfBirth(Date dateOfBirth) {

        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Sets the first name.
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Sets the identity type.
     * @param identityType the new identity type
     */
    public void setIdentityType(String identityType) {

        this.identityType = identityType;
    }

    /**
     * Sets the identity type2.
     * @param identityType2 the new identity type2
     */
    public void setIdentityType2(String identityType2) {

        this.identityType2 = identityType2;
    }

    /**
     * Sets the last nname.
     * @param lastName the new last nname
     */
    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    /**
     * Sets the marital status.
     * @param maritalStatus the new marital status
     */
    public void setMaritalStatus(String maritalStatus) {

        this.maritalStatus = maritalStatus;
    }

    /**
     * Sets the middle name.
     * @param middleName the new middle name
     */
    public void setMiddleName(String middleName) {

        this.middleName = middleName;
    }

    /**
     * Sets the place of birth.
     * @param placeOfBirth the new place of birth
     */
    public void setPlaceOfBirth(String placeOfBirth) {

        this.placeOfBirth = placeOfBirth;
    }

    /**
     * Sets the secondlast name.
     * @param secondlastName the new secondlast name
     */
    public void setSecondLastName(String secondlastName) {

        this.secondLastName = secondlastName;
    }

    /**
     * Sets the sex.
     * @param sex the new sex
     */
    public void setSex(String sex) {

        this.sex = sex;
    }
}
