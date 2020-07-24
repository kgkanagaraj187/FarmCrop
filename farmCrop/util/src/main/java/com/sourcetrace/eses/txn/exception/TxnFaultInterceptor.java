/*
 * TxnFaultInterceptor.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.txn.exception;

import java.io.OutputStream;
import java.util.Iterator;

import javax.ws.rs.core.MediaType;
import javax.xml.stream.XMLStreamWriter;

import org.apache.cxf.binding.xml.interceptor.XMLFaultOutInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.StaxOutEndingInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.service.ITransactionLogService;
import com.sourcetrace.eses.txn.schema.Response;
import com.sourcetrace.eses.txn.schema.Status;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.log.TransactionLog;

public class TxnFaultInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final String APPLICATION_JSON_UTF_8 = "application/json;charset=UTF-8";

    private static final Logger LOGGER = Logger.getLogger(TxnFaultInterceptor.class.getName());

    @Autowired
    private ITransactionLogService transactionLogService;

    /**
     * Instantiates a new txn fault interceptor.
     */
    public TxnFaultInterceptor() {

        super(Phase.MARSHAL);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
     */
    @Override
    public void handleMessage(Message message) throws Fault {

        for (Iterator<Interceptor<? extends Message>> iterator = message.getInterceptorChain()
                .getIterator(); iterator.hasNext();) {
            Interceptor<? extends Message> interceptor = iterator.next();
            if (interceptor instanceof XMLFaultOutInterceptor) {
                message.getInterceptorChain().remove(interceptor);
            } else if (interceptor instanceof StaxOutEndingInterceptor) {
                message.getInterceptorChain().remove(interceptor);
            }
        }

        Fault fault = (Fault) message.getContent(Exception.class);

        if (fault != null) {
            if (fault.getCause() instanceof TxnFault) {
                TxnFault se = (TxnFault) fault.getCause();
                com.sourcetrace.eses.txn.schema.Response errorResponse = se.getFaultInfo();
                if (errorResponse.getTxnLogId() > 0) {
                    updateTransactionLog(errorResponse.getTxnLogId(), errorResponse.getStatus(), 2);
                }
                String content = (String) message.getExchange().getInMessage()
                        .get(Message.CONTENT_TYPE);
                content = content.replaceAll("\\s", "");
                if (content.equals(MediaType.APPLICATION_JSON)
                        || content.equals(APPLICATION_JSON_UTF_8)) {
                    sendJSON(message, errorResponse);
                } else {
                    sendXML(message, errorResponse);
                }
            }
        }
    }

    private void updateTransactionLog(long txnLogId, Status status, int txnSts) {

        try {
            TransactionLog transactionLog = transactionLogService.findTransactionLogById(txnLogId);
            if (!ObjectUtil.isEmpty(transactionLog)) {
                transactionLog.setStatus(txnSts);
                transactionLog.setStatusCode(status.getCode());
                transactionLog.setStatusMsg(status.getMessage());
                transactionLogService.updateTransactionLog(transactionLog);
                // Changed error status to success for default success response
                // status.setCode(Status.SUCCESS_CODE);
                // status.setMessage(Status.SUCCESS_MSG);
            }
        } catch (Exception e) {
            LOGGER.info("Exception : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendXML(Message message, Response errorResponse) {

        message.put(Message.CONTENT_TYPE, MediaType.APPLICATION_XML);
        XMLStreamWriter writer = message.getContent(XMLStreamWriter.class);
        try {
            writer.writeStartElement("Response");
            writer.writeStartElement("status");
            writer.writeStartElement("code");
            writer.writeCharacters(errorResponse.getStatus().getCode());
            writer.writeEndElement();
            writer.writeStartElement("message");
            writer.writeCharacters(errorResponse.getStatus().getMessage());
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEndElement();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendJSON(Message message, Response errorResponse) {

        message.put(Message.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        OutputStream os = (OutputStream) message.getContent(OutputStream.class);

        try {
            JSONObject error = new JSONObject();
            error.put("code", errorResponse.getStatus().getCode());
            error.put("message", errorResponse.getStatus().getMessage());
            JSONObject obj = new JSONObject();
            obj.put("status", error);
            JSONObject tcfJSON = new JSONObject();
            tcfJSON.put("Response", obj);
            os.write(tcfJSON.toString().getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
