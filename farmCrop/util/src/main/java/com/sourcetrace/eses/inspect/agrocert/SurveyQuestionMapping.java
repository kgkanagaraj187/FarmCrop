/*
 * SurveyMaster.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.inspect.agrocert;

// TODO: Auto-generated Javadoc
public class SurveyQuestionMapping implements Comparable<SurveyQuestionMapping> {

	private long id;
	private SurveyMaster surveyMaster;
	private SurveyQuestion question;
	private int questionOrder;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {

		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {

		this.id = id;
	}

	/**
	 * Gets the survey master.
	 * 
	 * @return the survey master
	 */
	public SurveyMaster getSurveyMaster() {

		return surveyMaster;
	}

	/**
	 * Sets the survey master.
	 * 
	 * @param surveyMaster
	 *            the new survey master
	 */
	public void setSurveyMaster(SurveyMaster surveyMaster) {

		this.surveyMaster = surveyMaster;
	}

	/**
	 * Gets the question.
	 * 
	 * @return the question
	 */
	public SurveyQuestion getQuestion() {

		return question;
	}

	/**
	 * Sets the question.
	 * 
	 * @param question
	 *            the new question
	 */
	public void setQuestion(SurveyQuestion question) {

		this.question = question;
	}

	/**
	 * Gets the question order.
	 * 
	 * @return the question order
	 */
	public int getQuestionOrder() {

		return questionOrder;
	}

	/**
	 * Sets the question order.
	 * 
	 * @param questionOrder
	 *            the new question order
	 */
	public void setQuestionOrder(int questionOrder) {

		this.questionOrder = questionOrder;
	}

	public int compareTo(SurveyQuestionMapping object) {
		// TODO Auto-generated method stub
		int value = 0;
		SurveyQuestionMapping surveyQuestionMapping = (SurveyQuestionMapping) object;

		if (this.questionOrder > surveyQuestionMapping.questionOrder)
			value = 1;
		else if (this.questionOrder < surveyQuestionMapping.questionOrder)
			value = -1;
		else if (this.questionOrder == surveyQuestionMapping.questionOrder)
			value = 0;
		return value;
	}

}
