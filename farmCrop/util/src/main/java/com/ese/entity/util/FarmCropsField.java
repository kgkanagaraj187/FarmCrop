package com.ese.entity.util;

import java.util.Map;

public class FarmCropsField {
	public static final Long INACTIVE = 0L;
	public static final Long ACTIVE = 1L;
	private Long id;
	private String name;
	private String type;
	private String typeName;
	private Long parent;
	private Long status;
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
	public Long getFarmerProfileExport() {
		return farmerProfileExport;
	}
	public void setFarmerProfileExport(Long farmerProfileExport) {
		this.farmerProfileExport = farmerProfileExport;
	}
	public static Long getInactive() {
		return INACTIVE;
	}
	public static Long getActive() {
		return ACTIVE;
	}
}
