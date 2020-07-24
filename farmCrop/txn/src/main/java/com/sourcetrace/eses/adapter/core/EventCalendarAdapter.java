package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.EventCalendar;
import com.sourcetrace.eses.order.entity.txn.EventReport;
import com.sourcetrace.eses.order.entity.txn.Sensitizing;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.LocationService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class EventCalendarAdapter implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(EventCalendarAdapter.class.getName());
	public static final String NOT_APPLICABLE = "N/A";

	@Autowired
	private IAgentService agentService;

	@Autowired
	private IFarmerService farmerService;
	
	@Autowired
	private ILocationService locationService;
	
	@Autowired
	private ICatalogueService catalogueService;

	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		String servicePointId = head.getServPointId();
		String txnMode = head.getTxnType();
		

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
		if (txnMode.equalsIgnoreCase("396")) {
			String eventType = (String) reqData.get(TxnEnrollmentProperties.EVENT_TYPE);
			String sDate = (String) reqData.get(TxnEnrollmentProperties.START_DATEE);
			String eDate = (String) reqData.get(TxnEnrollmentProperties.END_DATEE);
			String sTime = (String) reqData.get(TxnEnrollmentProperties.START_TIME);
			String eTime = (String) reqData.get(TxnEnrollmentProperties.END_TIME);
			String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMERID);
			String purpouse = (String) reqData.get(TxnEnrollmentProperties.PURPOUSE);
			String msgNo = (String) reqData.get(TxnEnrollmentProperties.EVENT_ID);
			String grp = (String) reqData.get(TxnEnrollmentProperties.GRP);
			

			EventCalendar evntCal = new EventCalendar();
			evntCal.setAgentId(agent.getProfileId());
			evntCal.setAgentName(agent.getPersonalInfo().getFirstName());
			evntCal.setCreatedDate(new Date());
			evntCal.setEventType(eventType);
			Date startDate = DateUtil.convertStringToDate(sDate, DateUtil.FARMER_DOB);
			Date endDate = DateUtil.convertStringToDate(eDate, DateUtil.FARMER_DOB);
			evntCal.setStartDate(startDate);
			evntCal.setEndDate(endDate);
			evntCal.setFarmerId(farmerId);
			if(sTime!=null&&sTime!="")
			evntCal.setStartTime(sTime.substring(0,2)+":"+sTime.substring(2,4)+sTime.substring(4,6));
			if(eTime!=null&&eTime!="")
			evntCal.setEndTime(eTime.substring(0,2)+":"+eTime.substring(2,4)+eTime.substring(4,6));
			//evntCal.setEndTime(eTime);
			evntCal.setPurpose(purpouse);
			evntCal.setMsgNo(msgNo);
			evntCal.setBranchId(head.getBranchId());
			evntCal.setRevisionNo(DateUtil.getRevisionNoDateTimeMilliSec());
			evntCal.setStatus("0");
			evntCal.setGroup(grp);
			farmerService.save(evntCal);
		} else if (txnMode.equalsIgnoreCase("397")) {

			String eventId = (String) reqData.get(TxnEnrollmentProperties.EVENT_ID);
			String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMERID);
			String status = (String) reqData.get(TxnEnrollmentProperties.STATUS);
			String output = (String) reqData.get(TxnEnrollmentProperties.ER_OUTPUT);
			String remark = (String) reqData.get(TxnEnrollmentProperties.REMARKZ);
			DataHandler photoDataHandler = (DataHandler) reqData.get(TxnEnrollmentProperties.PHOTO);
			String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
			String longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
			String photoCaptureTime = (String) reqData.get(TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME);
			String grp = (String) reqData.get(TxnEnrollmentProperties.GRP);

			
			EventReport evntReport = new EventReport();
			evntReport.setEventId(eventId);
			evntReport.setFarmerId(farmerId);
			evntReport.setStatus(status);
			evntReport.setOutput(output);
			evntReport.setRemark(remark);
			evntReport.setAgentId(agent.getProfileId());
			evntReport.setAgentName(agent.getPersonalInfo().getFirstName());
			evntReport.setCreatedDate(new Date());
			evntReport.setBranchId(head.getBranchId());
			evntReport.setGroup(grp);
			
			byte[] photoContent = null;
			try {
				if (photoDataHandler != null)
					photoContent = IOUtils.toByteArray(photoDataHandler.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (!StringUtil.isEmpty(photoContent) && photoContent.length > 0) {
				if (photoContent != null) {
					evntReport.setPhoto(photoContent);
				}
			}
			evntReport.setLatitude(latitude);
			evntReport.setLongitude(longitude);
			Date photoCaptureDate = DateUtil.convertStringToDate(photoCaptureTime, DateUtil.TXN_TIME_FORMAT);
			evntReport.setCaptureTime(photoCaptureDate);
			EventCalendar eventCalender = farmerService.findEventByMessageNumber(eventId);
			eventCalender.setStatus(status);
			eventCalender.setUpdatedDate(new Date());
			eventCalender.setRevisionNo(DateUtil.getRevisionNoDateTimeMilliSec());
			
			
			evntReport.setStartDate(eventCalender.getStartDate());
			evntReport.setEndDate(eventCalender.getEndDate());
			evntReport.setStartTime(eventCalender.getStartTime());
			evntReport.setEndTime(eventCalender.getEndTime());
			evntReport.setEventType(eventCalender.getEventType());
			evntReport.setPurpose(eventCalender.getPurpose());
			Warehouse w = locationService.findWarehouseByCode(evntReport.getGroup());
			evntReport.setWarehouseName(w != null && !StringUtil.isEmpty(w.getName()) ? w.getName() : "");
			String catalogueName = catalogueService.findCatalogueValueByCode(evntReport.getEventType());
			evntReport.setEventTypeCatalogueName(!StringUtil.isEmpty(catalogueName) ? catalogueName : "");
			
			String statusCatalogueName = catalogueService.findCatalogueValueByCode(evntReport.getStatus());
			evntReport.setStatusCatalogueName((statusCatalogueName == null || statusCatalogueName == "null") ? "Initiated" : !StringUtil.isEmpty(statusCatalogueName) ? statusCatalogueName : "");
			Agent a = agentService.findAgentByAgentId(evntReport.getAgentId());
			evntReport.setAgentPersInfoName(a != null && a.getPersonalInfo() != null ? a.getPersonalInfo().getFirstName()+" "+a.getPersonalInfo().getLastName() : "");
			
			farmerService.update(eventCalender);
			farmerService.save(evntReport);
		}
		return null;

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
