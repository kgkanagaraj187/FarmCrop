/*
 * TransactionService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.ITransactionDAO;
import com.sourcetrace.eses.entity.Transaction;

@Service
@Transactional
public class TransactionService implements ITransactionService {

    @Autowired
    private ITransactionDAO transactionDAO;

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.eses.service.ITransactionService#createTransaction(com.sourcetrace.eses.entity
     * .Transaction)
     */
    @Override
    public void createTransaction(Transaction transaction) {

        transactionDAO.save(transaction);
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.service.ITransactionService#findTransactionById(long)
     */
    @Override
    public Transaction findTransactionById(long id) {

        return transactionDAO.findTransactionById(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.eses.service.ITransactionService#editTransaction(com.sourcetrace.eses.entity
     * .Transaction)
     */
    @Override
    public void editTransaction(Transaction transaction) {

        transactionDAO.update(transaction);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.eses.service.ITransactionService#deleteTransaction(com.sourcetrace.eses.entity
     * .Transaction)
     */
    @Override
    public void deleteTransaction(Transaction transaction) {

        transactionDAO.delete(transaction);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.eses.service.ITransactionService#findTransactionByMsgNoAgentIdStatus(long,
     * java.lang.String, java.lang.String)
     */
    @Override
    public Transaction findTransactionByMsgNoAgentIdStatus(String msgNo, String agentId,
            String statusCode) {

        return transactionDAO.findTransactionByMsgNoAgentIdStatus(msgNo, agentId, statusCode);
    }

}
