package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;

public class DynamicReportConfigDetail implements Serializable {

	private static final long serialVersionUID = -1473317221009454606L;
	private Long id;
	private DynamicReportConfig dynamicReportConfig;
	private String field;
	private Long fieldType;
	private Long accessType;
	private String method;
	private String expression;
	private Long width;
	private Long order;
	private String labelName;
	private Long groupProp;
	private Long sumProp;
	private Long status;
	private Boolean isGridAvailabiltiy;
	private Boolean isExportAvailabiltiy;
	private String parameters;
	private String alignment;
	private String isFooterSum;
	private String isGroupHeader;
	private String dataType;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DynamicReportConfig getDynamicReportConfig() {
		return dynamicReportConfig;
	}

	public void setDynamicReportConfig(DynamicReportConfig dynamicReportConfig) {
		this.dynamicReportConfig = dynamicReportConfig;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Long getFieldType() {
		return fieldType;
	}

	public void setFieldType(Long fieldType) {
		this.fieldType = fieldType;
	}

	public Long getAccessType() {
		return accessType;
	}

	public void setAccessType(Long accessType) {
		this.accessType = accessType;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public Long getWidth() {
		return width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public Long getGroupProp() {
		return groupProp;
	}

	public void setGroupProp(Long groupProp) {
		this.groupProp = groupProp;
	}

	public Long getSumProp() {
		return sumProp;
	}

	public void setSumProp(Long sumProp) {
		this.sumProp = sumProp;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Boolean getIsGridAvailabiltiy() {
		return isGridAvailabiltiy;
	}

	public void setIsGridAvailabiltiy(Boolean isGridAvailabiltiy) {
		this.isGridAvailabiltiy = isGridAvailabiltiy;
	}

	public Boolean getIsExportAvailabiltiy() {
		return isExportAvailabiltiy;
	}

	public void setIsExportAvailabiltiy(Boolean isExportAvailabiltiy) {
		this.isExportAvailabiltiy = isExportAvailabiltiy;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getAlignment() {
		return alignment;
	}

	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	public String getIsFooterSum() {
		return isFooterSum;
	}

	public void setIsFooterSum(String isFooterSum) {
		this.isFooterSum = isFooterSum;
	}

	public String getIsGroupHeader() {
		return isGroupHeader;
	}

	public void setIsGroupHeader(String isGroupHeader) {
		this.isGroupHeader = isGroupHeader;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

}
