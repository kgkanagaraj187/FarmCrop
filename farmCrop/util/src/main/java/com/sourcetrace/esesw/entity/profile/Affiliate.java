/*
 * Affiliate.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.profile;

import java.io.File;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.order.entity.profile.Category;
import com.sourcetrace.eses.umgmt.entity.ContactInfo;
import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.log.Auditable;
import com.sourcetrace.esesw.entity.txn.Balance;
import com.sourcetrace.esesw.entity.txn.Payment;

// TODO: Auto-generated Javadoc
/**
 * The Class Affiliate.
 * @author $Author: ganesh $
 * @version $Rev: 1306 $ $Date: 2010-06-30 15:09:53 +0530 (Wed, 30 Jun 2010) $
 */
public class Affiliate implements Auditable {

    
    public static final int PROV_TXN_ONLINE_MODE = 1;   
    public static final int PROV_TXN_OFFLINE_MODE = 2;   
    private static final int NAME_MAX_LENGTH = 25;
   
    private String affiliateId;   
    private Category category;    
    private ContactInfo contactInfo;   
    private AffiliateCredential credential;   
    private Balance balance;   
    private boolean enabled;   
    private Federation federation;   
    private long id;  
    private Image logo;  
    private String name;   
    private PaymentDay paymentDay;   
    private PaymentPeriod paymentPeriod;   
    private PaymentType paymentType;   
    private PersonalInfo personalInfo;   
    private String moneyGramAgentID;  
    private long revision;   
    private Set<Store> stores;  
    private Set<SwitchTxn> txns;   
    private Set<Payment> payments;

    /**
     * Gets the affiliate credential.
     * @return the affiliate credential
     */
    public AffiliateCredential getAffiliateCredential() {

        return credential;
    }

    /**
     * Gets the affiliate id.
     * @return the affiliate id
     */
    @Length(max = NAME_MAX_LENGTH, message = "length.affiliate.id")
    @Pattern(regexp = "[[^@#$%&*();:,{}^<>?+|^'!^/=\"\\_-]]+$", message = "pattern.affiliate.id")
    @NotEmpty(message = "empty.afiliate.id")
    @NotNull(message = "empty.afiliate.id")
    public String getAffiliateId() {

        return affiliateId;
    }

    /**
     * Gets the category.
     * @return the category
     */
    @NotNull(message = "empty.affiliate.category")
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
     * Gets the credential.
     * @return the credential
     */
    public AffiliateCredential getCredential() {

        return credential;
    }

    /**
     * Gets the federation.
     * @return the federation
     */
    @NotNull(message = "empty.affiliate.federation")
    public Federation getFederation() {

        return federation;
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
    @NotEmpty(message = "empty.afiliate.name")
    @NotNull(message = "empty.afiliate.name")
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
     * Gets the revision number.
     * @return the revision number
     */
    public long getRevision() {

        return revision;
    }

    /**
     * Gets the stores.
     * @return the stores
     */
    public Set<Store> getStores() {

        return stores;
    }

    /**
     * Gets the txns.
     * @return the txns
     */
    public Set<SwitchTxn> getTxns() {

        return txns;
    }

    /**
     * Checks if is enabled.
     * @return true, if is enabled
     */
    public boolean isEnabled() {

        return enabled;
    }

    /**
     * Sets the affiliate credential.
     * @param credential the new affiliate credential
     */
    public void setAffiliateCredential(AffiliateCredential credential) {

        this.credential = credential;
    }

    /**
     * Sets the affiliate id.
     * @param affiliateId the new affiliate id
     */
    public void setAffiliateId(String affiliateId) {

        this.affiliateId = affiliateId;
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
     * Sets the credential.
     * @param credential the new credential
     */
    public void setCredential(AffiliateCredential credential) {

        this.credential = credential;
    }

    /**
     * Sets the enabled.
     * @param enabled the new enabled
     */
    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }

    /**
     * Sets the federation.
     * @param federation the new federation
     */
    public void setFederation(Federation federation) {

        this.federation = federation;
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
     * Sets the revision number.
     * @param revisionNumber the new revision number
     */
    public void setRevision(long revisionNumber) {

        this.revision = revisionNumber;
    }

    /**
     * Sets the stores.
     * @param stores the new stores
     */
    public void setStores(Set<Store> stores) {

        this.stores = stores;
    }

    /**
     * Sets the txns.
     * @param transactions the new txns
     */
    public void setTxns(Set<SwitchTxn> transactions) {

        this.txns = transactions;
    }

    /**
     * Sets the image.
     * @param image the new image
     */
    public void setImage(File image) {

        if (getLogo() == null) {
            setLogo(new Image());
        }
        getLogo().setImageId(getAffiliateId());
        byte[] byteArray = FileUtil.getBinaryFileContent(image);
        //getLogo().setImage(Hibernate.createBlob(byteArray));
    }

    /**
     * Gets the balance.
     * @return the balance
     */
    public Balance getBalance() {

        return balance;
    }

    /**
     * Sets the balance.
     * @param balance the new balance
     */
    public void setBalance(Balance balance) {

        this.balance = balance;
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

  
}
