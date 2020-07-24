/*
 * ITransactionService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import com.sourcetrace.eses.entity.Transaction;

public interface ITransactionService {

    /**
     * Creates the transaction.
     * @param transaction the transaction
     */
    public void createTransaction(Transaction transaction);

    /**
     * Find transaction by id.
     * @param id the id
     * @return the transaction
     */
    public Transaction findTransactionById(long id);

    /**
     * Edits the transaction.
     * @param transaction the transaction
     */
    public void editTransaction(Transaction transaction);

    /**
     * Delete transaction.
     * @param transaction the transaction
     */
    public void deleteTransaction(Transaction transaction);

    /**
     * Find transaction by msg no agent id status.
     * @param msgNo the msg no
     * @param agentId the agent id
     * @param statusCode the status code
     * @return the transaction
     */
    public Transaction findTransactionByMsgNoAgentIdStatus(String msgNo, String agentId,
            String statusCode);
}
