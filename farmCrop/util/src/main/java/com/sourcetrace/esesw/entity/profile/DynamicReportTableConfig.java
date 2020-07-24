package com.sourcetrace.esesw.entity.profile;

import java.util.Set;

public class DynamicReportTableConfig {
	
	private long id;
	private String entityName;
	private String table;
	private String alias;
	private DynamicReportTableConfig parent;
	private String joinString;
	private String entityQuery;
	private Set<DynamicReportFieldsConfig> dynamicReportFieldsConfig;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public DynamicReportTableConfig getParent() {
		return parent;
	}
	public void setParent(DynamicReportTableConfig parent) {
		this.parent = parent;
	}
	public String getJoinString() {
		return joinString;
	}
	public void setJoinString(String joinString) {
		this.joinString = joinString;
	}
	public String getEntityQuery() {
		return entityQuery;
	}
	public void setEntityQuery(String entityQuery) {
		this.entityQuery = entityQuery;
	}
	public Set<DynamicReportFieldsConfig> getDynamicReportFieldsConfig() {
		return dynamicReportFieldsConfig;
	}
	public void setDynamicReportFieldsConfig(Set<DynamicReportFieldsConfig> dynamicReportFieldsConfig) {
		this.dynamicReportFieldsConfig = dynamicReportFieldsConfig;
	}

}
