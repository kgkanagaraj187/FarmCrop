/**
 * Provider.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.io.File;
import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.order.entity.profile.Category;
import com.sourcetrace.eses.umgmt.entity.ContactInfo;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.log.Auditable;
import com.sourcetrace.esesw.entity.txn.Payment;

/**
 * The Class Provider.
 * @author $Author: ganesh $
 * @version $Rev: 1084 $ $Date: 2010-03-29 16:33:23 +0530 (Mon, 29 Mar 2010) $
 */
public class Provider implements Auditable {

    public static final int PROV_TXN_ONLINE_MODE = 1;
    public static final int PROV_TXN_OFFLINE_MODE = 2;
    private static final int NAME_MAX_LENGTH = 25;
    private static final int DESC_MAX_LENGTH = 255;
    
    private boolean active;
    private Category category;
    private ContactInfo contactInfo;
    private String description;
    private long id;
    private Image logo;
    private String name;
    private PaymentDay paymentDay;
    private PaymentPeriod paymentPeriod;
    private PaymentType paymentType;
    private PersonalInfo personalInfo;
    private String providerId;
    private Date createdTime;
    private Date modifiedTime;
    private int txnMode;
    private Set<SwitchTxn> txns;
    private Set<TxnType> txnTypes;
    private Set<Payment> payments;

    // transient properties for handling Image Conversion/Validation
    private File emptyImage;
    private boolean imageFieldSelected;

    /**
     * Gets the category.
     * @return the category
     */
    @NotNull(message = "empty.category")
    public Category getCategory() {

        return category;
    }

    /**
     * Gets the contact info.
     * @return the contact info
     */
    public ContactInfo getContactInfo() {

        return contactInfo;
    }

    /**
     * Gets the description.
     * @return the description
     */
    @Length(max = DESC_MAX_LENGTH, message = "length.description")   
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
     * Gets the logo.
     * @return the logo
     */
    @NotNull(message = "empty.logo")
    public Image getLogo() {

        return logo;
    }

    /**
     * Gets the name.
     * @return the name
     */
    @Length(max = NAME_MAX_LENGTH, message = "length.name")
    @Pattern(regexp = "[[^@#$%&*();:,{}^<>?+|^'!^/=\"\\_-]]+$", message = "pattern.name")
    @NotEmpty(message = "empty.name")
    @NotNull(message = "empty.name")
    public String getName() {

        return name;
    }

    /**
     * Gets the payment day.
     * @return the payment day
     */
    @NotNull(message = "empty.paymentDay")
    public PaymentDay getPaymentDay() {

        return paymentDay;
    }

    /**
     * Gets the payment period.
     * @return the payment period
     */
    @NotNull(message = "empty.paymentPeriod")
    public PaymentPeriod getPaymentPeriod() {

        return paymentPeriod;
    }

    /**
     * Gets the payment type.
     * @return the payment type
     */
    @NotNull(message = "empty.paymentType")
    public PaymentType getPaymentType() {

        return paymentType;
    }

    /**
     * Gets the personal info.
     * @return the personal info
     */
    public PersonalInfo getPersonalInfo() {

        return personalInfo;
    }

    /**
     * Gets the provider id.
     * @return the provider id
     */
    @Length(max = NAME_MAX_LENGTH, message = "length.provider.id")
    @Pattern(regexp = "[[^@#$%&*();:,{}^<>?+|^'!^/=\"\\_-]]+$", message = "pattern.provider.id")
    @NotEmpty(message = "empty.provider.id")
    @NotNull(message = "empty.provider.id")
    public String getProviderId() {

        return providerId;
    }

    /**
     * Gets the txn mode.
     * @return the txn mode
     */
    @Min(value = 1, message = "empty.txnMode")
    public int getTxnMode() {

        return txnMode;
    }

    /**
     * Gets the txns.
     * @return the txns
     */
    public Set<SwitchTxn> getTxns() {

        return txns;
    }

    /**
     * Gets the txn types.
     * @return the txn types
     */
    public Set<TxnType> getTxnTypes() {

        return txnTypes;
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
     * Sets the category.
     * @param category the new category
     */
    public void setCategory(Category category) {

        this.category = category;
    }

    /**
     * Sets the contact info.
     * @param contactInfo the new contact info
     */
    public void setContactInfo(ContactInfo contactInfo) {

        this.contactInfo = contactInfo;
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
     * Sets the logo.
     * @param logo the new logo
     */
    public void setLogo(Image logo) {

        this.logo = logo;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Sets the payment day.
     * @param paymentDay the new payment day
     */
    public void setPaymentDay(PaymentDay paymentDay) {

        this.paymentDay = paymentDay;
    }

    /**
     * Sets the payment period.
     * @param paymentPeriod the new payment period
     */
    public void setPaymentPeriod(PaymentPeriod paymentPeriod) {

        this.paymentPeriod = paymentPeriod;
    }

    /**
     * Sets the payment type.
     * @param paymentType the new payment type
     */
    public void setPaymentType(PaymentType paymentType) {

        this.paymentType = paymentType;
    }

    /**
     * Sets the personal info.
     * @param personalInfo the new personal info
     */
    public void setPersonalInfo(PersonalInfo personalInfo) {

        this.personalInfo = personalInfo;
    }

    /**
     * Sets the provider id.
     * @param providerId the new provider id
     */
    public void setProviderId(String providerId) {

        this.providerId = providerId;
    }

    /**
     * Sets the txn mode.
     * @param txnMode the new txn mode
     */
    public void setTxnMode(int txnMode) {

        this.txnMode = txnMode;
    }

    /**
     * Sets the txns.
     * @param txns the new txns
     */
    public void setTxns(Set<SwitchTxn> txns) {

        this.txns = txns;
    }

    /**
     * Sets the txn types.
     * @param txnTypes the new txn types
     */
    public void setTxnTypes(Set<TxnType> txnTypes) {

        this.txnTypes = txnTypes;
    }

    /**
     * Gets the payments.
     * @return the payments
     */
    public Set<Payment> getPayments() {

        return payments;
    }

    /**
     * Sets the payments.
     * @param payments the new payments
     */
    public void setPayments(Set<Payment> payments) {

        this.payments = payments;
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

	public File getEmptyImage() {
		return emptyImage;
	}

	public void setEmptyImage(File emptyImage) {
		this.emptyImage = emptyImage;
	}

	public boolean isImageFieldSelected() {
		return imageFieldSelected;
	}

	public void setImageFieldSelected(boolean imageFieldSelected) {
		this.imageFieldSelected = imageFieldSelected;
	}



}
