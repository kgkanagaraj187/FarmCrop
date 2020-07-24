/*
 * FarmerCropProdAnswers.java
 * Copyright (c) 2016-2017, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.agrocert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.sourcetrace.esesw.entity.profile.Farmer;


// TODO: Auto-generated Javadoc
/**
 * The Class FarmerCropProdAnswers.
 */
public class SurveyFarmerCropProdAnswers {

    public static enum modeType {
        TXN, WEB, IMPORT
    }

    public static final int SAVE_STATUS_FULL = 0;
    public static final int SAVE_STATUS_PARTIAL = 1;
    public static final int SAVE_STATUS_HISTORY = 2;
    public static final int SAVE_STATUS_VERIFY_FULL = 3;
    public static final int SAVE_STATUS_VERIFY_PARTIAL = 4;
    public static final int SAVE_STATUS_VERIFY_HISTORY = 5;
    public static final int SAVE_STATUS_DELETED_SURVEY = 6;
    public static final int SAVE_STATUS_DELETED_VERIFY = 7;
    public static final long SURVEY_TYPE_ID = 8;
    public static final long FFM_SURVEY_TYPE = 9;
    public static final int NC_SURVEY_YES = 1;
    public static final int NC_SURVEY_NO = 0;

    private long id;
    private String surveyTxnId;
    private Date answeredDate;
    private String farmerId;
    private String farmId;
    private String cooperativeCode;
    private String categoryCode;
    private String categoryName;
    private SortedSet<SurveyFarmersSectionAnswers> farmersSectionAnswers;
     private String dataCollectorId;
    private String dataCollectorName;
    private long scopeId;
    private String surveyCode;
    private String surveyName;
    private int mode;
     private String comment;
     private int saveStatus;
    private long farmerCropProdAnsRefId;
    private String updateUserName;
    private Date updateDT;
     private String deletedReason;
     private Set<SurveyAnswersDetails> surveyAnswerDetails;
    private Set<SurveySubFormAnswersDetails> surveySubFormAnswersDetails;

    // Formula Query Fields
    private String farmerName;
    private String farmerCode;
    private String farmName;
    private String farmerOrgName;
    private String farmerOrgCode;
   
    private String failureMessage;

    // Transient variable
    private String entityData;
    private List<SurveyFarmersSectionAnswers> farmersSectionAnswersList;
  // private String[] farmerIdList;
    private List<Long> farmerCropProdIds;
    private List<String> surveyCodeList;
    private boolean fetchAnswers;

    private Map<String, Object> onceQuestionMap;
    private String language;
    private String jsonData;
    private List<String> foCodes;
    private int findDuplicate;
    private int duplicateCount;
    private long surveyTypeId;
    private String farmerFamilyMemberCode;
    private String farmerFamilyMemberName;
    private String fromPagel;
    private String subFormJsonData;
    private String quesCodes;
   private String branchId;
    private Date toAnsweredDate;
    private Date fromAnsweredDate;
    
    public static final int EXCEPTION_SAVE_STATUS = 8;
    private  Map<String, Object> onceQuestionEntityMap = new HashMap<String, Object>();
    
    /*
     * Added farmers collection set for joining farmer_crop_prod_answer_farmer_map on historical
     * report - do not use this property for insertion and deletion [Use this only for criteria
     * fetching]
     */
 //   private Set<Farmer> farmers;

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
     * Gets the survey txn id.
     * @return the survey txn id
     */
    public String getSurveyTxnId() {

        return surveyTxnId;
    }

    /**
     * Sets the survey txn id.
     * @param surveyTxnId the new survey txn id
     */
    public void setSurveyTxnId(String surveyTxnId) {

        this.surveyTxnId = surveyTxnId;
    }

    /**
     * Gets the answered date.
     * @return the answered date
     */
    public Date getAnsweredDate() {

        return answeredDate;
    }

    /**
     * Sets the answered date.
     * @param answeredDate the new answered date
     */
    public void setAnsweredDate(Date answeredDate) {

        this.answeredDate = answeredDate;
    }

    /**
     * Gets the farmer id.
     * @return the farmer id
     */
    public String getFarmerId() {

        return farmerId;
    }

    /**
     * Sets the farmer id.
     * @param farmerId the new farmer id
     */
    public void setFarmerId(String farmerId) {

        this.farmerId = farmerId;
    }

    /**
     * Sets the farm id.
     * @param farmId the new farm id
     */
    public void setFarmId(String farmId) {

        this.farmId = farmId;
    }

    /**
     * Gets the farm id.
     * @return the farm id
     */
    public String getFarmId() {

        return farmId;
    }

    /**
     * Gets the category code.
     * @return the category code
     */
    public String getCategoryCode() {

        return categoryCode;
    }

    /**
     * Sets the category code.
     * @param categoryCode the new category code
     */
    public void setCategoryCode(String categoryCode) {

        this.categoryCode = categoryCode;
    }

    /**
     * Gets the category name.
     * @return the category name
     */
    public String getCategoryName() {

        return categoryName;
    }

    /**
     * Sets the category name.
     * @param categoryName the new category name
     */
    public void setCategoryName(String categoryName) {

        this.categoryName = categoryName;
    }

    /**
     * Gets the farmers section answers.
     * @return the farmers section answers
     */
    public SortedSet<SurveyFarmersSectionAnswers> getFarmersSectionAnswers() {

        return farmersSectionAnswers;
    }

    /**
     * Sets the farmers section answers.
     * @param farmersSectionAnswers the new farmers section answers
     */
    public void setFarmersSectionAnswers(SortedSet<SurveyFarmersSectionAnswers> farmersSectionAnswers) {

        this.farmersSectionAnswers = farmersSectionAnswers;
    }

  
    /**
     * Sets the cooperative code.
     * @param cooperativeCode the new cooperative code
     */
    public void setCooperativeCode(String cooperativeCode) {

        this.cooperativeCode = cooperativeCode;
    }

    /**
     * Gets the cooperative code.
     * @return the cooperative code
     */
    public String getCooperativeCode() {

        return cooperativeCode;
    }

    /**
     * Gets the entity data.
     * @return the entity data
     */
    public String getEntityData() {

        return entityData;
    }

    /**
     * Sets the entity data.
     * @param entityData the new entity data
     */
    public void setEntityData(String entityData) {

        this.entityData = entityData;
    }

    /**
     * Sets the farmers section answers list.
     * @param farmersSectionAnswersList the new farmers section answers list
     */
    public void setFarmersSectionAnswersList(List<SurveyFarmersSectionAnswers> farmersSectionAnswersList) {

        this.farmersSectionAnswersList = farmersSectionAnswersList;
    }

    /**
     * Gets the farmers section answers list.
     * @return the farmers section answers list
     */
    public List<SurveyFarmersSectionAnswers> getFarmersSectionAnswersList() {

        return farmersSectionAnswersList;
    }

   /* *//**
     * Sets the farmer id list.
     * @param farmerIdList the new farmer id list
     *//*
    public void setFarmerIdList(String[] farmerIdList) {

        this.farmerIdList = farmerIdList;
    }

    *//**
     * Gets the farmer id list.
     * @return the farmer id list
     *//*
    public String[] getFarmerIdList() {

        return farmerIdList;
    }*/

    /**
     * Sets the data collector id.
     * @param dataCollectorId the new data collector id
     */
    public void setDataCollectorId(String dataCollectorId) {

        this.dataCollectorId = dataCollectorId;
    }

    /**
     * Gets the data collector id.
     * @return the data collector id
     */
    public String getDataCollectorId() {

        return dataCollectorId;
    }

    /**
     * Sets the data collector name.
     * @param dataCollectorName the new data collector name
     */
    public void setDataCollectorName(String dataCollectorName) {

        this.dataCollectorName = dataCollectorName;
    }

    /**
     * Gets the data collector name.
     * @return the data collector name
     */
    public String getDataCollectorName() {

        return dataCollectorName;
    }

  

    /**
     * Gets the survey code.
     * @return the survey code
     */
    public String getSurveyCode() {

        return surveyCode;
    }

    /**
     * Sets the survey code.
     * @param surveyCode the new survey code
     */
    public void setSurveyCode(String surveyCode) {

        this.surveyCode = surveyCode;
    }

    /**
     * Gets the survey name.
     * @return the survey name
     */
    public String getSurveyName() {

        return surveyName;
    }

    /**
     * Sets the survey name.
     * @param surveyName the new survey name
     */
    public void setSurveyName(String surveyName) {

        this.surveyName = surveyName;
    }

    /**
     * Sets the mode.
     * @param mode the new mode
     */
    public void setMode(int mode) {

        this.mode = mode;
    }

    /**
     * Gets the mode.
     * @return the mode
     */
    public int getMode() {

        return mode;
    }

    /**
     * Gets the farmer name.
     * @return the farmer name
     */
    public String getFarmerName() {

        return farmerName;
    }

    /**
     * Sets the farmer name.
     * @param farmerName the new farmer name
     */
    public void setFarmerName(String farmerName) {

        this.farmerName = farmerName;
    }

    /**
     * Gets the farm name.
     * @return the farm name
     */
    public String getFarmName() {

        return farmName;
    }

    /**
     * Sets the farm name.
     * @param farmName the new farm name
     */
    public void setFarmName(String farmName) {

        this.farmName = farmName;
    }

    /**
     * Gets the farmer org name.
     * @return the farmer org name
     */
    public String getFarmerOrgName() {

        return farmerOrgName;
    }

    /**
     * Sets the farmer org name.
     * @param farmerOrgName the new farmer org name
     */
    public void setFarmerOrgName(String farmerOrgName) {

        this.farmerOrgName = farmerOrgName;
    }

  
  /*  *//**
     * Gets the farmers.
     * @return the farmers
     *//*
    public Set<Farmer> getFarmers() {

        return farmers;
    }

    *//**
     * Sets the farmers.
     * @param farmers the new farmers
     *//*
    public void setFarmers(Set<Farmer> farmers) {

        this.farmers = farmers;
    }
*/
   
    /**
     * Sets the once question map.
     * @param onceQuestionMap the once question map
     */
    public void setOnceQuestionMap(Map<String, Object> onceQuestionMap) {

        this.onceQuestionMap = onceQuestionMap;
    }

    /**
     * Gets the once question map.
     * @return the once question map
     */
    public Map<String, Object> getOnceQuestionMap() {

        return onceQuestionMap;
    }

    /**
     * Sets the farmer code.
     * @param farmerCode the new farmer code
     */
    public void setFarmerCode(String farmerCode) {

        this.farmerCode = farmerCode;
    }

    /**
     * Gets the farmer code.
     * @return the farmer code
     */
    public String getFarmerCode() {

        return farmerCode;
    }

    /**
     * Gets the language.
     * @return the language
     */
    // public Set<LanguagePreferences> getLanguagePreferences() {
    //
    // return languagePreferences;
    // }

    /**
     * Sets the language preferences.
     * @param languagePreferences the new language preferences
     */
    // public void setLanguagePreferences(Set<LanguagePreferences>
    // languagePreferences) {
    //
    // this.languagePreferences = languagePreferences;
    // }

    /**
     * Gets the language.
     * @return the language
     */
    public String getLanguage() {

        return language;
    }

    /**
     * Sets the language.
     * @param language the new language
     */
    public void setLanguage(String language) {

        this.language = language;
    }

    /**
     * Gets the comment.
     * @return the comment
     */
    public String getComment() {

        return comment;
    }

    /**
     * Sets the comment.
     * @param comment the new comment
     */
    public void setComment(String comment) {

        this.comment = comment;
    }

    /**
     * Gets the save status.
     * @return the save status
     */
    public int getSaveStatus() {

        return saveStatus;
    }

    /**
     * Sets the save status.
     * @param saveStatus the new save status
     */
    public void setSaveStatus(int saveStatus) {

        this.saveStatus = saveStatus;
    }

    /**
     * Gets the farmer crop prod ans ref id.
     * @return the farmer crop prod ans ref id
     */
    public long getFarmerCropProdAnsRefId() {

        return farmerCropProdAnsRefId;
    }

    /**
     * Sets the farmer crop prod ans ref id.
     * @param farmerCropProdAnsRefId the new farmer crop prod ans ref id
     */
    public void setFarmerCropProdAnsRefId(long farmerCropProdAnsRefId) {

        this.farmerCropProdAnsRefId = farmerCropProdAnsRefId;
    }

    /**
     * Gets the update user name.
     * @return the update user name
     */
    public String getUpdateUserName() {

        return updateUserName;
    }

    /**
     * Sets the update user name.
     * @param updateUserName the new update user name
     */
    public void setUpdateUserName(String updateUserName) {

        this.updateUserName = updateUserName;
    }

    /**
     * Gets the update dt.
     * @return the update dt
     */
    public Date getUpdateDT() {

        return updateDT;
    }

    /**
     * Sets the update dt.
     * @param updateDT the new update dt
     */
    public void setUpdateDT(Date updateDT) {

        this.updateDT = updateDT;
    }

    /**
     * Gets the question count.
     * @return the question count
     */
    public int getQuestionCount() {

        int count = 0;
        for (SurveyFarmersSectionAnswers farmersSectionAnswers : getFarmersSectionAnswersList()) {
            count += farmersSectionAnswers.getFarmersQuestionAnswers().size();
        }
        return count;
    }

   
    /**
     * Gets the json data.
     * @return the json data
     */
    public String getJsonData() {

        return jsonData;
    }

    /**
     * Sets the json data.
     * @param jsonData the new json data
     */
    public void setJsonData(String jsonData) {

        this.jsonData = jsonData;
    }

    /**
     * Gets the scope id.
     * @return the scope id
     */
    public long getScopeId() {

        return scopeId;
    }

    /**
     * Sets the scope id.
     * @param scopeId the new scope id
     */
    public void setScopeId(long scopeId) {

        this.scopeId = scopeId;
    }

    /**
     * Gets the survey code list.
     * @return the survey code list
     */
    public List<String> getSurveyCodeList() {

        return surveyCodeList;
    }

    /**
     * Sets the survey code list.
     * @param codeList the new survey code list
     */
    public void setSurveyCodeList(List<String> codeList) {

        this.surveyCodeList = codeList;
    }

   

    /**
     * Gets the farmer crop prod ids.
     * @return the farmer crop prod ids
     */
    public List<Long> getFarmerCropProdIds() {

        return farmerCropProdIds;
    }

    /**
     * Sets the farmer crop prod ids.
     * @param farmerCropProdIds the new farmer crop prod ids
     */
    public void setFarmerCropProdIds(List<Long> farmerCropProdIds) {

        this.farmerCropProdIds = farmerCropProdIds;
    }

    /**
     * Gets the fo codes.
     * @return the fo codes
     */
    public List<String> getFoCodes() {

        return foCodes;
    }

    /**
     * Sets the fo codes.
     * @param foCodes the new fo codes
     */
    public void setFoCodes(List<String> foCodes) {

        this.foCodes = foCodes;
    }

   
    /**
     * Gets the deleted reason.
     * @return the deleted reason
     */
    public String getDeletedReason() {

        return deletedReason;
    }

    /**
     * Sets the deleted reason.
     * @param deletedReason the new deleted reason
     */
    public void setDeletedReason(String deletedReason) {

        this.deletedReason = deletedReason;
    }

    /**
     * Gets the find duplicate.
     * @return the find duplicate
     */
    public int getFindDuplicate() {

        return findDuplicate;
    }

    /**
     * Sets the find duplicate.
     * @param findDuplicate the new find duplicate
     */
    public void setFindDuplicate(int findDuplicate) {

        this.findDuplicate = findDuplicate;
    }

    /**
     * Gets the duplicate count.
     * @return the duplicate count
     */
    public int getDuplicateCount() {

        return duplicateCount;
    }

    /**
     * Sets the duplicate count.
     * @param duplicateCount the new duplicate count
     */
    public void setDuplicateCount(int duplicateCount) {

        this.duplicateCount = duplicateCount;
    }

  
    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((answeredDate == null) ? 0 : answeredDate.hashCode());
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((cooperativeCode == null) ? 0 : cooperativeCode.hashCode());
       
        result = prime * result + ((dataCollectorId == null) ? 0 : dataCollectorId.hashCode());
        result = prime * result + ((dataCollectorName == null) ? 0 : dataCollectorName.hashCode());
        result = prime * result + ((farmId == null) ? 0 : farmId.hashCode());
        result = prime * result + ((farmerId == null) ? 0 : farmerId.hashCode());
        result = prime * result + mode;
        result = prime * result + saveStatus;
        result = prime * result + (int) (scopeId ^ (scopeId >>> 32));
        result = prime * result + ((surveyCode == null) ? 0 : surveyCode.hashCode());
        result = prime * result + ((surveyName == null) ? 0 : surveyName.hashCode());
        result = prime * result + ((surveyTxnId == null) ? 0 : surveyTxnId.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SurveyFarmerCropProdAnswers other = (SurveyFarmerCropProdAnswers) obj;
        if (answeredDate == null) {
            if (other.answeredDate != null)
                return false;
        } else if (!answeredDate.equals(other.answeredDate))
            return false;
       
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        if (cooperativeCode == null) {
            if (other.cooperativeCode != null)
                return false;
        } else if (!cooperativeCode.equals(other.cooperativeCode))
            return false;
     
        if (dataCollectorId == null) {
            if (other.dataCollectorId != null)
                return false;
        } else if (!dataCollectorId.equals(other.dataCollectorId))
            return false;
        if (dataCollectorName == null) {
            if (other.dataCollectorName != null)
                return false;
        } else if (!dataCollectorName.equals(other.dataCollectorName))
            return false;
        if (farmId == null) {
            if (other.farmId != null)
                return false;
        } else if (!farmId.equals(other.farmId))
            return false;
        if (farmerId == null) {
            if (other.farmerId != null)
                return false;
        } else if (!farmerId.equals(other.farmerId))
            return false;
        if (mode != other.mode)
            return false;
        if (saveStatus != other.saveStatus)
            return false;
        if (scopeId != other.scopeId)
            return false;
        if (surveyCode == null) {
            if (other.surveyCode != null)
                return false;
        } else if (!surveyCode.equals(other.surveyCode))
            return false;
        if (surveyName == null) {
            if (other.surveyName != null)
                return false;
        } else if (!surveyName.equals(other.surveyName))
            return false;
        if (surveyTxnId == null) {
            if (other.surveyTxnId != null)
                return false;
        } else if (!surveyTxnId.equals(other.surveyTxnId))
            return false;

        return true;
    }

   
    /**
     * Gets the survey type id.
     * @return the survey type id
     */
    public long getSurveyTypeId() {

        return surveyTypeId;
    }

    /**
     * Sets the survey type id.
     * @param surveyTypeId the new survey type id
     */
    public void setSurveyTypeId(long surveyTypeId) {

        this.surveyTypeId = surveyTypeId;
    }

    /**
     * Sets the farmer family member code.
     * @param farmerFamilyMemberCode the new farmer family member code
     */
    public void setFarmerFamilyMemberCode(String farmerFamilyMemberCode) {

        this.farmerFamilyMemberCode = farmerFamilyMemberCode;
    }

    /**
     * Gets the farmer family member code.
     * @return the farmer family member code
     */
    public String getFarmerFamilyMemberCode() {

        return farmerFamilyMemberCode;
    }

    /**
     * Sets the farmer family member name.
     * @param farmerFamilyMemberName the new farmer family member name
     */
    public void setFarmerFamilyMemberName(String farmerFamilyMemberName) {

        this.farmerFamilyMemberName = farmerFamilyMemberName;
    }

    /**
     * Gets the farmer family member name.
     * @return the farmer family member name
     */
    public String getFarmerFamilyMemberName() {

        return farmerFamilyMemberName;
    }

    /**
     * Gets the from pagel.
     * @return the from pagel
     */
    public String getFromPagel() {

        return fromPagel;
    }

    /**
     * Sets the from pagel.
     * @param fromPagel the new from pagel
     */
    public void setFromPagel(String fromPagel) {

        this.fromPagel = fromPagel;
    }

   

    /**
     * Sets the sub form json data.
     * @param subFormJsonData the new sub form json data
     */
    public void setSubFormJsonData(String subFormJsonData) {

        this.subFormJsonData = subFormJsonData;
    }

    /**
     * Gets the json data for sub answers.
     * @return the json data for sub answers
     */
    public String getQuesCodes() {

        return quesCodes;
    }

    /**
     * Sets the json data for sub answers.
     * @param jsonDataForSubAnswers the new json data for sub answers
     */
    public void setQuesCodes(String quesCodes) {

        this.quesCodes = quesCodes;
    }

	public boolean isFetchAnswers() {
		return fetchAnswers;
	}

	public void setFetchAnswers(boolean fetchAnswers) {
		this.fetchAnswers = fetchAnswers;
	}

	public Set<SurveyAnswersDetails> getSurveyAnswerDetails() {
		return surveyAnswerDetails;
	}

	public void setSurveyAnswerDetails(Set<SurveyAnswersDetails> surveyAnswerDetails) {
		this.surveyAnswerDetails = surveyAnswerDetails;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public Set<SurveySubFormAnswersDetails> getSurveySubFormAnswersDetails() {
		return surveySubFormAnswersDetails;
	}

	public void setSurveySubFormAnswersDetails(Set<SurveySubFormAnswersDetails> surveySubFormAnswersDetails) {
		this.surveySubFormAnswersDetails = surveySubFormAnswersDetails;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public Date getToAnsweredDate() {
		return toAnsweredDate;
	}

	public void setToAnsweredDate(Date toAnsweredDate) {
		this.toAnsweredDate = toAnsweredDate;
	}

	public Date getFromAnsweredDate() {
		return fromAnsweredDate;
	}

	public void setFromAnsweredDate(Date fromAnsweredDate) {
		this.fromAnsweredDate = fromAnsweredDate;
	}

	public String getFarmerOrgCode() {
		return farmerOrgCode;
	}

	public void setFarmerOrgCode(String farmerOrgCode) {
		this.farmerOrgCode = farmerOrgCode;
	}

	public String getSubFormJsonData() {
		return subFormJsonData;
	}
	
	

}
