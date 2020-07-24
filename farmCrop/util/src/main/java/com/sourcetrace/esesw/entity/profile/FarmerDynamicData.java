package com.sourcetrace.esesw.entity.profile;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FarmerDynamicData {
	
	public static final int APPROVED = 1;
	public static final int DECLINED = 2;
	
	private Long id;
	private long txnUniqueId;
	private String referenceId;
	private String farmerId;
	private String txnType;
	private Date date;
	private String latitude;
	private String longitude;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;
	private String entityId;
	private String correctiveActionPlan;
	private String conversionStatus;
	private String status;
	private String icsName;
	private Map entityMap;	
	private String season;
	private String branch;
	private Integer isSingleRecord;
	private Set<FarmerDynamicFieldsValue> farmerDynamicFieldsValues;
	private FarmIcsConversion farmIcs;
	private Map<String,Object> profileUpdateFields;
	private Map<String, String> filterData;
	private String digSignByteString;
	private Set<DynamicImageData> dymamicImageData;
	private Integer isScore;
	private Integer actStatus;
	private String inspectionStatus;
	private Integer isSeason;
	private String txnDate;
	private LinkedList<String> reportValues;
	private Long revisionNo;
	private Date followUpDate;
	private String followUpUser;
	/*transient Var*/
	private Map<String,Map<String,String>> scoreValue;
	private List<String> branchesList;
	private Double totalScore;
	private DynamicImageData digitalSignature;
	private DynamicImageData agentSignature;
	private long farmerDynamicFieldValueId;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getTxnUniqueId() {
		return txnUniqueId;
	}

	public void setTxnUniqueId(long txnUniqueId) {
		this.txnUniqueId = txnUniqueId;
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Set<FarmerDynamicFieldsValue> getFarmerDynamicFieldsValues() {
		return farmerDynamicFieldsValues;
	}

	public void setFarmerDynamicFieldsValues(Set<FarmerDynamicFieldsValue> farmerDynamicFieldsValues) {
		this.farmerDynamicFieldsValues = farmerDynamicFieldsValues;
	}

	public Map<String, String> getFilterData() {
		return filterData;
	}

	public void setFilterData(Map<String, String> filterData) {
		this.filterData = filterData;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getCorrectiveActionPlan() {
		return correctiveActionPlan;
	}

	public void setCorrectiveActionPlan(String correctiveActionPlan) {
		this.correctiveActionPlan = correctiveActionPlan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConversionStatus() {
		return conversionStatus;
	}

	public void setConversionStatus(String conversionStatus) {
		this.conversionStatus = conversionStatus;
	}

	public String getIcsName() {
		return icsName;
	}

	public void setIcsName(String icsName) {
		this.icsName = icsName;
	}

	public Map getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(Map entityMap) {
		this.entityMap = entityMap;
	}

	public String getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Integer getIsSingleRecord() {
		return isSingleRecord;
	}

	public void setIsSingleRecord(Integer isSingleRecord) {
		this.isSingleRecord = isSingleRecord;
	}

	public Integer getIsSeason() {
		return isSeason;

	}
	public void setIsSeason(Integer isSeason) {
		this.isSeason = isSeason;

	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public FarmIcsConversion getFarmIcs() {
		return farmIcs;
	}

	public void setFarmIcs(FarmIcsConversion farmIcs) {
		this.farmIcs = farmIcs;
	}



	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	public LinkedList<String> getReportValues() {
		return reportValues;
	}

	public void setReportValues(LinkedList<String> reportValues) {
		this.reportValues = reportValues;
	}


	public Set<DynamicImageData> getDymamicImageData() {
		return dymamicImageData;
	}

	public void setDymamicImageData(Set<DynamicImageData> dymamicImageData) {
		this.dymamicImageData = dymamicImageData;
	}


	public Map<String, Object> getProfileUpdateFields() {
		return profileUpdateFields;
	}

	public void setProfileUpdateFields(Map<String, Object> profileUpdateFields) {
		this.profileUpdateFields = profileUpdateFields;
	}

	public String getDigSignByteString() {
		return digSignByteString;
	}

	public void setDigSignByteString(String digSignByteString) {
		this.digSignByteString = digSignByteString;
	}

	public Double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getIsScore() {
		return isScore;
	}

	public void setIsScore(Integer isScore) {
		this.isScore = isScore;
	}

	public Map<String, Map<String, String>> getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(Map<String, Map<String, String>> scoreValue) {
		this.scoreValue = scoreValue;
	}

	public Integer getActStatus() {
		return actStatus;
	}

	public void setActStatus(Integer actStatus) {
		this.actStatus = actStatus;
	}

	public Long getRevisionNo() {
		return revisionNo;
	}

	public void setRevisionNo(Long revisionNo) {
		this.revisionNo = revisionNo;
	}


	public String getFollowUpUser() {
		return followUpUser;
	}

	public void setFollowUpUser(String followUpUser) {
		this.followUpUser = followUpUser;
	}
	public Date getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}

	public DynamicImageData getDigitalSignature() {
		return digitalSignature;
	}

	public void setDigitalSignature(DynamicImageData digitalSignature) {
		this.digitalSignature = digitalSignature;
	}

	public DynamicImageData getAgentSignature() {
		return agentSignature;
	}

	public void setAgentSignature(DynamicImageData agentSignature) {
		this.agentSignature = agentSignature;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

	public long getFarmerDynamicFieldValueId() {
		return farmerDynamicFieldValueId;
	}

	public void setFarmerDynamicFieldValueId(long farmerDynamicFieldValueId) {
		this.farmerDynamicFieldValueId = farmerDynamicFieldValueId;
	}


	
	
}
