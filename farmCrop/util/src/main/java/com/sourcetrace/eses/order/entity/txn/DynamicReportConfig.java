package com.sourcetrace.eses.order.entity.txn;

import java.io.Serializable;
import java.util.Set;

public class DynamicReportConfig implements Serializable {
	public static final String FILTER_FIELDS = "FILTER_FIELDS";
	public static final String DYNAMIC_CONFIG_DETAIL="DYNAMIC_CONFIG_DETAIL";
	public static final String ENTITY="ENTITY";
	public static final String OTHER_FIELD="OTHERFIELD";
	public static final String ALIAS="ALIAS";
	public static final String GROUP_PROPERTY="GROUP_PROPERTY";
	private static final long serialVersionUID = -4125379987948364089L;
	private Long id;
	private String report;
	private Long fetchType;
	private Long gridType;
	private Long status;
	private String branchId;
	private String entityName;
	private String xlsName;
	private String pdfName;
	private String csvName;
	private Set<DynamicReportConfigDetail> dynmaicReportConfigDetails;
	private Set<DynamicReportConfigFilter> dynmaicReportConfigFilters;
	private Set<DynamicReportConfig> subGrid;
	private String alias;
	private String parentId;
	private String groupProperty;
	private String detailMethod;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public Long getGridType() {
		return gridType;
	}

	public void setGridType(Long gridType) {
		this.gridType = gridType;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Set<DynamicReportConfigDetail> getDynmaicReportConfigDetails() {
		return dynmaicReportConfigDetails;
	}

	public void setDynmaicReportConfigDetails(Set<DynamicReportConfigDetail> dynmaicReportConfigDetails) {
		this.dynmaicReportConfigDetails = dynmaicReportConfigDetails;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public Long getFetchType() {
		return fetchType;
	}

	public void setFetchType(Long fetchType) {
		this.fetchType = fetchType;
	}

	public Set<DynamicReportConfigFilter> getDynmaicReportConfigFilters() {
		return dynmaicReportConfigFilters;
	}

	public void setDynmaicReportConfigFilters(Set<DynamicReportConfigFilter> dynmaicReportConfigFilters) {
		this.dynmaicReportConfigFilters = dynmaicReportConfigFilters;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getXlsName() {
		return xlsName;
	}

	public void setXlsName(String xlsName) {
		this.xlsName = xlsName;
	}

	public String getPdfName() {
		return pdfName;
	}

	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getGroupProperty() {
		return groupProperty;
	}

	public void setGroupProperty(String groupProperty) {
		this.groupProperty = groupProperty;
	}

	public Set<DynamicReportConfig> getSubGrid() {
		return subGrid;
	}

	public void setSubGrid(Set<DynamicReportConfig> subGrid) {
		this.subGrid = subGrid;
	}

	public String getCsvName() {
		return csvName;
	}

	public void setCsvName(String csvName) {
		this.csvName = csvName;
	}

	public String getDetailMethod() {
		return detailMethod;
	}

	public void setDetailMethod(String detailMethod) {
		this.detailMethod = detailMethod;
	}

	
}