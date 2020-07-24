package com.sourcetrace.eses.adapter.core;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.OfflineGinningProcess;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class GinningAdapter implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(GinningAdapter.class.getName());
	public static final String NOT_APPLICABLE = "N/A";

	@Autowired
	private IAgentService agentService;
	@Autowired
	private IProductDistributionService productDistributionService;

	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String messageNo=head.getMsgNo();
		OfflineGinningProcess exist=productDistributionService.findGinningProcessByMessageNo(messageNo);
		if(exist==null || ObjectUtil.isEmpty(exist)){
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		String servicePointId = head.getServPointId();
		String txnMode = head.getTxnType();
		String branch = head.getBranchId();
		String heap = (String) reqData.get(TransactionProperties.HEAP_CODE);
		String product = (String) reqData.get(TransactionProperties.PRODUCT_CODE);
		String ics = (String) reqData.get(TransactionProperties.ICS);
		String proQty = (String) reqData.get(TransactionProperties.PROCESS_QTY);
		String lintQty = (String) reqData.get(TransactionProperties.LINT_QTY);
		String seedQty = (String) reqData.get(TransactionProperties.SEED_QTY);
		String scrapQty = (String) reqData.get(TransactionProperties.SCRAP_QTY);
		String proDate = (String) reqData.get(TransactionProperties.PROCESS_DATE);
		String season = (String) reqData.get(TxnEnrollmentProperties.SEASON);
		Agent agent = agentService.findAgentByAgentId(agentId);
		if (ObjectUtil.isEmpty(agent))
			throw new SwitchException(SwitchErrorCodes.INVALID_AGENT);
		else
			LOGGER.info("AGENT ID: " + agentId);
		if (StringUtil.isEmpty(serialNo))
			try {
				throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SERIAL_NO);
			} catch (Exception e) {
			}
		else
			LOGGER.info("SERIAL NO: " + serialNo);

		LOGGER.info("SERVICE POINT ID: " + servicePointId);
		LOGGER.info("TXN MODE: " + txnMode);

		OfflineGinningProcess ogp = new OfflineGinningProcess();
		ogp.setAgentId(agentId);
		ogp.setBranchId(branch);
		ogp.setCreatedDate(new Date());
		ogp.setCreatedUser(agentId);
		ogp.setUpdatedUser(agentId);
		ogp.setDeviceId(serialNo);
		ogp.setMessageNo(messageNo);
		ogp.setLintQty("0");
		ogp.setGinning(agent.getProcurementCenter());
		ogp.setRemark((String) reqData.get(TransactionProperties.REMARKS));
		ogp.setStatusCode(ESETxnStatus.PENDING.ordinal());
		ogp.setStatusMsg(ESETxnStatus.PENDING.toString());
		if(StringUtil.isEmpty(season))
			throw new SwitchException(SwitchErrorCodes.EMPTY_SEASON_CODE);
		else
			ogp.setSeason(season);
		if (StringUtil.isEmpty(heap))
			throw new SwitchException(SwitchErrorCodes.EMPTY_HEAP_CODE);
		else
			ogp.setHeapCode(heap);
		if (StringUtil.isEmpty(product))
			throw new SwitchException(SwitchErrorCodes.EMPTY_PRODUCT_CODE);
		else
			ogp.setProductCode(product);

		if (StringUtil.isEmpty(proQty))
			throw new SwitchException(SwitchErrorCodes.EMPTY_PROCESS_STOCK);
		else
			ogp.setProcessQty(proQty);

		ogp.setSeedQty(seedQty);

		ogp.setScrapQty(scrapQty);

		if (StringUtil.isEmpty(proDate))
			throw new SwitchException(SwitchErrorCodes.EMPTY_PROCESS_DATE);
		else
			ogp.setProcessDate(proDate);
		productDistributionService.addOfflineGinningProcess(ogp);
		}
		
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
