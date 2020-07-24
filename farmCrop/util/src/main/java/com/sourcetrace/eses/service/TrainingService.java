/*
 * TrainingService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.sourcetrace.eses.dao.ITrainingDAO;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;

@Service
@Transactional
public class TrainingService implements ITrainingService {
	@Autowired
    private ITrainingDAO trainingDAO;

    /**
     * Sets the training dao.
     * @param trainingDAO the new training dao
     */
    public void setTrainingDAO(ITrainingDAO trainingDAO) {

        this.trainingDAO = trainingDAO;
    }

    /**
     * Gets the training dao.
     * @return the training dao
     */
    public ITrainingDAO getTrainingDAO() {

        return trainingDAO;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#addFarmerTraining(com.ese.entity.txn.training
     * .FarmerTraining)
     */
    public void addFarmerTraining(FarmerTraining aFarmerTraining) {

        aFarmerTraining.setRevisionNo(DateUtil.getRevisionNumber());
        trainingDAO.save(aFarmerTraining);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#addTargetGroup(com.ese.entity.txn.training
     * .TargetGroup)
     */
    public void addTargetGroup(TargetGroup aTargetGroup) {

        trainingDAO.save(aTargetGroup);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#addTopic(com.ese.entity.txn.training.Topic
     * )
     */
    public void addTopic(Topic aTopic) {

        aTopic.setRevisionNo(DateUtil.getRevisionNumber());
        if (!ObjectUtil.isEmpty(aTopic.getTopicCategory()))
            aTopic.getTopicCategory().setRevisionNo(DateUtil.getRevisionNumber());
        trainingDAO.save(aTopic);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#addTrainingMethod(com.ese.entity.txn.training
     * .TrainingMethod)
     */
    public void addTrainingMethod(TrainingMethod aTrainingMethod) {

        trainingDAO.save(aTrainingMethod);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#addTrainingTopic(com.ese.entity.txn.training
     * .TrainingTopic)
     */
    public void addTrainingTopic(TrainingTopic aTrainingTopic) {

        trainingDAO.save(aTrainingTopic);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#findFarmerTrainingByCode(java.lang.String)
     */
    public FarmerTraining findFarmerTrainingByCode(String code) {

        return trainingDAO.findFarmerTrainingByCode(code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findFarmerTrainingById(long)
     */
    public FarmerTraining findFarmerTrainingById(long id) {

        return trainingDAO.findFarmerTrainingById(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#findTargetGroupByCode(java.lang.String)
     */
    public TargetGroup findTargetGroupByCode(String code) {

        return trainingDAO.findTargetGroupByCode(code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findTargetGroupById(long)
     */
    public TargetGroup findTargetGroupById(long id) {

        return trainingDAO.findTargetGroupById(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#findTargetGroupByName(java.lang.String)
     */
    public TargetGroup findTargetGroupByName(String name) {

        return trainingDAO.findTargetGroupByName(name);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findTopicByCode(java.lang.String)
     */
    public Topic findTopicByCode(String code) {

        return trainingDAO.findTopicByCode(code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findTopicById(long)
     */
    public Topic findTopicById(long id) {

        return trainingDAO.findTopicById(id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findTopicByName(java.lang.String)
     */
    public Topic findTopicByName(String name) {

        return trainingDAO.findTopicByName(name);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#findTrainingMethodByCode(java.lang.String)
     */
    public TrainingMethod findTrainingMethodByCode(String code) {

        return trainingDAO.findTrainingMethodByCode(code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findTrainingMethodById(long)
     */
    public TrainingMethod findTrainingMethodById(long id) {

        return trainingDAO.findTrainingMethodById(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#findTrainingMethodByName(java.lang.String)
     */
    public TrainingMethod findTrainingMethodByName(String name) {

        return trainingDAO.findTrainingMethodByName(name);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#findTrainingTopicByCode(java.lang.String)
     */
    public TrainingTopic findTrainingTopicByCode(String code) {

        return trainingDAO.findTrainingTopicByCode(code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findTrainingTopicById(long)
     */
    public TrainingTopic findTrainingTopicById(long id) {

        return trainingDAO.findTrainingTopicById(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#findTrainingTopicByName(java.lang.String)
     */
    public TrainingTopic findTrainingTopicByName(String name) {

        return trainingDAO.findTrainingTopicByName(name);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listFarmerTraining()
     */
    public List<FarmerTraining> listFarmerTraining() {

        return trainingDAO.listFarmerTraining();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listTargetGroup()
     */
    public List<TargetGroup> listTargetGroup() {

        return trainingDAO.listTargetGroup();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listTopic()
     */
    public List<Topic> listTopic() {

        return trainingDAO.listTopic();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listTrainingMethod()
     */
    public List<TrainingMethod> listTrainingMethod() {

        return trainingDAO.listTrainingMethod();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listTrainingTopic()
     */
    public List<TrainingTopic> listTrainingTopic() {

        return trainingDAO.listTrainingTopic();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#updateFarmerTraining(com.ese.entity.txn
     * .training.FarmerTraining)
     */
    public void editFarmerTraining(FarmerTraining eFarmerTraining) {

        eFarmerTraining.setRevisionNo(DateUtil.getRevisionNumber());
        trainingDAO.update(eFarmerTraining);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#updateTargetGroup(com.ese.entity.txn.training
     * .TargetGroup)
     */
    public void editTargetGroup(TargetGroup eTargetGroup) {

        trainingDAO.update(eTargetGroup);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#updateTopic(com.ese.entity.txn.training
     * .Topic)
     */
    public void editTopic(Topic eTopic) {

        eTopic.setRevisionNo(DateUtil.getRevisionNumber());
        if (!ObjectUtil.isEmpty(eTopic.getTopicCategory()))
            eTopic.getTopicCategory().setRevisionNo(DateUtil.getRevisionNumber());
        trainingDAO.update(eTopic);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#updateTrainingMethod(com.ese.entity.txn
     * .training.TrainingMethod)
     */
    public void editTrainingMethod(TrainingMethod eTrainingMethod) {

        trainingDAO.update(eTrainingMethod);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#updateTrainingTopic(com.ese.entity.txn
     * .training.TrainingTopic)
     */
    public void editTrainingTopic(TrainingTopic eTrainingTopic) {

        trainingDAO.update(eTrainingTopic);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#removeFarmerTraining(com.ese.entity.txn
     * .training.FarmerTraining)
     */
    public void removeFarmerTraining(FarmerTraining rFarmerTraining) {

        trainingDAO.delete(rFarmerTraining);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#removeTargetGroup(com.ese.entity.txn.training
     * .TargetGroup)
     */
    public void removeTargetGroup(TargetGroup rTargetGroup) {

        trainingDAO.delete(rTargetGroup);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#removeTopic(com.ese.entity.txn.training
     * .Topic)
     */
    public void removeTopic(Topic rTopic) {

        trainingDAO.delete(rTopic);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#removeTrainingMethod(com.ese.entity.txn
     * .training.TrainingMethod)
     */
    public void removeTrainingMethod(TrainingMethod rTrainingMethod) {

        trainingDAO.delete(rTrainingMethod);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#removeTrainingTopic(com.ese.entity.txn
     * .training.TrainingTopic)
     */
    public void removeTrainingTopic(TrainingTopic rTrainingTopic) {

        trainingDAO.delete(rTrainingTopic);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#addPlanner(com.ese.entity.txn.training
     * .Planner)
     */
    public void addPlanner(Planner planner) {

        trainingDAO.save(planner);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#addTopicCategory(com.ese.entity.txn.training
     * .TopicCategory)
     */
    public void addTopicCategory(TopicCategory trainingCriteriaCategory) {

        trainingCriteriaCategory.setRevisionNo(DateUtil.getRevisionNumber());
        trainingDAO.save(trainingCriteriaCategory);

    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findTopicCategoryById(java.lang.Long)
     */
    public TopicCategory findTopicCategoryById(Long id) {

        return trainingDAO.findTopicCategoryById(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#editTopicCategory(com.ese.entity.txn.training
     * .TopicCategory)
     */
    public void editTopicCategory(TopicCategory topicCategory) {

        topicCategory.setRevisionNo(DateUtil.getRevisionNumber());
        trainingDAO.update(topicCategory);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#removeTopicCategory(com.ese.entity.txn
     * .training.TopicCategory)
     */
    public void removeTopicCategory(TopicCategory trainingCriteriaCategory) {

        trainingDAO.delete(trainingCriteriaCategory);

    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listTopicCategory()
     */
    public List<TopicCategory> listTopicCategory() {

        return trainingDAO.listTopicCategory();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listFarmerTrainingByTopic(long)
     */
    public List<FarmerTraining> listFarmerTrainingByTopic(long id) {

        return trainingDAO.listFarmerTrainingByTopic(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#findFarmerTrainingSelectionWithTrainingTopic
     * (java.lang.Long)
     */
    public boolean findFarmerTrainingSelectionWithTrainingTopic(Long id) {

        return trainingDAO.findFarmerTrainingSelectionWithTrainingTopic(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#listFarmerTrainingByTrainingMethod(long)
     */
    public List<FarmerTraining> listFarmerTrainingByTrainingMethod(long id) {

        return trainingDAO.listFarmerTrainingByTrainingMethod(id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listFarmerTrainingByTargetGroup(long)
     */
    public List<FarmerTraining> listFarmerTrainingByTargetGroup(long id) {

        return trainingDAO.listFarmerTrainingByTargetGroup(id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listTopicByRevNo(java.lang.Long)
     */
    public List<Topic> listTopicByRevNo(Long revisionNo) {

        return trainingDAO.listTopicByRevNo(revisionNo);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#findTrainingStatusByReceiptNo(java.lang
     * .String)
     */
    public TrainingStatus findTrainingStatusByReceiptNo(String receiptNo) {

        return trainingDAO.findTrainingStatusByReceiptNo(receiptNo);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#addTrainingStatus(com.ese.entity.txn.training
     * .TrainingStatus)
     */
    public void addTrainingStatus(TrainingStatus trainingStatus) {

        trainingDAO.save(trainingStatus);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#addOfflineTrainingStatus(com.ese.entity
     * .txn.training.OfflineTrainingStatus)
     */
    public void addOfflineTrainingStatus(OfflineTrainingStatus offlineTrainingStatus) {

        trainingDAO.save(offlineTrainingStatus);

    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listOfflineTrainingStatus()
     */
    public List<OfflineTrainingStatus> listOfflineTrainingStatus() {

        return trainingDAO.listOfflineTrainingStatus();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#updateOfflineTrainingStatus(com.ese.entity
     * .txn.training.OfflineTrainingStatus)
     */
    public void updateOfflineTrainingStatus(OfflineTrainingStatus offlineTrainingStatus) {

        trainingDAO.update(offlineTrainingStatus);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#listFarmerTrainingByRevNo(java.lang.Long)
     */
    public List<FarmerTraining> listFarmerTrainingByRevNo(Long revisionNo) {

        return trainingDAO.listFarmerTrainingByRevNo(revisionNo);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#findTrainingStatusById(java.lang.Long)
     */
    public TrainingStatus findTrainingStatusById(Long id) {

        return trainingDAO.findTrainingStatusById(id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listTrainingStatusLoctaion(long)
     */
    public List<TrainingStatusLocation> listTrainingStatusLoctaion(long id) {

        return trainingDAO.listTrainingStatusLoctaion(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.service.txn.ITrainingService#loadTrainingStatusLocationImage(java.lang
     * .String)
     */
    public TrainingStatusLocation loadTrainingStatusLocationImage(Long id) {

        return trainingDAO.loadTrainingStatusLocationImage(id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findActiveFarmerTrainingById(long)
     */
    public FarmerTraining findActiveFarmerTrainingById(long farmerTrainingId) {

        return trainingDAO.findActiveFarmerTrainingById(farmerTrainingId);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#listTopicCategoryByRevNo(long)
     */
    public List<TopicCategory> listTopicCategoryByRevNo(long revNo) {

        return trainingDAO.listTopicCategoryByRevNo(revNo);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findFarmerTrainingLatestRevisionNo()
     */
    public Long findFarmerTrainingLatestRevisionNo() {

        return trainingDAO.findFarmerTrainingLatestRevisionNo();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findTopicCategoryLatestRevisionNo()
     */
    public Long findTopicCategoryLatestRevisionNo() {

        return trainingDAO.findTopicCategoryLatestRevisionNo();
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.service.txn.ITrainingService#findTopicLatestRevisionNo()
     */
    public Long findTopicLatestRevisionNo() {

        return trainingDAO.findTopicLatestRevisionNo();
    }

    @Override
    public List<OfflineTrainingStatus> listOfflineTrainingStatus(String tenantId) {

        return trainingDAO.listOfflineTrainingStatus(tenantId);
    }

    @Override
    public TrainingStatus findTrainingStatusByReceiptNo(String receiptNo, String tenantId) {

        return trainingDAO.findTrainingStatusByReceiptNo(receiptNo,tenantId);
    }

    @Override
    public Topic findTopicByCode(String topicValue, String tenantId) {

        return trainingDAO.findTopicByCode(topicValue,tenantId);
    }

    @Override
    public FarmerTraining findFarmerTrainingByCode(String farmerTrainingCode, String tenantId) {

        return trainingDAO.findFarmerTrainingByCode(farmerTrainingCode,tenantId);
    }

    @Override
    public void updateOfflineTrainingStatus(OfflineTrainingStatus offlineTrainingStatus,
            String tenantId) {

        trainingDAO.update(offlineTrainingStatus,tenantId);

        
    }

	@Override
	public void saveTrainingStatus(TrainingStatus trainingStatus,String tenantId) {
		// TODO Auto-generated method stub
		trainingDAO.saveTrainingStatus(trainingStatus,tenantId);
	}
	@Override
	public Observations findObservationsById(Long id) {
		// TODO Auto-generated method stub
		return trainingDAO.findObservationsById(id);
	}
	public boolean listFarmerTrainingByObservations(long id) {
		// TODO Auto-generated method stub
		return trainingDAO.listFarmerTrainingByObservations(id);
	}
	
	@Override
	public TrainingMaterial findTrainingMaterial(Long id) {
		// TODO Auto-generated method stub
		return trainingDAO.findTrainingMaterial(id);
	}
	public List<FarmerTraining> listFarmerTrainingByMaterialId(long id) {
		// TODO Auto-generated method stub
		return trainingDAO.listFarmerTrainingByMaterialId(id);
	}
	@Override
	public void add(Object obj) {
		trainingDAO.save(obj);
	}

	@Override
	public void update(Object obj) {
		trainingDAO.update(obj);
		
	}

	@Override
	public void delete(Object obj) {
		trainingDAO.delete(obj);
	}

	@Override
	public void saveOrUpdate(Object obj) {
		trainingDAO.saveOrUpdate(obj);
	}
	
	@Override
	public List<FarmerTraining> listFarmerTrainingByAgentIdandRevNo(long id, String revisionNo) {
		// TODO Auto-generated method stub
		return trainingDAO.listFarmerTrainingByAgentIdandRevNo(id,revisionNo);
	}
	
	@Override
	public TrainingMaterial findtrainingMaterialByCode(String code) {
		// TODO Auto-generated method stub
		return trainingDAO.findtrainingMaterialByCode(code);
	}
	
	@Override
	public TrainingMaterial findtrainingMaterialByCode(String code, String tenantId) {
		// TODO Auto-generated method stub
		return trainingDAO.findtrainingMaterialByCode(code,tenantId);
	}
	
	@Override
	public TrainingMethod findTrainingMethodByCode(String code, String tenant) {
		return trainingDAO.findTrainingMethodByCode(code,tenant);
	}
	
	@Override
	public Observations findtrainingObservationByCode(String code) {
		// TODO Auto-generated method stub
		return trainingDAO.findtrainingObservationByCode(code);
	}
	
	@Override
	public Observations findtrainingObservationByCode(String code, String tenantId) {
		// TODO Auto-generated method stub
		return trainingDAO.findtrainingObservationByCode(code,tenantId);
	}
	@Override
	public List<TrainingMaterial> listTrainingMaterials() {
		// TODO Auto-generated method stub
		return trainingDAO.listTrainingMaterials();
	}

	@Override
	public List<Observations> listObservations() {
		// TODO Auto-generated method stub
		return trainingDAO.listObservations();
	}

	@Override
	public List<FarmerTraining> listFarmerTrainingByCurrentWeek(int year, int month, int week,String branchId) {
		// TODO Auto-generated method stub
		return trainingDAO.listFarmerTrainingByCurrentWeek(year,month,week,branchId);
	}

	@Override
	public Planner findPlannerByMntYrWk(int year, int month, int week, long farmerTranId) {
		// TODO Auto-generated method stub
		return trainingDAO.findPlannerByMntYrWk(year,month,week,farmerTranId);
	}

	@Override
	public void deletePlanner(long id) {
		// TODO Auto-generated method stub
		 trainingDAO.deletePlanner(id);
	}

	@Override
	public TrainingTopic findTrainingTopicByNameBasedOnBranch(String name, String branchId) {
		// TODO Auto-generated method stub
		return trainingDAO.findTrainingTopicByNameBasedOnBranch(name,branchId);
	}

	@Override
	public List<TrainingStatus> findTrainingStatusByFarmerId(String farmerId) {
		// TODO Auto-generated method stub
		return trainingDAO.findTrainingStatusByFarmerId(farmerId);
	}

	@Override
	public List<FarmerTraining> listActiveFarmerTraining() {
		return trainingDAO.listActiveFarmerTraining();
	}

}
