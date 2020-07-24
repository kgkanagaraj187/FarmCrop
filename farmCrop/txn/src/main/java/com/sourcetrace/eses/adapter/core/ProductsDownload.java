/*
 * ProductsDownload.java
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
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;

@Component
public class ProductsDownload implements ITxnAdapter {
    private static final Logger LOGGER = Logger.getLogger(ProductsDownload.class.getName());
    @Autowired
    private IProductService productService;
    @Autowired
    private ICatalogueService catalogueService;
    @Autowired
    private ICertificationService certificationService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** GET REQUEST DATA **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);
        String revisionNo = (String) reqData
                .get(TxnEnrollmentProperties.PRODUCTS_DOWNLOAD_REVISION_NO);
    /*    if (StringUtil.isEmpty(revisionNo))
            throw new SwitchException(ITxnErrorCodes.EMPTY_PRODUCTS_REVISION_NO);
        LOGGER.info("REVISION NO" + revisionNo);
*/
        if(StringUtil.isEmpty(revisionNo) || !StringUtil.isLong(revisionNo)){
        	revisionNo = "0";
        }
        
        /** FORM RESPONSE DATA **/
        Map resp = new HashMap();
        List<Product> products = productService.listProductByRevisionNo(Long.valueOf(revisionNo),head.getBranchId());
        Collection collection = new Collection();
        List<Object> listOfProductObject = new ArrayList<Object>();

        
        Map<String, List<LanguagePreferences>> lpMap = null;
    	
    	if(products != null && !ObjectUtil.isEmpty(products)){
    		List<String> codes =  products.stream().map(u -> u.getCode()).collect(Collectors.toList());
    		 codes.addAll(products.stream().map(u -> u.getSubcategory().getCode()).collect(Collectors.toList())); 
            List<LanguagePreferences> lpList =  new ArrayList<>();
            if(codes!=null && !codes.isEmpty()){
         	 lpList = certificationService.listLangPrefByCodes(codes);
            }
            if(lpList != null && !ObjectUtil.isEmpty(lpList)){
            	 lpMap = lpList.stream().collect(Collectors.groupingBy(LanguagePreferences::getCode));
            }
    	}
        
        if (!ObjectUtil.isListEmpty(products)) {
            for (Product product : products) {
            	if(!ObjectUtil.isEmpty(product.getSubcategory())){
                Data categoryCodeData = new Data();
                categoryCodeData.setKey(TxnEnrollmentProperties.CATEGORY_CODE);
                categoryCodeData.setValue(product.getSubcategory().getCode());

                Data categoryNameData = new Data();
                categoryNameData.setKey(TxnEnrollmentProperties.CATEGORY_NAME);
                categoryNameData.setValue(product.getSubcategory().getName());

                Data productCodeData = new Data();
                productCodeData.setKey(TxnEnrollmentProperties.PRODUCT_CODE);
                productCodeData.setValue(product.getCode());

                Data productNameData = new Data();
                productNameData.setKey(TxnEnrollmentProperties.PRODUCT_NAME);
                productNameData.setValue(product.getName());

                Data productPriceData = new Data();
                productPriceData.setKey(TxnEnrollmentProperties.PRICE);
                productPriceData.setValue(!StringUtil.isEmpty(product.getPrice()) ? product.getPrice() : "");

                Data productUnitData = new Data();
                productUnitData.setKey(TxnEnrollmentProperties.UNIT);
                productUnitData.setValue(product.getUnit()==null || StringUtil.isEmpty(product.getUnit())?" ":product.getUnit());

                Data productManufactData=new Data();
                productManufactData.setKey(TxnEnrollmentProperties.MANUFACTURE);
                productManufactData.setValue(product.getManufacture()==null || StringUtil.isEmpty(product.getManufacture())?"":catalogueService.findCatalogueByCode(product.getManufacture()).getName());
                
                Data productManufactIdData=new Data();
                productManufactIdData.setKey(TxnEnrollmentProperties.MANUFACTURE_CODE);
                productManufactIdData.setValue(product.getManufacture()==null || StringUtil.isEmpty(product.getManufacture())?"":product.getManufacture());
                
                
                Data productIngredientData=new Data();
                productIngredientData.setKey(TxnEnrollmentProperties.INGREDIENT);
                productIngredientData.setValue(product.getIngredient()==null || StringUtil.isEmpty(product.getIngredient())?"":product.getIngredient());
                
                
                Data lang = new Data();
                if(products != null && !ObjectUtil.isEmpty(products) && !ObjectUtil.isEmpty(lpMap) ){
                    lang.setKey(TransactionProperties.LANG_LIST);
                    lang.setCollectionValue(getCollection(lpMap.get(product.getCode())));
                  }
                
                Data subcategory = new Data();
                if(products != null && !ObjectUtil.isEmpty(products) && !ObjectUtil.isEmpty(lpMap) ){
                	subcategory.setKey(TransactionProperties.LANG_LIST_CATEGORY);
                	subcategory.setCollectionValue(getCollection(lpMap.get(product.getSubcategory().getCode())));
                  }
                
                
                List<Data> productData = new ArrayList<Data>();
                productData.add(categoryCodeData);
                productData.add(categoryNameData);
                productData.add(productCodeData);
                productData.add(productNameData);
                productData.add(productPriceData);
                productData.add(productUnitData);
                productData.add(productManufactData);
                productData.add(productManufactIdData);
                productData.add(productIngredientData);
                productData.add(lang);
                productData.add(subcategory);
                

                Object productObject = new Object();
                productObject.setData(productData);

                listOfProductObject.add(productObject);
            	}
            }
            collection.setObject(listOfProductObject);
        }

        if (!ObjectUtil.isListEmpty(products)) {
            revisionNo = String.valueOf(products.get(0).getRevisionNo());
        }

        // response data
        resp.put(TxnEnrollmentProperties.PRODUCT_LIST, collection);
        resp.put(TxnEnrollmentProperties.PRODUCTS_DOWNLOAD_REVISION_NO, revisionNo);
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
