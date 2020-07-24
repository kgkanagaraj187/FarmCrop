package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

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
import com.sourcetrace.eses.order.entity.txn.OfflineProcurementTraceability;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurementTraceabilityDetails;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
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
public class OfflineProcurementTraceabtySche {

	private static final Logger LOGGER = Logger.getLogger(OfflineProcurementTraceabtySche.class.getName());
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

	@Resource(name = "datasources")
	private Map<String, DataSource> datasources;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	public void process() {

		List<String> tenantIds = new ArrayList(datasources.keySet());

		for (String tenantId : tenantIds) {
			List<OfflineProcurementTraceability> offlineProcurements = productDistributionService
					.listPendingOfflineProcurementTraceList(tenantId);
			for (OfflineProcurementTraceability offlineProcurementTrace : offlineProcurements) {
				int statusCode = ESETxnStatus.SUCCESS.ordinal();
				String statusMsg = ESETxnStatus.SUCCESS.toString();
				try {
					if (StringUtil.isEmpty(offlineProcurementTrace.getAgentId()))
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ID_EMPTY);

					Agent agent = agentService.findAgentByAgentId(offlineProcurementTrace.getAgentId(), tenantId,
							offlineProcurementTrace.getBranchId());
					if (ObjectUtil.isEmpty(agent))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_AGENT);

					if (StringUtil.isEmpty(offlineProcurementTrace.getDeviceId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);

					Device device = deviceService.findDeviceBySerialNumber(offlineProcurementTrace.getDeviceId(),
							tenantId);
					if (ObjectUtil.isEmpty(device))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DEVICE);

					/*
					 * if (StringUtil.isEmpty(offlineProcurementTrace.
					 * getServicePointId())) throw new
					 * OfflineTransactionException(ITxnErrorCodes.
					 * EMPTY_SERV_POINT_ID);
					 * 
					 * ServicePoint servicePoint = servicePointService
					 * .findServicePointByServPointId(offlineProcurementTrace.
					 * getServicePointId(), tenantId); if
					 * (ObjectUtil.isEmpty(servicePoint)) throw new
					 * OfflineTransactionException(ITxnErrorCodes.
					 * INVALID_SERVICE_POINT);
					 */
					if (StringUtil.isEmpty(offlineProcurementTrace.getReceiptNo())) {
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_RECEIPT_NO);
					}
					ProcurementTraceability existing = productDistributionService
							.findProcurementTraceabtyByRecNoAndOperType(offlineProcurementTrace.getReceiptNo(),
									ESETxn.ON_LINE, tenantId);
					if (!ObjectUtil.isEmpty(existing))
						throw new OfflineTransactionException(ITxnErrorCodes.PROCUREMENT_EXIST);

					Warehouse samithi = null;
					Farmer farmer = null;
					ESEAccount farmerAccount = null;

					if (!StringUtil.isEmpty(offlineProcurementTrace.getFarmerId())) {
						farmer = farmerService.findFarmerByFarmerId(offlineProcurementTrace.getFarmerId(), tenantId);
						if (ObjectUtil.isEmpty(farmer) || farmer == null)
							throw new OfflineTransactionException(ITxnErrorCodes.FARMER_NOT_EXIST);

						/** FETCHING FARMER ACCOUNT AND VALIDATE **/
						farmerAccount = farmerService.findAccountBySeassonProcurmentProductFarmer(0L, farmer.getId(),
								tenantId);
						if (ObjectUtil.isEmpty(farmerAccount))
							throw new OfflineTransactionException(ITxnErrorCodes.FARMER_ACCOUNT_UNAVAILABLE);

						if (ESEAccount.INACTIVE == farmerAccount.getStatus())
							throw new OfflineTransactionException(ITxnErrorCodes.FARMER_ACCOUNT_INACTIVE);
					}
					// Parsing payment amount
					double paymentAmount = 0;

					if (!StringUtil.isEmpty(offlineProcurementTrace.getPaymentAmount())) {
						try {
							paymentAmount = Double.valueOf(offlineProcurementTrace.getPaymentAmount());
						} catch (Exception e) {
							LOGGER.info("Could not Parse paymentAount");
							e.printStackTrace();
						}
					}

					// Parsing total amount
					double txnAmount = 0;

					try {
						txnAmount = Double.valueOf(Double.valueOf(offlineProcurementTrace.getTotalProVal()));
					} catch (Exception e) {
						LOGGER.info(e.getMessage());
						e.printStackTrace();
					}

					// Reducing payment amount with txn amount
					/*
					 * if(paymentAmount>0){ txnAmount-=paymentAmount; }
					 */

					/** FORMING PROCUREMENT OBJECT **/
					ProcurementTraceability procurementTraceabty = new ProcurementTraceability();

					procurementTraceabty.setReceiptNo(offlineProcurementTrace.getReceiptNo());
					procurementTraceabty.setPaymentAmount(paymentAmount);

					procurementTraceabty.setTotalProVal(Double.valueOf(offlineProcurementTrace.getTotalProVal()));

					procurementTraceabty.setSeason(offlineProcurementTrace.getSeason());
					Warehouse warehouse = productService.findWarehouseByCode(offlineProcurementTrace.getWarehouseId(),tenantId);
					if (!ObjectUtil.isEmpty(warehouse)) {
						procurementTraceabty.setWarehouse(warehouse);
					}

					Farmer farmerList = farmerService.findFarmerByFarmerId(offlineProcurementTrace.getFarmerId(),
							tenantId);
					procurementTraceabty.setFarmer(!ObjectUtil.isEmpty(farmerList) ? farmerList : null);
					// if(tenantId.equalsIgnoreCase("AWI")){
					procurementTraceabty.setStripPositive(Integer.valueOf(offlineProcurementTrace.getStripPositive()));
					procurementTraceabty.setTrash(offlineProcurementTrace.getTrash());
					procurementTraceabty.setMoisture(offlineProcurementTrace.getMoisture());
					procurementTraceabty.setStapleLen(offlineProcurementTrace.getStapleLen());
					procurementTraceabty.setKowdi_kapas(offlineProcurementTrace.getKowdi_kapas());
					procurementTraceabty.setYellow_cotton(offlineProcurementTrace.getYellow_cotton());
					procurementTraceabty.setProcurementDate(offlineProcurementTrace.getProcurementDate());
					// procurement.setFarmId(offlineProcurement.getFarmId());
					procurementTraceabty.setStripImage(offlineProcurementTrace.getStripImage());
					procurementTraceabty.setLatitude(offlineProcurementTrace.getLatitude());
					procurementTraceabty.setLongitude(offlineProcurementTrace.getLongitude());
					procurementTraceabty.setCreatedUser(agent.getProfileId());
					procurementTraceabty.setCreatedDate(offlineProcurementTrace.getCreatedDate());
					procurementTraceabty.setBranchId(offlineProcurementTrace.getBranchId());

					// }
					// AgroTransaction agentTransaction = new AgroTransaction();
					AgroTransaction farmerProcureTxn = new AgroTransaction();
					// agentTransaction.setRefAgroTransaction(farmerProcureTxn);
					try {
						/** FORMING AGRO TRANSACTION FOR FARMER PROCUREMENT **/
						farmerProcureTxn.setReceiptNo(offlineProcurementTrace.getReceiptNo());
						farmerProcureTxn.setAgentId(agent.getProfileId());
						farmerProcureTxn.setAgentName((ObjectUtil.isEmpty(agent.getPersonalInfo()) ? ""
								: agent.getPersonalInfo().getAgentName()));
						farmerProcureTxn.setDeviceId(device.getCode());
						farmerProcureTxn.setDeviceName(device.getName());
						// farmerProcureTxn.setServicePointId(servicePoint.getCode());
						// farmerProcureTxn.setServicePointName(servicePoint.getName());
						farmerProcureTxn.setSeasonCode(offlineProcurementTrace.getSeason());
						farmerProcureTxn
								.setFarmerId(!ObjectUtil.isEmpty(farmer) && !StringUtil.isEmpty(farmer.getFarmerId())
										? farmer.getFarmerId() : NOT_APPLICABLE);
						farmerProcureTxn.setFarmerName(
								!ObjectUtil.isEmpty(farmer) ? farmer.getFirstName() + " " + farmer.getLastName()
										: offlineProcurementTrace.getFarmerId());

						farmerProcureTxn.setBranch_id(offlineProcurementTrace.getBranchId());

						/*
						 * if("99".equalsIgnoreCase(offlineProcurement.
						 * getProcMasterType())){ farmerProcureTxn.setFarmerId(
						 * (isRegisteredFarmer) ? farmer.getFarmerId() :
						 * NOT_APPLICABLE);
						 * farmerProcureTxn.setFarmerName((isRegisteredFarmer) ?
						 * farmer.getFirstName() + " " + farmer.getLastName() :
						 * offlineProcurement.getFarmerId());
						 * 
						 * } else {
						 * 
						 * farmerProcureTxn.setProcMasterTypeId(
						 * offlineProcurement.getProcMasterTypeId());
						 * 
						 * }
						 */
						// farmerProcureTxn.setIntBalance((isRegisteredFarmer) ?
						// farmerAccount.getBalance() : 0);

						farmerProcureTxn.setTxnTime(offlineProcurementTrace.getCreatedDate());
						farmerProcureTxn.setTxnAmount(txnAmount);
						// Farmer account should be credited for procurement
						// because direct payment will
						// be made
						// farmerProcureTxn.setBalAmount(farmerProcureTxn.getIntBalance()
						// +
						// farmerProcureTxn.getTxnAmount());
						farmerProcureTxn.setTxnType(TransactionTypeProperties.PROCUREMENT_TRACEABILITY_ENROLLMENT);
						// farmerProcureTxn.setProfType(Profile.CLIENT);
						farmerProcureTxn.setOperType(ESETxn.ON_LINE);
						farmerProcureTxn.setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
						// Setting samithi reference
						farmerProcureTxn.setSamithi(samithi);
					} catch (Exception e) {
						throw new OfflineTransactionException(ITxnErrorCodes.DATA_CONVERSION_ERROR);
					}

					Set<ProcurementTraceabilityDetails> procurementDetails = new HashSet<ProcurementTraceabilityDetails>();
					if (!ObjectUtil.isListEmpty(offlineProcurementTrace.getOfflineprocurTraceabtyDetails())) {

						/* FORMING PROCUREMENT DETAIL OBJECT * */
						for (OfflineProcurementTraceabilityDetails ofProcurementDetail : offlineProcurementTrace
								.getOfflineprocurTraceabtyDetails()) {
							ProcurementTraceabilityDetails procurementTraceDetail = new ProcurementTraceabilityDetails();

							/*
							 * if (StringUtil.isEmpty(ofProcurementDetail.
							 * getProductCode())) { throw new
							 * OfflineTransactionException(ITxnErrorCodes.
							 * EMPTY_PRODUCT_CODE); } procurementProduct =
							 * productDistributionService.
							 * findProcurementProductByCode(
							 * ofProcurementDetail.getProductCode()); if
							 * (ObjectUtil.isEmpty(procurementProduct)) { throw
							 * new OfflineTransactionException(ITxnErrorCodes.
							 * PRODUCT_DOES_NOT_EXIST); }
							 * procurementDetail.setProcurementProduct(
							 * procurementProduct);
							 */

							try {
								if (StringUtil.isEmpty(ofProcurementDetail.getGradeCode()))
									throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_PROCUREMENT_GRADE);
								ProcurementGrade procurementGrade = productDistributionService
										.findProcurementGradeByCode(ofProcurementDetail.getGradeCode(), tenantId);
								if (ObjectUtil.isEmpty(procurementGrade))
									throw new OfflineTransactionException(ITxnErrorCodes.QUALITY_DOES_NOT_EXIST);

								procurementTraceDetail.setProcuremntGrade(procurementGrade);
								procurementTraceDetail.setProcurementProduct(
										ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? null
												: procurementGrade.getProcurementVariety().getProcurementProduct());
								procurementTraceDetail
										.setNumberOfBags(Long.valueOf(ofProcurementDetail.getNumberOfBags()));
								procurementTraceDetail
										.setNetWeight((Double.valueOf(ofProcurementDetail.getNetWeight())));
								procurementTraceDetail.setPrice(Double.valueOf(ofProcurementDetail.getPrice()));
								procurementTraceDetail.setPremiumPrice(Double.valueOf(ofProcurementDetail.getPremiumPrice()));
								procurementTraceDetail
										.setTotalPricepremium(Double.valueOf(ofProcurementDetail.getTotalPricepremium()));
								procurementTraceDetail.setTotalPrice((Double.valueOf(ofProcurementDetail.getTotalPrice())));
								procurementTraceDetail.setTotalPricepremium(
										Double.valueOf(ofProcurementDetail.getTotalPricepremium()));
								procurementTraceDetail.setUnit(!ObjectUtil.isEmpty(ofProcurementDetail.getUnit())
										? ofProcurementDetail.getUnit() : "");
							} catch (Exception e) {
								throw new OfflineTransactionException(ITxnErrorCodes.DATA_CONVERSION_ERROR);
							}
							// Product-Grade Mapping
							// if(!ObjectUtil.isEmpty(procurementProduct) &&
							// !ObjectUtil.isEmpty(gradeMaster) &&
							// (procurementProduct.getId() !=
							// gradeMaster.getProduct().getId()))
							// throw new
							// OfflineTransactionException(ITxnErrorCodes.PRODUCT_VARIETY_MISMATCH);

							procurementTraceDetail.setProcurementTraceability(procurementTraceabty);
							procurementDetails.add(procurementTraceDetail);
						} // inner loop end
					}
					try {
						procurementTraceabty.setProcurmentTraceabilityDetails(procurementDetails);
						/*
						 * if (isRegisteredFarmer &&
						 * !ObjectUtil.isEmpty(farmerAccount)) {
						 * farmerAccount.setBalance(farmerProcureTxn.
						 * getBalAmount());
						 * farmerProcureTxn.setAccount(farmerAccount); }
						 */
						// procurement.setAgroTransaction(agentTransac\tion);
						procurementTraceabty.setAgroTransaction(farmerProcureTxn);

						productDistributionService.addProcurementTracebty(procurementTraceabty, tenantId);
						/** UPDATE ACCOUNT FOR FARMER **/
						/*
						 * if (isRegisteredFarmer &&
						 * !ObjectUtil.isEmpty(farmerAccount)) {
						 * accountService.update(farmerAccount); }
						 */
					} catch (Exception e) {
						LOGGER.info(e.getMessage());
						throw new OfflineTransactionException(ITxnErrorCodes.ERROR);
					}

				} // end try
				catch (OfflineTransactionException ote) {
					ote.printStackTrace();
					statusCode = ESETxnStatus.ERROR.ordinal();
					statusMsg = ote.getError();
				} catch (Exception e) { // Catches all type of exception except
					// OfflineTransactionException
					statusCode = ESETxnStatus.ERROR.ordinal();
					statusMsg = e.getMessage().substring(0,
							e.getMessage().length() > 40 ? 40 : e.getMessage().length());
				}
				offlineProcurementTrace.setStatusCode(statusCode);
				offlineProcurementTrace.setStatusMsg(statusMsg);
				productDistributionService.updateOfflineProcurementTrace(offlineProcurementTrace, tenantId);
			} // end for loop
		}
		System.gc();
	}

	/**
	 * Gets the product distribution service.
	 * 
	 * @return the product distribution service
	 */
	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
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
	 * Gets the agent service.
	 * 
	 * @return the agent service
	 */
	public IAgentService getAgentService() {

		return agentService;
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
	 * Gets the account service.
	 * 
	 * @return the account service
	 */
	public IAccountService getAccountService() {

		return accountService;
	}

	/**
	 * Sets the account service.
	 * 
	 * @param accountService
	 *            the new account service
	 */
	public void setAccountService(IAccountService accountService) {

		this.accountService = accountService;
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
	 * Sets the device service.
	 * 
	 * @param deviceService
	 *            the new device service
	 */
	public void setDeviceService(IDeviceService deviceService) {

		this.deviceService = deviceService;
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
	 * Sets the service point service.
	 * 
	 * @param servicePointService
	 *            the new service point service
	 */
	public void setServicePointService(IServicePointService servicePointService) {

		this.servicePointService = servicePointService;
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
	 * Sets the farmer service.
	 * 
	 * @param farmerService
	 *            the new farmer service
	 */
	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
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
	 * Sets the product service.
	 * 
	 * @param productService
	 *            the new product service
	 */
	public void setProductService(IProductService productService) {

		this.productService = productService;
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

}
