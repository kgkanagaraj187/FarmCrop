/*
 * SeasonDownload.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.EventCalendar;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;

/**
 * The Class SeasonDownload.
 */
@Component
public class EventCalendarDownload implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(EventCalendarDownload.class.getName());
    
    @Autowired
    private IFarmerService farmerService;

    /*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)s
     */
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** HEADER VALUES **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);

        String revisionNo = (String) reqData
                .get(TxnEnrollmentProperties.EVENT_DOWNLOAD_REVISION_NO);
        if (StringUtil.isEmpty(revisionNo))
            throw new SwitchException(ITxnErrorCodes.EMPTY_EVENT_REVISION_NO);
        LOGGER.info("REVISION NO" + revisionNo);
        
        
        if(!StringUtil.isLong(revisionNo)){
        	revisionNo = "0";
        }
        String agent=head.getAgentToken();
        String agentId=head.getAgentId();
        
        /** REQUEST VALUES **/
        Map resp = new HashedMap();
        List<EventCalendar> eventList = farmerService.listEventByRevisionNo(Long
                .valueOf(revisionNo),agentId);// productDistributionService.listSeasons();
        Collection collection = new Collection();
        List<Object> events = new ArrayList<Object>();

        if (!ObjectUtil.isEmpty(eventList)) {
            for (EventCalendar event : eventList) {
                Data eventId = new Data();
                eventId.setKey(TxnEnrollmentProperties.EVENT_ID);
                eventId.setValue(event.getMsgNo());

                Data eventName = new Data();
                eventName.setKey(TxnEnrollmentProperties.EVENT_NAME);
                eventName.setValue(event.getEventType());
                
                Data grp = new Data();
                grp.setKey(TxnEnrollmentProperties.GRP);
                if(event.getGroup()!=null&&event.getGroup()!="")
                grp.setValue(event.getGroup());
                else
                	grp.setValue("");
                
                Data purpouse = new Data();
                purpouse.setKey(TxnEnrollmentProperties.PURPOUSE);
                if(event.getPurpose()!=null&&event.getPurpose()!="")
                purpouse.setValue(event.getPurpose());
                else
                	purpouse.setValue("");
                
                Data startDate = new Data();
                startDate.setKey(TxnEnrollmentProperties.EVENT_START_DATE);
                String sDate="";
                if(event.getStartDate()!=null)
                	sDate=DateUtil.convertDateToString(event.getStartDate(),  DateUtil.FARMER_DOB);
                startDate.setValue(String.valueOf(sDate));
                
                Data endDate = new Data();
                String eDate="";
                if(event.getEndDate()!=null)
                	eDate=DateUtil.convertDateToString(event.getEndDate(),  DateUtil.FARMER_DOB);
                endDate.setKey(TxnEnrollmentProperties.EVENT_END_DATE);
                endDate.setValue(String.valueOf(eDate));
                
                Data startTime = new Data();
                startTime.setKey(TxnEnrollmentProperties.EVENT_START_TIME);
                String sTime="";
                if(event.getStartTime()!=null&&event.getStartTime()!=""){
                	sTime=event.getStartTime().replace(":","");
                }
                startTime.setValue(String.valueOf(sTime));
                Data endTime = new Data();
                endTime.setKey(TxnEnrollmentProperties.EVENT_END_TIME);
                String eTime="";
                if(event.getEndTime()!=null&&event.getEndTime()!=""){
                	eTime=event.getEndTime().replace(":","");
                }
                endTime.setValue(String.valueOf(eTime));

//                Data seasonYear = new Data();
//                seasonYear.setKey(ESETxnEnrollmentProperties.SEASON_YEAR);
//                seasonYear.setValue(season.getYear());

                List<Data> eventDataList = new ArrayList<Data>();
                eventDataList.add(eventId);
                eventDataList.add(eventName);
                eventDataList.add(startDate);
                eventDataList.add(endDate);
                eventDataList.add(startTime);
                eventDataList.add(endTime);
                eventDataList.add(purpouse);
                eventDataList.add(grp);
              
  
             //   seasonDataList.add(seasonYear);

                Object eventMasterObject = new Object();
                eventMasterObject.setData(eventDataList);
                events.add(eventMasterObject);

            }

            collection.setObject(events);

        }

        if (!ObjectUtil.isListEmpty(eventList)) {
            revisionNo = String.valueOf(eventList.get(0).getRevisionNo());
        }

        /** RESPONSE DATA **/
        resp.put(TxnEnrollmentProperties.EVENTS, collection);
        resp.put(TxnEnrollmentProperties.EVENT_DOWNLOAD_REVISION_NO, revisionNo);
        return resp;
    }

    public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	/*
     * (non-Javadoc)
     * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#processVoid(java.util.Map)
     */
    @Override
    public Map<?, ?> processVoid(Map<?, ?> reqData) {

        return null;
    }

	
}
