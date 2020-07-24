/*
 * TxnLocationProcessService.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ese.entity.util.ESESystem;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.LocationHistory;
import com.sourcetrace.eses.entity.LocationHistoryDetail;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.txn.schema.Locate;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.OpenStreetMapUtils;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.BankInformation;

/**
 * @author PANNEER
 */
@WebService(serviceName = "ITxnLocationProcessService", name = "TxnLocationProcessService", targetNamespace = "http://www.sourcetrace.com")
public class TxnLocationProcessService implements ITxnLocationProcessService {

	public static Logger LOGGER = Logger.getLogger(TxnLocationProcessService.class.getName());

	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IPreferencesService preferncesService;
	SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.SYNC_DATE_TIME);
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.service.ITxnLocationProcessService#locationProcess(
	 * com.sourcetrace.eses .txn.schema.Locate)
	 */
	@Override
	public String locationProcess(Locate locate) {
		
		LOGGER.info("Txn Time:" + locate.getTxnTime() + "<--->Serial No:" + locate.getSerialNo() + "<--->Lat:"
				+ locate.getLatitude() + "<--->Lon:" + locate.getLongitude());
		
		boolean flag = true;
		try {

			if (!ObjectUtil.isEmpty(locate) && !ObjectUtil.isEmpty(locate.getSerialNo())
					&& !StringUtil.isEmpty(locate.getLatitude())) {
				Device device = deviceService.findDeviceBySerialNumber(locate.getSerialNo(), locate.getTenantId());
				if (!ObjectUtil.isEmpty(device) && !ObjectUtil.isEmpty(device.getAgent())) {
					Agent agent = agentService.findAgentByProfileId(String.valueOf(device.getAgent().getProfileId()));
					agent.setVersion(locate.getVersNo());
					agent.setUpdateTime(DateUtil.convertStringToDate(locate.getTxnTime(), DateUtil.TXN_TIME_FORMAT));
					agentService.update(agent);
					Date date=new SimpleDateFormat("yyyy-MM-dd").parse(locate.getTxnTime());  
					LocationHistory locationHistorys = locationService.findLocationHistoryByTxnTimeSerialNoBranch(date,
							device.getAgent().getProfileId(), device.getBranchId());
					if (locationHistorys == null) {
						LocationHistory locationHistory = new LocationHistory();
						locationHistory
								.setTxnTime(DateUtil.convertStringToDate(locate.getTxnTime(), DateUtil.TXN_DATE_TIME));
						locationHistory.setSerialNumber(locate.getSerialNo());
						locationHistory.setAgentId(device.getAgent().getProfileId());
						locationHistory.setBranchId(device.getBranchId());
						locationHistory.setCreatedTime(new Date());
						locationHistory.setLatitude(locate.getLatitude());
						locationHistory.setLongitude(locate.getLongitude());
						locationHistory.setStartTime(
								DateUtil.convertStringToDate(locate.getTxnTime(), DateUtil.TXN_DATE_TIME));
						locationHistory
								.setEndTime(DateUtil.convertStringToDate(locate.getTxnTime(), DateUtil.TXN_DATE_TIME));
						//Set<LocationHistoryDetail> LocationHistoryDetailSet = new HashSet<LocationHistoryDetail>();
						Set<LocationHistoryDetail> locationHistoryDetail=getLocationDetail_set(locate,device,locationHistory);
						
					
						locationHistory.setLocationHistoryDetails(locationHistoryDetail);
						if (flag) {
							agentService.addLocationHistory(locationHistory, locate.getTenantId());
						}

						// locationHistory.setLocationHistoryDetails(new
						// HashSet<LocationHistoryDetail>());
					} else {
						locationHistorys
								.setEndTime(DateUtil.convertStringToDate(locate.getTxnTime(), DateUtil.TXN_DATE_TIME));
						locationHistorys.setEndlongitude(locate.getLongitude());
						locationHistorys.setEndlatitude(locate.getLatitude());
						Set<LocationHistoryDetail> locationHistoryDetail=getLocationDetail_set(locate,device,locationHistorys);
						//locationHistorys.setLocationHistoryDetails(locationHistoryDetail);
						agentService.update(locationHistorys);
					}

				}
			}
			return "{\"Response\":{\"status\":{\"code\":\"00\",\"message\":\"SUCCESS\"}}}";
		} catch (Exception e) {
			return "{\"Response\":{\"status\":{\"code\":\"02\",\"message\":\"" + e.toString() + "\"}}}";
		}
	}

	
	
	public Set<LocationHistoryDetail> getLocationDetail_set(Locate locate,Device device,LocationHistory locationHistory){
		Set<LocationHistoryDetail> LocationHistoryDetailSet = new HashSet<LocationHistoryDetail>();
		DecimalFormat decimalFormat = new DecimalFormat("#");
		LocationHistoryDetail locationHistoryDetail = new LocationHistoryDetail();
		
		locationHistoryDetail.setTxnTime(
				DateUtil.convertStringToDate(locate.getTxnTime(), DateUtil.TXN_TIME_FORMAT));
		locationHistoryDetail.setSerialNumber(locate.getSerialNo());
		locationHistoryDetail.setLongitude(
				!StringUtil.isEmpty(locate.getLongitude()) ? locate.getLongitude().trim() : "");
		locationHistoryDetail.setLatitude(
				!StringUtil.isEmpty(locate.getLatitude()) ? locate.getLatitude().trim() : "");
		locationHistoryDetail.setAgentId(device.getAgent().getProfileId());
		locationHistoryDetail.setBranchId(device.getBranchId());
		locationHistoryDetail.setCreatedTime(new Date());
		locationHistoryDetail.setDistance("0");
		locationHistoryDetail.setLocHistory(locationHistory);
		if (locate.getAccuracy() != null) {
			long number = 0;
			try {
				number = decimalFormat.parse(locate.getAccuracy()).longValue();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			locationHistoryDetail.setAccuracy(number);
		} else {
			locationHistoryDetail.setAccuracy(1);
		}
		if (locate.getNetStatus() != null) {
			locationHistoryDetail.setNetStatus(locate.getNetStatus());
		}
		if (locate.getGpsStatus() != null) {
			locationHistoryDetail.setGpsStatus(locate.getGpsStatus());
		}
		if(locationHistory.getId() !=0){
		agentService.save(locationHistoryDetail);
		}
		LocationHistoryDetailSet.add(locationHistoryDetail);
		return LocationHistoryDetailSet;
	}
	
	public String getGeneralDateFormat() {
		ESESystem preferences = preferncesService.findPrefernceById("1");
		if (!StringUtil.isEmpty(preferences)) {
			return preferences.getPreferences().get(ESESystem.GENERAL_DATE_FORMAT);
		}
		return null;

	}




	
}
