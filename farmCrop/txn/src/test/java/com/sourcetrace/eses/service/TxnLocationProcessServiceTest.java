/*
 * TxnLocationProcessServiceTest.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;

import com.sourcetrace.eses.txn.schema.Locate;

public class TxnLocationProcessServiceTest {

    public static Logger LOGGER = Logger.getLogger(TxnLocationProcessServiceTest.class);
    private static final String LOCALHOST = "http://localhost:8081/tserv/rsl";
    private static final String format = "application/json";

    public static void main(String[] args) {

        WebClient client = WebClient.create(LOCALHOST);
        client.path("/locationTracking").accept(format).type(format);
        Locate locate = new Locate();
        locate.setSerialNo("2d76e9c1785b41ea60b86441835cb134");
        locate.setTxnTime("2017-01-10 10:20:56");
        locate.setLatitude("1.121212");
        locate.setLongitude("1.124543");
        locate.setTenantId("apmas");

        Response response = client.post(locate);

        LOGGER.info(response);
    }

}
