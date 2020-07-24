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
import com.sourcetrace.eses.order.entity.txn.OfflinePMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTNR;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTNRDetail;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
@Component
public class OfflinePMTNRSchedulerTask {
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IDeviceService deviceService;

	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IProductService productService;
	@Resource(name = "datasources")
	private Map<String, DataSource> datasources;

	public void process() {

		Vector<String> tenantIds = new Vector(datasources.keySet());

		for (String tenantId : tenantIds) {
			List<OfflinePMTNR> OfflineMTNTList = Collections
					.synchronizedList(this.productDistributionService.listPendingOfflineMTNR(tenantId));
			for (OfflinePMTNR offlinePMTNR : OfflineMTNTList) {
				int statusCode = ESETxnStatus.SUCCESS.ordinal();
				String statusMsg = ESETxnStatus.SUCCESS.toString();

				try {
					// Receipt No
					if (StringUtil.isEmpty(offlinePMTNR.getReceiptNo()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_RECEIPT_NO);

					if (!StringUtil.isEmpty(offlinePMTNR.getReceiptNo())) {
						PMT existing = productDistributionService.findPMTNRByReceiptNoAndType(
								offlinePMTNR.getReceiptNo(), offlinePMTNR.getTrnType(), tenantId);
						if (!ObjectUtil.isEmpty(existing))
							throw new OfflineTransactionException(ITxnErrorCodes.MTNT_EXIST);

					}
					// Agent
					if (StringUtil.isEmpty(offlinePMTNR.getAgentId()))
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ID_EMPTY);
					Agent agent = agentService.findAgentByAgentId(offlinePMTNR.getAgentId(), tenantId,
							offlinePMTNR.getBranchId());
					if (ObjectUtil.isEmpty(agent))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_AGENT);

					// Device
					if (StringUtil.isEmpty(offlinePMTNR.getDeviceId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);
					Device device = deviceService.findDeviceBySerialNumber(offlinePMTNR.getDeviceId(), tenantId);
					if (ObjectUtil.isEmpty(device))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DEVICE);

					// Warehouse
					if (StringUtil.isEmpty(offlinePMTNR.getCoOperativeCode()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_WAREHOUSE_CODE);
					Warehouse coOperative = locationService.findCoOperativeByCode(offlinePMTNR.getCoOperativeCode(),
							tenantId);
					if (ObjectUtil.isEmpty(coOperative)) {
						throw new OfflineTransactionException(ITxnErrorCodes.WAREHOUSE_DOES_NOT_EXIST);
					}

					/*// Truck Id
					if (StringUtil.isEmpty(offlinePMTNR.getTruckId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_TRUCK_ID);
					// Driver Id
					if (StringUtil.isEmpty(offlinePMTNR.getDriverName()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_DRIVER_ID);
*/
					// Forming PMT
					PMT pmtnr = new PMT();
					pmtnr.setMtnrReceiptNumber(offlinePMTNR.getReceiptNo());
					pmtnr.setAgentRef(agent);
					pmtnr.setCoOperative(coOperative);
					pmtnr.setTruckId(offlinePMTNR.getTruckId());
					pmtnr.setMtntReceiptNumber(offlinePMTNR.getMtntReceiptNo());
					pmtnr.setDriverName(offlinePMTNR.getDriverName());
					pmtnr.setSeasonCode(offlinePMTNR.getSeasonCode());
					pmtnr.setBranchId(offlinePMTNR.getBranchId());
					pmtnr.setTransporter(offlinePMTNR.getTransporter());
					PMT vals=productDistributionService.findDriverAndTransporterByReceiptNo(offlinePMTNR.getMtntReceiptNo(),tenantId);

					// MTNT Date
					try {
						pmtnr.setMtnrDate(
								DateUtil.convertStringToDate(offlinePMTNR.getMtnrDate(), DateUtil.TXN_DATE_TIME));
						pmtnr.setMtntDate(DateUtil.convertStringToDate(offlinePMTNR.getMtnrDate(), DateUtil.TXN_DATE_TIME));
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
					transferInfo.setServicePointId(
							(ObjectUtil.isEmpty(agent.getServicePoint()) ? "" : agent.getServicePoint().getCode()));
					transferInfo.setServicePointName(
							(ObjectUtil.isEmpty(agent.getServicePoint()) ? "" : agent.getServicePoint().getName()));
					transferInfo.setOperationType(ESETxn.ON_LINE);
					pmtnr.setMtnrTransferInfo(transferInfo);
					// Forming PMT Details
					Set<PMTDetail> pmtnrDetailsSet = new HashSet<PMTDetail>();

					// ProcurementProduct product = null;
					// GradeMaster gradeMaster = null;
					Set<OfflinePMTNRDetail> synchSet = Collections.synchronizedSet(offlinePMTNR.getOfflinePMTRDetail());
					if (!ObjectUtil.isEmpty(synchSet) && synchSet.size() > 0) {
						for (OfflinePMTNRDetail offlinePMTNRDetail : synchSet) {
							PMTDetail pmtnrDetail = new PMTDetail();

							// No Of Bags
							try {
								pmtnrDetail.setMtnrNumberOfBags(Long.parseLong(offlinePMTNRDetail.getNoOfBags()));
								pmtnrDetail.setMtntNumberOfBags(Long.parseLong(offlinePMTNRDetail.getTransferBags()));
								pmtnrDetail.setMtntQuintalWeight(Double.parseDouble(offlinePMTNRDetail.getTransferWeight()));
							} catch (Exception e) {
								throw new OfflineTransactionException(ITxnErrorCodes.INVALID_NO_OF_BAGS);
							}

							// Gross Weight
							try {
								pmtnrDetail.setMtnrGrossWeight(Double.parseDouble(offlinePMTNRDetail.getGrossWeight()));
							} catch (Exception e) {
								throw new OfflineTransactionException(ITxnErrorCodes.INVALID_GROSS_WEIGHT);
							}

							ProcurementProduct pr = productService.findProcurementProductByCode(offlinePMTNRDetail.getProductCode(),tenantId);
							
							if (ObjectUtil.isEmpty(pr))
								throw new OfflineTransactionException(ITxnErrorCodes.PRODUCT_DOES_NOT_EXIST);
							
							pmtnrDetail.setProcurementProduct(pr);
							Warehouse ginning = locationService.findCoOperativeByCode(offlinePMTNRDetail.getCoOperativeCode(),
									tenantId);
							pmtnrDetail.setCoOperative(ginning);
							pmtnrDetail.setStatus(PMT.Status.COMPLETE.ordinal());
							pmtnrDetail.setIcs(offlinePMTNRDetail.getIcs());
							pmtnrDetail.setHeap(offlinePMTNRDetail.getHeap());
							pmtnrDetail.setPmt(pmtnr);
							if(vals!=null && !ObjectUtil.isEmpty(vals) && vals.getPmtFarmerDetais()!=null && !ObjectUtil.isListEmpty(vals.getPmtFarmerDetais())) {
				            	vals.getPmtFarmerDetais().stream().filter(fi-> fi.getFarmer()!=null && !StringUtil.isEmpty(fi.getFarmer())).forEach(fr-> {
				            		pmtnrDetail.setFarmerId(pmtnr.getFarmerId()!=null && !StringUtil.isEmpty(pmtnr.getFarmerId())?!pmtnr.getFarmerId().contains(fr.getFarmer())?pmtnr.getFarmerId()+fr.getFarmer()+"~"+fr.getIcs()+"~"+fr.getProcurementProduct().getId()+",":pmtnr.getFarmerId():fr.getFarmer()+"~"+fr.getIcs()+"~"+fr.getProcurementProduct().getId()+",");	
				            	});
				            	pmtnrDetail.setFarmerId(StringUtil.removeLastComma(pmtnrDetail.getFarmerId()));
				            	
				            }
							pmtnrDetailsSet.add(pmtnrDetail);

						}
						
						if(vals!=null && !ObjectUtil.isEmpty(vals) && vals.getPmtFarmerDetais()!=null && !ObjectUtil.isListEmpty(vals.getPmtFarmerDetais())) {
				            	vals.getPmtFarmerDetais().stream().filter(fi-> fi.getFarmer()!=null && !StringUtil.isEmpty(fi.getFarmer())).forEach(fr-> {
				            		pmtnr.setFarmerId(pmtnr.getFarmerId()!=null && !StringUtil.isEmpty(pmtnr.getFarmerId())?!pmtnr.getFarmerId().contains(fr.getFarmer())?pmtnr.getFarmerId()+fr.getFarmer()+"~"+fr.getIcs()+"~"+fr.getProcurementProduct().getId()+",":pmtnr.getFarmerId():fr.getFarmer()+"~"+fr.getIcs()+"~"+fr.getProcurementProduct().getId()+",");	
				            	});
				            	pmtnr.setFarmerId(StringUtil.removeLastComma(pmtnr.getFarmerId()));
				            	
				            }
				           
						pmtnr.setPmtDetails(pmtnrDetailsSet);
						Set<OfflinePMTImageDetails> pmtImgSet = Collections.synchronizedSet(offlinePMTNR.getOfflinePmtImages());

						if (pmtImgSet.size() > 0) {
							Set<PMTImageDetails> pmtnrImgDetailsSet = new HashSet<PMTImageDetails>();

							for (OfflinePMTImageDetails imageDetails : offlinePMTNR.getOfflinePmtImages()) {
								PMTImageDetails img = new PMTImageDetails();
								img.setCaptureTime(imageDetails.getCaptureTime());
								img.setLatitude(imageDetails.getLatitude());
								img.setLongitude(imageDetails.getLongitude());
								img.setPhoto(imageDetails.getPhoto());
								img.setType(PMTImageDetails.Type.PMT.ordinal());
								pmtnrImgDetailsSet.add(img);
							}

							pmtnr.setPmtImageDetails(pmtnrImgDetailsSet);
						}
					} else {
						throw new SwitchException(SwitchErrorCodes.PROCUREMENT_MENT_PRODUCTS_NOT_FOUND);
					}
					try {
						// Saving PMT and PMT Details
						pmtnr.setStatusMessage(PMT.Status.MTNR.toString());
						pmtnr.setTrnType(PMT.TRN_TYPE_PRODUCT_RECEPTION);
						pmtnr.setStatusCode(PMT.Status.MTNR.ordinal());
						productDistributionService.savePMTNR(pmtnr, tenantId);

						// productDistributionService.processCityWarehouse(pmtnr);

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

				offlinePMTNR.setStatusCode(statusCode);
				offlinePMTNR.setStatusMsg(statusMsg);
				productDistributionService.updateOfflinePMTNR(offlinePMTNR, tenantId);

			} // end for loop

		}

	}

	private TransferInfo getTransferInfo(Head head) {

		TransferInfo transferInfo = new TransferInfo();
		Agent agent = agentService.findAgentByProfileId(head.getAgentId());
		if (!ObjectUtil.isEmpty(agent)) {
			transferInfo.setAgentId(agent.getProfileId());
			transferInfo.setAgentName(
					(ObjectUtil.isEmpty(agent.getPersonalInfo()) ? "" : agent.getPersonalInfo().getAgentName()));
			transferInfo.setServicePointId(
					(ObjectUtil.isEmpty(agent.getServicePoint()) ? "" : agent.getServicePoint().getCode()));
			transferInfo.setServicePointName(
					(ObjectUtil.isEmpty(agent.getServicePoint()) ? "" : agent.getServicePoint().getName()));
		}
		Device device = deviceService.findDeviceBySerialNumber(head.getSerialNo());
		if (!ObjectUtil.isEmpty(device)) {
			transferInfo.setDeviceId(device.getCode());
			transferInfo.setDeviceName(device.getName());
		}
		transferInfo.setOperationType(ESETxn.ON_LINE);
		return transferInfo;
	}
}
