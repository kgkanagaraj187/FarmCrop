/*
 * CustomerValidator.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.ese.view.profile.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.FarmerField;
import com.ese.util.IntegerUtil;
import com.ese.view.validator.IValidator;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sourcetrace.eses.dao.ICertificationDAO;
import com.sourcetrace.eses.dao.IClientDAO;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.inspect.agrocert.SurveyPreferences;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestion;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.JsonUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.ValidationUtil;
import com.sourcetrace.esesw.entity.profile.Country;
import com.sourcetrace.esesw.entity.profile.DataLevel;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomerValidator.
 */
public class QuestionValidator implements IValidator {

	private static final Logger logger = Logger.getLogger(QuestionValidator.class);
	private IClientDAO clientDAO;
	@Autowired
	private ICertificationDAO certificationDAO;
	@Autowired
	private IFarmerDAO farmerDAO;
	private Pattern pattern;
	private Matcher matcher;

	ScriptEngineManager mgr = new ScriptEngineManager();
	ScriptEngine engine = mgr.getEngineByName("JavaScript");

	/**
	 * Validate.
	 * 
	 * @param object
	 *            the object
	 * @return the map< string, string>
	 * @see com.ese.view.validator.IValidator#validate(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> validate(Object object) {

		Map<String, String> errorCodes = new LinkedHashMap<String, String>();

		ClassValidator<SurveyQuestion> classValidator = new ClassValidator<SurveyQuestion>(SurveyQuestion.class);
		SurveyQuestion aQuestion = (SurveyQuestion) object;

		if (logger.isInfoEnabled()) {
			logger.info("validate(Object) " + aQuestion.toString());
		}

		InvalidValue[] values = null;

		/*values = classValidator.getInvalidValues(aQuestion, "name");
		for (InvalidValue value : values) {
			errorCodes.put(value.getPropertyName(), value.getMessage());
		}*/
		
		 if(!StringUtil.isEmpty(aQuestion.getName())){
	        	if(!ValidationUtil.isPatternMaches(aQuestion.getName(), ValidationUtil.NAME_PATTERN)){
	        		errorCodes.put("name","pattern.name");
	        	}
	        }else{
	        	errorCodes.put("name","empty.name");
	        }
		 

		if (StringUtil.isEmpty(aQuestion.getQuestionType())) {
			errorCodes.put("empty.questionType", "empty.questionType");
		}

		if (aQuestion.getSection().getId() == -1 || ObjectUtil.isEmpty(aQuestion.getSection())) {
			errorCodes.put("empty.section", "empty.section");
		}
		

		/*
		 * if(StringUtil.isEmpty(aQuestion.getCountry())||
		 * aQuestion.getCountry() == null){ errorCodes.put("country",
		 * "empty.Country"); }
		 */

		if (!aQuestion.getQuestionType().equals(String.valueOf(SurveyQuestion.questionTypes.FARMULA.ordinal()))
				&& !aQuestion.getQuestionType().equals(String.valueOf(SurveyQuestion.questionTypes.SUB_FORM.ordinal()))
				&& aQuestion.getComponentType() == -1) {
			errorCodes.put("empty.compType", "empty.compType");
		}

		if (!aQuestion.getQuestionType().equals(String.valueOf(SurveyQuestion.questionTypes.SUB_FORM.ordinal()))
				&& aQuestion.getDataCollection() == -1) {
			errorCodes.put("empty.dc", "empty.dc");
		}

		if (!aQuestion.getQuestionType().equals(String.valueOf(SurveyQuestion.questionTypes.SUB_FORM.ordinal()))
				&& !aQuestion.getQuestionType().equals(String.valueOf(SurveyQuestion.questionTypes.FARMULA.ordinal()))
				&& aQuestion.getCollectionType() == -1) {
			errorCodes.put("empty.collType", "empty.collType");
		}
		if (!aQuestion.getQuestionType().equals(String.valueOf(SurveyQuestion.questionTypes.SUB_FORM.ordinal()))
				&& !aQuestion.getQuestionType().equals(String.valueOf(SurveyQuestion.questionTypes.FARMULA.ordinal()))
				&& aQuestion.getValidationType() == -1) {

			errorCodes.put("empty.valType", "empty.valType");
		}
		

		/*int i = 0;
		while (i < aQuestion.getLanguagePreferences().size()) {
			if (StringUtil.isEmpty(aQuestion.getLanguagePreferences().get(i).getName())) {
				errorCodes.put("empty.langPref", "empty.langPref");
			}
			i++;

		}*/

		if (Arrays
				.asList(SurveyQuestion.componentTypeMaster.TEXT_BOX.ordinal(),
						SurveyQuestion.componentTypeMaster.GPS_TEXT_BOX.ordinal(),
						SurveyQuestion.componentTypeMaster.TEXT_AREA.ordinal())
				.contains(aQuestion.getComponentType())) {
			if (aQuestion.getValidationType() == SurveyQuestion.validationTypeMaster.DOUBLE.ordinal()) {
				if (StringUtil.isEmpty(aQuestion.getMaxLength())) {
					errorCodes.put("empty.maxLength", "empty.maxLength");
				} else {
					// String maxLength = aQuestion.getMaxLength().replaceAll("
					// ,$", ".");
					String[] maxLengthArry = aQuestion.getMaxLength().split(",", -1);

					if (maxLengthArry.length == 2) {
						if (!IntegerUtil.isInteger(maxLengthArry[0]) || !IntegerUtil.isInteger(maxLengthArry[1])
								|| Integer.valueOf(maxLengthArry[0]) < 0 || Integer.valueOf(maxLengthArry[1]) < 0) {
							errorCodes.put("valid.maxLength", "valid.maxLength");
						}
					} else {
						errorCodes.put("valid.maxLength", "valid.maxLength");
					}
				}

			} else {
				if (!StringUtil.isEmpty(aQuestion.getMaxLength())) {
					if (!IntegerUtil.isInteger(aQuestion.getMaxLength())
							|| Integer.valueOf(aQuestion.getMaxLength()) < 0) {
						errorCodes.put("valid.maxLength", "valid.maxLength");
					}

				}
			}
		}
		if (aQuestion.getQuestionType().equals(String.valueOf(SurveyQuestion.questionTypes.FARMULA.ordinal()))
				&& StringUtil.isEmpty(aQuestion.getFormulaEquation())) {
			errorCodes.put("empty.formula", "empty.formula");
		}

		if (aQuestion.getQuestionType().equals(String.valueOf(SurveyQuestion.questionTypes.FARMULA.ordinal()))
				&& !StringUtil.isEmpty(aQuestion.getFormulaEquation())) {
			if (!processFormula(aQuestion.getFormulaEquation()).isEmpty()) {
				errorCodes.put("invalid Formula", "invalidFormula");
			}
		}

		if ((aQuestion.getDataCollection() == SurveyQuestion.dataCollectionType.ONCE.ordinal())
				&& (aQuestion.getComponentType() == SurveyQuestion.componentTypeMaster.PHOTO.ordinal())) {
			errorCodes.put("invalidPhotoComponentType", "invalidPhotoComponentType");

		}

		if (aQuestion.getComponentType() == SurveyQuestion.componentTypeMaster.GPS_TEXT_BOX.ordinal()
				&& aQuestion.getValidationType() != SurveyQuestion.validationTypeMaster.GPS_POINTS.ordinal()) {

			errorCodes.put("invalidValidationType", "invalidValidationType");

		}
		if ((aQuestion.getQuestionType().equals(String.valueOf(SurveyQuestion.questionTypes.DEPENDENCY.ordinal())))
				&& StringUtil.isEmpty(aQuestion.getDependencyKey()) && (StringUtil.isEmpty(aQuestion.getParentQuestion().getCode()))) {
			errorCodes.put("selectParentOrDependancy", "selectParentOrDependancy");

		}
		if (aQuestion.getDataCollection() == SurveyQuestion.dataCollectionType.ONCE.ordinal()
				&& StringUtil.isEmpty(aQuestion.getEntityColumn())) {
			errorCodes.put("selectEntity", "selectEntity");

		}

		if (aQuestion.getComponentType() == SurveyQuestion.componentTypeMaster.DATE_PICKER.ordinal()
				&& (aQuestion.getValidationType() != SurveyQuestion.validationTypeMaster.DATE.ordinal())) {
			errorCodes.put("invalidValType", "invalidValType");

		}

		if ((Arrays.asList(SurveyQuestion.validationTypeMaster.DATE.ordinal(),
				SurveyQuestion.validationTypeMaster.DOUBLE.ordinal()).contains(aQuestion.getValidationType()))
				&& aQuestion.getComponentType() == SurveyQuestion.componentTypeMaster.DATE_PICKER.ordinal()

				&& !aQuestion.getDataFormat().equalsIgnoreCase(SurveyQuestion.DATA_FORMAT_DATE)) {
			errorCodes.put("invalidDateFormatForDate", "invalidDateFormatForDate");
		}

		if (aQuestion.getComponentType() == SurveyQuestion.componentTypeMaster.TEXT_BOX.ordinal()
				&& (aQuestion.getValidationType() == SurveyQuestion.validationTypeMaster.DOUBLE.ordinal())
				&& Arrays.asList(SurveyQuestion.DATA_FORMAT_DATE, SurveyQuestion.DATA_FORMAT_NUMBER)
						.contains(aQuestion.getDataFormat())) {
			errorCodes.put("onlyDecimal", "onlyDecimal");

		}

		if (Arrays
				.asList(SurveyQuestion.componentTypeMaster.RADIO_BUTTON.ordinal(),
						SurveyQuestion.componentTypeMaster.RADIO_BUTTON_OTHERS.ordinal())
				.contains(aQuestion.getComponentType())) {
			if (!StringUtil.isEmpty(aQuestion.getListMethodName())
					&& (ObjectUtil.isListEmpty(aQuestion.getAnswerKeyCodes()))) {
				errorCodes.put("selectAnswerKey", "selectAnswerKey");
			} else if (!StringUtil.isEmpty(aQuestion.getListMethodName())) {
				errorCodes.put("onlyAnswerKey", "onlyAnswerKey");
			} else if (ObjectUtil.isListEmpty(aQuestion.getAnswerKeyCodes())) {
				errorCodes.put("selectAnswerKey", "selectAnswerKey");
			}

		}

		if (SurveyQuestion.dropDownComponentTypes.contains(aQuestion.getComponentType())
				&& (!ObjectUtil.isListEmpty(aQuestion.getAnswerKeyCodes()))
				&& !StringUtil.isEmpty(aQuestion.getListMethodName())) {
			errorCodes.put("answerKeyOrListMethod", "answerKeyOrListMethod");
		}

		/*
		 * if (!ObjectUtil.isEmpty(aQuestion.getParentQuestion())) { long
		 * parentId = aQuestion.getParentQuestion().getId(); if (parentId ==
		 * aQuestion.getId()) { errorCodes.put("same.Question",
		 * "same.question"); }
		 * 
		 * SurveyQuestion question =
		 * certificationDAO.findQuestionById(parentId); if (question != null &&
		 * !ObjectUtil.isEmpty(question)) { if
		 * (!SurveyQuestion.parentQuestionComponentTypes.contains(question.
		 * getComponentType()) && aQuestion.getQuestionType().equals(
		 * String.valueOf(SurveyQuestion.questionTypes.DEPENDENCY.ordinal()))) {
		 * errorCodes.put("invalid.parentQuestion", "invalid.parentQuestion"); }
		 * } }
		 */
		if (!StringUtil.isEmpty(aQuestion.getEntityColumn())) {

			String[] entityDetail = aQuestion.getEntityColumn().split("\\.");
		
				FarmerField fmField = farmerDAO.findFarmerFieldByFieldName(aQuestion.getEntityColumn());
				if(fmField!=null && fmField.getDataLevel()!=null){
				DataLevel dataLevel = fmField.getDataLevel();
				String entityName = dataLevel.getEntityAbsoluteName() + "." + entityDetail[1];
				if (aQuestion.getComponentType() == SurveyQuestion.componentTypeMaster.GPS_TEXT_BOX.ordinal()
						&& (!SurveyQuestion.latLongProperties.containsKey(aQuestion.getEntityColumn()))) {
					errorCodes.put("invalid.entityProp", "invalid.entityProp");
				}
				Field field = getField(entityName);
				if (!field.getType().isPrimitive() && !JsonUtil.isWrapperType(field.getType())
						&& StringUtil.isEmpty(aQuestion.getListMethodName())) {
					errorCodes.put("empty.listMethod", "empty.listMethod");
				}
				}
			
		}

		if (SurveyQuestion.otherComponetTypes.contains(aQuestion.getComponentType())
				&& StringUtil.isEmpty(aQuestion.getOtherCatalogValue())) {
			errorCodes.put("empty.otherCatalog", "empty.otherCatalog");

		}

		if ((aQuestion.getQuestionType().equals(String.valueOf(SurveyQuestion.questionTypes.SUB_FORM.ordinal())))
				&& StringUtil.isListEmpty(aQuestion.getQuestionCodes())) {
			errorCodes.put("name", "empty.QuestionCodes");
		}
		
		 if(!StringUtil.isEmpty(aQuestion.getExiDependencyKey()) && !StringUtil.isEmpty(aQuestion.getDependencyKey())){
	        	String err ="";
	        	List<String> exi = new ArrayList<String>();
	        	List<String> dep = new ArrayList<String>();
	        	exi = aQuestion.getExiDependencyKey().contains(",") ? Lists.newArrayList(Splitter.on(",").trimResults().split(aQuestion.getExiDependencyKey())) : new ArrayList<String>(
	                    Arrays.asList(aQuestion.getExiDependencyKey()));
	        	dep = aQuestion.getDependencyKey().contains(",") ? Lists.newArrayList(Splitter.on(",").trimResults().split(aQuestion.getDependencyKey())) : new ArrayList<String>(
	                    Arrays.asList(aQuestion.getDependencyKey()));
	            exi.removeAll(dep);
	            if(!exi.isEmpty()){
	            	for(String str :exi){
	            		SurveyQuestion q = certificationDAO.findQuestionByParentDepen(str);
	            		if(q!=null){
	            			 errorCodes.put("name", "mapped.depe");
	            			
	            		}
	            	}
	            }
	        	
	        }

		return errorCodes;
	}

	public static Field getField(String classAndFieldName) {

		String fieldName = StringUtil.substringLast(classAndFieldName, ".");
		String className = StringUtil.substringBeforeLast(classAndFieldName, ".");
		Field field = null;
		try {
			Class<?> objClass = Class.forName(className);
			field = objClass.getDeclaredField(fieldName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return field;
	}

	/**
	 * Process formula.
	 * 
	 * @param infix
	 *            the infix
	 * @return the map< string, string>
	 */
	public Map<String, String> processFormula(String infix) {

		Map<String, String> errorCodes = new LinkedHashMap<String, String>();
		infix = infix.replaceAll("\\s", "");

		Pattern p = Pattern.compile("\\##([^##]*)\\##");
		Matcher m = p.matcher(infix);
		Map<String, SurveyPreferences> surveyPreferencesMap = new HashMap<String, SurveyPreferences>();
		Country country = new Country();
		country.setId(4);
		while (m.find()) {
			SurveyPreferences surveyPreferences = surveyPreferencesMap.get(m.group(1));
			if (ObjectUtil.isEmpty(surveyPreferences)) {
				surveyPreferences = certificationDAO.findSurveyPreference(m.group(1), null,
						SurveyPreferences.typeMaster.Constants.ordinal(), country);
			}
			if (!ObjectUtil.isEmpty(surveyPreferences)) {
				surveyPreferencesMap.put(surveyPreferences.getCode(), surveyPreferences);
				infix = infix.replaceAll(SurveyQuestion.FORMULA_CONSTANT_VALUE + surveyPreferences.getCode()
						+ SurveyQuestion.FORMULA_CONSTANT_VALUE, "(" + surveyPreferences.getValue() + ")");
			}
		}
		List<String> subQuestionList = new ArrayList<String>();
		Pattern p2 = Pattern.compile("\\{([^}]*)\\}");
		Matcher m2 = p2.matcher(infix);
		while (m2.find())
			subQuestionList.add(m2.group(1));

		if (!ObjectUtil.isListEmpty(subQuestionList)) {
			for (String subQn : subQuestionList) {
				infix = infix.replaceAll("\\{" + subQn + "\\}",
						"(" + String.valueOf(IntegerUtil.randomGen(0, 1000)) + ")");
			}
		}

		if (infix.contains(SurveyQuestion.FORMULA_CURRENT_YEAR)) {
			infix = infix.replace(SurveyQuestion.FORMULA_CURRENT_YEAR, String.valueOf(DateUtil.getCurrentYear()));
		}

		try {
			String result = String.valueOf(engine.eval(infix));
			if (result.equalsIgnoreCase("infinity")) {
				errorCodes.put("infinity", "infinity");
			}
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			errorCodes.put(e.toString(), e.toString());
		}

		return errorCodes;
	}

	/**
	 * Gets the client dao.
	 * 
	 * @return the client dao
	 */
	public IClientDAO getClientDAO() {

		return clientDAO;
	}

	/**
	 * Sets the client dao.
	 * 
	 * @param clientDAO
	 *            the new client dao
	 */
	public void setClientDAO(IClientDAO clientDAO) {

		this.clientDAO = clientDAO;
	}

	public ICertificationDAO getCertificationDAO() {
		return certificationDAO;
	}

	public void setCertificationDAO(ICertificationDAO certificationDAO) {
		this.certificationDAO = certificationDAO;
	}

}
