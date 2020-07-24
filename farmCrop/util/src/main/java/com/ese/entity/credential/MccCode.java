/**
 * MccCode.java
 * Copyright (c) 2008-2009, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.ese.entity.credential;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.ese.entity.txn.fee.CommissionFeeGroup;

// TODO: Auto-generated Javadoc
/**
 * The Class MccCode.
 * @author $Author: aravind $
 * @version $Rev: 59 $, $Date: 2009-08-04 17:10:51 +0530 (Tue, 04 Aug 2009) $
 */
public class MccCode {
    private long id;
    private String mccCode;
    private String description;
    private CredentialGroup credentialGroup;
    private CommissionFeeGroup commissionFeeGroup;

    // transient
    private String siteUser;

    /**
     * Gets the site user.
     * @return the site user
     */
    public String getSiteUser() {

        return siteUser;
    }

    /**
     * Sets the site user.
     * @param siteUser the new site user
     */
    public void setSiteUser(String siteUser) {

        this.siteUser = siteUser;
    }

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
     * Gets the mcc code.
     * @return the mcc code
     */
    @Length(max = 45, message = "length.mccCode")
    @NotEmpty(message = "empty.mccCode")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.mccCode")
    public String getMccCode() {

        return mccCode;
    }

    /**
     * Sets the mcc code.
     * @param mccCode the new mcc code
     */
    public void setMccCode(String mccCode) {

        this.mccCode = mccCode;
    }

    /**
     * Gets the description.
     * @return the description
     */
    @Length(max = 255, message = "length.description")
    @NotEmpty(message = "empty.description")

    @Pattern(regexp = "[^@#$%^&*,()_-]+$", message = "pattern.description")
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
     * Gets the credential group.
     * @return the credential group
     */
    public CredentialGroup getCredentialGroup() {

        return credentialGroup;
    }

    /**
     * Sets the credential group.
     * @param credentialGroup the new credential group
     */
    public void setCredentialGroup(CredentialGroup credentialGroup) {

        this.credentialGroup = credentialGroup;
    }

    /**
     * Gets the commission fee group.
     * @return the commission fee group
     */
    public CommissionFeeGroup getCommissionFeeGroup() {

        return commissionFeeGroup;
    }

    /**
     * Sets the commission fee group.
     * @param commissionFeeGroup the new commission fee group
     */
    public void setCommissionFeeGroup(CommissionFeeGroup commissionFeeGroup) {

        this.commissionFeeGroup = commissionFeeGroup;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return String.valueOf(id);
    }
}
