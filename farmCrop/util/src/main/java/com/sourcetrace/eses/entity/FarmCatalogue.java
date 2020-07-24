package com.sourcetrace.eses.entity;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;

import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.StringUtil;

public class FarmCatalogue {

	/*
	 * public static enum catelogueTypes { ANSWER, UNIT, DEFAULT_ANSWER,
	 * CUSTOMER_PROGRAM, COCOA_VARIETIES };
	 */

	public static final int FARM_EQUIPMENT = 1;
	public static final int ANIMAL_HUSBANDARY = 2;
	public static final int FOODER = 3;
	public static final int ANIMAL_HOUSING = 4;
	public static final int REVENUE = 5;
	public static final int UOM = 10;
	public static final int MAX_LENGTH_NAME = 35;
	public static final Integer CATEGORY = 12;

	public static final String ACTIVE = "1";
	public static final String INACTIVE = "0";

	public static final int OTHER = 99;

	public static final int ANSWER = 94;
	public static final int UNIT = 95;
	public static final int DEFAULT_ANSWER = 96;
	public static final int CUSTOMER_PROGRAM = 97;
	public static final int COCOA_VARIETIES = 98;

	public static final String NAME = "name";
	public static final String DISP_NAME = "dispName";
	
	

	private long id;
	private String code;
	public String name;
	public String dispName;
	private int typez;
	private long revisionNo;
	private String branchId;
	private int typeValue;
	private String otherCatalogueType;
	private int isReserved;
	private String parentId;
	private String status;
	private String englishName;
	private List<LanguagePreferences> languagePreferences;
	private Integer masterStatus;

	/**
	 * Transient variable
	 */
	private List<String> branchesList;

	public long getId() {

		return id;
	}

	public void setId(long id) {

		this.id = id;
	}

	public String getCode() {

		return code;
	}

	public void setCode(String code) {

		this.code = code;
	}

	// @Length(max = MAX_LENGTH_NAME, message = "length.name")
	@Pattern(groups = First.class, regexp = "[^\\p{Punct}]+$", message = "pattern.name")
	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public int getTypez() {

		return typez;
	}

	public void setTypez(int typez) {

		this.typez = typez;
	}

	public long getRevisionNo() {

		return revisionNo;
	}

	public void setRevisionNo(long revisionNo) {

		this.revisionNo = revisionNo;
	}

	public String getBranchId() {

		return branchId;
	}

	public void setBranchId(String branchId) {

		this.branchId = branchId;
	}

	public int getTypeValue() {

		return typeValue;
	}

	public void setTypeValue(int typeValue) {

		this.typeValue = typeValue;
	}

	public String getOtherCatalogueType() {
		return otherCatalogueType;
	}

	public void setOtherCatalogueType(String otherCatalogueType) {
		this.otherCatalogueType = otherCatalogueType;
	}

	public int getIsReserved() {

		return isReserved;
	}

	public void setIsReserved(int isReserved) {

		this.isReserved = isReserved;
	}

	public String getParentId() {

		return parentId;
	}

	public void setParentId(String parentId) {

		this.parentId = parentId;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDispName() {
		return dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	public List<LanguagePreferences> getLanguagePreferences() {
		return languagePreferences;
	}

	public void setLanguagePreferences(List<LanguagePreferences> languagePreferences) {
		this.languagePreferences = languagePreferences;
	}

	public Integer getMasterStatus() {
		return masterStatus;
	}

	public void setMasterStatus(Integer statusSurvey) {
		this.masterStatus = statusSurvey;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	
	

}
