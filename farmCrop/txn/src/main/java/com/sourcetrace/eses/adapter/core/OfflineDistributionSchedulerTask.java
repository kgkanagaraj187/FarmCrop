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
import com.sourcetrace.eses.order.entity.txn.OfflinePMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
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
import com.sourcetrace.esesw.entity.profile.DistributionBalance;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

@Component
public class OfflineDistributionSchedulerTask{

    private static final Logger LOGGER = Logger
            .getLogger(OfflineDistributionSchedulerTask.class.getName());
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
        String batchNo="";
    	  Vector<String> tenantIds=new Vector(datasources.keySet());
    	  Set<PMTImageDetails> distImgList = new HashSet<PMTImageDetails>();
          for (String tenantId : tenantIds) {
        	  try{
              List<OfflineDistribution> offlineDistributions =  Collections.synchronizedList(productDistributionService
                      .listOfflineDistribution(tenantId));
              
            for (OfflineDistribution offlineDistribution : offlineDistributions) {
                
                int statusCode = ESETxnStatus.SUCCESS.ordinal();
                String statusMsg = ESETxnStatus.SUCCESS.toString();
                
                try {
                    WarehouseProduct warehouseProduct = new WarehouseProduct();
                    if (StringUtil.isEmpty(offlineDistribution.getAgentId())) {
                        throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ID_EMPTY);
                    }
                    Agent agent = agentService.findAgentByAgentId(offlineDistribution.getAgentId(),tenantId,offlineDistribution.getBranchId());
                    if (ObjectUtil.isEmpty(agent)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.INVALID_AGENT);
                    }

                    ESEAccount agentAccount = accountService.findAccountByProfileIdAndProfileType(
                            agent.getProfileId(), ESEAccount.AGENT_ACCOUNT,tenantId);
                    if (ObjectUtil.isEmpty(agentAccount))
                        throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_UNAVAILABLE);
                    if (ESEAccount.INACTIVE == agentAccount.getStatus())
                        throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_INACTIVE);
                    if (StringUtil.isEmpty(offlineDistribution.getDeviceId())) {
                        throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);
                    }

                    Device device = deviceService
                            .findDeviceBySerialNumber(offlineDistribution.getDeviceId(),tenantId);
                    if (ObjectUtil.isEmpty(device)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DEVICE);
                    }
                    if (StringUtil.isEmpty(offlineDistribution.getServicePointId())) {
                        throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERV_POINT_ID);
                    }

                    ServicePoint servicePoint = servicePointService.findByServicePointById(
                            Long.valueOf(offlineDistribution.getServicePointId()),tenantId);
                    if (ObjectUtil.isEmpty(servicePoint)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.INVALID_SERVICE_POINT);
                    }
                    String productDate = offlineDistribution.getDistributionDate();
                    String receiptNo = offlineDistribution.getReceiptNo();
                    String latitude=offlineDistribution.getLatitude();
                    String longitude=offlineDistribution.getLongitude();
                    if (StringUtil.isEmpty(receiptNo)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_RECEIPT_NO);
                    }
                    String villageCode = offlineDistribution.getVillageCode();
                    if (StringUtil.isEmpty(villageCode)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_VILLAGE_CODE);
                    }

                    Village village = locationService.findVillageByCode(villageCode,tenantId); 
                    if (ObjectUtil.isEmpty(village)) {
                        throw new OfflineTransactionException(ITxnErrorCodes.VILLAGE_NOT_EXIST);
                    }
                    Distribution exist = productDistributionService.findDistributionByReceiptNoTxnType(
                            receiptNo, offlineDistribution.getTxnType(),tenantId);
                    if (!ObjectUtil.isEmpty(exist)) {
                        throw new SwitchException((TransactionTypeProperties.PRODUCT_DISTRIBUTION
                                .equalsIgnoreCase(offlineDistribution.getTxnType()) || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                                .equalsIgnoreCase(offlineDistribution.getTxnType()))
                                        ? ITxnErrorCodes.DISTRIBUTION_EXIST
                                        : (TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                                                .equalsIgnoreCase(offlineDistribution.getTxnType())
                                                        ? ITxnErrorCodes.PRODUCT_RETURN_FROM_FARMER_EXIST
                                                        : ITxnErrorCodes.PRODUCT_RETURN_FROM_FIELDSTAFF_EXIST));
                    }
                    
                    String currentSeasonCode = offlineDistribution.getCurrentSeasonCode();
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
                    if ((TransactionTypeProperties.PRODUCT_DISTRIBUTION
                            .equalsIgnoreCase(offlineDistribution.getTxnType()) || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                            .equalsIgnoreCase(offlineDistribution.getTxnType()))
                            && StringUtil.isEmpty(offlineDistribution.getWarehouseCode())) {
                        registeredFarmer = false;
                    }
                    if (TransactionTypeProperties.PRODUCT_DISTRIBUTION
                            .equalsIgnoreCase(offlineDistribution.getTxnType())
                            || TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                                    .equalsIgnoreCase(offlineDistribution.getTxnType()) || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                                    .equalsIgnoreCase(offlineDistribution.getTxnType())) {
    
                        String farmerId = offlineDistribution.getFarmerId();
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
                            if (StringUtil.isEmpty(offlineDistribution.getWarehouseCode())) {
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.EMPTY_SAMITHI_CODE);
                            }
                            Warehouse warehouse = locationService
                                    .findSamithiByCode(offlineDistribution.getWarehouseCode(),tenantId);
                            if (ObjectUtil.isEmpty(warehouse)) {
                                throw new OfflineTransactionException(ITxnErrorCodes.SAMITHI_NOT_EXIST);
                            }
                        }
                        if (StringUtil.isEmpty(offlineDistribution.getDistributionDate())) {
                            throw new OfflineTransactionException(
                                    ITxnErrorCodes.EMPTY_DISTRIBUTION_DATE);
                        }
                        /*if (StringUtil.isEmpty(offlineDistribution.getSeasonCode()))
                            throw new SwitchException(ITxnErrorCodes.EMPTY_SEASON_CODE);*/
                        season = productDistributionService.findSeasonBySeasonCodeByTenantId(offlineDistribution.getSeasonCode(), tenantId);
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
    
                    Distribution distribution = new Distribution();
                    AgroTransaction agroTransaction = new AgroTransaction();
                    distribution.setAgroTransaction(agroTransaction);
    
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
                    agroTransaction.setTxnType(offlineDistribution.getTxnType());
                    agroTransaction.setOperType(ESETxn.ON_LINE);
                    agroTransaction.setProductStock(WarehouseProduct.StockType.AGENT_STOCK.name());
                    if (TransactionTypeProperties.PRODUCT_DISTRIBUTION
                            .equalsIgnoreCase(offlineDistribution.getTxnType()) || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                            .equalsIgnoreCase(offlineDistribution.getTxnType())
                            || TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                                    .equalsIgnoreCase(offlineDistribution.getTxnType())) {
                        if (registeredFarmer) {
                            distribution.setVillage(farmer.getVillage());
                            agroTransaction.setFarmerId(farmer.getFarmerId());
                            agroTransaction
                                    .setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
                            agroTransaction.setSamithi(farmer.getSamithi());
                            distribution.setFarmerId(farmer.getFarmerId());
                            distribution
                                    .setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
                            distribution.setSamithiId(!ObjectUtil.isEmpty(farmer.getSamithi())?String.valueOf(farmer.getSamithi().getId()):"");
                        } else {
                            distribution.setVillage(village);
                            agroTransaction.setFarmerId(NOT_APPLICABLE);
                            agroTransaction.setFarmerName(offlineDistribution.getFarmerId());
                            distribution.setMobileNumber(offlineDistribution.getMobileNumber());
                            distribution.setFarmerId(NOT_APPLICABLE);
                            distribution.setFarmerName(offlineDistribution.getFarmerId());
                        }
                        distribution.setBranchId(offlineDistribution.getBranchId());
                        distribution.setSeasonCode(harvestSeason.getCode());
                        distribution.setReceiptNumber(receiptNo);
                        distribution.setTxnTime(
                                DateUtil.convertStringToDate(productDate, DateUtil.TXN_DATE_TIME));
                        distribution.setSeason(season);
                        agroTransaction.setProfType(Profile.AGENT);
                        agroTransaction.setIntBalance(agentAccount.getCashBalance());
                        agroTransaction.setAccount(agentAccount);
                        distribution.setTxnType(offlineDistribution.getTxnType());
                        distribution.setServicePointId(!ObjectUtil.isEmpty(warehouse)
                                ? !StringUtil.isEmpty(warehouse.getCode()) ? warehouse.getCode() : ""
                                : "");
                        distribution.setServicePointName(!ObjectUtil.isEmpty(warehouse)
                                ? !StringUtil.isEmpty(warehouse.getName()) ? warehouse.getName() : ""
                                : "");
                        distribution.setWarehouseCode(!ObjectUtil.isEmpty(agent.getProcurementCenter())?agent.getProcurementCenter().getCode():"");
                        distribution.setWarehouseName(!ObjectUtil.isEmpty(agent.getProcurementCenter())?agent.getProcurementCenter().getName():"");
                        
                        distribution.setAgentId(agent.getProfileId());
                        distribution.setTenantId(tenantId);
                        distribution.setLatitude(latitude);
                        distribution.setLongitude(longitude);
                        distribution.setAgentName((ObjectUtil.isEmpty(agent.getPersonalInfo()) ? ""
                                : agent.getPersonalInfo().getAgentName()));
                        distribution.setProductStock(WarehouseProduct.StockType.AGENT_STOCK.name());
                        agroTransaction.setBranch_id(offlineDistribution.getBranchId());
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
                    Set<DistributionDetail> distributionDetailList = new HashSet<DistributionDetail>();
                    Set<OfflineDistributionDetail> offlineDistributionDetailList = Collections.synchronizedSet(
                            offlineDistribution.getOfflineDistributionDetails());
                    if (!ObjectUtil.isListEmpty(offlineDistributionDetailList)) {
                        for (OfflineDistributionDetail offlineDistributionDetail : offlineDistributionDetailList) {
                            DistributionDetail detail = new DistributionDetail();
    
                            if (StringUtil.isEmpty(offlineDistributionDetail.getProductCode())) {
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.EMPTY_PRODUCT_CODE);
    
                            }
                            Product product = productService.findProductByCodeByTenantId(offlineDistributionDetail.getProductCode(), tenantId);
                            if (ObjectUtil.isEmpty(product)) {
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.PRODUCT_DOES_NOT_EXIST);
                            }
                            detail.setProduct(product);
                            detail.setUnit(product.getUnit());
    
                            if(StringUtil.isEmpty(offlineDistributionDetail.getBatchNo())){
                                batchNo="NA";
                                
                            }
                           
                           
                            if(offlineDistributionDetail.getBatchNo()!=null){
                            // Validation Skipped For Farmer Product Return
                            if (TransactionTypeProperties.PRODUCT_DISTRIBUTION
                                    .equalsIgnoreCase(offlineDistribution.getTxnType()) || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                                    .equalsIgnoreCase(offlineDistribution.getTxnType())
                                    || TransactionTypeProperties.PRODUCT_RETURN_FROM_FIELDSTAFF
                                            .equalsIgnoreCase(offlineDistribution.getTxnType())) {
                                
                                if(!tenantId.equalsIgnoreCase("lalteer")){
                                    warehouseProduct = productDistributionService.findFieldStaffAvailableStockBySeasonAndBatch(agent.getProfileId(),
                                                product.getId(), distribution.getSeasonCode(),offlineDistributionDetail.getBatchNo(),tenantId,agent.getBranchId());
                                }else{
                                    
                                    warehouseProduct = productDistributionService.findFieldStaffAvailableStockByBatch(agent.getProfileId(),
                                            product.getId(),offlineDistributionDetail.getBatchNo(),tenantId);
                                }                             
                           
                            }
                            }else {
                                if(!tenantId.equalsIgnoreCase("lalteer")){
                                    warehouseProduct = productDistributionService.findFieldStaffAvailableStockBySeasonAndBatch(agent.getProfileId(),
                                            product.getId(), distribution.getSeasonCode(),batchNo,tenantId,agent.getBranchId());
                                }else{
                                    warehouseProduct = productDistributionService.findFieldStaffAvailableStockByBatch(agent.getProfileId(),
                                            product.getId(),batchNo,tenantId);
                               }
                        }
                            if (ObjectUtil.isEmpty(warehouseProduct))
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.WAREHOUSEPRODUCT_NOT_EXIST);
    
                            try {
    
                                detail.setQuantity(
                                        Double.valueOf(offlineDistributionDetail.getQuantity()));
                                /*
                                 * detail.setPricePerUnit((!StringUtil.isEmpty(offlineDistributionDetail
                                 * .getPricePerUnit()))? Double.valueOf(offlineDistributionDetail
                                 * .getPricePerUnit()) : 0.00);
                                 */
                                detail.setSellingPrice(Double.valueOf(0.00));
                                
                                detail.setCostPrice(
                                        StringUtil.isEmpty(offlineDistributionDetail.getSellingPrice())
                                                ? 0.00
                                                : Double.valueOf(offlineDistributionDetail.getSellingPrice()));
                                detail.setSubTotal(
                                        StringUtil.isEmpty(offlineDistributionDetail.getSubTotal())
                                                ? 0.00
                                                : Double.valueOf(
                                                        offlineDistributionDetail.getSubTotal()));
                                detail.setExistingQuantity(String.valueOf(warehouseProduct.getStock()));
                                detail.setCurrentQuantity(String.valueOf(warehouseProduct.getStock()-detail.getQuantity()));
                               
                                if(!StringUtil.isEmpty(offlineDistributionDetail.getBatchNo())){
                                    detail.setBatchNo(offlineDistributionDetail.getBatchNo());
                                }else{
                                    detail.setBatchNo("NA");
                                }
    
                            } catch (Exception e) {
                                throw new OfflineTransactionException(
                                        ITxnErrorCodes.DATA_CONVERSION_ERROR);
                            }
    
                            // Available stock validation - Validation Skipped For Farmer Product Return
                            if (TransactionTypeProperties.PRODUCT_DISTRIBUTION
                                    .equalsIgnoreCase(offlineDistribution.getTxnType()) || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                                    .equalsIgnoreCase(offlineDistribution.getTxnType())
                                    || TransactionTypeProperties.PRODUCT_RETURN_FROM_FIELDSTAFF
                                            .equalsIgnoreCase(offlineDistribution.getTxnType())) {
                                Double quantity = 0.0;
                                for (DistributionDetail existingDetail : distributionDetailList) {
                                    if (existingDetail.getProduct().getCode().equalsIgnoreCase(
                                            warehouseProduct.getProduct().getCode()) && existingDetail.getBatchNo().equals(warehouseProduct.getBatchNo())) {
                                        quantity = quantity + existingDetail.getQuantity();
    
                                    }
                                }
                                if (quantity + detail.getQuantity() > warehouseProduct.getStock())
                                    throw new OfflineTransactionException(
                                            ITxnErrorCodes.INSUFFICIENT_BAL);
                                
                                if ( TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                                        .equalsIgnoreCase(offlineDistribution.getTxnType())) {
                                	
                                	DistributionBalance  db  = productDistributionService.findDistributionBalanceByFarmerAndProduct(farmer.getId(),warehouseProduct.getProduct().getId(),tenantId);
                                    
                                    if (db==null || quantity + detail.getQuantity() > db.getStock())
                                        throw new OfflineTransactionException(
                                                ITxnErrorCodes.INSUFFICIENT_FARMER_BAL);
                                    
                                    
                                    detail.setInitFBalance(db.getStock());
                                    detail.setInitTBalance(detail.getQuantity());
                                    detail.setFinalFBalance(db.getStock() - detail.getQuantity());
                                    db.setStock(detail.getFinalFBalance()); 
                                    productDistributionService.updateDistributionBalance(db,tenantId);
                                    
                                    
                                }
                            }
                            
                           
                           
                            detail.setDistribution(distribution);
                            distributionDetailList.add(detail);
                        }
                        
                        distribution.setDistributionDetails(distributionDetailList);
                    }
                    
                    // Parsing payment amount
                    double paymentAmount = 0;
    
                    if (!StringUtil.isEmpty(offlineDistribution.getPaymentAmt())) {
                        try {
                            paymentAmount = Double.valueOf(offlineDistribution.getPaymentAmt());
                        } catch (Exception e) {
                            LOGGER.info("Could not Parse paymentAount");
                            e.printStackTrace();
                        }
                    }
    
                    // Setting payment amount
                    // distribution.setPaymentAmount(paymentAmount);
    
                    double totalAmount = 0;
                    for (DistributionDetail detail : distributionDetailList) {
                        totalAmount = totalAmount + detail.getSubTotal();
                    }
    
                    double amountWithTax = 0;
                    if (!StringUtil.isEmpty(offlineDistribution.getTax())) {
                       /* amountWithTax = (offlineDistribution.getTax() / 100 * totalAmount)
                                + totalAmount;*/
                        amountWithTax = offlineDistribution.getTax()+totalAmount;
                        distribution.setFinalAmount(amountWithTax);
                    } else {
                        distribution.setFinalAmount(totalAmount);
                    }
                    distribution.setPaymentMode(StringUtil.isEmpty(offlineDistribution.getPaymentMode())
                            ? "" : offlineDistribution.getPaymentMode());
                    distribution.setFreeDistribution(offlineDistribution.getFreeDistribution());
                    distribution.setTxnAmount(totalAmount);
                    distribution.setTax(StringUtil.isEmpty(offlineDistribution.getTax()) ? 0.00
                            : offlineDistribution.getTax());
                    distribution.setTotalAmount(totalAmount);
                    
                    distribution.setPaymentAmount(paymentAmount);
                    
                    /*if(!tenantId.equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
                    	distribution.setPaymentAmount(paymentAmount);
                    }else if(offlineDistribution.getPaymentMode().equals("CS")){
                    	 distribution.setPaymentAmount(Double.valueOf(offlineDistribution.getPaymentAmt()));
                    }else if(offlineDistribution.getPaymentMode().equals("CR")){
                    	 distribution.setPaymentAmount(distribution.getFinalAmount());
                    }else if(offlineDistribution.getPaymentMode().equals("")){
                    	distribution.setPaymentAmount(0.00);
                    }*/
                    
                 /*   if (offlineDistribution.getPaymentMode().equals("CS")) {
                        distribution
                                .setPaymentAmount(Double.valueOf(offlineDistribution.getPaymentAmt()));
                    } else if (offlineDistribution.getPaymentMode().equals("CR")) {
                        distribution.setPaymentAmount(distribution.getFinalAmount());
                    } else if (offlineDistribution.getPaymentMode().equals("")) {
                        distribution.setPaymentAmount(0.00);
                    }*/
                    
                    
                    // Reducing payment amount with total amount
                    /*
                     * if (paymentAmount > 0) { totalAmount -= paymentAmount; }
                     */
                    agroTransaction.setTxnAmount(amountWithTax);
                    // agroTransaction.setTxnAmount(totalAmount);
                    // Setting Desc and Type as per Txn Type
                    if (TransactionTypeProperties.PRODUCT_DISTRIBUTION
                            .equalsIgnoreCase(offlineDistribution.getTxnType()) || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                            .equalsIgnoreCase(offlineDistribution.getTxnType())) {
                        agroTransaction
                                .setTxnDesc(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION);
                        
                        /*
                         * agroTransaction.setBalAmount(agroTransaction.getIntBalance() +
                         * agroTransaction.getTxnAmount());
                         * agentAccount.setDistributionBalance(agroTransaction.getBalAmount());
                         */
                        agroTransaction.setIntBalance(
                                registeredFarmer ? farmerAccount.getCashBalance() : 0);
                        agroTransaction.setBalAmount(agroTransaction.getIntBalance() -
                                agroTransaction.getTxnAmount());
                        agroTransaction.setAccount(farmerAccount);
                        agroTransaction.setProfType(Profile.CLIENT);
    
                    } else if (TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                            .equalsIgnoreCase(offlineDistribution.getTxnType())) {
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
                    agroTransaction.setDistribution(distribution);
                    distribution.setPmtImageDetail(offlineDistribution.getPmtImageDetail());
                    
                    Set<PMTImageDetails> offlineDistImageDetailList= Collections.synchronizedSet(
                            offlineDistribution.getPmtImageDetail());
                    try {
                        productDistributionService.saveDistributionAndDistributionDetail(distribution,tenantId);
                        if (!ObjectUtil.isListEmpty(offlineDistImageDetailList)) {
                            for (PMTImageDetails offlineDistImg: offlineDistImageDetailList) {
                            	offlineDistImg.setPmt(distribution.getId());
                                distImgList.add(offlineDistImg);
                                distribution.setPmtImageDetail(distImgList);
                    			
                            }
                            
                            productDistributionService.updateDistribution(distribution,tenantId);
                        }
                        //productDistributionService.saveAgroTransaction(agroTransaction,tenantId);
                        // Updating Account for Product Distribution to Farmer and Product return from
                        // Farmer
                        /*
                         * if (TransactionTypeProperties.PRODUCT_DISTRIBUTION
                         * .equalsIgnoreCase(offlineDistribution.getTxnType()) ||
                         * TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                         * .equalsIgnoreCase(offlineDistribution.getTxnType())) {
                         * accountService.update(agentAccount); }
                         */
                        if (TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                                .equalsIgnoreCase(offlineDistribution.getTxnType())) {
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
                offlineDistribution.setStatusCode(statusCode);
                offlineDistribution.setStatusMsg(statusMsg);
                offlineDistribution.setPmtImageDetail(distImgList);
                productDistributionService.updateOfflineDistribution(offlineDistribution,tenantId);
    
            } // end outer for*/
        	  }catch (Exception e) {
        		 e.printStackTrace();
      			continue;
        }
         
		}
        
     System.gc();   
    }
}
