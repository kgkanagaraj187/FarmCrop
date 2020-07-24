package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Calf;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ResearchStation;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class CowEnrollment implements ITxnAdapter {

    private static final Logger LOGGER = Logger.getLogger(FarmerEnrollment.class.getName());
    private static final DateFormat TXN_DATE_FORMAT = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);

    private static final String RESEARCH_STATION_TYPE = "1";
    private static final String FARMER_TYPE = "0";
    private int mode = 1;

    @Autowired
    private IFarmerService farmerService;
    @Autowired
    private IAgentService agentService;
    @Override
    public Map<?, ?> process(Map<?, ?> reqData) {

        /** GET HEAD DATA **/
        Head head = (Head) reqData.get(TransactionProperties.HEAD);
        int statusCode = ESETxnStatus.SUCCESS.ordinal();
        String statusMsg = ESETxnStatus.SUCCESS.toString();

        mode = Integer.parseInt(head.getMode());

        /** Extract Key Values from Map */
        String season = (String) reqData.get(TxnEnrollmentProperties.CURRENT_SEASON_CODE);
        String elitType = (String) reqData.get(TxnEnrollmentProperties.ELITE_TYPE);
        String researchStationId = (String) reqData
                .get(TxnEnrollmentProperties.RESEARCH_STATION_ID);
        String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
        String farmCode = (String) reqData.get(TxnEnrollmentProperties.FARM_CODE);
        String cowId = (String) reqData.get(TxnEnrollmentProperties.COW_ID);
        DataHandler cowPhoto = (DataHandler) reqData.get(TxnEnrollmentProperties.PHOTO);
        String photoCaptureTime = (String) reqData
                .get(TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME);
        String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
        String longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
        String tagNumber = (String) reqData.get(TxnEnrollmentProperties.TAG_NUM);
        String dob = (String) reqData.get(TxnEnrollmentProperties.DOB);
        String sireId = (String) reqData.get(TxnEnrollmentProperties.SIRE_ID);
        String damId = (String) reqData.get(TxnEnrollmentProperties.DAM_ID);
        String cowGenoType = (String) reqData.get(TxnEnrollmentProperties.COW_GENO_TYPE);
        String history = (String) reqData.get(TxnEnrollmentProperties.HISTOY);
        String lactationNum = (String) reqData.get(TxnEnrollmentProperties.LACTATION_NUMBER);
        
     
            try {
                Cow cow = new Cow();
                if (elitType.equalsIgnoreCase(FARMER_TYPE)) {
                validate(farmerId, SwitchErrorCodes.EMPTY_FARMER_ID);
                Farmer farmer = farmerService.findFarmerByFarmerId(farmerId);
                validate(farmer, SwitchErrorCodes.FARMER_NOT_EXIST);
                validate(farmCode, SwitchErrorCodes.EMPTY_FARM_CODE);
                Farm farm = farmerService.findFarmByCode(farmCode);
                validate(farm, SwitchErrorCodes.FARM_NOT_EXIST);
                cow.setFarm(farm);
                
                byte[] photoContent = null;
                try {
                    farmer.setImageInfo(null);
                    if (cowPhoto != null) {
                        photoContent = IOUtils.toByteArray(cowPhoto.getInputStream());
                        if (photoContent != null) {
                            cow.setPhoto(photoContent);
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (elitType.equalsIgnoreCase(RESEARCH_STATION_TYPE)) {
               ResearchStation researchStation=farmerService.findResearchStationId(researchStationId);
              cow.setResearchStation(researchStation);
            }
                validate(cowId, SwitchErrorCodes.EMPTY_COW_ID);
                cow.setCowId(cowId);
    	        validate(elitType, ITxnErrorCodes.ELITE_TYPE);
    	        cow.setElitType(elitType);

                try {
                    Date photoCaptureDate = TXN_DATE_FORMAT.parse(photoCaptureTime);
                    cow.setPhotoCaptureTime(photoCaptureDate);
                } catch (Exception e) {
                    LOGGER.info(e.getMessage());
                }
                cow.setDob(DateUtil.convertStringToDate(dob, DateUtil.TXN_DATE_TIME));
                cow.setLatitude(latitude);
                cow.setLongitude(longitude);
                cow.setTagNo(tagNumber);
                cow.setSireId(sireId);
                cow.setDamId(damId);
                cow.setGenoType(cowGenoType);
               
                if (!StringUtil.isEmpty(history) && StringUtil.isDouble(history)) {
                    cow.setMilkFirstHundPerDay(Double.parseDouble(history));
                }
                if (!StringUtil.isEmpty(lactationNum)) {
                    cow.setLactationNo(lactationNum);
                }
                cow.setBranchId(!StringUtil.isEmpty(head.getBranchId())?head.getBranchId():"");
                Agent agent = agentService.findAgentByAgentId(head.getAgentId());
    	        if (!ObjectUtil.isEmpty(agent)) {
    	        	cow.setCreatedUserName(head.getAgentId());
    	        	cow.setLastUpdatedUserName(head.getAgentId());
    	        }
    	        

                cow.setCalfs(getCalfs(reqData));
                farmerService.save(cow);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info("ERROR IN COW ENROLLMENT:" + e.toString());
            }

       
        Map resp = new HashMap();
        return resp;
    }

    public Set<Calf> getCalfs(Map reqData) {

        Set<Calf> calfsSet = new LinkedHashSet<>();
        Collection calfCollection = (Collection) reqData.get(TxnEnrollmentProperties.CALF_LIST);
        if (!ObjectUtil.isEmpty(calfCollection)) {
            List<com.sourcetrace.eses.txn.schema.Object> farmObjects = calfCollection.getObject();
            farmObjects.stream().forEach(farmObject -> {
                List<Data> calfData = farmObject.getData();
                Calf calf = new Calf();
                calfData.stream().forEach(data -> {
                    Date tenativeServiceDate;
                    Date calvingDate;
                    String key = data.getKey();
                    String value = data.getValue();
                    if (key.equalsIgnoreCase(TxnEnrollmentProperties.SERVICE_DATE)) {
                        if (!StringUtil.isEmpty(value)) {
                            try {
                                tenativeServiceDate = DateUtil.convertStringToDate(value,
                                        DateUtil.TXN_DATE_TIME);
                                calf.setServiceDate(tenativeServiceDate);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (key.equalsIgnoreCase(TxnEnrollmentProperties.CALVING_DATE)) {
                        if (!StringUtil.isEmpty(value)) {
                            try {
                                calvingDate = DateUtil.convertStringToDate(value,
                                        DateUtil.TXN_DATE_TIME);
                                calf.setLastDateCalving(calvingDate);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (key.equalsIgnoreCase(TxnEnrollmentProperties.BULL_ID)) {
                        calf.setBullId(value);
                    } else if (key.equalsIgnoreCase(TxnEnrollmentProperties.CALF_ID)) {
                        calf.setCalfId(value);
                    } else if (key.equalsIgnoreCase(TxnEnrollmentProperties.SEX)) {
                            calf.setGender(value);
                    } else if (key.equalsIgnoreCase(TxnEnrollmentProperties.WEIGTH)) {
                        if (StringUtil.isDouble(value)) {
                            calf.setBirthWeight(Double.parseDouble(value));
                        }
                    } else if (key.equalsIgnoreCase(TxnEnrollmentProperties.INTERVAL)) {
                        if (StringUtil.isDouble(value)) {
                            calf.setCalvingIntvalDays(Double.parseDouble(value));
                        }
                    } else if (key.equalsIgnoreCase(TxnEnrollmentProperties.DELIVERY)) {
                        calf.setDeliveryProcess(value);
                    }

                });
                calfsSet.add(calf);

            });
        }
        return calfsSet;
    }

    @Override
    public Map<?, ?> processVoid(Map<?, ?> reqData) {

        return null;
    }

    private void validate(Object object, String errorCode)
            throws OfflineTransactionException, IOException {

        if (ObjectUtil.isEmpty(object)
                || ((object instanceof String) && (StringUtil.isEmpty(String.valueOf(object)))))
            throwError(errorCode);
    }

    private void throwError(String errorCode) throws OfflineTransactionException, IOException {

        if (mode == ESETxn.ONLINE_MODE)
            throw new SwitchException(errorCode);
        else
            throw new OfflineTransactionException(errorCode);
    }

}
