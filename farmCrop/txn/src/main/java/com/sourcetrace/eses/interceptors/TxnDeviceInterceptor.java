/*
 * TxnDeviceInterceptor.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.interceptors;

import java.util.Date;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
import com.sourcetrace.eses.interceptor.TxnMessageUtil;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.service.TxnProcessServiceImpl;
import com.sourcetrace.eses.txn.exception.TxnFault;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

@Component
public class TxnDeviceInterceptor extends AbstractPhaseInterceptor<Message> {

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IUniqueIDGenerator idGenerator;

    public TxnDeviceInterceptor() {

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
            validateDevice(msg);
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
     * Validate device.
     * @param msg the msg
     */
    private void validateDevice(Message msg) {

        /** FETCHING HEAD FROM MESSAGE **/
        Object head = TxnMessageUtil.getHead(msg);
        BeanWrapper wrapper = new BeanWrapperImpl(head);
        if (!ObjectUtil.isEmpty(head) && !((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE)).equals("400") ) {
   
            /** VALIDATION FOR DEVICE **/
            String serialNo = (String) wrapper.getPropertyValue(ITxnMessageUtil.SERIAL_NO);
            String agentId = (String) wrapper.getPropertyValue(ITxnMessageUtil.AGENT_ID);

            if (StringUtil.isEmpty(serialNo))
                throw new TxnFault(ITxnErrorCodes.EMPTY_SERIAL_NO);



//            if (StringUtil.isEmpty(serialNo)) {
//                throw new TxnFault(ITxnErrorCodes.EMPTY_SERIAL_NO);
//            }
            // To Find Device By Serial Number
            Device device = deviceService.findDeviceBySerialNumber(serialNo);
            // To Find Device is Available or not
//            if (ObjectUtil.isEmpty(device)) {
//                throw new TxnFault(ITxnErrorCodes.INVALID_DEVICE);
//            }
            
            if (ObjectUtil.isEmpty(device)
                    || device.getIsRegistered() == Device.DEVICE_NOT_REGISTERED) {
                if (ObjectUtil.isEmpty(device)) {
                    Device newDevice = new Device();
                    newDevice.setSerialNumber(serialNo);
                    newDevice.setDeviceType(Device.MOBILE_DEVICE);
                    newDevice.setEnabled(false);
                    newDevice.setDeleted(false);
                    newDevice.setCode(idGenerator.getDeviceIdSeq());
                    newDevice.setCreatedTime(new Date());
                    newDevice.setModifiedTime(new Date());
                    newDevice.setIsRegistered(Device.DEVICE_NOT_REGISTERED);
                    newDevice.setName(agentId);
                    deviceService.addDevice(newDevice);
                } else {
                    device.setDeviceType(Device.MOBILE_DEVICE);
                    device.setEnabled(false);
                    device.setDeleted(false);
                    device.setModifiedTime(new Date());
                    device.setIsRegistered(Device.DEVICE_NOT_REGISTERED);
                    deviceService.updateDevice(device);
                }
                throw new TxnFault(ITxnErrorCodes.INVALID_DEVICE);
            }else{
            	   String logintime = (String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TIME);
                  String appversion = (String) wrapper.getPropertyValue(ITxnMessageUtil.VERSION_NO);
                   String appvers = appversion.replace("|",".");
                   
                   device.setLogintime(logintime);
                   device.setAppversion(appvers);
                   deviceService.updateDevice(device);
            }
            
            
            if (!device.isEnabled()) {
                throw new TxnFault(ITxnErrorCodes.DEVICE_DISABLED);
            }
            // To check whether the device is mapped with agent or not
            if (ObjectUtil.isEmpty(device.getAgent())) {
                throw new TxnFault(
                        ITxnErrorCodes.AGENT_DEVICE_MAPPING_UNAVAILABLE);
            } else if (!agentId.equalsIgnoreCase(device.getAgent().getProfileId())) {
                throw new TxnFault(
                        ITxnErrorCodes.AGENT_DEVICE_MAPPING_UNAUTHORIZED);
            }
        }
    }

}