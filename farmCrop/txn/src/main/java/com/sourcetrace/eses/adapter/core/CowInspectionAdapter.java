package com.sourcetrace.eses.adapter.core;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.CowFeedType;
import com.sourcetrace.eses.order.entity.txn.CowInspection;
import com.sourcetrace.eses.order.entity.txn.CowInspectionImages;
import com.sourcetrace.eses.order.entity.txn.CowVaccination;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.ResearchStation;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
@Component
public class CowInspectionAdapter implements ITxnAdapter 
{
	private static final String EMPTY = "empty";
    private static final String INVALID = "invalid";
	@Autowired
    private IFarmerService farmerService;
	@Autowired
	private IAgentService agentService;
	
    private static final Logger LOGGER = Logger.getLogger(CowInspectionAdapter.class.getName());
   
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		
		 LOGGER.info("----- Cow Inspection Starts -----");
	        Head head = (Head) reqData.get(TransactionProperties.HEAD);
	        Map<String, String> errorCodes = new HashMap<String, String>();
	        CowInspection cowInspection=new CowInspection();
	        
	        String elitType = (String) reqData.get(TxnEnrollmentProperties.ELITE_TYPE);
	        if(!StringUtil.isEmpty(elitType) && elitType.equalsIgnoreCase(Cow.ELITE_RESEARCH))
	        {
	        	String researchStationId = (String) reqData.get(TxnEnrollmentProperties.RESEARCH_STATION_ID);
	        	validateEmptyString(researchStationId, ITxnErrorCodes.EMPTY_RESEARCH_STATION_ID);
	        	ResearchStation researchStation;
	        	  try {
	        		  researchStation = farmerService.findResearchStationId(researchStationId);
		          } catch (Exception e) {
		              e.printStackTrace();
		              LOGGER.info(e.getMessage());
		              throw new SwitchException(ITxnErrorCodes.INVALID_RESEARCH_STATION_ID);
		          }
		          validateEmptyObject(researchStation, ITxnErrorCodes.INVALID_RESEARCH_STATION_ID);
	        	cowInspection.setResearchStation(researchStation);
	        }
	        else
	        {
	            String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
	  	        validateEmptyString(farmerId, ITxnErrorCodes.EMPTY_FARMER_ID);
	  	        cowInspection.setFarmerId(farmerId);

	  	        String farmCode = (String) reqData.get(TxnEnrollmentProperties.FARM_CODE);
	  	        validateEmptyString(farmCode, SwitchErrorCodes.EMPTY_FARM_CODE);
	  	        
	  	      Farm farm = null;
	          try {
	              farm = farmerService.findFarmByCode(farmCode);
	          } catch (Exception e) {
	              e.printStackTrace();
	              LOGGER.info(e.getMessage());
	              throw new SwitchException(ITxnErrorCodes.INVALID_FARM);
	          }
	          validateEmptyObject(farm, ITxnErrorCodes.INVALID_FARM);
	           cowInspection.setFarm(farm);
	        }
	        validateEmptyString(elitType, ITxnErrorCodes.ELITE_TYPE);
	        cowInspection.setElitType(elitType);
	        String cowId = (String) reqData.get(TxnEnrollmentProperties.COW_ID);
	        validateEmptyString(cowId, ITxnErrorCodes.EMPTY_COW_ID);
	        cowInspection.setCowId(cowId);
	        String lastDate = (String) reqData.get(TxnEnrollmentProperties.INSPECTION_DATE);
	        errorCodes.put(EMPTY, ITxnErrorCodes.EMPTY_INSPECTION_DATE);
	        errorCodes.put(INVALID, ITxnErrorCodes.INVALID_INSPECTION_DATE);
	        cowInspection.setLastInspDate(validateDate(lastDate, DateUtil.TXN_TIME_FORMAT,true, errorCodes));

	        String currentDate = (String) reqData.get(TxnEnrollmentProperties.CURRENT_DATE);
	        errorCodes.put(EMPTY, ITxnErrorCodes.EMPTY_INSPECTION_DATE);
	        errorCodes.put(INVALID, ITxnErrorCodes.INVALID_INSPECTION_DATE);
	        cowInspection.setCurrentInspDate(validateDate(currentDate, DateUtil.TXN_TIME_FORMAT,true, errorCodes));
	        String intervalDays = (String) reqData.get(TxnEnrollmentProperties.INTERVAL_DAYS);
	        String medicineName = (String) reqData.get(TxnEnrollmentProperties.MEDICINE_NAME);
	        String milkMrngDay = (String) reqData.get(TxnEnrollmentProperties.MILK_MORNG_DAY);
	        String inspectionNo = (String) reqData.get(TxnEnrollmentProperties.INSPECTION_NO);
	        String milkEvngDay = (String) reqData.get(TxnEnrollmentProperties.MILK_EVNG_DAY);
	        String totMilkDay = (String) reqData.get(TxnEnrollmentProperties.TOT_MILK_DAY);
	        String totMilkPeriod = (String) reqData.get(TxnEnrollmentProperties.TOT_MILK_PERIOD);
	        String feedType = (String) reqData.get(TxnEnrollmentProperties.FEED_TYPE);
	        String amtFeedPeriod = (String) reqData.get(TxnEnrollmentProperties.AMT_FEED_PERIOD);
	        String infesPara = (String) reqData.get(TxnEnrollmentProperties.INFES_PARASITES);
	        String diseaseNotice = (String) reqData.get(TxnEnrollmentProperties.DISEASE_NOTICE);
	        String diseaseName = (String) reqData.get(TxnEnrollmentProperties.DISEASE_NAME);
	        String diseaseServices = (String) reqData.get(TxnEnrollmentProperties.DISEASE_SERVICES);
	        
	        String diseaseMedici = (String) reqData.get(TxnEnrollmentProperties.DISEASE_MEDICINE);
	        String healthProbl = (String) reqData.get(TxnEnrollmentProperties.HEALTH_PROBLEM);
	        String correctiveMeas = (String) reqData.get(TxnEnrollmentProperties.CORRECTIVE_MEASURE);
	        String vaccinationPlace = (String) reqData.get(TxnEnrollmentProperties.VACCINATION_PLACE);
	        String vaccinationDate = (String) reqData.get(TxnEnrollmentProperties.VACCINATION_DATE);
	        String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
	        String longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
	    //    String vaccinationName = (String) reqData.get(TxnEnrollmentProperties.VACCINATION_NAME);
	        String comments = (String) reqData.get(TxnEnrollmentProperties.COMMENTS);
	        String healthServi = (String) reqData.get(TxnEnrollmentProperties.HEALTH_SERVICES);
	        String healthMedici = (String) reqData.get(TxnEnrollmentProperties.HEALTH_MEDICINES);
	        String deworPlace = (String) reqData.get(TxnEnrollmentProperties.DEWORNMING_PLACE);
	        String isMilkingCow=(String)reqData.get(TxnEnrollmentProperties.IS_MILKING_COW);  
	        cowInspection.setIntervalDays(!StringUtil.isEmpty(intervalDays)?Integer.valueOf(intervalDays):0);
	        cowInspection.setMilkMorngPerday(!StringUtil.isEmpty(milkMrngDay)?Double.valueOf(milkMrngDay):0.0);
	        cowInspection.setMilkEvngPerday(!StringUtil.isEmpty(milkEvngDay)?Double.valueOf(milkEvngDay):0.0);
	        cowInspection.setTotalMilkPerDay(!StringUtil.isEmpty(totMilkDay)?Double.valueOf(totMilkDay):0.0);
	        cowInspection.setTotalMilkPerPeriod(!StringUtil.isEmpty(totMilkPeriod)?Double.valueOf(totMilkPeriod):0.0);
	        cowInspection.setInspectionNo(!StringUtil.isEmpty(milkMrngDay)?inspectionNo:"");
	        String audioFile = (String) reqData.get(TxnEnrollmentProperties.AUDIO);
	        byte[] voiceDataContent = null;
	        try {
	            if (audioFile != null && audioFile.length() > 0) {
	                voiceDataContent = Base64.decodeBase64(audioFile.getBytes());
	                cowInspection.setAudio(voiceDataContent);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            LOGGER.info(e.getMessage());
	            throw new SwitchException(ITxnErrorCodes.ERROR_WHILE_PROCESSING_COW_VOICE);
	        }
	        cowInspection.setCowVaccinations(getCowVaccination(vaccinationDate));
	        cowInspection.setDeworwingPlace(!StringUtil.isEmpty(deworPlace)?deworPlace:"");
	        cowInspection.setMedicineName(!StringUtil.isEmpty(medicineName)?medicineName:"");
	        cowInspection.setCowFeedTypes(getCowFeedType(amtFeedPeriod));
	        cowInspection.setFeedType(!StringUtil.isEmpty(feedType)?feedType:"");
	        cowInspection.setInfestationPara(!StringUtil.isEmpty(infesPara)?infesPara:"");
	        cowInspection.setDiseaseNoticed(!StringUtil.isEmpty(diseaseNotice)?diseaseNotice:"");
	        cowInspection.setDiseaseName(!StringUtil.isEmpty(diseaseName)?diseaseName:"");
	        cowInspection.setDiseaseServices(!StringUtil.isEmpty(diseaseServices)?diseaseServices:"");
	        cowInspection.setDiseaseMedicines(!StringUtil.isEmpty(diseaseMedici)?diseaseMedici:"");
	        cowInspection.setHealthProblem(!StringUtil.isEmpty(healthProbl)?healthProbl:"");
	        cowInspection.setCorrectivMeasure(!StringUtil.isEmpty(correctiveMeas)?correctiveMeas:"");
	        cowInspection.setVaccinationPlace(!StringUtil.isEmpty(vaccinationPlace)?vaccinationPlace:"");
	       // cowInspection.setVaccinationDate(!StringUtil.isEmpty(vaccinationDate)?DateUtil.convertStringToDate(vaccinationDate, DateUtil.TXN_TIME_FORMAT):null);
	       // cowInspection.setVaccinationName(!StringUtil.isEmpty(vaccinationName)?vaccinationName:"");
	        cowInspection.setComments(!StringUtil.isEmpty(comments)?comments:"");
	        cowInspection.setHealthServices(!StringUtil.isEmpty(healthServi)?healthServi:"");
	        cowInspection.setHealthMedicines(!StringUtil.isEmpty(healthMedici)?healthMedici:"");
	        cowInspection.setInspectionImages(getInspectionImages(reqData));
	        cowInspection.setIsMilkingCow(isMilkingCow);
	        cowInspection.setLatitude(latitude);
	        cowInspection.setLongitude(longitude);
	        Agent agent = agentService.findAgentByAgentId(head.getAgentId());
	        if (!ObjectUtil.isEmpty(agent)) {
	        	cowInspection.setCreatedUserName(head.getAgentId());
	        	cowInspection.setLastUpdatedUserName(head.getAgentId());
	        }
	        cowInspection.setBranchId(!StringUtil.isEmpty(head.getBranchId())?head.getBranchId():"");
	        if(!ObjectUtil.isEmpty(cowInspection.getFarm()))
	        {
	        	farmerService.updateFarmerRevisionNo(cowInspection.getFarm().getFarmer().getId(), DateUtil.getRevisionNumber());
	        }
	        else if(!ObjectUtil.isEmpty(cowInspection.getResearchStation()))
	        {
	        	
	        	farmerService.updateResearchStatRevisionNo(cowInspection.getResearchStation().getId(),DateUtil.getRevisionNumber());
	        	
	        }
	        farmerService.addCowInspection(cowInspection);

		return null;
	}
	
	
	private Set<CowFeedType> getCowFeedType(String feedValues) {
		 Set<CowFeedType> cowFeedTypeSet = new HashSet<CowFeedType>();
		//Set<CultivationDetail> cultivationDetailsSet = new HashSet<CultivationDetail>();
		if(!StringUtil.isEmpty(feedValues))
		{
			
				List<String> costSeperator = Arrays.asList(feedValues.split("\\,"));
					for (String seperatedString : costSeperator) {
						CowFeedType CowSeedType = new CowFeedType();
						String[] values = seperatedString.split(":");
						CowSeedType.setFeedType(values[0]);
						CowSeedType.setAmount(Double.valueOf(values[1]));
						cowFeedTypeSet.add(CowSeedType);
					}
				}
		return cowFeedTypeSet;
		}
	
	
	private Set<CowVaccination> getCowVaccination(String vaccination) {
		 Set<CowVaccination> cowVaccinSet = new HashSet<CowVaccination>();
		//Set<CultivationDetail> cultivationDetailsSet = new HashSet<CultivationDetail>();
		if(!StringUtil.isEmpty(vaccination))
		{
			
				List<String> costSeperator = Arrays.asList(vaccination.split("\\,"));
					for (String seperatedString : costSeperator) {
						CowVaccination cowVaccination = new CowVaccination();
						String[] values = seperatedString.split(":");
						cowVaccination.setName(values[0]);
						cowVaccination.setDate(DateUtil.convertStringToDate(values[1], DateUtil.FARMER_DOB));
						cowVaccinSet.add(cowVaccination);
					}
				}
	
		return cowVaccinSet;
		}
		
		
	
	
	 public Set<CowInspectionImages> getInspectionImages(Map reqData) {
		    Set<CowInspectionImages> inspectionImageSet = new HashSet<CowInspectionImages>();
	        Collection photoCollection = (Collection) reqData
	                .get(TxnEnrollmentProperties.PHOTO_LIST);
	        if (!CollectionUtil.isCollectionEmpty(photoCollection)) {
	        
	            List<com.sourcetrace.eses.txn.schema.Object> photoDataObject = photoCollection
	                    .getObject();

	            for (com.sourcetrace.eses.txn.schema.Object object : photoDataObject) {
	                List<Data> photoDatas = object.getData();
	                CowInspectionImages inspectionImage = new CowInspectionImages();
	                byte[] imageContent = null;

	                for (Data data : photoDatas) {
	                    String key = data.getKey();
	                    String value = (String) data.getValue();
	                    
	                    
	                    if (TxnEnrollmentProperties.PHOTO.equalsIgnoreCase(key)) {
	                        DataHandler photo = data.getBinaryValue();
	                        try {
	                            if (photo != null && photo.getInputStream().available() > 0) {
	                                imageContent = IOUtils.toByteArray(photo.getInputStream());
	                                inspectionImage.setPhoto(imageContent);
	                            }
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                            throw new SwitchException(ITxnErrorCodes.ERR0R_WHILE_PROCESSING);
	                        }
	                    }

	                    // Photo Capture Time
	                    if (TxnEnrollmentProperties.PHOTO_CAPTURE_TIME.equalsIgnoreCase(key)) {
	                        if (!StringUtil.isEmpty(value) && !value.equals("0")) {
	                            try {
	                                Date photoCaptureDate = DateUtil.convertStringToDate(value,
	                                        DateUtil.TXN_TIME_FORMAT);
	                                inspectionImage.setCaptureTime(photoCaptureDate);
	                            } catch (Exception e) {
	                                e.printStackTrace();
	                                throw new SwitchException(ITxnErrorCodes.DATE_CONVERSION_ERROR);
	                            }
	                        }
	                    }

	                    // Photo Capture Latitude
	                    if (TxnEnrollmentProperties.PHOTO_LATITUDE.equalsIgnoreCase(key))
	                    	if(!StringUtil.isEmpty(inspectionImage.getLatitude())){
	                            inspectionImage.setLatitude(value); 
	                            }else{
	                            	inspectionImage.setLatitude("0");
	                            }
	                     

	                    // Photo Capture Longitude
	                    if (TxnEnrollmentProperties.PHOTO_LONGITUDE.equalsIgnoreCase(key))
	                    	if(!StringUtil.isEmpty(inspectionImage.getLongitude())){
	                            inspectionImage.setLongitude(value);
	                        	}else{
	                        		inspectionImage.setLongitude("0");
	                        	}
	                }
	                
	                inspectionImageSet.add(inspectionImage);
	        }
	       
	    }
	        return inspectionImageSet;
	 }
	
	 private void validateEmptyString(String input, String errorCode) {

	        if (StringUtil.isEmpty(input))
	            throw new SwitchException(errorCode);
	    }
	 
	 
	    private Date validateDate(String dateString, String format, boolean isMandatory,
	            Map<String, String> errorCodes) {

	        if (isMandatory) {
	            if (StringUtil.isEmpty(dateString))
	                throw new SwitchException(errorCodes.get(EMPTY));
	        } else {
	            if (StringUtil.isEmpty(dateString))
	                return null;
	        }
	        try {
	            return DateUtil.convertStringToDate(dateString.trim(), format);
	        } catch (Exception e) {
	            e.printStackTrace();
	            LOGGER.info(e.getMessage());
	            throw new SwitchException(errorCodes.get(INVALID));
	        }
	    }


	    /**
	     * Validate empty object.
	     * @param input 
	     * @param errorCode 
	     */
	    private void validateEmptyObject(Object input, String errorCode) {

	        if (ObjectUtil.isEmpty(input))
	            throw new SwitchException(errorCode);
	    }

	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		// TODO Auto-generated method stub
		return null;
	}

}
