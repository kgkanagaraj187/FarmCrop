/*
\ * ICertificationService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.script.ScriptException;

import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerField;
import com.ese.entity.util.Language;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmerFeedbackEntity;
//import com.sourcetrace.eses.inspect.agrocert.AgentSurveyMapping;
import com.sourcetrace.eses.inspect.agrocert.CertificateCategory;
import com.sourcetrace.eses.inspect.agrocert.CertificateStandard;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.inspect.agrocert.Question;
import com.sourcetrace.eses.inspect.agrocert.Section;
import com.sourcetrace.eses.inspect.agrocert.SubFormQuestionMapping;
import com.sourcetrace.eses.inspect.agrocert.SurveyMaster;
import com.sourcetrace.eses.inspect.agrocert.SurveyPreferences;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestion;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestionMapping;
import com.sourcetrace.eses.inspect.agrocert.SurveySection;
import com.sourcetrace.eses.inspect.agrocert.SurveyType;
import com.sourcetrace.eses.order.entity.profile.Symptoms;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.txn.agrocert.DownloadTask;
import com.sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersQuestionAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersSectionAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmersQuestionAnswers;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.DataLevel;
import com.sourcetrace.esesw.entity.profile.Farmer;

public interface ICertificationService {

	public static final String REGISTERED_QUESTION = "1";
	public static final String UN_REGISTERED_QUESTION = "0";
	public static final String NORMAL_SECTION = "00";
	public static final String SECTION_THREE = "03";
	public static final String SECTION_SIX = "06";
	public static final String SECTION_THREE_FIRST_TABLE = "3.1";
	public static final String SECTION_SIX_FIRST_TABLE = "6.1";

	/**
	 * Find certificate category by code.
	 * 
	 * @param categoryCode
	 *            the category code
	 * @return the certificate category
	 */
	public CertificateCategory findCertificateCategoryByCode(String categoryCode);

	/**
	 * List section by category code.
	 * 
	 * @param categoryCode
	 *            the category code
	 * @return the list< section>
	 */
	public List<Section> listSectionByCategoryCode(String categoryCode);

	/**
	 * Adds the farmer crop prod answers.
	 * 
	 * @param farmerCropProdAnswers
	 *            the farmer crop prod answers
	 */
	public void addFarmerCropProdAnswers(FarmerCropProdAnswers farmerCropProdAnswers);

	/**
	 * Find farmer crop prod answers by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farmer crop prod answers
	 */
	FarmerCropProdAnswers findFarmerCropProdAnswersById(Long id);

	/**
	 * Find farmer question aswers by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farmers question answers
	 */
	FarmersQuestionAnswers findFarmerQuestionAswersById(Long id);

	/**
	 * List of inspection report farmers.
	 * 
	 * @param categoryCode
	 *            the category code
	 * @return the list< string>
	 */
	List<String> listOfInspectionReportFarmers(String categoryCode);

	/**
	 * Load farmer failed question procedure.
	 * 
	 * @param categoryCode
	 *            the category code
	 * @param answerType
	 *            the answer type
	 * @param answer
	 *            the answer
	 * @param userName
	 *            the user name
	 * @param farmerId
	 *            the farmer id
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 */
	void loadFarmerFailedQuestionProcedure(String categoryCode, String answerType, String answer, String userName,
			String farmerId, String startDate, String endDate);

	/**
	 * List of farmers.
	 * 
	 * @return the list< string>
	 */
	List<String> listOfFarmers();

	/**
	 * List certificate category.
	 * 
	 * @return the list< certificate category>
	 */
	List<CertificateCategory> listCertificateCategory();

	/**
	 * Call failed question procedure.
	 * 
	 * @param catgCode
	 *            the catg code
	 * @param answerType
	 *            the answer type
	 * @param answer
	 *            the answer
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @param userName
	 *            the user name
	 */
	public void callFailedQuestionProcedure(String catgCode, String answerType, String answer, String startDate,
			String endDate, String userName);

	/**
	 * List farmer inspection chart.
	 * 
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @return the list< object[]>
	 */
	public List<Object[]> listFarmerInspectionChart(String startDate, String endDate);

	/**
	 * List failed questions by farmer chart data.
	 * 
	 * @param category
	 *            the category
	 * @param answerType
	 *            the answer type
	 * @param answer
	 *            the answer
	 * @param limitValue
	 *            the limit value
	 * @return the list< object[]>
	 */
	public List<Object[]> listFailedQuestionsByFarmerChartData(String category, String answerType, String answer,
			int limitValue);

	/**
	 * List failed questions chart data.
	 * 
	 * @param category
	 *            the category
	 * @param farmerId
	 *            the farmer id
	 * @param answerType
	 *            the answer type
	 * @param answer
	 *            the answer
	 * @param limitValue
	 *            the limit value
	 * @return the list< object[]>
	 */
	public List<Object[]> listFailedQuestionsChartData(String category, String farmerId, String answerType,
			String answer, int limitValue);

	/**
	 * List of inspection farmers.
	 * 
	 * @param selectedCategory
	 *            the selected category
	 * @return the list< farmer>
	 */
	public List<Farmer> listOfInspectionFarmers(String selectedCategory);

	/**
	 * Find certificate standard by id.
	 * 
	 * @param id
	 *            the id
	 * @return the certificate standard
	 */
	public CertificateStandard findCertificateStandardById(long id);

	/**
	 * List certificate standard by certificate category id.
	 * 
	 * @param id
	 *            the id
	 * @return the list< certificate standard>
	 */
	public List<CertificateStandard> listCertificateStandardByCertificateCategoryId(long id);

	/**
	 * Find certificate category by id.
	 * 
	 * @param id
	 *            the id
	 * @return the certificate category
	 */
	public CertificateCategory findCertificateCategoryById(long id);

	/**
	 * List certificate standard.
	 * 
	 * @return the list< certificate standard>
	 */
	public List<CertificateStandard> listCertificateStandard();

	/**
	 * List certification category state chart data.
	 * 
	 * @param limitValue
	 *            the limit value
	 * @return the list< object[]>
	 */
	public List<Object[]> listCertificationCategoryStateChartData(int limitValue);

	/**
	 * List certification standard state chart data.
	 * 
	 * @param certificationCategoryCode
	 *            the certification category code
	 * @param limitValue
	 *            the limit value
	 * @return the list< object[]>
	 */
	public List<Object[]> listCertificationStandardStateChartData(String certificationCategoryCode, int limitValue);

	/**
	 * List certificate category names.
	 * 
	 * @return the list< string>
	 */
	public List<String> listCertificateCategoryNames();

	/**
	 * List certification standard names.
	 * 
	 * @param certificationCategoryCode
	 *            the certification category code
	 * @return the list< string>
	 */
	public List<String> listCertificationStandardNames(String certificationCategoryCode);

	/**
	 * List farmers by certification category.
	 * 
	 * @param certificateId
	 *            the certificate id
	 * @return the list< object[]>
	 */
	public List<Object[]> listFarmersByCertificationCategory(long certificateId);

	/**
	 * List traning time line data.
	 * 
	 * @return the list< object[]>
	 */
	public List<Object[]> listTraningTimeLineData();

	/**
	 * Find certificate standard by code.
	 * 
	 * @param code
	 *            the code
	 * @return the certificate standard
	 */
	public CertificateStandard findCertificateStandardByCode(String code);

	/**
	 * Adds the crop yield.
	 * 
	 * @param cropYield
	 *            the crop yield
	 */
	public void addCropYield(CropYield cropYield);

	/**
	 * Adds the farm crop prod answers and crop yield.
	 * 
	 * @param farmerCropProdAnswers
	 *            the farmer crop prod answers
	 * @param cropYield
	 *            the crop yield
	 */
	public void addFarmCropProdAnswersAndCropYield(FarmerCropProdAnswers farmerCropProdAnswers, CropYield cropYield);

	public List<Symptoms> findSymptomsCodeByType(Integer type);

	public void addPeriodicInspection(PeriodicInspection periodicInspection);

	public byte[] findInspectionImageById(Long id);

	public List listAnswersByQuestionAnswers(long id);

	public List listAnswersBySubQuestionAnswers(long id);

	FarmerCropProdAnswers findFarmerCropProdAnswersByFarmerId(String farmerId, String category);

	public List<FarmersSectionAnswers> listsectionByFarmerCropProd(long id);

	public Section findSectionByCode(String string);

	public Question findQuestionByCode(String string);

	public void addFarmerFeedback(FarmerFeedbackEntity fcpa);

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

	public List<FarmCatalogue> listCataloguesByCodes(List<String> codes);

	public FarmCatalogue findCatalogueByCode(String code);

	public void addQuestion(SurveyQuestion question);

	public void updateQuestion(String code, List<String> questionCodes);

	public Country findCountryById(Long id);

	public boolean isQuestionAndSubFormMappedWithSurvey(long id);

	public SurveySection findSectionById(long id);

	public void editQuestion(SurveyQuestion question);

	public void removeQuestion(SurveyQuestion question);

	public List<SurveyQuestion> listParentQuestions(long sectionId);

	public List<SurveySection> listSections();

	List<FarmCatalogue> listCataloguesByType(int type);

	public void addSection(SurveySection surveySection);

	public SurveySection findSectionByName(String selectedSection);

	public List<Language> findIsSurveyStatusLanguage();

	long findSurveyTypeBySurveyCode(String surveyCode);

	List<Object[]> listDataLevel();

	Map<String, LanguagePreferences> listLanguagePreferencesMapByLanguageAndType(String lang, int type);

	List<Object[]> listSectionsByDataLevelCode(String code);

	List<SurveyFarmersQuestionAnswers> processFormattingAnswersSubForm(
			SortedSet<SubFormQuestionMapping> subFormQuestionMap,
			Map<String, SurveyFarmersQuestionAnswers> farmerQuestionSubFormMap, Map<String, String> catalogueMap,
			Map<String, SubFormQuestionMapping> subFormQuestionMapping) throws ScriptException;

	void processFormattingAnswers(SurveyFarmersQuestionAnswers farmersQuestionAnswers, SurveyQuestion question,
			SurveyAnswers answers);

	SurveyAnswers processUnits(SurveyAnswers answers, SurveyQuestion question, Map<String, String> catalogueMap,
			Map<String, SurveyPreferences> convertionMap, int mode);

	void processOnceQuestion(SurveyQuestion question, Map<String, String> catalogueMap,
			SurveyFarmerCropProdAnswers farmerCropProdAnswers, SurveyAnswers answers,
			Map<String, Object> onceQuestionEntityMap);

	void addSurveyFarmerCropProdAnswers(SurveyFarmerCropProdAnswers farmerCropProdAnswers);

	void removeSurveyFarmerCropProdAnswers(SurveyFarmerCropProdAnswers farmerCropProdAnswers);

	List<Object[]> listCatalogueBySurveyCode(String surveyCode);

	public List<Object[]> listSurveyMasters();

	public void editFarmerCropProdAnsStatus(long id, int status, String username, String date);

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

	public void addSurveyMaster(SurveyMaster surveyMaster);

	public boolean isSurveyMasterMappedWithSurveyScope(long id);

	public SurveyType findSurveyTypeById(long id);

	public List<String> listQuestionsByIds(List<String> ids);

	public void removeSurveyMasterQuestionMapping(SurveyMaster surveyMaster);

	public void removeSurveyMasterTarget(SurveyMaster surveyMaster);

	public void removeSurveyMaster(SurveyMaster surveyMaster);

	public int isSurveyMasterVerified(String code);

	public List<Object[]> listAllSectionsObjectArrayForFOFarmerFarm();

	public List<SurveyQuestion> listQuestionsBySectionCodes(List<String> sectionCodes);

	public List<SurveyQuestion> listQuestionsBySectionCodesAndQuestionType(List<String> sectionCodes, int questionTyp);

	public List<LanguagePreferences> listLanguagePreferencesByLanguageAndType(String lang, int type);

	public List<String> listFilteredQuestionsByIds(List<String> ids);

	public LanguagePreferences findLanguagePreferenceByCodeAndLanguage(String string, String loggedInUserLanguage);

	public List<SurveyMaster> listSurveyMasterObjectByDvId(String dataCollectorId, long revisionNo);

	public List<SurveyMaster> listSurveyMasterObjectByAgentId(String dataCollectorId, long revisionNo);

	public long surveyMasterRevisionByAgentId(String agentId);

	public DataLevel findDataLevelByCode(String code);

	public List<Object[]> listSectionBySurveyMaster(SurveyMaster sm);

	public List<LanguagePreferences> findNameLanguageAndType(String lang, String type);

	public DataLevel findDataLevelBySectionCode(String string);

	public List<String> listFoCodesByCustomerProgram(List<String> codes);

	public List<DownloadTask> listDownloadTaskByUser(String userName);

	public void updateDownloadTask(DownloadTask downloadTask);

	public void addDownloadTask(DownloadTask downloadTask);

	public DownloadTask findDownloadTaskById(long id);

	public List<Object[]> findFarmerQuestionAnswerByIdAndMultiAnswers(List<Long> fqaIda);

	public void editFarmerCropProdAnswers(SurveyFarmerCropProdAnswers farmerCropProdAnswers);

	public List<String> findFQAAnswersByAnswerId(long id);

	public List<Object[]> listSamithiByFcpa();

	SurveyQuestion findQuestionByParentDepen(String str);

	String findDependancyKeyByQuestionId(Long parentId);

	public List<LanguagePreferences> listLanguagePreferences();

	public SurveyQuestion findSurveyQuestionByCode(String code);

	public List<FarmField> findFarmFieldsByParentIdandStatsuId(String sectionId2);

	public List<Object[]> findFarmCropsFieldsByParentIdandStatsuId(String sectionId2);

	public List<SurveyMaster> listSurveyBySurveyCodes(List<String> surveyCodes);

	public List<SurveyMaster> listAgentSurveyMaster(String agentId, String revisionNo);

	public List<SurveyQuestion> listSurveyQuestion(String revisionNo);

	public List<SurveySection> listSurveySections(String revisionNo);

	List<LanguagePreferences> listLanguagePreferencesByLanguage(int type);
	
	List<LanguagePreferences> listLangPrefByCodes(List<String> codes);

}
