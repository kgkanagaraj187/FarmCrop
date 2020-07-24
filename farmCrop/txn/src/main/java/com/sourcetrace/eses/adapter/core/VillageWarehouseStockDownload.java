/*
 * VillageWarehouseStockDownload.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.traceability.ProcurementTraceabilityStock;
import com.ese.entity.traceability.ProcurementTraceabilityStockDetails;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

@Component
public class VillageWarehouseStockDownload implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(VillageWarehouseStockDownload.class
            .getName());
    @Autowired
    private IProductDistributionService productDistributionService;
    @Autowired
    private IAgentService agentService;
    @Autowired
    private IDeviceService deviceService;
   
    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {
    	LOGGER.info("----------Village Warehouse Stock Download Start ----------");
        /** REQUEST HEAD DATA **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);
        String txnType = head.getTxnType();
        String tenantId = head.getTenantId();
        String agentId = head.getAgentId();
        String serialNumber = head.getSerialNo();
        Device device = deviceService.findDeviceBySerialNumber(serialNumber);
        Agent agent = agentService.findAgentByProfileAndBranchId(agentId,device.getBranchId());
        if (ObjectUtil.isEmpty(agent)) {
            throw new SwitchException(ITxnErrorCodes.INVALID_AGENT);
        }
        String season=(String) reqData.get(TxnEnrollmentProperties.CURRENT_SEASON_CODE);
        String revisionNo = (String) reqData
                .get(TxnEnrollmentProperties.VILLAGE_WAREHOUSE_STOCK_DOWNLOAD_REVISION_NO);
        if (StringUtil.isEmpty(revisionNo))
            throw new SwitchException(ITxnErrorCodes.EMPTY_VILLAGE_WAREHOUSE_STOCK_REVISION_NO);
        LOGGER.info("REVISION NO" + revisionNo);

        /** FORMING WAREHOUSE LIST OBJECT **/
        Map resp = new HashMap();
        Collection collection = new Collection();
        List<Object> listOfVillageObject = new ArrayList<Object>();
        List<CityWarehouse> cityWarehouses = new ArrayList<CityWarehouse>();
        List<java.lang.Object[]> ptstock = new ArrayList<>();
        if (tenantId.equalsIgnoreCase("chetna") || tenantId.equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
        	 ptstock = productDistributionService.listProcurementTraceabilityStockbyAgent(agentId, Long.valueOf(revisionNo),season);
    	}
		if(tenantId.equalsIgnoreCase("kpf") || tenantId.equalsIgnoreCase("gar") || tenantId.equalsIgnoreCase("wub")){
			cityWarehouses = productDistributionService
	                .listProcurementStockByAgentIdRevisionNo(agentId, Long.valueOf(revisionNo));
		}else {
		
		 cityWarehouses = productDistributionService
	                .listCityWarehouseByAgentIdRevisionNo(agentId, Long.valueOf(revisionNo));
		
	}
        // productDistributionService.findCityWarehouseByAgentId(agentId);
        DecimalFormat formatter = new DecimalFormat("0.000");
        if(!tenantId.equalsIgnoreCase("chetna") && !tenantId.equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
        for (CityWarehouse cityWarehouse : cityWarehouses) {
            Data villageCodeData = new Data();
            villageCodeData.setKey(TransactionProperties.VILLAGE_CODE);
            if(!tenantId.equalsIgnoreCase("kpf")||!tenantId.equalsIgnoreCase("wub")||!tenantId.equalsIgnoreCase("gar")){
            villageCodeData.setValue((ObjectUtil.isEmpty(cityWarehouse.getVillage()) ? ""
                    : cityWarehouse.getVillage().getCode()));
            }else{
            	  villageCodeData.setValue((ObjectUtil.isEmpty(cityWarehouse.getCoOperative()) ? ""
                          : cityWarehouse.getCoOperative().getCode()));
            }
            Data productCodeData = new Data();
            productCodeData.setKey(TxnEnrollmentProperties.WAREHOUSE_PRODUCT_CODE);
            productCodeData
                    .setValue((ObjectUtil.isEmpty(cityWarehouse.getProcurementProduct()) ? ""
                            : cityWarehouse.getProcurementProduct().getCode()));
            Data bagsData = new Data();
            bagsData.setKey(TxnEnrollmentProperties.NO_OF_BAGS);
            bagsData.setValue(String.valueOf(cityWarehouse.getNumberOfBags()));
            Data grossData = new Data();
            grossData.setKey(TxnEnrollmentProperties.GROSS_WEIGHT);
            grossData.setValue(formatter.format(cityWarehouse.getGrossWeight()));

            Data gradeData = new Data();
            gradeData.setKey(TxnEnrollmentProperties.GRADE_CODE);
            gradeData.setValue(cityWarehouse.getQuality());
            
           

            List<Data> warehouseData = new ArrayList<Data>();
            warehouseData.add(villageCodeData);
            warehouseData.add(productCodeData);
            warehouseData.add(bagsData);
            warehouseData.add(grossData);
            warehouseData.add(gradeData);

            Object warehouseObject = new Object();
            warehouseObject.setData(warehouseData);

            listOfVillageObject.add(warehouseObject);

        }
        }else{
        	if (ptstock.size() > 0) {
        	 for (java.lang.Object[] ptstockArray  : ptstock) {
                 Data villageCodeData = new Data();
                 villageCodeData.setKey(TransactionProperties.VILLAGE_CODE);
                 villageCodeData.setValue((ObjectUtil.isEmpty(ptstockArray) ? "" : String.valueOf(ptstockArray[0])));
                
                 Data productCodeData = new Data();
                 productCodeData.setKey(TxnEnrollmentProperties.WAREHOUSE_PRODUCT_CODE);
                 productCodeData
                         .setValue((ObjectUtil.isEmpty(ptstockArray) ? "" : String.valueOf(ptstockArray[1])));
                 Data bagsData = new Data();
                 bagsData.setKey(TxnEnrollmentProperties.NO_OF_BAGS);
                 bagsData.setValue((ObjectUtil.isEmpty(ptstockArray) ? "" : String.valueOf(ptstockArray[2])));
                 Data grossData = new Data();
                 grossData.setKey(TxnEnrollmentProperties.GROSS_WEIGHT);
                 grossData.setValue((ObjectUtil.isEmpty(ptstockArray) ? "" : String.valueOf(ptstockArray[3])));

                 Data gradeData = new Data();
                 gradeData.setKey(TxnEnrollmentProperties.GRADE_CODE);
                 gradeData.setValue((ObjectUtil.isEmpty(ptstockArray) ? "" : String.valueOf(ptstockArray[4])));
                 
                

                 List<Data> warehouseData = new ArrayList<Data>();
                 warehouseData.add(villageCodeData);
                 warehouseData.add(productCodeData);
                 warehouseData.add(bagsData);
                 warehouseData.add(grossData);
                 warehouseData.add(gradeData);

                 Object warehouseObject = new Object();
                 warehouseObject.setData(warehouseData);

                 listOfVillageObject.add(warehouseObject);

             }
           }
        }
        collection.setObject(listOfVillageObject);

        if (!ObjectUtil.isListEmpty(cityWarehouses)) {
            revisionNo = String.valueOf(cityWarehouses.get(0).getRevisionNo());
        }
   
        resp.put(TxnEnrollmentProperties.WAREHOUSE_STOCK_LIST, collection);
        resp.put(TxnEnrollmentProperties.VILLAGE_WAREHOUSE_STOCK_DOWNLOAD_REVISION_NO,
                revisionNo);
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
     * Sets the product distribution service.
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    /**
     * Sets the agent service.
     * @param agentService the new agent service
     */
    public void setAgentService(IAgentService agentService) {

        this.agentService = agentService;
    }

    /**
     * Gets the agent service.
     * @return the agent service
     */
    public IAgentService getAgentService() {

        return agentService;
    }

    /**
     * Gets the product distribution service.
     * @return the product distribution service
     */
    public IProductDistributionService getProductDistributionService() {

        return productDistributionService;
    }

}
