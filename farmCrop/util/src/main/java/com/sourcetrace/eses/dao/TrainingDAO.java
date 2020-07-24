/*
 * TrainingDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
import com.sourcetrace.eses.util.ObjectUtil;

@Repository
@Transactional
public class TrainingDAO extends ESEDAO implements ITrainingDAO {
	
	@Autowired
	public TrainingDAO(SessionFactory sessionFactory){
		this.setSessionFactory(sessionFactory);
	}
	
    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findFarmerTrainingById(long)
     */
    public FarmerTraining findFarmerTrainingById(long id) {

        return (FarmerTraining) find("FROM FarmerTraining ft WHERE ft.id = ?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listFarmerTraining()
     */
    @SuppressWarnings("unchecked")
    public List<FarmerTraining> listFarmerTraining() {
        return list("FROM FarmerTraining ft ORDER BY ft.code ASC");
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listTargetGroup()
     */
    @SuppressWarnings("unchecked")
    public List<TargetGroup> listTargetGroup() {

        return list("FROM TargetGroup tg");
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listTrainingTopic()
     */
    @SuppressWarnings("unchecked")
    public List<TrainingTopic> listTrainingTopic() {

        return list("FROM TrainingTopic tt ORDER BY tt.name ASC");
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTargetGroupByCode(java.lang.String)
     */
    public TargetGroup findTargetGroupByCode(String code) {

        return (TargetGroup) find("FROM TargetGroup tg WHERE tg.code = ?", code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTargetGroupByName(java.lang.String)
     */
    public TargetGroup findTargetGroupByName(String name) {

        return (TargetGroup) find("FROM TargetGroup tg WHERE tg.name = ?", name);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTrainingTopicByCode(java.lang.String)
     */
    public TrainingTopic findTrainingTopicByCode(String code) {

        return (TrainingTopic) find("FROM TrainingTopic tp WHERE tp.code = ?", code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTrainingTopicByName(java.lang.String)
     */
    public TrainingTopic findTrainingTopicByName(String name) {

        return (TrainingTopic) find("FROM TrainingTopic tp WHERE tp.name = ?", name);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listTopic()
     */
    @SuppressWarnings("unchecked")
    public List<Topic> listTopic() {

        return list("FROM Topic t");
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listTrainingMethod()
     */
    @SuppressWarnings("unchecked")
    public List<TrainingMethod> listTrainingMethod() {

        return list("FROM TrainingMethod tm");
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findFarmerTrainingByCode(java.lang.String)
     */
    public FarmerTraining findFarmerTrainingByCode(String code) {

        return (FarmerTraining) find("FROM FarmerTraining ft WHERE ft.code = ?", code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTargetGroupById(long)
     */
    public TargetGroup findTargetGroupById(long id) {

        return (TargetGroup) find("FROM TargetGroup tg WHERE tg.id = ?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTopicByCode(java.lang.String)
     */
    public Topic findTopicByCode(String code) {

        return (Topic) find("FROM Topic t WHERE t.code = ?", code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTopicById(long)
     */
    public Topic findTopicById(long id) {

        return (Topic) find("FROM Topic t WHERE t.id = ?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTopicByName(java.lang.String)
     */
    public Topic findTopicByName(String name) {

        return (Topic) find("FROM Topic t WHERE t.name = ?", name);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTrainingMethodByCode(java.lang.String)
     */
    public TrainingMethod findTrainingMethodByCode(String code) {

        return (TrainingMethod) find("FROM TrainingMethod tm WHERE tm.code = ?", code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTrainingMethodById(long)
     */
    public TrainingMethod findTrainingMethodById(long id) {

        return (TrainingMethod) find("FROM TrainingMethod tm WHERE tm.id = ?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTrainingMethodByName(java.lang.String)
     */
    public TrainingMethod findTrainingMethodByName(String name) {

        return (TrainingMethod) find("FROM TrainingMethod tm WHERE tm.name = ?", name);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTrainingTopicById(long)
     */
    public TrainingTopic findTrainingTopicById(long id) {

        return (TrainingTopic) find(" FROM TrainingTopic tt WHERE tt.id = ?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTopicCategoryById(java.lang.Long)
     */
    public TopicCategory findTopicCategoryById(Long id) {

        TopicCategory category = (TopicCategory) find("FROM TopicCategory tc WHERE tc.id = ?", id);
        return category;

    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTopicCategoryByCode(java.lang.String)
     */
    public TopicCategory findTopicCategoryByCode(String code) {

        TopicCategory category = (TopicCategory) find("FROM TopicCategory tc WHERE tc.code = ?",
                code);
        return category;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTopicCategoryByName(java.lang.String)
     */
    public TopicCategory findTopicCategoryByName(String name) {

        TopicCategory category = (TopicCategory) find("FROM TopicCategory tc WHERE tc.name = ?",
                name);
        return category;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listTopicCategory()
     */
    public List<TopicCategory> listTopicCategory() {

        return (List<TopicCategory>) list("FROM TopicCategory tc ORDER BY tc.name ASC");
    }

    /**
     * Find topic category by id.
     * @param id the id
     * @return the topic category
     */
    public TopicCategory findTopicCategoryById(long id) {

        return (TopicCategory) find("FROM TopicCategory tc WHERE tc.id=?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listFarmerTrainingByTopic(long)
     */
    @SuppressWarnings("unchecked")
    public List<FarmerTraining> listFarmerTrainingByTopic(long id) {

        return (List<FarmerTraining>) list(
                "SELECT ft FROM FarmerTraining ft INNER JOIN ft.topics t WHERE t.id = ?", id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.txn.ITrainingDAO#findFarmerTrainingSelectionWithTrainingTopic(java
     * .lang.Long)
     */
    @SuppressWarnings("unchecked")
    public boolean findFarmerTrainingSelectionWithTrainingTopic(Long id) {

        boolean isTrainingTopicExistForFarmerSelection = false;
        List<FarmerTraining> farmerTraining = list(
                "FROM FarmerTraining ft WHERE ft.trainingTopic.id=?", id);
        if (farmerTraining.size() > 0) {
            isTrainingTopicExistForFarmerSelection = true;
        }
        return isTrainingTopicExistForFarmerSelection;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listFarmerTrainingByTrainingMethod(long)
     */
    @SuppressWarnings("unchecked")
    public List<FarmerTraining> listFarmerTrainingByTrainingMethod(long id) {

        return (List<FarmerTraining>) list(
                "SELECT ft FROM FarmerTraining ft INNER JOIN ft.trainingMethods tm WHERE tm.id = ?",
                id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listFarmerTrainingByTargetGroup(long)
     */
    @SuppressWarnings("unchecked")
    public List<FarmerTraining> listFarmerTrainingByTargetGroup(long id) {

        return (List<FarmerTraining>) list(
                "SELECT ft FROM FarmerTraining ft INNER JOIN ft.targetGroups tg WHERE tg.id = ?",
                id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listTopicByRevNo(java.lang.Long)
     */
    public List<Topic> listTopicByRevNo(Long revisionNo) {

        return list("FROM Topic tp WHERE tp.revisionNo > ? ORDER BY tp.revisionNo DESC", revisionNo);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTrainingStatusByReceiptNo(java.lang.String)
     */
    public TrainingStatus findTrainingStatusByReceiptNo(String receiptNo) {

        return (TrainingStatus) find("FROM TrainingStatus ts WHERE ts.receiptNo = ? ", receiptNo);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listOfflineTrainingStatus()
     */
    public List<OfflineTrainingStatus> listOfflineTrainingStatus() {

        return list("FROM OfflineTrainingStatus ots WHERE ots.statusCode = 2");
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listFarmerTrainingByRevNo(java.lang.Long)
     */
    public List<FarmerTraining> listFarmerTrainingByRevNo(Long revisionNo) {

        return list(
                "FROM FarmerTraining ft WHERE ft.revisionNo > ? AND ft.status = 1 ORDER BY ft.revisionNo DESC",
                revisionNo);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTrainingStatusById(java.lang.Long)
     */
    public TrainingStatus findTrainingStatusById(Long id) {

        return (TrainingStatus) find("FROM TrainingStatus ts WHERE ts.id=?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listTrainingStatusLoctaion(long)
     */
    @SuppressWarnings("unchecked")
    public List<TrainingStatusLocation> listTrainingStatusLoctaion(long id) {

        return list("FROM TrainingStatusLocation tsl WHERE tsl.trainingStatus.id=?", id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.esesw.dao.txn.ITrainingDAO#loadTrainingStatusLocationImage(java.lang.Long)
     */
    public TrainingStatusLocation loadTrainingStatusLocationImage(Long id) {

        return (TrainingStatusLocation) find("FROM TrainingStatusLocation tsl WHERE tsl.id=?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findActiveFarmerTrainingById(long)
     */
    public FarmerTraining findActiveFarmerTrainingById(long farmerTrainingId) {

        Object[] values = { farmerTrainingId, FarmerTraining.FIELD_STAFF, FarmerTraining.ACTIVE };

        return (FarmerTraining) find(
                "FROM FarmerTraining ft WHERE ft.id=? AND (ft.status=? OR ft.status=?)", values);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#listTopicCategoryByRevNo(long)
     */
    public List<TopicCategory> listTopicCategoryByRevNo(long revNo) {

        return (List<TopicCategory>) list(
                "FROM TopicCategory tc WHERE tc.revisionNo > ? ORDER BY tc.revisionNo DESC", revNo);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findFarmerTrainingLatestRevisionNo()
     */
    public Long findFarmerTrainingLatestRevisionNo() {

        List<Long> revList = list("SELECT MAX(ft.revisionNo) FROM FarmerTraining ft WHERE ft.status = 1");
        if (!ObjectUtil.isListEmpty(revList) && !ObjectUtil.isEmpty(revList.get(0))) {
            return revList.get(0);
        }
        return 0l;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTopicCategoryLatestRevisionNo()
     */
    public Long findTopicCategoryLatestRevisionNo() {

        List<Long> revList = list("SELECT MAX(tc.revisionNo) FROM TopicCategory tc");
        if (!ObjectUtil.isListEmpty(revList) && !ObjectUtil.isEmpty(revList.get(0))) {
            return revList.get(0);
        }
        return 0l;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.esesw.dao.txn.ITrainingDAO#findTopicLatestRevisionNo()
     */
    public Long findTopicLatestRevisionNo() {

        List<Long> revList = list("SELECT MAX(tp.revisionNo) FROM Topic tp");
        if (!ObjectUtil.isListEmpty(revList) && !ObjectUtil.isEmpty(revList.get(0))) {
            return revList.get(0);
        }
        return 0l;
    }

    @Override
    public List<OfflineTrainingStatus> listOfflineTrainingStatus(String tenantId) {

        Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
        List<OfflineTrainingStatus> result = session.createQuery("FROM OfflineTrainingStatus ots WHERE ots.statusCode = 2")
                .list();
        session.flush();
        session.close();
        return result;
    }
    
    public TrainingStatus findTrainingStatusByReceiptNo(String receiptNo,String tenantId) {

        //return (TrainingStatus) find("FROM TrainingStatus ts WHERE ts.receiptNo = ? ", receiptNo);
        
        Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
        Query query = session.createQuery("FROM TrainingStatus ts WHERE ts.receiptNo = :receiptNo");
        query.setParameter("receiptNo", receiptNo);
        
        List<TrainingStatus> trainingStatusList = query.list();
        TrainingStatus trainingStatus = null;
        if (trainingStatusList.size() > 0) {
            trainingStatus = (TrainingStatus) trainingStatusList.get(0);
        }

        session.flush();
        session.close();
        return trainingStatus;
    }

    @Override
    public Topic findTopicByCode(String topicValue, String tenantId) {

        Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
        Query query = session.createQuery("FROM Topic t WHERE t.code = :topicValue");
        query.setParameter("topicValue", topicValue);
        
        List<Topic> topicList = query.list();
        Topic topic = null;
        if (topicList.size() > 0) {
            topic = (Topic) topicList.get(0);
        }

        session.flush();
        session.close();
        return topic;
    }

    @Override
    public FarmerTraining findFarmerTrainingByCode(String farmerTrainingCode, String tenantId) {

       // return (FarmerTraining) find("FROM FarmerTraining ft WHERE ft.code = ?", code);
        
        Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
        Query query = session.createQuery("FROM FarmerTraining ft WHERE ft.code = :farmerTrainingCode");
        query.setParameter("farmerTrainingCode", farmerTrainingCode);
        
        List<FarmerTraining> farmerTrainingList = query.list();
        FarmerTraining farmerTraining = null;
        if (farmerTrainingList.size() > 0) {
            farmerTraining = (FarmerTraining) farmerTrainingList.get(0);
        }

        session.flush();
        session.close();
        return farmerTraining;
        
        
    }

    @Override
    public void update(OfflineTrainingStatus offlineTrainingStatus, String tenantId) {

        Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
        sessions.update(offlineTrainingStatus);
        sessions.flush();
        sessions.close();
        
    }

	@Override
	public void saveTrainingStatus(TrainingStatus trainingStatus,String tenantId) {
		// TODO Auto-generated method stub
		Session sessions=getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(trainingStatus);
		sessions.flush();
		sessions.close();
	}
	
	@Override
	public Observations findObservationsById(Long id) {
		// TODO Auto-generated method stub
		return (Observations) find("FROM Observations obs WHERE obs.id=?",id);
	}
	

	public boolean listFarmerTrainingByObservations(long id) {
		boolean isTrainingTopicExistForFarmerSelection = false;
		 List<FarmerTraining> farmerTraining =  list("SELECT ft FROM FarmerTraining ft inner join ft.observations obs where obs.id=?",id);
		   if (farmerTraining.size() > 0) {
	            isTrainingTopicExistForFarmerSelection = true;
	        }
	        return isTrainingTopicExistForFarmerSelection;
	}
	@Override 
	public TrainingMaterial findTrainingMaterialByName(String name) {
		// TODO Auto-generated method stub
		return (TrainingMaterial) find("FROM TrainingMaterial tm where tm.name=?",name);
	}
	
	@Override
	public TrainingMaterial findTrainingMaterial(Long id) {
		// TODO Auto-generated method stub
		return (TrainingMaterial) find("FROM TrainingMaterial tm where tm.id=?",id);
	}

		public List<FarmerTraining> listFarmerTrainingByMaterialId(long id) {
			// TODO Auto-generated method stub
			return (List<FarmerTraining>) list("SELECT ft FROM FarmerTraining ft inner join ft.materials m where m.id=?",id);
		}
		
		@Override
		public List<FarmerTraining> listFarmerTrainingByAgentIdandRevNo(long id, String revisionNo) {
			// TODO Auto-generated method stub
			Session session=getSessionFactory().openSession();
			Query query=session.createSQLQuery("SELECT * from farmer_training ft WHERE REVISION_NO>=:revisionNo AND STATUS=:status"  ).addEntity(FarmerTraining.class);
			query.setParameter("revisionNo", revisionNo);
			query.setParameter("status", 1);
			List list=query.list();
			session.flush();
			session.close();
			return list;
		}


		@Override
		public TrainingMaterial findtrainingMaterialByCode(String code) {
			// TODO Auto-generated method stub
			return (TrainingMaterial) find("FROM TrainingMaterial tm where tm.code=?",code);
		}
		
		@Override
		public TrainingMaterial findtrainingMaterialByCode(String code, String tenantId) {
			Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
	        Query query = session.createQuery("FROM TrainingMaterial tm where tm.code= :code");
	        query.setParameter("code", code);
	        
	        List<TrainingMaterial> trainingMaterialList = query.list();
	        TrainingMaterial trainingMaterial = null;
	        if (trainingMaterialList.size() > 0) {
	        	trainingMaterial = (TrainingMaterial) trainingMaterialList.get(0);
	        }

	        session.flush();
	        session.close();
	        return trainingMaterial;
		}

		@Override
		public TrainingMethod findTrainingMethodByCode(String code, String tenant) {
			Session session = getSessionFactory().withOptions().tenantIdentifier(tenant).openSession();
	        Query query = session.createQuery("FROM TrainingMethod tm where tm.code= :code");
	        query.setParameter("code", code);
	        
	        List<TrainingMethod> trainingMethodList = query.list();
	        TrainingMethod trainingMethod = null;
	        if (trainingMethodList.size() > 0) {
	        	trainingMethod = (TrainingMethod) trainingMethodList.get(0);
	        }

	        session.flush();
	        session.close();
	        return trainingMethod;
		}

		@Override
		public Observations findtrainingObservationByCode(String code) {
			// TODO Auto-generated method stub
			return (Observations) find("FROM Observations obs WHERE obs.code=?",code);
		}
		
		@Override
		public Observations findtrainingObservationByCode(String code, String tenantId) {
			Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
	        Query query = session.createQuery("FROM Observations tm where tm.code= :code");
	        query.setParameter("code", code);
	        
	        List<Observations> observationsList = query.list();
	        Observations observations = null;
	        if (observationsList.size() > 0) {
	        	observations = (Observations) observationsList.get(0);
	        }

	        session.flush();
	        session.close();
	        return observations;
		}
		
		@Override
		public List<TrainingMaterial> listTrainingMaterials() {
			// TODO Auto-generated method stub
			return list("FROM TrainingMaterial tm");
		}

		@Override
		public List<Observations> listObservations() {
			// TODO Auto-generated method stub
			return list("FROM Observations obs");
		}

		@Override
		public List<FarmerTraining> listFarmerTrainingByCurrentWeek(int year, int month, int week,String branchId) {
			// TODO Auto-generated method stub
			Session session=getSessionFactory().openSession();
	        Query query = session.createSQLQuery("SELECT * FROM farmer_training ft INNER JOIN planner p ON p.FARMER_TRAINING_ID = ft.id WHERE ft.status=:status AND p.year=:year AND p.month=:month AND p.week=:week AND ft.BRANCH_ID=:branchId").addEntity(FarmerTraining.class);
	        query.setParameter("status", FarmerTraining.ACTIVE);
	        query.setParameter("year", year);
	        query.setParameter("month", month);
	        query.setParameter("week", week);
	        query.setParameter("branchId", branchId);
	        List list = query.list();
	        session.flush();
	        session.close();
			return list;
		}

		@Override
		public Planner findPlannerByMntYrWk(int year, int month, int week, long farmerTranId) {
			// TODO Auto-generated method stub
			Session session=getSessionFactory().openSession();
	        Query query = session.createQuery("FROM  Planner p  WHERE  p.year=:year AND p.month=:month AND p.week=:week AND p.farmerTraining.id=:farmerTranId");
	        query.setParameter("year", year);
	        query.setParameter("month", month);
	        query.setParameter("week", week);
	        query.setParameter("farmerTranId", farmerTranId);
	        List list = query.list();
	        Planner planner=null;
	        if (list.size() > 0) {
	        	planner = (Planner) list.get(0);
	        }
	        session.flush();
	        session.close();
			return planner;
		}

		@Override
		public void deletePlanner(long id) {
			// TODO Auto-generated method stub
			Session session=getSessionFactory().openSession();
	        Query query = session.createSQLQuery("delete FROM planner WHERE id=:id");
	        query.setParameter("id", id);
	        query.executeUpdate();
	       
		}

		@Override
		public TrainingTopic findTrainingTopicByNameBasedOnBranch(String name, String branchId) {
			// TODO Auto-generated method stub
			  Object[] values = { name, branchId};
			return (TrainingTopic) find("FROM TrainingTopic tp WHERE tp.name = ? AND tp.branchId=?", values);
		}

		@Override
		public List<TrainingStatus> findTrainingStatusByFarmerId(String farmerId) {

			Session session = getSessionFactory().openSession();
			Query query = session
					.createQuery("FROM TrainingStatus ts WHERE ts.farmerIds like:fId");
			
			query.setParameter("fId", "%" + farmerId + "%");
			List<TrainingStatus> codeList = query.list();
			session.flush();
			session.close();
			return codeList;
			
		}

		@Override
		public List<FarmerTraining> listActiveFarmerTraining() {
			 Object[] values = { FarmerTraining.FIELD_STAFF, FarmerTraining.ACTIVE };
		     return list("FROM FarmerTraining ft WHERE (ft.status=? OR ft.status=?)", values);
		}
}
