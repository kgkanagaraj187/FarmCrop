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

import com.sourcetrace.eses.entity.Agent;

// TODO: Auto-generated Javadoc
public class AgentSurveyMapping implements Comparable<AgentSurveyMapping> {

	private long id;
	private SurveyMaster surveyMaster;
	private Agent agent;
	private int surveyOrder;

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

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public int getSurveyOrder() {
		return surveyOrder;
	}

	public void setSurveyOrder(int surveyOrder) {
		this.surveyOrder = surveyOrder;
	}

	public int compareTo(AgentSurveyMapping object) {
		// TODO Auto-generated method stub
		int value = 0;
		AgentSurveyMapping surveyQuestionMapping = (AgentSurveyMapping) object;

		if (this.surveyOrder > surveyQuestionMapping.surveyOrder)
			value = 1;
		else if (this.surveyOrder < surveyQuestionMapping.surveyOrder)
			value = -1;
		else if (this.surveyOrder == surveyQuestionMapping.surveyOrder)
			value = 0;
		return value;
	}

}
