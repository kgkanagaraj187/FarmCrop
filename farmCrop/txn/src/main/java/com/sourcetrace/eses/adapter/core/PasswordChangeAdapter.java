/*
 * PasswordChangeAdapter.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.TxnFault;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.crypto.ICryptoUtil;

/**
 * @author PANNEER
 */
@Component
public class PasswordChangeAdapter implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(PasswordChangeAdapter.class.getName());

    @Autowired
    private IAgentService agentService;
    @Autowired
    private ICryptoUtil cryptoUtil;

    @SuppressWarnings("rawtypes")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        LOGGER.info("Password Change Txn Start !!! ");

        /** GET REQUEST DATA **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);

        if (StringUtil.isEmpty((String) reqData.get(TxnEnrollmentProperties.REMOTE_NEW_PASSWORD))) {
            throw new TxnFault(ITxnErrorCodes.EMPTY_REMOTE_NEW_PASSWORD);
        }
        if (StringUtil.isEmpty(
                (String) reqData.get(TxnEnrollmentProperties.REMOTE_PASSWORD_CHANGE_DATE))) {
            throw new TxnFault(ITxnErrorCodes.EMPTY_REMOTE_PASSWORD_DATE);
        }
        Agent agent = agentService.findAgentByAgentId(head.getAgentId());
        if (!ObjectUtil.isEmpty(agent)) {
            // agent.setPassword(cryptoUtil.encrypt(StringUtil.getMulipleOfEight(agent.getProfileId()+(String)
            // reqData.get(TransactionProperties.REMOTE_NEW_PASSWORD))));
            agent.setPassword((String) reqData.get(TxnEnrollmentProperties.REMOTE_NEW_PASSWORD));
            agent.setUpdateTime(new Date());
            agentService.editAgent(agent);
        }

        LOGGER.info("Password Change Txn Start !!! ");

        Map resp = new HashMap();
        return resp;
    }

    @Override
    public Map<?, ?> processVoid(Map<?, ?> reqData) {

        return null;
    }
}
