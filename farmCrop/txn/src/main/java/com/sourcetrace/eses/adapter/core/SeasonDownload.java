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

import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;

/**
 * The Class SeasonDownload.
 */
@Component
public class SeasonDownload implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(SeasonDownload.class.getName());
    
    @Autowired
    private IProductDistributionService productDistributionService;
    @Autowired
    private ICertificationService certificationService;
    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)s
     */
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** HEADER VALUES **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);

        String revisionNo = (String) reqData
                .get(TxnEnrollmentProperties.SEASON_DOWNLOAD_REVISION_NO);
    /*    if (StringUtil.isEmpty(revisionNo))
            throw new SwitchException(ITxnErrorCodes.EMPTY_SEASON_REVISION_NO);
        LOGGER.info("REVISION NO" + revisionNo);
    */    
        
        if(StringUtil.isEmpty(revisionNo) || !StringUtil.isLong(revisionNo)){
        	revisionNo = "0";
        }
        
        /** REQUEST VALUES **/
        Map resp = new HashedMap();
        List<HarvestSeason> seasonList = productDistributionService.listHarvestSeasonByRevisionNo(Long
                .valueOf(revisionNo));// productDistributionService.listSeasons();
        Collection collection = new Collection();
        List<Object> seasons = new ArrayList<Object>();

        
        Map<String, List<LanguagePreferences>> lpMap = null;
    	
    	if(seasonList != null && !ObjectUtil.isEmpty(seasonList)){
    		List<String> codes =  seasonList.stream().map(u -> u.getCode()).collect(Collectors.toList());
            List<LanguagePreferences> lpList =  new ArrayList<>();
            if(codes!=null && !codes.isEmpty()){
         	 lpList = certificationService.listLangPrefByCodes(codes);
            }
            if(lpList != null && !ObjectUtil.isEmpty(lpList)){
            	 lpMap = lpList.stream().collect(Collectors.groupingBy(LanguagePreferences::getCode));
            }
    	}
        
        if (!ObjectUtil.isEmpty(seasonList)) {
            for (HarvestSeason season : seasonList) {
                Data seasonCode = new Data();
                seasonCode.setKey(TxnEnrollmentProperties.SEASON_CODE_DOWNLOAD);
                seasonCode.setValue(season.getCode());

                Data seasonName = new Data();
                seasonName.setKey(TxnEnrollmentProperties.SEASON_NAME);
                seasonName.setValue(season.getName());

//                Data seasonYear = new Data();
//                seasonYear.setKey(ESETxnEnrollmentProperties.SEASON_YEAR);
//                seasonYear.setValue(season.getYear());

                Data lang = new Data();
                if(seasonList != null && !ObjectUtil.isEmpty(seasonList) && !ObjectUtil.isEmpty(lpMap) ){
                    lang.setKey(TransactionProperties.LANG_LIST);
                    lang.setCollectionValue(getCollection(lpMap.get(season.getCode())));
                }
                
                List<Data> seasonDataList = new ArrayList<Data>();
                seasonDataList.add(seasonCode);
                seasonDataList.add(seasonName);
                seasonDataList.add(lang);
             //   seasonDataList.add(seasonYear);

                Object seasonMasterObject = new Object();
                seasonMasterObject.setData(seasonDataList);
                seasons.add(seasonMasterObject);

            }

            collection.setObject(seasons);

        }

        if (!ObjectUtil.isListEmpty(seasonList)) {
            revisionNo = String.valueOf(seasonList.get(0).getRevisionNo());
        }

        /** RESPONSE DATA **/
        resp.put(TxnEnrollmentProperties.SEASONS, collection);
        resp.put(TxnEnrollmentProperties.SEASON_DOWNLOAD_REVISION_NO, revisionNo);
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
}
