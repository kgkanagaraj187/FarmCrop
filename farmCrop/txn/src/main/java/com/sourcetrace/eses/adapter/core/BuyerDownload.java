/*
 * customerDownload.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;

/**
 * The Class customerDownload.
 */

@Component

public class BuyerDownload implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(BuyerDownload.class.getName());

    @Autowired
    private IProductDistributionService productDistributionService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

    	LOGGER.info("---------- BUYER Download Start ----------");
        /** HEADER VALUES **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);

        String revisionNo = (String) reqData
                .get(TransactionProperties.BUYER_REV_NO);
        
        if(StringUtil.isEmpty(revisionNo) || !StringUtil.isLong(revisionNo)){
        	revisionNo = "0";
        }

        /** REQUEST VALUES **/
        Map resp = new HashedMap();
        List<Customer> customerList = productDistributionService.listCustomerByRevisionNo(Long
                .valueOf(revisionNo));// productDistributionService.listcustomers();
        Collection collection = new Collection();
        List<Object> customers = new ArrayList<Object>();

        if (!ObjectUtil.isEmpty(customerList)) {
            for (Customer customer : customerList) {
                Data customerCode = new Data();
                customerCode.setKey(TransactionProperties.BUYER_ID);
                customerCode.setValue(customer.getCustomerId());

                Data customerName = new Data();
                customerName.setKey(TransactionProperties.CUSTOMER_NAME);
                customerName.setValue(customer.getCustomerName());

                // Data customerYear = new Data();
                // customerYear.setKey(ESETxnEnrollmentProperties.customer_YEAR);
                // customerYear.setValue(customer.getYear());

                List<Data> customerDataList = new ArrayList<Data>();
                customerDataList.add(customerCode);
                customerDataList.add(customerName);
                // customerDataList.add(customerYear);

                Object customerMasterObject = new Object();
                customerMasterObject.setData(customerDataList);
                customers.add(customerMasterObject);

            }

            collection.setObject(customers);

        }

        if (!ObjectUtil.isListEmpty(customerList)) {
            revisionNo = String.valueOf(customerList.get(0).getRevisionNo());
        }

        /** RESPONSE DATA **/
        resp.put(TransactionProperties.BUYER, collection);
        resp.put(TransactionProperties.BUYER_DOWNLOAD_REVISION_NO, revisionNo);
        LOGGER.info("---------- BUYER Download END ----------");
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
    public void setProductDistributionService(IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

}
