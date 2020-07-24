/*
 * OfflineMTNSchedulerTask.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.MTNT;
import com.sourcetrace.eses.order.entity.txn.OfflineMTNT;
import com.sourcetrace.eses.order.entity.txn.OfflineMTNTDetail;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

@Component
public class OfflineMTNSchedulerTask{
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IServicePointService servicePointService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private ILocationService locationService;
	@Resource(name = "datasources")
	private Map<String, DataSource> datasources;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	public void process() {

		Vector<String> tenantIds = new Vector(datasources.keySet());

		for (String tenantId : tenantIds) {
			List<OfflineMTNT> OfflineMTNTList = Collections.synchronizedList(this.productDistributionService.listPendingOfflineMTNTAndMTNR(tenantId));
			for (OfflineMTNT offlineMTNT : OfflineMTNTList) {
				int statusCode = ESETxnStatus.SUCCESS.ordinal();
				String statusMsg = ESETxnStatus.SUCCESS.toString();
				try {
					// Receipt No
					if (StringUtil.isEmpty(offlineMTNT.getReceiptNo()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_RECEIPT_NO);

					if (!StringUtil.isEmpty(offlineMTNT.getReceiptNo())) {
						MTNT existing = productDistributionService
								.findMTNByReceiptNoTypeAndOperType(offlineMTNT.getReceiptNo(), offlineMTNT.getType(),tenantId);
						if (!ObjectUtil.isEmpty(existing))
							throw new OfflineTransactionException(ITxnErrorCodes.MTNT_EXIST);

					}
					// Agent
					if (StringUtil.isEmpty(offlineMTNT.getAgentId()))
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ID_EMPTY);
					Agent agent = agentService.findAgentByAgentId(offlineMTNT.getAgentId(),tenantId,offlineMTNT.getBranchId());
					if (ObjectUtil.isEmpty(agent))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_AGENT);
					// Service Point
					if (StringUtil.isEmpty(offlineMTNT.getServicePointId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERV_POINT_ID);
					ServicePoint servicePoint = servicePointService
							.findServicePointByServPointId(offlineMTNT.getServicePointId(),tenantId);
					if (ObjectUtil.isEmpty(servicePoint))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_SERVICE_POINT);

					// Device
					if (StringUtil.isEmpty(offlineMTNT.getDeviceId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);
					Device device = deviceService.findDeviceBySerialNumber(offlineMTNT.getDeviceId(),tenantId);
					if (ObjectUtil.isEmpty(device))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DEVICE);

					// Warehouse
					if (StringUtil.isEmpty(offlineMTNT.getWarehouseCode()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_WAREHOUSE_CODE);
					Warehouse coOperative = locationService.findCoOperativeByCode(offlineMTNT.getWarehouseCode(),tenantId);
					if (ObjectUtil.isEmpty(coOperative)) {
						throw new OfflineTransactionException(ITxnErrorCodes.WAREHOUSE_DOES_NOT_EXIST);
					}

					// Truck Id
					if (StringUtil.isEmpty(offlineMTNT.getTruckId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_TRUCK_ID);
					// Driver Id
					if (StringUtil.isEmpty(offlineMTNT.getDriverId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_DRIVER_ID);

					// Forming PMT
					PMT mtnt = new PMT();
					mtnt.setMtntReceiptNumber(offlineMTNT.getReceiptNo());
					mtnt.setAgentRef(agent);
					mtnt.setCoOperative(coOperative);
					mtnt.setTruckId(offlineMTNT.getTruckId());
					mtnt.setDriverName(offlineMTNT.getDriverId());
					mtnt.setSeasonCode(offlineMTNT.getSeasonCode());
					mtnt.setBranchId(offlineMTNT.getBranchId());
					mtnt.setTotalLabourCost(offlineMTNT.getTotalLabourCost());
					mtnt.setTransportCost(offlineMTNT.getTransportCost());
					mtnt.setClient(offlineMTNT.getClient());
                    mtnt.setTotalAmt(offlineMTNT.getTotalAmt());
                    mtnt.setInvoiceNo(offlineMTNT.getInvoiceNo());
                    mtnt.setLatitude(offlineMTNT.getLatitude());
                    mtnt.setLongitude(offlineMTNT.getLongitude());
					// MTNT Date
					try {
						mtnt.setMtntDate(
								DateUtil.convertStringToDate(offlineMTNT.getMtntDate(), DateUtil.TXN_DATE_TIME));
					} catch (Exception e) {
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DATE_FORMAT);

					}
					// Forming Transfer Info
					TransferInfo transferInfo = new TransferInfo();
					transferInfo.setAgentId(agent.getProfileId());
					transferInfo.setAgentName(
							!ObjectUtil.isEmpty(agent.getPersonalInfo()) ? agent.getPersonalInfo().getName() : "");
					transferInfo.setDeviceId(device.getCode());
					transferInfo.setDeviceName(device.getName());
					transferInfo.setServicePointId(servicePoint.getCode());
					transferInfo.setServicePointName(servicePoint.getName());
					transferInfo.setOperationType(ESETxn.ON_LINE);
					mtnt.setMtntTransferInfo(transferInfo);
					Map<String, String> villageMap = locationService.getVillageMappedWithAgent(agent.getWareHouses());
					// Forming PMT Details
					Set<PMTDetail> mtntDetails = new HashSet<PMTDetail>();

					// ProcurementProduct product = null;
					// GradeMaster gradeMaster = null;
					Set<OfflineMTNTDetail> synchSet =  Collections.synchronizedSet(offlineMTNT.getOfflineMTNTDetails());
					for (OfflineMTNTDetail offlineMTNTDetail : synchSet) {
						PMTDetail mtntDetail = new PMTDetail();
						// Product
						/*
						 * if
						 * (StringUtil.isEmpty(offlineMTNTDetail.getProductCode(
						 * ))) throw new
						 * OfflineTransactionException(ITxnErrorCodes.
						 * EMPTY_PRODUCT_CODE); product =
						 * productDistributionService.
						 * findProcurementProductByCode(offlineMTNTDetail.
						 * getProductCode()); if (ObjectUtil.isEmpty(product)) {
						 * throw new OfflineTransactionException(ITxnErrorCodes.
						 * PRODUCT_DOES_NOT_EXIST); }
						 * mtntDetail.setProcurementProduct(product);
						 */

						// Village
						if(!tenantId.equalsIgnoreCase("livelihood")&&!tenantId.equalsIgnoreCase("kpf")&& !tenantId.equalsIgnoreCase("wub") && !tenantId.equalsIgnoreCase("gar") &&!tenantId.equalsIgnoreCase("cabi")){
						if (StringUtil.isEmpty(offlineMTNTDetail.getVillageCode()))
							throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_VILLAGE_CODE);
						Village village = locationService.findVillageByCode(offlineMTNTDetail.getVillageCode(),tenantId);
						if (ObjectUtil.isEmpty(village))
							throw new OfflineTransactionException(ITxnErrorCodes.VILLAGE_NOT_EXIST);
						// Validation to check whether village comes under agent
						// samithi
						/*if (!villageMap.containsKey(offlineMTNTDetail.getVillageCode()))
							throw new OfflineTransactionException(ITxnErrorCodes.VILLAGE_NOT_MAPPED_WITH_SAMITHI);*/

						mtntDetail.setVillage(village);
						}else{
							mtntDetail.setVillage(null);
						}
						// No Of Bags
						try {
							mtntDetail.setMtntNumberOfBags(Long.parseLong(offlineMTNTDetail.getNumberOfBags()));
							mtntDetail.setMtnrNumberOfBags(Long.parseLong(offlineMTNTDetail.getNumberOfBags()));
						} catch (Exception e) {
							throw new OfflineTransactionException(ITxnErrorCodes.INVALID_NO_OF_BAGS);
						}

						// Gross Weight
						try {
							mtntDetail.setMtntGrossWeight(Double.parseDouble(offlineMTNTDetail.getGrossWeight()));
							mtntDetail.setMtnrGrossWeight(Double.parseDouble(offlineMTNTDetail.getGrossWeight()));
						} catch (Exception e) {
							throw new OfflineTransactionException(ITxnErrorCodes.INVALID_GROSS_WEIGHT);
						}

						// Grade
						if (StringUtil.isEmpty(offlineMTNTDetail.getQuality()))
							throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_GRADE_CODE);
						ProcurementGrade procurementGrade = productDistributionService
								.findProcurementGradeByCode(offlineMTNTDetail.getQuality(),tenantId);
						if (ObjectUtil.isEmpty(procurementGrade))
							throw new OfflineTransactionException(ITxnErrorCodes.GRADE_DOES_NOT_EXIST);
						mtntDetail.setProcurementGrade(procurementGrade);
						mtntDetail.setProcurementProduct(
								ObjectUtil.isEmpty(procurementGrade.getProcurementVariety()) ? null
										: ObjectUtil.isEmpty(
												procurementGrade.getProcurementVariety().getProcurementProduct()) ? null
														: procurementGrade.getProcurementVariety()
																.getProcurementProduct());

						/*
						 * if(!ObjectUtil.isEmpty(product) &&
						 * !ObjectUtil.isEmpty(gradeMaster) && (product.getId()
						 * != gradeMaster.getProduct().getId())) throw new
						 * OfflineTransactionException(ITxnErrorCodes.
						 * PRODUCT_VARIETY_MISMATCH);
						 */
						mtntDetail.setPricePerUnit(offlineMTNTDetail.getPricePerUnit());
						mtntDetail.setSubTotal(offlineMTNTDetail.getSubTotal());
						mtntDetail.setUom(offlineMTNTDetail.getUom());
						mtntDetail.setCoOperative(coOperative);
						mtntDetail.setPmt(mtnt);
						mtntDetails.add(mtntDetail);

					}
					mtnt.setStatusCode(PMT.Status.MTNT.ordinal());
					mtnt.setPmtDetails(mtntDetails);
					mtnt.setTrnType(PMT.TRN_TYPE_OTEHR);
					try {
						// Saving PMT and PMT Details
						productDistributionService.addProcurementMTNT(mtnt,tenantId);
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
				offlineMTNT.setStatusCode(statusCode);
				offlineMTNT.setStatusMessage(statusMsg);
				productDistributionService.editOfflineMTNT(offlineMTNT,tenantId);
			} // end for loop

		}
		
	}

}
