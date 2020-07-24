/*
 * TrainingCriteriaCategoryDownload.java
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

import com.ese.entity.txn.training.TopicCategory;
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
public class TrainingCriteriaCategoryDownload implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(TrainingCriteriaCategoryDownload.class
            .getName());
    
    @Autowired
    private ITrainingService trainingService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {
    	
    	LOGGER.info("TRAINING CRITERIA CATEGORY DOWNLOAD STARTS");
        String revisionNo = (String) reqData
                .get(TxnEnrollmentProperties.TRAINING_CRITERIA_CATEGORY_DOWNLOAD_REVISION_NO);
        if (StringUtil.isEmpty(revisionNo))
            throw new SwitchException(SwitchErrorCodes.EMPTY_TRAINING_CRITERIA_CATEGORY_REVISION_NO);
        LOGGER.info("REVISION NO" + revisionNo);

        List<TopicCategory> categoryList = trainingService.listTopicCategoryByRevNo(Long
                .valueOf(revisionNo));// trainingService.listTopicCategory();
        Map resp = new HashMap();
        Collection criteriaCategoryCollection = new Collection();
        List<Object> listOfCriteriaCategoryObject = new ArrayList<Object>();
        if (!ObjectUtil.isListEmpty(categoryList)) {
            for (TopicCategory category : categoryList) {
                Data codeData = new Data();
                codeData.setKey(TxnEnrollmentProperties.CRITERIA_CATEGORY_CODE);
                codeData.setValue(category.getCode());

                Data nameData = new Data();
                nameData.setKey(TxnEnrollmentProperties.CRITERIA_CATEGORY_NAME);
                nameData.setValue(category.getName());

                List<Data> criteriaCategoryData = new ArrayList<Data>();
                criteriaCategoryData.add(codeData);
                criteriaCategoryData.add(nameData);

                Object criteriaCategoryObject = new Object();
                criteriaCategoryObject.setData(criteriaCategoryData);

                listOfCriteriaCategoryObject.add(criteriaCategoryObject);
            }
            revisionNo = String.valueOf(categoryList.get(0).getRevisionNo());
        }
        criteriaCategoryCollection.setObject(listOfCriteriaCategoryObject);
        resp.put(TxnEnrollmentProperties.CRITERIA_CATEGORY_LIST, criteriaCategoryCollection);
        resp.put(TxnEnrollmentProperties.TRAINING_CRITERIA_CATEGORY_DOWNLOAD_REVISION_NO,
                revisionNo);
        
        LOGGER.info("TRAINING CRITERIA CATEGORY DOWNLOAD ENDS");
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
