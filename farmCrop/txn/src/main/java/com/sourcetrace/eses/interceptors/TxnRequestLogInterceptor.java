/*
 * TxnRequestLogInterceptor.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.interceptors;

import java.util.Date;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.dao.ITransactionDAO;
import com.sourcetrace.eses.entity.Transaction;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
import com.sourcetrace.eses.interceptor.TxnMessageUtil;
import com.sourcetrace.eses.service.ITransactionLogService;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.log.TransactionLog;

@Component
public class TxnRequestLogInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final Logger LOGGER = Logger.getLogger(TxnRequestLogInterceptor.class);

	@Autowired
	private ITransactionLogService transactionLogService;
	   @Autowired
	    private ITransactionDAO transactionDAO;

	/**
	 * Instantiates a new txn request log interceptor.
	 */
	public TxnRequestLogInterceptor() {

		super(Phase.PRE_INVOKE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.
	 * message .Message)
	 */
	@Override
	public void handleMessage(Message msg) throws Fault {

		String qString = (String) msg.get(Message.QUERY_STRING);
		if (qString != null && qString.contains("_wadl")) {
			return;
		}
		LOGGER.info("-------- Begins TxnRequestLogInterceptor --------");
		try {
			Head head = (Head) TxnMessageUtil.getHead(msg);
			  Date date = DateUtil.getTransactionDate(head.getTxnTime());
			  TransactionLog transactionLog = (TransactionLog) msg.getContent(TransactionLog.class);
			transactionLog.setRequestLog(transactionLog.getRequestLogStringBuffer().toString());
			TransactionLog txn = transactionLogService.findTransactionLogByMsgNo(date, head.getSerialNo(), head.getMsgNo());
			if (!ObjectUtil.isEmpty(head) && ObjectUtil.isEmpty(txn)) {
				transactionLog.setTxnType(head.getTxnType());
				transactionLog.setSerialNo(head.getSerialNo());
				transactionLog.setMsgNo(head.getMsgNo());
				transactionLog.setResentCount(head.getResentCount());
				transactionLog.setAgentId(head.getAgentId());
				transactionLog.setOperationType(head.getOperType());
				transactionLog.setMode(head.getMode());
				transactionLog.setVersion(head.getVersionNo());
				transactionLog.setBranchId(head.getBranchId());
				try {
				
					transactionLog
							.setTxnTime(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_TIME_FORMAT));
				} catch (Exception e) {
					e.printStackTrace();
				}
				transactionLog.setStatusCode(Transaction.PENDING);
				transactionLog.setStatusMsg(Transaction.PENDING_MSG);
				transactionLog.setStatus(Transaction.Status.PENDING.ordinal());
				transactionLog.setCreatedDT(new Date());
				transactionLogService.addTransactionLog(transactionLog);
			}else{
				transactionLog = txn;
			}
		

			Object reqObject = TxnMessageUtil.getRequest(msg);
			if (!ObjectUtil.isEmpty(reqObject) && reqObject instanceof Request) {
				Request request = (Request) reqObject;
				request.setTxnLogId(transactionLog.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Exception :" + e.getMessage());
		}
		LOGGER.info("-------- Ends TxnRequestLogInterceptor --------");
	}
}
