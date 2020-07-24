/*
 * Payment.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.esesw.entity.txn;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.esesw.entity.profile.Affiliate;
import com.sourcetrace.esesw.entity.profile.Bank;
import com.sourcetrace.esesw.entity.profile.Currency;
import com.sourcetrace.esesw.entity.profile.Provider;


/**
 * Payment is an entity to record the payment transactions done by affiliate or switch.
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
public class Payment {

   
    public static final int AFFILIATE_SWITCH_PAYMENT = 101;   
    public static final int SWITCH_AFFILIATE_PAYMENT = 102;
    public static final int PROVIDER_SWITCH_PAYMENT = 103;   
    public static final int SWITCH_PROVIDER_PAYMENT = 104;
    
    public static final int MAX_LENGTH_NAME = 35;
    public static final int MAX_LENGTH_DESC = 255;    
    public static final int PAYMENT_CREATE =0;
    
    public static final int PENDING = 1;  
    public static final int APPROVED = 2;   
    public static final int REJECTED = 3;   
    public static final int IMPORTED = 4;
    
    private long id;  
    private Date date;  
    private int type;   
    private Affiliate affiliate;   
    private Provider provider;   
    private String reference;    
    private String description;   
    private double amount;  
    private Currency currency;   
    private Bank bank;   
    private Image logo;   
    private int status;   
    private Date lastModifiedTime;    
    private Date[] filterDate;

    // transient to filter payments for provider and affiliate   
    List<Integer> paymentTypes;    
    List<Double> amountList;

    /**
     * Gets the payment types.
     * @return the paymentTypes
     */
    public List<Integer> getPaymentTypes() {

        return paymentTypes;
    }

    /**
     * Sets the payment types.
     * @param paymentTypes the paymentTypes to set
     */
    public void setPaymentTypes(List<Integer> paymentTypes) {

        this.paymentTypes = paymentTypes;
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
     * Gets the date.
     * @return the date
     */
    public Date getDate() {

        return date;
    }

    /**
     * Sets the date.
     * @param date the new date
     */
    public void setDate(Date date) {

        this.date = date;
    }

    /**
     * Gets the reference.
     * @return the reference
     */
    @Length(max = MAX_LENGTH_NAME, message = "length.reference")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.reference")
    @NotEmpty(message = "empty.reference")
    public String getReference() {

        return reference;
    }

    /**
     * Sets the reference.
     * @param reference the new reference
     */
    public void setReference(String reference) {

        this.reference = reference;
    }

    /**
     * Gets the description.
     * @return the description
     */
    @Length(max = MAX_LENGTH_DESC, message = "length.description")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.description")
    @NotEmpty(message = "empty.description")
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
     * Gets the amount.
     * @return the amount
     */
    @Range(max = 10000000, min = 1, message = "amount.range")
    public double getAmount() {

        return amount;
    }

    /**
     * Sets the amount.
     * @param amount the new amount
     */
    public void setAmount(double amount) {

        this.amount = amount;
    }

    /**
     * Gets the currency.
     * @return the currency
     */
    @NotEmpty(message = "empty.currency")
    public Currency getCurrency() {

        return currency;
    }

    /**
     * Sets the currency.
     * @param currency the new currency
     */
    public void setCurrency(Currency currency) {

        this.currency = currency;
    }

    /**
     * Gets the status.
     * @return the status
     */
    public int getStatus() {

        return status;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(int status) {

        this.status = status;
    }

    /**
     * Gets the affiliate.
     * @return the affiliate
     */
    @NotEmpty(message = "empty.affiliate")
    public Affiliate getAffiliate() {

        return affiliate;
    }

    /**
     * Sets the affiliate.
     * @param affiliate the new affiliate
     */
    public void setAffiliate(Affiliate affiliate) {

        this.affiliate = affiliate;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public int getType() {

        return type;
    }

    /**
     * Sets the type.
     * @param type the new type
     */
    public void setType(int type) {

        this.type = type;
    }

    /**
     * Gets the last modified time.
     * @return the last modified time
     */
    public Date getLastModifiedTime() {

        return lastModifiedTime;
    }

    /**
     * Sets the last modified time.
     * @param lastModifiedTime the new last modified time
     */
    public void setLastModifiedTime(Date lastModifiedTime) {

        this.lastModifiedTime = lastModifiedTime;
    }

    /**
     * Gets the provider.
     * @return the provider
     */
    @NotEmpty(message = "empty.provider")
    public Provider getProvider() {

        return provider;
    }

    /**
     * Sets the provider.
     * @param provider the new provider
     */
    public void setProvider(Provider provider) {

        this.provider = provider;
    }

    /**
     * Gets the filter date.
     * @return the filter date
     */
    public Date[] getFilterDate() {

        return filterDate;
    }

    /**
     * Sets the filter date.
     * @param filterDate the new filter date
     */
    public void setFilterDate(Date[] filterDate) {

        this.filterDate = filterDate;
    }

    public List<Double> getAmountList() {
		return amountList;
	}

	public void setAmountList(List<Double> amountList) {
		this.amountList = amountList;
	}

	/**
     * Gets the bank.
     * @return the bank
     */
    public Bank getBank() {

        return bank;
    }

    /**
     * Sets the bank.
     * @param bank the new bank
     */
    public void setBank(Bank bank) {

        this.bank = bank;
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
     * Sets the logo.
     * @param logo the new logo
     */
    public void setLogo(Image logo) {

        this.logo = logo;
    }

}
