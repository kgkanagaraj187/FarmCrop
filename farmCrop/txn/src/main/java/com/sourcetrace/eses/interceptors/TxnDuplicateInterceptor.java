/*
 * TxnDuplicateInterceptor.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.interceptors;

import java.util.Date;
import java.util.Set;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.sourcetrace.eses.dao.IDeviceDAO;
import com.sourcetrace.eses.dao.ITransactionDAO;
import com.sourcetrace.eses.entity.Transaction;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
import com.sourcetrace.eses.interceptor.TxnMessageUtil;
import com.sourcetrace.eses.service.TxnProcessServiceImpl;
import com.sourcetrace.eses.txn.exception.TxnFault;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;

public class TxnDuplicateInterceptor extends AbstractPhaseInterceptor<Message> {

    private Set<String> msgNoSet;

    @Autowired
    private ITransactionDAO transactionDAO;
    @Autowired
    private IDeviceDAO deviceDAO;

    /**
     * Instantiates a new txn duplicate interceptor.
     */
    public TxnDuplicateInterceptor() {

        super(Phase.PRE_INVOKE);
    }

    /**
     * Sets the msg no set.
     * @param msgNoSet the new msg no set
     */
    public void setMsgNoSet(Set<String> msgNoSet) {

        this.msgNoSet = msgNoSet;
    }

    /**
     * Gets the msg no set.
     * @return the msg no set
     */
    public Set<String> getMsgNoSet() {

        return msgNoSet;
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
            validateDuplicate(msg);
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
     * Validate duplicate.
     * @param msg the msg
     */
    private void validateDuplicate(Message msg) {

        /** GET HEAD FROM MESSAGE **/
        Object head = TxnMessageUtil.getHead(msg);

        BeanWrapper wrapper = new BeanWrapperImpl(head);
        if (!ObjectUtil.isEmpty(head) && !((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE)).equals("400") ) {
   
            String txnDate = (String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TIME);
            String operType = (String) wrapper.getPropertyValue(ITxnMessageUtil.OPERATION_TYPE);
            String txnType = (String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE);
            String serialNo = (String) wrapper.getPropertyValue(ITxnMessageUtil.SERIAL_NO);
  

            Date date = DateUtil.getTransactionDate(txnDate);
            int resentCount = 0;
            String msgNo = "";
            try {
                resentCount = Integer.parseInt((String) wrapper
                        .getPropertyValue(ITxnMessageUtil.RESENT_COUNT));
                msgNo = (String) wrapper.getPropertyValue(ITxnMessageUtil.MSG_NO);
            } catch (Exception e) {
                throw new TxnFault(ITxnErrorCodes.ERR0R_WHILE_PROCESSING);
            }
            /** VALIDATION FOR DUPLICATON **/
            Transaction txn = transactionDAO.findDuplication(date, serialNo, msgNo);

            if (msgNoSet.contains(txnType)) { // txnType contain in msgNoSet
                if (!Transaction.OPERATIONTYPE_VOID.equalsIgnoreCase(operType)) { // Not Void
                    if (!ObjectUtil.isEmpty(txn) && txn.getResentCount() == resentCount) {
                        throw new TxnFault(ITxnErrorCodes.DUPLICATE_TXN);
                    }
                }
                // update device msgNo
                deviceDAO.updateMsgNo(serialNo, msgNo);
            }
        }

    }

}