/*
 * ServicePointType.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

/**
 * The Class ServicePointType.
 * @author $Author: aravind $
 * @version $Rev: 516 $ $Date: 2009-12-12 18:01:35 +0530 (Sat, 12 Dec 2009) $
 */
public class ServicePointType {

    private String code;
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
    public Long getId() {

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
