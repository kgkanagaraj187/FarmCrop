/*
 * FarmerDownload.java
 * Copyright (c) 2014-2015, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.adapter.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.property.TransactionProperties;
import com.sourcetrace.eses.property.TxnEnrollmentProperties;
import com.sourcetrace.eses.service.IAccountService;
import com.sourcetrace.eses.service.IAgentService;
import com.sourcetrace.eses.service.ICardService;
import com.sourcetrace.eses.service.IClientService;
import com.sourcetrace.eses.service.IFarmCropsService;
import com.sourcetrace.eses.service.IFarmerService;
import com.sourcetrace.eses.service.IPreferencesService;
import com.sourcetrace.eses.service.IProductDistributionService;
import com.sourcetrace.eses.service.IServicePointService;
import com.sourcetrace.eses.txn.adapter.ITxnAdapter;
import com.sourcetrace.eses.txn.exception.SwitchException;
import com.sourcetrace.eses.txn.schema.Collection;
import com.sourcetrace.eses.txn.schema.Data;
import com.sourcetrace.eses.txn.schema.Head;
import com.sourcetrace.eses.txn.schema.Object;
import com.sourcetrace.eses.txn.schema.Request;
import com.sourcetrace.eses.util.CollectionUtil;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.profile.Calf;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sun.istack.ByteArrayDataSource;

@Component
public class FarmerDownload implements ITxnAdapter {

	private static final Logger LOGGER = Logger.getLogger(FarmerDownload.class.getName());
	private static final String FARMER_DOB = "yyyyMMdd";
	@Autowired
	private IFarmerService farmerService;
	@Autowired
	private IFarmCropsService farmCropsService;
	@Autowired
	private IServicePointService servicePointService;
	@Autowired
	private ICardService cardService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IProductDistributionService productDistributionService;
	@Autowired
	private IPreferencesService preferncesService;
	@Autowired
	private IAgentService agentService;
	 @Autowired
	private IClientService clientService;
	private String tenantId;

	private static final String IMAGE_CONTENT_TYPE = "image/jpeg";
	private static final String FINGER_PRINT_EXIST = "1";
	private static final String FINGER_PRINT_NOT_EXIST = "0";

	SimpleDateFormat sdf = new SimpleDateFormat(FARMER_DOB);
	SimpleDateFormat sdf1 = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
	SimpleDateFormat sdfTxnDate = new SimpleDateFormat(DateUtil.DATABASE_DATE_FORMAT);

	List<java.lang.Object[]> farmICSConversionList = new ArrayList<>();
	Map<Long, List<java.lang.Object[]>> farmObject = new HashMap<>();
	Map<Long, List<java.lang.Object[]>> farmCropsObject = new HashMap<>();
	protected Map<String, List<String>> menuMap = new LinkedHashMap<>();
	protected Map<String, List<String>> dynamicCount = new LinkedHashMap<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.txn.adapter.ITxnAdapter#process(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> process(Map<?, ?> reqData) {
		Map resp = new LinkedHashMap();
		/** REQUEST DATA **/
		Head head = (Head) reqData.get(TransactionProperties.HEAD);
		String agentId = head.getAgentId();
		setTenantId(head.getTenantId());
		if (StringUtil.isEmpty(agentId))
			throw new SwitchException(ITxnErrorCodes.AGENT_ID_EMPTY);

		Agent agent = agentService.findAgentByProfileId(agentId);
		if (ObjectUtil.isEmpty(agent))
			throw new SwitchException(ITxnErrorCodes.INVALID_AGENT);

		String revisionNo = (String) reqData.get(TxnEnrollmentProperties.FARMER_DOWNLOAD_REVISION_NO);
		if (StringUtil.isEmpty(revisionNo))
			throw new SwitchException(ITxnErrorCodes.EMPTY_FARMER_REVISION_NO);

		LOGGER.info("REVISION NO" + revisionNo);
		List<java.lang.Object[]> farmerList = new ArrayList<java.lang.Object[]>();

		Collection farmerCollection = new Collection();
		/*
		 * Collection farmCollection = new Collection(); Collection
		 * farmCropsCollection = new Collection();
		 */
		List<Object> farmerObjects = new ArrayList<Object>();
		/*
		 * List<Object> farmObjects = new ArrayList<Object>(); List<Object>
		 * farmCropsObjects = new ArrayList<Object>();
		 */

		ESESystem preferencesOne = preferncesService.findPrefernceById("1");
		Integer gmEnable = 0;
		if (!StringUtil.isEmpty(preferencesOne))
			gmEnable = Integer.valueOf(preferencesOne.getPreferences().get(ESESystem.ENABLE_GRAMPANJAYAT));

		/** FORMING FARMER OBJECT **/
		if (!tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
				|| (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)
						&& agent.getAgentType().getCode().equals("02"))) {
			if (!ObjectUtil.isListEmpty(agent.getWareHouses())) {
				/*
				 * No need of current season,We are not downloading based on
				 * season
				 */
				List<String> branch = new ArrayList<>();
				branch.add(head.getBranchId());
				/* if (gmEnable == 1) */

				farmerList = farmerService.listActiveContractFarmersFieldsBySeasonRevNoAndSamithiWithGramp(
						agent.getId(), "", Long.valueOf(revisionNo), branch);
				if (tenantId.equalsIgnoreCase(ESESystem.SYMRISE)) {

					List<Long> fidLi = farmerList.stream().map(u -> Long.valueOf(u[0].toString()))
							.collect(Collectors.toList());
					if (fidLi != null && !fidLi.isEmpty()) {
						List<java.lang.Object[]> listTemp = farmerService.listfarmerDynamicData(fidLi);
						for (java.lang.Object[] lp : listTemp) {
							List<String> obj = new ArrayList();
							obj.add(String.valueOf(lp[1]));
							obj.add(String.valueOf(lp[2]));
							menuMap.put(lp[0].toString(), obj);
						}
					}
					// return menuMap;

				}

				if (tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
					String seasonCode= clientService.findCurrentSeasonCodeByBranchId(head.getBranchId());
					List<Long> fidLi = farmerList.stream().map(u -> Long.valueOf(u[0].toString()))
							.collect(Collectors.toList());
					if (fidLi != null && !fidLi.isEmpty()) {
						List<java.lang.Object[]> listTemp = farmerService.findCountOfDynamicDataByFarmerId(fidLi,seasonCode);
						for (java.lang.Object[] lp : listTemp) {
							List<String> obj = new ArrayList();
							obj.add(String.valueOf(lp[1]));
							//obj.add(String.valueOf(lp[2]));
							dynamicCount.put(lp[0].toString(), obj);
						}
					}
					// return menuMap;

				}

				/*
				 * else farmerList = farmerService.
				 * listActiveContractFarmersFieldsBySeasonRevNoAndSamithi(agent.
				 * getId(), "", Long.valueOf(revisionNo), branch);
				 */
				if (!ObjectUtil.isListEmpty(farmerList)) {

					for (java.lang.Object[] farmer : farmerList) {

						Map objectMap = formResponseFarmer(farmer);
						Object farmerObject = new Object();
						farmerObject.setData(CollectionUtil.mapToList(objectMap));

						farmerObjects.add(farmerObject);

					}

				}

			}
			if (!ObjectUtil.isListEmpty(farmerList)) {
				resp.put(TxnEnrollmentProperties.REVISION_NO, farmerList.get(0)[12].toString());
			} else {
				resp.put(TxnEnrollmentProperties.REVISION_NO, revisionNo);
			}
		}
		if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) && agent.getAgentType().getCode().equals("05")) {
			List<Agent> agents = agentService.listAgentByWarehouseAndRevisionNo(agent.getProcurementCenter().getId(),
					revisionNo);
			List<Agent> fieldStaffs = new ArrayList<>();
			agents.stream().filter(f -> f.getAgentType().getCode().equals("02")
					&& f.getBranchId().equalsIgnoreCase(head.getBranchId())).forEach(fs -> {
						fieldStaffs.add(fs);
					});
			if (!ObjectUtil.isEmpty(fieldStaffs)) {
				for (Agent agnt : fieldStaffs) {
					Map objectMap = formResponseFieldStaff(agnt);
					Object agentObject = new Object();
					agentObject.setData(CollectionUtil.mapToList(objectMap));

					farmerObjects.add(agentObject);

				}
			}
			Agent lastValue = fieldStaffs.stream().reduce((a, b) -> b).orElse(null);
			if (!ObjectUtil.isEmpty(lastValue)) {

				resp.put(TxnEnrollmentProperties.REVISION_NO, lastValue.getRevisionNumber());
			} else {
				resp.put(TxnEnrollmentProperties.REVISION_NO, revisionNo);
			}
		}
		if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID) && agent.getAgentType().getCode().equals("06")) {
			List<Agent> agents = agentService.listAgentByRevisionNo(head.getBranchId(), Long.parseLong(revisionNo));
			List<Agent> fieldStaffs = new ArrayList<>();
			agents.stream().filter(f -> f.getAgentType().getCode().equals("02")
					|| f.getAgentType().getCode().equals("05") && f.getRevisionNumber() > Long.parseLong(revisionNo))
					.forEach(fs -> {
						fieldStaffs.add(fs);
					});
			if (!ObjectUtil.isEmpty(fieldStaffs)) {
				for (Agent agnt : fieldStaffs) {
					Map objectMap = formResponseFieldStaff(agnt);
					Object agentObject = new Object();
					agentObject.setData(CollectionUtil.mapToList(objectMap));

					farmerObjects.add(agentObject);

				}
			}
			Agent lastValue = fieldStaffs.stream().reduce((a, b) -> b).orElse(null);
			if (!ObjectUtil.isEmpty(lastValue)) {

				resp.put(TxnEnrollmentProperties.REVISION_NO, lastValue.getRevisionNumber());
			} else {
				resp.put(TxnEnrollmentProperties.REVISION_NO, revisionNo);
			}
		}
		farmerCollection.setObject(farmerObjects);

		resp.put(TxnEnrollmentProperties.FARMER_LIST, farmerCollection);

		return resp;
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

	private DataHandler getBase64EncodedImageFromByteArray(byte[] image) {

		return new DataHandler(
				new ByteArrayDataSource((ObjectUtil.isEmpty(image) ? new byte[0] : image), IMAGE_CONTENT_TYPE));
	}

	@SuppressWarnings("unchecked")
	public Map formResponseFieldStaff(Agent obj) {
		Map objectMap = new LinkedHashMap();
		objectMap.put(TxnEnrollmentProperties.FARMER_ID, (obj.getProfileId()));
		objectMap.put(TxnEnrollmentProperties.FIRST_NAME,
				(obj.getPersonalInfo().getFirstName() != null
						&& !StringUtil.isEmpty(obj.getPersonalInfo().getFirstName())
								? obj.getPersonalInfo().getFirstName() : ""));
		objectMap.put(TxnEnrollmentProperties.LAST_NAME,
				(obj.getPersonalInfo().getLastName() != null && !StringUtil.isEmpty(obj.getPersonalInfo().getLastName())
						? obj.getPersonalInfo().getLastName() : ""));
		objectMap.put(TxnEnrollmentProperties.FARMER_CODE, obj.getAgentType().getCode());
		objectMap.put(TxnEnrollmentProperties.STATUS, "1");
		return objectMap;
	}

	@SuppressWarnings("unchecked")
	public Map formResponseFarmer(java.lang.Object[] object) {

		Map objectMap = new LinkedHashMap();
		java.lang.Object[] farmer = (java.lang.Object[]) object;
		objectMap.put(TxnEnrollmentProperties.FARMER_ID, (farmer[1].toString()));
		objectMap.put("farmerIdd", (farmer[0].toString()));

		objectMap.put("farms", (farmer[13].toString()));
		/*
		 * if (tenantId.equalsIgnoreCase(ESESystem.PRATIBHA_TENANT_ID)) {
		 * objectMap.put(TxnEnrollmentProperties.FARMER_CODE,
		 * (farmer[15].toString())); } else {
		 */
		objectMap.put(TxnEnrollmentProperties.FARMER_CODE,
				(farmer[2] != null && !StringUtil.isEmpty(farmer[2]) ? farmer[2].toString() : ""));
		// }
		objectMap.put(TxnEnrollmentProperties.FIRST_NAME,
				(farmer[3] != null && !StringUtil.isEmpty(farmer[3]) ? farmer[3].toString() : ""));
		objectMap.put(TxnEnrollmentProperties.LAST_NAME,
				(farmer[4] != null && !StringUtil.isEmpty(farmer[4]) ? farmer[4].toString() : ""));
		objectMap.put(TxnEnrollmentProperties.SUR_NAME,
				(farmer[26] != null && !StringUtil.isEmpty(farmer[26]) ? farmer[26].toString() : ""));

		/* if (gmEnable == 1) { */
		objectMap.put(TransactionProperties.STATE_CODE,
				(farmer[25] != null && !StringUtil.isEmpty(farmer[25]) ? farmer[25].toString() : ""));
		objectMap.put(TransactionProperties.DISTRICT_CODE,
				(farmer[24] != null && !StringUtil.isEmpty(farmer[24]) ? farmer[24].toString() : ""));
		objectMap.put(TransactionProperties.CITY_CODE,
				(farmer[23] != null && !StringUtil.isEmpty(farmer[23]) ? farmer[23].toString() : ""));
		objectMap.put(TransactionProperties.PANCHAYAT_CODE,
				(farmer[22] != null && !StringUtil.isEmpty(farmer[22]) ? farmer[22].toString() : ""));
		/*
		 * } else { objectMap.put(TransactionProperties.STATE_CODE, (farmer[24]
		 * != null && !StringUtil.isEmpty(farmer[24])
		 * ?farmer[24].toString():""));
		 * objectMap.put(TransactionProperties.DISTRICT_CODE, (farmer[23] !=
		 * null && !StringUtil.isEmpty(farmer[23]) ?farmer[23].toString():""));
		 * objectMap.put(TransactionProperties.CITY_CODE, (farmer[22] != null &&
		 * !StringUtil.isEmpty(farmer[22]) ?farmer[22].toString():""));
		 * objectMap.put(TransactionProperties.PANCHAYAT_CODE, ""); }
		 */
		objectMap.put(TxnEnrollmentProperties.VILLAGE_CODE,
				(farmer[5] != null && !StringUtil.isEmpty(farmer[5]) ? farmer[5].toString() : ""));
		objectMap.put(TxnEnrollmentProperties.SAMITHI_CODE,
				(farmer[6] != null && !StringUtil.isEmpty(farmer[6]) ? farmer[6].toString() : ""));
		objectMap.put(TxnEnrollmentProperties.FARMER_CERTIFIED,
				(farmer[7] != null && !StringUtil.isEmpty(farmer[7]) ? farmer[7].toString() : ""));
		objectMap.put(TxnEnrollmentProperties.FARMER_CERTIFICATION_TYPE,
				(farmer[8] != null && !StringUtil.isEmpty(farmer[8]) ? farmer[8].toString() : ""));
		objectMap.put(TxnEnrollmentProperties.UTZ_STATUS,
				(farmer[11] != null && !StringUtil.isEmpty(farmer[11]) ? farmer[11].toString() : ""));
		objectMap.put(TxnEnrollmentProperties.HHID,
				(farmer[2] != null && !StringUtil.isEmpty(farmer[2]) ? farmer[2].toString() : ""));
		objectMap.put(TxnEnrollmentProperties.FARMER_CODE_BYTRACENET,
				(farmer[9] != null && !StringUtil.isEmpty(farmer[9]) ? farmer[9].toString() : ""));
		objectMap.put(TxnEnrollmentProperties.CO_OPERATIVE,
				(farmer[10] != null && !StringUtil.isEmpty(farmer[10]) ? farmer[10].toString() : ""));
		objectMap.put(TxnEnrollmentProperties.CURRENT_SEASON_CODE,
				farmer[17] != null && !StringUtil.isEmpty(farmer[17]) ? farmer[17].toString() : "");
		objectMap.put(TxnEnrollmentProperties.STATUS,
				(farmer[18] != null && !StringUtil.isEmpty(farmer[18]) ? farmer[18].toString() : ""));
		objectMap.put(TxnEnrollmentProperties.FARMER_ICS_CODE,
				farmer[19] != null && !StringUtil.isEmpty(farmer[19]) ? farmer[19].toString() : "");
		objectMap.put(TxnEnrollmentProperties.RIFAN_ID,
				farmer[20] != null && !StringUtil.isEmpty(farmer[20]) ? farmer[20].toString() : "");
		objectMap.put(TxnEnrollmentProperties.MOBILE_NUMBER,
				farmer[21] != null && !StringUtil.isEmpty(farmer[21]) ? farmer[21].toString() : "");
		objectMap.put(TxnEnrollmentProperties.FARMER_PHOTO,
				farmer[27] != null && !ObjectUtil.isEmpty(farmer[27]) ? farmer[27].toString() : 0);
		objectMap.put(TxnEnrollmentProperties.FARMER_ID_PROOF,
				farmer[28] != null && !ObjectUtil.isEmpty(farmer[28]) ? farmer[28].toString() : 0);
		objectMap.put(TxnEnrollmentProperties.FARMER_CERT_STATUS,
				farmer[29] != null && !ObjectUtil.isEmpty(farmer[29]) ? farmer[29].toString() : "");
		objectMap.put(TxnEnrollmentProperties.MARITAL,
				farmer[30] != null && !ObjectUtil.isEmpty(farmer[30]) ? farmer[30].toString() : "");
		objectMap.put(TxnEnrollmentProperties.PHONE_NUMBER,
				farmer[31] != null && !ObjectUtil.isEmpty(farmer[31]) ? farmer[31].toString() : "");
		objectMap.put(TxnEnrollmentProperties.CASTE_NAME,
				farmer[32] != null && !ObjectUtil.isEmpty(farmer[32]) ? farmer[32].toString() : "");
		objectMap.put(TxnEnrollmentProperties.TRADER,
				farmer[33] != null && !ObjectUtil.isEmpty(farmer[33]) ? farmer[33].toString() : "");
		objectMap.put(TxnEnrollmentProperties.ADDRESS,
				farmer[34] != null && !ObjectUtil.isEmpty(farmer[34]) ? farmer[34].toString() : "");
		if (tenantId.equalsIgnoreCase(ESESystem.SYMRISE)) {
			if (menuMap.containsKey(farmer[0].toString())) {
				objectMap.put(TxnEnrollmentProperties.INSPECTION,
						!menuMap.get(farmer[0].toString()).get(0).equals("null")
								? menuMap.get(farmer[0].toString()).get(0) : "");
				objectMap.put(TxnEnrollmentProperties.DEAD, !menuMap.get(farmer[0].toString()).get(1).equals("null")
						? menuMap.get(farmer[0].toString()).get(1) : "");
			} else {
				objectMap.put(TxnEnrollmentProperties.INSPECTION, "");
				objectMap.put(TxnEnrollmentProperties.DEAD, "");
			}

		}
		if (tenantId.equalsIgnoreCase(ESESystem.WELSPUN_TENANT_ID)) {
			if (dynamicCount.containsKey(farmer[0].toString())) {
				objectMap.put(TxnEnrollmentProperties.DYNAMIC_TRANSACTION_COUNT, !dynamicCount.get(farmer[0].toString()).get(0).equals("null")
						? dynamicCount.get(farmer[0].toString()).get(0) : "");
			}
			 else {
					objectMap.put(TxnEnrollmentProperties.DYNAMIC_TRANSACTION_COUNT, "");
			 }
			}

		return objectMap;

	}

	/**
	 * Sets the prefernces service.
	 * 
	 * @param preferncesService
	 *            the new prefernces service
	 */
	public void setPreferncesService(IPreferencesService preferncesService) {

		this.preferncesService = preferncesService;
	}

	/**
	 * Gets the prefernces service.
	 * 
	 * @return the prefernces service
	 */
	public IPreferencesService getPreferncesService() {

		return preferncesService;
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
	 * Sets the agent service.
	 * 
	 * @param agentService
	 *            the new agent service
	 */
	public void setAgentService(IAgentService agentService) {

		this.agentService = agentService;
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
	 * Gets the service point service.
	 * 
	 * @return the service point service
	 */
	public IServicePointService getServicePointService() {

		return servicePointService;
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
	 * Sets the service point service.
	 * 
	 * @param servicePointService
	 *            the new service point service
	 */
	public void setServicePointService(IServicePointService servicePointService) {

		this.servicePointService = servicePointService;
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
	 * Gets the farm crops service.
	 * 
	 * @return the farm crops service
	 */
	public IFarmCropsService getFarmCropsService() {

		return farmCropsService;
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
	 * Gets the product distribution service.
	 * 
	 * @return the product distribution service
	 */
	public IProductDistributionService getProductDistributionService() {

		return productDistributionService;
	}

	/**
	 * Sets the product distribution service.
	 * 
	 * @param productDistributionService
	 *            the new product distribution service
	 */
	public void setProductDistributionService(IProductDistributionService productDistributionService) {

		this.productDistributionService = productDistributionService;
	}

	public String getTenantId() {

		return tenantId;
	}

	public void setTenantId(String tenantId) {

		this.tenantId = tenantId;
	}

}
