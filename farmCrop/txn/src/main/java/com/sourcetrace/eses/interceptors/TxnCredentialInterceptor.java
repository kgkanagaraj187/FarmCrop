/*
 * TxnCredentialInterceptor.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.interceptors;

import java.util.Map;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.dao.IAgentDAO;
import com.sourcetrace.eses.dao.IDeviceDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
import com.sourcetrace.eses.interceptor.TxnMessageUtil;
import com.sourcetrace.eses.service.TxnProcessServiceImpl;
import com.sourcetrace.eses.txn.exception.TxnFault;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.crypto.ICryptoUtil;

public class TxnCredentialInterceptor extends AbstractPhaseInterceptor<Message> {

	@Autowired
	private IDeviceDAO deviceDAO;
	@Autowired
	private IAgentDAO agentDAO;
	@Autowired
	private ICryptoUtil cryptoUtil;

	private Map<String, String> txnCredMap;
	private static final String AGENT_TOKEN = "token";
	private static final String AGENT_CARD = "card";
	private static final String AGENT_BOTH = "token/card";
	private static final String POS = "POS";
	private static final String MOBILE = "MOBILE";

	/**
	 * Instantiates a new txn credential interceptor.
	 */
	public TxnCredentialInterceptor() {

		super(Phase.PRE_INVOKE);
	}

	/**
	 * Sets the txn cred map.
	 * 
	 * @param txnCredMap
	 *            the txn cred map
	 */
	public void setTxnCredMap(Map<String, String> txnCredMap) {

		this.txnCredMap = txnCredMap;
	}

	/**
	 * Gets the txn cred map.
	 * 
	 * @return the txn cred map
	 */
	public Map<String, String> getTxnCredMap() {

		return txnCredMap;
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

		// Exception catch handled to trac the request for logging in response
		// failure
		try {
			validateCredential(msg);
		} catch (Exception e) {
			TxnFault txnFault = null;
			if (e instanceof TxnFault) {
				txnFault = (TxnFault) e;
			} else {
				txnFault = new TxnFault(TxnProcessServiceImpl.SERVER_ERROR, e.getMessage());
			}
			txnFault.setTxnLogId(TxnMessageUtil.getTxnLogId(msg));
			throw txnFault;
		}
	}

	/**
	 * Validate credential.
	 * 
	 * @param msg
	 *            the msg
	 */
	private void validateCredential(Message msg) {

		Object head = TxnMessageUtil.getHead(msg);

	     BeanWrapper wrapper = new BeanWrapperImpl(head);
	        if (!ObjectUtil.isEmpty(head) && !((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE)).equals("400") ) {
	   		String txnType = (String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE);
			String agentId = (String) wrapper.getPropertyValue(ITxnMessageUtil.AGENT_ID);
			String serialNo = (String) wrapper.getPropertyValue(ITxnMessageUtil.SERIAL_NO);
			Device device = deviceDAO.findDeviceBySerialNumber(serialNo);
			String agentCreToken = (String) wrapper.getPropertyValue(ITxnMessageUtil.AGENT_TOKEN);
			String versionNo = (String) wrapper.getPropertyValue(ITxnMessageUtil.VERSION_NO);

			Agent agent = agentDAO.findAgentByProfileAndBranchId(agentId, device.getBranchId());

			/** FETCH AGENT TOKEN **/
			String agentToken = txnCredMap.get(txnType);
			/** FETCH DEVICE TYPE **/
			String deviceType = device.getDeviceType();

			if (StringUtil.isEmpty(versionNo)) {
				agentCreToken = cryptoUtil.encrypt(StringUtil.getMulipleOfEight(agent.getProfileId() + agentCreToken));
			}

			if ((AGENT_BOTH.equalsIgnoreCase(agentToken) || AGENT_TOKEN.equalsIgnoreCase(agentToken))
					&& MOBILE.equalsIgnoreCase(deviceType)) {
				if (agent.getPassword() == null || agentToken == null || !agent.getPassword().equals(agentCreToken)) {
					throw new TxnFault(ITxnErrorCodes.INVALID_CREDENTIAL);
				}
			} else if (AGENT_BOTH.equalsIgnoreCase(agentToken) && POS.equalsIgnoreCase(deviceType)
					|| AGENT_CARD.equalsIgnoreCase(agentToken)) {

			}

			Request requestData =  (Request) ReflectUtil.getCurrentTxnRequestData();
			if (!ObjectUtil.isEmpty(requestData)) {
				requestData.getHead().setBranchId(device.getBranchId());
			}
		}
	}

}