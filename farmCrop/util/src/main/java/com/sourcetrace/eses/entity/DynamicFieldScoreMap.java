package com.sourcetrace.eses.entity;

import java.util.Set;

import com.sourcetrace.eses.inspect.agrocert.SurveyQuestionMapping;
 
public class DynamicFieldScoreMap implements Comparable<DynamicFieldScoreMap>  {

	private long id;
	private DynamicMenuFieldMap dynamicMenuFieldMap;
	private String catalogueCode;
	private Integer score;
	private Double percentage;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public DynamicMenuFieldMap getDynamicMenuFieldMap() {
		return dynamicMenuFieldMap;
	}
	public void setDynamicMenuFieldMap(DynamicMenuFieldMap dynamicMenuFieldMap) {
		this.dynamicMenuFieldMap = dynamicMenuFieldMap;
	}
	public String getCatalogueCode() {
		return catalogueCode;
	}
	public void setCatalogueCode(String catalogueCode) {
		this.catalogueCode = catalogueCode;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	@Override
	public int compareTo(DynamicFieldScoreMap arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	
	
	
}
