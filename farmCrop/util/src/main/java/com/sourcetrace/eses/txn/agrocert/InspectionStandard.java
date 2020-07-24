/*
 * InspectionStandard.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.agrocert;

/**
 * The Class InspectionStandard.
 */
public class InspectionStandard {

    private long id;
    private String standardCode;
    private String standardName;
    private FarmerCropProdAnswers farmerCropProdAnswers;

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
     * Gets the standard code.
     * @return the standard code
     */
    public String getStandardCode() {

        return standardCode;
    }

    /**
     * Sets the standard code.
     * @param standardCode the new standard code
     */
    public void setStandardCode(String standardCode) {

        this.standardCode = standardCode;
    }

    /**
     * Gets the standard name.
     * @return the standard name
     */
    public String getStandardName() {

        return standardName;
    }

    /**
     * Sets the standard name.
     * @param standardName the new standard name
     */
    public void setStandardName(String standardName) {

        this.standardName = standardName;
    }

    /**
     * Gets the farmer crop prod answers.
     * @return the farmer crop prod answers
     */
    public FarmerCropProdAnswers getFarmerCropProdAnswers() {

        return farmerCropProdAnswers;
    }

    /**
     * Sets the farmer crop prod answers.
     * @param farmerCropProdAnswers the new farmer crop prod answers
     */
    public void setFarmerCropProdAnswers(FarmerCropProdAnswers farmerCropProdAnswers) {

        this.farmerCropProdAnswers = farmerCropProdAnswers;
    }

}
