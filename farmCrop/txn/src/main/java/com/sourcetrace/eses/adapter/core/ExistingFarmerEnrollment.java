/*
 * 
 */
package com.sourcetrace.eses.adapter.core;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldScoreMap;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLog;
import com.sourcetrace.eses.order.entity.txn.AgentAccessLogDetail;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICardService;
import com.sourcetrace.eses.service.ICatalogueService;
import com.sourcetrace.eses.service.ICertificationService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.ILocationService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.CurrencyUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.CoordinatesMap;
import com.sourcetrace.esesw.entity.profile.DocumentUpload;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;
import com.sourcetrace.esesw.entity.profile.FarmElement;
import com.sourcetrace.esesw.entity.profile.FarmICS;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.FarmerFamily;
import com.sourcetrace.esesw.entity.profile.FarmerIncomeDetails;
import com.sourcetrace.esesw.entity.profile.FarmerLandDetails;
import com.sourcetrace.esesw.entity.profile.FarmerPlants;
import com.sourcetrace.esesw.entity.profile.FarmerScheme;
import com.sourcetrace.esesw.entity.profile.FarmerSoilTesting;
import com.sourcetrace.esesw.entity.profile.FarmerSourceIncome;
import com.sourcetrace.esesw.entity.profile.HarvestData;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.HousingInfo;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.TreeDetail;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;

@Component
public class ExistingFarmerEnrollment implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(FarmerEnrollment.class.getName());
	private static final DateFormat TXN_DATE_FORMAT = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
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
	@Autowired
	private ICatalogueService catalogueService;
	private int mode = 1;
	private int farmerCertificationType = 0;
	private static final String REQUEST_DATA = "requestData";
	private static final String FARMER = "farmer";
	private static final String HEAD = "HEAD";
	private static final String FARM = "farm";
	private static final String FARMER_SOURCE_INCOME = "farmerSourceIncome";
	private static final String HARVEST_DATA = "harvestData";
	private static final String INVENTORY_COLLECTION = "frmInvnts";
	private static final String ANIMAL_COLLECTION = "frmAnmls";
	private static final String HARVEST_COLLECTION = "harvestCollection";
	private static final String FARM_CROP_COLLECTION = "farmCropCollection";
	private static final String FARM_ICS_COLLECTION = "icsLst";
	private static final int SELECT = -1;
	private static String BRANCH_ID = "";
	public static final String SELECT_MULTI = "-1";
	private static final String MACHINARY_COLLECTION = "machLst";
	private static final String POLY_COLLECTION = "phLst";
	private String tenantId;
	private String awiFarmCodeSeq="";
	private int basicInfo =0;
	private Farm finalFarm;
	private FarmerDynamicData farmerDynamicData = new FarmerDynamicData();
	List<String> farmCodeFmt = new ArrayList<>(Arrays.asList("A", "B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"));
	@Autowired
	private IProductDistributionService productDistributionService;
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
		String tenantId = null;
		String plotCaptureTime = null;

		mode = Integer.parseInt(head.getMode());
		int statusCode = ESETxnStatus.SUCCESS.ordinal();
		String statusMsg = ESETxnStatus.SUCCESS.toString();

		if (!ObjectUtil.isEmpty(head)) {
			LOGGER.info("Agent ID : " + head.getAgentId());
			LOGGER.info("Device ID : " + head.getSerialNo());
			agentId = head.getAgentId();
			serialNo = head.getSerialNo();
			tenantId = head.getTenantId();
			setTenantId(head.getTenantId());
			plotCaptureTime=head.getTxnTime();
		}
		/** INITAIALIZING COMMON DATA **/

		Set<HarvestData> harvestDatas = new HashSet<HarvestData>();
		Map requestMap = new HashMap();
		requestMap.put(REQUEST_DATA, reqData);
		requestMap.put(HEAD, head);

		Set<FarmerSourceIncome> sourceIncomeSet = new LinkedHashSet<FarmerSourceIncome>();

		/** GET REQUEST DATA **/
		Farmer existingFarmer = null;
		String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
		String photoCaptureTime = (String) reqData.get(TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME);
		String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
		String longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);
		DataHandler photoDataHandler = (DataHandler) reqData.get(TxnEnrollmentProperties.PHOTO);
		String grsMember = (String) reqData.get(TxnEnrollmentProperties.GRS_MEMEBER);
		String paidShareCapital = (String) reqData.get(TxnEnrollmentProperties.PAID_SHARE_CAPITAL);

		// APMAS Fields
		// String hhid = (String) reqData.get(TxnEnrollmentProperties.HHID);
		String adhaar = (String) reqData.get(TxnEnrollmentProperties.ADHAAR);
		String caste = (String) reqData.get(TxnEnrollmentProperties.CASTE);
		String religion = (String) reqData.get(TxnEnrollmentProperties.RELIGION);
		String religionOther = (String) reqData.get(TxnEnrollmentProperties.RELIGION_OTHER);
		String typeOfFamily = (String) reqData.get(TxnEnrollmentProperties.TYPE_OF_FAMILY);

		String householdLandholdingWet = (String) reqData.get(TxnEnrollmentProperties.HOUSEHOLD_LANDHOLDING_WET);
		String householdLandholdingDry = (String) reqData.get(TxnEnrollmentProperties.HOUSEHOLD_LANDHOLDING_DRY);
		String primaryHousehold = (String) reqData.get(TxnEnrollmentProperties.PRIMARY_HOUSEHOLD);
		String primaryHouseholdOther = (String) reqData.get(TxnEnrollmentProperties.PRIMARY_HOUSEHOLD_OTHER);

		String secondaryHousehold = (String) reqData.get(TxnEnrollmentProperties.SECONDARY_HOUSEHOLD);
		String secondaryHouseholdOther = (String) reqData.get(TxnEnrollmentProperties.SECONDARY_HOUSEHOLD_OTHER);

		String familyMemberCbo = (String) reqData.get(TxnEnrollmentProperties.FAMILY_MEMBER_CBO);
		String familyMemberCboOther = (String) reqData.get(TxnEnrollmentProperties.FAMILY_MEMBER_CBO_OTHER);

		String totalIncomePerAnnum = (String) reqData.get(TxnEnrollmentProperties.TOTAL_INCOME_PER_ANNUM);
		String opinionOfInvestigator = (String) reqData.get(TxnEnrollmentProperties.OPINION_OF_INVESTIGATOR);
		String positionInTheGroup = (String) reqData.get(TxnEnrollmentProperties.POSITION_IN_THE_GROUP);		
		String trader = (reqData.containsKey(TxnEnrollmentProperties.TRADER))
				? (String) reqData.get(TxnEnrollmentProperties.TRADER) : "";
		byte[] photoContent = null;
		ImageInfo imageInfo = null;

		try { // Basic Info

			// Validations
			validate(agentId, ITxnErrorCodes.AGENT_ID_EMPTY);
			agent = agentService.findAgentByAgentId(agentId);
			validate(agent, ITxnErrorCodes.INVALID_AGENT);
			validate(serialNo, ITxnErrorCodes.EMPTY_SERIAL_NO);
			device = deviceService.findDeviceBySerialNumber(serialNo);
			validate(device, ITxnErrorCodes.INVALID_DEVICE);

			validate(farmerId, ITxnErrorCodes.EMPTY_FARMER_ID);
			existingFarmer = farmerService.findFarmerByFarmerId(farmerId);
			if (ObjectUtil.isEmpty(existingFarmer))
				throw new SwitchException(ITxnErrorCodes.INVALID_FARMER_ID);

			BRANCH_ID = head.getBranchId();
			existingFarmer.setBranchId(head.getBranchId());
			// Adding photo capturing time
			if (!StringUtil.isEmpty(photoCaptureTime)) {
				try {
					Date photoCaptureDate = TXN_DATE_FORMAT.parse(photoCaptureTime);
					existingFarmer.setPhotoCaptureTime(photoCaptureDate);
				} catch (Exception e) {
					LOGGER.info(e.getMessage());
				}
			}

			/** FORMING IMAGE INFO **/
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
		
		
			
			existingFarmer.setLatitude(StringUtil.isEmpty(latitude) ? null : latitude);
			existingFarmer.setLongitude(StringUtil.isEmpty(longitude) ? null : longitude);
		
			//Digital Signature
			DataHandler ds = (reqData.containsKey(TxnEnrollmentProperties.FARMER_DIGITAL_SIGNATURE))
					? (DataHandler) reqData.get(TxnEnrollmentProperties.FARMER_DIGITAL_SIGNATURE) : null;
			if (!ObjectUtil.isEmpty(ds)) {
				byte[] digitalSign = null;
				try {
					if (ds != null) {
						digitalSign = IOUtils.toByteArray(ds.getInputStream());
						if (!StringUtil.isEmpty(digitalSign) && !digitalSign.equals("[]")) {
							existingFarmer.setDigitalSign(digitalSign);
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// existingFarmer.setFarmerCode(!StringUtil.isEmpty(hhid)? hhid :
			// "");
			existingFarmer.setAdhaarNo(!StringUtil.isEmpty(adhaar) ? adhaar : "");
			existingFarmer.setSocialCategory(!StringUtil.isEmpty(caste) ? caste : "");
			existingFarmer.setReligion(!StringUtil.isEmpty(religion) ? religion : "");
			existingFarmer.setReligionOther(!StringUtil.isEmpty(religionOther) ? religionOther : "");
			existingFarmer.setTypeOfFamily(!StringUtil.isEmpty(typeOfFamily) ? typeOfFamily : "");

			String householdLHDry = (!StringUtil.isEmpty(householdLandholdingDry)) ? householdLandholdingDry : "0";
			existingFarmer.setHouseHoldLandDry(householdLHDry);

			String householdLHWet = (!StringUtil.isEmpty(householdLandholdingWet)) ? householdLandholdingWet : "0";
			existingFarmer.setHouseHoldLandWet(householdLHWet);

			existingFarmer.setHouseOccupationPrimary(!StringUtil.isEmpty(primaryHousehold) ? primaryHousehold : "");
			existingFarmer.setHouseOccupationPriOther(
					!StringUtil.isEmpty(primaryHouseholdOther) ? primaryHouseholdOther : "");
			existingFarmer
					.setHouseOccupationSecondary(!StringUtil.isEmpty(secondaryHousehold) ? secondaryHousehold : "");
			existingFarmer.setHouseOccupationSecOther(
					!StringUtil.isEmpty(secondaryHouseholdOther) ? secondaryHouseholdOther : "");
			existingFarmer.setFamilyMember(!StringUtil.isEmpty(familyMemberCbo) ? familyMemberCbo : "");
			existingFarmer.setFamilyMemberOther(!StringUtil.isEmpty(familyMemberCboOther) ? familyMemberCboOther : "");

			String totalIncome = (!StringUtil.isEmpty(totalIncomePerAnnum))
					? CurrencyUtil.formatByUSDcomma(totalIncomePerAnnum) : "0";

			existingFarmer.setTotalSourceIncome(Double.valueOf(totalIncome));
			existingFarmer
					.setInvestigatorOpinion(!StringUtil.isEmpty(opinionOfInvestigator) ? opinionOfInvestigator : "");
			if (tenantId.equals("atma") ) {
				Agent tmpAgent = agentService.findAgentByAgentId(agentId);
				existingFarmer.setInvestigatorName(tmpAgent.getProfileId());
				existingFarmer
						.setInvestigatorDate(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
			}
			existingFarmer.setPositionGroup(!StringUtil.isEmpty(positionInTheGroup) ? positionInTheGroup : "");
			existingFarmer.setGrsMember(buildIntValue(grsMember));
			existingFarmer.setPaidShareCapitial(buildIntValue(paidShareCapital));
			//existingFarmer.setMasterData(!StringUtil.isEmpty(trader) ? trader : "");
		/*	Set<FarmerFamily> farmerFamilySet = getFarmerFamily(requestMap,tenantId);
			existingFarmer.setFarmerFamilies(ObjectUtil.isListEmpty(farmerFamilySet) ? null : farmerFamilySet);*/

			
			/*
			 * if(!StringUtil.isEmpty(hhid) && hhid.length()>=8){ String
			 * warehouseCode = hhid.substring(0, 8); String sanghamType =
			 * farmerService.findSanghamTypeFromWarehouseByWarehouseCode(
			 * warehouseCode);
			 * existingFarmer.setSangham(!StringUtil.isEmpty(sanghamType)?
			 * sanghamType : ""); }else{ existingFarmer.setSangham(""); }
			 */

			Collection agriActCollection = (Collection) reqData
					.get(TxnEnrollmentProperties.AGRICULTURAL_ACTIVITIES_LIST);
			Collection horicultureCollection = (Collection) reqData.get(TxnEnrollmentProperties.HORTICULTURE_LIST);
			Collection alliedSectorCollection = (Collection) reqData.get(TxnEnrollmentProperties.ALLIED_SECTOR_LIST);
			Collection employmentCollection = (Collection) reqData.get(TxnEnrollmentProperties.EMPLOYMENT_LIST);
			Collection othersCollection = (Collection) reqData.get(TxnEnrollmentProperties.OTHERS_LIST);

			if (!ObjectUtil.isEmpty(agriActCollection)) {
				Map agriActMap = getFarmerSourceIncome(requestMap, agriActCollection, Farmer.AGRICULTURE_ACTIVITIES);
				// existingFarmer.setFarmerSourceIncome((Set<FarmerSourceIncome>)
				// agriActMap.get(FARMER_SOURCE_INCOME));
				if (!ObjectUtil.isEmpty((FarmerSourceIncome) agriActMap.get(FARMER_SOURCE_INCOME)))
					sourceIncomeSet.add((FarmerSourceIncome) agriActMap.get(FARMER_SOURCE_INCOME));
			}

			if (!ObjectUtil.isEmpty(horicultureCollection)) {
				Map horicultureMap = getFarmerSourceIncome(requestMap, horicultureCollection, Farmer.HORTICULTURE);
				// existingFarmer.setFarmerSourceIncome((Set<FarmerSourceIncome>)
				// horicultureMap.get(FARMER_SOURCE_INCOME));
				if (!ObjectUtil.isEmpty((FarmerSourceIncome) horicultureMap.get(FARMER_SOURCE_INCOME)))
					sourceIncomeSet.add((FarmerSourceIncome) horicultureMap.get(FARMER_SOURCE_INCOME));
			}

			if (!ObjectUtil.isEmpty(alliedSectorCollection)) {
				Map alliedSectorMap = getFarmerSourceIncome(requestMap, alliedSectorCollection, Farmer.ALLIED_SECTOR);
				// existingFarmer.setFarmerSourceIncome((Set<FarmerSourceIncome>)
				// alliedSectorMap.get(FARMER_SOURCE_INCOME));
				if (!ObjectUtil.isEmpty((FarmerSourceIncome) alliedSectorMap.get(FARMER_SOURCE_INCOME)))
					sourceIncomeSet.add((FarmerSourceIncome) alliedSectorMap.get(FARMER_SOURCE_INCOME));
			}

			if (!ObjectUtil.isEmpty(employmentCollection)) {
				Map employmentMap = getFarmerSourceIncome(requestMap, employmentCollection, Farmer.EMPLOYMENT);
				// existingFarmer.setFarmerSourceIncome((Set<FarmerSourceIncome>)
				// employmentMap.get(FARMER_SOURCE_INCOME));
				if (!ObjectUtil.isEmpty((FarmerSourceIncome) employmentMap.get(FARMER_SOURCE_INCOME)))
					sourceIncomeSet.add((FarmerSourceIncome) employmentMap.get(FARMER_SOURCE_INCOME));
			}

			if (!ObjectUtil.isEmpty(othersCollection)) {
				Map othersMap = getFarmerSourceIncome(requestMap, othersCollection, Farmer.OTHERS);
				// existingFarmer.setFarmerSourceIncome((Set<FarmerSourceIncome>)
				// othersMap.get(FARMER_SOURCE_INCOME));
				if (!ObjectUtil.isEmpty((FarmerSourceIncome) othersMap.get(FARMER_SOURCE_INCOME)))
					sourceIncomeSet.add((FarmerSourceIncome) othersMap.get(FARMER_SOURCE_INCOME));
			}

			if (!ObjectUtil.isEmpty(sourceIncomeSet) && sourceIncomeSet.size() > 0)
				existingFarmer.setFarmerSourceIncome(sourceIncomeSet);

			/** FORMING FARM LIST **/
			requestMap.put(FARMER, existingFarmer);
			Map farmObjectMap = getFarm(requestMap,plotCaptureTime);
			existingFarmer.getFarms().addAll((Set<Farm>) farmObjectMap.get(FARM));
			harvestDatas = (Set<HarvestData>) farmObjectMap.get(HARVEST_DATA);
			existingFarmer.setStatusCode(statusCode);
			existingFarmer.setStatusMsg(statusMsg);
			farmerService.updateContractForFarmerAndHarvestDatas(existingFarmer, harvestDatas, agentId);

			if (existingFarmer.getId() > 0 && imageInfo != null && !ObjectUtil.isEmpty(imageInfo)
					&& imageInfo.getPhoto() != null) {
				imageInfo.getPhoto().setImageId(existingFarmer.getId() + "-FP");
				farmerService.addImageInfo(imageInfo);
				farmerService.updateFarmerImageInfo(existingFarmer.getId(), imageInfo.getId());
			}
			
			Collection dynamicFields = (reqData.containsKey(TransactionProperties.DYNAMIC_FIELD))
					? (Collection) reqData.get(TransactionProperties.DYNAMIC_FIELD) : null;

			Collection dynamicList = (reqData.containsKey(TransactionProperties.DYNAMIC_FIELD_LIST))
					? (Collection) reqData.get(TransactionProperties.DYNAMIC_FIELD_LIST) : null;
			Collection photoList = (reqData.containsKey(TransactionProperties.DYNAMIC_IMAGE_LIST))
					? (Collection) reqData.get(TransactionProperties.DYNAMIC_IMAGE_LIST) : null;

			if (!CollectionUtil.isCollectionEmpty(dynamicFields) || !CollectionUtil.isCollectionEmpty(dynamicList)
					|| !CollectionUtil.isCollectionEmpty(photoList)) {
				farmerDynamicData = new FarmerDynamicData();
				List<DynamicFeildMenuConfig> dmList = farmerService.findDynamicMenusByMType("359");
				if (dmList != null && !dmList.isEmpty()) {
					farmerDynamicData = getFarmerDynamicFields(dynamicFields, dynamicList, photoList, tenantId,
							dmList.get(0), finalFarm.getId());
					farmerDynamicData.setReferenceId(String.valueOf(finalFarm.getId()));
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
					farmerDynamicData.setEntityId(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()));
					farmerDynamicData.setTxnType(head.getTxnType());
					farmerDynamicData.setSeason(existingFarmer.getSeasonCode());
					farmerDynamicData.setIsScore(dmList.get(0).getIsScore());
					farmerDynamicData.setActStatus(0);
					farmerDynamicData.setScoreValue(new HashMap<>());
					LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap = new LinkedHashMap<>();
				dmList.get(0).getDynamicFieldConfigs().stream().forEach(section -> {
					section.getField().setfOrder(section.getOrder());
					fieldConfigMap.put(section.getField().getCode(), section.getField());
					if (section.getDynamicFieldScoreMap() != null && !section.getDynamicFieldScoreMap().isEmpty()) {
						farmerDynamicData.getScoreValue().put(section.getField().getCode(), section
								.getDynamicFieldScoreMap().stream()
								.collect(Collectors.toMap(DynamicFieldScoreMap::getCatalogueCode, u -> String
										.valueOf(String.valueOf(u.getScore()) + "~" + String.valueOf(u.getPercentage())))));
					}

				});
					
					
				farmerService.saveOrUpdate(farmerDynamicData, new HashMap<>(), fieldConfigMap);
					farmerService.deleteChildObjects(farmerDynamicData.getTxnType());
				}

			}
			
		} catch (SwitchException se) {
			se.printStackTrace();
			throw new SwitchException(se.getCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		/** FORM RESPONSE DATA **/
		Map resp = new HashMap();
		return resp;
	}
	
	private FarmerDynamicData getFarmerDynamicFields(Collection dynamicFields, Collection dynamicList,
			Collection photoList, String tenantId, DynamicFeildMenuConfig dm, long farmer) {
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
				farmerDynamicFieldsValue
				.setGrade(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getGrade() : null);

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
						farmerDynamicFieldsValue.setTxnType("359");
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
				farmerDynamicFieldsValue
				.setGrade(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getGrade() : null);
				
				farmerDynamicFieldsValue.setListMethod(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType() != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()
								: "");
				
				farmerDynamicFieldsValue.setParentId(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() != null
								? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getReferenceId() : 0);
				
				farmerDynamicFieldsValue.setTxnType("359");
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
				FarmerDynamicFieldsValue farmerDynamicFieldsValue = null;
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
								farmerDynamicFieldsValue
								.setGrade(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getGrade() : null);
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
				farmerDynamicFieldsValue.setTxnType("359");
				farmerDynamicFieldsValue.setDymamicImageData(imageDataSet);
				farmerDynamicFieldsValue.setReferenceId(String.valueOf(farmer));
				farmerDynamicFieldsValueList.add(farmerDynamicFieldsValue);

			}
		}
		farmerDynamicData.setFarmerDynamicFieldsValues(new HashSet<>(farmerDynamicFieldsValueList));

		return farmerDynamicData;

	}

	public Map<String, FarmerSourceIncome> getFarmerSourceIncome(Map farmerSourceIncomeMap,
			Collection sourceIncomeCollection, String sourceIncomeType)
			throws OfflineTransactionException, IOException {

		Map<String, FarmerSourceIncome> farmerSourceMap = new LinkedHashMap<String, FarmerSourceIncome>();
		/*
		 * Set<FarmerSourceIncome> farmerSourceIncomeInfo = new
		 * HashSet<FarmerSourceIncome>(); Map requestData = (Map)
		 * farmerSourceIncomeMap.get(REQUEST_DATA);
		 */

		FarmerSourceIncome farmerSourceIncomeDetails = null;
		Boolean dataFound = false;
		if (!ObjectUtil.isEmpty(sourceIncomeCollection)) {
			Set<FarmerIncomeDetails> farmerIncomeDetailSet = new LinkedHashSet<FarmerIncomeDetails>();
			List<com.sourcetrace.eses.txn.schema.Object> sourceIncomeObjects = sourceIncomeCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object sourceIncomeObj : sourceIncomeObjects) {

				List<Data> sourceIncomeList = sourceIncomeObj.getData();
				farmerSourceIncomeDetails = new FarmerSourceIncome();
				FarmerIncomeDetails farmerIncomeDetails = new FarmerIncomeDetails();
				for (Data sourceIncomeData : sourceIncomeList) {
					String key = sourceIncomeData.getKey();
					String value = sourceIncomeData.getValue();

					if (TxnEnrollmentProperties.SOURCE_OF_INCOME_AGRI_ACTIVITIES.equalsIgnoreCase(key)
							|| TxnEnrollmentProperties.SOURCE_OF_INCOME_HORICULTURE.equalsIgnoreCase(key)
							|| TxnEnrollmentProperties.SOURCE_OF_INCOME_ALLIED_SECTOR.equalsIgnoreCase(key)
							|| TxnEnrollmentProperties.SOURCE_OF_INCOME_EMPLOYMENT.equalsIgnoreCase(key)
							|| TxnEnrollmentProperties.SOURCE_OF_INCOME_OTHERS.equalsIgnoreCase(key)) {
						farmerIncomeDetails.setSourceName(!StringUtil.isEmpty(value) ? value : "");
					} else if (TxnEnrollmentProperties.INCOME_PER_ANNUM_AGRI_ACTIVITIES.equalsIgnoreCase(key)
							|| TxnEnrollmentProperties.INCOME_PER_ANNUM_HORICULTURE.equalsIgnoreCase(key)
							|| TxnEnrollmentProperties.INCOME_PER_ANNUM_ALLIED_SECTOR.equalsIgnoreCase(key)
							|| TxnEnrollmentProperties.INCOME_PER_ANNUM_EMPLOYMENT.equalsIgnoreCase(key)
							|| TxnEnrollmentProperties.INCOME_PER_ANNUM_OTHERS.equalsIgnoreCase(key)) {
						farmerIncomeDetails.setSourceIncome(!StringUtil.isEmpty(value) ? Double.valueOf(value) : 0.00);
					}
				}
				if (sourceIncomeList.size() > 0) {
					farmerIncomeDetailSet.add(farmerIncomeDetails);
					dataFound = true;
				} else {
					dataFound = false;
				}
			}
			if (dataFound) {
				farmerSourceIncomeDetails.setFarmerIncomeDetails(farmerIncomeDetailSet);
				farmerSourceIncomeDetails.setName(sourceIncomeType);
			}
		}
		if (dataFound)
			farmerSourceMap.put(FARMER_SOURCE_INCOME, farmerSourceIncomeDetails);
		else
			farmerSourceMap.put(FARMER_SOURCE_INCOME, null);
		return farmerSourceMap;
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

	/**
	 * Builds the int value.
	 * 
	 * @param value
	 *            the value
	 * @return the int
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int buildIntValue(String value) throws OfflineTransactionException, IOException {

		try {
			return (!StringUtil.isEmpty(value) ? Integer.parseInt(value) : 0);
		} catch (Exception e) {
			throwError(ITxnErrorCodes.DATA_CONVERSION_ERROR);
		}
		return 0;
	}

	/**
	 * Gets the farmer family.
	 * 
	 * @param familyMap
	 *            the family map
	 * @return the farmer family
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public Set<FarmerFamily> getFarmerFamily(Map familyMap,String tenantId) throws OfflineTransactionException, IOException {


		Set<FarmerFamily> farmerFamily = new HashSet<FarmerFamily>();
		Map requestData = (Map) familyMap.get(REQUEST_DATA);
		Collection familyCollection = (Collection) requestData.get(TxnEnrollmentProperties.FAMILY_MEMBERS);
		List<com.sourcetrace.eses.txn.schema.Object> familyObject = familyCollection.getObject();
		if(!tenantId.equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)){
		for (com.sourcetrace.eses.txn.schema.Object object : familyObject) {
			List<Data> familyData = object.getData();
			FarmerFamily family = new FarmerFamily();
			family.setEducation(String.valueOf(SELECT));
			for (Data data : familyData) {
				String key = data.getKey();
				String value = (String) data.getValue();
				if (TxnEnrollmentProperties.FAMILY_NAME.equalsIgnoreCase(key)) {
					validate(value, SwitchErrorCodes.EMPTY_FAMILY_MEMBERS_NAME);
					family.setName(value);
				}
				if (TxnEnrollmentProperties.FAMILY_AGE.equalsIgnoreCase(key))
					family.setAge(buildIntValue(value));
				if (TxnEnrollmentProperties.FAMILY_GENDER.equalsIgnoreCase(key)) {
					validate(value, SwitchErrorCodes.EMPTY_GENDER);
					family.setGender(value.equals("1") ? Farmer.SEX_MALE : Farmer.SEX_FEMALE);
				}
				if (TxnEnrollmentProperties.FAMILY_REALTION.equalsIgnoreCase(key)) {
					validate(value, SwitchErrorCodes.EMPTY_FARMER_RELATION);

					family.setRelation(value);

				}
				if (TxnEnrollmentProperties.FAMILY_EDUCATION.equalsIgnoreCase(key)) {

					family.setEducation(value);

				}
				if (TxnEnrollmentProperties.PROFESSION.equalsIgnoreCase(key))
					family.setProfession(buildDDIntValue(value));
				if (TxnEnrollmentProperties.OTHER_PROFESSION.equalsIgnoreCase(key))
					family.setOtherProfession(value);
			}
			family.setFarmer((Farmer) familyMap.get(FARMER));
			farmerFamily.add(family);
		}
		}
		else{
			familyCollection = (Collection) requestData.get(TxnEnrollmentProperties.FARMER_FAMILY);
			familyObject = familyCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object object : familyObject) {
				List<Data> familyData = object.getData();
				FarmerFamily family = new FarmerFamily();
				
				for (Data data : familyData) {
					String key = data.getKey();
					String value = (String) data.getValue();
					if (TxnEnrollmentProperties.FAMILY_MEMBER_NAME.equalsIgnoreCase(key)) {
						validate(value, SwitchErrorCodes.EMPTY_FAMILY_MEMBERS_NAME);
						family.setName(value);
					}
					
					if (TxnEnrollmentProperties.FAMILY_MEMBER_GENDER.equalsIgnoreCase(key)) {
						validate(value, SwitchErrorCodes.EMPTY_GENDER);
						family.setGender(value);
					}
					
					if (TxnEnrollmentProperties.FAMILY_MEMBER_AGE.equalsIgnoreCase(key))
						validate(value, SwitchErrorCodes.EMPTY_AGE);
						family.setAge(buildIntValue(value));
					
					
					if (TxnEnrollmentProperties.FAMILY_MEMBER_RELATION.equalsIgnoreCase(key)) {
						validate(value, SwitchErrorCodes.EMPTY_FARMER_RELATION);
						family.setRelation(value);
					}
					
					if (TxnEnrollmentProperties.FAMILY_MEMBER_EDUCATION.equalsIgnoreCase(key)) {
						family.setEducation(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.FAMILY_MEMBER_DISABILITY.equalsIgnoreCase(key)) {
						family.setDisability(buildIntValue(value));
					}
					
					if (TxnEnrollmentProperties.FARMILY_MEMBER_MARITAL.equalsIgnoreCase(key)) {
						family.setMaritalStatus(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.FARMILY_MEMBER_EDU_STATUS.equalsIgnoreCase(key)) {
						family.setEducationStatus(!StringUtil.isEmpty(value) ? value : "");
					}
					if(TxnEnrollmentProperties.FAMILY_MEMBER_DISABILITY_DETAIL.equalsIgnoreCase(key)){
						family.setDisableDetail(value);
				}
					
					
				}
				family.setFarmer((Farmer) familyMap.get(FARMER));
				farmerFamily.add(family);
			}
		}
		return farmerFamily;

	
	}

	/**
	 * Gets the farm.
	 * 
	 * @param farmMap
	 *            the farm map
	 * @return the farm
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public Map getFarm(Map farmMap,String plotCaptureTime) throws OfflineTransactionException, IOException {
		int farmNameSeq=1;
		int farmSize=0;
		int intSeq=0;
		Map reqData = (Map) farmMap.get(REQUEST_DATA); // Farm
		Farmer farmer = (Farmer) farmMap.get(FARMER); // Farmer
		Head headData =(Head) farmMap.get(HEAD);   //Head
		
		Map farmObjectMap = new HashMap();
		Set<Farm> farms = new HashSet<Farm>();
		Set<HarvestData> harvestDatas = new HashSet<HarvestData>();
		Map<String, Farm> farmRefMap = new HashMap<String, Farm>();
		HousingInfo housingInfo=new HousingInfo();

		Date formatedDate = DateUtil.convertStringToDate(plotCaptureTime, DateUtil.TXN_DATE_TIME);
		String serialNumber = (String) headData.getSerialNo();
		Device device = deviceService.findDeviceBySerialNumber(serialNumber);
		Agent agent = agentService.findAgentByProfileAndBranchId(headData.getAgentId(), device.getBranchId());
		AgentAccessLog agentAccessLog = agentService.findAgentAccessLogByAgentId(agent.getProfileId(),
				DateUtil.getDateWithoutTime(formatedDate));			
			
		BeanWrapper wrapper = new BeanWrapperImpl(TransactionProperties.HEAD);
		
		Collection farmCollection = (Collection) reqData.get(TxnEnrollmentProperties.FARM_LIST);
		if (!ObjectUtil.isEmpty(farmCollection)) {
			List<com.sourcetrace.eses.txn.schema.Object> farmObjects = farmCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object farmObject : farmObjects) {
				List<Data> farmData = farmObject.getData();
				Farm farm = new Farm();
				Date insDate = null;
				Set<FarmIcsConversion> icsConversion = new HashSet<FarmIcsConversion>();
				FarmIcsConversion conversion = new FarmIcsConversion();
				farmMap.put(FARM, farm);
				FarmDetailedInfo detailInfo = new FarmDetailedInfo();
				// Default values for farm non-mandatory values
				detailInfo.setFarmOwned(SELECT_MULTI);
				detailInfo.setLandGradient(SELECT_MULTI);
				detailInfo.setIrrigationSource(SELECT_MULTI);
				detailInfo.setIrrigationMethod(SELECT);
				detailInfo.setFarmIrrigation(SELECT_MULTI);
				detailInfo.setSoilType(SELECT_MULTI);
				detailInfo.setSoilTexture(SELECT_MULTI);
				detailInfo.setSoilFertility(SELECT);
				detailInfo.setLandUnderICSStatus(SELECT);
				for (Data data : farmData) {
					String key = data.getKey();
					String value = data.getValue();
					if (StringUtil.isEmpty(key)) {
						break;
					}
					if (TxnEnrollmentProperties.FARM_CODE.equalsIgnoreCase(key)) {
						validate(value, ITxnErrorCodes.EMPTY_FARM_CODE);
						Farm existingFarm = farmerService.findFarmByCode(value);
						if (!ObjectUtil.isEmpty(existingFarm) || farmRefMap.containsKey(value))
							throwError(ITxnErrorCodes.FARM_CODE_EXIST);
						farm.setFarmCode(value);
					}
					if (TxnEnrollmentProperties.FARM_NAME.equalsIgnoreCase(key)) {
						validate(value, ITxnErrorCodes.EMPTY_FARM_NAME);
						
						farm.setFarmName(value);
						
					}
					if (TxnEnrollmentProperties.IRRI_ACRE.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value)) {
								farm.setIrrigationLand(value);;
							}
					}
					if (TxnEnrollmentProperties.RAIN_FED.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value)) {
								farm.setOwnLand(value);
							}
					}
					if (TxnEnrollmentProperties.FALLOW_ACRE.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value)) {
								farm.setLeasedLand(value);
							}
					}
					

                    if (TxnEnrollmentProperties.VILLAGE_CODE.equalsIgnoreCase(key)){
                        if(!StringUtil.isEmpty(value)){
                            Village village = locationService.findVillageByCode(value);
                            if(!ObjectUtil.isEmpty(village)){
                                farm.setVillage(village);
                            }
                        }
                    }
                   
                    if (TxnEnrollmentProperties.SAMITHI_CODE.equalsIgnoreCase(key)){
                        if(!StringUtil.isEmpty(value)){
                            Warehouse warehouse = locationService.findSamithiByCode(value);
                            if(!ObjectUtil.isEmpty(warehouse)){
                                farm.setSamithi(warehouse);
                            }
                        } 
                    }
                    
                    if(!StringUtil.isEmpty(plotCaptureTime)){
    					farm.setPlotCapturingTime(DateUtil.convertStringToDate(plotCaptureTime, DateUtil.TXN_DATE_TIME));
    				}
                    
                    if (TxnEnrollmentProperties.FPOGROUP.equalsIgnoreCase(key)) {
                        farm.setFpo(!StringUtil.isEmpty(value) ? value : "");
                    }
                    if (TxnEnrollmentProperties.DISTANCE_PROCESS_UNIT.equalsIgnoreCase(key)) {
                        farm.setDistanceProcessingUnit(!StringUtil.isEmpty(value) ? value : "");
                    }
                    if (TxnEnrollmentProperties.FARM_OWNED_OTHER.equalsIgnoreCase(key)) {
						farm.setFarmOther(!StringUtil.isEmpty(value) ? value : "");
					}
                    if (TxnEnrollmentProperties.WATER_SRC.equalsIgnoreCase(key)) {
						farm.setWaterSource(!StringUtil.isEmpty(value) ? value : "");
					}
                    if (TxnEnrollmentProperties.PROCESSING_ACTIVITY.equalsIgnoreCase(key))
						//detailInfo.setMilletCultivated(Integer.parseInt(value));					
        					if (!StringUtil.isEmpty(value) && value != "") {
                                detailInfo.setProcessingActivity(Integer.parseInt(value));
                            } else {
                                detailInfo.setProcessingActivity(-1);
                            }
                    
					if (TxnEnrollmentProperties.SURVEY_NUMBER.equalsIgnoreCase(key))
						detailInfo.setSurveyNumber(value);
					if (TxnEnrollmentProperties.IS_SAME_AS_FARMER_ADDRESS.equalsIgnoreCase(key))
						detailInfo.setSameAddressofFarmer(buildIntValue(value) == 1 ? true : false);
					if (TxnEnrollmentProperties.FARM_ADDRESS.equalsIgnoreCase(key))
						detailInfo.setFarmAddress(value);
					if (TxnEnrollmentProperties.TOTAL_LAND_HOLDING_AREA.equalsIgnoreCase(key)){
						detailInfo.setTotalLandHolding(value);
                           if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) ){
                        		if(value!=null && !StringUtil.isEmpty(value) ){
                        			AgentAccessLogDetail agentAccessLogDetail = agentService
        									.findAgentAccessLogDetailByTxn(agentAccessLog.getId(), "3082");
        							if (!StringUtil.isEmpty(agentAccessLogDetail)) {
        								Long txnCount = agentAccessLogDetail.getTxnCount() + 1L;
        								agentAccessLogDetail.setTxnCount(txnCount);
        								agentAccessLogDetail.setTxnDate(formatedDate);
        								agentService.update(agentAccessLogDetail);
        							}
        							else{
							AgentAccessLogDetail agentAccessLogDetailNew = new AgentAccessLogDetail();
							agentAccessLogDetailNew.setAgentAccessLog(agentAccessLog);
							/*agentAccessLogDetailNew
									.setTxnType((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE));*/
							agentAccessLogDetailNew.setTxnType("3082");
							agentAccessLogDetailNew
									.setMessageNumber(headData.getMsgNo());
							agentAccessLogDetailNew.setTxnMode(headData.getMode());

							agentAccessLogDetailNew.setTxnDate(formatedDate);

							agentAccessLogDetailNew.setTxnCount(1L);

							agentAccessLogDetailNew.setServicePoint(
									headData.getServPointId());

							agentService.save(agentAccessLogDetailNew);
							}
                        		}
                           }

					}
					if (TxnEnrollmentProperties.PROPOSED_PLANTING_AREA.equalsIgnoreCase(key))
						detailInfo.setProposedPlantingArea(value);
					if (TxnEnrollmentProperties.FARM_OWNED.equalsIgnoreCase(key))
						detailInfo.setFarmOwned(StringUtil.isEmpty(value) ? "-1" : value);
					if (TxnEnrollmentProperties.LAND_GRADIENT.equalsIgnoreCase(key))
						detailInfo.setLandGradient(value);
					if (TxnEnrollmentProperties.APPROACH_ROAD.equalsIgnoreCase(key))
						detailInfo.setApproachRoad(value);
					if (TxnEnrollmentProperties.REGISTRATION_YEAR.equalsIgnoreCase(key)) {
						// validate(value,
						// ITxnErrorCodes.EMPTY_FARM_REGISTRATION_YEAR);
						detailInfo.setRegYear(!StringUtil.isEmpty(value) ? value : null);
					}
					if (TxnEnrollmentProperties.SOIL_TYPE.equalsIgnoreCase(key))
						detailInfo.setSoilType(!StringUtil.isEmpty(value) ? value : null);
					if (TxnEnrollmentProperties.SOIL_TEXTURE.equalsIgnoreCase(key))
						detailInfo.setSoilTexture(!StringUtil.isEmpty(value) ? value : null);
					if (TxnEnrollmentProperties.FERTILITY_STATUS.equalsIgnoreCase(key))
						detailInfo.setSoilFertility(buildDDIntValue(!StringUtil.isEmpty(value) ? value : null));
					if (TxnEnrollmentProperties.IRRIGATION_SOURCEZ.equalsIgnoreCase(key))
						detailInfo.setFarmIrrigation(!StringUtil.isEmpty(value) ? value : null);
					if (TxnEnrollmentProperties.IRRIGATION_SOURCE_TYPES.equalsIgnoreCase(key))
						detailInfo.setIrrigationSource(!StringUtil.isEmpty(value) ? value : null);
					if (TxnEnrollmentProperties.IRRIGATION_SOURCE_TYPES_OTHER.equalsIgnoreCase(key))
						detailInfo.setIrrigatedOther(!StringUtil.isEmpty(value) ? value : null);
					if (TxnEnrollmentProperties.METHOD_OF_IRRIGATION.equalsIgnoreCase(key))
						detailInfo.setMethodOfIrrigation(!StringUtil.isEmpty(value) ? value : null);
					if (TxnEnrollmentProperties.ATTEND_FFS.equalsIgnoreCase(key))
						//detailInfo.setFarmerFieldSchool(Integer.parseInt(value));
					
        					if (!StringUtil.isEmpty(value) && value != "") {
                                detailInfo.setFarmerFieldSchool(Integer.parseInt(value));
                            } else {
                                detailInfo.setFarmerFieldSchool(-1);
                            }

					if (TxnEnrollmentProperties.IS_FFS_BENIFITED.equalsIgnoreCase(key))
						detailInfo.setIsFFSBenifited(value);

					if (TxnEnrollmentProperties.BOREWELL_STRUCTURE.equalsIgnoreCase(key))
						//detailInfo.setBoreWellRechargeStructure(Integer.parseInt(value) );					
        					if (!StringUtil.isEmpty(value) && value != "") {
                                detailInfo.setBoreWellRechargeStructure(Integer.parseInt(value));
                            } else {
                                detailInfo.setBoreWellRechargeStructure(-1);
                            }

					if (TxnEnrollmentProperties.MILLET_CULTIVATED.equalsIgnoreCase(key))
						//detailInfo.setMilletCultivated(Integer.parseInt(value));					
        					if (!StringUtil.isEmpty(value) && value != "") {
                                detailInfo.setMilletCultivated(Integer.parseInt(value));
                            } else {
                                detailInfo.setMilletCultivated(-1);
                            }

					if (TxnEnrollmentProperties.MILLET_CROP_TYPE.equalsIgnoreCase(key))
						detailInfo.setMilletCropType(!StringUtil.isEmpty(value) ? value : null);

					if (TxnEnrollmentProperties.MILLET_CROP_COUNT.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value) && value != "") {
							detailInfo.setMilletCropCount(Integer.parseInt(value));
						} else {
							detailInfo.setMilletCropCount(-1);
						}
					}
					if (TxnEnrollmentProperties.FARM_PHOTO.equalsIgnoreCase(key)) {
						try {
							// Adding farm photo
							DataHandler farmPhotoDataHandler = (DataHandler) data.getBinaryValue();
							if (!ObjectUtil.isEmpty(farmPhotoDataHandler)) {
								byte[] farmPhotoData = IOUtils.toByteArray(farmPhotoDataHandler.getInputStream());
								if (farmPhotoData != null) {
									farm.setPhoto(farmPhotoData);
									// farm.getFarmer().setBasicInfo(1);
								} /*
									 * else { throwError(SwitchErrorCodes.
									 * EMPTY_FARM_PHOTO); }
									 */
							} /*
								 * else {
								 * throwError(SwitchErrorCodes.EMPTY_FARM_PHOTO)
								 * ; }
								 */
						} catch (IOException e) {
							LOGGER.info(e.getMessage());
							e.printStackTrace();
						}
					}
					if (TxnEnrollmentProperties.FARM_PHOTO_CAPTURING_TIME.equalsIgnoreCase(key)) {
						//validate(value, ITxnErrorCodes.EMPTY_FARM_PHOTO_CAPTURE_DT);
						if (!StringUtil.isEmpty(value)) {// Adding farm photo
															// capturing time
							try {
								Date photoCaptureDate = TXN_DATE_FORMAT.parse(value);
								farm.setPhotoCaptureTime(photoCaptureDate);
							} catch (Exception e) {
								LOGGER.info(e.getMessage());
							}
						}
					}
					if (TxnEnrollmentProperties.FARM_LATITUDE.equalsIgnoreCase(key)) {
						//validate(value, ITxnErrorCodes.EMPTY_FARM_PHOTO_LATITUDE);
						farm.setLatitude(value);
                        if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) ){
                        	
                        	if(value!=null && !StringUtil.isEmpty(value) ){
                 
                        		AgentAccessLogDetail agentAccessLogDetail = agentService
    									.findAgentAccessLogDetailByTxn(agentAccessLog.getId(), "3081");
    							if (!StringUtil.isEmpty(agentAccessLogDetail)) {
    								Long txnCount = agentAccessLogDetail.getTxnCount() + 1L;
    								agentAccessLogDetail.setTxnCount(txnCount);
    								agentAccessLogDetail.setTxnDate(formatedDate);
    								agentService.update(agentAccessLogDetail);
    							}
    							else{
	
							AgentAccessLogDetail agentAccessLogDetailNew = new AgentAccessLogDetail();
							agentAccessLogDetailNew.setAgentAccessLog(agentAccessLog);
							/*agentAccessLogDetailNew
									.setTxnType((String) wrapper.getPropertyValue(ITxnMessageUtil.TXN_TYPE));*/
							agentAccessLogDetailNew.setTxnType("3081");
							agentAccessLogDetailNew
									.setMessageNumber(headData.getMsgNo());
							agentAccessLogDetailNew.setTxnMode(headData.getMode());

							agentAccessLogDetailNew.setTxnDate(formatedDate);

							agentAccessLogDetailNew.setTxnCount(1L);

							agentAccessLogDetailNew.setServicePoint(
									headData.getServPointId());

							agentService.save(agentAccessLogDetailNew);
							}
                        	}
                        }
					}
					if (TxnEnrollmentProperties.FARM_LONGITUDE.equalsIgnoreCase(key)) {
						//validate(value, ITxnErrorCodes.EMPTY_FARM_PHOTO_LONGITUDE);
						farm.setLongitude(value);
					}

					if (TxnEnrollmentProperties.IFS_PRACTICES.equalsIgnoreCase(key)) {
						farm.setIfs(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.IFS_PRACTICES_OTHERS.equalsIgnoreCase(key)) {
						farm.setIfsOther(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.KITCHEN_GARDEN_VEGETABLES.equalsIgnoreCase(key)) {
						farm.setVegetableName(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.KITCHEN_GARDEN_USES.equalsIgnoreCase(key)) {
						farm.setKitchenGarden(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.BACK_YARD_POULTRY.equalsIgnoreCase(key)) {
						farm.setBackYardPoultry(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.PROGRAMS_OR_SCHEMES_AVAILED_LIST.equalsIgnoreCase(key)) {
						// Processing Farm Programs/Schemes availed
						if (data.getCollectionValue() != null) {
							farm.setFarmerScheme(buildFarmSchemes(data.getCollectionValue(), farm));
						}
					}
					if (TxnEnrollmentProperties.ADOPTED_IFS_PRACTICES_LIST.equalsIgnoreCase(key)) {
						// Processing Farm Adopted IFS Practices
						if (data.getCollectionValue() != null) {
							farm.setFarmerLandDetails(buildFarmerLandDetails(data.getCollectionValue(), farm));
						}
					}
					if (TxnEnrollmentProperties.SOIL_CONSERVATION.equalsIgnoreCase(key)) {
						farm.setSoilConservation(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.SOIL_CONSERVATION_OTHERS.equalsIgnoreCase(key)) {
						farm.setSoilConservationOther(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.WATER_CONSERVATION.equalsIgnoreCase(key)) {
						farm.setWaterConservation(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.WATER_CONSERVATION_OTHERS.equalsIgnoreCase(key)) {
						farm.setWaterConservationOther(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.PLANTS_OR_TREES_LIST.equalsIgnoreCase(key)) {
						// Processing Farm Plants/Trees
						if (data.getCollectionValue() != null) {
							farm.setFarmerPlants(buildFarmPlantsOrTrees(data.getCollectionValue(), farm));
						}
					}
					if (TxnEnrollmentProperties.SERVICE_CENTRES.equalsIgnoreCase(key)) {
						farm.setServiceCentres(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.SERVICE_CENTRES_OTHERS.equalsIgnoreCase(key)) {
						farm.setServiceCentresOther(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.SOIL_TESTING.equalsIgnoreCase(key)) {
						farm.setSoilTesting(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.SOIL_TESTING_OFFICERS_SUGGESTIONS_LIST.equalsIgnoreCase(key)) {
						// Processing Farm Soil testing officers suggestions
						if (data.getCollectionValue() != null) {
							farm.setFarmerSoilTesting(buildFarmSoilTesting(data.getCollectionValue(), farm));
						}
					}
					if (TxnEnrollmentProperties.TRAININGS_AND_PROGRAMS.equalsIgnoreCase(key)) {
						farm.setTrainingProgram(!StringUtil.isEmpty(value) ? value : "");
					}
					if(TxnEnrollmentProperties.FARM_CERTIFICATION_YEAR.equalsIgnoreCase(key)){
						farm.setCertYear(!StringUtil.isEmpty(value)?Integer.parseInt(value):0);
					}
					 if (TxnEnrollmentProperties.NO_CROTREE.equalsIgnoreCase(key)) {
						 farm.setNoOfCrotenTrees(!StringUtil.isEmpty(value) ? value : "");
	                    }
					 
					 if (tenantId.equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
							if (TxnEnrollmentProperties.PRE_BANANA_TREE.equalsIgnoreCase(key)) {
								farm.setPresenceBananaTrees(!StringUtil.isEmpty(value) ? value : "");
							}

							if (TxnEnrollmentProperties.PARALLEL_PROD.equalsIgnoreCase(key)) {
								farm.setParallelProd(!StringUtil.isEmpty(value) ? value : "");
							}

							if (TxnEnrollmentProperties.INPUT_ORG_UNIT.equalsIgnoreCase(key)) {
								farm.setInputOrganicUnit(!StringUtil.isEmpty(value) ? value : "");
							}

							if (TxnEnrollmentProperties.PRE_HIRED_LABOUR.equalsIgnoreCase(key)) {
								farm.setPresenceHiredLabour(!StringUtil.isEmpty(value) ? value : "");
							}

							if (TxnEnrollmentProperties.RISK_CATE.equalsIgnoreCase(key)) {
								farm.setRiskCategory(!StringUtil.isEmpty(value) ? value : "");
							}


						}
					 
					int isCertificationType = farmer.getIsCertifiedFarmer();
					if (Farmer.CERTIFIED_YES == isCertificationType||getTenantId().equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {

						// Farm Labour
						if (TxnEnrollmentProperties.FULL_TIME_WORKERS_COUNT.equalsIgnoreCase(key))
							detailInfo.setFullTimeWorkersCount(value);
						if (TxnEnrollmentProperties.PART_TIME_WORKERS_COUNT.equalsIgnoreCase(key))
							detailInfo.setPartTimeWorkersCount(value);
						if (TxnEnrollmentProperties.SEASONAL_WORKERS_COUNT.equalsIgnoreCase(key))
							detailInfo.setSeasonalWorkersCount(value);

						// Conversion Information
						if (TxnEnrollmentProperties.CHEMICAL_APPLICATION_LAST_DATE.equalsIgnoreCase(key))
							detailInfo.setLastDateOfChemicalApplication(value);
						if (TxnEnrollmentProperties.CONVENTIONAL_LAND.equalsIgnoreCase(key))
							detailInfo.setConventionalLand(value);
						if (TxnEnrollmentProperties.CONVENTIONAL_CROP.equalsIgnoreCase(key))
							detailInfo.setConventionalCrops(value);
						if (TxnEnrollmentProperties.CONVENTIONAL_ESTIMATED_YIELD.equalsIgnoreCase(key))
							detailInfo.setConventionalEstimatedYield(value);
						if (TxnEnrollmentProperties.PASTURE_LAND.equalsIgnoreCase(key))
							detailInfo.setFallowOrPastureLand(value);

						//Field History
						Date lastDateChem = null;
						if (TxnEnrollmentProperties.LAST_DATE_CHEMICAL.equalsIgnoreCase(key)) {
							if (!StringUtil.isEmpty(value)) {
								lastDateChem = DateUtil.convertStringToDate(value, DateUtil.DATE);
								detailInfo.setLastDateOfChemical(lastDateChem);
							}
						}	
						if (TxnEnrollmentProperties.FIEDL_NAME.equalsIgnoreCase(key))
							detailInfo.setFieldName(value);
						if (TxnEnrollmentProperties.FIEDL_CROP.equalsIgnoreCase(key))
							detailInfo.setFieldCrop(value);
						if (TxnEnrollmentProperties.FIELD_AREA.equalsIgnoreCase(key))
							detailInfo.setFieldArea(value);
						if (TxnEnrollmentProperties.QUANTITY_APPLIED.equalsIgnoreCase(key))
							detailInfo.setQuantityApplied(value);
						if (TxnEnrollmentProperties.INPUT_APP.equalsIgnoreCase(key))
							detailInfo.setInputApplied(value);
						if (TxnEnrollmentProperties.INPUT_SOURCE.equalsIgnoreCase(key))
							detailInfo.setActivitiesInCoconutFarming(value);
						if (TxnEnrollmentProperties.COCONUT_FARMING.equalsIgnoreCase(key))
							detailInfo.setInputSource(value);			
						// Land ICS details
						if (TxnEnrollmentProperties.ICS_LIST.equalsIgnoreCase(key)) {
							farm.setFarmICS(buildFarmICSDatas(data.getCollectionValue(), farm));
						}
						
						
					/*	// Farm Inventories
						if (TxnEnrollmentProperties.FARM_INVENTORIES.equalsIgnoreCase(key)) {
							farmMap.put(INVENTORY_COLLECTION, data.getCollectionValue());
							farm.setFarmInventory(getInventories(farmMap));
						}*/

						// Farm Animals
						/*if (TxnEnrollmentProperties.FARM_ANIMALS.equalsIgnoreCase(key)) {
							farmMap.put(ANIMAL_COLLECTION, data.getCollectionValue());
							farm.setAnimalHusbandary(getAnimals(farmMap));
						}*/

						// Land ICS details
						if (TxnEnrollmentProperties.ICS_LIST.equalsIgnoreCase(key)) {
							farm.setFarmICS(buildFarmICSDatas(data.getCollectionValue(), farm));
						}
						/*
						 * // Harvest Data if
						 * (TxnEnrollmentProperties.HARVEST_LIST.
						 * equalsIgnoreCase(key)) {
						 * farmMap.put(HARVEST_COLLECTION,
						 * data.getCollectionValue()); for (HarvestData harvest
						 * : getHarvestList(farmMap)) {
						 * harvestDatas.add(harvest); } }
						 */
					}
					/*if (TxnEnrollmentProperties.FARM_CROPS_LIST.equalsIgnoreCase(key)) {
						// Farm Crops
						farmMap.put(FARM_CROP_COLLECTION, data.getCollectionValue());
						if(tenantId.equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID))
						{
							farm.setTreeDetails(setTreeDetails(farm,farmMap));
							farm.setFarmCrops(null);
						}
						else
						{
							farm.setFarmCrops(getFarmCrops(farmMap,plotCaptureTime));

						}

					}*/
					if (TxnEnrollmentProperties.FARM_HISTORY.equalsIgnoreCase(key)) {
						if(tenantId.equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID))
						{
							farm.setFarmerLandDetails(buildFarmHistory(data.getCollectionValue(), farm));
						}
					}
					
					if (TxnEnrollmentProperties.FARM_LAND_AREA_GPS.equalsIgnoreCase(key)) {
						// Processing Farm Land Area
						if (data.getCollectionValue() != null) {
							//farm.setCoordinates(buildFarmLandArea(data.getCollectionValue(), farm));
							CoordinatesMap coordinatesMap=new CoordinatesMap();
							coordinatesMap.setFarmCoordinates(buildFarmLandArea(data.getCollectionValue(), farm));
							Head hd = (Head) reqData.get(TransactionProperties.HEAD);
							coordinatesMap.setAgentId(hd.getAgentId());
							coordinatesMap.setArea(detailInfo.getTotalLandHolding());
							coordinatesMap.setDate(	farm.getPlotCapturingTime());
							coordinatesMap.setFarm(farm);
							coordinatesMap.setMidLatitude(farm.getLongitude());
							coordinatesMap.setMidLongitude(farm.getLongitude());
							coordinatesMap.setStatus(CoordinatesMap.Status.ACTIVE.ordinal());
							if(farm.getCoordinatesMap()!=null && !ObjectUtil.isEmpty(farm.getCoordinatesMap())){
							farm.getCoordinatesMap().stream().forEach(co->{
								co.setStatus(CoordinatesMap.Status.INACTIVE.ordinal());
							});
							farm.getCoordinatesMap().add(coordinatesMap);
							}else{
								Set<CoordinatesMap> coMap=new LinkedHashSet<>();
								coMap.add(coordinatesMap);
								farm.setCoordinatesMap(coMap);
							}
							
							
							farm.setActiveCoordinates(coordinatesMap);
						}
					}

					// Machinary
					if (TxnEnrollmentProperties.MACHINARY_LIST.equalsIgnoreCase(key)) {
						farmMap.put(MACHINARY_COLLECTION, data.getCollectionValue());
						farm.setFarmElementMach(getMachinary(farmMap));
					}

					// PolyHouse
					if (TxnEnrollmentProperties.POLY_HOUSE_LIST.equalsIgnoreCase(key)) {
						farmMap.put(POLY_COLLECTION, data.getCollectionValue());
						farm.setFarmElement(getPolyHouse(farmMap));
					}

					if (TxnEnrollmentProperties.SOIL_TESTED.equalsIgnoreCase(key)) {
						farm.setSoilTesting(!StringUtil.isEmpty(value) ? value : "");
					}
					
					//Plot No
					if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
					if (TxnEnrollmentProperties.FARM_PLOT_NO.equalsIgnoreCase(key)) {
						farm.setFarmPlatNo(!StringUtil.isEmpty(value) ? value : "");
					}
					}
					if (tenantId.equalsIgnoreCase(ESESystem.AVT)) {
						if (TxnEnrollmentProperties.FARM_PLOT_NO.equalsIgnoreCase(key)) {
							farm.setLandmark(!StringUtil.isEmpty(value) ? value : "");
						}
						}
					
					if (TxnEnrollmentProperties.CERTIFICATION_TYPE.equalsIgnoreCase(key)) {
						conversion.setScope(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.CONVERSION_STS.equalsIgnoreCase(key)) {
						conversion.setIcsType(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.DATE_INSPECTION.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value)) {
							insDate = DateUtil.convertStringToDate(value, DateUtil.DATE);
							conversion.setInspectionDate(insDate);
						}
					}

					/*
					 * if
					 * (TxnEnrollmentProperties.CHEMICAL_APPLICATION_LAST_DATE
					 * .equalsIgnoreCase(key)){ if (!StringUtil.isEmpty(value))
					 * { insDate = DateUtil.convertStringToDate(value,
					 * DateUtil.DATE);
					 * conversion.setLastDateChemicalApp(insDate); } }
					 */
					
					if (TxnEnrollmentProperties.INSPECTOR_NAME.equalsIgnoreCase(key)) {
						conversion.setInspectorName(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.IS_QUALIFIED.equalsIgnoreCase(key)) {
						conversion.setQualified(!StringUtil.isEmpty(value) ? Integer.valueOf(value) : 0);
					}
					if (TxnEnrollmentProperties.SANCTION_REASON.equalsIgnoreCase(key)) {
						conversion.setSanctionreason(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.SANCTION_DURATION.equalsIgnoreCase(key)) {
						conversion.setSanctionDuration(!StringUtil.isEmpty(value) ? value : "");
					}
					
					
					if (TxnEnrollmentProperties.REG_NO.equalsIgnoreCase(key)) {
						farm.setFarmRegNumber(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.WATER_HARVEST.equalsIgnoreCase(key)) {
						farm.setWaterHarvest(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.AVG_STORAGE.equalsIgnoreCase(key)) {
						farm.setAvgStore(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.TREE_NAME.equalsIgnoreCase(key)) {
						farm.setTreeName(!StringUtil.isEmpty(value) ? value : "");
					}
					if (TxnEnrollmentProperties.SOIL_TEST_REPORT.equalsIgnoreCase(key)) {
						if (data.getBinaryValue() != null) {
							Set<DocumentUpload> documentUploads = buildDocumentUploadDatas(data.getBinaryValue(), farm);
							if (!ObjectUtil.isEmpty(documentUploads))
								farm.setDocUpload(documentUploads);
						}
					}
					
					if (tenantId.equalsIgnoreCase(ESESystem.COFBOARD_TENANT_ID)){
						if (TxnEnrollmentProperties.FARM_PHOTO_ID.equalsIgnoreCase(key)) {
							farm.setPhotoId(!StringUtil.isEmpty(value) ? String.valueOf(value) : "");
						}
						
						if (TxnEnrollmentProperties.NO_OF_COFFEE_PLANTS.equalsIgnoreCase(key)) {
							farm.setDistanceProcessingUnit(!StringUtil.isEmpty(value) ? String.valueOf(value) : "");
						}
						
						if (TxnEnrollmentProperties.VERMI_UNIT.equalsIgnoreCase(key)) {
							farm.setPresenceBananaTrees(!StringUtil.isEmpty(value) ? String.valueOf(value) : "");
						}
						
					}

					if (TxnEnrollmentProperties.LAND_TOPOGRAPHY.equalsIgnoreCase(key))
						farm.setLandTopology(value);
					
						if(tenantId.equalsIgnoreCase(ESESystem.BLRI_TENANT_ID))
	                    {
		                 	  Set<HousingInfo> housingSet=new HashSet<>();
		                 	  if (TxnEnrollmentProperties.COW_NO.equalsIgnoreCase(key)) {
		                 	     housingInfo.setNoCowShad(!StringUtil.isEmpty(value) ? value : "0");
		                      }
		                 	  
		                 	 if (TxnEnrollmentProperties.HOUSING_TYPE.equalsIgnoreCase(key)) {
		                         housingInfo.setHousingShadType(value);
		                     }
		                 	 
		                 	 if (TxnEnrollmentProperties.COW_SPACE.equalsIgnoreCase(key)) {
		                         housingInfo.setSpacePerCow(value);
		                     }
		                 	 housingSet.add(housingInfo);
		                 	 
		                 	 farm.setHousingInfos(housingSet);
	                   }

				}
				if (!ObjectUtil.isEmpty(detailInfo)) {
					if (detailInfo.isSameAddressofFarmer())
						detailInfo.setFarmAddress(!StringUtil.isEmpty(farmer.getAddress()) ? farmer.getAddress() + ", "
								+ farmer.getCity().getName() + ", " + farmer.getVillage().getName()
								: farmer.getCity().getName() + ", " + farmer.getVillage().getName());
					farm.setFarmDetailedInfo(detailInfo);
				}
				
				if (farm.getPhoto()!=null) {
					farmer.setBasicInfo(1);
				}
				if (tenantId.equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {
					conversion.setIcsType("0");
				}
				if (!StringUtil.isEmpty(conversion.getIcsType())) {
					conversion.setIsActive(1);
					icsConversion.add(conversion);
					farm.setFarmICSConversion(icsConversion);
				}
				farm.setFarmer(farmer);
				/*if (tenantId.equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {*/
					if(!ObjectUtil.isEmpty(farm) && !ObjectUtil.isEmpty(farm.getFarmer().getIsCertifiedFarmer())
							&& farm.getFarmer().getIsCertifiedFarmer()==1 && 
							!StringUtil.isEmpty(conversion.getIcsType()) && conversion.getIcsType().equalsIgnoreCase("3")){
						conversion.setOrganicStatus("3");
					}else{
						conversion.setOrganicStatus("0");
					}
				/*}*/
				if(tenantId.equalsIgnoreCase(ESESystem.AWI_TENANT_ID)){
				    if(StringUtil.isEmpty(getAwiFarmCodeSeq())){
                        if (!ObjectUtil.isEmpty(farm.getVillage())) {
                            String codeGen = idGenerator.getFarmWebCodeIdSeq(
                                    farm.getVillage().getCity().getCode().substring(0, 1),
                                    farm.getVillage().getGramPanchayat().getCode().substring(0, 1));
                            farm.setFarmId(codeGen);
                            setAwiFarmCodeSeq(codeGen);
                        }
                    }else{
                        String temp = getAwiFarmCodeSeq();
                        if (!ObjectUtil.isEmpty(farm.getVillage())) {
                            String codePrefix = farm.getVillage().getCity().getCode().substring(0, 1)+farm.getVillage().getGramPanchayat().getCode().substring(0, 1);
                            if(!StringUtil.isEmpty(temp)&&codePrefix.equalsIgnoreCase(temp.substring(0,2))){
                                String seq= temp.substring(2, 6);
                                BigInteger bigInt = new BigInteger(seq.trim());
                                String seqVal=  bigInt.add(BigInteger.ONE).toString();
                                String farmCode =getAwiFarmCodeSeq().substring(0,2)+StringUtil.getExact(seqVal, 4);
                                farm.setFarmId(farmCode);
                                setAwiFarmCodeSeq(farmCode);
                            }else{
                                String codeGen = idGenerator.getFarmWebCodeIdSeq(
                                        farm.getVillage().getCity().getCode().substring(0, 1),
                                        farm.getVillage().getGramPanchayat().getCode().substring(0, 1));
                                farm.setFarmId(codeGen);
                                setAwiFarmCodeSeq(codeGen);
                            }
                        }
                    }
	            }
				if (tenantId.equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)) {
					if (farmer.getFarms().size() > 0) {
						farm.setFarmId(String
								.valueOf(farmer.getFarmerCode() + "-" + farmCodeFmt.get(farmer.getFarms().size())));
					} else {
						farm.setFarmId(String.valueOf(farmer.getFarmerCode() + "-" + farmCodeFmt.get(0)));
					}
				}

				if(tenantId.equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)){
					
					if(!ObjectUtil.isEmpty(farmer.getFarms().size())){
						farmSize=farmer.getFarms().size();
					}
					
					String vilSeqCode=(farm.getFarmer().getFarmerCode()+"/"+(farmSize+farmNameSeq));
					
					farm.setFarmName(vilSeqCode);
				
				}
				farm.setCreatedUsername(agent.getPersonalInfo().getAgentName());
				farm.setCreatedDate(DateUtil.convertStringToDate(headData.getTxnTime(), DateUtil.TXN_DATE_TIME));
					farm.setStatus(Farmer.Status.ACTIVE.ordinal());
				
				farm.setIsVerified(0);
				farm.setRevisionNo(DateUtil.getRevisionNumber());
				finalFarm = farm;
				farms.add(farm);
				farmRefMap.put(farm.getFarmCode(), farm);
			}
		}
		farmObjectMap.put(FARM, farms);
		farmObjectMap.put(HARVEST_DATA, harvestDatas);
		
		return farmObjectMap;
	}

	private Set<FarmerScheme> buildFarmSchemes(Collection collection, Farm farm) {

		Set<FarmerScheme> returnSet = null;
		if (!ObjectUtil.isEmpty(collection)) {
			returnSet = new LinkedHashSet<FarmerScheme>();
			List<com.sourcetrace.eses.txn.schema.Object> farmerSchemeList = collection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object farmerSchemeObj : farmerSchemeList) {
				FarmerScheme farmerScheme = new FarmerScheme();
				farmerScheme.setFarmId(farm.getId());
				List<Data> farmerSchemeDataList = farmerSchemeObj.getData();

				for (Data data : farmerSchemeDataList) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.BENEFIT_DETAILS.equalsIgnoreCase(key)) {
						farmerScheme.setBenefitDetails(value);
					}
					if (TxnEnrollmentProperties.NO_OR_KGS.equalsIgnoreCase(key)) {
						farmerScheme.setNoOfKgs(Integer.valueOf(value));
					}
					if (TxnEnrollmentProperties.SCHEMES_OR_DEPARTMENT_NAMES.equalsIgnoreCase(key)) {
						farmerScheme.setDepartmentName(value);
					}
					if (TxnEnrollmentProperties.RECEIVED_ON_DATE.equalsIgnoreCase(key)) {
						farmerScheme.setReceivedDate(DateUtil.convertStringToDate(value, DateUtil.DATE_FORMAT));
					}
					if (TxnEnrollmentProperties.AMT_RECEIVED_RS.equalsIgnoreCase(key)) {
						farmerScheme.setReceivedAmt(Double.valueOf(value));
					}
					if (TxnEnrollmentProperties.OWN_CONTRIBUTION_RS.equalsIgnoreCase(key)) {
						farmerScheme.setContributionAmt(Double.valueOf(value));
					}
					/*
					 * if (TxnEnrollmentProperties.FARM_LAND_AREA_ORDER_NO.
					 * equalsIgnoreCase(key)) { if (!StringUtil.isEmpty(value))
					 * { farmerScheme.setOrderNo(Long.valueOf(value)); } else {
					 * farmerScheme.setOrderNo(0); } }
					 */
				}
				returnSet.add(farmerScheme);
			}
		}
		return returnSet;
	}

	private Set<FarmerLandDetails> buildFarmerLandDetails(Collection collection, Farm farm) {

		Set<FarmerLandDetails> returnSet = null;
		if (!ObjectUtil.isEmpty(collection)) {
			returnSet = new LinkedHashSet<FarmerLandDetails>();
			List<com.sourcetrace.eses.txn.schema.Object> farmerLandDetailsList = collection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object farmerLandDetailsObj : farmerLandDetailsList) {
				FarmerLandDetails farmerLandDetails = new FarmerLandDetails();
				farmerLandDetails.setFarmId(farm);
				List<Data> farmerLandDetailsDataList = farmerLandDetailsObj.getData();

				for (Data data : farmerLandDetailsDataList) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.SEASON_CODE_ADP_IFS_PRAC.equalsIgnoreCase(key)) {
						farmerLandDetails.setSeasonCode(value);
					}
					if (TxnEnrollmentProperties.IRRIGATED_TOTAL_LAND_IN_ACRES.equalsIgnoreCase(key)) {
						farmerLandDetails.setIrrigatedTotLand(Double.valueOf(value));
					}
					if (TxnEnrollmentProperties.IRRIGATED_LAND_IFS_PRACTICES_ADOPTING.equalsIgnoreCase(key)) {
						farmerLandDetails.setIrrigatedIfsPractices(value);
					}
					if (TxnEnrollmentProperties.RAINFED_TOTAL_LAND_IN_ACRES.equalsIgnoreCase(key)) {
						farmerLandDetails.setRainfedTotLand(Double.valueOf(value));
					}
					if (TxnEnrollmentProperties.RAINFED_LAND_IFS_PRACTICES_ADOPTING.equalsIgnoreCase(key)) {
						farmerLandDetails.setRanifedIfsPractices(value);
					}
				}
				returnSet.add(farmerLandDetails);
			}
		}
		return returnSet;
	}

	private Set<FarmerPlants> buildFarmPlantsOrTrees(Collection collection, Farm farm) {

		Set<FarmerPlants> returnSet = null;
		if (!ObjectUtil.isEmpty(collection)) {
			returnSet = new LinkedHashSet<FarmerPlants>();
			List<com.sourcetrace.eses.txn.schema.Object> farmerPlantsList = collection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object farmerPlantsObj : farmerPlantsList) {
				FarmerPlants farmerPlants = new FarmerPlants();
				farmerPlants.setFarmId(farm.getId());
				List<Data> farmerPlantsDataList = farmerPlantsObj.getData();

				for (Data data : farmerPlantsDataList) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.PLANTS_OR_TREES_NAME.equalsIgnoreCase(key)) {
						farmerPlants.setPlants(value);
					}
					if (TxnEnrollmentProperties.NO_OF_PLANTS_PLANTED.equalsIgnoreCase(key)) {
						farmerPlants.setNoOfPlants(Long.parseLong(value));
					}
					if (TxnEnrollmentProperties.NO_OF_LIVE_PLANTS.equalsIgnoreCase(key)) {
						farmerPlants.setNoOfLive(Long.parseLong(value));
					}
				}
				returnSet.add(farmerPlants);
			}
		}
		return returnSet;
	}

	private Set<FarmerSoilTesting> buildFarmSoilTesting(Collection collection, Farm farm) {

		Set<FarmerSoilTesting> returnSet = null;
		if (!ObjectUtil.isEmpty(collection)) {
			returnSet = new LinkedHashSet<FarmerSoilTesting>();
			List<com.sourcetrace.eses.txn.schema.Object> farmerSoilTestingList = collection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object farmerSoilTestingObj : farmerSoilTestingList) {
				FarmerSoilTesting farmerSoilTesting = new FarmerSoilTesting();
				farmerSoilTesting.setFarmId(farm.getId());
				List<Data> farmerSoilTestingDataList = farmerSoilTestingObj.getData();

				for (Data data : farmerSoilTestingDataList) {
					String key = data.getKey();
					String value = data.getValue();
					if (TxnEnrollmentProperties.SUGGESTIONS_FROM_OFFICERS.equalsIgnoreCase(key)) {
						farmerSoilTesting.setOfficersSuggestion(value);
					}
					if (TxnEnrollmentProperties.ACTION_TAKEN.equalsIgnoreCase(key)) {
						farmerSoilTesting.setTakenAction(value);
					}
				}
				returnSet.add(farmerSoilTesting);
			}
		}
		return returnSet;
	}

	private Set<FarmElement> getPolyHouse(Map polyMap) throws OfflineTransactionException, IOException {

		Set<FarmElement> polyHouse = new HashSet<FarmElement>();
		Collection polyCollection = (Collection) polyMap.get(POLY_COLLECTION);
		if (!ObjectUtil.isEmpty(polyCollection)) {
			List<com.sourcetrace.eses.txn.schema.Object> objects = polyCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object polyObj : objects) {
				List<Data> polyList = polyObj.getData();
				FarmElement element = new FarmElement();
				for (Data polyData : polyList) {
					String key = polyData.getKey();
					String value = polyData.getValue();
					if (TxnEnrollmentProperties.POLY_HOUSE_ITEM.equalsIgnoreCase(key)) {
						// validate(value,
						// SwitchErrorCodes.EMPTY_POLY_HOUSE_ITEM);
						FarmCatalogue cat = catalogueService.findCatalogueByCode(value);
						element.setCatalogueId(cat);
					}
					if (TxnEnrollmentProperties.POLY_HOUSE_COUNT.equalsIgnoreCase(key)) {
						// validate(value, SwitchErrorCodes.EMPTY_POLY_COUNT);
						element.setCount(value);
						element.setCatalogueType("6");
					}

				}
				element.setFarm((Farm) polyMap.get(FARM));
				polyHouse.add(element);
			}
		}
		return polyHouse;
	}

	private Set<FarmElement> getMachinary(Map machMap) throws OfflineTransactionException, IOException {

		Set<FarmElement> machinary = new HashSet<FarmElement>();
		Collection machinaryCollection = (Collection) machMap.get(MACHINARY_COLLECTION);
		if (!ObjectUtil.isEmpty(machinaryCollection)) {
			List<com.sourcetrace.eses.txn.schema.Object> objects = machinaryCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object machObj : objects) {
				List<Data> machList = machObj.getData();
				FarmElement element = new FarmElement();
				for (Data machData : machList) {
					String key = machData.getKey();
					String value = machData.getValue();
					if (TxnEnrollmentProperties.MACHINARY_ITEM.equalsIgnoreCase(key)) {
						// validate(value,
						// SwitchErrorCodes.EMPTY_MACHINARY_ITEM);
						FarmCatalogue cat = catalogueService.findCatalogueByCode(value);
						element.setCatalogueId(cat);
					}
					if (TxnEnrollmentProperties.MACHINARY_COUNT.equalsIgnoreCase(key)) {
						// validate(value,
						// SwitchErrorCodes.EMPTY_MACHINARY_COUNT);
						element.setCount(value);
						element.setCatalogueType("5");
					}

				}
				element.setFarm((Farm) machMap.get(FARM));
				machinary.add(element);
			}
		}
		return machinary;
	}

	/**
	 * Builds the farm land area.
	 * 
	 * @param collection
	 *            the collection
	 * @param farm
	 *            the farm
	 * @return the set< coordinates>
	 */
	private Set<Coordinates> buildFarmLandArea(Collection collection, Farm farm) {

		Set<Coordinates> returnSet = null;
		if (!ObjectUtil.isEmpty(collection)) {
			returnSet = new LinkedHashSet<Coordinates>();
			List<com.sourcetrace.eses.txn.schema.Object> coordinatesList = collection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object coordinatesObj : coordinatesList) {
				Coordinates coordinates = new Coordinates();
				coordinates.setFarm(farm);
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
				farm.setPlottingStatus(1);
				returnSet.add(coordinates);
			}
		}
		return returnSet;
	}

	/**
	 * Gets the farm crops.
	 * 
	 * @param farmMap
	 *            the farm map
	 * @return the farm crops
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public Set<FarmCrops> getFarmCrops(Map farmMap,String plotCaptureTime) throws OfflineTransactionException, IOException {

		Set<FarmCrops> farmCrops = new HashSet<FarmCrops>();
		Collection cropCollection = (Collection) farmMap.get(FARM_CROP_COLLECTION);
		if (!ObjectUtil.isEmpty(cropCollection)) {
			List<com.sourcetrace.eses.txn.schema.Object> cropObjectList = cropCollection.getObject();

			for (com.sourcetrace.eses.txn.schema.Object cropObject : cropObjectList) {
				List<Data> cropDataList = cropObject.getData();
				FarmCrops farmCrop = new FarmCrops();

				for (Data cropData : cropDataList) {
					String cropKey = cropData.getKey();
					String cropValue = cropData.getValue();

					if (TxnEnrollmentProperties.FARM_CROPS_CODE.equalsIgnoreCase(cropKey)) {
						validate(cropValue, ITxnErrorCodes.EMPTY_FARM_VARIETY_CODE);
						ProcurementVariety procurementVariety = farmCropsService
								.findProcurementVarietyByCode(cropValue);
						validate(procurementVariety, ITxnErrorCodes.FARM_VARIETY_DOES_NOT_EXIST);
						farmCrop.setProcurementVariety(procurementVariety);
						// farmCrop.setFarmCropsMaster(null);
					}
					if (TxnEnrollmentProperties.STAPLE_LENGTH_MAIN.equalsIgnoreCase(cropKey))
						farmCrop.setStapleLength(StringUtil.isEmpty(cropValue) ? "0.0" : cropValue);

					if (TxnEnrollmentProperties.SEED_USED_MAIN.equalsIgnoreCase(cropKey))
						farmCrop.setSeedQtyUsed(StringUtil.isEmpty(cropValue) ? 0.0 : Double.valueOf(cropValue));

					if (TxnEnrollmentProperties.SEED_COST_MAIN.equalsIgnoreCase(cropKey))
						farmCrop.setSeedQtyCost(StringUtil.isEmpty(cropValue) ? 0.0 : Double.valueOf(cropValue));

					if (TxnEnrollmentProperties.EST_YIELD.equalsIgnoreCase(cropKey))
						farmCrop.setEstimatedYield(StringUtil.isEmpty(cropValue) ? 0.0 : Double.valueOf(cropValue));

					if (TxnEnrollmentProperties.FARM_CROP_TYPE.equalsIgnoreCase(cropKey))
						farmCrop.setType((StringUtil.isEmpty(cropValue) || cropValue == null) ? "" : cropValue);

					if (TxnEnrollmentProperties.SEED_SOURCE.equalsIgnoreCase(cropKey))
						farmCrop.setSeedSource(StringUtil.isEmpty(cropValue) ? "" : cropValue);
					// if
					// (ESETxnEnrollmentProperties.PRODUCTION_YEAR.equalsIgnoreCase(cropKey))
					// farmCrop.setProductionPerYear(cropValue);
					if (TxnEnrollmentProperties.PROD_STATUS.equalsIgnoreCase(cropKey)) {
						// validate(cropValue,
						// SwitchErrorCodes.EMPTY_CROP_SEASON);
						HarvestSeason hs = farmerService.findHarvestSeasonByCode(cropValue);
						farmCrop.setCropSeason(hs);
					}
					if (TxnEnrollmentProperties.CROP_CATEGORY.equalsIgnoreCase(cropKey)) {
						// validate(cropValue,
						// ITxnErrorCodes.EMPTY_CROP_CATEGORY);
						farmCrop.setCropCategory(StringUtil.isEmpty(cropValue) ? -1 : buildDDIntValue(cropValue));
					}
					if (TxnEnrollmentProperties.SEED_TREATMENT_DETAILS.equalsIgnoreCase(cropKey)) {
						farmCrop.setSeedTreatmentDetails(cropValue);
					}
					if (TxnEnrollmentProperties.OTHER_SEED_TREATMENT_DETAILS.equalsIgnoreCase(cropKey)) {
						farmCrop.setOtherSeedTreatmentDetails(cropValue);
					}
					if (TxnEnrollmentProperties.RISK_ASSESMENT.equalsIgnoreCase(cropKey)) {
						farmCrop.setRiskAssesment(cropValue);
					}
					if (TxnEnrollmentProperties.RISK_BUFFER_ZONE_DISTANCE.equalsIgnoreCase(cropKey)) {
						farmCrop.setRiskBufferZoneDistanse(cropValue);
					}
					if (TxnEnrollmentProperties.SOWING_DATE.equalsIgnoreCase(cropKey)) {
						if (!StringUtil.isEmpty(cropValue)) {
							farmCrop.setSowingDate(DateUtil.convertStringToDate(cropValue, DateUtil.DATE));
						}
					}

					if (TxnEnrollmentProperties.ESTIMATED_HARVEST_DATE.equalsIgnoreCase(cropKey)) {
						if (!StringUtil.isEmpty(cropValue)) {
							farmCrop.setEstimatedHarvestDate(DateUtil.convertStringToDate(cropValue, DateUtil.DATE));
						}

					}

					if (TxnEnrollmentProperties.FARM_CROP_TYPE_OTHER.equalsIgnoreCase(cropKey)) {
						farmCrop.setOtherType(cropValue);
					}
					if (TxnEnrollmentProperties.CULTIVATION_TYPE.equalsIgnoreCase(cropKey)) {
						farmCrop.setCropCategoryList(cropValue);

					}
					if (TxnEnrollmentProperties.CULTI_AREA.equalsIgnoreCase(cropKey)) {
						farmCrop.setCultiArea(cropValue);

					}
					if (TxnEnrollmentProperties.NO_OF_TREES.equalsIgnoreCase(cropKey))
						farmCrop.setNoOfTrees((StringUtil.isEmpty(cropValue) || cropValue == null) ? "" : cropValue);

				}
				
				farmCrop.setPlotCapturingTime(DateUtil.convertStringToDate(plotCaptureTime, DateUtil.TXN_DATE_TIME));

				Farm farm = (Farm) farmMap.get(FARM); // Check Duplication
				if (!ObjectUtil.isEmpty(farm.getFarmCrops())) {
					for (FarmCrops crops : farm.getFarmCrops()) {
						if (crops.getProcurementVariety().getCode()
								.equalsIgnoreCase(farmCrop.getProcurementVariety().getCode()))
							throwError(ITxnErrorCodes.FARM_CROPS_ALREADY_MAPPED_WITH_THIS_FARM);
					}
				}
				farmCrop.setStatus(Farmer.Status.ACTIVE.ordinal());
				farmCrop.setBranchId(BRANCH_ID);
				farmCrop.setRevisionNo(DateUtil.getRevisionNumber());
				farmCrop.setFarm(farm);
				farmCrops.add(farmCrop);
			}
		}
		return farmCrops;
	}

	/**
	 * Gets the harvest list.
	 * 
	 * @param farmMap
	 *            the farm map
	 * @return the harvest list
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	private Set<HarvestData> getHarvestList(Map farmMap) throws OfflineTransactionException, IOException {

		Set<HarvestData> harvestDatas = new HashSet<HarvestData>();
		Farm farm = (Farm) farmMap.get(FARM);
		Map farmCropMap = new HashMap(); // Farm Crops
		Iterator itCrop = farm.getFarmCrops().iterator();
		while (itCrop.hasNext()) {
			FarmCrops crops = (FarmCrops) itCrop.next();
			farmCropMap.put(crops.getProcurementVariety().getCode(), crops);
		}
		Collection harvestCollection = (Collection) farmMap.get(HARVEST_COLLECTION);
		if (!ObjectUtil.isEmpty(harvestCollection)) {
			List<com.sourcetrace.eses.txn.schema.Object> harvestObjectList = harvestCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object harvestObject : harvestObjectList) {
				List<Data> harvestDataList = harvestObject.getData();
				HarvestData harvest = new HarvestData();
				for (Data harvestData : harvestDataList) {
					String harvestKey = harvestData.getKey();
					String harvestValue = harvestData.getValue();
					if (TxnEnrollmentProperties.HARVEST_FARM_CROP_CODE_REF.equalsIgnoreCase(harvestKey)) {
						validate(harvestValue, ITxnErrorCodes.EMPTY_HARVEST_FARM_CROPS_CODE_REF);
						if (farmCropMap.containsKey(harvestValue))
							harvest.setFarmCrops((FarmCrops) farmCropMap.get(harvestValue));
						else
							throwError(ITxnErrorCodes.INVALID_HARVEST_FARM_CROPS_CODE);
					}
					if (TxnEnrollmentProperties.HARVEST_DATE.equalsIgnoreCase(harvestKey)) {
						validate(harvestValue, ITxnErrorCodes.EMPTY_HARVEST_DATE);
						try {
							harvest.setHarvestedDate(
									DateUtil.convertStringToDate(harvestValue, DateUtil.TXN_DATE_TIME));
						} catch (Exception e) {
							throwError(ITxnErrorCodes.INVALID_DATE_FORMAT);
						}
					}
					if (TxnEnrollmentProperties.HARVEST_QUANTITY.equalsIgnoreCase(harvestKey)) {
						validate(harvestValue, ITxnErrorCodes.EMPTY_HARVEST_QUANTITY);
						harvest.setHarvested(harvestValue);
					}
					if (TxnEnrollmentProperties.HARVEST_AMOUNT.equalsIgnoreCase(harvestKey)) {
						validate(harvestValue, ITxnErrorCodes.EMPTY_HARVEST_AMOUNT);
						harvest.setHarvestedAmount(harvestValue);
					}
					if (TxnEnrollmentProperties.BUYER_NAME.equalsIgnoreCase(harvestKey)) {
						validate(harvestValue, ITxnErrorCodes.EMPTY_BUYER_NAME);
						harvest.setBuyerName(harvestValue);
					}
					if (TxnEnrollmentProperties.CROP_PHOTO.equalsIgnoreCase(harvestKey)) {
						DataHandler farmCropPhotoDataHandler = (DataHandler) harvestData.getBinaryValue();
						if (!ObjectUtil.isEmpty(farmCropPhotoDataHandler))
							try {
								harvest.setHarvestDataImage(
										IOUtils.toByteArray(farmCropPhotoDataHandler.getInputStream()));
							} catch (IOException e) {
								e.printStackTrace();
							}
					}
				}
				harvestDatas.add(harvest);
			}
		}
		return harvestDatas;
	}

	/**
	 * Gets the animals.
	 * 
	 * @param farmMap
	 *            the farm map
	 * @return the animals
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	/*@SuppressWarnings("unchecked")
	private Set<AnimalHusbandary> getAnimals(Map farmMap) throws OfflineTransactionException, IOException {

		Set<AnimalHusbandary> animals = new HashSet<AnimalHusbandary>();
		Collection animalCollection = (Collection) farmMap.get(ANIMAL_COLLECTION);
		if (!ObjectUtil.isEmpty(animalCollection)) {
			List<com.sourcetrace.eses.txn.schema.Object> animalObjects = animalCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object animalObject : animalObjects) {
				List<Data> animalDataList = animalObject.getData();
				AnimalHusbandary animal = new AnimalHusbandary();
				for (Data animalData : animalDataList) {
					String animalKey = animalData.getKey();
					String animalValue = animalData.getValue();
					if (TxnEnrollmentProperties.FARM_ANIMAL.equalsIgnoreCase(animalKey)) {
						validate(animalValue, ITxnErrorCodes.EMPTY_FARM_ANIMAL);
						FarmCatalogue cat = catalogueService.findCatalogueByCode(animalValue);
						animal.setFarmAnimal(cat);
					}
					if (TxnEnrollmentProperties.FARM_ANIMAL_OTHER.equalsIgnoreCase(animalKey))
						animal.setOtherFarmAnimal(animalValue);
					if (TxnEnrollmentProperties.FARM_ANIMAL_COUNT.equalsIgnoreCase(animalKey)) {
						// validate(animalValue,
						// ITxnErrorCodes.EMPTY_FARM_ANIMAL_COUNT);
						animal.setAnimalCount(StringUtil.isEmpty(animalValue) ? String.valueOf("0") : animalValue);
					}
					if (TxnEnrollmentProperties.FARM_ANIMAL_HOUSE.equalsIgnoreCase(animalKey)) {
						// validate(animalValue,
						// ITxnErrorCodes.EMPTY_FARM_ANIMAL_HOUSE);
						FarmCatalogue cat = catalogueService.findCatalogueByCode(animalValue);
						animal.setAnimalHousing(cat);
					}
					if (TxnEnrollmentProperties.FARM_ANIMAL_HOUSE_OTHER.equalsIgnoreCase(animalKey))
						animal.setOtherAnimalHousing(StringUtil.isEmpty(animalValue) ? null : animalValue);
					if (TxnEnrollmentProperties.FODDER.equalsIgnoreCase(animalKey)) {
						FarmCatalogue cat = catalogueService.findCatalogueByCode(animalValue);
						String animalName=cat.getName();
						animal.setFodder(animalName);
						if(!StringUtil.isEmpty(animalValue) && animalValue!=null ){
                            String fodderValue ="";
                            FarmCatalogue fodder = new FarmCatalogue();
                            String fodderArr[] = animalValue.split("\\,");                             
                            for (int i = 0; i < fodderArr.length; i++) {
                                String fodderTrim = fodderArr[i].replaceAll("\\s+", "");
                                fodder  = catalogueService.findCatalogueByCode(fodderTrim);
                                
                                fodderValue += fodder.getId() +",";
                             
                            }   
                            fodderValue = fodderValue.substring(0, fodderValue.length() - 1);
                       
                            animal.setFodder(fodderValue);
                        }else{
                            animal.setFodder("");
                        }
					}
					if (TxnEnrollmentProperties.FODDER_OTHER.equalsIgnoreCase(animalKey))
						animal.setOtherFodder(StringUtil.isEmpty(animalValue) ? null : animalValue);
					if (TxnEnrollmentProperties.REVENUE.equalsIgnoreCase(animalKey)) {
						
						 * FarmCatalogue cat =
						 * catalogueService.findCatalogueByCode(animalValue);
						 * animal.setRevenue(cat);
						 
						animal.setRevenue(StringUtil.isEmpty(animalValue) ? null : animalValue);
					}

					if (TxnEnrollmentProperties.REVENUE_OTHER.equalsIgnoreCase(animalKey))
						animal.setOtherRevenue(StringUtil.isEmpty(animalValue) ? null : animalValue);
					
					 if (TxnEnrollmentProperties.BREED.equalsIgnoreCase(animalKey))
                        animal.setBreed(StringUtil.isEmpty(animalValue) ? null : animalValue);
				}
				animal.setFarm((Farm) farmMap.get(FARM));
				animals.add(animal);
			}
		}
		return animals;
	}
*/
	/**
	 * Gets the inventories.
	 * 
	 * @param inventoryMap
	 *            the inventory map
	 * @return the inventories
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	/*@SuppressWarnings("unchecked")
	public Set<FarmInventory> getInventories(Map inventoryMap) throws OfflineTransactionException, IOException {

		Set<FarmInventory> inventories = new HashSet<FarmInventory>();
		Collection inventoryCollection = (Collection) inventoryMap.get(INVENTORY_COLLECTION);
		if (!ObjectUtil.isEmpty(inventoryCollection)) {
			List<com.sourcetrace.eses.txn.schema.Object> inventoryObjects = inventoryCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object inventoryObject : inventoryObjects) {
				List<Data> inventoryDataList = inventoryObject.getData();
				FarmInventory inventory = new FarmInventory();
				for (Data inventoryData : inventoryDataList) {
					String inventoryKey = inventoryData.getKey();
					String inventoryValue = inventoryData.getValue();
					if (TxnEnrollmentProperties.FARM_INVENTORY_ITEM.equalsIgnoreCase(inventoryKey)) {
						FarmCatalogue cat = catalogueService.findCatalogueByCode(inventoryValue);
						inventory.setInventoryItem(cat);
					}
					if (TxnEnrollmentProperties.FARM_INVENTORY_ITEM_OTHER.equalsIgnoreCase(inventoryKey)) {
						inventory.setOtherInventoryItem(StringUtil.isEmpty(inventoryValue) ? null : inventoryValue);
					}
					if (TxnEnrollmentProperties.FARM_INVENTORY_ITEM_COUNT.equalsIgnoreCase(inventoryKey)) {
						validate(inventoryValue, ITxnErrorCodes.EMPTY_INVENTORY_ITEM_COUNT);
						inventory.setItemCount(inventoryValue);
					}
				}
				inventory.setFarm((Farm) inventoryMap.get(FARM));
				inventories.add(inventory);
			}
		}
		return inventories;
	}
*/
	private Set<FarmerLandDetails> buildFarmHistory(Collection collection, Farm farm) {

		Set<FarmerLandDetails> returnSet = null;
		if (!ObjectUtil.isEmpty(collection)) {
			returnSet = new LinkedHashSet<FarmerLandDetails>();
			List<com.sourcetrace.eses.txn.schema.Object> farmerLandDetailsList = collection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object farmerLandDetailsObj : farmerLandDetailsList) {
				FarmerLandDetails farmerLandDetails = new FarmerLandDetails();
				farmerLandDetails.setFarmId(farm);
				List<Data> farmerLandDetailsDataList = farmerLandDetailsObj.getData();

				for (Data data : farmerLandDetailsDataList) {
					String key = data.getKey();
					String value = data.getValue();
					
					if (TxnEnrollmentProperties.YEAR.equalsIgnoreCase(key)) {
						farmerLandDetails.setYear(value);
					}
					if (TxnEnrollmentProperties.CROP.equalsIgnoreCase(key)) {
						farmerLandDetails.setCrops(value);
					}
					if (TxnEnrollmentProperties.SEEDLING.equalsIgnoreCase(key)) {
						farmerLandDetails.setSeedlings(value);
					}
					if (TxnEnrollmentProperties.ESTIMATED_ACREAGE.equalsIgnoreCase(key)) {
						if(!ObjectUtil.isEmpty(value)){
							farmerLandDetails.setEstimatedAcreage(Double.valueOf(value));
						}else{
							farmerLandDetails.setEstimatedAcreage(0.0);
						}
						
					}
					if (TxnEnrollmentProperties.NO_TREES.equalsIgnoreCase(key)) {
						farmerLandDetails.setNoOfTrees((value));
					}
					if (TxnEnrollmentProperties.PEST_DISEASES.equalsIgnoreCase(key)) {
						farmerLandDetails.setPestdiseases(value);
					}
					if (TxnEnrollmentProperties.PEST_DISEASES_CONTROL.equalsIgnoreCase(key)) {
						farmerLandDetails.setPestdiseasesControl(value);
					}
					if (TxnEnrollmentProperties.FERTILIZATION_METHOD.equalsIgnoreCase(key)) {
						farmerLandDetails.setFertilizationMethod(value);
					}
					if (TxnEnrollmentProperties.TWO_METER_DIST.equalsIgnoreCase(key)) {
						farmerLandDetails.setInputs(value);
					}
					if (TxnEnrollmentProperties.TEN_METER_DIST.equalsIgnoreCase(key)) {
						farmerLandDetails.setWithBuffer(value);
					}
					if (TxnEnrollmentProperties.THIRTY_METER_DIST.equalsIgnoreCase(key)) {
						farmerLandDetails.setWithoutBuffer(value);
					}
					if (TxnEnrollmentProperties.DATE_LAST_APP.equalsIgnoreCase(key)) {
						if(!StringUtil.isEmpty(value) && !ObjectUtil.isEmpty(value)){
							farmerLandDetails.setDate(DateUtil.convertStringToDate(value, DateUtil.DATE_FORMAT_1));
						}
						
					}
				}
				farmerLandDetails.setBranchId(BRANCH_ID);
				returnSet.add(farmerLandDetails);
			}
		}
		return returnSet;
	}
	
	/**
	 * Build farm ics datas.
	 * 
	 * @param collection
	 * @param farm
	 * @return the set< farm ic s>
	 */
	private Set<FarmICS> buildFarmICSDatas(Collection collection, Farm farm) {

		Set<FarmICS> returnSet = null;
		if (!ObjectUtil.isEmpty(collection)) {
			returnSet = new LinkedHashSet<FarmICS>();
			List<com.sourcetrace.eses.txn.schema.Object> icsList = collection.getObject();

			for (com.sourcetrace.eses.txn.schema.Object icsObj : icsList) {
				FarmICS farmIcs = new FarmICS();
				farmIcs.setFarm(farm);
				List<Data> icsDataList = icsObj.getData();

				for (Data data : icsDataList) {
					String key = data.getKey();
					String value = data.getValue();

					if (TxnEnrollmentProperties.ICS_TYPE.equalsIgnoreCase(key))
						farmIcs.setIcsType(Integer.valueOf(value));
					if (TxnEnrollmentProperties.ICS_LAND_DETAILS.equalsIgnoreCase(key))
						farmIcs.setLandIcsDetails(value);
					if (TxnEnrollmentProperties.ICS_SURVEY_NO.equalsIgnoreCase(key))
						farmIcs.setSurveyNo(value);
					if (TxnEnrollmentProperties.ICS_BEGIN_CONV.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value)) {
							try {
								Date beginOfConversion = TXN_DATE_FORMAT.parse(value);
								farmIcs.setBeginOfConversion(beginOfConversion);
							} catch (Exception e) {
								LOGGER.info(e.getMessage());
							}
						}
					}
				}
				farmIcs.setStatus(FarmICS.ACTIVE);
				returnSet.add(farmIcs);
			}
		}
		return returnSet;
	}

	private Set<DocumentUpload> buildDocumentUploadDatas(DataHandler docUploadDataHandler, Farm farm) {
		// DataHandler docUploadDataHandler = (DataHandler)
		// docUploadDataHandler;
		Set<DocumentUpload> documentUpload = new HashSet<DocumentUpload>();
		DocumentUpload docUpload = new DocumentUpload();

		try {
			if (!ObjectUtil.isEmpty(docUploadDataHandler)) {
				byte[] docUploadData = IOUtils.toByteArray(docUploadDataHandler.getInputStream());
				if (docUploadData != null) {
					docUpload.setFarm(farm);
					docUpload.setContent(docUploadData);
					docUpload.setName(farm.getFarmName() + "" + ".png");
					documentUpload.add(docUpload);
				}
			}
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
		return documentUpload;
	}
	
	
	
	@SuppressWarnings("unused")
	private Set<TreeDetail> setTreeDetails(Farm farm, Map farmMap)throws OfflineTransactionException, IOException {
		Double totOrgTrees = 0.0;
		Double totConvTrees = 0.0;
		Double totOrgFuerte = 0.0;
		Double totOrgHass = 0.0;
		Double totConvFuerte = 0.0;
		Double totConvHass = 0.0;
		Double totAvocadoTrees=0.0;
		Double hecOrgAvocadoTrees = 0.0;
		Double hecConvAvocadoTrees = 0.0;
		Double hecAvocadoTrees = 0.0;
		ProcurementVariety procurementVariety = null;
		Set<TreeDetail> treeDetails = new HashSet<TreeDetail>();
		Set<TreeDetail> tempSet = new HashSet<TreeDetail>();
		Collection cropCollection = (Collection) farmMap.get(FARM_CROP_COLLECTION);
		if (!ObjectUtil.isEmpty(cropCollection)) {
			List<com.sourcetrace.eses.txn.schema.Object> cropObjectList = cropCollection.getObject();

			for (com.sourcetrace.eses.txn.schema.Object cropObject : cropObjectList) {
				List<Data> cropDataList = cropObject.getData();
				TreeDetail treeDetail = new TreeDetail();

				String sowingDate = null;
				String estimatedHarvestDate = null;

				for (Data cropData : cropDataList) {
					String key = cropData.getKey();
					String value = cropData.getValue();

					if (TxnEnrollmentProperties.CULTIVAR.equalsIgnoreCase(key)) {
						treeDetail.setVariety(value);

					}

					if (TxnEnrollmentProperties.YEARS.equalsIgnoreCase(key)) {
						// validate(value, SwitchErrorCodes.EMPTY_POLY_COUNT);
						treeDetail.setYears(value);
					}

					if (TxnEnrollmentProperties.PROD_STATUS.equalsIgnoreCase(key)) {
						// validate(value, SwitchErrorCodes.EMPTY_POLY_COUNT);
						treeDetail.setProdStatus(value);

					}

					if (TxnEnrollmentProperties.TOTAL_TREES.equalsIgnoreCase(key)) {
						// validate(value, SwitchErrorCodes.EMPTY_POLY_COUNT);
						treeDetail.setNoOfTrees(value);

					}

				
				}
				tempSet.add(treeDetail);
			}
			
			for(TreeDetail treeDetail:tempSet)
			{
				if (!ObjectUtil.isEmpty(treeDetail) && !StringUtil.isEmpty(treeDetail.getProdStatus())
						&& treeDetail.getProdStatus().equalsIgnoreCase("1")) {
					totOrgTrees+= Double.valueOf(treeDetail.getNoOfTrees());
					farm.setTotalOrganicTrees(totConvTrees);

				} 	if (!ObjectUtil.isEmpty(treeDetail) && !StringUtil.isEmpty(treeDetail.getProdStatus())
						&& treeDetail.getProdStatus().equalsIgnoreCase("0")) {
					totConvTrees+= Double.valueOf(treeDetail.getNoOfTrees());
					farm.setTotalConventionalTrees(totConvTrees);

				}
				totAvocadoTrees=totOrgTrees+totConvTrees;
				farm.setTotalAvocadoTrees(totAvocadoTrees);
				hecOrgAvocadoTrees=(totOrgTrees*7*7/10000);
				hecConvAvocadoTrees=(totConvTrees*7*7/10000);
				hecOrgAvocadoTrees=(totAvocadoTrees*7*7/10000);
				farm.setHectarAvocadoTrees(hecOrgAvocadoTrees);
				farm.setHectarOrganicTrees(hecOrgAvocadoTrees);
				farm.setHectarConventionalTrees(hecConvAvocadoTrees);
				treeDetail.setFarm(farm);
				treeDetails.add(treeDetail);
			}
			
		
		return treeDetails;
		
		}
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

    public String getTenantId() {
    
        return tenantId;
    }

    public void setTenantId(String tenantId) {
    
        this.tenantId = tenantId;
    }

    public String getAwiFarmCodeSeq() {
    
        return awiFarmCodeSeq;
    }

    public void setAwiFarmCodeSeq(String awiFarmCodeSeq) {
    
        this.awiFarmCodeSeq = awiFarmCodeSeq;
    }

	public int getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(int basicInfo) {
		this.basicInfo = basicInfo;
	}

	public Farm getFinalFarm() {
		return finalFarm;
	}

	public void setFinalFarm(Farm finalFarm) {
		this.finalFarm = finalFarm;
	}
	
	
}
