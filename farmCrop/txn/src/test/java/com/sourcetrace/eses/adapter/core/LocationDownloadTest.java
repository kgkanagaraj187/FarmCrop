/*
 * LocationDownloadTest.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;

import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.txn.schema.Body;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.txn.schema.Response;

public class LocationDownloadTest {

    private static Logger LOGGER = Logger.getLogger(LocationDownloadTest.class);
    private static final String LOCALHOST = "http://localhost:9002/tserv/rs";
    private static final String format = "application/json";

    /**
     * The main method.
     * @param args the arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {

        WebClient client = WebClient.create(LOCALHOST);
        client.path("processRequest").accept(format).type(format);
        Head head = new Head();
        head.setSerialNo("123456");
        head.setAgentId("AG00001");
        head.setAgentToken("123456");
        head.setServPointId("");
        head.setTxnTime("2012-12-12 10:20:56");
        head.setTxnType(TransactionTypeProperties.LOCATION_DOWNLOAD);
        head.setOperType(TransactionProperties.REGULAR_TXN);
        head.setMode(TransactionProperties.ONLINE_MODE);
        head.setResentCount("0");
        head.setMsgNo("12345");

//        Map<String, String> dataMap = new HashMap<String, String>();
//        dataMap.put(TransactionProperties.LOCATION_DOWNLOAD_REVISION_NO, "0|0|0|0|0|0|0");

//        List<Data> dataList = CollectionUtil.mapToList(dataMap);
        Body body = new Body();
        Request request = new Request();
//        body.setData(dataList);

        request.setHead(head);
        request.setBody(body);

        try {
            Response response = client.post(request, Response.class);
            LOGGER.info(response.getStatus().getCode());
            LOGGER.info(response.getStatus().getMessage());
            System.out.println("result" + response.getStatus().getCode());
            System.out.println("result" + response.getStatus().getMessage());

            LOGGER.info("-----END------");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
