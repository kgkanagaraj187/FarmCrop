package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.FarmerFeedbackEntity;
import com.sourcetrace.eses.inspect.agrocert.CertificateCategory;
import com.sourcetrace.eses.inspect.agrocert.Question;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TransactionTypeProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.agrocert.Answers;
import com.sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersQuestionAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersSectionAnswers;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class FarmerFeedback implements ITxnAdapter {
	private static final Logger LOGGER = Logger.getLogger(FarmerFeedback.class.getName());
	private static final DateFormat TXN_DATE_FORMAT = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
	
	private static final String FARMER = "farmer";
	private static final String FARM = "farm";
	private static final String REQUEST_DATA = "requestData";
	@Autowired
    private ICertificationService certificationService;
	private String latitude;
	private String longitude;
	
	private String flatitude;
    private String flongitude;
    
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IFarmCropsService farmCropsService;

	private int mode = 1;
	Map requestMap = new HashMap();
	
	private void validate(Object object, String errorCode) throws OfflineTransactionException, IOException {

		if (ObjectUtil.isEmpty(object) || ((object instanceof String) && (StringUtil.isEmpty(String.valueOf(object)))))
			throwError(errorCode);
	}
	
	private void throwError(String errorCode) throws OfflineTransactionException, IOException {

		if (mode == ESETxn.ONLINE_MODE)
			throw new SwitchException(errorCode);
		else
			throw new OfflineTransactionException(errorCode);
	}
	
	
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		
				
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		String servPointId = head.getServPointId();
		String txnMode = head.getMode();
		String branchId = head.getBranchId();
		String tenantId = head.getTenantId();

		LOGGER.info("AGENT ID : " + agentId);
		LOGGER.info("SERIAL NO : " + serialNo);
		LOGGER.info("TXN TYPE: " + TransactionTypeProperties.FARMER_FEEDBACK);
		

        //Farmer FeedBack		
		String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
		latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
		longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
		String q1 = (String) reqData.get("areaProd");
		String q2 = (String) reqData.get("estCotton");
		String q3 = (String) reqData.get("estcottonYld");
		String q4 = (String) reqData.get("ics");
		String answerDate = (String) reqData.get(TxnEnrollmentProperties.HAR_DATE);
		
		//PreHarvest
		String farmId = (String) reqData.get(TxnEnrollmentProperties.PREHARVEST_FARM);
		String areaProd = (String) reqData.get(TxnEnrollmentProperties.PREHARVEST_AREA);
		String cotton = (String) reqData.get(TxnEnrollmentProperties.EST_COTTON);
		String cottonYeild = (String) reqData.get(TxnEnrollmentProperties.EST_COTTON_YIELD);
		String season = (String) reqData.get(TxnEnrollmentProperties.PRE_SEASON_CODE);
		String village = (String) reqData.get(TxnEnrollmentProperties.PRE_VILLAGE);
		String ics= (String) reqData.get(TxnEnrollmentProperties.PRE_ICS);
		
		
		Date harvestDate = null;
		Farmer existingFarmer = null;
		Farm existingFarm = null;
		FarmCrops existingCrop = null;
		FarmIcsConversion existingIcs = null;
		
		
		try{
			
			if(head.getTenantId().equalsIgnoreCase("pgss")){
			// Basic Info Validations
			validate(farmerId,ITxnErrorCodes.EMPTY_FARMER_ID);
			//validate(farmCode, ITxnErrorCodes.EMPTY_FARM_CODE);
			
			existingFarmer = farmerService.findFarmerByFarmerId(farmerId);
			if (ObjectUtil.isEmpty(existingFarmer)){
				throwError(ITxnErrorCodes.INVALID_FARMER_ID);
			}else{
			FarmerFeedbackEntity fcpa = new FarmerFeedbackEntity();
					fcpa.setFarmerId(farmerId);
			//fcpa.setAnsweredDate(answeredDate);
			fcpa.setAnsweredDate(DateUtil.convertStringToDate(answerDate,
	                    DateUtil.TXN_DATE_TIME));
			
			fcpa.setQuestion1(q1);
			fcpa.setQuestion2(q2);
			fcpa.setQuestion3(q3);
			fcpa.setQuestion4(q4);
			fcpa.setVillage(existingFarmer.getVillage());
			fcpa.setWarehouse(existingFarmer.getSamithi());
			fcpa.setFarmerName(existingFarmer.getFirstName()+" "+existingFarmer.getLastName());
			certificationService.addFarmerFeedback(fcpa);
			}
			}else{
				
				//Validation
				validate(farmerId, SwitchErrorCodes.EMPTY_FARMER_ID);
				validate(farmId, SwitchErrorCodes.EMPTY_FARM);
				validate(areaProd, SwitchErrorCodes.EMPTY_AREA_PRODUCTION);
				validate(village, SwitchErrorCodes.EMPTY_VILLAGE_CODE);
					existingFarmer = farmerService.findFarmerByFarmerId(farmerId);
				if (ObjectUtil.isEmpty(existingFarmer)) {
					throwError(SwitchErrorCodes.FARMER_NOT_EXIST);
				}
						existingFarm = farmerService.findFarmByFarmCode(farmId);
				if (ObjectUtil.isEmpty(existingFarm)) {
					throwError(SwitchErrorCodes.FARM_NOT_EXIST);
				}
				existingCrop = farmerService.findFarmCropsByFarmCodeAndCategory(existingFarm.getId());
				if (ObjectUtil.isEmpty(existingCrop)) {
					throwError(SwitchErrorCodes.FARM_CROPS_DOES_NOT_EXIST);
				}
				
				existingIcs = farmerService.findFarmIcsConversionByFarmId(Long.valueOf(existingFarm.getId()));
				Set<FarmIcsConversion> icsSet = new HashSet<FarmIcsConversion>();
					FarmIcsConversion farmIcs = new FarmIcsConversion();
					farmIcs.setIcsType(ics);
					farmIcs.setStatus(mode);
					icsSet.add(farmIcs);
					existingFarm.setFarmICSConversion(icsSet);
				   existingFarm.getFarmDetailedInfo().setProposedPlantingArea(areaProd);
				
				existingCrop.setPreHarvestProd(areaProd);
				existingCrop.setCultiArea(areaProd);
				existingCrop.setEstimatedCotton(Double.valueOf(cotton));
				existingCrop.setEstimatedYield(Double.valueOf(cottonYeild));
				HarvestSeason harSeason = farmerService.findHarvestSeasonByCode(season);
				existingCrop.setCropSeason(harSeason);
				existingCrop.setRevisionNo(DateUtil.getRevisionNumber());
				
				/*if (!StringUtil.isEmpty(answerDate)) {
					try {
						harvestDate = DateUtil.convertStringToDate(answerDate, DateUtil.FARMER_DOB);
					} catch (Exception e) {
						harvestDate = new Date();
					}
				} else {
					harvestDate = new Date();
				}*/
				farmerService.editFarm(existingFarm);
				farmCropsService.editFarmCrops(existingCrop);
				//farmCrops.setFarm(farmerService.findFarmByfarmId(Long.parseLong(this.getFarmId())));
				farmerService.updateFarmerRevisionNo(existingCrop.getFarm().getFarmer().getId(),
						DateUtil.getRevisionNumber());
				
			}
			
		}catch (Exception e) {
			if (mode == ESETxn.ONLINE_MODE) {
				e.printStackTrace();
				throw new SwitchException(
						e instanceof SwitchException ? ((SwitchException) e).getCode() : ITxnErrorCodes.ERROR);
			} 
		}
		

		 Map resp = new HashMap();
	        return resp;
		
	}
	
	
	private FarmersQuestionAnswers buildQuestionAnswers(String q1, FarmersSectionAnswers fs) {
		 SortedSet<Answers> answers = new TreeSet<Answers>();
		FarmersQuestionAnswers fq = new FarmersQuestionAnswers();
		 String[] q1Arr = q1.split(":");
		 Question q = certificationService.findQuestionByCode(q1Arr[0].trim());
		 fq.setQuestionCode(q.getCode());
		 fq.setQuestionName(q.getName());
		 fq.setRegisteredQuestion("1");
		 fq.setLatitude(latitude);
		 fq.setLongtitude(longitude);
		 fq.setFarmersSectionAnswers(fs);
		 
		 Answers an = new Answers();
		 an.setAnswer(q1Arr[1]!=null && StringUtil.isEmail(q1Arr[1])? q1Arr[1].trim() : "");
		 answers.add(an);
		 an.setFarmersQuestionAnswers(fq);
		 fq.setAnswers(answers);
		return fq;
	}

	private Set<Coordinates> buildFarmLandArea(Collection collection) {

		Set<Coordinates> returnSet = null;
		if (!ObjectUtil.isEmpty(collection)) {
			returnSet = new LinkedHashSet<Coordinates>();
			List<com.sourcetrace.eses.txn.schema.Object> coordinatesList = collection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object coordinatesObj : coordinatesList) {
				Coordinates coordinates = new Coordinates();
				//coordinates.setFarm(farm);
				List<Data> coordinatesDataList = coordinatesObj.getData();

				for (Data data : coordinatesDataList) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.FARM_LAND_AREA_LATITUDE.equalsIgnoreCase(key)) {
						coordinates.setLatitude(value);
					}
					if (TxnEnrollmentProperties.FARM_LAND_AREA_LONGITUDE.equalsIgnoreCase(key)) {
						coordinates.setLongitude(value);
					}
					if (TxnEnrollmentProperties.FARM_LAND_AREA_ORDER_NO.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value)) {
							coordinates.setOrderNo(Long.valueOf(value));
						} else {
							coordinates.setOrderNo(0);
						}
					}
				}
				returnSet.add(coordinates);
			}
		}
		return returnSet;
	}
	
	

		@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {
		return null;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getFlatitude() {
		return flatitude;
	}

	public void setFlatitude(String flatitude) {
		this.flatitude = flatitude;
	}

	public String getFlongitude() {
		return flongitude;
	}

	public void setFlongitude(String flongitude) {
		this.flongitude = flongitude;
	}

	public IFarmerService getFarmerService() {
		return farmerService;
	}

	public void setFarmerService(IFarmerService farmerService) {
		this.farmerService = farmerService;
	}

	public IFarmCropsService getFarmCropsService() {
		return farmCropsService;
	}

	public void setFarmCropsService(IFarmCropsService farmCropsService) {
		this.farmCropsService = farmCropsService;
	}
	
	
}
