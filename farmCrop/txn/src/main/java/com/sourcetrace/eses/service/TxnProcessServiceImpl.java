/*
 * TxnProcessServiceImpl.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.sourcetrace.eses.adapter.core.ProductsDownload;
import com.sourcetrace.eses.entity.Transaction;
import com.sourcetrace.eses.entity.TransactionHeader;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.TxnFault;
import com.sourcetrace.eses.txn.schema.Body;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.txn.schema.Response;
import com.sourcetrace.eses.txn.schema.Status;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.log.TransactionLog;

// TODO: Auto-generated Javadoc
@Component
@Path("/")
@Produces({ "application/json", "application/xml" })
@WebService(targetNamespace = "http://service.eses.sourcetrace.com/", endpointInterface = "com.sourcetrace.eses.service.ITxnProcessService", portName = "tservport", serviceName = "TxnProcessServiceImpl")
public class TxnProcessServiceImpl implements ITxnProcessService {

	private static final Logger LOGGER = Logger.getLogger(TxnProcessServiceImpl.class.getName());
	public static final String TXN_ID = "txnId";
	public static final String SERVER_ERROR = "SERVER_ERROR";

	private Map<String, ITxnAdapter> txnAdapterMap;

	private Transaction transaction;

	@Autowired
	private ITransactionService transactionService;
	@Autowired
	private IUniqueIDGenerator uniqueIDGenerator;
	@Autowired
	private ITransactionLogService transactionLogService;

	/**
	 * Set txn adapter map.
	 * 
	 * @param txnAdapterMap
	 *            the txn adapter map
	 */
	public void setTxnAdapterMap(Map<String, ITxnAdapter> txnAdapterMap) {

		this.txnAdapterMap = txnAdapterMap;
	}

	/**
	 * Gets the txn adapter.
	 * 
	 * @param txnType
	 *            the txn type
	 * @return the txn adapter
	 */
	public ITxnAdapter getTxnAdapter(String txnType) {

		LOGGER.info("Txn Type " + txnType);
		return txnAdapterMap.get(txnType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.service.ITxnProcessService#processRequest(com.
	 * sourcetrace.eses.txn. schema .Request)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@POST
	@Path("/processTxnRequest")
	@Consumes({ "application/json", "application/xml" })
	@WebMethod
	@CrossOrigin(origins = "*")
	public Response processRequest(Request request) {
		Response response = new Response();
		Map reqData = new HashMap();
		Map respData = new HashMap();
		Status status = new Status();
		saveTransaction(request);
		ITxnAdapter txnAdapter = getTxnAdapter(request.getHead().getTxnType());
		if (!ObjectUtil.isEmpty(txnAdapter)) {
			try {
				if (!ObjectUtil.isEmpty(request.getBody()) && !request.getBody().getData().isEmpty())
					reqData = CollectionUtil.listToMap(request.getBody());

				reqData.put(ITxnMessageUtil.HEAD, request.getHead());

				if (Transaction.OPERATIONTYPE_REGULAR.equalsIgnoreCase(request.getHead().getOperType())) {
					if (!StringUtil.isEmpty(transaction.getTxnCode()))
						respData.put(TXN_ID, transaction.getTxnCode());

					if(request.getHead().getTxnType().equals("500")){
						respData = 	processDynamicTxn(reqData,request);
					}else{
					respData = txnAdapter.process(reqData);
					}
				} else if (Transaction.OPERATIONTYPE_VOID.equalsIgnoreCase(request.getHead().getOperType())) {
					respData = txnAdapter.processVoid(reqData);
				}

				Body body = new Body();
				body.setData(CollectionUtil.mapToList(respData));
				response.setBody(body);
				response.setStatus(status);
				updateTransaction(status);
				updateTransactionLog(request.getTxnLogId(), status, Transaction.Status.SUCCESS.ordinal());
			} catch (Exception exception) {
				String exceptionDetailMsg = exception.getMessage();
				if (exception instanceof TxnFault) {
					TxnFault txnFault = (TxnFault) exception;
					status.setCode(txnFault.getCode());
					status.setMessage(txnFault.getMessage());
				} else {
					status.setCode(SERVER_ERROR);
					String exceptionDetailLog = exception.getMessage();
					if (StringUtil.isEmpty(exceptionDetailLog) || exceptionDetailLog.equalsIgnoreCase("null")) {
						// StringWriter sw = new StringWriter();
						// PrintWriter pw = new PrintWriter(sw);
						// exception.printStackTrace(pw);
						// exceptionDetailMsg = sw.toString();
						exceptionDetailMsg = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(exception);
						exceptionDetailLog = exception.toString();
					}
					status.setMessage(exceptionDetailLog);
				}
				updateTransaction(status);
				// Log Transaction and send response always success
				updateTransactionLog(request.getTxnLogId(), status, Transaction.Status.FAILED.ordinal());
				LOGGER.error("Exception Occurred in  : " + exceptionDetailMsg);
				throw new TxnFault(status.getCode(), status.getMessage());
				// response.setBody(new Body());
				// response.setStatus(new Status());
			}
		}
		return response;
	}

	private Map processDynamicTxn(Map reqData, Request request) {
		
		String txnType = (reqData.containsKey(TxnEnrollmentProperties.DYNAMIC_TXN_ID))
				? (String) reqData.get(TxnEnrollmentProperties.DYNAMIC_TXN_ID) : "";
				ITxnAdapter txnAdapter = getTxnAdapter(request.getHead().getTxnType());
		if(txnType.contains("-")){
			 txnAdapter = getTxnAdapter("501");
		}
		return(txnAdapter.process(reqData));
	}

	private void updateTransactionLog(long txnLogId, Status status, int txnSts) {

		try {
			TransactionLog transactionLog = transactionLogService.findTransactionLogById(txnLogId);
			if (!ObjectUtil.isEmpty(transactionLog)) {
				transactionLog.setStatus(txnSts);
				transactionLog.setStatusCode(status.getCode());
				transactionLog.setStatusMsg(status.getMessage());
				transactionLogService.updateTransactionLog(transactionLog);
			}
		} catch (Exception e) {
			LOGGER.info("Exception : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Save transaction.
	 * 
	 * @param request
	 *            the request
	 */
	private void saveTransaction(Request request) {

		if (!ObjectUtil.isEmpty(request) && !ObjectUtil.isEmpty(request.getHead())) {
			transaction = new Transaction();
			TransactionHeader transactionHeader = new TransactionHeader();
			transactionHeader.setSerialNo(request.getHead().getSerialNo());
			transactionHeader.setVersionNo(request.getHead().getVersionNo());
			transactionHeader.setAgentId(request.getHead().getAgentId());
			transactionHeader.setServPointId(request.getHead().getServPointId());

			transaction.setTxnTime(DateUtil.getTransactionDate(request.getHead().getTxnTime()));
			transaction.setTxnType(request.getHead().getTxnType());
			transaction.setTxnCode(getTxnIdSeq(request.getHead().getTxnType()));
			transaction.setOperType(request.getHead().getOperType()!=null ? Integer.valueOf(request.getHead().getOperType()) : 0);
			transaction.setMode(request.getHead().getMode()!=null ? Integer.valueOf(request.getHead().getMode()) : 0);
			try {
				if (!StringUtil.isEmpty(request.getHead().getResentCount()))
					transaction.setResentCount(Long.valueOf(request.getHead().getResentCount()));
				if (!StringUtil.isEmpty(request.getHead().getMsgNo()))
					transaction.setMsgNo(request.getHead().getMsgNo());
				if (!StringUtil.isEmpty(request.getHead().getBatchNo()))
					transaction.setBatchNo(Long.valueOf(request.getHead().getBatchNo()));
				if (!StringUtil.isEmpty(request.getHead().getBatchNo()))
					transaction.setBranchId(request.getHead().getBranchId());
			} catch (Exception e) {
				LOGGER.info(e.getMessage());
			}
			transaction.setStatusCode(Transaction.PENDING);
			transaction.setStatusMessage(Transaction.PENDING_MSG);
			transaction.setHeader(transactionHeader);

			transactionService.createTransaction(transaction);
		}
	}

	/**
	 * Update transaction.
	 * 
	 * @param status
	 *            the status
	 */
	private void updateTransaction(Status status) {

		if (!ObjectUtil.isEmpty(status) && !ObjectUtil.isEmpty(transaction)
				&& !StringUtil.isEmpty(transaction.getStatusCode())
				&& !StringUtil.isEmpty(transaction.getStatusMessage())) {
			transaction.setStatusCode(status.getCode());
			transaction.setStatusMessage(status.getMessage());
			transactionService.editTransaction(transaction);
		}
	}

	/**
	 * Gets the txn id seq.
	 * 
	 * @param txnType
	 *            the txn type
	 * @return the txn id seq
	 */
	private String getTxnIdSeq(String txnType) {

		return uniqueIDGenerator.getTxnIdSeq();
	}
}
