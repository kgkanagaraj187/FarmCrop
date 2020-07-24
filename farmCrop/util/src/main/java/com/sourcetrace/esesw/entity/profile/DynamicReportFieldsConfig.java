package com.sourcetrace.esesw.entity.profile;

public class DynamicReportFieldsConfig {

	private long id;
	private String field;
	private String label;
	private String dataType;
	private String IsGroupByField;
	private String filterAlias;
	private DynamicReportTableConfig dynamicReportTable;
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getIsGroupByField() {
		return IsGroupByField;
	}
	public void setIsGroupByField(String isGroupByField) {
		IsGroupByField = isGroupByField;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public DynamicReportTableConfig getDynamicReportTable() {
		return dynamicReportTable;
	}
	public void setDynamicReportTable(DynamicReportTableConfig dynamicReportTable) {
		this.dynamicReportTable = dynamicReportTable;
	}
	public String getFilterAlias() {
		return filterAlias;
	}
	public void setFilterAlias(String filterAlias) {
		this.filterAlias = filterAlias;
	}

	
	
}
