/*
 * FarmersQuestionAnswers.java
 * Copyright (c) 2016-2017, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.agrocert;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.inspect.agrocert.SubFormAnswerMapping;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestion;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class FarmersQuestionAnswers.
 */
public class SurveyFarmersQuestionAnswers implements Comparable<SurveyFarmersQuestionAnswers> {

    private long id;
    private SurveyFarmersSectionAnswers farmersSectionAnswers;
    private String questionCode;
    private String serialNo;
    private String questionName;
    private String registeredQuestion;
    private String questionType;
    private byte[] image;
    private String latitude;
    private String longtitude;
    private Date GPSCaptureDateTime;
    private String followUp;
    private SortedSet<SurveyAnswers> answers;
    private SurveyFarmersQuestionAnswers parentQuestionAnswers;
    private SortedSet<SurveyFarmersQuestionAnswers> subQuestions;
     private SurveyQuestion question;
    private int questionOrder;
    private int componentType;
      private SortedSet<SubFormAnswerMapping> subFormAnswers;
   private List<SurveyAnswers> answersList;
    private LinkedList<SurveyFarmersQuestionAnswers> subQuestionList;
    // privatr List<>
    private String info;

    private String maxLength;
    private int validationType;
    private String listMethodName;
    private String defaultUnit;
    private String otherCatalogValue;
    private String unitOtherCatalogValue;
    private String dependencyKey;
    private Set<FarmCatalogue> answerKeys;
    private Set<FarmCatalogue> units;
    private Set<FarmCatalogue> defaultValues;

    private String questionMasterId;
    private String parentQuestionId;
    private String dependentJSFunction;
    private String componentClass;
    private String tableRowClass;
    private String tableRowId;
    private boolean hasSubQuestions;
    private boolean childQuestion;
    private String parentQuestionCode;
    private String photoByteString;
    private File imageByteArray;
    private String imageByteArrayContentType;
    private String imageByteArrayFileName;
    private boolean nonConformity;
    private float score;
    private List<SurveyAnswers> vanswers;
    private List<SurveyAnswers> ansList;
    private SurveyAnswers ans;
    private Map<Integer,LinkedList<SurveyAnswers>>  subQuestionAnswers;

   
    /**
     * Gets the doubles float no max length.
     * @return the doubles float no max length
     */
    public int getDoublesFloatNoMaxLength() {

        int length = 3;
        if (this.maxLength != null && maxLength.contains(",")) {
            length = Integer.valueOf(maxLength.split(",")[1]);
        }
        return length;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(SurveyFarmersQuestionAnswers object) {

        int value = 0;
        SurveyFarmersQuestionAnswers farmersQuestionAnswers = (SurveyFarmersQuestionAnswers) object;

        if (this.questionOrder > farmersQuestionAnswers.questionOrder)
            value = 1;
        else if (this.questionOrder < farmersQuestionAnswers.questionOrder)
            value = -1;
        else if (this.questionOrder == farmersQuestionAnswers.questionOrder)
            value = 0;

        return value;
    }

  
    /**
     * Gets the other answer.
     * @param answers the answers
     * @return the other answer
     */
    private String getOtherAnswer(List<SurveyAnswers> answers) {

        if ((componentType == 4 || componentType == 8) && !isNonConfirmed()) {
            if (!ObjectUtil.isListEmpty(answers)) {
                SurveyAnswers answers2 = answers.get(0);
                if (!StringUtil.isEmpty(question.getListMethodName())
                        && answers2.getAnswer().equals(question.getOtherCatalogValue())) {
                    return "Other : " + answers2.getAnswer1();
                } else if (answers2.getAnswer().equals(question.getOtherCatalogValue())) {
                    return answers2.getAnswer1()
                            + " : "
                            + (ObjectUtil.isEmpty(answers2.getAnswer2()) ? "" : answers2
                                    .getAnswer2());
                } else {
                    return answers2.getAnswer1();
                }
            }
        }
        return "";
    }

    /**
     * Checks if is non confirmed.
     * @return true, if is non confirmed
     */
    public boolean isNonConfirmed() {

        for (SurveyAnswers answers : getAnswers()) {
            return answers.isNonConfirm();
        }
        return false;
    }

 
    public boolean isMatched() {

        if (!ObjectUtil.isListEmpty(vanswers) && !ObjectUtil.isListEmpty(answers)
                && answers.size() == vanswers.size()) {
            for (SurveyAnswers answer : answers) {
                answer = (SurveyAnswers) ReflectUtil.setEmptyStringToNull(answer);
                vanswers = ReflectUtil.setListObjEmptyStringToNull(vanswers);
                if (!vanswers.contains(answer)) {
                    return false;
                }
                // for (Answers vanswer : vanswers) {
                // vanswer = (Answers)
                // ReflectUtil.setEmptyStringToNull(vanswer);
                // if (!vanswer.equals(answer)) {
                // return false;
                // }
                // }
            }
            return true;
        }
        return false;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SurveyFarmersSectionAnswers getFarmersSectionAnswers() {
		return farmersSectionAnswers;
	}

	public void setFarmersSectionAnswers(SurveyFarmersSectionAnswers farmersSectionAnswers2) {
		this.farmersSectionAnswers = farmersSectionAnswers2;
	}

	public String getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public String getRegisteredQuestion() {
		return registeredQuestion;
	}

	public void setRegisteredQuestion(String registeredQuestion) {
		this.registeredQuestion = registeredQuestion;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public Date getGPSCaptureDateTime() {
		return GPSCaptureDateTime;
	}

	public void setGPSCaptureDateTime(Date gPSCaptureDateTime) {
		GPSCaptureDateTime = gPSCaptureDateTime;
	}

	public String getFollowUp() {
		return followUp;
	}

	public void setFollowUp(String followUp) {
		this.followUp = followUp;
	}

	public SortedSet<SurveyAnswers> getAnswers() {
		return answers;
	}

	public void setAnswers(SortedSet<SurveyAnswers> answers) {
		this.answers = answers;
	}

	public SurveyFarmersQuestionAnswers getParentQuestionAnswers() {
		return parentQuestionAnswers;
	}

	public void setParentQuestionAnswers(SurveyFarmersQuestionAnswers parentQuestionAnswers) {
		this.parentQuestionAnswers = parentQuestionAnswers;
	}

	public SortedSet<SurveyFarmersQuestionAnswers> getSubQuestions() {
		return subQuestions;
	}

	public void setSubQuestions(SortedSet<SurveyFarmersQuestionAnswers> subQuestions) {
		this.subQuestions = subQuestions;
	}

	
	public SurveyQuestion getQuestion() {
		return question;
	}

	public void setQuestion(SurveyQuestion question) {
		this.question = question;
	}

	public int getQuestionOrder() {
		return questionOrder;
	}

	public void setQuestionOrder(int questionOrder) {
		this.questionOrder = questionOrder;
	}

	public int getComponentType() {
		return componentType;
	}

	public void setComponentType(int componentType) {
		this.componentType = componentType;
	}

	public SortedSet<SubFormAnswerMapping> getSubFormAnswers() {
		return subFormAnswers;
	}

	public void setSubFormAnswers(SortedSet<SubFormAnswerMapping> subFormAnswers) {
		this.subFormAnswers = subFormAnswers;
	}


	public LinkedList<SurveyFarmersQuestionAnswers> getSubQuestionList() {
		return subQuestionList;
	}

	public void setSubQuestionList(LinkedList<SurveyFarmersQuestionAnswers> subQuestionList) {
		this.subQuestionList = subQuestionList;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public int getValidationType() {
		return validationType;
	}

	public void setValidationType(int validationType) {
		this.validationType = validationType;
	}

	public String getListMethodName() {
		return listMethodName;
	}

	public void setListMethodName(String listMethodName) {
		this.listMethodName = listMethodName;
	}

	public String getDefaultUnit() {
		return defaultUnit;
	}

	public void setDefaultUnit(String defaultUnit) {
		this.defaultUnit = defaultUnit;
	}

	public String getOtherCatalogValue() {
		return otherCatalogValue;
	}

	public void setOtherCatalogValue(String otherCatalogValue) {
		this.otherCatalogValue = otherCatalogValue;
	}

	public String getUnitOtherCatalogValue() {
		return unitOtherCatalogValue;
	}

	public void setUnitOtherCatalogValue(String unitOtherCatalogValue) {
		this.unitOtherCatalogValue = unitOtherCatalogValue;
	}

	public String getDependencyKey() {
		return dependencyKey;
	}

	public void setDependencyKey(String dependencyKey) {
		this.dependencyKey = dependencyKey;
	}

	public Set<FarmCatalogue> getAnswerKeys() {
		return answerKeys;
	}

	public void setAnswerKeys(Set<FarmCatalogue> answerKeys) {
		this.answerKeys = answerKeys;
	}

	public Set<FarmCatalogue> getUnits() {
		return units;
	}

	public void setUnits(Set<FarmCatalogue> units) {
		this.units = units;
	}

	public Set<FarmCatalogue> getDefaultValues() {
		return defaultValues;
	}

	public void setDefaultValues(Set<FarmCatalogue> defaultValues) {
		this.defaultValues = defaultValues;
	}

	public String getQuestionMasterId() {
		return questionMasterId;
	}

	public void setQuestionMasterId(String questionMasterId) {
		this.questionMasterId = questionMasterId;
	}

	public String getParentQuestionId() {
		return parentQuestionId;
	}

	public void setParentQuestionId(String parentQuestionId) {
		this.parentQuestionId = parentQuestionId;
	}

	public String getDependentJSFunction() {
		return dependentJSFunction;
	}

	public void setDependentJSFunction(String dependentJSFunction) {
		this.dependentJSFunction = dependentJSFunction;
	}

	public String getComponentClass() {
		return componentClass;
	}

	public void setComponentClass(String componentClass) {
		this.componentClass = componentClass;
	}

	public String getTableRowClass() {
		return tableRowClass;
	}

	public void setTableRowClass(String tableRowClass) {
		this.tableRowClass = tableRowClass;
	}

	public String getTableRowId() {
		return tableRowId;
	}

	public void setTableRowId(String tableRowId) {
		this.tableRowId = tableRowId;
	}

	public boolean isHasSubQuestions() {
		return hasSubQuestions;
	}

	public void setHasSubQuestions(boolean hasSubQuestions) {
		this.hasSubQuestions = hasSubQuestions;
	}

	public boolean isChildQuestion() {
		return childQuestion;
	}

	public void setChildQuestion(boolean childQuestion) {
		this.childQuestion = childQuestion;
	}

	public String getParentQuestionCode() {
		return parentQuestionCode;
	}

	public void setParentQuestionCode(String parentQuestionCode) {
		this.parentQuestionCode = parentQuestionCode;
	}

	public String getPhotoByteString() {
		return photoByteString;
	}

	public void setPhotoByteString(String photoByteString) {
		this.photoByteString = photoByteString;
	}

	public File getImageByteArray() {
		return imageByteArray;
	}

	public void setImageByteArray(File imageByteArray) {
		this.imageByteArray = imageByteArray;
	}

	public String getImageByteArrayContentType() {
		return imageByteArrayContentType;
	}

	public void setImageByteArrayContentType(String imageByteArrayContentType) {
		this.imageByteArrayContentType = imageByteArrayContentType;
	}

	public String getImageByteArrayFileName() {
		return imageByteArrayFileName;
	}

	public void setImageByteArrayFileName(String imageByteArrayFileName) {
		this.imageByteArrayFileName = imageByteArrayFileName;
	}

	public boolean isNonConformity() {
		return nonConformity;
	}

	public void setNonConformity(boolean nonConformity) {
		this.nonConformity = nonConformity;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public List<SurveyAnswers> getVanswers() {
		return vanswers;
	}

	public void setVanswers(List<SurveyAnswers> vanswers) {
		this.vanswers = vanswers;
	}

	public SurveyAnswers getAns() {
		return ans;
	}

	public void setAns(SurveyAnswers ans) {
		this.ans = ans;
	}

	public Map<Integer, LinkedList<SurveyAnswers>> getSubQuestionAnswers() {
		return subQuestionAnswers;
	}

	public void setSubQuestionAnswers(Map<Integer, LinkedList<SurveyAnswers>> subQuestionAnswers) {
		this.subQuestionAnswers = subQuestionAnswers;
	}

	public List<SurveyAnswers> getAnsList() {
		return ansList;
	}

	public void setAnsList(List<SurveyAnswers> ansList) {
		this.ansList = ansList;
	}

	public List<SurveyAnswers> getAnswersList() {
		return answersList;
	}

	public void setAnswersList(List<SurveyAnswers> answersList) {
		this.answersList = answersList;
	}

  
    
}
