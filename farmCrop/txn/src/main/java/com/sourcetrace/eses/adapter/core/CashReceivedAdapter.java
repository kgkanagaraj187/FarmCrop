/*
 * PeriodicInspectionAdapter.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;

// TODO: Auto-generated Javadoc
@Component
public class CashReceivedAdapter implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(CashReceivedAdapter.class.getName());
@Autowired
    private IFarmerService farmerService;
@Autowired
    private IAgentService agentService;
@Autowired
    private IAccountService accountService;

    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** REQUEST DATA **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);

        /** VALIDATING REQUEST DATA **/
        String agentId = head.getAgentId();
        String serialNo = head.getSerialNo();
        String servPointId = head.getServPointId();
        String txnMode = head.getMode();
        LOGGER.info("AGENT ID : " + agentId);
        LOGGER.info("SERIAL NO : " + serialNo);
        LOGGER.info("SERVICE POINT ID : " + servPointId);
        LOGGER.info("TXN MODE: " + txnMode);
        
        String cashRecDate = (String) reqData.get(TxnEnrollmentProperties.CASH_RECEIVED_DATE);
        String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
        String cashRecMod = (String) reqData.get(TxnEnrollmentProperties.CASH_RECEIVED_MODE);
        String amount = (String) reqData.get(TxnEnrollmentProperties.AMOUNT);
        
        if(StringUtil.isEmpty(cashRecDate))
            throw new SwitchException(ITxnErrorCodes.EMPTY_CASH_RECEIVED_DATE);
        if(StringUtil.isEmpty(farmerId))
            throw new SwitchException(ITxnErrorCodes.EMPTY_FARMER_ID);
        if(StringUtil.isEmpty(cashRecMod))
            throw new SwitchException(ITxnErrorCodes.EMPTY_CASH_RECEIVED_MODE);
        if(StringUtil.isEmpty(amount))
            throw new SwitchException(ITxnErrorCodes.EMPTY_CASH_AMOUNT);
        
        Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
        if(ObjectUtil.isEmpty(farmer))
            throw new SwitchException(ITxnErrorCodes.INVALID_FARMER_ID);
        
        // ESEAccount farmerAccount = accountService.findAccountByProfileIdAndProfileType(profileId, farmerAccount);
        
        return null;
    }

    @Override
    public Map<?, ?> processVoid(Map<?, ?> reqData) {

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Gets the farmer service.
     * @return the farmer service
     */
    public IFarmerService getFarmerService() {

        return farmerService;
    }

    /**
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
    }

    /**
     * Gets the agent service.
     * @return the agent service
     */
    public IAgentService getAgentService() {

        return agentService;
    }

    /**
     * Sets the agent service.
     * @param agentService the new agent service
     */
    public void setAgentService(IAgentService agentService) {

        this.agentService = agentService;
    }

    /**
     * Gets the account service.
     * @return the account service
     */
    public IAccountService getAccountService() {

        return accountService;
    }

    /**
     * Sets the account service.
     * @param accountService the new account service
     */
    public void setAccountService(IAccountService accountService) {

        this.accountService = accountService;
    }

}
