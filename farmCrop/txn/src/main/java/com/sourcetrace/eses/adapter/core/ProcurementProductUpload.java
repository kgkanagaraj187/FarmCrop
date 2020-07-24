/*
 * ProcurementProductUpload.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.ese.entity.util.LoanInterest;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurement;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ClientService;
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
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

@SuppressWarnings({ "unused", "unchecked" })
@Component
public class ProcurementProductUpload implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(ProcurementProductUpload.class.getName());
	public static final String NOT_APPLICABLE = "N/A";
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
	private String agentName;
	private String enableLoanModule;
	private ESEAccount account;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {

		/** REQUEST DATA **/
		Head head = (Head) reqData.get(TransactionProperties.HEAD);

		/** VALIDATING REQUEST DATA **/
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		String servPointId = head.getServPointId();
		String txnMode = head.getMode();
		String branchId = head.getBranchId();
		String tenantId = head.getTenantId();

		LOGGER.info("AGENT ID : " + agentId);
		LOGGER.info("SERIAL NO : " + serialNo);
		LOGGER.info("SERVICE POINT ID : " + servPointId);
		LOGGER.info("TXN MODE: " + txnMode);

		// To fetch amt value
		String paymentAmt = (String) reqData.get(TxnEnrollmentProperties.PAYMENT);
		// To fetch mobileNo value
		String mobileNumber = (String) reqData.get(TxnEnrollmentProperties.MOBILE_NO);

		String supplierType = (String) reqData.get(TxnEnrollmentProperties.SUPPLIER_TYPE);

		String season = (String) reqData.get(TxnEnrollmentProperties.CURRENT_SEASON_CODE);

		String roadMapCode = (reqData.containsKey(TxnEnrollmentProperties.ROAD_MAP_CODE))
				? (String) reqData.get(TxnEnrollmentProperties.ROAD_MAP_CODE) : "";
		String substituteName = (reqData.containsKey(TxnEnrollmentProperties.SUBSTITUTE_NAME))
				? (String) reqData.get(TxnEnrollmentProperties.SUBSTITUTE_NAME) : "";
		String farmerAttnce = (reqData.containsKey(TxnEnrollmentProperties.FARMER_ATTENDANCE))
				? (String) reqData.get(TxnEnrollmentProperties.FARMER_ATTENDANCE) : "";
		String vehicleNum = (reqData.containsKey(TxnEnrollmentProperties.VEHICLE_NUM))
				? (String) reqData.get(TxnEnrollmentProperties.VEHICLE_NUM) : "";
		String batchNo = (reqData.containsKey(TxnEnrollmentProperties.BATCH_NO))
				? (String) reqData.get(TxnEnrollmentProperties.BATCH_NO) : "";

		String labourCost = (reqData.containsKey(TxnEnrollmentProperties.LABOUR_COST))
				? (String) reqData.get(TxnEnrollmentProperties.LABOUR_COST) : "";
		String transCost = (reqData.containsKey(TxnEnrollmentProperties.TRANS_COST))
				? (String) reqData.get(TxnEnrollmentProperties.TRANS_COST) : "";
		
		String TRANSPORTATION_VEHICLE = (reqData.containsKey(TxnEnrollmentProperties.TRANSPORTATION_VEHICLE))
				? (String) reqData.get(TxnEnrollmentProperties.TRANSPORTATION_VEHICLE) : "";		
		
				
				
		String ffc = (reqData.containsKey(TxnEnrollmentProperties.FFC))
				? (String) reqData.get(TxnEnrollmentProperties.FFC) : "";
		String cType = (reqData.containsKey(TxnEnrollmentProperties.CP_TYPE))
				? (String) reqData.get(TxnEnrollmentProperties.CP_TYPE) : "";
				
		String invoiceNo = (reqData.containsKey(TxnEnrollmentProperties.INVOICE_NO))
	            ? (String) reqData.get(TxnEnrollmentProperties.INVOICE_NO) : "";
	            
	    String isReg = (reqData.containsKey(TxnEnrollmentProperties.IS_REGISTERED_FARMER))
	    	            ? (String) reqData.get(TxnEnrollmentProperties.IS_REGISTERED_FARMER) : "";
	    	            
	    String buyer=(reqData.containsKey(TxnEnrollmentProperties.SALES_BUYER))
	            ? (String) reqData.get(TxnEnrollmentProperties.SALES_BUYER) : "";
	            
	   	
	    	    	            
	   setEnableLoanModule(preferncesService.findPrefernceByName(ESESystem.ENABLE_LOAN_MODULE));
		/** ONLINE TXN **/
		if (TransactionProperties.ONLINE_MODE.equals(txnMode)) {

			/** VALIDATE REQUEST DATA **/
			Agent agent = agentService.findAgentByAgentId(agentId);

			if (!ObjectUtil.isEmpty(agent.getPersonalInfo())) {
				agentName = agent.getPersonalInfo().getAgentName();

			}
			if (ObjectUtil.isEmpty(agent))
				throw new SwitchException(ITxnErrorCodes.INVALID_AGENT);

			Device device = deviceService.findDeviceBySerialNumber(serialNo);
			if (ObjectUtil.isEmpty(device))
				throw new SwitchException(ITxnErrorCodes.INVALID_DEVICE);

			ServicePoint servicePoint = servicePointService.findServicePointByServPointId(servPointId);
			if (ObjectUtil.isEmpty(servicePoint))
				throw new SwitchException(ITxnErrorCodes.INVALID_SERVICE_POINT);

			String receiptNo = (String) reqData.get(TxnEnrollmentProperties.RECEIPT_NO);
			if (StringUtil.isEmpty(receiptNo))
				throw new SwitchException(ITxnErrorCodes.EMPTY_RECEIPT_NO);

			Procurement existing = productDistributionService.findProcurementByRecNo(receiptNo);
			if (!ObjectUtil.isEmpty(existing)) {
				throw new SwitchException(ITxnErrorCodes.PROCUREMENT_EXIST);
			}

			String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
			if (StringUtil.isEmpty(farmerId))
				throw new SwitchException(ITxnErrorCodes.EMPTY_FARMER_ID);

			String samithiCode = (String) reqData.get(TxnEnrollmentProperties.SAMITHI_CODE);
			String villageCode = (String) reqData.get(TxnEnrollmentProperties.PROCUREMENT_VILLAGE_CODE);
			if (StringUtil.isEmpty(villageCode))
				throw new SwitchException(ITxnErrorCodes.EMPTY_VILLAGE_CODE);

			Village procurementVillage = locationService.findVillageByCode(villageCode);
			if (ObjectUtil.isEmpty(procurementVillage))
				throw new SwitchException(ITxnErrorCodes.VILLAGE_NOT_EXIST);

			ESEAccount agentAccount = accountService.findAccountByProfileId(agent.getProfileId());
			if (ObjectUtil.isEmpty(agentAccount))
				throw new SwitchException(ITxnErrorCodes.AGENT_ACCOUNT_UNAVAILABLE);

			if (agentAccount.getStatus() == ESEAccount.INACTIVE) {
				throw new SwitchException(ITxnErrorCodes.AGENT_ACCOUNT_INACTIVE);
			} else if (agentAccount.getStatus() == ESEAccount.BLOCKED) {
				throw new SwitchException(ITxnErrorCodes.AGENT_ACCOUNT_BLOCKED);
			}

			if (StringUtil.isEmpty(supplierType))
				throw new SwitchException(ITxnErrorCodes.EMPTY_SUPPLIER_TYPE);

			Warehouse samithi = null;
			Farmer farmer = null;
			ESEAccount farmerAccount = null;
			String procurementDate = (String) reqData.get(TxnEnrollmentProperties.PROCUREMENT_DATE);
			String totalAmount = (String) reqData.get(TxnEnrollmentProperties.TOTAL_AMOUNT);

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

			// Parsing total amount
			double txnAmount = 0;
			try {
				txnAmount = Double.valueOf(totalAmount);
			} catch (Exception e) {
				LOGGER.info(e.getMessage());
				e.printStackTrace();
			}

			boolean isRegisteredFarmer = false;
			if (!StringUtil.isEmpty(samithiCode)) {
				// Registered farmer procurement
				samithi = locationService.findSamithiByCode(samithiCode);
				if (ObjectUtil.isEmpty(samithi))
					throw new SwitchException(ITxnErrorCodes.SAMITHI_NOT_EXIST);

				farmer = farmerService.findFarmerByFarmerId(farmerId);
				if (ObjectUtil.isEmpty(farmer))
					throw new SwitchException(ITxnErrorCodes.FARMER_NOT_EXIST);

				/** CHECKING FARMER STATUS **/
				if (farmer.getStatus() == Farmer.Status.INACTIVE.ordinal()) {
					throw new SwitchException(ITxnErrorCodes.FARMER_INACTIVE);
				}

				/** FETCHING FARMER ACCOUNT AND VALIDATE **/
				farmerAccount = farmerService.findAccountBySeassonProcurmentProductFarmer(0L, farmer.getId());

				if (ObjectUtil.isEmpty(farmerAccount))
					throw new SwitchException(ITxnErrorCodes.FARMER_ACCOUNT_UNAVAILABLE);
				if (ESEAccount.INACTIVE == farmerAccount.getStatus())
					throw new SwitchException(ITxnErrorCodes.FARMER_ACCOUNT_INACTIVE);

				isRegisteredFarmer = true;
			}

			/** FORMING PROCUREMENT OBJECT * */
			Procurement procurement = new Procurement();

			// Added payment amount in to po number column because unused column
			// procurement.setPoNumber(String.valueOf(paymentAmount));
			procurement.setPaymentAmount(paymentAmount);
			if (StringUtil.isEmpty(paymentAmount) || paymentAmount == 0) {
				procurement.setPaymentMode(ESEAccount.PAYMENT_MODE_CREDIT);
			} else {
				procurement.setPaymentMode(ESEAccount.PAYMENT_MODE_CASH);
			}
			procurement.setTotalProVal(txnAmount);

			// sets mobile number for unregistered farmer
			if (!StringUtil.isEmpty(mobileNumber))
				procurement.setMobileNumber(mobileNumber);

			procurement.setSeason(productDistributionService.findCurrentSeason());
			procurement.setVillage((isRegisteredFarmer) ? farmer.getVillage() : procurementVillage);
			procurement.setProcMasterType(!StringUtil.isEmpty(supplierType) ? supplierType : "99");
			// procurement.setProcMasterType(supplierType);
			if (!"99".equalsIgnoreCase(!StringUtil.isEmpty(supplierType) ? supplierType : "99")) {
				MasterData master = farmerService.findMasterDataIdByCode(supplierType);
				procurement.setProcMasterTypeId(master != null ? String.valueOf(master.getId()) : null);
			}

			procurement.setSeasonCode(season);
			if (branchId.equalsIgnoreCase("AWI")) {
				procurement.setRoadMapCode(roadMapCode);

				procurement.setFarmerAttnce(farmerAttnce);
				if (!StringUtil.isEmpty(farmerAttnce) && farmerAttnce.equalsIgnoreCase("1")) {
					procurement.setSubstituteName(substituteName);
				} else {
					procurement.setSubstituteName(null);
				}
				procurement.setVehicleNum(vehicleNum);
			}
			if (!StringUtil.isEmpty(buyer)){
			Customer customer=clientService.findCustomerById(buyer);
			procurement.setBuyer(customer != null ? customer : null);
		}
			// AgroTransaction agentTransaction = new AgroTransaction();

			AgroTransaction farmerProcureTxn = new AgroTransaction();
			// agentTransaction.setRefAgroTransaction(farmerProcureTxn);
			try {
				/** FORMING AGRO TRANSACTION FOR FARMER PROCUREMENT **/
				farmerProcureTxn.setReceiptNo(receiptNo);
				farmerProcureTxn.setAgentId(agentId);
				farmerProcureTxn.setAgentName(
						(ObjectUtil.isEmpty(agent.getPersonalInfo()) ? "" : agent.getPersonalInfo().getAgentName()));
				farmerProcureTxn.setDeviceId(device.getCode());
				farmerProcureTxn.setDeviceName(device.getName());
				farmerProcureTxn.setServicePointId(servicePoint.getCode());
				farmerProcureTxn.setServicePointName(servicePoint
						.getName());/*
									 * farmerProcureTxn.setProcMasterType(
									 * supplierType);
									 * if("99".equalsIgnoreCase(supplierType)){
									 * farmerProcureTxn.setFarmerId((
									 * isRegisteredFarmer) ? farmerId :
									 * NOT_APPLICABLE);
									 * farmerProcureTxn.setFarmerName((
									 * isRegisteredFarmer) ?
									 * farmer.getFirstName() + " " +
									 * farmer.getLastName() : farmerId); } else
									 * { MasterData master =
									 * farmerService.findMasterDataIdByCode(
									 * farmerId);
									 * farmerProcureTxn.setProcMasterTypeId(
									 * String.valueOf(master.getId()));
									 * 
									 * }
									 */
				// farmerProcureTxn.setIntBalance((isRegisteredFarmer)?farmerAccount.getBalance():0);
				farmerProcureTxn.setTxnTime(DateUtil.convertStringToDate(procurementDate, DateUtil.TXN_DATE_TIME));
				farmerProcureTxn.setTxnAmount(txnAmount);
				// Credit to farmer account
				// farmerProcureTxn.setBalAmount(farmerProcureTxn.getIntBalance()
				// +
				// farmerProcureTxn.getTxnAmount());
				farmerProcureTxn.setTxnType(TransactionTypeProperties.PROCUREMENT_PRODUCT_ENROLLMENT);
				farmerProcureTxn.setProfType(Profile.CLIENT);
				farmerProcureTxn.setOperType(ESETxn.ON_LINE);
				farmerProcureTxn.setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
				farmerProcureTxn.setSamithi(samithi);
			} catch (Exception e) {
				throw new SwitchException(ITxnErrorCodes.DATA_CONVERSION_ERROR);
			}

			Set<ProcurementDetail> procurementDetails = new HashSet<ProcurementDetail>();

			/** FORMING PROCUREMENT DETAIL OBJECT **/
			Collection collection = (Collection) reqData.get(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_DETAIL);
			if (!ObjectUtil.isEmpty(collection)) {
				List<com.sourcetrace.eses.txn.schema.Object> objectList = collection.getObject();
				if (!ObjectUtil.isEmpty(objectList)) {
					for (Object object : objectList) {
						List<Data> procurementDataList = object.getData();
						ProcurementDetail procurementDetail = new ProcurementDetail();
						ProcurementProduct product = new ProcurementProduct();
						// GradeMaster gradeMaster = new GradeMaster();
						for (Data procurementData : procurementDataList) {
							String key = procurementData.getKey();
							String value = procurementData.getValue();
							/*
							 * if (TxnEnrollmentProperties.PRODUCT_CODE.
							 * equalsIgnoreCase(key)) { if
							 * (StringUtil.isEmpty(value)) throw new
							 * SwitchException(ITxnErrorCodes.EMPTY_PRODUCT_CODE
							 * ); product = productDistributionService.
							 * findProcurementProductByCode(value); if
							 * (ObjectUtil.isEmpty(product)) throw new
							 * SwitchException(ITxnErrorCodes.
							 * PRODUCT_DOES_NOT_EXIST);
							 * procurementDetail.setProcurementProduct(product);
							 * }
							 */
							try {
								if (TxnEnrollmentProperties.QUALITY.equalsIgnoreCase(key)) {
									if (StringUtil.isEmpty(value))
										throw new SwitchException(ITxnErrorCodes.EMPTY_PROCUREMENT_GRADE);
									ProcurementGrade procurementGrade = productDistributionService
											.findProcurementGradeByCode(value);
									if (ObjectUtil.isEmpty(procurementGrade))
										throw new SwitchException(ITxnErrorCodes.QUALITY_DOES_NOT_EXIST);

									procurementDetail.setProcurementGrade(procurementGrade);
									procurementDetail.setQuality(value);
									procurementDetail.setProcurementProduct(
											ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? null
													: procurementGrade.getProcurementVariety().getProcurementProduct());
								}
								if (TxnEnrollmentProperties.NO_OF_BAGS.equalsIgnoreCase(key)) {
									procurementDetail.setNumberOfBags(Long.valueOf(value));
								}
								if (TxnEnrollmentProperties.GROSS_WEIGHT.equalsIgnoreCase(key)) {
									procurementDetail.setGrossWeight(Double.valueOf(value));
								}
								if (TxnEnrollmentProperties.TARE_WEIGHT.equalsIgnoreCase(key)) {
									procurementDetail.setTareWeight(Double.valueOf(value));
								}
								if (TxnEnrollmentProperties.NET_WEIGHT.equalsIgnoreCase(key)) {
									procurementDetail.setNetWeight(Double.valueOf(value));
								}
								if (TxnEnrollmentProperties.PRICE_PER_UNIT.equalsIgnoreCase(key)) {
									procurementDetail.setRatePerUnit(Double.valueOf(value));
								}
								if (TxnEnrollmentProperties.SUB_TOTAL.equalsIgnoreCase(key)) {
									procurementDetail.setSubTotal(Double.valueOf(value));
								}
							} catch (Exception e) {
								throw new SwitchException(ITxnErrorCodes.DATA_CONVERSION_ERROR);
							}
						}
						// if(gradeMaster.getProduct().getId() !=
						// product.getId())
						// throw new
						// SwitchException(ITxnErrorCodes.PRODUCT_VARIETY_MISMATCH);

						procurementDetail.setProcurement(procurement);
						procurementDetails.add(procurementDetail);
					}
				}
			}
			/** SAVING PROCUREMENT,PROCUREMENT DETAIL OBJECT **/
			try {
				/** SETTING FARMER AGRO TRANSACTION INTO PROCUREMENT **/
				/*
				 * if(isRegisteredFarmer && !ObjectUtil.isEmpty(farmerAccount)){
				 * farmerAccount.setBalance(farmerProcureTxn.getBalAmount());
				 * farmerProcureTxn.setAccount(farmerAccount); }
				 */
				// procurement.setAgroTransaction(agentTransaction);
				procurement.setAgroTransaction(farmerProcureTxn);
				procurement.setProcurementDetails(procurementDetails);
				procurement.setCreatedDate(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
				procurement.setCreatedUser(agentName);
				productDistributionService.addProcurement(procurement);
				agent.setReceiptNumber(receiptNo);
			} catch (Exception e) {
				LOGGER.info(e.getMessage());
				throw new SwitchException(ITxnErrorCodes.ERROR);
			}
		} else {
			/* *//** OFFLINE TXN **/
			/*
			*//** GET REQUEST DATA **/
			String receiptNo = (String) reqData.get(TxnEnrollmentProperties.RECEIPT_NO);
			if (!StringUtil.isEmpty(receiptNo)) {
				agentService.updateAgentReceiptNoSequence(agentId, receiptNo);
			}
			String samithiCode = (String) reqData.get(TxnEnrollmentProperties.SAMITHI_CODE);
			String villageCode = (String) reqData.get(TxnEnrollmentProperties.PROCUREMENT_VILLAGE_CODE);
			boolean registeredFarmer = true;
			
			String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
			String procurementDate = (String) reqData.get(TxnEnrollmentProperties.PROCUREMENT_DATE);
			String totalAmount = (String) reqData.get(TxnEnrollmentProperties.TOTAL_AMOUNT);

			String farmId = (reqData.containsKey(TxnEnrollmentProperties.FARM_ID))
					? (String) reqData.get(TxnEnrollmentProperties.FARM_ID) : "";
			String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
			String longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
			

			// String paymentAmt = (String)
			// reqData.get(TxnEnrollmentProperties.PAYMENT_AMOUNT);

			/** FORMING OFFLINE PROCUREMENT OBJECT **/

			OfflineProcurement offlineProcurement = new OfflineProcurement();
			offlineProcurement.setAgentId(agentId);
			offlineProcurement.setDeviceId(serialNo);
			offlineProcurement.setIsReg(isReg);
			offlineProcurement.setProcMasterType(!StringUtil.isEmpty(supplierType) ? supplierType : "-1");
			// offlineProcurement.setProcMasterType(supplierType);
		/*	if ("99".equalsIgnoreCase(!StringUtil.isEmpty(supplierType) ? supplierType : "99")) {
				offlineProcurement.setFarmerId(farmerId);
			} else {
				MasterData master = farmerService.findMasterDataIdByCode(supplierType);
				offlineProcurement.setProcMasterTypeId(master != null ? String.valueOf(master.getId()) : null);
			}
			if (!StringUtil.isEmpty(farmerId)) {
				MasterData data = farmerService.findMasterDataIdByCode(farmerId);
				if (!StringUtil.isEmpty(data)) {
					offlineProcurement.setProcMasterTypeId(String.valueOf(data.getId()));
				} else {
					offlineProcurement.setProcMasterTypeId("");
				}

			}*/Farmer farmerList = new Farmer();
			if ("99".equalsIgnoreCase(!StringUtil.isEmpty(supplierType) ? supplierType : "99")  
			        || "11".equalsIgnoreCase(!StringUtil.isEmpty(supplierType) ? supplierType : "11")
			        ) {
				if(isReg.equalsIgnoreCase("0")){
					offlineProcurement.setFarmerId(farmerId);
	                 farmerList = farmerService.findFarmerByFarmerId(farmerId, tenantId);
	                 offlineProcurement.setProcMasterTypeId(!ObjectUtil.isEmpty(farmerList) && !ObjectUtil.isEmpty(farmerList.getSamithi()) && 
	                 		!StringUtil.isEmpty(farmerList.getSamithi().getCode()) ? farmerList.getSamithi().getCode() : "");
				}else{
					offlineProcurement.setFarmerId(farmerId);
					offlineProcurement.setMobileNumber(!StringUtil.isEmpty(mobileNumber) ? mobileNumber : "");
				}
				
                
                
            } else {
                MasterData master = farmerService.findMasterDataIdByCode(farmerId);
                offlineProcurement.setProcMasterTypeId(!ObjectUtil.isEmpty(master) ? master.getCode() : "");
            }
			//offlineProcurement.setFarmerId(farmerId);
			offlineProcurement.setProcurementDate(procurementDate);
			offlineProcurement.setReceiptNo(receiptNo);
			offlineProcurement.setServicePointId(servPointId);
			offlineProcurement.setSamithiCode(samithiCode);
			offlineProcurement.setVillageCode(villageCode);
			offlineProcurement.setTotalAmount(totalAmount);
			offlineProcurement.setPaymentAmt(paymentAmt);
			offlineProcurement.setSeasonCode(season);
			offlineProcurement.setRoadMapCode(roadMapCode);
			offlineProcurement.setFarmerAttnce(farmerAttnce);
			offlineProcurement.setWarehouseId(ffc);
			offlineProcurement.setCropType(cType);
			offlineProcurement.setInvoiceNo(invoiceNo);
			if (!StringUtil.isEmpty(buyer)){
				Customer customer=clientService.findCustomerById(buyer);
				offlineProcurement.setBuyer(customer != null ? customer : null);
			}
			if (!StringUtil.isEmpty(farmerAttnce)
					|| farmerAttnce.equalsIgnoreCase("") && farmerAttnce.equalsIgnoreCase("1")) {
				offlineProcurement.setSubstituteName(substituteName);
			} else {
				offlineProcurement.setSubstituteName(null);
			}
			offlineProcurement.setVehicleNo(vehicleNum);
			if (branchId.equalsIgnoreCase("AWI") || tenantId.equalsIgnoreCase("AWI")) {
				
				Farm farm = farmerService.findFarmByfarmId(farmId);
				offlineProcurement.setFarmId(farm.getFarmId());
				/*
				 * if (registeredFarmer) { if (StringUtil.isEmpty(farmId)){
				 * throw new SwitchException(ITxnErrorCodes.EMPTY_FARM_ID);
				 * 
				 * }else{ offlineProcurement.setFarmId(farmId); } }
				 */
			}
			offlineProcurement.setCreatedDate(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
			offlineProcurement.setCreatedUser(agentName);
			if (StringUtil.isEmpty(paymentAmt) || "0".equalsIgnoreCase(paymentAmt)) {
				offlineProcurement.setPaymentMode(ESEAccount.PAYMENT_MODE_CREDIT);
			} else {
				offlineProcurement.setPaymentMode(ESEAccount.PAYMENT_MODE_CASH);
			}

			// sets mobile number for unregistered farmer
			if (!StringUtil.isEmpty(mobileNumber))
				offlineProcurement.setMobileNumber(mobileNumber);

			offlineProcurement.setLatitude(!StringUtil.isEmpty(latitude) ? latitude : "0");
			offlineProcurement.setLongitude(!StringUtil.isEmpty(longitude) ? longitude : "0");
			offlineProcurement.setTotalLabourCost(Double.valueOf(!StringUtil.isEmpty(labourCost) ? labourCost : "0.0"));
			offlineProcurement.setTransportCost(Double.valueOf(!StringUtil.isEmpty(transCost) ? transCost : "0.0"));
			offlineProcurement.setVehicleType(!StringUtil.isEmpty(TRANSPORTATION_VEHICLE) ? TRANSPORTATION_VEHICLE : "");
			if(!StringUtil.isEmpty(getEnableLoanModule()) && getEnableLoanModule().equalsIgnoreCase("1")){
				
				Double loanIntPer =0.0;
				Double loanRepaymentAmt=0.0;
				Double loanRepayAmt=0.0;
				Double outStandingAmt =0.0;
				Double finalAmt=0.00;
				if (!StringUtil.isEmpty(farmerId)) {
				 
					account = accountService.findAccountByProfileIdAndProfileType(farmerList.getFarmerId(),
						ESEAccount.CONTRACT_ACCOUNT);
				if (!ObjectUtil.isEmpty(account) && account.getLoanAmount()>0.00 && account.getOutstandingLoanAmount() >0.00) {
					if (!StringUtil.isEmpty(offlineProcurement.getTotalAmount())) {
						LoanInterest loanVal = farmerService.findLoanPercent(Long.valueOf(totalAmount));						
						loanIntPer = ObjectUtil.isEmpty(loanVal) ? 0.0 : loanVal.getInterest();						
						outStandingAmt=account.getOutstandingLoanAmount();
						loanRepayAmt =((loanIntPer)/100* (Double.valueOf(totalAmount) )); 					
						offlineProcurement.setLoanInterest(!ObjectUtil.isEmpty(loanIntPer) ? loanIntPer : 0.0);
						
						if(!ObjectUtil.isEmpty(outStandingAmt) && outStandingAmt==0){
							offlineProcurement.setFinalPayAmt(!StringUtil.isEmpty(offlineProcurement.getTotalAmount()) ? Double.valueOf(offlineProcurement.getTotalAmount()) : 0.0);
						}else{
							if(!ObjectUtil.isEmpty(loanRepayAmt) && !ObjectUtil.isEmpty(outStandingAmt) 
									&& loanRepayAmt>0 && outStandingAmt < loanRepayAmt){
									finalAmt = Double.valueOf(totalAmount)-outStandingAmt;
										offlineProcurement.setFinalPayAmt(!ObjectUtil.isEmpty(finalAmt) ? finalAmt : 0D);		
										offlineProcurement.setLoanRepaymentAmt(!ObjectUtil.isEmpty(outStandingAmt) ? outStandingAmt : 0.00);
									}else{
										finalAmt = Double.valueOf(totalAmount)-loanRepayAmt;
										offlineProcurement.setFinalPayAmt(!ObjectUtil.isEmpty(finalAmt) ? finalAmt : 0D);	
										offlineProcurement.setLoanRepaymentAmt(!ObjectUtil.isEmpty(loanRepayAmt) ? loanRepayAmt : 0.00);
										
										
									}
									
						}
						
					}
		
				}
			}
			}
			offlineProcurement.setStatusCode(ESETxnStatus.PENDING.ordinal());
			offlineProcurement.setStatusMsg(ESETxnStatus.PENDING.toString());
			Set<OfflineProcurementDetail> offlineProcurementDetails = new HashSet<OfflineProcurementDetail>();

			/** FORMING OFFLINE PROCUREMENT DETAIL OBJECT **/
			Collection collection = (Collection) reqData.get(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_DETAIL);
			List<com.sourcetrace.eses.txn.schema.Object> objectList = collection.getObject();
			for (Object object : objectList) {
				List<Data> offlineProcurementDataList = object.getData();
				OfflineProcurementDetail offlineProcurementDetail = new OfflineProcurementDetail();

				for (Data offlineProcurementData : offlineProcurementDataList) {

					String key = offlineProcurementData.getKey();
					String value = offlineProcurementData.getValue();

					if (TxnEnrollmentProperties.PRODUCT_CODE.equalsIgnoreCase(key)) {
						offlineProcurementDetail.setProductCode(value);
					}

					if (TxnEnrollmentProperties.QUALITY.equalsIgnoreCase(key)) {
						offlineProcurementDetail.setQuality(value);
					}
					if (TxnEnrollmentProperties.NO_OF_BAGS.equalsIgnoreCase(key)) {
						offlineProcurementDetail.setNumberOfBags(value);
					}
					if (TxnEnrollmentProperties.NO_OF_FRUIT_BAGS.equalsIgnoreCase(key)){
						offlineProcurementDetail.setNumberOfFruitBags(value);
					}
					if (TxnEnrollmentProperties.GROSS_WEIGHT.equalsIgnoreCase(key)) {
						offlineProcurementDetail.setGrossWeight(value);
					}
					if (TxnEnrollmentProperties.TARE_WEIGHT.equalsIgnoreCase(key)) {
						offlineProcurementDetail.setTareWeight(value);
					}
					if (TxnEnrollmentProperties.NET_WEIGHT.equalsIgnoreCase(key)) {
						offlineProcurementDetail.setNetWeight(value);
					}
					if (TxnEnrollmentProperties.PRICE_PER_UNIT.equalsIgnoreCase(key)) {
						offlineProcurementDetail.setRatePerUnit(value);
					}
					if (TxnEnrollmentProperties.SUB_TOTAL.equalsIgnoreCase(key)) {
						offlineProcurementDetail.setSubTotal(value);
					}
					if (TxnEnrollmentProperties.BATCH_NO.equalsIgnoreCase(key)) {
						offlineProcurementDetail.setBatchNo(value);
					}

					if (TxnEnrollmentProperties.PROCURMENT_UNIT.equalsIgnoreCase(key)) {
						offlineProcurementDetail.setUnit(value);
					}
				}
				offlineProcurementDetail.setOfflineProcurement(offlineProcurement);
				offlineProcurementDetails.add(offlineProcurementDetail);
			}

			/**
			 * SAVING OFFLINE PROCUREMENT, OFFLINE PROCUREMENT DETAIL OBJECT
			 **/
			try {
				offlineProcurement.setBranchId(head.getBranchId());
				offlineProcurement.setOfflineProcurementDetails(offlineProcurementDetails);

				double totalInv = Double.valueOf(offlineProcurement.getTotalAmount())
						+ offlineProcurement.getTotalLabourCost() + offlineProcurement.getTransportCost();
				offlineProcurement.setInvoiceValue(totalInv);
				productDistributionService.addOfflineProcurementAndOfflineProcurementDetail(offlineProcurement);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/** FORMING RESPONSE **/
		Map resp = new HashMap();
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

		Head head = (Head) reqData.get(TransactionProperties.HEAD);

		String receiptNo = (String) reqData.get(TxnEnrollmentProperties.RECEIPT_NO);
		List<AgroTransaction> existingList = productDistributionService.findAgroTxnByReceiptNo(receiptNo);
		if (!ObjectUtil.isListEmpty(existingList)) {
			productDistributionService.updateAgroTxnVillageWarehouseAndAccount(existingList);
		}

		Map resp = new HashMap();
		return resp;
	}

	/**
	 * Sets the product distribution service.
	 * 
	 * @param productDistributionService
	 *            the new product distribution service
	 */
	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	/**
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
	}

	/**
	 * Sets the location service.
	 * 
	 * @param locationService
	 *            the new location service
	 */
	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	/**
	 * Sets the product service.
	 * 
	 * @param productService
	 *            the new product service
	 */
	public void setProductService(IProductService productService) {

		this.productService = productService;
	}

	/**
	 * Gets the product service.
	 * 
	 * @return the product service
	 */
	public IProductService getProductService() {

		return productService;
	}

	/**
	 * Sets the farmer service.
	 * 
	 * @param farmerService
	 *            the new farmer service
	 */
	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	/**
	 * Gets the farmer service.
	 * 
	 * @return the farmer service
	 */
	public IFarmerService getFarmerService() {

		return farmerService;
	}

	/**
	 * Sets the account service.
	 * 
	 * @param accountService
	 *            the accountService to set
	 */
	public void setAccountService(IAccountService accountService) {

		this.accountService = accountService;
	}

	/**
	 * Gets the account service.
	 * 
	 * @return the accountService
	 */
	public IAccountService getAccountService() {

		return accountService;
	}

	/**
	 * Sets the service point service.
	 * 
	 * @param servicePointService
	 *            the new service point service
	 */
	public void setServicePointService(IServicePointService servicePointService) {

		this.servicePointService = servicePointService;
	}

	/**
	 * Gets the service point service.
	 * 
	 * @return the service point service
	 */
	public IServicePointService getServicePointService() {

		return servicePointService;
	}

	/**
	 * Sets the device service.
	 * 
	 * @param deviceService
	 *            the new device service
	 */
	public void setDeviceService(IDeviceService deviceService) {

		this.deviceService = deviceService;
	}

	/**
	 * Gets the device service.
	 * 
	 * @return the device service
	 */
	public IDeviceService getDeviceService() {

		return deviceService;
	}

	/**
	 * Sets the agent service.
	 * 
	 * @param agentService
	 *            the new agent service
	 */
	public void setAgentService(IAgentService agentService) {

		this.agentService = agentService;
	}

	/**
	 * Gets the agent service.
	 * 
	 * @return the agent service
	 */
	public IAgentService getAgentService() {

		return agentService;
	}

	/**
	 * Gets the product distribution service.
	 * 
	 * @return the product distribution service
	 */
	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getEnableLoanModule() {
		return enableLoanModule;
	}

	public void setEnableLoanModule(String enableLoanModule) {
		this.enableLoanModule = enableLoanModule;
	}

	public IClientService getClientService() {
		return clientService;
	}

	public void setClientService(IClientService clientService) {
		this.clientService = clientService;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public ESEAccount getAccount() {
		return account;
	}

	public void setAccount(ESEAccount account) {
		this.account = account;
	}
	
}
