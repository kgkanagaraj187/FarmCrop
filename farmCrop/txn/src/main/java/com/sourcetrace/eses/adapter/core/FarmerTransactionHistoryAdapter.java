/*
 * FarmerTransactionHistoryAdapter.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;

@Component
public class FarmerTransactionHistoryAdapter implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(FarmerTransactionHistoryAdapter.class);
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#.##");
    private static final int FARMER_TRANSACTION_HISTORY_LIMIT = 10;
    private static final DateFormat TXN_DATE_FORMAT = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
    private static final String DISTRIBUTION = "DI";
    private static final String PROCUREMENT = "PR";
    private static final String PAYMENT = "PP";
    private static final String PRODUCT_RETURN = "PRN";
    @Autowired
    private IFarmerService farmerService;
    @Autowired
    private IProductDistributionService productDistributionService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        LOGGER.info("========== FarmerTransactionHistoryAdapter Start ==========");
        /** REQUEST DATA **/
        String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
        LOGGER.info("Farmer Id : " + farmerId);

        /** VALIDATING FARMER **/
        if (StringUtil.isEmpty(farmerId))
            throw new SwitchException(ITxnErrorCodes.EMPTY_FARMER_ID);

        Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
        if (ObjectUtil.isEmpty(farmer))
            throw new SwitchException(ITxnErrorCodes.FARMER_NOT_EXIST);

        /** RETRIVING FARMER TRANSACTION HISTORY & BUILDING RESPONSE OBJECTS **/
        String[] transactionArray = new String[] { TransactionTypeProperties.PRODUCT_DISTRIBUTION,
                TransactionTypeProperties.PROCUREMENT_PRODUCT_ENROLLMENT,
                TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER,
                TransactionTypeProperties.PAYMENT_ADAPTER };
        List<AgroTransaction> farmerTransactionHistory = productDistributionService
                .listFarmerTransactionHistory(farmerId, transactionArray,
                        FARMER_TRANSACTION_HISTORY_LIMIT);

        Collection transactionHistoryCollection = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> transactionHistoryObject = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
        transactionHistoryCollection.setObject(transactionHistoryObject);
        if (!ObjectUtil.isListEmpty(farmerTransactionHistory)) {

            for (AgroTransaction agroTransaction : farmerTransactionHistory) {
                String txnTime = TXN_DATE_FORMAT.format(agroTransaction.getTxnTime());
                String txnType = getTxnShortDescriptionbtTxnType(agroTransaction.getTxnType());
                String txnAmount = NUMBER_FORMAT.format(agroTransaction.getTxnAmount());
                String balance = NUMBER_FORMAT.format(agroTransaction.getBalAmount());
                String desc = agroTransaction.getTxnDesc();

                Data txnTimeData = new Data();
                txnTimeData.setKey(TransactionProperties.TXN_TIME);
                txnTimeData.setValue(txnTime);

                Data txnTypeData = new Data();
                txnTypeData.setKey(TransactionProperties.TXN_TYPE);
                txnTypeData.setValue(txnType);

                Data txnAmountData = new Data();
                txnAmountData.setKey(TransactionProperties.TXN_AMOUNT);
                txnAmountData.setValue(txnAmount);

                List<Data> dataList = new ArrayList<Data>();
                dataList.add(txnTimeData);
                dataList.add(txnTypeData);
                dataList.add(txnAmountData);

                com.sourcetrace.eses.txn.schema.Object object = new com.sourcetrace.eses.txn.schema.Object();
                object.setData(dataList);
                if (desc.equalsIgnoreCase(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION)
                        || desc.equalsIgnoreCase(Procurement.PROCUREMENT_AMOUNT)) {
                    transactionHistoryObject.add(object);
                }

            }
        }

        /** FORM RESPONSE DATA **/
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put(TransactionProperties.TRANSACTION_HISTORY, transactionHistoryCollection);
        LOGGER.info("========== FarmerTransactionHistoryAdapter End ==========");
        return response;
    }

    /**
     * Gets the txn short descriptionbt txn type.
     * @param transactionType the transaction type
     * @return the txn short descriptionbt txn type
     */
    private String getTxnShortDescriptionbtTxnType(String transactionType) {

        if (Distribution.PRODUCT_DISTRIBUTION_TO_FARMER.equalsIgnoreCase(transactionType)) {
            return DISTRIBUTION;
        } else if (TransactionTypeProperties.PROCUREMENT_PRODUCT_ENROLLMENT
                .equalsIgnoreCase(transactionType)) {
            return PROCUREMENT;
        } else if (Distribution.PRODUCT_RETURN_FROM_FARMER.equalsIgnoreCase(transactionType)) {
            return PRODUCT_RETURN;
        } else {
            return PAYMENT;
        }
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
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
    }

    /**
     * Sets the product distribution service.
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(
            IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

}
