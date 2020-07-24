/*
 * TxnMessageUtil.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.interceptor;

import java.util.List;

import org.apache.cxf.message.Message;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.util.ObjectUtil;

/**
 * @author Saravanan
 */
public abstract class TxnMessageUtil implements ITxnMessageUtil {

    /**
     * Gets the head.
     * @param msg the msg
     * @return the head
     */
    public static Object getHead(Message msg) {

        @SuppressWarnings("rawtypes")
        List list = msg.getContent(List.class);
        Object reqObject = null;
        if (list == null) {
            reqObject = msg.getContent(Object.class);
        } else {
            reqObject = list.get(0);
        }

        Object header = null;
        BeanWrapper req = new BeanWrapperImpl(reqObject);
        header = req.getPropertyValue("head");

        return header;
    }

    /**
     * Gets the body.
     * @param mesg the mesg
     * @return the body
     */
    public static Object getBody(Message mesg) {

        @SuppressWarnings("rawtypes")
        List list = mesg.getContent(List.class);
        Object reqObject = null;
        if (list == null) {
            reqObject = mesg.getContent(Object.class);
        } else {
            reqObject = list.get(0);
        }

        Object datas = null;
        BeanWrapper req = new BeanWrapperImpl(reqObject);
        datas = req.getPropertyValue("body");

        return datas;
    }

    /**
     * Gets the request.
     * @param mesg the mesg
     * @return the request
     */
    public static Object getRequest(Message mesg) {

        @SuppressWarnings("rawtypes")
        List list = mesg.getContent(List.class);
        Object reqObject = null;
        if (list == null) {
            reqObject = mesg.getContent(Object.class);
        } else {
            reqObject = list.get(0);
        }
        return reqObject;
    }

    /**
     * Gets the txn log id.
     * @param mesg the mesg
     * @return the txn log id
     */
    public static long getTxnLogId(Message mesg) {
        
        Object requestObject = getRequest(mesg);
        if(!ObjectUtil.isEmpty(requestObject)){
            Request request = (Request) requestObject;
            return request.getTxnLogId();
        }
        return 0;
    }
}
