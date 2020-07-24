/*
 * Rule.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The Class Rule.
 * @author $Author: moorthy $
 * @version $Rev: 1373 $ $Date: 2010-08-05 12:36:53 +0530 (Thu, 05 Aug 2010) $
 */
public class Rule {

    private Set<String> attributes;
    private String code;
    private String description;
    private long id;
    private String name;        
    
    public static final String ADAPTER_MANDATORY = "1";
    public static final String ADAPTER_OPTIONAL = "2";
    public static final String ADAPTER_UNSUPPORT = "3";
    
    // Transient variables for dynamic rules displaying 
    public List<TxnType> tempAttributes=new ArrayList<TxnType>();
   
	/**
     * Gets the attributes.
     * @return the attributes
     */
    public Set<String> getAttributes() {

        return attributes;
    }

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
     * Sets the attributes.
     * @param attributes the new attributes
     */
    public void setAttributes(Set<String> attributes) {

        this.attributes = attributes;
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
    
    public List<TxnType> getTempAttributes() {

        return tempAttributes;
    }

    public void setTempAttributes(List<TxnType> tempAttributes) {

        this.tempAttributes = tempAttributes;
    }

   
}
