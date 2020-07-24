/*
 * SeasonDownload.java
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
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.inspect.agrocert.SurveyQuestion;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import  com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;;




// TODO: Auto-generated Javadoc
/**
 * The Class SeasonDownload.
 */
@Component
public class CatalogueDownload implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(CatalogueDownload.class.getName());
@Autowired

    private IProductDistributionService productDistributionService;
@Autowired
private ICertificationService certificationService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** HEADER VALUES **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);

        String revisionNo = (String) reqData
                .get(TransactionProperties.CATALOGUE_DOWNLOAD_REVISION_NO);
        
        if(StringUtil.isEmpty(revisionNo) || !StringUtil.isLong(revisionNo)){
        	revisionNo = "0";
        }
        
        /** REQUEST VALUES **/
        Map resp = new HashedMap();
        List<FarmCatalogue> catalogueList = productDistributionService.listCatalogueByRevisionNo(Long
                .valueOf(revisionNo));// productDistributionService.listSeasons();
       List<String> codes =  catalogueList.stream().map(u -> u.getCode())
				.collect(Collectors.toList());
       List<LanguagePreferences> lpList =  new ArrayList<>();
       if(codes!=null && !codes.isEmpty()){
    	 lpList = certificationService.listLangPrefByCodes(codes);
       }
      
       Map<String, List<LanguagePreferences>> lpMap = lpList.stream().collect(Collectors.groupingBy(LanguagePreferences::getCode));
        Collection collection = new Collection();
        List<Object> catalogues = new ArrayList<Object>();

        if (!ObjectUtil.isEmpty(catalogueList)) {
            for (FarmCatalogue cat : catalogueList) {
                Data catalogue = new Data();
                catalogue.setKey(TransactionProperties.CATLOGUE_CODE);
                if(cat.getName().equalsIgnoreCase(ESESystem.OTHERS))
                {
                    catalogue.setValue("99");

                }
                else
                {
                    catalogue.setValue(cat.getCode());

                }
                
              //  catalogue.setValue(cat.getCode());
                
                Data catalogueName = new Data();
                catalogueName.setKey(TransactionProperties.CATLOGUE_NAME);
                catalogueName.setValue(cat.getName());

               Data catalogueType = new Data();
                catalogueType.setKey(TransactionProperties.CATLOGUE_TYPE);
                catalogueType.setValue(String.valueOf(cat.getTypez()));

                // Data catalogueYear = new Data();
                // catalogueYear.setKey(ESETxnEnrollmentProperties.SEASON_YEAR);
                // catalogueYear.setValue(catalogue.getYear());
                
                Data catReserved = new Data();
                catReserved.setKey(TransactionProperties.SEQ_NO);
                catReserved.setValue(String.valueOf(cat.getIsReserved()));
                
                Data parentId = new Data();
                parentId.setKey(TransactionProperties.PARENT_ID);
                parentId.setValue(String.valueOf(cat.getParentId()));

                
                Data lang = new Data();
                lang.setKey(TransactionProperties.LANG_LIST);
                lang.setCollectionValue(getCollection(lpMap.get(cat.getCode())));
                
               
                

                List<Data> catalogueDataList = new ArrayList<Data>();
                catalogueDataList.add(catalogue);
                catalogueDataList.add(catalogueName);
               catalogueDataList.add(catalogueType);
               catalogueDataList.add(catReserved);
               catalogueDataList.add(parentId);
               catalogueDataList.add(lang);
                // catalogueDataList.add(catalogueYear);

                Object catalogueMasterObject = new Object();
                catalogueMasterObject.setData(catalogueDataList);
                catalogues.add(catalogueMasterObject);

            }

            collection.setObject(catalogues);

        }

        if (!ObjectUtil.isListEmpty(catalogueList)) {
            revisionNo = String.valueOf(catalogueList.get(0).getRevisionNo());
        }

        /** RESPONSE DATA **/
        resp.put(TransactionProperties.CATALOGUES, collection);
        resp.put(TransactionProperties.CATALOGUE_DOWNLOAD_REVISION_NO, revisionNo);
        return resp;
    }

    
    public Collection getCollection(List<LanguagePreferences> lpListObj) {
		Collection langColl = new Collection();
		List<Object> languages = new ArrayList<Object>();
		if (lpListObj!=null && !lpListObj.isEmpty()) {
	for (LanguagePreferences lp : lpListObj) {
				Data langCode = new Data();
				langCode.setKey(TransactionProperties.LANGUAGE_CODE);
				langCode.setValue(lp.getLang());

				Data langName = new Data();
				langName.setKey(TransactionProperties.LANGUAGE_NAME);
				langName.setValue(lp.getName());

				List<Data> langDataList = new ArrayList<Data>();
				langDataList.add(langCode);
				langDataList.add(langName);

				Object langMasterObject = new Object();
				langMasterObject.setData(langDataList);
				languages.add(langMasterObject);
				langColl.setObject(languages);

			}
		} 
		
		return langColl;
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
   

}
