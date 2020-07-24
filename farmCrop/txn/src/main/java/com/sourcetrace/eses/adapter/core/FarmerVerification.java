/*
 * FarmerEnrollment.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICardService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.CoordinatesMap;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;

@Component
public class FarmerVerification implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(FarmerVerification.class.getName());
	private static final DateFormat TXN_DATE_FORMAT = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
	private static final String FARM_CROP_COLLECTION = "farmCropCollection";
	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IAgentService agentService;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private ICardService cardService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private ICertificationService certificationService;
	@Autowired
	private IClientService clientService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private IFarmCropsService farmCropsService;
	private int mode = 1;
	private static final String REQUEST_DATA = "requestData";
	private static final String FARMER = "farmer";
	private static final String FARM = "farm";
	private static String BRANCH_ID="";
	private String latitude;
	private String longitude;
	private String flatitude;
    private String flongitude;
    private static String TENANT_ID="";
    private static String SEASON = "";
	private FarmCrops finalFarm;
	private String cropEditStatus;
	
	/**
	 * Validate.
	 * 
	 * @param object
	 *            the object
	 * @param errorCode
	 *            the error code
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void validate(Object object, String errorCode) throws OfflineTransactionException, IOException {

		if (ObjectUtil.isEmpty(object) || ((object instanceof String) && (StringUtil.isEmpty(String.valueOf(object)))))
			throwError(errorCode);
	}

	/**
	 * Throw error.
	 * 
	 * @param errorCode
	 *            the error code
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void throwError(String errorCode) throws OfflineTransactionException, IOException {

		if (mode == ESETxn.ONLINE_MODE)
			throw new SwitchException(errorCode);
		else
			throw new OfflineTransactionException(errorCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {

		/** GET HEAD DATA **/
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String agentId = null;
		String serialNo = null;
		Agent agent = null;
		Device device = null;
		String plotCaptureTime = null;

		mode = Integer.parseInt(head.getMode());
		int statusCode = ESETxnStatus.SUCCESS.ordinal();
		String statusMsg = ESETxnStatus.SUCCESS.toString();

		if (!ObjectUtil.isEmpty(head)) {
			LOGGER.info("Agent ID : " + head.getAgentId());
			LOGGER.info("Device ID : " + head.getSerialNo());
			agentId = head.getAgentId();
			serialNo = head.getSerialNo();
			plotCaptureTime=head.getTxnTime();
		}
		/** INITAIALIZING COMMON DATA **/
		// Farmer farmer = new Farmer();
		// Map requestMap = new HashMap();
		// requestMap.put(REQUEST_DATA, reqData);
		// requestMap.put(FARMER, farmer);
		/** GET REQUEST DATA **/
		String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
		String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
        String longitude =(String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
		setLatitude(latitude);
		setLongitude(longitude);
		
		DataHandler photoDataHandler = (DataHandler) reqData.get(TxnEnrollmentProperties.PHOTO);
		String photoCaptureTime = (String) reqData.get(TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME);

		String farmCode = (String) reqData.get(TxnEnrollmentProperties.FARM_CODE);
		String farmPhotoCaptureTime = (String) reqData.get(TxnEnrollmentProperties.FARM_PHOTO_CAPTURING_TIME);
		DataHandler farmPhotoDataHandler = (DataHandler) reqData.get(TxnEnrollmentProperties.FARM_PHOTO);
		String farmLatitude = (String) reqData.get(TxnEnrollmentProperties.FARM_LATITUDE);
		String farmLongitude = (String) reqData.get(TxnEnrollmentProperties.FARM_LONGITUDE);
setFlatitude(farmLatitude);
setFlongitude(farmLongitude);
		Collection coordinatesCollection = (Collection) reqData.get(TxnEnrollmentProperties.FARM_LAND_AREA_GPS);
		
		String proposedPlantingArea = (String) reqData.get(TxnEnrollmentProperties.LAND_IN_PRODUCTION);
		String totalLandHoldingArea = (String) reqData.get(TxnEnrollmentProperties.LAND_NOT_IN_PRODUCTION);
		
		String lhInsurance = (String) reqData.get(TxnEnrollmentProperties.LH_INSURANCE);
		String cInsurance = (String) reqData.get(TxnEnrollmentProperties.CRP_INSU);
        String bankAcc =(String) reqData.get(TxnEnrollmentProperties.BANK_ACC);
        String loanTakenYr =(String) reqData.get(TxnEnrollmentProperties.LOAN_TAKEN_LENDER);
        TENANT_ID = head.getTenantId();
        String season = (String) reqData.get(TxnEnrollmentProperties.PRE_SEASON_CODE);
        SEASON = season;
				
		byte[] photoContent = null;
		byte[] farmPhotoContent = null;
		  


		ImageInfo imageInfo = null;
		Farmer existingFarmer = null;
		Farm existingFarm = null;
		try {
			// Basic Info Validations
			validate(agentId, ITxnErrorCodes.AGENT_ID_EMPTY);
			agent = agentService.findAgentByAgentId(agentId);
			validate(agent, ITxnErrorCodes.INVALID_AGENT);
			validate(serialNo, ITxnErrorCodes.EMPTY_SERIAL_NO);
			device = deviceService.findDeviceBySerialNumber(serialNo);
			validate(device, ITxnErrorCodes.INVALID_DEVICE);

			validate(farmerId, ITxnErrorCodes.EMPTY_FARMER_ID);
			validate(farmCode, ITxnErrorCodes.EMPTY_FARM_CODE);
			existingFarmer = farmerService.findFarmerByFarmerId(farmerId);
			if (ObjectUtil.isEmpty(existingFarmer))
				throwError(ITxnErrorCodes.INVALID_FARMER_ID);
			existingFarmer.setIsVerified(1);
			BRANCH_ID = existingFarmer.getBranchId();
			
			if (!StringUtil.isEmpty(head.getTxnTime())) {
				try {
					Date trxnDate = TXN_DATE_FORMAT.parse(head.getTxnTime());
					existingFarmer.setVerifiedDate(trxnDate);
				} catch (Exception e) {
					LOGGER.info(e.getMessage());
				}
			}
			existingFarmer.setVerifiedAgentId(agent.getProfileId());
			existingFarmer.setVerifiedAgentName(agent.getPersonalInfo().getAgentName());
			// farmerService.editFarmer(existingFarmer);
			existingFarm = farmerService.findFarmByFarmCode(farmCode);
			if (ObjectUtil.isEmpty(existingFarm))
				//throwError(ITxnErrorCodes.INVALID_FARM_ID);
			throw new SwitchException(ITxnErrorCodes.INVALID_FARMER_ID);
			existingFarm.setIsVerified(1);

			if (!StringUtil.isEmpty(head.getTxnTime())) {
				try {
					Date trxnDate = TXN_DATE_FORMAT.parse(head.getTxnTime());
					existingFarm.setVerifiedDate(trxnDate);
				} catch (Exception e) {
					LOGGER.info(e.getMessage());
				}
			}
			existingFarm.setVerifiedAgentId(agent.getProfileId());
			existingFarm.setVerifiedAgentName(agent.getPersonalInfo().getAgentName());

			/*FarmCrops farmCrops = farmerService.findFarmCropsByFarmCode(existingFarm.getId());
			farmCrops.setLatitude(farmLatitude);
			farmCrops.setLongitude(farmLongitude);*/
			// Updating farmer photo
			try {
				if (photoDataHandler != null)
					photoContent = IOUtils.toByteArray(photoDataHandler.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (!StringUtil.isEmpty(photoContent) && photoContent.length > 0) {
				if (!ObjectUtil.isEmpty(existingFarmer.getImageInfo())) {
					imageInfo = existingFarmer.getImageInfo();
					Image photo = imageInfo.getPhoto();
					photo.setImage(photoContent);
					imageInfo.setPhoto(photo);
					farmerService.updateImageInfo(imageInfo);
				} else {
					imageInfo = new ImageInfo();
					if (photoContent != null) {
						Image photo = new Image();
						photo.setImage(photoContent);
						photo.setImageId(farmerId + "-FP");
						imageInfo.setPhoto(photo);
					}

				}
			}
			
		
			// Updating farmer photo capturing time
			if (!StringUtil.isEmpty(photoCaptureTime)) {
				try {
					Date photoCaptureDate = TXN_DATE_FORMAT.parse(photoCaptureTime);
					existingFarmer.setPhotoCaptureTime(photoCaptureDate);
				} catch (Exception e) {
					LOGGER.info(e.getMessage());
				}
			}
			// Updating farmer photo capture latitude and longitude
			if (!StringUtil.isEmpty(getLatitude()))
				existingFarmer.setLatitude(getLatitude());
			if (!StringUtil.isEmpty(getLongitude()))
				existingFarmer.setLongitude(getLongitude());

			// Updating farm photo
			try {
				if (farmPhotoDataHandler != null)
					farmPhotoContent = IOUtils.toByteArray(farmPhotoDataHandler.getInputStream());
				if (farmPhotoContent != null) {
					if (farmPhotoContent.length > 0)
						existingFarm.setPhoto(farmPhotoContent);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Updating Farm photo capturing time
			if (!StringUtil.isEmpty(farmPhotoCaptureTime)) {
				try {
					Date farmphotoCaptureDate = TXN_DATE_FORMAT.parse(farmPhotoCaptureTime);
					existingFarm.setPhotoCaptureTime(farmphotoCaptureDate);
				} catch (Exception e) {
					LOGGER.info(e.getMessage());
				}
			}
			if (!StringUtil.isEmpty(farmLatitude))
				existingFarm.setLatitude(farmLatitude);
			if (!StringUtil.isEmpty(farmLongitude))
				existingFarm.setLongitude(farmLongitude);
				
			
			if (!StringUtil.isEmpty(totalLandHoldingArea))
				existingFarm.getFarmDetailedInfo().setTotalLandHolding(totalLandHoldingArea);
			if (!StringUtil.isEmpty(proposedPlantingArea))
				existingFarm.getFarmDetailedInfo().setProposedPlantingArea(proposedPlantingArea);
			
			existingFarm.setPlotCapturingTime(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));

			// Audio File
			String voiceData = (String) reqData.get(TxnEnrollmentProperties.FARMER_AUDIO);
			byte[] voiceDataContent = null;
			try {
				if (voiceData != null && voiceData.length() > 0) {
					voiceDataContent = Base64.decodeBase64(voiceData.getBytes());
					existingFarm.setAudio(voiceDataContent);
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info(e.getMessage());
				throw new SwitchException(ITxnErrorCodes.ERROR_WHILE_PROCESSING_FARMER_VOICE);
			}

			if (!ObjectUtil.isEmpty(coordinatesCollection)) {
				Set<Coordinates> coordinatess = new LinkedHashSet<Coordinates>();
				List<com.sourcetrace.eses.txn.schema.Object> coordinateObjects = coordinatesCollection.getObject();

				for (com.sourcetrace.eses.txn.schema.Object coordinateObject : coordinateObjects) {
					Coordinates coordinates = new Coordinates();
					coordinates.setFarm(existingFarm);
					List<Data> coordinatesDataList = coordinateObject.getData();

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
					coordinatess.add(coordinates);
				}
				if (!ObjectUtil.isListEmpty(coordinatess)){
					CoordinatesMap coordinatesMap=new CoordinatesMap();
					//existingFarm.setCoordinates(coordinatess);
					coordinatesMap.setFarmCoordinates(coordinatess);
					coordinatesMap.setAgentId(agentId);
					coordinatesMap.setArea(existingFarm.getFarmDetailedInfo().getTotalLandHolding());
					coordinatesMap.setDate(	existingFarm.getPlotCapturingTime());
					coordinatesMap.setFarm(existingFarm);
					coordinatesMap.setMidLatitude(existingFarm.getLongitude());
					coordinatesMap.setMidLongitude(existingFarm.getLongitude());
					coordinatesMap.setStatus(CoordinatesMap.Status.ACTIVE.ordinal());
					if(existingFarm.getCoordinatesMap()!=null && !ObjectUtil.isListEmpty(existingFarm.getCoordinatesMap())){
					existingFarm.getCoordinatesMap().stream().forEach(co->{
						co.setStatus(CoordinatesMap.Status.INACTIVE.ordinal());
					});
					existingFarm.getCoordinatesMap().add(coordinatesMap);
					}
					else{
						Set<CoordinatesMap> coMap=new LinkedHashSet<>();
						coMap.add(coordinatesMap);
						existingFarm.setCoordinatesMap(coMap);
						
					}
					existingFarm.setActiveCoordinates(coordinatesMap);
				}
					
			}
			Set<FarmCrops> newFarmrops = getFarmCrops(reqData, existingFarm,plotCaptureTime,agentId);
			
			if(newFarmrops.size()>0){
			/*if(existingFarm.getFarmCrops()!=null && existingFarm.getFarmCrops().size()>0){
				Set<FarmCrops> newSet = existingFarm.getFarmCrops();
				newSet.addAll(newFarmrops);
				existingFarm.setFarmCrops(newSet);
			}else{*/
				
				existingFarm.setFarmCrops(newFarmrops);
			
				
			//}
			}
			//existingFarm.setFarmCrops(getFarmCrops(reqData, existingFarm));

		} catch (Exception e) {
			if (mode == ESETxn.ONLINE_MODE) {
				e.printStackTrace();
				throw new SwitchException(
						e instanceof SwitchException ? ((SwitchException) e).getCode() : ITxnErrorCodes.ERROR);
			} else {
				statusCode = ESETxnStatus.ERROR.ordinal();
				statusMsg = e instanceof OfflineTransactionException ? ((OfflineTransactionException) e).getError()
						: e.getMessage().substring(0, e.getMessage().length() > 40 ? 40 : e.getMessage().length());
			}
		}
		if (!ObjectUtil.isEmpty(existingFarmer)) {
			//existingFarmer.setStatusCode(statusCode);
			//existingFarmer.setStatusMsg(statusMsg);
			farmerService.editFarmer(existingFarmer);
			if (ESETxnStatus.SUCCESS.ordinal() == statusCode && !ObjectUtil.isEmpty(imageInfo)) {
				if (existingFarmer.getId() > 0 && imageInfo != null && !ObjectUtil.isEmpty(imageInfo)
						&& imageInfo.getPhoto() != null) {
					imageInfo.getPhoto().setImageId(existingFarmer.getId() + "-FP");
					farmerService.addImageInfo(imageInfo);
					farmerService.updateFarmerImageInfo(existingFarmer.getId(), imageInfo.getId());
				}
			}

			
			if (!ObjectUtil.isEmpty(existingFarm)) {
				existingFarm.setRevisionNo(DateUtil.getRevisionNumber());
				farmerService.editFarm(existingFarm);
			}
			farmerService.removeUnmappedFarmCropObject();
		}
		Collection dynamicFields = (reqData.containsKey(TransactionProperties.DYNAMIC_FIELD))
				? (Collection) reqData.get(TransactionProperties.DYNAMIC_FIELD) : null;

		Collection dynamicList = (reqData.containsKey(TransactionProperties.DYNAMIC_FIELD_LIST))
				? (Collection) reqData.get(TransactionProperties.DYNAMIC_FIELD_LIST) : null;
		Collection photoList = (reqData.containsKey(TransactionProperties.DYNAMIC_IMAGE_LIST))
				? (Collection) reqData.get(TransactionProperties.DYNAMIC_IMAGE_LIST) : null;

		if ((!CollectionUtil.isCollectionEmpty(dynamicFields) || !CollectionUtil.isCollectionEmpty(dynamicList)
				|| !CollectionUtil.isCollectionEmpty(photoList)) && (finalFarm!=null)) {
			FarmerDynamicData farmerDynamicData = new FarmerDynamicData();
			List<DynamicFeildMenuConfig> dmList = farmerService.findDynamicMenusByMType("357");
			if (dmList != null && !dmList.isEmpty()) {
				farmerDynamicData = getFarmerDynamicFields(dynamicFields, dynamicList, photoList, head.getTenantId(),
						dmList.get(0), finalFarm.getId());
				farmerDynamicData.setTxnUniqueId(Long.valueOf(head.getMsgNo()));
				farmerDynamicData.setReferenceId(String.valueOf(finalFarm.getId()));

				Date txnDateVal = null;
				try {
					if (!StringUtil.isEmpty(head.getTxnTime())) {
						txnDateVal = DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME);
						farmerDynamicData.setDate(txnDateVal);
					}
				} catch (Exception e) {
					farmerDynamicData.setDate(new Date(0));
				}
				farmerDynamicData.setLatitude(StringUtil.isEmpty(latitude) ? null : latitude);
				farmerDynamicData.setLongitude(StringUtil.isEmpty(longitude) ? null : longitude);
				farmerDynamicData.setCreatedDate(
						DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
				farmerDynamicData.setCreatedUser(head.getAgentId());
				farmerDynamicData.setStatus("0");
				farmerDynamicData.setBranch(existingFarmer.getBranchId());
				farmerDynamicData.setEntityId(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM_CROPS.ordinal()));
				farmerDynamicData.setTxnType(head.getTxnType());
				farmerDynamicData.setSeason(existingFarmer.getSeasonCode());
				LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap = new LinkedHashMap<>();
				dmList.get(0).getDynamicFieldConfigs().stream().forEach(section -> {
					section.getField().setfOrder(section.getOrder());
					fieldConfigMap.put(section.getField().getCode(), section.getField());
				});
				farmerService.saveOrUpdate(farmerDynamicData, new HashMap<>(), fieldConfigMap);
				if( fieldConfigMap.values().stream().anyMatch(p -> Arrays.asList("4").contains(p.getIsMobileAvail()) && p.getFormula() != null
						&& !StringUtil.isEmpty(p.getFormula()))){
					farmerService.processCustomisedFormula(farmerDynamicData, fieldConfigMap);
		      }
				farmerService.deleteChildObjects(farmerDynamicData.getTxnType());
			}

		}
		Map resp = new HashMap();
		return resp;
	}

	/**
	 * Gets the farm crops.
	 * 
	 * @param farmMap
	 * @param existingFarm
	 * @return the farm crops
	 * @throws OfflineTransactionException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public Set<FarmCrops> getFarmCrops(Map farmMap, Farm existingFarm,String plotCaptureTime,String agentId) throws OfflineTransactionException, IOException {

		Set<FarmCrops> farmCropDataSet = new HashSet<FarmCrops>();

		Collection cropCollection = (Collection) farmMap.get(TxnEnrollmentProperties.FARM_CROPS_LIST);
		if (!ObjectUtil.isEmpty(cropCollection)) {
			Map<String, FarmCrops> farmCropMap = getFarmCropsDatas(existingFarm);

			List<com.sourcetrace.eses.txn.schema.Object> cropObjectList = cropCollection.getObject();
			int startIndex = 1;
			for (com.sourcetrace.eses.txn.schema.Object cropObject : cropObjectList) {
				List<Data> cropDataList = cropObject.getData();

				ProcurementVariety procurementVariety = null;
				String productionYear = null;
				HarvestSeason cropSeason = new HarvestSeason();
				int cropCategory = 0;
				//int seedSource = 0;
				String seedSource ="";
				String type ="";
				Double estyld = 0.00;
				Double seedCost = 0.0;
				Double seedUsed = 0.0;
				String stapleLnth = "";
				String stbLenght = null;
				 Date sowingDate = null;
	             Date estimatedHarvestDate = null;
	             String otherTypeVal = "";
	             String cultType ="";
	             String culArea = "";
	             String seedTrest = "";
	             String riskAss = "";
	             String otherSeedTrest = "";
	             String riskBuff = "";
	             String prodArea = "";
	             String seedCt = "";
	             String lintCt = "";
	             String actualSeed = "";
	             String interType = "";
	             String interAcres = "";
	             String cropHarInter = "";
	             String interGrossIncom = "";
	             String noOfTrees = "";
	             String prodTrees = "";
	             String affTrees="";
	             String yearOfPlant="";
	             String varietyCode="";
	         	 String cropEditStatus="";

	              Set<FarmCropsCoordinates> farmCropsCoordinateSet=new LinkedHashSet<FarmCropsCoordinates>();
	              Collection collection=new Collection();
	             
				for (Data cropData : cropDataList) {

					String cropKey = cropData.getKey();
					System.out.println(cropKey+"cropKey");
					String cropValue = cropData.getValue();

					if (TxnEnrollmentProperties.FARM_CROPS_CODE.equalsIgnoreCase(cropKey)) {
						validate(cropValue, ITxnErrorCodes.EMPTY_FARM_VARIETY_CODE);
						procurementVariety = farmCropsService.findProcurementVarietyByCode(cropValue);
						validate(procurementVariety, ITxnErrorCodes.FARM_VARIETY_DOES_NOT_EXIST);
					}
					if (TxnEnrollmentProperties.SEED_SOURCE.equalsIgnoreCase(cropKey)) {
						seedSource = StringUtil.isEmpty(cropValue) ? "" : cropValue;
					}
					if (TxnEnrollmentProperties.PRODUCTION_YEAR.equalsIgnoreCase(cropKey))
						productionYear = cropValue;
					if (TxnEnrollmentProperties.CROP_SEASON.equalsIgnoreCase(cropKey)) {
						// validate(cropValue,
						// ITxnErrorCodes.EMPTY_CROP_SEASON);
						HarvestSeason hs = farmerService.findHarvestSeasonByCode(cropValue);
					    cropSeason = hs;
							
					}
					if (TxnEnrollmentProperties.CROP_CATEGORY.equalsIgnoreCase(cropKey)) {
						// validate(cropValue,
						// ITxnErrorCodes.EMPTY_CROP_CATEGORY);
						cropCategory = StringUtil.isEmpty(cropValue) ? -1 : buildDDIntValue(cropValue);
					}
					if (TxnEnrollmentProperties.FARM_CROP_TYPE.equalsIgnoreCase(cropKey)) {
						type = StringUtil.isEmpty(cropValue) ? "" : cropValue;
					}
					if (TxnEnrollmentProperties.EST_YIELD.equalsIgnoreCase(cropKey)) {
						estyld = !StringUtil.isEmpty(cropValue) ? Double.valueOf(cropValue) : 0;
					}
					if (TxnEnrollmentProperties.SEED_COST_MAIN.equalsIgnoreCase(cropKey)) {
						seedCost = !StringUtil.isEmpty(cropValue) ? Double.valueOf(cropValue) : null;
					}
					if (TxnEnrollmentProperties.SEED_USED_MAIN.equalsIgnoreCase(cropKey)) {
						seedUsed = !StringUtil.isEmpty(cropValue) ? Double.valueOf(cropValue) : null;
					}
					if (TxnEnrollmentProperties.STAPLE_LENGTH_MAIN.equalsIgnoreCase(cropKey)) {
						stapleLnth = cropValue;
					}
				
					if (TxnEnrollmentProperties.SOWING_DATE.equalsIgnoreCase(cropKey)) {
                   	  if(!StringUtil.isEmpty(cropValue)){
                   		sowingDate =DateUtil.convertStringToDate(cropValue,DateUtil.DATE);
                   	  }
                   }
					if (TxnEnrollmentProperties.YEAR_OF_PLANTING.equalsIgnoreCase(cropKey)) {
						yearOfPlant = StringUtil.isEmpty(cropValue) ? "" : cropValue;
					}
                    if (TxnEnrollmentProperties.ESTIMATED_HARVEST_DATE.equalsIgnoreCase(cropKey)) {
                    	 if(!StringUtil.isEmpty(cropValue)){
                    		 estimatedHarvestDate=DateUtil.convertStringToDate(cropValue,DateUtil.DATE);
                    	}
                   }
                    if (TxnEnrollmentProperties.FARM_CROP_TYPE_OTHER.equalsIgnoreCase(cropKey)) {
                        otherTypeVal= cropValue;
                    }if (TxnEnrollmentProperties.CULTIVATION_TYPE.equalsIgnoreCase(cropKey)) {
                        cultType=cropValue;

                    } if (TxnEnrollmentProperties.CULTI_AREA
                            .equalsIgnoreCase(cropKey)) {
                        culArea=cropValue;

                    }
                    if(TxnEnrollmentProperties.SEED_TREATMENT_DETAILS.equalsIgnoreCase(cropKey)){
                    	seedTrest=cropValue;
                    }

                    if(TxnEnrollmentProperties.RISK_ASSESMENT.equalsIgnoreCase(cropKey)){
                    	riskAss=cropValue;
                    }
                    if(TxnEnrollmentProperties.OTHER_SEED_TREATMENT_DETAILS.equalsIgnoreCase(cropKey)){
                    	otherSeedTrest=cropValue;
                    }
                    if(TxnEnrollmentProperties.RISK_BUFFER_ZONE_DISTANCE.equalsIgnoreCase(cropKey)){
                    	riskBuff=cropValue;
                    }
                    if(TxnEnrollmentProperties.CROP_GPS.equalsIgnoreCase(cropKey)){
                    	 collection=cropData.getCollectionValue();
                     }
                   
                    if(TxnEnrollmentProperties.POST_HARVEST_INTER.equalsIgnoreCase(cropKey)){
                       	interType = cropValue;
                      }
                    
                    if(TxnEnrollmentProperties.INTER_ACRES.equalsIgnoreCase(cropKey)){
                       	interAcres = cropValue;
                        }
                    
                    if(TxnEnrollmentProperties.INTER_CROP.equalsIgnoreCase(cropKey)){
                    	cropHarInter = cropValue;
                        }
                    
                    if(TxnEnrollmentProperties.GROSS_INCOME.equalsIgnoreCase(cropKey)){
                       	interGrossIncom = cropValue;
                        }
                   
                    	if(TxnEnrollmentProperties.NO_OF_TREES.equalsIgnoreCase(cropKey)){
                           	noOfTrees = cropValue;
                            }
                   
                    	if(TxnEnrollmentProperties.PRODUCTIVE_TREES.equalsIgnoreCase(cropKey)){
                           	prodTrees = cropValue;
                            }
                    	if(TxnEnrollmentProperties.AFFECTED_TREES.equalsIgnoreCase(cropKey)){
                           	affTrees = cropValue;
                            }
                    	
                    	if (TxnEnrollmentProperties.CROP_EDIT_STATUS.equalsIgnoreCase(cropKey)) {
        			        cropEditStatus = cropValue;
        				}
                    	
				}
				
				

				/*FarmCrops farmCrops = farmCropMap.get(String.valueOf(startIndex));
				if (!ObjectUtil.isEmpty(farmCrops)) {
					farmCrops.setProcurementVariety(procurementVariety);
					farmCrops.setSeedSource(String.valueOf(seedSource));
					// farmCrops.setEstimatedYield(Double.valueOf(productionYear));
					farmCrops.setCropSeason(cropSeason);
					farmCrops.setCropCategory(cropCategory);
					farmCrops.setType(String.valueOf(type));
					farmCrops.setEstimatedYield(estyld);
					farmCrops.setSeedQtyCost(seedCost);
					farmCrops.setSeedQtyUsed(seedUsed);
					farmCrops.setStapleLength(stapleLnth);
					farmCrops.setBranchId(BRANCH_ID);
					farmCrops.setSowingDate(sowingDate);
					farmCrops.setOtherType(otherTypeVal);
					farmCrops.setCultiArea(culArea);
					farmCrops.setCropCategoryList(culType);
					farmCrops.setEstimatedHarvestDate(estimatedHarvestDate);
				} else {*/
				 if(StringUtil.isEmpty(cropEditStatus)){
			         cropEditStatus="0";
			        }
				
				    FarmCrops farmCrops = new FarmCrops();
					if(cropEditStatus.equalsIgnoreCase("0")){	
						 FarmCrops existingfarmCrops = farmerService.findFarmCropByCropIdAndFarmIdAndSeason(existingFarm.getId(),procurementVariety.getId(),cropSeason.getId());
						 if(ObjectUtil.isEmpty(existingfarmCrops)){
					farmCrops.setProcurementVariety(procurementVariety);
					farmCrops.setSeedSource(String.valueOf(seedSource));
					farmCrops.setFarm(existingFarm);
					// farmCrops.setEstimatedYield(Double.valueOf(productionYear));
					farmCrops.setCropSeason(cropSeason);
					farmCrops.setCropCategory(cropCategory);
					farmCrops.setType(String.valueOf(type));
					 if(TENANT_ID.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
						 farmCrops.setEstimatedYield(estyld*100); 
					 }else{
					farmCrops.setEstimatedYield(estyld);
					 }
					farmCrops.setSeedQtyCost(seedCost);
					farmCrops.setStatus(Farmer.Status.ACTIVE.ordinal());
					farmCrops.setSeedQtyUsed(seedUsed);
					farmCrops.setStapleLength(stapleLnth);
					farmCrops.setBranchId(BRANCH_ID);
					farmCrops.setSowingDate(sowingDate);
					farmCrops.setOtherType(otherTypeVal);
					farmCrops.setCultiArea(culArea);
                    farmCrops.setCropCategoryList(cultType);
					farmCrops.setEstimatedHarvestDate(estimatedHarvestDate);
					farmCrops.setLatitude(getFlatitude());
					farmCrops.setLongitude(getFlongitude());
					farmCrops.setRiskAssesment(riskAss);
					farmCrops.setSeedTreatmentDetails(seedTrest);
					farmCrops.setOtherSeedTreatmentDetails(otherSeedTrest);
					farmCrops.setRiskBufferZoneDistanse(riskBuff);
					farmCrops.setRevisionNo(DateUtil.getRevisionNumber());
					farmCrops.setPlotCapturingTime(DateUtil.convertStringToDate(plotCaptureTime, DateUtil.TXN_DATE_TIME));
					if(!CollectionUtil.isCollectionEmpty(collection)){
						
						
						
						
						CoordinatesMap coordinatesMap=new CoordinatesMap();
						farmCropsCoordinateSet.addAll(buildFarmCropCoordinated(collection,farmCrops));
						coordinatesMap.setFarmCropsCoordinates(farmCropsCoordinateSet);
						Head hd = (Head) farmMap.get(TransactionProperties.HEAD);
						coordinatesMap.setAgentId(hd.getAgentId());
						coordinatesMap.setArea(farmCrops.getFarm().getFarmDetailedInfo().getTotalLandHolding());
						coordinatesMap.setDate(	farmCrops.getFarm().getPlotCapturingTime());
						coordinatesMap.setFarmCrops(farmCrops);
						coordinatesMap.setMidLatitude(farmCrops.getLatitude());
						coordinatesMap.setMidLongitude(farmCrops.getLongitude());
						coordinatesMap.setStatus(CoordinatesMap.Status.ACTIVE.ordinal());
						if(farmCrops.getCoordinatesMap()!=null && !ObjectUtil.isListEmpty(farmCrops.getCoordinatesMap())){
							farmCrops.getCoordinatesMap().stream().forEach(co->{
								co.setStatus(CoordinatesMap.Status.INACTIVE.ordinal());
							});
							farmCrops.getCoordinatesMap().add(coordinatesMap);
							}
							else{
								Set<CoordinatesMap> coMap=new LinkedHashSet<>();
								coMap.add(coordinatesMap);
								farmCrops.setCoordinatesMap(coMap);
								
							}
						farmCrops.setActiveCoordinates(coordinatesMap);
					
					//farmCrops.setFarmCropsCoordinates(farmCropsCoordinateSet);
					}
					farmCrops.setNoOfTrees(!StringUtil.isEmpty(noOfTrees)? noOfTrees :"");
					farmCrops.setProdTrees(!StringUtil.isEmpty(prodTrees)? prodTrees :"");
					farmCrops.setAffTrees(!StringUtil.isEmpty(affTrees)? affTrees :"");
					farmCrops.setInterAcre(!StringUtil.isEmpty(yearOfPlant)? yearOfPlant :"");
			//	}
					farmCrops.setCropEditStatus(0);
					finalFarm=farmCrops;
					if(!ObjectUtil.isEmpty(farmCrops.getProcurementVariety())){
					    farmCropMap.put(String.valueOf(farmCrops.getProcurementVariety().getId()+String.valueOf(farmCrops.getCropSeason().getId())),farmCrops);
					}
						 }
						 
						
					}else{						
						//farmCrops = farmerService.findFarmCropByCropIdAndFarmId(existingFarm.getId(),procurementVariety.getId());	
						farmCrops = farmerService.findFarmCropByCropIdAndFarmIdAndSeason(existingFarm.getId(),procurementVariety.getId(),cropSeason.getId());
						if(ObjectUtil.isEmpty(farmCrops)){
						validate(farmCrops, ITxnErrorCodes.EMPTY_FARM_CROP);
						}
						farmCrops.setSeedSource(seedSource!=null && !StringUtil.isEmpty(seedSource)? seedSource :farmCrops.getSeedSource());
						farmCrops.setCropSeason(cropSeason!=null && !StringUtil.isEmpty(cropSeason)? cropSeason :farmCrops.getCropSeason());
						farmCrops.setCropCategory(!StringUtil.isEmpty(cropCategory)? cropCategory :farmCrops.getCropCategory());
						farmCrops.setType(type!=null && !StringUtil.isEmpty(type)? type :farmCrops.getType());
						 if(TENANT_ID.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)){
							 farmCrops.setEstimatedYield(estyld!=null && !StringUtil.isEmpty(estyld)? estyld*100 :farmCrops.getEstimatedYield());
						 }else{
							 farmCrops.setEstimatedYield(estyld!=null && !StringUtil.isEmpty(estyld)? estyld :farmCrops.getEstimatedYield());
						 }
						farmCrops.setSeedQtyCost(seedUsed!=null && !StringUtil.isEmpty(seedUsed)? seedUsed :farmCrops.getSeedQtyCost());
						farmCrops.setStapleLength(stapleLnth!=null && !StringUtil.isEmpty(stapleLnth)? stapleLnth :farmCrops.getStapleLength());
						farmCrops.setSowingDate(sowingDate!=null && !StringUtil.isEmpty(sowingDate)? sowingDate :farmCrops.getSowingDate());
						farmCrops.setOtherType(otherTypeVal!=null && !StringUtil.isEmpty(otherTypeVal)? otherTypeVal :farmCrops.getOtherType());
						farmCrops.setCultiArea(culArea!=null && !StringUtil.isEmpty(culArea)? culArea :farmCrops.getCultiArea());
						farmCrops.setCropCategoryList(cultType!=null && !StringUtil.isEmpty(cultType)? cultType :farmCrops.getCropCategoryList());
						farmCrops.setEstimatedHarvestDate(estimatedHarvestDate!=null && !StringUtil.isEmpty(estimatedHarvestDate)? estimatedHarvestDate :farmCrops.getEstimatedHarvestDate());
						farmCrops.setLatitude(!StringUtil.isEmpty(getFlatitude())? getFlatitude() :farmCrops.getLatitude());
						farmCrops.setLongitude(!StringUtil.isEmpty(getFlongitude())? getFlongitude() :farmCrops.getLongitude());
						farmCrops.setRiskAssesment(riskAss!=null && !StringUtil.isEmpty(riskAss)? riskAss :farmCrops.getRiskAssesment());
						farmCrops.setSeedTreatmentDetails(seedTrest!=null && !StringUtil.isEmpty(seedTrest)? seedTrest :farmCrops.getSeedTreatmentDetails());
						farmCrops.setOtherSeedTreatmentDetails(otherSeedTrest!=null && !StringUtil.isEmpty(otherSeedTrest)? otherSeedTrest :farmCrops.getOtherSeedTreatmentDetails());
						farmCrops.setRiskBufferZoneDistanse(riskBuff!=null && !StringUtil.isEmpty(riskBuff)? riskBuff :farmCrops.getRiskBufferZoneDistanse());
						farmCrops.setRevisionNo(DateUtil.getRevisionNumber());
						farmCrops.setCropEditStatus(1);
						farmCrops.setPlotCapturingTime(plotCaptureTime!=null && !StringUtil.isEmpty(plotCaptureTime)? DateUtil.convertStringToDate(plotCaptureTime, DateUtil.TXN_DATE_TIME) :farmCrops.getPlotCapturingTime());
						if(!CollectionUtil.isCollectionEmpty(collection)){/*
						farmCropsCoordinateSet.addAll(buildFarmCropCoordinated(collection,farmCrops));
						farmCrops.setFarmCropsCoordinates(farmCropsCoordinateSet);
						*/

							CoordinatesMap coordinatesMap=new CoordinatesMap();
							farmCropsCoordinateSet.addAll(buildFarmCropCoordinated(collection,farmCrops));
							coordinatesMap.setFarmCropsCoordinates(farmCropsCoordinateSet);
							Head hd = (Head) farmMap.get(TransactionProperties.HEAD);
							coordinatesMap.setAgentId(hd.getAgentId());
							coordinatesMap.setArea(farmCrops.getFarm().getFarmDetailedInfo().getTotalLandHolding());
							coordinatesMap.setDate(	farmCrops.getFarm().getPlotCapturingTime());
							coordinatesMap.setFarmCrops(farmCrops);
							coordinatesMap.setMidLatitude(farmCrops.getLatitude());
							coordinatesMap.setMidLongitude(farmCrops.getLongitude());
							coordinatesMap.setStatus(CoordinatesMap.Status.ACTIVE.ordinal());
							if(farmCrops.getCoordinatesMap()!=null && !ObjectUtil.isListEmpty(farmCrops.getCoordinatesMap())){
								farmCrops.getCoordinatesMap().stream().forEach(co->{
									co.setStatus(CoordinatesMap.Status.INACTIVE.ordinal());
								});
								farmCrops.getCoordinatesMap().add(coordinatesMap);
								}
								else{
									Set<CoordinatesMap> coMap=new LinkedHashSet<>();
									coMap.add(coordinatesMap);
									farmCrops.setCoordinatesMap(coMap);
									
								}
							farmCrops.setActiveCoordinates(coordinatesMap);
						
						//farmCrops.setFarmCropsCoordinates(farmCropsCoordinateSet);
							
						}
						farmCrops.setNoOfTrees(noOfTrees!=null && !StringUtil.isEmpty(noOfTrees)? noOfTrees :farmCrops.getNoOfTrees());
						farmCrops.setProdTrees(prodTrees!=null && !StringUtil.isEmpty(prodTrees)? prodTrees :farmCrops.getProdTrees());
						farmCrops.setAffTrees(affTrees!=null && !StringUtil.isEmpty(affTrees)? affTrees :farmCrops.getAffTrees());
						farmCrops.setInterAcre(yearOfPlant!=null && !StringUtil.isEmpty(yearOfPlant)? yearOfPlant :farmCrops.getInterAcre());
						finalFarm=farmCrops;
						//farmCropMap.remove(farmCrops.getProcurementVariety().getId()+String.valueOf(farmCrops.getCropSeason().getId()));
					    farmCropMap.replace(String.valueOf(farmCrops.getProcurementVariety().getId()+String.valueOf(farmCrops.getCropSeason().getId())),farmCrops);
					}
				 
				startIndex++;
			}
			
			//farmCropDataSet.clear();
			farmCropDataSet.addAll(farmCropMap.values());
		}
		return farmCropDataSet;

	}

	private Set<FarmCropsCoordinates> buildFarmCropCoordinated(Collection collectionValue, FarmCrops farmCrops) {

		Set<FarmCropsCoordinates> returnSet = null;
		if (!ObjectUtil.isEmpty(collectionValue)) {
			returnSet = new LinkedHashSet<FarmCropsCoordinates>();
			List<com.sourcetrace.eses.txn.schema.Object> farmCropCoordinates = collectionValue.getObject();
			for (com.sourcetrace.eses.txn.schema.Object coordinateObj : farmCropCoordinates) {
				FarmCropsCoordinates cropsCoordinate = new FarmCropsCoordinates();
				cropsCoordinate.setFarmCrops(farmCrops);
				List<Data> farmCropCoordinateList = coordinateObj.getData();

				for (Data data : farmCropCoordinateList) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.CROP_LATITUDE.equalsIgnoreCase(key)) {
						cropsCoordinate.setLatitude(value);
					}
					if (TxnEnrollmentProperties.CROP_LONGITUDE.equalsIgnoreCase(key)) {
						cropsCoordinate.setLongitude(value);
					}
					if (TxnEnrollmentProperties.CROP_GRS_SNO.equalsIgnoreCase(key)) {
						cropsCoordinate.setOrderNo(!StringUtil.isEmpty(value)?Long.valueOf(value):0);
					}
				}
				returnSet.add(cropsCoordinate);
			}
		}
		return returnSet;
	}
	
	private FarmerDynamicData getFarmerDynamicFields(Collection dynamicFields, Collection dynamicList,
			Collection photoList, String tenantId, DynamicFeildMenuConfig dm, Long farmer) {
		FarmerDynamicData farmerDynamicData = new FarmerDynamicData();

		Map<String, DynamicFieldConfig> fieldConfigMap = new HashMap<>();
		dm.getDynamicFieldConfigs().stream().forEach(section -> {
			section.getField().setfOrder(section.getOrder());
			fieldConfigMap.put(section.getField().getCode(), section.getField());
		});
		Map<String, String> fieldTypeMap = new HashMap<>();
		farmerDynamicData.setTxnType(dm.getTxnType());
		List<FarmerDynamicFieldsValue> farmerDynamicFieldsValueList = new LinkedList<>();
		if (!ObjectUtil.isEmpty(dynamicFields)) {
			List<com.sourcetrace.eses.txn.schema.Object> fieldObjects = dynamicFields.getObject();
			for (com.sourcetrace.eses.txn.schema.Object fieldObject : fieldObjects) {
				List<Data> dynamicData = fieldObject.getData();
				FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();

				for (Data data : dynamicData) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.FIELD_ID.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setFieldName(value);
					} else if (TxnEnrollmentProperties.FIELD_VAL.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setFieldValue(value);
					} else if (TxnEnrollmentProperties.COMPONENT_TYPE.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setComponentType(value);
					}
				}

				farmerDynamicFieldsValue.setTxnType(dm.getTxnType());
				farmerDynamicFieldsValue.setReferenceId(String.valueOf(farmer));
				farmerDynamicFieldsValue
						.setValidationType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getValidation() : "0");
				farmerDynamicFieldsValue
						.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType() : 0);
				farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
				farmerDynamicFieldsValue
						.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail() : "0");

				farmerDynamicFieldsValue.setListMethod(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()
								: "");
				farmerDynamicFieldsValue.setParentId(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
				&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
						? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() : 0);
		
				farmerDynamicFieldsValueList.add(farmerDynamicFieldsValue);

			}
		}

		if (!ObjectUtil.isEmpty(dynamicList)) {
			List<com.sourcetrace.eses.txn.schema.Object> fieldObjects = dynamicList.getObject();
			for (com.sourcetrace.eses.txn.schema.Object fieldObject : fieldObjects) {
				List<Data> dynamicData = fieldObject.getData();
				FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
				for (Data data : dynamicData) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.FIELD_ID.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setFieldName(value);
					} else if (TxnEnrollmentProperties.FIELD_VAL.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setFieldValue(value);
					} else if (TxnEnrollmentProperties.COMPONENT_TYPE.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setComponentType(value);
					} else if (TxnEnrollmentProperties.TXN_TYPE_ID.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setTxnType("357");
					} else if (TxnEnrollmentProperties.LIST_ITRATION.equalsIgnoreCase(key)) {

						Integer type = null;
						if (fieldTypeMap != null && fieldTypeMap.containsKey(farmerDynamicFieldsValue.getFieldName())) {

							String iterateCount = fieldTypeMap.get(farmerDynamicFieldsValue.getFieldName());

							if (!StringUtil.isEmpty(iterateCount) && StringUtil.isInteger(iterateCount)) {
								type = Integer.valueOf(iterateCount) + Integer.valueOf(value);
							} else {
								type = Integer.valueOf(value);
							}
						} else {
							type = Integer.valueOf(value);
						}

						farmerDynamicFieldsValue.setTypez(type);
					}
				}

				farmerDynamicFieldsValue
						.setValidationType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getValidation() : "0");
				farmerDynamicFieldsValue
				.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType() : 0);
				
				farmerDynamicFieldsValue.setTxnType(dm.getTxnType());
				farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);

				farmerDynamicFieldsValue
						.setIsMobileAvail(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsMobileAvail() : "0");
				
				farmerDynamicFieldsValue.setListMethod(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()
								: "");
				farmerDynamicFieldsValue.setParentId(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() : 0);
				
				farmerDynamicFieldsValue.setTxnType("357");
				farmerDynamicFieldsValue.setReferenceId(String.valueOf(farmer));
				farmerDynamicFieldsValueList.add(farmerDynamicFieldsValue);
			}
		}

		Map<String, FarmerDynamicFieldsValue> farmerDynmaicFieldValuesList = new HashMap<>();
		if (!ObjectUtil.isListEmpty(farmerDynamicFieldsValueList)) {
			farmerDynamicFieldsValueList.stream().forEach(obj -> {
				if (!farmerDynmaicFieldValuesList.containsKey(obj.getFieldName())) {
					farmerDynmaicFieldValuesList.put(obj.getFieldName(), obj);
				}
			});
		}

		if (!ObjectUtil.isEmpty(photoList)) {
			List<com.sourcetrace.eses.txn.schema.Object> fieldObjects = photoList.getObject();
			for (com.sourcetrace.eses.txn.schema.Object fieldObject : fieldObjects) {
				List<Data> dynamicData = fieldObject.getData();
				DynamicImageData dynamicImageData = new DynamicImageData();
				FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
				for (Data data : dynamicData) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.FIELD_ID.equalsIgnoreCase(key)) {
						if (fieldConfigMap.containsKey(value)) {
							if (!ObjectUtil.isEmpty(fieldConfigMap.get(value).getParentDepen())) {
								String parentKey = fieldConfigMap.get(value).getParentDepen().getCode();
								farmerDynamicFieldsValue = farmerDynmaicFieldValuesList.get(parentKey);
								farmerDynamicFieldsValueList.remove(farmerDynamicFieldsValue);
								dynamicImageData.setFarmerDynamicFieldsValue(farmerDynamicFieldsValue);

							} else {
								farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
								farmerDynamicFieldsValue.setComponentType(String
										.valueOf(DynamicFieldConfig.COMPONENT_TYPES.PHOTO_CERTIFICATION.ordinal()));
								farmerDynamicFieldsValue.setCreatedDate(new Date());
								farmerDynamicFieldsValue.setCreatedDate(new Date());
								farmerDynamicFieldsValue.setFieldName(value);
								farmerDynamicFieldsValue.setFieldValue("");
								farmerDynamicFieldsValue.setTxnType(dm.getTxnType());

								farmerDynamicFieldsValue.setIsMobileAvail(
										fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
												? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
														.getIsMobileAvail()
												: "0");
								farmerDynamicFieldsValue.setValidationType(
										fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
												? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
														.getValidation()
												: "0");
								
								farmerDynamicFieldsValue
								.setAccessType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getAccessType() : 0);
								
								farmerDynamicFieldsValue.setListMethod(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
												? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()
												: "");
								farmerDynamicFieldsValue.setParentId(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
												? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() : 0);
								
								// farmerService.save(farmerDynamicFieldsValue);
								dynamicImageData.setFarmerDynamicFieldsValue(farmerDynamicFieldsValue);
							}
						} else {
							break;
						}
					} else if (TxnEnrollmentProperties.F_PHOTO.equalsIgnoreCase(key)) {
						DataHandler photo = data.getBinaryValue();
						byte[] imageContent = null;
						try {
							if (photo != null && photo.getInputStream().available() > 0) {
								imageContent = IOUtils.toByteArray(photo.getInputStream());
								dynamicImageData.setImage(imageContent);
							}
						} catch (Exception e) {
							e.printStackTrace();
							throw new SwitchException(ITxnErrorCodes.ERR0R_WHILE_PROCESSING);
						}

					} else if (TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value) && !value.equals("0")) {
							try {
								Date photoCaptureDate = DateUtil.convertStringToDate(value, DateUtil.TXN_TIME_FORMAT);
								dynamicImageData.setPhotoCaptureTime(photoCaptureDate);
							} catch (Exception e) {
								e.printStackTrace();
								throw new SwitchException(ITxnErrorCodes.DATE_CONVERSION_ERROR);
							}
						}
					} else if (TxnEnrollmentProperties.LIST_ITRATION.equalsIgnoreCase(key) && value != null
							&& Integer.valueOf(value) > 0) {
						Integer type = null;
						if (fieldTypeMap != null && fieldTypeMap.containsKey(farmerDynamicFieldsValue.getFieldName())) {

							String iterateCount = fieldTypeMap.get(farmerDynamicFieldsValue.getFieldName());

							if (!StringUtil.isEmpty(iterateCount) && StringUtil.isInteger(iterateCount)) {
								type = Integer.valueOf(iterateCount) + Integer.valueOf(value);
							} else {
								type = Integer.valueOf(value);
							}
						} else {
							type = Integer.valueOf(value);
						}

						farmerDynamicFieldsValue.setTypez(type);
					} else if (TxnEnrollmentProperties.PHOTO_LATITUDE.equalsIgnoreCase(key))
						dynamicImageData.setLatitude(value);

					else if (TxnEnrollmentProperties.PHOTO_LONGITUDE.equalsIgnoreCase(key))
						dynamicImageData.setLongitude(value);

				}
				Set<DynamicImageData> imageDataSet = new HashSet<>();
				imageDataSet.add(dynamicImageData);
				farmerDynamicFieldsValue.setTxnType("357");
				farmerDynamicFieldsValue.setDymamicImageData(imageDataSet);
				farmerDynamicFieldsValue.setReferenceId(String.valueOf(farmer));
				farmerDynamicFieldsValueList.add(farmerDynamicFieldsValue);

			}
		}
		farmerDynamicData.setFarmerDynamicFieldsValues(new HashSet<>(farmerDynamicFieldsValueList));

		return farmerDynamicData;

	}

	/**
	 * Gets the farm crops datas.
	 * 
	 * @param existingFarm
	 * @return the farm crops datas
	 */
	private Map getFarmCropsDatas(Farm existingFarm) {

		Map<String, FarmCrops> farmCropMap = new HashMap<String, FarmCrops>();
		int i = 1;
		for (FarmCrops farmCrops : existingFarm.getFarmCrops()) {
			
		/*	 if(Farmer.CERTIFICATION_TYPE_NONE !=
			  existingFarm.getFarmer().getCertificationType()) {
			  if(!farmCropMap.containsKey(farmCrops.getCropSeason()+farmCrops.
			  getCropCode()))
			  farmCropMap.put(farmCrops.getCropSeason()+farmCrops.getCropCode()
			  , farmCrops); } 
			 else { farmCropMap.put(farmCrops.getCropCode(),
			  farmCrops); }*/
			 
			farmCropMap.put(String.valueOf(farmCrops.getProcurementVariety().getId()+String.valueOf(farmCrops.getCropSeason().getId())), farmCrops);
			i++;
		}
		return farmCropMap;
	}

	/**
	 * Builds the dd int value.
	 * 
	 * @param value
	 *            the value
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 */
	public int buildDDIntValue(String value) throws IOException, OfflineTransactionException {

		try {
			return (!StringUtil.isEmpty(value) ? Integer.parseInt(value) : -1);
		} catch (Exception e) {
			throwError(ITxnErrorCodes.DATA_CONVERSION_ERROR);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.txn.adapter.ITxnAdapter#processVoid(java.util.Map)
	 */
	@Override
	public Map<?, ?> processVoid(Map<?, ?> reqData) {

		return null;
	}

	/**
	 * Sets the certification service.
	 * 
	 * @param certificationService
	 *            the new certification service
	 */
	public void setCertificationService(ICertificationService certificationService) {

		this.certificationService = certificationService;
	}

	/**
	 * Gets the certification service.
	 * 
	 * @return the certification service
	 */
	public ICertificationService getCertificationService() {

		return certificationService;
	}

	/**
	 * Gets the client service.
	 * 
	 * @return the client service
	 */
	public IClientService getClientService() {

		return clientService;
	}

	/**
	 * Sets the client service.
	 * 
	 * @param clientService
	 *            the new client service
	 */
	public void setClientService(IClientService clientService) {

		this.clientService = clientService;
	}

	/**
	 * Gets the farmer service.
	 * 
	 * @return the farmer service
	 */
	public IFarmerService getFarmerService() {

		return farmerService;
	}

	/**
	 * Sets the farmer service.
	 * 
	 * @param farmerService
	 *            the new farmer service
	 */
	public void setFarmerService(IFarmerService farmerService) {

		this.farmerService = farmerService;
	}

	/**
	 * Gets the location service.
	 * 
	 * @return the location service
	 */
	public ILocationService getLocationService() {

		return locationService;
	}

	/**
	 * Sets the account service.
	 * 
	 * @param accountService
	 *            the new account service
	 */
	public void setAccountService(IAccountService accountService) {

		this.accountService = accountService;
	}

	/**
	 * Gets the account service.
	 * 
	 * @return the account service
	 */
	public IAccountService getAccountService() {

		return accountService;
	}

	/**
	 * Sets the location service.
	 * 
	 * @param locationService
	 *            the new location service
	 */
	public void setLocationService(ILocationService locationService) {

		this.locationService = locationService;
	}

	/**
	 * Sets the card service.
	 * 
	 * @param cardService
	 *            the new card service
	 */
	public void setCardService(ICardService cardService) {

		this.cardService = cardService;
	}

	/**
	 * Gets the card service.
	 * 
	 * @return the card service
	 */
	public ICardService getCardService() {

		return cardService;
	}

	/**
	 * Sets the device service.
	 * 
	 * @param deviceService
	 *            the new device service
	 */
	public void setDeviceService(IDeviceService deviceService) {

		this.deviceService = deviceService;
	}

	/**
	 * Gets the device service.
	 * 
	 * @return the device service
	 */
	public IDeviceService getDeviceService() {

		return deviceService;
	}

	/**
	 * Sets the agent service.
	 * 
	 * @param agentService
	 *            the new agent service
	 */
	public void setAgentService(IAgentService agentService) {

		this.agentService = agentService;
	}

	/**
	 * Gets the agent service.
	 * 
	 * @return the agent service
	 */
	public IAgentService getAgentService() {

		return agentService;
	}

	/**
	 * Sets the id generator.
	 * 
	 * @param idGenerator
	 *            the new id generator
	 */
	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
	}

	/**
	 * Gets the id generator.
	 * 
	 * @return the id generator
	 */
	public IUniqueIDGenerator getIdGenerator() {

		return idGenerator;
	}

	/**
	 * Sets the farm crops service.
	 * 
	 * @param farmCropsService
	 *            the new farm crops service
	 */
	public void setFarmCropsService(IFarmCropsService farmCropsService) {

		this.farmCropsService = farmCropsService;
	}

	/**
	 * Gets the farm crops service.
	 * 
	 * @return the farm crops service
	 */
	public IFarmCropsService getFarmCropsService() {

		return farmCropsService;
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

	public FarmCrops getFinalFarm() {
		return finalFarm;
	}

	public void setFinalFarm(FarmCrops finalFarm) {
		this.finalFarm = finalFarm;
	}
	
	

}
