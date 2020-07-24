package com.sourcetrace.eses.entity;

import java.util.Set;

import com.sourcetrace.eses.inspect.agrocert.SurveyQuestionMapping;
 
public class DynamicMenuFieldMap implements Comparable<DynamicMenuFieldMap>  {

	public static enum scoreType {
		SINGLE, MULTIPLE
	}
	
	private long id;
	private DynamicFeildMenuConfig menu;
	private DynamicFieldConfig field;
	private Integer isFilter;
	private int order;
	private Set<DynamicFieldScoreMap> dynamicFieldScoreMap;
	private Integer scoreType;
	private String branchId;
	
	private String beforeInsert;
	private String afterInsert;

	private String mBeforeInsert;
	private String mAfterInsert;

	public DynamicFeildMenuConfig getMenu() {
		return menu;
	}

	public void setMenu(DynamicFeildMenuConfig menu) {
		this.menu = menu;
	}

	public DynamicFieldConfig getField() {
		return field;
	}

	public void setField(DynamicFieldConfig field) {
		this.field = field;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public int compareTo(DynamicMenuFieldMap object) {
		// TODO Auto-generated method stub
		int value = 0;
		DynamicMenuFieldMap surveyQuestionMapping = (DynamicMenuFieldMap) object;

		if (this.order > surveyQuestionMapping.order)
			value = 1;
		else if (this.order < surveyQuestionMapping.order)
			value = -1;
		else if (this.order == surveyQuestionMapping.order)
			value = 0;
		return value;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getIsFilter() {
		return isFilter;
	}

	public void setIsFilter(Integer isFilter) {
		this.isFilter = isFilter;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Set<DynamicFieldScoreMap> getDynamicFieldScoreMap() {
		return dynamicFieldScoreMap;
	}

	public void setDynamicFieldScoreMap(Set<DynamicFieldScoreMap> dynamicFieldScoreMap) {
		this.dynamicFieldScoreMap = dynamicFieldScoreMap;
	}

	public Integer getScoreType() {
		return scoreType;
	}

	public void setScoreType(Integer scoreType) {
		this.scoreType = scoreType;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBeforeInsert() {
		return beforeInsert;
	}

	public void setBeforeInsert(String beforeInsert) {
		this.beforeInsert = beforeInsert;
	}

	public String getAfterInsert() {
		return afterInsert;
	}

	public void setAfterInsert(String afterInsert) {
		this.afterInsert = afterInsert;
	}

	public String getmBeforeInsert() {
		return mBeforeInsert;
	}

	public void setmBeforeInsert(String mBeforeInsert) {
		this.mBeforeInsert = mBeforeInsert;
	}

	public String getmAfterInsert() {
		return mAfterInsert;
	}

	public void setmAfterInsert(String mAfterInsert) {
		this.mAfterInsert = mAfterInsert;
	}
	
	
	
	
	
}
