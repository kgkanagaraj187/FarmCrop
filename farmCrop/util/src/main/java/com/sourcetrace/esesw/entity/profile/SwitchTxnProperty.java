/*
 * SwitchTxnProperty.java
 * Copyright (c) 2010-2011, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.util.StringUtil;

/**
 * The Class SwitchTxnProperty.
 * @author $Author: moorthy $
 * @version $Rev: 1312 $ $Date: 2010-07-05 11:25:52 +0530 (Mon, 05 Jul 2010) $
 */
public class SwitchTxnProperty implements Comparable<SwitchTxnProperty> {

    public static final int DROPDOWN_OFFLINE = 101;
    public static final int DEPENDENT_DROPDOWN_OFFLINE = 102;
    public static final int DROPDOWN_ONLINE = 103;
    public static final int DEPENDENT_DROPDOWN_ONLINE = 104;
    public static final int CHECKBOX_OFFLINE = 105;
    public static final int CHECKBOX_ONLINE = 106;
    public static final int RADIO_BUTTON_OFFLINE = 107;
    public static final int RADIO_BUTTON_ONLINE = 108;
    public static final int TEXTBOX = 109;

    public static final int OFFLINE = 1;
    public static final int ONLINE = 2;
    public static final String DROPDOWN = "Dropdown";
    public static final String DEPENDENT_DROPDOWN = "DependentDropdown";
    public static final String CHECKBOX = "CheckBox";
    public static final String RADIO_BUTTON = "RadioButton";
    public static final String TEXT_BOX = "TextBox";
    public static final String ACCEPT = "accept";

    private String defaultValue;
    private String description;

    private long id;
    private String name;
    private String tooltip;
    private Parameter parameter;
    private Set<SwitchTxnPropertyRule> rules;
    private ParameterType type;
    private Long childId;
    private Long prop_type;
    private String dependentProp;
    private String dependentResPropName;
    private String dependentResPropValue;
    private int orderId;
    private String reference;

    // transient variable
    // orderId datatype is integer so for validation purpose using
    private String parameterOrderId;
  
    /**
     * Gets the tooltip.
     * @return the tooltip
     */

    @Length(max = 100, message = "length.tooltip.name")
    @Pattern(regexp = "[[^@#$%&*();:,{}^<>?+|^'!^/=\"\\_-]]+$", message = "pattern.tooltip.name")
    @NotEmpty(message = "empty.tooltip.name")
    public String getTooltip() {

        return tooltip;
    }

    /**
     * Gets the child id.
     * @return the child id
     */
    public Long getChildId() {

        return childId;
    }

    /**
     * Sets the child id.
     * @param childId the new child id
     */
    public void setChildId(Long childId) {

        this.childId = childId;
    }

    /**
     * Sets the tooltip.
     * @param tooltip the new tooltip
     */
    public void setTooltip(String tooltip) {

        this.tooltip = tooltip;
    }
    
    

  

    /**
     * Gets the order id.
     * @return the order id
     */

    public int getOrderId() {

        return orderId;
    }

    /**
     * Sets the order id.
     * @param orderId the new order id
     */
    public void setOrderId(int orderId) {

        this.orderId = orderId;
    }

    /**
     * Gets the default value.
     * @return the default value
     */
    public String getDefaultValue() {

        return defaultValue;
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
    @Length(max = 60, message = "length.property.name")
    @Pattern(regexp = "[[^@#$%&*();:,{}^<>?+|^'!^/=\"\\_-]]+$", message = "pattern.property.name")
    @NotEmpty(message = "empty.property.name")
    public String getName() {

        return name;
    }

    /**
     * Gets the parameter.
     * @return the parameter
     */
    public Parameter getParameter() {

        return parameter;
    }

    /**
     * Gets the property rules.
     * @return the property rules
     */
    public Set<SwitchTxnPropertyRule> getRules() {

        if (rules == null) {
            rules = new LinkedHashSet<SwitchTxnPropertyRule>();
        }

        return rules;
    }

    /**
     * Sets the default value.
     * @param defaultValue the new default value
     */
    public void setDefaultValue(String defaultValue) {

        this.defaultValue = defaultValue;
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
     * Sets the parameter.
     * @param parameter the new parameter
     */
    public void setParameter(Parameter parameter) {

        this.parameter = parameter;
    }

    /**
     * Sets the property rules.
     * @param propertyRules the new property rules
     */
    public void setRules(Set<SwitchTxnPropertyRule> propertyRules) {

        this.rules = propertyRules;
    }

    /**
     * Gets the description.
     * @return the description
     */
    public String getDescription() {

        return description;
    }

    /**
     * Sets the description.
     * @param description the new description
     */
    public void setDescription(String description) {

        this.description = description;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public ParameterType getType() {

        return type;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(ParameterType type) {

        this.type = type;
    }

    /**
     * Gets the parameter order id.
     * @return the parameter order id
     */
    @Length(max = 5, message = "length.orderId.name")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.orderId.name")
    @NotEmpty(message = "empty.orderId.name")
    public String getParameterOrderId() {

        return parameterOrderId;
    }

    /**
     * Sets the parameter order id.
     * @param parameterOrderId the new parameter order id
     */
    public void setParameterOrderId(String parameterOrderId) {

        this.parameterOrderId = parameterOrderId;
    }

    /**
     * Compare to.
     * @param prop the prop
     * @return the int
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(SwitchTxnProperty prop) {

        int diff = 0;

        if (orderId != prop.getOrderId()) {

            if (orderId > prop.getOrderId()) {
                diff = 1;
            } else {
                diff = -1;
            }
        }

        return diff;
    }

    /**
     * Gets the dependent prop.
     * @return the dependent prop
     */
    public String getDependentProp() {

        return dependentProp;
    }

    /**
     * Sets the dependent prop.
     * @param dependentProp the new dependent prop
     */
    public void setDependentProp(String dependentProp) {

        this.dependentProp = dependentProp;
    }

    /**
     * Gets the dependent res prop name.
     * @return the dependent res prop name
     */
    public String getDependentResPropName() {

        return dependentResPropName;
    }

    /**
     * Sets the dependent res prop name.
     * @param dependentResPropName the new dependent res prop name
     */
    public void setDependentResPropName(String dependentResPropName) {

        this.dependentResPropName = dependentResPropName;
    }

    /**
     * Gets the dependent res prop value.
     * @return the dependent res prop value
     */
    public String getDependentResPropValue() {

        return dependentResPropValue;
    }

    /**
     * Sets the dependent res prop value.
     * @param dependentResPropValue the new dependent res prop value
     */
    public void setDependentResPropValue(String dependentResPropValue) {

        this.dependentResPropValue = dependentResPropValue;
    }

    /**
     * Gets the out put type.
     * @param type the type
     * @return the out put type
     */
    public String getOutPutType(Long type) {

        String OutPut = null;
        if (StringUtil.isEmpty(type)) {
            OutPut = TEXT_BOX;
        } else if (type == DROPDOWN_OFFLINE || type == DROPDOWN_ONLINE) {
            OutPut = DROPDOWN;
        } else if (type == DEPENDENT_DROPDOWN_OFFLINE || type == DEPENDENT_DROPDOWN_ONLINE) {
            OutPut = DEPENDENT_DROPDOWN;
        } else if (type == CHECKBOX_OFFLINE || type == CHECKBOX_ONLINE) {
            OutPut = CHECKBOX;
        } else if (type == RADIO_BUTTON_OFFLINE || type == RADIO_BUTTON_ONLINE) {
            OutPut = RADIO_BUTTON;
        } else {
            OutPut = TEXT_BOX;
        }

        return OutPut;
    }

    /**
     * Gets the out put type adapter.
     * @param type the type
     * @return the out put type adapter
     */
    public String getOutPutTypeAdapter(Long type) {

        String OutPut = null;
        if (StringUtil.isEmpty(type)) {
            OutPut = "invalid";
        } else if (type == DROPDOWN_ONLINE) {
            OutPut = String.valueOf(ONLINE);
        }

        else if (type == CHECKBOX_ONLINE) {
            OutPut = String.valueOf(ONLINE);
        } else if (type == RADIO_BUTTON_ONLINE) {
            OutPut = String.valueOf(ONLINE);
        } else {
            OutPut = "invalid";
        }

        return OutPut;
    }

    /**
     * Gets the out put type import.
     * @param type the type
     * @return the out put type import
     */
    public String getOutPutTypeImport(Long type) {

        String OutPut = null;
        if (StringUtil.isEmpty(type)) {
            OutPut = "invalid";
        } else if (type == DROPDOWN_OFFLINE) {
            OutPut = String.valueOf(OFFLINE);
        } else if (type == DEPENDENT_DROPDOWN_OFFLINE) {
            OutPut = String.valueOf(OFFLINE);
        } else if (type == CHECKBOX_OFFLINE) {
            OutPut = String.valueOf(OFFLINE);
        } else if (type == RADIO_BUTTON_OFFLINE) {
            OutPut = String.valueOf(OFFLINE);
        } else {
            OutPut = "invalid";
        }

        return OutPut;
    }

    /**
     * Gets the rule output type.
     * @param type the type
     * @return the rule output type
     */
    public String getRuleOutputType(String type) {

        String OutPut = null;
        if (StringUtil.isEmpty(type)) {
            OutPut = "invalid";
        } else if (type.equalsIgnoreCase(DROPDOWN)) {
            OutPut = ACCEPT;
        } else if (type.equalsIgnoreCase(CHECKBOX)) {
            OutPut = ACCEPT;
        } else if (type.equalsIgnoreCase(RADIO_BUTTON)) {
            OutPut = ACCEPT;
        } else {
            OutPut = "invalid";
        }

        return OutPut;
    }

    /**
     * Gets the prop_type.
     * @return the prop_type
     */
    public Long getProp_type() {

        return prop_type;
    }

    /**
     * Sets the prop_type.
     * @param propType the new prop_type
     */
    public void setProp_type(Long propType) {

        prop_type = propType;
    }

    public String getReference() {
    
        return reference;
    }

    public void setReference(String reference) {
    
        this.reference = reference;
    }

   
}
