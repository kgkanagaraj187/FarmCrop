/*
 * TxnAgentInterceptor.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.interceptors;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.dao.IAgentDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
import com.sourcetrace.eses.interceptor.TxnMessageUtil;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.TxnProcessServiceImpl;
import com.sourcetrace.eses.txn.exception.TxnFault;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

@Component
public class TxnAgentInterceptor extends AbstractPhaseInterceptor<Message> {

    @Autowired
    private IAgentDAO agentDAO;
    
    @Autowired
    private IDeviceService deviceService;

    /**
     * Instantiates a new txn agent interceptor.
     */
    public TxnAgentInterceptor() {

        super(Phase.PRE_INVOKE);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
     */
    @Override
    public void handleMessage(Message msg) throws Fault {

        String qString = (String) msg.get(Message.QUERY_STRING);
        if (qString != null && qString.contains("_wadl")) {
            return;
        }

        // Exception catch handled to trac the request for logging in response failure
        try {
            validateAgent(msg);
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
     * Validate agent.
     * @param msg the msg
     */
    private void validateAgent(Message msg) {

        /** GET HEAD FROM MESSAGE **/
        Object head = TxnMessageUtil.getHead(msg);
        BeanWrapper wrapper = new BeanWrapperImpl(head);
        if (!ObjectUtil.isEmpty(head) && !((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE)).equals("400") ) {
           

            /** VALIDATION FOR AGENT **/
            String agentId = (String) wrapper.getPropertyValue(ITxnMessageUtil.AGENT_ID);
            String serialNumber = (String) wrapper.getPropertyValue(ITxnMessageUtil.SERIAL_NO);
            Device device = deviceService.findDeviceBySerialNumber(serialNumber);
            if (StringUtil.isEmpty(agentId))
                throw new TxnFault(ITxnErrorCodes.AGENT_ID_EMPTY);
            
            Agent agent = agentDAO.findAgentByProfileAndBranchId(agentId, device.getBranchId());
            if (ObjectUtil.isEmpty(agent))
                throw new TxnFault(ITxnErrorCodes.INVALID_AGENT);

            if (agent.getStatus() == Profile.INACTIVE)
                throw new TxnFault(ITxnErrorCodes.AGENT_DISABLED);

        }

    }

}