/*
 * OfflineDistributionSchedulerTask.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ese.entity.util.ESESystem;
import com.google.gson.Gson;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.ITransactionLogService;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Response;
import com.sourcetrace.eses.txn.schema.Status;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.log.TransactionLog;

@Component
@Transactional
public class OfflineTxnScheduler {

	private static final Logger LOGGER = Logger.getLogger(OfflineTxnScheduler.class.getName());
	private static final String NOT_APPLICABLE = "N/A";
	private static final DateFormat TXN_DATE_FORMAT = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
	@Autowired
	private IFarmerService farmerService;
	private String LOCALHOST = "http://localhost:8081/agrotxn/rss";
	private static final String format = "application/json";
	@Autowired
	private ITransactionLogService eseReqResLogService;
	@Autowired
	private IPreferencesService preferncesService;
	@Resource(name = "datasources")
	private Map<String, DataSource> datasources;
	/**
	 * Sets the farmer service.
	 * 
	 * @param farmerService
	 *            the new farmer service
	 */
	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	/**
	 * Gets the farmer service.
	 * 
	 * @return the farmer service
	 */
	public IFarmerService getFarmerService() {

		return farmerService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	public void process() {
		Vector<String> tenantIds = new Vector<>(datasources.keySet());

		for (String tenantId : tenantIds) {
		List<TransactionLog> offlineDistributions = eseReqResLogService.listTransactionLogByStatus(11,tenantId);
		if (offlineDistributions!=null && !offlineDistributions.isEmpty()) {
			ESESystem eseSystem = preferncesService.findPrefernceById(ESESystem.SYSTEM_ESE,tenantId);
			if (!StringUtil.isEmpty(eseSystem.getPreferences().get(ESESystem.SCHEDULER_URL))) {
				LOCALHOST = eseSystem.getPreferences().get(ESESystem.SCHEDULER_URL);
			}
			 offlineDistributions.stream().sorted(Comparator.comparing(TransactionLog::getId)).forEach(txnLog ->{
				 
				List<Object> providers = new ArrayList<>();
				providers.add(new JSONProvider());

				WebClient client = WebClient.create(LOCALHOST, providers);
				client.path("processTxnRequestScheduler").accept(format).type(format);
				// String jsonObj = .substring(1,
				// txnLog.getRequestLog().length() -
				// 1);

				try {
					String jsonObj = txnLog.getRequestLog().replaceAll("\\\\\"", "\"");
					Response response = client.accept("application/json").post(jsonObj, Response.class);

					if (response.getStatus() != null && response.getStatus().getCode().equals("00")) {

						txnLog.setStatus(0);
					} else {
						txnLog.setStatus(11);
					}

					txnLog.setStatusCode(response.getStatus().getCode());
					txnLog.setStatusMsg(response.getStatus().getMessage());
					txnLog.setLastUpdatedDT(new Date());
					eseReqResLogService.updateTxnLog(txnLog,tenantId);

				} catch (Exception e) {
					Status status = new Status();
					if (e instanceof SwitchException) {
						SwitchException se = (SwitchException) e;
						status.setCode(se.getCode());
						status.setMessage(se.getMessage());
						LOGGER.error("Caught Switch Exception : " + se.getMessage());
					} else {
						status.setCode(TransactionProperties.SERVER_ERROR);
						status.setMessage(e.fillInStackTrace().toString());
						LOGGER.error("Caught Server Error : " + e.fillInStackTrace().toString());
						e.printStackTrace();
					}

					txnLog.setStatusCode(status.getCode());
					txnLog.setStatusMsg(status.getMessage());
					txnLog.setLastUpdatedDT(new Date());
					txnLog.setStatus(11);
					eseReqResLogService.updateTxnLog(txnLog,tenantId);

					System.out.println(txnLog.getId());
				}

			});
		}
		}

	}

	public static String toJson(Object obj) {

		String jsonString = "";
		try {
			jsonString = new Gson().toJson(obj);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}

}
