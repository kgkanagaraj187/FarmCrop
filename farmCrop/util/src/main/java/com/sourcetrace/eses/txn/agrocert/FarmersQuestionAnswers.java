/*
 * FarmersQuestionAnswers.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.agrocert;

import java.util.Date;
import java.util.List;
import java.util.SortedSet;

/**
 * The Class FarmersQuestionAnswers.
 */
public class FarmersQuestionAnswers implements Comparable<FarmersQuestionAnswers> {

    private long id;
    private FarmersSectionAnswers farmersSectionAnswers;
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
    private SortedSet<Answers> answers;
    private FarmersQuestionAnswers parentQuestionAnswers;
    private SortedSet<FarmersQuestionAnswers> subQuestions;

    
    // Transient variable
    private List<Answers> answersList;
    private Answers answer;
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
     * Gets the farmers section answers.
     * @return the farmers section answers
     */
    public FarmersSectionAnswers getFarmersSectionAnswers() {

        return farmersSectionAnswers;
    }

    /**
     * Sets the farmers section answers.
     * @param farmersSectionAnswers the new farmers section answers
     */
    public void setFarmersSectionAnswers(FarmersSectionAnswers farmersSectionAnswers) {

        this.farmersSectionAnswers = farmersSectionAnswers;
    }

    /**
     * Gets the question code.
     * @return the question code
     */
    public String getQuestionCode() {

        return questionCode;
    }

    /**
     * Sets the question code.
     * @param questionCode the new question code
     */
    public void setQuestionCode(String questionCode) {

        this.questionCode = questionCode;
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
     * Gets the question name.
     * @return the question name
     */
    public String getQuestionName() {

        return questionName;
    }

    /**
     * Sets the question name.
     * @param questionName the new question name
     */
    public void setQuestionName(String questionName) {

        this.questionName = questionName;
    }

    /**
     * Gets the registered question.
     * @return the registered question
     */
    public String getRegisteredQuestion() {

        return registeredQuestion;
    }

    /**
     * Sets the registered question.
     * @param registeredQuestion the new registered question
     */
    public void setRegisteredQuestion(String registeredQuestion) {

        this.registeredQuestion = registeredQuestion;
    }

    /**
     * Gets the question type.
     * @return the question type
     */
    public String getQuestionType() {

        return questionType;
    }

    /**
     * Sets the question type.
     * @param questionType the new question type
     */
    public void setQuestionType(String questionType) {

        this.questionType = questionType;
    }

    /**
     * Gets the image.
     * @return the image
     */
    public byte[] getImage() {

        return image;
    }

    /**
     * Sets the image.
     * @param image the new image
     */
    public void setImage(byte[] image) {

        this.image = image;
    }

    /**
     * Gets the latitude.
     * @return the latitude
     */
    public String getLatitude() {

        return latitude;
    }

    /**
     * Sets the latitude.
     * @param latitude the new latitude
     */
    public void setLatitude(String latitude) {

        this.latitude = latitude;
    }

    /**
     * Gets the longtitude.
     * @return the longtitude
     */
    public String getLongtitude() {

        return longtitude;
    }

    /**
     * Sets the longtitude.
     * @param longtitude the new longtitude
     */
    public void setLongtitude(String longtitude) {

        this.longtitude = longtitude;
    }

    /**
     * Gets the gPS capture date time.
     * @return the gPS capture date time
     */
    public Date getGPSCaptureDateTime() {

        return GPSCaptureDateTime;
    }

    /**
     * Sets the gPS capture date time.
     * @param gPSCaptureDateTime the new gPS capture date time
     */
    public void setGPSCaptureDateTime(Date gPSCaptureDateTime) {

        GPSCaptureDateTime = gPSCaptureDateTime;
    }

    /**
     * Gets the follow up.
     * @return the follow up
     */
    public String getFollowUp() {

        return followUp;
    }

    /**
     * Sets the follow up.
     * @param followUp the new follow up
     */
    public void setFollowUp(String followUp) {

        this.followUp = followUp;
    }

    /**
     * Gets the answers.
     * @return the answers
     */
    public SortedSet<Answers> getAnswers() {

        return answers;
    }

    /**
     * Sets the answers.
     * @param answers the new answers
     */
    public void setAnswers(SortedSet<Answers> answers) {

        this.answers = answers;
    }

    /**
     * Gets the parent question answers.
     * @return the parent question answers
     */
    public FarmersQuestionAnswers getParentQuestionAnswers() {

        return parentQuestionAnswers;
    }

    /**
     * Sets the parent question answers.
     * @param parentQuestionAnswers the new parent question answers
     */
    public void setParentQuestionAnswers(FarmersQuestionAnswers parentQuestionAnswers) {

        this.parentQuestionAnswers = parentQuestionAnswers;
    }

    /**
     * Gets the sub questions.
     * @return the sub questions
     */
    public SortedSet<FarmersQuestionAnswers> getSubQuestions() {

        return subQuestions;
    }

    /**
     * Sets the sub questions.
     * @param subQuestions the new sub questions
     */
    public void setSubQuestions(SortedSet<FarmersQuestionAnswers> subQuestions) {

        this.subQuestions = subQuestions;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(FarmersQuestionAnswers paramT) {

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

	public List<Answers> getAnswersList() {
		return answersList;
	}

	public void setAnswersList(List<Answers> answersList) {
		this.answersList = answersList;
	}

	public Answers getAnswer() {
		return answer;
	}

	public void setAnswer(Answers answer) {
		this.answer = answer;
	}
	
	
}
