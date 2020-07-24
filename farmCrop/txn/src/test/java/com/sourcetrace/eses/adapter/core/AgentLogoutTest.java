/*
 * AgentLoginTest.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;

import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.txn.schema.Body;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.txn.schema.Response;
import com.sourcetrace.eses.util.DateUtil;

public class AgentLogoutTest {

    public static Logger LOGGER = Logger.getLogger(AgentLogoutTest.class);
    private static final String LOCALHOST = "http://localhost:8081/tserv/rs";
    private static final String format = "application/json";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_TIME_FORMAT);
    
    /**
     * The main method.
     * @param args the arguments
     * @throws Exception the exception
     */
    public static void main(String args[]) throws Exception {

        WebClient client = WebClient.create(LOCALHOST);
        client.path("processTxnRequest").accept(format).type(format);
        Head head = new Head();
        head.setVersionNo("1.00");
        head.setSerialNo("6ca09c0be1d3a99157a77e1c2c2605e6");
        head.setAgentId("2222");
        head.setAgentToken("F2431B0F510FDEB244FC86FD4D34DBAB");
        head.setServPointId("");
        head.setTxnTime(sdf.format(new Date()));
        head.setTxnType(TransactionTypeProperties.AGENT_LOGOUT);
        head.setOperType(TransactionProperties.REGULAR_TXN);
        head.setMode(TransactionProperties.ONLINE_MODE);
        head.setResentCount("0");
        head.setMsgNo("1234567");
        
        Body body = new Body();
        Request request = new Request();
        body.setData(null);

        request.setHead(head);
        request.setBody(body);
        
        Response response = client.post(request, Response.class);

        LOGGER.info(response.getStatus().getCode());
        LOGGER.info(response.getStatus().getMessage());
        System.out.println("Status Code : " + response.getStatus().getCode());
        System.out.println("Status Msg  : " + response.getStatus().getMessage());
    }

}
