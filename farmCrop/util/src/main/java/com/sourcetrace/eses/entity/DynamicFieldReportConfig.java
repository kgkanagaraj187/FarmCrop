package com.sourcetrace.eses.entity;

public class DynamicFieldReportConfig {
	private Long id;
	private String entityType;
	private String label;
	private String txnType;
	private String field;
	private String sumpProp;
	private String groupProp;
	private String gridAvailable;
	private String exportAvailable;
	private String orderz;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getSumpProp() {
		return sumpProp;
	}

	public void setSumpProp(String sumpProp) {
		this.sumpProp = sumpProp;
	}

	public String getGroupProp() {
		return groupProp;
	}

	public void setGroupProp(String groupProp) {
		this.groupProp = groupProp;
	}

	public String getGridAvailable() {
		return gridAvailable;
	}

	public void setGridAvailable(String gridAvailable) {
		this.gridAvailable = gridAvailable;
	}

	public String getExportAvailable() {
		return exportAvailable;
	}

	public void setExportAvailable(String exportAvailable) {
		this.exportAvailable = exportAvailable;
	}

	public String getOrderz() {
		return orderz;
	}

	public void setOrderz(String orderz) {
		this.orderz = orderz;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
