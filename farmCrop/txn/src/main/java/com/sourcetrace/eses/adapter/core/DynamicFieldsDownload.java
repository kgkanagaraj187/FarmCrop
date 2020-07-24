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

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.ObjectUtil;;

// TODO: Auto-generated Javadoc
/**
 * The Class SeasonDownload.
 */
@Component
public class DynamicFieldsDownload implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(DynamicFieldsDownload.class.getName());
	@Autowired

	private IFarmerService farmerService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		/** REQUEST VALUES **/
		Map resp = new HashedMap();
		List<DynamicFieldConfig> fieldsList = farmerService.listDynamicFields();
		List<DynamicSectionConfig> secList = farmerService.listDynamicSections();
		
		Collection secCollection = new Collection();
		
		List<Object> sections = new ArrayList<Object>();
		if (!ObjectUtil.isEmpty(secList)) {

			

			for (DynamicSectionConfig field : secList) {
				List<Data> fieldList = new ArrayList<Data>();
				Data fieldId = new Data();
				fieldId.setKey(TxnEnrollmentProperties.SECTION_CODE);
				fieldId.setValue(String.valueOf(field.getSectionCode()));
				fieldList.add(fieldId);

				Data fieldName = new Data();
				fieldName.setKey(TxnEnrollmentProperties.SECTION_NAME);
				fieldName.setValue(String.valueOf(field.getSectionName()));
				fieldList.add(fieldName);
				
				Data sectionType = new Data();
				sectionType.setKey(TxnEnrollmentProperties.SECTION_TYPE);
				sectionType.setValue(String.valueOf(field.getmTxnType()));
				fieldList.add(sectionType);
				
				Data sectionOrder = new Data();
				sectionOrder.setKey(TxnEnrollmentProperties.SECTION_ORDER);
				sectionOrder.setValue(String.valueOf(field.getSecorder()));
				fieldList.add(sectionOrder);

				
				// catalogueDataList.add(catalogueYear);

				Object fiedlsObj = new Object();
				fiedlsObj.setData(fieldList);
				sections.add(fiedlsObj);

			}

			secCollection.setObject(sections);

		}
		resp.put(TransactionProperties.SECTIONS, secCollection);
		Collection collection = new Collection();
		List<Object> fields = new ArrayList<Object>();
		if (!ObjectUtil.isEmpty(fieldsList)) {

		

			for (DynamicFieldConfig field : fieldsList) {
				List<Data> fieldList = new ArrayList<Data>();
				Data fieldId = new Data();
				fieldId.setKey(TxnEnrollmentProperties.FIELD_ID);
				fieldId.setValue(String.valueOf(field.getCode()));
				fieldList.add(fieldId);

				Data fieldName = new Data();
				fieldName.setKey(TxnEnrollmentProperties.FIELD_NAME);
				fieldName.setValue(String.valueOf(field.getComponentName()));
				fieldList.add(fieldName);

				Data fieldType = new Data();
				fieldType.setKey(TxnEnrollmentProperties.FIELD_TYPE);
				fieldType.setValue(String.valueOf(field.getComponentType()));
				fieldList.add(fieldType);

				Data maxLenghth = new Data();
				maxLenghth.setKey(TxnEnrollmentProperties.MAX_LENGTH);
				maxLenghth.setValue(String.valueOf(field.getComponentMaxLength()));
				fieldList.add(maxLenghth);
				
				Data depeKey = new Data();
				depeKey.setKey(TxnEnrollmentProperties.DEPENDANCY_KEY);
				depeKey.setValue(field.getParentDepen()!=null ? String.valueOf(field.getParentDepen().getCode()) : "");
				fieldList.add(depeKey);

				Data isMan = new Data();
				isMan.setKey(TxnEnrollmentProperties.IS_MANDATORY);
				isMan.setValue(String.valueOf(field.getIsRequired()));
				fieldList.add(isMan);
				
				Data order = new Data();
				order.setKey(TxnEnrollmentProperties.ORDER);
				order.setValue(String.valueOf(field.getOrderSet()));
				fieldList.add(order);
				
				Data section = new Data();
				section.setKey(TxnEnrollmentProperties.SECTION);
				section.setValue(String.valueOf(field.getDynamicSectionConfig().getSectionCode()));
				fieldList.add(section);
				
				Data catType = new Data();
				catType.setKey(TxnEnrollmentProperties.CATLOGUE_TYPE);
				catType.setValue(String.valueOf(field.getCatalogueType()));
				fieldList.add(catType);

				// catalogueDataList.add(catalogueYear);

				Object fiedlsObj = new Object();
				fiedlsObj.setData(fieldList);
				fields.add(fiedlsObj);

			}

			collection.setObject(fields);

		}

		/** RESPONSE DATA **/
		resp.put(TransactionProperties.DYNAMIC_FIELDS, collection);
		resp.put(TransactionProperties.DYNAMIC_REV_NO, "0");

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

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

}
