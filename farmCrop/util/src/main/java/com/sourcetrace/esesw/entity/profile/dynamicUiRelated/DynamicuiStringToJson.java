package com.sourcetrace.esesw.entity.profile.dynamicUiRelated;

import java.util.ArrayList;

public class DynamicuiStringToJson {
	private String txnType;
	private String menuName;
	private String iconClass;
	private String entity;
	private String isSeason;
	private String isSingleRecord;
	private String agentType;
	private String mTxnType;
	private String beforeInsert;
	private String afterInsert;
	private String revisionNo;
	private String isScore;
	private  ArrayList<SectionArrayContains> sectionArray = new ArrayList<SectionArrayContains>();
	
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public ArrayList<SectionArrayContains> getSectionArray() {
		return sectionArray;
	}
	public void setSectionArray(ArrayList<SectionArrayContains> sectionArray) {
		this.sectionArray = sectionArray;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getIconClass() {
		return iconClass;
	}
	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	public String getmTxnType() {
		return mTxnType;
	}
	public void setmTxnType(String mTxnType) {
		this.mTxnType = mTxnType;
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
	public String getIsSeason() {
		return isSeason;
	}
	public void setIsSeason(String isSeason) {
		this.isSeason = isSeason;
	}
	public String getIsSingleRecord() {
		return isSingleRecord;
	}
	public void setIsSingleRecord(String isSingleRecord) {
		this.isSingleRecord = isSingleRecord;
	}
	public String getRevisionNo() {
		return revisionNo;
	}
	public void setRevisionNo(String revisionNo) {
		this.revisionNo = revisionNo;
	}
	public String getIsScore() {
		return isScore;
	}
	public void setIsScore(String isScore) {
		this.isScore = isScore;
	}
	
}






