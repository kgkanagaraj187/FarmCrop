package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
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
import com.sourcetrace.eses.order.entity.txn.EventCalendar;
import com.sourcetrace.eses.order.entity.txn.EventReport;
import com.sourcetrace.eses.order.entity.txn.OfflineFarmCropCalendar;
import com.sourcetrace.eses.order.entity.txn.OfflineFarmCropCalendarDetail;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurement;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.Sensitizing;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ServicePoint;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class FarmCropCalendarAdapter implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(FarmCropCalendarAdapter.class.getName());
	public static final String NOT_APPLICABLE = "N/A";

	@Autowired
	private IAgentService agentService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IProductDistributionService productDistributionService;
	
	private String agentName;

	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {

		/** REQUEST DATA **/
		Head head = (Head) reqData.get(TransactionProperties.HEAD);

		/** VALIDATING REQUEST DATA **/
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		String servPointId = head.getServPointId();
		String txnMode = head.getMode();
		String branchId = head.getBranchId();
		String tenantId = head.getTenantId();

		LOGGER.info("AGENT ID : " + agentId);
		LOGGER.info("SERIAL NO : " + serialNo);
		LOGGER.info("SERVICE POINT ID : " + servPointId);
		LOGGER.info("TXN MODE: " + txnMode);

		String farmerId = (reqData.containsKey(TxnEnrollmentProperties.FARMER_ID))
				? (String) reqData.get(TxnEnrollmentProperties.FARMER_ID) : "";
		String farmCode = (reqData.containsKey(TxnEnrollmentProperties.FARM_ID))
				? (String) reqData.get(TxnEnrollmentProperties.FARM_ID) : "";
		String varietyId = (reqData.containsKey(TxnEnrollmentProperties.CROP_ID))
				? (String) reqData.get(TxnEnrollmentProperties.CROP_ID) : "";
		String seasonCode = (reqData.containsKey(TxnEnrollmentProperties.CROP_SEASON))
				? (String) reqData.get(TxnEnrollmentProperties.CROP_SEASON) : "";

		

			/** FORMING OFFLINE FARM CROP CALENDAR OBJECT **/

			OfflineFarmCropCalendar offlineFarmCropCalendar = new OfflineFarmCropCalendar();
			offlineFarmCropCalendar.setAgentId(agentId);
			offlineFarmCropCalendar.setDeviceId(serialNo);		
			offlineFarmCropCalendar.setFarmerId(farmerId);
			offlineFarmCropCalendar.setFarmCode(farmCode);
			offlineFarmCropCalendar.setVarietyId(varietyId);
			offlineFarmCropCalendar.setSeasonCode(seasonCode);	
			offlineFarmCropCalendar.setCreatedDate(head.getTxnTime());
			Agent agent = agentService.findAgentByAgentId(agentId);

			if (!ObjectUtil.isEmpty(agent.getPersonalInfo())) {
				agentName = agent.getPersonalInfo().getAgentName();
			}
			offlineFarmCropCalendar.setCreatedUser(agentName);			
			offlineFarmCropCalendar.setStatusCode(ESETxnStatus.PENDING.ordinal());
			offlineFarmCropCalendar.setStatusMsg(ESETxnStatus.PENDING.toString());
			Set<OfflineFarmCropCalendarDetail> offlineFarmCropCalendarDetails = new HashSet<OfflineFarmCropCalendarDetail>();

			/** FORMING OFFLINE PROCUREMENT DETAIL OBJECT **/
			Collection collection = (Collection) reqData.get(TxnEnrollmentProperties.ACTIVITY_LIST);
			List<com.sourcetrace.eses.txn.schema.Object> objectList = collection.getObject();
			for (Object object : objectList) {
				List<Data> offlineFarmCropCalendarDataList = object.getData();
				OfflineFarmCropCalendarDetail offlineFarmCropCalendarDetail = new OfflineFarmCropCalendarDetail();

				for (Data offlineFarmCropCalendarData : offlineFarmCropCalendarDataList) {

					String key = offlineFarmCropCalendarData.getKey();
					String value = offlineFarmCropCalendarData.getValue();

					if (TxnEnrollmentProperties.ACTIVITY_ID.equalsIgnoreCase(key)) {
						offlineFarmCropCalendarDetail.setActivityMethod(value);
					}

					if (TxnEnrollmentProperties.STATUS.equalsIgnoreCase(key)) {
						offlineFarmCropCalendarDetail.setStatus(value);
					}
					if (TxnEnrollmentProperties.WEATHER_DATE.equalsIgnoreCase(key)) {
						offlineFarmCropCalendarDetail.setDate(value);
					}
					if (TxnEnrollmentProperties.REMARKS.equalsIgnoreCase(key)) {
						offlineFarmCropCalendarDetail.setRemarks(value);
					}
					
				}
				offlineFarmCropCalendarDetail.setOfflineFarmCropCalendar(offlineFarmCropCalendar);
				offlineFarmCropCalendarDetails.add(offlineFarmCropCalendarDetail);
			}

			/**
			 * SAVING OFFLINE PROCUREMENT, OFFLINE PROCUREMENT DETAIL OBJECT
			 **/
			try {
				offlineFarmCropCalendar.setBranchId(head.getBranchId());
				offlineFarmCropCalendar.setOfflineFarmCropCalendarDetail(offlineFarmCropCalendarDetails);

				productDistributionService.addOfflineFarmCropCalendarAndOfflineFarmCropCalendarDetail(offlineFarmCropCalendar);
			} catch (Exception e) {
				e.printStackTrace();
			}

		/** FORMING RESPONSE **/
		Map resp = new HashMap();
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

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	

}
