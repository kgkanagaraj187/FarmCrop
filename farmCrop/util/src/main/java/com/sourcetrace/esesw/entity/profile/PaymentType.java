/*
 * PaymentType.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * The Class PaymentType.
 * @author $Author: ganesh $
 * @version $Rev: 580 $ $Date: 2009-12-22 13:17:57 +0530 (Tue, 22 Dec 2009) $
 */
public class PaymentType {
    public static final int MAX_LENGTH_CODE=12;
    public static final int MAX_LENGTH_NAME=35;
    public static final int MAX_LENGTH_DESC=255;
    private String code;
    private String description;
    private long id;
    private String name;

    /**
     * Gets the code.
     * @return the code
     */
    @Length(max =MAX_LENGTH_CODE, message = "length.code")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.code")
    @NotEmpty(message = "empty.code")
    public String getCode() {

        return code;
    }

    /**
     * Gets the description.
     * @return the description
     */
    @Length(max =MAX_LENGTH_DESC, message = "length.description")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.description")
    @NotEmpty(message = "empty.description")
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
    @Length(max = MAX_LENGTH_NAME, message = "length.name")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.name")
    @NotEmpty(message = "empty.name")
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

        return name;
    }
}
