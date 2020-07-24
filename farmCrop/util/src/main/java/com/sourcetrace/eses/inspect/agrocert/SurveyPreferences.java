/*
 * SurveyPreferences.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.inspect.agrocert;

import java.util.Date;

import com.sourcetrace.esesw.entity.profile.Country;

public class SurveyPreferences {

    public static enum typeMaster {
        Conversion, Constants
    }

    public static enum statusMaster {
        Inactive, Active
    }

    private long id;
    private int type;
    private Country country;
    private String code;
    private String name;
    private String convertionCode;
    private String convertionName;
    private String value;
    private Date createdDate;
    private Date updatedDate;
    private String createdUserName;
    private String updatedUserName;
    private long revisionNumber;
    private int status;

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
     * Gets the type.
     * @return the type
     */
    public int getType() {

        return type;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(int type) {

        this.type = type;
    }

    /**
     * Gets the country.
     * @return the country
     */
    public Country getCountry() {

        return country;
    }

    /**
     * Sets the country.
     * @param country the new country
     */
    public void setCountry(Country country) {

        this.country = country;
    }

    /**
     * Gets the code.
     * @return the code
     */
    public String getCode() {

        return code;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the convertion code.
     * @return the convertion code
     */
    public String getConvertionCode() {

        return convertionCode;
    }

    /**
     * Sets the convertion code.
     * @param convertionCode the new convertion code
     */
    public void setConvertionCode(String convertionCode) {

        this.convertionCode = convertionCode;
    }

    /**
     * Gets the convertion name.
     * @return the convertion name
     */
    public String getConvertionName() {

        return convertionName;
    }

    /**
     * Sets the convertion name.
     * @param convertionName the new convertion name
     */
    public void setConvertionName(String convertionName) {

        this.convertionName = convertionName;
    }

    /**
     * Gets the value.
     * @return the value
     */
    public String getValue() {

        return value;
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(String value) {

        this.value = value;
    }

    /**
     * Gets the created date.
     * @return the created date
     */
    public Date getCreatedDate() {

        return createdDate;
    }

    /**
     * Sets the created date.
     * @param createdDate the new created date
     */
    public void setCreatedDate(Date createdDate) {

        this.createdDate = createdDate;
    }

    /**
     * Gets the updated date.
     * @return the updated date
     */
    public Date getUpdatedDate() {

        return updatedDate;
    }

    /**
     * Sets the updated date.
     * @param updatedDate the new updated date
     */
    public void setUpdatedDate(Date updatedDate) {

        this.updatedDate = updatedDate;
    }

    /**
     * Gets the created user name.
     * @return the created user name
     */
    public String getCreatedUserName() {

        return createdUserName;
    }

    /**
     * Sets the created user name.
     * @param createdUserName the new created user name
     */
    public void setCreatedUserName(String createdUserName) {

        this.createdUserName = createdUserName;
    }

    /**
     * Gets the updated user name.
     * @return the updated user name
     */
    public String getUpdatedUserName() {

        return updatedUserName;
    }

    /**
     * Sets the updated user name.
     * @param updatedUserName the new updated user name
     */
    public void setUpdatedUserName(String updatedUserName) {

        this.updatedUserName = updatedUserName;
    }

    /**
     * Gets the revision number.
     * @return the revision number
     */
    public long getRevisionNumber() {

        return revisionNumber;
    }

    /**
     * Sets the revision number.
     * @param revisionNumber the new revision number
     */
    public void setRevisionNumber(long revisionNumber) {

        this.revisionNumber = revisionNumber;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public int getStatus() {

        return status;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(int status) {

        this.status = status;
    }

}
