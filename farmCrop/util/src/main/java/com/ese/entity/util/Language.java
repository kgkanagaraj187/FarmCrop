package com.ese.entity.util;

public class Language {
	private long id;
	private String code;
	private String name;
	private int webStatus;
	private int surveyStatus;
	
	public static final int WEB_STATUS_ACTIVE = 1;
	public static final int WEB_STATUS_IN_ACTIVE = 0;
	public static final int SURVEY_STATUS_ACTIVE = 1;
	public static final int SURVEY_STATUS_IN_ACTIVE = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWebStatus() {
		return webStatus;
	}

	public void setWebStatus(int webStatus) {
		this.webStatus = webStatus;
	}

	public int getSurveyStatus() {
		return surveyStatus;
	}

	public void setSurveyStatus(int surveyStatus) {
		this.surveyStatus = surveyStatus;
	}
	
	
	 

}
