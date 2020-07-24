/*
 * ClientProjectDownload.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.CustomerProject;
@Component
public class ClientProjectDownload implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(ClientProjectDownload.class.getName());
@Autowired
    private IClientService clientService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** FETCHING REQUEST DATA **/
        String revisionNo = (String) reqData.get(TxnEnrollmentProperties.REVISION_NO);
        if (StringUtil.isEmpty(revisionNo))
            throw new SwitchException(ITxnErrorCodes.EMPTY_REVISION_NO);
        LOGGER.info("REVISION NO : " + revisionNo);
        List<Customer> customerList = clientService.listCustomerByRevNo(Long.valueOf(revisionNo));
        Collection clientCollection = new Collection();
        List<Object> clientObjects = new ArrayList<Object>();
        /** FORM RESPONSE DATA **/
        Map resp = new HashMap();
        for (Customer customer : customerList) {
            Data clientCodeData = new Data();
            clientCodeData.setKey(TxnEnrollmentProperties.WAREHOUSE_CATEGORY_CODE);
            clientCodeData.setValue(customer.getCustomerId());

            Data clientNameData = new Data();
            clientNameData.setKey(TxnEnrollmentProperties.CLIENT_NAME);
            clientNameData.setValue(customer.getCustomerName());

            Data projectData = new Data();
            projectData.setKey(TxnEnrollmentProperties.PROJECTS);
            Collection clientProjectCollection = new Collection();
            List<Object> clientProjectObjects = new ArrayList<Object>();
            for (CustomerProject project : customer.getCustomerProjects()) {
                Data projectCodeData = new Data();
                projectCodeData.setKey(TxnEnrollmentProperties.PROCUREMENT_PROD_CODE);
                projectCodeData.setValue(project.getCodeOfProject());

                Data projectNameData = new Data();
                projectNameData.setKey(TxnEnrollmentProperties.PRICE_PATTERN_NAME);
                projectNameData.setValue(project.getNameOfProject());

                Data inspectionData = new Data();
                inspectionData.setKey(TxnEnrollmentProperties.CLIENT_INSPECTION_TYPE);
                inspectionData.setValue(String.valueOf(project.getInspection()));

                Data icsData = new Data();
                icsData.setKey(TxnEnrollmentProperties.LAND_UNDER_ICS_STATUS);
                icsData.setValue(String.valueOf(project.getIcsStatus()));

                Data certificateCategoryData = new Data();
                certificateCategoryData.setKey(TxnEnrollmentProperties.CROP_CATEGORY);
                certificateCategoryData.setValue(project.getCertificateStandard().getCategory().getCode());

                Data certificateStandardData = new Data();
                certificateStandardData.setKey(TxnEnrollmentProperties.CROP_SEASON);
                certificateStandardData.setValue(project.getCertificateStandard().getCode());

                List<Data> projectListData = new ArrayList<Data>();
                projectListData.add(projectCodeData);
                projectListData.add(projectNameData);
                projectListData.add(inspectionData);
                projectListData.add(icsData);
                projectListData.add(certificateCategoryData);
                projectListData.add(certificateStandardData);

                Object projectObject = new Object();
                projectObject.setData(projectListData);

                clientProjectObjects.add(projectObject);
            }
            clientProjectCollection.setObject(clientProjectObjects);
            projectData.setCollectionValue(clientProjectCollection);

            List<Data> clientDataList = new ArrayList<Data>();
            clientDataList.add(clientCodeData);
            clientDataList.add(clientNameData);
            clientDataList.add(projectData);

            Object clientObject = new Object();
            clientObject.setData(clientDataList);

            clientObjects.add(clientObject);

        }
        clientCollection.setObject(clientObjects);
        resp.put(TxnEnrollmentProperties.CLIENTS, clientCollection);
        resp.put(TxnEnrollmentProperties.REVISION_NO,
                (!ObjectUtil.isListEmpty(customerList) ? customerList.get(0).getRevisionNo(): revisionNo));
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
     * Sets the client service.
     * @param clientService the new client service
     */
    public void setClientService(IClientService clientService) {

        this.clientService = clientService;
    }

    /**
     * Gets the client service.
     * @return the client service
     */
    public IClientService getClientService() {

        return clientService;
    }

}
