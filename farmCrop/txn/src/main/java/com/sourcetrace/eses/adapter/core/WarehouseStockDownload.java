package com.sourcetrace.eses.adapter.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;

@Component
public class WarehouseStockDownload implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(WarehouseStockDownload.class.getName());
    @Autowired
    private IAgentService agentService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IClientService clientService;
    
    @Autowired
    private ILocationService locationService;
    
    @Autowired
    private IDeviceService deviceService;
    
    @Autowired
	private IPreferencesService preferncesService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {
    	
    	LOGGER.info("---------- Warehouse Stock Download Start ----------");
    	
        /** REQUEST HEAD DATA **/	
        Head head = (Head) reqData.get(TransactionProperties.HEAD);

        String agentId = head.getAgentId();
        String serialNumber = head.getSerialNo();
        Device device = deviceService.findDeviceBySerialNumber(serialNumber);
        Agent agent = agentService.findAgentByProfileAndBranchId(agentId,device.getBranchId());
        if (ObjectUtil.isEmpty(agent)) {
            throw new SwitchException(ITxnErrorCodes.INVALID_AGENT);
        }

        String revisionNo = (String) reqData
                .get(TxnEnrollmentProperties.WAREHOUSE_PRODUCT_STOCK_DOWNLOAD_REVISION_NO);
        String requestSeasonCode=(String) reqData
                .get(TxnEnrollmentProperties.CURRENT_SEASON_CODE); 
        if (StringUtil.isEmpty(revisionNo))
            throw new SwitchException(ITxnErrorCodes.EMPTY_WAREHOUSE_PRODUCT_STOCK_REVISION_NO);
        LOGGER.info("REVISION NO" + revisionNo);
        
        
        if(!StringUtil.isLong(revisionNo)){
        	revisionNo = "0";
        }
        
        /** FORMING WAREHOUSE STOCK LIST OBJECT **/
        Map resp = new HashMap();
        Collection collection = new Collection();
        List<Object> listOfWarehouseObject = new ArrayList<Object>();
        List<WarehouseProduct> warehouseProducts = null;
        DecimalFormat formatter = new DecimalFormat("0.000");
        
        Request requestData =  (Request) ReflectUtil.getCurrentTxnRequestData();
		String currentBranch = ObjectUtil.isEmpty(requestData) ? null : requestData.getHead().getBranchId();
		String seasonCode= clientService.findCurrentSeasonCodeByBranchId(currentBranch);
		String tenantId = requestData.getHead().getTenantId();
		 ESESystem preferencesOne = preferncesService.findPrefernceById("1");
 		Integer WarehouseSeason = 0;
        /*if (!ObjectUtil.isEmpty(agent.getCooperative()))
            warehouseProducts = productService.listWarehouseProductByAgentIdRevisionNo(agent.getId(), agent
                    .getCooperative().getId(),Long.valueOf(revisionNo)); */

 		if (!StringUtil.isEmpty(preferencesOne) && !StringUtil.isEmpty(preferencesOne.getPreferences().get(ESESystem.ENABLE_WAREHOUSEDOWNLOAD_SEASON))
				&& !preferencesOne.getPreferences().get(ESESystem.ENABLE_WAREHOUSEDOWNLOAD_SEASON).equalsIgnoreCase(null))
            WarehouseSeason = Integer.valueOf(preferencesOne.getPreferences().get(ESESystem.ENABLE_WAREHOUSEDOWNLOAD_SEASON));
 		
    		if (WarehouseSeason == 1){
    			warehouseProducts = productService.listWarehouseProductByAgentRevisionNoStockByBatch(agent.getId(),Long.valueOf(revisionNo));
    		}
    		else{
    			warehouseProducts = productService.listWarehouseProductByAgentRevisionNoStockAndSeasonCodeByBatch(agent.getId(),Long.valueOf(revisionNo),seasonCode);
    		}
	
        
		//productService.listWarehouseProductByAgentId(agent.getId(), agent.getCooperative().getId());
        if (!ObjectUtil.isListEmpty(warehouseProducts)) {
            for (WarehouseProduct warehouseProduct : warehouseProducts) {
                Data subCategoryCodeData = new Data();
                subCategoryCodeData.setKey(TxnEnrollmentProperties.WAREHOUSE_CATEGORY_CODE);
                subCategoryCodeData
                        .setValue(((ObjectUtil.isEmpty(warehouseProduct.getProduct()) && ObjectUtil
                                .isEmpty(warehouseProduct.getProduct().getSubcategory())) ? ""
                                : warehouseProduct.getProduct().getSubcategory().getCode()));

                Data productCodeData = new Data();
                productCodeData.setKey(TxnEnrollmentProperties.WAREHOUSE_PRODUCT_CODE);
                productCodeData.setValue(((ObjectUtil.isEmpty(warehouseProduct.getProduct())) ? ""
                        : warehouseProduct.getProduct().getCode()));

                Data statusData = new Data();
                statusData.setKey(TxnEnrollmentProperties.STOCK);
                statusData.setValue(formatter.format(warehouseProduct.getStock()));
                
                Data branchData = new Data();
                branchData.setKey(TxnEnrollmentProperties.BATCH_NO);
                branchData.setValue(warehouseProduct.getBatchNo());
                
     
        		Data seasonData = new Data();
                seasonData.setKey(TxnEnrollmentProperties.SEASON);
                seasonData.setValue(warehouseProduct.getSeasonCode());
                
                
                List<Data> warehouseData = new ArrayList<Data>();
                warehouseData.add(subCategoryCodeData);
                warehouseData.add(productCodeData);
                warehouseData.add(statusData);
                warehouseData.add(branchData);
                warehouseData.add(seasonData);
                Object warehouseObject = new Object();
                warehouseObject.setData(warehouseData);

                listOfWarehouseObject.add(warehouseObject);
            }// warehouse Product
        }// empty check warehouse products list

        collection.setObject(listOfWarehouseObject);
//        
        if (!ObjectUtil.isListEmpty(warehouseProducts)) {
            revisionNo = String.valueOf(warehouseProducts.get(0).getRevisionNo());
        }
        
        resp.put(TxnEnrollmentProperties.WAREHOUSE_STOCK_LIST, collection);
        resp.put(TxnEnrollmentProperties.WAREHOUSE_PRODUCT_STOCK_DOWNLOAD_REVISION_NO, revisionNo);
        LOGGER.info("---------- Warehouse Stock Download END ----------");
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

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}
	
	
}
