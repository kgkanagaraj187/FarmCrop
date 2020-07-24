/*
 * ProductDistribution.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.activation.DataHandler;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.profile.InspectionImage;
import com.sourcetrace.eses.order.entity.profile.InspectionImageInfo;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.OfflineDistribution;
import com.sourcetrace.eses.order.entity.txn.OfflineDistributionDetail;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

@Component
public class ProductDistribution implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(ProductDistribution.class.getName());
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

    /**
     * Sets the product distribution service.
     * @param productDistributionService the new product distribution service
     */
    public void setProductDistributionService(
            IProductDistributionService productDistributionService) {

        this.productDistributionService = productDistributionService;
    }

    /**
     * Gets the agent service.
     * @return the agent service
     */
    public IAgentService getAgentService() {

        return agentService;
    }

    /**
     * Sets the agent service.
     * @param agentService the new agent service
     */
    public void setAgentService(IAgentService agentService) {

        this.agentService = agentService;
    }

    /**
     * Gets the product service.
     * @return the product service
     */
    public IProductService getProductService() {

        return productService;
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
     * Sets the product service.
     * @param productService the new product service
     */
    public void setProductService(IProductService productService) {

        this.productService = productService;
    }

    /**
     * Sets the farmer service.
     * @param farmerService the new farmer service
     */
    public void setFarmerService(IFarmerService farmerService) {

        this.farmerService = farmerService;
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

    /**
     * Gets the farmer service.
     * @return the farmer service
     */
    public IFarmerService getFarmerService() {

        return farmerService;
    }

    /**
     * Gets the device service.
     * @return the device service
     */
    public IDeviceService getDeviceService() {

        return deviceService;
    }

    /**
     * Sets the device service.
     * @param deviceService the new device service
     */
    public void setDeviceService(IDeviceService deviceService) {

        this.deviceService = deviceService;
    }

    /**
     * Gets the service point service.
     * @return the service point service
     */
    public IServicePointService getServicePointService() {

        return servicePointService;
    }

    /**
     * Sets the service point service.
     * @param servicePointService the new service point service
     */
    public void setServicePointService(IServicePointService servicePointService) {

        this.servicePointService = servicePointService;
    }

    /**
     * Gets the product distribution service.
     * @return the product distribution service
     */
    public IProductDistributionService getProductDistributionService() {

        return productDistributionService;
    }

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
     */
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** REQUEST DATA **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);
        String agentId = head.getAgentId();
        String serialNo = head.getSerialNo();
        String servPointId = head.getServPointId();
        String txnType = head.getTxnType();
        // To fetch amt value
        String paymentAmt = (String) reqData.get(TxnEnrollmentProperties.PAYMENT);
        // To fetch mobileNo value
        String mobileNumber = (String) reqData.get(TxnEnrollmentProperties.MOBILE_NO);
        // If Samithi Code Empty means UnRegisteredFarmer
        String samithiCode = (String) reqData.get(TxnEnrollmentProperties.SAMITHI_CODE);

        String isFreeDistribution = (String) reqData
                .get(TxnEnrollmentProperties.IS_FREE_DISTRIBUTION);

        String tax = (String) reqData.get(TxnEnrollmentProperties.TAX);

        String modeOfPayment = (String) reqData.get(TxnEnrollmentProperties.MODE_OF_PAYMENT);
        String latitude = (String) reqData
                .get(TxnEnrollmentProperties.LATITUDE);
        String longitude = (String) reqData
                .get(TxnEnrollmentProperties.LONGITUDE);
        
        String tenantId=head.getTenantId();
        
        if (ESETxn.ONLINE_MODE == Integer.parseInt(head.getMode())) {

            /** VALIDATE REQUEST DATA **/
            Agent agent = agentService.findAgentByAgentId(agentId);
            if (ObjectUtil.isEmpty(agent)) {
                throw new SwitchException(ITxnErrorCodes.INVALID_AGENT);
            }
            ESEAccount agentAccount = accountService.findAccountByProfileIdAndProfileType(
                    agent.getProfileId(), ESEAccount.AGENT_ACCOUNT);
            if (ObjectUtil.isEmpty(agentAccount)) {
                throw new SwitchException(ITxnErrorCodes.AGENT_ACCOUNT_UNAVAILABLE);
            }
            Device device = deviceService.findDeviceBySerialNumber(serialNo);
            if (ObjectUtil.isEmpty(device)) {
                throw new SwitchException(ITxnErrorCodes.INVALID_DEVICE);
            }
            ServicePoint servicePoint = servicePointService
                    .findServicePointByServPointId(servPointId);
            if (ObjectUtil.isEmpty(servicePoint)) {
                throw new SwitchException(ITxnErrorCodes.INVALID_SERVICE_POINT);
            }

            String productDate = (String) reqData
                    .get(TxnEnrollmentProperties.PRODUCT_DISTRIBUTION_DATE);
            String receiptNo = (String) reqData.get(TxnEnrollmentProperties.RECEIPT_NO);
            if (StringUtil.isEmpty(receiptNo)) {
                throw new SwitchException(ITxnErrorCodes.EMPTY_RECEIPT_NO);
            }

            String villageCode = (String) reqData
                    .get(TxnEnrollmentProperties.PROCUREMENT_VILLAGE_CODE);
            if (StringUtil.isEmpty(villageCode)) {
                throw new SwitchException(ITxnErrorCodes.EMPTY_VILLAGE_CODE);
            }

            Village village = locationService.findVillageByCode(villageCode);
            if (ObjectUtil.isEmpty(village)) {
                throw new SwitchException(ITxnErrorCodes.VILLAGE_NOT_EXIST);
            }

            /** TO CHECK DUPILCATION **/
            Distribution exist = productDistributionService
                    .findDistributionByReceiptNoTxnType(receiptNo, txnType);
            if (!ObjectUtil.isEmpty(exist)) {
                throw new SwitchException(
                        (TransactionTypeProperties.PRODUCT_DISTRIBUTION.equalsIgnoreCase(txnType)  || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                                .equalsIgnoreCase(txnType))
                                ? ITxnErrorCodes.DISTRIBUTION_EXIST
                                : (TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                                        .equalsIgnoreCase(txnType)
                                                ? ITxnErrorCodes.PRODUCT_RETURN_FROM_FARMER_EXIST
                                                : ITxnErrorCodes.PRODUCT_RETURN_FROM_FIELDSTAFF_EXIST));
            }

            Farmer farmer = null;
            Season season = null;
            ESEAccount farmerAccount = null;

            boolean registeredFarmer = true;
            String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
            if ((TransactionTypeProperties.PRODUCT_DISTRIBUTION.equalsIgnoreCase(txnType)  || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                    .equalsIgnoreCase(txnType))
                    && StringUtil.isEmpty(samithiCode)) {
                registeredFarmer = false;
            }

            // Farmer,Season,Account related validations are skipped for FieldStaff Product Return
            if ((TransactionTypeProperties.PRODUCT_DISTRIBUTION.equalsIgnoreCase(txnType))
                    || TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                            .equalsIgnoreCase(txnType)  || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                            .equalsIgnoreCase(txnType)) {

                if (registeredFarmer) {
                    if (StringUtil.isEmpty(farmerId)) {
                        throw new SwitchException(ITxnErrorCodes.EMPTY_FARMER_ID);
                    }
                    farmer = farmerService.findFarmerByFarmerId(farmerId);
                    if (ObjectUtil.isEmpty(farmer)) {
                        throw new SwitchException(ITxnErrorCodes.FARMER_NOT_EXIST);
                    }
                    if (farmer.getStatus() == Farmer.Status.INACTIVE.ordinal()) {
                        throw new SwitchException(ITxnErrorCodes.FARMER_INACTIVE);
                    }
                    farmerAccount = farmerService.findAccountBySeassonProcurmentProductFarmer(0L,
                            farmer.getId());
                    if (ObjectUtil.isEmpty(farmerAccount))
                        throw new SwitchException(ITxnErrorCodes.FARMER_ACCOUNT_UNAVAILABLE);
                    if (ESEAccount.INACTIVE == farmerAccount.getStatus())
                        throw new SwitchException(ITxnErrorCodes.FARMER_ACCOUNT_INACTIVE);
                }

                String seasonCode = (String) reqData.get(TxnEnrollmentProperties.SEASON_CODE);
                if (StringUtil.isEmpty(seasonCode))
                    throw new SwitchException(ITxnErrorCodes.EMPTY_SEASON_CODE);
                season = productDistributionService.findSeasonBySeasonCode(seasonCode);
                if (ObjectUtil.isEmpty(season))
                    throw new SwitchException(ITxnErrorCodes.SEASON_NOT_EXIST);
            }

            Warehouse warehouse = agent.getCooperative();
            if (StringUtil.isEmpty(warehouse)) {
                throw new SwitchException(ITxnErrorCodes.AGENT_SAMITHI_UNAVAILABLE);
            }

            WarehouseProduct warehouseProduct = new WarehouseProduct();
            Distribution distribution = new Distribution();
            AgroTransaction agroTransaction = new AgroTransaction();
            distribution.setAgroTransaction(agroTransaction);
            distribution.setBranchId(head.getBranchId());
            warehouseProduct.setBranchId(head.getBranchId());
            /** FORMING AGRO TRANSACTION OBJECT **/
            agroTransaction.setReceiptNo(receiptNo);
            agroTransaction.setAgentId(agentId);
            agroTransaction.setAgentName((ObjectUtil.isEmpty(agent.getPersonalInfo()) ? ""
                    : agent.getPersonalInfo().getAgentName()));
            agroTransaction.setDeviceId(device.getCode());
            agroTransaction.setDeviceName(device.getName());
            agroTransaction.setServicePointId(warehouse.getCode());
            agroTransaction.setServicePointName(warehouse.getName());
            agroTransaction.setTxnType(txnType);
            agroTransaction.setOperType(ESETxn.ON_LINE);
            if (TransactionTypeProperties.PRODUCT_DISTRIBUTION.equalsIgnoreCase(txnType)
                    || TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                            .equalsIgnoreCase(txnType)  || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                            .equalsIgnoreCase(txnType)) {
                distribution.setSeason(season);
                agroTransaction.setProfType(Profile.AGENT);
                agroTransaction.setIntBalance(agentAccount.getDistributionBalance());
                agroTransaction.setAccount(agentAccount);

                if (registeredFarmer) {
                    distribution.setVillage(farmer.getVillage());
                    agroTransaction.setFarmerId(farmer.getFarmerId());
                    agroTransaction
                            .setFarmerName(farmer.getFirstName() + " " + farmer.getLastName());
                    agroTransaction.setSamithi(farmer.getSamithi());
                } else {
                    distribution.setVillage(village);
                    agroTransaction.setFarmerId(NOT_APPLICABLE);
                    agroTransaction.setFarmerName(farmerId);
                    distribution.setMobileNumber(mobileNumber);
                }
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
            Collection collection = (Collection) reqData.get(TxnEnrollmentProperties.PRODUCT_LIST);
            List<com.sourcetrace.eses.txn.schema.Object> productObject = collection.getObject();
            for (com.sourcetrace.eses.txn.schema.Object object : productObject) {
                List<Data> productData = object.getData();
                DistributionDetail detail = new DistributionDetail();
                detail.setExistingQuantity(warehouseProduct.getStockQty());
                for (Data data : productData) {
                    String key = data.getKey();
                    String value = data.getValue();

                    if (TxnEnrollmentProperties.PRODUCT_CODE.equalsIgnoreCase(key)) {
                        if (StringUtil.isEmpty(value)) {
                            throw new SwitchException(ITxnErrorCodes.EMPTY_PRODUCT_CODE);
                        }
                        Product product = productService.findProductByCode(value);
                        if (ObjectUtil.isEmpty(product)) {
                            throw new SwitchException(ITxnErrorCodes.PRODUCT_DOES_NOT_EXIST);
                        }
                        detail.setProduct(product);
                        detail.setUnit(product.getUnit());
                        // Validation Skipped For Farmer Product Return
                        if (TransactionTypeProperties.PRODUCT_DISTRIBUTION.equalsIgnoreCase(txnType)
                                || TransactionTypeProperties.PRODUCT_RETURN_FROM_FIELDSTAFF
                                        .equalsIgnoreCase(txnType) || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                                        .equalsIgnoreCase(txnType)) {
                            warehouseProduct = productDistributionService
                                    .findAgentAvailableStock(agentId, product.getId());
                            if (ObjectUtil.isEmpty(warehouseProduct)) {
                                throw new SwitchException(
                                        ITxnErrorCodes.WAREHOUSEPRODUCT_NOT_EXIST);
                            }
                        }
                    }
                    try {
                        if (TxnEnrollmentProperties.QUANTITY.equalsIgnoreCase(key)) {
                            detail.setQuantity(Double.valueOf(value));
                            detail.setCurrentQuantity(String.valueOf(warehouseProduct.getStock() - detail.getQuantity()));
                        }
                        // if("0".equalsIgnoreCase(distribution.getFreeDistribution())){
                        if (TxnEnrollmentProperties.PRICE_PER_UNIT.equalsIgnoreCase(key)) {
                            detail.setSellingPrice(
                                    !StringUtil.isEmpty(value) ? Double.valueOf(value) : 0.00);
                        }
                        // }
                        if (TxnEnrollmentProperties.SUB_TOTAL.equalsIgnoreCase(key)) {
                            detail.setSubTotal(Double.valueOf(value));
                        }
                    } catch (Exception e) {
                        throw new SwitchException(ITxnErrorCodes.DATA_CONVERSION_ERROR);
                    }
                }

                // Available stock validation - Validation Skipped For Farmer Product Return
                if (TransactionTypeProperties.PRODUCT_DISTRIBUTION.equalsIgnoreCase(txnType)
                        || TransactionTypeProperties.PRODUCT_RETURN_FROM_FIELDSTAFF
                                .equalsIgnoreCase(txnType)  || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                                .equalsIgnoreCase(txnType)) {
                    Double quantity = 0.0;
                    for (DistributionDetail existingDetail : distributionDetailList) {
                        if (existingDetail.getProduct().getCode()
                                .equalsIgnoreCase(warehouseProduct.getProduct().getCode())) {
                            quantity = quantity + existingDetail.getQuantity();

                        }
                    }

                    if (quantity + detail.getQuantity() > warehouseProduct.getStock())
                        throw new SwitchException(ITxnErrorCodes.INSUFFICIENT_BAL);
                }
                detail.setDistribution(distribution);
                distributionDetailList.add(detail);

            }

            // Parsing payment amount
            double paymentAmount = 0;
            if (!StringUtil.isEmpty(paymentAmt)) {
                try {
                    paymentAmount = Double.valueOf(paymentAmt);
                } catch (Exception e) {
                    LOGGER.info("Could not Parse paymentAount");
                    e.printStackTrace();
                }
            }

            // setting IsFreeDistribution
            distribution.setFreeDistribution(
                    (StringUtil.isEmpty(isFreeDistribution)) ? "" : isFreeDistribution);

            if ("0".equalsIgnoreCase(distribution.getFreeDistribution())) {
                distribution.setTax((StringUtil.isEmpty(tax)) ? 0.00 : Double.valueOf(tax));
                distribution.setPaymentMode(StringUtil.isEmpty(modeOfPayment) ? "" : modeOfPayment);
                // Setting payment amount
                distribution.setPaymentAmount(paymentAmount);

                double totalAmount = 0;
                distribution.setDistributionDetails(distributionDetailList);
                for (DistributionDetail detail : distributionDetailList) {
                    totalAmount = totalAmount + detail.getSubTotal();
                }

                double amountWithTax = 0;
                if (!StringUtil.isEmpty(distribution.getTax())) {
                    // amountWithTax = (distribution.getTax()/100 * totalAmount)+totalAmount;
                    amountWithTax = distribution.getTax() + totalAmount;
                }
                distribution.setFinalAmount(amountWithTax);

                agroTransaction.setTxnAmount(amountWithTax);

            } else if ("1".equalsIgnoreCase(distribution.getFreeDistribution())) {

                distribution.setTxnAmount(0.00);
                distribution.setTax(0.00);
                distribution.setFinalAmount(0.00);
                distribution.setPaymentMode("");
                distribution.setPaymentAmount(0.00);
            }

            // Reducing payment amount with total amount
            /*
             * if (paymentAmount > 0) { totalAmount -= paymentAmount; }
             */

            // Setting Desc and Type as per Txn Type
            if (TransactionTypeProperties.PRODUCT_DISTRIBUTION.equalsIgnoreCase(txnType)  || TransactionTypeProperties.PRODUCT_DISTRIBUTION_FARMER_BALANCE
                    .equalsIgnoreCase(txnType)) {
                agroTransaction.setTxnDesc(Distribution.PRODUCT_DISTRIBUTION_TO_FARMER_DESCRIPTION);
                // agroTransaction.setBalAmount(agroTransaction.getIntBalance()
                // + agroTransaction.getTxnAmount());
                // agentAccount.setDistributionBalance(agroTransaction.getBalAmount());
                agroTransaction.setIntBalance(
                        registeredFarmer ? farmerAccount.getDistributionBalance() : 0);
                agroTransaction.setProfType(Profile.CLIENT);
                agroTransaction.setAccount(farmerAccount);
            } else if (TransactionTypeProperties.PRODUCT_RETURN_FROM_FARMER
                    .equalsIgnoreCase(txnType)) {
                agroTransaction.setTxnDesc(Distribution.PRODUCT_RETURN_FROM_FARMER_DESCRIPTION);
                agroTransaction.setBalAmount(
                        agroTransaction.getIntBalance() - agroTransaction.getTxnAmount());
                agentAccount.setDistributionBalance(agroTransaction.getBalAmount());
            } else {
                agroTransaction.setTxnDesc(Distribution.PRODUCT_RETURN_FROM_FIELDSTAFF_DESCRIPTION);
                agroTransaction.setBalAmount(
                        agroTransaction.getIntBalance() - agroTransaction.getTxnAmount());
            }
           /* if(!StringUtil.isEmpty(batchNo)){
                distribution.setBatchNo(batchNo);
            }else{
                distribution.setBatchNo("NA");
            }*/
            
            /**
             * SAVING DISTRIBUTION AND DISTRIBUTION DETAIL OBJECT UPDATING FARMER ACCOUNT ,AGENT AND
             * WAREHOUSE PRODUCT OBJECT
             **/
            agroTransaction.setDistribution(distribution);
            try {
                productDistributionService.saveDistributionAndDistributionDetail(distribution);
                agent.setReceiptNumber(receiptNo);
            } catch (Exception e) {
                throw new SwitchException(ITxnErrorCodes.ERROR);
            }

        } else if (ESETxn.OFFLINE_MODE == Integer.parseInt(head.getMode())) {

            Agent agent = agentService.findAgentByAgentId(agentId);
            /** UPDATE OFFLINE RECIPT NO IN AGENT OBJECT **/
            String offlineReceiptNo = (String) reqData.get(TxnEnrollmentProperties.RECEIPT_NO);
            if (!StringUtil.isEmpty(offlineReceiptNo)) {
                agentService.updateAgentReceiptNoSequence(agentId, offlineReceiptNo);
            }

            /** FORMING OFFLINE DISTRIBUTION OBJECT **/
            OfflineDistribution offlineDistribution = new OfflineDistribution();
            offlineDistribution.setTxnType(txnType);
            offlineDistribution.setReceiptNo(offlineReceiptNo);
            offlineDistribution.setDistributionDate(
                    (String) reqData.get(TxnEnrollmentProperties.PRODUCT_DISTRIBUTION_DATE));
            offlineDistribution
                    .setFarmerId((String) reqData.get(TxnEnrollmentProperties.FARMER_ID));
            offlineDistribution
                    .setWarehouseCode((String) reqData.get(TxnEnrollmentProperties.SAMITHI_CODE));
            offlineDistribution.setVillageCode(
                    (String) reqData.get(TxnEnrollmentProperties.PROCUREMENT_VILLAGE_CODE));
            offlineDistribution
                    .setSeasonCode((String) reqData.get(TxnEnrollmentProperties.SEASON_CODE));
            offlineDistribution.setLatitude(latitude);
            offlineDistribution.setLongitude(longitude);
            DataHandler photoDataHandler = (DataHandler) reqData.get(TxnEnrollmentProperties.ADDITIONAL_PHOTO);
            Set<PMTImageDetails> distImageSet = new HashSet<PMTImageDetails>();
            PMTImageDetails pmtImageDetails = new PMTImageDetails();
			byte[] photoContent = null;
			try {
				if (photoDataHandler != null) {
					photoContent = IOUtils.toByteArray(photoDataHandler.getInputStream());
					if (photoContent != null) {
						pmtImageDetails.setPhoto(photoContent);
						pmtImageDetails.setLatitude(latitude);
						pmtImageDetails.setLongitude(longitude);
						distImageSet.add(pmtImageDetails);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			photoDataHandler = (DataHandler) reqData.get(TxnEnrollmentProperties.ADDITIONAL_PHOTO2);
			pmtImageDetails = new PMTImageDetails();
			try {
				if (photoDataHandler != null) {
					photoContent = IOUtils.toByteArray(photoDataHandler.getInputStream());
					if (photoContent != null) {
						pmtImageDetails.setPhoto(photoContent);
						distImageSet.add(pmtImageDetails);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			
          offlineDistribution.setPmtImageDetail(distImageSet);
            offlineDistribution.setAgentId(agentId);
            offlineDistribution.setDeviceId(serialNo);
            offlineDistribution.setServicePointId(!ObjectUtil.isEmpty(agent)
                    ? String.valueOf(agent.getServicePoint().getId()) : null);
            offlineDistribution.setFreeDistribution(isFreeDistribution);
            if ("0".equalsIgnoreCase(offlineDistribution.getFreeDistribution())) {
                offlineDistribution.setTax((StringUtil.isEmpty(tax)) ? 0.00 : Double.valueOf(tax));
                offlineDistribution
                        .setPaymentMode(StringUtil.isEmpty(modeOfPayment) ? "" : modeOfPayment);
                // Adding payment amt
                offlineDistribution.setPaymentAmt(paymentAmt);

            } else if ("1".equalsIgnoreCase(offlineDistribution.getFreeDistribution())) {
                offlineDistribution.setPaymentMode("");
                offlineDistribution.setTax(0.00);
                offlineDistribution.setTotalAmount(0.00);
                offlineDistribution.setPaymentAmt("");
            }
            // Adding payment amt
            // offlineDistribution.setPaymentAmt(paymentAmt);
            // sets mobile number for unregistered farmer
            if (!StringUtil.isEmpty(mobileNumber))
                offlineDistribution.setMobileNumber(mobileNumber);
            offlineDistribution.setTenantId(tenantId);
            offlineDistribution.setStatusCode(ESETxnStatus.PENDING.ordinal());
            offlineDistribution.setStatusMsg(ESETxnStatus.PENDING.toString());
            offlineDistribution.setBranchId(head.getBranchId());
            offlineDistribution.setCurrentSeasonCode((String)reqData.get(TxnEnrollmentProperties.CURRENT_SEASON_CODE));
            /** FORMING OFFLINE DISTRIBUTION DETAIL OBJECT **/
            Collection offlineCollection = (Collection) reqData
                    .get(TxnEnrollmentProperties.PRODUCT_LIST);
            List<com.sourcetrace.eses.txn.schema.Object> offlineProductObject = offlineCollection
                    .getObject();
            Set<OfflineDistributionDetail> offlineDistributionDetailList = new HashSet<OfflineDistributionDetail>();
            for (com.sourcetrace.eses.txn.schema.Object object : offlineProductObject) {
                List<Data> offlineProductData = object.getData();
                OfflineDistributionDetail offlineDistributionDetail = new OfflineDistributionDetail();
                for (Data data : offlineProductData) {
                    String key = data.getKey();
                    String value = data.getValue();

                    if (TxnEnrollmentProperties.PRODUCT_CODE.equalsIgnoreCase(key)) {
                        offlineDistributionDetail.setProductCode(value);

                    }

                    if (TxnEnrollmentProperties.QUANTITY.equalsIgnoreCase(key)) {
                        offlineDistributionDetail.setQuantity(value);
                    }

                    if (TxnEnrollmentProperties.PRICE_PER_UNIT.equalsIgnoreCase(key)) {
                        offlineDistributionDetail.setSellingPrice(
                                (StringUtil.isEmpty(value)) ? 0.00 : Double.valueOf(value));
                    }
                    
                    if(TxnEnrollmentProperties.BATCH_NO.equalsIgnoreCase(key)){
                        offlineDistributionDetail.setBatchNo(value);
                    }
                    
                    if ("0".equalsIgnoreCase(offlineDistribution.getFreeDistribution())) {
                        if (TxnEnrollmentProperties.SUB_TOTAL.equalsIgnoreCase(key)) {
                            offlineDistributionDetail.setSubTotal(value);
                        }
                    } else {
                        offlineDistributionDetail.setSubTotal("0.00");
                    }

                }
                offlineDistributionDetail.setOfflineDistribution(offlineDistribution);
                offlineDistributionDetailList.add(offlineDistributionDetail);
            }

            double totalAmount = 0;
            offlineDistribution.setOfflineDistributionDetails(offlineDistributionDetailList);
            for (OfflineDistributionDetail detail : offlineDistributionDetailList) {
                totalAmount = totalAmount + Double.valueOf(detail.getSubTotal());
            }

            double amountWithTax = 0;
            if (!StringUtil.isEmpty(offlineDistribution.getTax())) {
                amountWithTax = (offlineDistribution.getTax() / 100 * totalAmount) + totalAmount;
                offlineDistribution.setTotalAmount(amountWithTax);
            } else {
                offlineDistribution.setTotalAmount(totalAmount);
            }
            
            /** SAVING OFFLINE DISTRIBUTION AND DISTRIBUTION DETAIL OBJECT **/
            try {
                offlineDistribution.setOfflineDistributionDetails(offlineDistributionDetailList);
                productDistributionService.saveOfflineDistribution(offlineDistribution);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        }

        /** FORM RESPONSE DATA **/
        return new HashMap();
    }
   
    
    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#processVoid(java.util.Map)
     */
    @Override
    public Map<?, ?> processVoid(Map<?, ?> reqData) {

        return null;
    }
}
