/**
 * Store.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.umgmt.entity.ContactInfo;


// TODO: Auto-generated Javadoc
/**
 * The Class Store.
 * @author $Author: ganesh $
 * @version $Rev: 1306 $ $Date: 2010-06-30 15:09:53 +0530 (Wed, 30 Jun 2010) $
 */
public class Store {
    
    /** The Constant NAME_MAX_LENGTH. */
    private static final int NAME_MAX_LENGTH = 25;
    /** The active. */
    private boolean active;

    /** The affiliate. */
    private Affiliate affiliate;

    /** The contact info. */
    private ContactInfo contactInfo;

    /** The id. */
    private long id;

    /** The name. */
    private String name;

    /** The store id. */
    private String storeId;

    /** The created time. */
    private Date createdTime;

    /** The modified time. */
    private Date modifiedTime;

    /** The service points. */
    private Set<ServicePoint> servicePoints;

    /** The money gram agent id. */
    private String moneyGramAgentID;

    /**
     * Gets the affiliate.
     * @return the affiliate
     */
    public Affiliate getAffiliate() {

        return affiliate;
    }

    /**
     * Gets the contact info.
     * @return the contact info
     */
    public ContactInfo getContactInfo() {

        return contactInfo;
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
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.name")
    @Length(max = 30, message = "length.name")
    @NotEmpty(message = "empty.name")
    public String getName() {

        return name;
    }

    /**
     * Gets the service points.
     * @return the service points
     */
    public Set<ServicePoint> getServicePoints() {

        return servicePoints;
    }

    /**
     * Checks if is active.
     * @return true, if is active
     */
    public boolean isActive() {

        return active;
    }

    /**
     * Sets the active.
     * @param active the new active
     */
    public void setActive(boolean active) {

        this.active = active;
    }

    /**
     * Sets the affiliate.
     * @param affiliate the new affiliate
     */
    public void setAffiliate(Affiliate affiliate) {

        this.affiliate = affiliate;
    }

    /**
     * Sets the contact info.
     * @param contactInfo the new contact info
     */
    public void setContactInfo(ContactInfo contactInfo) {

        this.contactInfo = contactInfo;
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
     * Sets the service points.
     * @param servicePoints the new service points
     */
    public void setServicePoints(Set<ServicePoint> servicePoints) {

        this.servicePoints = servicePoints;
    }

    /**
     * Gets the store id.
     * @return the store id
     */
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.storeid")
    @Length(max = 30, message = "length.storeid")
    @NotEmpty(message = "empty.storeid")
    public String getStoreId() {

        return storeId;
    }

    /**
     * Sets the store id.
     * @param storeId the new store id
     */
    public void setStoreId(String storeId) {

        this.storeId = storeId;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return name;
    }

    /**
     * Gets the created time.
     * @return the created time
     */
    public Date getCreatedTime() {

        return createdTime;
    }

    /**
     * Sets the created time.
     * @param createdTime the new created time
     */
    public void setCreatedTime(Date createdTime) {

        this.createdTime = createdTime;
    }

    /**
     * Gets the modified time.
     * @return the modified time
     */
    public Date getModifiedTime() {

        return modifiedTime;
    }

    /**
     * Sets the modified time.
     * @param modifiedTime the new modified time
     */
    public void setModifiedTime(Date modifiedTime) {

        this.modifiedTime = modifiedTime;
    }

    /**
     * Gets the money gram agent id.
     * @return the money gram agent id
     */
    @Length(max = NAME_MAX_LENGTH, message = "length.MoneyGremAgentId")
    @Pattern(regexp = "[0-9^-]+", message = "pattern.MoneyGremAgentId")
    public String getMoneyGramAgentID() {

        return moneyGramAgentID;
    }

    /**
     * Sets the money gram agent id.
     * @param moneyGramAgentID the new money gram agent id
     */
    public void setMoneyGramAgentID(String moneyGramAgentID) {

        this.moneyGramAgentID = moneyGramAgentID;
    }

}
