package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Map;

public class DynamicReportConfigFilter implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private DynamicReportConfig dynamicReportConfig;
	private String field;
	private String label;
	private Long status;
	private String method;
	private Long order;
	private Long Type;
	private Long isDateFilter;
	private Map<String,String> options;
	private String defaultFilter;

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

	public Map<String, String> getOptions() {
		return options;
	}

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
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

	public Long getType() {
		return Type;
	}

	public void setType(Long type) {
		Type = type;
	}

	public Long getIsDateFilter() {
		return isDateFilter;
	}

	public void setIsDateFilter(Long isDateFilter) {
		this.isDateFilter = isDateFilter;
	}

	public String getDefaultFilter() {
		return defaultFilter;
	}

	public void setDefaultFilter(String defaultFilter) {
		this.defaultFilter = defaultFilter;
	}
	
}
