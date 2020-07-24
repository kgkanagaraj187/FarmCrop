/*
 * OfflineDistributionSchedulerTask.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.OfflineDistribution;
import com.sourcetrace.eses.order.entity.txn.OfflineDistributionDetail;
import com.sourcetrace.eses.order.entity.txn.OfflineProductReturn;
import com.sourcetrace.eses.order.entity.txn.OfflineProductReturnDetail;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.ProductReturn;
import com.sourcetrace.eses.order.entity.txn.ProductReturnDetail;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

@Component
public class OfflineProductReturnSchedulerTask{

    private static final Logger LOGGER = Logger
            .getLogger(OfflineProductReturnSchedulerTask.class.getName());
    private static final String NOT_APPLICABLE = "N/A";
    @Autowired
    private IProductDistributionService productDistributionService;
    @Autowired
    private IAgentService agentService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IServicePointService servicePointService;
    @Autowired
    private IFarmerService farmerService;
    @Autowired
    private IProductService productService;
    @Autowired
    private ILocationService locationService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private IPreferencesService preferncesService;
    @Resource(name = "datasources")
    private Map<String, DataSource> datasources;

    /**
     * Sets the product distribution service.
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(
            IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    /**
     * Gets the product distribution service.
     * @return the product distribution service
     */
    public IProductDistributionService getProductDistributionService() {

        return productDistributionService;
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
     * Sets the device service.
     * @param deviceService the new device service
     */
    public void setDeviceService(IDeviceService deviceService) {

        this.deviceService = deviceService;
    }

    /**
     * Gets the device service.
     * @return the device service
     */
    public IDeviceService getDeviceService() {

        return deviceService;
    }

    /**
     * Gets the account service.
     * @return the account service
     */
    public IAccountService getAccountService() {

        return accountService;
    }

    /**
     * Sets the account service.
     * @param accountService the new account service
     */
    public void setAccountService(IAccountService accountService) {

        this.accountService = accountService;
    }

    /**
     * Sets the service point service.
     * @param servicePointService the new service point service
     */
    public void setServicePointService(IServicePointService servicePointService) {

        this.servicePointService = servicePointService;
    }

    /**
     * Gets the service point service.
     * @return the service point service
     */
    public IServicePointService getServicePointService() {

        return servicePointService;
    }

    /**
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
    }

    /**
     * Gets the farmer service.
     * @return the farmer service
     */
    public IFarmerService getFarmerService() {

        return farmerService;
    }

    /**
     * Sets the product service.
     * @param productService the new product service
     */
    public void setProductService(IProductService productService) {

        this.productService = productService;
    }

    /**
     * Gets the product service.
     * @return the product service
     */
    public IProductService getProductService() {

        return productService;
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

    public IPreferencesService getPreferncesService() {
    
        return preferncesService;
    }

    public void setPreferncesService(IPreferencesService preferncesService) {
    
        this.preferncesService = preferncesService;
    }

    /*
     * (non-Javadoc)
     * @see java.util.TimerTask#run()
     */
    public void process() {
    	Set<PMTImageDetails> distImgList = new HashSet<PMTImageDetails>();
        String batchNo="";
    	  Vector<String> tenantIds=new Vector(datasources.keySet());
          for (String tenantId : tenantIds) {
              List<OfflineProductReturn> offlineProductReturns =  Collections.synchronizedList(productDistributionService
                      .listOfflineProductReturn(tenantId));
              
            for (OfflineProductReturn offlineProductReturn : offlineProductReturns) {                
                int statusCode = ESETxnStatus.SUCCESS.ordinal();
                String statusMsg = ESETxnStatus.SUCCESS.toString();
                
                try {
                    WarehouseProduct warehouseProduct = new WarehouseProduct();
                    if (StringUtil.isEmpty(offlineProductReturn.getAgentId())) {
                        throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ID_EMPTY);
                    }
                    Agent agent = agentService.findAgentByAgentId(offlineProductReturn.getAgentId(),tenantId,offlineProductReturn.getBranchId());
                    if (ObjectUtil.isEmpty(agent)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.INVALID_AGENT);
                    }

                    ESEAccount agentAccount = accountService.findAccountByProfileIdAndProfileType(
                            agent.getProfileId(), ESEAccount.AGENT_ACCOUNT,tenantId);
                    if (ObjectUtil.isEmpty(agentAccount))
                        throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_UNAVAILABLE);
                    if (ESEAccount.INACTIVE == agentAccount.getStatus())
                        throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_INACTIVE);
                    if (StringUtil.isEmpty(offlineProductReturn.getDeviceId())) {
                        throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);
                    }

                    Device device = deviceService
                            .findDeviceBySerialNumber(offlineProductReturn.getDeviceId(),tenantId);
                    if (ObjectUtil.isEmpty(device)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DEVICE);
                    }
                    if (StringUtil.isEmpty(offlineProductReturn.getServicePointId())) {
                        throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERV_POINT_ID);
                    }

                    ServicePoint servicePoint = servicePointService.findByServicePointById(
                            Long.valueOf(offlineProductReturn.getServicePointId()),tenantId);
                    if (ObjectUtil.isEmpty(servicePoint)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.INVALID_SERVICE_POINT);
                    }
                    String productDate = offlineProductReturn.getProductReturnDate();
                    String receiptNo = offlineProductReturn.getReceiptNo();
                    if (StringUtil.isEmpty(receiptNo)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_RECEIPT_NO);
                    }
                    String villageCode = offlineProductReturn.getVillageCode();
                    if (StringUtil.isEmpty(villageCode)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_VILLAGE_CODE);
                    }

                    Village village = locationService.findVillageByCode(villageCode,tenantId); 
                    if (ObjectUtil.isEmpty(village)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.VILLAGE_NOT_EXIST);
                    }
                    ProductReturn exist = productDistributionService.findProductReturnByReceiptNoTxnType(
                            receiptNo, offlineProductReturn.getTxnType(),tenantId);
                    if (!ObjectUtil.isEmpty(exist)) {
                        throw new SwitchException(TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER.equalsIgnoreCase(offlineProductReturn.getTxnType())
                                        ? ITxnErrorCodes.PRODUCT_RETURN_FROM_FARMER_EXIST
                                        : (TransactionTypeProperties.PRODUCT_RETURN_FROM_FIELDSTAFF
                                                .equalsIgnoreCase(offlineProductReturn.getTxnType())
                                                        ? ITxnErrorCodes.PRODUCT_RETURN_FROM_FARMER_EXIST
                                                        : ITxnErrorCodes.PRODUCT_RETURN_FROM_FIELDSTAFF_EXIST));
                    }
                    
                    String currentSeasonCode = offlineProductReturn.getCurrentSeasonCode();
                    if(StringUtil.isEmpty(currentSeasonCode)){
                    	throw new SwitchException(ITxnErrorCodes.EMPTY_CURRENT_SEASON_CODE);
                    }
                    
                   HarvestSeason harvestSeason= clientService.findSeasonByCodeByTenant(currentSeasonCode, tenantId);
                   if(ObjectUtil.isEmpty(harvestSeason)){
                	   throw new SwitchException(ITxnErrorCodes.SEASON_NOT_EXIST);
                   }
    
                    Farmer farmer = null;
                    Season season = null;
                    ESEAccount farmerAccount = null;
                    boolean registeredFarmer = true;
                    if (TransactionTypeProperties.PRODUCT_DISTRIBUTION
                            .equalsIgnoreCase(offlineProductReturn.getTxnType())
                            && StringUtil.isEmpty(offlineProductReturn.getWarehouseCode())) {
                        registeredFarmer = false;
                    }
                    if (TransactionTypeProperties.PRODUCT_DISTRIBUTION
                            .equalsIgnoreCase(offlineProductReturn.getTxnType())
                            || TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                                    .equalsIgnoreCase(offlineProductReturn.getTxnType())) {
    
                        String farmerId = offlineProductReturn.getFarmerId();
                        if (registeredFarmer) {
                            if (StringUtil.isEmpty(farmerId)) {
                                throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARMER_ID);
                            }

                            //farmer = farmerService.findFarmerByFarmerId(farmerId,tenantId);

                            farmer = farmerService.findFarmerByFarmerId(farmerId, tenantId);

                            if (ObjectUtil.isEmpty(farmer)) {
                                throw new OfflineTransactionException(ITxnErrorCodes.FARMER_NOT_EXIST);
                            }
                            if (farmer.getStatus() == Farmer.Status.INACTIVE.ordinal()) {
                                throw new OfflineTransactionException(ITxnErrorCodes.FARMER_INACTIVE);
                            }

                            //farmerAccount = farmerService.findAccountBySeassonProcurmentProductFarmer(0L, farmer.getId(),tenantId);

                            farmerAccount = farmerService.findAccountBySeassonProcurmentProductFarmer(0L, farmer.getId(), tenantId);

                            if (ObjectUtil.isEmpty(farmerAccount))
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.FARMER_ACCOUNT_UNAVAILABLE);
                            if (ESEAccount.INACTIVE == farmerAccount.getStatus())
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.FARMER_ACCOUNT_INACTIVE);
                            if (StringUtil.isEmpty(offlineProductReturn.getWarehouseCode())) {
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.EMPTY_SAMITHI_CODE);
                            }
                            Warehouse warehouse = locationService
                                    .findSamithiByCode(offlineProductReturn.getWarehouseCode(),tenantId);
                            if (ObjectUtil.isEmpty(warehouse)) {
                                throw new OfflineTransactionException(ITxnErrorCodes.SAMITHI_NOT_EXIST);
                            }
                        }
                        if (StringUtil.isEmpty(offlineProductReturn.getProductReturnDate())) {
                            throw new OfflineTransactionException(
                                    ITxnErrorCodes.EMPTY_PRODCUT_RETURN_DATE);
                        }
                        /*if (StringUtil.isEmpty(offlineDistribution.getSeasonCode()))
                            throw new SwitchException(ITxnErrorCodes.EMPTY_SEASON_CODE);*/
                        season = productDistributionService.findSeasonBySeasonCodeByTenantId(offlineProductReturn.getSeasonCode(), tenantId);
                       /* if (ObjectUtil.isEmpty(season))
                            throw new SwitchException(ITxnErrorCodes.SEASON_NOT_EXIST);*/
                    }
    
                    Warehouse warehouse = agent.getCooperative();
    
                    if (AgentType.COOPERATIVE_MANAGER.equals(agent.getAgentType().getCode())) {
    
                        if (ObjectUtil.isEmpty(warehouse)) {
                            throw new OfflineTransactionException(
                                    ITxnErrorCodes.AGENT_SAMITHI_UNAVAILABLE);
                        }
                    }
    
                    ProductReturn productReturn = new ProductReturn();
                    AgroTransaction agroTransaction = new AgroTransaction();
                    productReturn.setAgroTransaction(agroTransaction);
    
                    /** FORMING AGRO TRANSACTION OBJECT **/
                    agroTransaction.setReceiptNo(receiptNo);
                    agroTransaction.setAgentId(agent.getProfileId());
                    agroTransaction.setAgentName((ObjectUtil.isEmpty(agent.getPersonalInfo()) ? ""
                            : agent.getPersonalInfo().getAgentName()));
                    agroTransaction.setDeviceId(device.getCode());
                    agroTransaction.setDeviceName(device.getName());
                    agroTransaction.setServicePointId(!ObjectUtil.isEmpty(warehouse)
                            ? !StringUtil.isEmpty(warehouse.getCode()) ? warehouse.getCode() : "" : "");
                    agroTransaction.setServicePointName(!ObjectUtil.isEmpty(warehouse)
                            ? !StringUtil.isEmpty(warehouse.getName()) ? warehouse.getName() : "" : "");
                    agroTransaction.setTxnType(offlineProductReturn.getTxnType());
                    agroTransaction.setOperType(ESETxn.ON_LINE);
                    agroTransaction.setProductStock(WarehouseProduct.StockType.AGENT_STOCK.name());
                    if (TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                                    .equalsIgnoreCase(offlineProductReturn.getTxnType())) {
                        if (registeredFarmer) {
                        	productReturn.setVillage(farmer.getVillage());
                            agroTransaction.setFarmerId(farmer.getFarmerId());
                            agroTransaction
                                    .setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
                            agroTransaction.setSamithi(farmer.getSamithi());
                            productReturn.setFarmerId(farmer.getFarmerId());
                            productReturn
                                    .setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
                        } else {
                        	productReturn.setVillage(village);
                            agroTransaction.setFarmerId(NOT_APPLICABLE);
                            agroTransaction.setFarmerName(offlineProductReturn.getFarmerId());
                            productReturn.setMobileNumber(offlineProductReturn.getMobileNumber());
                            productReturn.setFarmerId(NOT_APPLICABLE);
                            productReturn.setFarmerName(offlineProductReturn.getFarmerId());
                        }
                        productReturn.setBranchId(offlineProductReturn.getBranchId());
                        productReturn.setSeasonCode(harvestSeason.getCode());
                        productReturn.setReceiptNumber(receiptNo);
                        productReturn.setTxnTime(
                                DateUtil.convertStringToDate(productDate, DateUtil.TXN_DATE_TIME));
                        productReturn.setSeason(season);
                        agroTransaction.setProfType(Profile.AGENT);
                        agroTransaction.setIntBalance(agentAccount.getCashBalance());
                        agroTransaction.setAccount(agentAccount);
                        productReturn.setWarehouse(warehouse);
                        productReturn.setAgent(agent);
                        productReturn.setTxnType(offlineProductReturn.getTxnType());
                        productReturn.setStockType(offlineProductReturn.getStockType());
                        productReturn.setServicePointId(!ObjectUtil.isEmpty(warehouse)
                                ? !StringUtil.isEmpty(warehouse.getCode()) ? warehouse.getCode() : ""
                                : "");
                        productReturn.setServicePointName(!ObjectUtil.isEmpty(warehouse)
                                ? !StringUtil.isEmpty(warehouse.getName()) ? warehouse.getName() : ""
                                : "");
                        productReturn.setAgentId(agent.getProfileId());
                        productReturn.setTenantId(tenantId);
                        productReturn.setAgentName((ObjectUtil.isEmpty(agent.getPersonalInfo()) ? ""
                                : agent.getPersonalInfo().getAgentName()));
                        productReturn.setProductStock(WarehouseProduct.StockType.AGENT_STOCK.name());
                    } else {
                        agroTransaction.setProfType(Profile.AGENT);
                    }
                    try {
                        agroTransaction.setTxnTime(
                                DateUtil.convertStringToDate(productDate, DateUtil.TXN_DATE_TIME));
                    } catch (Exception e) {
                        throw new SwitchException(ITxnErrorCodes.DATA_CONVERSION_ERROR);
                    }
    
                    /** FORMING DISTRIBUTION DETAIL OBJECT **/
                    Set<ProductReturnDetail> productReturnDetailList = new HashSet<ProductReturnDetail>();
    
                    Set<OfflineProductReturnDetail> offlineProductReturnDetailList = Collections.synchronizedSet(
                            offlineProductReturn.getOfflineProductReturnDetail());
                    if (!ObjectUtil.isListEmpty(offlineProductReturnDetailList)) {
                        for (OfflineProductReturnDetail offlineProductReturnDetail : offlineProductReturnDetailList) {
                        	ProductReturnDetail detail = new ProductReturnDetail();
    
                            if (StringUtil.isEmpty(offlineProductReturnDetail.getProductCode())) {
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.EMPTY_PRODUCT_CODE);
    
                            }
                            Product product = productService.findProductByCodeByTenantId(offlineProductReturnDetail.getProductCode(), tenantId);
                            if (ObjectUtil.isEmpty(product)) {
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.PRODUCT_DOES_NOT_EXIST);
                            }
                            detail.setProduct(product);
                            detail.setUnit(product.getUnit());
    
                            if(StringUtil.isEmpty(offlineProductReturnDetail.getBatchNo())){
                                batchNo="NA";
                                
                            }
                           
                           
                            if(offlineProductReturnDetail.getBatchNo()!=null){
                            // Validation Skipped For Farmer Product Return
                            if (TransactionTypeProperties.PRODUCT_RETURN_FROM_FIELDSTAFF
                                            .equalsIgnoreCase(offlineProductReturn.getTxnType())) {
                                
                                if(!tenantId.equalsIgnoreCase("lalteer")){
                                    warehouseProduct = productDistributionService.findFieldStaffAvailableStockBySeasonAndBatch(agent.getProfileId(),
                                                product.getId(), productReturn.getSeasonCode(),offlineProductReturnDetail.getBatchNo(),tenantId,agent.getBranchId());
                                }else{
                                    
                                    warehouseProduct = productDistributionService.findFieldStaffAvailableStockByBatch(agent.getProfileId(),
                                            product.getId(),offlineProductReturnDetail.getBatchNo(),tenantId);
                                }                             
                           
                          
                            }else {
                                if(!tenantId.equalsIgnoreCase("lalteer")){
                                    warehouseProduct = productDistributionService.findFieldStaffAvailableStockBySeasonAndBatch(agent.getProfileId(),
                                            product.getId(), productReturn.getSeasonCode(),offlineProductReturnDetail.getBatchNo(),tenantId,agent.getBranchId());
                                }else{
                                    warehouseProduct = productDistributionService.findFieldStaffAvailableStockByBatch(agent.getProfileId(),
                                            product.getId(),offlineProductReturnDetail.getBatchNo(),tenantId);
                               }
                        }
                        }
                           /* if (ObjectUtil.isEmpty(warehouseProduct)){
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.WAREHOUSEPRODUCT_NOT_EXIST);
                            }*/
                            	  if(warehouseProduct==null){
                               	   warehouseProduct = new WarehouseProduct();
                               	   warehouseProduct.setStock(0);
                               	   warehouseProduct.setCostPrice(0.00);
                                  }
                           
    
                            try {
    
                                detail.setQuantity(
                                        Double.valueOf(offlineProductReturnDetail.getQuantity()));
                                /*
                                 * detail.setPricePerUnit((!StringUtil.isEmpty(offlineDistributionDetail
                                 * .getPricePerUnit()))? Double.valueOf(offlineDistributionDetail
                                 * .getPricePerUnit()) : 0.00);
                                 */
                                detail.setSellingPrice(
                                        StringUtil.isEmpty(offlineProductReturnDetail.getSellingPrice())
                                                ? 0.00
                                                : Double.valueOf(
                                                        offlineProductReturnDetail.getSellingPrice()));
                                
                                detail.setCostPrice(
                                        StringUtil.isEmpty(warehouseProduct.getCostPrice())
                                                ? 0.00
                                                : Double.valueOf(
                                                        warehouseProduct.getCostPrice()));
                                detail.setSubTotal(
                                        StringUtil.isEmpty(offlineProductReturnDetail.getSubTotal())
                                                ? 0.00
                                                : Double.valueOf(
                                                        offlineProductReturnDetail.getSubTotal()));
                                detail.setExistingQuantity(String.valueOf(warehouseProduct.getStock()));
                                detail.setCurrentQuantity(String.valueOf(warehouseProduct.getStock()+detail.getQuantity()));
                               
                                if(!StringUtil.isEmpty(offlineProductReturnDetail.getBatchNo())){
                                    detail.setBatchNo(offlineProductReturnDetail.getBatchNo());
                                }else{
                                    detail.setBatchNo("NA");
                                }
    
                            } catch (Exception e) {
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.DATA_CONVERSION_ERROR);
                            }
    
                            // Available stock validation - Validation Skipped For Farmer Product Return
                            if (TransactionTypeProperties.PRODUCT_RETURN_FROM_FIELDSTAFF
                                            .equalsIgnoreCase(offlineProductReturn.getTxnType())) {
                                Double quantity = 0.0;
                                for (ProductReturnDetail existingDetail : productReturnDetailList) {
                                    if (existingDetail.getProduct().getCode().equalsIgnoreCase(
                                            warehouseProduct.getProduct().getCode()) && existingDetail.getBatchNo().equals(warehouseProduct.getBatchNo())) {
                                        quantity = quantity + existingDetail.getQuantity();
    
                                    }
                                }
                                if (quantity + detail.getQuantity() > warehouseProduct.getStock())
                                    throw new OfflineTransactionException(
                                            ITxnErrorCodes.INSUFFICIENT_BAL);
                            }
                            detail.setProductReturn(productReturn);
                            productReturnDetailList.add(detail);
                        }
                        productReturn.setProductReturnDetail(productReturnDetailList);
                    }
    
                    // Parsing payment amount
                    double paymentAmount = 0;
    
                    if (!StringUtil.isEmpty(offlineProductReturn.getPaymentAmt())) {
                        try {
                            paymentAmount = Double.valueOf(offlineProductReturn.getPaymentAmt());
                        } catch (Exception e) {
                            LOGGER.info("Could not Parse paymentAount");
                            e.printStackTrace();
                        }
                    }
    
                    // Setting payment amount
                    // distribution.setPaymentAmount(paymentAmount);
    
                    double totalAmount = 0;
                    for (ProductReturnDetail detail : productReturnDetailList) {
                        totalAmount = totalAmount + detail.getSubTotal();
                    }
    
                    double amountWithTax = 0;
                    if (!StringUtil.isEmpty(offlineProductReturn.getTax())) {
                       /* amountWithTax = (offlineDistribution.getTax() / 100 * totalAmount)
                                + totalAmount;*/
                        amountWithTax = offlineProductReturn.getTax()+totalAmount;
                        productReturn.setFinalAmount(amountWithTax);
                    } else {
                    	productReturn.setFinalAmount(totalAmount);
                    }
                    productReturn.setPaymentMode(StringUtil.isEmpty(offlineProductReturn.getPaymentMode())
                            ? "" : offlineProductReturn.getPaymentMode());
                    productReturn.setFreeDistribution(offlineProductReturn.getFreeDistribution());
                    productReturn.setTxnAmount(totalAmount);
                    productReturn.setTax(StringUtil.isEmpty(offlineProductReturn.getTax()) ? 0.00
                            : offlineProductReturn.getTax());
                    productReturn.setTotalAmount(totalAmount);
                    if(!ObjectUtil.isEmpty(offlineProductReturn.getPaymentMode())){
	                    if (offlineProductReturn.getPaymentMode().equals("CS")) {
	                    	productReturn
	                                .setPaymentAmount(Double.valueOf(offlineProductReturn.getPaymentAmt()));
	                    } else if (offlineProductReturn.getPaymentMode().equals("CR")) {
	                    	productReturn.setPaymentAmount(productReturn.getFinalAmount());
	                    }
                    }else  {
                    	productReturn.setPaymentAmount(0.00);
                    }
                   agroTransaction.setTxnAmount(amountWithTax);
                   
                   /* if (TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                            .equalsIgnoreCase(offlineProductReturn.getTxnType())) {
                        agroTransaction
                                .setTxnDesc(ProductReturn.PRODUCT_RETURN_FROM_FARMER_DESCRIPTION);
                        agroTransaction.setIntBalance(
                                registeredFarmer ? farmerAccount.getCashBalance() : 0);
                        agroTransaction.setBalAmount(agroTransaction.getIntBalance() -
                                agroTransaction.getTxnAmount());
                        agroTransaction.setAccount(farmerAccount);
                        agroTransaction.setProfType(Profile.CLIENT);
    
                    } else */if (TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                            .equalsIgnoreCase(offlineProductReturn.getTxnType())) {
                        agroTransaction.setTxnDesc(Distribution.PRODUCT_RETURN_FROM_FARMER_DESCRIPTION);
                        agroTransaction.setBalAmount(
                                agroTransaction.getIntBalance() - agroTransaction.getTxnAmount());
                        agentAccount.setDistributionBalance(agroTransaction.getBalAmount());
                    } else {
                        agroTransaction
                                .setTxnDesc(Distribution.PRODUCT_RETURN_FROM_FIELDSTAFF_DESCRIPTION);
                        agroTransaction.setBalAmount(
                                agroTransaction.getIntBalance() - agroTransaction.getTxnAmount());
                    }
                   
                    /**
                     * SAVING DISTRIBUTION AND DISTRIBUTION DETAIL OBJECT UPDATING ACCOUNT AND WAREHOUSE
                     * PRODUCT OBJECT
                     **/
                    
                    agroTransaction.setProductReturn(productReturn);
                    productReturn.setAgroTransaction(agroTransaction);
                    Set<PMTImageDetails> offlineDistImageDetailList= Collections.synchronizedSet(
                            offlineProductReturn.getPmtImageDetail());
                    try {
                        productDistributionService.saveProductReturnAndProductReturnDetail(productReturn,tenantId);
                        
                        if (!ObjectUtil.isListEmpty(offlineDistImageDetailList)) {
                            for (PMTImageDetails offlineDistImg: offlineDistImageDetailList) {
                            	offlineDistImg.setPmt(productReturn.getId());
                                distImgList.add(offlineDistImg);
                                productReturn.setPmtImageDetail(distImgList);
                    			
                            }
                            
                            productDistributionService.updateProductReturn(productReturn,tenantId);
                        }
                       
                        if (TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                                .equalsIgnoreCase(offlineProductReturn.getTxnType())) {
                            accountService.update(agentAccount,tenantId);
                        }
    
                    } catch (Exception e) {
                        throw new OfflineTransactionException(ITxnErrorCodes.ERROR);
                    }
                } // end try
    
                catch (OfflineTransactionException ote) {
                    statusCode = ESETxnStatus.ERROR.ordinal();
                    statusMsg = ote.getError();
                } catch (Exception e) { // Catches all type of exception except
                    // OfflineTransactionException
                    statusCode = ESETxnStatus.ERROR.ordinal();
                    statusMsg = e.getMessage().substring(0,
                            e.getMessage().length() > 40 ? 40 : e.getMessage().length());
                }
                // Update the error msg in Offline Distribution
                offlineProductReturn.setStatusCode(statusCode);
                offlineProductReturn.setStatusMsg(statusMsg);
                productDistributionService.updateOfflineProductReturn(offlineProductReturn,tenantId);
    
            } // end outer for*/
    
        }
        
     System.gc();   
    }
}
