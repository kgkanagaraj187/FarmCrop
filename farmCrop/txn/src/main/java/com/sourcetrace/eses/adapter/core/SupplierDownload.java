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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.StringUtil;;

// TODO: Auto-generated Javadoc
/**
 * The Class SupplierDownload.
 */
@Component
public class SupplierDownload implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(SupplierDownload.class.getName());

	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private IClientService clientService;

	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		/** REQUEST VALUES **/
		Map resp = new HashedMap();
		String revisionNo = (String) reqData.get(TransactionProperties.SUPPLIER_DOWNLOAD_REVISION_NO);
		
		  if(StringUtil.isEmpty(revisionNo) || !StringUtil.isLong(revisionNo)){
	        	revisionNo = "0";
	        }
		LOGGER.info("=================SUPPLIER Rev NO ======================" + revisionNo);
		
		Collection supplierCollection = new Collection();
		List<com.sourcetrace.eses.txn.schema.Object> masterObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();

		String masterProperties = getLocaleProperty("mTypeList");
		if (!StringUtil.isEmpty(masterProperties)) {
			
			List<MasterData> masterDataList = productDistributionService
					.listMasterDataByRevisionNo(Long.valueOf(revisionNo));

			Arrays.asList(masterProperties.split(",")).stream().forEach(master -> {
				String[] masterSplit = master.split("~");
				Collection supplierTypeCollection = new Collection();
				List<Data> masterList = new ArrayList<Data>();

				Data supIdData = new Data();
				supIdData.setKey(TransactionProperties.SUPPLIER_TYPE_ID);
				supIdData.setValue(masterSplit[0]);

				Data supNameData = new Data();
				supNameData.setKey(TransactionProperties.SUPPLIER_TYPE_NAME);
				supNameData.setValue(masterSplit[1]);

				//com.sourcetrace.eses.txn.schema.Object supplierObj = new com.sourcetrace.eses.txn.schema.Object();

				List<com.sourcetrace.eses.txn.schema.Object> supplierObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();

				com.sourcetrace.eses.txn.schema.Object masterObj = new com.sourcetrace.eses.txn.schema.Object();

				masterDataList.stream().filter(obj -> (obj.getMasterType().equals(masterSplit[0]))).forEach(obj -> {
					
					  com.sourcetrace.eses.txn.schema.Object supplierObj = new com.sourcetrace.eses.txn.schema.Object();
					List<Data> supplierDataList = new ArrayList<Data>();
					Data supId = new Data();
					supId.setKey(TransactionProperties.SUPPLIER_ID);
					supId.setValue(String.valueOf(obj.getCode()));

					Data supNmeData = new Data();
					supNmeData.setKey(TransactionProperties.SUPPLIER_NAME);
					supNmeData.setValue(obj.getName());

					supplierDataList.add(supId);
					supplierDataList.add(supNmeData);

					supplierObj.setData(supplierDataList);
					supplierObjectList.add(supplierObj);
				});

				supplierTypeCollection.setObject(supplierObjectList);

				Data supplierData = new Data();
				supplierData.setKey(TransactionProperties.SUPPLIER_lIST);
				supplierData.setCollectionValue(supplierTypeCollection);

				masterList.add(supIdData);
				masterList.add(supNameData);
				masterList.add(supplierData);

				masterObj.setData(masterList);
				masterObjectList.add(masterObj);
			});
		}
		
		supplierCollection.setObject(masterObjectList);

		resp.put(TransactionProperties.SUPPLIER_TYPE_lIST, supplierCollection);
		resp.put(TransactionProperties.SUPPLIER_DOWNLOAD_REVISION_NO, revisionNo);
		return resp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.txn.adapter.ITxnAdapter#processVoid(java.util.Map)
	 */
	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {

		return null;
	}

	public String getLocaleProperty(String prop) {
		String locProp = clientService.findLocaleProperty(prop, "en");
		return locProp;
	}
}
