/**
 * CredentialGroup.java
 * Copyright (c) 2009, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.ese.entity.credential;

import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class CredentialGroup.
 * @author $Author: aravind $
 * @version $Rev: 59 $
 */
public class CredentialGroup {
    private long id;
    private String name;
    private String description;
    private Set<TxnCredential> txnCredential;

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

    @Length(max = 45, message = "length.name")
    @NotEmpty(message = "empty.tempname")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.name")
    public String getName() {

        return name;
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

    /**
     * Gets the txn credential.
     * @return the txn credential
     */
    public Set<TxnCredential> getTxnCredential() {

        return txnCredential;
    }

    /**
     * Sets the txn credential.
     * @param txnCredential the new txn credential
     */
    public void setTxnCredential(Set<TxnCredential> txnCredential) {

        this.txnCredential = txnCredential;
    }

    // For Type Conversion
    public String toString() {

        return String.valueOf(this.id);
    }
}
