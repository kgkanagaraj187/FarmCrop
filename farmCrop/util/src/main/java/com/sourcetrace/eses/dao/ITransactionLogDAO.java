/*
 * ITransactionLogDAO.java
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

import com.sourcetrace.eses.util.log.TransactionLog;

public interface ITransactionLogDAO extends IESEDAO {

    /**
     * Find transaction log by id.
     * @param id the id
     * @return the transaction log
     */
    public TransactionLog findTransactionLogById(long id);

	public TransactionLog findTransactionLogByMsgNo(Date date, String serialNo, String msgNo);

	void updateTxnLog(TransactionLog transactionLog,String tenant);

	List<TransactionLog> listTransactionLogByStatus(Integer status,String tenant);
}
