package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
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

import com.ese.entity.traceability.ProcurementTraceability;
import com.ese.entity.traceability.ProcurementTraceabilityDetails;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTNR;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurementTraceability;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurementTraceabilityDetails;
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
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
@Component
public class ProcurementTraceabilityAdapter implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(ProcurementTraceabilityAdapter.class.getName());
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
	private String agentName;

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
		String messageNo=head.getMsgNo();
		String serialNo = head.getSerialNo();
		String servPointId = head.getServPointId();
		String txnMode = head.getMode();
		String branchId = head.getBranchId();
		String tenantId = head.getTenantId();

		LOGGER.info("AGENT ID : " + agentId);
		LOGGER.info("SERIAL NO : " + serialNo);
		LOGGER.info("SERVICE POINT ID : " + servPointId);
		LOGGER.info("TXN MODE: " + txnMode);
		String season = (String) reqData.get(TxnEnrollmentProperties.CURRENT_SEASON_CODE);

		String paymentAmt = (String) reqData.get(TxnEnrollmentProperties.PAYMENT);

		String stripTest = (String) reqData.get(TxnEnrollmentProperties.CPROCUREMENT_STRIP_TEST);

		String trash = (String) reqData.get(TxnEnrollmentProperties.CPROCUREMENT_TRASH);

		String moisture = (String) reqData.get(TxnEnrollmentProperties.CPROCUREMENT_MOISTURE);

		String kowdikaps = (String) reqData.get(TxnEnrollmentProperties.CPROCUREMENT_KOWDIKAPAS);

		String stapleLeng = (String) reqData.get(TxnEnrollmentProperties.CPROCUREMENT_STAPLE_LENGTH);

		String yellowCotton = (String) reqData.get(TxnEnrollmentProperties.CPROCUREMENT_YELLOW_COTTON);

		String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);

		String longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
		String procurCenter = (String) reqData.get(TxnEnrollmentProperties.CPROCUREMENT_CENTER);

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

			ProcurementTraceability existing = productDistributionService.findProcurementTraceabtyByRecNo(receiptNo);
			if (!ObjectUtil.isEmpty(existing)) {
				throw new SwitchException(ITxnErrorCodes.PROCUREMENT_EXIST);
			}

			String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
			if (StringUtil.isEmpty(farmerId))
				throw new SwitchException(ITxnErrorCodes.EMPTY_FARMER_ID);

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

			Warehouse samithi = null;
			Farmer farmer = null;
			String procurementDate = (String) reqData.get(TxnEnrollmentProperties.PROCUREMENT_DATE);
			String totalAmount = (String) reqData.get(TxnEnrollmentProperties.TOTAL_AMOUNT);

			farmer = farmerService.findFarmerByFarmerId(farmerId);
			if (ObjectUtil.isEmpty(farmer))
				throw new SwitchException(ITxnErrorCodes.FARMER_NOT_EXIST);

			/** CHECKING FARMER STATUS **/
			if (farmer.getStatus() == Farmer.Status.INACTIVE.ordinal()) {
				throw new SwitchException(ITxnErrorCodes.FARMER_INACTIVE);
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

			// Parsing total amount
			double txnAmount = 0;
			try {
				txnAmount = Double.valueOf(totalAmount);
			} catch (Exception e) {
				LOGGER.info(e.getMessage());
				e.printStackTrace();
			}

			/** FORMING PROCUREMENT OBJECT * */
			ProcurementTraceability procurementTraceability = new ProcurementTraceability();

			// Added payment amount in to po number column because unused column
			// procurementTraceability.setPoNumber(String.valueOf(paymentAmount));
			procurementTraceability.setPaymentAmount(paymentAmount);

			procurementTraceability.setTotalProVal(txnAmount);

			procurementTraceability.setStripPositive(Integer.valueOf(stripTest));
			procurementTraceability.setTrash(trash);
			procurementTraceability.setMoisture(moisture);
			procurementTraceability.setStapleLen(stapleLeng);
			procurementTraceability.setKowdi_kapas(kowdikaps);
			procurementTraceability.setYellow_cotton(yellowCotton);
			// sets mobile number for unregistered farmer

			DataHandler photoDataHandler = (DataHandler) reqData.get(TxnEnrollmentProperties.PHOTO);
			byte[] photoContent = null;
			if (photoDataHandler != null) {
				try {
					photoContent = IOUtils.toByteArray(photoDataHandler.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (photoContent != null) {
					procurementTraceability.setStripImage(photoContent);
				}

			}
			procurementTraceability.setLatitude(!StringUtil.isEmpty(latitude) ? latitude : "0");
			procurementTraceability.setLongitude(!StringUtil.isEmpty(longitude) ? longitude : "0");
		//	procurementTraceability.setVillage(farmer.getVillage());
			procurementTraceability.setSeason(season);
			if (!StringUtil.isEmpty(procurCenter)) {
				// Registered farmer procurement
				samithi = locationService.findSamithiByCode(procurCenter);
				if (ObjectUtil.isEmpty(samithi))
					throw new SwitchException(ITxnErrorCodes.SAMITHI_NOT_EXIST);
			}
			// AgroTransaction agentTransaction = new AgroTransaction();
			procurementTraceability.setWarehouse(samithi);
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
				farmerProcureTxn.setTxnDesc(procurementTraceability.PROCUREMENT_AMOUNT);
				farmerProcureTxn.setSamithi(samithi);
			} catch (Exception e) {
				throw new SwitchException(ITxnErrorCodes.DATA_CONVERSION_ERROR);
			}

			Set<ProcurementTraceabilityDetails> procurementTraceDetailSet = new HashSet<ProcurementTraceabilityDetails>();

			/** FORMING PROCUREMENT DETAIL OBJECT **/
			Collection collection = (Collection) reqData.get(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_DETAIL);
			if (!ObjectUtil.isEmpty(collection)) {
				List<com.sourcetrace.eses.txn.schema.Object> objectList = collection.getObject();
				if (!ObjectUtil.isEmpty(objectList)) {
					for (Object object : objectList) {
						List<Data> procurementDataList = object.getData();
						ProcurementTraceabilityDetails procurementTraceDetail = new ProcurementTraceabilityDetails();
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

									procurementTraceDetail.setProcuremntGrade(procurementGrade);
									procurementTraceDetail.setProcurementProduct(
											ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? null
													: procurementGrade.getProcurementVariety().getProcurementProduct());
								}
								if (TxnEnrollmentProperties.NO_OF_BAGS.equalsIgnoreCase(key)) {
									procurementTraceDetail.setNumberOfBags(Long.valueOf(value));
								}
								if (TxnEnrollmentProperties.GROSS_WEIGHT.equalsIgnoreCase(key)) {
									procurementTraceDetail.setNetWeight(Double.valueOf(value));
								}
								if (TxnEnrollmentProperties.EDITED_MSP.equalsIgnoreCase(key)) {
									procurementTraceDetail.setPrice(Double.valueOf(value));
								}
								if (TxnEnrollmentProperties.MSP_PERCENTAGE.equalsIgnoreCase(key)) {
									procurementTraceDetail.setPremiumPrice(Double.valueOf(value));
								}

								if (TxnEnrollmentProperties.SUB_TOTAL.equalsIgnoreCase(key)) {
									procurementTraceDetail.setTotalPricepremium(Double.valueOf(value));
								}
							} catch (Exception e) {
								throw new SwitchException(ITxnErrorCodes.DATA_CONVERSION_ERROR);
							}
						}
						// if(gradeMaster.getProduct().getId() !=
						// product.getId())
						// throw new
						// SwitchException(ITxnErrorCodes.PRODUCT_VARIETY_MISMATCH);

						procurementTraceDetail.setProcurementTraceability(procurementTraceability);
						procurementTraceDetailSet.add(procurementTraceDetail);
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
				// procurementTraceability.setAgroTransaction(agentTransaction);
				procurementTraceability.setAgroTransaction(farmerProcureTxn);
				procurementTraceability.setProcurmentTraceabilityDetails(procurementTraceDetailSet);
				procurementTraceability
						.setCreatedDate(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
				procurementTraceability.setCreatedUser(agentName);
				productDistributionService.addProcurementTraceability(procurementTraceability);
				agent.setReceiptNumber(receiptNo);
			} catch (Exception e) {
				LOGGER.info(e.getMessage());
				throw new SwitchException(ITxnErrorCodes.ERROR);
			}
		} else {
			/* *//** OFFLINE TXN **/
			/*
			*//** GET REQUEST DATA **/
			OfflineProcurementTraceability exist=productDistributionService.findOfflineProcurementTraceabilityByMessageNo(messageNo);
			if(exist==null || ObjectUtil.isEmpty(exist)){
			String receiptNo = (String) reqData.get(TxnEnrollmentProperties.RECEIPT_NO);
			if (!StringUtil.isEmpty(receiptNo)) {
				agentService.updateAgentReceiptNoSequence(agentId, receiptNo);
			}
			String villageCode = (String) reqData.get(TxnEnrollmentProperties.PROCUREMENT_VILLAGE_CODE);

			String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
			String procurementDate = (String) reqData.get(TxnEnrollmentProperties.PROCUREMENT_DATE);
			String totalAmount = (String) reqData.get(TxnEnrollmentProperties.TOTAL_AMOUNT);

			// String paymentAmt = (String)
			// reqData.get(TxnEnrollmentProperties.PAYMENT_AMOUNT);

		
			

			OfflineProcurementTraceability offlineProcurementTraceabty = new OfflineProcurementTraceability();
			offlineProcurementTraceabty.setAgentId(agentId);
			offlineProcurementTraceabty.setMessageNo(messageNo);
			offlineProcurementTraceabty.setDeviceId(serialNo);
			offlineProcurementTraceabty.setFarmerId(farmerId);
			offlineProcurementTraceabty.setReceiptNo(receiptNo);
			offlineProcurementTraceabty.setServicePointId(servPointId);
			offlineProcurementTraceabty.setTotalProVal(Double.valueOf(totalAmount));
			offlineProcurementTraceabty.setPaymentAmount(Double.valueOf(paymentAmt));
			offlineProcurementTraceabty.setSeason(season);
			offlineProcurementTraceabty.setVillageCode(villageCode);
			offlineProcurementTraceabty
					.setCreatedDate(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
			offlineProcurementTraceabty
					.setProcurementDate(DateUtil.convertStringToDate(procurementDate, DateUtil.TXN_DATE_TIME));
			offlineProcurementTraceabty.setCreatedUser(agentId);
			offlineProcurementTraceabty.setStripPositive(Integer.valueOf(stripTest));
			offlineProcurementTraceabty.setTrash(trash);
			offlineProcurementTraceabty.setMoisture(moisture);
			offlineProcurementTraceabty.setStapleLen(stapleLeng);
			offlineProcurementTraceabty.setKowdi_kapas(kowdikaps);
			offlineProcurementTraceabty.setYellow_cotton(yellowCotton);
			offlineProcurementTraceabty.setWarehouseId(procurCenter);
			// sets mobile number for unregistered farmer
			offlineProcurementTraceabty.setLatitude(!StringUtil.isEmpty(latitude) ? latitude : "0");
			offlineProcurementTraceabty.setLongitude(!StringUtil.isEmpty(longitude) ? longitude : "0");
			offlineProcurementTraceabty.setStatusCode(ESETxnStatus.PENDING.ordinal());
			offlineProcurementTraceabty.setStatusMsg(ESETxnStatus.PENDING.toString());
			DataHandler photoDataHandler = (DataHandler) reqData.get(TxnEnrollmentProperties.PHOTO);
			byte[] photoContent = null;
			if (photoDataHandler != null) {
				try {
					photoContent = IOUtils.toByteArray(photoDataHandler.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (photoContent != null) {
					offlineProcurementTraceabty.setStripImage(photoContent);
				}

			}
			Set<OfflineProcurementTraceabilityDetails> offlineProcurementDetails = new HashSet<OfflineProcurementTraceabilityDetails>();

			/** FORMING OFFLINE PROCUREMENT DETAIL OBJECT **/
			Collection collection = (Collection) reqData.get(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_DETAIL);
			List<com.sourcetrace.eses.txn.schema.Object> objectList = collection.getObject();
			for (Object object : objectList) {
				List<Data> offlineProcurementDataList = object.getData();
				OfflineProcurementTraceabilityDetails offlineProcurementTraceabtyDetail = new OfflineProcurementTraceabilityDetails();

				for (Data offlineProcurementData : offlineProcurementDataList) {

					String key = offlineProcurementData.getKey();
					String value = offlineProcurementData.getValue();
					
					if (TxnEnrollmentProperties.QUALITY.equalsIgnoreCase(key)) {
						if (StringUtil.isEmpty(value))
							throw new SwitchException(ITxnErrorCodes.EMPTY_PROCUREMENT_GRADE);
						ProcurementGrade procurementGrade = productDistributionService
								.findProcurementGradeByCode(value);
						if (ObjectUtil.isEmpty(procurementGrade))
							throw new SwitchException(ITxnErrorCodes.QUALITY_DOES_NOT_EXIST);

						offlineProcurementTraceabtyDetail.setGradeCode(value);
						
						if(!ObjectUtil.isEmpty(procurementGrade.getProcurementVariety())){
							offlineProcurementTraceabtyDetail.setUnit(procurementGrade.getProcurementVariety().getProcurementProduct().getUnit());
						}
						
					}

					if (TxnEnrollmentProperties.PRODUCT_CODE.equalsIgnoreCase(key)) {
						offlineProcurementTraceabtyDetail.setProductCode(value);
					}

					if (TxnEnrollmentProperties.NO_OF_BAGS.equalsIgnoreCase(key)) {
						offlineProcurementTraceabtyDetail.setNumberOfBags(Long.valueOf(value));
					}

					if (TxnEnrollmentProperties.NET_WEIGHT.equalsIgnoreCase(key)) {
						offlineProcurementTraceabtyDetail.setTotalPrice(Double.valueOf(value));
					}

					
					if (TxnEnrollmentProperties.GROSS_WEIGHT.equalsIgnoreCase(key)) {
						offlineProcurementTraceabtyDetail.setNetWeight(Double.valueOf(value));
					}
					if (TxnEnrollmentProperties.EDITED_MSP.equalsIgnoreCase(key)) {
						offlineProcurementTraceabtyDetail.setPrice(Double.valueOf(value));
					}

					if (TxnEnrollmentProperties.MSP_PERCENTAGE.equalsIgnoreCase(key)) {
						offlineProcurementTraceabtyDetail.setPremiumPrice(Double.valueOf(value));
					}

					if (TxnEnrollmentProperties.SUB_TOTAL.equalsIgnoreCase(key)) {
						offlineProcurementTraceabtyDetail.setTotalPricepremium(Double.valueOf(value));
					}

			/*		if (TxnEnrollmentProperties.PROCURMENT_UNIT.equalsIgnoreCase(key)) {
						offlineProcurementTraceabtyDetail.setUnit(value);
					}*/
				}
				offlineProcurementTraceabtyDetail.setOfflineProcurementTraceability(offlineProcurementTraceabty);
				offlineProcurementDetails.add(offlineProcurementTraceabtyDetail);
			}

			/**
			 * SAVING OFFLINE PROCUREMENT, OFFLINE PROCUREMENT DETAIL OBJECT
			 **/
			try {
				offlineProcurementTraceabty.setBranchId(branchId);
				offlineProcurementTraceabty.setOfflineprocurTraceabtyDetails(offlineProcurementDetails);
				offlineProcurementTraceabty.setTotalProVal(Double.valueOf(totalAmount));
				productDistributionService.addOfflineTraceabtyProcurementAndDetail(offlineProcurementTraceabty);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
}
