package com.sourcetrace.eses.interceptors;

import java.util.Date;
import java.util.Set;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
import com.sourcetrace.eses.interceptor.TxnMessageUtil;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLogDetail;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.txn.schema.Body;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;

@Component
public class TxnAcessInterceptor extends AbstractPhaseInterceptor<Message> {

	private Set<String> txnSet;
	@Autowired
	private IAgentService agentService;

	@Autowired
	private IDeviceService deviceService;

	public TxnAcessInterceptor() {
		super(Phase.PRE_INVOKE);
	}

	@Override
	public void handleMessage(Message msg) throws Fault {

		Object head = TxnMessageUtil.getHead(msg);

	     BeanWrapper wrapper = new BeanWrapperImpl(head);
	        if (!ObjectUtil.isEmpty(head) && !((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE)).equals("400") ) {
	   		String txnType = (String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE);
			if (txnSet.contains(txnType)) {
				String agentId = (String) wrapper.getPropertyValue(ITxnMessageUtil.AGENT_ID);
				String serialNumber = (String) wrapper.getPropertyValue(ITxnMessageUtil.SERIAL_NO);
				Device device = deviceService.findDeviceBySerialNumber(serialNumber);
				Agent agent = agentService.findAgentByProfileAndBranchId(agentId, device.getBranchId());
				String count = (String) wrapper.getPropertyValue(ITxnMessageUtil.RESENT_COUNT);
				String txnDate = (String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TIME);

				if (txnType.equals("500")) {
					Body body = (Body) TxnMessageUtil.getBody(msg);
					Data d = body.getData().stream().filter(u -> u.getKey().equals(TxnEnrollmentProperties.DYNAMIC_TXN_ID)).findFirst().orElse( new Data());
					if (d!=null && !ObjectUtil.isEmpty(d)) {
						txnType = d.getValue();
						if( d.getValue().contains("-")){
							txnType = d.getValue().split("-")[0].trim().toString();
						}
						
					}
				}
				// String todaysDate=DateUtil.convertDateToString(new
				// Date(),DateUtil.DATABASE_DATE_FORMAT);
				Date formatedDate = DateUtil.convertStringToDate(txnDate, DateUtil.TXN_DATE_TIME);

				AgentAccessLog agentAccessLog = agentService.findAgentAccessLogByAgentId(agent.getProfileId(),
						DateUtil.getDateWithoutTime(formatedDate));
				if (!ObjectUtil.isEmpty(agentAccessLog)) {

					AgentAccessLogDetail agentAccessLogDetailExisting = agentService
							.listAgnetAccessLogDetailsByIdTxnType(agentAccessLog.getId(), txnType,
									(String) wrapper.getPropertyValue(ITxnMessageUtil.MSG_NO));

					if (ObjectUtil.isEmpty(agentAccessLogDetailExisting)) {

						AgentAccessLogDetail agentAccessLogDetail = agentService
								.findAgentAccessLogDetailByTxn(agentAccessLog.getId(), txnType);
						if (!ObjectUtil.isEmpty(agentAccessLogDetail)) {
							if( Long.valueOf(count)==0){
							Long txnCount = agentAccessLogDetail.getTxnCount() + 1L;
							agentAccessLogDetail.setTxnCount(txnCount);
							}
							agentAccessLogDetail.setTxnDate(formatedDate);
							agentService.update(agentAccessLogDetail);
						} else {
							AgentAccessLogDetail agentAccessLogDetailNew = new AgentAccessLogDetail();
							agentAccessLogDetailNew.setAgentAccessLog(agentAccessLog);
							/*agentAccessLogDetailNew
									.setTxnType((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE));*/
							agentAccessLogDetailNew.setTxnType(txnType);
							agentAccessLogDetailNew
									.setMessageNumber((String) wrapper.getPropertyValue(ITxnMessageUtil.MSG_NO));
							agentAccessLogDetailNew.setTxnMode((String) wrapper.getPropertyValue(ITxnMessageUtil.MODE));

							agentAccessLogDetailNew.setTxnDate(formatedDate);

							agentAccessLogDetailNew.setTxnCount(1L);

							agentAccessLogDetailNew.setServicePoint(
									(String) wrapper.getPropertyValue(ITxnMessageUtil.SERVICE_POINT_ID));

							agentService.save(agentAccessLogDetailNew);
						}

						agentAccessLog.setLogin(formatedDate);
						agentAccessLog.setLastTxnTime(formatedDate);
						agentService.update(agentAccessLog);
					}
				} else {
					AgentAccessLog agentAccessLogNew = new AgentAccessLog();
					agentAccessLogNew.setLogin(formatedDate);
					agentAccessLogNew.setMobileVersion((String) wrapper.getPropertyValue(ITxnMessageUtil.VERSION_NO));
					agentAccessLogNew.setProfileId((String) wrapper.getPropertyValue(ITxnMessageUtil.AGENT_ID));
					agentAccessLogNew.setSerialNo((String) wrapper.getPropertyValue(ITxnMessageUtil.SERIAL_NO));
					agentAccessLogNew.setBranchId(device.getBranchId());
					agentAccessLogNew.setLastTxnTime(formatedDate);
					agentService.save(agentAccessLogNew);

					AgentAccessLogDetail agentAccessLogDetail = new AgentAccessLogDetail();
					agentAccessLogDetail.setAgentAccessLog(agentAccessLogNew);
					agentAccessLogDetail.setTxnType(txnType);
					agentAccessLogDetail.setMessageNumber((String) wrapper.getPropertyValue(ITxnMessageUtil.MSG_NO));
					agentAccessLogDetail.setTxnMode((String) wrapper.getPropertyValue(ITxnMessageUtil.MODE));
					agentAccessLogDetail.setTxnCount(1L);
					agentAccessLogDetail
							.setServicePoint((String) wrapper.getPropertyValue(ITxnMessageUtil.SERVICE_POINT_ID));
					agentAccessLogDetail.setTxnDate(formatedDate);
					agentService.save(agentAccessLogDetail);
				}
			}
		}
	}

	public Set<String> getTxnSet() {

		return txnSet;
	}

	public void setTxnSet(Set<String> txnSet) {

		this.txnSet = txnSet;
	}

}
