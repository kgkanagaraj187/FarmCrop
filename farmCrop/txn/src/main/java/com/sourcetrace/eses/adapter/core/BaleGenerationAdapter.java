package com.sourcetrace.eses.adapter.core;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.OfflineBaleGeneration;
import com.sourcetrace.eses.order.entity.txn.OfflineGinningProcess;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTNRDetail;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class BaleGenerationAdapter implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(BaleGenerationAdapter.class.getName());
	public static final String NOT_APPLICABLE = "N/A";
	
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IProductDistributionService productDistributionService;
	
	
	
	
	
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		Head head=(Head)reqData.get(TransactionProperties.HEAD);
		String messageNo=head.getMsgNo();
		OfflineBaleGeneration exist=productDistributionService.findBaleGenerationByMessageNo(messageNo);
		if(exist==null || ObjectUtil.isEmpty(exist)){
		String agentId=head.getAgentId();
		String serialNo=head.getSerialNo();
		String servicePointId=head.getServPointId();
		String txnMode=head.getTxnType();
		String branch=head.getBranchId();
		/*String heap=(String)reqData.get(TransactionProperties.HEAP_CODE);
		String balDate=(String)reqData.get(TransactionProperties.BALE_DATE);
		String lotNo=(String)reqData.get(TransactionProperties.LOT_NO);
		String prNo=(String)reqData.get(TransactionProperties.PR_NO);
		String baleWeight=(String) reqData.get(TransactionProperties.BALE_WEIGHT);*/
		
		
		Agent agent=agentService.findAgentByAgentId(agentId);
		if(ObjectUtil.isEmpty(agent))
			throw new SwitchException(SwitchErrorCodes.INVALID_AGENT);
		else
			LOGGER.info("AGENT ID: "+agentId);
		if(StringUtil.isEmpty(serialNo))
			try{
			throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);
			}catch(Exception e) {}
		else
			LOGGER.info("SERIAL NO: "+serialNo);
		
			LOGGER.info("SERVICE POINT ID: "+servicePointId);
			LOGGER.info("TXN MODE: "+txnMode);
		Collection collection = (Collection) reqData.get(TxnEnrollmentProperties.BALE_COLLECTION);
		if (!ObjectUtil.isEmpty(collection)) {
			List<com.sourcetrace.eses.txn.schema.Object> objectList = collection.getObject();
			if (!ObjectUtil.isEmpty(objectList) && objectList.size() > 0) {
				for (Object object : objectList) {
					List<Data> baleDataList = object.getData();
					OfflineBaleGeneration obg = new OfflineBaleGeneration();
					obg.setMessageNo(messageNo);
					obg.setAgentId(agentId);
					obg.setBranchId(branch);
					obg.setCreatedDate(new Date());
					obg.setCreatedUser(agentId);
					obg.setUpdatedUser(agentId);
					obg.setDeviceId(serialNo);
					String season=(String)reqData.get(TxnEnrollmentProperties.SEASON);
					if(season!=null && !StringUtil.isEmpty(season))
						obg.setSeason(season);
					else
						throw new SwitchException(SwitchErrorCodes.EMPTY_SEASON_CODE);
					obg.setBaleDate((String)reqData.get(TransactionProperties.BALE_DATE));
					obg.setStatusCode(ESETxnStatus.PENDING.ordinal());
					obg.setStatusMsg(ESETxnStatus.PENDING.toString());
					for (Data baleData : baleDataList) {
						String key = baleData.getKey();
						String value = baleData.getValue();
						
						if (TransactionProperties.HEAP_CODE.equalsIgnoreCase(key)) {
							obg.setHeap(value);
						}
						if (TransactionProperties.GIN_DATE.equalsIgnoreCase(key)) {
							obg.setGenDate(value);
						}
						if (TransactionProperties.LOT_NO.equalsIgnoreCase(key)) {
							obg.setLotNo(value);
						}
						if (TransactionProperties.PR_NO.equalsIgnoreCase(key)) {
							obg.setPrNo(value);
						}
						if (TransactionProperties.BALE_WEIGHT.equalsIgnoreCase(key)) {
							obg.setBaleWeight(value);
						}
			}
					productDistributionService.addOfflineBaleGeneration(obg);
		}	
			}
		}
		}
		/*OfflineBaleGeneration obg=new OfflineBaleGeneration();
		obg.setAgentId(agentId);
		obg.setBranchId(branch);
		obg.setCreatedDate(new Date());
		obg.setCreatedUser(agentId);
		obg.setUpdatedUser(agentId);
		obg.setDeviceId(serialNo);
		obg.setStatusCode(ESETxnStatus.PENDING.ordinal());
		obg.setStatusMsg(ESETxnStatus.PENDING.toString());
		obg.setHeap(heap);
		obg.setBaleWeight(baleWeight);
		obg.setLotNo(lotNo);
		obg.setPrNo(prNo);
		obg.setGenDate(balDate);*/
		
		//productDistributionService.addOfflineBaleGeneration(obg);
		Map<String, Object> resp = new LinkedHashMap<String, Object>();
		return resp;
	}
	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		return null;
	}
	public IAgentService getAgentService() {
		return agentService;
	}
	public void setAgentService(IAgentService agentService) {
		this.agentService = agentService;
	}
	
	
}
