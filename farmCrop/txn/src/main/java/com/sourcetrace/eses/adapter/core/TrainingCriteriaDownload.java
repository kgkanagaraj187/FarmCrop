/*
 * TrainingCriteriaDownload.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
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

import com.ese.entity.txn.training.Topic;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;

@Component
public class TrainingCriteriaDownload implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(TrainingCriteriaDownload.class.getName());
    @Autowired
    private ITrainingService trainingService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** REQUEST DATA **/
        String revisionNo = (String) reqData
                .get(TxnEnrollmentProperties.REVISION_NO);
        if (StringUtil.isEmpty(revisionNo)) {
            throw new SwitchException(SwitchErrorCodes.EMPTY_REVISION_NO);
        }
        LOGGER.info("REVISION NO" + revisionNo);
        Map resp = new HashMap();
        Collection criteriaCollection = new Collection();
        List<Object> criteriaObjectList = new ArrayList<Object>();
        List<Topic> criteriaList = trainingService.listTopicByRevNo(Long.valueOf(revisionNo));
        for (Topic topic : criteriaList) {
            Data codeData = new Data();
            codeData.setKey(TxnEnrollmentProperties.WAREHOUSE_CATEGORY_CODE);
            codeData.setValue(topic.getCode());

            Data principleData = new Data();
            principleData.setKey(TxnEnrollmentProperties.PRINCIPLE);
            principleData.setValue(topic.getPrinciple());

            Data descData = new Data();
            descData.setKey(TxnEnrollmentProperties.DESCRIPTION);
            descData.setValue(topic.getDes());

            Data categoryCodeData = new Data();
            categoryCodeData.setKey(TxnEnrollmentProperties.CRITERIA_CATEGORY_CODE);
            categoryCodeData.setValue(!ObjectUtil.isEmpty(topic.getTopicCategory()) ? topic.getTopicCategory().getCode() : "");

            List<Data> criteriaData = new ArrayList<Data>();
            criteriaData.add(codeData);
            criteriaData.add(principleData);
            criteriaData.add(descData);
            criteriaData.add(categoryCodeData);

            Object criteriaObject = new Object();
            criteriaObject.setData(criteriaData);

            criteriaObjectList.add(criteriaObject);
        }
        criteriaCollection.setObject(criteriaObjectList);
        resp.put(TxnEnrollmentProperties.SHOP_DEALER_CREDIT_LIST,criteriaCollection);
        if (!ObjectUtil.isListEmpty(criteriaList)) {
            resp.put(TxnEnrollmentProperties.REVISION_NO, criteriaList.get(0).getRevisionNo());
        } else {
            resp.put(TxnEnrollmentProperties.REVISION_NO, revisionNo);
        }
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

}
