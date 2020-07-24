/**
 * CommissionFeeGroup.java
 * Copyright (c) 2009, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.ese.entity.txn.fee;

import java.util.Currency;
import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class CommissionFeeGroup.
 * @author $Author: boopalan $
 * @version $Rev: 69 $
 */
public class CommissionFeeGroup {
    private long id;
    private String name;
    private String description;
    private Currency currency;

    /**
     * @return the currency
     */
    public Currency getCurrency() {

        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(Currency currency) {

        this.currency = currency;
    }

    private Set<CommissionFee> commissionFee;

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
     * Gets the name.
     * @return the name
     */

    @Length(max = 30, message = "length.Name")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.name")
    @NotEmpty(message = "empty.Name")
    public String getName() {

        return name;
    }

    public Set<CommissionFee> getCommissionFee() {

        return commissionFee;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the description.
     * @return the description
     */
    @Length(max = 255, message = "length.description")
    @NotEmpty(message = "empty.descname")
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

    public void setCommissionFee(Set<CommissionFee> commissionFee) {

        this.commissionFee = commissionFee;
    }

    // For Type Conversion
    public String toString() {

        return this.name;
    }

}
