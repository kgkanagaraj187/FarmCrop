/*
 * Federation.java
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
 * The Class Federation.
 * @author $Author: ganesh $
 * @version $Rev: 1150 $ $Date: 2010-04-22 13:32:35 +0530 (Thu, 22 Apr 2010) $
 */
public class Federation {
    public static final int MAX_LENGTH_NAME=45;
    public static final int MAX_LENGTH_DESC=255;
    private String description;
    private long id;
    private String name;

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

        return this.name;
    }
}
