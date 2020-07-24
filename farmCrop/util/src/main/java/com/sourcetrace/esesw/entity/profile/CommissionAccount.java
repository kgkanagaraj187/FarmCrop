/**
 * CommissionAccount.java
 * Copyright (c) 2008-2009, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.esesw.entity.profile;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.entity.Profile;

/**
 * The Class CommissionAccount.
 * @author $Author: aravind $
 * @version $Rev: 59 $, $Date: 2009-08-04 17:10:51 +0530 (Tue, 04 Aug 2009) $
 */
public class CommissionAccount {

    private long id;
    private Profile profile;
    private String accountNumber;
    private String accountType;
    private Currency currency;

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
	 * Gets the account number.
	 * @return the account number
	 */
    @Length(max = 45, message = "length.accountNumber")
	@Pattern(regexp="[^\\p{Punct}]+$", message="pattern.commissionAccount")
	@NotEmpty(message = "empty.commissionAccount")
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
	 * Sets the account number.
	 * @param accountNumber the new account number
	 */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    /**
	 * Gets the account type.
	 * @return the account type
	 */
    public String getAccountType() {
		return accountType;
	}

	/**
	 * Sets the account type.
	 * @param accountType the new account type
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * Gets the currency.
	 * @return the currency
	 */
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
	 * Gets the profile.
	 * @return the profile
	 */
    public Profile getProfile() {
		return profile;
	}

	/**
	 * Sets the profile.
	 * @param profile the new profile
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		boolean equal = false;
		if(object instanceof CommissionAccount) {

			CommissionAccount other = (CommissionAccount) object;
			equal = other.toString().equals(toString());
		}

		return equal;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		StringBuffer buff = new StringBuffer();
		buff.append(accountNumber + "-");
		buff.append(accountType + "-");
		//buff.append(currency.getCurrencyCode());

		return buff.toString();
	}
}
