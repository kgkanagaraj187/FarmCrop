package com.sourcetrace.eses.entity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.util.StringUtil;

public class DynamicSectionConfig {

	private Long id;
	private String sectionName;
	private String branchId;
	private String tableId;
	private String sectionCode;
	private String type;
	private String secorder;
	private String afterInsert;
	private Set<DynamicFieldConfig> dynamicFieldConfigs;
	private String mTxnType;
	private DynamicFeildMenuConfig dynamicFieldMenuConfig;
	private Set<LanguagePreferences> languagePreferences;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public Set<DynamicFieldConfig> getDynamicFieldConfigs() {
		return dynamicFieldConfigs;
	}

	public void setDynamicFieldConfigs(Set<DynamicFieldConfig> dynamicFieldConfigs) {
		this.dynamicFieldConfigs = dynamicFieldConfigs;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getmTxnType() {
		return mTxnType;
	}

	public void setmTxnType(String mTxnType) {
		this.mTxnType = mTxnType;
	}

	public String getSecorder() {
		return secorder;
	}

	public void setSecorder(String secorder) {
		this.secorder = secorder;
	}

	public DynamicFeildMenuConfig getDynamicFieldMenuConfig() {
		return dynamicFieldMenuConfig;
	}

	public void setDynamicFieldMenuConfig(DynamicFeildMenuConfig dynamicFieldMenuConfig) {
		this.dynamicFieldMenuConfig = dynamicFieldMenuConfig;
	}

	public Set<LanguagePreferences> getLanguagePreferences() {
		return languagePreferences;
	}

	public void setLanguagePreferences(Set<LanguagePreferences> languagePreferences) {
		this.languagePreferences = languagePreferences;
	}

	public String getLangName(String loggedInUserLanguage) {

		if (this.getLanguagePreferences() != null && this.getLanguagePreferences().size() > 0 && !StringUtil.isEmpty(loggedInUserLanguage)) {
			List<LanguagePreferences> lpList = this.getLanguagePreferences().stream()
					.filter(u -> u.getLang().equals(loggedInUserLanguage)).collect(Collectors.toList());
			return (lpList != null && !lpList.isEmpty()) ? lpList.get(0).getName() : this.sectionName;
		} else {
			return this.sectionName;
		}
	}
	
	public Integer getMobileFieldsSize() {

	return this.getDynamicFieldConfigs()!=null && this.getDynamicFieldConfigs().size()>0 ? 	this.getDynamicFieldConfigs().stream().filter(u -> (u.getIsMobileAvail()!=null && u.getIsMobileAvail().equals("1"))).collect(Collectors.toList()).size() : 0;
	}

	public String getAfterInsert() {
		return afterInsert;
	}

	public void setAfterInsert(String afterInsert) {
		this.afterInsert = afterInsert;
	}

	/*public String getmBeforeInsert() {
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
	}*/

}
