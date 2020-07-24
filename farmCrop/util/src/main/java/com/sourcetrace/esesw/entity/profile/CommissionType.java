/*
 * CommissionType.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.esesw.entity.profile;

/**
 * The Class CommissionType.
 * @author $Author: aravind $
 * @version $Rev: 618 $ $Date: 2010-01-03 16:40:55 +0530 (Sun, 03 Jan 2010) $
 */
public class CommissionType {

    public static final String FIXED = "fixed";
    public static final String PERCENT = "percentage";
    public static final String CALCULATED = "calculated";
    private String code;
    private Currency currencyType;
    private String description;
    private long id;
    private String name;

    /**
     * Gets the code.
     * @return the code
     */
    public String getCode() {

        return code;
    }

    /**
     * Gets the currency type.
     * @return the currency type
     */
    public Currency getCurrencyType() {

        return currencyType;
    }

    /**
     * Gets the description.
     * @return the description
     */
    public String getDescription() {

        return description;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Sets the currency type.
     * @param currencyType the new currency type
     */
    public void setCurrencyType(Currency currencyType) {

        this.currencyType = currencyType;
    }

    /**
     * Sets the description.
     * @param description the new description
     */
    public void setDescription(String description) {

        this.description = description;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * To string.
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        String str = (name == null || name.equals("")) ? code : name;
        return str;
    }
}
