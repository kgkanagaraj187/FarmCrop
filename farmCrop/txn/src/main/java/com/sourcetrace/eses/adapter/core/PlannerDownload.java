/*
 * PlannerDownload.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.txn.training.Observations;
import com.ese.entity.txn.training.Planner;
import com.ese.entity.txn.training.Topic;
import com.ese.entity.txn.training.TopicCategory;
import com.ese.entity.txn.training.TrainingMaterial;
import com.ese.entity.txn.training.TrainingMethod;
import com.ese.entity.txn.training.TrainingTopic;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;

@Component
public class PlannerDownload implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(PlannerDownload.class.getName());
	@Autowired
	private ITrainingService trainingService;
	@Autowired
	private IAgentService agentService;
	private String tenantId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		/** REQUEST DATA **/
		LOGGER.info("--------------------PlannerDownload statrts--------------------------");
		String revisionNo = (reqData.containsKey(TransactionProperties.PLANNER_DOWNLOAD_REVISION_NO))
				? (String) reqData.get(TransactionProperties.PLANNER_DOWNLOAD_REVISION_NO) : "0";

		LOGGER.info("=================PLANNER_DOWNLOAD_REV_NO  ======================" + revisionNo);
		  if(StringUtil.isEmpty(revisionNo) || !StringUtil.isLong(revisionNo)){
	        	revisionNo = "0";
	        }
		/*
		 * String revisionNo = (String)
		 * reqData.get(TxnEnrollmentProperties.PLANNER_DOWNLOAD_REVISION_NO);
		 */
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String agentId = head.getAgentId();
		setTenantId(head.getTenantId());
		if (StringUtil.isEmpty(agentId))
			throw new SwitchException(ITxnErrorCodes.AGENT_ID_EMPTY);
		Agent agent = agentService.findAgentByProfileId(agentId);
		if (ObjectUtil.isEmpty(agent))
			throw new SwitchException(ITxnErrorCodes.INVALID_AGENT);
		if (StringUtil.isEmpty(revisionNo)) {
			throw new SwitchException(SwitchErrorCodes.EMPTY_REVISION_NO);
		}
		LOGGER.info("REVISION NO" + revisionNo);
		Map resp = new HashMap();
		// Agent agentInfo = agentService.findAgent(Long.valueOf(agentId));
		List<Object> trainingObjList = new ArrayList<Object>();
		Collection trainingCollection = new Collection();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int week = 0;//cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);

		try {
			// SimpleDateFormat df = new SimpleDateFormat(format);
			Date date = new Date();
			Calendar calz = Calendar.getInstance();
			calz.setTime(date);
			week = calz.get(Calendar.WEEK_OF_MONTH);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * List<FarmerTraining> farmerTrainings =
		 * trainingService.listFarmerTrainingByAgentIdandRevNo(agent.getId(),
		 * revisionNo);
		 */
		List<FarmerTraining> farmerTrainings = trainingService.listFarmerTrainingByCurrentWeek(year, month, week,
				head.getBranchId());
		Map objectMap = new LinkedHashMap();
		if (!ObjectUtil.isListEmpty(farmerTrainings)) {
			for (FarmerTraining farmerTraining : farmerTrainings) {
				objectMap.put(TxnEnrollmentProperties.TRAINING_CODE, farmerTraining.getCode());
				TrainingTopic trainingTopic = farmerTraining.getTrainingTopic();
				objectMap.put(TxnEnrollmentProperties.TRAINING_NAME, trainingTopic.getName());
				Set<TopicCategory> topicCategories = farmerTraining.getTopicCategories();
				// Training Topics
				Collection topicCollection = new Collection();
				List<Object> topicObjList = new ArrayList<Object>();
				if (topicCategories != null) {
					for (TopicCategory topicCategory : topicCategories) {
						Map topicLists = formTopics(topicCategory,farmerTraining.getTopics());
						Object topicObj = new Object();
						topicObj.setData(CollectionUtil.mapToList(topicLists));
						topicObjList.add(topicObj);
					}
				}
				topicCollection.setObject(topicObjList);
				objectMap.put(TxnEnrollmentProperties.TOPIC_LIST, topicCollection);
				// Training Materials
				Collection materialCollection = new Collection();
				List<Object> materialObjList = new ArrayList();
				Set<TrainingMaterial> trainingMaterialSet = farmerTraining.getMaterials();
				if (!ObjectUtil.isListEmpty(trainingMaterialSet)) {
					trainingMaterialSet.forEach(trainingMaterial -> {
						Map materialMap = formTrainingMaterial(trainingMaterial);
						Object object = new Object();
						object.setData(CollectionUtil.mapToList(materialMap));
						materialObjList.add(object);
					});
					materialCollection.setObject(materialObjList);
					objectMap.put(TxnEnrollmentProperties.TRAINING_MATERIAL_LIST, materialCollection);
				}
				// Training Methods
				Collection methodCollection = new Collection();
				List<Object> methodListObj = new ArrayList<Object>();
				Set<TrainingMethod> trainingMethodSet = farmerTraining.getTrainingMethods();
				if (!ObjectUtil.isListEmpty(trainingMethodSet)) {
					trainingMethodSet.forEach(tariningMtd -> {
						Map methodMap = formTrainingMethod(tariningMtd);
						Object object = new Object();
						object.setData(CollectionUtil.mapToList(methodMap));
						methodListObj.add(object);
					});
					methodCollection.setObject(methodListObj);
					objectMap.put(TxnEnrollmentProperties.TRAINING_METHOD_LIST, methodCollection);
				}
				// Training Observation
				Collection observationCollection = new Collection();
				List<Object> observationListObj = new ArrayList<Object>();
				Set<Observations> observationSet = farmerTraining.getObservations();
				if (!ObjectUtil.isListEmpty(observationSet)) {
					observationSet.forEach(obs -> {
						Map observationMap = formObservation(obs);
						Object object = new Object();
						object.setData(CollectionUtil.mapToList(observationMap));
						observationListObj.add(object);
					});
					observationCollection.setObject(observationListObj);
					objectMap.put(TxnEnrollmentProperties.TRAINING_OBSERVATION_LIST, observationCollection);
				}
				// TopicCategory topicCategory=farmerTrainings
				/*
				 * Data trainingCode=new Data();
				 * trainingCode.setKey(TxnEnrollmentProperties.TRAINING_CODE);
				 * trainingCode.setValue(farmerTraining.getCode());
				 * 
				 * Data trainingName=new Data();
				 * trainingName.setKey(TxnEnrollmentProperties.TRAINING_NAME);
				 * TrainingTopic
				 * trainingTopic=farmerTraining.getTrainingTopic();
				 * trainingName.setValue(trainingTopic.getName());
				 */

				// Planners
				/*
				 * List<Object> plannerObjectList = new ArrayList<Object>(); for
				 * (Planner planner : farmerTraining.getPlanners()) { Data
				 * plannerData = new Data();
				 * plannerData.setKey(TxnEnrollmentProperties.PLANNER);
				 * plannerData.setValue(planner.getYear() + "-" +
				 * planner.getMonth() + "-" + planner.getWeek());
				 * 
				 * Data trainingCodeData = new Data();
				 * trainingCodeData.setKey(TxnEnrollmentProperties.
				 * PLANNER_TRAINING_CODE_REF);
				 * trainingCodeData.setValue(farmerTraining.getCode());
				 * 
				 * List<Data> plannerDataList = new ArrayList<Data>();
				 * plannerDataList.add(trainingCodeData);
				 * plannerDataList.add(plannerData);
				 * 
				 * Object plannerObject = new Object();
				 * plannerObject.setData(plannerDataList);
				 * 
				 * plannerObjectList.add(plannerObject);
				 * 
				 * }
				 */

				// Planners
				Collection plannerCollection = new Collection();
				List<Object> plannerObjectList = new ArrayList<Object>();
				Set<Planner> plannerSet = farmerTraining.getPlanners();
				if (!ObjectUtil.isListEmpty(plannerSet)) {
					plannerSet.forEach(obs -> {
						Map plannerMap = formPlanner(obs);
						Object object = new Object();
						object.setData(CollectionUtil.mapToList(plannerMap));
						plannerObjectList.add(object);
					});
					plannerCollection.setObject(plannerObjectList);
					objectMap.put(TxnEnrollmentProperties.PLANNER_LIST, plannerCollection);
				}

				Object object = new Object();
				object.setData(CollectionUtil.mapToList(objectMap));
				trainingObjList.add(object);
			}

		}
		trainingCollection.setObject(trainingObjList);

		/*
		 * Collection criteriaCollection = new Collection(); Collection
		 * plannerCollection = new Collection(); List<Object> criteriaObjectList
		 * = new ArrayList<Object>(); List<Object> plannerObjectList = new
		 * ArrayList<Object>(); List<FarmerTraining> farmerTrainingList =
		 * trainingService.listFarmerTrainingByRevNo(Long.parseLong(revisionNo))
		 * ; for (FarmerTraining training : farmerTrainingList) { // Topics for
		 * (Topic topic : training.getTopics()) { Data trainingCodeData = new
		 * Data(); trainingCodeData.setKey(TxnEnrollmentProperties.
		 * CRITERIA_TRAINING_CODE_REF);
		 * trainingCodeData.setValue(training.getCode());
		 * 
		 * Data topicCodeData = new Data();
		 * topicCodeData.setKey(TxnEnrollmentProperties.WAREHOUSE_CATEGORY_CODE)
		 * ; topicCodeData.setValue(topic.getCode());
		 * 
		 * List<Data> criteriaDataList = new ArrayList<Data>();
		 * criteriaDataList.add(trainingCodeData);
		 * criteriaDataList.add(topicCodeData);
		 * 
		 * Object criteriaObject = new Object();
		 * criteriaObject.setData(criteriaDataList);
		 * 
		 * criteriaObjectList.add(criteriaObject); } // Planners for (Planner
		 * planner : training.getPlanners()) { Data plannerData = new Data();
		 * plannerData.setKey(TxnEnrollmentProperties.PLANNER);
		 * plannerData.setValue(planner.getYear() + "-" + planner.getMonth() +
		 * "-" + planner.getWeek());
		 * 
		 * Data trainingCodeData = new Data();
		 * trainingCodeData.setKey(TxnEnrollmentProperties.
		 * PLANNER_TRAINING_CODE_REF);
		 * trainingCodeData.setValue(training.getCode());
		 * 
		 * List<Data> plannerDataList = new ArrayList<Data>();
		 * plannerDataList.add(trainingCodeData);
		 * plannerDataList.add(plannerData);
		 * 
		 * Object plannerObject = new Object();
		 * plannerObject.setData(plannerDataList);
		 * 
		 * plannerObjectList.add(plannerObject);
		 * 
		 * }
		 * 
		 * } criteriaCollection.setObject(criteriaObjectList);
		 * plannerCollection.setObject(plannerObjectList);
		 * resp.put(TxnEnrollmentProperties.SHOP_DEALER_CREDIT_LIST,
		 * criteriaCollection); resp.put(TxnEnrollmentProperties.PLANNER_LIST,
		 * plannerCollection);
		 */
		resp.put(TxnEnrollmentProperties.TRAINING_LIST, trainingCollection);
		if (!ObjectUtil.isListEmpty(farmerTrainings)) {
			resp.put(TxnEnrollmentProperties.PLANNER_DOWNLOAD_REVISION_NO, 0);
		} else {
			resp.put(TxnEnrollmentProperties.PLANNER_DOWNLOAD_REVISION_NO, 0);
		}
		LOGGER.info("-----------Planner download ends-----------------------");
		return resp;
	}

	private Map formObservation(Observations obs) {
		Map objectMap = new LinkedHashMap();
		if (!ObjectUtil.isEmpty(obs)) {
			objectMap.put(TxnEnrollmentProperties.TRAINING_OBSERVATION_CODE, obs.getCode());
			objectMap.put(TxnEnrollmentProperties.TRAINING_OBSERVATION_NAME, obs.getName());
		}
		// TODO Auto-generated method stub
		return objectMap;
	}

	private Map formTrainingMethod(TrainingMethod tariningMtd) {
		Map objectMap = new LinkedHashMap();
		if (!ObjectUtil.isEmpty(tariningMtd)) {
			objectMap.put(TxnEnrollmentProperties.TRAINING_METHOD_CODE, tariningMtd.getCode());
			objectMap.put(TxnEnrollmentProperties.TRAINING_METHOD_NAME, tariningMtd.getName());
		}
		// TODO Auto-generated method stub
		return objectMap;

	}

	private Map formTrainingMaterial(TrainingMaterial trainingMaterial) {
		Map objectMap = new LinkedHashMap();
		if (!ObjectUtil.isEmpty(trainingMaterial)) {
			objectMap.put(TxnEnrollmentProperties.TRAINING_MATERIAL_CODE, trainingMaterial.getCode());
			objectMap.put(TxnEnrollmentProperties.TRAINING_MATERIAL_NAME, trainingMaterial.getName());
		}
		// TODO Auto-generated method stub
		return objectMap;
	}

	@SuppressWarnings("unchecked")
	private Map formTopics(TopicCategory topicCategory, Set<Topic> topics) {
		Map objectMap = new LinkedHashMap();
		Collection criteriaCollection = new Collection();
		List<Object> criteriaObjList = new ArrayList();
		Map<String, List<Object>> topicObjectsMap = new HashMap<String, List<Object>>();
		if (!ObjectUtil.isEmpty(topicCategory)) {
			objectMap.put(TxnEnrollmentProperties.CRITERIA_CATEGORY_CODE, topicCategory.getCode());
			objectMap.put(TxnEnrollmentProperties.CRITERIA_CATEGORY_NAME, topicCategory.getName());
			//Set<Topic> topics = topicCategory.getTopics();
			topics.forEach(topic -> {
				Map obejctCriteriaMap = formCriteria(topic);
				Object criteriaObj = new Object();
				criteriaObj.setData(CollectionUtil.mapToList(obejctCriteriaMap));
				criteriaObjList.add(criteriaObj);
				criteriaCollection.setObject(criteriaObjList);
			});

			objectMap.put(TxnEnrollmentProperties.CRITERIA_LIST, criteriaCollection);
		}
		// TODO Auto-generated method stub
		return objectMap;
	}

	private Map formCriteria(Topic topic) {
		Map objectMap = new LinkedHashMap();
		if (!ObjectUtil.isEmpty(topic)) {
			objectMap.put(TxnEnrollmentProperties.CRITERIA_CODE, topic.getCode());
			objectMap.put(TxnEnrollmentProperties.CRITERIA_NAME, topic.getPrinciple());
		}
		// TODO Auto-generated method stub
		return objectMap;
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

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	private Map formPlanner(Planner planner) {
		Map objectMap = new LinkedHashMap();
		if (!ObjectUtil.isEmpty(planner)) {
			objectMap.put(TxnEnrollmentProperties.PLANNER,
					planner.getYear() + "-" + planner.getMonth() + "-" + planner.getWeek());
			objectMap.put(TxnEnrollmentProperties.PLANNER_TRAINING_CODE_REF,
					!ObjectUtil.isEmpty(planner.getFarmerTraining()) ? planner.getFarmerTraining().getCode() : "");
		}
		// TODO Auto-generated method stub
		return objectMap;
	}
}
