/*
 * AgentLogin.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.LoanInterest;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.txn.ESETxn;

/**
 * The Class AgentLogin.
 */
public class SyncLogin implements ITxnAdapter {

	SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.SYNC_DATE_TIME);
	private static final Logger LOGGER = Logger.getLogger(SyncLogin.class.getName());
	@Autowired
	private IAgentService agentService;

	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IClientService clientService;
	@Autowired
	private IPreferencesService preferncesService;
	private Map<String, ITxnAdapter> txnMap;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map reqData) {
		Map groupResponse = new LinkedHashMap<>();
		Map resp = new LinkedHashMap<>();
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		List<String> lognTxns = new ArrayList<>();
		Long farmerRevisionNo = 0l;
		Long farmRevisionNo = 0l;
		Long farmCropsRevisionNo = 0l;
		ESESystem eseSystem = preferncesService.findPrefernceById("1");
		Agent agent = agentService.findAgentByProfileAndBranchId(agentId, head.getBranchId());
		String seasonCode = clientService.findCurrentSeasonCodeByBranchId(head.getBranchId());
		farmerRevisionNo = farmerService.findActiveContractFarmersLatestRevisionNoByAgentAndSeason(agent.getId(),
				seasonCode);

	

		farmRevisionNo = farmerService.listFarmFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(agent.getId());

		farmCropsRevisionNo = farmerService
				.listFarmCropsFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(agent.getId());

		resp.put(TxnEnrollmentProperties.FARMER_DOWNLOAD_REVISION_NO, farmerRevisionNo);
		resp.put(TxnEnrollmentProperties.FARM_DOWNLOAD_REVISION_NO, farmRevisionNo);
		resp.put(TxnEnrollmentProperties.FARM_CROPS_DOWNLOAD_REVISION_NO, farmCropsRevisionNo);
		resp.put(TxnEnrollmentProperties.AGENT_REVISION_NO, agent.getRevisionNumber());
		resp.put(TxnEnrollmentProperties.CURRENT_SEASON_CODE_LOGIN, seasonCode);
		
		Collection collection = new Collection();
		List<Object> listOfSamithiObject = new ArrayList<Object>();
		if (!ObjectUtil.isListEmpty(agent.getWareHouses())) {
			for (Warehouse samithi : agent.getWareHouses()) {
				Data samithiCodeData = new Data();
				samithiCodeData.setKey(TxnEnrollmentProperties.SAMITHI_CODE);
				samithiCodeData.setValue(samithi.getCode());

				List<Data> samithiData = new ArrayList<Data>();
				samithiData.add(samithiCodeData);

				Object samithiObject = new Object();
				samithiObject.setData(samithiData);

				listOfSamithiObject.add(samithiObject);
			}
			collection.setObject(listOfSamithiObject);
		}
		resp.put(TxnEnrollmentProperties.SAMITHI_LIST, collection);
		
		// Loan Interest Collection
		/*if (eseSystem.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE).equals("1")) {
			Collection interestCollection = new Collection();
			List<Object> interestObject = new ArrayList<Object>();
			if (!ObjectUtil.isListEmpty(eseSystem.getLoanInterestDetails())) {
				for (LoanInterest loanInt : eseSystem.getLoanInterestDetails()) {
					Data minRange = new Data();
					minRange.setKey(TxnEnrollmentProperties.MIN_RANGE);
					minRange.setValue(String.valueOf(loanInt.getMinRange()));

					Data maxRange = new Data();
					maxRange.setKey(TxnEnrollmentProperties.MAX_RANGE);
					maxRange.setValue(String.valueOf(loanInt.getMaxRange()));

					Data interest = new Data();
					interest.setKey(TxnEnrollmentProperties.LOAN_PERCENTAGE);
					interest.setValue(String.valueOf(loanInt.getInterest()));

					List<Data> InterestData = new ArrayList<Data>();
					InterestData.add(minRange);
					InterestData.add(maxRange);
					InterestData.add(interest);

					Object interestObjectVal = new Object();
					interestObjectVal.setData(InterestData);

					interestObject.add(interestObjectVal);
				}
				interestCollection.setObject(interestObject);
			}
			resp.put(TxnEnrollmentProperties.INTEREST_LIST, interestCollection);
		}*/
				
				

		groupResponse.put(TxnEnrollmentProperties.AGENT_LOGIN_RESP_KEY, buildCollection(resp));

		try {
			lognTxns = agentService.listSyncLogins();
		} catch (Exception e) {

		}

		groupDownloadTransactionsResponses(reqData, groupResponse, lognTxns);
		
		agentService.updateAgentBODStatus(agent.getProfileId(), ESETxn.BOD);
		return groupResponse;
	}

	/**
	 * Group download transactions responses.
	 * 
	 * @param req
	 *            the req
	 * @param res
	 *            the res
	 */
	@SuppressWarnings("unchecked")
	private void groupDownloadTransactionsResponses(Map req, Map res, List<String> lognTxns) {

		if (lognTxns.isEmpty()) {
			if (!ObjectUtil.isEmpty(txnMap)) {
				for (Map.Entry txnAdapterEntry : txnMap.entrySet()) {
					ITxnAdapter txnAdapter = (ITxnAdapter) txnAdapterEntry.getValue();
					if (!ObjectUtil.isEmpty(txnAdapter)) {
						Map txnAdapterResp = txnAdapter.process(req);
						res.put(txnAdapterEntry.getKey(), buildCollection(txnAdapterResp));
					}
				}
			}
		} else {
			for (String txnAdapterEntry : lognTxns) {
				ITxnAdapter txnAdapter = (ITxnAdapter) txnMap.get(txnAdapterEntry);
				if (!ObjectUtil.isEmpty(txnAdapter)) {
					Map txnAdapterResp = txnAdapter.process(req);
					res.put(txnAdapterEntry, buildCollection(txnAdapterResp));
				}
			}
		}

	}

	/**
	 * Builds the collection.
	 * 
	 * @param res
	 *            the res
	 * @return the collection
	 */
	@SuppressWarnings("unchecked")
	private Collection buildCollection(Map res) {

		Collection respCollection = new Collection();
		Object respObject = new Object();
		List<Object> respObjectList = new ArrayList<Object>();
		respObject.setData(CollectionUtil.mapToList(res));
		respObjectList.add(respObject);
		respCollection.setObject(respObjectList);
		return respCollection;
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
	 * Gets the txn map.
	 * 
	 * @return the txn map
	 */
	public Map<String, ITxnAdapter> getTxnMap() {

		return txnMap;
	}

	/**
	 * Sets the txn map.
	 * 
	 * @param txnMap
	 *            the txn map
	 */
	public void setTxnMap(Map<String, ITxnAdapter> txnMap) {

		this.txnMap = txnMap;
	}
}
