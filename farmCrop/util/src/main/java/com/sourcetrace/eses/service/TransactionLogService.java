/*
 * TransactionLogService.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.ITransactionLogDAO;
import com.sourcetrace.eses.util.log.TransactionLog;

@Service
@Transactional
public class TransactionLogService implements ITransactionLogService {

    @Autowired
    private ITransactionLogDAO transactionLogDAO;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.service.ITransactionLogService#findTransactionLogById(long)
     */
    @Override
    public TransactionLog findTransactionLogById(long id) {

        return transactionLogDAO.findTransactionLogById(id);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.eses.service.ITransactionLogService#addTransactionLog(com.sourcetrace.eses
     * .util.log.TransactionLog)
     */
    @Override
    public void addTransactionLog(TransactionLog transactionLog) {

        transactionLogDAO.save(transactionLog);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sourcetrace.eses.service.ITransactionLogService#updateTransactionLog(com.sourcetrace.
     * eses.util.log.TransactionLog)
     */
    @Override
    public void updateTransactionLog(TransactionLog transactionLog) {

        transactionLogDAO.update(transactionLog);
    }

	@Override
	public TransactionLog findTransactionLogByMsgNo(Date date, String serialNo, String msgNo) {
	return   transactionLogDAO.findTransactionLogByMsgNo(date,serialNo,msgNo);
	}
	@Override
	public void updateTxnLog(TransactionLog transactionLog,String tenant){
		transactionLogDAO.updateTxnLog(transactionLog,tenant);
	}
	@Override
	public List<TransactionLog> listTransactionLogByStatus(Integer status,String tenant){
		return transactionLogDAO.listTransactionLogByStatus(status,tenant);
	}

}
