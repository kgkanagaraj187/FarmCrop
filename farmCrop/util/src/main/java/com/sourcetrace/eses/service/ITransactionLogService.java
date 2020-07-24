/*
 * ITransactionLogService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.Date;
import java.util.List;

import com.sourcetrace.eses.util.log.TransactionLog;

public interface ITransactionLogService {

    /**
     * Find transaction log by id.
     * @param id the id
     * @return the transaction log
     */
    public TransactionLog findTransactionLogById(long id);

    /**
     * Adds the transaction log.
     * @param transactionLog the transaction log
     */
    public void addTransactionLog(TransactionLog transactionLog);

    /**
     * Update transaction log.
     * @param transactionLog the transaction log
     */
    public void updateTransactionLog(TransactionLog transactionLog);

	public TransactionLog findTransactionLogByMsgNo(Date date, String serialNo, String msgNo);
	
	public void updateTxnLog(TransactionLog transactionLog,String tenant);

	public List<TransactionLog> listTransactionLogByStatus(Integer status,String tenant);
}
