package com.sourcetrace.eses.entity;

import java.util.Set;

public class DynamicMenuSectionMap implements Comparable<DynamicMenuSectionMap>  {
	private long id;

	private DynamicFeildMenuConfig menu;
	private DynamicSectionConfig section;
	private Integer order;
	private String branchId;
	private String mBeforeInsert;
	private String mAfterInsert;
	private String afterInsert;
	private String beforeInsert;
	public DynamicFeildMenuConfig getMenu() {
		return menu;
	}

	public void setMenu(DynamicFeildMenuConfig menu) {
		this.menu = menu;
	}

	public DynamicSectionConfig getSection() {
		return section;
	}

	public void setSection(DynamicSectionConfig section) {
		this.section = section;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public int compareTo(DynamicMenuSectionMap object) {
		// TODO Auto-generated method stub
		int value = 0;
		DynamicMenuSectionMap surveyQuestionMapping = (DynamicMenuSectionMap) object;

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

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
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

	public String getAfterInsert() {
		return afterInsert;
	}

	public void setAfterInsert(String afterInsert) {
		this.afterInsert = afterInsert;
	}

	public String getBeforeInsert() {
		return beforeInsert;
	}

	public void setBeforeInsert(String beforeInsert) {
		this.beforeInsert = beforeInsert;
	}
	
	

}
