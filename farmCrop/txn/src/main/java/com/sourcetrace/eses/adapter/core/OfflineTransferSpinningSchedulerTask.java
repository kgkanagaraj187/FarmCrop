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

import com.ese.entity.traceability.BaleGeneration;
import com.ese.entity.traceability.GinningProcess;
import com.ese.entity.traceability.SpinningTransfer;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.OfflineGinningProcess;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTNR;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTNRDetail;
import com.sourcetrace.eses.order.entity.txn.OfflineSpinningTransfer;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.TransferInfo;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
import com.sourcetrace.eses.service.LocationService;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class OfflineTransferSpinningSchedulerTask {
	@Resource(name = "datasources")
	private Map<String, DataSource> datasources;

	@Autowired
	private IProductDistributionService productDistributionService;

	@Autowired
	private IAgentService agentService;
	
	@Autowired
	private IDeviceService deviceService;
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private ILocationService locationService;

	String farmers;
	public void process() {
		Vector<String> tenantIds = new Vector(datasources.keySet());
		for (String tenantId : tenantIds) {
			List<OfflineSpinningTransfer> offlineSpinningTransferList = Collections
					.synchronizedList(this.productDistributionService.listPendingOfflineSpinningTransfer(tenantId));
			for (OfflineSpinningTransfer offlineProcess : offlineSpinningTransferList) {
				int statusCode = ESETxnStatus.SUCCESS.ordinal();
				String statusMsg = ESETxnStatus.SUCCESS.toString();
				try {
					if (StringUtil.isEmpty(offlineProcess.getAgentId()))
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ID_EMPTY);
					Agent agent = agentService.findAgentByAgentId(offlineProcess.getAgentId(), tenantId,
							offlineProcess.getBranchId());
					if (ObjectUtil.isEmpty(agent))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_AGENT);
					if(StringUtil.isEmpty(offlineProcess.getDeviceId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);
					Device device=deviceService.findDeviceBySerialNumber(offlineProcess.getDeviceId(),tenantId);
					if(ObjectUtil.isEmpty(device))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DEVICE);
					SpinningTransfer st=new SpinningTransfer();
					st.setGinning(agent.getProcurementCenter());
					Warehouse spin=locationService.findWarehouseByCodeAndType(offlineProcess.getSpinnerCode(),Warehouse.WarehouseTypes.SPINNER.ordinal(),tenantId);
					if(!ObjectUtil.isEmpty(spin))
						st.setSpinning(spin);
					else
						throw new OfflineTransactionException(ITxnErrorCodes.WAREHOUSE_DOES_NOT_EXIST);
					try {
						st.setTransDate(DateUtil.convertStringToDate(offlineProcess.getTransDate(),DateUtil.TXN_DATE_TIME));
					} catch (Exception e) {
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DATE_FORMAT);
					}
					st.setInvoiceNo(offlineProcess.getInvoiceNo());
					st.setTruckNo(offlineProcess.getTruckNo());
					st.setLotNo(offlineProcess.getLotNo());
					st.setPrNo(offlineProcess.getPrNo());
					st.setSeason(offlineProcess.getSeason());
					st.setNoBals(Long.parseLong(offlineProcess.getNoBals()));
					st.setNetWeight(Double.parseDouble(offlineProcess.getNetWeight()));
					st.setRate(Double.parseDouble(offlineProcess.getRate()));
					st.setNetAmt(Double.parseDouble(offlineProcess.getNetAmt()));
					st.setType(offlineProcess.getType());
					st.setBranchId(offlineProcess.getBranchId());

					farmers="";
					
					if(offlineProcess.getLotNo().contains(",")){
						for(String lot: offlineProcess.getLotNo().split(",")){
								List<BaleGeneration> baleList=productDistributionService.findBaleGenerationByLotNo(lot.trim(),tenantId);
								if(baleList!=null && !ObjectUtil.isListEmpty(baleList)){
									baleList.stream().filter(f-> f.getGinningProcess()!=null && !ObjectUtil.isEmpty(f.getGinningProcess()) && f.getGinningProcess().getFarmer()!=null && !StringUtil.isEmpty(f.getGinningProcess().getFarmer())).forEach(a-> {
									farmers=farmers!=null && !StringUtil.isEmpty(farmers)?!farmers.contains(a.getGinningProcess().getFarmer())?farmers+","+a.getGinningProcess().getFarmer():farmers:a.getGinningProcess().getFarmer();
								//if(!farmers.contains(a.getGinningProcess().getFarmer()))
									//farmers+=a.getGinningProcess().getFarmer()+", ";	
							});
								}
						}
						farmers=StringUtil.removeLastComma(farmers.trim());
					}
					else{
						List<BaleGeneration> baleList=productDistributionService.findBaleGenerationByLotNo(offlineProcess.getLotNo().trim(),tenantId);
						if(baleList!=null && !ObjectUtil.isListEmpty(baleList)){
						baleList.stream().filter(f-> f.getGinningProcess()!=null && !ObjectUtil.isEmpty(f.getGinningProcess()) && f.getGinningProcess().getFarmer()!=null && !StringUtil.isEmpty(f.getGinningProcess().getFarmer())).forEach(a-> {
							farmers=farmers!=null && !StringUtil.isEmpty(farmers)?!farmers.contains(a.getGinningProcess().getFarmer())?farmers+","+a.getGinningProcess().getFarmer():farmers:a.getGinningProcess().getFarmer();
						});
						farmers=StringUtil.removeLastComma(farmers.trim());
						}
					}
					st.setFarmer(farmers!=null && !StringUtil.isEmpty(farmers)?farmers:null);
					Set<OfflinePMTImageDetails> pmtImgSet = Collections.synchronizedSet(offlineProcess.getOfflinePmtImages());

					if (pmtImgSet.size() > 0) {
						Set<PMTImageDetails> pmtnrImgDetailsSet = new HashSet<PMTImageDetails>();

						for (OfflinePMTImageDetails imageDetails : offlineProcess.getOfflinePmtImages()) {
							PMTImageDetails img = new PMTImageDetails();
							img.setCaptureTime(imageDetails.getCaptureTime());
							img.setLatitude(imageDetails.getLatitude());
							img.setLongitude(imageDetails.getLongitude());
							img.setPhoto(imageDetails.getPhoto());
							img.setType(PMTImageDetails.Type.SPINNING.ordinal());
							pmtnrImgDetailsSet.add(img);
						}

						st.setPmtImageDetails(pmtnrImgDetailsSet);
					}
					
					productDistributionService.saveSpinningTransfer(st,tenantId);
					
					

				} catch (OfflineTransactionException ote) {
					statusCode = ESETxnStatus.ERROR.ordinal();
					statusMsg = ote.getError();
				} catch (Exception e) { // Catches all type of exception except
					// OfflineTransactionException
					statusCode = ESETxnStatus.ERROR.ordinal();
					statusMsg = e.getMessage().substring(0,
							e.getMessage().length() > 40 ? 40 : e.getMessage().length());
				}
				offlineProcess.setStatusCode(statusCode);
				offlineProcess.setStatusMsg(statusMsg);
				productDistributionService.updateOfflineSpinningTransfer(offlineProcess,tenantId);
			}
		}
	}
	public String getFarmers() {
		return farmers;
	}
	public void setFarmers(String farmers) {
		this.farmers = farmers;
	}
	
}
