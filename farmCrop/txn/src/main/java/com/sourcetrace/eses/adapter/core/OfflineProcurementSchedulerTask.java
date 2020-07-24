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
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurement;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
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
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

@Component
public class OfflineProcurementSchedulerTask {

	private static final Logger LOGGER = Logger.getLogger(OfflineProcurementSchedulerTask.class.getName());
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
	private IPreferencesService preferencesService;
	@Autowired
	private ISMSService smsService;
	@Resource(name = "datasources")
	private Map<String, DataSource> datasources;
	private ArrayList<String[]> smsData = new ArrayList<String[]>(); 
	String providerResponse = null;
	int smsType = SMSHistory.SMS_SINGLE;
	String status = null;
	String descrption = null;
	private String enableLoanModule;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	public void process() {

		List<String> tenantIds = new ArrayList(datasources.keySet());

		for (String tenantId : tenantIds) {
			List<OfflineProcurement> offlineProcurements = productDistributionService
					.listPendingOfflineProcurementList(tenantId);
			for (OfflineProcurement offlineProcurement : offlineProcurements) {
				int statusCode = ESETxnStatus.SUCCESS.ordinal();
				String statusMsg = ESETxnStatus.SUCCESS.toString();
				try {
					if (StringUtil.isEmpty(offlineProcurement.getAgentId()))
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ID_EMPTY);

					Agent agent = agentService.findAgentByAgentId(offlineProcurement.getAgentId(), tenantId,
							offlineProcurement.getBranchId());
					if (ObjectUtil.isEmpty(agent))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_AGENT);

					if (StringUtil.isEmpty(offlineProcurement.getDeviceId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);

					Device device = deviceService.findDeviceBySerialNumber(offlineProcurement.getDeviceId(), tenantId);
					if (ObjectUtil.isEmpty(device))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DEVICE);

					if (StringUtil.isEmpty(offlineProcurement.getServicePointId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERV_POINT_ID);

					ServicePoint servicePoint = servicePointService
							.findServicePointByServPointId(offlineProcurement.getServicePointId(), tenantId);
					if (ObjectUtil.isEmpty(servicePoint))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_SERVICE_POINT);

					if (StringUtil.isEmpty(offlineProcurement.getReceiptNo())) {
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_RECEIPT_NO);
					}
					Procurement existing = productDistributionService.findProcurementByRecNoAndOperType(
							offlineProcurement.getReceiptNo(), ESETxn.ON_LINE, tenantId);
					if (!ObjectUtil.isEmpty(existing))
						throw new OfflineTransactionException(ITxnErrorCodes.PROCUREMENT_EXIST);

					/*
					 * if (StringUtil.isEmpty(offlineProcurement.getFarmerId()))
					 * throw new OfflineTransactionException(ITxnErrorCodes.
					 * EMPTY_FARMER_ID);
					 */

					if (StringUtil.isEmpty(offlineProcurement.getVillageCode()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_VILLAGE_CODE);

					Village procurementVillage = locationService.findVillageByCode(offlineProcurement.getVillageCode(),
							tenantId);
					if (ObjectUtil.isEmpty(procurementVillage))
						throw new OfflineTransactionException(ITxnErrorCodes.VILLAGE_NOT_EXIST);

					ESEAccount agentAccount = accountService.findAccountByProfileId(agent.getProfileId(), tenantId);
					if (ObjectUtil.isEmpty(agentAccount))
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_UNAVAILABLE);

					if (agentAccount.getStatus() == ESEAccount.INACTIVE) {
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_INACTIVE);
					} else if (agentAccount.getStatus() == ESEAccount.BLOCKED) {
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_BLOCKED);
					}

					// Agent account balance is checked
					/*
					 * if (!(agentAccount.getBalance() >=
					 * Double.valueOf(offlineProcurement .getTotalAmount()))) {
					 * throw new OfflineTransactionException(ITxnErrorCodes.
					 * INSUFFICIENT_BAL); }
					 */
					ESESystem system = preferencesService.findPrefernceById("1");
					setEnableLoanModule(system.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE));
					boolean isRegisteredFarmer = false;
					Warehouse samithi = null;
					Farmer farmer = null;
					ESEAccount farmerAccount = null;

					if (!StringUtil.isEmpty(offlineProcurement.getSamithiCode())) {

						samithi = locationService.findSamithiByCode(offlineProcurement.getSamithiCode(), tenantId);
						if (ObjectUtil.isEmpty(samithi))
							throw new OfflineTransactionException(ITxnErrorCodes.SAMITHI_NOT_EXIST);

						
						if(!StringUtil.isEmpty(offlineProcurement.getIsReg()) && offlineProcurement.getIsReg().equalsIgnoreCase("0")){
							
						
							if (!StringUtil.isEmpty(offlineProcurement.getFarmerId())) {
								farmer = farmerService.findFarmerByFarmerId(offlineProcurement.getFarmerId(), tenantId);
								if (ObjectUtil.isEmpty(farmer) || farmer == null)
									throw new OfflineTransactionException(ITxnErrorCodes.FARMER_NOT_EXIST);
	
								/** FETCHING FARMER ACCOUNT AND VALIDATE **/
								farmerAccount = farmerService.findAccountBySeassonProcurmentProductFarmer(0L,
										farmer.getId(), tenantId);
								if (ObjectUtil.isEmpty(farmerAccount))
									throw new OfflineTransactionException(ITxnErrorCodes.FARMER_ACCOUNT_UNAVAILABLE);
	
								if (ESEAccount.INACTIVE == farmerAccount.getStatus())
									throw new OfflineTransactionException(ITxnErrorCodes.FARMER_ACCOUNT_INACTIVE);
								isRegisteredFarmer = true;
							}
						}
					}
					// Parsing payment amount
					double paymentAmount = 0;

					if (!StringUtil.isEmpty(offlineProcurement.getPaymentAmt())) {
						try {
							paymentAmount = Double.valueOf(offlineProcurement.getPaymentAmt());
						} catch (Exception e) {
							LOGGER.info("Could not Parse paymentAount");
							e.printStackTrace();
						}
					}

					// Parsing total amount
					double txnAmount = 0;

					try {
						txnAmount = Double.valueOf(Double.valueOf(offlineProcurement.getTotalAmount()));
					} catch (Exception e) {
						LOGGER.info(e.getMessage());
						e.printStackTrace();
					}

					// Reducing payment amount with txn amount
					/*
					 * if(paymentAmount>0){ txnAmount-=paymentAmount; }
					 */
					
					String temp =  system.getPreferences().get(ESESystem.ENABLE_SMS_IN_PROCUREMENT_MODULE);
					/** FORMING PROCUREMENT OBJECT **/
					Procurement procurement = new Procurement();

					procurement.setTotalLabourCost(offlineProcurement.getTotalLabourCost());
					procurement.setTransportCost(offlineProcurement.getTransportCost());
					procurement.setVehicleType(offlineProcurement.getVehicleType());
					procurement.setInvoiceValue(offlineProcurement.getInvoiceValue());
					procurement.setCropType(offlineProcurement.getCropType());
					if (!StringUtil.isEmpty(offlineProcurement.getWarehouseId())) {
						Warehouse warehouse = locationService.findCoOperativeByCode(offlineProcurement.getWarehouseId(),tenantId);
						if (!ObjectUtil.isEmpty(warehouse))
							procurement.setWarehouseId(String.valueOf(warehouse.getId()));
					}

					// Added payment amount in to po number column because
					// unused column
					// procurement.setPoNumber(String.valueOf(paymentAmount));
					procurement.setPaymentAmount(paymentAmount);
					procurement.setPaymentMode(offlineProcurement.getPaymentMode());
					procurement.setType(!StringUtil.isEmpty(offlineProcurement.getIsReg())?Integer.parseInt(offlineProcurement.getIsReg()):0);
					// sets mobile number for unregistered farmer
					if (!StringUtil.isEmpty(offlineProcurement.getMobileNumber()))
						procurement.setMobileNumber(offlineProcurement.getMobileNumber());

					procurement.setSeason(productDistributionService.findCurrentSeason(tenantId));
					procurement.setVillage((isRegisteredFarmer) ? farmer.getVillage() : procurementVillage);

					procurement.setProcMasterType(offlineProcurement.getProcMasterType());
					/*procurement.setProcMasterTypeId(StringUtil.isEmpty(offlineProcurement.getProcMasterTypeId()) ? ""
							: offlineProcurement.getProcMasterTypeId());

					if (!"99".equalsIgnoreCase(offlineProcurement.getProcMasterType())) {
						procurement.setProcMasterTypeId(offlineProcurement.getProcMasterTypeId());
					}*/
					if ("99".equalsIgnoreCase(!StringUtil.isEmpty(offlineProcurement.getProcMasterType()) ? offlineProcurement.getProcMasterType() : "99") 
					        || "11".equalsIgnoreCase(!StringUtil.isEmpty(offlineProcurement.getProcMasterType()) ? offlineProcurement.getProcMasterType() : "11")
					        ) {
						if(!StringUtil.isEmpty(offlineProcurement.getIsReg()) && offlineProcurement.getIsReg().equalsIgnoreCase("0")){
						
					   // Farmer farmerList = farmerService.findFarmerByFarmerId(offlineProcurement.getFarmerId(), tenantId);
	                    procurement.setFarmer(!ObjectUtil.isEmpty(farmer) ? farmer : null);
	                    procurement.setProcMasterTypeId(!ObjectUtil.isEmpty(farmer) && !ObjectUtil.isEmpty(farmer.getSamithi())
	                            && !StringUtil.isEmpty(farmer.getSamithi().getCode()) ? farmer.getSamithi().getCode() : "");
							}else{
								procurement.setMobileNumber(!StringUtil.isEmpty(offlineProcurement.getMobileNumber()) ? offlineProcurement.getMobileNumber() : "");
							}
		            } else {
		              MasterData master = farmerService.findMasterDataIdByCode(offlineProcurement.getProcMasterTypeId(),tenantId);
		                procurement.setProcMasterTypeId(!ObjectUtil.isEmpty(master) ? String.valueOf(master.getId()) : "");
		                
		            }
					procurement.setTotalProVal(Double.valueOf(offlineProcurement.getTotalAmount()));

					procurement.setSeasonCode(offlineProcurement.getSeasonCode());

					procurement.setInvoiceNo(offlineProcurement.getInvoiceNo());
					procurement.setBuyer(offlineProcurement.getBuyer());

					//Farmer farmerList = farmerService.findFarmerByFarmerId(offlineProcurement.getFarmerId(), tenantId);
					  procurement.setFarmer(!ObjectUtil.isEmpty(farmer) ? farmer : null);
					// if(tenantId.equalsIgnoreCase("AWI")){
					procurement.setRoadMapCode(offlineProcurement.getRoadMapCode());
					procurement.setVehicleNum(offlineProcurement.getVehicleNo());
					procurement.setFarmerAttnce(offlineProcurement.getFarmerAttnce());
					if (offlineProcurement.getFarmerAttnce().equals("1")) {
						procurement.setSubstituteName(offlineProcurement.getSubstituteName());
					}
					// procurement.setFarmId(offlineProcurement.getFarmId());
					procurement.setFarmId(offlineProcurement.getFarmId());
					Farm farm = farmerService.findFarmByCode(offlineProcurement.getFarmId(),tenantId);
					if (!ObjectUtil.isEmpty(farm)) {
						procurement.setFarmer(farm.getFarmer());
					}
					procurement.setLatitude(offlineProcurement.getLatitude());
					procurement.setLongitude(offlineProcurement.getLongitude());
					procurement.setCreatedUser(agent.getProfileId());
					procurement.setCreatedDate(DateUtil.convertStringToDate(offlineProcurement.getProcurementDate(),
							DateUtil.TXN_DATE_TIME));
					procurement.setBranchId(offlineProcurement.getBranchId());
					procurement.setStatus(0);
					if(!StringUtil.isEmpty(getEnableLoanModule()) && getEnableLoanModule().equalsIgnoreCase("1")){
					procurement.setFinalPayAmt(Double.valueOf(offlineProcurement.getTotalAmount())- Double.valueOf(offlineProcurement.getLoanRepaymentAmt()));
					procurement.setLoanInterest(offlineProcurement.getLoanInterest());
					procurement.setLoanAmount(offlineProcurement.getLoanRepaymentAmt());
					}
					// }
					// AgroTransaction agentTransaction = new AgroTransaction();
					AgroTransaction farmerProcureTxn = new AgroTransaction();
					// agentTransaction.setRefAgroTransaction(farmerProcureTxn);
					try {
						/** FORMING AGRO TRANSACTION FOR FARMER PROCUREMENT **/
						farmerProcureTxn.setReceiptNo(offlineProcurement.getReceiptNo());
						farmerProcureTxn.setAgentId(agent.getProfileId());
						farmerProcureTxn.setAgentName((ObjectUtil.isEmpty(agent.getPersonalInfo()) ? ""
								: agent.getPersonalInfo().getAgentName()));
						farmerProcureTxn.setDeviceId(device.getCode());
						farmerProcureTxn.setDeviceName(device.getName());
						farmerProcureTxn.setServicePointId(servicePoint.getCode());
						farmerProcureTxn.setServicePointName(servicePoint.getName());
						farmerProcureTxn.setSeasonCode(offlineProcurement.getSeasonCode());
						if(!StringUtil.isEmpty(offlineProcurement.getIsReg()) && offlineProcurement.getIsReg().equalsIgnoreCase("0")){
						farmerProcureTxn.setFarmerId(!ObjectUtil.isEmpty(farmer) && !StringUtil.isEmpty(farmer.getFarmerId()) ? farmer.getFarmerId() : NOT_APPLICABLE);
						farmerProcureTxn
								.setFarmerName(!ObjectUtil.isEmpty(farmer) ? farmer.getFirstName() + " " + farmer.getLastName() : NOT_APPLICABLE);
						}else{
							farmerProcureTxn.setFarmerId("NA");
							farmerProcureTxn
									.setFarmerName(!StringUtil.isEmpty(offlineProcurement.getFarmerId()) ? offlineProcurement.getFarmerId() : "NA");
						}
						farmerProcureTxn.setBranch_id(offlineProcurement.getBranchId());

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

						farmerProcureTxn.setTxnTime(DateUtil
								.convertStringToDate(offlineProcurement.getProcurementDate(), DateUtil.TXN_DATE_TIME));
						farmerProcureTxn.setTxnAmount(txnAmount);
						// Farmer account should be credited for procurement
						// because direct payment will
						// be made
						// farmerProcureTxn.setBalAmount(farmerProcureTxn.getIntBalance()
						// +
						// farmerProcureTxn.getTxnAmount());
						farmerProcureTxn.setTxnType(TransactionTypeProperties.PROCUREMENT_PRODUCT_ENROLLMENT);
						farmerProcureTxn.setProfType(Profile.CLIENT);
						farmerProcureTxn.setOperType(ESETxn.ON_LINE);
						farmerProcureTxn.setTxnDesc(Procurement.PROCUREMENT_AMOUNT);
						// Setting samithi reference
						farmerProcureTxn.setSamithi(samithi);
					} catch (Exception e) {
						throw new OfflineTransactionException(ITxnErrorCodes.DATA_CONVERSION_ERROR);
					}

					Set<ProcurementDetail> procurementDetails = new HashSet<ProcurementDetail>();
					if (!ObjectUtil.isListEmpty(offlineProcurement.getOfflineProcurementDetails())) {

						/* FORMING PROCUREMENT DETAIL OBJECT * */
						for (OfflineProcurementDetail ofProcurementDetail : offlineProcurement
								.getOfflineProcurementDetails()) {
							ProcurementDetail procurementDetail = new ProcurementDetail();

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
								if (StringUtil.isEmpty(ofProcurementDetail.getQuality()))
									throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_PROCUREMENT_GRADE);
								ProcurementGrade procurementGrade = productDistributionService
										.findProcurementGradeByCode(ofProcurementDetail.getQuality(), tenantId);
								if (ObjectUtil.isEmpty(procurementGrade))
									throw new OfflineTransactionException(ITxnErrorCodes.QUALITY_DOES_NOT_EXIST);

								procurementDetail.setProcurementGrade(procurementGrade);
								procurementDetail.setQuality(ofProcurementDetail.getQuality());
								procurementDetail.setProcurementProduct(
										ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? null
												: procurementGrade.getProcurementVariety().getProcurementProduct());
								procurementDetail.setNumberOfBags(Long.valueOf(ofProcurementDetail.getNumberOfBags()));
								procurementDetail.setNumberOfFruitBags(ofProcurementDetail.getNumberOfFruitBags()!=null && !StringUtil.isEmpty(ofProcurementDetail.getNumberOfFruitBags())?ofProcurementDetail.getNumberOfFruitBags():"0");
								procurementDetail.setGrossWeight(Double.valueOf(ofProcurementDetail.getGrossWeight()));
								procurementDetail.setTareWeight(Double.valueOf(ofProcurementDetail.getTareWeight()));
								procurementDetail.setNetWeight(Double.valueOf(ofProcurementDetail.getNetWeight()));
								procurementDetail.setRatePerUnit(Double.valueOf(ofProcurementDetail.getRatePerUnit()));
								procurementDetail.setSubTotal(Double.valueOf(ofProcurementDetail.getSubTotal()));
								procurementDetail.setBatchNo(!ObjectUtil.isEmpty(ofProcurementDetail.getBatchNo())
										? ofProcurementDetail.getBatchNo() : "");
								procurementDetail.setUnit(!ObjectUtil.isEmpty(ofProcurementDetail.getUnit())
										? ofProcurementDetail.getUnit() : "");
								if(!StringUtil.isEmpty(temp)&& temp.equalsIgnoreCase("1")){
								String[] strArray = new String[13];
								
								if(!StringUtil.isEmpty(ofProcurementDetail.getQuality().trim())){
									strArray[0] = procurementGrade.getName();
								}else{
									strArray[0] = "";
								}								 
								strArray[1] = ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? "" : procurementGrade.getProcurementVariety().getName();
								strArray[2] = ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? "" : procurementGrade.getProcurementVariety().getProcurementProduct().getName();
								strArray[3] = ofProcurementDetail.getRatePerUnit();
								strArray[4] = ofProcurementDetail.getSubTotal();
								strArray[5] = DateUtil.convertDateToString(farmerProcureTxn.getTxnTime(), DateUtil.DATE_FORMAT_1);
								strArray[6]= ofProcurementDetail.getNetWeight();
								strArray[7]= procurementGrade.getProcurementVariety().getProcurementProduct().getUnit();
								strArray[8] = (ObjectUtil.isEmpty(agent.getPersonalInfo()) ? "" : agent.getPersonalInfo().getAgentName());
								strArray[9] = offlineProcurement.getBranchId();
								strArray[10] = (!StringUtil.isEmpty(ofProcurementDetail.getOfflineProcurement().getIsReg()) && ofProcurementDetail.getOfflineProcurement().getIsReg().equalsIgnoreCase("0"))?farmer.getMobileNumber():offlineProcurement.getMobileNumber();
								strArray[11] = String.valueOf(ofProcurementDetail.getId());
								strArray[12] = String.valueOf(ofProcurementDetail.getId());
								smsData.add(strArray);
								}
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
							procurementDetail.setProcurement(procurement);
							procurementDetails.add(procurementDetail);
						} // inner loop end
					}
					try {
						procurement.setProcurementDetails(procurementDetails);
						/*
						 * if (isRegisteredFarmer &&
						 * !ObjectUtil.isEmpty(farmerAccount)) {
						 * farmerAccount.setBalance(farmerProcureTxn.
						 * getBalAmount());
						 * farmerProcureTxn.setAccount(farmerAccount); }
						 */
						// procurement.setAgroTransaction(agentTransac\tion);
						procurement.setAgroTransaction(farmerProcureTxn);
						if(tenantId.equalsIgnoreCase(ESESystem.AWI_TENANT_ID)){
							productDistributionService.addUnapprovedProcurement(procurement,tenantId);
						}else{
							productDistributionService.addProcurement(procurement, tenantId);	
						}
						if(!StringUtil.isEmpty(temp)&& temp.equalsIgnoreCase("1")){
						smsData.stream().forEach(i -> {
							try{
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
				offlineProcurement.setStatusCode(statusCode);
				offlineProcurement.setStatusMsg(statusMsg);
				productDistributionService.updateOfflineProcurement(offlineProcurement, tenantId);
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

	public IPreferencesService getPreferencesService() {
		return preferencesService;
	}

	public void setPreferencesService(IPreferencesService preferencesService) {
		this.preferencesService = preferencesService;
	}

	public String getEnableLoanModule() {
		return enableLoanModule;
	}

	public void setEnableLoanModule(String enableLoanModule) {
		this.enableLoanModule = enableLoanModule;
	}
	
	
}
