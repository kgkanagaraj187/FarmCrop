/*
 * AgentMovementAdapter.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.AgentMovement;
import com.sourcetrace.eses.order.entity.txn.AgentMovementLocation;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.txn.ESETxn;
@Component
public class AgentMovementAdapter implements ITxnAdapter {

    private static Logger LOGGER = Logger.getLogger(AgentMovementAdapter.class);
    private static final String AGENT_MOVEMENT = "AGENT MOVEMENT";
@Autowired
    private IAgentService agentService;
@Autowired
    private IDeviceService deviceService;
@Autowired
    private IFarmerService farmerService;
@Autowired
    private IServicePointService servicePointService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** GET REQUEST DATA **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);
        String agentId = head.getAgentId();
        String serialNo = head.getSerialNo();
        String servPointId = head.getServPointId();

        String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
        String farmCode = (String) reqData.get(TxnEnrollmentProperties.FARM_CODE);
        String purpose = (String) reqData.get(TxnEnrollmentProperties.PURPOSE);
        String remarks = (String) reqData.get(TxnEnrollmentProperties.REMARKS);

        /** VALIDATING REQUEST DATA **/
        Agent agent = agentService.findAgentByAgentId(agentId);
        if (ObjectUtil.isEmpty(agent)) {
            throw new SwitchException(ITxnErrorCodes.INVALID_AGENT);
        }
        Device device = deviceService.findDeviceBySerialNumber(serialNo);
        if (ObjectUtil.isEmpty(device)) {
            throw new SwitchException(ITxnErrorCodes.INVALID_DEVICE);
        }
        ServicePoint servicePoint = servicePointService.findServicePointByServPointId(servPointId);
        if (ObjectUtil.isEmpty(servicePoint)) {
            throw new SwitchException(ITxnErrorCodes.INVALID_SERVICE_POINT);
        }

        if (StringUtil.isEmpty(farmerId)) {
            throw new SwitchException(ITxnErrorCodes.EMPTY_FARMER_ID);
        }
        Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
        if (ObjectUtil.isEmpty(farmer)) {
            throw new SwitchException(ITxnErrorCodes.FARMER_NOT_EXIST);
        }
        if (StringUtil.isEmpty(farmCode)) {
            throw new SwitchException(ITxnErrorCodes.EMPTY_FARM_CODE);
        }
        Farm farm = farmerService.findFarmByCode(farmCode);
        if (ObjectUtil.isEmpty(farmer)) {
            throw new SwitchException(ITxnErrorCodes.FARM_NOT_EXIST);
        }
        if (!farmer.equals(farm.getFarmer())) {
            throw new SwitchException(ITxnErrorCodes.INVALID_FARM);
        }
        if (StringUtil.isEmpty(purpose)) {
            throw new SwitchException(ITxnErrorCodes.EMPTY_PURPOSE_OF_VISIT);
        }

        /** FORMING AGRO TRANSACTION OBJECT **/
        AgroTransaction agroTransaction = new AgroTransaction();
        agroTransaction.setAgentId(agentId);
        agroTransaction.setAgentName((ObjectUtil.isEmpty(agent.getPersonalInfo()) ? "" : agent.getPersonalInfo().getAgentName()));
        agroTransaction.setDeviceId(device.getCode());
        agroTransaction.setDeviceName(device.getName());
        agroTransaction.setServicePointId(servicePoint.getCode());
        agroTransaction.setServicePointName(servicePoint.getName());
        agroTransaction.setFarmerId(farmerId);
        agroTransaction.setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
        agroTransaction.setProfType(Profile.AGENT);
        agroTransaction.setTxnType(TransactionTypeProperties.AGENT_MOVEMENT);
        agroTransaction.setOperType(ESETxn.ON_LINE);
        agroTransaction.setReceiptNo("N/A");
        agroTransaction.setTxnDesc(AGENT_MOVEMENT);
        
        // To set farmer audio file content in agro transaction 
        String voiceData = (String) reqData.get(TxnEnrollmentProperties.FARMER_AUDIO);
        byte[] voiceDataContent = null;
        try {
            if (voiceData != null && voiceData.length() > 0) {
                voiceDataContent = Base64.decodeBase64(voiceData.getBytes());
                agroTransaction.setAudioFile(voiceDataContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
            throw new SwitchException(ITxnErrorCodes.ERROR_WHILE_PROCESSING_FARMER_VOICE);
        }
                
        try {
            agroTransaction.setTxnTime(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
        } catch (Exception e) {
            throw new SwitchException(ITxnErrorCodes.DATE_CONVERSION_ERROR);
        }

        /** FORMING AGENT MOVEMENT OBJECT **/
        AgentMovement agentMovement = new AgentMovement();
        agentMovement.setAgroTransaction(agroTransaction);
        agentMovement.setFarm(farm);
        agentMovement.setPurpose(purpose);
        agentMovement.setRemarks(remarks);

        /** FORMING AGENT MOVEMENT LOCATION OBJECT **/
        Collection collection = (Collection) reqData.get(TxnEnrollmentProperties.AGENT_MOVEMENT_LOCATIONS);
        Set<AgentMovementLocation> locationSet = new LinkedHashSet<AgentMovementLocation>();

        if (!ObjectUtil.isEmpty(collection)) {
            List<com.sourcetrace.eses.txn.schema.Object> loctionsList = collection.getObject();
            for (com.sourcetrace.eses.txn.schema.Object object : loctionsList) {
                List<Data> locationData = object.getData();
                AgentMovementLocation location = new AgentMovementLocation();
                for (Data data : locationData) {
                    String key = data.getKey();
                    if (TxnEnrollmentProperties.UPDATE_TIME.equalsIgnoreCase(key)) {
                        location.setUpdateTime(data.getValue());
                    } else if (TxnEnrollmentProperties.PHOTO.equalsIgnoreCase(key)) {
                        DataHandler photoDataHandler = (DataHandler) data.getBinaryValue();
                        byte[] photoContent = null;
                        try {
                            if (photoDataHandler != null) {
                                photoContent = IOUtils.toByteArray(photoDataHandler
                                        .getInputStream());
                                location.setPhoto(photoContent);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new SwitchException(ITxnErrorCodes.INVALID_IMAGE);
                        }
                    } else if (TxnEnrollmentProperties.LATITUDE.equalsIgnoreCase(key)) {
                        location.setLatitude(data.getValue());
                    } else if (TxnEnrollmentProperties.LONGITUDE.equalsIgnoreCase(key)) {
                        location.setLongitude(data.getValue());
                    }
                }
                locationSet.add(location);
            }
        }
        if (!ObjectUtil.isListEmpty(locationSet)) {
            agentMovement.setAgentMovementLocations(locationSet);
        }

        /** SAVING AGENT MOVEMENT AND AGENT MOVEMENT LOCATION OBJECT **/
        try {
            agentService.addAgentMovement(agentMovement);
        } catch (Exception e) {
            throw new SwitchException(ITxnErrorCodes.ERROR);
        }

        /** FORM RESPONSE DATA **/
        Map resp = new HashMap();
        return resp;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#processVoid(java.util.Map)
     */
    @Override
    public Map<?, ?> processVoid(Map<?, ?> reqData) {

        return null;
    }

    /**
     * Gets the agent service.
     * @return the agent service
     */
    public IAgentService getAgentService() {

        return agentService;
    }

    /**
     * Sets the agent service.
     * @param agentService the new agent service
     */
    public void setAgentService(IAgentService agentService) {

        this.agentService = agentService;
    }

    /**
     * Gets the farmer service.
     * @return the farmer service
     */
    public IFarmerService getFarmerService() {

        return farmerService;
    }

    /**
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
    }

    /**
     * Gets the device service.
     * @return the device service
     */
    public IDeviceService getDeviceService() {

        return deviceService;
    }

    /**
     * Sets the device service.
     * @param deviceService the new device service
     */
    public void setDeviceService(IDeviceService deviceService) {

        this.deviceService = deviceService;
    }

    /**
     * Gets the service point service.
     * @return the service point service
     */
    public IServicePointService getServicePointService() {

        return servicePointService;
    }

    /**
     * Sets the service point service.
     * @param servicePointService the new service point service
     */
    public void setServicePointService(IServicePointService servicePointService) {

        this.servicePointService = servicePointService;
    }
}
