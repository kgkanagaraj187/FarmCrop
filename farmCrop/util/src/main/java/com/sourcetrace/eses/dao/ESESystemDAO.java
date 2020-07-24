/*
 * ESESystemDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ese.entity.txn.mfi.InterestRateHistory;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.LoanInterest;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;

// TODO: Auto-generated Javadoc
/**
 * The Class ESESystemDAO.
 */
@Repository("systemDAO")
@Transactional
public class ESESystemDAO extends ESEDAO implements IESESystemDAO{
    
    @Autowired
    public ESESystemDAO(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }

	public ESESystem findESESystem() {
		ESESystem sys = (ESESystem) find("FROM ESESystem es WHERE es.id = ?", 1);
		return sys;
	}

	/**
	 * List prefernces.
	 * 
	 * @return the list< ese system>
	 */
	public List<ESESystem> listPrefernces() {

		return (List<ESESystem>) list("FROM ESESystem");
	}

	/**
	 * Find prefernce by id.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the eSE system
	 */
	public ESESystem findPrefernceById(String id) {
		ESESystem sys = (ESESystem) find("FROM ESESystem es WHERE es.id = ?", Integer.valueOf(id));
		return sys;
	}

	public ESESystem findPrefernceById(String id, String tenantId) 
	{
		/*ESESystem sys = (ESESystem) find("FROM ESESystem es WHERE es.id = ?", Integer.valueOf(id));
		return sys;*/
		
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM ESESystem es WHERE es.id = :id");
		query.setParameter("id", Integer.valueOf(id));
		
		/*List<ESESystem> sysList = query.list();
		ESESystem eseSystem = (ESESystem) sysList.get(0);*/
		
		List<ESESystem> sysList = query.list();
		
		ESESystem eseSystem = null;
		if (sysList.size() > 0) {
			eseSystem = (ESESystem) sysList.get(0);
		}

		session.flush();
		session.close();
		return eseSystem;
	}
	
	/**
	 * Find agent timer value.
	 * 
	 * @return the string
	 */
	public String findAgentTimerValue() {
		String queryString = "SELECT VAL FROM pref WHERE pref.NAME = 'AGENT_EXPIRY_TIMER'";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object> list = query.list();
		sessions.flush();
		sessions.close();
		if (!ObjectUtil.isListEmpty(list))
			return (String) list.get(0);
		return null;

	}

	public InterestRateHistory findLastInterestRateHistory() {
		Session session = getSessionFactory().openSession();
		InterestRateHistory interestRateHistory = (InterestRateHistory) session
				.createCriteria(InterestRateHistory.class).addOrder(Order.desc("id")).setMaxResults(1).uniqueResult();
		session.flush();
		session.close();
		return interestRateHistory;
	}

	public InterestRateHistory findLastInterestRateHistoryForExFarmers() {
		Session session = getSessionFactory().openSession();
		InterestRateHistory interestRateHistory = (InterestRateHistory) session
				.createCriteria(InterestRateHistory.class).add(Restrictions.eq("affectExistingFarmerBal", 1))
				.addOrder(Order.desc("id")).setMaxResults(1).uniqueResult();
		session.flush();
		session.close();
		return interestRateHistory;
	}

	public InterestRateHistory findLastInactiveInterestRateHistory() {
		Session session = getSessionFactory().openSession();
		InterestRateHistory interestRateHistory = (InterestRateHistory) session
				.createCriteria(InterestRateHistory.class).add(Restrictions.eq("isActive", 0))
				.addOrder(Order.desc("id")).setMaxResults(1).uniqueResult();
		session.flush();
		session.close();
		return interestRateHistory;
	}

	public InterestRateHistory findLastInactiveInterestRateHistoryForExFarmers() {
		Session session = getSessionFactory().openSession();
		InterestRateHistory interestRateHistory = (InterestRateHistory) session
				.createCriteria(InterestRateHistory.class).add(Restrictions.eq("affectExistingFarmerBal", 1))
				.add(Restrictions.eq("isActive", 0)).addOrder(Order.desc("id")).setMaxResults(1).uniqueResult();
		session.flush();
		session.close();
		return interestRateHistory;
	}

	public void addInterestRateHistory(InterestRateHistory interestRateHistory) {
		InterestRateHistory latestInterestRateHistory = findLastInterestRateHistory();
		if (!ObjectUtil.isEmpty(latestInterestRateHistory)) {
			latestInterestRateHistory.setIsActive(0);
			latestInterestRateHistory.setRevisionNo(DateUtil.getRevisionNumber());
			latestInterestRateHistory.setLastUpdateDt(new Date());
			latestInterestRateHistory.setUpdateUserName(interestRateHistory.getCreateUserName());
			update(latestInterestRateHistory);
		}
		interestRateHistory.setCreateDt(new Date());
		interestRateHistory.setRevisionNo(DateUtil.getRevisionNumber());
		save(interestRateHistory);
	}

	public List<InterestRateHistory> listInterestRateHistories() {
		return list("from InterestRateHistory ih order by ih.isActive desc");
	}

	@Override
	public String findPrefernceByName(String enableMultiProduct) {
		// TODO Auto-generated method stub
		String queryString = "SELECT VAL FROM pref WHERE pref.NAME ='"+enableMultiProduct+"' ";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object> list = query.list();
		sessions.flush();
		sessions.close();
		if (!ObjectUtil.isListEmpty(list))
			return (String) list.get(0);
		return null;
	}
	
	@Override
	public ESESystem findPrefernceByOrganisationId(String id) {
		ESESystem sys = (ESESystem) find("FROM ESESystem es WHERE es.name = ?", id);
		return sys;
	}

	@Override
	public List<Object[]> findAgroPrefernceDetailById(String id) {
		String queryString = "SELECT * FROM eses_agro.pref es WHERE es.ese_id ='" +id+"' ";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}
	@Override
	public List<Object[]> listPreferncesByName(String prefName) {
		// TODO Auto-generated method stub
		String queryString = "SELECT name,VAL FROM pref WHERE pref.NAME like '"+prefName+"%' ";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}
	public List<LoanInterest> listLoanList(){
		return list("from LoanInterest");
				
	}

	@Override
	public LoanInterest findRangeFromLoanInterest(long minRange,long maxRange) {
		// TODO Auto-generated method stub
	
		
		
		 Session session = getSessionFactory().getCurrentSession();
			String queryString = "SELECT * from loan_interest li WHERE li.MIN_RANGE < '"+minRange+"' AND '"+maxRange+"' < li.MAX_RANGE OR '"+minRange+"' BETWEEN li.MIN_RANGE AND li.MAX_RANGE  OR '"+maxRange+"' BETWEEN li.MIN_RANGE AND li.MAX_RANGE;";
			Query query = session.createSQLQuery(queryString).addEntity(LoanInterest.class);
			List<LoanInterest> list = query.list();
			if (list.size() > 0){
				return list.get(0);
				}
			else{
				return null;
				}
	}
}
