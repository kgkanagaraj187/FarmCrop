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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.FarmCropCalendar;
import com.sourcetrace.eses.order.entity.txn.FarmCropCalendarDetail;
import com.sourcetrace.eses.order.entity.txn.OfflineFarmCropCalendar;
import com.sourcetrace.eses.order.entity.txn.OfflineFarmCropCalendarDetail;
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
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

@Component
public class OfflineFarmCropCalendarSchedulerTask {

	private static final Logger LOGGER = Logger.getLogger(OfflineFarmCropCalendarSchedulerTask.class.getName());
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
			List<OfflineFarmCropCalendar> offlineFarmCropCalendars = productDistributionService
					.listPendingOfflineFarmCropCalendarList(tenantId);
			for (OfflineFarmCropCalendar offlineFarmCropCalendar : offlineFarmCropCalendars) {
				int statusCode = ESETxnStatus.SUCCESS.ordinal();
				String statusMsg = ESETxnStatus.SUCCESS.toString();
				try {
					if (StringUtil.isEmpty(offlineFarmCropCalendar.getAgentId()))
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ID_EMPTY);

					Agent agent = agentService.findAgentByAgentId(offlineFarmCropCalendar.getAgentId(), tenantId,
							offlineFarmCropCalendar.getBranchId());
					if (ObjectUtil.isEmpty(agent))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_AGENT);

					if (StringUtil.isEmpty(offlineFarmCropCalendar.getDeviceId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);

					Device device = deviceService.findDeviceBySerialNumber(offlineFarmCropCalendar.getDeviceId(), tenantId);
					if (ObjectUtil.isEmpty(device))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DEVICE);
					
					ESEAccount agentAccount = accountService.findAccountByProfileId(agent.getProfileId(), tenantId);
					if (ObjectUtil.isEmpty(agentAccount))
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_UNAVAILABLE);

					if (agentAccount.getStatus() == ESEAccount.INACTIVE) {
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_INACTIVE);
					} else if (agentAccount.getStatus() == ESEAccount.BLOCKED) {
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ACCOUNT_BLOCKED);
					}

	
					/** FORMING PROCUREMENT OBJECT **/
					FarmCropCalendar farmCropCalendar = new FarmCropCalendar();
					if(!StringUtil.isEmpty(offlineFarmCropCalendar.getFarmCode())){
						Farm farm = farmerService.findFarmByCode(offlineFarmCropCalendar.getFarmCode(), tenantId);
						farmCropCalendar.setFarm(!ObjectUtil.isEmpty(farm) ? farm :null);
					}else{
						farmCropCalendar.setFarm(null);
					}
					if(!StringUtil.isEmpty(offlineFarmCropCalendar.getVarietyId())){
						ProcurementVariety variety = productService.findProcurementVarietyByCode(offlineFarmCropCalendar.getVarietyId(),tenantId);
						farmCropCalendar.setVariety(!ObjectUtil.isEmpty(variety) ? variety : null);
					}else{
						farmCropCalendar.setVariety(null);
					}
					farmCropCalendar.setSeasonCode(!StringUtil.isEmpty(offlineFarmCropCalendar.getSeasonCode()) ? offlineFarmCropCalendar.getSeasonCode() : "");
					farmCropCalendar.setAgentId(agent.getProfileId());
					farmCropCalendar.setCreatedDate(DateUtil.convertStringToDate(offlineFarmCropCalendar.getCreatedDate(), DateUtil.TXN_DATE_TIME));
					farmCropCalendar.setCreatedUser(offlineFarmCropCalendar.getCreatedUser());
					farmCropCalendar.setBranchId(offlineFarmCropCalendar.getBranchId());
					Set<FarmCropCalendarDetail> farmCropCalendarDetails = new HashSet<FarmCropCalendarDetail>();
					if (!ObjectUtil.isListEmpty(offlineFarmCropCalendar.getOfflineFarmCropCalendarDetail())) {

						/* FORMING PROCUREMENT DETAIL OBJECT * */
						for (OfflineFarmCropCalendarDetail ofFarmCropCalendarDetail : offlineFarmCropCalendar.getOfflineFarmCropCalendarDetail()) {
							FarmCropCalendarDetail farmCropCalendarDetail = new FarmCropCalendarDetail();
							
							try {
								farmCropCalendarDetail.setActivityMethod(!StringUtil.isEmpty(ofFarmCropCalendarDetail.getActivityMethod()) ? ofFarmCropCalendarDetail.getActivityMethod():"");
								farmCropCalendarDetail.setStatus(Integer.valueOf(ofFarmCropCalendarDetail.getStatus()));
								if(ofFarmCropCalendarDetail.getStatus().equalsIgnoreCase("0")){
									farmCropCalendarDetail.setDate(ofFarmCropCalendarDetail.getDate());
								}
								farmCropCalendarDetail.setRemarks(!StringUtil.isEmpty(ofFarmCropCalendarDetail.getRemarks()) ? ofFarmCropCalendarDetail.getRemarks():"");
								
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
							
							farmCropCalendarDetail.setFarmCropCalendar(farmCropCalendar);
							farmCropCalendarDetails.add(farmCropCalendarDetail);
						} // inner loop end
					}
					try {
						farmCropCalendar.setFarmCropCalendarDetail(farmCropCalendarDetails);
						
						
						productDistributionService.addFarmCropCalendar(farmCropCalendar, tenantId);
						
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
				offlineFarmCropCalendar.setStatusCode(statusCode);
				offlineFarmCropCalendar.setStatusMsg(statusMsg);
				productDistributionService.updateOfflineFarmCropCalendar(offlineFarmCropCalendar, tenantId);
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
