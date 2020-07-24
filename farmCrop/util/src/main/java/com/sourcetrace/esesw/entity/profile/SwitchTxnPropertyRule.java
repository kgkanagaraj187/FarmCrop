/*
 * SwitchTxnPropertyRule.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class TxnPropertyRule.
 * @author $Author: moorthy $
 * @version $Rev: 1311 $ $Date: 2010-07-01 19:31:51 +0530 (Thu, 01 Jul 2010) $
 */
public class SwitchTxnPropertyRule {

    private long id;
    private Rule rule;
    private Map<String, String> attributesValues;
    
   // public static final int DROPDOWN_OFFLINE = 101;
   // public static final int DROPDOWN_ONLINE = 102;
   // static final String DROPDOWN = "Dropdown";
    
    

    //transient property
    public List<String> temprules=new ArrayList<String>();

    

    public List<String> getTemprules() {
		return temprules;
	}

	public void setTemprules(List<String> temprules) {
		this.temprules = temprules;
	}

    /**
     * Gets the attribute value.
     * @return the attribute value
     */
    public Map<String, String> getAttributesValues() {

        return attributesValues;
    }

    /**
     * Sets the attribute value.
     * @param attributeValue the attribute value
     */
    public void setAttributesValues(Map<String, String> attributeValue) {

        this.attributesValues = attributeValue;
    }

    /**
     * Gets the id.
     * @return the id
     * @uml.property name="id"
     */
    public Long getId() {

        return id;
    }

    /**
     * Gets the biz rule.
     * @return the biz rule
     * @uml.property name="rule"
     */
    public Rule getRule() {

        return rule;
    }

    /**
     * Sets the biz rule.
     * @param bizRule the new biz rule
     * @uml.property name="rule"
     */
    public void setRule(Rule bizRule) {

        this.rule = bizRule;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }
    
    /**
     * Adds the attribute value.
     * @param attribute the attribute
     * @param value the value
     */
    public void addAttributeValue(String attribute, String value) {
        
        if(attributesValues == null) {
            attributesValues = new LinkedHashMap<String, String>();
        }
        
        attributesValues.put(attribute, value);
    }
}
