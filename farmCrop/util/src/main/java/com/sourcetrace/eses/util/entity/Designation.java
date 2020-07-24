/*
 * Designation.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

package com.sourcetrace.eses.util.entity;

import javax.validation.GroupSequence;

import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.Second;
import com.sourcetrace.eses.util.StringUtil;

/**
 * @author PANNEER
 */
@GroupSequence({ Designation.class, First.class, Second.class })
public class Designation extends EntityInfo {

    private long id;
    private String code;
    private String name;

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
     * Gets the code and name.
     * @return the code and name
     */
    public String getCodeAndName() {

        StringBuffer sb = new StringBuffer();
        if (!StringUtil.isEmpty(code)) {
            sb.append(code);
            if (!StringUtil.isEmpty(name)) {
                sb.append(" - ");
            }
        }

        if (!StringUtil.isEmpty(name)) {
            sb.append(name);
        }
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return code;
    }
}
