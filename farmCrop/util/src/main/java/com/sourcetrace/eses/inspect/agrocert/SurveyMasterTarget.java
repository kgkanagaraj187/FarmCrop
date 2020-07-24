package com.sourcetrace.eses.inspect.agrocert;

import java.util.Date;
import java.util.Set;

public class SurveyMasterTarget {

	public static enum STATUS {
		INACTIVE, ACTIVE
	};

	private int id;
	private SurveyMaster surveyMaster;
	private long targetValue;
	private String createUserName;
	private Date createdTime;
	private long revisionNumber;
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SurveyMaster getSurveyMaster() {
		return surveyMaster;
	}

	public void setSurveyMaster(SurveyMaster surveyMaster) {
		this.surveyMaster = surveyMaster;
	}

	public long getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(long targetValue) {
		this.targetValue = targetValue;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public long getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(long revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
