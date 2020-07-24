package com.sourcetrace.eses.dao;

/*
 * ITrainingDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */

import java.util.List;

import com.ese.entity.txn.training.FarmerTraining;
import com.ese.entity.txn.training.Observations;
import com.ese.entity.txn.training.OfflineTrainingStatus;
import com.ese.entity.txn.training.Planner;
import com.ese.entity.txn.training.TargetGroup;
import com.ese.entity.txn.training.Topic;
import com.ese.entity.txn.training.TopicCategory;
import com.ese.entity.txn.training.TrainingMaterial;
import com.ese.entity.txn.training.TrainingMethod;
import com.ese.entity.txn.training.TrainingStatus;
import com.ese.entity.txn.training.TrainingStatusLocation;
import com.ese.entity.txn.training.TrainingTopic;

public interface ITrainingDAO extends IESEDAO {

	/**
	 * List farmer training.
	 * 
	 * @return the list< farmer training>
	 */
	List<FarmerTraining> listFarmerTraining();

	/**
	 * Find farmer training by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farmer training
	 */
	FarmerTraining findFarmerTrainingById(long id);

	/**
	 * List target group.
	 * 
	 * @return the list< target group>
	 */
	List<TargetGroup> listTargetGroup();

	/**
	 * List training topic.
	 * 
	 * @return the list< training topic>
	 */
	List<TrainingTopic> listTrainingTopic();

	/**
	 * Find target group by code.
	 * 
	 * @param code
	 *            the code
	 * @return the target group
	 */
	TargetGroup findTargetGroupByCode(String code);

	/**
	 * Find target group by name.
	 * 
	 * @param name
	 *            the name
	 * @return the target group
	 */
	TargetGroup findTargetGroupByName(String name);

	/**
	 * Find training topic by code.
	 * 
	 * @param code
	 *            the code
	 * @return the training topic
	 */
	TrainingTopic findTrainingTopicByCode(String code);

	/**
	 * Find training topic by name.
	 * 
	 * @param name
	 *            the name
	 * @return the training topic
	 */
	TrainingTopic findTrainingTopicByName(String name);

	/**
	 * List topic.
	 * 
	 * @return the list< topic>
	 */
	List<Topic> listTopic();

	/**
	 * List training method.
	 * 
	 * @return the list< training method>
	 */
	List<TrainingMethod> listTrainingMethod();

	/**
	 * Find target group by id.
	 * 
	 * @param id
	 *            the id
	 * @return the target group
	 */
	TargetGroup findTargetGroupById(long id);

	/**
	 * Find topic by code.
	 * 
	 * @param code
	 *            the code
	 * @return the topic
	 */
	Topic findTopicByCode(String code);

	/**
	 * Find topic by id.
	 * 
	 * @param id
	 *            the id
	 * @return the topic
	 */
	Topic findTopicById(long id);

	/**
	 * Find topic by name.
	 * 
	 * @param name
	 *            the name
	 * @return the topic
	 */
	Topic findTopicByName(String name);

	/**
	 * Find training method by code.
	 * 
	 * @param code
	 *            the code
	 * @return the training method
	 */
	TrainingMethod findTrainingMethodByCode(String code);

	/**
	 * Find training method by id.
	 * 
	 * @param id
	 *            the id
	 * @return the training method
	 */
	TrainingMethod findTrainingMethodById(long id);

	/**
	 * Find training method by name.
	 * 
	 * @param name
	 *            the name
	 * @return the training method
	 */
	TrainingMethod findTrainingMethodByName(String name);

	/**
	 * Find training topic by id.
	 * 
	 * @param id
	 *            the id
	 * @return the training topic
	 */
	TrainingTopic findTrainingTopicById(long id);

	/**
	 * Find farmer training by code.
	 * 
	 * @param code
	 *            the code
	 * @return the farmer training
	 */
	FarmerTraining findFarmerTrainingByCode(String code);

	/**
	 * Find topic category by id.
	 * 
	 * @param id
	 *            the id
	 * @return the topic category
	 */
	TopicCategory findTopicCategoryById(Long id);

	/**
	 * Find topic category by code.
	 * 
	 * @param code
	 *            the code
	 * @return the topic category
	 */
	TopicCategory findTopicCategoryByCode(String code);

	/**
	 * Find topic category by name.
	 * 
	 * @param name
	 *            the name
	 * @return the topic category
	 */
	TopicCategory findTopicCategoryByName(String name);

	/**
	 * List topic category.
	 * 
	 * @return the list< topic category>
	 */
	List<TopicCategory> listTopicCategory();

	/**
	 * List farmer training by topic.
	 * 
	 * @param id
	 *            the id
	 * @return the list< farmer training>
	 */
	List<FarmerTraining> listFarmerTrainingByTopic(long id);

	/**
	 * Find farmer training selection with training topic.
	 * 
	 * @param id
	 *            the id
	 * @return true, if successful
	 */
	boolean findFarmerTrainingSelectionWithTrainingTopic(Long id);

	/**
	 * List farmer training by training method.
	 * 
	 * @param id
	 *            the id
	 * @return the list< farmer training>
	 */
	List<FarmerTraining> listFarmerTrainingByTrainingMethod(long id);

	/**
	 * List farmer training by target group.
	 * 
	 * @param id
	 *            the id
	 * @return the list< farmer training>
	 */
	List<FarmerTraining> listFarmerTrainingByTargetGroup(long id);

	/**
	 * List topic by rev no.
	 * 
	 * @param revisionNo
	 *            the revision no
	 * @return the list< topic>
	 */
	List<Topic> listTopicByRevNo(Long revisionNo);

	/**
	 * Find training status by receipt no.
	 * 
	 * @param receiptNo
	 *            the receipt no
	 * @return the training status
	 */
	TrainingStatus findTrainingStatusByReceiptNo(String receiptNo);

	/**
	 * List offline training status.
	 * 
	 * @return the list< offline training status>
	 */
	List<OfflineTrainingStatus> listOfflineTrainingStatus();

	/**
	 * List farmer training by rev no.
	 * 
	 * @param revisionNo
	 *            the revision no
	 * @return the list< farmer training>
	 */
	List<FarmerTraining> listFarmerTrainingByRevNo(Long revisionNo);

	/**
	 * Find training status by id.
	 * 
	 * @param id
	 *            the id
	 * @return the training status
	 */
	public TrainingStatus findTrainingStatusById(Long id);

	/**
	 * List training status loctaion.
	 * 
	 * @param id
	 *            the id
	 * @return the list< training status location>
	 */
	public List<TrainingStatusLocation> listTrainingStatusLoctaion(long id);

	/**
	 * Load training status location image.
	 * 
	 * @param id
	 *            the id
	 * @return the training status location
	 */
	public TrainingStatusLocation loadTrainingStatusLocationImage(Long id);

	/**
	 * Find active farmer training by id.
	 * 
	 * @param farmerTrainingId
	 *            the farmer training id
	 * @return the farmer training
	 */
	public FarmerTraining findActiveFarmerTrainingById(long farmerTrainingId);

	/**
	 * List topic category by rev no.
	 * 
	 * @param revNo
	 *            the rev no
	 * @return the list< topic category>
	 */
	public List<TopicCategory> listTopicCategoryByRevNo(long revNo);

	/**
	 * Find farmer training latest revision no.
	 * 
	 * @return the long
	 */
	public Long findFarmerTrainingLatestRevisionNo();

	/**
	 * Find topic latest revision no.
	 * 
	 * @return the long
	 */
	public Long findTopicLatestRevisionNo();

	/**
	 * Find topic category latest revision no.
	 * 
	 * @return the long
	 */
	public Long findTopicCategoryLatestRevisionNo();

	public List<OfflineTrainingStatus> listOfflineTrainingStatus(String tenantId);

	public TrainingStatus findTrainingStatusByReceiptNo(String receiptNo, String tenantId);

	public Topic findTopicByCode(String topicValue, String tenantId);

	public FarmerTraining findFarmerTrainingByCode(String farmerTrainingCode, String tenantId);

	void update(OfflineTrainingStatus offlineTrainingStatus, String tenantId);

	void saveTrainingStatus(TrainingStatus trainingStatus, String tenantId);

	List<FarmerTraining> listFarmerTrainingByAgentIdandRevNo(long id, String revisionNo);
	
	TrainingMaterial findtrainingMaterialByCode(String code);
	
	TrainingMaterial findtrainingMaterialByCode(String code, String tenantId);
	
	TrainingMethod findTrainingMethodByCode(String code,String tenant);


TrainingMaterial findTrainingMaterialByName(String name);

Observations findObservationsById(Long id);

boolean listFarmerTrainingByObservations(long id);

List<FarmerTraining> listFarmerTrainingByMaterialId(long id);

TrainingMaterial findTrainingMaterial(Long id);

Observations findtrainingObservationByCode(String code);

Observations findtrainingObservationByCode(String code, String tenantId);

List<TrainingMaterial> listTrainingMaterials();

List<Observations> listObservations();

List<FarmerTraining> listFarmerTrainingByCurrentWeek(int year, int month, int week, String branchId);

Planner findPlannerByMntYrWk(int year, int month, int week, long farmerTranId);

void deletePlanner(long id);

TrainingTopic findTrainingTopicByNameBasedOnBranch(String name, String branchId);

List<TrainingStatus> findTrainingStatusByFarmerId(String farmerId);

public List<FarmerTraining> listActiveFarmerTraining();
}
