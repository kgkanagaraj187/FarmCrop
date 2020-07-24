/*
 * Season.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import com.sourcetrace.eses.util.StringUtil;

/**
 * The Class Season.
 */
public class Season {

    private long id;
    private String code;
    private String name;
    private String year;
    private long revisionNo;

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * Sets the year.
     * @param year the new year
     */
    public void setYear(String year) {

        this.year = year;
    }

    /**
     * Gets the year.
     * @return the year
     */
    public String getYear() {

        return year;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Gets the code.
     * @return the code
     */
    public String getCode() {

        return code;
    }

    /**
     * Gets the season name and code.
     * @return the season name and code
     */
    public String getSeasonNameAndCode() {

        StringBuffer sb = new StringBuffer();
        if (!StringUtil.isEmpty(name)) {
            sb.append(name).append(" - ");
        }
        if (!StringUtil.isEmpty(code)) {
            sb.append(code);
        }
        return sb.toString();
    }

    /**
     * Gets the revision no.
     * @return the revision no
     */
    public long getRevisionNo() {

        return revisionNo;
    }

    /**
     * Sets the revision no.
     * @param revisionNo the new revision no
     */
    public void setRevisionNo(long revisionNo) {

        this.revisionNo = revisionNo;
    }

}
