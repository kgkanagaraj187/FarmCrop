/*
 * PasswordChangeAdapterTest.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;

import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.txn.schema.Body;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.txn.schema.Response;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;

/**
 * @author PANNEER
 */
public class PasswordChangeAdapterTest {

    public static Logger LOGGER = Logger.getLogger(PasswordChangeAdapterTest.class);
    private static final String LOCALHOST = "http://localhost:9002/tserv/rs";
    private static final String format = "application/json";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.TXN_TIME_FORMAT);

    public static void main(String[] args) {

        WebClient client = WebClient.create(LOCALHOST);
        client.path("processTxnRequest").accept(format).type(format);
        Head head = new Head();
        head.setVersionNo("1.00");
        head.setSerialNo("76a68323e01423878f7af33969c4347a999");
        head.setAgentId("panneersts");
        head.setAgentToken("4139F1732A5C2E81F66D1F2C9E38FFE4");
        head.setServPointId("");
        head.setTxnTime(sdf.format(new Date()));
//        head.setTxnType(TransactionTypeProperties.PASSWORD_CHANGE);
        head.setOperType(TransactionProperties.REGULAR_TXN);
        head.setMode(TransactionProperties.ONLINE_MODE);
        head.setResentCount("0");
        head.setMsgNo("12345");

        Map<String, String> dataMap = new HashMap<String, String>();
//        dataMap.put(TransactionProperties.REMOTE_NEW_PASSWORD, "123!@#");
//        dataMap.put(TransactionProperties.REMOTE_NEW_PASSWORD_CREATION_DATE, sdf.format(new Date()));

        List<Data> dataList = CollectionUtil.mapToList(dataMap);
        Body body = new Body();
        Request request = new Request();
        body.setData(dataList);

        request.setHead(head);
        request.setBody(body);

        Response response = client.post(request, Response.class);

        LOGGER.info(response.getStatus().getCode());
        LOGGER.info(response.getStatus().getMessage());
        System.out.println("Status Code : " + response.getStatus().getCode());
        System.out.println("Status Msg  : " + response.getStatus().getMessage());
    }

}
