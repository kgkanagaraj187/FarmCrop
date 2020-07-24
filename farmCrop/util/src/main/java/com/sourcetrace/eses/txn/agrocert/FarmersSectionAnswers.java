/*
 * FarmersSectionAnswers.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.agrocert;

import java.util.List;
import java.util.SortedSet;

/**
 * The Class FarmersSectionAnswers.
 */
public class FarmersSectionAnswers implements Comparable<FarmersSectionAnswers> {

    private long id;
    private FarmerCropProdAnswers farmerCropProdAnswers;
    private String sectionCode;
    private String serialNo;
    private String sectionName;
    private String sectionType;
    private SortedSet<FarmersQuestionAnswers> farmersQuestionAnswers;

 // Transient variable
    private List<FarmersQuestionAnswers> farmersQuestionAnswersList;
 
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

    /**
     * Gets the section code.
     * @return the section code
     */
    public String getSectionCode() {

        return sectionCode;
    }

    /**
     * Sets the section code.
     * @param sectionCode the new section code
     */
    public void setSectionCode(String sectionCode) {

        this.sectionCode = sectionCode;
    }

    /**
     * Gets the serial no.
     * @return the serial no
     */
    public String getSerialNo() {

        return serialNo;
    }

    /**
     * Sets the serial no.
     * @param serialNo the new serial no
     */
    public void setSerialNo(String serialNo) {

        this.serialNo = serialNo;
    }

    /**
     * Gets the section name.
     * @return the section name
     */
    public String getSectionName() {

        return sectionName;
    }

    /**
     * Sets the section name.
     * @param sectionName the new section name
     */
    public void setSectionName(String sectionName) {

        this.sectionName = sectionName;
    }

    /**
     * Gets the section type.
     * @return the section type
     */
    public String getSectionType() {

        return sectionType;
    }

    /**
     * Sets the section type.
     * @param sectionType the new section type
     */
    public void setSectionType(String sectionType) {

        this.sectionType = sectionType;
    }

    /**
     * Gets the farmers question answers.
     * @return the farmers question answers
     */
    public SortedSet<FarmersQuestionAnswers> getFarmersQuestionAnswers() {

        return farmersQuestionAnswers;
    }

    /**
     * Sets the farmers question answers.
     * @param farmersQuestionAnswers the new farmers question answers
     */
    public void setFarmersQuestionAnswers(SortedSet<FarmersQuestionAnswers> farmersQuestionAnswers) {

        this.farmersQuestionAnswers = farmersQuestionAnswers;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(FarmersSectionAnswers paramT) {

        int returnValue = 1;
        if (this.id != 0 && paramT.id != 0) {
            if (this.id < paramT.id) {
                returnValue = -1;
            } else if (this.id == paramT.id) {
                returnValue = 0;
            }
        }
        return returnValue;
    }

	public List<FarmersQuestionAnswers> getFarmersQuestionAnswersList() {
		return farmersQuestionAnswersList;
	}

	public void setFarmersQuestionAnswersList(List<FarmersQuestionAnswers> farmersQuestionAnswersList) {
		this.farmersQuestionAnswersList = farmersQuestionAnswersList;
	}

	
}
