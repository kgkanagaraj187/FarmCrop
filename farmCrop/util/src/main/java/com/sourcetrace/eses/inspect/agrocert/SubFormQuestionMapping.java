/*
 * SubFormQuestionMapping.java
 * Copyright (c) 2016-2017, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.inspect.agrocert;


// TODO: Auto-generated Javadoc
public class SubFormQuestionMapping implements Comparable<SubFormQuestionMapping> {

    private long id;
    private SurveyQuestion parentQuestion;
    private SurveyQuestion childQuestion;
    private int questionOrder;

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
     * Gets the parent question.
     * @return the parent question
     */
    public SurveyQuestion getParentQuestion() {

        return parentQuestion;
    }

    /**
     * Sets the parent question.
     * @param parentQuestion the new parent question
     */
    public void setParentQuestion(SurveyQuestion parentQuestion) {

        this.parentQuestion = parentQuestion;
    }

    /**
     * Gets the child question.
     * @return the child question
     */
    public SurveyQuestion getChildQuestion() {

        return childQuestion;
    }

    /**
     * Sets the child question.
     * @param childQuestion the new child question
     */
    public void setChildQuestion(SurveyQuestion childQuestion) {

        this.childQuestion = childQuestion;
    }

    /**
     * Gets the question order.
     * @return the question order
     */
    public int getQuestionOrder() {

        return questionOrder;
    }

    /**
     * Sets the question order.
     * @param questionOrder the new question order
     */
    public void setQuestionOrder(int questionOrder) {

        this.questionOrder = questionOrder;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(SubFormQuestionMapping object) {

        // TODO Auto-generated method stub
        int value = 0;
        SubFormQuestionMapping surveyQuestionMapping = (SubFormQuestionMapping) object;

        if (this.questionOrder > surveyQuestionMapping.questionOrder)
            value = 1;
        else if (this.questionOrder < surveyQuestionMapping.questionOrder)
            value = -1;
        else if (this.questionOrder == surveyQuestionMapping.questionOrder)
            value = 0;
        return value;
    }

}
