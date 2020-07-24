/*
 * SwitchTxn.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * The Class SwitchTxn.
 * @author $Author: moorthy $
 * @version $Rev: 1330 $ $Date: 2010-07-13 16:23:28 +0530 (Tue, 13 Jul 2010) $
 */
public class SwitchTxn {

    public static final int NIGHTL_PROCESS = 1001;
    public static final int VALIDATION_PROCESS = 1002;
    public static final int CHILD_PROCESS = 1003;
    public static final int MAX_AMOUNT_VALIDATION_PROCESS = 1004;
    public static final int REVERSAL = 1005;
    private long id;
    private Long parentId;
    private String name;
    private String switchTxnId;
    private Provider provider;
    private int order;
    private long revision;
    private TxnType type;
    private boolean afterParent;
    private boolean printReceipt;
    private TxnOutputType output;
    private SwitchTxnCommission commission;
    private String txnMode;
    private String helpUrl;
    private boolean conformationScreen;
    private Boolean active;
    private Currency currency;
    private Set<SwitchTxn> dependentTxns;
    private Set<SwitchTxnProperty> properties;
    private Set<SwitchTxnResponseProperty> responseProperties;

    // transient property used for getting value from dynamic property text field while saving
    // switch txn config page
    Map<String, String> propertyMap = new LinkedHashMap<String, String>();   
    Map<String, SwitchTxnProperty> propertyMapSequence = new LinkedHashMap<String, SwitchTxnProperty>();  
    private String tempTxnId;

    /**
     * Checks if is conformation screen.
     * 
     * @return true, if is conformation screen
     */
    public boolean isConformationScreen() {

        return conformationScreen;
    }

    /**
     * Sets the conformation screen.
     * 
     * @param conformationScreen the new conformation screen
     */
    public void setConformationScreen(boolean conformationScreen) {

        this.conformationScreen = conformationScreen;
    }

    /**
     * Gets the help url.
     * 
     * @return the help url
     */
    public String getHelpUrl() {

        return helpUrl;
    }

    /**
     * Sets the help url.
     * 
     * @param helpUrl the new help url
     */
    public void setHelpUrl(String helpUrl) {

        this.helpUrl = helpUrl;
    }

    /**
     * Gets the txn mode.
     * 
     * @return the txn mode
     */
    public String getTxnMode() {

        return txnMode;
    }

    /**
     * Sets the txn mode.
     * 
     * @param txnMode the new txn mode
     */
    public void setTxnMode(String txnMode) {

        this.txnMode = txnMode;
    }

    /**
     * Gets the temp txn id.
     * 
     * @return the temp txn id
     */
    public String getTempTxnId() {

        return tempTxnId;
    }

    /**
     * Sets the temp txn id.
     * 
     * @param tempTxnId the new temp txn id
     */
    public void setTempTxnId(String tempTxnId) {

        this.tempTxnId = tempTxnId;
    }

    /**
     * Gets the order.
     * 
     * @return the order
     */
    public int getOrder() {

        return order;
    }

    /**
     * Sets the order.
     * 
     * @param order the new order
     */
    public void setOrder(int order) {

        this.order = order;
    }

    /**
     * Gets the parent id.
     * 
     * @return the parent id
     */
    public Long getParentId() {

        return parentId;
    }

    /**
     * Sets the parent id.
     * 
     * @param parentId the new parent id
     */
    public void setParentId(Long parentId) {

        this.parentId = parentId;
    }

    /**
     * Gets the dependent txns.
     * 
     * @return the dependent txns
     */
    public Set<SwitchTxn> getDependentTxns() {

        return dependentTxns;
    }

    /**
     * Sets the dependent txns.
     * 
     * @param dependentTxns the new dependent txns
     */
    public void setDependentTxns(Set<SwitchTxn> dependentTxns) {

        this.dependentTxns = dependentTxns;
    }

    /**
     * Gets the active.
     * 
     * @return the active
     */
    public Boolean getActive() {

        return active;
    }

    /**
     * Sets the active.
     * 
     * @param active the new active
     */
    public void setActive(Boolean active) {

        this.active = active;
    }

    /**
     * Gets the commission.
     * 
     * @return the commission
     */
    public SwitchTxnCommission getCommission() {

        return commission;
    }

    /**
     * Sets the commission.
     * 
     * @param commission the new commission
     */
    public void setCommission(SwitchTxnCommission commission) {

        this.commission = commission;
    }

    /**
     * Gets the currency.
     * 
     * @return the currency
     */
    public Currency getCurrency() {

        return currency;
    }

    /**
     * Sets the currency.
     * 
     * @param currency the new currency
     */
    public void setCurrency(Currency currency) {

        this.currency = currency;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    @Length(max = 30, message = "length.switchTxnName")
    @Pattern(regexp = "[[^@#%&*();:,{}^<>?+|^'!^/=\"\\_]]+$", message = "pattern.switchTxnName")
    @NotEmpty(message = "empty.switchTxnName")
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the properties.
     * 
     * @return the properties
     */
    public Set<SwitchTxnProperty> getProperties() {

        return properties;
    }

    /**
     * Sets the properties.
     * 
     * @param properties the new properties
     */
    public void setProperties(Set<SwitchTxnProperty> properties) {

        this.properties = properties;
    }

    /**
     * Gets the provider.
     * 
     * @return the provider
     */
    public Provider getProvider() {

        return provider;
    }

    /**
     * Sets the provider.
     * 
     * @param provider the new provider
     */
    public void setProvider(Provider provider) {

        this.provider = provider;
    }

    /**
     * Gets the revision.
     * 
     * @return the revision
     */
    public long getRevision() {

        return revision;
    }

    /**
     * Sets the revision.
     * 
     * @param revision the new revision
     */
    public void setRevision(long revision) {

        this.revision = revision;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public TxnType getType() {

        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type the new type
     */
    public void setType(TxnType type) {

        this.type = type;
    }

    /**
     * Gets the switch txn id.
     * 
     * @return the switch txn id
     */
    @Length(max = 20, message = "length.switchTxnId")
    @Pattern(regexp = "[0-9^-]+", message = "pattern.switchTxnId")
    @NotEmpty(message = "empty.switchTxnId")
    public String getSwitchTxnId() {

        return switchTxnId;
    }

    /**
     * Sets the switch txn id.
     * 
     * @param switchTxnId the new switch txn id
     */
    public void setSwitchTxnId(String switchTxnId) {

        this.switchTxnId = switchTxnId;
    }

    // getters setters for transient variable

    /**
     * Gets the property map.
     * 
     * @return the property map
     */
    /**
     * Gets the property map.
     * @return the property map
     */
    public Map<String, String> getPropertyMap() {

        return propertyMap;
    }

    /**
     * Sets the property map.
     * 
     * @param propertyMap the property map
     */

    public void setPropertyMap(Map<String, String> propertyMap) {

        this.propertyMap = propertyMap;
    }

    /**
     * Gets the property map sequence.
     * 
     * @return the property map sequence
     */
    public Map<String, SwitchTxnProperty> getPropertyMapSequence() {

        return propertyMapSequence;
    }

    /**
     * Sets the property map sequence.
     * 
     * @param propertyMapSequence the property map sequence
     */
    public void setPropertyMapSequence(Map<String, SwitchTxnProperty> propertyMapSequence) {

        this.propertyMapSequence = propertyMapSequence;
    }

    /**
     * Gets the response properties.
     * 
     * @return the response properties
     */
    public Set<SwitchTxnResponseProperty> getResponseProperties() {

        return responseProperties;
    }

    /**
     * Sets the response properties.
     * 
     * @param responseProperties the new response properties
     */
    public void setResponseProperties(Set<SwitchTxnResponseProperty> responseProperties) {

        this.responseProperties = responseProperties;
    }

    /**
     * Checks if is after parent.
     * 
     * @return true, if is after parent
     */
    public boolean isAfterParent() {

        return afterParent;
    }

    /**
     * Sets the after parent.
     * 
     * @param afterParent the new after parent
     */
    public void setAfterParent(boolean afterParent) {

        this.afterParent = afterParent;
    }

    /**
     * Checks if is prints the receipt.
     * 
     * @return true, if is prints the receipt
     */
    public boolean isPrintReceipt() {

        return printReceipt;
    }

    /**
     * Sets the prints the receipt.
     * 
     * @param printReceipt the new prints the receipt
     */
    public void setPrintReceipt(boolean printReceipt) {

        this.printReceipt = printReceipt;
    }

    /**
     * Gets the output.
     * 
     * @return the output
     */
    public TxnOutputType getOutput() {

        return output;
    }

    /**
     * Sets the output.
     * 
     * @param output the new output
     */
    public void setOutput(TxnOutputType output) {

        this.output = output;
    }

}
