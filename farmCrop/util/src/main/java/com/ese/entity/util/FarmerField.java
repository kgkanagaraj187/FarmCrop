package com.ese.entity.util;

import java.util.Map;

import com.sourcetrace.esesw.entity.profile.DataLevel;

public class FarmerField {
	public static final Long INACTIVE = 0L;
	public static final Long ACTIVE = 1L;
	private Long id;
	private String name;
	private String type;
	private String typeName;
	private Long parent;
	private Long status;
	private Map<String, String> farmFields;
	private Map<String, String> fields;

	private String field;
	private DataLevel dataLevel;
	private Integer others;

	private String report;
	private String entityFields;
	private String projectionFields;
	private Long farmerProfileExport;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Map<String, String> getFarmFields() {
		return farmFields;
	}

	public void setFarmFields(Map<String, String> farmFields) {
		this.farmFields = farmFields;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getEntityFields() {
		return entityFields;
	}

	public void setEntityFields(String entityFields) {
		this.entityFields = entityFields;
	}

	public String getProjectionFields() {
		return projectionFields;
	}

	public void setProjectionFields(String projectionFields) {
		this.projectionFields = projectionFields;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public DataLevel getDataLevel() {
		return dataLevel;
	}

	public void setDataLevel(DataLevel dataLevel) {
		this.dataLevel = dataLevel;
	}

	public Integer getOthers() {
		return others;
	}

	public void setOthers(Integer others) {
		this.others = others;
	}

	public Long getFarmerProfileExport() {
		return farmerProfileExport;
	}

	public void setFarmerProfileExport(Long farmerProfileExport) {
		this.farmerProfileExport = farmerProfileExport;
	}

}