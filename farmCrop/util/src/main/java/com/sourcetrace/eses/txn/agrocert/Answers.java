/*
 * Answers.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.agrocert;

/**
 * The Class Answers.
 */
public class Answers implements Comparable<Answers> {

    private long id;
    private FarmersQuestionAnswers farmersQuestionAnswers;
    private int answerType;
    private String answer;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String followUp;

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
     * Gets the farmers question answers.
     * @return the farmers question answers
     */
    public FarmersQuestionAnswers getFarmersQuestionAnswers() {

        return farmersQuestionAnswers;
    }

    /**
     * Sets the farmers question answers.
     * @param farmersQuestionAnswers the new farmers question answers
     */
    public void setFarmersQuestionAnswers(FarmersQuestionAnswers farmersQuestionAnswers) {

        this.farmersQuestionAnswers = farmersQuestionAnswers;
    }

    /**
     * Gets the answer type.
     * @return the answer type
     */
    public int getAnswerType() {

        return answerType;
    }

    /**
     * Sets the answer type.
     * @param answerType the new answer type
     */
    public void setAnswerType(int answerType) {

        this.answerType = answerType;
    }

    /**
     * Gets the answer.
     * @return the answer
     */
    public String getAnswer() {

        return answer;
    }

    /**
     * Sets the answer.
     * @param answer the new answer
     */
    public void setAnswer(String answer) {

        this.answer = answer;
    }

    /**
     * Gets the answer1.
     * @return the answer1
     */
    public String getAnswer1() {

        return answer1;
    }

    /**
     * Sets the answer1.
     * @param answer1 the new answer1
     */
    public void setAnswer1(String answer1) {

        this.answer1 = answer1;
    }

    /**
     * Gets the answer2.
     * @return the answer2
     */
    public String getAnswer2() {

        return answer2;
    }

    /**
     * Sets the answer2.
     * @param answer2 the new answer2
     */
    public void setAnswer2(String answer2) {

        this.answer2 = answer2;
    }

    /**
     * Gets the answer3.
     * @return the answer3
     */
    public String getAnswer3() {

        return answer3;
    }

    /**
     * Sets the answer3.
     * @param answer3 the new answer3
     */
    public void setAnswer3(String answer3) {

        this.answer3 = answer3;
    }

    /**
     * Gets the answer4.
     * @return the answer4
     */
    public String getAnswer4() {

        return answer4;
    }

    /**
     * Sets the answer4.
     * @param answer4 the new answer4
     */
    public void setAnswer4(String answer4) {

        this.answer4 = answer4;
    }

    /**
     * Gets the answer5.
     * @return the answer5
     */
    public String getAnswer5() {

        return answer5;
    }

    /**
     * Sets the answer5.
     * @param answer5 the new answer5
     */
    public void setAnswer5(String answer5) {

        this.answer5 = answer5;
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

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Answers paramT) {

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

}
