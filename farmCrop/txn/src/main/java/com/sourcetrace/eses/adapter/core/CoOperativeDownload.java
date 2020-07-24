package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmCatalogueMaster;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Village;

@Component
public class CoOperativeDownload implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(CoOperativeDownload.class.getName());
    @Autowired
    private ILocationService locationService;
    @Autowired
    private ICatalogueService catalogueService;
    
    private Map<String, List<String>> villageMap = new LinkedHashMap<String, List<String>>();

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @SuppressWarnings( { "unchecked" })
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {
    	  Head head = (Head) reqData.get(TransactionProperties.HEAD);
        villageMap.clear();
        LOGGER.info("========== Co operativeDownload Begin ==========");
        String revisionNo = (String) reqData.get(TxnEnrollmentProperties.COOPERATIVE_DOWNLOAD_REVISION_NO);
        if(StringUtil.isEmpty(revisionNo) || !StringUtil.isLong(revisionNo)){
        	revisionNo = "0";
        }
        
        if(!StringUtil.isLong(revisionNo)){
        	revisionNo = "0";
        }
        
        String[] revisionNoArray = revisionNo.split("\\|");
        if(revisionNoArray.length!=2) {
            revisionNoArray = new String[2];
            revisionNoArray[0]="0";
            revisionNoArray[1]="0";
        }
        
        LOGGER.info("COOPERATIVE : REVISION NO" + revisionNoArray[0]);
        LOGGER.info("SAMITHI     : REVISION NO" + revisionNoArray[1]);
        
        Collection coOperativeList = new Collection();
        Collection samithiList = new Collection();
        List<Warehouse> cooperativeWarehouses = null;
        List<Warehouse> samithiWarehouses = null;
        try {
            // Fetch list of Co-Operatives To Forming Co-Operative List
            if(head.getTenantId().equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID) || head.getTenantId().equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID))
            {
            cooperativeWarehouses = locationService.listCoOperativeAndSamithiByRevisionNo(Long.valueOf(revisionNoArray[0]), Warehouse.WarehouseTypeArray);
            }else{
             cooperativeWarehouses = locationService.listCoOperativeAndSamithiByRevisionNo(Long.valueOf(revisionNoArray[0]), Warehouse.COOPERATIVE);
            }
            if (!ObjectUtil.isListEmpty(cooperativeWarehouses)) {
                List<com.sourcetrace.eses.txn.schema.Object> coOperativeObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();

                for (Warehouse warehouse : cooperativeWarehouses) {
                    
                    // if (ObjectUtil.isEmpty(warehouse.getRefCooperative())) {
                        com.sourcetrace.eses.txn.schema.Object coOperativeObject = new com.sourcetrace.eses.txn.schema.Object();
                        List<Data> coOperativeDataList = new ArrayList<Data>();

                        Data coOperativeCode = new Data();
                        coOperativeCode.setKey(TxnEnrollmentProperties.COOPERATIVE_CODE);
                        coOperativeCode.setValue(warehouse.getCode());

                        Data coOperativeName = new Data();
                        coOperativeName.setKey(TxnEnrollmentProperties.COOPERATIVE_NAME);
                        coOperativeName.setValue(warehouse.getName());
                        
                        Data coOperativeType = new Data();
                        coOperativeType.setKey(TxnEnrollmentProperties.COOPERTAIVE_TYPE);
                        coOperativeType.setValue(String.valueOf(warehouse.getTypez()));
                        
                        

                        coOperativeDataList.add(coOperativeCode);
                        coOperativeDataList.add(coOperativeName);
                        coOperativeDataList.add(coOperativeType);
                        coOperativeObject.setData(coOperativeDataList);
                        coOperativeObjectList.add(coOperativeObject);

                    // }
                }
                coOperativeList.setObject(coOperativeObjectList);
            }
            
            // Fetch list of Samithi To Forming Samithi List
           
                samithiWarehouses = locationService.listCoOperativeAndSamithiByRevisionNo(Long.valueOf(revisionNoArray[1]), Warehouse.SAMITHI);

            
            if (!ObjectUtil.isListEmpty(samithiWarehouses)) {
                List<com.sourcetrace.eses.txn.schema.Object> samithiObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
                villageMap = new LinkedHashMap<String, List<String>>();

                for (Warehouse warehouse : samithiWarehouses) {
                    
                    // if (!ObjectUtil.isEmpty(warehouse.getRefCooperative())) {

                        com.sourcetrace.eses.txn.schema.Object samithiObject = new com.sourcetrace.eses.txn.schema.Object();
                        List<Data> samithiDataList = new ArrayList<Data>();

                        Data samithiCode = new Data();
                        samithiCode.setKey(TxnEnrollmentProperties.SAMITHI_CODE);
                        samithiCode.setValue(warehouse.getCode());

                        Data samithiName = new Data();
                        samithiName.setKey(TxnEnrollmentProperties.SAMITHI_NAME);
                        samithiName.setValue(warehouse.getName());
                        
                        /*Data samithiCodeType = new Data();
                        samithiCodeType.setKey(TxnEnrollmentProperties.SAMITHI_TYPE);
                        samithiCodeType.setValue(String.valueOf(warehouse.getTypez()));*/
                        
                        Data samithiStatus = new Data();
                        samithiStatus.setKey(TxnEnrollmentProperties.UTZ_STATS);
                        samithiStatus.setValue(String.valueOf(warehouse.getUtzStatus()));
                        
                        samithiDataList.add(samithiCode);
                        samithiDataList.add(samithiName);
                      //  samithiDataList.add(samithiCodeType);
                        samithiDataList.add(samithiStatus);
                        
                        samithiObject.setData(samithiDataList);
                        samithiObjectList.add(samithiObject);
                    // }
                    processVillage(warehouse);
                }
                samithiList.setObject(samithiObjectList);
            }
            
        } catch (Exception e) {
            LOGGER.error("Exception Occurred.. : " + e.getMessage());
            e.printStackTrace();
        }

        if (!ObjectUtil.isListEmpty(cooperativeWarehouses)) {
            revisionNo = String.valueOf(cooperativeWarehouses.get(0).getRevisionNo());
        }
        if (!ObjectUtil.isListEmpty(samithiWarehouses)) {
            revisionNo = revisionNo+"|"+String.valueOf(samithiWarehouses.get(0).getRevisionNo());
        }
        
        // Build response map object
        Map resp = new LinkedHashMap();
        resp.put(TxnEnrollmentProperties.COOPERATIVE_LIST, coOperativeList);
        resp.put(TxnEnrollmentProperties.SAMITHI_LIST, samithiList);
        resp.put(TxnEnrollmentProperties.SANGHAM_LIST, getSanghamList());
        
       // resp.put(TxnEnrollmentProperties.VILLAGE_LIST, getVillageList());
        resp.put(TxnEnrollmentProperties.COOPERATIVE_DOWNLOAD_REVISION_NO, revisionNo);
        LOGGER.info("========== CoOperativeDownload End ==========");

        return resp;
    }
    
    
    public Collection getSanghamList() {
    	Collection sanghamCollection = new Collection();
    	List<com.sourcetrace.eses.txn.schema.Object> sanghamObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
    	
		AtomicInteger i = new AtomicInteger(0);
		FarmCatalogueMaster farmCatalougeMaster = catalogueService.findFarmCatalogueMasterByName("Sangham");
		if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(d.intValue());
			if(!ObjectUtil.isListEmpty(farmCatalougeList)){
				for(FarmCatalogue farmCatlouge:farmCatalougeList){
					
					com.sourcetrace.eses.txn.schema.Object sanghamObject = new com.sourcetrace.eses.txn.schema.Object();
		            List<Data> sanghamDataList = new ArrayList<Data>();
		            
		            Data sanghamTypeId=new Data();
		            sanghamTypeId.setKey(TxnEnrollmentProperties.GROUP_TYPE_ID);
		            sanghamTypeId.setValue(StringUtil.getExact((String.valueOf(i.incrementAndGet())), 2));
		            
		            Data sanghamTypeName=new Data();
		            sanghamTypeName.setKey(TxnEnrollmentProperties.GROUP_TYPE_NAME);
		            sanghamTypeName.setValue(farmCatlouge.getName());
		            
		            sanghamDataList.add(sanghamTypeId);
		            sanghamDataList.add(sanghamTypeName);
		            
		            sanghamObject.setData(sanghamDataList);
		            
		            sanghamObjectList.add(sanghamObject);
				}
			}
			
			sanghamCollection.setObject(sanghamObjectList);
		}
		
		
		/*if (!ObjectUtil.isEmpty(farmCatalougeMaster)) {
			Double d = new Double(farmCatalougeMaster.getId());
			List<FarmCatalogue> farmCatalougeList = catalogueService.findFarmCatalougeByType(d.intValue());
			sanghamList = farmCatalougeList.stream().collect(Collectors.toMap(
					obj -> (StringUtil.getExact((String.valueOf(i.incrementAndGet())), 2)), FarmCatalogue::getName));
		}*/
		
		return sanghamCollection;
	}
    
    
    
    
    
    /**
     * Gets the village list.
     * @return the village list
     */
    /*@SuppressWarnings("unchecked")
    private Collection getVillageList() {

        Collection villageList = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> villageObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();

        if (villageMap.size() > 0) {

            Iterator it = villageMap.entrySet().iterator();
            while (it.hasNext()) {
                com.sourcetrace.eses.txn.schema.Object villageObject = new com.sourcetrace.eses.txn.schema.Object();
                List<Data> villageDataList = new ArrayList<Data>();

                Map.Entry villageValues = (Map.Entry) it.next();

                Data codeData = new Data();
                codeData.setKey(TxnEnrollmentProperties.VILLAGE_COOPERATIVE_CODE);
                codeData.setValue(villageValues.getKey().toString());

                List<String> infoList = (List<String>) villageValues.getValue();

                Data nameData = new Data();
                nameData.setKey(TxnEnrollmentProperties.VILLAGE_COOPEARATIVE_NAME);
                nameData.setValue(infoList.get(0).toString());
                
                
                Data coOperativeData = new Data();
                coOperativeData.setKey(TxnEnrollmentProperties.COOPEARATIVE_VILLAGE_REF);
                coOperativeData.setValue(infoList.get(1).toString());
                
                
                Data samithiData = new Data();
                samithiData.setKey(TxnEnrollmentProperties.SAMITHI_VILLAGE_LIST);
                Collection samithiVillageList = new Collection();
                List<com.sourcetrace.eses.txn.schema.Object> samithiVillageObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();

                if (infoList.size() > 1) {
                    for (int i = 1; i <infoList.size(); i++) {
                        com.sourcetrace.eses.txn.schema.Object villageSamithiObject = new com.sourcetrace.eses.txn.schema.Object();
                        List<Data> villageSamithiDataList = new ArrayList<Data>();

                        Data samithiCode = new Data();
                        samithiCode.setKey(TxnEnrollmentProperties.SAMITHI_VILLAGE_REF);
                        samithiCode.setValue(infoList.get(i).toString());

                        villageSamithiDataList.add(samithiCode);
                        villageSamithiObject.setData(villageSamithiDataList);

                        samithiVillageObjectList.add(villageSamithiObject);

                    }// end of for loop
                }// end of if loop for samithi list
                samithiVillageList.setObject(samithiVillageObjectList);
                samithiData.setCollectionValue(samithiVillageList);

                villageDataList.add(codeData);
                villageDataList.add(nameData);
                // villageDataList.add(coOperativeData);
                villageDataList.add(samithiData);

                villageObject.setData(villageDataList);
                villageObjectList.add(villageObject);
            }// end of while loop to check next instance

        }// end of if loop to check village map size
        villageList.setObject(villageObjectList);
        return villageList;
    }*/

    /**
     * Process village.
     * @param warehouse 
     */
    private void processVillage(Warehouse warehouse) {

        for (Village village : warehouse.getVillages()) {
            List<String> values = villageMap.get(village.getCode());
            // To check whether village already exist in village map
            if (ObjectUtil.isListEmpty(values)) {
                values = new ArrayList<String>();
                values.add(0, village.getName());
                if (!ObjectUtil.isEmpty(warehouse.getRefCooperative()))
                    values.add(1, warehouse.getRefCooperative().getCode());

            }
            values.add(values.size(), warehouse.getCode());
            villageMap.put(village.getCode(), values);

        }
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
     * Sets the location service.
     * @param locationService the new location service
     */
    public void setLocationService(ILocationService locationService) {

        this.locationService = locationService;
    }

    /**
     * Gets the location service.
     * @return the location service
     */
    public ILocationService getLocationService() {

        return locationService;
    }
    
   
}
