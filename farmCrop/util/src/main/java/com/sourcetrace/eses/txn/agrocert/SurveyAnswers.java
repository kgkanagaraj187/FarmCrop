/*
 * Answers.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.agrocert;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class Answers.
 */
public class SurveyAnswers implements Comparable<SurveyAnswers> {

	public static enum CONFIRMATION {
		CONFIRMED, NON_CONFIRMED
	};
	
	public static enum ANSWER_TYPE {
        CONFIRMED, SUB_FORM
    };

	private long id;
	private SurveyFarmersQuestionAnswers farmersQuestionAnswers;
	private int answerType;
	// Main Answer Code (Eg - Text box answer , Drop down code ,Check box ,
	// Radio button Code )
	private String answer;
	// Main Answer Label ( Eg - Drop down Label , Check Box Label , Radio Button
	// Label )
	private String answer1;
	// Sub Answer Code ( Eg - Drop down with other , if yes ask other question )
	private String answer2;
	// Sub Answer Label (Eg - Sub Answer Drop down Label , Sub Answer Check Box
	// Label , Sub Answer
	// Radio Button Label
	private String answer3;
	// If Unit Exist unit Code
	private String answer4;
	// If Unit Exist unit Name
	private String answer5;
	// Unit Other value
	private String answer6;

	private String followUp;
	private byte[] photo;
 
	private boolean nonConfirm;
	private  Map<String, Object> onceQuestionEntityMap = new HashMap<String, Object>();

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
	 * Gets the farmers question answers.
	 * 
	 * @return the farmers question answers
	 */
	public SurveyFarmersQuestionAnswers getFarmersQuestionAnswers() {

		return farmersQuestionAnswers;
	}

	/**
	 * Sets the farmers question answers.
	 * 
	 * @param farmersQuestionAnswers
	 *            the new farmers question answers
	 */
	public void setFarmersQuestionAnswers(SurveyFarmersQuestionAnswers farmersQuestionAnswers) {

		this.farmersQuestionAnswers = farmersQuestionAnswers;
	}

	/**
	 * Gets the answer type.
	 * 
	 * @return the answer type
	 */
	public int getAnswerType() {

		return answerType;
	}

	/**
	 * Sets the answer type.
	 * 
	 * @param answerType
	 *            the new answer type
	 */
	public void setAnswerType(int answerType) {

		this.answerType = answerType;
	}

	/**
	 * Gets the answer.
	 * 
	 * @return the answer
	 */
	public String getAnswer() {

		return answer;
	}

	/**
	 * Sets the answer.
	 * 
	 * @param answer
	 *            the new answer
	 */
	public void setAnswer(String answer) {

		this.answer = answer;
	}

	/**
	 * Gets the answer1.
	 * 
	 * @return the answer1
	 */
	public String getAnswer1() {

		return answer1;
	}

	/**
	 * Sets the answer1.
	 * 
	 * @param answer1
	 *            the new answer1
	 */
	public void setAnswer1(String answer1) {

		this.answer1 = answer1;
	}

	/**
	 * Gets the answer2.
	 * 
	 * @return the answer2
	 */
	public String getAnswer2() {

		return answer2;
	}

	/**
	 * Sets the answer2.
	 * 
	 * @param answer2
	 *            the new answer2
	 */
	public void setAnswer2(String answer2) {

		this.answer2 = answer2;
	}

	/**
	 * Gets the answer3.
	 * 
	 * @return the answer3
	 */
	public String getAnswer3() {

		return answer3;
	}

	/**
	 * Sets the answer3.
	 * 
	 * @param answer3
	 *            the new answer3
	 */
	public void setAnswer3(String answer3) {

		this.answer3 = answer3;
	}

	/**
	 * Gets the answer4.
	 * 
	 * @return the answer4
	 */
	public String getAnswer4() {

		return answer4;
	}

	/**
	 * Sets the answer4.
	 * 
	 * @param answer4
	 *            the new answer4
	 */
	public void setAnswer4(String answer4) {

		this.answer4 = answer4;
	}

	/**
	 * Gets the answer5.
	 * 
	 * @return the answer5
	 */
	public String getAnswer5() {

		return answer5;
	}

	/**
	 * Sets the answer5.
	 * 
	 * @param answer5
	 *            the new answer5
	 */
	public void setAnswer5(String answer5) {

		this.answer5 = answer5;
	}

	/**
	 * Gets the follow up.
	 * 
	 * @return the follow up
	 */
	public String getFollowUp() {

		return followUp;
	}

	/**
	 * Sets the follow up.
	 * 
	 * @param followUp
	 *            the new follow up
	 */
	public void setFollowUp(String followUp) {

		this.followUp = followUp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SurveyAnswers paramT) {

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

	/**
	 * Sets the answer6.
	 * 
	 * @param answer6
	 *            the new answer6
	 */
	public void setAnswer6(String answer6) {

		this.answer6 = answer6;
	}

	/**
	 * Gets the answer6.
	 * 
	 * @return the answer6
	 */
	public String getAnswer6() {

		return answer6;
	}

	public boolean isNonConfirm() {
		return nonConfirm;
	}

	public void setNonConfirm(boolean nonConfirm) {
		this.nonConfirm = nonConfirm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result + ((answer1 == null) ? 0 : answer1.hashCode());
		result = prime * result + ((answer2 == null) ? 0 : answer2.hashCode());
		result = prime * result + ((answer3 == null) ? 0 : answer3.hashCode());
		result = prime * result + ((answer4 == null) ? 0 : answer4.hashCode());
		result = prime * result + ((answer5 == null) ? 0 : answer5.hashCode());
		result = prime * result + ((answer6 == null) ? 0 : answer6.hashCode());
		result = prime * result + (nonConfirm ? 1231 : 1237);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SurveyAnswers other = (SurveyAnswers) obj;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		if (answer1 == null) {
			if (other.answer1 != null)
				return false;
		} else if (!answer1.equals(other.answer1))
			return false;
		if (answer2 == null) {
			if (other.answer2 != null)
				return false;
		} else if (!answer2.equals(other.answer2))
			return false;
		if (answer3 == null) {
			if (other.answer3 != null)
				return false;
		} else if (!answer3.equals(other.answer3))
			return false;
		if (answer4 == null) {
			if (other.answer4 != null)
				return false;
		} else if (!answer4.equals(other.answer4))
			return false;
		if (answer5 == null) {
			if (other.answer5 != null)
				return false;
		} else if (!answer5.equals(other.answer5))
			return false;
		if (answer6 == null) {
			if (other.answer6 != null)
				return false;
		} else if (!answer6.equals(other.answer6))
			return false;
		if (nonConfirm != other.nonConfirm)
			return false;
		return true;
	}

	public Map<String, Object> getOnceQuestionEntityMap() {
		return onceQuestionEntityMap;
	}

	public void setOnceQuestionEntityMap(Map<String, Object> onceQuestionEntityMap) {
		this.onceQuestionEntityMap = onceQuestionEntityMap;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	

}
