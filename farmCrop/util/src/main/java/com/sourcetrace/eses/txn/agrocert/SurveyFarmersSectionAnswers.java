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
import java.util.Set;

/**
 * The Class FarmersSectionAnswers.
 */
public class SurveyFarmersSectionAnswers implements Comparable<SurveyFarmersSectionAnswers> {

    private long id;
    private SurveyFarmerCropProdAnswers farmerCropProdAnswers;
    private String sectionCode;
    private String serialNo;
    private String sectionName;
    private String sectionType;
    private Set<SurveyFarmersQuestionAnswers> farmersQuestionAnswers;

 // Transient variable
    private List<SurveyFarmersQuestionAnswers> farmersQuestionAnswersList;
 
  
    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(SurveyFarmersSectionAnswers paramT) {

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


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public SurveyFarmerCropProdAnswers getFarmerCropProdAnswers() {
		return farmerCropProdAnswers;
	}


	public void setFarmerCropProdAnswers(SurveyFarmerCropProdAnswers farmerCropProdAnswers) {
		this.farmerCropProdAnswers = farmerCropProdAnswers;
	}


	public String getSectionCode() {
		return sectionCode;
	}


	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}


	public String getSerialNo() {
		return serialNo;
	}


	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}


	public String getSectionName() {
		return sectionName;
	}


	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}


	public String getSectionType() {
		return sectionType;
	}


	public void setSectionType(String sectionType) {
		this.sectionType = sectionType;
	}


	public Set<SurveyFarmersQuestionAnswers> getFarmersQuestionAnswers() {
		return farmersQuestionAnswers;
	}


	public void setFarmersQuestionAnswers(Set<SurveyFarmersQuestionAnswers> farmersQuestionAnswers) {
		this.farmersQuestionAnswers = farmersQuestionAnswers;
	}


	public List<SurveyFarmersQuestionAnswers> getFarmersQuestionAnswersList() {
		return farmersQuestionAnswersList;
	}


	public void setFarmersQuestionAnswersList(List<SurveyFarmersQuestionAnswers> farmersQuestionAnswersList) {
		this.farmersQuestionAnswersList = farmersQuestionAnswersList;
	}


	
}
