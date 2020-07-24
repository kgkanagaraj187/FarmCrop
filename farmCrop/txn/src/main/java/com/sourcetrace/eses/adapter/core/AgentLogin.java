/*
 * AgentLogin.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.AgentType;
import com.sourcetrace.eses.entity.BranchMaster;
import com.sourcetrace.eses.entity.Device;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IDeviceService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.ITrainingService;
import com.sourcetrace.eses.service.IUniqueIDGenerator;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.ShopDealer;
import com.sourcetrace.esesw.entity.txn.ESETxn;

/**
 * The Class AgentLogin.
 */
@Component
public class AgentLogin implements ITxnAdapter {

	SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.SYNC_DATE_TIME);
	private static final Logger LOGGER = Logger.getLogger(AgentLogin.class.getName());

	@Autowired
	private IAgentService agentService;
	@Autowired
	private IDeviceService deviceService;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IClientService clientService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private ITrainingService trainingService;
	private Map<String, ITxnAdapter> txnMap;
	private String MOBILE_VERSION_DETAILS;
	private String tenantId;
	private String branch;
	public static final String REMOTE_APK_VERSION = "REMOTE_APK_VERSION";
	public static final String REMOTE_CONFIG_VERSION = "REMOTE_CONFIG_VERSION";
	public static final String REMOTE_DB_VERSION = "REMOTE_DB_VERSION";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map reqData) {

		MOBILE_VERSION_DETAILS = "1";
		/** GET REQUEST DATA **/
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String agentId = head.getAgentId();
		String serialNo = head.getSerialNo();
		DecimalFormat formatter = new DecimalFormat("0.00");
		tenantId = head.getTenantId();
		branch = head.getBranchId();
		LOGGER.info("AGENT ID : " + agentId);
		LOGGER.info("SERIAL NO : " + serialNo);
		Device device = deviceService.findDeviceBySerialNumber(serialNo);
		Agent agent = agentService.findAgentByProfileAndBranchId(agentId, device.getBranchId());
		agent.setCreateTime(DateUtil.convertStringToDate(head.getTxnTime(), DateUtil.TXN_TIME_FORMAT));
		agentService.update(agent);
		ESEAccount account = accountService.findAccountByProfileIdAndProfileType(agent.getProfileId(),
				ESEAccount.AGENT_ACCOUNT);
		ESESystem eseSystem = preferncesService.findPrefernceById("1");
		ESESystem preferences = preferncesService.findPrefernceById("2");
		String branchId = device.getBranchId();
		BranchMaster branchMaster= clientService.findBranchMasterByBranchId(branchId);
		ESESystem eseSystemUser = preferncesService.findPrefernceByOrganisationId(branchId);
		eseSystemUser = eseSystemUser == null || eseSystemUser.getPreferences() == null
				|| eseSystemUser.getPreferences().isEmpty() ? eseSystem : eseSystemUser;
        
		/** VALIDATE WEB_BOD STATUS **/
		if (agent.getBodStatus() == ESETxn.WEB_BOD) {
			throw new SwitchException(ITxnErrorCodes.WEB_BOD_EXIST,
					(Long) reqData.get(TransactionProperties.TXN_LOG_ID));
		}
		/** VALIDATE AGENT TYPE **/
		if (AgentType.COOPERATIVE_MANAGER.equalsIgnoreCase(agent.getAgentType().getCode()))
			throw new SwitchException(ITxnErrorCodes.AGENT_UNAUTHORIZED);

		long aRevNo = 0;
		String agentRevisionNo = (String) reqData.get(TxnEnrollmentProperties.AGENT_REVISION_NO);
		if (!StringUtil.isEmpty(agentRevisionNo)) {
			aRevNo = Long.valueOf(agentRevisionNo);
		}

		/** FORM RESPONSE DATA **/
		Map resp = new LinkedHashMap<>();

		// Building gruoup collection object;

		resp.put(TxnEnrollmentProperties.AGENT_NAME,
				(ObjectUtil.isEmpty(agent) && ObjectUtil.isEmpty(agent.getPersonalInfo()) ? ""
						: agent.getPersonalInfo().getAgentName()));
		resp.put(TransactionProperties.DEVICE_ID, device.getCode());
		resp.put(TransactionProperties.SYNC_TIME_STAMP, sdf.format(new Date()));
		resp.put(TransactionProperties.DISPLAY_TS_FORMAT, DateUtil.TXN_DATE_TIME);
		resp.put(TransactionProperties.SERVICE_POINT_ID,
				(!ObjectUtil.isEmpty(agent) && !ObjectUtil.isEmpty(agent.getServicePoint())
						? agent.getServicePoint().getCode() : ""));
		resp.put(TransactionProperties.SERVICE_POINT_NAME,
				(!ObjectUtil.isEmpty(agent) && !ObjectUtil.isEmpty(agent.getServicePoint())
						? agent.getServicePoint().getName() : ""));
		/*
		 * resp.put(TransactionProperties.FARMER_CARD_ID_SEQ,
		 * agent.getFarmerCardIdSequence());
		 * resp.put(TransactionProperties.FARMER_ACCOUNT_NO_SEQ,
		 * agent.getFarmerAccountNoSequence());
		 * resp.put(TransactionProperties.SHOP_DEALER_CARD_ID_SEQ,
		 * agent.getShopDealerCardIdSequence());
		 * resp.put(TransactionProperties.RECEIPT_NO_SEQ,
		 * agent.getReceiptNumber());
		 * resp.put(TransactionProperties.ORDER_NUMBER_SEQ,
		 * agent.getOrderNoSeq());
		 * resp.put(TransactionProperties.DELIVERY_NUMBER_SEQ,
		 * agent.getDeliveryNoSeq());
		 * resp.put(TransactionProperties.AGENT_INTERNAL_ID,
		 * agent.getInternalIdSequence());
		 * resp.put(TransactionProperties.MSG_NO_SEQ, device.getMsgNo());
		 */
		resp.put(TransactionProperties.BALANCE,
				(ObjectUtil.isEmpty(account)) ? "" : formatter.format(account.getCashBalance()));
		/*
		 * resp.put(TransactionProperties.BALANCE, (ObjectUtil.isEmpty(account))
		 * ? "" : formatter.format(account.getBalance()) + "|" +
		 * formatter.format(account.getDistributionBalance()) + "|" +
		 * formatter.format( account.getShareAmount() +
		 * account.getSavingAmount()));
		 */
		resp.put(TxnEnrollmentProperties.CLIENT_ID_SEQ, getFarmerIdSeq(agent));
		resp.put(TransactionProperties.SHOP_DEALER_ID_SEQ, getShopDealerIdSeq(agent));
		resp.put(TransactionProperties.AGENT_TYPE,
				(ObjectUtil.isEmpty(agent) && ObjectUtil.isEmpty(agent.getAgentType()) ? ""
						: agent.getAgentType().getCode()));
		resp.put(TransactionProperties.TARE_WEIGHT, preferences.getPreferences().get(ESESystem.TARE_WEIGHT));
		String seasonCode = clientService.findCurrentSeasonCodeByBranchId(branchId);
		resp.put(TxnEnrollmentProperties.CURRENT_SEASON_CODE_LOGIN, seasonCode);
		// resp.put(TransactionProperties.COMPANY_NAMES,
		// preferences.getPreferences().get(ESESystem.COMPANY_NAMES));
		resp.put(TransactionProperties.DISPLAY_TXN_DATE_FORMAT, DateUtil.TXN_DATE_DISPLAY_FORMAT);
		resp.put(TransactionProperties.IS_GENERIC, eseSystem.getPreferences().get(ESESystem.ENABLE_MULTI_PRODUCT));
		resp.put(TransactionProperties.IS_BATCH_AVAIL, eseSystem.getPreferences().get(ESESystem.ENABLE_BATCH_NO));
		resp.put(TxnEnrollmentProperties.IS_GRAMPANCHAYAT,
				StringUtil.isEmpty(eseSystem.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT)) ? "0"
						: eseSystem.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));
		
		resp.put(TxnEnrollmentProperties.IS_FIELDSTAFF_TRACKING,
				!eseSystem.getPreferences().containsKey(ESESystem.IS_FIELDSTAFF_TRACKING)  || StringUtil.isEmpty(eseSystem.getPreferences().get(ESESystem.IS_FIELDSTAFF_TRACKING)) ? "0"
						: eseSystem.getPreferences().get(ESESystem.IS_FIELDSTAFF_TRACKING));
		
		
		//resp.put(TransactionProperties.CURRENCY, eseSystem.getPreferences().get(ESESystem.CURRENCY_TYPE));
		resp.put(TransactionProperties.CURRENCY,
				StringUtil.isEmpty(eseSystemUser.getPreferences().get(ESESystem.CURRENCY_TYPE))?
						eseSystem.getPreferences().get(ESESystem.CURRENCY_TYPE):eseSystemUser.getPreferences().get(ESESystem.CURRENCY_TYPE));
		resp.put(TransactionProperties.AREA_TYPE, eseSystem.getPreferences().get(ESESystem.AREA_TYPE));
		resp.put(TransactionProperties.WAREHOUSE_ID,
				(ObjectUtil.isEmpty(agent) || ObjectUtil.isEmpty(agent.getProcurementCenter())
						|| StringUtil.isEmpty(agent.getProcurementCenter().getCode()) ? ""
								: agent.getProcurementCenter().getCode()));

		/** FORMING SAMITHI COLLECTION **/
		Collection collection = new Collection();
		List<Object> listOfSamithiObject = new ArrayList<Object>();
		if (!ObjectUtil.isListEmpty(agent.getWareHouses())) {
			for (Warehouse samithi : agent.getWareHouses()) {
				// if (!ObjectUtil.isEmpty(samithi.getRefCooperative())) {
				Data samithiCodeData = new Data();
				samithiCodeData.setKey(TxnEnrollmentProperties.SAMITHI_CODE);
				samithiCodeData.setValue(samithi.getCode());

				List<Data> samithiData = new ArrayList<Data>();
				samithiData.add(samithiCodeData);

				Object samithiObject = new Object();
				samithiObject.setData(samithiData);

				listOfSamithiObject.add(samithiObject);
				// }
			}
			collection.setObject(listOfSamithiObject);
		}
		resp.put(TxnEnrollmentProperties.SAMITHI_LIST, collection);

		/*
		 * if (tenantId.equals(ESESystem.CANDA_TENANT_ID)) { // Building
		 * Response Number Long farmerRevisionNo = null; Long farmRevisionNo =
		 * null; Long farmCropsRevisionNo = null;
		 * 
		 * resp.put(TxnEnrollmentProperties.FARMER_DOWNLOAD_REVISION_NO,
		 * farmerRevisionNo);
		 * resp.put(TxnEnrollmentProperties.FARM_DOWNLOAD_REVISION_NO,
		 * farmRevisionNo);
		 * resp.put(TxnEnrollmentProperties.FARM_CROPS_DOWNLOAD_REVISION_NO,
		 * farmCropsRevisionNo); } else { // Building Response Number Long
		 * farmerRevisionNo = null; Long farmRevisionNo = null; Long
		 * farmCropsRevisionNo = null; if (!ObjectUtil.isEmpty(agent) &&
		 * !ObjectUtil.isListEmpty(agent.getWareHouses())) farmerRevisionNo =
		 * farmerService.
		 * findActiveContractFarmersLatestRevisionNoByAgentAndSeason(
		 * agent.getId(),
		 * preferences.getPreferences().get(ESESystem.CURRENT_SEASON_CODE));
		 * 
		 * farmRevisionNo =
		 * farmerService.listFarmFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(
		 * agent.getId());
		 * 
		 * farmCropsRevisionNo = farmerService
		 * .listFarmCropsFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(agent.
		 * getId());
		 * 
		 * resp.put(TxnEnrollmentProperties.FARMER_DOWNLOAD_REVISION_NO,
		 * farmerRevisionNo);
		 * resp.put(TxnEnrollmentProperties.FARM_DOWNLOAD_REVISION_NO,
		 * farmRevisionNo);
		 * resp.put(TxnEnrollmentProperties.FARM_CROPS_DOWNLOAD_REVISION_NO,
		 * farmCropsRevisionNo); }
		 */

		Long farmerRevisionNo = 0l;
		Long farmRevisionNo = 0l;
		Long farmCropsRevisionNo = 0l;
		farmerRevisionNo = farmerService.findActiveContractFarmersLatestRevisionNoByAgentAndSeason(agent.getId(),
				seasonCode);
  	farmRevisionNo = farmerService.listFarmFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(agent.getId());

		farmCropsRevisionNo = farmerService
				.listFarmCropsFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(agent.getId());


		resp.put(TxnEnrollmentProperties.FARMER_DOWNLOAD_REVISION_NO, farmerRevisionNo);
		resp.put(TxnEnrollmentProperties.FARM_DOWNLOAD_REVISION_NO, farmRevisionNo);
		resp.put(TxnEnrollmentProperties.FARM_CROPS_DOWNLOAD_REVISION_NO, farmCropsRevisionNo);
		resp.put(TxnEnrollmentProperties.CUSTOMER_PROJECT_REVISION_NO, clientService.findCustomerLatestRevisionNo());
		List<String> lognTxns = new ArrayList<>();

		resp.put(TxnEnrollmentProperties.PRODUCTS_DOWNLOAD_REVISION_NO, "0");

		resp.put(TxnEnrollmentProperties.SEASON_DOWNLOAD_REVISION_NO, "0");

		resp.put(TxnEnrollmentProperties.LOCATION_DOWNLOAD_REVISION_NO, "0");

		resp.put(TransactionProperties.WAREHOUSE_PRODUCT_STOCK_DOWNLOAD_REVISION_NO, "0");

		resp.put(TxnEnrollmentProperties.VILLAGE_WAREHOUSE_STOCK_DOWNLOAD_REVISION_NO, "0");

		resp.put(TxnEnrollmentProperties.FARMER_OUTSTANDING_BALANCE_DOWNLOAD_REVISION_NO, "0");

		resp.put(TxnEnrollmentProperties.PROCUREMENT_PRODUCT_DOWNLOAD_REVISION_NO, "0");

		resp.put(TxnEnrollmentProperties.BUYER_DOWNLOAD_REVISION_NO, "0");

		resp.put(TxnEnrollmentProperties.CATALOGUE_DOWNLOAD_REVISION_NO, "0");

		resp.put(TxnEnrollmentProperties.COOPERATIVE_DOWNLOAD_REVISION_NO, "0");

		resp.put(TransactionProperties.SUPPLIER_DOWNLOAD_REVISION_NO, "0");

		resp.put(TxnEnrollmentProperties.DYNAMIC_COMPONENT_DOWNLOAD_REVISION_NO, "0");

		resp.put(ITxnErrorCodes.EMPTY_FARMER_OUTSTANDING_BALANCE_REVISION_NO, "0");

		resp.put(TxnEnrollmentProperties.TRAINING_CRITERIA_CATEGORY_DOWNLOAD_REVISION_NO,
				trainingService.findTopicCategoryLatestRevisionNo());
		resp.put(TxnEnrollmentProperties.TRAINING_CRITERIA_DOWNLOAD_REVISION_NO,
				trainingService.findTopicLatestRevisionNo());
		resp.put(TxnEnrollmentProperties.PLANNER_DOWNLOAD_REVISION_NO,
				trainingService.findFarmerTrainingLatestRevisionNo());

		if (agent.getRevisionNumber() > aRevNo) {
			reqData.put(TxnEnrollmentProperties.FARMER_OUTSTANDING_BALANCE_DOWNLOAD_REVISION_NO, "0");
			reqData.put(TxnEnrollmentProperties.VILLAGE_WAREHOUSE_STOCK_DOWNLOAD_REVISION_NO, "0");
		}

		 resp.put(TxnEnrollmentProperties.AGENT_REVISION_NO,agent.getRevisionNumber());
		resp.put(TxnEnrollmentProperties.INTERESRT_RATE_APPLICAPLE,
				eseSystem.getPreferences().get(ESESystem.INTEREST_MODULE));
		if (eseSystem.getPreferences().get(ESESystem.INTEREST_MODULE).equals("1")) {
			resp.put(TxnEnrollmentProperties.RATE_OF_INTEREST,
					eseSystem.getPreferences().get(ESESystem.RATE_OF_INTEREST));
			resp.put(TxnEnrollmentProperties.EFFECTIVE_FROM,
					DateUtil.convertDateFormat(eseSystem.getPreferences().get(ESESystem.ROI_EFFECTIVE_FROM),
							DateUtil.DATE_FORMAT, DateUtil.DATABASE_DATE_FORMAT));
			resp.put(TxnEnrollmentProperties.IS_APPLICAPLE_EX_FARMER,
					eseSystem.getPreferences().get(ESESystem.ROI_EFFECT_EXISTING_FARMER));
			resp.put(TxnEnrollmentProperties.PREVIOUS_INTEREST_RATE,
					ObjectUtil.isEmpty(preferncesService.findLastInactiveInterestRateHistory()) ? 0
							: preferncesService.findLastInactiveInterestRateHistory().getRateOfInterest());
		} else {
			resp.put(TxnEnrollmentProperties.RATE_OF_INTEREST, "");
			resp.put(TxnEnrollmentProperties.EFFECTIVE_FROM, "");
			resp.put(TxnEnrollmentProperties.IS_APPLICAPLE_EX_FARMER, "");
			resp.put(TxnEnrollmentProperties.PREVIOUS_INTEREST_RATE, "");
		}
		resp.put(TxnEnrollmentProperties.IS_QR_CODE_SEARCH_REQUIRED,
				eseSystem.getPreferences().get(ESESystem.IS_QR_CODE_SEARCH_REQUIRED));
		resp.put(TransactionProperties.BRANCH_ID, branchId);
		
	    resp.put(TransactionProperties.PARENT_BRANCH_ID, branchMaster.getParentBranch());
		
		

		if (MOBILE_VERSION_DETAILS
				.equalsIgnoreCase((String) reqData.get(TxnEnrollmentProperties.AGRO_VERSION_DETAILS))) {

			List<java.lang.Object[]> agroDetail = preferncesService.findAgroPrefernceDetailById(ESESystem.SYSTEM_ESE);

			for (java.lang.Object[] obj : agroDetail) {
				if (obj[1].equals(REMOTE_APK_VERSION)) {
					resp.put(TxnEnrollmentProperties.REMOTE_APK_VERSION, String.valueOf(obj[2]));
				}
				if (obj[1].equals(REMOTE_CONFIG_VERSION)) {
					resp.put(TxnEnrollmentProperties.REMOTE_CONFIG_VERSION, String.valueOf(obj[2]));
				}
				if (obj[1].equals(REMOTE_DB_VERSION)) {
					resp.put(TxnEnrollmentProperties.REMOTE_DB_VERSION, String.valueOf(obj[2]));
				}
			}

		} else {
			resp.put(TxnEnrollmentProperties.REMOTE_APK_VERSION,
					eseSystem.getPreferences().get(ESESystem.REMOTE_APK_VERSION));

			resp.put(TxnEnrollmentProperties.REMOTE_CONFIG_VERSION,
					eseSystem.getPreferences().get(ESESystem.REMOTE_CONFIG_VERSION));
			resp.put(TxnEnrollmentProperties.REMOTE_DB_VERSION,
					eseSystem.getPreferences().get(ESESystem.REMOTE_DB_VERSION));
		}
		resp.put(TxnEnrollmentProperties.AREA_CAPTURE_MODE,
				StringUtil.isEmpty(eseSystemUser.getPreferences().get(ESESystem.AREA_CAPTURE_MODE)) ? ""
						: eseSystemUser.getPreferences().get(ESESystem.AREA_CAPTURE_MODE));

		resp.put(TxnEnrollmentProperties.GEO_FENCING_FLAG,
				StringUtil.isEmpty(eseSystemUser.getPreferences().get(ESESystem.GEO_FENCING_FLAG)) ? ""
						: eseSystemUser.getPreferences().get(ESESystem.GEO_FENCING_FLAG));
		resp.put(TxnEnrollmentProperties.GEO_FENCING_RADIUS_MT,
				StringUtil.isEmpty(eseSystemUser.getPreferences().get(ESESystem.GEO_FENCING_RADIUS_MT)) ? ""
						: eseSystemUser.getPreferences().get(ESESystem.GEO_FENCING_RADIUS_MT));

		resp.put(TransactionProperties.FTP_PASSWORD, eseSystem.getPreferences().get(ESESystem.FTP_PASSWORD));

		resp.put(TransactionProperties.FTP_URL, eseSystem.getPreferences().get(ESESystem.FTP_URL));
		resp.put(TransactionProperties.FTP_USERNAME, eseSystem.getPreferences().get(ESESystem.FTP_USERNAME));

		resp.put(TransactionProperties.FTP_VIDEO_PATH, eseSystem.getPreferences().get(ESESystem.FTP_VIDEO_PATH));
		
		resp.put(TransactionProperties.IS_BUYER, eseSystem.getPreferences().get(ESESystem.ENABLE_BUYER));
		resp.put(TransactionProperties.DISTRIBUTION_IMAGE_AVILABLE,eseSystem.getPreferences().get(ESESystem.ENABLE_DISTRIBUTION_IMAGE));
		resp.put(TransactionProperties.DIGITAL_SIGNATURE,eseSystem.getPreferences().get(ESESystem.ENABLE_DIGITAL_SIGNATURE));
		resp.put(TransactionProperties.CROP_CALENDAR,eseSystem.getPreferences().get(ESESystem.ENABLE_CROP_CALENDAR));
		resp.put(TransactionProperties.WAREHOUSEDOWNLOAD_SEASON,eseSystem.getPreferences().get(ESESystem.ENABLE_WAREHOUSEDOWNLOAD_SEASON));
		Map groupResponse = new LinkedHashMap<>();
		groupResponse.put(TxnEnrollmentProperties.AGENT_LOGIN_RESP_KEY, buildCollection(resp));
		//groupResponse.put("dummy",new Collection());
		if(branchId!=null){
			head.setBranchId(branchId);
			 reqData.put(TransactionProperties.HEAD,head);
		}
		
		 groupDownloadTransactionsResponses(reqData, groupResponse);
		
		/** UPDATE AGENT EOD STATUS **/
		agentService.updateAgentBODStatus(agent.getProfileId(), ESETxn.BOD);
		/** update device info **/
		String androidVer = (String) reqData.get(TxnEnrollmentProperties.ANDROID_VERSION);
        String mobileModel = (String) reqData.get(TxnEnrollmentProperties.MOBILE_MODEL);
        device.setAndroidVer(androidVer);
        device.setMobileModel(mobileModel);
        deviceService.updateDevice(device);
		return groupResponse;
	}

	@SuppressWarnings("unchecked")
	private void groupDownloadTransactionsResponses(Map req, Map res) {

		if (!ObjectUtil.isEmpty(txnMap)) {
			for (Map.Entry txnAdapterEntry : txnMap.entrySet()) {
				ITxnAdapter txnAdapter = (ITxnAdapter) txnAdapterEntry.getValue();
				if (!ObjectUtil.isEmpty(txnAdapter)) {
					Map txnAdapterResp = txnAdapter.process(req);
					res.put(txnAdapterEntry.getKey(), buildCollection(txnAdapterResp));
				}
			}
		}
	}
	/**
	 * Builds the collection.
	 * 
	 * @param res
	 *            the res
	 * @return the collection
	 */
	@SuppressWarnings("unchecked")
	private Collection buildCollection(Map res) {
		if(res.isEmpty()){
			return new Collection();
		}else{

		Collection respCollection = new Collection();
		Object respObject = new Object();
		List<Object> respObjectList = new ArrayList<Object>();
		respObject.setData(CollectionUtil.mapToList(res));
		respObjectList.add(respObject);
		respCollection.setObject(respObjectList);
		return respCollection;
		}
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
	 * Gets the farmer id seq.
	 * 
	 * @param agent
	 *            the agent
	 * @return the farmer id seq
	 */
	private String getFarmerIdSeq(Agent agent) {

		String farmerIdCurrIdSeq = "";
		String farmerAllotResIdSeq = "";
		String farmerAllotIdLimit = "";
		long farmerMaxRange = Farmer.FARMER_ID_MAX_RANGE;
		String farmerSeqLength = preferncesService.findPrefernceByName(ESESystem.FARMER_ID_LENGTH);
		String farmerMaxRanges = preferncesService.findPrefernceByName(ESESystem.FARMER_MAX_RANGE);
		if (farmerMaxRanges != null && !farmerMaxRanges.isEmpty()) {

			farmerMaxRange = Long.valueOf(farmerMaxRanges);
		}

		if (!ObjectUtil.isEmpty(agent)) {

			long famerCurrSeqId = StringUtil.isEmpty(agent.getFarmerCurrentIdSeq()) ? 0
					: Long.parseLong(agent.getFarmerCurrentIdSeq());
			long famerAllotSeqId = StringUtil.isEmpty(agent.getFarmerAllotedIdSeq()) ? 0
					: Long.parseLong(agent.getFarmerAllotedIdSeq());
			long maxIndex = famerAllotSeqId + Farmer.FARMER_SEQ_RANGE;

		
				if ((famerCurrSeqId == 0)
						|| (famerCurrSeqId == maxIndex && StringUtil.isEmpty(agent.getFarmerAllotedResIdSeq()))) {

					if (farmerSeqLength == null || farmerSeqLength.isEmpty()) {
						farmerIdCurrIdSeq = idGenerator.getFarmerRemoteIdSeq();

					} else {
						farmerIdCurrIdSeq = idGenerator.getFarmerRemoteIdSeq(Integer.valueOf(farmerSeqLength),
								farmerMaxRange);
					}

					agent.setFarmerCurrentIdSeq(farmerIdCurrIdSeq);
					agent.setFarmerAllotedIdSeq(farmerIdCurrIdSeq);
					agent.setFarmerAllotedResIdSeq(null);
				} else {
					long reserveIndex = maxIndex - Farmer.FARMER_RESERVE_INDEX;

					if (famerCurrSeqId > reserveIndex && StringUtil.isEmpty(agent.getFarmerAllotedResIdSeq())) {
						if (farmerSeqLength == null || farmerSeqLength.isEmpty()) {
							String newIdSeq = idGenerator.getFarmerRemoteIdSeq();
							agent.setFarmerAllotedResIdSeq(newIdSeq);

						} else {
							String newIdSeq = idGenerator.getFarmerRemoteIdSeq(Integer.valueOf(farmerSeqLength),
									farmerMaxRange);
							agent.setFarmerAllotedResIdSeq(newIdSeq);

						}

					} else {

						if (!StringUtil.isEmpty(agent.getFarmerAllotedResIdSeq())) {
							long reserveMaxIndex = Long.parseLong(agent.getFarmerAllotedResIdSeq())
									+ Farmer.FARMER_SEQ_RANGE;
							if (famerCurrSeqId == reserveMaxIndex) {
								if (farmerSeqLength == null || farmerSeqLength.isEmpty()) {
									farmerIdCurrIdSeq = idGenerator.getFarmerRemoteIdSeq();

								} else {
									farmerIdCurrIdSeq = idGenerator
											.getFarmerRemoteIdSeq(Integer.valueOf(farmerSeqLength), farmerMaxRange);
								}
								agent.setFarmerCurrentIdSeq(farmerIdCurrIdSeq);
								agent.setFarmerAllotedIdSeq(farmerIdCurrIdSeq);
								agent.setFarmerAllotedResIdSeq(null);
							} else if (famerCurrSeqId > maxIndex) {
								agent.setFarmerCurrentIdSeq(String.valueOf(famerCurrSeqId));
								agent.setFarmerAllotedIdSeq(agent.getFarmerAllotedResIdSeq());
								agent.setFarmerAllotedResIdSeq(null);
							}
						}
					}
				}
			farmerIdCurrIdSeq = StringUtil.isEmpty(agent.getFarmerCurrentIdSeq()) ? "0" : agent.getFarmerCurrentIdSeq();
			farmerAllotResIdSeq = StringUtil.isEmpty(agent.getFarmerAllotedResIdSeq()) ? "0"
					: agent.getFarmerAllotedResIdSeq();

			long allotSeqId = StringUtil.isEmpty(agent.getFarmerAllotedIdSeq()) ? 0
					: Long.parseLong(agent.getFarmerAllotedIdSeq());
			if (allotSeqId > 0) {
				long allotIdSeqLimit = allotSeqId + Farmer.FARMER_SEQ_RANGE;
				if (allotIdSeqLimit >= farmerMaxRange) {
					allotIdSeqLimit = farmerMaxRange;
				}
				farmerAllotIdLimit = String.valueOf(allotIdSeqLimit);
			}

		}
		agentService.editAgent(agent);
		return farmerIdCurrIdSeq + "|" + farmerAllotResIdSeq + "|" + farmerAllotIdLimit;
	}

	/**
	 * Gets the shop dealer id seq.
	 * 
	 * @param agent
	 *            the agent
	 * @return the shop dealer id seq
	 */
	private String getShopDealerIdSeq(Agent agent) {

		String shopDealerIdCurrIdSeq = "";
		String shopDealerAllotResIdSeq = "";
		String shopDealerAllotIdLimit = "";

		if (!ObjectUtil.isEmpty(agent)) {

			int shopDealerCurrSeqId = StringUtil.isEmpty(agent.getShopDealerCurrentIdSeq()) ? 0
					: Integer.parseInt(agent.getShopDealerCurrentIdSeq());
			int shopDealerAllotSeqId = StringUtil.isEmpty(agent.getShopDealerAllotedIdSeq()) ? 0
					: Integer.parseInt(agent.getShopDealerAllotedIdSeq());
			int maxIndex = shopDealerAllotSeqId + ShopDealer.SHOP_DEALER_SEQ_RANGE;

			if ((shopDealerCurrSeqId == 0)
					|| (shopDealerCurrSeqId == maxIndex && StringUtil.isEmpty(agent.getShopDealerAllotedResIdSeq()))) {
				shopDealerIdCurrIdSeq = idGenerator.getShopDealerRemoteIdSeq();
				agent.setShopDealerCurrentIdSeq(shopDealerIdCurrIdSeq);
				agent.setShopDealerAllotedIdSeq(shopDealerIdCurrIdSeq);
				agent.setShopDealerAllotedResIdSeq(null);
			} else {
				int reserveIndex = maxIndex - ShopDealer.SHOP_DEALER_RESERVE_INDEX;
				if (shopDealerCurrSeqId > reserveIndex && StringUtil.isEmpty(agent.getShopDealerAllotedResIdSeq())) {
					String newIdSeq = idGenerator.getShopDealerRemoteIdSeq();
					agent.setShopDealerAllotedResIdSeq(newIdSeq);
				} else {
					if (shopDealerCurrSeqId >= maxIndex && !StringUtil.isEmpty(agent.getShopDealerAllotedResIdSeq())) {
						agent.setShopDealerAllotedIdSeq(agent.getShopDealerAllotedResIdSeq());
						agent.setShopDealerCurrentIdSeq(agent.getShopDealerAllotedResIdSeq());
						agent.setShopDealerAllotedResIdSeq(null);
					}
				}
			}
			shopDealerIdCurrIdSeq = StringUtil.isEmpty(agent.getShopDealerCurrentIdSeq()) ? "0"
					: agent.getShopDealerCurrentIdSeq();
			shopDealerAllotResIdSeq = StringUtil.isEmpty(agent.getShopDealerAllotedResIdSeq()) ? "0"
					: agent.getShopDealerAllotedResIdSeq();

			int allotSeqId = StringUtil.isEmpty(agent.getShopDealerAllotedIdSeq()) ? 0
					: Integer.parseInt(agent.getShopDealerAllotedIdSeq());
			if (allotSeqId > 0) {
				int allotIdSeqLimit = allotSeqId + ShopDealer.SHOP_DEALER_SEQ_RANGE;
				if (allotIdSeqLimit >= ShopDealer.SHOP_DEALER_ID_MAX_RANGE) {
					allotIdSeqLimit = ShopDealer.SHOP_DEALER_ID_MAX_RANGE;
				}
				shopDealerAllotIdLimit = String.valueOf(allotIdSeqLimit);
			}
		}
		return shopDealerIdCurrIdSeq + "|" + shopDealerAllotResIdSeq + "|" + shopDealerAllotIdLimit;
	}

	/**
	 * Gets the txn map.
	 * 
	 * @return the txn map
	 */
	public Map<String, ITxnAdapter> getTxnMap() {

		return txnMap;
	}

	/**
	 * Sets the txn map.
	 * 
	 * @param txnMap
	 *            the txn map
	 */
	public void setTxnMap(Map<String, ITxnAdapter> txnMap) {

		this.txnMap = txnMap;
	}
}
