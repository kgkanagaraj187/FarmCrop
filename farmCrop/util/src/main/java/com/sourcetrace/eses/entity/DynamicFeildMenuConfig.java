package com.sourcetrace.eses.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.util.StringUtil;

/**
 * @author Administrator
 *
 */
public class DynamicFeildMenuConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String code;
	private String name;
	private String iconClass;
	private String txnType;
	private String entity;
	private int order;
	private Integer isSeason;
	private Integer isSingleRecord;
	private String agentType;
	private String mTxnType;
	private String beforeInsert;
	private String afterInsert;
	private long revisionNo;
	private Integer isScore;
	private SortedSet<DynamicMenuSectionMap> dynamicSectionConfigs;
	private SortedSet<DynamicMenuFieldMap> dynamicFieldConfigs;
	private Set<LanguagePreferences> languagePreferences;
	private String branchId;
	private String method;
	private String parameter;
	public static enum EntityTypes {
		NA, FARMER, FARM, GROUP, CERTIFICATION, TRAINING, FARM_CROPS,RFA_CERT
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public SortedSet<DynamicMenuSectionMap> getDynamicSectionConfigs() {
		return dynamicSectionConfigs;
	}

	public void setDynamicSectionConfigs(SortedSet<DynamicMenuSectionMap> dynamicSectionConfigs) {
		this.dynamicSectionConfigs = dynamicSectionConfigs;
	}

	public SortedSet<DynamicMenuFieldMap> getDynamicFieldConfigs() {
		return dynamicFieldConfigs;
	}

	public void setDynamicFieldConfigs(SortedSet<DynamicMenuFieldMap> dynamicFieldConfigs) {
		this.dynamicFieldConfigs = dynamicFieldConfigs;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<LanguagePreferences> getLanguagePreferences() {
		return languagePreferences;
	}

	public void setLanguagePreferences(Set<LanguagePreferences> languagePreferences) {
		this.languagePreferences = languagePreferences;
	}

	public Integer getIsSeason() {
		return isSeason;
	}

	public void setIsSeason(Integer isSeason) {
		this.isSeason = isSeason;
	}

	public Integer getIsSingleRecord() {
		return isSingleRecord;
	}

	public void setIsSingleRecord(Integer isSingleRecord) {
		this.isSingleRecord = isSingleRecord;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	
	public String getBeforeInsert() {
		return beforeInsert;
	}

	public void setBeforeInsert(String beforeInsert) {
		this.beforeInsert = beforeInsert;
	}

	public String getAfterInsert() {
		return afterInsert;
	}

	public void setAfterInsert(String afterInsert) {
		this.afterInsert = afterInsert;
	}
	
	
  
	public long getRevisionNo() {
		return revisionNo;
	}

	public void setRevisionNo(long revisionNo) {
		this.revisionNo = revisionNo;
	}

	public String getLangName(String loggedInUserLanguage) {
		
		if (this.getLanguagePreferences() != null && this.getLanguagePreferences().size() > 0 && !StringUtil.isEmpty(loggedInUserLanguage)) {
			List<LanguagePreferences> lpList =   this.getLanguagePreferences().stream().filter(u -> u.getLang().equals(loggedInUserLanguage)).collect(Collectors.toList());
			return (lpList != null && !lpList.isEmpty()) ? lpList.get(0).getName() : this.name;
		}else{
			return this.name;
		}
	}

	public String getmTxnType() {
		return mTxnType;
	}

	public void setmTxnType(String mTxnType) {
		this.mTxnType = mTxnType;
	}

	public Integer getIsScore() {
		return isScore;
	}

	public void setIsScore(Integer isScore) {
		this.isScore = isScore;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	
	

}
