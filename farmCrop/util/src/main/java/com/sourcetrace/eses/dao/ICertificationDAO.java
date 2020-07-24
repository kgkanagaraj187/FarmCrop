/*
 * ICertificationDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerField;
import com.ese.entity.util.Language;
import com.sourcetrace.eses.entity.FarmCatalogue;
//import com.sourcetrace.eses.inspect.agrocert.AgentSurveyMapping;
import com.sourcetrace.eses.inspect.agrocert.CertificateCategory;
import com.sourcetrace.eses.inspect.agrocert.CertificateStandard;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.inspect.agrocert.Question;
import com.sourcetrace.eses.inspect.agrocert.Section;
import com.sourcetrace.eses.inspect.agrocert.SurveyMaster;
import com.sourcetrace.eses.inspect.agrocert.SurveyPreferences;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestion;
import com.sourcetrace.eses.inspect.agrocert.SurveySection;
import com.sourcetrace.eses.inspect.agrocert.SurveyType;
import com.sourcetrace.eses.order.entity.profile.Symptoms;
import com.sourcetrace.eses.txn.agrocert.DownloadTask;
import com.sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersQuestionAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersSectionAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmersQuestionAnswers;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.DataLevel;
import com.sourcetrace.esesw.entity.profile.Farmer;

public interface ICertificationDAO extends IESEDAO {

	 /**
     * Find certificate category by code.
     * @param categoryCode the category code
     * @return the certificate category
     */
    public CertificateCategory findCertificateCategoryByCode(String categoryCode);

    /**
     * List section by category code.
     * @param categoryCode the category code
     * @return the list< section>
     */
    public List<Section> listSectionByCategoryCode(String categoryCode);

    /**
     * Find farmer crop prod answers by id.
     * @param id the id
     * @return the farmer crop prod answers
     */
    public FarmerCropProdAnswers findFarmerCropProdAnswersById(Long id);

    /**
     * Find farmer question aswers by id.
     * @param id the id
     * @return the farmers question answers
     */
    public FarmersQuestionAnswers findFarmerQuestionAswersById(Long id);

    /**
     * List of inspection report farmers.
     * @param categoryCode the category code
     * @return the list< string>
     */
    public List<String> listOfInspectionReportFarmers(String categoryCode);

    /**
     * List certificate category.
     * @return the list< certificate category>
     */
    public List<CertificateCategory> listCertificateCategory();

    /**
     * List of farmers.
     * @return the list< string>
     */
    public List<String> listOfFarmers();

    /**
     * Load farmer failed question procedure.
     * @param categoryCode the category code
     * @param answerType the answer type
     * @param answer the answer
     * @param userName the user name
     * @param farmerId the farmer id
     * @param startDate the start date
     * @param endDate the end date
     */
    public void loadFarmerFailedQuestionProcedure(String categoryCode, String answerType,
            String answer, String userName, String farmerId, String startDate, String endDate);

    /**
     * Call failed question procedure.
     * @param catgCode the catg code
     * @param answerType the answer type
     * @param answer the answer
     * @param startDate the start date
     * @param endDate the end date
     * @param userName the user name
     */
    public void callFailedQuestionProcedure(String catgCode, String answerType, String answer,
            String startDate, String endDate, String userName);

    /**
     * List failed questions by farmer chart data.
     * @param string the string
     * @return the list< object[]>
     */
    public List<Object[]> listFailedQuestionsByFarmerChartData(String string);

    /**
     * List failed questions chart data.
     * @param string the string
     * @return the list< object[]>
     */
    public List<Object[]> listFailedQuestionsChartData(String string);

    /**
     * List farmer inspection chart.
     * @param startDate the start date
     * @param endDate the end date
     * @return the list< object[]>
     */
    public List<Object[]> listFarmerInspectionChart(String startDate, String endDate);

    /**
     * List of inspection farmers.
     * @param selectedCategory the selected category
     * @return the list< farmer>
     */
    public List<Farmer> listOfInspectionFarmers(String selectedCategory);

    /**
     * Find certificate standard by id.
     * @param id the id
     * @return the certificate standard
     */
    public CertificateStandard findCertificateStandardById(long id);

    /**
     * List certificate standard by certificate category id.
     * @param id the id
     * @return the list< certificate standard>
     */
    public List<CertificateStandard> listCertificateStandardByCertificateCategoryId(long id);

    /**
     * Find certificate category by id.
     * @param id the id
     * @return the certificate category
     */
    public CertificateCategory findCertificateCategoryById(long id);

    /**
     * List certificate standard.
     * @return the list< certificate standard>
     */
    public List<CertificateStandard> listCertificateStandard();

    /**
     * List certification category state chart data.
     * @param limitValue the limit value
     * @return the list< object[]>
     */
    public List<Object[]> listCertificationCategoryStateChartData(int limitValue);

    /**
     * List certification standard state chart data.
     * @param certificationCategoryCode the certification category code
     * @param limitValue the limit value
     * @return the list< object[]>
     */
    public List<Object[]> listCertificationStandardStateChartData(String certificationCategoryCode,
            int limitValue);

    /**
     * List certificate category names.
     * @return the list< string>
     */
    public List<String> listCertificateCategoryNames();

    /**
     * List certification standard names.
     * @param certificationCategoryCode the certification category code
     * @return the list< string>
     */
    public List<String> listCertificationStandardNames(String certificationCategoryCode);

    /**
     * List farmers by certification category.
     * @param certificateId the certificate id
     * @return the list< object[]>
     */
    public List<Object[]> listFarmersByCertificationCategory(long certificateId);

    /**
     * List traning time line data.
     * @return the list< object[]>
     */
    public List<Object[]> listTraningTimeLineData();

    /**
     * Find certificate standard by code.
     * @param code the code
     * @return the certificate standard
     */
    public CertificateStandard findCertificateStandardByCode(String code);

    public List<Symptoms> findSymptomsCodeByType(Integer type);

    public byte[] findInspectionImageById(Long id);

	

	public List listAnswersByQuestionAnswers(long id);

	public List listQuestionByCategoryCode(String code);

	public List listAnswersBySubQuestionAnswers(long id);

	FarmerCropProdAnswers findFarmerCropProdAnswersByFarmerId(String farmerId, String category);

public	List<FarmersSectionAnswers> listsectionByFarmerCropProd(long id);

public Section findSectionByCode(String string);

Question findQuestionByCode(String string);
	

public boolean isQuestionMappedWithSurvey(long id);

public Country findCountryByCode(String contryCode);

public SurveyQuestion findQuestionById(long id);

public List<Country> listCountryByCode(String contryCode);

public List<Country> listCountries();

public List<SurveySection> listAllSectionsForFOFarmerFarmByCountryId(long Cid);

public List<SurveyQuestion> listParentQuestions(long sectionId, String questionId);

public List<Language> listLanguages();

public List<LanguagePreferences> listLangPrefByCode(String code);

public boolean isParentQuestion(long id);

public void removeQuestionSurveyMapping(SurveyQuestion question);

public List<SurveyQuestion> listQuestionsNumericBySectionId(long sectionId);

public List<SurveyPreferences> listSurveyPreferencesByType(int type);

public DataLevel findDataLevelBySectionId(long sectionId);

public List<Object[]> listQuestionsByQuestionTypeAndComponentTypeAndSection(long sectionId);

public List<SurveyQuestion> listQuestionsByCodes(List<String> codes);

public List<FarmCatalogue> listCataloguesByTypeAndCountry(String type, long cid);

public List<FarmCatalogue> listCataloguesByType(int type);

public List<FarmCatalogue> listCataloguesByCodes(List<String> codes);

public FarmCatalogue findCatalogueByCode(String code);

public void updateQuestion(String code, List<String> questionCodes);

public Country findCountryById(Long id);

public boolean isQuestionAndSubFormMappedWithSurvey(long id);

public SurveySection findSectionById(long id);

public List<SurveyQuestion> listParentQuestions(long sectionId);

public List<SurveySection> listSections();

public SurveyPreferences findSurveyPreference(String fromUnit, String toUnit, int type, Country country);

public SurveySection findSectionByName(String selectedSection);

public List<Language> findIsSurveyStatusLanguage();

public long findSurveyTypeBySurveyCode(String surveyCode);

public List<Object[]> listDataLevel();

public List<LanguagePreferences> listLanguagePreferencesByLanguageAndType(String lang, int type);

public List<Object[]> listSectionsByDataLevelCode(String code);

public SurveyMaster findSurveyMasterBySurveyCode(String code);

public Object findEntityByEntityNameAndProperty(String string, String string2, String farmCropProdAnswerProperty);

public SurveyFarmerCropProdAnswers findSurveyFarmerCropProdAnswersById(Long id);

public List<Object[]> listSubFormQuestionsAnswerCatalogueBySurveyCode(String surveyCode);

public List<Object[]> listSubFormQuestionsUnitsCatalogueBySurveyCode(String surveyCode);

public List<Object[]> listAnswerCataLogueBySurveyCode(String surveyCode);

public List<Object[]> listUnitCataLogueBySurveyCode(String surveyCode);

public List<Object[]> listSurveyMasters();

void saveFarmerCropProdAnswers(SurveyFarmerCropProdAnswers farmerCropProdAnswers);

public void editFarmerCropProdAnsStatus(long id, int status, String username, String deleteReason);

public List<Object[]> listPartiallySavedFarmerCropPAInfo();

public SurveyFarmersQuestionAnswers findSurveyFarmerQuestionAswersById(Long id);

public SurveyFarmersQuestionAnswers findVerificationPhototById(Long id);

public List<SurveyQuestion> listQuestionsBySectionCode(String sectionCode);

public SurveyQuestion findSurveyQuestionByCode(String questionCode);

DataLevel findDataLevelBySectionCode(String sectionId);
	
public List<FarmerField> findFieldsByParentIdandStatsuId(String code);


public List<Long> listSurveyMasterIdsByAgentId(String agentId);

public void editSurveyMaster(SurveyMaster surveyMaster);

public SurveyMaster findSurveyMasterById(Long id);

public List<SurveySection> listSectionsByCodes(List<String> codes);

public List<SurveyType> listSurveyTypes();

public void updateSurveyMasterVerifyQuestion(long surveyId, List<Long> questionIds);

public String findDataLevelCodeByInfraTypeCode(String infraTypeCode);

public List<Section> listAllSectionsForFOFarmerFarm();

public List<SurveySection> listSectionCodesByDataLevelId(long dataLvlId);

public boolean isSurveyMasterMappedWithSurveyScope(long id);

public SurveyType findSurveyTypeById(long id);

public List<String> listQuestionsByIds(List<String> ids);

public void removeSurveyMasterQuestionMapping(SurveyMaster surveyMaster);

public void removeSurveyMasterTarget(SurveyMaster surveyMaster);

public int isSurveyMasterVerified(String code);

public List<Object[]> listAllSectionsObjectArrayForFOFarmerFarm();

public List<SurveyQuestion> listQuestionsBySectionCodes(List<String> sectionCodes);

public List<SurveyQuestion> listQuestionsBySectionCodesAndQuestionType(List<String> sectionCodes, int questionTyp);	

public List<String> listFilteredQuestionsByIds(List<String> ids);

public LanguagePreferences findLanguagePreferenceByCodeAndLanguage(String string, String loggedInUserLanguage);

public List<SurveyMaster> listSurveyMasterObjectByDvId(String dataCollectorId, long revisionNo);

public List<SurveyMaster> listSurveyMasterObjectByAgentId(String dataCollectorId, long revisionNo);

public long surveyMasterRevisionByAgentId(String agentId);

public DataLevel findDataLevelByCode(String code);

public List<Object[]> listSectionBySurveyMaster(SurveyMaster sm);

public List<LanguagePreferences> findNameLanguageAndType(String lang, String type);

public List<String> listFoCodesByCustomerProgram(List<String> codes);

public List<DownloadTask> listDownloadTaskByUser(String userName);

public void updateDownloadTask(DownloadTask downloadTask);

public DownloadTask findDownloadTaskById(long id);

public List<Object[]> findFarmerQuestionAnswerByIdAndMultiAnswers(List<Long> id);

public List<String> findFQAAnswersByAnswerId(long id);

public List<Object[]> listSamithiByFcpa();

SurveyQuestion findQuestionByParentDepen(String str);

String findDependancyKeyByQuestionId(Long parentId);

public List<LanguagePreferences> listLanguagePreferences();

public List<FarmField> findFarmFieldsByParentIdandStatsuId(String sectionId2);

public List<Object[]> findFarmCropsFieldsByParentIdandStatsuId(String sectionId2);

public List<SurveyMaster> listSurveyBySurveyCodes(List<String> surveyCodes);

public List<SurveyMaster> listAgentSurveyMaster(String agentId, String revisionNo);
public List<SurveyQuestion> listSurveyQuestion(String revisionNo);
public List<SurveySection> listSurveySections(String revisionNo);

List<LanguagePreferences> listLanguagePreferencesByLanguage(int type);

List<LanguagePreferences> listLangPrefByCodes(List<String> codes);

}
