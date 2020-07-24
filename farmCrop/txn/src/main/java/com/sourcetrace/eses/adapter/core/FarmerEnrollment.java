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
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import java.util.function.Function;
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
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldScoreMap;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.interceptor.ITxnMessageUtil;
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
import com.sourcetrace.eses.service.IPreferencesService;
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
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.CoordinatesMap;
import com.sourcetrace.esesw.entity.profile.DocumentUpload;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.ESECard;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;
import com.sourcetrace.esesw.entity.profile.FarmElement;
import com.sourcetrace.esesw.entity.profile.FarmICS;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.FarmInventory;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.FarmerEconomy;
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
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.TreeDetail;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.esesw.excep.SwitchErrorCodes;
import com.sun.istack.ByteArrayDataSource;

@Component
public class FarmerEnrollment implements ITxnAdapter {

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
	@Autowired
	public ICatalogueService catService;
	@Autowired
	public IUniqueIDGenerator idServoce;
	private IProductDistributionService productDistributionService;
	private int mode = 1;
	private int farmerCertificationType = 0;
	private static final String REQUEST_DATA = "requestData";
	private static final String FARMER = "farmer";
	private static final String FARM = "farm";
	private static final String FARMER_SOURCE_INCOME = "farmerSourceIncome";
	private static final String HARVEST_DATA = "harvestData";
	private static final String INVENTORY_COLLECTION = "inventoryCollection";
	private static final String ANIMAL_COLLECTION = "animalCollection";
	private static final String HARVEST_COLLECTION = "harvestCollection";
	private static final String FARM_CROP_COLLECTION = "farmCropCollection";
	private static final int SELECT = -1;
	public static final String SELECT_MULTI = "-1";
	private static final String HEAD = "HEAD";
	// private static final String BANK_INFORMATION = "bankCollection";
	private static final String MACHINARY_COLLECTION = "machinaryCollection";
	private static final String POLY_COLLECTION = "polyCollection";
	private String awiFarmCodeSeq = "";
	private Map<String, DynamicFieldConfig> fieldMap = new HashMap<>();
	private static String BRANCH_ID = "";
	private static String SEASON = "";
	private String agriImplements;
	@Autowired
	private IPreferencesService preferncesService;
	private static final String IMAGE_CONTENT_TYPE = "image/jpeg";
	private FarmerDynamicData farmerDynamicData = new FarmerDynamicData();

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
		String branchId = null;
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
			branchId = head.getBranchId();
			tenantId = head.getTenantId();
			plotCaptureTime = head.getTxnTime();
		}
		/** INITAIALIZING COMMON DATA **/
		Farmer farmer = new Farmer();
		Set<HarvestData> harvestDatas = new HashSet<HarvestData>();
		Map requestMap = new HashMap();
		requestMap.put(REQUEST_DATA, reqData);
		requestMap.put(FARMER, farmer);
		requestMap.put(HEAD, head);

		List<DynamicFieldConfig> dyField = farmerService.listDynamicFields();
		if (dyField != null && !dyField.isEmpty()) {
			fieldMap = dyField.stream().collect(Collectors.toMap(DynamicFieldConfig::getCode, Function.identity()));
		}

		BRANCH_ID = head.getBranchId();
		Set<FarmerSourceIncome> sourceIncomeSet = new LinkedHashSet<FarmerSourceIncome>();

		/** GET REQUEST DATA **/
		// seasonCode
		String season = (String) reqData.get(TxnEnrollmentProperties.CURRENT_SEASON_CODE);
		String farmerId = (String) reqData.get(TxnEnrollmentProperties.FARMER_ID);
		String farmerCode = (String) reqData.get(TxnEnrollmentProperties.FARMER_CODE);
		String dateofjoin = (String) reqData.get(TxnEnrollmentProperties.DOJ);
		String enrollmentPlace = (String) reqData.get(TxnEnrollmentProperties.ENROLLMENT_PLACE);
		String enrollmentPlaceOther = (String) reqData.get(TxnEnrollmentProperties.ENROLLMENT_PLACE_OTHER);
		String firstName = (String) reqData.get(TxnEnrollmentProperties.FIRST_NAME);
		String gender = (String) reqData.get(TxnEnrollmentProperties.GENDER);
		String lastName = (String) reqData.get(TxnEnrollmentProperties.LAST_NAME);
		String dateofBirth = (String) reqData.get(TxnEnrollmentProperties.DOB);
		String martialStatus = (String) reqData.get(TxnEnrollmentProperties.MARITAL_STATUS);
		String adultCountMale = (String) reqData.get(TxnEnrollmentProperties.ADULT_COUNT_MALE);
		String adultCountFemale = (String) reqData.get(TxnEnrollmentProperties.ADULT_COUNT_FEMALE);
		String childCountMale = (String) reqData.get(TxnEnrollmentProperties.CHILD_COUNT_MALE);
		String childCountFemale = (String) reqData.get(TxnEnrollmentProperties.CHILD_COUNT_FEMALE);
		String annualIncome = (String) reqData.get(TxnEnrollmentProperties.FARMER_ANNUAL_INCOME);
		String education = (String) reqData.get(TxnEnrollmentProperties.EDUCATION);
		String lifeInsCmpy = (String) reqData.get(TxnEnrollmentProperties.lf_ins_cmp);
		String healthInsCmpy = (String) reqData.get(TxnEnrollmentProperties.hlth_ins_cmp);
		String crpInsCmpy = (String) reqData.get(TxnEnrollmentProperties.crp_ins_cmp);
		String fpo = "";
		if (!StringUtil.isEmpty(reqData.get(TxnEnrollmentProperties.FPOGROUP)))
			fpo = (String) reqData.get(TxnEnrollmentProperties.FPOGROUP);
		String address = (String) reqData.get(TxnEnrollmentProperties.ADDRESS);
		String villageCode = (String) reqData.get(TxnEnrollmentProperties.VILLAGE_CODE);
		String otherVi = (reqData.containsKey(TxnEnrollmentProperties.OTHER_VILLAGE)
				? (String) reqData.get(TxnEnrollmentProperties.OTHER_VILLAGE) : "");
		String cityCode = (String) reqData.get(TxnEnrollmentProperties.CITY_CODE);
		String samithiCode = (String) reqData.get(TxnEnrollmentProperties.SAMITHI_CODE);
		String pinCode = (String) reqData.get(TxnEnrollmentProperties.PINCODE);
		String phoneNumber = (String) reqData.get(TxnEnrollmentProperties.PHONE_NO);
		String mobileNumber = (String) reqData.get(TxnEnrollmentProperties.MOBILE_NO);
		String email = (String) reqData.get(TxnEnrollmentProperties.EMAIL);
		String latitude = (String) reqData.get(TxnEnrollmentProperties.LATITUDE);
		String longitude = (String) reqData.get(TxnEnrollmentProperties.LONGITUDE);

		String age = (String) reqData.get(TxnEnrollmentProperties.AGE);

		String photoCaptureTime = (String) reqData.get(TxnEnrollmentProperties.FARMER_PHOTO_CAPTURE_TIME);
		// String
		// enrolledDate=(String)reqData.get(TxnEnrollmentProperties.ENROLL_DATE_TIME);
		String certifiedFarmer = (String) reqData.get(TxnEnrollmentProperties.FARMER_CERTIFIED);
		String certifiicationType = (String) reqData.get(TxnEnrollmentProperties.FARMER_CERTIFICATION_TYPE);
		String farmerCardId = (String) reqData.get(TxnEnrollmentProperties.FARMER_CARD_ID);
		String farmerAcctNo = (String) reqData.get(TxnEnrollmentProperties.FARMER_ACCOUNT_NO);
		String houseOwner = (String) reqData.get(TxnEnrollmentProperties.FARMER_HOUSE_OWNERSHIP);
		String houseType = (String) reqData.get(TxnEnrollmentProperties.FARMER_HOUSE_TYPE);
		String smart = (String) reqData.get(TxnEnrollmentProperties.SMARTPHONE);
		// String
		// bankName=(String)reqData.get(TxnEnrollmentProperties.BANK_NAME);
		// String accNo = (String)
		// reqData.get(TxnEnrollmentProperties.ACCOUNT_NO);
		// String branchdetail = (String)
		// reqData.get(TxnEnrollmentProperties.BRANCH_DETAILS);
		// String sortCode = (String)
		// reqData.get(TxnEnrollmentProperties.SORT_CODE);
		// String swiftCode = (String)
		// reqData.get(TxnEnrollmentProperties.SWIFT_CODE);
		String houseTypeOther = (String) reqData.get(TxnEnrollmentProperties.FARMER_HOUSE_TYPE_OTHER);
		String houseOwnerOther = (String) reqData.get(TxnEnrollmentProperties.OTHER_HOWSING_OWNER);

		String enrollDate = (String) reqData.get(TxnEnrollmentProperties.ENROLL_DATE);
		String icsName = (String) reqData.get(TxnEnrollmentProperties.ICS_NAME);
		String icsCode = (String) reqData.get(TxnEnrollmentProperties.ICS_CODE);
		String totalFamilyMem = (String) reqData.get(TxnEnrollmentProperties.TOTAL_FAMILY_COUNT);
		String schoolCountMale = (String) reqData.get(TxnEnrollmentProperties.SCHOOL_COUNT_MALE);
		String schoolCountFemale = (String) reqData.get(TxnEnrollmentProperties.SCHOOL_COUNT_FEMALE);
		String houseHold = (String) reqData.get(TxnEnrollmentProperties.TOTAL_HOUSEHOLD_MEMBERS);
		String male = (String) reqData.get(TxnEnrollmentProperties.MALE_COUNT);
		String female = (String) reqData.get(TxnEnrollmentProperties.FEMALE_COUNT);
		String agriInc = (String) reqData.get(TxnEnrollmentProperties.AGRI_INCOME);
		String otherInc = (String) reqData.get(TxnEnrollmentProperties.OTHER_INCOME);
		String consElec = (String) reqData.get(TxnEnrollmentProperties.CONSUMER_ELECTRONICS);
		String vehicle = (String) reqData.get(TxnEnrollmentProperties.VEHICLE);
		String cell = (String) reqData.get(TxnEnrollmentProperties.CELL_PHONE);
		String othrVecle = (String) reqData.get(TxnEnrollmentProperties.OTHER_VEHICLE);
		String othrElectro = (String) reqData.get(TxnEnrollmentProperties.CONSUMER_ELECTRONICS_OTHER);
		String drinking = (String) reqData.get(TxnEnrollmentProperties.DRINKING_WATER_SOURCE);
		String drinkingWaterOther = (String) reqData.get(TxnEnrollmentProperties.DRINKING_WATER_SOURCE_OTHER);
		String lifeInsurance = (String) reqData.get(TxnEnrollmentProperties.FARMER_LIFE_INSURANCE);
		String cropInsurance = (String) reqData.get(TxnEnrollmentProperties.CROP_INSURANCE);
		String isElectrified = (String) reqData.get(TxnEnrollmentProperties.IS_ELECTRIFIED);
		String loantaken = (String) reqData.get(TxnEnrollmentProperties.LOAN_TAKEN);
		String isLoantakenScheme = (String) reqData.get(TxnEnrollmentProperties.IS_LOAN_TAKEN_SCHEME);
		String loantakenScheme = (String) reqData.get(TxnEnrollmentProperties.LOAN_TAKEN_SCHEME);
		String loanTakenFrom = (String) reqData.get(TxnEnrollmentProperties.LOAN_TAKEN_FROM);
		String grsMember = (String) reqData.get(TxnEnrollmentProperties.GRS_MEMEBER);
		String paidShareCapital = (String) reqData.get(TxnEnrollmentProperties.PAID_SHARE_CAPITAL);

		// Chetna fields
		String icsUnitNo = (String) reqData.get(TxnEnrollmentProperties.ICS_UNIT_NO);
		String icsTracenetRegNo = (String) reqData.get(TxnEnrollmentProperties.ICS_TRACENET_REG_NO);
		String farmerCodeByIcs = (String) reqData.get(TxnEnrollmentProperties.FARMER_CODE_BYICS);
		String farmersCodeTracenet = (String) reqData.get(TxnEnrollmentProperties.FARMER_CODE_BYTRACENET);
		String category = (String) reqData.get(TxnEnrollmentProperties.CATEGORY);
		String personalStatus = (String) reqData.get(TxnEnrollmentProperties.STATUS);
		String isBeneficiaryInAnyGovScheme = (String) reqData.get(TxnEnrollmentProperties.IS_BENEFICIARY_INANY_GOV_SCHEME);
		String nameOfScheme = (String) reqData.get(TxnEnrollmentProperties.NAME_OF_THE_STRING);
		String otherLoanTakenFrom = (String) reqData.get(TxnEnrollmentProperties.OTHER_LOAN_TAKEN_FROM);
		String loanAmount = (String) reqData.get(TxnEnrollmentProperties.LOAN_AMOUNT);
		String loanPurpose = (String) reqData.get(TxnEnrollmentProperties.LOAN_PURPOSE);
		String loanIntRate = (String) reqData.get(TxnEnrollmentProperties.LOAN_INTREST_RATE);
		String loanSecurity = (String) reqData.get(TxnEnrollmentProperties.LOAN_SECURITY);
		String loanTakensFrom = (String) reqData.get(TxnEnrollmentProperties.LOANS_TAKEN_FROM);
		String surName = (String) reqData.get(TxnEnrollmentProperties.SUR_NAME);
		String loanRepAmt = (reqData.containsKey(TxnEnrollmentProperties.LOAN_REPAYMENT_AMOUNT))
				? (String) reqData.get(TxnEnrollmentProperties.LOAN_REPAYMENT_AMOUNT) : "";
		String loanRepDt = (reqData.containsKey(TxnEnrollmentProperties.LOAN_REPAYMENT_DATE))
				? (String) reqData.get(TxnEnrollmentProperties.LOAN_REPAYMENT_DATE) : "";

		String farmCertiYear = (reqData.containsKey(TxnEnrollmentProperties.FARM_CERTIFICATION_YEAR))
				? (String) reqData.get(TxnEnrollmentProperties.FARM_CERTIFICATION_YEAR) : "";

		// APMAS Fields
		/* String hhid = (String) reqData.get(TxnEnrollmentProperties.HHID); */
		String adhaar = (String) reqData.get(TxnEnrollmentProperties.ADHAAR);
		String caste = (String) reqData.get(TxnEnrollmentProperties.CASTE);
		String religion = (String) reqData.get(TxnEnrollmentProperties.RELIGION);
		String religionOther = (String) reqData.get(TxnEnrollmentProperties.RELIGION_OTHER);
		String typeOfFamily = (String) reqData.get(TxnEnrollmentProperties.TYPE_OF_FAMILY);

		String householdLandholdingDry = (String) reqData.get(TxnEnrollmentProperties.HOUSEHOLD_LANDHOLDING_DRY);
		String householdLandholdingWet = (String) reqData.get(TxnEnrollmentProperties.HOUSEHOLD_LANDHOLDING_WET);
		String primaryHousehold = (String) reqData.get(TxnEnrollmentProperties.PRIMARY_HOUSEHOLD);
		String primaryHouseholdOther = (String) reqData.get(TxnEnrollmentProperties.PRIMARY_HOUSEHOLD_OTHER);

		String secondaryHousehold = (String) reqData.get(TxnEnrollmentProperties.SECONDARY_HOUSEHOLD);
		String secondaryHouseholdOther = (String) reqData.get(TxnEnrollmentProperties.SECONDARY_HOUSEHOLD_OTHER);

		String familyMemberCbo = (String) reqData.get(TxnEnrollmentProperties.FAMILY_MEMBER_CBO);
		String familyMemberCboOther = (String) reqData.get(TxnEnrollmentProperties.FAMILY_MEMBER_CBO_OTHER);

		String totalIncomePerAnnum = (String) reqData.get(TxnEnrollmentProperties.TOTAL_INCOME_PER_ANNUM);
		String opinionOfInvestigator = (String) reqData.get(TxnEnrollmentProperties.OPINION_OF_INVESTIGATOR);
		String positionInTheGroup = (String) reqData.get(TxnEnrollmentProperties.POSITION_IN_THE_GROUP);

		String headOfFamily = (String) reqData.get(TxnEnrollmentProperties.HEAD_OF_FAMILY);

		String govtDept = (String) reqData.get(TxnEnrollmentProperties.GOVT_DEP);
		String healthInsurance = (String) reqData.get(TxnEnrollmentProperties.HEALTH_INSURANCE);

		String lifeInsAmount = (String) reqData.get(TxnEnrollmentProperties.LIFE_INSURANCE_AMT);

		String healthInsAmount = (String) reqData.get(TxnEnrollmentProperties.HEALTH_INSURANCE_AMT);

		String acresInsured = (String) reqData.get(TxnEnrollmentProperties.AREA_INSURED);
		String loanPuposeOther = (String) reqData.get(TxnEnrollmentProperties.LOAN_PURPOSE_OTHER);
		String loanIntPeriod = (String) reqData.get(TxnEnrollmentProperties.LOAN_TYPE);
		String loanSecurityOther = (String) reqData.get(TxnEnrollmentProperties.LOAN_SEC_OTHER);
		String casteName = (String) reqData.get(TxnEnrollmentProperties.CASTE_TRIBE_NAME);
		String farmerCropInsurance = (String) reqData.get(TxnEnrollmentProperties.CROP_INSURANCE_VALUE);

		String idProof = (String) reqData.get(TxnEnrollmentProperties.ID_PROOF);
		String proofNo = (String) reqData.get(TxnEnrollmentProperties.ID_PROOF_VALUE);
		String otherIdProof = (String) reqData.get(TxnEnrollmentProperties.ID_PROOF_OTHER);

		String homeDiff = (reqData.containsKey(TxnEnrollmentProperties.HOMEDIFF))
				? (String) reqData.get(TxnEnrollmentProperties.HOMEDIFF) : "";
		String workDiff = (reqData.containsKey(TxnEnrollmentProperties.WORKDIFF))
				? (String) reqData.get(TxnEnrollmentProperties.WORKDIFF) : "";
		String comDiff = (reqData.containsKey(TxnEnrollmentProperties.COMDIFF))
				? (String) reqData.get(TxnEnrollmentProperties.COMDIFF) : "";
		String assistiveDev = (reqData.containsKey(TxnEnrollmentProperties.ASSISTIVEDEV))
				? (String) reqData.get(TxnEnrollmentProperties.ASSISTIVEDEV) : "";
		String assistiveDevName = (reqData.containsKey(TxnEnrollmentProperties.ASSISTIVEDEVNAME))
				? (String) reqData.get(TxnEnrollmentProperties.ASSISTIVEDEVNAME) : "";
		String reqassistiveDev = (reqData.containsKey(TxnEnrollmentProperties.REQASSISTIVEDEV))
				? (String) reqData.get(TxnEnrollmentProperties.REQASSISTIVEDEV) : "";
		String healthIssue = (reqData.containsKey(TxnEnrollmentProperties.HEALTHISSUE))
				? (String) reqData.get(TxnEnrollmentProperties.HEALTHISSUE) : "";
		String healthIssueDes = (reqData.containsKey(TxnEnrollmentProperties.HEALTHISSUEDES))
				? (String) reqData.get(TxnEnrollmentProperties.HEALTHISSUEDES) : "";
		String prefWrk = (reqData.containsKey(TxnEnrollmentProperties.PREFERENCE_OF_WORK))
				? (String) reqData.get(TxnEnrollmentProperties.PREFERENCE_OF_WORK) : "";

		String form = (reqData.containsKey(TxnEnrollmentProperties.FORM_FILLED_BY))
				? (String) reqData.get(TxnEnrollmentProperties.FORM_FILLED_BY) : "";

		String assess = (reqData.containsKey(TxnEnrollmentProperties.ASSESS))
				? (String) reqData.get(TxnEnrollmentProperties.ASSESS) : "";

		String placeOfAssess = (reqData.containsKey(TxnEnrollmentProperties.PLACE_ASSES))
				? (String) reqData.get(TxnEnrollmentProperties.PLACE_ASSES) : "";

		String objective = (reqData.containsKey(TxnEnrollmentProperties.OBJECTIVE))
				? (String) reqData.get(TxnEnrollmentProperties.OBJECTIVE) : "";
		String toiletAvailable = (String) reqData.get(TxnEnrollmentProperties.IS_TOILET_AVAILABLE);
		String ifToiletAvailable = (String) reqData.get(TxnEnrollmentProperties.TOILET_AVAILABLE);
		String cookingFuel = (String) reqData.get(TxnEnrollmentProperties.COOKING_FUEL);
		String cookingFuelSourceOther = (String) reqData.get(TxnEnrollmentProperties.OTHER_COOKING_FUEL_USED);
		String qtyPerMonth = (String) reqData.get(TxnEnrollmentProperties.QTY_COOKING_FUEL_PERMONTH);
		String costPerMonth = (String) reqData.get(TxnEnrollmentProperties.COST_COOKING_FUEL_PERMONTH);
		String fpo1 = (String) reqData.get(TxnEnrollmentProperties.CO_OPERATIVE);
		String type = (reqData.containsKey(TxnEnrollmentProperties.FARMER_TYPE))
				? (String) reqData.get(TxnEnrollmentProperties.FARMER_TYPE) : "";
		// Wilmar

		String traders = (reqData.containsKey(TxnEnrollmentProperties.TRADER))
				? (String) reqData.get(TxnEnrollmentProperties.TRADER) : "";
		String noOfTrees = (reqData.containsKey(TxnEnrollmentProperties.NO_OF_TREES))
				? (String) reqData.get(TxnEnrollmentProperties.NO_OF_TREES) : "";

		// String fp = (String)
		// reqData.get(TxnEnrollmentProperties.FARMER_FINGER_PRINT);
		SEASON = season;

		String icsYr = (String) reqData.get(TxnEnrollmentProperties.SEASON);
		String agriImpl = (String) reqData.get(TxnEnrollmentProperties.AGRI_IMPLEMENT);

		Date farmerDOB = null;
		Date farmerDOJ = null;
		ImageInfo imageInfo = new ImageInfo();
		
		try {// Basic Info

			// Validations
			validate(agentId, SwitchErrorCodes.AGENT_ID_EMPTY);
			agent = agentService.findAgentByAgentId(agentId);
			validate(agent, SwitchErrorCodes.INVALID_AGENT);
			validate(serialNo, SwitchErrorCodes.EMPTY_SERIAL_NO);
			device = deviceService.findDeviceBySerialNumber(serialNo);
			validate(device, SwitchErrorCodes.INVALID_DEVICE);

			// Farmer Validation
			validate(farmerId, SwitchErrorCodes.EMPTY_FARMER_ID);
			// ICS Year

			// Update Farmer Id Sequence For Agent
			// if (!StringUtil.isEmpty(farmerId))
			// agent.setFarmerCurrentIdSeq(farmerId);

			Farmer existing = farmerService.findFarmerByFarmerId(farmerId);
			farmer.setRefId(null);
			if (!ObjectUtil.isEmpty(existing)) {
				throwError(SwitchErrorCodes.FARMER_EXIST);
			}
			validate(firstName, SwitchErrorCodes.EMPTY_FIRST_NAME);
			// validate(lastName, SwitchErrorCodes.EMPTY_LAST_NAME);
			// validate(gender, SwitchErrorCodes.EMPTY_GENDER);

			// validate(address, SwitchErrorCodes.EMPTY_ADDRESS);
			validate(villageCode, SwitchErrorCodes.EMPTY_VILLAGE_CODE);
			Village village = locationService.findVillageByCode(villageCode);
			validate(village, SwitchErrorCodes.VILLAGE_NOT_EXIST);
			farmer.setVillage(village);
			farmer.setCity((Municipality) (!ObjectUtil.isEmpty(village.getCity())
					? !ObjectUtil.isEmpty(village.getCity()) ? village.getCity() : "" : ""));

			validate(samithiCode, SwitchErrorCodes.EMPTY_SAMITHI_CODE);
			Warehouse warehouse = locationService.findSamithiByCode(samithiCode);
			validate(warehouse, SwitchErrorCodes.INVALID_SAMITHI);
			farmer.setSamithi(warehouse);
			farmer.setTypez(type);
			farmer.setAge(StringUtil.isEmpty(age) ? null : Integer.valueOf(age));

			// validate(mobileNumber, SwitchErrorCodes.EMPTY_FARMER_MOBILE);
			/*
			 * if (StringUtil.isEmpty(fp)|| fp==null) {
			 * throwError(SwitchErrorCodes.EMPTY_FINGER_PRINT); }
			 */
			// Validating DOB and DOJ
			if (!StringUtil.isEmpty(dateofBirth)) {
				try {
					farmerDOB = DateUtil.convertStringToDate(dateofBirth, DateUtil.FARMER_DOB);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// validate(dateofjoin,
			// SwitchErrorCodes.EMPTY_FARMER_DATE_OF_JOINING);
			if (!StringUtil.isEmpty(dateofjoin)) {
				try {
					farmerDOJ = DateUtil.convertStringToDate(dateofjoin, DateUtil.FARMER_DOB);
				} catch (Exception e) {
					farmerDOJ = new Date();
				}
			} else {
				farmerDOJ = new Date();
			}

			// Farmer Photo capture time, latitude, longitude, photo
			farmer.setLatitude(StringUtil.isEmpty(latitude) ? null : latitude);
			farmer.setSmartPhone(smart != null && !StringUtil.isEmpty(smart) ? Integer.valueOf(smart) : 0);
			farmer.setLongitude(StringUtil.isEmpty(longitude) ? null : longitude);
			if (!StringUtil.isEmpty(photoCaptureTime)) {
				try {
					Date photoCaptureDate = TXN_DATE_FORMAT.parse(photoCaptureTime);
					farmer.setPhotoCaptureTime(photoCaptureDate);
				} catch (Exception e) {
					LOGGER.info(e.getMessage());
				}
			}
			
			DataHandler photoDataHandler = (DataHandler) reqData.get(TxnEnrollmentProperties.PHOTO);
           
			byte[] photoContent = null;
			try {
				farmer.setImageInfo(null);
				if (photoDataHandler != null) {
					photoContent = IOUtils.toByteArray(photoDataHandler.getInputStream());
					if (photoContent != null) {
						Image photo = new Image();
						photo.setImage(photoContent);
						imageInfo.setPhoto(photo);
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			
			Date formatedDate = DateUtil.convertStringToDate(photoCaptureTime, DateUtil.TXN_DATE_TIME);
			String serialNumber = (String) head.getSerialNo();
			AgentAccessLog agentAccessLog = agentService.findAgentAccessLogByAgentId(agent.getProfileId(),
					DateUtil.getDateWithoutTime(formatedDate));	
			if(tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) && photoDataHandler!= null){
				
				AgentAccessLogDetail agentAccessLogDetail = agentService
						.findAgentAccessLogDetailByTxn(agentAccessLog.getId(), "3083");
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
				agentAccessLogDetailNew.setTxnType("3083");
				agentAccessLogDetailNew
						.setMessageNumber(head.getMsgNo());
				agentAccessLogDetailNew.setTxnMode(head.getMode());

				agentAccessLogDetailNew.setTxnDate(formatedDate);

				agentAccessLogDetailNew.setTxnCount(1L);

				agentAccessLogDetailNew.setServicePoint(
						head.getServPointId());

				agentService.save(agentAccessLogDetailNew);
			}
			}
			// Finger Print

			DataHandler fp = (reqData.containsKey(TxnEnrollmentProperties.FARMER_FINGER_PRINT))
					? (DataHandler) reqData.get(TxnEnrollmentProperties.FARMER_FINGER_PRINT) : null;
			if (!ObjectUtil.isEmpty(fp)) {
				byte[] fingerPrint = null;
				try {
					if (fp != null) {
						fingerPrint = IOUtils.toByteArray(fp.getInputStream());
						if (!StringUtil.isEmpty(fingerPrint) && !fingerPrint.equals("[]")) {
							farmer.setFingerPrint(fingerPrint);
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			//Digital Signature
			DataHandler ds = (reqData.containsKey(TxnEnrollmentProperties.FARMER_DIGITAL_SIGNATURE))
					? (DataHandler) reqData.get(TxnEnrollmentProperties.FARMER_DIGITAL_SIGNATURE) : null;
			if (!ObjectUtil.isEmpty(ds)) {
				byte[] digitalSign = null;
				try {
					if (ds != null) {
						digitalSign = IOUtils.toByteArray(ds.getInputStream());
						if (!StringUtil.isEmpty(digitalSign) && !digitalSign.equals("[]")) {
							farmer.setDigitalSign(digitalSign);
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			DataHandler idpRoof = (reqData.containsKey(TxnEnrollmentProperties.IDPROOF_PHOTO))
					? (DataHandler) reqData.get(TxnEnrollmentProperties.IDPROOF_PHOTO) : null;
			if (!ObjectUtil.isEmpty(idpRoof)) {
				byte[] idproof = null;
				try {
					if (idpRoof != null) {
						idproof = IOUtils.toByteArray(idpRoof.getInputStream());
						if (!StringUtil.isEmpty(idproof) && !idproof.equals("[]")) {
							farmer.setIdProofImg(idproof);
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			/*
			 * DataHandler fp = (DataHandler)
			 * reqData.get(TxnEnrollmentProperties.FARMER_FINGER_PRINT);
			 * 
			 * byte[] fingerPrint = null; try { if (fp != null) { fingerPrint =
			 * IOUtils.toByteArray(fp.getInputStream()); if
			 * (StringUtil.isEmpty(fingerPrint)&& !fingerPrint.equals("[]")) {
			 * farmer.setFingerPrint(fingerPrint); }else {
			 * throwError(SwitchErrorCodes. EMPTY_FINGER_PRINT); }
			 * 
			 * }
			 * 
			 * } catch (IOException e) { e.printStackTrace(); }
			 */

			// Card Info
			if (Device.MOBILE_DEVICE.equalsIgnoreCase(device.getDeviceType())) {
				farmerCardId = idGenerator.createFarmerCardIdSequence(IUniqueIDGenerator.ESE_MOBILE_REQUEST,
						agent.getInternalIdSequence());
			} else {
				if (StringUtil.isEmpty(farmerCardId)) {
					farmerCardId = idGenerator.createFarmerCardIdSequence(IUniqueIDGenerator.WEB_REQUEST,
							IUniqueIDGenerator.ESE_AGENT_INTERNAL_ID);
				}
				ESECard existingCard = cardService.findESECardByCardId(farmerCardId);
				if (!ObjectUtil.isEmpty(existingCard))
					throwError(SwitchErrorCodes.FARMER_CARD_EXIST);
			}
			// Farmer Family Information
			if (tenantId.equals(ESESystem.CHETNA_TENANT_ID)) {
				Set<FarmerFamily> farmerFamilySet = getFarmerFamily(requestMap, tenantId);
				farmer.setFarmerFamilies(ObjectUtil.isListEmpty(farmerFamilySet) ? null : farmerFamilySet);
			}

			validate(certifiedFarmer, SwitchErrorCodes.EMPTY_FARMER_CERTIFIED);
			if (certifiedFarmer.equalsIgnoreCase(String.valueOf(Farmer.CERTIFIED_YES))) {
				if (!tenantId.equals(ESESystem.CHETNA_TENANT_ID)) {
					Set<FarmerFamily> farmerFamilySet = getFarmerFamily(requestMap, tenantId);
					farmer.setFarmerFamilies(ObjectUtil.isListEmpty(farmerFamilySet) ? null : farmerFamilySet);
				}
				// To Validate farmer certification type
				validate(certifiicationType, SwitchErrorCodes.EMPTY_FARMER_CERTIFICATION_TYPE);

				if (!StringUtil.isEmpty(houseOwner) || !StringUtil.isEmpty(houseType)
						|| !StringUtil.isEmpty(houseTypeOther) || !StringUtil.isEmpty(drinking)
						|| !StringUtil.isEmpty(lifeInsurance) || !StringUtil.isEmpty(cropInsurance)
						|| !StringUtil.isEmpty(isElectrified) || !StringUtil.isEmpty(houseOwnerOther)
						|| !StringUtil.isEmpty(toiletAvailable) || !StringUtil.isEmpty(cookingFuel)
						|| !StringUtil.isEmpty(ifToiletAvailable) || !StringUtil.isEmpty(qtyPerMonth)
						|| !StringUtil.isEmpty(costPerMonth)) {
					// Farmer Economy
					FarmerEconomy economy = new FarmerEconomy();
					// validate(houseOwner,
					// SwitchErrorCodes.EMPTY_FARMER_HOUSE_OWNERSHIP);
					economy.setHousingOwnership(StringUtil.isEmpty(houseOwner) ? -1 : buildDDIntValue(houseOwner));
					economy.setHousingOwnershipOther(StringUtil.isEmpty(houseOwnerOther) ? null : houseOwnerOther);

					// validate(houseType,
					// SwitchErrorCodes.EMPTY_FARMER_HOUSE_TYPE);
					economy.setHousingType(StringUtil.isEmpty(houseType) ? "-1" : String.valueOf(houseType));
					economy.setOtherHousingType(StringUtil.isEmpty(houseTypeOther) ? null : houseTypeOther);
					economy.setDrinkingWaterSource(drinking);

					if (!StringUtil.isEmpty(drinking)) {
						if (drinking.contains("99")) {
							economy.setDrinkingWaterSourceOther(drinkingWaterOther);
						}
					}
					economy.setDrinkingWaterSource(drinking);
					/*
					 * if (!StringUtil.isEmpty(cropInsurance) &&
					 * StringUtil.isInteger(cropInsurance)) {
					 * economy.setCropInsurance(Integer.parseInt(cropInsurance))
					 * ; } else { economy.setCropInsurance(-1); }
					 */

					if (isElectrified != null) {
						if (isElectrified.equals("1")) {
							economy.setElectrifiedHouse(1);
						} else {
							economy.setElectrifiedHouse(2);
						}
					}

					if (!StringUtil.isEmpty(lifeInsurance) && StringUtil.isInteger(lifeInsurance)) {
						economy.setLifeOrHealthInsurance(Integer.parseInt(lifeInsurance));
					}

					if (!StringUtil.isEmpty(toiletAvailable) && StringUtil.isInteger(toiletAvailable)) {
						economy.setToiletAvailable(Integer.parseInt(toiletAvailable));
					}

					economy.setIfToiletAvailable(!StringUtil.isEmpty(ifToiletAvailable) ? ifToiletAvailable : "");
					economy.setCookingFuel(!StringUtil.isEmpty(cookingFuel) ? cookingFuel : "");
					economy.setCookingFuelSourceOther(
							!StringUtil.isEmpty(cookingFuelSourceOther) ? cookingFuelSourceOther : "");
					economy.setQtyCookingPerMonth(qtyPerMonth);
					economy.setCostCookingPerMonth(costPerMonth);

					economy.setFarmer(farmer);
					farmer.setFarmerEconomy(economy);
				}
			}
			farmer.setMsgNo(head.getMsgNo());
			farmer.setFarmerId(farmerId);
			farmer.setPrefWrk(prefWrk);
			if (tenantId.equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {
				farmer.setMasterData(traders);
			}
		
			/*
			 * if (tenantId.equalsIgnoreCase(ESESystem.AWI_TENANT_ID)) {
			 * 
			 * if (!ObjectUtil.isEmpty(farmer.getVillage())) { String codeGen =
			 * idGenerator.getFarmerWebCodeIdSeq(
			 * farmer.getVillage().getCity().getCode().substring(0, 1),
			 * farmer.getVillage().getGramPanchayat().getCode().substring(0,
			 * 1)); farmer.setFarmerCode(codeGen); }
			 * 
			 * 
			 * farmer.setFarmerCode(farmer.getFarmerId()); } else
			 */
			int seq=0; 
			if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
				farmer.setFarmerCode(farmerCode);
				farmer.setTraceId(farmerCode);
			} else if (tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {

				/*String farmerGenCode = "";
				String districtName = farmer.getVillage().getCity().getLocality().getName();
				districtName = districtName.substring(0, 3);
				String cityName = farmer.getVillage().getCity().getName();
				cityName = cityName.substring(0, 3);
				String doj = DateUtil.getYearByDateTime(farmerDOJ);
				String villageName = farmer.getVillage().getName();
				villageName = villageName.substring(0, 3);
				String samithiName = farmer.getSamithi().getCode();
				samithiName = samithiName.substring(1);
				String farmerGenId = farmer.getFarmerId();
				farmerGenCode = districtName + "" + doj + "" + cityName + "" + villageName + "" + samithiName + ""
						+ farmerGenId;
				farmer.setFarmerCode(farmerGenCode);*/
				String farmers="A000";
				farmer.setFarmerCode(farmers+farmer.getFarmerId());

			} else if(tenantId.equalsIgnoreCase(ESESystem.STICKY_TENANT_ID)){
				String samithiName= farmer.getSamithi().getCode();
				samithiName = samithiName.substring(1);
				farmer.setFarmerCode(tenantId+samithiName+farmer.getFarmerId());
			}
			 
			else if(tenantId.equalsIgnoreCase(ESESystem.SYMRISE)){
				//String samithiName= farmer.getVillage().getCode();
				seq=Integer.valueOf(farmer.getVillage().getSeq());
				farmer.setFarmerCode(farmer.getVillage().getCode()+"_"+seq);
			}else if(tenantId.equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID)){
				String farmerBranch="";
				
				   BranchMaster branchMaster = StringUtil.isEmpty(branchId) ? null
		                    : clientService.findBranchMasterByBranchId(branchId);
				   if(!ObjectUtil.isEmpty(branchMaster)){
					   farmerBranch=branchMaster.getContactPerson();
					   }
				
				String vilSeqCode=farmerBranch+"/"+village.getCity().getLocality().getState().getCountry().getName().substring(0,2).toUpperCase()
						+"/"+village.getCity().getLocality().getState().getName().substring(0,2).toUpperCase()
						+"/"+village.getName().substring(0,3).toUpperCase()
						+"/"+village.getSeq()				
						;
				
				farmer.setFarmerCode(vilSeqCode);
				seq=Integer.parseInt(farmer.getVillage().getSeq());
			}
			else {
				farmer.setFarmerCode(farmerCode);
			}
			
			if (tenantId.equalsIgnoreCase(ESESystem.AVT)) {
				seq=Integer.valueOf(farmer.getVillage().getSeq());
				farmer.setFarmerCode("F"+farmer.getVillage().getCode()+seq);
			}
			farmer.setDateOfJoining(farmerDOJ);
			farmer.setEnrollmentPlace(!StringUtil.isEmpty(enrollmentPlace) ? Integer.valueOf(enrollmentPlace) : SELECT);
			//farmer.setEnrollmentPlace(!StringUtil.isEmpty(enrollmentPlace) ? enrollmentPlace : SELECT_MULTI);
			farmer.setEnrollmentPlaceOther(!StringUtil.isEmpty(enrollmentPlaceOther) ? enrollmentPlaceOther : "");
			farmer.setFirstName(firstName);
			farmer.setLastName(!StringUtil.isEmpty(lastName) ? lastName : " ");
			farmer.setGender(!StringUtil.isEmpty(gender) ? gender.toUpperCase() : null);
			farmer.setDateOfBirth(farmerDOB);
			farmer.setMaritalSatus(martialStatus);
			farmer.setAdultCountMale(adultCountMale);
			farmer.setAdultCountFemale(adultCountFemale);
			farmer.setChildCountMale(childCountMale);
			farmer.setChildCountFemale(childCountFemale);
			farmer.setAnnualIncome(annualIncome);
			farmer.setEducation(education);
			farmer.setFormFilledBy(form);
			farmer.setAssess(assess);
			farmer.setPlaceOfAsss(placeOfAssess);
			farmer.setObjective(objective);
			if (tenantId.equalsIgnoreCase("kpf")||tenantId.equalsIgnoreCase("wub") || tenantId.equalsIgnoreCase("gar"))
				farmer.setFpo(fpo);
			else if (tenantId.equalsIgnoreCase("chetna"))
				farmer.setFpo(fpo1);
			else
				farmer.setFpo(fpo);

			farmer.setAddress(address);
			farmer.setPinCode(StringUtil.isEmpty(pinCode) ? null : pinCode);
			farmer.setPhoneNumber(phoneNumber);
			farmer.setMobileNumber(!StringUtil.isEmpty(mobileNumber) ? mobileNumber : null);
			farmer.setEmail(email);
			farmer.setSurName(surName);
			farmer.setSeasonCode(StringUtil.isEmpty(season) ? "" : season);

			farmer.setIsCertifiedFarmer(buildIntValue(certifiedFarmer));
			
			if(tenantId.equalsIgnoreCase(ESESystem.SYMRISE)){
				farmer.setCertificationType(2);
				farmer.setCertificateStandardLevel(-2);
			}
			else{
			farmer.setCertificationType(buildIntValue(certifiicationType));
			}
			if(tenantId.equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
			farmer.setStatus(Farmer.Status.INACTIVE.ordinal());
			String ld=farmer.getCity().getLocality().getName().replaceAll("\\s","").substring(farmer.getCity().getLocality().getName().replaceAll("\\s","").length()-2).toUpperCase().concat(farmer.getCity().getName().replaceAll("\\s","").substring(farmer.getCity().getName().replaceAll("\\s","").length()-2).toUpperCase()).concat(farmer.getVillage().getName().replaceAll("\\s","").substring(farmer.getVillage().getName().replaceAll("\\s","").length()-2).toUpperCase());			farmer.setFarmerCode(ld.concat(idGenerator.getFarmerCodeSeq()));
			//farmer.setNoOfFamilyMembers(StringUtil.isEmpty(totalFamilyMem) ? 0 : Integer.valueOf(totalFamilyMem));
			farmer.setNoOfHouseHoldMem(StringUtil.isEmpty(totalFamilyMem) ? 0 : Integer.valueOf(totalFamilyMem));
			}else{
				farmer.setStatus(Farmer.Status.ACTIVE.ordinal());
				farmer.setNoOfHouseHoldMem(StringUtil.isEmpty(houseHold) ? 0 : Integer.valueOf(houseHold));
				farmer.setNoOfFamilyMembers(StringUtil.isEmpty(totalFamilyMem) ? 0 : Integer.valueOf(totalFamilyMem));
			}
			farmer.setIsVerified(0);
			farmer.setEnrollDate(DateUtil.convertStringToDate(enrollDate, DateUtil.FARMER_DOB));
			farmer.setIcsCode(icsCode);
			if(tenantId.equalsIgnoreCase(ESESystem.LIVELIHOOD_TENANT_ID)){
			farmer.setIcsName("NA");
			}else{
				farmer.setIcsName(icsName);
			}
			

			farmer.setNoOfSchoolChildMale(StringUtil.isEmpty(schoolCountMale) ? 0 : Integer.valueOf(schoolCountMale));
			farmer.setNoOfSchoolChildFemale(
					StringUtil.isEmpty(schoolCountFemale) ? 0 : Integer.valueOf(schoolCountFemale));
			
			farmer.setMaleCnt(StringUtil.isEmpty(male) ? 0 : Integer.valueOf(male));
			farmer.setFemaleCnt(StringUtil.isEmpty(female) ? 0 : Integer.valueOf(female));
			farmer.setAgriculture(agriInc);
			farmer.setOtherSource(otherInc);
			double agri = agriInc == null || agriInc.isEmpty() ? 0.0 : Double.valueOf(agriInc);
			double other = otherInc == null || otherInc.isEmpty() ? 0.0 : Double.valueOf(otherInc);
			double total = agri + other;
			farmer.setTotal(String.valueOf(total));
			farmer.setConsumerElectronics(consElec);
			farmer.setVehicle(vehicle);
			farmer.setVehicleOther(othrVecle);
			farmer.setConsumerElectronicsOther(othrElectro);
			farmer.setCellPhone(cell);
			farmer.setLoanTakenFrom(loanTakenFrom);
			farmer.setIsLoanTakenScheme(isLoantakenScheme);
			farmer.setLoanTakenScheme(loantakenScheme);
			farmer.setBranchId(branchId);
			farmer.setLoanPuposeOther(loanPuposeOther);
			if (loantaken != null) {
				if (loantaken.equals("1")) {
					farmer.setLoanTakenLastYear(1);
					farmer.setLoanRepaymentAmount(loanRepAmt);
					if (!StringUtil.isEmpty(loanRepDt)) {
						farmer.setLoanRepaymentDate(DateUtil.convertStringToDate(loanRepDt, DateUtil.FARMER_DOB));
					}
				} else {
					farmer.setLoanTakenLastYear(2);
				}
			} else {
				farmer.setLoanTakenLastYear(0);
			}

			farmer.setIsCropInsured(cropInsurance);
			farmer.setLoanTakenFrom(loanTakensFrom);
			farmer.setIcsUnitNo(icsUnitNo);
			farmer.setIcsTracenetRegNo(icsTracenetRegNo);
			farmer.setFarmerCodeByIcs(farmerCodeByIcs);
			farmer.setFarmersCodeTracenet(farmersCodeTracenet);
			farmer.setCategory(!StringUtil.isEmpty(category) ? String.valueOf(category) : null);
			farmer.setPersonalStatus(!StringUtil.isEmpty(personalStatus) ? Integer.valueOf(personalStatus) : null);
			farmer.setIsBeneficiaryInGovScheme(
					!StringUtil.isEmpty(isBeneficiaryInAnyGovScheme) ? (isBeneficiaryInAnyGovScheme) : null);
			farmer.setNameOfTheScheme(nameOfScheme);
			farmer.setOtherLoanTakenFrom(otherLoanTakenFrom);
			farmer.setLoanAmount(loanAmount);
			farmer.setLoanPupose(loanPurpose);
			farmer.setLoanIntRate(loanIntRate);
			farmer.setLoanSecurity(loanSecurity);
			// farmer.setAgricultureImplements(agriImpl);
			Set<BankInformation> bankInfoSet = getBankInfo(requestMap, tenantId);
			farmer.setBankInfo(ObjectUtil.isListEmpty(bankInfoSet) ? null : bankInfoSet);
			/*
			 * if (tenantId.equalsIgnoreCase("atma")) {
			 * farmer.setFarmerCode(!StringUtil.isEmpty(hhid) ? hhid : "");
			 * 
			 * }
			 */
			farmer.setAdhaarNo(!StringUtil.isEmpty(adhaar) ? adhaar : "");
			farmer.setSocialCategory(!StringUtil.isEmpty(caste) ? caste : "");
			farmer.setReligion(!StringUtil.isEmpty(religion) ? religion : "");
			farmer.setReligionOther(!StringUtil.isEmpty(religionOther) ? religionOther : "");
			farmer.setTypeOfFamily(!StringUtil.isEmpty(typeOfFamily) ? typeOfFamily : "");

			String householdLHDry = (!StringUtil.isEmpty(householdLandholdingDry)) ? householdLandholdingDry : "0";
			farmer.setHouseHoldLandDry((householdLHDry));

			String householdLHWet = (!StringUtil.isEmpty(householdLandholdingWet)) ? householdLandholdingWet : "0";
			farmer.setHouseHoldLandWet((householdLHWet));

			farmer.setHouseOccupationPrimary(!StringUtil.isEmpty(primaryHousehold) ? primaryHousehold : "");
			farmer.setHouseOccupationPriOther(!StringUtil.isEmpty(primaryHouseholdOther) ? primaryHouseholdOther : "");
			farmer.setHouseOccupationSecondary(!StringUtil.isEmpty(secondaryHousehold) ? secondaryHousehold : "");
			farmer.setHouseOccupationSecOther(
					!StringUtil.isEmpty(secondaryHouseholdOther) ? secondaryHouseholdOther : "");
			farmer.setFamilyMember(!StringUtil.isEmpty(familyMemberCbo) ? familyMemberCbo : "");
			farmer.setFamilyMemberOther(!StringUtil.isEmpty(familyMemberCboOther) ? familyMemberCboOther : "");

			String totalIncome = (!StringUtil.isEmpty(totalIncomePerAnnum))
					? CurrencyUtil.formatByUSDcomma(totalIncomePerAnnum) : "0";

			farmer.setTotalSourceIncome(Double.valueOf(totalIncome));
			farmer.setInvestigatorOpinion(!StringUtil.isEmpty(opinionOfInvestigator) ? opinionOfInvestigator : "");
			Agent tmpAgent = agentService.findAgentByAgentId(agentId);
			/*
			 * if (tenantId.equals("atma")) {
			 * farmer.setInvestigatorName(tmpAgent.getProfileId());
			 * farmer.setInvestigatorDate(DateUtil.convertStringToDate(head.
			 * getTxnTime(), DateUtil.TXN_DATE_TIME)); }
			 */
			farmer.setPositionGroup(!StringUtil.isEmpty(positionInTheGroup) ? positionInTheGroup : "");

			farmer.setGovtDept(!StringUtil.isEmpty(govtDept) ? govtDept : "");
			farmer.setHealthInsurance(!StringUtil.isEmpty(healthInsurance) ? healthInsurance : "");
			farmer.setLifeInsurance(!StringUtil.isEmpty(lifeInsurance) ? lifeInsurance : "");
			farmer.setAcresInsured(!StringUtil.isEmpty(acresInsured) ? acresInsured : "");
			
			farmer.setLifInsCmpName(!StringUtil.isEmpty(lifeInsCmpy) ? lifeInsCmpy : "");
			farmer.setHealthInsCmpName(!StringUtil.isEmpty(healthInsCmpy) ? healthInsCmpy : "");
			farmer.setCrpInsCmpName(!StringUtil.isEmpty(crpInsCmpy) ? crpInsCmpy : "");
			// farmer.setLoanPupose(!StringUtil.isEmpty(loanPupose)? loanPupose
			// : "");
			farmer.setCreatedUsername(tmpAgent.getPersonalInfo().getAgentName());
			farmer.setCreatedDate(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME));
			farmer.setLoanSecurityOther(!StringUtil.isEmpty(loanSecurityOther) ? loanSecurityOther : "");

			farmer.setHeadOfFamily(StringUtil.isEmpty(headOfFamily) ? 0 : Integer.valueOf(headOfFamily));
			// farmer.setHeadOfFamily(!StringUtil.isEmpty(headOfFamily)?
			// Integer.parseInt(headOfFamily) : 0);
			farmer.setLoanIntPeriod(!StringUtil.isEmpty(loanIntPeriod) ? loanIntPeriod : "");
			farmer.setLifeInsAmount(!StringUtil.isEmpty(lifeInsAmount) ? lifeInsAmount : "");
			farmer.setHealthInsAmount(!StringUtil.isEmpty(healthInsAmount) ? healthInsAmount : "");
			farmer.setIdProof(!StringUtil.isEmpty(idProof) ? idProof : "");
			farmer.setProofNo(!StringUtil.isEmpty(proofNo) ? proofNo : "");

			farmer.setOtherIdProof(!StringUtil.isEmpty(otherIdProof) ? otherIdProof : "");
			farmer.setCasteName(!StringUtil.isEmpty(casteName) ? casteName : "");

			farmer.setFarmerCropInsurance(!StringUtil.isEmpty(farmerCropInsurance) ? farmerCropInsurance : "");
			farmer.setIsCertifiedFarmer(buildIntValue(certifiedFarmer));
			farmer.setGrsMember(buildIntValue(grsMember));
			farmer.setPaidShareCapitial(buildIntValue(paidShareCapital));
			farmer.setYearOfICS(!StringUtil.isEmpty(icsYr) ? icsYr : "");
			/*
			 * validate(icsYr, SwitchErrorCodes.EMPTY_YEAR_OF_ICS); }
			 */
			/** PGSS Related Changes */

			/*
			 * farmer.setHomeDifficulty(homeDiff);
			 * farmer.setWorkDiffficulty(workDiff);
			 * farmer.setCommunitiyDifficulty(comDiff);
			 * farmer.setAssistiveDeivce(assistiveDev);
			 * farmer.setAssistiveDeivceName(assistiveDevName);
			 * farmer.setAssistiveDeviceReq(reqassistiveDev);
			 * farmer.setHealthIssue(healthIssue);
			 * farmer.setHealthIssueDescribe(healthIssueDes);
			 * 
			 * Collection healthAsses =
			 * (reqData.containsKey(TxnEnrollmentProperties.HEALTHASSES)) ?
			 * (Collection) reqData.get(TxnEnrollmentProperties.HEALTHASSES) :
			 * null; Collection selfAsses =
			 * (reqData.containsKey(TxnEnrollmentProperties.CAREASSES)) ?
			 * (Collection) reqData.get(TxnEnrollmentProperties.CAREASSES) :
			 * null;
			 * 
			 * Set<FarmerHealthAsses> healthAssesSet = new LinkedHashSet<>();
			 * Set<FarmerSelfAsses> selfAssesSet = new LinkedHashSet<>();
			 * 
			 * if (!CollectionUtil.isCollectionEmpty(healthAsses)) {
			 * List<com.sourcetrace.eses.txn.schema.Object> healthObjects =
			 * healthAsses.getObject(); healthObjects.stream().forEach(healthObj
			 * -> { FarmerHealthAsses farmerHealthAsses = new
			 * FarmerHealthAsses(); List<Data> healthObjDataList =
			 * healthObj.getData(); healthObjDataList.stream().forEach(data -> {
			 * String key = data.getKey(); String value = data.getValue(); if
			 * (TxnEnrollmentProperties.DISABILITYTYPE.equalsIgnoreCase(key)) {
			 * farmerHealthAsses.setDiabilityType(value); } else if
			 * (TxnEnrollmentProperties.DISABILITYTYPE.equalsIgnoreCase(key)) {
			 * farmerHealthAsses.setDiabilityType(value); } else if
			 * (TxnEnrollmentProperties.ORIGIN.equalsIgnoreCase(key)) {
			 * farmerHealthAsses.setOrigin(value); } else if
			 * (TxnEnrollmentProperties.REMARKZ.equalsIgnoreCase(key)) {
			 * farmerHealthAsses.setRemark(value); } else if
			 * (TxnEnrollmentProperties.PFSNALCONSULTATION.equalsIgnoreCase(key)
			 * ) { farmerHealthAsses.setConsultationStatus(value); } else if
			 * (TxnEnrollmentProperties.DETCONSULTATION.equalsIgnoreCase(key)) {
			 * farmerHealthAsses.setConsulatationDetail(value); } });
			 * farmerHealthAsses.setRevsionNo(DateUtil.getRevisionNumber());
			 * healthAssesSet.add(farmerHealthAsses); });
			 * farmer.setFarmerHealthAsses(healthAssesSet); }
			 * 
			 * if (!CollectionUtil.isCollectionEmpty(selfAsses)) {
			 * List<com.sourcetrace.eses.txn.schema.Object> selfAssesObjects =
			 * selfAsses.getObject();
			 * 
			 * selfAssesObjects.stream().forEach(selfAssesObj -> {
			 * FarmerSelfAsses farmerSelfAsses = new FarmerSelfAsses();
			 * List<Data> healthObjDataList = selfAssesObj.getData();
			 * healthObjDataList.stream().forEach(data -> { String key =
			 * data.getKey(); String value = data.getValue(); if
			 * (TxnEnrollmentProperties.ACTIVITY.equalsIgnoreCase(key)) {
			 * farmerSelfAsses.setActivity(value); } else if
			 * (TxnEnrollmentProperties.VALUE.equalsIgnoreCase(key)) {
			 * farmerSelfAsses.setValue(value); } else if
			 * (TxnEnrollmentProperties.REMARKZ.equalsIgnoreCase(key)) {
			 * farmerSelfAsses.setRemark(value); } });
			 * farmerSelfAsses.setRevsionNo(DateUtil.getRevisionNumber());
			 * selfAssesSet.add(farmerSelfAsses); });
			 * farmer.setFarmerSelfAsses(selfAssesSet); }
			 */

			/** PGSS Changes ENDS */

			/*
			 * Farm Farm = new Farm();
			 * Farm.setLandTopology(!StringUtil.isEmpty(landTopology)?
			 * landTopology : "");
			 */

			/*
			 * if (!StringUtil.isEmpty(hhid) && hhid.length() >= 8) { String
			 * warehouseCode = hhid.substring(0, 8);
			 * 
			 * String sanghamType =
			 * farmerService.findSanghamTypeFromWarehouseByWarehouseCode(
			 * warehouseCode);
			 * farmer.setSangham(!StringUtil.isEmpty(sanghamType) ? sanghamType
			 * : "");
			 * 
			 * } else { if (tenantId.equals("atma")) { String sanghamType =
			 * farmerService.findSanghamTypeFromWarehouseByWarehouseCode(
			 * samithiCode); farmer.setSangham(!StringUtil.isEmpty(sanghamType)
			 * ? sanghamType : ""); } else { farmer.setSangham(""); } }
			 */

			// Farm Inventry
			Set<FarmInventory> farmInventry = getInventories(requestMap);
			farmer.setFarmInventory(ObjectUtil.isListEmpty(farmInventry) ? null : farmInventry);

			// Animal Husbandary
			Set<AnimalHusbandary> animalHusbanday = getAnimals(requestMap);
			farmer.setAnimalHusbandary(ObjectUtil.isListEmpty(animalHusbanday) ? null : animalHusbanday);

			// farmer.setAgricultureImplements(getAgriImplements());
			if (getAgriImplements() != null)
				farmer.setAgricultureImplements(getAgriImplements());
			// farmer.setFingerPrint(fp);

			/*
			 * Set<FarmerSourceIncome> farmerSourceIncomeSet =
			 * getFarmerSourceIncome(requestMap);
			 * farmer.setFarmerSourceIncome(ObjectUtil.isListEmpty(
			 * farmerSourceIncomeSet) ? null : farmerSourceIncomeSet);
			 */

			Collection agriActCollection = (Collection) reqData
					.get(TxnEnrollmentProperties.AGRICULTURAL_ACTIVITIES_LIST);
			Collection horicultureCollection = (Collection) reqData.get(TxnEnrollmentProperties.HORTICULTURE_LIST);
			Collection alliedSectorCollection = (Collection) reqData.get(TxnEnrollmentProperties.ALLIED_SECTOR_LIST);
			Collection employmentCollection = (Collection) reqData.get(TxnEnrollmentProperties.EMPLOYMENT_LIST);
			Collection othersCollection = (Collection) reqData.get(TxnEnrollmentProperties.OTHERS_LIST);

			if (!ObjectUtil.isEmpty(agriActCollection)) {
				Map agriActMap = getFarmerSourceIncome(requestMap, agriActCollection, Farmer.AGRICULTURE_ACTIVITIES);
				// farmer.setFarmerSourceIncome((Set<FarmerSourceIncome>)
				// agriActMap.get(FARMER_SOURCE_INCOME));
				if (!ObjectUtil.isEmpty((FarmerSourceIncome) agriActMap.get(FARMER_SOURCE_INCOME)))
					sourceIncomeSet.add((FarmerSourceIncome) agriActMap.get(FARMER_SOURCE_INCOME));
			}

			if (!ObjectUtil.isEmpty(horicultureCollection)) {
				Map horicultureMap = getFarmerSourceIncome(requestMap, horicultureCollection, Farmer.HORTICULTURE);
				// farmer.setFarmerSourceIncome((Set<FarmerSourceIncome>)
				// horicultureMap.get(FARMER_SOURCE_INCOME));
				if (!ObjectUtil.isEmpty((FarmerSourceIncome) horicultureMap.get(FARMER_SOURCE_INCOME)))
					sourceIncomeSet.add((FarmerSourceIncome) horicultureMap.get(FARMER_SOURCE_INCOME));
			}

			if (!ObjectUtil.isEmpty(alliedSectorCollection)) {
				Map alliedSectorMap = getFarmerSourceIncome(requestMap, alliedSectorCollection, Farmer.ALLIED_SECTOR);
				// farmer.setFarmerSourceIncome((Set<FarmerSourceIncome>)
				// alliedSectorMap.get(FARMER_SOURCE_INCOME));
				if (!ObjectUtil.isEmpty((FarmerSourceIncome) alliedSectorMap.get(FARMER_SOURCE_INCOME)))
					sourceIncomeSet.add((FarmerSourceIncome) alliedSectorMap.get(FARMER_SOURCE_INCOME));
			}

			if (!ObjectUtil.isEmpty(employmentCollection)) {
				Map employmentMap = getFarmerSourceIncome(requestMap, employmentCollection, Farmer.EMPLOYMENT);
				// farmer.setFarmerSourceIncome((Set<FarmerSourceIncome>)
				// employmentMap.get(FARMER_SOURCE_INCOME));
				if (!ObjectUtil.isEmpty((FarmerSourceIncome) employmentMap.get(FARMER_SOURCE_INCOME)))
					sourceIncomeSet.add((FarmerSourceIncome) employmentMap.get(FARMER_SOURCE_INCOME));
			}

			if (!ObjectUtil.isEmpty(othersCollection)) {
				Map othersMap = getFarmerSourceIncome(requestMap, othersCollection, Farmer.OTHERS);
				// farmer.setFarmerSourceIncome((Set<FarmerSourceIncome>)
				// othersMap.get(FARMER_SOURCE_INCOME));
				if (!ObjectUtil.isEmpty((FarmerSourceIncome) othersMap.get(FARMER_SOURCE_INCOME)))
					sourceIncomeSet.add((FarmerSourceIncome) othersMap.get(FARMER_SOURCE_INCOME));
			}

			/*
			 * Set<FarmerSourceIncome> set1 = (Set<FarmerSourceIncome>)
			 * agriActMap.get(FARMER_SOURCE_INCOME); Set<FarmerSourceIncome>
			 * set2 = (Set<FarmerSourceIncome>)
			 * horicultureMap.get(FARMER_SOURCE_INCOME); Set set3 = null;
			 * set3.add(set1); set3.add(set2);
			 */

			if (!ObjectUtil.isEmpty(sourceIncomeSet))
				farmer.setFarmerSourceIncome(sourceIncomeSet);

			/** FORMING FARM LIST **/
			if (!tenantId.equals("atma")) {
				Map farmObjectMap = getFarm(requestMap, tenantId,plotCaptureTime,agentId);
				farmer.setFarms((Set<Farm>) farmObjectMap.get(FARM));
				harvestDatas = (Set<HarvestData>) farmObjectMap.get(HARVEST_DATA);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mode == ESETxn.ONLINE_MODE)
				throw new SwitchException(
						e instanceof SwitchException ? ((SwitchException) e).getCode() : SwitchErrorCodes.ERROR);
			else {
				statusCode = ESETxnStatus.ERROR.ordinal();
				statusMsg = e instanceof OfflineTransactionException ? ((OfflineTransactionException) e).getError()
						: e.getMessage().substring(0, e.getMessage().length() > 40 ? 40 : e.getMessage().length());
			}
		}
		farmer.setStatusCode(statusCode);
		farmer.setStatusMsg(statusMsg);
		/*
		 * if (ESETxnStatus.SUCCESS.ordinal() == statusCode) {
		 * farmer.setImageInfo(imageInfo); }
		 */
		farmer.setAccountBalance(0.00);
		/*
		 * farmer.setAccountRupee("0"); farmer.setAccountPaise("0");
		 */
		farmerDynamicData = new FarmerDynamicData();
		Farmer msgNumber = farmerService.findFarmerByMsgNo(head.getMsgNo());
		if (ObjectUtil.isEmpty(msgNumber) || msgNumber == null) {
			try {

				farmerService.addContractForFarmerAndHarvestData(farmer, agentId);
				if (farmer.getId() > 0 && imageInfo != null && !ObjectUtil.isEmpty(imageInfo)
						&& imageInfo.getPhoto() != null) {
					imageInfo.getPhoto().setImageId(farmer.getId() + "-FP");
					farmerService.addImageInfo(imageInfo);
					farmerService.updateFarmerImageInfo(farmer.getId(), imageInfo.getId());

				}

				Collection dynamicFields = (reqData.containsKey(TransactionProperties.DYNAMIC_FIELD))
						? (Collection) reqData.get(TransactionProperties.DYNAMIC_FIELD) : null;

				Collection dynamicList = (reqData.containsKey(TransactionProperties.DYNAMIC_FIELD_LIST))
						? (Collection) reqData.get(TransactionProperties.DYNAMIC_FIELD_LIST) : null;
				Collection photoList = (reqData.containsKey(TransactionProperties.DYNAMIC_IMAGE_LIST))
						? (Collection) reqData.get(TransactionProperties.DYNAMIC_IMAGE_LIST) : null;
						LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap = new LinkedHashMap<>();

				if (!CollectionUtil.isCollectionEmpty(dynamicFields) || !CollectionUtil.isCollectionEmpty(dynamicList)
						|| !CollectionUtil.isCollectionEmpty(photoList)) {
					List<DynamicFeildMenuConfig> dmList = farmerService.findDynamicMenusByMType("308");
					if (dmList != null && !dmList.isEmpty()) {
						try{
						farmerDynamicData = getFarmerDynamicFields(dynamicFields, dynamicList, photoList, tenantId,
								dmList.get(0), farmer);
						}catch(Exception ee){
							ee.printStackTrace();
						}

						farmerDynamicData.setReferenceId(String.valueOf(farmer.getId()));
						farmerDynamicData.setTxnUniqueId(Long.valueOf(head.getMsgNo()));
						farmerDynamicData.setReferenceId(String.valueOf(farmer.getId()));

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
						farmerDynamicData.setBranch(farmer.getBranchId());
						farmerDynamicData.setEntityId("1");
						farmerDynamicData.setTxnType(head.getTxnType());
						farmerDynamicData.setSeason(season);
						farmerDynamicData.setIsScore(dmList.get(0).getIsScore());
						farmerDynamicData.setScoreValue(new HashMap<>());
						farmerDynamicData.setActStatus(0);
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
						if( fieldConfigMap.values().stream().anyMatch(p -> Arrays.asList("4").contains(p.getIsMobileAvail()) && p.getFormula() != null
								&& !StringUtil.isEmpty(p.getFormula()))){
							farmerService.processCustomisedFormula(farmerDynamicData, fieldConfigMap);
				      }
						farmerService.deleteChildObjects(farmerDynamicData.getTxnType());
					}

				}
			} catch (Exception e) {
				statusMsg = e instanceof OfflineTransactionException ? ((OfflineTransactionException) e).getError()
						: e.getMessage().substring(0, e.getMessage().length() > 200 ? 200 : e.getMessage().length());
				farmer.setStatusCode(ESETxnStatus.ERROR.ordinal());
				farmer.setStatus(0);
				farmer.setStatusMsg(statusMsg);
				if (farmer.getId() > 0) {
					farmerService.editFarmerStatus(farmer.getId(), ESETxnStatus.ERROR.ordinal(), statusMsg);
				}

			}
			if(tenantId.equalsIgnoreCase(ESESystem.SYMRISE) || tenantId.equalsIgnoreCase(ESESystem.GRIFFITH_TENANT_ID) || tenantId.equalsIgnoreCase(ESESystem.AVT)){
				int seq=Integer.valueOf(farmer.getVillage().getSeq());
				seq=seq+1;
				farmer.getVillage().setSeq(String.valueOf(seq));
				farmerService.update(farmer.getVillage());
			}
			/*
			 * if(farmer.getId()>0) {
			 * //farmer.setFarmerSourceIncome(sourceIncomeSet); for
			 * (FarmerSourceIncome farmerSourceIncome : sourceIncomeSet) {
			 * farmerSourceIncome.setFarmerId(farmer.getId());
			 * farmerService.addfarmerSourceIncome(farmerSourceIncome); } }
			 */

			if (farmer.getStatusCode() == 0) {
				/** SAVING CARD INFORMATION **/
				agentService.createESECard(farmerId, farmerCardId,
						DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_DATE_TIME), ESECard.FARMER_CARD);
				if (Device.POS_DEVICE.equalsIgnoreCase(device.getDeviceType())) {
					agent.setFarmerCardIdSequence(farmerCardId);
					agent.setFarmerAccountNoSequence(farmerAcctNo);
				}
			}
			if (!StringUtil.isEmpty(farmerId)) {
				if (Long.parseLong(agent.getFarmerCurrentIdSeq()) < Long.parseLong(farmerId)) {
					agent.setFarmerCurrentIdSeq(farmerId);
					agentService.editAgent(agent);
				}
			}
		}
		/** FORM RESPONSE DATA **/
		Map resp = new HashMap();
		return resp;
	}

	private FarmerDynamicData getFarmerDynamicFields(Collection dynamicFields, Collection dynamicList,
			Collection photoList, String tenantId, DynamicFeildMenuConfig dm, Farmer farmer) {
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
						if (fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getIsOther() == 1) {
							if (Integer.valueOf(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
									.getAccessType()) == DynamicFieldConfig.ACCESS_TYPE.CATALOGUE_TYPE.ordinal()) {
								if (farmerDynamicFieldsValue.getFieldValue().contains(",")) {
									Arrays.asList(farmerDynamicFieldsValue.getFieldValue().split(",")).stream()
											.forEach(u -> {
												FarmCatalogue fm = catService.findCatalogueByCode(u);
												if (fm == null) {
													fm = new FarmCatalogue();
													fm.setCode(idServoce.getCatalogueIdSeq());
													fm.setName(u);
													fm.setRevisionNo(DateUtil.getRevisionNumber());
													fm.setStatus("1");
													fm.setTypez(Integer.valueOf(fieldConfigMap
															.get(farmerDynamicFieldsValue.getFieldName())
															.getCatalogueType()));
													catService.addCatalogue(fm);
													farmerDynamicFieldsValue.setFieldValue(farmerDynamicFieldsValue
															.getFieldValue().replaceAll(u, fm.getCode()));
												}

											});
								} else {
									FarmCatalogue fm = catService
											.findCatalogueByCode(farmerDynamicFieldsValue.getFieldValue());
									if (fm == null) {
										fm = new FarmCatalogue();
										fm.setCode(idServoce.getCatalogueIdSeq());
										fm.setName(farmerDynamicFieldsValue.getFieldValue());
										fm.setRevisionNo(DateUtil.getRevisionNumber());
										fm.setStatus("1");
										fm.setTypez(Integer.valueOf(fieldConfigMap
												.get(farmerDynamicFieldsValue.getFieldName()).getCatalogueType()));
										catService.addCatalogue(fm);
									}
									farmerDynamicFieldsValue.setFieldValue(fm.getCode());
								}
							}
						}
					} else if (TxnEnrollmentProperties.COMPONENT_TYPE.equalsIgnoreCase(key)) {
						farmerDynamicFieldsValue.setComponentType(value);
					}
				}
				farmerDynamicFieldsValue
				.setComponentType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getComponentType() : "0");
				
			
				farmerDynamicFieldsValue.setTxnType(dm.getTxnType());
				farmerDynamicFieldsValue.setReferenceId(String.valueOf(farmer.getId()));
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
						farmerDynamicFieldsValue.setTxnType("308");
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
				.setComponentType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
						? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getComponentType() : "0");
				
				
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
				
				farmerDynamicFieldsValue.setTxnType("308");
				farmerDynamicFieldsValue.setReferenceId(String.valueOf(farmer.getId()));
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
								.setComponentType(fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()) != null
										? fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName()).getComponentType() : "0");
								
							
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
				farmerDynamicFieldsValue.setTxnType("308");
				farmerDynamicFieldsValue.setDymamicImageData(imageDataSet);
				farmerDynamicFieldsValue.setReferenceId(String.valueOf(farmer.getId()));
				farmerDynamicFieldsValueList.add(farmerDynamicFieldsValue);

			}
		}
		farmerDynamicData.setFarmerDynamicFieldsValues(new HashSet<>(farmerDynamicFieldsValueList));

		return farmerDynamicData;

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
			throwError(SwitchErrorCodes.DATA_CONVERSION_ERROR);
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
			return (!StringUtil.isEmpty(value) ? Integer.parseInt(value) : -1);
		} catch (Exception e) {
			throwError(SwitchErrorCodes.DATA_CONVERSION_ERROR);
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
	public Set<FarmerFamily> getFarmerFamily(Map familyMap, String tenant)
			throws OfflineTransactionException, IOException {

		Set<FarmerFamily> farmerFamily = new HashSet<FarmerFamily>();
		Map requestData = (Map) familyMap.get(REQUEST_DATA);
		Collection familyCollection;
		List<com.sourcetrace.eses.txn.schema.Object> familyObject;
		if (!tenant.equalsIgnoreCase(ESESystem.CHETNA_TENANT_ID)) {
			familyCollection = (Collection) requestData.get(TxnEnrollmentProperties.FAMILY_MEMBERS);
			if (!CollectionUtil.isCollectionEmpty(familyCollection)) {
				familyObject = familyCollection.getObject();

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
		} else {
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
						family.setGender(value.equals("1") ? Farmer.SEX_MALE : Farmer.SEX_FEMALE);
					}

					if (TxnEnrollmentProperties.FAMILY_MEMBER_AGE.equalsIgnoreCase(key)) {
						validate(value, SwitchErrorCodes.EMPTY_AGE);
						family.setAge(buildIntValue(value));
					}

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
					if (TxnEnrollmentProperties.FARMILY_IS_HEAD.equalsIgnoreCase(key))
						family.setHeadOfFamily(value);

					if (TxnEnrollmentProperties.FAMILY_MEMBER_DISABILITY_DETAIL.equalsIgnoreCase(key)) {
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
	 * @param tenantId
	 * @param agentId 
	 * @return the farm
	 * @throws OfflineTransactionException
	 *             the offline transaction exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public Map getFarm(Map farmMap, String tenantId,String plotCaptureTime, String agentId) throws OfflineTransactionException, IOException {

		Map reqData = (Map) farmMap.get(REQUEST_DATA); // Farm
		Head headData =(Head) farmMap.get(HEAD);   
		Farmer farmer = (Farmer) farmMap.get(FARMER); // Farmer
		String labour = "";
		Map farmObjectMap = new HashMap();
		Set<Farm> farms = new HashSet<Farm>();
		Set<HarvestData> harvestDatas = new HashSet<HarvestData>();
		Map<String, Farm> farmRefMap = new HashMap<String, Farm>();

		Date formatedDate = DateUtil.convertStringToDate(plotCaptureTime, DateUtil.TXN_DATE_TIME);
		String serialNumber = (String) headData.getSerialNo();
		Device device = deviceService.findDeviceBySerialNumber(serialNumber);
		Agent agent = agentService.findAgentByProfileAndBranchId(agentId, device.getBranchId());
		AgentAccessLog agentAccessLog = agentService.findAgentAccessLogByAgentId(agent.getProfileId(),
				DateUtil.getDateWithoutTime(formatedDate));			
			
		BeanWrapper wrapper = new BeanWrapperImpl(TransactionProperties.HEAD);
		List<com.sourcetrace.eses.txn.schema.Object> farmObjects;
		List<Data> farmData;
		Collection farmCollection = (Collection) reqData.get(TxnEnrollmentProperties.FARM_LIST);
		if (!ObjectUtil.isEmpty(farmCollection)) {
			 farmObjects = farmCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object farmObject : farmObjects) {
				Date insDate = null;
				Set<FarmIcsConversion> icsConversion = new HashSet<FarmIcsConversion>();
				FarmIcsConversion conversion = new FarmIcsConversion();
				farmData = farmObject.getData();
				Farm farm = new Farm();
				farmMap.put(FARM, farm);
				FarmDetailedInfo detailInfo = new FarmDetailedInfo();
				HousingInfo housingInfo = new HousingInfo();
				Set<HousingInfo> housingSet = new HashSet<>();
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
					if (TxnEnrollmentProperties.FARM_CODE.equalsIgnoreCase(key)) {
						validate(value, SwitchErrorCodes.EMPTY_FARM_CODE);
						Farm existingFarm = farmerService.findFarmByCode(value);
						if (!ObjectUtil.isEmpty(existingFarm) || farmRefMap.containsKey(value))
							throwError(SwitchErrorCodes.FARM_CODE_EXIST);
						farm.setFarmCode(value);
					}
					if (TxnEnrollmentProperties.FARM_NAME.equalsIgnoreCase(key)) {

						validate(value, SwitchErrorCodes.EMPTY_FARM_NAME);
						farm.setFarmName(value);

					}
					if (TxnEnrollmentProperties.IRRI_ACRE.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value)) {
							farm.setIrrigationLand(value);
							;
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

					if (TxnEnrollmentProperties.VILLAGE_CODE.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value)) {
							Village village = locationService.findVillageByCode(value);
							if (!ObjectUtil.isEmpty(village)) {
								farm.setVillage(village);
							}
						}
					}
					if (TxnEnrollmentProperties.SAMITHI_CODE.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value)) {
							Warehouse warehouse = locationService.findSamithiByCode(value);
							if (!ObjectUtil.isEmpty(warehouse)) {
								farm.setSamithi(warehouse);
							}
						}
					}

					if (TxnEnrollmentProperties.FPOGROUP.equalsIgnoreCase(key)) {
						farm.setFpo(!StringUtil.isEmpty(value) ? value : "");
					}

					if(!StringUtil.isEmpty(plotCaptureTime)){
						farm.setPlotCapturingTime(DateUtil.convertStringToDate(plotCaptureTime, DateUtil.TXN_DATE_TIME));
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
					if (TxnEnrollmentProperties.PROPOSED_PLANTING_AREA.equalsIgnoreCase(key))
						detailInfo.setProposedPlantingArea(value);
					if (TxnEnrollmentProperties.FARM_OWNED.equalsIgnoreCase(key))
						detailInfo.setFarmOwned(StringUtil.isEmpty(value) ? "-1" : value);

					if (TxnEnrollmentProperties.LAND_GRADIENT.equalsIgnoreCase(key))
						detailInfo.setLandGradient(value);
					if (TxnEnrollmentProperties.APPROACH_ROAD.equalsIgnoreCase(key))
						detailInfo.setApproachRoad(value);
					if (TxnEnrollmentProperties.SEASON.equalsIgnoreCase(key)) {
						// validate(value,
						// SwitchErrorCodes.EMPTY_FARM_REGISTRATION_YEAR);
						detailInfo.setSessionYear(value);
					}
					if (TxnEnrollmentProperties.SOIL_TYPE.equalsIgnoreCase(key))
						detailInfo.setSoilType(value);
					if (TxnEnrollmentProperties.SOIL_TEXTURE.equalsIgnoreCase(key))
						detailInfo.setSoilTexture(value);
					if (TxnEnrollmentProperties.FERTILITY_STATUS.equalsIgnoreCase(key))
						detailInfo.setSoilFertility(buildDDIntValue(value));
					if (TxnEnrollmentProperties.IRRIGATION_SOURCEZ.equalsIgnoreCase(key))
						detailInfo.setFarmIrrigation(value);
					if (TxnEnrollmentProperties.IRRIGATION_SOURCE_TYPES.equalsIgnoreCase(key))
						detailInfo.setIrrigationSource(!StringUtil.isEmpty(value) ? value : "-1");
					if (TxnEnrollmentProperties.IRRIGATION_SOURCE_TYPES_OTHER.equalsIgnoreCase(key))
						detailInfo.setIrrigatedOther(value);
					if (TxnEnrollmentProperties.FULL_TIME_WORKERS_COUNT.equalsIgnoreCase(key))
						detailInfo.setFullTimeWorkersCount(value);
					if (TxnEnrollmentProperties.PART_TIME_WORKERS_COUNT.equalsIgnoreCase(key))
						detailInfo.setPartTimeWorkersCount(value);
					if (TxnEnrollmentProperties.SEASONAL_WORKERS_COUNT.equalsIgnoreCase(key))
						detailInfo.setSeasonalWorkersCount(value);

					if (TxnEnrollmentProperties.METHOD_OF_IRRIGATION.equalsIgnoreCase(key))
						detailInfo.setMethodOfIrrigation(value);
					/*
					 * if
					 * (TxnEnrollmentProperties.ATTEND_FFS.equalsIgnoreCase(key)
					 * ){
					 * detailInfo.setFarmerFieldSchool(Integer.parseInt(value));
					 * }
					 */

					if (TxnEnrollmentProperties.ATTEND_FFS.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value) && value != "") {
							detailInfo.setFarmerFieldSchool(Integer.parseInt(value));
						} else {
							detailInfo.setFarmerFieldSchool(-1);
						}
					}
					if (TxnEnrollmentProperties.IS_FFS_BENIFITED.equalsIgnoreCase(key))
						detailInfo.setIsFFSBenifited(value);

					if (TxnEnrollmentProperties.BOREWELL_STRUCTURE.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value) && value != "") {
							detailInfo.setBoreWellRechargeStructure(Integer.parseInt(value));
						} else {
							detailInfo.setBoreWellRechargeStructure(-1);
						}
					}

					if (TxnEnrollmentProperties.MILLET_CULTIVATED.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value) && value != "") {
							detailInfo.setMilletCultivated(Integer.parseInt(value));
						} else {
							detailInfo.setMilletCultivated(-1);
						}
					}

					if (TxnEnrollmentProperties.MILLET_CROP_TYPE.equalsIgnoreCase(key))
						detailInfo.setMilletCropType(value);

					if (TxnEnrollmentProperties.MILLET_CROP_COUNT.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value) && value != "") {
							detailInfo.setMilletCropCount(Integer.parseInt(value));
						} else {
							detailInfo.setMilletCropCount(-1);
						}
					}

					if (TxnEnrollmentProperties.FARM_CERTIFICATION_YEAR.equalsIgnoreCase(key)) {
						if (!StringUtil.isEmpty(value) && value != "") {
							farm.setCertYear(Integer.parseInt(value));
						} else {
							farm.setCertYear(0);
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

					if (TxnEnrollmentProperties.SOIL_TEST_REPORT.equalsIgnoreCase(key)) {
						if (data.getBinaryValue() != null) {
							Set<DocumentUpload> documentUploads = buildDocumentUploadDatas(data.getBinaryValue(), farm);
							if (!ObjectUtil.isEmpty(documentUploads))
								farm.setDocUpload(documentUploads);
						}
					}
					if (TxnEnrollmentProperties.FARM_PHOTO_CAPTURING_TIME.equalsIgnoreCase(key)) {
						// validate(value,
						// SwitchErrorCodes.EMPTY_FARM_PHOTO_CAPTURE_DT);
						if (!StringUtil.isEmpty(value)) { // Adding farm photo
															// capturing time
							try {
								Date photoCaptureDate = TXN_DATE_FORMAT.parse(value);
								farm.setPhotoCaptureTime(photoCaptureDate);
							} catch (Exception e) {
								LOGGER.info(e.getMessage());
							}
						}
					}
					// Wilmar
					if (tenantId.equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)
							|| tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID) || tenantId.equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)) {

						if (TxnEnrollmentProperties.DISTANCE_PROCESS_UNIT.equalsIgnoreCase(key)) {
							farm.setDistanceProcessingUnit(!StringUtil.isEmpty(value) ? String.valueOf(value) : "");
						}
						if (TxnEnrollmentProperties.PROCESSING_ACTIVITY.equalsIgnoreCase(key)) {
							if (!StringUtil.isEmpty(value) && value != "") {
								detailInfo.setProcessingActivity(Integer.parseInt(value));
							} else {
								detailInfo.setProcessingActivity(-1);
							}
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

					if (TxnEnrollmentProperties.FARM_LATITUDE.equalsIgnoreCase(key)) {
						// validate(value,
						// SwitchErrorCodes.EMPTY_FARM_PHOTO_LATITUDE);
						farm.setLatitude(value);
						if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) ){
							
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
					if (TxnEnrollmentProperties.FARM_LONGITUDE.equalsIgnoreCase(key)) {
						// validate(value,
						// SwitchErrorCodes.EMPTY_FARM_PHOTO_LONGITUDE);
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
					if (TxnEnrollmentProperties.FARM_HISTORY.equalsIgnoreCase(key)) {
						if(tenantId.equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID))
						{
							farm.setFarmerLandDetails(buildFarmHistory(data.getCollectionValue(), farm));
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

					int isCertificationType = farmer.getIsCertifiedFarmer();
					if (Farmer.CERTIFIED_YES == isCertificationType || tenantId.equals("pratibha")
							|| tenantId.equals("welspun")) {

						// Farm Labour

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
					}
					if (TxnEnrollmentProperties.FARM_CROPS_LIST.equalsIgnoreCase(key)) {
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

					}
					
					if (TxnEnrollmentProperties.FARM_LAND_AREA_GPS.equalsIgnoreCase(key)) {
						// Processing Farm Land Area
						if (data.getCollectionValue() != null) {
							//farm.setCoordinates(buildFarmLandArea(data.getCollectionValue(), farm));
							CoordinatesMap coordinatesMap=new CoordinatesMap();
							coordinatesMap.setFarmCoordinates(buildFarmLandArea(data.getCollectionValue(), farm));
							coordinatesMap.setAgentId(!StringUtil.isEmpty(agentId)?agentId:"");
							coordinatesMap.setArea(detailInfo.getTotalLandHolding());
							coordinatesMap.setDate(	farm.getPlotCapturingTime());
							coordinatesMap.setFarm(farm);
							coordinatesMap.setMidLatitude(farm.getLongitude());
							coordinatesMap.setMidLongitude(farm.getLongitude());
							coordinatesMap.setStatus(CoordinatesMap.Status.ACTIVE.ordinal());
							if(farm.getCoordinatesMap()!=null && !ObjectUtil.isListEmpty(farm.getCoordinatesMap())){
								farm.getCoordinatesMap().stream().forEach(co->{
									co.setStatus(CoordinatesMap.Status.INACTIVE.ordinal());
								});
								farm.getCoordinatesMap().add(coordinatesMap);
								
								}
								else{
									Set<CoordinatesMap> coMap=new LinkedHashSet<>();
									coMap.add(coordinatesMap);
									farm.setCoordinatesMap(coMap);
									//farm.setPlottingStatus(1);
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
					if (TxnEnrollmentProperties.FARM_OWNED_OTHER.equalsIgnoreCase(key)) {
						farm.setFarmOther(!StringUtil.isEmpty(value) ? value : "");
					}

					// Plot No
					if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
						if (TxnEnrollmentProperties.FARM_PLOT_NO.equalsIgnoreCase(key)) {
							farm.setFarmPlatNo(!StringUtil.isEmpty(value) ? value : "");
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

					if (TxnEnrollmentProperties.WATER_SRC.equalsIgnoreCase(key)) {
						farm.setWaterSource(!StringUtil.isEmpty(value) ? value : "");
					}

					if (TxnEnrollmentProperties.NME_CROTREE.equalsIgnoreCase(key)) {
						farm.setLocalNameOfCrotenTree(!StringUtil.isEmpty(value) ? value : "");
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
					// BLRI

					/*
					 * if (tenantId.equalsIgnoreCase(ESESystem.BLRI_TENANT_ID))
					 * {
					 * 
					 * if (TxnEnrollmentProperties.COW_NO.equalsIgnoreCase(key))
					 * { housingInfo.setNoCowShad(!StringUtil.isEmpty(value) ?
					 * String.valueOf(value) : "0"); }
					 * 
					 * if
					 * (TxnEnrollmentProperties.HOUSING_TYPE.equalsIgnoreCase(
					 * key)) {
					 * housingInfo.setHousingShadType(!StringUtil.isEmpty(value)
					 * ? value : ""); }
					 * 
					 * if
					 * (TxnEnrollmentProperties.COW_SPACE.equalsIgnoreCase(key))
					 * { housingInfo.setSpacePerCow(!StringUtil.isEmpty(value) ?
					 * value : ""); }
					 * 
					 * if (TxnEnrollmentProperties.COW_NO.equalsIgnoreCase(key)
					 * || TxnEnrollmentProperties.HOUSING_TYPE.equalsIgnoreCase(
					 * key) ||
					 * TxnEnrollmentProperties.COW_SPACE.equalsIgnoreCase(key))
					 * 
					 * {
					 * 
					 * housingInfo.setFarm(farm); housingSet.add(housingInfo);
					 * farm.setHousingInfos(housingSet); } }
					 */

					if (!ObjectUtil.isEmpty(detailInfo)) {
						farm.setFarmDetailedInfo(detailInfo);
					}
				}
				if(tenantId.equalsIgnoreCase(ESESystem.OLIVADO_TENANT_ID)){
					conversion.setIcsType("0");
				}
				
				if (!StringUtil.isEmpty(conversion.getIcsType())) {
					conversion.setIsActive(1);
					icsConversion.add(conversion);
					farm.setFarmICSConversion(icsConversion);
				}
				if (farm.getPhoto() != null) {
					farmer.setBasicInfo(1);
				}
				farm.setFarmer(farmer);
				farm.setIsVerified(0);
				farm.setFarmer(farmer);

				if (!ObjectUtil.isEmpty(detailInfo)) {
					if (detailInfo.isSameAddressofFarmer())
						detailInfo.setFarmAddress(farm.getFarmer().getAddress() + " "
								+ farm.getFarmer().getCity().getName() + " " + farm.getFarmer().getVillage().getName());
					farm.setFarmDetailedInfo(detailInfo);
				}

				
				if(tenantId.equalsIgnoreCase("awi")){
				farm.setFarmCode(StringUtil.getExact(idGenerator.getFarmWebIdSeq(),6)); }
				 
			/*	if (tenantId.equalsIgnoreCase(ESESystem.WILMAR_TENANT_ID)) {*/

					if (!ObjectUtil.isEmpty(farm) && !ObjectUtil.isEmpty(farm.getFarmer().getIsCertifiedFarmer())
							&& farm.getFarmer().getIsCertifiedFarmer() == 1
							&& !StringUtil.isEmpty(conversion.getIcsType())
							&& conversion.getIcsType().equalsIgnoreCase("3")) {
						conversion.setOrganicStatus("3");
					} else {
						conversion.setOrganicStatus("0");
					}

				/*}*/

				
		if (tenantId.equalsIgnoreCase(ESESystem.AWI_TENANT_ID)) {
			if(StringUtil.isEmpty(getAwiFarmCodeSeq())) {
				if(!ObjectUtil.isEmpty(farm.getVillage())) {
					String codeGen =idGenerator.getFarmWebCodeIdSeq( farm.getVillage().getCity().getCode().substring(0, 1),farm.getVillage().getGramPanchayat().getCode().substring(0,1)); 
					farm.setFarmId(codeGen); 
					setAwiFarmCodeSeq(codeGen); 
					} 
				}else { 
					String temp = getAwiFarmCodeSeq(); 
					if(!ObjectUtil.isEmpty(farm.getVillage())) { 
						String codePrefix= farm.getVillage().getCity().getCode().substring(0, 1) +farm.getVillage().getGramPanchayat().getCode().substring(0,1);
						
						if (!StringUtil.isEmpty(temp) && codePrefix.equalsIgnoreCase(temp.substring(0, 2))) {
							String seq = temp.substring(2, 6); 
							BigInteger bigInt = new BigInteger(seq.trim()); 
							String seqVal = bigInt.add(BigInteger.ONE).toString(); 
							String farmCode =getAwiFarmCodeSeq().substring(0, 2) +StringUtil.getExact(seqVal, 4); 
							farm.setFarmId(farmCode);
							setAwiFarmCodeSeq(farmCode); 
						} else { 
							String codeGen = idGenerator.getFarmWebCodeIdSeq(farm.getVillage().getCity().getCode().substring(0, 1),farm.getVillage().getGramPanchayat().getCode().substring(0,1)); 
							farm.setFarmId(codeGen); 
							setAwiFarmCodeSeq(codeGen); 
							} 
						}
				  } 
			}
		 
		farm.setCreatedUsername(agent.getPersonalInfo().getAgentName());
		farm.setCreatedDate(DateUtil.convertStringToDate(headData.getTxnTime(), DateUtil.TXN_DATE_TIME));

				farm.setRevisionNo(DateUtil.getRevisionNumber());
				farm.setStatus(Farmer.Status.ACTIVE.ordinal());
				farms.add(farm);
				farmRefMap.put(farm.getFarmCode(), farm);
			}//for
			 
		}//if
		
		farmObjectMap.put(FARM, farms);
		farmObjectMap.put(HARVEST_DATA, harvestDatas);
		return farmObjectMap;
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

		Head head = (Head) farmMap.get(HEAD);
		Set<FarmCrops> farmCrops = new HashSet<FarmCrops>();
		Collection cropCollection = (Collection) farmMap.get(FARM_CROP_COLLECTION);
		if (!ObjectUtil.isEmpty(cropCollection)) {
			List<com.sourcetrace.eses.txn.schema.Object> cropObjectList = cropCollection.getObject();

			for (com.sourcetrace.eses.txn.schema.Object cropObject : cropObjectList) {
				List<Data> cropDataList = cropObject.getData();
				FarmCrops farmCrop = new FarmCrops();

				String sowingDate = null;
				String estimatedHarvestDate = null;

				for (Data cropData : cropDataList) {
					String cropKey = cropData.getKey();
					String cropValue = cropData.getValue();

					if (TxnEnrollmentProperties.FARM_CROPS_CODE.equalsIgnoreCase(cropKey)) {
						validate(cropValue, SwitchErrorCodes.EMPTY_FARM_VARIETY_CODE_MAIN);
						ProcurementVariety procurementVariety = farmCropsService
								.findProcurementVarietyByCode(cropValue);
						validate(procurementVariety, SwitchErrorCodes.FARM_VARIETY_DOES_NOT_EXIST_MAIN);
						farmCrop.setProcurementVariety(procurementVariety);

					}

					else if (TxnEnrollmentProperties.STAPLE_LENGTH_MAIN.equalsIgnoreCase(cropKey))
						farmCrop.setStapleLength(StringUtil.isEmpty(cropValue) ? "0.0" : cropValue);

					else if (TxnEnrollmentProperties.SEED_USED_MAIN.equalsIgnoreCase(cropKey))
						farmCrop.setSeedQtyUsed(StringUtil.isEmpty(cropValue) ? 0.0 : Double.valueOf(cropValue));

					else if (TxnEnrollmentProperties.SEED_COST_MAIN.equalsIgnoreCase(cropKey))
						farmCrop.setSeedQtyCost(StringUtil.isEmpty(cropValue) ? 0.0 : Double.valueOf(cropValue));

					else if (TxnEnrollmentProperties.EST_YIELD.equalsIgnoreCase(cropKey))
						farmCrop.setEstimatedYield(StringUtil.isEmpty(cropValue) ? 0.0 : Double.valueOf(cropValue));

					else if (TxnEnrollmentProperties.FARM_CROP_TYPE.equalsIgnoreCase(cropKey))
						farmCrop.setType((StringUtil.isEmpty(cropValue) || cropValue == null) ? "" : cropValue);

					else if (TxnEnrollmentProperties.SEED_SOURCE.equalsIgnoreCase(cropKey))
						farmCrop.setSeedSource(StringUtil.isEmpty(cropValue) ? "" : cropValue);
					// if
					// (TxnEnrollmentProperties.PRODUCTION_YEAR.equalsIgnoreCase(cropKey))
					// farmCrop.setProductionPerYear(cropValue);
					else if (TxnEnrollmentProperties.CROP_SEASON.equalsIgnoreCase(cropKey)) {
						// validate(cropValue,
						// SwitchErrorCodes.EMPTY_CROP_SEASON);
						HarvestSeason hs = farmerService.findHarvestSeasonByCode(cropValue);
						farmCrop.setCropSeason(hs);
					} else if (TxnEnrollmentProperties.CROP_CATEGORY.equalsIgnoreCase(cropKey)) {
						validate(cropValue, SwitchErrorCodes.EMPTY_CROP_CATEGORY);
						farmCrop.setCropCategory(buildDDIntValue(cropValue));
					} else if (TxnEnrollmentProperties.SEED_TREATMENT_DETAILS.equalsIgnoreCase(cropKey)) {
						farmCrop.setSeedTreatmentDetails(cropValue);
					} else if (TxnEnrollmentProperties.OTHER_SEED_TREATMENT_DETAILS.equalsIgnoreCase(cropKey)) {
						farmCrop.setOtherSeedTreatmentDetails(cropValue);
						addFarmCatalogue(cropValue);
					} else if (TxnEnrollmentProperties.RISK_ASSESMENT.equalsIgnoreCase(cropKey)) {
						farmCrop.setRiskAssesment(cropValue);
					} else if (TxnEnrollmentProperties.RISK_BUFFER_ZONE_DISTANCE.equalsIgnoreCase(cropKey)) {
						farmCrop.setRiskBufferZoneDistanse(cropValue);
					}

					else if (TxnEnrollmentProperties.SOWING_DATE.equalsIgnoreCase(cropKey)) {
						if (!StringUtil.isEmpty(cropValue)) {
							farmCrop.setSowingDate(DateUtil.convertStringToDate(cropValue, DateUtil.DATE));
						}
					}

					else if (TxnEnrollmentProperties.ESTIMATED_HARVEST_DATE.equalsIgnoreCase(cropKey)) {
						if (!StringUtil.isEmpty(cropValue)) {
							farmCrop.setEstimatedHarvestDate(DateUtil.convertStringToDate(cropValue, DateUtil.DATE));
						}

					}

					else if (TxnEnrollmentProperties.FARM_CROP_TYPE_OTHER.equalsIgnoreCase(cropKey)) {
						farmCrop.setOtherType(cropValue);

					} else if (TxnEnrollmentProperties.CULTIVATION_TYPE.equalsIgnoreCase(cropKey)) {
						farmCrop.setCropCategoryList(cropValue);

					} else if (TxnEnrollmentProperties.CULTI_AREA.equalsIgnoreCase(cropKey)) {
						farmCrop.setCultiArea(cropValue);

					} else if (TxnEnrollmentProperties.NO_OF_TREES.equalsIgnoreCase(cropKey)) {
						farmCrop.setNoOfTrees(StringUtil.isEmpty(cropValue) ? "" : cropValue);
					}

					// if
					// (TxnEnrollmentProperties.PRODUCTION_YEAR.equalsIgnoreCase(cropKey))
					// farmCrop.setProductionPerYear(cropValue);

				}
				Farm farm = (Farm) farmMap.get(FARM); // Check Duplication
				if (!ObjectUtil.isEmpty(farm.getFarmCrops())) {
					for (FarmCrops crops : farm.getFarmCrops()) {
						if (crops.getProcurementVariety().getCode()
								.equalsIgnoreCase(farmCrop.getProcurementVariety().getCode()))
							throwError(SwitchErrorCodes.FARM_CROPS_ALREADY_MAPPED_WITH_THIS_FARM);
					}
				}
				farmCrop.setPlotCapturingTime(DateUtil.convertStringToDate(plotCaptureTime, DateUtil.TXN_DATE_TIME));
				farmCrop.setBranchId(head.getBranchId());
				farmCrop.setFarm(farm);
				farmCrop.setStatus(Farmer.Status.ACTIVE.ordinal());
				farmCrop.setRevisionNo(DateUtil.getRevisionNumber());
				farmCrops.add(farmCrop);

			}
		}
		return farmCrops;
	}

	@SuppressWarnings("unchecked")
	public Set<FarmCrops> getDefalutFarmCrops(String value, Farm farm) throws OfflineTransactionException, IOException {

		Set<FarmCrops> farmCrops = new HashSet<FarmCrops>();
		FarmCrops farmCrop = new FarmCrops();

		ProcurementVariety procurementVariety = farmCropsService.findProcurementVarietyByCode(value);
		validate(procurementVariety, SwitchErrorCodes.FARM_VARIETY_DOES_NOT_EXIST_MAIN);
		farmCrop.setProcurementVariety(procurementVariety);
		HarvestSeason season = farmerService.findHarvestSeasonByCode(SEASON);
		farmCrop.setCropSeason(season);
		farmCrop.setCropCategory(farmerCertificationType);
		farmCrop.setPreSowingProd(farm.getFarmDetailedInfo().getProposedPlantingArea());
		farmCrop.setCultiArea(farm.getFarmDetailedInfo().getProposedPlantingArea());
		if (!ObjectUtil.isEmpty(farm.getFarmCrops())) {
			for (FarmCrops crops : farm.getFarmCrops()) {
				if (crops.getProcurementVariety().getCode()
						.equalsIgnoreCase(farmCrop.getProcurementVariety().getCode()))
					throwError(ITxnErrorCodes.FARM_CROPS_ALREADY_MAPPED_WITH_THIS_FARM);
			}
		}
		farmCrop.setRevisionNo(DateUtil.getRevisionNumber());
		farmCrop.setBranchId(BRANCH_ID);
		farmCrop.setStatus(Farmer.Status.ACTIVE.ordinal());
		farmCrop.setFarm(farm);
		farmCrops.add(farmCrop);

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
	@SuppressWarnings({ "unchecked", "unused" })
	private Set<HarvestData> getHarvestList(Map farmMap) throws OfflineTransactionException, IOException {

		Set<HarvestData> harvestDatas = new HashSet<HarvestData>();
		Farm farm = (Farm) farmMap.get(FARM);
		Map farmCropMap = new HashMap(); // Farm Crops
		Iterator itCrop = farm.getFarmCrops().iterator();
		while (itCrop.hasNext()) {
			FarmCrops crops = (FarmCrops) itCrop.next();
			farmCropMap.put(crops.getProcurementVariety().getProcurementProduct().getCode(), crops);
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
						validate(harvestValue, SwitchErrorCodes.EMPTY_HARVEST_FARM_CROPS_CODE_REF);
						if (farmCropMap.containsKey(harvestValue))
							harvest.setFarmCrops((FarmCrops) farmCropMap.get(harvestValue));
						else
							throwError(SwitchErrorCodes.INVALID_HARVEST_FARM_CROPS_CODE);
					}
					if (TxnEnrollmentProperties.HARVEST_DATE.equalsIgnoreCase(harvestKey)) {
						validate(harvestValue, SwitchErrorCodes.EMPTY_HARVEST_DATE);
						try {
							harvest.setHarvestedDate(
									DateUtil.convertStringToDate(harvestValue, DateUtil.TXN_DATE_TIME));
						} catch (Exception e) {
							throwError(SwitchErrorCodes.INVALID_DATE_FORMAT);
						}
					}
					if (TxnEnrollmentProperties.HARVEST_QUANTITY.equalsIgnoreCase(harvestKey)) {
						validate(harvestValue, SwitchErrorCodes.EMPTY_HARVEST_QUANTITY);
						harvest.setHarvested(harvestValue);
					}
					if (TxnEnrollmentProperties.HARVEST_AMOUNT.equalsIgnoreCase(harvestKey)) {
						validate(harvestValue, SwitchErrorCodes.EMPTY_HARVEST_AMOUNT);
						harvest.setHarvestedAmount(harvestValue);
					}
					if (TxnEnrollmentProperties.BUYER_NAME.equalsIgnoreCase(harvestKey)) {
						validate(harvestValue, SwitchErrorCodes.EMPTY_BUYER_NAME);
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
	@SuppressWarnings("unchecked")
	private Set<AnimalHusbandary> getAnimals(Map animalMap) throws OfflineTransactionException, IOException {

		Set<AnimalHusbandary> animals = new HashSet<AnimalHusbandary>();
		Map requestData = (Map) animalMap.get(REQUEST_DATA);
		Collection animalCollection = (Collection) requestData.get(TxnEnrollmentProperties.FARM_ANIMALS);

		if (!ObjectUtil.isEmpty(animalCollection)) {
			List<com.sourcetrace.eses.txn.schema.Object> animalObjects = animalCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object animalObject : animalObjects) {
				List<Data> animalDataList = animalObject.getData();
				AnimalHusbandary animal = new AnimalHusbandary();
				for (Data animalData : animalDataList) {
					String animalKey = animalData.getKey();
					String animalValue = animalData.getValue();
					if (TxnEnrollmentProperties.FARM_ANIMAL.equalsIgnoreCase(animalKey)) {
						validate(animalValue, SwitchErrorCodes.EMPTY_FARM_ANIMAL);
						FarmCatalogue cat = catalogueService.findCatalogueByCode(animalValue);
						animal.setFarmAnimal(cat);
					} else if (TxnEnrollmentProperties.FARM_ANIMAL_OTHER.equalsIgnoreCase(animalKey))
						animal.setOtherFarmAnimal(animalValue);
					else if (TxnEnrollmentProperties.FARM_ANIMAL_COUNT.equalsIgnoreCase(animalKey)) {
						// validate(animalValue,
						// SwitchErrorCodes.EMPTY_FARM_ANIMAL_COUNT);
						animal.setAnimalCount(StringUtil.isEmpty(animalValue) ? String.valueOf("0") : animalValue);
					} else if (TxnEnrollmentProperties.ANIMAL_HOUSE.equalsIgnoreCase(animalKey)) {
						// validate(animalValue,
						// SwitchErrorCodes.EMPTY_FARM_ANIMAL_HOUSE);
						FarmCatalogue cat = catalogueService.findCatalogueByCode(animalValue);
						animal.setAnimalHousing(cat);
					} else if (TxnEnrollmentProperties.ANIMAL_HOUSING_OTHERS.equalsIgnoreCase(animalKey)) {
						animal.setOtherAnimalHousing(StringUtil.isEmpty(animalValue) ? null : animalValue);
					} else if (TxnEnrollmentProperties.FODDER.equalsIgnoreCase(animalKey)) {
						if (!StringUtil.isEmpty(animalValue) && animalValue != null) {
							String fodderValue = "";
							FarmCatalogue fodder = new FarmCatalogue();
							String fodderArr[] = animalValue.split("\\,");
							for (int i = 0; i < fodderArr.length; i++) {
								String fodderTrim = fodderArr[i].replaceAll("\\s+", "");
								fodder = catalogueService.findCatalogueByCode(fodderTrim);

								fodderValue += fodder.getId() + ",";

							}
							fodderValue = fodderValue.substring(0, fodderValue.length() - 1);

							animal.setFodder(fodderValue);
						} else {
							animal.setFodder("");
						}
						/*
						 * FarmCatalogue cat =
						 * catalogueService.findCatalogueByCode(animalValue);
						 * 
						 * if(!ObjectUtil.isEmpty(cat)){ String
						 * animalName=cat.getName();
						 * animal.setFodder(animalName); }
						 */

					}

					else if (TxnEnrollmentProperties.FODDER_OTHER.equalsIgnoreCase(animalKey))
						animal.setOtherFodder(StringUtil.isEmpty(animalValue) ? null : animalValue);

					/*
					 * else if
					 * (TxnEnrollmentProperties.REVENUE.equalsIgnoreCase(
					 * animalKey)){ FarmCatalogue cat =
					 * catalogueService.findCatalogueByCode(animalValue);
					 * animal.setRevenue(cat); }
					 */

					else if (TxnEnrollmentProperties.REVENUE.equalsIgnoreCase(animalKey)) {
						animal.setRevenue(StringUtil.isEmpty(animalValue) ? null : animalValue);
					}

					else if (TxnEnrollmentProperties.REVENUE_OTHER.equalsIgnoreCase(animalKey))
						animal.setOtherRevenue(StringUtil.isEmpty(animalValue) ? null : animalValue);

					else if (TxnEnrollmentProperties.BREED.equalsIgnoreCase(animalKey))
						animal.setBreed(StringUtil.isEmpty(animalValue) ? null : animalValue);
					else if (TxnEnrollmentProperties.MANURE_COLLECT.equalsIgnoreCase(animalKey))
						animal.setManureCollected(StringUtil.isEmpty(animalValue) ? null : animalValue);
					else if (TxnEnrollmentProperties.URINE_COLLECT.equalsIgnoreCase(animalKey))
						animal.setUrineCollected(StringUtil.isEmpty(animalValue) ? null : animalValue);

				}
				animal.setFarmer((Farmer) animalMap.get(FARMER));
				animals.add(animal);
			}
		}
		return animals;
	}

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
	@SuppressWarnings("unchecked")
	public Set<FarmInventory> getInventories(Map inventoryMap) throws OfflineTransactionException, IOException {
		setAgriImplements("");
		Set<FarmInventory> inventories = new HashSet<FarmInventory>();
		Map requestData = (Map) inventoryMap.get(REQUEST_DATA);
		Collection inventoryCollection = (Collection) requestData.get(TxnEnrollmentProperties.FARM_INVENTORIES);
		if (!ObjectUtil.isEmpty(inventoryCollection)) {
			List<com.sourcetrace.eses.txn.schema.Object> inventoryObjects = inventoryCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object inventoryObject : inventoryObjects) {
				List<Data> inventoryDataList = inventoryObject.getData();
				FarmInventory inventory = new FarmInventory();
				for (Data inventoryData : inventoryDataList) {
					String inventoryKey = inventoryData.getKey();
					String inventoryValue = inventoryData.getValue();
					if (TxnEnrollmentProperties.FARM_INVENTORY_ITEM.equalsIgnoreCase(inventoryKey)) {
						// validate(inventoryValue,
						// SwitchErrorCodes.EMPTY_INVENTORY_ITEM_ID);
						FarmCatalogue cat = catalogueService.findCatalogueByCode(inventoryValue);
						inventory.setInventoryItem(cat);

						String agriImplmnts = cat.getCode() + "," + getAgriImplements();
						setAgriImplements(StringUtil.removeLastComma(agriImplmnts));
					}
					if (TxnEnrollmentProperties.FARM_EQUIPMENTS_OTHERS.equalsIgnoreCase(inventoryKey)) {
						inventory.setOtherInventoryItem(StringUtil.isEmpty(inventoryValue) ? null : inventoryValue);
					}

					if (TxnEnrollmentProperties.FARM_INVENTORY_ITEM_COUNT.equalsIgnoreCase(inventoryKey)) {
						inventory.setItemCount(StringUtil.isEmpty(inventoryValue) ? null : inventoryValue);
					}

				}
				inventory.setFarmer((Farmer) inventoryMap.get(FARMER));
				inventories.add(inventory);
			}
		}
		return inventories;
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
						farmerLandDetails.setEstimatedAcreage(Double.valueOf(value));
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
	 * Gets the bank info.
	 * 
	 * @param bankInfoMap
	 * @return the bank info
	 * @throws OfflineTransactionException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public Set<BankInformation> getBankInfo(Map bankInfoMap, String tenantId)
			throws OfflineTransactionException, IOException {

		Set<BankInformation> bankInfo = new HashSet<BankInformation>();
		Map requestData = (Map) bankInfoMap.get(REQUEST_DATA);
		Collection bankCollection = (Collection) requestData.get(TxnEnrollmentProperties.BANK_INFORMATION);
		if (!ObjectUtil.isEmpty(bankCollection)) {

			List<com.sourcetrace.eses.txn.schema.Object> bankObjects = bankCollection.getObject();
			for (com.sourcetrace.eses.txn.schema.Object bankObject : bankObjects) {
				List<Data> bankDataList = bankObject.getData();
				BankInformation bank = new BankInformation();

				for (Data bankData : bankDataList) {
					String key = bankData.getKey();
					String value = bankData.getValue();
					if (TxnEnrollmentProperties.ACCOUNT_TYPE.equalsIgnoreCase(key)) {
						bank.setAccType(value);
					}

					else if (TxnEnrollmentProperties.BANK_NAME.equalsIgnoreCase(key)) {
						if (!tenantId.equalsIgnoreCase("chetna")) {
							bank.setBankName(value);
						} else {
							FarmCatalogue fCat = catalogueService.findCatalogueByCode(value);
							bank.setBankName(!ObjectUtil.isEmpty(fCat) ? fCat.getName() : "");
						}
					} else if (TxnEnrollmentProperties.ACCOUNT_NUMBER.equalsIgnoreCase(key)) {
						bank.setAccNo(value);
					} else if (TxnEnrollmentProperties.BRANCH_DETAILS.equalsIgnoreCase(key)) {
						if (!tenantId.equalsIgnoreCase("chetna")) {
							bank.setBranchName(value);
						} else {
							FarmCatalogue fCat = catalogueService.findCatalogueByCode(value);
							bank.setBranchName(!ObjectUtil.isEmpty(fCat) ? fCat.getName() : "");
						}

					} else if (TxnEnrollmentProperties.SORT_CODE.equalsIgnoreCase(key)) {
						bank.setSortCode(value);
					} else if (TxnEnrollmentProperties.SWIFT_CODE.equalsIgnoreCase(key)) {
						bank.setSwiftCode(value);
					}else if (TxnEnrollmentProperties.ACC_NAME.equalsIgnoreCase(key)) {
						bank.setAccName(value);
					}
				}
				bank.setFarmer((Farmer) bankInfoMap.get(FARMER));
				bankInfo.add(bank);
			}
		}
		return bankInfo;
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
	 * public Map getFarm1(Map farmMap) throws OfflineTransactionException,
	 * IOException { Map reqData = (Map) farmMap.get(REQUEST_DATA); // Farm
	 * Farmer farmer = (Farmer) farmMap.get(FARMER); // Farmer String labour =
	 * ""; Map farmObjectMap = new HashMap(); Set<Farm> farms = new
	 * HashSet<Farm>(); Set<HarvestData> harvestDatas = new
	 * HashSet<HarvestData>(); Map<String, Farm> farmRefMap = new
	 * HashMap<String, Farm>(); Collection farmCollection = (Collection)
	 * reqData.get(TxnEnrollmentProperties.FARM_LIST); if
	 * (!ObjectUtil.isEmpty(farmCollection)) {
	 * List<com.sourcetrace.eses.txn.schema.Object> farmObjects =
	 * farmCollection.getObject(); for (com.sourcetrace.eses.txn.schema.Object
	 * farmObject : farmObjects) { List<Data> farmData = farmObject.getData();
	 * Farm farm = new Farm(); farmMap.put(FARM, farm); FarmDetailedInfo
	 * detailInfo = new FarmDetailedInfo(); // Default values for farm
	 * non-mandatory values detailInfo.setFarmOwned(SELECT);
	 * detailInfo.setLandGradient(SELECT_MULTI); for (Data data : farmData) {
	 * String key = data.getKey(); String value = data.getValue(); if
	 * (TxnEnrollmentProperties.FARM_CODE.equalsIgnoreCase(key)) {
	 * validate(value, SwitchErrorCodes.EMPTY_FARM_CODE); Farm existingFarm =
	 * farmerService.findFarmByCode(value); if
	 * (!ObjectUtil.isEmpty(existingFarm) || farmRefMap.containsKey(value))
	 * throwError(SwitchErrorCodes.FARM_CODE_EXIST); farm.setFarmCode(value); }
	 * if (TxnEnrollmentProperties.FARM_NAME.equalsIgnoreCase(key)) {
	 * validate(value, SwitchErrorCodes.EMPTY_FARM_NAME);
	 * farm.setFarmName(value); } if
	 * (TxnEnrollmentProperties.SURVEY_NUMBER.equalsIgnoreCase(key))
	 * detailInfo.setSurveyNumber(value); if
	 * (TxnEnrollmentProperties.FARM_LATITUDE.equalsIgnoreCase(key)) { //
	 * validate(value, SwitchErrorCodes.EMPTY_FARM_PHOTO_LATITUDE);
	 * farm.setLatitude(value); } if
	 * (TxnEnrollmentProperties.FARM_LONGITUDE.equalsIgnoreCase(key)) { //
	 * validate(value, SwitchErrorCodes.EMPTY_FARM_PHOTO_LONGITUDE);
	 * farm.setLongitude(value); } int isCertificationType =
	 * farmer.getIsCertifiedFarmer(); if (Farmer.CERTIFIED_YES ==
	 * isCertificationType) { // Farm Labour if
	 * (TxnEnrollmentProperties.PASTURE_LAND.equalsIgnoreCase(key))
	 * detailInfo.setFallowOrPastureLand(value); // Farm Inventories if
	 * (TxnEnrollmentProperties.FARM_INVENTORIES.equalsIgnoreCase(key)) {
	 * farmMap.put(INVENTORY_COLLECTION, data.getCollectionValue());
	 * farm.setFarmInventory(getInventories(farmMap)); } // Farm Animals if
	 * (TxnEnrollmentProperties.FARM_ANIMALS.equalsIgnoreCase(key)) {
	 * farmMap.put(ANIMAL_COLLECTION, data.getCollectionValue());
	 * farm.setAnimalHusbandary(getAnimals(farmMap)); } // Land ICS details if
	 * (TxnEnrollmentProperties.ICS_LIST.equalsIgnoreCase(key)) {
	 * farm.setFarmICS(buildFarmICSDatas(data.getCollectionValue(), farm)); } }
	 * if (TxnEnrollmentProperties.FARM_CROPS_LIST.equalsIgnoreCase(key)) { //
	 * Farm Crops farmMap.put(FARM_CROP_COLLECTION, data.getCollectionValue());
	 * farm.setFarmCrops(getFarmCrops(farmMap)); } // Machinary if
	 * (TxnEnrollmentProperties.MACHINARY_LIST.equalsIgnoreCase(key)) {
	 * farmMap.put(MACHINARY_COLLECTION, data.getCollectionValue());
	 * farm.setFarmElementMach(getMachinary(farmMap)); } } if
	 * (!ObjectUtil.isEmpty(detailInfo)) { if
	 * (detailInfo.isSameAddressofFarmer())
	 * detailInfo.setFarmAddress(farmer.getAddress());
	 * farm.setFarmDetailedInfo(detailInfo); } farm.setIsVerified(0);
	 * farm.setFarmer(farmer); farms.add(farm);
	 * farmRefMap.put(farm.getFarmCode(), farm); } } farmObjectMap.put(FARM,
	 * farms); farmObjectMap.put(HARVEST_DATA, harvestDatas); return
	 * farmObjectMap; }
	 */

	public void addFarmCatalogue(String catalogueName) {

		if (!StringUtil.isEmpty(catalogueName)) {
			FarmCatalogue catalogue = new FarmCatalogue();
			catalogue.setName(catalogueName);
			catalogue.setCode(idGenerator.getCatalogueIdSeq());
			catalogue.setRevisionNo(DateUtil.getRevisionNumber());
			// catalogue.setBranchId(branchIdTemp);
			catalogue.setTypez(TxnEnrollmentProperties.FARM_CATALOGUE_TYPE_SEED);
			catalogueService.addCatalogue(catalogue);
		}
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

	public String getAwiFarmCodeSeq() {

		return awiFarmCodeSeq;
	}

	public void setAwiFarmCodeSeq(String awiFarmCodeSeq) {

		this.awiFarmCodeSeq = awiFarmCodeSeq;
	}

	public Map<String, DynamicFieldConfig> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, DynamicFieldConfig> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public String getAgriImplements() {
		return agriImplements;
	}

	public void setAgriImplements(String agriImplements) {
		this.agriImplements = agriImplements;
	}

	private DataHandler getBase64EncodedImageFromByteArray(byte[] image) {

		return new DataHandler(
				new ByteArrayDataSource((ObjectUtil.isEmpty(image) ? new byte[0] : image), IMAGE_CONTENT_TYPE));
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
		Head head = (Head) farmMap.get(HEAD);
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
}	
