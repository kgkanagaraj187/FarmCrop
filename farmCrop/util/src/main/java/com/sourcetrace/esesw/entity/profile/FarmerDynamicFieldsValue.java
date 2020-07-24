package com.sourcetrace.esesw.entity.profile;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.sourcetrace.eses.entity.DynamicMenuFieldMap;

public class FarmerDynamicFieldsValue implements Comparable<FarmerDynamicFieldsValue>  {
	private Long id;
	private String fieldName;
	private String fieldValue;
	
	private String dynamicFieldType;
	private String componentType;
	private Integer accessType;
	private String listMethod;
	private Integer typez;
	private String referenceId;
	private String txnType;
	private Date createdDate;
	private String createdUser;
	private long txnUniqueId;
	private String validationType;
	private FarmerDynamicData farmerDynamicData;
	private Set<DynamicImageData> dymamicImageData;
	private String imageIds;
	private Long parentId;
	private String isUpdateProfile;
	private String profileField;
	private Integer score;
	private String isMobileAvail;
	private Double percentage;
	
	private FarmerDynamicFieldsValue actionPlan;
	private FarmerDynamicFieldsValue followUpParent;
	
	private FarmerDynamicFieldsValue deadline;
	private Integer actStatus;
	private int followUp;
	private String parentActField;
	private String parentActKey;

	private int order;
	private SortedSet<FarmerDynamicFieldsValue> followUps;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Map<String, String> dynamicDatas;
	private String grade;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}


	public Map<String, String> getDynamicDatas() {
		return dynamicDatas;
	}

	public void setDynamicDatas(Map<String, String> dynamicDatas) {
		this.dynamicDatas = dynamicDatas;
	}

	public String getDynamicFieldType() {
		return dynamicFieldType;
	}

	public void setDynamicFieldType(String dynamicFieldType) {
		this.dynamicFieldType = dynamicFieldType;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public Integer getTypez() {
		return typez;
	}

	public void setTypez(Integer typez) {
		this.typez = typez;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
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

	public long getTxnUniqueId() {
		return txnUniqueId;
	}

	public void setTxnUniqueId(long txnUniqueId) {
		this.txnUniqueId = txnUniqueId;
	}

	public FarmerDynamicData getFarmerDynamicData() {
		return farmerDynamicData;
	}

	public void setFarmerDynamicData(FarmerDynamicData farmerDynamicData) {
		this.farmerDynamicData = farmerDynamicData;
	}

	public Set<DynamicImageData> getDymamicImageData() {
		return dymamicImageData;
	}

	public void setDymamicImageData(Set<DynamicImageData> dymamicImageData) {
		this.dymamicImageData = dymamicImageData;
	}

	public String getIsMobileAvail() {
		return isMobileAvail;
	}

	public void setIsMobileAvail(String isMobileAvail) {
		this.isMobileAvail = isMobileAvail;
	}


	public String getValidationType() {
		return validationType;
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	public String getImageIds() {
		return imageIds;
	}

	public void setImageIds(String imageIds) {
		this.imageIds = imageIds;
	}

	public Integer getAccessType() {
		return accessType;
	}

	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
	}

	public String getListMethod() {
		return listMethod;
	}

	public void setListMethod(String listMethod) {
		this.listMethod = listMethod;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getIsUpdateProfile() {
		return isUpdateProfile;
	}

	public void setIsUpdateProfile(String isUpdateProfile) {
		this.isUpdateProfile = isUpdateProfile;
	}

	public String getProfileField() {
		return profileField;
	}

	public void setProfileField(String profileField) {
		this.profileField = profileField;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	
	public FarmerDynamicFieldsValue getActionPlan() {
		return actionPlan;
	}

	public void setActionPlan(FarmerDynamicFieldsValue actionPlan) {
		this.actionPlan = actionPlan;
	}


	public FarmerDynamicFieldsValue getDeadline() {
		return deadline;
	}

	public void setDeadline(FarmerDynamicFieldsValue deadline) {
		this.deadline = deadline;
	}

	public Integer getActStatus() {
		return actStatus;
	}

	public void setActStatus(Integer actStatus) {
		this.actStatus = actStatus;
	}

	

	public SortedSet<FarmerDynamicFieldsValue> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(SortedSet<FarmerDynamicFieldsValue> followUps) {
		this.followUps = followUps;
	}

	public int getFollowUp() {
		return followUp;
	}

	public void setFollowUp(int followUp) {
		this.followUp = followUp;
	}

	public String getParentActField() {
		return parentActField;
	}

	public void setParentActField(String parentActField) {
		this.parentActField = parentActField;
	}

	public String getParentActKey() {
		return parentActKey;
	}

	public void setParentActKey(String parentActKey) {
		this.parentActKey = parentActKey;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}


	public FarmerDynamicFieldsValue getFollowUpParent() {
		return followUpParent;
	}

	public void setFollowUpParent(FarmerDynamicFieldsValue followUpParent) {
		this.followUpParent = followUpParent;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int compareTo(FarmerDynamicFieldsValue object) {
		// TODO Auto-generated method stub
		int value = 0;
		FarmerDynamicFieldsValue surveyQuestionMapping = (FarmerDynamicFieldsValue) object;

		if (this.order > surveyQuestionMapping.order)
			value = 1;
		else if (this.order < surveyQuestionMapping.order)
			value = -1;
		else if (this.order == surveyQuestionMapping.order)
			value = 0;
		return value;
	}



	

}
