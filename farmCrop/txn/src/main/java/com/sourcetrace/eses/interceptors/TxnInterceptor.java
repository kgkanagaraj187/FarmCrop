/*
 * TxnInterceptor.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.interceptors;

import java.util.Map;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.dao.ITransactionDAO;
import com.sourcetrace.eses.entity.Transaction;
import com.sourcetrace.eses.entity.TransactionType;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
import com.sourcetrace.eses.interceptor.TxnMessageUtil;
import com.sourcetrace.eses.service.TxnProcessServiceImpl;
import com.sourcetrace.eses.txn.exception.TxnFault;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

@SuppressWarnings("unused")
public class TxnInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Logger LOGGER = Logger.getLogger(TxnInterceptor.class.getName());
    @Autowired
    private TxnAgentInterceptor txnAgentInterceptor;
    @Autowired
    private TxnDeviceInterceptor txnDeviceInterceptor;
    @Autowired
    private TxnServicePointInterceptor txnServicePointInterceptor;
    @Autowired
    private TxnCredentialInterceptor txnCredentialInterceptor;
    @Autowired
    private TxnDuplicateInterceptor txnDuplicateInterceptor;

    private Map<String, String> txnTypeValidationMap;

    @Autowired
    private ITransactionDAO transactionDAO;

    /**
     * Instantiates a new txn interceptor.
     */
    public TxnInterceptor() {

        super(Phase.PRE_INVOKE);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message .Message)
     */
    @Override
    public void handleMessage(Message msg) throws Fault {

        String qString = (String) msg.get(Message.QUERY_STRING);
        if (qString != null && qString.contains("_wadl")) {
            return;
        }

        // Exception catch handled to trac the request for logging in response failure
        try {
            validateHead(msg);
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
     * Validate head.
     * @param msg the msg
     */
    private void validateHead(Message msg) {

        Object head = TxnMessageUtil.getHead(msg);

        BeanWrapper wrapper = new BeanWrapperImpl(head);
        if (!ObjectUtil.isEmpty(head) && !((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE)).equals("400") ) {
             String txnTime = (String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TIME);
            String txnType = (String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE);
            String operationType = (String) wrapper
                    .getPropertyValue(ITxnMessageUtil.OPERATION_TYPE);
            String mode = (String) wrapper.getPropertyValue(ITxnMessageUtil.MODE);

            LOGGER.info("-------- Header Validation --------");
            LOGGER.info("Txn Time       : " + txnTime);
            LOGGER.info("Txn Type       : " + txnType);
            LOGGER.info("Operation Type : " + operationType);
            LOGGER.info("Mode           : " + mode);

            // Transaction type validation
            if (StringUtil.isEmpty(txnType))
                throw new TxnFault(ITxnErrorCodes.TXN_TYPE_EMPTY);

            TransactionType trxnType = transactionDAO.findTxnTypeByCode(txnType);
            if (ObjectUtil.isEmpty(trxnType))
                throw new TxnFault(ITxnErrorCodes.TXN_TYPE_UNAVAILABLE);

      

            if (!(Transaction.OPERATIONTYPE_REGULAR.equalsIgnoreCase(operationType)
                    || Transaction.OPERATIONTYPE_VOID.equalsIgnoreCase(operationType)))
                throw new TxnFault(ITxnErrorCodes.INVALID_OPERATION_TYPE);
            
            // If txnType is "adminTxn", Then remove service point & duplicate interceptor
            if (ITxnMessageUtil.ADMIN_TXN
                    .equalsIgnoreCase(getTxnTypeValidationMap().get(txnType))) {
                msg.getInterceptorChain().remove(txnServicePointInterceptor);
                msg.getInterceptorChain().remove(txnDuplicateInterceptor);
            }
        }
    }

    /**
     * Gets the txn type validation map.
     * @return the txn type validation map
     */
    public Map<String, String> getTxnTypeValidationMap() {

        return txnTypeValidationMap;
    }

    /**
     * Sets the txn type validation map.
     * @param txnTypeValidationMap the txn type validation map
     */
    public void setTxnTypeValidationMap(Map<String, String> txnTypeValidationMap) {

        this.txnTypeValidationMap = txnTypeValidationMap;
    }

}
