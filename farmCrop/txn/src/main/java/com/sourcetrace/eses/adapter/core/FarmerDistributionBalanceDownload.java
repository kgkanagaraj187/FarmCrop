/*
 * FarmerOutstandingBalanceDownload.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

@Component
public class FarmerDistributionBalanceDownload implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(FarmerDistributionBalanceDownload.class.getName());
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private IPreferencesService preferencesService;
	@Autowired
	private IDeviceService deviceService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		LOGGER.info("----------Farmer Outstanding BalanceDownload Start ----------");
		/** REQUEST DATA **/
		Head head = (Head) reqData.get(TransactionProperties.HEAD);

		/** VALIDATE AGENT DATA **/
		String agentId = head.getAgentId();
		Device device = deviceService.findDeviceBySerialNumber(head.getSerialNo());
		Agent agent = agentService.findAgentByProfileAndBranchId(agentId, device.getBranchId());
		String strevisionNo = (String) reqData.get(TxnEnrollmentProperties.DIST_STOCK_REV_NO);
		if (StringUtil.isEmpty(strevisionNo)){
			strevisionNo="0";
		}
			

		LOGGER.info("STOCK REVISION NO" + strevisionNo);
		/** FORM RESPONSE DATA **/
		Map resp = new HashMap();
		List<java.lang.Object[]> distBalanceList = farmerService.listDistributionBalanceDownload(agent.getId(),
				strevisionNo);
		Collection balCollection = new Collection();
		List<Object> listOfDist = new ArrayList<Object>();
		if (!ObjectUtil.isListEmpty(distBalanceList)) {
			int i = 0;
			for (java.lang.Object[] farmer : distBalanceList) {
				try {
					Data farmerIdData = new Data();
					farmerIdData.setKey(TxnEnrollmentProperties.FARMER_ID);
					farmerIdData.setValue(farmer[0].toString());

					Data farmerNameData = new Data();
					farmerNameData.setKey(TxnEnrollmentProperties.WAREHOUSE_CATEGORY_CODE);
					farmerNameData.setValue(farmer[1] != null ? farmer[1].toString() : "");

					Data farmerOutstandingData = new Data();
					farmerOutstandingData.setKey(TxnEnrollmentProperties.WAREHOUSE_PRODUCT_CODE);
					farmerOutstandingData.setValue(farmer[2] != null ? farmer[2].toString() : "");

					Data stock = new Data();
					stock.setKey(TxnEnrollmentProperties.STOCK);
					stock.setValue(farmer[3] != null ? farmer[3].toString() : "");

					List<Data> farmerDataList = new ArrayList<Data>();
					farmerDataList.add(farmerIdData);
					farmerDataList.add(farmerNameData);
					farmerDataList.add(farmerOutstandingData);
					farmerDataList.add(stock);

					Object farmerCreditObject = new Object();
					farmerCreditObject.setData(farmerDataList);

					listOfDist.add(farmerCreditObject);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		balCollection.setObject(listOfDist);
		if (!ObjectUtil.isListEmpty(distBalanceList)) {
			try {
				strevisionNo = distBalanceList.get(0)[4].toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		resp.put(TxnEnrollmentProperties.DIST_STOCK_REV_NO, strevisionNo);
		resp.put(TxnEnrollmentProperties.DIST_STOCK_LIST, balCollection);
		return resp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.txn.adapter.ITxnAdapter#processVoid(java.util.Map)
	 */
	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {

		return null;
	}

	/**
	 * Sets the account service.
	 * 
	 * @param accountService
	 *            the new account service
	 */
	public void setAccountService(IAccountService accountService) {

		this.accountService = accountService;
	}

	/**
	 * Gets the account service.
	 * 
	 * @return the account service
	 */
	public IAccountService getAccountService() {

		return accountService;
	}

	/**
	 * Gets the agent service.
	 * 
	 * @return the agent service
	 */
	public IAgentService getAgentService() {

		return agentService;
	}

	/**
	 * Sets the agent service.
	 * 
	 * @param agentService
	 *            the new agent service
	 */
	public void setAgentService(IAgentService agentService) {

		this.agentService = agentService;
	}

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

	/**
	 * Gets the product distribution service.
	 * 
	 * @return the product distribution service
	 */
	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	/**
	 * Sets the product distribution service.
	 * 
	 * @param productDistributionService
	 *            the new product distribution service
	 */
	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	public IPreferencesService getPreferencesService() {

		return preferencesService;
	}

	public void setPreferencesService(IPreferencesService preferencesService) {

		this.preferencesService = preferencesService;
	}

}
