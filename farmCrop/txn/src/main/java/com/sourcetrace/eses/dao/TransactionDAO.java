/*
 * TransactionDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.entity.Transaction;
import com.sourcetrace.eses.entity.TransactionType;

@Repository
public class TransactionDAO extends ESEDAO implements ITransactionDAO {

    /**
     * Instantiates a new transaction dao.
     * @param sessionFactory the session factory
     */
    @Autowired
    public TransactionDAO(SessionFactory sessionFactory) {

        this.setSessionFactory(sessionFactory);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.ITxnDAO#findTxnTypeByCode(java.lang.String)
     */
    @Override
    public TransactionType findTxnTypeByCode(String code) {

        return (TransactionType) find("From TransactionType txnType where txnType.code=?", code);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.ITransactionDAO#findDuplication(java.util.Date,
     * java.lang.String, java.lang.String)
     */
    @Override
    public Transaction findDuplication(Date txnTime, String serialNo, String msgNo) {

        Object[] values = { txnTime, serialNo, msgNo };
        Transaction transaction = (Transaction) find(
                "FROM Transaction ts WHERE ts.txnTime = ? AND ts.header.serialNo = ? AND ts.msgNo = ?",
                values);
        return transaction;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.ITransactionDAO#findTransactionById(long)
     */
    @Override
    public Transaction findTransactionById(long id) {

        return (Transaction) find("FROM Transaction txn WHERE txn.id = ?", id);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.dao.ITransactionDAO#findTransactionByMsgNoAgentIdStatus(long,
     * java.lang.String, java.lang.String)
     */
    @Override
    public Transaction findTransactionByMsgNoAgentIdStatus(String msgNo, String agentId,
            String statusCode) {

        return (Transaction) find(
                "FROM Transaction txn WHERE txn.msgNo=? AND txn.header.agentId=? AND txn.status.code=?",
                new Object[] { msgNo, agentId, statusCode });
    }

}
