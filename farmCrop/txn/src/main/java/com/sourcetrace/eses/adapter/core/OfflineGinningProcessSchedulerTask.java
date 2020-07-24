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

import com.ese.entity.traceability.GinningProcess;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
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
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IProductService;
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
public class OfflineGinningProcessSchedulerTask {
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

	public void process() {
		Vector<String> tenantIds = new Vector(datasources.keySet());
		for (String tenantId : tenantIds) {
			List<OfflineGinningProcess> offlineGinningProcessList = Collections
					.synchronizedList(this.productDistributionService.listPendingOfflineGinningProcess(tenantId));
			for (OfflineGinningProcess offlineProcess : offlineGinningProcessList) {
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
					GinningProcess gp=new GinningProcess();
					gp.setGinning(offlineProcess.getGinning());
					gp.setHeapCode(offlineProcess.getHeapCode());
					gp.setSeason(offlineProcess.getSeason());
					try {
						gp.setProcessDate(offlineProcess.getProcessDate());
					} catch (Exception e) {
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_DATE_FORMAT);
					}
					gp.setBaleCount(0);
					gp.setProduct(productService.findProcurementProductByCode(offlineProcess.getProductCode(),tenantId));
					gp.setProcessQty(Double.parseDouble(offlineProcess.getProcessQty()));
					gp.setTotlintCotton(0.0);
					gp.setTotseedCotton(Double.parseDouble(offlineProcess.getSeedQty()));
					gp.setTotscrap(Double.parseDouble(offlineProcess.getScrapQty()));
					//gp.setLintPer((gp.getTotlintCotton()/gp.getProcessQty())*100);
					gp.setSeedPer((gp.getTotseedCotton()/gp.getProcessQty())*100);
					gp.setScrapPer((gp.getTotscrap()/gp.getProcessQty())*100);
					gp.setBranchId(offlineProcess.getBranchId());
					// Makes Wrong while heap stock updation
					
					/*GinningProcess exist  =productDistributionService.findGinningProcessByDateHeapAndProduct(gp.getGinning().getId(),gp.getProcessDate(), gp.getHeapCode(), gp.getProduct().getId(), tenantId);
					if(exist!=null){
						exist.setTotseedCotton(exist.getTotseedCotton()+gp.getTotseedCotton());
						exist.setTotscrap(exist.getTotscrap()+gp.getTotscrap());
						exist.setProcessQty(exist.getProcessQty()+gp.getProcessQty());
						exist.setSeedPer((exist.getTotseedCotton()/exist.getProcessQty())*100);
						exist.setScrapPer((exist.getTotscrap()/exist.getProcessQty())*100);
					
					}else{
						exist = gp;
					}*/
					productDistributionService.saveGinningProcess(gp,tenantId);
					
					

				} catch (OfflineTransactionException ote) {
					statusCode = ESETxnStatus.ERROR.ordinal();
					statusMsg = ote.getError();
				} catch (Exception e) { // Catches all type of exception except
					// OfflineTransactionException
					statusCode = ESETxnStatus.ERROR.ordinal();
					statusMsg = e.getMessage();
				}
				offlineProcess.setStatusCode(statusCode);
				offlineProcess.setStatusMsg(statusMsg);
				productDistributionService.updateOfflineGinningProcess(offlineProcess,tenantId);
			}
		}
	}
	
}
