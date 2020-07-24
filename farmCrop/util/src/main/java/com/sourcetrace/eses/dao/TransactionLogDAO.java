/*
 * TransactionLogDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
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
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.util.log.TransactionLog;

@Repository
@Transactional
public class TransactionLogDAO extends ESEDAO implements ITransactionLogDAO {

    /**
     * Instantiates a new transaction log dao.
     * @param sessionFactory the session factory
     */
    @Autowired
    public TransactionLogDAO(SessionFactory sessionFactory) {

        this.setSessionFactory(sessionFactory);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.ITransactionLogDAO#findTransactionLogById(long)
     */
    @Override
    public TransactionLog findTransactionLogById(long id) {

        return (TransactionLog) find("FROM TransactionLog WHERE id=?", id);
    }

	@Override
	public TransactionLog findTransactionLogByMsgNo(Date date, String serialNo, String msgNo) {
		 Object[] values = { date, serialNo, msgNo };
		 TransactionLog transaction = (TransactionLog) find(
	                "FROM TransactionLog ts WHERE ts.txnTime = ? AND ts.serialNo = ? AND ts.msgNo = ?",
	                values);
	        return transaction;
	}
	
	

	@Override
	public List<TransactionLog> listTransactionLogByStatus(Integer status,String tenant) {
		
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenant).openSession();
		Query query = session.createQuery("FROM TransactionLog ts WHERE ts.status = :status order by id asc");
		query.setParameter("status", status);
		query.setMaxResults(50);
		List<TransactionLog> transaction = (List<TransactionLog>) query.list();
		session.flush();
		session.close();
		return transaction;
		
	}

	@Override
	public void updateTxnLog(TransactionLog transactionLog,String tenant) {

		Session session = null;
		Transaction txn = null;
		try {
			session = getHibernateTemplate().getSessionFactory().withOptions().tenantIdentifier(tenant).openSession();
			txn = session.beginTransaction();
			session.update(transactionLog);
			txn.commit();
			session.flush();
			session.close();
		} catch (Exception e) {
			if(txn!=null){
				txn.rollback();
				txn.commit();
			}
			if(session!=null){
				session.flush();
				session.close();
			}
		}
	
		
	}
}
