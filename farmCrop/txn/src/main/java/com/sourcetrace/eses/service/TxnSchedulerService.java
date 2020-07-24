/*
 * ESETxnProcessorService.java
 * Copyright (c) 2016-2017, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Body;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.txn.schema.Response;
import com.sourcetrace.eses.txn.schema.Status;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.excep.SwitchException;


// TODO: Auto-generated Javadoc
public class TxnSchedulerService implements ITxnSchedulerService {

	private static final Logger LOGGER = Logger.getLogger(TxnSchedulerService.class);
	private Map<String, ITxnAdapter> txnAdapterMap;

	@SuppressWarnings("unchecked")
	@Override
	public Response processTxnRequestScheduler(Request request) throws Exception {

		Response response = new Response();
		ESETxn eseTxn = new ESETxn();
		Map reqData = new HashMap<String, Object>();
		Map respData = new HashMap<String, Object>();
		Status status = new Status();
		status.setCode("00");
		status.setMessage("SUCCESS");
		ITxnAdapter txnAdapter = getTxnAdapter(request.getHead().getTxnType());

		if (request.getBody() != null && !request.getBody().getData().isEmpty())
			reqData = CollectionUtil.listToMap(request.getBody());
		// put head object, txn id and txn log id into reqData
		reqData.put(TransactionProperties.HEAD, request.getHead());
		reqData.put(TransactionProperties.TXN_ID, eseTxn.getTxnId());
		reqData.put(TransactionProperties.TXN_LOG_ID, request.getTxnLogId());
		if (Integer.parseInt(request.getHead().getOperType()) == ESETxn.ON_LINE) {
			try {
				respData = txnAdapter.process(reqData);
			
			} catch (Exception e) {
				 if (e instanceof SwitchException) {
		                SwitchException se = (SwitchException) e;
		                status.setCode(se.getCode());
		                status.setMessage(se.getMessage());
		                LOGGER.error("Caught Switch Exception : " + se.getMessage());
		            } else {
		                status.setCode(TransactionProperties.SERVER_ERROR);
		                status.setMessage(e.fillInStackTrace().toString());
		                LOGGER.error("Caught Server Error : "
		                        + e.fillInStackTrace().toString());
		                e.printStackTrace();
		            }
			}
		} else if (Integer.parseInt(request.getHead().getOperType()) == ESETxn.VOID) {
			respData = txnAdapter.processVoid(reqData);
		}

		// response part
		Body body = new Body();
		body.setData(CollectionUtil.mapToList(respData));
		response.setBody(body);
		response.setStatus(status);
		return response;
	}

	/**
	 * Validated empty.
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	private String validatedEmpty(String value) {

		if (StringUtil.isEmpty(value))
			return "0";
		else
			return value.trim();
	}

	/**
	 * Serialize.
	 * 
	 * @param obj
	 *            the obj
	 * @return the byte[]
	 */
	public static byte[] serialize(Object obj) {

		ByteArrayOutputStream out = null;
		ObjectOutputStream os = null;
		try {
			out = new ByteArrayOutputStream();
			os = new ObjectOutputStream(out);
			os.writeObject(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * To json.
	 * 
	 * @param obj
	 *            the obj
	 * @return the string
	 */
	public static String toJson(Object obj) {

		String jsonString = "";
		try {
			jsonString = new Gson().toJson(obj);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
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

	public Map<String, ITxnAdapter> getTxnAdapterMap() {
		return txnAdapterMap;
	}

	public void setTxnAdapterMap(Map<String, ITxnAdapter> txnAdapterMap) {
		this.txnAdapterMap = txnAdapterMap;
	}

}
