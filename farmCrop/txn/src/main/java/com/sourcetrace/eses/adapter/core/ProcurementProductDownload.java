/*
 * ProcurementProductDownload.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.CropCalendarDetail;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
@Component
public class ProcurementProductDownload implements ITxnAdapter {

    private static Logger LOGGER = Logger.getLogger(ProcurementProductDownload.class);
    @Autowired
    private IProductDistributionService productDistributionService;
    
    @Autowired
	private IPreferencesService preferncesService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private ICertificationService certificationService;
    private long procurementProductRevisionNo;
 
    private long varietyRevisionNo;

    private long gradeRevisionNo;

    private boolean initialDownload;
    
    private long calendarRevisionNo;
    

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {
    	Head head = (Head) reqData.get(TransactionProperties.HEAD);
        LOGGER.info("---------- Procurement Product Download Start ----------");
        
        initializeRevisionNumbers();
        String revisionNoString = (String) reqData.get(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_DOWNLOAD_REVISION_NO);
        
        if (!StringUtil.isEmpty(revisionNoString) && revisionNoString.contains("|")) {
            String[] revisionNoArray = revisionNoString.split("\\|");
            if (revisionNoArray.length == 3) {
                try {
                    procurementProductRevisionNo = Long.valueOf(revisionNoArray[0]);
                    varietyRevisionNo = Long.valueOf(revisionNoArray[1]);
                    gradeRevisionNo = Long.valueOf(revisionNoArray[2]);
                } catch (Exception e) {
                    LOGGER.info("Revision No Conversion Error:" + e.getMessage());
                    initializeRevisionNumbers();
                }
            }
            if (procurementProductRevisionNo + varietyRevisionNo + gradeRevisionNo > 0)
                initialDownload = false;
        }
       
        // Remove this Method only After Revision number based Task complete from Mobile Side
        // initializeRevisionNumbers();        
        
        /** FORM RESPONSE DATA **/
        
        Collection procurementProductCollection = new Collection();
        Collection varietyCollection = new Collection();  
        Collection gradeCollection = new Collection();  
        
        if (initialDownload) {
        	if(head.getTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)){
        		List<ProcurementProduct> procurementProductList = productDistributionService.listProcurementProductByBranch(head.getBranchId());
        		 procurementProductCollection = buildProcurementProductCollection(procurementProductList);
        	}
        	else{
            List<ProcurementProduct> procurementProductList = productDistributionService.listProcurementProduct();
            procurementProductCollection = buildProcurementProductCollection(procurementProductList);
        	}
           
        }else{
        	if(head.getTenantId().equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)){
            List<ProcurementProduct> procurementProductList = productDistributionService.listProcurementProductByRevisionNoByBranch(Long.valueOf(procurementProductRevisionNo),head.getBranchId());
            procurementProductCollection = buildProcurementProductCollection(procurementProductList);

            List<ProcurementVariety> varietyList = productDistributionService.listProcurementProductVarietyByRevisionNoByBranch(Long.valueOf(varietyRevisionNo),head.getBranchId());
            varietyCollection = buildVarietyCollection(varietyList);

        	}
        	else{
        		  List<ProcurementProduct> procurementProductList = productDistributionService.listProcurementProductByRevisionNo(Long.valueOf(procurementProductRevisionNo));
                  procurementProductCollection = buildProcurementProductCollection(procurementProductList);

                  List<ProcurementVariety> varietyList = productDistributionService.listProcurementProductVarietyByRevisionNo(Long.valueOf(varietyRevisionNo));
                  varietyCollection = buildVarietyCollection(varietyList);
        	}
            List<ProcurementGrade> gradeList = productDistributionService.listProcurementProductGradeByRevisionNo(Long.valueOf(gradeRevisionNo));
            gradeCollection = buildGradeCollection(gradeList);
            
           

        }
        
            revisionNoString = procurementProductRevisionNo + "|" + varietyRevisionNo + "|" + gradeRevisionNo;        

            Map resp = new LinkedHashMap();
            resp.put(TxnEnrollmentProperties.PROCUREMENT_MTNT_PRODUCTS, procurementProductCollection);
            if (!initialDownload) {
                resp.put(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_VARIETY_LIST, varietyCollection);
                resp.put(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_GRADE_LIST, gradeCollection);
            }
            resp.put(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_DOWNLOAD_REVISION_NO, revisionNoString);
            
            LOGGER.info("----------Procurement Product Download End ----------");
            return resp;
    }

    /**
     * Build procurement product collection.
     * @param procurementProductList 
     * @return the collection
     */
    private Collection buildProcurementProductCollection(List<ProcurementProduct> procurementProductList) {
        
        Collection procurementProductCollection = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> procurementProductObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
       
        Map<String, List<LanguagePreferences>> lpMap = null;
    	
    	if(procurementProductList != null && !ObjectUtil.isEmpty(procurementProductList)){
    		List<String> codes =  procurementProductList.stream().map(u -> u.getCode()).collect(Collectors.toList());
            List<LanguagePreferences> lpList =  new ArrayList<>();
            if(codes!=null && !codes.isEmpty()){
         	 lpList = certificationService.listLangPrefByCodes(codes);
            }
            if(lpList != null && !ObjectUtil.isEmpty(lpList)){
            	 lpMap = lpList.stream().collect(Collectors.groupingBy(LanguagePreferences::getCode));
            }
    	}
        
        if (!ObjectUtil.isListEmpty(procurementProductList)) {
            
            for (ProcurementProduct procurementProduct : procurementProductList) {
              
                com.sourcetrace.eses.txn.schema.Object procurementProductObj = new com.sourcetrace.eses.txn.schema.Object();
                List<Data> procurementProductDataList = new ArrayList<Data>();

                Data productCode = new Data();
                productCode.setKey(TxnEnrollmentProperties.PROCUREMENT_PROD_CODE);
                productCode.setValue(procurementProduct.getCode());
                procurementProductDataList.add(productCode);
                
                Data productName = new Data();
                productName.setKey(TxnEnrollmentProperties.PRICE_PATTERN_NAME);
                productName.setValue(procurementProduct.getName());  
                procurementProductDataList.add(productName);
                
                Data productUnit = new Data();
                productUnit.setKey(TxnEnrollmentProperties.PROCUREMENT_PROD_UNIT);
                productUnit.setValue(!StringUtil.isEmpty(procurementProduct.getUnit())?procurementProduct.getUnit():"");  
                procurementProductDataList.add(productUnit);
                
                
                Data cropCat = new Data();
                cropCat.setKey(TxnEnrollmentProperties.PRODUCT_CATEGORY);
                cropCat.setValue(!StringUtil.isEmpty(procurementProduct.getCropCategory())?procurementProduct.getCropCategory():"");  
                procurementProductDataList.add(cropCat);
                
                
                Data mspRate = new Data();
                mspRate.setKey(TxnEnrollmentProperties.MSP_RATE);
                mspRate.setValue(!StringUtil.isEmpty(procurementProduct.getMspRate())?String.valueOf(procurementProduct.getMspRate()):"");  
                procurementProductDataList.add(mspRate);
              
                
                
                Data msgPercentage = new Data();
                msgPercentage.setKey(TxnEnrollmentProperties.MSP_PERCENTAGE);
                msgPercentage.setValue(!StringUtil.isEmpty(procurementProduct.getMspPercentage())?String.valueOf(procurementProduct.getMspPercentage()):"");  
                procurementProductDataList.add(msgPercentage);
              
                
                /*Data harvestDays = new Data();
                harvestDays.setKey(TxnEnrollmentProperties.ESTIMATION_HARVEST_DAYS);
                harvestDays.setValue(!StringUtil.isEmpty(procurementProduct.getNoOfDaysToGrow())?procurementProduct.getNoOfDaysToGrow():"");  
                procurementProductDataList.add(harvestDays);*/
               
                if(procurementProductList != null && !ObjectUtil.isEmpty(procurementProductList) && !ObjectUtil.isEmpty(lpMap) ){
                    Data lang = new Data();
                    lang.setKey(TransactionProperties.LANG_LIST);
                    lang.setCollectionValue(getCollection(lpMap.get(procurementProduct.getCode())));
                    procurementProductDataList.add(lang);
                    }

                if (initialDownload) {
                    
                    Data procurementVarietyData = new Data();
                    procurementVarietyData.setKey(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_VARIETY_LIST);
                    //List<ProcurementVariety> procurementVarietyList = productDistributionService.listProcurementProductVarietyByProcurementProductId(procurementProduct.getId(),varietyRevisionNo);
                    procurementVarietyData.setCollectionValue(buildVarietyCollection(!ObjectUtil.isListEmpty(procurementProduct.getProcurementVarieties()) ? new LinkedList<ProcurementVariety>(procurementProduct.getProcurementVarieties()) : null));
                    procurementProductDataList.add(procurementVarietyData);
                    procurementProductRevisionNo = Math.max(procurementProduct.getRevisionNo(), procurementProductRevisionNo);
                }
                procurementProductObj.setData(procurementProductDataList); 
                procurementProductObjectList.add(procurementProductObj);
            }
            if (!initialDownload)
                procurementProductRevisionNo = procurementProductList.get(0).getRevisionNo();
        }
        procurementProductCollection.setObject(procurementProductObjectList);
        return procurementProductCollection;    
    }

    /**
     * Build variety collection.
     * @param varietyList 
     * @return the collection
     */
    private Collection buildVarietyCollection(List<ProcurementVariety> varietyList) {
        
    	Map<String, List<LanguagePreferences>> lpMap = null;
    	
    	if(varietyList != null && !ObjectUtil.isEmpty(varietyList)){
    		List<String> codes =  varietyList.stream().map(u -> u.getCode()).collect(Collectors.toList());
            List<LanguagePreferences> lpList =  new ArrayList<>();
            if(codes!=null && !codes.isEmpty()){
         	 lpList = certificationService.listLangPrefByCodes(codes);
            }
            if(lpList != null && !ObjectUtil.isEmpty(lpList)){
            	 lpMap = lpList.stream().collect(Collectors.groupingBy(LanguagePreferences::getCode));
            }
    	}
    	
        Collection varietyCollection = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> varietyObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
        ESESystem preferencesOne = preferncesService.findPrefernceById("1");
        
        Request requestData =  (Request) ReflectUtil.getCurrentTxnRequestData();
      	String currentBranch = ObjectUtil.isEmpty(requestData) ? null : requestData.getHead().getBranchId();
      	String currentSeasonCode= clientService.findCurrentSeasonCodeByBranchId(currentBranch);
      	
		Integer calendarEnable = 0;
		if (!StringUtil.isEmpty(preferencesOne) && !StringUtil.isEmpty(preferencesOne.getPreferences().get(ESESystem.ENABLE_CROP_CALENDAR))
				&& !preferencesOne.getPreferences().get(ESESystem.ENABLE_CROP_CALENDAR).equalsIgnoreCase(null))
			calendarEnable = Integer.valueOf(preferencesOne.getPreferences().get(ESESystem.ENABLE_CROP_CALENDAR));
        if (!ObjectUtil.isListEmpty(varietyList)) {
            
            for (ProcurementVariety procurementVariety : varietyList) {              
                com.sourcetrace.eses.txn.schema.Object varietyObj = new com.sourcetrace.eses.txn.schema.Object();                
                List<Data> varietyDataList = new ArrayList<Data>();

                Data varietyCode = new Data();
                varietyCode.setKey(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_VARIETY_CODE);
                varietyCode.setValue(procurementVariety.getCode());
                varietyDataList.add(varietyCode);

                Data varietyName = new Data();
                varietyName.setKey(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_VARIETY_NAME);
                varietyName.setValue(procurementVariety.getName());                
                varietyDataList.add(varietyName);
                
                Data harvestDays = new Data();
                harvestDays.setKey(TxnEnrollmentProperties.ESTIMATION_HARVEST_DAYS);
                harvestDays.setValue(!StringUtil.isEmpty(procurementVariety.getHarvestDays())?procurementVariety.getHarvestDays():"");  
                varietyDataList.add(harvestDays);
                
                if (calendarEnable == 1) {
                    Data cropCalendarData = new Data();
                    cropCalendarData.setKey(TxnEnrollmentProperties.CROP_CALENDAR_LIST);                   
                    List<CropCalendarDetail> cropCalendarDetailList = productDistributionService.listCropCalendarDetailByProcurementVarietyId(procurementVariety.getId(),currentSeasonCode);
                    cropCalendarData.setCollectionValue(buildCropCalendarCollection(!ObjectUtil.isListEmpty(cropCalendarDetailList) ? cropCalendarDetailList : null));
                    varietyDataList.add(cropCalendarData);
                    
                    }

                if(varietyList != null && !ObjectUtil.isEmpty(varietyList) && !ObjectUtil.isEmpty(lpMap) ){
                    Data lang = new Data();
                    lang.setKey(TransactionProperties.LANG_LIST);
                    lang.setCollectionValue(getCollection(lpMap.get(procurementVariety.getCode())));
                    varietyDataList.add(lang);
                    }
                
                if (initialDownload) {
                    Data gradeData = new Data();
                    gradeData.setKey(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_GRADE_LIST);                   
                    //List<ProcurementGrade> procurementGradeList = productDistributionService.listProcurementProductGradeByProcurementProductVarietyId(procurementVariety.getId(),gradeRevisionNo);
                    gradeData.setCollectionValue(buildGradeCollection(!ObjectUtil.isListEmpty(procurementVariety.getProcurementGrades()) ?  new LinkedList<ProcurementGrade>(procurementVariety.getProcurementGrades()) : null));
                    varietyDataList.add(gradeData);
                    
                    
                    varietyRevisionNo = Math.max(procurementVariety.getRevisionNo(), varietyRevisionNo);
                }else {
                    Data varietyProcurementCode = new Data();
                    varietyProcurementCode.setKey(TxnEnrollmentProperties.PROCUREMENT_PROD_CODE);
                    varietyProcurementCode.setValue(procurementVariety.getProcurementProduct().getCode());
                    varietyDataList.add(varietyProcurementCode);
                }
                varietyObj.setData(varietyDataList);
                varietyObjectList.add(varietyObj);  
                
                
            }
            if (!initialDownload)
                varietyRevisionNo = varietyList.get(0).getRevisionNo();
        }
        varietyCollection.setObject(varietyObjectList);
        return varietyCollection;
    }

    /**
     * Build grade collection.
     * @param gradeList 
     * @return the collection
     */
    private Collection buildGradeCollection(List<ProcurementGrade> gradeList) {
        
        Collection gradeCollection = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> gradeObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
       
        Map<String, List<LanguagePreferences>> lpMap = null;
    	
    	if(gradeList != null && !ObjectUtil.isEmpty(gradeList)){
    		List<String> codes =  gradeList.stream().map(u -> u.getCode()).collect(Collectors.toList());
            List<LanguagePreferences> lpList =  new ArrayList<>();
            if(codes!=null && !codes.isEmpty()){
         	 lpList = certificationService.listLangPrefByCodes(codes);
            }
            if(lpList != null && !ObjectUtil.isEmpty(lpList)){
            	 lpMap = lpList.stream().collect(Collectors.groupingBy(LanguagePreferences::getCode));
            }
    	}
        
        if (!ObjectUtil.isListEmpty(gradeList)) {
            
            for (ProcurementGrade procurementGrade : gradeList) {
              
                com.sourcetrace.eses.txn.schema.Object gradeObj = new com.sourcetrace.eses.txn.schema.Object();
                List<Data> gradeDataList = new ArrayList<Data>();                

                Data gradeCode = new Data();
                gradeCode.setKey(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_GRADE_CODE);
                gradeCode.setValue(procurementGrade.getCode());
                gradeDataList.add(gradeCode);

                Data gradeName = new Data();
                gradeName.setKey(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_GRADE_NAME);
                gradeName.setValue(procurementGrade.getName());   
                gradeDataList.add(gradeName);
                
                Data gradePrice= new Data();
                gradePrice.setKey(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_GRADE_PRICE);
                gradePrice.setValue(String.valueOf(procurementGrade.getPrice()));   
                gradeDataList.add(gradePrice);
                
                if(gradeList != null && !ObjectUtil.isEmpty(gradeList) && !ObjectUtil.isEmpty(lpMap) ){
                    Data lang = new Data();
                    lang.setKey(TransactionProperties.LANG_LIST);
                    lang.setCollectionValue(getCollection(lpMap.get(procurementGrade.getCode())));
                    gradeDataList.add(lang);
                 }

                if (initialDownload)
                    gradeRevisionNo = Math.max(procurementGrade.getRevisionNo(), gradeRevisionNo);
                else {
                    Data gradeVarietyCode = new Data();
                    gradeVarietyCode.setKey(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_VARIETY_CODE);
                    gradeVarietyCode.setValue(procurementGrade.getProcurementVariety().getCode());
                    gradeDataList.add(gradeVarietyCode);
                }            
                gradeObj.setData(gradeDataList);
                gradeObjectList.add(gradeObj);                
            }
            if (!initialDownload)
                gradeRevisionNo = gradeList.get(0).getRevisionNo();
        }
        gradeCollection.setObject(gradeObjectList);
        return gradeCollection;
    }
    
    
 private Collection buildCropCalendarCollection(List<CropCalendarDetail> calendarList) {
        
        Collection calendarCollection = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> calendarObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
       
        if (!ObjectUtil.isListEmpty(calendarList)) {
            
            for (CropCalendarDetail cropCalendarDetail : calendarList) {
              
                com.sourcetrace.eses.txn.schema.Object calendatObj = new com.sourcetrace.eses.txn.schema.Object();
                List<Data> calendarDataList = new ArrayList<Data>();                

                Data calendrName = new Data();
                calendrName.setKey(TxnEnrollmentProperties.CALENDAR_NAME);
                calendrName.setValue(cropCalendarDetail.getCropCalendar().getName());
                calendarDataList.add(calendrName);

                Data seasonCode = new Data();
                seasonCode.setKey(TxnEnrollmentProperties.PROD_STATUS);
                seasonCode.setValue(cropCalendarDetail.getCropCalendar().getSeasonCode());   
                calendarDataList.add(seasonCode);
                
                Data activityType= new Data();
                activityType.setKey(TxnEnrollmentProperties.CALENDAR_ACTIVITY_TYPE);
                activityType.setValue(String.valueOf(cropCalendarDetail.getCropCalendar().getActivityType()));   
                calendarDataList.add(activityType);
                
                Data activityMethod= new Data();
                activityMethod.setKey(TxnEnrollmentProperties.CALENDAR_ACTIVITY_METHOD);
                activityMethod.setValue(cropCalendarDetail.getActivityMethod());   
                calendarDataList.add(activityMethod);
                
                Data noOfDays= new Data();
                noOfDays.setKey(TxnEnrollmentProperties.NO_OF_DAYS);
                noOfDays.setValue(String.valueOf(cropCalendarDetail.getNoOfDays()));   
                calendarDataList.add(noOfDays);
                
                

                if (initialDownload)
                	calendarRevisionNo = Math.max(Long.valueOf(cropCalendarDetail.getCropCalendar().getRevisionNo()), calendarRevisionNo);
                else {
                    Data gradeVarietyCode = new Data();
                    gradeVarietyCode.setKey(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_VARIETY_CODE);
                    gradeVarietyCode.setValue(cropCalendarDetail.getCropCalendar().getProcurementVariety().getCode());
                    calendarDataList.add(gradeVarietyCode);
                }            
                calendatObj.setData(calendarDataList);
                calendarObjectList.add(calendatObj);                
            }
            if (!initialDownload)
            	calendarRevisionNo = Long.valueOf(calendarList.get(0).getCropCalendar().getRevisionNo());
        }
        calendarCollection.setObject(calendarObjectList);
        return calendarCollection;
    }

    /**
     * Initialize revision numbers.
     */
    private void initializeRevisionNumbers() {

        procurementProductRevisionNo = 0;
        varietyRevisionNo = 0;
        gradeRevisionNo= 0;
        calendarRevisionNo = 0;
        initialDownload = true;
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
     * Sets the product distribution service.
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    /**
     * Gets the product distribution service.
     * @return the product distribution service
     */
    public IProductDistributionService getProductDistributionService() {

        return productDistributionService;
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
