/*
 * SeasonDownload.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.inspect.agrocert.AgentSurveyMapping;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.inspect.agrocert.SurveyMaster;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestion;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestionMapping;
import com.sourcetrace.eses.inspect.agrocert.SurveySection;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;;

// TODO: Auto-generated Javadoc
/**
 * The Class SeasonDownload.
 */
@Component
public class SurveyMasterDownload implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(SurveyMasterDownload.class.getName());
	@Autowired

	private ICertificationService certificationService;
	@Autowired

	private IAgentService agentService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {

		/** HEADER VALUES **/
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		Agent agent = agentService.findAgentByProfileIdWithSurvey(head.getAgentId());

		String revisionNo = (String) reqData.get(TransactionProperties.QUESTION_REVISION_NO);
		if (StringUtil.isEmpty(revisionNo))
			throw new SwitchException(SwitchErrorCodes.EMPTY_CATALOGUE_REVISION_NO);
		LOGGER.info("REVISION NO" + revisionNo);

		if (!StringUtil.isLong(revisionNo)) {
			revisionNo = "0";
		}
	
		List<SurveyMaster> surveys =	agent.getSurveys().stream()
				.map(AgentSurveyMapping::getSurveyMaster).collect(Collectors.toList());

		/** REQUEST VALUES **/
		Map resp = new HashedMap();

		if (!ObjectUtil.isListEmpty(surveys)) {
			revisionNo = String.valueOf(surveys.get(0).getRevisionNumber());
		}

		Collection questionCollection = new Collection();
		List<Object> questionObjects = new ArrayList<Object>();

		for (SurveyMaster question : surveys) {

			Map objectMap = formResponse(question);
			Object quesObject = new Object();
			quesObject.setData(CollectionUtil.mapToList(objectMap));

			questionObjects.add(quesObject);

		}
		questionCollection.setObject(questionObjects);
		/** RESPONSE DATA **/
		resp.put(TransactionProperties.QUESTION_LIST, questionCollection);
		resp.put(TransactionProperties.QUESTION_REVISION_NO, revisionNo);
		return resp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.txn.adapter.ITxnAdapter#processVoid(java.util.Map)
	 */
	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {

		return null;
	}

	public Collection getCollection(List<java.lang.Object> lpListObj, Map<String, Integer> quesOrde) {
		Collection langColl = new Collection();
		List<Object> languages = new ArrayList<Object>();
		if (!lpListObj.isEmpty() && lpListObj.get(0) instanceof LanguagePreferences) {

			List<LanguagePreferences> lpList = (List<LanguagePreferences>) (java.lang.Object) lpListObj;
			for (LanguagePreferences lp : lpList) {
				Data langCode = new Data();
				langCode.setKey(TransactionProperties.LANGUAGE_CODE);
				langCode.setValue(lp.getLang());

				Data langName = new Data();
				langName.setKey(TransactionProperties.LANGUAGE_NAME);
				langName.setValue(lp.getName());

				List<Data> langDataList = new ArrayList<Data>();
				langDataList.add(langCode);
				langDataList.add(langName);

				Object langMasterObject = new Object();
				langMasterObject.setData(langDataList);
				languages.add(langMasterObject);
				langColl.setObject(languages);

			}
		} else if (!lpListObj.isEmpty() && lpListObj.get(0) instanceof SurveyQuestion) {

			List<SurveyQuestion> lpList = (List<SurveyQuestion>) (java.lang.Object) lpListObj;
			for (SurveyQuestion lp : lpList) {
				Data langCode = new Data();
				langCode.setKey(TransactionProperties.QUESTIONS_CODE);
				langCode.setValue(String.valueOf(lp.getCode()));

				
				Data order = new Data();
				order.setKey(TransactionProperties.ORDER_NUMBER_SEQ);
				order.setValue(quesOrde.containsKey(lp.getCode()) ? String.valueOf(quesOrde.get(lp.getCode())) : "0");

				List<Data> langDataList = new ArrayList<Data>();
				langDataList.add(langCode);
			
				langDataList.add(order);

				Object langMasterObject = new Object();
				langMasterObject.setData(langDataList);
				languages.add(langMasterObject);
				langColl.setObject(languages);

			}
		}
		return langColl;
	}

	@SuppressWarnings("unchecked")
	public Map formResponse(SurveyMaster survey) {

		Map objectMap = new LinkedHashMap();
		List<SurveyQuestion> quesList = survey.getSurveyQuestionMapping().stream()
				.map(SurveyQuestionMapping::getQuestion).collect(Collectors.toList());
		Map<String, Integer> quesOrde =  survey.getSurveyQuestionMapping().stream()
				.collect(Collectors.toMap(obj -> obj.getQuestion().getCode(),SurveyQuestionMapping::getQuestionOrder));
		Map<SurveySection, List<SurveyQuestion>> sectQues = quesList.stream()
				.collect(Collectors.groupingBy(obj -> obj.getSection()));
			objectMap.put(TransactionProperties.SURVEY_CODE, (survey.getCode()));
		objectMap.put(TransactionProperties.SURVEYS_NAME, (survey.getName()));
		objectMap.put(TransactionProperties.SURVEY_TYPE, (survey.getSurveyType().getId()));
		objectMap.put(TransactionProperties.DATA_LEVEL, (survey.getDataLevel().getCode()));
		objectMap.put(TransactionProperties.LANG_LIST,
				(getCollection(new ArrayList<java.lang.Object>(survey.getLanguagePreferences()),quesOrde)));

		Collection langColl = new Collection();
		List<Object> languages = new ArrayList<Object>();

		for (Entry<SurveySection, List<SurveyQuestion>> section : sectQues.entrySet()) {
			SurveySection sect = section.getKey();
			List<SurveyQuestion> questionList = section.getValue();

			Data secCode = new Data();
			secCode.setKey(TransactionProperties.SECTIONS_CODE);
			secCode.setValue(String.valueOf(sect.getId()));

			Data ques = new Data();
			ques.setKey(TransactionProperties.QUESTION_LIST);
			ques.setCollectionValue((getCollection(new ArrayList<java.lang.Object>(questionList),quesOrde)));

			List<Data> langDataList = new ArrayList<Data>();
			langDataList.add(secCode);
			langDataList.add(ques);
			Object langMasterObject = new Object();
			langMasterObject.setData(langDataList);
			languages.add(langMasterObject);
			langColl.setObject(languages);

		}

		objectMap.put(TransactionProperties.SECTION_LIST, (langColl));
		return objectMap;

	}

	/**
	 * Gets the product distribution service.
	 * 
	 * @return the product distribution service
	 */

}
