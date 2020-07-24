/*
 * OfflineProcurementSchedulerTask.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.sms.SMSHistory;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.OfflineSupplierProcurement;
import com.sourcetrace.eses.order.entity.txn.OfflineSupplierProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurement;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurementDetail;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.ISMSService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

@Component
public class OfflineSupplierProcurementSchedulerTask {

	private static final Logger LOGGER = Logger.getLogger(OfflineSupplierProcurementSchedulerTask.class.getName());
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
	private ISMSService smsService;
	@Autowired
	private IPreferencesService preferencesService;
	
	
	@Resource(name = "datasources")
	private Map<String, DataSource> datasources;
	private Farmer farmerList;
	String providerResponse = null;
	int smsType = SMSHistory.SMS_SINGLE;
	String status = null;
	String descrption = null;

	private ArrayList<String[]> smsData = new ArrayList<String[]>(); 
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	public void process() {

		List<String> tenantIds = new ArrayList(datasources.keySet());

		for (String tenantId : tenantIds) {
			List<OfflineSupplierProcurement> offlineSupplierProcurements = productDistributionService
					.listPendingOfflineSupplierProcurementList(tenantId);
			for (OfflineSupplierProcurement offlineSupplierProcurement : offlineSupplierProcurements) {
				int statusCode = ESETxnStatus.SUCCESS.ordinal();
				String statusMsg = ESETxnStatus.SUCCESS.toString();
				Warehouse samithi = null;
				boolean isRegisteredFarmer = false;					
				Farmer farmer = null;
				ESEAccount farmerAccount = null;
				try {
					if (StringUtil.isEmpty(offlineSupplierProcurement.getAgentId()))
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ID_EMPTY);

					Agent agent = agentService.findAgentByAgentId(offlineSupplierProcurement.getAgentId(), tenantId,
							offlineSupplierProcurement.getBranchId());
					if (ObjectUtil.isEmpty(agent))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_AGENT);

					if (StringUtil.isEmpty(offlineSupplierProcurement.getDeviceId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);

					Device device = deviceService.findDeviceBySerialNumber(offlineSupplierProcurement.getDeviceId(), tenantId);
					if (ObjectUtil.isEmpty(device))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DEVICE);

					if (StringUtil.isEmpty(offlineSupplierProcurement.getServicePointId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERV_POINT_ID);

					ServicePoint servicePoint = servicePointService
							.findServicePointByServPointId(offlineSupplierProcurement.getServicePointId(), tenantId);
					if (ObjectUtil.isEmpty(servicePoint))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_SERVICE_POINT);

					if (StringUtil.isEmpty(offlineSupplierProcurement.getReceiptNo())) {
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_RECEIPT_NO);
					}
					SupplierProcurement existing = productDistributionService.findSupplierProcurementByRecNoAndOperType(
							offlineSupplierProcurement.getReceiptNo(), ESETxn.ON_LINE, tenantId);
					if (!ObjectUtil.isEmpty(existing))
						throw new OfflineTransactionException(ITxnErrorCodes.PROCUREMENT_EXIST);

				
					ESEAccount agentAccount = accountService.findAccountByProfileId(agent.getProfileId(), tenantId);
					if (ObjectUtil.isEmpty(agentAccount))
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_UNAVAILABLE);

					if (agentAccount.getStatus() == ESEAccount.INACTIVE) {
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_INACTIVE);
					} else if (agentAccount.getStatus() == ESEAccount.BLOCKED) {
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_BLOCKED);
					}
					
					// Parsing payment amount
					double paymentAmount = 0;

					if (!StringUtil.isEmpty(offlineSupplierProcurement.getPaymentAmt())) {
						try {
							paymentAmount = Double.valueOf(offlineSupplierProcurement.getPaymentAmt());
						} catch (Exception e) {
							LOGGER.info("Could not Parse paymentAount");
							e.printStackTrace();
						}
					}

					// Parsing total amount
					double txnAmount = 0;

					try {
						txnAmount = Double.valueOf(Double.valueOf(offlineSupplierProcurement.getTotalAmount()));
					} catch (Exception e) {
						LOGGER.info(e.getMessage());
						e.printStackTrace();
					}

					/** FORMING PROCUREMENT OBJECT **/
					SupplierProcurement supplierProcurement = new SupplierProcurement();					
					
					if (!StringUtil.isEmpty(offlineSupplierProcurement.getWarehouseId())) {
						Warehouse warehouse = locationService.findCoOperativeByCode(offlineSupplierProcurement.getWarehouseId(),tenantId);
						if (!ObjectUtil.isEmpty(warehouse))
							supplierProcurement.setWarehouseId(String.valueOf(warehouse.getId()));
					}

					
					supplierProcurement.setPaymentAmount(paymentAmount);
					supplierProcurement.setIsRegSupplier(Integer.parseInt(offlineSupplierProcurement.getIsRegSupplier()));

					if(offlineSupplierProcurement.getIsRegSupplier().equalsIgnoreCase("0")){
						if (!StringUtil.isEmpty(offlineSupplierProcurement.getMobileNumber())){
							supplierProcurement.setMobileNumber(offlineSupplierProcurement.getMobileNumber());
							}
					}else{
						supplierProcurement.setMobileNumber("");
					}
				
					supplierProcurement.setSeason(productDistributionService.findCurrentSeason(tenantId));	
					supplierProcurement.setProcMasterType(offlineSupplierProcurement.getProcMasterType());	
			
					supplierProcurement.setTotalProVal(Double.valueOf(offlineSupplierProcurement.getTotalAmount()));

					supplierProcurement.setSeasonCode(offlineSupplierProcurement.getSeasonCode());
					supplierProcurement.setTotalLabourCost(offlineSupplierProcurement.getTotalLabourCost());
					supplierProcurement.setTransportCost(offlineSupplierProcurement.getTransportCost());
					supplierProcurement.setInvoiceValue(offlineSupplierProcurement.getInvoiceValue());
					supplierProcurement.setInvoiceNo(offlineSupplierProcurement.getInvoiceNo());
					supplierProcurement.setLatitude(offlineSupplierProcurement.getLatitude());
					supplierProcurement.setLongitude(offlineSupplierProcurement.getLongitude());
					supplierProcurement.setCreatedUser(agent.getProfileId());
					supplierProcurement.setCreatedDate(DateUtil.convertStringToDate(offlineSupplierProcurement.getProcurementDate(),
							DateUtil.TXN_DATE_TIME));
					supplierProcurement.setBranchId(offlineSupplierProcurement.getBranchId());
					supplierProcurement.setTaxAmt(offlineSupplierProcurement.getTaxAmt());
					supplierProcurement.setOtherCost(offlineSupplierProcurement.getOtherCost());
					
					AgroTransaction farmerProcureTxn = new AgroTransaction();
					
					try {
						/** FORMING AGRO TRANSACTION FOR FARMER PROCUREMENT **/
						farmerProcureTxn.setReceiptNo(offlineSupplierProcurement.getReceiptNo());
						farmerProcureTxn.setAgentId(agent.getProfileId());
						farmerProcureTxn.setAgentName((ObjectUtil.isEmpty(agent.getPersonalInfo()) ? ""
								: agent.getPersonalInfo().getAgentName()));
						farmerProcureTxn.setDeviceId(device.getCode());
						farmerProcureTxn.setDeviceName(device.getName());
						farmerProcureTxn.setServicePointId(servicePoint.getCode());
						farmerProcureTxn.setServicePointName(servicePoint.getName());
						farmerProcureTxn.setSeasonCode(offlineSupplierProcurement.getSeasonCode());						
						farmerProcureTxn.setBranch_id(offlineSupplierProcurement.getBranchId());
						farmerProcureTxn.setTxnTime(DateUtil
								.convertStringToDate(offlineSupplierProcurement.getProcurementDate(), DateUtil.TXN_DATE_TIME));
						
						
						farmerProcureTxn.setTxnAmount(txnAmount);
					
						farmerProcureTxn.setTxnType(TransactionTypeProperties.SUPPLIER_PROCUREMENT);
						farmerProcureTxn.setProfType(Profile.CLIENT);
						farmerProcureTxn.setOperType(ESETxn.ON_LINE);
						farmerProcureTxn.setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
						// Setting samithi reference
						//farmerProcureTxn.setSamithi(samithi);
					} catch (Exception e) {
						throw new OfflineTransactionException(ITxnErrorCodes.DATA_CONVERSION_ERROR);
					}

					Set<SupplierProcurementDetail> supplierProcurementDetails = new HashSet<SupplierProcurementDetail>();
					if (!ObjectUtil.isListEmpty(offlineSupplierProcurement.getOfflineSupplierProcurementDetails())) {

						/* FORMING PROCUREMENT DETAIL OBJECT * */
						for (OfflineSupplierProcurementDetail ofSupplierProcurementDetail : offlineSupplierProcurement
								.getOfflineSupplierProcurementDetails()) {
							SupplierProcurementDetail supplierProcurementDetail = new SupplierProcurementDetail();

							try {
								if (StringUtil.isEmpty(ofSupplierProcurementDetail.getQuality()))
									throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_PROCUREMENT_GRADE);
								ProcurementGrade procurementGrade = productDistributionService
										.findProcurementGradeByCode(ofSupplierProcurementDetail.getQuality(), tenantId);
								if (ObjectUtil.isEmpty(procurementGrade))
									throw new OfflineTransactionException(ITxnErrorCodes.QUALITY_DOES_NOT_EXIST);
							
							
																	
						if ("99".equalsIgnoreCase(!StringUtil.isEmpty(offlineSupplierProcurement.getProcMasterType()) ? offlineSupplierProcurement.getProcMasterType() : "99") 
						        || "11".equalsIgnoreCase(!StringUtil.isEmpty(offlineSupplierProcurement.getProcMasterType()) ? offlineSupplierProcurement.getProcMasterType() : "11")) {
							supplierProcurementDetail.setIsReg(!StringUtil.isEmpty(ofSupplierProcurementDetail.getIsRegFarmer()) ? ofSupplierProcurementDetail.getIsRegFarmer() : "");
							if(!StringUtil.isEmpty(ofSupplierProcurementDetail.getIsRegFarmer())
									&& ofSupplierProcurementDetail.getIsRegFarmer().equalsIgnoreCase("0")){
							
						     farmerList = farmerService.findFarmerByFarmerId(ofSupplierProcurementDetail.getFarmerId(), tenantId);
						    supplierProcurementDetail.setFarmer(!ObjectUtil.isEmpty(farmerList) ? farmerList : null);
						    supplierProcurementDetail.setVillageCode(!ObjectUtil.isEmpty(farmerList) && !ObjectUtil.isEmpty(farmerList.getVillage()) && 
			                 		!StringUtil.isEmpty(farmerList.getVillage().getCode()) ? farmerList.getVillage().getCode() : "");
						    supplierProcurementDetail.setFarmerName(!ObjectUtil.isEmpty(farmerList) ? farmerList.getFirstName() + " " + farmerList.getLastName() : "");
								}else{
									supplierProcurementDetail.setIsReg("1");
									supplierProcurementDetail.setFarmer(null);
									supplierProcurementDetail.setFarmerName(!StringUtil.isEmpty(ofSupplierProcurementDetail.getFarmerId()) ? ofSupplierProcurementDetail.getFarmerId() : "");
									supplierProcurementDetail.setFarmerMobileNumber(!StringUtil.isEmpty(offlineSupplierProcurement.getMobileNumber()) ? offlineSupplierProcurement.getMobileNumber() : "");
									supplierProcurementDetail.setVillageCode("");
								}
							supplierProcurement.setProcMasterTypeId("");
			            } else {
			            	supplierProcurementDetail.setIsReg("");
			            	supplierProcurementDetail.setFarmer(null);
			            	supplierProcurementDetail.setFarmerName("");
			            	supplierProcurementDetail.setFarmerMobileNumber("");
							supplierProcurementDetail.setVillageCode("");							
							supplierProcurement.setProcMasterTypeId(!StringUtil.isEmpty(offlineSupplierProcurement.getProcMasterTypeId()) ? offlineSupplierProcurement.getProcMasterTypeId() : "");
			                
			            }							
						supplierProcurementDetail.setProcurementGrade(procurementGrade);
						supplierProcurementDetail.setQuality(ofSupplierProcurementDetail.getQuality());
						supplierProcurementDetail.setProcurementProduct(
										ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? null
												: procurementGrade.getProcurementVariety().getProcurementProduct());
						supplierProcurementDetail.setNumberOfBags(Long.valueOf(ofSupplierProcurementDetail.getNumberOfBags()));
						supplierProcurementDetail.setGrossWeight(Double.valueOf(ofSupplierProcurementDetail.getGrossWeight()));								
						supplierProcurementDetail.setNetWeight(Double.valueOf(ofSupplierProcurementDetail.getNetWeight()));
						supplierProcurementDetail.setRatePerUnit(Double.valueOf(ofSupplierProcurementDetail.getRatePerUnit()));
						supplierProcurementDetail.setSubTotal(Double.valueOf(ofSupplierProcurementDetail.getSubTotal()));
						supplierProcurementDetail.setBatchNo(!ObjectUtil.isEmpty(ofSupplierProcurementDetail.getBatchNo())
										? ofSupplierProcurementDetail.getBatchNo() : "");
						supplierProcurementDetail.setUnit(!ObjectUtil.isEmpty(ofSupplierProcurementDetail.getUnit())
										? ofSupplierProcurementDetail.getUnit() : "");
						supplierProcurementDetail.setCropType(ofSupplierProcurementDetail.getCropType());
								if(!StringUtil.isEmpty(ofSupplierProcurementDetail.getIsRegFarmer()) && ofSupplierProcurementDetail.getIsRegFarmer().equalsIgnoreCase("0")){
									farmerProcureTxn.setFarmerId(!ObjectUtil.isEmpty(farmer) && !StringUtil.isEmpty(farmer.getFarmerId()) ? farmer.getFarmerId() : NOT_APPLICABLE);
									farmerProcureTxn
											.setFarmerName(!ObjectUtil.isEmpty(farmer) ? farmer.getFirstName() + " " + farmer.getLastName()
													: ofSupplierProcurementDetail.getFarmerId());
									}else{
										farmerProcureTxn.setFarmerId("NA");
										farmerProcureTxn
												.setFarmerName(!StringUtil.isEmpty(ofSupplierProcurementDetail.getFarmerId()) ? ofSupplierProcurementDetail.getFarmerId() : "NA");
									}
							
								if(tenantId.equalsIgnoreCase("gsma")){
								String[] strArray = new String[13];
								
								if(!StringUtil.isEmpty(ofSupplierProcurementDetail.getQuality().trim())){
									ProcurementGrade grade = productService.findProcurementGradeByCode(ofSupplierProcurementDetail.getQuality().trim());
									strArray[0] = grade.getName();
								}else{
									strArray[0] = "";
								}
								 
								strArray[1] = ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? "" : procurementGrade.getProcurementVariety().getName();
								strArray[2] = ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? "" : procurementGrade.getProcurementVariety().getProcurementProduct().getName();
								strArray[3] = ofSupplierProcurementDetail.getRatePerUnit();
								strArray[4] = ofSupplierProcurementDetail.getSubTotal();
								strArray[5] = DateUtil.convertDateToString(farmerProcureTxn.getTxnTime(), DateUtil.DATE_FORMAT_1);
								strArray[6]= ofSupplierProcurementDetail.getNetWeight();
								strArray[7]= procurementGrade.getProcurementVariety().getProcurementProduct().getUnit();
								strArray[8] = (ObjectUtil.isEmpty(agent.getPersonalInfo()) ? "" : agent.getPersonalInfo().getAgentName());
								strArray[9] = offlineSupplierProcurement.getBranchId();
								strArray[10] = farmerList.getMobileNumber();
								strArray[11] = String.valueOf(offlineSupplierProcurement.getId());
								strArray[12] = String.valueOf(ofSupplierProcurementDetail.getId());
								smsData.add(strArray);
							}
							} catch (Exception e) {
								throw new OfflineTransactionException(ITxnErrorCodes.DATA_CONVERSION_ERROR);
							}
						
							supplierProcurementDetail.setSupplierProcurement(supplierProcurement);
							supplierProcurementDetails.add(supplierProcurementDetail);
						} // inner loop end
					}
					try {
						supplierProcurement.setSupplierProcurementDetails(supplierProcurementDetails);
						supplierProcurement.setAgroTransaction(farmerProcureTxn);
						
						productDistributionService.addSupplierProcurement(supplierProcurement, tenantId);
						
						if(tenantId.equalsIgnoreCase("gsma")){
							smsData.stream().forEach(i -> {
								try{
									ESESystem system = preferencesService.findPrefernceById("1");
									SMSHistory sms = new SMSHistory();
									sms.setCreatedUser(i[8]);
									sms.setBranchId(i[9]);
									sms.setSmsRoute(system.getPreferences().get(ESESystem.SMS_ROUTE));
									sms.setReceiverMobNo(i[10]);
									sms.setSenderMobNo(i[10]);
									
									if (!ObjectUtil.isEmpty(system) && !ObjectUtil.isEmpty(system.getPreferences())) {
										String msg =  system.getPreferences().get(ESESystem.SMS_MESSAGE);
										
										 msg = msg.replaceAll("<grade>", i[0]);
										 msg = msg.replaceAll("<ProcurementProduct>", i[2]);
										 msg = msg.replaceAll("<ProcurementVariety>", i[1]);
										 msg = msg.replaceAll("<RatePerUnit>", i[3]);
										 msg = msg.replaceAll("<SubTotal>", i[4]);
										 msg = msg.replaceAll("<procurementDate>", i[5]);
										 msg = msg.replaceAll("<quantity>", i[6]);
										 msg = msg.replaceAll("<unit>", i[7]);
										 sms.setMessage(msg);
									}
									
									
									providerResponse = smsService.sendSMS(smsType, sms.getReceiverMobNo(),sms.getMessage());
									sms.setResponce(providerResponse);
									if (!StringUtil.isEmpty(providerResponse)) {
										JSONObject respObj = null;
										try {
											respObj = new JSONObject(providerResponse);
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if (!respObj.has(ISMSService.ERROR)) {
											
											//statusMsg = "Success";
											sms.setStatusMsg("Delivered");
											
											try {
												sms.setUuid(respObj.getString("batch_id"));
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											farmerService.save(sms);
										} else {
											//statusMsg = "ERROR";
											sms.setStatusMsg("Failed");
											farmerService.save(sms);
										}
										
									}
								}catch (Exception e) {
									System.out.println("Error occured in OfflineSupplierProcurementSchedulerTask --------> OfflineSupplierProcurementId =  "+i[11]+" And procurementDetailId = "+i[12]);
									System.out.println("sms will not sent for --> mobile no = "+i[10]+" | variety = "+i[1]+" | product = "+i[2]);
									smsData.remove(i);
									
								}
								
								
							});
							
							smsData.clear();

							
						}
						
						
						
						
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
				offlineSupplierProcurement.setStatusCode(statusCode);
				offlineSupplierProcurement.setStatusMsg(statusMsg);
				productDistributionService.updateOfflineSupplierProcurement(offlineSupplierProcurement, tenantId);
				
				
				
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
