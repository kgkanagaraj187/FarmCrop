/*
 * Question.java
 * Copyright (c) 2016-2017, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.inspect.agrocert;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.regex.Matcher;

import org.hibernate.validator.constraints.NotEmpty;
import org.json.simple.JSONObject;

import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;

/**
 * The Class Question.
 */
public class SurveyQuestion implements Comparable<SurveyQuestion> {

    public static enum dataCollectionType {
        NA, ONCE, PERIODICALLY, ANNUAL, TWICE
    }

    public static enum collectionTypeMaster {
        COLLECT, FORMULA, ADMIN
    }

    public static enum componentTypeMaster {
        NA, TEXT_BOX, GPS_TEXT_BOX, RADIO_BUTTON, RADIO_BUTTON_OTHERS, CHECK_BOX, CHECK_BOX_OTHERS, DROP_DOWN, DROP_DOWN_OTHERS, TEXT_AREA, DATE_PICKER, PHOTO, MULTI_SELECT_DROP_DOWN, MULTI_SELECT_DROP_DOWN_OTHERS
    }

    public static enum validationTypeMaster {
        NA, NUMBER, DOUBLE, DATE, GPS_POINTS, PERCENTAGE, HOURS, MOBILE_NO
    }

    public static enum onceQuestionAnswerProcessTypeMaster {
        ANSWER, CATALOGUE_CODE, CATALOGUE_NAME, CATALOGUE_INDEX, DATE, GPS, OTHERS, ENTITY, PHOTO
    }

    public static enum listMethodNames {
        getYearMap, getCombinedYearMap, listCountry, listRegion, listDistrict, listSection, listCity, listVillage, getFarmerMap, listCocoaVarities, listCommunity
    }

    public static enum questionTypes {
        NA, FARMULA, DEPENDENCY,SUB_FORM
    }

    public static enum dependencyTypes {
        PARENT, CHILD
    }
    public static enum isMandatory {
        MANDATORY, NONMANDATORY
    }

    public static final String FORMULA_CURRENT_YEAR = "$YEAR";
    public static final String YEAR_LIST_METHOD_NAME = "getYearMap";
    public static final String FORMULA_CONSTANT_VALUE = "##";
    public static final String ONCE_QUESTION_CONSTANT_VALUE = "$$";

    public static final String CAT_IDONTKNOW = "CT136";
    public static final String CAT_NA = "CT139";
    public static final String CAT_IDONTKNOW_IMPORT = "#IDK";
    public static final String CAT_NA_IMPORT = "#NA";

    public static final String CODE_PREFIX = "PQ";
    public static final String DATA_FORMAT_THREE_DECIMAL = "#0.000";
    public static final String DATA_FORMAT_TWO_DECIMAL = "#0.00";
    public static final String DATA_FORMAT_NUMBER = "#0";
    public static final String DATA_FORMAT_DATE = "ddMMyyyy";

  

    public static final List<Integer> otherComponetTypes = new ArrayList<Integer>() {
        {
            add(componentTypeMaster.RADIO_BUTTON_OTHERS.ordinal());
            add(componentTypeMaster.CHECK_BOX_OTHERS.ordinal());
            add(componentTypeMaster.DROP_DOWN_OTHERS.ordinal());
            add(componentTypeMaster.MULTI_SELECT_DROP_DOWN_OTHERS.ordinal());
        }
    };

    public static final List<Integer> catlalogueComponetTypes = new ArrayList<Integer>() {
        {
            add(componentTypeMaster.RADIO_BUTTON.ordinal());
            add(componentTypeMaster.RADIO_BUTTON_OTHERS.ordinal());
            add(componentTypeMaster.CHECK_BOX.ordinal());
            add(componentTypeMaster.CHECK_BOX_OTHERS.ordinal());
            add(componentTypeMaster.DROP_DOWN.ordinal());
            add(componentTypeMaster.DROP_DOWN_OTHERS.ordinal());
            add(componentTypeMaster.MULTI_SELECT_DROP_DOWN.ordinal());
            add(componentTypeMaster.MULTI_SELECT_DROP_DOWN_OTHERS.ordinal());
        }
    };

    public static List<Integer> parentQuestionComponentTypes = new ArrayList<Integer>() {
        {
            add(componentTypeMaster.RADIO_BUTTON.ordinal());
            add(componentTypeMaster.RADIO_BUTTON_OTHERS.ordinal());
            add(componentTypeMaster.DROP_DOWN.ordinal());
            add(componentTypeMaster.DROP_DOWN_OTHERS.ordinal());
            add(componentTypeMaster.MULTI_SELECT_DROP_DOWN.ordinal());
            add(componentTypeMaster.MULTI_SELECT_DROP_DOWN_OTHERS.ordinal());

        }
    };

    public static List<Integer> dropDownComponentTypes = new ArrayList<Integer>() {
        {
            add(componentTypeMaster.MULTI_SELECT_DROP_DOWN.ordinal());
            add(componentTypeMaster.MULTI_SELECT_DROP_DOWN_OTHERS.ordinal());
            add(componentTypeMaster.DROP_DOWN.ordinal());
            add(componentTypeMaster.DROP_DOWN_OTHERS.ordinal());

        }
    };

    public static List<Integer> validationTypeNA = new ArrayList<Integer>() {
        {
            add(componentTypeMaster.RADIO_BUTTON.ordinal());
            add(componentTypeMaster.RADIO_BUTTON_OTHERS.ordinal());
            add(componentTypeMaster.CHECK_BOX.ordinal());
            add(componentTypeMaster.CHECK_BOX_OTHERS.ordinal());
            add(componentTypeMaster.DROP_DOWN.ordinal());
            add(componentTypeMaster.DROP_DOWN_OTHERS.ordinal());
            add(componentTypeMaster.MULTI_SELECT_DROP_DOWN.ordinal());
            add(componentTypeMaster.MULTI_SELECT_DROP_DOWN_OTHERS.ordinal());

        }
    };
    // for Once Question Entity..Those Property who have other values
    public static final Map<String, String> onceOtherProperties = new LinkedHashMap<String, String>() {
        {
            put("FarmerOrganisation.farmerOrgType", "FarmerOrganisation.otherValue");
            put("FarmerOrganisation.sourceOrEvidenceMembers",
                    "FarmerOrganisation.sourceOrEvidenceMembersOtherValue");
        }
    };
    // For finding List method value
    public static final Map<String, String> listMethodValueFindingMethods = new LinkedHashMap<String, String>() {
        {
            put(SurveyQuestion.listMethodNames.getYearMap.toString(), "NA");
            put(SurveyQuestion.listMethodNames.getCombinedYearMap.toString(), "NA");
            put(SurveyQuestion.listMethodNames.listCountry.toString(), "Country-code-name");
            put(SurveyQuestion.listMethodNames.listRegion.toString(), "State-code-name");
            put(SurveyQuestion.listMethodNames.listDistrict.toString(), "Locality-code-name");
            put(SurveyQuestion.listMethodNames.listSection.toString(), "GramPanchayat-code-name");
            put(SurveyQuestion.listMethodNames.listCity.toString(), "Municipality-code-name");
            put(SurveyQuestion.listMethodNames.listVillage.toString(), "Village-code-name");
            put(SurveyQuestion.listMethodNames.getFarmerMap.toString(), "Farmer-farmerId-firstName");
          
        }
    };

    public static final String OTHER_YEAR = "OTHY";
    public static final String OTHER_COMBINED_YEAR = "OTHCY";
    public static final String OTHER_COUNTRY = "OTHC";
    public static final String OTHER_REGION = "OTHR";
    public static final String OTHER_DISTRICT = "OTHD";
    public static final String OTHER_GRAMPANCHAYAT = "OTHG";
    public static final String OTHER_CITY = "OTHCI";
    public static final String OTHER_VILLAGE = "OTHV";
    public static final String OTHER_FARMER = "OTHF";
   

    public static final Map<String, String> listMethodOtherValues = new LinkedHashMap<String, String>() {
        {
            put(SurveyQuestion.listMethodNames.getYearMap.toString(), OTHER_YEAR);
            put(SurveyQuestion.listMethodNames.getCombinedYearMap.toString(), OTHER_COMBINED_YEAR);
            put(SurveyQuestion.listMethodNames.listCountry.toString(), OTHER_COUNTRY);
            put(SurveyQuestion.listMethodNames.listRegion.toString(), OTHER_REGION);
            put(SurveyQuestion.listMethodNames.listDistrict.toString(), OTHER_DISTRICT);
            put(SurveyQuestion.listMethodNames.listSection.toString(), OTHER_GRAMPANCHAYAT);
            put(SurveyQuestion.listMethodNames.listCity.toString(), OTHER_CITY);
            put(SurveyQuestion.listMethodNames.listVillage.toString(), OTHER_VILLAGE);
            put(SurveyQuestion.listMethodNames.getFarmerMap.toString(), OTHER_FARMER);
          
        }
    };
    

    // For Once Question Gps Components
    public static final Map<String, String> latLongProperties = new LinkedHashMap<String, String>() {
        {
            put("FarmerOrganisation.gpsLatitude", "FarmerOrganisation.gpsLongitude");
            put("Farmer.latitude", "Farmer.longitude");
            put("Farm.latitude", "Farm.longitude");
        }
    };


    public static final List<Integer> certificationComponents = new ArrayList<Integer>() {
        {
            add(componentTypeMaster.RADIO_BUTTON.ordinal());
            add(componentTypeMaster.RADIO_BUTTON_OTHERS.ordinal());
            add(componentTypeMaster.DROP_DOWN.ordinal());
            add(componentTypeMaster.DROP_DOWN_OTHERS.ordinal());
            add(componentTypeMaster.MULTI_SELECT_DROP_DOWN.ordinal());
            add(componentTypeMaster.MULTI_SELECT_DROP_DOWN_OTHERS.ordinal());
         }
    };

    private long id;
    private String code;
    private String serialNo;
    private String name;
    private String questionType;
    private SurveySection section;
    private SurveyQuestion parentQuestion;
    private Set<SurveyQuestion> subQuestions;
    private SortedSet<SubFormQuestionMapping> subFormQuestions;
    private long revisionNumber;

    private String info;
    private String formulaEquation;
    private String dataFormat;
    private String maxLength;
    private int dataCollection;
    private int componentType;
    private int collectionType;
    private int validationType;
    private boolean historical;
    private String sectionId;
    // Display Question Based on Status
    private boolean status;
    private String listMethodName;
    private String defaultUnit;
    private String otherCatalogValue;
    private String unitOtherCatalogValue;
    private String dependencyKey;
    private String subFormKey;
    private int mandatory;
    private String entityColumnSub;
    private String exiDependencyKey;
    private Long minRange;
    private Long maxRange;
    private String parentDepen;
    private String answerCatType;
    //private Country country;
    // FarmCropProdAnswer Property $$ Entity Name $$ Entity Property To Fetch
    // Entity from Db
    // $$ Entity Setter Property$$Answer process Type $$ Result Entity class
    // (Answer have to be
    // casted to this type)
    // $$ In Case Answer Process type is Entity we can find that entity from db
    // by this value
    private String entityProperty;
    private Set<FarmCatalogue> units;
    private Set<FarmCatalogue> defaultValues;
   // private String answerKeyType;
    
 

    // Transient
    private List<LanguagePreferences> languagePreferences;
    private List<String> answerKeyCodes;
    private List<String> unitCodes;
    private String defaultValueCode;
    private int dependencyType;
    private String entityColumn;
    private int passScore;
    private int totalScore;
    private List<String> questionCodes;
    private List<String> availQuestionCodes; 
    private JSONObject subFormTr = new JSONObject();

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the code.
     * @return the code
     */
    public String getCode() {

        return code;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Gets the serial no.
     * @return the serial no
     */
    public String getSerialNo() {

        return serialNo;
    }

    /**
     * Sets the serial no.
     * @param serialNo the new serial no
     */
    public void setSerialNo(String serialNo) {

        this.serialNo = serialNo;
    }

    /**
     * Gets the name.
     * @return the name
     */
   // @Pattern(regex = "[^@#$%&*();:,{}^<>+|^!^/=\"\\_-`~]+$", message = "pattern.name")
    @NotEmpty(message = "empty.name")
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the question type.
     * @return the question type
     */
    public String getQuestionType() {

        return questionType;
    }

    /**
     * Sets the question type.
     * @param questionType the new question type
     */
    public void setQuestionType(String questionType) {

        this.questionType = questionType;
    }

    /**
     * Gets the section.
     * @return the section
     */
    public SurveySection getSection() {

        return section;
    }

    /**
     * Sets the section.
     * @param section the new section
     */
    public void setSection(SurveySection section) {

        this.section = section;
    }

    /**
     * Gets the parent question.
     * @return the parent question
     */
    public SurveyQuestion getParentQuestion() {

        return parentQuestion;
    }

    /**
     * Sets the parent question.
     * @param parentQuestion the new parent question
     */
    public void setParentQuestion(SurveyQuestion parentQuestion) {

        this.parentQuestion = parentQuestion;
    }

    /**
     * Gets the sub questions.
     * @return the sub questions
     */
    public Set<SurveyQuestion> getSubQuestions() {

        return subQuestions;
    }

    /**
     * Sets the sub questions.
     * @param subQuestions the new sub questions
     */
    public void setSubQuestions(Set<SurveyQuestion> subQuestions) {

        this.subQuestions = subQuestions;
    }

    /**
     * Gets the revision number.
     * @return the revision number
     */
    public long getRevisionNumber() {

        return revisionNumber;
    }

    /**
     * Sets the revision number.
     * @param revisionNumber the new revision number
     */
    public void setRevisionNumber(long revisionNumber) {

        this.revisionNumber = revisionNumber;
    }

    /**
     * Gets the info.
     * @return the info
     */
    public String getInfo() {

        return info;
    }

    /**
     * Sets the info.
     * @param info the new info
     */
    public void setInfo(String info) {

        this.info = info;
    }

    /**
     * Gets the formula equation.
     * @return the formula equation
     */
    public String getFormulaEquation() {

        return formulaEquation;
    }

    /**
     * Sets the formula equation.
     * @param formulaEquation the new formula equation
     */
    public void setFormulaEquation(String formulaEquation) {

        this.formulaEquation = formulaEquation;
    }

    /**
     * Gets the data collection.
     * @return the data collection
     */
    public int getDataCollection() {

        return dataCollection;
    }

    /**
     * Sets the data collection.
     * @param dataCollection the new data collection
     */
    public void setDataCollection(int dataCollection) {

        this.dataCollection = dataCollection;
    }

    /**
     * Gets the component type.
     * @return the component type
     */
    public int getComponentType() {

        return componentType;
    }

    /**
     * Sets the component type.
     * @param componentType the new component type
     */
    public void setComponentType(int componentType) {

        this.componentType = componentType;
    }

   

    /**
     * Gets the units.
     * @return the units
     */
    public Set<FarmCatalogue> getUnits() {

        return units;
    }

    /**
     * Sets the units.
     * @param units the new units
     */
    public void setUnits(Set<FarmCatalogue> units) {

        this.units = units;
    }

    /**
     * Sets the historical.
     * @param historical the new historical
     */
    public void setHistorical(boolean historical) {

        this.historical = historical;
    }

    /**
     * Checks if is historical.
     * @return true, if is historical
     */
    public boolean isHistorical() {

        return historical;
    }

    /**
     * Sets the collection type.
     * @param collectionType the new collection type
     */
    public void setCollectionType(int collectionType) {

        this.collectionType = collectionType;
    }

    /**
     * Gets the collection type.
     * @return the collection type
     */
    public int getCollectionType() {

        return collectionType;
    }

    /**
     * Sets the validation type.
     * @param validationType the new validation type
     */
    public void setValidationType(int validationType) {

        this.validationType = validationType;
    }

    /**
     * Gets the validation type.
     * @return the validation type
     */
    public int getValidationType() {

        return validationType;
    }

    /**
     * Sets the max length.
     * @param maxLength the new max length
     */
    public void setMaxLength(String maxLength) {

        this.maxLength = maxLength;
    }

    /**
     * Gets the max length.
     * @return the max length
     */
    public String getMaxLength() {

        return maxLength;
    }

    /**
     * Sets the data format.
     * @param dataFormat the new data format
     */
    public void setDataFormat(String dataFormat) {

        this.dataFormat = dataFormat;
    }

    /**
     * Gets the data format.
     * @return the data format
     */
    public String getDataFormat() {

        return dataFormat;
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    public void setStatus(boolean status) {

        this.status = status;
    }

    /**
     * Checks if is status.
     * @return true, if is status
     */
    public boolean isStatus() {

        return status;
    }

    /**
     * Sets the list method name.
     * @param listMethodName the new list method name
     */
    public void setListMethodName(String listMethodName) {

        this.listMethodName = listMethodName;
    }

    /**
     * Gets the list method name.
     * @return the list method name
     */
    public String getListMethodName() {

        return listMethodName;
    }

    /**
     * Gets the doubles whole no max length.
     * @return the doubles whole no max length
     */
    public int getDoublesWholeNoMaxLength() {

        int length = 7;
        if (this.maxLength != null && maxLength.contains(",")) {
            length = Integer.valueOf(maxLength.split(",")[0]);
        }
        return length;
    }

    /**
     * Gets the doubles float no max length.
     * @return the doubles float no max length
     */
    public int getDoublesFloatNoMaxLength() {

        int length = 3;
        if (this.maxLength != null && maxLength.contains(",")) {
            length = Integer.valueOf(maxLength.split(",")[1]);
        }
        return length;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(SurveyQuestion paramT) {

        int returnValue = 1;
        if (this.id != 0 && paramT.id != 0) {
            if (this.id < paramT.id) {
                returnValue = -1;
            } else if (this.id == paramT.id) {
                returnValue = 0;
            }
        }
        return returnValue;
    }

    /**
     * Sets the default values.
     * @param defaultValues the new default values
     */
    public void setDefaultValues(Set<FarmCatalogue> defaultValues) {

        this.defaultValues = defaultValues;
    }

    /**
     * Gets the default values.
     * @return the default values
     */
    public Set<FarmCatalogue> getDefaultValues() {

        return defaultValues;
    }

    /**
     * Gets the default catalogue.
     * @return the default catalogue
     */
    public FarmCatalogue getDefaultCatalogue() {

        if (!ObjectUtil.isListEmpty(this.defaultValues)) {
            return this.defaultValues.iterator().next();
        }
        return null;
    }

    /**
     * Sets the default unit.
     * @param defaultUnit the new default unit
     */
    public void setDefaultUnit(String defaultUnit) {

        this.defaultUnit = defaultUnit;
    }

    /**
     * Gets the default unit.
     * @return the default unit
     */
    public String getDefaultUnit() {

        return defaultUnit;
    }

    /**
     * Gets the other catalog value.
     * @return the other catalog value
     */
    public String getOtherCatalogValue() {

        return otherCatalogValue;
    }

    /**
     * Sets the other catalog value.
     * @param otherCatalogValue the new other catalog value
     */
    public void setOtherCatalogValue(String otherCatalogValue) {

        this.otherCatalogValue = otherCatalogValue;
    }

    /**
     * Sets the unit other catalog value.
     * @param unitOtherCatalogValue the new unit other catalog value
     */
    public void setUnitOtherCatalogValue(String unitOtherCatalogValue) {

        this.unitOtherCatalogValue = unitOtherCatalogValue;
    }

    /**
     * Gets the unit other catalog value.
     * @return the unit other catalog value
     */
    public String getUnitOtherCatalogValue() {

        return unitOtherCatalogValue;
    }

    /**
     * Sets the entity property.
     * @param entityProperty the new entity property
     */
    public void setEntityProperty(String entityProperty) {

        this.entityProperty = entityProperty;
    }

    /**
     * Gets the entity property.
     * @return the entity property
     */
    public String getEntityProperty() {

        return entityProperty;
    }

    /**
     * Gets the dependency key.
     * @return the dependency key
     */
    public String getDependencyKey() {

        return dependencyKey;
    }

    /**
     * Sets the dependency key.
     * @param dependencyKey the new dependency key
     */
    public void setDependencyKey(String dependencyKey) {

        this.dependencyKey = dependencyKey;
    }

    /**
     * Checks if is child question.
     * @return true, if is child question
     */
    public boolean isChildQuestion() {

        return !ObjectUtil.isEmpty(this.parentQuestion);
    }

    /**
     * Checks for sub questions.
     * @return true, if successful
     */
    public boolean hasSubQuestions() {

        return !ObjectUtil.isListEmpty(this.subQuestions);
    }

    /**
     * Gets the table row id.
     * @return the table row id
     */
    public String getTableRowId() {

        return "question_" + this.id;
    }

    /**
     * Gets the table row class.
     * @return the table row class
     */
    public String getTableRowClass() {

        StringBuffer sb = new StringBuffer("");
        if (hasSubQuestions()) {
            sb.append("parent parent_tr_" + this.id).append(" ");
        }
        if (isChildQuestion() && this.parentDepen!=null) {
        	if(this.parentDepen.contains(",")){
        		String[] str = this.parentDepen.split(",");
        		for(String depem : str){
        			 sb.append("dependent dependent_tr_" + this.parentQuestion.getId()+"_"+depem.trim()+" ");
        		}
        		
        	}else{
            sb.append("dependent dependent_tr_" + this.parentQuestion.getId()+"_"+this.parentDepen);
        	}
        }
        return sb.toString();
    }

    /**
     * Gets the component class.
     * @return the component class
     */
    public String getComponentClass() {

        StringBuffer sb = new StringBuffer("");
        if (isChildQuestion() && this.parentDepen!=null) {
        	if(this.parentDepen.contains(",")){
        		String[] str = this.parentDepen.split(",");
        		for(String depem : str){
        			 sb.append(" dependent_component_to_" + this.parentQuestion.getId()+"_"+depem.trim()+" " );
        		}
        		
        	}else{
            sb.append(" dependent_component_to_" + this.parentQuestion.getId()+"_"+this.parentDepen +" ");
        	}
          
        }
        if (hasSubQuestions()) {
            sb.append("parent_component_" + this.id);
        }
        return sb.toString();
    }

    /**
     * Gets the dependent js function.
     * @return the dependent js function
     */
    public String getDependentJSFunction() {

        StringBuffer sb = new StringBuffer("");
        if (hasSubQuestions()) {
            sb
                    .append("showDependentQuestion(this,'" + this.id + "','" + this.dependencyKey
                            + "', true);");
        }
        return sb.toString();
    }

    /**
     * Gets the language preferences.
     * @return the language preferences
     */
    public List<LanguagePreferences> getLanguagePreferences() {

        return languagePreferences;
    }

    /**
     * Sets the language preferences.
     * @param languagePreferences the new language preferences
     */
    public void setLanguagePreferences(List<LanguagePreferences> languagePreferences) {

        this.languagePreferences = languagePreferences;
    }

    /**
     * Gets the answer key codes.
     * @return the answer key codes
     */
    public List<String> getAnswerKeyCodes() {

        return answerKeyCodes;
    }

    /**
     * Sets the answer key codes.
     * @param answerKeyCodes the new answer key codes
     */
    public void setAnswerKeyCodes(List<String> answerKeyCodes) {

        this.answerKeyCodes = answerKeyCodes;
    }

    /**
     * Gets the unit codes.
     * @return the unit codes
     */
    public List<String> getUnitCodes() {

        return unitCodes;
    }

    /**
     * Sets the unit codes.
     * @param unitCodes the new unit codes
     */
    public void setUnitCodes(List<String> unitCodes) {

        this.unitCodes = unitCodes;
    }

    /**
     * Gets the default value code.
     * @return the default value code
     */
    public String getDefaultValueCode() {

        return defaultValueCode;
    }

    /**
     * Sets the default value code.
     * @param defaultValueCode the new default value code
     */
    public void setDefaultValueCode(String defaultValueCode) {

        this.defaultValueCode = defaultValueCode;
    }

    /**
     * Gets the formula question codes.
     * @return the formula question codes
     */
    public List<String> getFormulaQuestionCodes() {

        List<String> codes = new ArrayList<String>();
        if (!StringUtil.isEmpty(formulaEquation)) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\{([^}]*)\\}");
            Matcher m = p.matcher(formulaEquation);
            while (m.find()) {
                codes.add(m.group(1));
            }
        }
        return codes;
    }

    /**
     * Gets the dependency type.
     * @return the dependency type
     */
    public int getDependencyType() {

        dependencyType = -1;
        if (!ObjectUtil.isEmpty(parentQuestion)) {
            dependencyType = dependencyTypes.CHILD.ordinal();
        } else if (!StringUtil.isEmpty(dependencyKey)) {
            dependencyType = dependencyTypes.PARENT.ordinal();
        }
        return dependencyType;
    }

    /**
     * Sets the dependency type.
     * @param dependencyType the new dependency type
     */
    public void setDependencyType(int dependencyType) {

        this.dependencyType = dependencyType;
    }

    /**
     * Gets the entity column.
     * @return the entity column
     */
    public String getEntityColumn() {

        return entityColumn;
    }

    /**
     * Sets the entity column.
     * @param entityColumn the new entity column
     */
    public void setEntityColumn(String entityColumn) {

        this.entityColumn = entityColumn;
    }

    /**
     * Gets the entity column string.
     * @return the entity column string
     */
    public String getEntityColumnString() {

        String entityColumn = "";
        if (!ObjectUtil.isEmpty(section) && !StringUtil.isEmpty(entityProperty)) {
            String[] details = entityProperty.split("##");
            String[] entityDetails = details[0].split("\\$\\$");
            entityColumn = "Farmer" + ".";
            if (entityDetails[3].contains(",")) {
                entityColumn += entityDetails[3].split(",")[0];
            } else if (entityDetails[4].equals(7) && entityDetails.length > 6) {
                entityColumn += entityDetails[3] + "." + entityDetails[6];
            } else {
                entityColumn += entityDetails[3];
            }
        }
        return entityColumn;
    }

    /**
     * Gets the pass score.
     * @return the pass score
     */
    public int getPassScore() {

        return passScore;
    }

    /**
     * Sets the pass score.
     * @param passScore the new pass score
     */
    public void setPassScore(int passScore) {

        this.passScore = passScore;
    }

    /**
     * Gets the total score.
     * @return the total score
     */
    public int getTotalScore() {

        return totalScore;
    }

    /**
     * Sets the total score.
     * @param totalScore the new total score
     */
    public void setTotalScore(int totalScore) {

        this.totalScore = totalScore;
    }

	public SortedSet<SubFormQuestionMapping> getSubFormQuestions() {
		return subFormQuestions;
	}

	public void setSubFormQuestions(
			SortedSet<SubFormQuestionMapping> subFormQuestions) {
		this.subFormQuestions = subFormQuestions;
	}

    public List<String> getQuestionCodes() {
    
        return questionCodes;
    }

    public void setQuestionCodes(List<String> questionCodes) {
    
        this.questionCodes = questionCodes;
    }

    public List<String> getAvailQuestionCodes() {
    
        return availQuestionCodes;
    }

    public void setAvailQuestionCodes(List<String> availQuestionCodes) {
    
        this.availQuestionCodes = availQuestionCodes;
    }

	public String getSubFormKey() {
		return subFormKey;
	}

	public void setSubFormKey(String subFormKey) {
		this.subFormKey = subFormKey;
	}

	public int getMandatory() {
		return mandatory;
	}

	public void setMandatory(int mandatory) {
		this.mandatory = mandatory;
	}


	public String getEntityColumnSub() {
		return entityColumnSub;
	}

	public void setEntityColumnSub(String entityColumnSub) {
		this.entityColumnSub = entityColumnSub;
	}

	public String getExiDependencyKey() {
		return exiDependencyKey;
	}

	public void setExiDependencyKey(String exiDependencyKey) {
		this.exiDependencyKey = exiDependencyKey;
	}

	public Long getMinRange() {
		return minRange;
	}

	public void setMinRange(Long minRange) {
		this.minRange = minRange;
	}

	public Long getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(Long maxRange) {
		this.maxRange = maxRange;
	}

	public String getParentDepen() {
		return parentDepen;
	}

	public void setParentDepen(String parentDepen) {
		this.parentDepen = parentDepen;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public JSONObject getSubFormTr() {
		return subFormTr;
	}

	public void setSubFormTr(JSONObject subFormTr) {
		this.subFormTr = subFormTr;
	}

	public String getAnswerCatType() {
		return answerCatType;
	}

	public void setAnswerCatType(String answerCatType) {
		this.answerCatType = answerCatType;
	}

  
    

}
