package com.sourcetrace.eses.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.util.StringUtil;

public class DynamicFieldConfig {

	public static enum COMPONENT_TYPES {
		NA, TEXTBOX, RADIO, DATE_PICKER, DROP_DOWN, TEXT_AREA, CHECKBOX, LABEL, LIST, MULTIPLE_SELECT, BUTTON, AUDIO, PHOTO_CERTIFICATION,VIDEO, WEATHER_INFO,PLOTTING
		
	}
	
	public static enum LIST_METHOD {
		NA, getStatesDynamic, getDistrictsDynamic, getTaluksDynamic, getGpsDynamic, getVillagesDynamic, getGroupsDynamic, getFarmersDynamic, getFarmsDynamic, getCropsDynamic,getVarietyDynamic,getManufacturerDynamic,getFarmProductDynamic,getGradeDynamic
	}
	
	public static enum CONSTANTS {
		NA, noOfPalmTrees
	}
	
	public static enum IS_MOBILE_AVAILABLE {
		NA, WEB_AND_MOBILE,ONLY_RPEORT,ONLY_MOBILE,CUSTOMIZED_FORMULA
	}
	
	public static enum ACCESS_TYPE {
		NA, CATALOGUE_TYPE,CATALOGUE_NAMES,LIST_METHOD
	}
	public static enum followUp{
		NA, ACTION_PLAN,DEADLINE,ACTION_PLAN_PARENT,FOLLOW_UP_FIELDS
	}
	public static enum VALIDATION_TYPE {
		NA, STRING,NUMBER,EMAIL,DECIMAL,ALPHANUMERIC
	}
	  public static final String FORMULA_CONSTANT_VALUE = "##";

	private Long id;
	private String code;
	private String componentType;
	private String componentName;
	
	private DynamicFieldConfig parentDepen;
	private String componentMaxLength;
	private String isRequired;
	private DynamicSectionConfig dynamicSectionConfig;
	private Date createdDate;
	private Date updatedDate;
	private String defaultValue;
	
	private String catalogueType;
	private String componentId;
	private Integer orderSet;
	private Long referenceId;
	private String textBoxType;
	private String dataFormat;
	private String validation;
	private String minLen;
	private String isDependency;
	private String isUpdateProfile;
	private String profileField;
	private String dependencyKey;
	private String parentDependencyKey;
	
	private String catDependencyKey;

	private String dynamicValue;
	private String farmerDynamicValueId;

	private String label;
	private String isMobileAvail;
	private String isReportAvail;
	private String formula;
	private Set<DynamicFieldConfig> listFields;
	private Set<LanguagePreferences> languagePreferences;
    private int isOther;

	
	private int followUp;
	private String parentActField;
	private String parentActKey;
	private String grade;
	private int accessType;
	private Integer fOrder;
	private int valueDependency;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	
	public String getComponentMaxLength() {
		return componentMaxLength;
	}

	public void setComponentMaxLength(String componentMaxLength) {
		this.componentMaxLength = componentMaxLength;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDynamicValue() {
		return dynamicValue;
	}

	public void setDynamicValue(String dynamicValue) {
		this.dynamicValue = dynamicValue;
	}

	public String getFarmerDynamicValueId() {
		return farmerDynamicValueId;
	}

	public void setFarmerDynamicValueId(String farmerDynamicValueId) {
		this.farmerDynamicValueId = farmerDynamicValueId;
	}


	public String getCatalogueType() {
		return catalogueType;
	}

	public void setCatalogueType(String catalogueType) {
		this.catalogueType = catalogueType;
	}

	public String getComponentId() {
		if (!StringUtil.isEmpty(componentName)) {
			componentId = componentName;
			componentId = componentId.replace(" ", "_");
		}
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public DynamicFieldConfig getParentDepen() {
		return parentDepen;
	}

	public void setParentDepen(DynamicFieldConfig parentDepen) {
		this.parentDepen = parentDepen;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public DynamicSectionConfig getDynamicSectionConfig() {
		return dynamicSectionConfig;
	}

	public void setDynamicSectionConfig(DynamicSectionConfig dynamicSectionConfig) {
		this.dynamicSectionConfig = dynamicSectionConfig;
	}

	public Integer getOrderSet() {
		return orderSet;
	}

	public void setOrderSet(Integer orderSet) {
		this.orderSet = orderSet;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public String getTextBoxType() {
		return textBoxType;
	}

	public void setTextBoxType(String textBoxType) {
		this.textBoxType = textBoxType;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public String getMinLen() {
		return minLen;
	}

	public void setMinLen(String minLen) {
		this.minLen = minLen;
	}

	public String getIsDependency() {
		return isDependency;
	}

	public void setIsDependency(String isDependency) {
		this.isDependency = isDependency;
	}

	public String getDependencyKey() {
		return dependencyKey;
	}

	public void setDependencyKey(String dependencyKey) {
		this.dependencyKey = dependencyKey;
	}

	public String getParentDependencyKey() {
		return parentDependencyKey;
	}

	public void setParentDependencyKey(String parentDependencyKey) {
		this.parentDependencyKey = parentDependencyKey;
	}


	public String getIsMobileAvail() {
		return isMobileAvail;
	}

	public void setIsMobileAvail(String isMobileAvail) {
		this.isMobileAvail = isMobileAvail;
	}

	public String getIsReportAvail() {
		return isReportAvail;
	}

	public void setIsReportAvail(String isReportAvail) {
		this.isReportAvail = isReportAvail;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Set<LanguagePreferences> getLanguagePreferences() {
		return languagePreferences;
	}

	public void setLanguagePreferences(Set<LanguagePreferences> languagePreferences) {
		this.languagePreferences = languagePreferences;
	}

	public int getAccessType() {
		return accessType;
	}

	public void setAccessType(int accessType) {
		this.accessType = accessType;
	}

	public Object getReduceType() {
		if (this.validation != null && this.validation.equals("4")) {
			return 0.00;
		} else if (this.validation != null && this.validation.equals("4")) {

			return 0;

		} else {
			return "0";
		}

	}

	public String getLangName(String loggedInUserLanguage) {

		if (this.getLanguagePreferences() != null && this.getLanguagePreferences().size() > 0 && !StringUtil.isEmpty(loggedInUserLanguage)) {
			List<LanguagePreferences> lpList = this.getLanguagePreferences().stream()
					.filter(u -> u.getLang().equals(loggedInUserLanguage)).collect(Collectors.toList());
			return (lpList != null && !lpList.isEmpty()) ? lpList.get(0).getName() : this.componentName;
		} else {
			return this.componentName;
		}
	}

	public String getCatDependencyKey() {
		return catDependencyKey;
	}

	public void setCatDependencyKey(String catDependencyKey) {
		this.catDependencyKey = catDependencyKey;
	}

	public Set<DynamicFieldConfig> getListFields() {
		return listFields;
	}

	public void setListFields(Set<DynamicFieldConfig> listFields) {
		this.listFields = listFields;
	}



	public int getIsOther() {
		return isOther;
	}

	public void setIsOther(int isOther) {
		this.isOther = isOther;
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

	public Integer getfOrder() {
		return fOrder;
	}

	public void setfOrder(Integer fOrder) {
		this.fOrder = fOrder;
	}

	public int getValueDependency() {
		return valueDependency;
	}

	public void setValueDependency(int valueDependency) {
		this.valueDependency = valueDependency;
	}

	

}
