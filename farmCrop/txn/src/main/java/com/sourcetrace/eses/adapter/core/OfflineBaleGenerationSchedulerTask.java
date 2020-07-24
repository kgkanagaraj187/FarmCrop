package com.sourcetrace.eses.adapter.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
import com.ese.entity.traceability.HeapData;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.OfflineBaleGeneration;
import com.sourcetrace.eses.order.entity.txn.OfflineGinningProcess;
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
import com.sourcetrace.eses.service.IPreferencesService;
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
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class OfflineBaleGenerationSchedulerTask {
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
	private IPreferencesService preferncesService;
	String seasonId;

	public void process() {
		Vector<String> tenantIds = new Vector(datasources.keySet());
		for (String tenantId : tenantIds) {
			List<OfflineBaleGeneration> offlineBaleGenerationList = Collections
					.synchronizedList(this.productDistributionService.listPendingOfflingBaleGeneration(tenantId));
			for (OfflineBaleGeneration offlineProcess : offlineBaleGenerationList) {
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
					BaleGeneration bg=new BaleGeneration();
					bg.setHeap(offlineProcess.getHeap());
					bg.setSeason(offlineProcess.getSeason());
					try {
						bg.setGenDate(DateUtil.convertStringToDate(offlineProcess.getGenDate(),DateUtil.DATE));
						bg.setBaleDate(DateUtil.convertStringToDate(offlineProcess.getBaleDate(),DateUtil.DATE));
					} catch (Exception e) {
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DATE_FORMAT);
					}
					bg.setGinning(agent.getProcurementCenter());
					bg.setLotNo(offlineProcess.getLotNo());
					bg.setPrNo(offlineProcess.getPrNo());
					bg.setBaleWeight(Double.parseDouble(offlineProcess.getBaleWeight()));
					bg.setBranchId(offlineProcess.getBranchId());
					bg.setStatus(0);
					HeapData heapData=!StringUtil.isEmpty(offlineProcess.getHeap())?productDistributionService.findHeapDataByHeapCode(agent.getProcurementCenter().getId(),offlineProcess.getHeap(),tenantId,offlineProcess.getSeason()):null;
					if(!ObjectUtil.isEmpty(heapData)){
					GinningProcess ginningProcess=productDistributionService.findGinningProcessByDateHeapAndProduct(agent.getProcurementCenter().getId(),offlineProcess.getGenDate(),offlineProcess.getHeap(),heapData.getProcurementProduct().getId(),tenantId,offlineProcess.getSeason());
					if(!ObjectUtil.isEmpty(ginningProcess)){
						ginningProcess.setFarmer(heapData.getFarmer());
						ginningProcess.setTotlintCotton(ginningProcess.getTotlintCotton()+(Double.parseDouble(offlineProcess.getBaleWeight())/100));
						ginningProcess.setLintPer((ginningProcess.getTotlintCotton()/ginningProcess.getProcessQty())*100);
						ginningProcess.setBaleCount(ginningProcess.getBaleCount()+1);
						Set<BaleGeneration> balGen=new LinkedHashSet<>();
						balGen.add(bg);
						ginningProcess.setBaleGenerations(balGen);
						productDistributionService.updateGinningProcess(ginningProcess,tenantId);
						bg.setGinningProcess(ginningProcess);
						productDistributionService.saveBaleGeneration(bg,tenantId);
					}
					else{
						throw new OfflineTransactionException(ITxnErrorCodes.GINNING_UNAVAILABLE);
					}
					}
					
					

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
				productDistributionService.updateOfflineBaleGeneration(offlineProcess,tenantId);
			}
		}
	}
	
	public IPreferencesService getPreferncesService() {
		return preferncesService;
	}

	public void setPreferncesService(IPreferencesService preferncesService) {
		this.preferncesService = preferncesService;
	}

	public String getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}
	
}
