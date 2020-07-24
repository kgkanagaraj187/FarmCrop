/*
 * FarmerOutstandingBalanceDownload.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.txn.mfi.InterestCalcConsolidated;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

@Component
public class FarmerOutstandingBalanceDownload implements ITxnAdapter {

    private static final Logger LOGGER = Logger
            .getLogger(FarmerOutstandingBalanceDownload.class.getName());
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IFarmerService farmerService;
    @Autowired
    private IAgentService agentService;
    @Autowired
    private IProductDistributionService productDistributionService;
    @Autowired
    private IPreferencesService preferencesService;
    @Autowired
    private IDeviceService deviceService;
    
    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {
    	LOGGER.info("----------Farmer Outstanding BalanceDownload Start ----------");
        /** REQUEST DATA **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);

        /** VALIDATE AGENT DATA **/
        String agentId = head.getAgentId();
        if (StringUtil.isEmpty(agentId)) {
            throw new SwitchException(ITxnErrorCodes.AGENT_ID_EMPTY);
        }
        String serialNumber = head.getSerialNo();
        Device device = deviceService.findDeviceBySerialNumber(serialNumber);
        Agent agent = agentService.findAgentByProfileAndBranchId(agentId,device.getBranchId());
        if (ObjectUtil.isEmpty(agent)) {
            throw new SwitchException(ITxnErrorCodes.INVALID_AGENT);
        }

        String revisionNo = (String) reqData
                .get(TxnEnrollmentProperties.FARMER_OUTSTANDING_BALANCE_DOWNLOAD_REVISION_NO);
        if (StringUtil.isEmpty(revisionNo))
            throw new SwitchException(ITxnErrorCodes.EMPTY_FARMER_OUTSTANDING_BALANCE_REVISION_NO);
        LOGGER.info("REVISION NO" + revisionNo);

        Date revisionDate = getDateFromRevisionNo(revisionNo);

        /** FORM RESPONSE DATA **/
        Map resp = new HashMap();
        NumberFormat formatter = new DecimalFormat("0.00");
        // List<java.lang.Object[]> farmerList =
        // farmerService.listFarmerWithAccount();
        /*
         * List<java.lang.Object[]> farmerList = farmerService
         * .listFarmerWithAccountByRevisionDate(revisionDate);
         */
        ESESystem eseSystem = preferencesService.findPrefernceById("1");
        List<java.lang.Object[]> farmerList = farmerService
                .listActiveContractFarmersAccountByAgentAndSeason(agent.getId(), revisionDate);
        Collection collection = new Collection();
        List<Object> listOfFarmerObject = new ArrayList<Object>();
        if (!ObjectUtil.isListEmpty(farmerList)) {
        	int i=0;
            for (java.lang.Object[] farmer : farmerList) {
            	try{
                Data farmerIdData = new Data();
                farmerIdData.setKey(TxnEnrollmentProperties.FARMER_ID);
                farmerIdData.setValue(farmer[0].toString());

                Data farmerNameData = new Data();
                farmerNameData.setKey(TxnEnrollmentProperties.FARMER_NAME);
                farmerNameData.setValue((!StringUtil.isEmpty(farmer[1]))?farmer[1].toString():"" + " " + farmer[2]!=null||farmer[2]!=""?farmer[2].toString():"");

                Data farmerOutstandingData = new Data();
                farmerOutstandingData.setKey(TxnEnrollmentProperties.OUTSTANDING_BALANCE);
                farmerOutstandingData
                .setValue(formatter.format(farmer[5]));
               /* farmerOutstandingData
                        .setValue(formatter.format(farmer[3]) + "|" + formatter.format(farmer[4]));*/
                /*Data farmerLoanAmt = new Data();
                Data outstandingLoanAmount = new Data();
                if (eseSystem.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE).equals("1")) {
                	
                    farmerLoanAmt.setKey(TxnEnrollmentProperties.LOAN_AMOUNT);
                    farmerLoanAmt.setValue(formatter.format(farmer[6]));
                 
                    
                   
                    outstandingLoanAmount.setKey(TxnEnrollmentProperties.OUTSTANDING_LOAN_AMOUNT);
                    outstandingLoanAmount.setValue(formatter.format(farmer[7]));
                }else{
                	 farmerLoanAmt.setValue("0");
                	  outstandingLoanAmount.setValue("0");
                }*/
                
                Data farmerRoiData = new Data();
                farmerRoiData.setKey(TxnEnrollmentProperties.RATE_OF_INTEREST_FOB);

                Data farmerPrincipalAmtData = new Data();
                farmerPrincipalAmtData.setKey(TxnEnrollmentProperties.PRINCIPAL_AMOUNT);

                Data farmerIntAmtAccumlatedData = new Data();
                farmerIntAmtAccumlatedData
                        .setKey(TxnEnrollmentProperties.INTEREST_AMOUNT_ACCUMULATED);

                if (eseSystem.getPreferences().get(ESESystem.INTEREST_MODULE).equals("1")) {
                    InterestCalcConsolidated calcConsolidated = farmerService
                            .findInterestCalcConsolidatedByfarmerProfileIdOpt(farmer[0].toString());
                    farmerRoiData.setValue(String.valueOf(ObjectUtil.isEmpty(calcConsolidated) ? 0
                            : calcConsolidated.getCurrentRateOfInterest()));
                    farmerPrincipalAmtData
                            .setValue(String.valueOf(ObjectUtil.isEmpty(calcConsolidated) ? 0
                                    : calcConsolidated.getAccumulatedPrincipalAmount()));
                    farmerIntAmtAccumlatedData
                            .setValue(String.valueOf(ObjectUtil.isEmpty(calcConsolidated) ? 0
                                    : calcConsolidated.getAccumulatedIntAmount()));
                } else {
                    farmerRoiData.setValue("");
                    farmerPrincipalAmtData.setValue("");
                    farmerIntAmtAccumlatedData.setValue("");
                }
                List<Data> farmerDataList = new ArrayList<Data>();
                farmerDataList.add(farmerIdData);
                farmerDataList.add(farmerNameData);
                farmerDataList.add(farmerOutstandingData);
                farmerDataList.add(farmerRoiData);
                farmerDataList.add(farmerPrincipalAmtData);
                farmerDataList.add(farmerIntAmtAccumlatedData);
               // farmerDataList.add(farmerLoanAmt);
              //  farmerDataList.add(outstandingLoanAmount);
                Object farmerCreditObject = new Object();
                farmerCreditObject.setData(farmerDataList);

                listOfFarmerObject.add(farmerCreditObject);
            	}
            	catch (Exception e) {
					e.printStackTrace();
				}
            }
        }
        collection.setObject(listOfFarmerObject);
        if (!ObjectUtil.isListEmpty(farmerList)) {
            try {
                revisionDate = (Date) farmerList.get(0)[4];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        resp.put(TxnEnrollmentProperties.FARMER_OUTSTANDING_BALANCE_DOWNLOAD_REVISION_NO,
                getRevisionNoFromDate(revisionDate, revisionNo));
        resp.put(TxnEnrollmentProperties.FARMER_BALANCE_LIST, collection);
        return resp;
    }

    /**
     * Gets the date from revision no.
     * @param revisionNo the revision no
     * @return the date from revision no
     */
    private Date getDateFromRevisionNo(String revisionNo) {

        Date revisionNoDate = null;
        try {
            revisionNoDate = DateUtil.convertStringToDate(revisionNo,
                    DateUtil.REVISION_NO_DATE_TIME_FORMAT);
        } catch (Exception e) {
            LOGGER.info("Exception while parsing revisionNo : " + e.getMessage());
        }
        return revisionNoDate;
    }

    /**
     * Gets the revision no from date.
     * @param date the date
     * @param defaultRevionNo the default revion no
     * @return the revision no from date
     */
    private String getRevisionNoFromDate(Date date, String defaultRevionNo) {

        String revisionNo = defaultRevionNo;
        try {
            revisionNo = DateUtil.convertDateToString(date, DateUtil.REVISION_NO_DATE_TIME_FORMAT);
        } catch (Exception e) {
            LOGGER.info("Exception while parsing date : " + e.getMessage());
        }
        if (StringUtil.isEmpty(revisionNo)) {
            revisionNo = defaultRevionNo;
        }
        return revisionNo;
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
     * Gets the product distribution service.
     * @return the product distribution service
     */
    public IProductDistributionService getProductDistributionService() {

        return productDistributionService;
    }

    /**
     * Sets the product distribution service.
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(
            IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    public IPreferencesService getPreferencesService() {

        return preferencesService;
    }

    public void setPreferencesService(IPreferencesService preferencesService) {

        this.preferencesService = preferencesService;
    }

}
