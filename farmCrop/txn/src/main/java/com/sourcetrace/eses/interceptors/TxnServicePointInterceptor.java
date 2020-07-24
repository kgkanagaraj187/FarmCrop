/*
 * TxnServicePointInterceptor.java
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
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
import com.sourcetrace.eses.interceptor.TxnMessageUtil;
import com.sourcetrace.eses.service.IServicePointDAO;
import com.sourcetrace.eses.txn.exception.TxnFault;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ServicePoint;

@Component
public class TxnServicePointInterceptor extends AbstractPhaseInterceptor<Message> {

    @Autowired
    private ILocationDAO locationDAO;
    @Autowired
    private IAgentDAO agentDAO;
    @Autowired
    private IServicePointDAO servicePointDAO;

    /**
     * Instantiates a new txn service point interceptor.
     */
    public TxnServicePointInterceptor() {

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
        validateServicePoint(msg);
    }

    /**
     * Validate service point.
     * @param msg the msg
     */
    private void validateServicePoint(Message msg) {

        /** GET HEAD FROM MESSAGE **/
        Object head = TxnMessageUtil.getHead(msg);

        if (!ObjectUtil.isEmpty(head)) {

            BeanWrapper wrapper = new BeanWrapperImpl(head);

            /** VALIDATION FOR SERVICE PLACE **/
            String servicePlaceId = (String) wrapper
                    .getPropertyValue(ITxnMessageUtil.SERVICE_POINT_ID);

            String agentId = (String) wrapper.getPropertyValue(ITxnMessageUtil.AGENT_ID);

            if (StringUtil.isEmpty(servicePlaceId))
                throw new TxnFault(ITxnErrorCodes.EMPTY_SERV_POINT_ID);

            ServicePoint servicePoint = servicePointDAO.findServicePointByCode(servicePlaceId);
            // To check whether servicePoint is empty or not
            if (ObjectUtil.isEmpty(servicePoint)) {
                throw new TxnFault(ITxnErrorCodes.INVALID_SERVICE_POINT);
            }
            ServicePoint agentServicePoint = agentDAO
                    .findAgentsServicePointByAgentId(agentId);
            if (!ObjectUtil.isEmpty(agentServicePoint)
                    && !servicePoint.getCode().equalsIgnoreCase(
                            agentServicePoint.getCode())) {
                throw new TxnFault(
                        ITxnErrorCodes.AGENT_SERVICE_POINT_MAPPING_UNAVAILABLE);
            }
    }
    }

}