/*
 * PaymentAdapter.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.OfflinePayment;
import com.sourcetrace.eses.order.entity.txn.PaymentMode;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
@Component
public class PaymentAdapter implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(PaymentAdapter.class.getName());
    @Autowired
    private IProductDistributionService productDistributionService;
    @Autowired
    private IAgentService agentService;
    @Autowired

    private IDeviceService deviceService;
    @Autowired

    private IServicePointService servicePointService;
    @Autowired
    private IFarmerService farmerService;
    @Autowired
    private ILocationService locationService;
    @Autowired
    private IAccountService accountService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** FETCHING REQUEST DATA **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);

        String agentId = head.getAgentId();
        LOGGER.info("AGENT ID :" + agentId);
        String serialNo = head.getSerialNo();
        LOGGER.info("DEVICE ID :" + serialNo);
        String servPointId = head.getServPointId();
        LOGGER.info("SERVICE POINT ID :" + servPointId);

        String paymentDate = (String) reqData.get(TxnEnrollmentProperties.PAYMENT_DATE);
        String receiptNo = (String) reqData.get(TxnEnrollmentProperties.RECEIPT_NO);
        String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
        String seasonCode = (String) reqData.get(TxnEnrollmentProperties.CURR_SEASON_CODE);
        String pageNo = (String) reqData.get(TxnEnrollmentProperties.PAGE_NO);
        String paymentType = (String) reqData.get(TxnEnrollmentProperties.PAYMENT_TYPE);
        String paymentAmount = (String) reqData.get(TxnEnrollmentProperties.PAYMENT_AMT);
        String remarksValue = (String) reqData.get(TxnEnrollmentProperties.REMARK);
       
        String paymMode = (String) reqData.get(TxnEnrollmentProperties.PAY_MODE);
        String remark=(String) reqData.get(TxnEnrollmentProperties.REMARK);
        DataHandler photoDataHandler = (DataHandler) reqData.get(TxnEnrollmentProperties.PHOTO);
        String photoCaptureTime = (String) reqData
                .get(TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME);
        String latitude= (String) reqData
                .get(TxnEnrollmentProperties.LATITUDE);
        String longitude= (String) reqData
                .get(TxnEnrollmentProperties.LONGITUDE);

        
        String remarks = null;
        // Substring remarks request to fit the table column length
        if (!StringUtil.isEmpty(remarksValue)) {
            remarks = (remarksValue.length() > 250) ? remarksValue.substring(0, 250) : remarksValue;
        }

        /** ONLINE PROCESSING **/
        if (ESETxn.ONLINE_MODE == Integer.parseInt(head.getMode())) {

            /** VALIDATE REQUEST DATA **/
            Agent agent = agentService.findAgentByProfileId(agentId);
            if (ObjectUtil.isEmpty(agent))
                throw new SwitchException(ITxnErrorCodes.INVALID_AGENT);

            Device device = deviceService.findDeviceBySerialNumber(serialNo);
            if (ObjectUtil.isEmpty(device))
                throw new SwitchException(ITxnErrorCodes.INVALID_DEVICE);

            ServicePoint servicePoint = servicePointService.findServicePointByCode(servPointId);
            if (ObjectUtil.isEmpty(servicePoint))
                throw new SwitchException(ITxnErrorCodes.INVALID_SERVICE_POINT);

            if (StringUtil.isEmpty(receiptNo))
                throw new SwitchException(ITxnErrorCodes.EMPTY_RECEIPT_NO);
            List<AgroTransaction> exist = productDistributionService
                    .findAgroTxnByReceiptNo(receiptNo);
            if (!ObjectUtil.isListEmpty(exist))
                throw new SwitchException(ITxnErrorCodes.PAYMENT_ALREADY_EXIST);

            if (StringUtil.isEmpty(farmerId))
                throw new SwitchException(ITxnErrorCodes.EMPTY_FARMER_ID);

            Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
            if (ObjectUtil.isEmpty(farmer))
                throw new SwitchException(ITxnErrorCodes.FARMER_NOT_EXIST);

            if (StringUtil.isEmpty(seasonCode))
                throw new SwitchException(ITxnErrorCodes.EMPTY_SEASON_CODE);
            Season season = productDistributionService.findSeasonBySeasonCode(seasonCode);
            if (ObjectUtil.isEmpty(season))
                throw new SwitchException(ITxnErrorCodes.SEASON_NOT_EXIST);

            if (StringUtil.isEmpty(paymentType))
                throw new SwitchException(ITxnErrorCodes.EMPTY_PAYMENT_TYPE);

            PaymentMode paymentMode = productDistributionService.findPaymentModeByCode(paymentType);
            if (ObjectUtil.isEmpty(paymentMode))
                throw new SwitchException(ITxnErrorCodes.INVALID_PAYMENT_MODE);

            if (StringUtil.isEmpty(paymentDate))
                throw new SwitchException(ITxnErrorCodes.EMPTY_PAYMENT_DATE);

            /** CHECKING FARMER STATUS **/
            if (farmer.getStatus() == Farmer.Status.INACTIVE.ordinal())
                throw new SwitchException(ITxnErrorCodes.FARMER_INACTIVE);

            /** FETCHING ACCOUNT FOR AGENT **/
            ESEAccount agentAccount = accountService.findAccountByProfileIdAndProfileType(agentId,
                    ESEAccount.AGENT_ACCOUNT);
            if (ObjectUtil.isEmpty(agentAccount))
                throw new SwitchException(ITxnErrorCodes.AGENT_ACCOUNT_INVALID);
            else if (ESEAccount.INACTIVE == agentAccount.getStatus())
                throw new SwitchException(ITxnErrorCodes.AGENT_ACCOUNT_INACTIVE);

            /** FETCHING ACCOUNT FOR FARMER **/
            ESEAccount farmerAccount = farmerService.findAccountBySeassonProcurmentProductFarmer(0L,
                    farmer.getId());
            if (ObjectUtil.isEmpty(farmerAccount))
                throw new SwitchException(ITxnErrorCodes.FARMER_ACCOUNT_UNAVAILABLE);
            else if (ESEAccount.INACTIVE == farmerAccount.getStatus())
                throw new SwitchException(ITxnErrorCodes.FARMER_ACCOUNT_INACTIVE);

            AgroTransaction agentPaymentTxn = new AgroTransaction();
            AgroTransaction farmerPaymentTxn = new AgroTransaction();

            if (StringUtil.isEmpty(pageNo))
                pageNo = AgroTransaction.EMPTY_PAGE_NO;

            try {
                /** FORMING AGRO TRANSACTION FOR AGENT **/
                agentPaymentTxn.setReceiptNo(receiptNo);
                agentPaymentTxn.setAgentId(agentId);
                agentPaymentTxn.setAgentName(!ObjectUtil.isEmpty(agent.getPersonalInfo())
                        ? agent.getPersonalInfo().getAgentName() : "");
                agentPaymentTxn.setDeviceId(device.getCode());
                agentPaymentTxn.setDeviceName(device.getName());
                agentPaymentTxn.setServicePointId(servicePoint.getCode());
                agentPaymentTxn.setServicePointName(servicePoint.getName());
                agentPaymentTxn.setFarmerId(farmerId);
                agentPaymentTxn.setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
                agentPaymentTxn.setOperType(ESETxn.ON_LINE);
                agentPaymentTxn.setProfType(Profile.AGENT);
                agentPaymentTxn.setTxnTime(
                        DateUtil.convertStringToDate(paymentDate, DateUtil.TXN_DATE_TIME));
                agentPaymentTxn.setTxnAmount(Double.parseDouble(paymentAmount));
                /*agentPaymentTxn
                        .setTxnDesc(AgroTransaction.CASH_WITHDRAW + "|" + paymentMode.getName()
                                + "|" + season.getName() + "|" + season.getYear() + "|" + pageNo);*/
                agentPaymentTxn.setTxnDesc(paymentMode.getName());
                

                /** FORMING AGRO TRANSACTION FOR FARMER **/
                farmerPaymentTxn.setReceiptNo(receiptNo);
                farmerPaymentTxn.setAgentId(agentId);
                farmerPaymentTxn.setAgentName(!ObjectUtil.isEmpty(agent.getPersonalInfo())
                        ? agent.getPersonalInfo().getAgentName() : "");
                farmerPaymentTxn.setDeviceId(device.getCode());
                farmerPaymentTxn.setDeviceName(device.getName());
                farmerPaymentTxn.setServicePointId(servicePoint.getCode());
                farmerPaymentTxn.setServicePointName(servicePoint.getName());
                farmerPaymentTxn.setFarmerId(farmerId);
                farmerPaymentTxn.setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
                farmerPaymentTxn.setOperType(ESETxn.ON_LINE);
                farmerPaymentTxn.setProfType(Profile.AGENT);
                farmerPaymentTxn.setTxnTime(
                        DateUtil.convertStringToDate(paymentDate, DateUtil.TXN_DATE_TIME));
                farmerPaymentTxn.setTxnAmount(Double.parseDouble(paymentAmount));
               /* farmerPaymentTxn
                        .setTxnDesc(AgroTransaction.CASH_PAYMENT + "|" + paymentMode.getName() + "|"
                                + season.getName() + "|" + season.getYear() + "|" + pageNo);*/
                farmerPaymentTxn.setTxnDesc(paymentMode.getName());

                // Adding remarks to the Transaction description
                if (!StringUtil.isEmpty(remarks)) {
             	   if(!PaymentMode.LOAN_REPAYMENT
                            .equalsIgnoreCase(paymentType)){
                     agentPaymentTxn.setTxnDesc(agentPaymentTxn.getTxnDesc() + "|" + remarks);
             	   }
                     farmerPaymentTxn.setTxnDesc(farmerPaymentTxn.getTxnDesc() + "|" + remarks);
                 }
                // Added txn insufficient balance check
                if (!PaymentMode.DISTRIBUTION_ADVANCE_PAYMENT_MODE_CODE
                        .equalsIgnoreCase(paymentMode.getCode())) {
                    agentPaymentTxn.setTxnType(PaymentMode.PROCURMENT_PAYMENT_TXN);
                    farmerPaymentTxn.setTxnType(PaymentMode.PROCURMENT_PAYMENT_TXN);
                }else if(PaymentMode.LOAN_REPAYMENT
                        .equalsIgnoreCase(paymentType)){
                     farmerPaymentTxn.setTxnType(PaymentMode.LOAN_REPAYMENT_TXN);
                } else {
                    agentPaymentTxn.setTxnType(PaymentMode.DISTIBUTION_PAYMENT_TXN);
                    farmerPaymentTxn.setTxnType(PaymentMode.DISTIBUTION_PAYMENT_TXN);
                }
                if(PaymentMode.LOAN_REPAYMENT
                        .equalsIgnoreCase(paymentType)){
                	farmerPaymentTxn.setIntBalance(farmerAccount.getOutstandingLoanAmount());
                	farmerPaymentTxn.setTxnAmount(Double.parseDouble(paymentAmount));
                	farmerPaymentTxn.setBalAmount(farmerAccount.getOutstandingLoanAmount() - Double.parseDouble(paymentAmount));
    	            
                }
                /** SETTING ACCOUNT FOR AGENT AND FARMER **/
                agentPaymentTxn.setAccount(agentAccount);
                farmerPaymentTxn.setAccount(farmerAccount);
                
                agentPaymentTxn.setBranchId(head.getBranchId());
                farmerPaymentTxn.setBranchId(head.getBranchId());
                
                agentPaymentTxn.setBranch_id(head.getBranchId());
                farmerPaymentTxn.setBranch_id(head.getBranchId());

                /** SAVE AND UPDATE PAYMENT TXN AND ACCOUNT **/
                productDistributionService.saveAgroTransactionForPayment(farmerPaymentTxn,
                        agentPaymentTxn);

                agent.setReceiptNumber(receiptNo);

            } catch (Exception e) {
                LOGGER.info(e.getMessage());
                if (e instanceof SwitchException)
                    throw new SwitchException(((SwitchException) e).getCode());
                else
                    throw new SwitchException(ITxnErrorCodes.ERROR);
            }
        } /** OFFLINE PROCESSING **/
        else if (ESETxn.OFFLINE_MODE == Integer.parseInt(head.getMode())) {

            if (!StringUtil.isEmpty(receiptNo))
                agentService.updateAgentReceiptNoSequence(agentId, receiptNo);

            /** FORMING OFFLINE PAYMENT **/
            OfflinePayment payment = new OfflinePayment();
            payment.setAgentId(agentId);
            payment.setDeviceId(serialNo);
            payment.setFarmerId(farmerId);
            payment.setServicePointId(servPointId);
            payment.setReceiptNo(receiptNo);
            payment.setSeasonCode(seasonCode);
            payment.setPageNo(pageNo);
            // Adding remarks to offline payment
            payment.setRemarks(remarks);
            payment.setPaymentDate(paymentDate);
            payment.setPaymentType(paymentType);
            payment.setPaymentAmt(paymentAmount);
            payment.setRemark(remark);
            payment.setPaymentMode(paymMode);
            payment.setPcTime(photoCaptureTime);
            payment.setLatitude(latitude);
            payment.setLongitude(longitude);
            payment.setBranchId(head.getBranchId());
            
            byte[] photoContent = null;
            try {
            	if (photoDataHandler != null) {
                    photoContent = IOUtils.toByteArray(photoDataHandler.getInputStream());
                    if (photoContent != null) {
                    	payment.setImage(photoContent);
                    }
            	}
            }catch (Exception e) {
            	 payment.setStatusCode(ESETxnStatus.ERROR.ordinal());
                 payment.setStatusMsg(ITxnErrorCodes.INVALID_IMAGE);
			}
            
            payment.setStatusCode(ESETxnStatus.PENDING.ordinal());
            payment.setStatusMsg(ESETxnStatus.PENDING.toString());

            /** SAVING OFFLINE PAYMENT **/
            productDistributionService.saveOfflinePayment(payment);

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
     * Sets the product distribution service.
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(
            IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    /**
     * Gets the product distribution service.
     * @return the product distribution service
     */
    public IProductDistributionService getProductDistributionService() {

        return productDistributionService;
    }

    /**
     * Sets the agent service.
     * @param agentService the new agent service
     */
    public void setAgentService(IAgentService agentService) {

        this.agentService = agentService;
    }

    /**
     * Gets the agent service.
     * @return the agent service
     */
    public IAgentService getAgentService() {

        return agentService;
    }

    /**
     * Sets the device service.
     * @param deviceService the new device service
     */
    public void setDeviceService(IDeviceService deviceService) {

        this.deviceService = deviceService;
    }

    /**
     * Gets the device service.
     * @return the device service
     */
    public IDeviceService getDeviceService() {

        return deviceService;
    }

    /**
     * Sets the service point service.
     * @param servicePointService the new service point service
     */
    public void setServicePointService(IServicePointService servicePointService) {

        this.servicePointService = servicePointService;
    }

    /**
     * Gets the service point service.
     * @return the service point service
     */
    public IServicePointService getServicePointService() {

        return servicePointService;
    }

    /**
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
    }

    /**
     * Gets the farmer service.
     * @return the farmer service
     */
    public IFarmerService getFarmerService() {

        return farmerService;
    }

    /**
     * Sets the location service.
     * @param locationService the new location service
     */
    public void setLocationService(ILocationService locationService) {

        this.locationService = locationService;
    }

    /**
     * Gets the location service.
     * @return the location service
     */
    public ILocationService getLocationService() {

        return locationService;
    }

    /**
     * Sets the account service.
     * @param accountService the new account service
     */
    public void setAccountService(IAccountService accountService) {

        this.accountService = accountService;
    }

    /**
     * Gets the account service.
     * @return the account service
     */
    public IAccountService getAccountService() {

        return accountService;
    }

}
