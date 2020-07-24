/*
 * InspectionImageInfo.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.order.entity.profile;

import java.util.Set;

public class InspectionImageInfo {

    private long id;
    private String txnType;
    private Set<InspectionImage> inspectionImages;

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
     * Gets the txn type.
     * @return the txn type
     */
    public String getTxnType() {

        return txnType;
    }

    /**
     * Sets the txn type.
     * @param txnType the new txn type
     */
    public void setTxnType(String txnType) {

        this.txnType = txnType;
    }

    /**
     * Gets the inspection images.
     * @return the inspection images
     */
    public Set<InspectionImage> getInspectionImages() {

        return inspectionImages;
    }

    /**
     * Sets the inspection images.
     * @param inspectionImages the new inspection images
     */
    public void setInspectionImages(Set<InspectionImage> inspectionImages) {

        this.inspectionImages = inspectionImages;
    }

}
