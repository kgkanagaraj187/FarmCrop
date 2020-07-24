package com.sourcetrace.eses.adapter.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.entity.profile.ResearchStation;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sourcetrace.esesw.excep.SwitchException;

@Component
public class ResearchStationDownload implements ITxnAdapter {
    private static final Logger LOGGER = Logger.getLogger(ResearchStationDownload.class.getName());

    @Autowired
    private IFarmerService farmerService;
    SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FARMER_DOB);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {
        Head head = (Head) reqData.get(TransactionProperties.HEAD);
        
        String revisionNo =
        		(reqData.containsKey(TransactionProperties.RESEARCH_STATION_REV_NO))?(String) reqData.get(TransactionProperties.RESEARCH_STATION_REV_NO):"0";
        		
        if (StringUtil.isEmpty(revisionNo))
            throw new SwitchException(SwitchErrorCodes.EMPTY_RESEARCH_STATION_REVISION_NO);
        LOGGER.info("REVISION NO" + revisionNo);
       
        LOGGER.info("=================RESEARCH_STATION_DOWNLOAD_REV_NO  ======================"+revisionNo);
        if(!StringUtil.isLong(revisionNo)){
        	revisionNo = "0";
        }
        
        List<ResearchStation> researchStationList = farmerService.listResearchStationByRevNo(Long.valueOf(revisionNo));
        Collection researchStationCollection = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> researchStationObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
        researchStationList.stream().filter(researchStation->!ObjectUtil.isEmpty(researchStation)).forEach(researchStation->{
            com.sourcetrace.eses.txn.schema.Object researchStationObj = new com.sourcetrace.eses.txn.schema.Object();
            List<Data> researchStationDataList = new ArrayList<Data>();
            
            Data researchStationId = new Data();
            researchStationId.setKey(TransactionProperties.RESEARCH_STATION_ID);
            researchStationId.setValue(researchStation.getResearchStationId());
            researchStationDataList.add(researchStationId);
            
            Data researchStationName = new Data();
            researchStationName.setKey(TransactionProperties.RESEARCH_STATION_NAME);
            researchStationName.setValue(researchStation.getName());
            researchStationDataList.add(researchStationName);
            
            Data pointPerson = new Data();
            pointPerson.setKey(TransactionProperties.RESEARCH_STATION_POINT_PERSON);
            pointPerson.setValue(researchStation.getPointPerson());
            researchStationDataList.add(pointPerson);
            
            Data cowList = new Data();
            cowList.setKey(TransactionProperties.COW_LIST);
            cowList.setCollectionValue(buidCowValues(researchStation));
            researchStationDataList.add(cowList);
            
            researchStationObj.setData(researchStationDataList);
            researchStationObjectList.add(researchStationObj);
        });
        researchStationCollection.setObject(researchStationObjectList);
       
        Map resp = new LinkedHashMap();
        if (!ObjectUtil.isListEmpty(researchStationList)) {
			resp.put(TransactionProperties.RESEARCH_STATION_REV_NO, researchStationList.get(0).getRevisionNo());
		} else {
			resp.put(TransactionProperties.RESEARCH_STATION_REV_NO, StringUtil.isEmpty(revisionNo)?0:revisionNo);
		}
        resp.put(TransactionProperties.RESEARCH_STATION_LIST, researchStationCollection);
        return resp;
    }

    private Collection buidCowValues(ResearchStation researchStation) {

        Collection cowCollection = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> cowObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();

        if (!ObjectUtil.isListEmpty(researchStation.getCows())) {
            researchStation.getCows().stream().forEach(cow -> {
                com.sourcetrace.eses.txn.schema.Object cowObj = new com.sourcetrace.eses.txn.schema.Object();
                List<Data> cowDataList = new ArrayList<Data>();

                Data cowId = new Data();
                cowId.setKey(TxnEnrollmentProperties.COW_ID);
                cowId.setValue(cow.getCowId());
                cowDataList.add(cowId);

                Data lacationNumber = new Data();
                lacationNumber.setKey(TxnEnrollmentProperties.LACTATION_NUMBER);
                lacationNumber.setValue(cow.getLactationNo());
                cowDataList.add(lacationNumber);
                
                Date inspDate=farmerService.findByLastInspDate(cow.getCowId());
                Data inspLastDate = new Data();
                inspLastDate.setKey(TxnEnrollmentProperties.INSPECTION_DATE);
                inspLastDate.setValue(!ObjectUtil.isEmpty(inspDate)?DateUtil.convertDateToString(inspDate, DateUtil.DATE):"");
                cowDataList.add(inspLastDate);
                
                Data calfList = new Data();
                calfList.setKey(TransactionProperties.CALF_LIST);
                calfList.setCollectionValue(buildCalfValues(cow));
                cowDataList.add(calfList);
                
                cowObj.setData(cowDataList);
                cowObjectList.add(cowObj);
            });
        }
        cowCollection.setObject(cowObjectList);
        return cowCollection;
    }

    private Collection buildCalfValues(Cow cow) {

        Collection calfCollection = new Collection();
        List<com.sourcetrace.eses.txn.schema.Object> calfObjectList = new ArrayList<com.sourcetrace.eses.txn.schema.Object>();
        if (!ObjectUtil.isListEmpty(cow.getCalfs())) {
            cow.getCalfs().stream().filter(calf -> !ObjectUtil.isEmpty(calf)).forEach(calf -> {
                com.sourcetrace.eses.txn.schema.Object cowObj = new com.sourcetrace.eses.txn.schema.Object();
                List<Data> calfDataList = new ArrayList<Data>();

                Data calfId = new Data();
                calfId.setKey(TxnEnrollmentProperties.CALF_ID);
                calfId.setValue(calf.getCalfId());
                calfDataList.add(calfId);

                Data sex = new Data();
                sex.setKey(TxnEnrollmentProperties.SEX);
                sex.setValue(calf.getGender());
                calfDataList.add(sex);

                Data weight = new Data();
                weight.setKey(TxnEnrollmentProperties.WEIGTH);
                weight.setValue(String.valueOf(calf.getBirthWeight()));
                calfDataList.add(weight);

                Data calvingInterval = new Data();
                calvingInterval.setKey(TxnEnrollmentProperties.INTERVAL);
                calvingInterval.setValue(String.valueOf(calf.getCalvingIntvalDays()));
                calfDataList.add(calvingInterval);

                Data delivery = new Data();
                delivery.setKey(TxnEnrollmentProperties.DELIVERY);
                delivery.setValue(calf.getDeliveryProcess()==null || StringUtil.isEmpty(calf.getDeliveryProcess())?" ":calf.getDeliveryProcess());
                calfDataList.add(delivery);

                Data bulID = new Data();
                bulID.setKey(TxnEnrollmentProperties.BULL_ID);
                bulID.setValue(calf.getBullId());
                calfDataList.add(bulID);

                Data tentative = new Data();
                tentative.setKey(TxnEnrollmentProperties.SERVICE_DATE);
                tentative.setValue(!StringUtil.isEmpty(calf.getServiceDate())
                        ? sdf.format(calf.getServiceDate()) : "");
                calfDataList.add(tentative);

                Data calvingDate = new Data();
                calvingDate.setKey(TxnEnrollmentProperties.CALVING_DATE);
                calvingDate.setValue(!StringUtil.isEmpty(calf.getLastDateCalving())
                        ? sdf.format(calf.getLastDateCalving()) : "");
                calfDataList.add(calvingDate);

                cowObj.setData(calfDataList);
                calfObjectList.add(cowObj);
            });
        }
        calfCollection.setObject(calfObjectList);
        return calfCollection;
    }

    @Override
    public Map<?, ?> processVoid(Map<?, ?> reqData) {

        // TODO Auto-generated method stub
        return null;
    }
}
