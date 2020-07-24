/*
 * CustomerProjectSequence.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.entity.util;

/**
 * The Class CustomerProjectSequence.
 */
public class CustomerProjectSequence {

    private long id;
    private String customerId;
    private String projectCodeSeq;

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
     * Gets the customer id.
     * @return the customer id
     */
    public String getCustomerId() {

        return customerId;
    }

    /**
     * Sets the customer id.
     * @param customerId the new customer id
     */
    public void setCustomerId(String customerId) {

        this.customerId = customerId;
    }

    /**
     * Gets the project code seq.
     * @return the project code seq
     */
    public String getProjectCodeSeq() {

        return projectCodeSeq;
    }

    /**
     * Sets the project code seq.
     * @param projectCodeSeq the new project code seq
     */
    public void setProjectCodeSeq(String projectCodeSeq) {

        this.projectCodeSeq = projectCodeSeq;
    }

}
