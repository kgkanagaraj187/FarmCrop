/*
 * ITransactionDAO.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.Date;

import com.sourcetrace.eses.entity.Transaction;
import com.sourcetrace.eses.entity.TransactionType;

public interface ITransactionDAO extends IESEDAO {

    /**
     * Find txn type by code.
     * @param code the code
     * @return the transaction type
     */
    public TransactionType findTxnTypeByCode(String code);

    /**
     * Find duplication.
     * @param txnTime the txn time
     * @param serialNo the serial no
     * @param msgNo the msg no
     * @return the transaction
     */
    public Transaction findDuplication(Date txnTime, String serialNo, String msgNo);

    /**
     * Find transaction by id.
     * @param id the id
     * @return the transaction
     */
    public Transaction findTransactionById(long id);

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
