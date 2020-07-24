/*
 * CertificationService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerField;
import com.ese.entity.util.Language;
import com.sourcetrace.eses.dao.ICertificationDAO;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmerFeedbackEntity;
//import com.sourcetrace.eses.inspect.agrocert.AgentSurveyMapping;
import com.sourcetrace.eses.inspect.agrocert.CertificateCategory;
import com.sourcetrace.eses.inspect.agrocert.CertificateStandard;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.inspect.agrocert.Question;
import com.sourcetrace.eses.inspect.agrocert.Section;
import com.sourcetrace.eses.inspect.agrocert.SubFormAnswerMapping;
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
import com.sourcetrace.eses.txn.agrocert.SurveyAnswersDetails;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmersQuestionAnswers;
import com.sourcetrace.eses.txn.agrocert.SurveyFarmersSectionAnswers;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.DataLevel;
import com.sourcetrace.esesw.entity.profile.Farmer;

@Service
@Transactional
public class CertificationService implements ICertificationService {
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(CertificationService.class);

		@Autowired
	  private ICertificationDAO certificationDAO;

		
		/** The farmer dao. */
		@Autowired
		private IFarmerDAO farmerDAO;

	    /**
	     * Sets the certification dao.
	     * @param certificationDAO the new certification dao
	     */
	    public void setCertificationDAO(ICertificationDAO certificationDAO) {

	        this.certificationDAO = certificationDAO;
	    }

	    /**
	     * Gets the certification dao.
	     * @return the certification dao
	     */
	    public ICertificationDAO getCertificationDAO() {

	        return certificationDAO;
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * addFarmerCropProdAnswers(com. sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers)
	     */
	    public void addFarmerCropProdAnswers(FarmerCropProdAnswers farmerCropProdAnswers) {

	        certificationDAO.save(farmerCropProdAnswers);

	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * findCertificateCategoryByCode (java.lang.String)
	     */
	    public CertificateCategory findCertificateCategoryByCode(String categoryCode) {

	        return certificationDAO.findCertificateCategoryByCode(categoryCode);
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * listSectionByCategoryCode(java .lang.String)
	     */
	    public List<Section> listSectionByCategoryCode(String categoryCode) {

	        return certificationDAO.listSectionByCategoryCode(categoryCode);

	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * findFarmerCropProdAnswersById(java.lang.Long)
	     */
	    public FarmerCropProdAnswers findFarmerCropProdAnswersById(Long id) {

	        return certificationDAO.findFarmerCropProdAnswersById(id);
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * findFarmerQuestionAswersById(java.lang.Long)
	     */
	    public FarmersQuestionAnswers findFarmerQuestionAswersById(Long id) {

	        return certificationDAO.findFarmerQuestionAswersById(id);
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * listOfInspectionReportFarmers(java.lang.String)
	     */
	    public List<String> listOfInspectionReportFarmers(String categoryCode) {

	        return certificationDAO.listOfInspectionReportFarmers(categoryCode);
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService# listCertificateCategory()
	     */
	    public List<CertificateCategory> listCertificateCategory() {

	        return certificationDAO.listCertificateCategory();
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.eses.agrocert.service.ICertificationService#listOfFarmers ()
	     */
	    public List<String> listOfFarmers() {

	        return certificationDAO.listOfFarmers();
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * loadFarmerFailedQuestionProcedure(java.lang.String, java.lang.String, java.lang.String,
	     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	     */
	    public void loadFarmerFailedQuestionProcedure(String categoryCode, String answerType,
	            String answer, String userName, String farmerId, String startDate, String endDate) {

	        certificationDAO.loadFarmerFailedQuestionProcedure(categoryCode, answerType, answer,
	                userName, farmerId, startDate, endDate);

	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * callFailedQuestionProcedure(java.lang.String, java.lang.String, java.lang.String,
	     * java.lang.String, java.lang.String, java.lang.String)
	     */
	    public void callFailedQuestionProcedure(String catgCode, String answerType, String answer,
	            String startDate, String endDate, String userName) {

	        certificationDAO.callFailedQuestionProcedure(catgCode, answerType, answer, startDate,
	                endDate, userName);

	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * listFailedQuestionsByFarmerChartData(java.lang.String, java.lang.String, java.lang.String,
	     * int)
	     */
	    public List<Object[]> listFailedQuestionsByFarmerChartData(String category, String answerType,
	            String answer, int limitValue) {

	        StringBuffer query = new StringBuffer();
	        query
	                .append("SELECT FARMER_ID,SUM(FAILED_COUNT) AS FAILED_COUNT FROM")
	                .append(
	                        "(SELECT fcpa.FARMER_ID AS FARMER_ID,COUNT(1) AS FAILED_COUNT FROM farmer_crop_prod_answers fcpa")
	                .append(
	                        " INNER JOIN farmer_section_answers fsa ON fcpa.id=fsa.FARMER_CROP_PROD_ANSWERS_ID")
	                .append(
	                        " INNER JOIN farmer_question_answers fqa ON fsa.id=fqa.FARMER_SECTION_ANSWERS_ID")
	                .append(" INNER JOIN answers a ON fqa.ID=a.FARMER_QUESTION_ANSWERS_ID")
	                .append(" WHERE  a.ANSWER_TYPE IN (" + answerType + ",2 ) AND a.ANSWER=" + answer)
	                .append(" AND fcpa.CATEGORY_CODE LIKE '%" + category + "%'")
	                .append(" GROUP BY fcpa.FARMER_ID")
	                .append(" UNION")

	                .append(
	                        " SELECT fcpa.FARMER_ID AS FARMER_ID,COUNT(1) AS FAILED_COUNT FROM farmer_crop_prod_answers fcpa")
	                .append(
	                        " INNER JOIN farmer_section_answers fsa ON fcpa.id=fsa.FARMER_CROP_PROD_ANSWERS_ID")
	                .append(
	                        " INNER JOIN farmer_question_answers fqa ON fsa.id=fqa.FARMER_SECTION_ANSWERS_ID")
	                .append(
	                        " INNER JOIN farmer_question_answers fqa1 ON fqa.id=fqa1.PARENT_FARMER_QUESTION_ANSWERS_ID")
	                .append(" INNER JOIN answers a ON fqa1.ID=a.FARMER_QUESTION_ANSWERS_ID").append(
	                        " WHERE a.ANSWER_TYPE IN (" + answerType + ",2 ) AND a.ANSWER=" + answer)
	                .append(" AND fcpa.CATEGORY_CODE LIKE '%" + category + "%'").append(
	                        " GROUP BY fcpa.FARMER_ID").append(
	                        ") Questions_And_SubQuestions GROUP BY FARMER_ID ORDER BY FAILED_COUNT DESC limit "
	                                + limitValue);

	        return certificationDAO.listFailedQuestionsByFarmerChartData(query.toString());

	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * listFailedQuestionsChartData(java.lang.String, java.lang.String, java.lang.String,
	     * java.lang.String, int)
	     */
	    public List<Object[]> listFailedQuestionsChartData(String category, String farmerId,
	            String answerType, String answer, int limitValue) {

	        category = StringUtil.isEmpty(category) ? "" : category;
	        farmerId = StringUtil.isEmpty(farmerId) ? "" : farmerId;

	        StringBuffer query = new StringBuffer();
	        query
	                .append("SELECT SERIAL_NO,FAILED_COUNT FROM")
	                .append(
	                        "(SELECT fqa.SERIAL_NO AS SERIAL_NO,COUNT(1) AS FAILED_COUNT FROM farmer_crop_prod_answers fcpa")
	                .append(
	                        " INNER JOIN farmer_section_answers fsa ON fcpa.id=fsa.FARMER_CROP_PROD_ANSWERS_ID")
	                .append(
	                        " INNER JOIN farmer_question_answers fqa ON fsa.id=fqa.FARMER_SECTION_ANSWERS_ID")
	                .append(" INNER JOIN answers a ON fqa.ID=a.FARMER_QUESTION_ANSWERS_ID")
	                .append(" WHERE a.ANSWER_TYPE IN (" + answerType + ",2 ) AND a.ANSWER=" + answer)
	                .append(
	                        " AND fcpa.CATEGORY_CODE LIKE '%" + category
	                                + "%' AND fcpa.FARMER_ID LIKE '%" + farmerId + "%'")
	                .append(" AND fqa.SERIAL_NO IS NOT NULL")
	                .append(" GROUP BY fqa.QUESTION_CODE")
	                .append(" UNION")

	                .append(
	                        " SELECT fqa1.SERIAL_NO AS SERIAL_NO,COUNT(1) AS FAILED_COUNT FROM farmer_crop_prod_answers fcpa")
	                .append(
	                        " INNER JOIN farmer_section_answers fsa ON fcpa.id=fsa.FARMER_CROP_PROD_ANSWERS_ID")
	                .append(
	                        " INNER JOIN farmer_question_answers fqa ON fsa.id=fqa.FARMER_SECTION_ANSWERS_ID")
	                .append(
	                        " INNER JOIN farmer_question_answers fqa1 ON fqa.id=fqa1.PARENT_FARMER_QUESTION_ANSWERS_ID")
	                .append(" INNER JOIN answers a ON fqa1.ID=a.FARMER_QUESTION_ANSWERS_ID").append(
	                        " WHERE a.ANSWER_TYPE IN (" + answerType + ",2 ) AND a.ANSWER=" + answer)
	                .append(
	                        " AND fcpa.CATEGORY_CODE LIKE '%" + category
	                                + "%' AND fcpa.FARMER_ID LIKE '%" + farmerId + "%'").append(
	                        " AND fqa1.SERIAL_NO IS NOT NULL").append(" GROUP BY fqa1.QUESTION_CODE")
	                .append(
	                        ") Questions_And_SubQuestions ORDER BY FAILED_COUNT DESC limit "
	                                + limitValue);

	        return certificationDAO.listFailedQuestionsChartData(query.toString());
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * listFarmerInspectionChart(java.lang.String, java.lang.String)
	     */
	    public List<Object[]> listFarmerInspectionChart(String startDate, String endDate) {

	        return certificationDAO.listFarmerInspectionChart(startDate, endDate);
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * listOfInspectionFarmers(java.lang.String)
	     */
	    public List<Farmer> listOfInspectionFarmers(String selectedCategory) {

	        return certificationDAO.listOfInspectionFarmers(selectedCategory);
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.eses.agrocert.service.ICertificationService#findCertificateStandardById(long)
	     */
	    public CertificateStandard findCertificateStandardById(long id) {

	        return certificationDAO.findCertificateStandardById(id);
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * listCertificateStandardByCertificateCategoryId(long)
	     */
	    public List<CertificateStandard> listCertificateStandardByCertificateCategoryId(long id) {

	        return certificationDAO.listCertificateStandardByCertificateCategoryId(id);
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.eses.agrocert.service.ICertificationService#findCertificateCategoryById(long)
	     */
	    public CertificateCategory findCertificateCategoryById(long id) {

	        return certificationDAO.findCertificateCategoryById(id);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.eses.agrocert.service.ICertificationService#listCertificateStandard()
	     */
	    public List<CertificateStandard> listCertificateStandard() {

	        return certificationDAO.listCertificateStandard();
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * listCertificationCategoryStateChartData(int)
	     */
	    public List<Object[]> listCertificationCategoryStateChartData(int limitValue) {

	        return certificationDAO.listCertificationCategoryStateChartData(limitValue);
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	     * listCertificationStandardStateChartData(java.lang.String, int)
	     */
	    public List<Object[]> listCertificationStandardStateChartData(String certificationCategoryCode,
	            int limitValue) {

	        return certificationDAO.listCertificationStandardStateChartData(certificationCategoryCode,
	                limitValue);
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.eses.agrocert.service.ICertificationService#listCertificateCategoryNames()
	     */
	    public List<String> listCertificateCategoryNames() {

	        return certificationDAO.listCertificateCategoryNames();
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.eses.agrocert.service.ICertificationService#listCertificationStandardNames
	     * (java.lang.String)
	     */
	    public List<String> listCertificationStandardNames(String certificationCategoryCode) {

	        return certificationDAO.listCertificationStandardNames(certificationCategoryCode);
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.eses.agrocert.service.ICertificationService#listFarmersByCertificationCategory
	     * (long)
	     */
	    public List<Object[]> listFarmersByCertificationCategory(long certificateId) {

	        return certificationDAO.listFarmersByCertificationCategory(certificateId);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.eses.agrocert.service.ICertificationService#listTraningTimeLineData()
	     */
	    public List<Object[]> listTraningTimeLineData() {

	        return certificationDAO.listTraningTimeLineData();
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.eses.agrocert.service.ICertificationService#findCertificateStandardByCode
	     * (java.lang.String)
	     */
	    public CertificateStandard findCertificateStandardByCode(String code) {

	        return certificationDAO.findCertificateStandardByCode(code);
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.eses.agrocert.service.ICertificationService#addCropYield(com.sourcetrace.
	     * eses.txn.agrocert.CropYield)
	     */
	    public void addCropYield(CropYield cropYield) {

	        certificationDAO.save(cropYield);
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.eses.agrocert.service.ICertificationService#addFarmCropProdAnswersAndCropYield
	     * (com.sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers,
	     * com.sourcetrace.eses.txn.agrocert.CropYield)
	     */
	    public void addFarmCropProdAnswersAndCropYield(FarmerCropProdAnswers farmerCropProdAnswers,
	            CropYield cropYield) {

	        addFarmerCropProdAnswers(farmerCropProdAnswers);
	        addCropYield(cropYield);
	    }

	    public List<Symptoms> findSymptomsCodeByType(Integer type) {
	     
	        return certificationDAO.findSymptomsCodeByType(type);
	    }

	    public void addPeriodicInspection(PeriodicInspection periodicInspection) {
	       certificationDAO.save(periodicInspection);
	        
	    }

	    public byte[] findInspectionImageById(Long id) {
	        return certificationDAO.findInspectionImageById(id);
	    }
	    @Override
		public FarmerCropProdAnswers findFarmerCropProdAnswersByFarmerId(String farmerId,String category) {
				  return certificationDAO.findFarmerCropProdAnswersByFarmerId(farmerId,category);	
			}
		@Override
		public List listAnswersByQuestionAnswers(long id) 
		{
			return certificationDAO.listAnswersByQuestionAnswers(id);
			}

		@Override
		public List listAnswersBySubQuestionAnswers(long id) {
			return certificationDAO.listAnswersBySubQuestionAnswers(id);
		}

		

		@Override
		public List<FarmersSectionAnswers> listsectionByFarmerCropProd(long id) {
			// TODO Auto-generated method stub
			return certificationDAO.listsectionByFarmerCropProd(id);
		}

		@Override
		public Section findSectionByCode(
				String string) {
			return certificationDAO.findSectionByCode(string);
		}	
		
		@Override
		public Question findQuestionByCode(
				String string) {
			return certificationDAO.findQuestionByCode(string);
		}

		@Override
		public void addFarmerFeedback(FarmerFeedbackEntity fcpa) {
			certificationDAO.save(fcpa);
		}	
		

		@Override
		public boolean isQuestionMappedWithSurvey(long id) {
			
			return certificationDAO.isQuestionMappedWithSurvey(id);
		}

		@Override
		public Country findCountryByCode(String contryCode) {
			// TODO Auto-generated method stub
			return certificationDAO.findCountryByCode(contryCode);
		}

		@Override
		public SurveyQuestion findQuestionById(long id) {
			// TODO Auto-generated method stub
			return certificationDAO.findQuestionById(id);
		}

		@Override
		public List<Country> listCountryByCode(String contryCode) {
			// TODO Auto-generated method stub
			return certificationDAO.listCountryByCode(contryCode);
		}

		@Override
		public List<Country> listCountries() {
			// TODO Auto-generated method stub
			return certificationDAO.listCountries();
		}

		@Override
		public List<SurveySection> listAllSectionsForFOFarmerFarmByCountryId(long Cid) {
			// TODO Auto-generated method stub
			return certificationDAO.listAllSectionsForFOFarmerFarmByCountryId(Cid);
		}

		@Override
		public List<SurveyQuestion> listParentQuestions(long sectionId, String questionId) {
			// TODO Auto-generated method stub
			return certificationDAO.listParentQuestions(sectionId, questionId);
		}

		@Override
		public List<Language> listLanguages() {
			// TODO Auto-generated method stub
			return certificationDAO.listLanguages();
		}

		@Override
		public List<LanguagePreferences> listLangPrefByCode(String code) {
			// TODO Auto-generated method stub
			return certificationDAO.listLangPrefByCode(code);
		}

		@Override
		public boolean isParentQuestion(long id) {
			// TODO Auto-generated method stub
			return certificationDAO.isParentQuestion(id);
		}

		@Override
		public void removeQuestionSurveyMapping(SurveyQuestion question) {

			certificationDAO.removeQuestionSurveyMapping(question);
		}

		@Override
		public List<SurveyQuestion> listQuestionsNumericBySectionId(long sectionId) {
			// TODO Auto-generated method stub
			return certificationDAO.listQuestionsNumericBySectionId(sectionId);
		}

		@Override
		public List<SurveyPreferences> listSurveyPreferencesByType(int type) {
			// TODO Auto-generated method stub
			return certificationDAO.listSurveyPreferencesByType(type);
		}

		@Override
		public DataLevel findDataLevelBySectionId(long sectionId) {
			// TODO Auto-generated method stub
			return certificationDAO.findDataLevelBySectionId(sectionId);
		}

		@Override
		public List<Object[]> listQuestionsByQuestionTypeAndComponentTypeAndSection(long sectionId) {
			// TODO Auto-generated method stub
			return certificationDAO.listQuestionsByQuestionTypeAndComponentTypeAndSection(sectionId);
		}

		@Override
		public List<SurveyQuestion> listQuestionsByCodes(List<String> codes) {
			// TODO Auto-generated method stub
			return certificationDAO.listQuestionsByCodes(codes);
		}

		@Override
		public List<FarmCatalogue> listCataloguesByTypeAndCountry(String type, long cid) {
			// TODO Auto-generated method stub
			return certificationDAO.listCataloguesByTypeAndCountry(type,cid);
		}

		@Override
		public List<FarmCatalogue> listCataloguesByType(int type) {
			// TODO Auto-generated method stub
			return certificationDAO.listCataloguesByType(type);
		}

		@Override
		public List<FarmCatalogue> listCataloguesByCodes(List<String> codes) {
			// TODO Auto-generated method stub
			return certificationDAO.listCataloguesByCodes(codes);
		}

		@Override
		public FarmCatalogue findCatalogueByCode(String code) {
			// TODO Auto-generated method stub
			return certificationDAO.findCatalogueByCode(code);
		}

		@Override
		public void addQuestion(SurveyQuestion question) {

			// TODO Auto-generated method stub
			certificationDAO.save(question);
			question.setCode(SurveyQuestion.CODE_PREFIX + String.format("%04d", question.getId()));
			question.setSerialNo(question.getId() + "");	
			certificationDAO.update(question);
			for (LanguagePreferences preferences : question.getLanguagePreferences()) {
				preferences.setCode(question.getCode());
				if(preferences.getName()==null || StringUtil.isEmpty(preferences.getName())){
					preferences.setName(question.getName());
				}
				certificationDAO.save(preferences);
			}
		}

		@Override
		public void updateQuestion(String code, List<String> questionCodes) {
			certificationDAO.updateQuestion(code, questionCodes);
		}

		@Override
		public Country findCountryById(Long id) {
			// TODO Auto-generated method stub
			return certificationDAO.findCountryById(id);
		}

		@Override
		public boolean isQuestionAndSubFormMappedWithSurvey(long id) {
			// TODO Auto-generated method stub
			return certificationDAO.isQuestionAndSubFormMappedWithSurvey(id);
		}

		@Override
		public SurveySection findSectionById(long id) {
			// TODO Auto-generated method stub
			return certificationDAO.findSectionById(id);
		}


		@Override
		public void editQuestion(SurveyQuestion question) {

			certificationDAO.update(question);
			for (LanguagePreferences preferences : question.getLanguagePreferences()) {
				if (!ObjectUtil.isEmpty(preferences))
					certificationDAO.update(preferences);
			}
		}

		@Override
		public void removeQuestion(SurveyQuestion question) {
			// TODO Auto-generated method stub
			certificationDAO.delete(question);
		}

		@Override
		public List<SurveyQuestion> listParentQuestions(long sectionId) {
			// TODO Auto-generated method stub
			return certificationDAO.listParentQuestions(sectionId);
		}

		@Override
		public List<SurveySection> listSections() {
			// TODO Auto-generated method stub
			return certificationDAO.listSections();
		}
		
		public SurveyPreferences findSurveyPreference(String fromUnit, String toUnit, int type, Country country) {

			return certificationDAO.findSurveyPreference(fromUnit, toUnit, type, country);
		}

		@Override
		public void addSection(SurveySection surveySection) {
			
			surveySection.setRevisionNumber(DateUtil.getRevisionNumber());
			certificationDAO.save(surveySection);
		}

		@Override
		public SurveySection findSectionByName(String selectedSection) {
			// TODO Auto-generated method stub
			return certificationDAO.findSectionByName(selectedSection);
		}

		@Override
		public List<Language> findIsSurveyStatusLanguage() {
			// TODO Auto-generated method stub
			return certificationDAO.findIsSurveyStatusLanguage();
		}

		@Override
		public List<FarmerField> findFieldsByParentIdandStatsuId(String code) {
			// TODO Auto-generated method stub
			return certificationDAO.findFieldsByParentIdandStatsuId(code);
		}
		
		
		@Override
public long findSurveyTypeBySurveyCode(String surveyCode) {

		return certificationDAO.findSurveyTypeBySurveyCode(surveyCode);
	}
@Override
	public List<Object[]> listDataLevel() {

		return certificationDAO.listDataLevel();
	}
	@Override
	public Map<String, LanguagePreferences> listLanguagePreferencesMapByLanguageAndType(String lang, int type) {

		Map<String, LanguagePreferences> languagePreferencesMap = new HashMap<String, LanguagePreferences>();
		List<LanguagePreferences> languagePreferencesList = listLanguagePreferencesByLanguageAndType(lang, type);
		if (!ObjectUtil.isListEmpty(languagePreferencesList)) {
			for (LanguagePreferences languagePreferences : languagePreferencesList) {
				languagePreferencesMap.put(languagePreferences.getCode(), languagePreferences);
			}
		}
		return languagePreferencesMap;
	}
	
	public List<LanguagePreferences> listLanguagePreferencesByLanguageAndType(String lang, int type) {

		return certificationDAO.listLanguagePreferencesByLanguageAndType(lang, type);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	 * listSectionsByDataLevelCode(java .lang.String)
	 */
	@Override
	public List<Object[]> listSectionsByDataLevelCode(String code) {

		return certificationDAO.listSectionsByDataLevelCode(code);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	 * findSurveyMasterBySurveyCodeLanguage (java.lang.String, java.lang.String)
	 */
	
	public SurveyMaster findSurveyMasterBySurveyCode(String code) {

		return certificationDAO.findSurveyMasterBySurveyCode(code);
	}

	@Override
	public List<SurveyFarmersQuestionAnswers> processFormattingAnswersSubForm(
			SortedSet<SubFormQuestionMapping> subFormQuestionMap,
			Map<String, SurveyFarmersQuestionAnswers> farmerQuestionSubFormMap, Map<String, String> catalogueMap,
			Map<String, SubFormQuestionMapping> subFormQuestionMapping) throws ScriptException {

		List<SurveyFarmersQuestionAnswers> fmList = new ArrayList<SurveyFarmersQuestionAnswers>();
		Map<String, SurveyPreferences> surveyPreferencesMap = new HashMap<String, SurveyPreferences>();
		Map<String, SurveyPreferences> convartionMap = new HashMap<String, SurveyPreferences>();
		Map<String, SurveyFarmersQuestionAnswers> tempFQues = new LinkedHashMap<String, SurveyFarmersQuestionAnswers>();

		if (!ObjectUtil.isEmpty(subFormQuestionMap)) {

			// ScriptEngine To Evaluate Expression
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			for (SubFormQuestionMapping surveyQuestionMapping : subFormQuestionMap) {
				SurveyQuestion question = surveyQuestionMapping.getChildQuestion();
				if (!StringUtil.isEmpty(question.getFormulaEquation())) {
					SurveyFarmersQuestionAnswers fmSub = new SurveyFarmersQuestionAnswers();
					String replaceFormulaQn = "";
					String formulaEqnString = question.getFormulaEquation();
					Map<Integer, String> formulas = new LinkedHashMap<Integer, String>();
					List<String> subQuestionList = new ArrayList<String>();
					Pattern p = Pattern.compile("\\{([^}]*)\\}");
					Matcher m = p.matcher(formulaEqnString);
					while (m.find())
						subQuestionList.add(m.group(1));
					fmSub.setQuestionName(question.getName());

					fmSub.setQuestion(question);
					fmSub.setSerialNo(question.getSerialNo());
					fmSub.setQuestionCode(question.getCode());
					fmSub.setComponentType(question.getComponentType());
					fmSub.setComponentType(Integer.valueOf(question.getComponentType()));
					fmSub.setQuestionType(question.getQuestionType());

					fmSub.setQuestionOrder(subFormQuestionMapping.get(question.getCode()).getQuestionOrder());

					if (!ObjectUtil.isListEmpty(subQuestionList)) {
						String formulaEqn = question.getFormulaEquation();
						boolean calculateFormula = true;
						boolean skipFormula = false;
						List<String> notAnsweredQuestions = new ArrayList<String>();
						// Processing Constant Values
						if (formulaEqn.contains(SurveyQuestion.FORMULA_CONSTANT_VALUE)) {
							p = Pattern.compile("\\##([^##]*)\\##");
							m = p.matcher(formulaEqn);
							// Get Country Form Agent in Future
							// Ivory Coast Country is used for pilot
							Country country = new Country();
							country.setId(4);
							while (m.find()) {
								SurveyPreferences surveyPreferences = surveyPreferencesMap.get(m.group(1));
								if (ObjectUtil.isEmpty(surveyPreferences)) {
									surveyPreferences = findSurveyPreference(m.group(1), null,
											SurveyPreferences.typeMaster.Constants.ordinal(), country);
								}
								if (!ObjectUtil.isEmpty(surveyPreferences)) {
									surveyPreferencesMap.put(surveyPreferences.getCode(), surveyPreferences);
									formulaEqn = formulaEqn
											.replaceAll(
													SurveyQuestion.FORMULA_CONSTANT_VALUE + surveyPreferences.getCode()
															+ SurveyQuestion.FORMULA_CONSTANT_VALUE,
													surveyPreferences.getValue());
								} else {
									skipFormula = true;
									break;
								}
							}
						}
						replaceFormulaQn = formulaEqn;
						if (!skipFormula) {
							for (String subQuestionCode : subQuestionList) {
								tempFQues.put(subQuestionCode, farmerQuestionSubFormMap.get(subQuestionCode));
							}
							for (Entry<String, SurveyFarmersQuestionAnswers> subFormEntry : tempFQues.entrySet()) {
								SurveyFarmersQuestionAnswers farmersQuestionAnswers = subFormEntry.getValue();
								for (SubFormAnswerMapping subForm : farmersQuestionAnswers.getSubFormAnswers()) {
									String subQuestionCode = subForm.getSubFormChildQuestion().getQuestionCode();
									if (formulas.containsKey(subForm.getAnswerOrder())) {
										formulaEqn = formulas.get(subForm.getAnswerOrder());
									} else {
										formulaEqn = replaceFormulaQn;
										formulas.put(subForm.getAnswerOrder(), formulaEqn);
									}

									SubFormQuestionMapping subSurveyQuestionMapping = subFormQuestionMapping
											.get(subQuestionCode);
									SurveyQuestion subQuestion = null;
									if (!ObjectUtil.isEmpty(surveyQuestionMapping))
										subQuestion = subSurveyQuestionMapping.getChildQuestion();

									if (ObjectUtil.isEmpty(farmersQuestionAnswers)

											|| ObjectUtil.isEmpty(subQuestion)) {
										if (calculateFormula)
											calculateFormula = false;
										notAnsweredQuestions.add(subQuestionCode);
										continue;
									}

									SurveyAnswers answerObj = subForm.getAnswer();
									if (!StringUtil.isEmpty(answerObj.getAnswer4())
											&& !StringUtil.isEmpty(subQuestion.getUnitOtherCatalogValue())
											&& subQuestion.getUnitOtherCatalogValue()
													.equalsIgnoreCase(answerObj.getAnswer4())) {
										calculateFormula = false;
										skipFormula = true;
										break;
									}
									if (answerObj.getAnswer() != null && !StringUtil.isEmpty(answerObj.getAnswer())
											&& ((answerObj.isNonConfirm())
													|| answerObj.getAnswer().equals(SurveyQuestion.CAT_IDONTKNOW))) {
										calculateFormula = false;
										skipFormula = true;
										break;
									}
									if (formulaEqn.contains(SurveyQuestion.FORMULA_CURRENT_YEAR)) {
										String year = "";
										// For getYearMap methods no need to
										// format answer to get
										// year
										if ((answerObj.getAnswer() == null || StringUtil.isEmpty(answerObj.getAnswer())
												&& answerObj.isNonConfirm())
												|| answerObj.getAnswer().equals(SurveyQuestion.CAT_IDONTKNOW)) {

										} else if (SurveyQuestion.YEAR_LIST_METHOD_NAME
												.equals(subQuestion.getListMethodName())) {
											year = answerObj.getAnswer();
										}
										// Other Date Based questions need to be
										// formatted to get
										// year
										else {
											String dataFormat = !StringUtil.isEmpty(subQuestion.getDataFormat())
													? subQuestion.getDataFormat() : DateUtil.DATE_FORMAT;
											if (!StringUtil.isEmpty(dataFormat)
													&& DateUtil.isValidDateFormat(dataFormat, answerObj.getAnswer())) {
												Date date = DateUtil.convertStringToDate(answerObj.getAnswer(),
														dataFormat);
												SimpleDateFormat df = new SimpleDateFormat("yyyy");
												year = df.format(date);
											}
										}
										if (StringUtil.isEmpty(year) && StringUtil.isDouble(answerObj.getAnswer())) {
											year = answerObj.getAnswer();
										}
										if (StringUtil.isEmpty(year)) {
											year = "0";
										}
										formulaEqn = formulaEqn.replaceAll("\\{" + subQuestionCode + "\\}", year);

										formulaEqn = formulaEqn.replace(SurveyQuestion.FORMULA_CURRENT_YEAR,
												String.valueOf(DateUtil.getCurrentYear()));
									} else {
										if ((answerObj.getAnswer() == null || StringUtil.isEmpty(answerObj.getAnswer())
												&& answerObj.isNonConfirm())
												|| answerObj.getAnswer().equals(SurveyQuestion.CAT_IDONTKNOW)) {

										}
										// Processing Arithmetic Expressions
										else if (formulaEqn.contains("//") || formulaEqn.contains("*")) {
											try {
												if (Double.valueOf(answerObj.getAnswer()) == 0) {
													// Handling Division By Zero
													// Exception
													if (calculateFormula)
														calculateFormula = false;
													notAnsweredQuestions.add(subQuestionCode);
												} else {
													
														formulaEqn = formulaEqn.replaceAll(
																"\\{" + subQuestionCode + "\\}", answerObj.getAnswer());
													
												}
											} catch (Exception e) {
												if (calculateFormula)
													calculateFormula = false;
												notAnsweredQuestions.add(subQuestionCode);
											}
										} else {
											
												formulaEqn = formulaEqn.replaceAll("\\{" + subQuestionCode + "\\}",
														answerObj.getAnswer());
											
										}
									}

									formulas.put(subForm.getAnswerOrder(), formulaEqn);

								}
							}
							SortedSet<SubFormAnswerMapping> subFormAnswers = new TreeSet<SubFormAnswerMapping>();
							for (Entry<Integer, String> formulasEntry : formulas.entrySet()) {
								formulaEqn = formulasEntry.getValue();
								if (!skipFormula && !ObjectUtil.isListEmpty(notAnsweredQuestions)
										&& notAnsweredQuestions.size() < subQuestionList.size()) {
									if (!(formulaEqn.contains("/") || formulaEqn.contains("*"))) {
										for (String subQuestionCode : notAnsweredQuestions) {
											formulaEqn = formulaEqn.replaceAll("\\{" + subQuestionCode + "\\}", "0");
										}
										calculateFormula = true;
									}
								}

								if (!skipFormula && calculateFormula) {
									SubFormAnswerMapping smAns = new SubFormAnswerMapping();
									SurveyFarmersQuestionAnswers formulaFarmersQuestionAnswers = new SurveyFarmersQuestionAnswers();
									formulaFarmersQuestionAnswers.setQuestionCode(question.getCode());
									formulaFarmersQuestionAnswers.setQuestionName(question.getName());
									formulaFarmersQuestionAnswers.setSerialNo(question.getSerialNo());
									formulaFarmersQuestionAnswers
											.setQuestionOrder(surveyQuestionMapping.getQuestionOrder());
									formulaFarmersQuestionAnswers
											.setRegisteredQuestion(ICertificationService.REGISTERED_QUESTION);
									formulaFarmersQuestionAnswers.setQuestionType(question.getQuestionType());
									formulaFarmersQuestionAnswers.setComponentType(question.getComponentType());

									SurveyAnswers answers = new SurveyAnswers();
									answers.setFarmersQuestionAnswers(formulaFarmersQuestionAnswers);
									// System.out.println(engine.eval(formulaEqn));
									if (answers.getAnswer() != null
											|| !StringUtil.isEmpty(answers.getAnswer()) && (answers.isNonConfirm()
													|| answers.getAnswer().equals(SurveyQuestion.CAT_IDONTKNOW))) {

									}else {
										if (ObjectUtil.isValidFormula(formulaEqn)) {
											answers.setAnswer(String.valueOf(engine.eval(formulaEqn)));
										}
									}

									if (!ObjectUtil.isListEmpty(question.getUnits())
											&& !StringUtil.isEmpty(question.getDefaultUnit())) {
										answers.setAnswer4(question.getDefaultUnit());
										answers = processUnits(answers, question, catalogueMap, convartionMap,
												SurveyFarmerCropProdAnswers.modeType.WEB.ordinal());
									}
									processFormattingAnswers(formulaFarmersQuestionAnswers, question, answers);

									smAns.setSubFormChildQuestion(formulaFarmersQuestionAnswers);
									smAns.setSubFormParentQuestion(fmSub);
									smAns.setAnswerOrder(formulasEntry.getKey());
									smAns.setAnswer(answers);
									subFormAnswers.add(smAns);
								}

							}
							fmSub.setSubFormAnswers(subFormAnswers);
							fmList.add(fmSub);
						}

					}

				}
			}

		}

		return fmList;

	}

	@Override
	public void processFormattingAnswers(SurveyFarmersQuestionAnswers farmersQuestionAnswers, SurveyQuestion question,
			SurveyAnswers answers) {

		// Double Format
		if (farmersQuestionAnswers.getComponentType() == SurveyQuestion.componentTypeMaster.TEXT_BOX.ordinal()
				&& (SurveyQuestion.validationTypeMaster.DOUBLE.ordinal() == question.getValidationType()
						|| SurveyQuestion.validationTypeMaster.PERCENTAGE.ordinal() == question.getValidationType()
						|| SurveyQuestion.validationTypeMaster.HOURS.ordinal() == question.getValidationType())) {
			NumberFormat formatter = new DecimalFormat(question.getDataFormat());
			if (!StringUtil.isEmpty(answers.getAnswer1())) {
				answers.setAnswer(formatter.format(Double.valueOf(answers.getAnswer() + "." + answers.getAnswer1())));
				answers.setAnswer1(null);
			} else {
				if (question.getValidationType() == SurveyQuestion.validationTypeMaster.DOUBLE.ordinal()) {
					answers.setAnswer(answers.getAnswer());
				} else {
					answers.setAnswer(formatter.format(Double.valueOf(answers.getAnswer())));
				}
			}

		}
		// Processing Date
		else if (farmersQuestionAnswers.getComponentType() == SurveyQuestion.componentTypeMaster.DATE_PICKER.ordinal()
				&& SurveyQuestion.validationTypeMaster.DATE.ordinal() == question.getValidationType()) {
			try {
				if ((answers.getAnswer() != null || !StringUtil.isEmpty(answers.getAnswer()))
						&& !answers.getAnswer().equals(SurveyQuestion.CAT_IDONTKNOW) && !answers.isNonConfirm()) {
					Date date = DateUtil.convertStringToDate(answers.getAnswer().trim(), DateUtil.DATE_FORMAT);
					String dateString = DateUtil.convertDateToString(date, question.getDataFormat());
					answers.setAnswer(dateString);
				}
			} catch (Exception e) {
				e.printStackTrace();
				answers.setAnswer("");
			}
		}

	}
	@Override
	public SurveyAnswers processUnits(SurveyAnswers answers, SurveyQuestion question, Map<String, String> catalogueMap,
			Map<String, SurveyPreferences> convertionMap, int mode) {

		if (!ObjectUtil.isListEmpty(question.getUnits())) {
			if (!StringUtil.isEmpty(question.getDefaultUnit())
					&& !question.getDefaultUnit().equalsIgnoreCase(answers.getAnswer4())) {

				if (!StringUtil.isEmpty(question.getUnitOtherCatalogValue())
						&& question.getUnitOtherCatalogValue().equalsIgnoreCase(answers.getAnswer4())) {
					answers.setAnswer5(catalogueMap.get(answers.getAnswer4().trim()));
				} else {

					// Get Country Form Agent in Future
					// Ivory Coast Country is used for pilot
					Country country = new Country();
					country.setId(4);
					String countryString = !ObjectUtil.isEmpty(country) ? String.valueOf(country.getId()) : "";
					SurveyPreferences surveyPreference = convertionMap
							.get(answers.getAnswer4() + "~" + question.getDefaultUnit() + "~" + countryString);

					if (ObjectUtil.isEmpty(surveyPreference))
						surveyPreference = findSurveyPreference(answers.getAnswer4(), question.getDefaultUnit(),
								SurveyPreferences.typeMaster.Conversion.ordinal(), country);
					if (!ObjectUtil.isEmpty(surveyPreference)) {
						convertionMap.put(answers.getAnswer4() + "~" + question.getDefaultUnit() + "~" + countryString,
								surveyPreference);
						Double value = 0d;
						try {
							if (mode == SurveyFarmerCropProdAnswers.modeType.WEB.ordinal())
								value = Double.valueOf(answers.getAnswer() + "." + answers.getAnswer1());
							else
								value = Double.valueOf(answers.getAnswer());

						} catch (Exception e) {
							value = 0d;
						}
						Double calculatedValue = value * Double.valueOf(surveyPreference.getValue());
						if (mode == SurveyFarmerCropProdAnswers.modeType.WEB.ordinal()) {
							String[] valueArray = String.valueOf(calculatedValue).split("\\.");
							answers.setAnswer(valueArray[0]);
							answers.setAnswer1(valueArray[1]);
						} else {
							answers.setAnswer(String.valueOf(calculatedValue));
						}
						answers.setAnswer4(surveyPreference.getConvertionCode());
						answers.setAnswer5(surveyPreference.getConvertionName());
					} else {
						if (!StringUtil.isEmpty(answers.getAnswer4())) {
							answers.setAnswer5(catalogueMap.get(answers.getAnswer4().trim()));
						}
					}
				}
			} else {
				if (!StringUtil.isEmpty(answers.getAnswer4())) {
					answers.setAnswer5(catalogueMap.get(answers.getAnswer4().trim()));
				}
			}

		}
		return answers;
	}
	
	@Override
	public void processOnceQuestion(SurveyQuestion question, Map<String, String> catalogueMap,
			SurveyFarmerCropProdAnswers farmerCropProdAnswers, SurveyAnswers answers,
			Map<String, Object> onceQuestionEntityMap) {

		String[] details = question.getEntityProperty().split("##");
		try {
			if (details != null && details.length > 0) {
				for (String detail : details) {
					String[] entityDetails = detail.split("\\$\\$");
					if (entityDetails != null && entityDetails.length > 0) {
						onceQuestionEntityMap = processOneOnceQuestion(entityDetails, question, catalogueMap,
								 farmerCropProdAnswers, answers, onceQuestionEntityMap);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("Excetion While Processing once Quesion :" + e.getMessage());
			e.printStackTrace();
		}

	}
	
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> processOneOnceQuestion(String[] entityDetails, SurveyQuestion question,
			Map<String, String> catalogueMap, 
			SurveyFarmerCropProdAnswers farmerCropProdAnswers, SurveyAnswers answers, Map<String, Object> onceQuestionEntityMap)
			throws Exception {

		// entityDetails[0] -> FarmCropProdAnswer Property
		// entityDetails[1] -> Entity Name (Assuming all Entity in the
		// com.ese.entity.profile
		// package)
		// entityDetails[2] -> Entity Property To Fetch Entity from Db (Used for
		// Condition )
		// entityDetails[3] -> Entity Setter Property (Update this property
		// value from Answer)
		// entityDetails[4] -> Answer process Type (refer @ Question entity
		// enum)
		// entityDetails[5] -> Result Entity class (Answer have to be casted to
		// this type)
		// entityDetails[6] -> In Case Answer Process type is Entity we can find
		// that entity from db
		// by this value

		Method method = getterMethod(entityDetails[0], SurveyFarmerCropProdAnswers.class);
		if (method != null) {

			// Fetching main Entity
			String farmCropProdAnswerProperty = (String) method.invoke(farmerCropProdAnswers);
			if (!StringUtil.isEmpty(farmCropProdAnswerProperty)) {
				Object object = onceQuestionEntityMap.get(entityDetails[1] + farmCropProdAnswerProperty);
				if (ObjectUtil.isEmpty(object))
					object = certificationDAO.findEntityByEntityNameAndProperty(entityDetails[1], entityDetails[2],
							farmCropProdAnswerProperty);

				if (!ObjectUtil.isEmpty(object)) {
					int answerProcessType = Integer.valueOf(entityDetails[4]);
					Class entityClass = Class.forName("com.sourcetrace.esesw.entity.profile." + entityDetails[1]);
					Method setterMethod = null;
					if (!entityDetails[3].contains(","))
						setterMethod = setterMethod(entityClass, entityDetails[3]);

					// Processing Answers

					if ((answers.getAnswer() == null
							|| StringUtil.isEmpty(answers.getAnswer()) && answers.isNonConfirm())
							|| answers.getAnswer().equals(SurveyQuestion.CAT_IDONTKNOW)) {

					} else if (SurveyQuestion.onceQuestionAnswerProcessTypeMaster.ANSWER.ordinal() == answerProcessType
							&& !ObjectUtil.isEmpty(setterMethod)) {
						if ("Integer".equalsIgnoreCase(entityDetails[5])) {
							setterMethod.invoke(object, Integer.valueOf(answers.getAnswer()));
						} else if ("Long".equalsIgnoreCase(entityDetails[5])) {
							setterMethod.invoke(object, Long.valueOf(answers.getAnswer()));
						} else if ("Double".equalsIgnoreCase(entityDetails[5])) {
							setterMethod.invoke(object, Double.valueOf(answers.getAnswer()));
						} else {
							setterMethod.invoke(object, answers.getAnswer());
						}

					} else if (SurveyQuestion.onceQuestionAnswerProcessTypeMaster.CATALOGUE_CODE
							.ordinal() == answerProcessType && !ObjectUtil.isEmpty(setterMethod)) {
						setterMethod.invoke(object, answers.getAnswer());
					} else if (SurveyQuestion.onceQuestionAnswerProcessTypeMaster.CATALOGUE_NAME
							.ordinal() == answerProcessType && !ObjectUtil.isEmpty(setterMethod)) {
						setterMethod.invoke(object, catalogueMap.get(answers.getAnswer()));
					}else if (SurveyQuestion.onceQuestionAnswerProcessTypeMaster.DATE.ordinal() == answerProcessType
							&& !ObjectUtil.isEmpty(setterMethod)) {
						Date date = DateUtil.convertStringToDate(answers.getAnswer(),
								!StringUtil.isEmpty(question.getDataFormat()) ? question.getDataFormat()
										: DateUtil.DATE_FORMAT);
						setterMethod.invoke(object, date);
					} else if (SurveyQuestion.onceQuestionAnswerProcessTypeMaster.GPS.ordinal() == answerProcessType) {
						// Processing latitude ,longitude
						// Answer have comma separated latitude and longitude
						String[] entitySetterProperties = entityDetails[3].split(",");
						String[] coordinates = answers.getAnswer().split(",");
						if (coordinates != null && entitySetterProperties != null && coordinates.length == 2
								&& entitySetterProperties.length == 2) {
							setterMethod = setterMethod(entityClass, entitySetterProperties[0]);
							setterMethod.invoke(object, coordinates[0]);
							setterMethod = setterMethod(entityClass, entitySetterProperties[1]);
							setterMethod.invoke(object, coordinates[1]);
						}
					} else if (SurveyQuestion.onceQuestionAnswerProcessTypeMaster.OTHERS.ordinal() == answerProcessType
							&& !ObjectUtil.isEmpty(setterMethod)) {
						setterMethod.invoke(object, answers.getAnswer2());
					} else if (SurveyQuestion.onceQuestionAnswerProcessTypeMaster.ENTITY.ordinal() == answerProcessType
							&& !ObjectUtil.isEmpty(setterMethod)) {
						// Fetching result Entity
						Object answerObject = certificationDAO.findEntityByEntityNameAndProperty(entityDetails[5],
								entityDetails[6], answers.getAnswer());
						if (!ObjectUtil.isEmpty(answerObject)) {
							setterMethod.invoke(object, answerObject);
						}
					}
					onceQuestionEntityMap.put(entityDetails[1] + farmCropProdAnswerProperty, object);
				}
			}
		}
		return onceQuestionEntityMap;
	}
	
	
	@SuppressWarnings("unchecked")
	private Method setterMethod(Class entityClass, String propertyName) throws Exception {

		BeanInfo info = Introspector.getBeanInfo(entityClass);
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (PropertyDescriptor pd : props) {
			if (pd.getName().equals(propertyName))
				return pd.getWriteMethod();
		}
		return null;
	}

	private Method getterMethod(String property, Class<SurveyFarmerCropProdAnswers> class1) throws Exception {

		BeanInfo info = Introspector.getBeanInfo(class1);
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (PropertyDescriptor pd : props) {
			if (pd.getName().equals(property))
				return pd.getReadMethod();
		}
		return null;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.agrocert.service.ICertificationService#
	 * addFarmerCropProdAnswers(com.
	 * sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers)
	 */
	@Override
	public void addSurveyFarmerCropProdAnswers(SurveyFarmerCropProdAnswers farmerCropProdAnswers) {

		certificationDAO.save(farmerCropProdAnswers);

	}

	
	@Override
	public void removeSurveyFarmerCropProdAnswers(SurveyFarmerCropProdAnswers farmerCropProdAnswers) {

		certificationDAO.delete(farmerCropProdAnswers);
	}
	
		
	@Override
	public List<Object[]> listCatalogueBySurveyCode(String surveyCode) {

		List<Object[]> answerlist = certificationDAO.listAnswerCataLogueBySurveyCode(surveyCode);
		if (ObjectUtil.isListEmpty(answerlist))
			answerlist = new ArrayList<Object[]>();
		List<Object[]> unitlist = certificationDAO.listUnitCataLogueBySurveyCode(surveyCode);
		if (!ObjectUtil.isListEmpty(unitlist))
			answerlist.addAll(unitlist);
		return answerlist;
	}

	@Override
	public List<Object[]> listSurveyMasters() {
		// TODO Auto-generated method stub
		return certificationDAO.listSurveyMasters();
	}
	
	public SurveyFarmerCropProdAnswers processFormula(SurveyFarmerCropProdAnswers farmerCropProdAnswers,
			Map<String, SurveyFarmersQuestionAnswers> farmerQuestionMap, Map<String, SurveyQuestionMapping> surveyQuestionMap,
			Map<String, String> catalogueMap) throws ScriptException {

		Map<String, SurveyPreferences> surveyPreferencesMap = new HashMap<String, SurveyPreferences>();
		Map<String, SurveyPreferences> convartionMap = new HashMap<String, SurveyPreferences>();
		if (!StringUtil.isEmpty(farmerCropProdAnswers.getSurveyCode()) && farmerQuestionMap != null) {
			SurveyMaster surveyMasterObj = findSurveyMasterBySurveyCode(farmerCropProdAnswers.getSurveyCode());
			if (!ObjectUtil.isEmpty(surveyMasterObj)
					&& !ObjectUtil.isListEmpty(surveyMasterObj.getSurveyQuestionMapping())) {

				// ScriptEngine To Evaluate Expression
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine engine = mgr.getEngineByName("JavaScript");

				for (SurveyQuestionMapping surveyQuestionMapping : surveyMasterObj.getSurveyQuestionMapping()) {
					SurveyQuestion question = surveyQuestionMapping.getQuestion();
					if (!StringUtil.isEmpty(question.getFormulaEquation())) {

						// Finding Dependent Questions for formula
						String formulaEqnString = question.getFormulaEquation();
						List<String> subQuestionList = new ArrayList<String>();
						Pattern p = Pattern.compile("\\{([^}]*)\\}");
						Matcher m = p.matcher(formulaEqnString);
						while (m.find())
							subQuestionList.add(m.group(1));

		
							String formulaEqn = question.getFormulaEquation();
							boolean calculateFormula = true;
							boolean skipFormula = false;
							List<String> notAnsweredQuestions = new ArrayList<String>();
							// Processing Constant Values
							if (formulaEqn.contains(SurveyQuestion.FORMULA_CONSTANT_VALUE)) {
								p = Pattern.compile("\\##([^##]*)\\##");
								m = p.matcher(formulaEqn);
								// Get Country Form Agent in Future
								// Ivory Coast Country is used for pilot
								Country country = new Country();
								country.setId(4);
								while (m.find()) {
									SurveyPreferences surveyPreferences = surveyPreferencesMap.get(m.group(1));
									if (ObjectUtil.isEmpty(surveyPreferences)) {
										surveyPreferences = findSurveyPreference(m.group(1), null,
												SurveyPreferences.typeMaster.Constants.ordinal(), country);
									}
									if (!ObjectUtil.isEmpty(surveyPreferences)) {
										surveyPreferencesMap.put(surveyPreferences.getCode(), surveyPreferences);
										formulaEqn = formulaEqn.replaceAll(
												SurveyQuestion.FORMULA_CONSTANT_VALUE + surveyPreferences.getCode()
														+ SurveyQuestion.FORMULA_CONSTANT_VALUE,
												surveyPreferences.getValue());
									} else {
										skipFormula = true;
										break;
									}
								}
							}
							if (!skipFormula) {
								// Forming Equation to evaluate
								
                              if (formulaEqn.contains(SurveyQuestion.FORMULA_CURRENT_YEAR)) {
                            	  formulaEqn = formulaEqn.replace(SurveyQuestion.FORMULA_CURRENT_YEAR,
											String.valueOf(DateUtil.getCurrentYear()));
                            	  calculateFormula=true;
								}
								for (String subQuestionCode : subQuestionList) {
									SurveyFarmersQuestionAnswers farmersQuestionAnswers = farmerQuestionMap
											.get(subQuestionCode);
									SurveyQuestionMapping subSurveyQuestionMapping = surveyQuestionMap
											.get(subQuestionCode);
									SurveyQuestion subQuestion = null;
									if (!ObjectUtil.isEmpty(subSurveyQuestionMapping))
										subQuestion = subSurveyQuestionMapping.getQuestion();

									// No need to calculate formula for not
									// answered questions
									if (ObjectUtil.isEmpty(farmersQuestionAnswers)
											|| ObjectUtil.isListEmpty(farmersQuestionAnswers.getAnswers())
											|| ObjectUtil.isEmpty(subQuestion)) {
										if (calculateFormula)
											calculateFormula = false;
										notAnsweredQuestions.add(subQuestionCode);
										continue;
									}

									SurveyAnswers answerObj = farmersQuestionAnswers.getAnswers().iterator().next();

									// Skip Calculate formula for Other Units
									if (!StringUtil.isEmpty(answerObj.getAnswer4())
											&& !StringUtil.isEmpty(subQuestion.getUnitOtherCatalogValue())
											&& subQuestion.getUnitOtherCatalogValue()
													.equalsIgnoreCase(answerObj.getAnswer4())) {
										calculateFormula = false;
										skipFormula = true;
										break;
									}
									if (answerObj.getAnswer() != null && !StringUtil.isEmpty(answerObj.getAnswer())
											&& ((answerObj.isNonConfirm())
													|| answerObj.getAnswer().equals(SurveyQuestion.CAT_IDONTKNOW))) {
										calculateFormula = false;
										skipFormula = true;
										break;
									}
								
										if ((answerObj.getAnswer() == null || StringUtil.isEmpty(answerObj.getAnswer())
												&& answerObj.isNonConfirm())
												|| answerObj.getAnswer().equals(SurveyQuestion.CAT_IDONTKNOW)) {

										}
										// Processing Arithmetic Expressions
										else if (formulaEqn.contains("//") || formulaEqn.contains("*")) {
											try {
												if (Double.valueOf(answerObj.getAnswer()) == 0) {
													// Handling Division By Zero
													// Exception
													if (calculateFormula)
														calculateFormula = false;
													notAnsweredQuestions.add(subQuestionCode);
												} else {
														formulaEqn = formulaEqn.replaceAll(
																"\\{" + subQuestionCode + "\\}", answerObj.getAnswer());
													
												}
											} catch (Exception e) {
												if (calculateFormula)
													calculateFormula = false;
												notAnsweredQuestions.add(subQuestionCode);
											}
										} else {
												formulaEqn = formulaEqn.replaceAll("\\{" + subQuestionCode + "\\}",
														answerObj.getAnswer());
											
										}
									
								}
								
								
							}
							// If Division or Multiplication all the dependent
							// questions need to be
							// answered to calculate formula
							// else if any dependent question answered we can
							// calculate formula by
							// replacing 0
							if (!skipFormula && !ObjectUtil.isListEmpty(notAnsweredQuestions)
									&& notAnsweredQuestions.size() < subQuestionList.size()) {
								if (!(formulaEqn.contains("/") || formulaEqn.contains("*"))) {
									for (String subQuestionCode : notAnsweredQuestions) {
										formulaEqn = formulaEqn.replaceAll("\\{" + subQuestionCode + "\\}", "0");
									}
									calculateFormula = true;
								}
							}

							// Forming FarmersQuestionAnswers Object
							if (!skipFormula && calculateFormula) {
								SurveyFarmersQuestionAnswers formulaFarmersQuestionAnswers = new SurveyFarmersQuestionAnswers();
								formulaFarmersQuestionAnswers.setQuestionCode(question.getCode());
								formulaFarmersQuestionAnswers.setQuestionName(question.getName());
								formulaFarmersQuestionAnswers.setSerialNo(question.getSerialNo());
								formulaFarmersQuestionAnswers
										.setQuestionOrder(surveyQuestionMapping.getQuestionOrder());
								formulaFarmersQuestionAnswers
										.setRegisteredQuestion(ICertificationService.REGISTERED_QUESTION);
								formulaFarmersQuestionAnswers.setQuestionType(question.getQuestionType());
								formulaFarmersQuestionAnswers.setComponentType(question.getComponentType());

								SurveyAnswers answers = new SurveyAnswers();
								answers.setFarmersQuestionAnswers(formulaFarmersQuestionAnswers);
								// System.out.println(engine.eval(formulaEqn));
								if (answers.getAnswer() != null
										|| !StringUtil.isEmpty(answers.getAnswer()) && (answers.isNonConfirm()
												|| answers.getAnswer().equals(SurveyQuestion.CAT_IDONTKNOW))) {

								}
								// For Radio Button Component Type result of
								// formula equation will
								// be boolean
								
									if (ObjectUtil.isValidFormula(formulaEqn)) {
										answers.setAnswer(String.valueOf(engine.eval(formulaEqn)));
									
								}

								if (!ObjectUtil.isListEmpty(question.getUnits())
										&& !StringUtil.isEmpty(question.getDefaultUnit())) {
									answers.setAnswer4(question.getDefaultUnit());
									answers = processUnits(answers, question, catalogueMap, convartionMap,
											SurveyFarmerCropProdAnswers.modeType.WEB.ordinal());
								}
								processFormattingAnswers(formulaFarmersQuestionAnswers, question, answers);
								formulaFarmersQuestionAnswers.setAnswers(new TreeSet<SurveyAnswers>());
								formulaFarmersQuestionAnswers.getAnswers().add(answers);

								// Finding farmersSectionAnswers
								boolean sectionExist = false;
								for (SurveyFarmersSectionAnswers farmersSectionAnswers : farmerCropProdAnswers
										.getFarmersSectionAnswers()) {
									if (question.getSection().getCode().equals(farmersSectionAnswers.getSectionCode())
											&& !ObjectUtil
													.isListEmpty(farmersSectionAnswers.getFarmersQuestionAnswers())) {
										sectionExist = true;
										formulaFarmersQuestionAnswers.setFarmersSectionAnswers(farmersSectionAnswers);
										farmersSectionAnswers.getFarmersQuestionAnswers()
												.add(formulaFarmersQuestionAnswers);
									}
								}
								if (!sectionExist) {
									SurveyFarmersSectionAnswers farmersSectionAnswers = new SurveyFarmersSectionAnswers();
									farmersSectionAnswers.setSectionCode(question.getSection().getCode());
									farmersSectionAnswers.setSectionName(question.getSection().getName());
									//farmersSectionAnswers.setSectionType(question.getSection().getSectionType());
									farmersSectionAnswers.setSerialNo(question.getSection().getSerialNo());
									farmersSectionAnswers.setFarmerCropProdAnswers(farmerCropProdAnswers);
									farmersSectionAnswers
											.setFarmersQuestionAnswers(new HashSet<SurveyFarmersQuestionAnswers>());
									farmersSectionAnswers.getFarmersQuestionAnswers()
											.add(formulaFarmersQuestionAnswers);
									if (ObjectUtil.isListEmpty(farmerCropProdAnswers.getFarmersSectionAnswers())) {
										farmerCropProdAnswers
												.setFarmersSectionAnswers(new TreeSet<SurveyFarmersSectionAnswers>());
									}
									farmerCropProdAnswers.getFarmersSectionAnswers().add(farmersSectionAnswers);
								}
								farmerQuestionMap.put(formulaFarmersQuestionAnswers.getQuestionCode(),
										formulaFarmersQuestionAnswers);
							}
						
					}
				}
			}
		}
		return farmerCropProdAnswers;

	}
	
	public void addSurveyFarmerCropProdAnswers(SurveyFarmerCropProdAnswers farmerCropProdAnswers,
			Map<String, Object> onceQuestionEntityMap) {

		//Object[] surveyScope = farmerDAO.findSurveyScopeInfoByProfileId(farmerCropProdAnswers.getDataCollectorId());
		//farmerCropProdAnswers.setScopeId(Long.valueOf(surveyScope[0].toString()));
		certificationDAO.saveFarmerCropProdAnswers(farmerCropProdAnswers);
		try {
			// Processing Once Question
			if (onceQuestionEntityMap != null && onceQuestionEntityMap.size() > 0
					&& farmerCropProdAnswers.getSaveStatus() == SurveyFarmerCropProdAnswers.SAVE_STATUS_FULL) {
				for (Map.Entry<String, Object> entry : onceQuestionEntityMap.entrySet()) {
					Object object = entry.getValue();
					if (object instanceof Farmer) {
						Farmer farmer = (Farmer) object;
						if (!ObjectUtil.isEmpty(farmer.getVillage())) {
							farmer.setCity(farmer.getVillage().getCity());
						}
						farmer.setRevisionNo(DateUtil.getRevisionNumber());
						certificationDAO.saveOrUpdate(farmer);
					}
					 else {
						certificationDAO.saveOrUpdate(object);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("Excetion While Saving once Quesion :" + e.getMessage());
			e.printStackTrace();
		}

	}
	


	@Override
	public void editFarmerCropProdAnsStatus(long id, int status, String username, String date) {
		// TODO Auto-generated method stub
		certificationDAO.editFarmerCropProdAnsStatus(id, status, username, date);
	}
	public List<Object[]> listPartiallySavedFarmerCropPAInfo() {

		// TODO Auto-generated method stub
		return certificationDAO.listPartiallySavedFarmerCropPAInfo();
	}



	
	@Override
	public DataLevel findDataLevelBySectionCode(String sectionId) {

		// TODO Auto-generated method stub
		return certificationDAO.findDataLevelBySectionCode(sectionId);
	}


	@Override
	public List<Long> listSurveyMasterIdsByAgentId(String agentId) {

		return certificationDAO.listSurveyMasterIdsByAgentId(agentId);
	}

	@Override
	public void editSurveyMaster(SurveyMaster surveyMaster) {

		// TODO Auto-generated method stub
		certificationDAO.editSurveyMaster(surveyMaster);
	}

	@Override
	public SurveyMaster findSurveyMasterById(Long id) {

		return certificationDAO.findSurveyMasterById(id);
	}

	@Override
	public List<SurveySection> listSectionsByCodes(List<String> codes) {

		// TODO Auto-generated method stub
		return certificationDAO.listSectionsByCodes(codes);
	}



	public List<SurveyType> listSurveyTypes() {

		// TODO Auto-generated method stub
		return certificationDAO.listSurveyTypes();
	}

	public void updateSurveyMasterVerifyQuestion(long surveyId, List<Long> questionIds) {

		// TODO Auto-generated method stub
		certificationDAO.updateSurveyMasterVerifyQuestion(surveyId, questionIds);
	}

	public String findDataLevelCodeByInfraTypeCode(String infraTypeCode) {

		return certificationDAO.findDataLevelCodeByInfraTypeCode(infraTypeCode);
	}

	public List<Section> listAllSectionsForFOFarmerFarm() {

		return certificationDAO.listAllSectionsForFOFarmerFarm();
	}

	public List<SurveySection> listSectionCodesByDataLevelId(long dataLvlId) {

		return certificationDAO.listSectionCodesByDataLevelId(dataLvlId);
	}

	public void addSurveyMaster(SurveyMaster surveyMaster) {

		// TODO Auto-generated method stub
		certificationDAO.save(surveyMaster);
		surveyMaster.setCode(SurveyMaster.CODE_PREFIX + String.format("%03d", surveyMaster.getId()));
		for (LanguagePreferences preferences : surveyMaster.getLanguagePreferencesList()) {
			preferences.setCode(surveyMaster.getCode());
			certificationDAO.save(preferences);
		}
	}

	public boolean isSurveyMasterMappedWithSurveyScope(long id) {

		return certificationDAO.isSurveyMasterMappedWithSurveyScope(id);
	}

	public SurveyType findSurveyTypeById(long id) {

		// TODO Auto-generated method stub
		return certificationDAO.findSurveyTypeById(id);
	}

	public List<String> listQuestionsByIds(List<String> ids) {

		// TODO Auto-generated method stub
		return certificationDAO.listQuestionsByIds(ids);
	}

	public void removeSurveyMasterQuestionMapping(SurveyMaster surveyMaster) {

		certificationDAO.removeSurveyMasterQuestionMapping(surveyMaster);
	}

	public void removeSurveyMasterTarget(SurveyMaster surveyMaster) {

		certificationDAO.removeSurveyMasterTarget(surveyMaster);

	}

	public void removeSurveyMaster(SurveyMaster surveyMaster) {

		// TODO Auto-generated method stub
		certificationDAO.delete(surveyMaster);
	}

	public int isSurveyMasterVerified(String code) {

		return certificationDAO.isSurveyMasterVerified(code);
	}

	public List<Object[]> listAllSectionsObjectArrayForFOFarmerFarm() {

		return certificationDAO.listAllSectionsObjectArrayForFOFarmerFarm();
	}

	public List<SurveyQuestion> listQuestionsBySectionCodes(List<String> sectionCodes) {

		// TODO Auto-generated method stub
		return certificationDAO.listQuestionsBySectionCodes(sectionCodes);
	}

	@Override
	public List<SurveyQuestion> listQuestionsBySectionCodesAndQuestionType(List<String> sectionCodes, int questionTyp) {
		// TODO Auto-generated method stub
		return certificationDAO.listQuestionsBySectionCodesAndQuestionType(sectionCodes, questionTyp);
	}

	public List<String> listFilteredQuestionsByIds(List<String> ids) {

		// TODO Auto-generated method stub
		return certificationDAO.listFilteredQuestionsByIds(ids);
	}

	/*
	 * private void processMultilingualForSurveyAnswers(SurveyAnswers
	 * surveyAnswers, Map<String, Map<String, LanguagePreferences>>
	 * languagePreferencesMap) {
	 * 
	 * Map<String, LanguagePreferences> questionMap = languagePreferencesMap
	 * .get(LanguagePreferences.Type.QUESTION.toString()); Map<String,
	 * LanguagePreferences> catalogueMap = languagePreferencesMap
	 * .get(LanguagePreferences.Type.CATALOGUE.toString()); // Map<String,
	 * LanguagePreferences> sectionMap = //
	 * languagePreferencesMap.get(LanguagePreferences.Type.SECTION.toString());
	 * Map<String, LanguagePreferences> dataLevelMap = languagePreferencesMap
	 * .get(LanguagePreferences.Type.DATA_LEVEL.toString()); Map<String,
	 * LanguagePreferences> surveyMasterNameMap = languagePreferencesMap
	 * .get(LanguagePreferences.Type.SURVEY_MASTER.toString());
	 * 
	 * if (!ObjectUtil.isEmpty(surveyMasterNameMap) &&
	 * surveyMasterNameMap.containsKey(surveyAnswers.getSurveyCode())) { String
	 * surveyName =
	 * surveyMasterNameMap.get(surveyAnswers.getSurveyCode()).getName(); if
	 * (!StringUtil.isEmpty(surveyName)) {
	 * surveyAnswers.setSurveyName(surveyName); } }
	 * 
	 * if (!ObjectUtil.isEmpty(dataLevelMap)) { if
	 * (dataLevelMap.containsKey(surveyAnswers.getCategoryCode())) { if
	 * (!StringUtil.isEmpty(dataLevelMap.get(surveyAnswers.getCategoryCode()).
	 * getName())) surveyAnswers.setCategoryName(dataLevelMap.get(surveyAnswers.
	 * getCategoryCode()).getName()); } }
	 * 
	 * if (!ObjectUtil.isListEmpty(surveyAnswers.getSurveyAnswerDetails())) {
	 * for (SurveyAnswersDetails surveyAnswersDetails :
	 * surveyAnswers.getSurveyAnswerDetails()) {
	 * 
	 * if (!ObjectUtil.isEmpty(questionMap) &&
	 * questionMap.containsKey(surveyAnswersDetails.getQuestionCode())) { if
	 * (!StringUtil.isEmpty(questionMap.get(surveyAnswersDetails.getQuestionCode
	 * ()).getName())) surveyAnswersDetails
	 * .setQuestionName(questionMap.get(surveyAnswersDetails.getQuestionCode()).
	 * getName()); if
	 * (!StringUtil.isEmpty(questionMap.get(surveyAnswersDetails.getQuestionCode
	 * ()).getInfo()))
	 * surveyAnswersDetails.setInfo(questionMap.get(surveyAnswersDetails.
	 * getQuestionCode()).getInfo()); }
	 * setMultilingualNameForSurveyAnswersDetails(surveyAnswersDetails,
	 * catalogueMap);
	 * 
	 * } } }
	 */

	private void setMultilingualNameForSurveyAnswersDetails(SurveyAnswersDetails answers,
			Map<String, LanguagePreferences> catalogueMap) {

		int componentType = answers.getComponentType();
		if (componentType == SurveyQuestion.componentTypeMaster.TEXT_BOX.ordinal()) {
			if (catalogueMap.containsKey(answers.getAnswer4())) {
				answers.setAnswer5(catalogueMap.get(answers.getAnswer4()).getName());
			}
		} else if (componentType == SurveyQuestion.componentTypeMaster.RADIO_BUTTON.ordinal()
				|| componentType == SurveyQuestion.componentTypeMaster.DROP_DOWN.ordinal()
				|| componentType == SurveyQuestion.componentTypeMaster.DROP_DOWN_OTHERS.ordinal()) {
			if (catalogueMap.containsKey(answers.getAnswer())) {
				answers.setAnswer1(catalogueMap.get(answers.getAnswer()).getName());
			}
		}
	}

	public LanguagePreferences findLanguagePreferenceByCodeAndLanguage(String string, String loggedInUserLanguage) {

		return certificationDAO.findLanguagePreferenceByCodeAndLanguage(string, loggedInUserLanguage);
	}

	public List<SurveyMaster> listSurveyMasterObjectByDvId(String dataCollectorId, long revisionNo) {

		return certificationDAO.listSurveyMasterObjectByDvId(dataCollectorId, revisionNo);
	}

	public List<SurveyMaster> listSurveyMasterObjectByAgentId(String dataCollectorId, long revisionNo) {

		return certificationDAO.listSurveyMasterObjectByAgentId(dataCollectorId, revisionNo);
	}

	public long surveyMasterRevisionByAgentId(String agentId) {

		return certificationDAO.surveyMasterRevisionByAgentId(agentId);
	}

	public DataLevel findDataLevelByCode(String code) {

		return certificationDAO.findDataLevelByCode(code);
	}

	public List<Object[]> listSectionBySurveyMaster(SurveyMaster sm) {

		return certificationDAO.listSectionBySurveyMaster(sm);
	}

	public List<LanguagePreferences> findNameLanguageAndType(String lang, String code) {

		return certificationDAO.findNameLanguageAndType(lang, code);
	}
	
	public List<String> listFoCodesByCustomerProgram(List<String> codes) {

		return certificationDAO.listFoCodesByCustomerProgram(codes);
	}
	

	public List<DownloadTask> listDownloadTaskByUser(String userName) {

		// TODO Auto-generated method stub
		return certificationDAO.listDownloadTaskByUser(userName);
	}
	
	public void updateDownloadTask(DownloadTask downloadTask) {

		certificationDAO.updateDownloadTask(downloadTask);
		// certificationDAO.removeOldDownloads(downloadTask.getUserName());
	}
	
	public void addDownloadTask(DownloadTask downloadTask) {

		certificationDAO.save(downloadTask);
		// certificationDAO.removeOldDownloads(downloadTask.getUserName());
	}
	
	public DownloadTask findDownloadTaskById(long id) {

		// TODO Auto-generated method stub
		return certificationDAO.findDownloadTaskById(id);
	}
	
	
	@Override
	public List<Object[]> findFarmerQuestionAnswerByIdAndMultiAnswers(List<Long> id) {
		return certificationDAO.findFarmerQuestionAnswerByIdAndMultiAnswers(id);
	}
	
	public void editFarmerCropProdAnswers(SurveyFarmerCropProdAnswers farmerCropProdAnswers) {

		certificationDAO.update(farmerCropProdAnswers);
	}
		
	@Override
	public List<String> findFQAAnswersByAnswerId(long id) {
		return certificationDAO.findFQAAnswersByAnswerId(id);
	}

	@Override
	public List<Object[]>  listSamithiByFcpa() {
		return certificationDAO.listSamithiByFcpa();
	}

	@Override
	public SurveyQuestion findQuestionByParentDepen(String str) {
		return certificationDAO.findQuestionByParentDepen(str);
	}

	@Override
	public String findDependancyKeyByQuestionId(Long parentId) {
		// TODO Auto-generated method stub
		return certificationDAO.findDependancyKeyByQuestionId(parentId);
	}

	@Override
	public List<LanguagePreferences> listLanguagePreferences() {
		return certificationDAO.listLanguagePreferences();
		
	}

	@Override
	public SurveyQuestion findSurveyQuestionByCode(String code) {
		return certificationDAO.findSurveyQuestionByCode(code);
	}

	@Override
	public List<FarmField> findFarmFieldsByParentIdandStatsuId(String sectionId2) {
		return certificationDAO.findFarmFieldsByParentIdandStatsuId(sectionId2);
	}

	@Override
	public List<Object[]> findFarmCropsFieldsByParentIdandStatsuId(String sectionId2) {
		return certificationDAO.findFarmCropsFieldsByParentIdandStatsuId(sectionId2);
	}

	@Override
	public List<SurveyMaster> listSurveyBySurveyCodes(List<String> surveyCodes) {
		return certificationDAO.listSurveyBySurveyCodes(surveyCodes);
	}

	@Override
	public List<SurveyMaster> listAgentSurveyMaster(String agentId, String revisionNo) {
		return certificationDAO.listAgentSurveyMaster(agentId,revisionNo);
	}

	@Override
	public List<SurveyQuestion> listSurveyQuestion(String revisionNo) {
		return certificationDAO.listSurveyQuestion(revisionNo);
	}

	@Override
	public List<SurveySection> listSurveySections(String revisionNo) {
		return certificationDAO.listSurveySections(revisionNo);
	}

	@Override
	public List<LanguagePreferences> listLanguagePreferencesByLanguage(int type) {
		return certificationDAO.listLanguagePreferencesByLanguage(type);
	}

	@Override
	public List<LanguagePreferences> listLangPrefByCodes(List<String> codes) {
		return certificationDAO.listLangPrefByCodes(codes);
	}


}

