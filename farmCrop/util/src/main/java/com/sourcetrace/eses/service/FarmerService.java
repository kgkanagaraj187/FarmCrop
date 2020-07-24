/*
\\ * FarmerService.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.profile.ViewFarmerActivity;
import com.ese.entity.traceability.FarmerTraceability;
import com.ese.entity.txn.mfi.InterestCalcConsolidated;
import com.ese.entity.txn.mfi.InterestRateHistory;
import com.ese.entity.util.ESESystem;
import com.ese.entity.util.FarmCropsField;
import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerField;
import com.ese.entity.util.FarmerLocationMapField;
import com.ese.entity.util.LoanInterest;
import com.sourcetrace.eses.dao.IAccountDAO;
import com.sourcetrace.eses.dao.IAgentDAO;
import com.sourcetrace.eses.dao.ICardDAO;
import com.sourcetrace.eses.dao.IESESystemDAO;
import com.sourcetrace.eses.dao.IFarmCropsDAO;
import com.sourcetrace.eses.dao.IFarmerDAO;
import com.sourcetrace.eses.dao.ILocationDAO;
import com.sourcetrace.eses.dao.IProductDistributionDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.ColdStorageDetail;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig.LIST_METHOD;
import com.sourcetrace.eses.entity.DynamicFieldReportConfig;
import com.sourcetrace.eses.entity.DynamicFieldReportJoinMap;
import com.sourcetrace.eses.entity.DynamicFieldScoreMap;
import com.sourcetrace.eses.entity.DynamicMenuFieldMap;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmerFeedbackEntity;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.SamithiIcs;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseStorageMap;
import com.sourcetrace.eses.inspect.agrocert.CertificateStandard;
import com.sourcetrace.eses.inspect.agrocert.LanguagePreferences;
import com.sourcetrace.eses.interceptor.ITxnErrorCodes;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.Contract;
import com.sourcetrace.eses.order.entity.txn.CostFarming;
import com.sourcetrace.eses.order.entity.txn.CowInspection;
import com.sourcetrace.eses.order.entity.txn.CropSupplyImages;
import com.sourcetrace.eses.order.entity.txn.Cultivation;
import com.sourcetrace.eses.order.entity.txn.CultivationDetail;
import com.sourcetrace.eses.order.entity.txn.EventCalendar;
import com.sourcetrace.eses.order.entity.txn.Forecast;
import com.sourcetrace.eses.order.entity.txn.ForecastAdvisory;
import com.sourcetrace.eses.order.entity.txn.ForecastAdvisoryDetails;
import com.sourcetrace.eses.order.entity.txn.LoanApplication;
import com.sourcetrace.eses.order.entity.txn.LoanDistribution;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspection;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspectionData;
import com.sourcetrace.eses.order.entity.txn.PeriodicInspectionSymptom;
import com.sourcetrace.eses.order.entity.txn.WeatherForeCast;
import com.sourcetrace.eses.txn.agrocert.CropYield;
import com.sourcetrace.eses.txn.agrocert.CropYieldDetail;
import com.sourcetrace.eses.txn.agrocert.FarmerCropProdAnswers;
import com.sourcetrace.eses.txn.agrocert.FarmersQuestionAnswers;
import com.sourcetrace.eses.txn.exception.OfflineTransactionException;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.entity.Image;
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Calf;
import com.sourcetrace.esesw.entity.profile.CashReceived;
import com.sourcetrace.esesw.entity.profile.Coordinates;
import com.sourcetrace.esesw.entity.profile.CoordinatesMap;
import com.sourcetrace.esesw.entity.profile.CottonPrice;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.entity.profile.CropCalendar;
import com.sourcetrace.esesw.entity.profile.CropCalendarDetail;
import com.sourcetrace.esesw.entity.profile.DataLevel;
import com.sourcetrace.esesw.entity.profile.DistributionBalance;
import com.sourcetrace.esesw.entity.profile.DocumentUpload;
import com.sourcetrace.esesw.entity.profile.DynamicConstants;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.DynamicReportFieldsConfig;
import com.sourcetrace.esesw.entity.profile.DynamicReportTableConfig;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.ESECard;
import com.sourcetrace.esesw.entity.profile.Expenditure;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.FarmCropsCoordinates;
import com.sourcetrace.esesw.entity.profile.FarmCropsMaster;
import com.sourcetrace.esesw.entity.profile.FarmDetailedInfo;
import com.sourcetrace.esesw.entity.profile.FarmElement;
import com.sourcetrace.esesw.entity.profile.FarmIcsConversion;
import com.sourcetrace.esesw.entity.profile.FarmInventory;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicData;
import com.sourcetrace.esesw.entity.profile.FarmerDynamicFieldsValue;
import com.sourcetrace.esesw.entity.profile.FarmerEconomy;
import com.sourcetrace.esesw.entity.profile.FarmerFamily;
import com.sourcetrace.esesw.entity.profile.FarmerIncomeDetails;
import com.sourcetrace.esesw.entity.profile.FarmerLandDetails;
import com.sourcetrace.esesw.entity.profile.FarmerSoilTesting;
import com.sourcetrace.esesw.entity.profile.FarmerSourceIncome;
import com.sourcetrace.esesw.entity.profile.GMO;
import com.sourcetrace.esesw.entity.profile.GinnerQuantitySold;
import com.sourcetrace.esesw.entity.profile.HarvestData;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.HousingInfo;
import com.sourcetrace.esesw.entity.profile.InterestCalcHistory;
import com.sourcetrace.esesw.entity.profile.OfflineFarmCropsEnrollment;
import com.sourcetrace.esesw.entity.profile.OfflineFarmEnrollment;
import com.sourcetrace.esesw.entity.profile.OfflineFarmerEnrollment;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.ResearchStation;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.TreeDetail;
import com.sourcetrace.esesw.entity.profile.Village;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.entity.Agent;

/**
 * The Class FarmerService.
 */
@Service
@Transactional
public class FarmerService implements IFarmerService {

	private static final Logger LOGGER = Logger.getLogger(FarmerService.class.getName());
	private static final DateFormat TXN_DATE_FORMAT = new SimpleDateFormat(DateUtil.TXN_DATE_TIME);
	private static final String NOT_APPLICABLE = "N/A";

	@Autowired
	private IFarmerDAO farmerDAO;
	@Autowired
	private ILocationDAO locationDAO;
	@Autowired
	private ICardDAO cardDAO;
	@Autowired
	private IAccountDAO accountDAO;
	@Autowired
	private IUniqueIDGenerator idGenerator;
	@Autowired
	private IFarmCropsDAO farmCropsDAO;
	@Autowired
	private IAgentDAO agentDAO;
	@Autowired
	private IESESystemDAO systemDAO;
	@Autowired
	private IProductDistributionDAO productDistributionDAO;
	@Autowired
	private IPreferencesService preferencesService;
	@Autowired
	private ICertificationService certificationService;

	/**
	 * selectedObject Sets the farmer dao.
	 * 
	 * @param farmerDAO
	 *            the new farmer dao
	 */
	public void setFarmerDAO(IFarmerDAO farmerDAO) {

		this.farmerDAO = farmerDAO;
	}

	/**
	 * Gets the farmer dao.
	 * 
	 * @return the farmer dao
	 */
	public IFarmerDAO getFarmerDAO() {

		return farmerDAO;
	}

	/**
	 * Gets the location dao.
	 * 
	 * @return the location dao
	 */
	public ILocationDAO getLocationDAO() {

		return locationDAO;
	}

	/**
	 * Sets the location dao.
	 * 
	 * @param locationDAO
	 *            the new location dao
	 */
	public void setLocationDAO(ILocationDAO locationDAO) {

		this.locationDAO = locationDAO;
	}

	/**
	 * Gets the card dao.
	 * 
	 * @return the card dao
	 */
	public ICardDAO getCardDAO() {

		return cardDAO;
	}

	/**
	 * Sets the card dao.
	 * 
	 * @param cardDAO
	 *            the new card dao
	 */
	public void setCardDAO(ICardDAO cardDAO) {

		this.cardDAO = cardDAO;
	}

	/**
	 * Gets the account dao.
	 * 
	 * @return the account dao
	 */
	public IAccountDAO getAccountDAO() {

		return accountDAO;
	}

	/**
	 * Sets the account dao.
	 * 
	 * @param accountDAO
	 *            the new account dao
	 */
	public void setAccountDAO(IAccountDAO accountDAO) {

		this.accountDAO = accountDAO;
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
	 * Sets the id generator.
	 * 
	 * @param idGenerator
	 *            the new id generator
	 */
	public void setIdGenerator(IUniqueIDGenerator idGenerator) {

		this.idGenerator = idGenerator;
	}

	/**
	 * Sets the farm crops dao.
	 * 
	 * @param farmCropsDAO
	 *            the new farm crops dao
	 */
	public void setFarmCropsDAO(IFarmCropsDAO farmCropsDAO) {

		this.farmCropsDAO = farmCropsDAO;
	}

	/**
	 * Sets the agent dao.
	 * 
	 * @param agentDAO
	 *            the new agent dao
	 */
	public void setAgentDAO(IAgentDAO agentDAO) {

		this.agentDAO = agentDAO;
	}

	/**
	 * Sets the system dao.
	 * 
	 * @param systemDAO
	 *            the new system dao
	 */
	public void setSystemDAO(IESESystemDAO systemDAO) {

		this.systemDAO = systemDAO;
	}

	/**
	 * Gets the product distribution dao.
	 * 
	 * @return the product distribution dao
	 */
	public IProductDistributionDAO getProductDistributionDAO() {

		return productDistributionDAO;
	}

	/**
	 * Sets the product distribution dao.
	 * 
	 * @param productDistributionDAO
	 *            the new product distribution dao
	 */
	public void setProductDistributionDAO(IProductDistributionDAO productDistributionDAO) {

		this.productDistributionDAO = productDistributionDAO;
	}

	/**
	 * Gets the preferences service.
	 * 
	 * @return the preferences service
	 */
	public IPreferencesService getPreferencesService() {

		return preferencesService;
	}

	/**
	 * Sets the preferences service.
	 * 
	 * @param preferencesService
	 *            the new preferences service
	 */
	public void setPreferencesService(IPreferencesService preferencesService) {

		this.preferencesService = preferencesService;
	}

	/**
	 * Gets the farm crops dao.
	 * 
	 * @return the farm crops dao
	 */
	public IFarmCropsDAO getFarmCropsDAO() {

		return farmCropsDAO;
	}

	/**
	 * Gets the agent dao.
	 * 
	 * @return the agent dao
	 */
	public IAgentDAO getAgentDAO() {

		return agentDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findFarmerByFarmerId(java.lang.String)
	 */
	public Farmer findFarmerByFarmerId(String farmerId) {

		return farmerDAO.findFarmerByFarmerId(farmerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#addFarmer(com
	 * .ese.entity.profile.Farmer)
	 */
	public void addFarmer(Farmer farmer) {

		farmer.setRevisionNo(DateUtil.getRevisionNumber());
		farmerDAO.save(farmer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#findFarmerById
	 * (java.lang.Long)
	 */
	public Farmer findFarmerById(Long id) {

		return farmerDAO.findFarmerById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#editFarmer(
	 * com.ese.entity.profile.Farmer)
	 */
	public void editFarmer(Farmer existing) {

		existing.setRevisionNo(DateUtil.getRevisionNumber());
		farmerDAO.update(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#removeFarmer
	 * (com.ese.entity.profile.Farmer)
	 */
	public void removeFarmer(Farmer farmer) {

		farmerDAO.delete(farmer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#addFarm(com
	 * .ese.entity.profile.Farm)
	 */
	public void addFarm(Farm farm) {

		if (!ObjectUtil.isEmpty(farm.getFarmer())) {
			farm.getFarmer().setRevisionNo(DateUtil.getRevisionNumber());
			farm.setStatus(Farmer.Status.ACTIVE.ordinal());
		}
		farmerDAO.save(farm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#findFarmById
	 * (java.lang.Long)
	 */
	public Farm findFarmById(Long id) {

		return farmerDAO.findFarmById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#editFarm(com
	 * .ese.entity.profile.Farm)
	 */
	public void editFarm(Farm existing) {

		if (!ObjectUtil.isEmpty(existing.getFarmer())) {
			existing.getFarmer().setRevisionNo(DateUtil.getRevisionNumber());
		}
		farmerDAO.update(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#removeFarm(
	 * com.ese.entity.profile.Farm)
	 */
	public void removeFarm(Farm farm) {
		farm.setStatus(Farm.Status.DELETED.ordinal());
		farm.setRevisionNo(DateUtil.getRevisionNumber());
		farmerDAO.update(farm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * removeFarmByFarmerId(java.lang.String)
	 */
	public void removeFarmByFarmerId(long id) {

		farmerDAO.removeFarmByFarmerId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * saveFarmerFarmAndFarmCropsDetail(com.ese.entity.profile.Farmer,
	 * java.util.List, java.util.List)
	 */
	public void saveFarmerFarmAndFarmCropsDetail(Farmer farmer, List<Farm> farmList, List<FarmCrops> farmCropsList) {

		// FARMER SAVING
		farmerDAO.save(farmer);

		// FARM SAVING
		for (Farm farmObj : farmList) {
			farmObj.setFarmer(farmer);
			farmerDAO.save(farmObj);
		}

		// FARM CROP SAVING
		for (FarmCrops farmCropObj : farmCropsList) {
			farmerDAO.save(farmCropObj);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#findFarmByCode
	 * (java.lang.String)
	 */
	public Farm findFarmByCode(String code) {

		return farmerDAO.findFarmByCode(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * listFarmByFarmerId (long)
	 */
	public List<Farm> listFarmByFarmerId(long farmerId) {

		return farmerDAO.listFarmByFarmerId(farmerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listFarmerByRevNoAndCity(java.lang.Long, long)
	 */
	public List<Farmer> listFarmerByRevNoAndCity(Long revisionNo, long id) {

		return farmerDAO.listFarmerByRevNoAndCity(revisionNo, id);
	}

	/**
	 * Find farmer by farmer name.
	 * 
	 * @param selectedFarmer
	 *            the selected farmer
	 * @return the farmer
	 */
	public Farmer findFarmerByFarmerName(String selectedFarmer) {

		return farmerDAO.findFarmerByFarmerName(selectedFarmer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findFarmerByVillageCode(java.lang.String)
	 */
	public List<Farmer> findFarmerByVillageCode(String villageCode) {

		return farmerDAO.findFarmerByVillageCode(villageCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#listFarmer()
	 */
	public List<Farmer> listFarmer() {

		return farmerDAO.listFarmer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * addOfflineFarmerEnrollment(com.
	 * ese.entity.profile.OfflineFarmerEnrollment)
	 */
	public void addOfflineFarmerEnrollment(OfflineFarmerEnrollment offlineFarmerEnrollment) {

		farmerDAO.save(offlineFarmerEnrollment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listOfflineFarmerEnrollmentByStatusCode (int)
	 */
	public List<OfflineFarmerEnrollment> listOfflineFarmerEnrollmentByStatusCode(int statusCode) {

		return farmerDAO.listOfflineFarmerEnrollmentByStatusCode(statusCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * processOfflineFarmerEnrollment()
	 */
	public void processOfflineFarmerEnrollment() {

		List<OfflineFarmerEnrollment> offlineFarmerEnrollments = listOfflineFarmerEnrollmentByStatusEnrollmentType(
				ESETxnStatus.PENDING.ordinal(), OfflineFarmerEnrollment.FARMER_ENROLLMENT);
		if (!ObjectUtil.isListEmpty(offlineFarmerEnrollments)) {
			for (OfflineFarmerEnrollment offlineFarmerEnrollment : offlineFarmerEnrollments) {
				Agent agent = null;
				if (!StringUtil.isEmpty(offlineFarmerEnrollment.getEnrolledAgentId()))
					agent = agentDAO.findAgentByAgentId(offlineFarmerEnrollment.getEnrolledAgentId());
				try {

					if (ObjectUtil.isEmpty(agent))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_AGENT);

					Date farmerDateOfBirth = null;
					Date farmerDateOfJoining = null;

					if (StringUtil.isEmpty(offlineFarmerEnrollment.getFarmerId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARMER_ID);

					Farmer farmerExisting = findFarmerByFarmerId(offlineFarmerEnrollment.getFarmerId());
					if (!ObjectUtil.isEmpty(farmerExisting))
						throw new OfflineTransactionException(ITxnErrorCodes.FARMER_EXIST);

					if (StringUtil.isEmpty(offlineFarmerEnrollment.getFirstName()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FIRST_NAME);

					if (StringUtil.isEmpty(offlineFarmerEnrollment.getGender()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_GENDER);
					// Validating DOB and DOJ
					if (!StringUtil.isEmpty(offlineFarmerEnrollment.getDateOfBirth())) {
						try {
							farmerDateOfBirth = DateUtil.convertStringToDate(offlineFarmerEnrollment.getDateOfBirth(),
									"yyyyMMdd");
						} catch (Exception e) {
							throw new OfflineTransactionException(ITxnErrorCodes.INVALID_FARMER_DOB);
						}
					}
					if (!StringUtil.isEmpty(offlineFarmerEnrollment.getDateOfJoining())) {
						try {
							farmerDateOfJoining = DateUtil
									.convertStringToDate(offlineFarmerEnrollment.getDateOfJoining(), "yyyyMMdd");
						} catch (Exception e) {
							throw new OfflineTransactionException(ITxnErrorCodes.INVALID_FARMER_DOJ);
						}
					} else
						farmerDateOfJoining = new Date();

					if (StringUtil.isEmpty(offlineFarmerEnrollment.getAddress()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_ADDRESS);

					if (StringUtil.isEmpty(offlineFarmerEnrollment.getVillageCode()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_VILLAGE_CODE);

					Village village = locationDAO.findVillageByCode(offlineFarmerEnrollment.getVillageCode());
					if (ObjectUtil.isEmpty(village))
						throw new OfflineTransactionException(ITxnErrorCodes.VILLAGE_NOT_EXIST);

					// Validating Samithi
					if (StringUtil.isEmpty(offlineFarmerEnrollment.getSamithiCode()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_SAMITHI_CODE);
					Warehouse warehouse = locationDAO.findSamithiByCode(offlineFarmerEnrollment.getSamithiCode());
					if (ObjectUtil.isEmpty(warehouse))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_SAMITHI);

					if (StringUtil.isEmpty(offlineFarmerEnrollment.getPinCode()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_POSTAL_CODE);

					if (!StringUtil.isEmpty(offlineFarmerEnrollment.getCardId())) {
						ESECard card = cardDAO.findESECardByCardId(offlineFarmerEnrollment.getCardId());
						if (!ObjectUtil.isEmpty(card))
							throw new OfflineTransactionException(ITxnErrorCodes.FARMER_CARD_EXIST);
					}

					if (!StringUtil.isEmpty(offlineFarmerEnrollment.getAcctNumber())) {
						ESEAccount account = accountDAO.findAccountByAccountNo(offlineFarmerEnrollment.getAcctNumber());
						if (!ObjectUtil.isEmpty(account))
							throw new OfflineTransactionException(ITxnErrorCodes.FARMER_ACCOUNT_EXIST);
					}

					if (StringUtil.isEmpty(offlineFarmerEnrollment.getLatitude()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_LATITUDE);

					if (StringUtil.isEmpty(offlineFarmerEnrollment.getLongitude()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_LONGITUDE);

					String cardIdSeq = offlineFarmerEnrollment.getCardId(),
							accountNoSeq = offlineFarmerEnrollment.getAcctNumber();

					if (StringUtil.isEmpty(cardIdSeq)) {
						cardIdSeq = idGenerator.createFarmerCardIdSequence(IUniqueIDGenerator.WEB_REQUEST,
								IUniqueIDGenerator.ESE_AGENT_INTERNAL_ID);
					}

					if (StringUtil.isEmpty(accountNoSeq)) {
						accountNoSeq = idGenerator.createFarmerAccountNoSequence(IUniqueIDGenerator.WEB_REQUEST,
								IUniqueIDGenerator.ESE_AGENT_INTERNAL_ID);
					}

					Farmer farmer = new Farmer();
					farmer.setFarmerId(offlineFarmerEnrollment.getFarmerId());
					farmer.setFarmerCode(offlineFarmerEnrollment.getFarmerCode());
					farmer.setFirstName(offlineFarmerEnrollment.getFirstName());
					farmer.setLastName(!StringUtil.isEmpty(offlineFarmerEnrollment.getLastName())
							? offlineFarmerEnrollment.getLastName() : " ");
					farmer.setGender(offlineFarmerEnrollment.getGender().toUpperCase());
					farmer.setDateOfBirth(farmerDateOfBirth);
					farmer.setDateOfJoining(farmerDateOfJoining);
					int noOfFamilyMembers = 0;
					try {
						if (!StringUtil.isEmpty(offlineFarmerEnrollment.getNoOfFamilyMembers()))
							noOfFamilyMembers = Integer.parseInt(offlineFarmerEnrollment.getNoOfFamilyMembers());
					} catch (Exception e) {
						LOGGER.info(e.getMessage());
						e.printStackTrace();
					}

					farmer.setNoOfFamilyMembers(noOfFamilyMembers);
					farmer.setAddress(offlineFarmerEnrollment.getAddress());
					farmer.setVillage(village);
					farmer.setCity(locationDAO.findMunicipalityByVillageCode(village.getCode()));
					farmer.setPinCode(offlineFarmerEnrollment.getPinCode());
					farmer.setPhoneNumber(offlineFarmerEnrollment.getPhoneNumber());
					farmer.setMobileNumber(offlineFarmerEnrollment.getMobileNumber());
					farmer.setEmail(offlineFarmerEnrollment.getEmail());
					farmer.setLatitude(offlineFarmerEnrollment.getLatitude());
					farmer.setLongitude(offlineFarmerEnrollment.getLongitude());
					farmer.setPostOffice(offlineFarmerEnrollment.getPostOffice());
					farmer.setSamithi(warehouse);
					farmer.setStatus(Farmer.Status.ACTIVE.ordinal());
					// Adding photo capturing time
					if (!StringUtil.isEmpty(offlineFarmerEnrollment.getPhotoCaptureTime())) {
						try {
							Date photoCaptureDate = TXN_DATE_FORMAT
									.parse(offlineFarmerEnrollment.getPhotoCaptureTime());
							farmer.setPhotoCaptureTime(photoCaptureDate);
						} catch (Exception e) {
							LOGGER.info(e.getMessage());
							e.getMessage();
						}
					}

					Set<Farm> farms = new LinkedHashSet<Farm>();

					Map<String, Object> farmRefMap = new LinkedHashMap<String, Object>();

					if (!ObjectUtil.isListEmpty(offlineFarmerEnrollment.getFarms())) {
						for (OfflineFarmEnrollment offlineFarmEnrollment : offlineFarmerEnrollment.getFarms()) {
							if (StringUtil.isEmpty(offlineFarmEnrollment.getFarmCode()))
								throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARM_CODE);
							else {
								// Farm
								Farm existing = farmerDAO.findFarmByCode(offlineFarmEnrollment.getFarmCode());
								if (!ObjectUtil.isEmpty(existing)
										|| farmRefMap.containsKey(offlineFarmEnrollment.getFarmCode()))
									throw new OfflineTransactionException(ITxnErrorCodes.FARM_CODE_EXIST);
							}
							if (StringUtil.isEmpty(offlineFarmEnrollment.getFarmName()))
								throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARM_NAME);

							if (StringUtil.isEmpty(offlineFarmEnrollment.getLatitude()))
								throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARM_LATITUDE);

							if (StringUtil.isEmpty(offlineFarmEnrollment.getLongitude()))
								throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARM_LONGITUDE);

							Farm farm = new Farm();
							farm.setFarmCode(offlineFarmEnrollment.getFarmCode());
							farm.setFarmName(offlineFarmEnrollment.getFarmName());
							farm.setHectares(offlineFarmEnrollment.getHectares());
							farm.setLandInProduction(offlineFarmEnrollment.getLandInProduction());
							farm.setLandNotInProduction(offlineFarmEnrollment.getLandNotInProduction());
							farm.setLatitude(offlineFarmEnrollment.getLatitude());
							farm.setLongitude(offlineFarmEnrollment.getLongitude());
							farm.setPhoto(offlineFarmEnrollment.getPhoto());

							// Adding Farm photo capturing time
							if (!StringUtil.isEmpty(offlineFarmEnrollment.getPhotoCaptureTime())) {
								try {
									Date photoCaptureDate = TXN_DATE_FORMAT
											.parse(offlineFarmEnrollment.getPhotoCaptureTime());
									farm.setPhotoCaptureTime(photoCaptureDate);
								} catch (Exception e) {
									LOGGER.info(e.getMessage());
									e.printStackTrace();
								}
							}

							farm.setFarmer(farmer);

							farmRefMap.put(farm.getFarmCode(), farm);

							farms.add(farm);
						}
					}

					if (!ObjectUtil.isListEmpty(offlineFarmerEnrollment.getFarmCrops())) {
						for (OfflineFarmCropsEnrollment offlineFarmCropsEnrollment : offlineFarmerEnrollment
								.getFarmCrops()) {
							if (StringUtil.isEmpty(offlineFarmCropsEnrollment.getCropCode()))
								throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARM_CROP_CODE);

							FarmCropsMaster farmCropsMaster = farmCropsDAO
									.findFarmCropsMasterByCode(offlineFarmCropsEnrollment.getCropCode());
							if (ObjectUtil.isEmpty(farmCropsMaster))
								throw new OfflineTransactionException(ITxnErrorCodes.FARM_CROPS_DOES_NOT_EXIST);

							if (StringUtil.isEmpty(offlineFarmCropsEnrollment.getFarmCode()))
								throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARM_CODE_REFERENCE);

							if (StringUtil.isEmpty(offlineFarmCropsEnrollment.getCropArea()))
								throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARM_CROP_AREA);

							if (StringUtil.isEmpty(offlineFarmCropsEnrollment.getProductionPerYear()))
								throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARM_PRODUCTION_YEAR);

							FarmCrops farmCrops = new FarmCrops();
							/*
							 * farmCrops.setFarmCropsMaster(farmCropsMaster);
							 * farmCrops.setCropArea(offlineFarmCropsEnrollment.
							 * getCropArea()); farmCrops.setProductionPerYear(
							 * offlineFarmCropsEnrollment.getProductionPerYear()
							 * );
							 */

							Farm refFarm = (Farm) farmRefMap.get(offlineFarmCropsEnrollment.getFarmCode());
							if (ObjectUtil.isEmpty(refFarm))
								throw new OfflineTransactionException(ITxnErrorCodes.FARM_NOT_EXIST);
							farmCrops.setFarm(refFarm);
							if (ObjectUtil.isListEmpty(refFarm.getFarmCrops()))
								refFarm.setFarmCrops(new LinkedHashSet<FarmCrops>());
							else {
								for (FarmCrops crops : refFarm.getFarmCrops()) {
									if (crops.getProcurementVariety().getCode()
											.equalsIgnoreCase(farmCrops.getProcurementVariety().getCode()))
										throw new OfflineTransactionException(ITxnErrorCodes.FARM_CODE_EXIST);
								}
							}
							refFarm.getFarmCrops().add(farmCrops);
						}
					}

					farmer.setFarms(farms);

					// Farmer Image
					if (offlineFarmerEnrollment.getPhoto() != null
							|| offlineFarmerEnrollment.getFingerPrint() != null) {
						ImageInfo imageInfo = new ImageInfo();
						if (offlineFarmerEnrollment.getPhoto() != null) {
							Image photo = new Image();
							photo.setImage(offlineFarmerEnrollment.getPhoto());
							photo.setImageId(farmer.getFarmerId() + "-FP");
							imageInfo.setPhoto(photo);
						}
						if (offlineFarmerEnrollment.getFingerPrint() != null) {
							Image fp = new Image();
							fp.setImage(offlineFarmerEnrollment.getFingerPrint());
							fp.setImageId(farmer.getFarmerId() + "-FF");
							imageInfo.setBiometric(fp);
						}
						farmer.setImageInfo(imageInfo);
					}

					Date txnTime = DateUtil.convertStringToDate(offlineFarmerEnrollment.getEnrolledDate(),
							DateUtil.TXN_DATE_TIME);

					// Farmer Contract
					// addContractForFarmer(farmer);

					// Farmer card
					ESECard card = new ESECard();
					card.setCardId(cardIdSeq);
					card.setType(ESECard.FARMER_CARD);
					card.setCreateTime(txnTime);
					card.setIssueDate(txnTime);
					card.setProfileId(farmer.getFarmerId());
					card.setStatus(ESECard.INACTIVE);
					card.setCardRewritable(ESECard.IS_REWRITABLE_NO);
					agentDAO.save(card);

					offlineFarmerEnrollment.setStatusCode(ESETxnStatus.SUCCESS.ordinal());
					offlineFarmerEnrollment.setStatusMsg(ESETxnStatus.SUCCESS.toString());
				} catch (OfflineTransactionException ote) {
					offlineFarmerEnrollment.setStatusCode(ESETxnStatus.ERROR.ordinal());
					offlineFarmerEnrollment.setStatusMsg(ote.getError());
				} catch (Exception e) {
					offlineFarmerEnrollment.setStatusCode(ESETxnStatus.ERROR.ordinal());
					offlineFarmerEnrollment.setStatusMsg(e.getMessage().substring(0, 40));
				}
				// Update offline farmer enrollment entity
				editOfflineFarmerEnrollment(offlineFarmerEnrollment);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listOfflineFarmerEnrollmentByStatusEnrollmentType(int, java.lang.String)
	 */
	public List<OfflineFarmerEnrollment> listOfflineFarmerEnrollmentByStatusEnrollmentType(int statusCode,
			String txnType) {

		return farmerDAO.listOfflineFarmerEnrollmentByStatusEnrollmentType(statusCode, txnType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * editOfflineFarmerEnrollment(com
	 * .ese.entity.profile.OfflineFarmerEnrollment)
	 */
	public void editOfflineFarmerEnrollment(OfflineFarmerEnrollment offlineFarmerEnrollment) {

		farmerDAO.update(offlineFarmerEnrollment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * processOfflineBiometricUpload()
	 */
	public void processOfflineBiometricUpload() {

		List<OfflineFarmerEnrollment> offlineFarmerEnrollments = listOfflineFarmerEnrollmentByStatusEnrollmentType(
				ESETxnStatus.PENDING.ordinal(), OfflineFarmerEnrollment.FARMER_BIOMETRIC_UPLOAD);
		if (!ObjectUtil.isListEmpty(offlineFarmerEnrollments)) {
			for (OfflineFarmerEnrollment offlineFarmerEnrollment : offlineFarmerEnrollments) {

				Agent agent = null;

				try {

					if (StringUtil.isEmpty(offlineFarmerEnrollment.getEnrolledAgentId())) {
						throw new OfflineTransactionException(ITxnErrorCodes.AGENT_ID_EMPTY);
					}
					agent = agentDAO.findAgentByAgentId(offlineFarmerEnrollment.getEnrolledAgentId());
					if (ObjectUtil.isEmpty(agent))
						throw new OfflineTransactionException(ITxnErrorCodes.INVALID_AGENT);

					if (StringUtil.isEmpty(offlineFarmerEnrollment.getFarmerId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARMER_ID);

					if (StringUtil.isEmpty(offlineFarmerEnrollment.getCardId()))
						throw new OfflineTransactionException(ITxnErrorCodes.EMPTY_FARMER_CARD_ID);

					Farmer farmer = findFarmerByFarmerId(offlineFarmerEnrollment.getFarmerId());
					if (ObjectUtil.isEmpty(farmer))
						throw new OfflineTransactionException(ITxnErrorCodes.FARMER_NOT_EXIST);

					ESECard card = cardDAO.findCardByProfileIdAndCardId(farmer.getFarmerId(),
							offlineFarmerEnrollment.getCardId());
					if (ObjectUtil.isEmpty(card)) {
						throw new OfflineTransactionException(ITxnErrorCodes.FARMER_CARD_UNAVAILABLE);
					}

					/** DATA PROCESSING **/
					if (ObjectUtil.isEmpty(farmer.getImageInfo())) {
						if (offlineFarmerEnrollment.getPhoto() != null
								&& offlineFarmerEnrollment.getFingerPrint() != null) {
							ImageInfo imageInfo = new ImageInfo();
							if (offlineFarmerEnrollment.getPhoto() != null) {
								Image photo = new Image();
								photo.setImage(offlineFarmerEnrollment.getPhoto());
								photo.setImageId(offlineFarmerEnrollment.getFarmerId() + "-FP");
								imageInfo.setPhoto(photo);
							}
							if (offlineFarmerEnrollment.getFingerPrint() != null) {
								Image fp = new Image();
								fp.setImage(offlineFarmerEnrollment.getFingerPrint());
								fp.setImageId(offlineFarmerEnrollment.getFarmerId() + "-FF");
								imageInfo.setBiometric(fp);
							}
							farmer.setImageInfo(imageInfo);
						}
					} else {
						if (offlineFarmerEnrollment.getFingerPrint() != null) {
							if (ObjectUtil.isEmpty(farmer.getImageInfo().getBiometric())) {
								Image fp = new Image();
								fp.setImage(offlineFarmerEnrollment.getFingerPrint());
								fp.setImageId(offlineFarmerEnrollment.getFarmerId() + "-FF");
								farmer.getImageInfo().setBiometric(fp);
							} else {
								farmer.getImageInfo().getBiometric().setImage(offlineFarmerEnrollment.getFingerPrint());
							}
						}
						if (offlineFarmerEnrollment.getPhoto() != null) {
							if (ObjectUtil.isEmpty(farmer.getImageInfo().getPhoto())) {
								Image photo = new Image();
								photo.setImage(offlineFarmerEnrollment.getPhoto());
								photo.setImageId(offlineFarmerEnrollment.getFarmerId() + "-FP");
								farmer.getImageInfo().setPhoto(photo);
							} else {
								farmer.getImageInfo().getPhoto().setImage(offlineFarmerEnrollment.getPhoto());
							}
						}
					}

					farmerDAO.update(farmer);
					offlineFarmerEnrollment.setStatusCode(ESETxnStatus.SUCCESS.ordinal());
					offlineFarmerEnrollment.setStatusMsg(ESETxnStatus.SUCCESS.toString());
				} catch (OfflineTransactionException ote) {
					offlineFarmerEnrollment.setStatusCode(ESETxnStatus.ERROR.ordinal());
					offlineFarmerEnrollment.setStatusMsg(ote.getError());
				} catch (Exception e) {
					offlineFarmerEnrollment.setStatusCode(ESETxnStatus.ERROR.ordinal());
					offlineFarmerEnrollment.setStatusMsg(e.getMessage().substring(0, 40));
				}
				// Update offline farmer enrollment entity
				editOfflineFarmerEnrollment(offlineFarmerEnrollment);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * findFarmPhotoById (java.lang.Long)
	 */
	public byte[] findFarmPhotoById(Long id) {

		return farmerDAO.findFarmPhotoById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#listSeasons()
	 */
	public List<Season> listSeasons() {

		return farmerDAO.listSeasons();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#findSeasonById
	 * (java.lang.Long)
	 */
	public Season findSeasonById(Long id) {

		return farmerDAO.findSeasonById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * listFarmerByCityId (long)
	 */
	public List<Farmer> listFarmerByCityId(long id) {

		return farmerDAO.listFarmerByCityId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listFarmerByVillageId(long)
	 */
	public List<Farmer> listFarmerByVillageId(long id) {

		return farmerDAO.listFarmerByVillageId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findFarmerAccountStatuById(java .lang.String)
	 */
	public String findFarmerAccountStatuById(String farmerId) {

		return farmerDAO.findFarmerAccountStatuById(farmerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findFarmerAccountStatuById(java .lang.String)
	 */
	public List<Farmer> listActiveFarmersByVillageCode(String villageCode) {

		return farmerDAO.listActiveFarmersByVillageCode(villageCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listActiveFarmerByCityCode(java .lang.String)
	 */
	public List<Farmer> listActiveFarmerByCityCode(String cityCode) {

		return farmerDAO.listActiveFarmerByCityCode(cityCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findContractByFarmerIdSeasonCodeProcurementProduct(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public Contract findContractByFarmerIdSeasonCodeProcurementProduct(String farmerId, String seasonCode,
			String procurementProductCode) {

		return farmerDAO.findContractByFarmerIdSeasonCodeProcurementProduct(farmerId, seasonCode,
				procurementProductCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#addContract
	 * (com.sourcetrace.eses .order.entity.txn.Contract)
	 */
	public Contract addContract(Contract contract) {

		String contractNoSeq = idGenerator.createContractNoSequence();
		contract.setContractNo(contractNoSeq);
		ESEAccount account = createContractESEAccount(contract.getFarmer().getFarmerId(),
				idGenerator.createFarmerAccountNoSequence(IUniqueIDGenerator.WEB_REQUEST,
						IUniqueIDGenerator.ESE_AGENT_INTERNAL_ID),
				contract.getFarmer().getBranchId());
		contract.setAccount(account);
		contract.setStatus(Contract.Status.ACTIVE.ordinal());
		contract.getFarmer().setRevisionNo(DateUtil.getRevisionNumber());
		return contract;

	}

	/**
	 * Creates the contract ese account.
	 * 
	 * @param profileId
	 *            the profile id
	 * @param accountNo
	 *            the account no
	 * @return the eSE account
	 */
	public ESEAccount createContractESEAccount(String profileId, String accountNo, String branchId) {

		ESEAccount account = new ESEAccount();
		account.setAccountNo(accountNo);
		account.setType(ESEAccount.CONTRACT_ACCOUNT);
		account.setAccountType(ESEAccount.SAVING_BANK_ACCOUNT);
		account.setStatus(ESEAccount.ACTIVE);
		account.setBranchId(branchId);
		account.setBalance(0.0);
		account.setDistributionBalance(0.0);
		account.setSavingAmount(0.0);
		account.setShareAmount(0.0);
		account.setAccountOpenDate(new Date());
		account.setCreateTime(new Date());
		account.setProfileId(profileId);

		return account;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#editContract
	 * (com.sourcetrace.eses .order.entity.txn.Contract)
	 */
	public void editContract(Contract contract) {

		contract.getFarmer().setRevisionNo(DateUtil.getRevisionNumber());
		farmerDAO.update(contract);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listActiveContractFarmersByVillageCodeSeasonId(java.lang.String,
	 * java.lang.Long)
	 */
	public List<Farmer> listActiveContractFarmersByVillageCodeSeasonCode(String villageCode, String seasonCode) {

		return farmerDAO.listActiveContractFarmersByVillageCodeSeasonCode(villageCode, seasonCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findAccountBySeassonProcurmentProductFarmer(long, long)
	 */
	public ESEAccount findAccountBySeassonProcurmentProductFarmer(long procurementProductId, long farmerId) {

		ESEAccount account = null;
		Season season = findCurrentSeason();
		if (!ObjectUtil.isEmpty(season))
			account = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), procurementProductId,
					farmerId);
		return account;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findContractBySeassonProcurmentProductFarmer(long, long)
	 */
	public Contract findContractBySeasonProcurmentProductFarmer(long procurementProductId, long farmerId) {

		Contract contract = null;
		Season season = findCurrentSeason();
		if (!ObjectUtil.isEmpty(season))
			contract = farmerDAO.findContractBySeassonProcurmentProductFarmer(season.getId(), procurementProductId,
					farmerId);
		return contract;
	}

	/**
	 * Find current season.
	 * 
	 * @return the season
	 */
	private Season findCurrentSeason() {

		ESESystem preference = systemDAO.findPrefernceById(ESESystem.SYSTEM_SWITCH);
		String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
		if (!StringUtil.isEmpty(currentSeasonCode)) {
			Season currentSeason = productDistributionDAO.findSeasonBySeasonCode(currentSeasonCode);
			return currentSeason;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * isFarmerContractMappingExist(java .lang.String)
	 */
	public boolean isFarmerContractMappingExist(String farmerId) {

		return farmerDAO.isFarmerContractMappingExist(farmerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listActiveContractFarmersBySeasonRevNoAndCity(java.lang.Long,
	 * java.lang.String, java.lang.Long)
	 */
	public List<Farmer> listActiveContractFarmersBySeasonRevNoAndCity(Long cityId, String seasonCode, Long revisionNo) {

		return farmerDAO.listActiveContractFarmersBySeasonRevNoAndCity(cityId, seasonCode, revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listActiveContractFarmersBySeasonProcurementProduct(long)
	 */
	public List<Farmer> listActiveContractFarmersBySeasonProcurementProduct(long procurementProductId) {

		List<Farmer> farmers = new ArrayList<Farmer>();
		Season season = findCurrentSeason();
		if (!ObjectUtil.isEmpty(season))
			farmers = farmerDAO.listActiveContractFarmersBySeasonProcurementProduct(season.getId(),
					procurementProductId);
		return farmers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listActiveContractFarmersBySeasonProcurementProductVillage
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Farmer> listActiveContractFarmersBySeasonProcurementProductVillage(String seasonCode,
			String productCode, String selectedVillage) {

		Season season = null;
		List<Farmer> farmers = new LinkedList<Farmer>();
		if (StringUtil.isEmpty(seasonCode)) {
			season = findCurrentSeason();
		}
		if (!ObjectUtil.isEmpty(season)) {
			farmers = farmerDAO.listActiveContractFarmersBySeasonProcurementProductVillage(season.getCode(),
					productCode, selectedVillage);
		}
		return farmers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findActiveContractByFarmerIdProcurementProductCodeNotInSeasonCode
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	public Contract findActiveContractByFarmerIdProcurementProductCodeNotInSeasonCode(String farmerId,
			String procurementProductCode, String seasonCode) {

		return farmerDAO.findActiveContractByFarmerIdProcurementProductCodeNotInSeasonCode(farmerId,
				procurementProductCode, seasonCode);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#processContract
	 * (com.sourcetrace .eses.order.entity.txn.Contract)
	 */
	public void processContract(Contract contract) {

		AgroTransaction agroTransaction = null;
		contract = addContract(contract);

		if (Contract.CARRY_FORWARD == contract.getExistingContractDecision()) {

			Contract existingContract = findActiveContractByFarmerIdProcurementProductCodeNotInSeasonCode(
					contract.getFarmer().getFarmerId(), contract.getProcurementProduct().getCode(),
					contract.getSeason().getCode());
			if (!ObjectUtil.isEmpty(existingContract)) {
				agroTransaction = new AgroTransaction();
				agroTransaction.setReceiptNo(NOT_APPLICABLE);
				agroTransaction.setAgentId(NOT_APPLICABLE);
				agroTransaction.setAgentName(NOT_APPLICABLE);
				agroTransaction.setDeviceId(NOT_APPLICABLE);
				agroTransaction.setDeviceName(NOT_APPLICABLE);
				agroTransaction.setServicePointId(NOT_APPLICABLE);
				agroTransaction.setServicePointName(NOT_APPLICABLE);
				agroTransaction.setFarmerId(contract.getFarmer().getFarmerId());
				agroTransaction
						.setFarmerName(contract.getFarmer().getFirstName() + " " + contract.getFarmer().getLastName());
				agroTransaction.setProfType(Profile.CLIENT);
				agroTransaction.setOperType(ESETxn.ON_LINE);
				agroTransaction.setTxnType(Contract.CONTRACT_TXN);
				/** Transaction Type , SeasonName, SeasonYear **/
				agroTransaction.setTxnDesc(Contract.CONTRACT_TXN_DESCRIPTION + "||"
						+ existingContract.getSeason().getName() + "|" + existingContract.getSeason().getYear() + "||");
				agroTransaction.setTxnTime(new Date());
				agroTransaction.setIntBalance(0);
				agroTransaction.setTxnAmount(existingContract.getAccount().getBalance());
				agroTransaction.setBalAmount(existingContract.getAccount().getBalance());
				agroTransaction.setAccount(contract.getAccount());
				contract.getAccount().setBalance(agroTransaction.getBalAmount());
			}
		}

		updateFarmerExistingContractStatus(contract.getFarmer().getFarmerId(),
				contract.getProcurementProduct().getCode(), contract.getSeason().getCode());
		farmerDAO.save(contract.getAccount());
		farmerDAO.save(contract);
		if (!ObjectUtil.isEmpty(agroTransaction)) {
			farmerDAO.save(agroTransaction);
		}

	}

	/**
	 * Update farmer existing contract status.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @param procurementProductCode
	 *            the procurement product code
	 * @param seasonCode
	 *            the season code
	 */
	public void updateFarmerExistingContractStatus(String farmerId, String procurementProductCode, String seasonCode) {

		Contract contract = farmerDAO.findActiveContractByFarmerIdNotInSeasonCode(farmerId, procurementProductCode,
				seasonCode);
		if (!ObjectUtil.isEmpty(contract) && !ObjectUtil.isEmpty(contract.getAccount())) {
			Date tempDate = contract.getAccount().getUpdateTime();
			contract.getAccount().setStatus(ESEAccount.INACTIVE);
			farmerDAO.update(contract.getAccount());
			// Revert back UpdateTime for the existing Contract's Account
			farmerDAO.updateFarmerModifyTime(contract.getAccount().getId(), tempDate);
			farmerDAO.update(contract);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findFarmerCurrentContractStatusById(java.lang.Long, java.lang.Long)
	 */
	public boolean findFarmerCurrentContractStatusById(Long id, Long seasonId) {

		return farmerDAO.findFarmerCurrentContractStatusById(id, seasonId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findSeasonBySeasonCode(java.lang.String)
	 */
	public Long findSeasonBySeasonCode(String seasonCode) {

		return farmerDAO.findSeasonBySeasonCode(seasonCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#listFarm()
	 */
	public List<Farm> listFarm() {

		return farmerDAO.listFarm();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * addContractForFarmerAndHarvestData (com.ese.entity.profile.Farmer,
	 * java.util.Set)
	 */
	public void addContractForFarmerAndHarvestData(Farmer farmer, String createUserName) {

		addContractForFarmer(farmer, createUserName);
		// addHarvestData(harvestDatas);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * addContractForFarmer(com.ese.entity .profile.Farmer)
	 */
	/**
	 * Adds the contract for farmer.
	 * 
	 * @param farmer
	 *            the farmer
	 */
	@SuppressWarnings("unchecked")
	public void addContractForFarmer(Farmer farmer, String createUserName) {

		// Farmer
		if (!ObjectUtil.isEmpty(farmer.getFarmerEconomy()))
			farmerDAO.save(farmer.getFarmerEconomy());
		// Farm Detailed Info
		if (!ObjectUtil.isEmpty(farmer.getFarms())) {
			for (Farm farm : farmer.getFarms()) {
				farmerDAO.save(farm.getFarmDetailedInfo());
			}
		}
		addFarmer(farmer);

		// Contract
		if (farmer.getStatusCode() == 0) {
			Contract contract = new Contract();
			contract.setContractNo(idGenerator.createContractNoSequence());
			contract.setPricePatterns(new HashSet(productDistributionDAO.listPricePattern()));
			contract.setAccount(createContractFarmerESEAccount(farmer.getFarmerId(), farmer,
					idGenerator.createFarmerAccountNoSequence(IUniqueIDGenerator.WEB_REQUEST,
							IUniqueIDGenerator.ESE_AGENT_INTERNAL_ID),
					farmer.getBranchId()));
			contract.setFarmer(farmer);
			contract.setSeason(findCurrentSeason());
			contract.setStatus(Contract.Status.ACTIVE.ordinal());
			farmerDAO.save(contract.getAccount());
			farmerDAO.save(contract);

			ESESystem eseSystem = preferencesService.findPrefernceById("1");

			if (eseSystem.getPreferences().get(ESESystem.INTEREST_MODULE).equals("1")) {
				InterestCalcConsolidated interestCalcConsolidated = new InterestCalcConsolidated();
				interestCalcConsolidated.setFarmerProfileId(farmer.getFarmerId());
				interestCalcConsolidated.setFarmerAccountRef(contract.getAccount().getAccountNo());
				interestCalcConsolidated.setAccumulatedPrincipalAmount(0);
				interestCalcConsolidated.setAccumulatedPrincipalAmount2(0);
				InterestRateHistory interestRateHistory = findCurrentRateOfInterest();
				double rateOfInterest = ObjectUtil.isEmpty(interestRateHistory) ? 0
						: interestRateHistory.getRateOfInterest();
				interestCalcConsolidated.setCurrentRateOfInterest(rateOfInterest);
				interestCalcConsolidated.setCurrentRateOfInterest2(0);

				interestCalcConsolidated.setAccumulatedIntAmount(0);
				interestCalcConsolidated.setAccumulatedIntAmount2(0);
				interestCalcConsolidated.setLastCalcDate(new Date());
				interestCalcConsolidated.setCreateUserName(createUserName);
				interestCalcConsolidated.setCreateDt(new Date());
				interestCalcConsolidated.setRevisionNo(DateUtil.getRevisionNumber());
				farmerDAO.save(interestCalcConsolidated);
			}
		}

	}

	/**
	 * Adds the harvest data.
	 * 
	 * @param harvestDatas
	 *            the harvest datas
	 */
	@SuppressWarnings("unchecked")
	public void addHarvestData(Set<HarvestData> harvestDatas) {

		Iterator hdIt = harvestDatas.iterator();
		while (hdIt.hasNext()) {
			farmerDAO.save(hdIt.next());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listActiveContractFarmersBySeasonRevNoAndSamithi(long, java.lang.String,
	 * java.lang.Long)
	 */
	public List<Farmer> listActiveContractFarmersBySeasonRevNoAndSamithi(long id, String currentSeasonCode,
			Long revisionNo) {

		return farmerDAO.listActiveContractFarmersBySeasonRevNoAndSamithi(id, currentSeasonCode, revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * removeContractByFarmerId(long)
	 */
	public void removeContractByFarmerId(long id) {

		farmerDAO.removeContractByFarmerId(id);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listActiveContractFarmersByVillageCodeSeasonCodeSamithiCode
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Farmer> listActiveContractFarmersByVillageCodeSeasonCodeSamithiCode(String villageCode,
			String seasonCode, String samithiCode) {

		return farmerDAO.listActiveContractFarmersByVillageCodeSeasonCodeSamithiCode(villageCode, seasonCode,
				samithiCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listActiveContractFarmersByVillageCodeSeasonCodeSamithiId
	 * (java.lang.String, java.lang.String, long)
	 */
	public List<Farmer> listActiveContractFarmersByVillageCodeSeasonCodeSamithiId(String villageCode, String seasonCode,
			long samithiId) {

		return farmerDAO.listActiveContractFarmersByVillageCodeSeasonCodeSamithiId(villageCode, seasonCode, samithiId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listFarmerWithAccount()
	 */
	public List<Object[]> listFarmerWithAccount() {

		return farmerDAO.listFarmerWithAccount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listFarmerByCooperativeCertificateStandardVillage(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public List<Object[]> listFarmerByCooperativeCertificateStandardVillage(String selectedCooperative,
			String selectedCertificateStandard, String selectedVillage) {

		return farmerDAO.listFarmerByCooperativeCertificateStandardVillage(selectedCooperative,
				selectedCertificateStandard, selectedVillage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findHeadOfFamilyById(long)
	 */
	public FarmerFamily findHeadOfFamilyById(long id) {

		return farmerDAO.findHeadOfFamilyById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#addFarmerFamily
	 * (com.ese.entity. profile.FarmerFamily)
	 */
	public void addFarmerFamily(FarmerFamily farmerFamily) {

		farmerDAO.save(farmerFamily);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findFarmerFamilyById(long)
	 */
	public FarmerFamily findFarmerFamilyById(long id) {

		return farmerDAO.findFarmerFamilyById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * editFarmerFamily (com.ese.entity .profile.FarmerFamily)
	 */
	public void editFarmerFamily(FarmerFamily farmerFamily) {

		farmerDAO.update(farmerFamily);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * removeFarmerFamily (com.ese.entity .profile.FarmerFamily)
	 */
	public void removeFarmerFamily(FarmerFamily farmerFamily) {

		farmerDAO.delete(farmerFamily);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * addFarmerEconomy (com.ese.entity .profile.FarmerEconomy)
	 */
	public void addFarmerEconomy(FarmerEconomy farmerEconomy) {

		farmerDAO.save(farmerEconomy);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * editFarmerEconomy (com.ese.entity .profile.FarmerEconomy)
	 */
	public void editFarmerEconomy(FarmerEconomy farmerEconomy) {

		farmerDAO.update(farmerEconomy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * removeFarmerEconomy (com.ese.entity .profile.FarmerEconomy)
	 */
	public void removeFarmerEconomy(FarmerEconomy farmerEconomy) {

		if (!ObjectUtil.isEmpty(farmerEconomy))
			farmerDAO.delete(farmerEconomy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * removeFarmerEconomyMappingSQL(com .ese.entity.profile.FarmerEconomy)
	 */
	public void removeFarmerEconomyMappingSQL(FarmerEconomy farmerEconomy) {

		farmerDAO.removeFarmerEconomyMappingSQL(farmerEconomy);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * addHarvestSeason (com.ese.entity .profile.HarvestSeason)
	 */
	public void addHarvestSeason(HarvestSeason harvestSeason) {

		farmerDAO.save(harvestSeason);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * editHarvestSeason (com.ese.entity .profile.HarvestSeason)
	 */
	public void editHarvestSeason(HarvestSeason harvestSeason) {

		farmerDAO.update(harvestSeason);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * removeHarvestSeason (com.ese.entity .profile.HarvestSeason)
	 */
	public void removeHarvestSeason(HarvestSeason harvestSeason) {

		farmerDAO.delete(harvestSeason);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findHarvestSeasonById(java.lang .Long)
	 */
	public HarvestSeason findHarvestSeasonById(Long id) {

		return farmerDAO.findHarvestSeasonById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#addHarvest(
	 * com.ese.entity.profile .HarvestData)
	 */
	public void addHarvest(HarvestData harvestData) {

		farmerDAO.save(harvestData);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#editHarvest
	 * (com.ese.entity.profile .HarvestData)
	 */
	public void editHarvest(HarvestData harvestData) {

		farmerDAO.update(harvestData);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#removeHarvest
	 * (com.ese.entity.profile .HarvestData)
	 */
	public void removeHarvest(HarvestData harvestData) {

		farmerDAO.delete(harvestData);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * findHarvestDataById (java.lang.Long)
	 */
	public HarvestData findHarvestDataById(Long id) {

		return farmerDAO.findHarvestDataById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * addFarmInventory (com.ese.entity .profile.FarmInventory)
	 */
	public void addFarmInventory(FarmInventory farmInventory) {

		farmerDAO.save(farmInventory);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findFarmInventoryById(java.lang .String)
	 */
	public FarmInventory findFarmInventoryById(String id) {

		return farmerDAO.findFarmInventoryById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * editFarmInventory (com.ese.entity .profile.FarmInventory)
	 */
	public void editFarmInventory(FarmInventory farmInventory) {

		farmerDAO.update(farmInventory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * removeFarmInventory (com.ese.entity .profile.FarmInventory)
	 */
	public void removeFarmInventory(FarmInventory farmInventory) {

		farmerDAO.delete(farmInventory);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * addFarmDetailedInfo (com.ese.entity .profile.FarmDetailedInfo)
	 */
	public void addFarmDetailedInfo(FarmDetailedInfo farmDetailedInfo) {

		farmerDAO.save(farmDetailedInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * removeFarmDetailedInfo(com.ese. entity.profile.FarmDetailedInfo)
	 */
	public void removeFarmDetailedInfo(FarmDetailedInfo farmDetailedInfo) {

		farmerDAO.delete(farmDetailedInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findFarmInventoryItem(long, int)
	 */
	public FarmInventory findFarmInventoryItem(long farmId, int inventoryItem) {

		return farmerDAO.findFarmInventoryItem(farmId, inventoryItem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * addAnimalHusbandary (com.ese.entity .profile.AnimalHusbandary)
	 */
	public void addAnimalHusbandary(AnimalHusbandary animalHusbandary) {

		farmerDAO.save(animalHusbandary);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findAnimalHusbandaryById(java.lang .String)
	 */
	public AnimalHusbandary findAnimalHusbandaryById(String id) {

		return farmerDAO.findAnimalHusbandaryById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * editAnimalHusbandary(com.ese.entity .profile.AnimalHusbandary)
	 */
	public void editAnimalHusbandary(AnimalHusbandary animalHusbandary) {

		farmerDAO.update(animalHusbandary);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * removeAnimalHusbandary(com.ese. entity.profile.AnimalHusbandary)
	 */
	public void removeAnimalHusbandary(AnimalHusbandary animalHusbandary) {

		farmerDAO.delete(animalHusbandary);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * isFarmMappingexist (long)
	 */
	public String isFarmMappingexist(long id) {

		boolean animalHusbandary = farmerDAO.isFarmMappingAnimalHusbandry(id);
		if (animalHusbandary) {
			return "animalHusbandry.mapped";
		}

		boolean farmInventory = farmerDAO.isFarmMappingFarmInventory(id);
		if (farmInventory) {
			return "farmInvemtory.mapped";
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listOfCertifiedFarmers()
	 */
	public List<Farmer> listOfCertifiedFarmers() {

		return farmerDAO.listOfCertifiedFarmers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listFarmerWithAccountByRevisionDate (java.util.Date)
	 */
	public List<Object[]> listFarmerWithAccountByRevisionDate(Date revisionDate) {

		return farmerDAO.listFarmerWithAccountByRevisionDate(revisionDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findActiveContractFarmersLatestRevisionNoBySeasonAndSamithi(long,
	 * java.lang.String)
	 */
	public Long findActiveContractFarmersLatestRevisionNoByAgentAndSeason(long agentId, String seasonCode) {

		return farmerDAO.findActiveContractFarmersLatestRevisionNoByAgentAndSeason(agentId, seasonCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * listActiveContractFarmersAccountByAgentAndSeason(long, java.util.Date)
	 */
	public List<Object[]> listActiveContractFarmersAccountByAgentAndSeason(long agentId, Date revisionDate) {

		List<Object[]> accountList = new ArrayList<Object[]>();
		Season season = findCurrentSeason();
		if (!ObjectUtil.isEmpty(season))
			accountList = farmerDAO.listActiveContractFarmersAccountByAgentAndSeason(agentId, season.getCode(),
					revisionDate);
		return accountList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IFarmerService#
	 * findInterestCalcConsolidatedByfarmerProfileId(java.lang.String)
	 */
	public InterestCalcConsolidated findInterestCalcConsolidatedByfarmerProfileId(String farmerProfileId) {

		// TODO Auto-generated method stub
		return farmerDAO.findInterestCalcConsolidatedByfarmerProfileId(farmerProfileId);

	}

	/**
	 * Calculate interest amount.
	 * 
	 * @param interestCalcConsolidated
	 *            the interest calc consolidated
	 * @param curRateOfInterest
	 *            the cur rate of interest
	 * @param eseSystem
	 *            the ese system
	 * @param calcType
	 *            the calc type
	 * @return the double
	 */
	public double calculateInterestAmount(InterestCalcConsolidated interestCalcConsolidated, double curRateOfInterest,
			ESESystem eseSystem, int calcType) {

		// TODO Auto-generated method stub
		boolean yearlyInterestCalcType = eseSystem.getPreferences().get(ESESystem.DAILY_INTEREST_CALC_TYPE).equals("0");// 0-static,1-dynamic
		if (curRateOfInterest > 0) {
			double interestPerYear = (interestCalcConsolidated.getAccumulatedPrincipalAmount()
					* (curRateOfInterest / 100));
			if (calcType == 0) {// Annual Interest
				return interestPerYear;
			} else if (calcType == 1) {// Monthly Interest
				return (interestPerYear / 12);
			} else if (calcType == 2) {// Daily Interest
				int noOfDays = 0;
				if (yearlyInterestCalcType) {// static
					noOfDays = Integer.parseInt(eseSystem.getPreferences().get(ESESystem.NO_OF_DAYS_PER_YEAR));
				} else {// dynamic
					noOfDays = DateUtil.noOfDaysInYear(interestCalcConsolidated.getLastCalcDate());
				}
				return (interestPerYear / Double.valueOf(noOfDays));
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * findCurrentRateOfInterest()
	 */
	public InterestRateHistory findCurrentRateOfInterest() {

		// TODO Auto-generated method stub
		InterestRateHistory interestRateHistory = preferencesService.findLastInterestRateHistory();
		if (!ObjectUtil.isEmpty(interestRateHistory)) {
			LocalDate effFrmDt = new DateTime(interestRateHistory.getRoiEffFrom()).toLocalDate();
			LocalDate toDayDate = new DateTime(new Date()).toLocalDate();
			if (effFrmDt.compareTo(toDayDate) <= 0) {
				return interestRateHistory;
			} else {
				interestRateHistory = preferencesService.findLastInactiveInterestRateHistory();
				if (!ObjectUtil.isEmpty(interestRateHistory)) {
					effFrmDt = new DateTime(interestRateHistory.getRoiEffFrom()).toLocalDate();
					toDayDate = new DateTime(new Date()).toLocalDate();
					if (effFrmDt.compareTo(toDayDate) <= 0) {
						return interestRateHistory;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Find current rate of interest for exist farmers.
	 * 
	 * @return the interest rate history
	 */
	public InterestRateHistory findCurrentRateOfInterestForExistFarmers() {

		// TODO Auto-generated method stub
		InterestRateHistory interestRateHistory = preferencesService.findLastInterestRateHistoryForExFarmers();
		if (!ObjectUtil.isEmpty(interestRateHistory)) {
			LocalDate effFrmDt = new DateTime(interestRateHistory.getRoiEffFrom()).toLocalDate();
			LocalDate toDayDate = new DateTime(new Date()).toLocalDate();
			if (effFrmDt.compareTo(toDayDate) <= 0) {
				return interestRateHistory;
			} else {
				interestRateHistory = preferencesService.findLastInactiveInterestRateHistoryForExFarmers();
				if (!ObjectUtil.isEmpty(interestRateHistory)) {
					effFrmDt = new DateTime(interestRateHistory.getRoiEffFrom()).toLocalDate();
					toDayDate = new DateTime(new Date()).toLocalDate();
					if (effFrmDt.compareTo(toDayDate) <= 0) {
						return interestRateHistory;
					}
				}
			}
		}
		return null;
	}

	/**
	 * List interest calc consolidated.
	 * 
	 * @return the list< interest calc consolidated>
	 */
	public List<InterestCalcConsolidated> listInterestCalcConsolidated() {

		return farmerDAO.list("from InterestCalcConsolidated");
	}

	/**
	 * List interest calc history by date.
	 * 
	 * @param formerId
	 *            the former id
	 * @param date
	 *            the date
	 * @return the list< interest calc history>
	 */
	public List<InterestCalcHistory> listInterestCalcHistoryByDate(String formerId, Date date) {

		return farmerDAO.listInterestCalcHistoryByDate(formerId, date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * processDailyInterestCalc()
	 */
	public void processDailyInterestCalc() {

		// TODO Auto-generated method stub
		ESESystem eseSystem = systemDAO.findPrefernceById("1");
		if (eseSystem.getPreferences().get(ESESystem.INTEREST_MODULE).equals("1")) {
			InterestRateHistory interestRateHistory = findCurrentRateOfInterest();
			List<InterestCalcConsolidated> interestCalcConsolidateds = listInterestCalcConsolidated();
			for (InterestCalcConsolidated interestCalcConsolidated : interestCalcConsolidateds) {
				double curRateOfInterest = 0;
				if (!ObjectUtil.isEmpty(interestRateHistory)) {
					curRateOfInterest = interestRateHistory.getRateOfInterest();
					if (interestRateHistory.getAffectExistingFarmerBal() == 0) {
						LocalDate effFrmDt = new DateTime(interestRateHistory.getRoiEffFrom()).toLocalDate();
						LocalDate createDate = new DateTime(interestCalcConsolidated.getCreateDt()).toLocalDate();
						if (effFrmDt.compareTo(createDate) > 0) {
							curRateOfInterest = interestCalcConsolidated.getCurrentRateOfInterest();
						}
					}
				}
				if (curRateOfInterest > 0) {
					double interestAmount = calculateInterestAmount(interestCalcConsolidated, curRateOfInterest,
							eseSystem, 2);
					Farmer farmer = findFarmerByFarmerId(interestCalcConsolidated.getFarmerProfileId());
					List<Contract> contracts = new ArrayList<Contract>(farmer.getContracts());
					if (!ObjectUtil.isListEmpty(contracts)) {
						if (interestCalcConsolidated.getAccumulatedPrincipalAmount() > 0) {
							Contract contract = contracts.get(0);
							ESEAccount account = contract.getAccount();

							Date lastCalcDate = interestCalcConsolidated.getLastCalcDate();

							List<Date> dates = DateUtil.listDatesBwTwoDates(lastCalcDate, new Date());
							dates.add(new Date());
							for (Date date : dates) {
								List<InterestCalcHistory> interestCalcHistories = listInterestCalcHistoryByDate(
										interestCalcConsolidated.getFarmerProfileId(), date);
								if (interestCalcHistories.size() == 0) {
									AgroTransaction agroTransaction = new AgroTransaction();

									agroTransaction.setIntBalance(account.getDistributionBalance());

									account.setDistributionBalance(account.getDistributionBalance() - interestAmount);

									interestCalcConsolidated.setCurrentRateOfInterest(curRateOfInterest);
									interestCalcConsolidated.setAccumulatedIntAmount(
											interestCalcConsolidated.getAccumulatedIntAmount() + interestAmount);
									interestCalcConsolidated.setLastCalcDate(date);
									interestCalcConsolidated.setLastUpdateDt(new Date());
									interestCalcConsolidated.setUpdateUserName("exec");
									interestCalcConsolidated.setRevisionNo(DateUtil.getRevisionNumber());

									InterestCalcHistory interestCalcHistory = new InterestCalcHistory();
									interestCalcHistory
											.setFarmerProfileId(interestCalcConsolidated.getFarmerProfileId());
									interestCalcHistory
											.setFarmerAccountRef(interestCalcConsolidated.getFarmerAccountRef());
									interestCalcHistory.setPrincipalAmount(
											interestCalcConsolidated.getAccumulatedPrincipalAmount());
									interestCalcHistory.setRateOfInterest(curRateOfInterest);
									interestCalcHistory.setInterestAmount(interestAmount);
									interestCalcHistory.setCalcStatus(1);
									interestCalcHistory.setCalcRemarks("Via Daily Interest Calculation Scheduler");
									interestCalcHistory.setTrxnRefId("");
									interestCalcHistory.setAccumulatedIntAmount(
											interestCalcConsolidated.getAccumulatedIntAmount());
									interestCalcHistory.setCalcDate(date);
									interestCalcHistory.setCreateDt(new Date());
									interestCalcHistory.setCreateUserName("exec");
									interestCalcHistory.setRevisionNo(DateUtil.getRevisionNumber());

									farmerDAO.update(account);
									farmerDAO.update(interestCalcConsolidated);
									farmerDAO.save(interestCalcHistory);

									agroTransaction.setReceiptNo("N/A");
									agroTransaction.setAgentId("N/A");
									agroTransaction.setAgentName("exec");
									agroTransaction.setDeviceId("N/A");
									agroTransaction.setDeviceName("N/A");
									agroTransaction.setServicePointId("N/A");
									agroTransaction.setServicePointName("N/A");
									agroTransaction.setFarmerId(farmer.getFarmerId());
									agroTransaction.setProfType("02");
									agroTransaction.setOperType(1);
									agroTransaction.setTxnType("334DI");
									agroTransaction.setTxnAmount(interestAmount);
									agroTransaction.setBalAmount(account.getDistributionBalance());
									agroTransaction.setTxnTime(new Date());
									agroTransaction.setTxnDesc("DISTRIBUTION INTEREST");
									agroTransaction.setAccount(account);
									agroTransaction.setSamithi(farmer.getSamithi());
									farmerDAO.save(agroTransaction);
								}
							}
						}
					}
				}
			}
			// }
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#removeImageInfo
	 * (com.ese.entity. profile.ImageInfo)
	 */
	public void removeImageInfo(ImageInfo imageInfo) {

		farmerDAO.delete(imageInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#updateImageInfo
	 * (com.ese.entity. profile.ImageInfo)
	 */
	public void updateImageInfo(ImageInfo imageInfo) {

		farmerDAO.update(imageInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * updateContractForFarmerAndHarvestDatas (com.ese.entity.profile.Farmer,
	 * java.util.Set, java.lang.String)
	 */
	public void updateContractForFarmerAndHarvestDatas(Farmer farmer, Set<HarvestData> harvestDatas,
			String createUserName) {

		farmerDAO.update(farmer);
		addHarvestData(harvestDatas);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * findFarmByfarmId(java.lang.String)
	 */
	public Farm findFarmByfarmId(String farmId) {

		return farmerDAO.findFarmByfarmId(farmId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * removeUnmappedFarmCropObject()
	 */
	public void removeUnmappedFarmCropObject() {

		farmerDAO.removeUnmappedFarmCropObject();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * listOfFarmersCount()
	 */
	public long listOfFarmersCount() {

		return farmerDAO.listOfFarmersCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#findFarmerList(
	 * )
	 */
	public List<Farmer> findFarmerList() {

		// TODO Auto-generated method stub
		return farmerDAO.findFarmerList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IFarmerService#addCashReceived
	 * (com.sourcetrace .eses.order.entity.txn.CashReceived)
	 */
	public void addCashReceived(CashReceived cashReceived) {

		farmerDAO.save(cashReceived);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IFarmerService#
	 * deleteelemetbyId(java.lang.Long)
	 */
	public void deleteelemetbyId(Long fileid) {

		farmerDAO.deleteelemetbyId(fileid);
	}

	public ESEAccount createContractFarmerESEAccount(String profileId, Farmer farmer, String accountNo,
			String branchId) {
		ESESystem eseSystem = preferencesService.findPrefernceById("1");
		ESEAccount account = new ESEAccount();
		account.setAccountNo(accountNo);
		account.setType(ESEAccount.CONTRACT_ACCOUNT);
		account.setAccountType(ESEAccount.SAVING_BANK_ACCOUNT);
		account.setStatus(ESEAccount.ACTIVE);
		account.setBranchId(branchId);
		account.setBalance(0.0);
		account.setDistributionBalance(0.0);
		account.setSavingAmount(0.0);
		account.setShareAmount(0.0);
		account.setAccountOpenDate(new Date());
		account.setCreateTime(new Date());
		account.setProfileId(profileId);
		account.setCashBalance(farmer.getAccountBalance());
		

		if (eseSystem.getPreferences().get(ESESystem.ENABLE_LOAN_MODULE).equals("1")) {
			account.setLoanAccountNo(idGenerator.getLoanAccountNoSeq());
			account.setLoanAmount(0.00);
			account.setOutstandingLoanAmount(0.00);
			
		}
		return account;
		
	}

	public void saveCultivation(Cultivation cultivation) {

		// TODO Auto-generated method stub
		farmerDAO.save(cultivation);
	}

	public List<FarmCatalogue> listFarmAnimalBasedOnType() {

		return farmerDAO.listFarmAnimalBasedOnType();
	}

	public FarmCrops findFarmCropsByFarmCode(Long farmId) {

		return farmerDAO.findFarmCropsByFarmCode(farmId);
	}

	public FarmDetailedInfo findTotalLandHoldingById(Long id) {
		return farmerDAO.findTotalLandHoldingById(id);
	}

	public List<String[]> listOfFarmerCode() {

		// TODO Auto-generated method stub

		return farmerDAO.listOfFarmerCode();
	}

	public List<String[]> listOfFarmerName() {

		// TODO Auto-generated method stub
		return farmerDAO.listOfFarmerName();
	}

	public List<Cultivation> findCostOfCultivationsByFarmerCode(String farmerId, String farmCode) {

		// TODO Auto-generated method stub
		return farmerDAO.findCostOfCultivationsByFarmerCode(farmerId, farmCode);

	}

	public List<CultivationDetail> findCultivationDetailsByCultivationId(long id) {

		// TODO Auto-generated method stub
		return farmerDAO.findCultivationDetailsByCultivationId(id);
	}

	public List<Object[]> findCultivationCost(String farmerId, String farmCode) {

		// TODO Auto-generated method stub
		return farmerDAO.findCultivationCost(farmerId, farmCode);
	}

	public Farmer findFarmerByFarmerCode(String farmerCode) {

		return farmerDAO.findFarmerByFarmerCode(farmerCode);
	}

	public Object findCottonIncomeByFarmerCode(String farmerCode, String farmCode) {

		// TODO Auto-generated method stub
		return farmerDAO.findCottonIncomeByFarmerCode(farmerCode, farmCode);
	}

	public List<String[]> listFarmerName() {

		// TODO Auto-generated method stub
		return farmerDAO.listFarmerName();
	}

	public List<String[]> listFatherName() {

		// TODO Auto-generated method stub
		return farmerDAO.listFatherName();
	}

	public List<String[]> listFarmerCode() {

		// TODO Auto-generated method stub
		return farmerDAO.listFarmerCode();
	}

	public double findCottonIncomeByFarmerCodeAndFarmCode(String farmerId, String farmCode) {

		// TODO Auto-generated method stub
		return farmerDAO.findCottonIncomeByFarmerCodeAndFarmCode(farmerId, farmCode);
	}

	public List<HarvestSeason> listHarvestSeasons() {

		return farmerDAO.listHarvestSeasons();
	}

	public HarvestSeason findHarvestSeasonByCode(String seasonCode) {

		return farmerDAO.findHarvestSeasonByCode(seasonCode);
	}

	public Farm findFarmByfarmId(Long farmId) {

		return farmerDAO.findFarmByfarmId(farmId);
	}

	public void addAnimalInventory(AnimalHusbandary animalHusbandary) {

		farmerDAO.save(animalHusbandary);

	}

	public void updateAnimalInventory(AnimalHusbandary animalHusbandary) {

		farmerDAO.updateAnimalInventory(animalHusbandary);
	}

	public List<CropHarvest> findCropHarvestByFarmCode(String farmCode) {

		return farmerDAO.findCropHarvestByFarmCode(farmCode);
	}

	public List<CropSupply> findCropSupplyByFarmCode(String farmCode) {

		return farmerDAO.findCropSupplyByFarmCode(farmCode);
	}

	public List<FarmCatalogue> listFarmEquipmentBasedOnType() {

		return farmerDAO.listFarmEquipmentBasedOnType();
	}

	public void updateFarmInventory(FarmInventory fm) {

		farmerDAO.updateFarmInventory(fm);

	}

	public FarmInventory findFarmInventoryItem(long farmId, String inventoryItem) {

		return farmerDAO.findFarmInventoryItem(farmId, inventoryItem);
	}

	public List<FarmInventory> listFarmInventryList(Long farmerId) {

		return farmerDAO.listFarmInventryList(farmerId);
	}

	public void removeFarmInventry(FarmInventory inventry) {

		farmerDAO.delete(inventry);

	}

	public Object findAnimalHusbandaryByFarmAndOtherItems(long farmId, long animalStr, String revenueStr,
			long housStr) {

		return farmerDAO.findAnimalHusbandaryByFarmAndOtherItems(farmId, animalStr, revenueStr, housStr);
	}

	public List<FarmCatalogue> listFodderBasedOnType() {

		return farmerDAO.listFodderBasedOnType();
	}

	public List<FarmCatalogue> listAnimalHousingBasedOnType() {

		return farmerDAO.listAnimalHousingBasedOnType();
	}

	public List<FarmCatalogue> listRevenueBasedOnType() {

		return farmerDAO.listRevenueBasedOnType();
	}

	public List<AnimalHusbandary> listAnimalHusbandaryList(Long farmId) {

		return farmerDAO.listAnimalHusbandaryList(farmId);
	}

	public byte[] findfarmerVerificationFarmerVoiceById(Long id) {

		return farmerDAO.findfarmerVerificationFarmerVoiceById(id);
	}

	@Override
	public CropSupply findcropSupplyId(String id) {

		// TODO Auto-generated method stub
		return farmerDAO.findcropSupplyId(id);
	}

	@Override
	public List<CropSupplyDetails> findCropSupplyDetailId(long id) {

		// TODO Auto-generated method stub
		return farmerDAO.findCropSupplyDetailId(id);
	}

	@Override
	public List<Farm> listFarmerFarmByFarmerId(String farmerId) {

		// TODO Auto-generated method stub
		return farmerDAO.listFarmerFarmByFarmerId(farmerId);
	}

	@Override
	public List<Farm> listfarmbyVillageId(Long id) {

		// TODO Auto-generated method stub
		return farmerDAO.listfarmbyVillageId(id);
	}

	@Override
	public List<Object[]> listPeriodicInspectionFarmer() {

		// TODO Auto-generated method stub
		return farmerDAO.listPeriodicInspectionFarmer();
	}

	@Override
	public List<FarmCrops> listOfCrops(long id) {

		// TODO Auto-generated method stub
		return farmerDAO.listOfCrops(id);
	}

	public List<Object[]> listPeriodicInspectionFarm() {

		return farmerDAO.listPeriodicInspectionFarm();
	}

	@Override
	public void removeCropSupply(CropSupply cropSupply) {

		// TODO Auto-generated method stub
		farmerDAO.delete(cropSupply);

	}

	@Override
	public List<Farmer> listOfFarmers(String selectedVillage) {

		// TODO Auto-generated method stub
		return farmerDAO.listOfFarmers(selectedVillage);
	}

	@Override
	public List<FarmCrops> listOfCropNames(String selectedCropType, long farmId) {

		// TODO Auto-generated method stub
		return farmerDAO.listOfCropNames(selectedCropType, farmId);
	}

	@Override
	public List<FarmCrops> listOfVariety(String selectedCropName) {

		// TODO Auto-generated method stub
		return farmerDAO.listOfVariety(selectedCropName);
	}

	@Override
	public List<ProcurementGrade> listOfGrade(String selectedVariety) {

		// TODO Auto-generated method stub
		return farmerDAO.listOfGrade(selectedVariety);
	}

	@Override
	public Double findGradePrice(String selectedGrade) {

		// TODO Auto-generated method stub
		return farmerDAO.findGradePrice(selectedGrade);
	}

	@Override
	public void saveCropSupply(CropSupply cropSupply) {

		// TODO Auto-generated method stub
		farmerDAO.save(cropSupply);

	}

	@Override
	public void saveCropHarvest(CropHarvest cropHarvest) {

		// TODO Auto-generated method stub
		farmerDAO.save(cropHarvest);
	}

	public List<String[]> listIcsName() {

		// TODO Auto-generated method stub
		return farmerDAO.listIcsName();
	}

	public List<Warehouse> listSamithiName() {

		// TODO Auto-generated method stub
		return farmerDAO.listSamithiName();
	}

	public List<Farm> listFarmName(String branchId) {

		// TODO Auto-generated method stub
		return farmerDAO.listFarmName(branchId);
	}

	@Override
	public List<BankInformation> findFarmerBankinfo(long id) {

		// TODO Auto-generated method stub
		return farmerDAO.findFarmerBankinfo(id);
	}

	@Override
	public void addBankInformation(BankInformation bankInformation) {

		farmerDAO.save(bankInformation);

	}

	@Override
	public long listOfFarmersCountBasedOnBranch(String branchIdValue) {

		return farmerDAO.listOfFarmersCountBasedOnBranch(branchIdValue);
	}

	public List<Object[]> listFertilizerAppliedAndPestAppliedWithQty(String farmCode, String cropCode) {

		return farmerDAO.listFertilizerAppliedAndPestAppliedWithQty(farmCode, cropCode);
	}

	@Override
	public Cultivation findByCultivationId(String id) {

		// TODO Auto-generated method stub
		return farmerDAO.findByCultivationId(id);
	}

	@Override
	public List<Object[]> listCropSupplyChartDetails() {

		return farmerDAO.listCropSupplyChartDetails();
	}

	public List<Object[]> listCropSupplyQtyChartDetails() {

		return farmerDAO.listCropSupplyQtyChartDetails();
	}

	public List<Object[]> listCropHarvestQtyChartDetails() {

		return farmerDAO.listCropHarvestQtyChartDetails();
	}

	@Override
	public List<Object[]> listCropHarvestBuyerChartDetails() {

		return farmerDAO.listCropHarvestBuyerChartDetails();
	}

	public List<FarmCatalogue> listCatelogueType(String type) {

		// TODO Auto-generated method stub
		return farmerDAO.listCatelogueType(type);
	}

	@Override
	public List<String[]> listFarmerId() {

		// TODO Auto-generated method stub
		return farmerDAO.listFarmerId();
	}

	@Override
	public Integer findFarmerCountByMonth(Date sDate, Date eDate) {

		return farmerDAO.findFarmerCountByMonth(sDate, eDate);
	}

	@Override
	public List<Object> listFarmerCountByGroup() {

		return farmerDAO.listFarmerCountByGroup();
	}

	@Override
	public List<FarmCatalogue> listMachinary() {

		return farmerDAO.listMachinary();
	}

	@Override
	public List<FarmCatalogue> listPolyHouse() {

		return farmerDAO.listPolyHouse();
	}

	@Override
	public FarmElement findFarmElementById(Long templateId) {

		return farmerDAO.findFarmElementById(templateId);
	}

	public void removeFarmElement(FarmElement farmElement) {

		farmerDAO.delete(farmElement);

	}

	@Override
	public List<FarmElement> listFarmElementList(Long farmId) {

		return farmerDAO.listFarmElementList(farmId);
	}

	@Override
	public FarmElement findFarmElementItem(Long farmId, String machStr) {

		return farmerDAO.findFarmElementItem(farmId, machStr);
	}

	@Override
	public void addFarmElement(FarmElement element) {

		farmerDAO.save(element);
	}

	@Override
	public void updateFarmElement(FarmElement element) {

		farmerDAO.updateFarmElement(element);

	}

	@Override
	public List<FarmElement> listMachinaryList(Long farmId, String machType) {

		return farmerDAO.listMachinaryList(farmId, machType);
	}

	@Override
	public MasterData findMasterDataIdByCode(String farmerId) {

		// TODO Auto-generated method stub
		return farmerDAO.findMasterDataIdByCode(farmerId);
	}

	public Integer findFarmersCount() {

		return farmerDAO.findFarmersCount();
	}

	@Override
	public FarmCrops findByFarmCropsId(Long cropId) {

		// TODO Auto-generated method stub
		return farmerDAO.findByFarmCropsId(cropId);
	}

	public Farmer findFarmerByFarmerId(String farmerId, String tenantId) {

		return farmerDAO.findFarmerByFarmerId(farmerId, tenantId);
	}

	public ESEAccount findAccountBySeassonProcurmentProductFarmer(long procurementProductId, long farmerId,
			String tenantId) {

		ESEAccount account = null;
		Season season = findCurrentSeason(tenantId);
		if (!ObjectUtil.isEmpty(season))
			account = farmerDAO.findAccountBySeassonProcurmentProductFarmer(season.getId(), procurementProductId,
					farmerId, tenantId);
		return account;
	}

	public Season findCurrentSeason(String tenantId) {

		ESESystem preference = systemDAO.findPrefernceById(ESESystem.SYSTEM_SWITCH, tenantId);
		String currentSeasonCode = preference.getPreferences().get(ESESystem.CURRENT_SEASON_CODE);
		if (!StringUtil.isEmpty(currentSeasonCode)) {
			Season currentSeason = productDistributionDAO.findSeasonBySeasonCode(currentSeasonCode, tenantId);
			return currentSeason;
		}
		return null;
	}

	@Override
	public List<Object[]> listFarmCropsByVillage(String selectedVillage, String branchId) {

		return farmerDAO.listFarmCropsByVillage(selectedVillage, branchId);
	}

	public List<Object[]> listFarmCropsByVillage(String selectedVillage, int startIndex, int limit, String branchId) {

		return farmerDAO.listFarmCropsByVillage(selectedVillage, startIndex, limit, branchId);
	}

	public List<Object[]> listFarmCropsDetailsByVillageCode(String selectedVillage, String branchId) {

		return farmerDAO.listFarmCropsDetailsByVillageCode(selectedVillage, branchId);
	}

	public List<Object[]> listFarmCropsDetailsByVillageCode(String selectedVillage, int startIndex, int limit,
			String branchId) {

		return farmerDAO.listFarmCropsDetailsByVillageCode(selectedVillage, startIndex, limit, branchId);
	}

	@Override
	public List<Object[]> listFarmCropsByCropCode(String selectedCropCode, int startIndex, int limit, String branchId) {

		return farmerDAO.listFarmCropsByCropCode(selectedCropCode, startIndex, limit, branchId);
	}

	@Override
	public List<Object[]> listFarmCropsByCropCode(String selectedCropCode, String branchId) {

		return farmerDAO.listFarmCropsByCropCode(selectedCropCode, branchId);
	}

	@Override
	public List<Object[]> listFarmCropsDetailsByCropCode(String selectedCropCode, String branchId) {

		return farmerDAO.listFarmCropsDetailsByCropCode(selectedCropCode, branchId);
	}

	@Override
	public List<Object[]> listFarmCropsDetailsByCropCode(String selectedCropCode, int startIndex, int limit,
			String branchId) {

		return farmerDAO.listFarmCropsDetailsByCropCode(selectedCropCode, startIndex, limit, branchId);
	}

	@Override
	public List<FarmCatalogue> listSeedTreatmentDetailsBasedOnType() {

		return farmerDAO.listSeedTreatmentDetailsBasedOnType();
	}

	@Override
	public FarmCatalogue findfarmcatalogueById(String farmCatalogueId) {

		// TODO Auto-generated method stub
		return farmerDAO.findfarmcatalogueById(farmCatalogueId);
	}

	@Override
	public void addImageInfo(ImageInfo imageInfo) {

		farmerDAO.save(imageInfo);

	}

	@Override
	public void updateFarmerImageInfo(long id, long imageInfoId) {

		farmerDAO.updateFarmerImageInfo(id, imageInfoId);

	}

	@Override
	public List<Object> listPeriodicInsoectionFatherName() {

		// TODO Auto-generated method stub
		return farmerDAO.listPeriodicInsoectionFatherName();
	}

	@Override
	public List<String[]> listByFatherNameList() {

		// TODO Auto-generated method stub
		return farmerDAO.listByFatherNameList();
	}

	@Override
	public List<Object[]> listFarmerCodeIdNameByVillageCode(String villageCode) {

		return farmerDAO.listFarmerCodeIdNameByVillageCode(villageCode);
	}

	@Override
	public List<Object[]> listSeasonCodeAndName() {

		return farmerDAO.listSeasonCodeAndName();
	}

	@Override
	public HarvestSeason findSeasonNameByCode(String seasonCode) {

		return farmerDAO.findSeasonNameByCode(seasonCode);
	}

	public List<Object[]> listHarvestSeasonFromToPeriod() {

		return farmerDAO.listHarvestSeasonFromToPeriod();
	}

	@Override
	public List<Object[]> listFarmerInfo() {

		return farmerDAO.listFarmerInfo();
	}

	@Override
	public List<FarmerSoilTesting> listFarmerSoilTestingByFarmId(String farmId) {

		return farmerDAO.listFarmerSoilTestingByFarmId(farmId);
	}

	public List<Object[]> listfarmingseasonlist() {

		return farmerDAO.listfarmingseasonlist();
	}

	public void addfarmerlanddetail(FarmerLandDetails farmlanddetail) {

		farmerDAO.save(farmlanddetail);
	}

	public String findHarvestSeasonBycodeusingname(String name) {

		return farmerDAO.findHarvestSeasonBycodeusingname(name);
	}

	public String findSanghamTypeFromWarehouseByWarehouseCode(String warehouseCode) {

		return farmerDAO.findSanghamTypeFromWarehouseByWarehouseCode(warehouseCode);
	}

	@Override
	public List<FarmerLandDetails> listFarmingSystemByFarmId(long id) {

		// TODO Auto-generated method stub
		return farmerDAO.listFarmingSystemByFarmId(id);
	}

	@Override
	public FarmerLandDetails findFarmerLandDetailsById(long id) {

		// TODO Auto-generated method stub
		return farmerDAO.findFarmerLandDetailsById(id);
	}

	@Override
	public void deleteFarmerLandDetailById(long farmerLandId) {

		// TODO Auto-generated method stub
		farmerDAO.deleteFarmerLandDetailById(farmerLandId);
	}

	@Override
	public List<FarmerSourceIncome> listFarmSourceIncomeByFarmerId(String farmerId) {

		return farmerDAO.listFarmSourceIncomeByFarmerId(farmerId);
	}

	@Override
	public List<FarmerIncomeDetails> listFarmerIncomeDetailsBySourceIncomeId(String id) {

		return farmerDAO.listFarmerIncomeDetailsBySourceIncomeId(id);
	}

	public void removeFarmerSourceIncome(FarmerSourceIncome farmerSourceIncome) {

		farmerDAO.delete(farmerSourceIncome);
		farmerDAO.deleteFarmerIncomeDetails("FARMER_INCOME_DETAILS", "FARMER_SOURCE_ID");
		// farmerDAO.removeBlindChilds("farmer_income_details",
		// "FARMER_SOURCE_ID");
	}

	@Override
	public void deleteFarmerIncomeDetails(String tableName, String columnName) {

		farmerDAO.deleteFarmerIncomeDetails(tableName, columnName);

	}

	/*
	 * public void deleteFarmerIncomeDetails(String id){
	 * farmerDAO.deleteFarmerIncomeDetails(id); }
	 */
	public void updateFarmerIdInFarmerSourceIncome(String sourceIncomeIds, String farmerId) {

		farmerDAO.updateFarmerIdInFarmerSourceIncome(sourceIncomeIds, farmerId);
	}

	@Override
	public List<Expenditure> listExpentitureListByFarmId(Long id) {

		// TODO Auto-generated method stub
		return farmerDAO.listExpentitureListByFarmId(id);
	}

	@Override
	public HarvestSeason findHarvestSeasosnByName(String name) {

		return farmerDAO.findHarvestSeasosnByName(name);
	}

	@Override
	public Warehouse findWareHouseByFarmerId(String farmerId) {

		return farmerDAO.findWareHouseByFarmerId(farmerId);
	}

	@Override
	public DocumentUpload findDocumentById(Long id) {

		return farmerDAO.findDocumentById(id);
	}

	@Override
	public String findBySeasonCode(String seasonCode) {

		// TODO Auto-generated method stub
		return farmerDAO.findBySeasonCode(seasonCode);
	}

	@Override
	public List<FarmCatalogue> listCatelogueTypeWithOther(String type) {

		return farmerDAO.listCatelogueTypeWithOther(type);
	}

	@Override
	public List<FarmerCropProdAnswers> listFarmerwithCategoryCode(String categoryCode) {

		// TODO Auto-generated method stub
		return farmerDAO.listFarmerwithCategoryCode(categoryCode);
	}

	@Override
	public List<FarmersQuestionAnswers> listQuestionBySection(long id) {

		// TODO Auto-generated method stub
		return farmerDAO.listQuestionBySection(id);
	}

	@Override
	public List<Object> listFarmerCountByBranch() {

		return farmerDAO.listFarmerCountByBranch();
	}

	@Override
	public List<Object> listTotalFarmAcreByBranch(String branchId) {

		return farmerDAO.listTotalFarmAcreByBranch(branchId);
	}

	@Override
	public List<Object> listSeedTypeCount() {

		return farmerDAO.listSeedTypeCount();
	}

	@Override
	public List<Object> listSeedTypeCountByBranch(String selectedSeedType, String selectedCrop,
			String selectedCooperative, String selectedGender) {
		return farmerDAO.listSeedTypeCountByBranch(selectedSeedType, selectedCrop, selectedCooperative, selectedGender);
	}

	@Override
	public List<Object> listSeedSourceCount() {

		return farmerDAO.listSeedSourceCount();
	}

	@Override
	public List<Object> listSeedSourceCountBySource(String selectedSeedSource) {

		return farmerDAO.listSeedSourceCountBySource(selectedSeedSource);
	}

	@Override
	public void deleteObject(Object object) {

		farmerDAO.delete(object);
	}

	@Override
	public Integer findFarmersCountByStatus() {

		// TODO Auto-generated method stub
		return farmerDAO.findFarmersCountByStatus();
	}

	@Override
	public List<Object[]> listFarmInfo() {

		return farmerDAO.listFarmInfo();
	}

	@Override
	public Object[] findFarmerAndFatherNameByFarmerId(String farmerId) {

		return farmerDAO.findFarmerAndFatherNameByFarmerId(farmerId);
	}

	public List<Farmer> listFarmerByFarmerId(long id) {

		// TODO Auto-generated method stub
		return farmerDAO.listFarmerByFarmerId(id);
	}

	@Override
	public List<Object> listFarmerCountByGroupAndBranch(String branchId, Integer selectedYear) {

		return farmerDAO.listFarmerCountByGroupAndBranch(branchId, selectedYear);
	}

	@Override
	public Integer findFarmersCountByBranch(String branchId) {

		return farmerDAO.findFarmersCountByBranch(branchId);
	}

	@Override
	public List<Object> findTotalFarmAcreByBranch(String selectedBranch) {

		return farmerDAO.findTotalFarmAcreByBranch(selectedBranch);
	}

	@Override
	public Long findInputCostByBranch(String branchId) {

		// TODO Auto-generated method stub
		return farmerDAO.findInputCostByBranch(branchId);
	}

	@Override
	public Long findInputCost() {

		return farmerDAO.findInputCost();
	}

	@Override
	public Integer findTotalCultivationProdLandByBranch(String selectedBranch, Integer selectedYear) {

		return farmerDAO.findTotalCultivationProdLandByBranch(selectedBranch, selectedYear);
	}

	/*
	 * @Override public Integer findTotalCultivationProdLand() {
	 * 
	 * return farmerDAO.findTotalCultivationProdLand(); }
	 */
	@Override
	public Long findTotalIncomeFromCottonByBranch(String selectedBranch, Integer selectedYear) {

		return farmerDAO.findTotalIncomeFromCottonByBranch(selectedBranch, selectedYear);
	}

	/*
	 * @Override public Long findTotalIncomeFromCotton() {
	 * 
	 * return farmerDAO.findTotalIncomeFromCotton(); }
	 */

	@Override
	public Long findCultivatedFarmersCount() {
		return farmerDAO.findCultivatedFarmersCount();
	}

	@Override
	public Long findCultivatedFarmersCountByBranch(String branchId) {
		return farmerDAO.findCultivatedFarmersCountByBranch(branchId);
	}

	@Override
	public List<PeriodicInspectionData> findPeriodicDataByType(String value, long id) {

		return farmerDAO.findPeriodicDataByType(value, id);
	}

	@Override
	public List<PeriodicInspectionSymptom> findPeriodicSymptomsByType(String value, long id) {

		return farmerDAO.findPeriodicSymptomsByType(value, id);
	}

	@Override
	public void editPeriodicInspection(PeriodicInspection existing) {

		farmerDAO.update(existing);

	}

	@Override
	public List<PeriodicInspectionData> listPestRecomentedById(Long inspId) {

		return farmerDAO.listPestRecomentedById(inspId);
	}

	@Override
	public List<PeriodicInspectionData> listFungicideById(Long inspId) {
		return farmerDAO.listFungicideById(inspId);
	}

	@Override
	public List<PeriodicInspectionData> listManureById(Long inspId) {
		return farmerDAO.listManureById(inspId);
	}

	@Override
	public List<PeriodicInspectionData> listFertilizerById(Long inspId) {

		return farmerDAO.listFertilizerById(inspId);
	}

	@Override
	public List<Object[]> listFarmerFarmInfo() {
		return farmerDAO.listFarmerFarmInfo();
	}

	@Override
	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm, String selectedOrganicStatus,
			String selectedFarmer) {
		return farmerDAO.listFarmerFarmInfoByVillageId(farm, selectedOrganicStatus, selectedFarmer);
	}

	@Override
	public void updateFarmerUtzStatus(String farmId, String certificationStatus) {
		// TODO Auto-generated method stub
		farmerDAO.updateFarmerUtzStatus(farmId, certificationStatus);
	}

	@Override
	public void updateSamithiUtzStatus(String farmerId, String certificationStatus) {
		// TODO Auto-generated method stub
		farmerDAO.updateSamithiUtzStatus(farmerId, certificationStatus);
	}

	@Override
	public void editFarmerStatus(long id, int statusCode, String statusMsg) {
		farmerDAO.editFarmerStatus(id, statusCode, statusMsg);

	}

	@Override
	public List<Object[]> listPeriodicInspectionVillage() {
		return farmerDAO.listPeriodicInspectionVillage();
	}

	@Override
	public int findLandPreparationDetailsByFarmCode(String farmCode, String seasonCode) {
		return farmerDAO.findLandPreparationDetailsByFarmCode(farmCode, seasonCode);
	}

	@Override
	public void updateFarmerRevisionNo(long farmerId, Long revisionNo) {
		farmerDAO.updateFarmerRevisionNo(farmerId, revisionNo);
	}

	@Override
	public PeriodicInspection findperiodicInspectionByBranchIdAndFarmId(String branchId, String farmId) {
		return farmerDAO.findperiodicInspectionByBranchIdAndFarmId(branchId, farmId);
	}

	@Override
	public int findWeedingStatusByCode(String branchId, String Code, String farmCode, String seasonCode) {
		return farmerDAO.findWeedingStatusByCode(branchId, Code, farmCode, seasonCode);
	}

	@Override
	public int findUsageCountforFertilizerFromCultivationDetails(String branchId, String Code, String farmCode, Long l,
			String seasonCode) {
		return farmerDAO.findUsageCountforFertilizerFromCultivationDetails(branchId, Code, farmCode, l, seasonCode);
	}

	@Override
	public void updateFarmerRevisionNoAndBasicInfo(long farmerId, Long revisionNo, int basicInfo) {
		farmerDAO.updateFarmerRevisionNoAndBasicInfo(farmerId, revisionNo, basicInfo);

	}

	@Override
	public int findPickingStatusByCode(String branchId, String Code, String farmCode, String seasonCode) {
		return farmerDAO.findPickingStatusByCode(branchId, Code, farmCode, seasonCode);
	}

	@Override
	public Farm findFarmByFarmerId(long farmerId) {

		return farmerDAO.findFarmByFarmerId(farmerId);
	}

	@Override
	public List<Object[]> listFarmByFarmerrId(String farmerId) {

		// TODO Auto-generated method stub
		return farmerDAO.listFarmByFarmerrId(farmerId);
	}

	@Override
	public void save(Object obj) {
		farmerDAO.save(obj);
	}

	@Override
	public List<Object[]> listFarmerInfoByCultivation(String branch) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerInfoByCultivation(branch);
	}

	@Override
	public List<Object[]> listFarmerInformationByCultivation() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerInformationByCultivation();
	}

	@Override
	public List<Object[]> listFarmerInfoByDistribution() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerInfoByDistribution();
	}

	@Override
	public List<Object[]> listFarmerInfoByProcurement() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerInfoByProcurement();
	}

	@Override
	public List<Object[]> listFarmerInfoByCropHarvest() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerInfoByCropHarvest();
	}

	@Override
	public List<Object[]> listFarmerInfoByCropSale() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerInfoByCropSale();
	}

	public ResearchStation findResearchStation(long id) {

		return farmerDAO.findResearchStation(id);
	}

	public void addResearchStation(ResearchStation researchStation) {
		researchStation.setRevisionNo(DateUtil.getRevisionNumber());

		farmerDAO.save(researchStation);

	}

	public void editResearchStation(ResearchStation researchStation) {

		researchStation.setRevisionNo(DateUtil.getRevisionNumber());
		farmerDAO.update(researchStation);

	}

	public void removeResearchStation(ResearchStation researchStation) {

		farmerDAO.delete(researchStation);

	}

	@Override
	public List<ResearchStation> listResearchStationByRevNo(Long revNo) {
		return farmerDAO.listResearchStationByRevNo(revNo);
	}

	@Override
	public List<CultivationDetail> findCultivationDetailsByCultivationIdAndSession(long id) {
		return farmerDAO.findCultivationDetailsByCultivationIdAndSession(id);
	}

	@Override
	public List<Cultivation> findCostOfCultivationsByFarmerCodeAndSeason(String farmerId, String farmCode,
			String seasonCode) {
		return farmerDAO.findCostOfCultivationsByFarmerCodeAndSeason(farmerId, farmCode, seasonCode);
	}

	@Override
	public List<Object[]> listFarmInfoByCropHarvest() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmInfoByCropHarvest();
	}

	@Override
	public List<Object[]> listOfVillageByCultivation() {
		return farmerDAO.listOfVillageByCultivation();
	}

	@Override
	public Integer findFarmsCountByBranch(String branchId) {

		// TODO Auto-generated method stub
		return farmerDAO.findFarmsCountByBranch(branchId);
	}

	@Override
	public Integer findFarmCountByMonth(Date sDate, Date eDate) {

		return farmerDAO.findFarmCountByMonth(sDate, eDate);
	}

	@Override
	public Integer findFarmsCount() {

		return farmerDAO.findFarmsCount();
	}

	@Override
	public Integer findFarmCropCountByMonth(Date sDate, Date eDate) {

		return farmerDAO.findFarmCropCountByMonth(sDate, eDate);
	}

	@Override
	public Integer findFarmsCropCountByBranch(String branchId) {

		return farmerDAO.findFarmsCropCountByBranch(branchId);
	}

	@Override
	public Integer findFarmCropCount() {

		return farmerDAO.findFarmCropCount();
	}

	@Override
	public String findFarmTotalLandAreaCount() {

		// TODO Auto-generated method stub
		return farmerDAO.findFarmTotalLandAreaCount();
	}

	@Override
	public Cow findCowByCowId(long id) {
		return farmerDAO.findCowByCowId(id);
	}

	@Override
	public List<CostFarming> listCostFarming() {
		return farmerDAO.listCostFarming();
	}

	@Override
	public ResearchStation findResearchStationId(String researchStationId) {
		// TODO Auto-generated method stub
		return farmerDAO.findResearchStationId(researchStationId);
	}

	@Override
	public void addCow(Cow cow) {
		// TODO Auto-generated method stub
		cow.setRevisionNo(DateUtil.getRevisionNumber());
		farmerDAO.save(cow);
	}

	@Override
	public HousingInfo findByHousingInfo(long farmId) {
		// TODO Auto-generated method stub
		return farmerDAO.findByHousingInfo(farmId);
	}

	@Override
	public Cow findByCowId(String cowId) {
		// TODO Auto-generated method stub
		return farmerDAO.findByCowId(cowId);
	}

	@Override
	public List<Calf> listOfCalfs(long id) {
		// TODO Auto-generated method stub
		return farmerDAO.listOfCalfs(id);
	}

	@Override
	public void updateCow(Cow cow) {
		// TODO Auto-generated method stub
		farmerDAO.saveOrUpdate(cow);
	}

	@Override
	public void removeCow(Cow cow) {
		// TODO Auto-generated method stub
		farmerDAO.delete(cow);
	}

	@Override
	public void addCostFarming(CostFarming costFarming) {
		// TODO Auto-generated method stub
		farmerDAO.save(costFarming);
	}

	@Override
	public void addCowInspection(CowInspection cowInspection) {
		// TODO Auto-generated method stub
		farmerDAO.save(cowInspection);
	}

	@Override
	public Date findByLastInspDate(String cowId) {
		// TODO Auto-generated method stub
		return farmerDAO.findByLastInspDate(cowId);
	}

	@Override
	public void addHousingInfo(HousingInfo housingInfo) {
		// TODO Auto-generated method stub
		farmerDAO.save(housingInfo);
	}

	@Override
	public HousingInfo findByHousingId(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findByHousingId(id);
	}

	@Override
	public void removeHousingInfo(HousingInfo housingInfo) {
		// TODO Auto-generated method stub
		farmerDAO.delete(housingInfo);
	}

	@Override
	public CowInspection findCowInspectionById(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findCowInspectionById(id);
	}

	@Override
	public List<Object[]> findByCowInspFarmer() {
		// TODO Auto-generated method stub
		return farmerDAO.findByCowInspFarmer();
	}

	@Override
	public List<Object[]> findByResearchStation() {
		// TODO Auto-generated method stub
		return farmerDAO.findByResearchStation();
	}

	@Override
	public List<String> findByCowList() {
		// TODO Auto-generated method stub
		return farmerDAO.findByCowList();
	}

	@Override
	public byte[] findCowInspectionCowVoiceById(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findCowInspectionCowVoiceById(id);
	}

	@Override
	public Integer findCowCount() {
		// TODO Auto-generated method stub
		return farmerDAO.findCowCount();
	}

	@Override
	public Integer findCowCountByMonth(Date firstDateOfMonth, Date date) {
		// TODO Auto-generated method stub
		return farmerDAO.findCowCountByMonth(firstDateOfMonth, date);
	}

	@Override
	public void updateResearchStatRevisionNo(long id, long revisionNumber) {
		// TODO Auto-generated method stub
		farmerDAO.updateResearchStatRevisionNo(id, revisionNumber);
	}

	@Override
	public List<Object[]> findFarmInfoByFarmerId(Long id) {
		return farmerDAO.findFarmInfoByFarmerId(id);
	}

	@Override
	public List<Object[]> findByResearchStationList() {
		// TODO Auto-generated method stub
		return farmerDAO.findByResearchStationList();
	}

	@Override
	public List<CostFarming> findByCowList(long cowId) {
		// TODO Auto-generated method stub
		return farmerDAO.findByCowList(cowId);
	}

	@Override
	public List<Object> findMilkingCountByCow(String isMilking) {
		// TODO Auto-generated method stub
		return farmerDAO.findMilkingCountByCow(isMilking);
	}

	@Override
	public List<Object> listCowCountByVillage() {
		// TODO Auto-generated method stub
		return farmerDAO.listCowCountByVillage();
	}

	@Override
	public List<Object> listCowCountByRS() {
		// TODO Auto-generated method stub
		return farmerDAO.listCowCountByRS();
	}

	@Override
	public List<Object> listCowCountByDiseaseName() {
		// TODO Auto-generated method stub
		return farmerDAO.listCowCountByDiseaseName();
	}

	@Override
	public List<Object> listTotalFarmAcreByVillage() {
		// TODO Auto-generated method stub
		return farmerDAO.listTotalFarmAcreByVillage();
	}

	@Override
	public List<Object> findByCowCost() {
		// TODO Auto-generated method stub
		return farmerDAO.findByCowCost();
	}

	@Override
	public List<CowInspection> findByCowList(String id) {
		// TODO Auto-generated method stub
		return farmerDAO.findByCowList(id);
	}

	@Override
	public Farm findFarmByFarmId(String farmId) {

		// TODO Auto-generated method stub
		return farmerDAO.findFarmByFarmId(farmId);
	}

	@Override
	public List<Object[]> listFarmFieldsByFarmerId(List<Long> farmerId) {

		return farmerDAO.listFarmFieldsByFarmerId(farmerId);
	}

	@Override
	public List<Object[]> listFarmCropsFieldsByFarmerId(Long farmId) {

		return farmerDAO.listFarmCropsFieldsByFarmerId(farmId);
	}

	@Override
	public List<Cow> listCowFieldsByFarmId(Long farmId) {
		return farmerDAO.listCowFieldsByFarmId(farmId);
	}

	@Override
	public List<Object[]> listFarmIcsByFarmId() {

		return farmerDAO.listFarmIcsByFarmId();
	}

	@Override
	public List<Object[]> listActiveContractFarmersFieldsBySeasonRevNoAndSamithi(long id, String currentSeasonCode,
			Long revisionNo) {
		return farmerDAO.listActiveContractFarmersFieldsBySeasonRevNoAndSamithi(id, currentSeasonCode, revisionNo);
	}

	public InterestCalcConsolidated findInterestCalcConsolidatedByfarmerProfileIdOpt(String farmerProfileId) {

		// TODO Auto-generated method stub
		return farmerDAO.findInterestCalcConsolidatedByfarmerProfileIdOpt(farmerProfileId);

	}

	@Override
	public long listOfFarmerCountBySamithiId(long id) {

		return farmerDAO.listOfFarmerCountBySamithiId(id);
	}

	public List<Object[]> findFarmCropProductList() {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmCropProductList();
	}

	@Override
	public Object[] findFarmFarmerAndYieldCount(String varietyCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmFarmerAndYieldCount(varietyCode);
	}

	@Override
	public List<Object[]> findSowingDateAndInterval(String vCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findSowingDateAndInterval(vCode);
	}

	public Integer findMinVarietyIntervalDays() {
		return farmerDAO.findMinVarietyIntervalDays();
	}

	@Override
	public List<Cultivation> findCOCByFarmerIdFarmIdCropCodeSeason(String farmerId, String farmId, String cropCode,
			String seasonCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findCOCByFarmerIdFarmIdCropCodeSeason(farmerId, farmId, cropCode, seasonCode);
	}

	@Override
	public List<Object[]> findCropProductBySeason(String seasonCode, String varietyCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findCropProductBySeason(seasonCode, varietyCode);
	}

	@Override
	public Object[] findFarmFarmerAndYieldCountBySeason(String seasonCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmFarmerAndYieldCountBySeason(seasonCode);
	}

	@Override
	public List<FarmCrops> listOfVarietyByCropTypeFarmCodeAndCrop(String selectedCropType, String selectedFarm,
			String selectedCropName) {
		return farmerDAO.listOfVarietyByCropTypeFarmCodeAndCrop(selectedCropType, selectedFarm, selectedCropName);
	}

	@Override
	public String findFarmTotalLandAreaCount(String BranchId) {
		return farmerDAO.findFarmTotalLandAreaCount(BranchId);
	}

	@Override
	public List<PeriodicInspectionData> listDiseaseById(Long inspId) {

		return farmerDAO.listDiseaseById(inspId);
	}

	public long findFarmerCropProdAnswerByFarmerIdAndCategoryCode(String farmerId, String code) {
		return farmerDAO.findFarmerCropProdAnswerByFarmerIdAndCategoryCode(farmerId, code);
	}

	@Override
	public List<Cultivation> listCultivationExpenses() {
		return farmerDAO.listCultivationExpenses();
	}

	@Override
	public List<Object[]> findFarmerCountWithPhotos(String branchId) {
		return farmerDAO.findFarmerCountWithPhotos(branchId);
	}

	@Override
	public List<Object[]> findFarmCountWithPhotos(String branchId) {
		return farmerDAO.findFarmCountWithPhotos(branchId);
	}

	@Override
	public List<Object[]> findFarmCountWithGPSTag(String branchId) {
		return farmerDAO.findFarmCountWithGPSTag(branchId);
	}

	@Override
	public List<Object> findFarmerCountByICconversion(String selectedBranch, Integer selectedYear,
			String selectedSeason, String selectedGender, String selectedState) {
		return farmerDAO.findFarmerCountByICconversion(selectedBranch, selectedYear, selectedSeason, selectedGender,
				selectedState);
	}

	@Override
	public List<Object> findTotalFarmAcreAndYieldByBranch(String selectedBranch, Integer selectedYear) {
		return farmerDAO.findTotalFarmAcreAndYieldByBranch(selectedBranch, selectedYear);
	}

	@Override
	public Integer findTotalFarmerCount(String selectedBranch, String selectedStaple, String selectedSeason,
			String selectedGender, String selectedState) {
		return farmerDAO.findTotalFarmerCount(selectedBranch, selectedStaple, selectedSeason, selectedGender,
				selectedState);
	}

	@Override
	public Integer findTotalFarmCount(String selectedBranch, Integer selectedYear) {
		return farmerDAO.findTotalFarmCount(selectedBranch, selectedYear);
	}

	@Override
	public List<Object[]> listOfCropNamesByCropTypeAndFarm(String selectedCropType, String selectedFarm) {
		// TODO Auto-generated method stub
		return farmerDAO.listOfCropNamesByCropTypeAndFarm(selectedCropType, selectedFarm);
	}

	public List<FarmerField> listRemoveFarmerFields() {
		return farmerDAO.listRemoveFarmerFields();
	}

	public List<FarmField> listRemoveFarmFields() {
		return farmerDAO.listRemoveFarmFields();
	}

	public List<Object[]> listFarmInfoByFarmerId(long id) {
		return farmerDAO.listFarmInfoByFarmerId(id);
	}

	@Override
	public List<Object[]> findCountOfFarmerEnrollment(List<String> agent) {
		// TODO Auto-generated method stub
		return farmerDAO.findCountOfFarmerEnrollment(agent);
	}

	@Override
	public List<FarmerField> findFarmerFieldByType(String type) {
		return farmerDAO.findFarmerFieldByType(type);
	}

	public List<FarmerField> listFarmerFields() {
		return farmerDAO.listFarmerFields();
	}

	@Override
	public Object[] findFarmerCodeNameVillageSamithibyFarmerId(String farmerId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerCodeNameVillageSamithibyFarmerId(farmerId);
	}

	@Override
	public List<Object[]> listFarmerInfoByProductReturn() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerInfoByProductReturn();
	}

	@Override
	public Warehouse findSamithiByFarmerId(String farmerId) {
		// TODO Auto-generated method stub
		return farmerDAO.findSamithiByFarmerId(farmerId);
	}

	@Override
	public List<Object> listTotalFarmAcreByFpo() {

		return farmerDAO.listTotalFarmAcreByFpo();
	}

	@Override
	public List<PMT> lisReceiptNumberList() {

		return farmerDAO.lisReceiptNumberList();
	}

	@Override
	public Cultivation findCultivationByCultivationId(long cultiId) {
		// TODO Auto-generated method stub
		return farmerDAO.findCultivationByCultivationId(cultiId);
	}

	@Override
	public ProcurementProduct findCropByCropCode(String cropCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findCropByCropCode(cropCode);
	}

	public void update(Object obj) {
		farmerDAO.update(obj);
	}

	@Override
	public List<Object[]> listFarmerIDAndNameByFarmerID(String farmerId) {
		return farmerDAO.listFarmerIDAndNameByFarmerID(farmerId);
	}

	@Override
	public List<FarmCrops> findFarmCropsByFarmerIdAndProcId(long id, long proId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmCropsByFarmerIdAndProcId(id, proId);
	}

	@Override
	public void updatePeriodicInspectionData(String farmId, String cropCode, String category, String type) {

		farmerDAO.updatePeriodicInspectionData(farmId, cropCode, category, type);

	}

	@Override
	public List<Object[]> listActiveContractFarmersFieldsBySeasonRevNoAndSamithi(long id, String currentSeasonCode,
			Long revisionNo, List<String> branch) {
		return farmerDAO.listActiveContractFarmersFieldsBySeasonRevNoAndSamithi(id, currentSeasonCode, revisionNo,
				branch);
	}

	/** DASHBOARD MIGRATION */
	@Override
	public Integer findTotalFarmerCountByStapleLength(String selectedBranch, int selectedYear,
			List<String> selectedStapleLen) {
		// TODO Auto-generated method stub

		return farmerDAO.findTotalFarmerCountByStapleLength(selectedBranch, selectedYear, selectedStapleLen);
	}

	@Override
	public List<String> findFarmerIdsByfarmCode(List<String> farmCodes) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerIdsByfarmCode(farmCodes);
	}

	@Override
	public List<Object> findTotalFarmAcreAndYieldAndHarvstByBranch(String selectedBranch, int selectedYear) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalFarmAcreAndYieldAndHarvstByBranch(selectedBranch, selectedYear);
	}

	@Override
	public String findTotalQtyByFarm(List<String> farmCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalQtyByFarm(farmCode);
	}

	@Override
	public Object[] listObjectCultivationExpenses(String selectedBranch, String selectedSeason, String selectedYear) {
		// TODO Auto-generated method stub
		return farmerDAO.listObjectCultivationExpenses(selectedBranch, selectedSeason, selectedYear);
	}

	@Override
	public Double findCottonIncByFarmerCode(List<String> farmerIds, List<String> farmCodes) {
		// TODO Auto-generated method stub
		return farmerDAO.findCottonIncByFarmerCode(farmerIds, farmCodes);
	}

	@Override
	public String findTotalLandByFarmCode(List<String> farmerIds, List<String> farmCodes) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalLandByFarmCode(farmerIds, farmCodes);
	}

	@Override
	public List<Object> findTotalFarmAcreAndYieldAndStapleByBranch(String selectedBranch, int parseInt,
			List<String> selectedStapleLen) {
		return farmerDAO.findTotalFarmAcreAndYieldAndStapleByBranch(selectedBranch, parseInt, selectedStapleLen);
	}

	@Override
	public List<String> findTotalFarmCodeByBranch(String selectedBranch, int selectedYear, String selectedSeason) {
		return farmerDAO.findTotalFarmCodeByBranch(selectedBranch, selectedYear, selectedSeason);
	}

	@Override
	public List<String> findTotalFarmCodeByBranch(String selectedBranch, int selectedYear, List<String> stapleCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalFarmCodeByBranch(selectedBranch, selectedYear, stapleCode);
	}

	@Override
	public String findTotalLandAcre(String selectedBranch, int selectedYear, String selectedSeason) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalLandAcre(selectedBranch, selectedYear, selectedSeason);
	}

	@Override
	public List<String> findFarmCodesByCultivation(String selectedBranch, String selectedSeason, String selectedYear) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmCodesByCultivation(selectedBranch, selectedSeason, selectedYear);
	}

	@Override
	public String findByFarmCode(String farmCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findByFarmCode(farmCode);
	}

	@Override
	public double findTotalCottonAreaCount() {

		return farmerDAO.findTotalCottonAreaCount();
	}

	@Override
	public double findTotalCottonAreaCountByMonth(Date sDate, Date eDate) {

		return farmerDAO.findTotalCottonAreaCountByMonth(sDate, eDate);
	}

	@Override
	public List<Object> findFarmerDetailsByStateAndCrop(String selectedBranch, Long selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerDetailsByStateAndCrop(selectedBranch, selectedState, selectedCrop,
				selectedCooperative, selectedGender);
	}

	@Override
	public Integer findFarmerCountByStateAndCrop(String selectedBranch, int selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerCountByStateAndCrop(selectedBranch, selectedState, selectedCrop, selectedCooperative,
				selectedGender);
	}

	@Override
	public void updateFarmerStatus(long id) {

		farmerDAO.updateFarmerStatus(id);

	}

	@Override
	public List<Object> listFarmerCountByGroupAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedCooperative, String selectedGender, String selectedCrop, String typez,
			String selectedVillage) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerCountByGroupAndBranchStateCoop(selectedBranch, selectedState, selectedGender,
				selectedCooperative, selectedCrop, typez, selectedVillage);
	}

	@Override
	public Farm findFarmByFarmCode(String farmId) {

		// TODO Auto-generated method stub
		return farmerDAO.findFarmByFarmCode(farmId);
	}

	@Override
	public FarmCatalogue findfarmcatalogueByCode(String code) {

		return farmerDAO.findfarmcatalogueByCode(code);
	}

	@Override
	public Double findTotalYieldByBranch(String selectedBranch, String selectedStaple, String selectedSeason,
			String selectedState) {
		return farmerDAO.findTotalYieldByBranch(selectedBranch, selectedStaple, selectedSeason, selectedState);
	}

	@Override
	public List<Object> listFarmerCountByFpoGroup() {

		return farmerDAO.listFarmerCountByFpoGroup();
	}

	@Override
	public Double findCocCostByCropBranchCooperativeAndGender(String branchId, String selectedCooperative,
			String selectedCrop, String selectedGender, String selectedYear, String selectedState) {

		// TODO Auto-generated method stub
		return farmerDAO.findCocCostByCropBranchCooperativeAndGender(branchId, selectedCooperative, selectedCrop,
				selectedGender, selectedYear, selectedState);
	}

	@Override
	public Integer findFarmerCountByState(String selectedBranch, Integer selectedState) {
		return farmerDAO.findFarmerCountByState(selectedBranch, selectedState);
	}

	@Override
	public List<Object> findTotalFarmAcreAndYieldByState(String selectedBranch, Long selectedState) {
		return farmerDAO.findTotalFarmAcreAndYieldByState(selectedBranch, selectedState);
	}

	@Override
	public Long findTotalIncomeFromCottonByState(String selectedBranch, String selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender) {
		return farmerDAO.findTotalIncomeFromCottonByState(selectedBranch, selectedState, selectedCrop,
				selectedCooperative, selectedGender);
	}

	@Override
	public Integer findTotalCultivationProdLandByState(String selectedBranch, String selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender) {
		return farmerDAO.findTotalCultivationProdLandByState(selectedBranch, selectedState, selectedCrop,
				selectedCooperative, selectedGender);
	}

	@Override
	public String findCropHarvestByYearAndBranch(String selectedBranch, Integer selectedYear) {
		return farmerDAO.findCropHarvestByYearAndBranch(selectedBranch, selectedYear);
	}

	@Override
	public Double findCropSupplyByYearAndBranch(String selectedBranch, Integer selectedYear) {
		return farmerDAO.findCropSupplyByYearAndBranch(selectedBranch, selectedYear);
	}

	@Override
	public List<Object> listSeedSourceCountBySource(String selectedBranch, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedVariety, String selectedState) {
		return farmerDAO.listSeedSourceCountBySource(selectedBranch, selectedCrop, selectedCooperative, selectedGender,
				selectedVariety, selectedState);
	}

	@Override
	public List<Object> listTotalFarmAcreByBranch() {
		return farmerDAO.listTotalFarmAcreByBranch();
	}

	@Override
	public List<Object[]> listFarmerFarmInfoByVillageIdImg(Long villageId, Long farmerId, Long farmId) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerFarmInfoByVillageIdImg(villageId, farmerId, farmId);
	}

	@Override
	public List<Object[]> findFarmerByVillageCodeAndProcurement(String villageCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerByVillageCodeAndProcurement(villageCode);
	}

	@Override
	public List<Object[]> findFarmerByFpo(String fpo) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerByFpo(fpo);
	}

	@Override
	public String findComponentNameByDynamicField(String compName) {
		// TODO Auto-generated method stub
		return farmerDAO.findComponentNameByDynamicField(compName);
	}

	@Override
	public void addDynamicComponents(DynamicFieldConfig dynamicFieldConfig) {
		farmerDAO.saveOrUpdate(dynamicFieldConfig);

		dynamicFieldConfig.setCode("F" + String.format("%04d", dynamicFieldConfig.getId()));
		farmerDAO.update(dynamicFieldConfig);
		for (LanguagePreferences preferences : dynamicFieldConfig.getLanguagePreferences()) {
			preferences.setCode(dynamicFieldConfig.getCode());
			if (preferences.getName().isEmpty()) {
				preferences.setName(dynamicFieldConfig.getComponentName());
			}

			farmerDAO.save(preferences);
		}

	}

	@Override
	public List<DynamicSectionConfig> findDynamicFieldsBySectionId(String sectionId) {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicFieldsBySectionId(sectionId);
	}

	@Override
	public Object[] findDynamicValueByFarmerId(String componentName, long farmerId) {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicValueByFarmerId(componentName, farmerId);
	}

	@Override
	public List<DynamicFieldConfig> listDynamicFields() {
		return farmerDAO.listDynamicFields();
	}

	@Override
	public List<DynamicSectionConfig> listDynamicSections() {
		return farmerDAO.listDynamicSections();
	}

	@Override
	public List<FarmerFeedbackEntity> findFamerFromFarmerFeedback() {
		// TODO Auto-generated method stub
		return farmerDAO.findFamerFromFarmerFeedback();
	}

	@Override
	public List<Object[]> listFarmerBySamithi(Long samithiIds) {
		return farmerDAO.listFarmerBySamithi(samithiIds);
	}

	@Override
	public AnimalHusbandary findAnimalHusbandaryByFarmerIdAndId(Long farmerId, Long animalId) {
		// TODO Auto-generated method stub
		return farmerDAO.findAnimalHusbandaryByFarmerIdAndId(farmerId, animalId);
	}

	@Override
	public FarmIcsConversion findFarmIcsConversionByFarmId(Long farmId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmIcsConversionByFarmId(farmId);
	}

	@Override
	public FarmCrops findFarmCropsByFarmCodeAndCategory(long farmId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmCropsByFarmCodeAndCategory(farmId);
	}

	@Override
	public List<Object[]> listCropsByFarmIdBasedOnCategory(Long farmId) {
		// TODO Auto-generated method stub
		return farmerDAO.listCropsByFarmIdBasedOnCategory(farmId);
	}

	@Override
	public Object[] findTotalQtyByCropHarvest(String selectedBranch, String selectedSeason, String selectedStaple,
			String selectedState) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalQtyByCropHarvest(selectedBranch, selectedSeason, selectedStaple, selectedState);
	}

	@Override
	public Integer findCropCount() {

		return farmerDAO.findCropCount();
	}

	@Override
	public Double findSaleCottonByCoc(List<String> farmerIds, List<String> farmCodes, String selectedSeason) {
		// TODO Auto-generated method stub
		return farmerDAO.findSaleCottonByCoc(farmerIds, farmCodes, selectedSeason);
	}

	@Override
	public String findTotalLandByPostHarvest(String selectedBranch, int selectedYear, String selectedSeason) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalLandByPostHarvest(selectedBranch, selectedYear, selectedSeason);
	}

	@Override
	public Long findFarmCountByFarmerId(Long farmerId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmCountByFarmerId(farmerId);
	}

	/** ENDD */

	@Override
	public List<Object[]> listFarmerIdAndName() {
		return farmerDAO.listFarmerIdAndName();
	}

	@Override
	public FarmerField findFarmerFieldByFieldName(String entityColumn) {
		return farmerDAO.findFarmerFieldByFieldName(entityColumn);
	}

	@Override
	public List<Cultivation> listCultivation() {
		return farmerDAO.listCultivation();
	}

	@Override
	public List<CropHarvest> listCropHarvest() {
		return farmerDAO.listCropHarvest();
	}

	@Override
	public List<Object[]> listFarmerByCooperativeCode(List<String> samithiIds) {
		return farmerDAO.listFarmerByCooperativeCode(samithiIds);
	}

	@Override
	public List<Object[]> findCocCostByFarmAndFarmer(String farmerId, String farmCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findCocCostByFarmAndFarmer(farmerId, farmCode);
	}

	@Override
	public Object findCottonIncomeByFarmerAndFarm(String farmerId, String farmCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findCottonIncomeByFarmerAndFarm(farmerId, farmCode);
	}

	@Override
	public List<Object[]> findFarmerByGroup(long fpo) {

		return farmerDAO.findFarmerByGroup(fpo);
	}

	@Override
	public MasterData findMasterDataIdById(long farmerId) {
		// TODO Auto-generated method stub
		return farmerDAO.findMasterDataIdById(farmerId);
	}

	@Override
	public MasterData findMasterDataIdByCode(String farmerId, String tenantId) {
		// TODO Auto-generated method stub
		return farmerDAO.findMasterDataIdByCode(farmerId, tenantId);
	}

	@Override
	public Farm findFarmByCode(String code, String tenantId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmByCode(code, tenantId);
	}

	@Override
	public Farmer findFarmerbyFarmerIdAndSeason(String farmerId, String season) {
		return farmerDAO.findFarmerbyFarmerIdAndSeason(farmerId, season);
	}

	@Override
	public List<Cultivation> listCultivationBySeason(String currentSeasonsCode) {
		return farmerDAO.listCultivationBySeason(currentSeasonsCode);
	}

	@Override
	public List<Object[]> findCultivationCostBySeason(String farmerId, String farmCode, String season) {
		return farmerDAO.findCultivationCostBySeason(farmerId, farmCode, season);
	}

	@Override
	public Farm findFarmByfarmIdAndSeason(String farmId, String cSeasonCode) {
		return farmerDAO.findFarmByfarmIdAndSeason(farmId, cSeasonCode);
	}

	@Override
	public Farm findFarmByFarmCodeAndSeason(String farmId, String season) {
		return farmerDAO.findFarmByfarmCodeAndSeason(farmId, season);
	}

	@Override
	public List<Object> listFarmerCountByGroupByType() {
		return farmerDAO.listFarmerCountByGroupByType();
	}

	@Override
	public Integer findFarmersCountByStatusByTypez() {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmersCountByStatusByTypez();
	}

	@Override
	public Farmer findFarmerById(Long id, String branchId) {
		return farmerDAO.findFarmerById(id, branchId);
	}

	@Override
	public List<Object[]> listQtyByUomAndType(String id, String uom) {
		// TODO Auto-generated method stub
		return farmerDAO.listQtyByUomAndType(id, uom);
	}

	@Override
	public List<Object[]> listUomType(String id) {
		// TODO Auto-generated method stub
		return farmerDAO.listUomType(id);
	}

	@Override
	public List<CultivationDetail> listCultivationDetail() {
		return farmerDAO.listCultivationDetail();
	}

	@Override
	public List<PeriodicInspection> listPeriodicInspection() {
		return farmerDAO.listPeriodicInspection();
	}

	public Farmer findFarmerByTraceId(String traceId) {

		return farmerDAO.findFarmerByTraceId(traceId);
	}

	@Override
	public List<Cultivation> findCOCByFarmerIdFarmIdSeason(String farmerId, String farmId, String seasonCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findCOCByFarmerIdFarmIdSeason(farmerId, farmId, seasonCode);
	}

	@Override
	public Map listActivityReport(String sord, String sidx, int startIndex, int limit, Date getsDate, Date geteDate,
			ViewFarmerActivity filter, int page, Object object) {
		Map data = new HashMap();
		List list = farmerDAO.listActivityReport(sord, sidx, startIndex, limit, getsDate, geteDate, filter, page,
				object);
		data.put("rows", list);
		data.put("records", farmerDAO
				.listActivityReport(sord, sidx, startIndex, 0, getsDate, geteDate, filter, page, object).size());
		data.put("pagenumber", page);

		return data;
	}

	@Override
	public String findCropNameByCropCode(String cropCode) {
		return farmerDAO.findCropNameByCropCode(cropCode);
	}

	@Override
	public List<Object[]> listFcpawithCategoryCode(String cropCode) {
		return farmerDAO.listFcpawithCategoryCode(cropCode);
	}

	@Override
	public List<Object> findICSFarmerCountByGroup(String selectedBranch, int selectedYear, String selectedSeason,
			int icsTyp) {
		// TODO Auto-generated method stub
		return farmerDAO.findICSFarmerCountByGroup(selectedBranch, selectedYear, selectedSeason, icsTyp);
	}

	@Override
	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByFarmerId(Long farmerId, String txnType) {
		return farmerDAO.listFarmerDynmaicFieldsByFarmerId(farmerId, txnType);
	}

	@Override
	public List<Object[]> ListOfFarmerHealthAssessmentByFarmerId() {
		// TODO Auto-generated method stub
		return farmerDAO.ListOfFarmerHealthAssessmentByFarmerId();
	}

	@Override
	public List<Object[]> ListOfFarmerSelfAssesmentByFarmerId() {
		// TODO Auto-generated method stub
		return farmerDAO.ListOfFarmerSelfAssesmentByFarmerId();
	}

	@Override
	public List<FarmerDynamicFieldsValue> listDynmaicFieldsInfo() {
		// TODO Auto-generated method stub
		return farmerDAO.listDynmaicFieldsInfo();
	}

	@Override
	public List<Object[]> listFarmCropsFieldsByFarmerId(List<Long> farmIds) {
		return farmerDAO.listFarmCropsFieldsByFarmerId(farmIds);
	}

	@Override
	public Integer findFarmerSowingCount(String selectedBranch, int selectedYear, String selectedSeason) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerSowingCount(selectedBranch, selectedYear, selectedSeason);

	}

	@Override
	public Integer findFarmerPreHarvestCount(String selectedBranch, int selectedYear, String selectedSeason,
			String selectedGender, String selectedState) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerPreHarvestCount(selectedBranch, selectedYear, selectedSeason, selectedGender,
				selectedState);
	}

	@Override
	public Integer findFarmerPostHarvestCount(String selectedBranch, int selectedYear, String selectedSeason,
			String selectedGender, String selectedState) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerPostHarvestCount(selectedBranch, selectedYear, selectedSeason, selectedGender,
				selectedState);
	}

	@Override
	public List<Object[]> findComponentListVal(String text) {
		// TODO Auto-generated method stub
		return farmerDAO.findComponentListVal(text);
	}

	@Override
	public int findMaxOrderNo() {
		// TODO Auto-generated method stub
		return farmerDAO.findMaxOrderNo();
	}

	@Override
	public String findSectionCodeByReferenceId(String listType) {
		// TODO Auto-generated method stub
		return farmerDAO.findSectionCodeByReferenceId(listType);
	}

	@Override
	public void updateFarmerStatusByFarmerId(String farmerId) {
		farmerDAO.updateFarmerStatusByFarmerId(farmerId);

	}

	@Override
	public Integer findFarmersCountByStatusAndSeason(String currentSeason) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmersCountByStatusAndSeason(currentSeason);
	}

	@Override
	public Object[] findTotalQtyAndAmt(String location, String farmerId, String product, String seasonCode,
			String stateName, String fpo, String icsName, String branchIdParma, String subBranchIdParma,
			String selectedFieldStaff, Date startDate, Date endDate, String branch) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalQtyAndAmt(location, farmerId, product, seasonCode, stateName, fpo, icsName,
				branchIdParma, subBranchIdParma, selectedFieldStaff, startDate, endDate, branch);
	}

	@Override
	public Object[] findTotalQty(String warehouse, String order, String selectedProduct, String vendor, Date startDate,
			Date endDate, String receipt, String seasonCode, String branchIdParma) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalQty(warehouse, order, selectedProduct, vendor, receipt, seasonCode, branchIdParma,
				startDate, endDate);
	}

	@Override
	public Object[] findTotalAmtAndweightByProcurement(String filterList, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalAmtAndweightByProcurement(filterList, params);
	}

	@Override
	public Object[] findTotalNoBagsAndNetWeg(String farmerId, String selectedCoOperative, String branchIdParma,
			String branch, String productId) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalNoBagsAndNetWeg(farmerId, selectedCoOperative, branchIdParma, branch, productId);
	}

	@Override
	public void updateDynamicFarmerFieldComponentType() {
		farmerDAO.updateDynamicFarmerFieldComponentType();
	}

	@Override
	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByRefId(String refId, String txnType) {
		return farmerDAO.listFarmerDynmaicFieldsByRefId(refId, txnType);
	}

	@Override
	public Object[] findTotalQtyAndAmt(String location, String farmerId, String product, String seasonCode,
			String stateName, String fpo, String icsName, String branchIdParma, String subBranchIdParma,
			String selectedFieldStaff) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> listIcNameByFarmer(String selectedFarmer) {
		return farmerDAO.listIcNameByFarmer(selectedFarmer);
	}

	public Object[] findTotalQtyInAgent(String location, String farmerId, String productId, String seasonCode,
			String stateName, String fpo, String icsName, String branchIdParma, String subBranchIdParma, String agentId,
			Date convertStringToDate, Date convertStringToDate2, String branch) {
		return farmerDAO.findTotalQtyInAgent(location, farmerId, productId, seasonCode, stateName, fpo, icsName,
				branchIdParma, subBranchIdParma, agentId, convertStringToDate, convertStringToDate2, branch);
	}

	public List<Object> findCropHavestQuantity(String farmerId, String seasonCode, String stateName, String farmCode,
			String crop, String icsName, String branchIdParma, Date convertStringToDate, Date convertStringToDate2,
			String agentId) {
		return farmerDAO.findCropHavestQuantity(farmerId, seasonCode, stateName, farmCode, crop, icsName, branchIdParma,
				convertStringToDate, convertStringToDate2, agentId);
	}

	public Object[] findCropSaleQtyAmt(String farmerId, String seasonCode, String buyerInfo, String stateName,
			String fpo, String icsName, String branchIdParma, Date convertStringToDate, Date convertStringToDate2,
			String agentId, String subBranchIdParam) {
		return farmerDAO.findCropSaleQtyAmt(farmerId, seasonCode, buyerInfo, stateName, fpo, icsName, branchIdParma,
				convertStringToDate, convertStringToDate2, agentId,subBranchIdParam);
	}

	@Override
	public List<Object[]> listVillageByGroup(long id) {
		return farmerDAO.listVillageByGroup(id);
	}

	@Override
	public List<Object[]> listIcsStatusByFarmer(String selectedFarmer) {
		return farmerDAO.listIcsStatusByFarmer(selectedFarmer);
	}

	@Override
	public List<Object[]> listICSByGroup(long id) {
		return farmerDAO.listICSByGroup(id);
	}

	@Override
	public List<Object[]> findDynamicSectionFieldsByTxnType(String txnType) {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicSectionFieldsByTxnType(txnType);
	}

	@Override
	public List<Object[]> findDynamicSectionFields() {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicSectionFields();
	}

	@Override
	public List<DynamicSectionConfig> listDynamicSectionByTxnType(String txnType) {
		return farmerDAO.listDynamicSectionByTxnType(txnType);
	}

	@Override
	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByTxnId(String selectedObject) {
		return farmerDAO.listFarmerDynmaicFieldsByTxnId(selectedObject);
	}

	@Override
	public FarmerDynamicData findFarmerDynamicData(String id) {
		return farmerDAO.findFarmerDynamicData(id);
	}

	@Override
	public Integer findFarmersCountFarmerLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmersCountFarmerLoca(selectedCrop, selectedState, selectedLocality, selectedTaluk,
				selectedVillage, selectedFarmer, selectedSeason, selectedOrganicStatus);
	}

	@Override
	public Object[] findTotalAcreAndEstYield(String selectedCrop, String selectedTaluk, String selectedVillage,
			String selectedSeason) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalAcreAndEstYield(selectedCrop, selectedTaluk, selectedVillage, selectedSeason);
	}

	@Override
	public Object[] findPeriodicInsDateByFarmCode(String farmCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findPeriodicInsDateByFarmCode(farmCode);
	}

	public List<java.lang.Object[]> listFarmFieldsByFarmerIdByAgentIdAndSeason(long id, Long revisionNo) {
		return farmerDAO.listFarmFieldsByFarmerIdByAgentIdAndSeason(id, revisionNo);
	}

	public List<java.lang.Object[]> listFarmCropsFieldsByFarmerIdByAgentIdAndSeason(long id, Long revisionNo) {
		return farmerDAO.listFarmCropsFieldsByFarmerIdByAgentIdAndSeason(id, revisionNo);

	}

	@Override
	public Farmer findFarmerByMsgNo(String msgNo) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerByMsgNo(msgNo);
	}

	public Long listFarmFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(long id) {
		return farmerDAO.listFarmFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(id);
	}

	public Long listFarmCropsFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(long id) {
		return farmerDAO.listFarmCropsFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(id);

	}

	@Override
	public List<Object[]> findTotalAreaProdByOrg(String selectedSeason, String selectedGender, String selectedState,
			String branchId) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalAreaProdByOrg(selectedSeason, selectedGender, selectedState, branchId);
	}

	@Override
	public List<Object[]> findTotalAreaProdByIcs(String selectedSeason) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalAreaProdByIcs(selectedSeason);
	}

	@Override
	public List<MasterData> listMasterType() {

		return farmerDAO.listMasterType();
	}

	@Override
	public GMO findGMOById(Long gmoId) {
		// TODO Auto-generated method stub
		return farmerDAO.findGMOById(gmoId);
	}

	@Override
	public void editGMO(GMO gmo) {
		// TODO Auto-generated method stub
		farmerDAO.update(gmo);
	}

	@Override
	public void removeGMO(GMO gmo) {
		// TODO Auto-generated method stub
		farmerDAO.delete(gmo);
	}

	@Override
	public void saveGMO(GMO gmo) {
		// TODO Auto-generated method stub
		farmerDAO.save(gmo);
	}

	@Override
	public int findMaxTypeId() {
		// TODO Auto-generated method stub
		return farmerDAO.findMaxTypeId();
	}

	@Override
	public GinnerQuantitySold findGinnerQtySoldById(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findGinnerQtySoldById(id);
	}

	@Override
	public void editGinnerQtySold(GinnerQuantitySold ginnerQtySold) {
		// TODO Auto-generated method stub
		farmerDAO.update(ginnerQtySold);
	}

	@Override
	public void removeGinnerQtySold(GinnerQuantitySold ginnerQtySold) {
		// TODO Auto-generated method stub
		farmerDAO.delete(ginnerQtySold);
	}

	@Override
	public void saveGinnerQtySold(GinnerQuantitySold ginnerQuantitySold) {
		// TODO Auto-generated method stub
		farmerDAO.save(ginnerQuantitySold);
	}

	@Override
	public List<Object[]> findTotalGinnerQty(String selectedSeason, String selectedBranch) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalGinnerQty(selectedSeason, selectedBranch);
	}

	@Override
	public List<Object[]> findGmoPercentage(String selectedSeason, String selectedBranch) {
		// TODO Auto-generated method stub
		return farmerDAO.findGmoPercentage(selectedSeason, selectedBranch);
	}

	@Override
	public void updateDynmaicImage(FarmerDynamicFieldsValue fdfv, String parentId) {
		farmerDAO.updateDynmaicImage(fdfv, parentId);

	}

	@Override
	public DynamicImageData findDynamicImageDataById(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicImageDataById(id);
	}

	@Override
	public List<Object> listOfICS() {
		return farmerDAO.listOfICS();
	}

	@Override
	public List<Object[]> listProcurementTraceabilityCity() {
		return farmerDAO.listProcurementTraceabilityCity();
	}

	@Override
	public List<Object[]> listProcurementTraceabilityVillage() {
		return farmerDAO.listProcurementTraceabilityVillage();
	}

	@Override
	public List<Object> listFarmerFpo() {
		return farmerDAO.listFarmerFpo();
	}

	public List<Object[]> listcooperativeByFarmer(String selectedFarmer) {
		return farmerDAO.listcooperativeByFarmer(selectedFarmer);
	}

	@Override
	public List<Object[]> findfpoByFarmerID(long id) {
		return farmerDAO.findfpoByFarmerID(id);
	}

	@Override
	public double findTotalStockInHeap(String gining, String procurementProduct, String ics, String heapDataName,
			String branchIdParma, String season) {
		return farmerDAO.findTotalStockInHeap(gining, procurementProduct, ics, heapDataName, branchIdParma, season);
	}

	@Override
	public List<Object[]> findFarmerDatasByFarmerID(long id) {
		return farmerDAO.findFarmerDatasByFarmerID(id);
	}

	@Override
	public List<DynamicFeildMenuConfig> listDynamicMenus() {
		return farmerDAO.listDynamicMenus();
	}

	@Override
	public List<FarmerDynamicFieldsValue> listFarmerDynamicFieldsValuePhotoByRefTxnType(String refId, String txnType) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerDynamicFieldsValuePhotoByRefTxnType(refId, txnType);
	}

	public void saveDynamicSection(DynamicSectionConfig dsc) {
		// TODO Auto-generated method stub
		farmerDAO.save(dsc);
	}

	public void updateDynamicSection(DynamicSectionConfig dsc) {
		// TODO Auto-generated method stub
		farmerDAO.update(dsc);
	}

	public void saveDynamicField(DynamicFieldConfig dfc) {
		// TODO Auto-generated method stub
		farmerDAO.save(dfc);
	}

	public void updateDynamicField(DynamicFieldConfig dfc) {
		// TODO Auto-generated method stub
		farmerDAO.update(dfc);
	}

	@Override
	public DynamicFieldConfig findDynamicFieldConfigById(Long id) {
		return farmerDAO.findDynamicFieldConfigById(id);
	}

	@Override
	public CottonPrice findProdPriceById(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findProdPriceById(id);
	}

	@Override
	public void editProdPrice(CottonPrice productPrice) {
		// TODO Auto-generated method stub
		farmerDAO.update(productPrice);
	}

	@Override
	public void saveCottonPrice(CottonPrice cottonPrice) {
		// TODO Auto-generated method stub
		farmerDAO.save(cottonPrice);
	}

	@Override
	public void removeCottonPrice(CottonPrice cottonPrice) {
		// TODO Auto-generated method stub
		farmerDAO.delete(cottonPrice);
	}

	@Override
	public String findMspByStapleLen(String selectedSeason, String selectedStapleLen) {
		// TODO Auto-generated method stub
		return farmerDAO.findMspByStapleLen(selectedSeason, selectedStapleLen);
	}

	@Override
	public GMO findGMOType(String type, String season) {
		// TODO Auto-generated method stub
		return farmerDAO.findGMOType(type, season);
	}

	@Override
	public CottonPrice findStapleByCottonPrice(String staple, String seasonCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findStapleByCottonPrice(staple, seasonCode);
	}

	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenusByType(String type) {
		return farmerDAO.findDynamicMenusByType(type);
	}

	@Override
	public List<Object> listFarmerFpoFromTraceabilityStock() {
		return farmerDAO.listFarmerFpoFromTraceabilityStock();
	}

	@Override
	public List<Object[]> listFarmerIDAndName() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerIDAndName();
	}

	@Override
	public List<Object[]> listFarmIDAndName() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmIDAndName();
	}

	@Override
	public void updateFarmICSStatusByFarmId(long farmId, String icsType) {
		farmerDAO.updateFarmICSStatusByFarmId(farmId, icsType);

	}

	public void addcropSupplyImages(CropSupplyImages cropSupplyImages) {
		farmerDAO.save(cropSupplyImages);
	}

	public List<CropSupplyImages> listCropSupplyImages(long id) {
		return farmerDAO.listCropSupplyImages(id);
	}

	public CropSupplyImages loadCropSupplyImages(Long id) {
		return farmerDAO.loadCropSupplyImages(id);
	}

	public List<DynamicFieldConfig> listDynamicFieldsByCodes(List<String> selectedSection) {
		return farmerDAO.listDynamicFieldsByCodes(selectedSection);
	}

	@Override
	public DynamicFeildMenuConfig findDynamicMenuConfigById(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicMenuConfigById(id);
	}

	@Override
	public void editDynamicFeildMenuConfig(DynamicFeildMenuConfig tempDynamicMenuConfig) {
		// TODO Auto-generated method stub
		farmerDAO.update(tempDynamicMenuConfig);
	}

	@Override
	public List<Object[]> findDynamicSectionByMenuId(Long menuId) {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicSectionByMenuId(menuId);
	}

	@Override
	public List<Object[]> findDynamicFieldsByMenuId(Long menuId) {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicFieldsByMenuId(menuId);
	}

	@Override
	public DynamicSectionConfig findDynamicSectionByName(String sectionName) {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicSectionByName(sectionName);
	}

	@Override
	public List<String> listOfDynamicSections() {
		// TODO Auto-generated method stub
		return farmerDAO.listOfDynamicSections();
	}

	@Override
	public List<Object[]> listOfDynamicFields(List<String> selectedSections) {
		// TODO Auto-generated method stub
		return farmerDAO.listOfDynamicFields(selectedSections);
	}

	@Override
	public List<String> listOfDynamicFields() {
		// TODO Auto-generated method stub
		return farmerDAO.listOfDynamicFields();
	}

	@Override
	public void removedynamicFeildMenuConfig(DynamicFeildMenuConfig dynamicFeildMenuConfig) {
		// TODO Auto-generated method stub
		farmerDAO.delete(dynamicFeildMenuConfig);
	}

	@Override
	public List<DynamicFieldConfig> listDynamicFieldsBySectionId(List<String> selectedSection) {
		// TODO Auto-generated method stub
		return farmerDAO.listDynamicFieldsBySectionId(selectedSection);
	}

	@Override
	public List<DynamicSectionConfig> listDynamicSectionsBySectionId(List<String> selectedSection) {
		// TODO Auto-generated method stub
		return farmerDAO.listDynamicSectionsBySectionId(selectedSection);
	}

	@Override
	public void addDynamicFeildMenuConfig(DynamicFeildMenuConfig dynamicFeildMenuConfig) {
		// TODO Auto-generated method stub
		farmerDAO.save(dynamicFeildMenuConfig);
		// dynamicFeildMenuConfig.setCode("M" + dynamicFeildMenuConfig.getId());
		// farmerDAO.update(dynamicFeildMenuConfig);
	}

	@Override
	public Object[] listFarmInfoByCode(String code) {
		return farmerDAO.listFarmInfoByCode(code);
	}

	@Override
	public List<DynamicFieldReportJoinMap> findDynamicFieldReportJoinMapByEntityAndTxn(String entity, String txnType) {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicFieldReportJoinMapByEntityAndTxn(entity, txnType);
	}

	@Override
	public List<DynamicFieldReportConfig> findDynamicFieldReportConfigByEntityAndTxn(String entity, String txnType) {
		return farmerDAO.findDynamicFieldReportConfigByEntityAndTxn(entity, txnType);
	}

	@Override
	public Farm findFarmByID(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmByID(id);
	}

	@Override
	public List<String> findFarmCodsByStapleLen(String selectedSeason, String selectedStapleLen, String branch) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmCodsByStapleLen(selectedSeason, selectedStapleLen, branch);
	}

	@Override
	public List<Object[]> findSalePriceByFarmCodes(List<String> farmCodes) {
		// TODO Auto-generated method stub
		return farmerDAO.findSalePriceByFarmCodes(farmCodes);
	}

	public List<Object[]> getParentMenus() {
		return farmerDAO.getParentMenus();
	}

	public List<Object[]> getAction() {
		return farmerDAO.getAction();
	}

	public List<Object[]> getRole() {
		return farmerDAO.getRole();
	}

	public List<Object[]> findSubMenusByParentMenuId(String parentMenuId) {
		return farmerDAO.findSubMenusByParentMenuId(parentMenuId);
	}

	public void delete_subMenu(Long id, String name) {
		farmerDAO.delete_subMenu(id, name);
	}

	public void save_subMenu(String parentId, String menuName, String menuDes, String menuUrl, String menuOrder,
			String ese_ent_name, String ese_action_actionId, String role_id) {
		farmerDAO.save_subMenu(parentId, menuName, menuDes, menuUrl, menuOrder, ese_ent_name, ese_action_actionId,
				role_id);
	}

	@Override
	public List<FarmCatalogue> listHeapData() {
		return farmerDAO.listHeapData();
	}

	@Override
	public List<Object> listFarmerCountByGroupAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedCooperative, String selectedGender, String typez) {
		return farmerDAO.listFarmerCountByGroupAndBranchStateCoop(selectedBranch, selectedState, selectedCooperative,
				selectedGender, typez);
	}

	@Override
	public Long findCropIdByFarmCode(String farmCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findCropIdByFarmCode(farmCode);
	}

	@Override
	public void removeFarmCoordinates(Long id) {
		// TODO Auto-generated method stub
		farmerDAO.removeFarmCoordinates(id);
	}

	@Override
	public Farm listOfFarmCoordinateByFarmId(long id) {
		// TODO Auto-generated method stub
		return farmerDAO.listOfFarmCoordinateByFarmId(id);
	}

	@Override
	public List<FarmCrops> listFarmCropsByFarmId(long farmId) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmCropsByFarmId(farmId);
	}

	@Override
	public List<Object[]> listFarmerBySamithiId(long id) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerBySamithiId(id);
	}

	@Override
	public void removeFarmCropCoordinates(Long id) {
		// TODO Auto-generated method stub
		farmerDAO.removeFarmCropCoordinates(id);
	}

	@Override
	public List<FarmerLocationMapField> listRemoveFarmerLocMapFields() {
		// TODO Auto-generated method stub
		return farmerDAO.listRemoveFarmerLocMapFields();
	}

	public void deleteField(DynamicFieldConfig dfc) {
		// TODO Auto-generated method stub
		farmerDAO.delete(dfc);
	}

	public void deleteListFields(Long id) {
		// TODO Auto-generated method stub
		farmerDAO.deleteListFields(id);
	}

	public DynamicSectionConfig findDynamicSectionConfigById(Long id) {
		return farmerDAO.findDynamicSectionConfigById(id);
	}

	public void deleteSection(DynamicSectionConfig dsc) {
		// TODO Auto-generated method stub
		farmerDAO.delete(dsc);
	}

	@Override
	public List<DynamicFieldConfig> getListComponent(String sectionCode) {
		// TODO Auto-generated method stub
		return farmerDAO.getListComponent(sectionCode);
	}

	public List<DynamicFieldConfig> getLitsComponentsBySectionCodeAndListId(String sectionCode, String id) {
		return farmerDAO.getLitsComponentsBySectionCodeAndListId(sectionCode, id);
	}

	public void updateListComponentsOrder(Long id) {
		farmerDAO.updateListComponentsOrder(id);
	}

	public void updateOrderForSubMenus(Long id, int order) {
		farmerDAO.updateOrderForSubMenus(id, order);
	}

	public List<Object[]> isAlreadyAvailableMenuByParentId(Long parentMenuId, String menuName) {
		return farmerDAO.isAlreadyAvailableMenuByParentId(parentMenuId, menuName);
	}

	public FarmerDynamicData findFarmerDynamicData(String txnType, String referenceId) {
		return farmerDAO.findFarmerDynamicData(txnType, referenceId);
	}

	FarmerDynamicData fdData = null;

	@Override
	public void saveOrUpdate(FarmerDynamicData farmerDynamicData, Map<String, List<FarmerDynamicFieldsValue>> fdMap,
			LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap) {
		fdData = farmerDynamicData;
		if (fdMap.isEmpty()) {
			fdMap = fdData.getFarmerDynamicFieldsValues().stream()
					.collect(Collectors.groupingBy(FarmerDynamicFieldsValue::getFieldName));
		}
		if (fdData.getIsScore() != null && (fdData.getIsScore() == 1 || fdData.getIsScore() == 2 || fdData.getIsScore() == 3)
				&& fdData.getScoreValue() != null && !fdData.getScoreValue().isEmpty()) {
			fdData = processScoreCalculation(fdData, fieldConfigMap);
		}
		List<FarmerDynamicFieldsValue> formulaLIst = farmerDAO.processFormula(fdData, fdMap, fieldConfigMap);
		formulaLIst.stream().filter(u -> (u.getIsUpdateProfile() != null && u.getIsUpdateProfile().equals("1")))
				.forEach(form -> {
					fdData.getProfileUpdateFields().put(form.getComponentType(), form.getFieldValue());
				});
		fdData.getFarmerDynamicFieldsValues().addAll(formulaLIst);
		if (fdData.getIsScore() != null && fdData.getIsScore() == 2) {
			fdData = processAuditCalcul(fdData, fieldConfigMap, fdMap, "");
		}else if (fdData.getIsScore() != null && fdData.getIsScore() == 3) {
			fdData = processAuditCalculSym(fdData, fieldConfigMap, fdMap, "");
		}
		fdData.setRevisionNo(DateUtil.getRevisionNumber());
		farmerDAO.saveOrUpdate(fdData);

		if (fdData.getProfileUpdateFields() != null && !ObjectUtil.isEmpty(fdData.getProfileUpdateFields())) {
			processProfileUpdates(fdData.getProfileUpdateFields(), fieldConfigMap, fdData);
		}

	}

	boolean updated = false;
	Integer val = 0;
	Method setterMethod;
	private FarmerDynamicData processAuditCalcul(FarmerDynamicData farmerDynamicData,
			Map<String, DynamicFieldConfig> fieldConfigMap, Map<String, List<FarmerDynamicFieldsValue>> fdMap,
			String auditedYear) {
		String prefs = preferencesService.findPrefernceByName("AUDIT_FIELD");
		if (farmerDynamicData.getEntityId().equals("1")) {
			
			if (prefs != null) {
				
				List<CertificateStandard> ld = certificationService.listCertificateStandardByCertificateCategoryId(6l);
				Farmer obj = (Farmer) getObjectWithEntity(prefs.split("#")[0].toString().trim(),
						farmerDynamicData.getEntityId(), farmerDynamicData.getReferenceId(), new HashMap<>());
			
				try {
					setterMethod = setterMethod(obj.getClass(), prefs.split("#")[1].toString().trim());
				} catch (Exception e1) {
					setterMethod = null;
				}
				Object objVal = ReflectUtil.getFieldValue(obj, prefs.split("#")[1].toString().trim());
				if(obj.getSamithi().getGroupFormationDate()!=null){
				if (obj.getCertificationType() == 2) {
					objVal = DateUtil.getCurrentYear() - Integer.valueOf(DateUtil.getYearByDateTime(obj.getSamithi().getGroupFormationDate()));
				} else {

					objVal = ReflectUtil.getFieldValue(obj, "certificateStandard");
				}
				val = 0;
				if (objVal instanceof CertificateStandard) {
					CertificateStandard standars = (CertificateStandard) objVal;
					val = Integer.valueOf(standars.getCode().split("_")[0].toString());
					val = val+1;
				} else if (objVal instanceof Integer) {
					val = (Integer) objVal;
					
				}

				updated = false;
				Map<String, CertificateStandard> ldMap = ld.stream()
						.filter(u -> u.getCode().startsWith(String.valueOf(val)))
						.collect(Collectors.toMap(u -> u.getCode().split("_")[1], u -> u));

				if (auditedYear!=null && !StringUtil.isEmpty(auditedYear)) {
					ldMap = ld.stream().filter(u -> u.getCode().startsWith(String.valueOf(auditedYear)))
							.collect(Collectors.toMap(u -> u.getCode().split("_")[1], u -> u));
				}

				ldMap.entrySet().stream().forEach(u -> {
					if (!updated && u.getValue().getCriteria() != null && !StringUtil.isEmpty(u.getValue().getCriteria())) {
						Integer result = validateAnswer(u.getValue().getCriteria(), fdMap, fieldConfigMap);
						if (result == 1) {
							try {
								
								if (setterMethod!=null) {
									if (prefs.split("#")[2].toString().trim().equals("Integer")) {
										setterMethod.invoke(obj, Integer.valueOf(u.getKey()));
									}
									if (farmerDynamicData.getEntityId().equals("1")) {
									Method	cersetterMethod = setterMethod(obj.getClass(), "certificateStandard");
									cersetterMethod.invoke(obj, u.getValue());
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							updated = true;
							farmerDynamicData.setTotalScore(Double.valueOf(u.getKey()));
							farmerDynamicData.setConversionStatus(String.valueOf(val));
						}
					} 
				});
				
				if (!updated  && ldMap.entrySet().stream().anyMatch(u-> StringUtil.isEmpty(u.getValue().getCriteria()))) {
					Entry<String, CertificateStandard> u = ldMap.entrySet().stream().filter(ss-> StringUtil.isEmpty(ss.getValue().getCriteria())).findFirst().get();
					if (prefs.split("#")[2].toString().trim().equals("Integer")) {
						try {
							setterMethod.invoke(obj, Integer.valueOf(u.getKey()));
							if (farmerDynamicData.getEntityId().equals("1")) {
								Method	cersetterMethod = setterMethod(obj.getClass(), "certificateStandard");
								cersetterMethod.invoke(obj, u.getValue());
								}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					updated = true;
					farmerDynamicData.setTotalScore(Double.valueOf(u.getKey()));
					farmerDynamicData.setConversionStatus(String.valueOf(val));
				}
					
				

			}
			if(farmerDynamicData.getTotalScore()==2){
			farmerDynamicData.setActStatus(1);	
			}
		
			}
		}
		return farmerDynamicData;
	}
	
	private FarmerDynamicData processAuditCalculSym(FarmerDynamicData farmerDynamicData,
			Map<String, DynamicFieldConfig> fieldConfigMap, Map<String, List<FarmerDynamicFieldsValue>> fdMap,
			String auditedYear) {
		
		if (farmerDynamicData.getEntityId().equals("1")) {
			String prefs = preferencesService.findPrefernceByName("AUDIT_FIELD");
			List<CertificateStandard> ld=null;
			if (prefs != null) {
				if(farmerDynamicData.getActStatus()!=null && farmerDynamicData.getActStatus()!=2){
					ld = certificationService.listCertificateStandardByCertificateCategoryId(7l);
				
				
				Farmer obj = (Farmer) getObjectWithEntity(prefs.split("#")[0].toString().trim(),
						farmerDynamicData.getEntityId(), farmerDynamicData.getReferenceId(), new HashMap<>());
			
				try {
					setterMethod = setterMethod(obj.getClass(), prefs.split("#")[1].toString().trim());
				} catch (Exception e1) {
					setterMethod = null;
				}
			
				val = 0;


				updated = false;
				Map<String, CertificateStandard> ldMap = ld.stream().collect(Collectors.toMap(u -> u.getCode().split("_")[1], u -> u));

				/*if (auditedYear!=null && !StringUtil.isEmpty(auditedYear)) {
					ldMap = ld.stream().filter(u -> u.getCode().contains(String.valueOf(auditedYear)))
							.collect(Collectors.toMap(u -> u.getCode().split("_")[1], u -> u));
				}*/

				ldMap.entrySet().stream().forEach(u -> {
					if (!updated && u.getValue().getCriteria() != null && !StringUtil.isEmpty(u.getValue().getCriteria())) {
						Integer result = validateAnswer(u.getValue().getCriteria(), fdMap, fieldConfigMap);
						if (result == 1) {
							try {
								
								if (setterMethod!=null) {
									if (prefs.split("#")[2].toString().trim().equals("Integer")) {
										setterMethod.invoke(obj, Integer.valueOf(u.getKey()));
									}
									if (farmerDynamicData.getEntityId().equals("1")) {
									Method	cersetterMethod = setterMethod(obj.getClass(), "certificateStandard");
									cersetterMethod.invoke(obj, u.getValue());
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							updated = true;
							farmerDynamicData.setTotalScore(Double.valueOf(u.getKey()));
							farmerDynamicData.setConversionStatus(String.valueOf(val));
							if(farmerDynamicData.getTxnType().equalsIgnoreCase("308")&&u.getKey().equalsIgnoreCase("0")){
							obj.setStatus(0);
							//obj.setStatusCode(2);
							
							}
							this.update(obj);
						}
					} 
				});
				
				if (!updated  && ldMap.entrySet().stream().anyMatch(u-> StringUtil.isEmpty(u.getValue().getCriteria()))) {
					Entry<String, CertificateStandard> u = ldMap.entrySet().stream().filter(ss-> StringUtil.isEmpty(ss.getValue().getCriteria())).findFirst().get();
					if (prefs.split("#")[2].toString().trim().equals("Integer")) {
						try {
							setterMethod.invoke(obj, Integer.valueOf(u.getKey()));
							if (farmerDynamicData.getEntityId().equals("1")) {
								Method	cersetterMethod = setterMethod(obj.getClass(), "certificateStandard");
								cersetterMethod.invoke(obj, u.getValue());
								}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					updated = true;
					farmerDynamicData.setTotalScore(Double.valueOf(u.getKey()));
					farmerDynamicData.setConversionStatus(String.valueOf(val));
				}
					
				

			//}
			/*if(farmerDynamicData.getTotalScore()==2){
			farmerDynamicData.setActStatus(1);	
			}*/
		
			}}
		} else if (farmerDynamicData.getEntityId().equals("2")) {
			String prefs = preferencesService.findPrefernceByName("FARM_AUDIT_FIELD");
				Farm f = findFarmByfarmId(Long.valueOf(farmerDynamicData.getReferenceId()));
				FarmerDynamicData fd = findFarmerDynamicDataByReferenceIdAndTxnType(f.getFarmer().getId());
				if(!ObjectUtil.isEmpty(fd)){
					Map<String, List<FarmerDynamicFieldsValue>> fdFarmerMap = fd.getFarmerDynamicFieldsValues().stream()
							.collect(Collectors.groupingBy(FarmerDynamicFieldsValue::getFieldName));
					fdMap.putAll(fdFarmerMap);
				}
				List<CertificateStandard> ld=null;
			if (prefs != null) {
				if(farmerDynamicData.getActStatus()!=null && farmerDynamicData.getActStatus()!=2){
				ld = certificationService.listCertificateStandardByCertificateCategoryId(8l);
				}else{
				ld = certificationService.listCertificateStandardByCertificateCategoryId(10l);
				}
				Farm obj = (Farm) getObjectWithEntity(prefs.split("#")[0].toString().trim(),
						farmerDynamicData.getEntityId(), farmerDynamicData.getReferenceId(), new HashMap<>());
			
				try {
					setterMethod = setterMethod(obj.getClass(), prefs.split("#")[1].toString().trim());
				} catch (Exception e1) {
					setterMethod = null;
				}
			
				val = 0;


				updated = false;
				Map<String, CertificateStandard> ldMap = ld.stream().collect(Collectors.toMap(u -> u.getCode().split("_")[1], u -> u));

				/*if (auditedYear!=null && !StringUtil.isEmpty(auditedYear)) {
					ldMap = ld.stream().filter(u -> u.getCode().contains(String.valueOf(auditedYear)))
							.collect(Collectors.toMap(u -> u.getCode().split("_")[1], u -> u));
				}*/

				ldMap.entrySet().stream().forEach(u -> {
					if (!updated && u.getValue().getCriteria() != null && !StringUtil.isEmpty(u.getValue().getCriteria())) {
						Integer result = validateAnswer(u.getValue().getCriteria(), fdMap, fieldConfigMap);
						if (result == 1) {
							try {
								
								if (setterMethod!=null) {
									if (prefs.split("#")[2].toString().trim().equals("Integer")) {
										setterMethod.invoke(obj, Integer.valueOf(u.getKey()));
									}
									if (farmerDynamicData.getEntityId().equals("1")) {
									Method	cersetterMethod = setterMethod(obj.getClass(), "certificateStandard");
									cersetterMethod.invoke(obj, u.getValue());
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							updated = true;
							farmerDynamicData.setTotalScore(Double.valueOf(u.getKey()));
							farmerDynamicData.setConversionStatus(String.valueOf(val));

							obj.setCertificateStandardLevel(Integer.valueOf(u.getKey()));
							if(u.getKey().equalsIgnoreCase("0")){
								//obj.getFarmer().setStatus(0);
								//obj.getFarmer().setStatusCode(2);
								obj.setStatus(0);
								obj.getFarmer().setCertificateStandardLevel(0);
							}else if(u.getKey().equalsIgnoreCase("1")){
								int size=obj.getFarmer().getFarms().stream().filter(fm-> fm.getCertificateStandardLevel()==0).collect(Collectors.toList()).size();
								//obj.setStatus(1);
								if(size>0){
								obj.getFarmer().setCertificateStandardLevel(0);
								}else{
									obj.getFarmer().setCertificateStandardLevel(1);
								}
							}
							else if(u.getKey().equalsIgnoreCase("2")){
								int size=obj.getFarmer().getFarms().stream().filter(fm-> fm.getCertificateStandardLevel()==0).collect(Collectors.toList()).size();
								int sizeInPg=obj.getFarmer().getFarms().stream().filter(fm->fm.getCertificateStandardLevel()==1).collect(Collectors.toList()).size();
								if(sizeInPg>0){
								obj.getFarmer().setCertificateStandardLevel(1);
								}else if(size>0){
									obj.getFarmer().setCertificateStandardLevel(0);
								}else{
									obj.getFarmer().setCertificateStandardLevel(2);
								}
							}
							obj.getFarmer().setRevisionNo(DateUtil.getRevisionNumber());
							obj.setRevisionNo(DateUtil.getRevisionNumber());
							this.update(obj.getFarmer());
							this.update(obj);
							
						}
					} 
				});
				
				if (!updated  && ldMap.entrySet().stream().anyMatch(u-> StringUtil.isEmpty(u.getValue().getCriteria()))) {
					Entry<String, CertificateStandard> u = ldMap.entrySet().stream().filter(ss-> StringUtil.isEmpty(ss.getValue().getCriteria())).findFirst().get();
					if (prefs.split("#")[2].toString().trim().equals("Integer")) {
						try {
							setterMethod.invoke(obj, Integer.valueOf(u.getKey()));
							if (farmerDynamicData.getEntityId().equals("1")) {
								Method	cersetterMethod = setterMethod(obj.getClass(), "certificateStandard");
								cersetterMethod.invoke(obj, u.getValue());
								}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					updated = true;
					farmerDynamicData.setTotalScore(Double.valueOf(u.getKey()));
					farmerDynamicData.setConversionStatus(String.valueOf(val));
				}
					
				

			//}
			/*if(farmerDynamicData.getTotalScore()==2){
			farmerDynamicData.setActStatus(1);	
			}*/
		
			}
		}
		return farmerDynamicData;
	}



	

	String formula;

	private Integer validateAnswer(String value, Map<String, List<FarmerDynamicFieldsValue>> fdMap,
			Map<String, DynamicFieldConfig> fieldConfigMap) {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");

		List<String> fieldLiust = new ArrayList<>();
		Matcher p = Pattern.compile("\\{(.*?)\\}").matcher(value);
		Matcher p1 = Pattern.compile("\\[(.*?)\\]").matcher(value);
		formula = value;

		while (p.find())
			fieldLiust.add(p.group(1));

		while (p1.find())
			fieldLiust.add(p1.group(1));

		fieldLiust.stream().forEach(uu -> {

			if (uu.trim().equals("APLAN")) {
				Long cnt = fdMap.values().stream().flatMap(u -> u.stream()).collect(Collectors.toList()).stream()
						.filter(u -> u.getActionPlan() != null
								&& !StringUtil.isEmpty(u.getActionPlan().getFieldValue()))
						.collect(Collectors.counting());
				formula = formula.replaceAll("\\{" + uu + "\\}", String.valueOf(cnt));
			} if (uu.trim().contains("APLAN~")) {
				String grade = uu.split("~")[1].toString();
				Long cnt = fdMap.values().stream().flatMap(u -> u.stream()).collect(Collectors.toList()).stream()
						.filter(u -> u.getGrade()!=null && u.getGrade().equals(grade))
						.collect(Collectors.counting());
				formula = formula.replaceAll("\\{" + uu + "\\}", String.valueOf(cnt));
			} 
			
 
			if (uu.contains("#")) {
				String grade = uu.split("#")[0].toString();
				Integer score = Integer.valueOf(uu.split("#")[1].toString());
				String operat ="";
				if(uu.split("#").length>=3){
				 operat = uu.split("#")[2].toString();
				}
				Long gradeCount = fieldConfigMap.values().stream()
						.filter(field -> field.getGrade() != null && field.getGrade().equals(grade))
						.collect(Collectors.counting());
				Long actulaVal = fdMap.values().stream().flatMap(ff -> ff.stream())
						.collect(Collectors.toList()).stream().filter(field -> field.getGrade() != null
								&& field.getGrade().equals(grade) && field.getScore() == score)
						.collect(Collectors.counting());
				if (operat!=null && operat.equals("%")) {
					if (actulaVal == 0) {
						formula = formula.replaceAll("\\[" + uu + "\\]", "0");
					} else {
						formula = formula.replaceAll("\\[" + uu + "\\]",String.valueOf((gradeCount / actulaVal) * 100 ));
					}

				}else if(operat!=null && operat.equals(">")){
					{
						formula = formula.replaceAll("\\[" + uu + "\\]",String.valueOf(actulaVal));
				}
					//formula = formula.replaceAll("\\[" + uu + "\\]",String.valueOf(actulaVal) );
				}
			} else {
				String valu = fdMap.containsKey(uu) ? fdMap.get(uu).get(0).getFieldValue() : "";
				if (StringUtil.isLong(valu) || StringUtil.isInteger(valu) || StringUtil.isDouble(valu)) {
					formula = formula.replaceAll("\\{" + uu + "\\}", valu);
				} else {
					formula = formula.replaceAll("\\{" + uu + "\\}", "\"" + valu + "\"");
				}
			}
		});

		try {
			if ((Boolean) engine.eval(formula)) {
				return 1;
			}
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		return 0;
	}

	Map<String, Map<String, String>> scoreMap = new HashMap<>();
	Map<String, Integer> scoreTypeMap = new HashMap<>();
	Double totalPerce = 0.0;
	List<FarmerDynamicFieldsValue> fdv = new ArrayList<>();
	Map<String, FarmerDynamicFieldsValue> fdMap = new HashMap<>();
boolean updatedS=false;
	private FarmerDynamicData processScoreCalculation(FarmerDynamicData farmerDynamicData,
			Map<String, DynamicFieldConfig> fieldConfigMap) {
		totalPerce = 0.0;
		scoreMap = farmerDynamicData.getScoreValue();
		fdv = new ArrayList<>();
		fdMap = new HashMap<>();
		farmerDynamicData.getFarmerDynamicFieldsValues().stream().filter(u -> scoreMap.containsKey(u.getFieldName()))
				.forEach(u -> {
					Map<String, String> smMap = scoreMap.get(u.getFieldName());
					updatedS=false;
					if(u.getPercentage()==null){
						u.setPercentage(0.0);
					}
					if (!updatedS) {
						if (Arrays.asList("6", "9").contains(u.getComponentType())) {

							smMap.entrySet().forEach(score -> {
								if (!updatedS) {
									// List<String> an =
									// Arrays.asList(u.getFieldValue().split(","));
									List<String> an = new ArrayList(Arrays.asList(u.getFieldValue().split(",")));
									if (score.getKey().contains("!")) {
										List<String> excluse = Arrays.asList(score.getKey().split("!")[1].split(","));
										String noOf = score.getKey().split("!")[0].toString();

										an.removeAll(excluse);
										if (StringUtil.isInteger(noOf)) {
											Integer noOfEl = Integer.valueOf(noOf);
											if (noOfEl == an.size()) {
												u.setScore(Integer.valueOf(score.getValue().split("~")[0].toString()));
												u.setPercentage(
														Double.valueOf(score.getValue().split("~")[1].toString()));
												updatedS=true;
												return;
											} else if (noOfEl == -1
													&& (getCatCount(u.getListMethod(), u.getAccessType())
															- excluse.size()) == an.size()) {
												u.setScore(Integer.valueOf(score.getValue().split("~")[0].toString()));
												u.setPercentage(
														Double.valueOf(score.getValue().split("~")[1].toString()));
												updatedS=true;
											}
										} else if (noOf.contains(",")) {
											if (Arrays.asList(noOf.split(","))
													.containsAll(Arrays.asList(u.getFieldValue().split(",")))
													&& noOf.split(",").length == u.getFieldValue().split(",").length) {
												u.setScore(Integer.valueOf(score.getValue().split("~")[0].toString()));
												u.setPercentage(
														Double.valueOf(score.getValue().split("~")[1].toString()));
												updatedS=true;
											}
										} else {
											if (noOf.equals(u.getFieldValue())) {
												u.setScore(Integer.valueOf(score.getValue().split("~")[0].toString()));
												u.setPercentage(
														Double.valueOf(score.getValue().split("~")[1].toString()));
												updatedS=true;
											}
										}

									} else {
										if (StringUtil.isInteger(score.getKey())) {
											Integer noOfEl = Integer.valueOf(score.getKey());
											if (noOfEl == an.size()) {
												u.setScore(Integer.valueOf(score.getValue().split("~")[0].toString()));
												u.setPercentage(
														Double.valueOf(score.getValue().split("~")[1].toString()));
												updatedS=true;
											} else if (noOfEl == -1
													&& (getCatCount(u.getListMethod(), u.getAccessType())) == an
															.size()) {
												u.setScore(Integer.valueOf(score.getValue().split("~")[0].toString()));
												u.setPercentage(
														Double.valueOf(score.getValue().split("~")[1].toString()));
												updatedS=true;
											}
										} else {
											if (Arrays.asList(score.getKey().split(","))
													.containsAll(Arrays.asList(u.getFieldValue().split(",")))
													&& score.getKey().split(",").length == u.getFieldValue()
															.split(",").length) {
												u.setScore(Integer.valueOf(score.getValue().split("~")[0].toString()));
												u.setPercentage(
														Double.valueOf(score.getValue().split("~")[1].toString()));
												updatedS=true;
											} else if (score.getKey().equals("-1")
													&& getCatCount(u.getListMethod(), u.getAccessType()) == Arrays
															.asList(u.getFieldValue().split(",")).size()) {
												u.setScore(Integer.valueOf(score.getValue().split("~")[0].toString()));
												u.setPercentage(
														Double.valueOf(score.getValue().split("~")[1].toString()));
												updatedS=true;
											}
										}
									}
							}
							});
							if (u.getPercentage() != null) {
								totalPerce = totalPerce + u.getPercentage();
							}
						} else {
							smMap.entrySet().forEach(score -> {
								if (!updatedS) {
									if (score.getKey().contains("#")) {
										/*
										 * format of the score
										 * /*CT001#f1~3~2.0,f2~2~10,f3~3-2.0
										 */
										String[] cond = score.getKey().split("#");
										String catCode = cond[0].toString();
										String[] fields = cond[1].toString().split(",");
										if (u.getFieldValue().equals(catCode.trim())) {
											Arrays.asList(fields).stream().forEach(field -> {
												String ff = field.split("~")[0];
												Integer sc = Integer.valueOf(field.split("~")[1].toString());
												Double per = Double.valueOf(field.split("~")[2].toString());
												FarmerDynamicFieldsValue farmerDynamicFieldsValue = new FarmerDynamicFieldsValue();
												farmerDynamicFieldsValue.setTxnType(farmerDynamicData.getTxnType());
												farmerDynamicFieldsValue.setFieldName(ff.trim());
												farmerDynamicFieldsValue
														.setReferenceId(farmerDynamicData.getReferenceId());
												farmerDynamicFieldsValue.setCreatedDate(new Date());
												farmerDynamicFieldsValue
														.setCreatedUser(farmerDynamicData.getCreatedUser());
												farmerDynamicFieldsValue
														.setTxnUniqueId(farmerDynamicData.getTxnUniqueId());
												farmerDynamicFieldsValue.setScore(sc);
												farmerDynamicFieldsValue.setPercentage(per);
												farmerDynamicFieldsValue.setAccessType(fieldConfigMap
														.get(farmerDynamicFieldsValue.getFieldName()) != null
																? fieldConfigMap
																		.get(farmerDynamicFieldsValue.getFieldName())
																		.getAccessType()
																: 0);
												farmerDynamicFieldsValue.setComponentType(fieldConfigMap
														.get(farmerDynamicFieldsValue.getFieldName()) != null
																? fieldConfigMap
																		.get(farmerDynamicFieldsValue.getFieldName())
																		.getComponentType()
																: "0");

												farmerDynamicFieldsValue.setListMethod(fieldConfigMap
														.get(farmerDynamicFieldsValue.getFieldName()) != null
														&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
																.getCatalogueType() != null
																		? fieldConfigMap
																				.get(farmerDynamicFieldsValue
																						.getFieldName())
																				.getCatalogueType()
																		: "");
												farmerDynamicFieldsValue.setParentId(fieldConfigMap
														.get(farmerDynamicFieldsValue.getFieldName()) != null
														&& fieldConfigMap.get(farmerDynamicFieldsValue.getFieldName())
																.getReferenceId() != null
																		? fieldConfigMap
																				.get(farmerDynamicFieldsValue
																						.getFieldName())
																				.getReferenceId()
																		: 0);

												farmerDynamicFieldsValue.setValidationType(fieldConfigMap
														.get(farmerDynamicFieldsValue.getFieldName()) != null
																? fieldConfigMap
																		.get(farmerDynamicFieldsValue.getFieldName())
																		.getValidation()
																: "0");
												farmerDynamicFieldsValue.setFarmerDynamicData(farmerDynamicData);
												farmerDynamicFieldsValue.setIsMobileAvail(fieldConfigMap
														.get(farmerDynamicFieldsValue.getFieldName()) != null
																? fieldConfigMap
																		.get(farmerDynamicFieldsValue.getFieldName())
																		.getIsMobileAvail()
																: "0");
												fdv.add(farmerDynamicFieldsValue);
												updatedS=true;
												if (u.getPercentage() != null) {
													totalPerce = totalPerce + per;
												}
											});
										}

									} /*
										 * else
										 * if(score.getKey().contains("\\|")){
										 * format of the score
										 * /*CT001|f1,f2,f3~-1~3-2.0 String[]
										 * cond =score.getKey().split("\\|");
										 * String catCode= cond[0].toString();
										 * if(u.getFieldValue().equals(catCode.
										 * trim())){ String[] fields =
										 * cond[1].toString().split("~")[0].
										 * split(","); String noOf =
										 * cond[1].toString().split("~")[1].
										 * toString(); String scPer =
										 * cond[1].toString().split("~")[2].
										 * toString(); List<Integer> ii = new
										 * ArrayList<>();
										 * Arrays.asList(fields).stream().
										 * forEach(field ->{
										 * if(fdMap.containsKey(field.trim())){
										 * ii.add(fdMap.get(field.trim()).
										 * getScore()); }else{ ii.add(0); } });
										 * 
										 * if(noOf!=null && noOf.equals("-1") &&
										 * ii.stream().filter(per ->
										 * per==3).count() ==fields.length ){
										 * Integer sc =
										 * Integer.valueOf(scPer.split("-")[0].
										 * toString()); Double per =
										 * Double.valueOf(scPer.split("-")[1].
										 * toString()); u.setScore(sc);
										 * u.setPercentage(per);
										 * 
										 * }else if(noOf!=null &&
										 * ii.stream().filter(per ->
										 * Arrays.asList(noOf.split(",")).
										 * contains(String.valueOf(per))).count(
										 * ) ==fields.length ){ Integer sc =
										 * Integer.valueOf(scPer.split("-")[0].
										 * toString()); Double per =
										 * Double.valueOf(scPer.split("-")[1].
										 * toString()); u.setScore(sc);
										 * u.setPercentage(per);
										 * 
										 * }else{ u.setScore(0);
										 * u.setPercentage(0.0);
										 * 
										 * } } if (u.getPercentage() != null) {
										 * totalPerce = totalPerce
										 * +u.getPercentage(); } }
										 */else if (score.getKey().equals(u.getFieldValue())) {
										u.setScore(Integer.valueOf(score.getValue().split("~")[0].toString()));
										u.setPercentage(Double.valueOf(score.getValue().split("~")[1].toString()));
										updatedS=true;
										if (u.getPercentage() != null) {
											totalPerce = totalPerce + u.getPercentage();
										}
									} else if (score.getKey().equals("-1") && getCatCount(u.getListMethod(),
											u.getAccessType()) == Arrays.asList(u.getFieldValue().split(",")).size()) {
										u.setScore(Integer.valueOf(score.getValue().split("~")[0].toString()));
										u.setPercentage(Double.valueOf(score.getValue().split("~")[1].toString()));
										if (u.getPercentage() != null) {
											totalPerce = totalPerce + u.getPercentage();
										}
										updatedS=true;
									}
							
								}
							});

						}
					} else {
						if (u.getPercentage() != null) {
							totalPerce = totalPerce + u.getPercentage();
						}
					}
					fdMap.put(u.getFieldName(), u);

				});
		farmerDynamicData.getFarmerDynamicFieldsValues().addAll(fdv);
		farmerDynamicData.setTotalScore(totalPerce);
		return farmerDynamicData;
	}

	public Integer getCatCount(String catType, Integer accessType) {
		if (accessType > 0 && accessType == 1) {
			return listCatelogueType(catType).size();
		} else if (accessType > 0 && accessType == 2) {
			return catType.split(",").length;
		} else if (accessType > 0 && accessType == 3) {
			LIST_METHOD[] methods = DynamicFieldConfig.LIST_METHOD.values();
			try {
				return getOptions(methods[Integer.valueOf(catType)].toString()).size();
			} catch (Exception e) {

			}
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getOptions(String methodName) {

		Map<String, String> returnMap = new LinkedHashMap<String, String>();
		try {
			Method method = this.getClass().getMethod(methodName);
			Object returnObj = method.invoke(this);
			if (!ObjectUtil.isEmpty(returnObj)) {
				returnMap = (Map<String, String>) returnObj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	@Override
	public void processCustomisedFormula(FarmerDynamicData farmerDynamicData,
			Map<String, DynamicFieldConfig> fieldConfigMap) {
		farmerDAO.processCustomisedFormula(farmerDynamicData, fieldConfigMap);

	}

	Integer count = 1;

	public void processProfileUpdates(Map<String, Object> profileUpdateFields,
			Map<String, DynamicFieldConfig> fieldConfigMap, FarmerDynamicData farmerDynamicData) {
		Map<Class, Object> profileObject = new HashMap<>();
		profileUpdateFields.entrySet().stream().forEach(u -> {
			DynamicFieldConfig dm = fieldConfigMap.get(u.getKey());
			if (dm.getComponentType().equals("15")) {
				count = 1;
				String area = ((String) u.getValue()).split("~")[1].toString();
				String coord = ((String) u.getValue()).split("~")[0].toString();
				if (farmerDynamicData.getEntityId().equals("2") || farmerDynamicData.getEntityId().equals("4")) {
					Farm fm = null;
					if (profileObject.containsKey(fm.getClass())) {
						fm = (Farm) profileObject.get(fm.getClass());
					} else {
						fm = findFarmById(Long.valueOf(farmerDynamicData.getReferenceId()));
					}
					Set<Coordinates> fcSet = new HashSet<>();
					Arrays.asList(coord.split("|")).stream().forEach(cp -> {
						Coordinates fcc = new Coordinates();
						fcc.setLatitude(cp.split(",")[0].trim().toString());
						fcc.setLongitude(cp.split(",")[1].trim().toString());
						fcc.setOrderNo(count);
						count++;
						fcSet.add(fcc);
					});
					fm.getFarmDetailedInfo().setTotalLandHolding(area);
					// fm.setCoordinates(fcSet);
					/*
					 * fm.getCoordinatesMap().stream().forEach(fc->{
					 * fc.setStatus(CoordinatesMap.Status.INACTIVE.ordinal());
					 * });
					 */
					CoordinatesMap co = new CoordinatesMap();
					co.setFarmCoordinates(fcSet);
					co.setAgentId(farmerDynamicData.getCreatedUser());
					co.setArea(fm.getFarmDetailedInfo().getTotalLandHolding());
					co.setDate(fm.getPhotoCaptureTime());
					co.setFarm(fm);
					co.setMidLatitude(fm.getLatitude());
					co.setMidLongitude(fm.getLongitude());
					co.setStatus(CoordinatesMap.Status.ACTIVE.ordinal());

					if (fm.getCoordinatesMap() != null && !ObjectUtil.isListEmpty(fm.getCoordinatesMap())) {
						fm.getCoordinatesMap().stream().forEach(c -> {
							co.setStatus(CoordinatesMap.Status.INACTIVE.ordinal());
						});
						fm.getCoordinatesMap().add(co);
					} else {
						Set<CoordinatesMap> coMap = new LinkedHashSet<>();
						coMap.add(co);
						fm.setCoordinatesMap(coMap);

					}
					fm.setActiveCoordinates(co);
					profileObject.put(fm.getClass(), fm);
					// editFarm(fm);
				} else if (farmerDynamicData.getEntityId().equals("6")) {
					FarmCrops fm = null;
					if (profileObject.containsKey(FarmCrops.class)) {
						fm = (FarmCrops) profileObject.get(fm.getClass());
					} else {
						fm = findFarmCropById(Long.valueOf(farmerDynamicData.getReferenceId()));
					}

					Set<FarmCropsCoordinates> fcSet = new HashSet<>();
					Arrays.asList(coord.split(Pattern.quote("|"))).stream().forEach(cp -> {
						FarmCropsCoordinates fcc = new FarmCropsCoordinates();
						fcc.setLatitude(cp.split(",")[0].trim().toString());
						fcc.setLongitude(cp.split(",")[1].trim().toString());
						fcc.setOrderNo(count);
						count++;
						fcSet.add(fcc);
					});
					fm.setCultiArea(area);
					fm.setFarmCropsCoordinates(fcSet);
					profileObject.put(fm.getClass(), fm);
				}
			} else if (dm.getComponentType().equals("12") && !StringUtil.isEmpty(dm.getProfileField())) {
				String profileField = dm.getProfileField();
				String entity = profileField.split("#")[0].trim().toString();
				String type = profileField.split("#")[1].trim().toString();
				String field = profileField.split("#")[2].trim().toString();
				if (type.equals("byte[]") && (field.contains("image") || field.contains("photo"))) {
					Object obj = getObjectWithEntity(entity, farmerDynamicData.getEntityId(),
							farmerDynamicData.getReferenceId(), profileObject);
					if (obj != null && field.contains("image") && obj instanceof Farmer) {
						byte[] photoContent = (byte[]) u.getValue();

						Farmer existingFarmer = (Farmer) obj;

						ImageInfo imageInfo = null;
						if (!StringUtil.isEmpty(photoContent) && photoContent.length > 0) {
							if (!ObjectUtil.isEmpty(existingFarmer.getImageInfo())) {
								imageInfo = existingFarmer.getImageInfo();
								Image photo = imageInfo.getPhoto();
								photo.setImage(photoContent);
								imageInfo.setPhoto(photo);
								updateImageInfo(imageInfo);
							} else {
								imageInfo = new ImageInfo();
								if (photoContent != null) {
									Image photo = new Image();
									photo.setImage(photoContent);
									photo.setImageId(existingFarmer.getFarmerId() + "-FP");
									imageInfo.setPhoto(photo);
									addImageInfo(imageInfo);
									existingFarmer.setImageInfo(imageInfo);
									profileObject.put(existingFarmer.getClass(), existingFarmer);
								}

							}
							if (!StringUtil.isEmpty(farmerDynamicData.getLatitude())) {
								existingFarmer.setLatitude(farmerDynamicData.getLatitude());
								existingFarmer.setLongitude(farmerDynamicData.getLongitude());
							}
						}
					} else if (obj != null && field.contains("image") && obj instanceof Farm) {
						byte[] photoContent = (byte[]) u.getValue();

						Farm existingFarmer = (Farm) obj;

						ImageInfo imageInfo = null;
						if (!StringUtil.isEmpty(photoContent) && photoContent.length > 0) {
							existingFarmer.setPhoto(photoContent);
							profileObject.put(existingFarmer.getClass(), existingFarmer);
							if (!StringUtil.isEmpty(farmerDynamicData.getLatitude())) {
								existingFarmer.setLatitude(farmerDynamicData.getLatitude());
								existingFarmer.setLongitude(farmerDynamicData.getLongitude());
							}
						}
					}
				}

			} else {
				String profileField = dm.getProfileField();
				String entity = profileField.split("#")[0].trim().toString();
				String type = profileField.split("#")[1].trim().toString();
				String field = profileField.split("#")[2].trim().toString();
				if (!StringUtil.isEmpty(u.getValue())) {
					Object obj = getObjectWithEntity(entity, farmerDynamicData.getEntityId(),
							farmerDynamicData.getReferenceId(), profileObject);
					if (obj != null) {
						Method setterMethod;
						try {
							setterMethod = setterMethod(obj.getClass(), field);
							if (type.equals("Integer")) {
								setterMethod.invoke(obj, Integer.valueOf(u.getValue().toString()));
							} else if (type.equals("Decimal")) {
								setterMethod.invoke(obj, Double.valueOf(u.getValue().toString()));
							} else {
								setterMethod.invoke(obj, u.getValue().toString());
							}
							//String revisionNo = null;
							try{
							Field dField = obj.getClass().getDeclaredField("revisionNo");
							if(dField!=null){
								setterMethod = setterMethod(obj.getClass(), "revisionNo");
								setterMethod.invoke(obj, DateUtil.getRevisionNumber());
								}
							}
							catch  (Exception e){
								
							}
						} catch (Exception e) {
							System.out.println("error in ocne qun");
						}

					}

				}
			}

		});
		profileObject.entrySet().forEach(u -> {
			farmerDAO.saveOrUpdate(u.getValue());
		});

	}

	@SuppressWarnings("unchecked")
	private Method setterMethod(Class entityClass, String propertyName) throws Exception {

		BeanInfo info = Introspector.getBeanInfo(entityClass);
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (PropertyDescriptor pd : props) {
			if (pd.getName().equals(propertyName))
				return pd.getWriteMethod();
		}
		return null;
	}

	private Method getterMethod(String property, Class class1) throws Exception {

		BeanInfo info = Introspector.getBeanInfo(class1);
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (PropertyDescriptor pd : props) {
			if (pd.getName().equals(property))
				return pd.getReadMethod();
		}
		return null;
	}

	public FarmCrops findFarmCropById(Long caseId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmCropById(caseId);
	}

	private Object getObjectWithEntity(String profileEnitiy, String entityId, String referenceId,
			Map<Class, Object> profileObject) {
		Object f = null;
		if (profileEnitiy.equals(entityId)) {

			if (entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARMER.ordinal()))) {
				if (profileObject.containsKey(Farmer.class)) {
					f = (Farmer) profileObject.get(Farmer.class);
				} else {
					f = findFarmerById(Long.valueOf(referenceId));
				}
			} else if (entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()))
					|| entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()))) {
				if (profileObject.containsKey(Farm.class)) {
					f = (Farm) profileObject.get(Farm.class);
				} else {
					f = findFarmByID(Long.valueOf(referenceId));
				}

			} else if (entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM_CROPS.ordinal()))) {
				if (profileObject.containsKey(FarmCrops.class)) {
					f = (FarmCrops) profileObject.get(FarmCrops.class);
				} else {
					f = findFarmCropById(Long.valueOf(referenceId));
				}

			} else if (entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.GROUP.ordinal()))) {
				if (profileObject.containsKey(Warehouse.class)) {
					f = (Warehouse) profileObject.get(Warehouse.class);
				} else {
					f = locationDAO.findSamithiById(Long.valueOf(referenceId));
				}
			}
			return f;
		} else {
			if (entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM_CROPS.ordinal()))) {
				if (profileEnitiy.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARMER.ordinal()))) {
					if (profileObject.containsKey(Farmer.class)) {
						f = (Farmer) profileObject.get(Farmer.class);
					} else {
						FarmCrops fm = farmerDAO.findFarmCropById(Long.valueOf(referenceId));
						f = fm.getFarm().getFarmer();
					}

				} else if (profileEnitiy.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()))
						|| profileEnitiy
								.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()))) {
					if (profileObject.containsKey(Farm.class)) {
						f = (Farm) profileObject.get(Farm.class);
					} else {
						FarmCrops fm = farmerDAO.findFarmCropById(Long.valueOf(referenceId));
						f = fm.getFarm();
					}

				}
			} else if (entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()))
					|| entityId.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()))) {
				if (profileEnitiy.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.FARM.ordinal()))
						|| profileEnitiy
								.equals(String.valueOf(DynamicFeildMenuConfig.EntityTypes.CERTIFICATION.ordinal()))) {
					if (profileObject.containsKey(Farmer.class)) {
						f = (Farmer) profileObject.get(Farmer.class);
					} else {
						Farm fm = findFarmById(Long.valueOf(referenceId));
						f = fm.getFarmer();
					}

				}
			}
		}

		return f;
	}

	@Override
	public FarmerDynamicData findFarmerDynamicDataBySeason(String txnType, String id, String season) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerDynamicDataBySeason(txnType, id, season);

	}

	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenusByMType(String txnType) {
		return farmerDAO.findDynamicMenusByMType(txnType);
	}

	@Override
	public void saveOrUpdate(FarmerDynamicData fyd) {
		farmerDAO.saveOrUpdate(fyd);
		farmerDAO.updateDynamicFarmerFieldComponentType();

	}

	@Override
	public void deleteChildObjects(String txnType) {
		farmerDAO.deleteChildObjects(txnType);

	}

	@Override
	public List<DynamicFieldReportConfig> findDynamicFieldReportConfigByEntity(String txnType) {
		return farmerDAO.findDynamicFieldReportConfigByEntity(txnType);

	}

	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenusByEntityType(String txnType) {
		return farmerDAO.findDynamicMenusByEntityType(txnType);

	}

	@Override
	public List<DynamicFieldReportJoinMap> findDynamicFieldReportJoinMapByEntity(String txnType) {
		return farmerDAO.findDynamicFieldReportJoinMapByEntity(txnType);

	}

	public List<Object[]> listOfFarmInfo() {
		return farmerDAO.listOfFarmInfo();
	}

	@Override
	public void addWeatherForeCast(WeatherForeCast foreCast) {
		// TODO Auto-generated method stub
		farmerDAO.addWeatherForeCast(foreCast);
	}

	@Override
	public List<Object[]> findLatAndLongByFarm() {
		// TODO Auto-generated method stub
		return farmerDAO.findLatAndLongByFarm();
	}

	@Override
	public String findLatAndLong(String lat, String lon) {
		// TODO Auto-generated method stub
		return farmerDAO.findLatAndLong(lat, lon);
	}

	@Override
	public List<WeatherForeCast> findForecastByCity(String selectedCity, List<String> daysList) {
		// TODO Auto-generated method stub
		return farmerDAO.findForecastByCity(selectedCity, daysList);
	}

	@Override
	public void removeForecastData() {
		// TODO Auto-generated method stub
		farmerDAO.removeForecastData();
	}

	@Override
	public Object[] findFarmInfoById(Long id) {
		return farmerDAO.findFarmInfoById(id);
	}

	@Override
	public Object[] findFarmerInfoById(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerInfoById(id);
	}

	@Override
	public List<Object[]> listActiveContractFarmersFieldsBySeasonRevNoAndSamithiWithGramp(long id,
			String currentSeasonCode, Long revisionNo, List<String> branch) {
		return farmerDAO.listActiveContractFarmersFieldsBySeasonRevNoAndSamithiWithGramp(id, currentSeasonCode,
				revisionNo, branch);
	}

	@Override
	public String findFarmIcsTypByFarmId(Long farmId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmIcsTypByFarmId(farmId);
	}

	@Override
	public String findCropNameByCropId(Long farmId) {
		// TODO Auto-generated method stub
		return farmerDAO.findCropNameByCropId(farmId);
	}

	@Override
	public boolean isPlottingExist(Long id) {
		return farmerDAO.isPlottingExist(id);
	}

	@Override
	public List<Object[]> listFarmListByFarmerId(Long farmerId) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmListByFarmerId(farmerId);
	}

	public List<DynamicFieldConfig> getFieldsListForFormula() {
		// TODO Auto-generated method stub
		return farmerDAO.getFieldsListForFormula();
	}

	@Override
	public List<Object[]> listFarmerAddressByFarmerId(String farmerId) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerAddressByFarmerId(farmerId);
	}

	public ForecastAdvisoryDetails findforeCastAdvisoryDetailsById(Long id) {
		return farmerDAO.findforeCastAdvisoryDetailsById(id);
	}

	public ForecastAdvisory findforeCastAdvisoryById(Long id) {
		return farmerDAO.findforeCastAdvisoryById(id);
	}

	@Override
	public List<Object[]> findAdvisoryList() {
		// TODO Auto-generated method stub
		return farmerDAO.findAdvisoryList();
	}

	@Override
	public List<Object[]> findForeCastList(Long cropId) {
		// TODO Auto-generated method stub
		return farmerDAO.findForeCastList(cropId);
	}

	@Override
	public void addForeCast(Forecast foreCast) {
		// TODO Auto-generated method stub
		farmerDAO.addForeCast(foreCast);
	}

	@Override
	public void removeForecast() {
		// TODO Auto-generated method stub
		farmerDAO.removeForecast();
	}

	public void updateDependencyKeyForDependentFieldsOfFormula(String dependentFieldCode, String formula_label_code) {
		farmerDAO.updateDependencyKeyForDependentFieldsOfFormula(dependentFieldCode, formula_label_code);
	}

	public List<DynamicFieldConfig> availableFieldsForList() {
		return farmerDAO.availableFieldsForList();
	}

	public void updateFieldsReferenceId(Long listId, List<Long> fieldsIdList) {
		farmerDAO.updateFieldsReferenceId(listId, fieldsIdList);
	}

	public void updateListButtonOrder(Long referenceId) {
		farmerDAO.updateListButtonOrder(referenceId);
	}

	@Override
	public List<Object[]> listFarmFieldsByFarmerId(long id) {
		return farmerDAO.listFarmFieldsByFarmerId(id);
	}

	@Override
	public List<Object[]> listCropNamesWithFarm() {
		return farmerDAO.listCropNamesWithFarm();
	}

	@Override
	public List<Object[]> findFarmerTraceDetailsByFarmCode(String farmCode, String tenantId) {
		return farmerDAO.findFarmerTraceDetailsByFarmCode(farmCode, tenantId);
	}

	@Override
	public Farmer findFarmerByID(Long id, String tenantId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerByID(id, tenantId);
	}

	public List<Farmer> findExistingFarmerByFarmerId(String farmerId) {
		return farmerDAO.findExistingFarmerByFarmerId(farmerId);
	}

	public void updateExistingFarmerFlagById(List<Farmer> existing_farmer) {
		// TODO Auto-generated method stub
		farmerDAO.updateExistingFarmerFlagById(existing_farmer);
	}

	@Override
	public List<Object[]> listDistributionBalanceDownload(long id, String strevisionNo) {
		return farmerDAO.listDistributionBalanceDownload(id, strevisionNo);
	}

	@Override
	public String findTotalAcreAndEstYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedSeason) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalAcreAndEstYield(selectedCrop, selectedState, selectedLocality, selectedTaluk,
				selectedVillage, selectedSeason);
	}

	@Override
	public DistributionBalance findDistributionBalanceItem(Long id, String procurementProduct) {
		// TODO Auto-generated method stub
		return farmerDAO.findDistributionBalanceItem(id, procurementProduct);
	}

	@Override
	public void updateDistributionBalance(DistributionBalance db) {
		// TODO Auto-generated method stub
		farmerDAO.updateDistributionBalance(db);
	}

	@Override
	public void addDistributionBalance(DistributionBalance distBal) {
		// TODO Auto-generated method stub
		farmerDAO.save(distBal);
	}

	@Override
	public List<DistributionBalance> listDistributionBalanceList(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.listDistributionBalanceList(id);
	}

	@Override
	public DistributionBalance findDistributionBalanceById(long templateId) {
		// TODO Auto-generated method stub
		return farmerDAO.findDistributionBalanceById(templateId);
	}

	@Override
	public void removeDistributionBalance(DistributionBalance distBal) {
		// TODO Auto-generated method stub
		farmerDAO.delete(distBal);
	}

	@Override
	public void editDistributionBalance(DistributionBalance distBal) {
		// TODO Auto-generated method stub
		farmerDAO.update(distBal);
	}

	@Override
	public FarmIcsConversion findFarmIcsConversionByFarmIdWithActive(Long farmId) {
		return farmerDAO.findFarmIcsConversionByFarmIdWithActive(farmId);
	}

	@Override
	public FarmIcsConversion findFarmIcsConversionByFarmIdAndInspectionTypeAndScopeOperationWithInspectionDate(
			long selectedFarm, String insType, String scope, int inspectionDateYear) {
		return farmerDAO.findFarmIcsConversionByFarmIdAndInspectionTypeAndScopeOperationWithInspectionDate(selectedFarm,
				insType, scope, inspectionDateYear);
	}

	@Override
	public void updateFarmICSStatusByFarmIdInsTypeAndIcsType(Long farmId, String insType, String icsType) {
		farmerDAO.updateFarmICSStatusByFarmIdInsTypeAndIcsType(farmId, insType, icsType);

	}

	@Override
	public void updateFarmIcsConversionByFarmId(long farmId, FarmIcsConversion existing) {
		farmerDAO.updateFarmIcsConversionByFarmId(farmId);

	}

	@Override
	public FarmIcsConversion findFarmIcsConversionById(Long id) {
		return farmerDAO.findFarmIcsConversionById(id);
	}

	@Override
	public void saveFarmIcsConversionByFarmId(long farmId, FarmIcsConversion farmIcsconversion) {
		farmerDAO.updateFarmIcsConversionByFarmId(farmId);
		farmerDAO.save(farmIcsconversion);

	}

	public List<Object[]> listCultivationByFarmerIncome(String branchId) {
		return farmerDAO.listCultivationByFarmerIncome(branchId);
	}

	@Override
	public void insertDistBalance(String stringCellValue, String string, Double valueOf) {
		farmerDAO.insertDistBalance(stringCellValue, string, valueOf);

	}

	public List<Farmer> listFarmerByFarmerIdByIdList(List<String> id) {
		return farmerDAO.listFarmerByFarmerIdByIdList(id);
	}

	@Override
	public void addFarmIcsConversion(FarmIcsConversion existing) {
		// TODO Auto-generated method stub
		farmerDAO.save(existing);
	}

	@Override
	public FarmIcsConversion findFarmIcsConversionByFarmIcsConvId(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmIcsConversionByFarmIcsConvId(id);
	}

	@Override
	public Object[] findTotalAcreAndEstimatedYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalAcreAndEstimatedYield(selectedCrop, selectedState, selectedLocality, selectedTaluk,
				selectedVillage, selectedFarmer, selectedSeason, selectedOrganicStatus);
	}

	@Override
	public Object[] findFarmerCountByStateName(String stateName) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerCountByStateName(stateName);
	}

	public List<Object[]> populateStateAndFarmerCountList(String selectedBranch) {
		return farmerDAO.populateStateAndFarmerCountList(selectedBranch);
	}

	public List<Object[]> populateDistrictAndFarmerCountList(String selectedBranch) {
		return farmerDAO.populateDistrictAndFarmerCountList(selectedBranch);
	}

	public List<Object[]> populateDistrictAndactiveFarmers(String selectedBranch) {
		return farmerDAO.populateDistrictAndactiveFarmers(selectedBranch);
	}

	public List<Object[]> getCertifiedFarmerCount() {
		return farmerDAO.getCertifiedFarmerCount();
	}

	public List<Object[]> getNonCertifiedFarmerCount() {
		return farmerDAO.getNonCertifiedFarmerCount();
	}

	public List<Object[]> getFarmDetailsAndProposedPlantingArea(String locationLevel, String selectedBranch,
			String gramPanchayatEnable) {
		return farmerDAO.getFarmDetailsAndProposedPlantingArea(locationLevel, selectedBranch, gramPanchayatEnable);
	}

	public BigInteger[] getPramidChartDetails() {
		return farmerDAO.getPramidChartDetails();
	}

	@Override
	public List<DynamicFeildMenuConfig> listDynamicMenusByRevNo(String revisionNo) {
		return farmerDAO.listDynamicMenusByRevNo(revisionNo);
	}

	public List<DynamicFieldConfig> listDynamicFieldsBySectionCode(String sectionCode) {
		// TODO Auto-generated method stub
		return farmerDAO.listDynamicFieldsBySectionCode(sectionCode);
	}

	public void addNewComponentIntoList(Long component_id, Long list_id) {
		farmerDAO.addNewComponentIntoList(component_id, list_id);
	}

	@Override
	public List<Object[]> findTotalWeightAmtNameByProcurement(String hqlQueryAppnd, Map<String, Object> params) {
		return farmerDAO.findTotalWeightAmtNameByProcurement(hqlQueryAppnd, params);
	}

	@Override
	public List<Object[]> findSeasonCodeList(String selectedBranch) {
		// TODO Auto-generated method stub
		return farmerDAO.findSeasonCodeList(selectedBranch);
	}

	public List<Object[]> findProductsByWarehouse(String warehouse, String chartType) {

		return farmerDAO.findProductsByWarehouse(warehouse, chartType);
	}

	public List<Object[]> listDistributionWarehouse(String branch, String season, String chartType) {

		return farmerDAO.listDistributionWarehouse(branch, season, chartType);
	}

	@Override
	public List<Object[]> findAmtAndQtyByProcurment(String selectedBranch, String selectedState, String selectedSeason,
			Date sDate, Date eDate) {
		// TODO Auto-generated method stub
		return farmerDAO.findAmtAndQtyByProcurment(selectedBranch, selectedState, selectedSeason, sDate, eDate);
	}

	@Override
	public Object[] findSaleIncomeByFarmer(List<String> farmerIds, List<String> farmCodes) {
		// TODO Auto-generated method stub
		return farmerDAO.findSaleIncomeByFarmer(farmerIds, farmCodes);
	}

	@Override
	public List<Object[]> listOfFarmersByFarmCrops() {
		return farmerDAO.listOfFarmersByFarmCrops();
	}

	@Override
	public Object[] findTotalAmtAndweightByProcurementWithDate(String filterList, Map<String, Object> params,
			Date convertStringToDate, Date convertStringToDate2, String branch) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalAmtAndweightByProcurementWithDate(filterList, params, convertStringToDate,
				convertStringToDate2, branch);
	}

	public double findStockAdded(Long distId, Long warehouseId) {
		return farmerDAO.findStockAdded(distId, warehouseId);
	}

	public List<Object[]> farmersByBranch() {
		return farmerDAO.farmersByBranch();
	}

	public List<Object[]> farmersByCountry(String selectedBranch) {
		return farmerDAO.farmersByCountry(selectedBranch);
	}

	public List<Object[]> farmersByState(String selectedBranch) {
		return farmerDAO.farmersByState(selectedBranch);
	}

	public List<Object[]> farmersByLocality(String selectedBranch) {
		return farmerDAO.farmersByLocality(selectedBranch);
	}

	public List<Object[]> farmersByMunicipality(String selectedBranch) {
		return farmerDAO.farmersByMunicipality(selectedBranch);
	}

	public List<Object[]> farmersByGramPanchayat(String selectedBranch) {
		return farmerDAO.farmersByGramPanchayat(selectedBranch);
	}

	public List<Object[]> farmersByVillageWithGramPanchayat(String selectedBranch) {
		return farmerDAO.farmersByVillageWithGramPanchayat(selectedBranch);
	}

	public List<Object[]> farmersByVillageWithOutGramPanchayat(String selectedBranch) {
		return farmerDAO.farmersByVillageWithOutGramPanchayat(selectedBranch);
	}

	public List<Object[]> farmerDetailsByVillage(String selectedBranch) {
		return farmerDAO.farmerDetailsByVillage(selectedBranch);
	}

	public List<Object[]> populateFarmerLocationCropChart(String codeForCropChart) {
		return farmerDAO.populateFarmerLocationCropChart(codeForCropChart);
	}

	public List<Object[]> listfarmerwithoutorganic() {
		return farmerDAO.listfarmerwithoutorganic();
	}

	public List<Object[]> estimatedAndActualYield(String locationCode) {
		return farmerDAO.estimatedAndActualYield(locationCode);
	}

	public List<Object[]> listFarmFieldsByFarmerIdAndNonOrganic(long id) {
		return farmerDAO.listFarmFieldsByFarmerIdAndNonOrganic(id);
	}

	public List<Object[]> listFarmFieldsByFarmerIdNonCertified(long id) {
		return farmerDAO.listFarmFieldsByFarmerIdNonCertified(id);
	}

	@Override
	public List<DynamicImageData> listDynamicImageByIds(List<Long> isList) {
		return farmerDAO.listDynamicImageByIds(isList);
	}

	@Override
	public void updateDistributionStatus(int deleteStatus, long distId) {
		// TODO Auto-generated method stub
		farmerDAO.updateDistributionStatus(deleteStatus, distId);
	}

	@Override
	public void addAgroTxn(AgroTransaction agroTxn) {
		// TODO Auto-generated method stub
		farmerDAO.save(agroTxn);
	}

	public List<Object[]> estimatedYield(String locationCode) {
		return farmerDAO.estimatedYield(locationCode);
	}

	public List<Object[]> actualYield(String locationCode) {
		return farmerDAO.actualYield(locationCode);
	}

	public List<Farmer> listFarmerByIds(List<String> ids) {
		return farmerDAO.listFarmerByIds(ids);
	}

	public List<DynamicConstants> listDynamicConstants() {
		return farmerDAO.listDynamicConstants();
	}

	public String getFieldValueByContant(String entityId, String referenceId, String group) {
		return farmerDAO.getFieldValueByContant(entityId, referenceId, group);
	}

	public List<Object[]> warehouseToMobileUserChart(String branch) {
		return farmerDAO.warehouseToMobileUserChart(branch);
	}

	public List<Object[]> warehouseToMobileUser_AgentChart(String branch, String warehouse) {
		return farmerDAO.warehouseToMobileUser_AgentChart(branch, warehouse);
	}

	public List<Object[]> warehouseToMobileUser_ProductChart(String branch, String warehouse, String agentId) {
		return farmerDAO.warehouseToMobileUser_ProductChart(branch, warehouse, agentId);
	}

	public List<Object[]> populateMobileUserToFarmer_AgentChart(String branch, String season) {
		return farmerDAO.populateMobileUserToFarmer_AgentChart(branch, season);
	}

	public List<Object[]> populateMobileUserToFarmer_FarmerChart(String branch, String agent) {
		return farmerDAO.populateMobileUserToFarmer_FarmerChart(branch, agent);
	}

	public List<Object[]> populateMobileUserToFarmer_ProductChart(String branch, String agent, String farmerId) {
		return farmerDAO.populateMobileUserToFarmer_ProductChart(branch, agent, farmerId);
	}

	public List<Object[]> populateWarehouseToFarmer_WarehouseChart(String branch) {
		return farmerDAO.populateWarehouseToFarmer_WarehouseChart(branch);
	}

	public List<Object[]> populateWarehouseToFarmer_FarmerChart(String branch, String warehouse) {
		return farmerDAO.populateWarehouseToFarmer_FarmerChart(branch, warehouse);
	}

	public List<Object[]> populateWarehouseToFarmer_ProductChart(String branch, String warehouse, String farmerId) {
		return farmerDAO.populateWarehouseToFarmer_ProductChart(branch, warehouse, farmerId);
	}

	public List<Object[]> productChartByWarehouseToMobileUser(String branch, String warehouse) {
		return farmerDAO.productChartByWarehouseToMobileUser(branch, warehouse);
	}

	public List<Object[]> productChartByMobileUserToFarmer(String branch, String agent, String selectedSeason) {
		return farmerDAO.productChartByMobileUserToFarmer(branch, agent, selectedSeason);
	}

	public List<Object[]> productChartByWarehouseToFarmer(String branch, String warehouse) {
		return farmerDAO.productChartByWarehouseToFarmer(branch, warehouse);
	}

	public List<Object[]> listParentFields() {
		return farmerDAO.listParentFields();
	}

	public List<Object[]> ListFarmerDynamicDataAgentByTxnType(String farmerCode, String branchId) {
		// TODO Auto-generated method stub
		return farmerDAO.ListFarmerDynamicDataAgentByTxnType(farmerCode, branchId);
	}

	public List<Object[]> populateAvailableColumns() {
		return farmerDAO.populateAvailableColumns();
	}

	public List<Object[]> getGridData(String query) {
		return farmerDAO.getGridData(query);
	}

	@Override
	public List<FarmerDynamicData> ListFarmerDynamicDatas(long id) {
		return farmerDAO.ListFarmerDynamicDatas(id);
	}

	@Override
	public FarmerDynamicData findFarmerDynamicDataByTxnUniquId(String txnUniqueId) {
		return farmerDAO.findFarmerDynamicDataByTxnUniquId(txnUniqueId);
	}

	public List<Object[]> cropHarvestByFarmerId(String farmerId, String startDate, String endDate, String seasonCode) {
		return farmerDAO.cropHarvestByFarmerId(farmerId, startDate, endDate, seasonCode);
	}

	public List<Object[]> distributionToFarmerByFarmerId(String farmerId, String startDate, String endDate,
			String seasonCode) {
		return farmerDAO.distributionToFarmerByFarmerId(farmerId, startDate, endDate, seasonCode);
	}

	public List<Object[]> productReturnByFarmerId(String farmerId, String startDate, String endDate,
			String seasonCode) {
		return farmerDAO.productReturnByFarmerId(farmerId, startDate, endDate, seasonCode);
	}

	public List<Object[]> trainingStatusReportByFarmerId(String farmerId, String startDate, String endDate) {
		return farmerDAO.trainingStatusReportByFarmerId(farmerId, startDate, endDate);
	}

	public List<Object[]> farmerBalanceReportByFarmerId(String farmerId, String startDate, String endDate) {
		return farmerDAO.farmerBalanceReportByFarmerId(farmerId, startDate, endDate);
	}

	public List<Object[]> procurementTransactionsByFarmerId(String farmerId, String startDate, String endDate,
			String seasonCode) {
		return farmerDAO.procurementTransactionsByFarmerId(farmerId, startDate, endDate, seasonCode);
	}

	public void saveDynamicReport(String title, String description, String query, String header_fields,
			String filter_data, String entity, String fields, String groupByField, String id) {
		farmerDAO.saveDynamicReport(title, description, query, header_fields, filter_data, entity, fields, groupByField,
				id);
	}

	public void deleteDynamicReportById(String id) {
		farmerDAO.deleteDynamicReportById(id);
	}

	public void updateDynamicReportById(String id, String title, String description, String query, String header_fields,
			String filter_data, String entity, String fields, String groupByField) {
		farmerDAO.updateDynamicReportById(id, title, description, query, header_fields, filter_data, entity, fields,
				groupByField);
	}

	@Override
	public void addTreeDetails(TreeDetail treeDetail) {
		// TODO Auto-generated method stub
		farmerDAO.save(treeDetail);
	}

	@Override
	public List<TreeDetail> findTreeDetailByFarmId(long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findTreeDetailByFarmId(id);

	}

	public List<Object[]> periodicInspectionsByFarmerId(String farmCode, String startDate, String endDate,
			String seasonCode) {
		return farmerDAO.periodicInspectionsByFarmerId(farmCode, startDate, endDate, seasonCode);
	}

	public List<Object[]> periodicNeedBasedInspectionsByFarmerId(String farmCode, String startDate, String endDate,
			String seasonCode) {
		return farmerDAO.periodicNeedBasedInspectionsByFarmerId(farmCode, startDate, endDate, seasonCode);
	}

	@Override
	public List<Object> ListFarmerDynamicDatasByIds(List<Long> mainIds) {
		return farmerDAO.ListFarmerDynamicDatasByIds(mainIds);
	}

	public List<FarmCropsField> listFarmCropsFields() {
		return farmerDAO.listFarmCropsFields();
	}

	public FarmerDynamicData findFarmerDynamicDataByReferenceId(long referenceId) {
		return farmerDAO.findFarmerDynamicDataByReferenceId(referenceId);
	}
	
	private FarmerDynamicData findFarmerDynamicDataByReferenceIdAndTxnType(long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerDynamicDataByReferenceIdAndTxnType(id);
	}

	public List<DynamicReportFieldsConfig> populateDynamicReportFields() {
		return farmerDAO.populateDynamicReportFields();
	}

	public List<Object[]> listFarmerHavingPlots() {
		return farmerDAO.listFarmerHavingPlots();
	}

	public List<Object[]> listFarmByFarmerIds(long id) {
		return farmerDAO.listFarmByFarmerIds(id);
	}

	@Override
	public void removeCropCalendar(CropCalendar cropCalendar) {

		// TODO Auto-generated method stub
		farmerDAO.delete(cropCalendar);

	}

	@Override
	public void updateCropCalendarDetails(CropCalendarDetail cropCalendarDetails) {
		// TODO Auto-generated method stub
		farmerDAO.update(cropCalendarDetails);
	}

	@Override
	public Farmer findFarmerByFirstNameLastNameAndSurName(String firstName, String lastName, String surName) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerByFirstNameLastNameAndSurName(firstName, lastName, surName);
	}

	@Override
	public void saveCropCalendar(CropCalendar cropCalendar) {
		// TODO Auto-generated method stub
		farmerDAO.save(cropCalendar);
	}

	@Override
	public Farmer findFarmerByFirstNameAndSurName(String firstName, String surName) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerByFirstNameAndSurName(firstName, surName);
	}

	@Override
	public int findDynamicMenuMaxOrderNo() {
		return farmerDAO.findDynamicMenuMaxOrderNo();
	}

	@Override
	public DynamicReportTableConfig findDyamicReportTableConfigById(Long enity) {
		return farmerDAO.findDyamicReportTableConfigById(enity);
	}

	@Override
	public List<Object[]> listColdStorageNameDynamic() {
		// TODO Auto-generated method stub
		return farmerDAO.listColdStorageNameDynamic();
	}

	@Override
	public String isFarmInventoryMappingexist(long id) {
		// TODO Auto-generated method stub

		boolean farmCorps = farmerDAO.isFarmMappedFarmCrops(id);
		if (farmCorps) {
			return "farmCrops.mapped";
		}
		return null;
	}

	@Override
	public String isFarmerMappingexist(long id) {

		boolean farm = farmerDAO.isFarmMappingFarm(id);
		if (farm) {
			return "farm.mapped";
		}
		return null;
	}

	@Override
	public WarehouseStorageMap findWarehouseStorageMapByWarehouseId(Long id) {
		return farmerDAO.findWarehouseStorageMapByWarehouseId(id);
	}

	@Override
	public List<WarehouseStorageMap> listOfwarehouseStorageMap(long id) {
		return farmerDAO.listOfWarehouseStorageMap(id);
	}

	@Override
	public List<Object[]> listFarmCropsFieldsByFarmerIdAgentIdAndSeason(long agentId, Long revisionNo,
			String seasonCode) {
		return farmerDAO.listFarmCropsFieldsByFarmerIdAgentIdAndSeason(agentId, revisionNo, seasonCode);
	}

	@Override
	public List<Object[]> getFarmDetailsAndCultivationArea(String locationLevel, String selectedBranch,
			String gramPanchayatEnable) {
		// TODO Auto-generated method stub
		return farmerDAO.getFarmDetailsAndCultivationArea(locationLevel, selectedBranch, gramPanchayatEnable);

	}

	@Override
	public FarmCrops findFarmCropByCropIdAndFarmId(long farmId, long varietyId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmCropByCropIdAndFarmId(farmId, varietyId);
	}

	@Override
	public List<Object[]> listValuesbyQuery(String methodName, Object[] parameter, String branchId) {
		// TODO Auto-generated method stub
		return farmerDAO.listValuesbyQuery(methodName, parameter, branchId);
	}

	public List<Object[]> plottingAreaByBranch() {
		return farmerDAO.plottingAreaByBranch();
	}

	public List<Object[]> plottingByWarehouse(String selectedBranch) {
		return farmerDAO.plottingByWarehouse(selectedBranch);
	}

	public List<Object[]> plottingAreaByBranch(String selectedSeason) {
		return farmerDAO.plottingAreaByBranch(selectedSeason);
	}

	public List<Object[]> plottingByMobileUser(String selectedBranch) {
		return farmerDAO.plottingByMobileUser(selectedBranch);
	}

	@Override
	public List<Object[]> trainingsByBranch() {
		return farmerDAO.trainingsByBranch();
	}

	@Override
	public List<Object[]> findAgentTrainingData(long warehouseId,String selectedFinYear) {
		return farmerDAO.findAgentTrainingData(warehouseId,selectedFinYear);
	}

	@Override
	public List<Object[]> populateWarehouseMobileUserToFarmer_AgentChart(String branch, String season, String selectedFinYear) {
		return farmerDAO.populateWarehouseMobileUserToFarmer_AgentChart(branch, season,selectedFinYear);
	}

	@Override
	public EventCalendar findEventByMessageNumber(String eventId) {
		// TODO Auto-generated method stub
		return farmerDAO.findEventByMessageNumber(eventId);
	}

	@Override
	public List<EventCalendar> listEventByRevisionNo(Long revisionNo, String agentId) {
		// TODO Auto-generated method stub
		return farmerDAO.listEventByRevisionNo(revisionNo, agentId);
	}

	@Override
	public String getValueByQuery(String methodName, Object[] parameter, String branchId) {
		// TODO Auto-generated method stub
		return farmerDAO.getValueByQuery(methodName, parameter, branchId);
	}

	@Override
	public DynamicFieldScoreMap findDynamicFieldScoreByFieldIdAndComponentCode(String id, String code) {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicFieldScoreByFieldIdAndComponentCode(id, code);
	}

	@Override
	public List<Object[]> getValueListByQuery(String methodName, Object[] parameter, String branchId) {
		return farmerDAO.getValueListByQuery(methodName, parameter, branchId);
	}

	@Override
	public List<Object[]> listFarmerFarmInfoFarmCropsByVillageId(FarmCrops crops) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerFarmInfoFarmCropsByVillageId(crops);
	}

	@Override
	public List<Object[]> listOfFarmersByVillageAndFarmCrops(long villageId,String plottingType) {
		// TODO Auto-generated method stub
		return farmerDAO.listOfFarmersByVillageAndFarmCrops(villageId,plottingType);
	}

	@Override
	public Integer findFarmersCountSowingLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmersCountSowingLoca(selectedCrop, selectedState, selectedLocality, selectedTaluk,
				selectedVillage, selectedFarmer);
	}

	@Override
	public Object[] findTotalAcreAndEstimatedYieldSwoingLoca(String selectedCrop, String selectedState,
			String selectedLocality, String selectedTaluk, String selectedVillage, String selectedFarmer) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalAcreAndEstimatedYieldSwoingLoca(selectedCrop, selectedState, selectedLocality,
				selectedTaluk, selectedVillage, selectedFarmer);
	}

	@Override
	public List<Object[]> listFarmerFarmCropInfoByVillageIdImg(String cropId, Long fType, Long farmId) {
		return farmerDAO.listFarmerFarmCropInfoByVillageIdImg(cropId, fType, farmId);
	}

	@Override
	public void saveSectionBatch(List<DynamicSectionConfig> dscList) {
		farmerDAO.saveSectionBatch(dscList);
	}

	public long findDynamicFieldsCountByCode(String code) {
		return farmerDAO.findDynamicFieldsCountByCode(code);
	}

	public long findDynamicMenuCountByCode(String code) {
		return farmerDAO.findDynamicMenuCountByCode(code);
	}

	public long findDynamicSectionCountByCode(String code) {
		return farmerDAO.findDynamicSectionCountByCode(code);
	}

	public void saveFieldsBatch(List<DynamicFieldConfig> dfcList) {
		farmerDAO.saveFieldsBatch(dfcList);
	}

	public List<Object[]> listOfFarmersByVillageAndFarm(long villageId) {
		// TODO Auto-generated method stub
		return farmerDAO.listOfFarmersByVillageAndFarm(villageId);
	}

	@Override
	public List<DynamicReportFieldsConfig> listDynamicColumnsByEntity(String enities) {
		return farmerDAO.listDynamicColumnsByEntity(enities);
	}

	public List<Object[]> updateLotStatus(String selectedBales) {
		return farmerDAO.updateLotStatus(selectedBales);
	}

	@Override
	public FarmerTraceability findTraceabilityDataById(Long id) {
		return farmerDAO.findTraceabilityDataById(id);
	}

	@Override
	public String findQrCodeParameterByKey(String farmCode, String tenantId) {
		// TODO Auto-generated method stub
		return farmerDAO.findQrCodeParameterByKey(farmCode, tenantId);
	}

	@Override
	public Farm findFarmByID(Long id, String tenantId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmByID(id, tenantId);
	}

	@Override
	public List<Object[]> listFarmDetailsInfo() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmDetailsInfo();
	}

	@Override
	public List<FarmerDynamicFieldsValue> listFarmerDynamicFieldByFdIds(List<Long> ids) {
		return farmerDAO.listFarmerDynamicFieldByFdIds(ids);
	}

	@Override
	public List<DataLevel> listDataLevel() {
		// TODO Auto-generated method stub
		return farmerDAO.listDataLevel();
	}

	@Override
	public CropYield findCropYieldById(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findCropYieldById(id);
	}

	@Override
	public List<CropYieldDetail> findCropYieldDetailById(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findCropYieldDetailById(id);
	}

	@Override
	public void updateFDVS(List<FarmerDynamicFieldsValue> fdvs) {
		farmerDAO.updateFDVS(fdvs);

	}

	@Override
	public List<Object[]> listFDVSForFolloUp(String agentId,String revisionNo) {
		return farmerDAO.listFDVSForFolloUp(agentId,revisionNo);

	}

	@Override
	public void updateFarmerDynamicData(FarmerDynamicData farmerDynamicData,
			LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap,Map<String, List<FarmerDynamicFieldsValue>> fdMap) {
		if (farmerDynamicData.getIsScore() != null && farmerDynamicData.getIsScore() == 2) {
			farmerDynamicData = processScoreCalculation(farmerDynamicData, fieldConfigMap);
			processAuditCalcul(farmerDynamicData, fieldConfigMap, fdMap, farmerDynamicData.getConversionStatus());
		}else if (farmerDynamicData.getIsScore() != null && farmerDynamicData.getIsScore() == 3) {
			farmerDynamicData = processScoreCalculation(farmerDynamicData, fieldConfigMap);
			processAuditCalculSym(farmerDynamicData, fieldConfigMap, fdMap, farmerDynamicData.getConversionStatus());
		}
		farmerDynamicData.setActStatus(2);
		farmerDynamicData.setRevisionNo(DateUtil.getRevisionNumber());
		farmerDAO.saveOrUpdate(farmerDynamicData);

	}

	@Override
	public List<Object[]> findFarmerByListOfFarmerId(List<String> farmerList) {
		return farmerDAO.findFarmerByListOfFarmerId(farmerList);
	}

	@Override
	public String findComponentNameByDynamicFieldCode(String fieldCode) {
		return farmerDAO.findComponentNameByDynamicFieldCode(fieldCode);
	}

	
	public List<Object[]> listFarmerDynamicFieldsValuesByFarmIdList(List<String> farmIdList,List<String> selectedDynamicFieldCodeList) {
		return farmerDAO.listFarmerDynamicFieldsValuesByFarmIdList(farmIdList,selectedDynamicFieldCodeList);
	}
	
	@Override
	public Farmer findFarmerByMSISDN(String msisdn) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerByMSISDN(msisdn);
	}
	
	public List<Object[]> listDynamicFieldsCodeAndName() {
		return farmerDAO.listDynamicFieldsCodeAndName();
	}

	@Override
	public List<DynamicMenuFieldMap> listDynamisMenubyscoreType(int i) {
		return farmerDAO.listDynamisMenubyscoreType(i);

	}

	@Override
	public List<Object[]> listFarmerCodeIdNameByStateCode(String selectedState) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerCodeIdNameByStateCode(selectedState);
	}

	@Override
	public List<DynamicFeildMenuConfig> listDynamicMenusByRevNo(String revisionNo, String branchId,String tenantId) {
		return farmerDAO.listDynamicMenusByRevNo( revisionNo,  branchId, tenantId);
	}
	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenusByType(String txnTypez, String branchId){
		return farmerDAO.findDynamicMenusByType( txnTypez,  branchId);
	}
	
	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenusByTypeForOCP(String txnTypez, String branchId){
		return farmerDAO.findDynamicMenusByTypeForOCP(txnTypez, branchId);
	}
	
	@Override
	public List<Object[]> listValueByFieldName(String field,String branchId) {
		// TODO Auto-generated method stub
		return farmerDAO.listValueByFieldName(field,branchId);
	}
	
	@Override
	public FarmCrops findFarmCropFullObjectById(String id) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmCropFullObjectById(id);
	}
	
	@Override
	public FarmerDynamicFieldsValue findFarmerDynamicFieldsValueById(String id) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerDynamicFieldsValueById(id);
	}
	
	@Override
	public FarmerDynamicData processCustomisedFormula(FarmerDynamicData farmerDynamicData,
			Map<String, DynamicFieldConfig> fieldConfigMap, Map<String, List<FarmerDynamicFieldsValue>> fdMap) {
		return farmerDAO.processCustomisedFormula(farmerDynamicData, fieldConfigMap,fdMap);

	}
	
	@Override
	public ColdStorage findColdStorageById(String id) {
		// TODO Auto-generated method stub
		return farmerDAO.findColdStorageById(id);
	}

	@Override
	public ColdStorageDetail findColdStorageDetailById(Long valueOf) {
		// TODO Auto-generated method stub
		return farmerDAO.findColdStorageDetailById(valueOf);
	}

	@Override
	public List<DynamicMenuFieldMap> listDynamisMenubyscoreType() {
		// TODO Auto-generated method stub
		return farmerDAO.listDynamisMenubyscoreType();
	}

	@Override
	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm, String selectedOrganicStatus,String selectedFarmer,List<Long> yieldEstimationDoneFarmers) {
		return farmerDAO.listFarmerFarmInfoByVillageId(farm,selectedOrganicStatus,selectedFarmer,yieldEstimationDoneFarmers);
	}
	
	@Override
	public List<FarmerDynamicData> listFarmerDynamicDataByTxnId(String txnType) {
		return farmerDAO.listFarmerDynamicDataByTxnId(txnType);
	}
	
	public List<Object[]> listfarmerDynamicData(List<Long> fidLi){
		return farmerDAO.listfarmerDynamicData(fidLi);
	}
	
	public FarmCrops findFarmCropByCropIdAndFarmIdAndSeason(long farmId, long varietyId, long seasonId){
		return farmerDAO.findFarmCropByCropIdAndFarmIdAndSeason(farmId, varietyId,seasonId);
	}
	
	@Override
	public List<Object> listFarmerCountByGroupTraderAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedCooperative, String selectedGender, String selectedCrop, String typez,
			String selectedVillage) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerCountByGroupTraderAndBranchStateCoop(selectedBranch, selectedState, selectedGender,
				selectedCooperative, selectedCrop, typez, selectedVillage);
	}
	
	public List<Object> listFarmerCountByFarmInspection(Date sDate, Date eDate){
		return farmerDAO.listFarmerCountByFarmInspection(sDate,eDate);
	}

	@Override
	public FarmerDynamicFieldsValue findLotCodeFromFarmerDynamicFieldsValue(Long id, String fieldName) {
		// TODO Auto-generated method stub
		return farmerDAO.findLotCodeFromFarmerDynamicFieldsValue(id, fieldName);
	}
	
	@Override
	public List<Object[]> populateTrainingChart(String selectedBranch, String selectedFinYear) {
		// TODO Auto-generated method stub
		return farmerDAO.populateTrainingChart(selectedBranch,selectedFinYear);
	}

	@Override
	public Integer findFarmerCountByStateAndCrop(String selectedBranch, int i, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedFinYear) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerCountByStateAndCrop(selectedBranch, i, selectedCrop, selectedCooperative,
				selectedGender,selectedFinYear);
	}

	@Override
	public List<Object> findFarmerDetailsByStateAndCrop(String selectedBranch, Long selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedFinYear) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerDetailsByStateAndCrop(selectedBranch, selectedState, selectedCrop,
				selectedCooperative, selectedGender,selectedFinYear);
	}

	@Override
	public List<Object[]> productChartByMobileUserToFarmer(String selectedBranch, String agentId, String selectedSeason,
			String selectedFinYear) {
		// TODO Auto-generated method stub
		return farmerDAO.productChartByMobileUserToFarmer(selectedBranch, agentId, selectedSeason,
				selectedFinYear);
	}
	
	public List<FarmCropsField> listRemoveFarmCropFields() {
		return farmerDAO.listRemoveFarmCropFields();
	}

	@Override
	public List<Object[]> listFarmsLastInspectionDate() {
		return farmerDAO.listFarmsLastInspectionDate();
	}
	
	@Override
	public CropYield findMoleculeDateByLotCode(String lotCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findMoleculeDateByLotCode(lotCode);
	}

	@Override
	public Farmer findOlivadoFarmerByFarmerCode(String farmerCode) {
		// TODO Auto-generated method stub
		return farmerDAO.findOlivadoFarmerByFarmerCode(farmerCode);
	}
	
	public List<Object[]> listFarmerAddressById(Long farmerId){
		return farmerDAO.listFarmerAddressById(farmerId);
	}

	@Override
	public List<Object[]> populateWarehouseMobileUserToFarmer_AgentChart(String selectedBranch, String selectedSeason) {
		// TODO Auto-generated method stub
		return farmerDAO.populateWarehouseMobileUserToFarmer_AgentChart(selectedBranch, selectedSeason);
	}
	
	@Override
	public List<Object[]> findDyamicReportTableConfigParentIdsById(String entity) {
		// TODO Auto-generated method stub
		return farmerDAO.findDyamicReportTableConfigParentIdsById(entity);
	}

	@Override
	public List<String> listFarmerByCreatedUser() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerByCreatedUser();
	}
	

	@Override
	public List<Object[]> listFarmersWithFarmParcelIds(){
		return farmerDAO.listFarmersWithFarmParcelIds();
	}
	
	@Override
	public List<Object[]> listFarmParcelIdsByFarmer(String farmerId){
		return farmerDAO.listFarmParcelIdsByFarmer(farmerId);
	}

	@Override
	public List<Object[]> listFarmCropsWithGcParcelIdsByFarm(String farmId){
		return farmerDAO.listFarmCropsWithGcParcelIdsByFarm(farmId);
	}
	
	@Override
	public List<Object[]> listFarmersWithFarmCropParcelIds() {
		return farmerDAO.listFarmersWithFarmCropParcelIds();
	}
	
	@Override
	public List<Object[]> listFarmWithoutCropParcelIdsByFarmer(String farmerId) {
		return farmerDAO.listFarmWithoutCropParcelIdsByFarmer(farmerId);
	}
	
	@Override
	public List<Object[]> listFarmersWithIdAndCode() {
		return farmerDAO.listFarmersWithIdAndCode();
	}
	
	@Override
	public List<Object[]> listFarmsOfFarmerId(long farmerId) {
		return farmerDAO.listFarmsOfFarmerId(farmerId);
	}
	
	@Override
	public boolean findIfFarmHasParcelId(String farmId){
		return farmerDAO.findIfFarmHasParcelId(farmId);
	}
	
	@Override
	public List<Object[]> listFarmCropsByFarm(String farmId){
		return farmerDAO.listFarmCropsByFarm(farmId);
	}
	
	@Override
	public List<Object[]> listFarmersWithFarmCoordinates() {
		return farmerDAO.listFarmersWithFarmCoordinates();
	}
	
	@Override
	public List<Object[]> listFarmersWithFarmCropCoordinates(){
		return farmerDAO.listFarmersWithFarmCropCoordinates();
	}

	@Override
	public List<Object[]> listFarmWithCropParcelIdsByFarmer(String farmerId){
		return farmerDAO.listFarmWithCropParcelIdsByFarmer(farmerId);
	}
		
	
	@Override
	public List<Object[]> listFarmCropByFarmId(String farmId) {
		return farmerDAO.listFarmCropByFarmId(farmId);
	}
	
	@Override
	public List<Object[]> listFarmCropsWithFarmId(long farmId) {
		return farmerDAO.listFarmCropsWithFarmId(farmId);
	}

	@Override
	public List<Object[]> listValueByFieldName(String localeProperty) {
		// TODO Auto-generated method stub
		return farmerDAO.listValueByFieldName(localeProperty);
	}
	
	@Override
	public List<Object[]> listVendorByLoanStatusAndFarmerAndVendor(Long farmerId,String vendorId) {
		// TODO Auto-generated method stub
		return farmerDAO.listVendorByLoanStatusAndFarmerAndVendor(farmerId, vendorId);
	}
	
	@Override
	public List<Object[]> listOfProofNo() {
		// TODO Auto-generated method stub
		return farmerDAO.listOfProofNo();
	}

	@Override
	public List<Object[]> listFarmerCodeIdNameByFarmerTypezAndLoanApplication(String branchId) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerCodeIdNameByFarmerTypezAndLoanApplication( branchId);
	}
	
	@Override
	public List<Object[]> listActiveContractFarmersByLoanStatusAndSamithiAndVendor(Long id, String vendorId) {
		// TODO Auto-generated method stub
		return farmerDAO.listActiveContractFarmersByLoanStatusAndSamithiAndVendor(id, vendorId);
	}
	
	
	@Override
	public LoanDistribution findFarmerLatestLoanYear(Long farmerId){
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerLatestLoanYear(farmerId);
	}
	
	@Override
	public LoanDistribution findGroupLatestLoanYear(Long farmerId){
		// TODO Auto-generated method stub
		return farmerDAO.findGroupLatestLoanYear(farmerId);
	}
	
	public LoanApplication findFarmerLatestLoanApplication(Long farmerId){
		return farmerDAO.findFarmerLatestLoanApplication(farmerId);
	}
	
	public LoanApplication findGroupLatestLoanApplication(Long farmerId){
		return farmerDAO.findGroupLatestLoanApplication(farmerId);
	}
	
	@Override
	public List<Farmer> listGroup() {
		// TODO Auto-generated method stub
		return farmerDAO.listGroup();
	}
	
	public Farmer findGroupById(Long id){
		return farmerDAO.findGroupById(id);
	}
	
	@Override
	public List<Object> findFarmerDetailsByStateAndCropbyFarmerStatus(String selectedBranch, Long selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender,String selectedStatus){
		return farmerDAO.findFarmerDetailsByStateAndCropbyFarmerStatus(selectedBranch,selectedState,selectedCrop,selectedCooperative,selectedGender,selectedStatus);
	}
	
	@Override
	public Integer findFarmerCountByStateAndCropbyFarmerStatus(String selectedBranch, int selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender,String selectedStatus){
		return farmerDAO.findFarmerCountByStateAndCropbyFarmerStatus(selectedBranch,selectedState,selectedCrop,selectedCooperative,selectedGender,selectedStatus);
	}
	

	@Override
	public List<Object[]> listFarmscountInfo(){
		return farmerDAO.listFarmscountInfo();
	}
	
	@Override
	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm,String selectedOrganicStatus,String selectedFarmer,List<Long> yieldEstimationDoneFarmers,
			String selectedStatus ) {
		return farmerDAO.listFarmerFarmInfoByVillageId(farm, selectedOrganicStatus, selectedFarmer,yieldEstimationDoneFarmers,selectedStatus);
	}
	
	@Override
	public Integer findFarmersCountFarmerLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus,String selectedStatus){
		return farmerDAO.findFarmersCountFarmerLoca(selectedCrop, selectedState, selectedLocality, selectedTaluk,
				selectedVillage, selectedFarmer, selectedSeason, selectedOrganicStatus,selectedStatus);
	}
	
	@Override
	public Object[] findTotalAcreAndEstimatedYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus,String selectedStatus){
		return farmerDAO.findTotalAcreAndEstimatedYield(selectedCrop, selectedState, selectedLocality, selectedTaluk,
				selectedVillage, selectedFarmer, selectedSeason, selectedOrganicStatus,selectedStatus);		
	}
	
	@Override
	public List<Object[]> listFarmerFarmInfoFarmCropsByVillageId(Object obj,String selectedStatus,String plottingType){
		return farmerDAO.listFarmerFarmInfoFarmCropsByVillageId(obj,selectedStatus,plottingType);		
	}
	
	@Override
	public Integer findFarmersCountSowingLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer,String selectedStatus) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmersCountSowingLoca(selectedCrop, selectedState, selectedLocality, selectedTaluk,
				selectedVillage, selectedFarmer, selectedStatus);
	}
	
	@Override
	public Object[] findTotalAcreAndEstimatedYieldSwoingLoca(String selectedCrop, String selectedState,
			String selectedLocality, String selectedTaluk, String selectedVillage, String selectedFarmer,String selectedStatus,String selectedFarm) {
		// TODO Auto-generated method stub
		return farmerDAO.findTotalAcreAndEstimatedYieldSwoingLoca(selectedCrop, selectedState, selectedLocality,
				selectedTaluk, selectedVillage, selectedFarmer,selectedStatus,selectedFarm);
	}

	@Override
	public List<Object[]> farmersByGroup(String selectedBranch) {
		// TODO Auto-generated method stub
		return farmerDAO.farmersByGroup(selectedBranch);
	}

	@Override
	public List<Object[]> populateFarmerLocationCropChartByGroup(String codeForCropChart, String selectedBranch,String seasonCode) {
		// TODO Auto-generated method stub
		return farmerDAO.populateFarmerLocationCropChartByGroup(codeForCropChart, selectedBranch,seasonCode);
	}

	@Override
	public List<Object[]> getFarmDetailsAndProposedPlantingAreaByGroup(String locationLevel1, String selectedBranch,
			String gramPanchayatEnable) {
		// TODO Auto-generated method stub
		return farmerDAO.getFarmDetailsAndProposedPlantingAreaByGroup(locationLevel1, selectedBranch, gramPanchayatEnable);
	}

	@Override
	public List<Object[]> findFarmerCountByGroupICS(String selectedBranch) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerCountByGroupICS(selectedBranch);
	}
	
	@Override
	public List<DynamicFeildMenuConfig> findDynamicMenus() {
		// TODO Auto-generated method stub
		return farmerDAO.findDynamicMenus();
	}
	
	public List<Object[]> farmersByBranchandSeason(String season) {
		return farmerDAO.farmersByBranchandSeason(season);
	}
	
	public List<Object[]> farmersByBranchandSeason(String branch,String season) {
		return farmerDAO.farmersByBranchandSeason( branch, season);
	}
	
	public List<Object[]> farmersByCountryAndSeason(String selectedBranch,String seasonCode) {
		return farmerDAO.farmersByCountryAndSeason(selectedBranch,seasonCode);
	}
	
	@Override
	public List<Object[]> farmersByGroupAndSeason(String selectedBranch,String seasonCode ) {
		// TODO Auto-generated method stub
		return farmerDAO.farmersByGroupAndSeason(selectedBranch,seasonCode);
	}
	
	public List<Object[]> farmersByBranch(String branch) {
		return farmerDAO.farmersByBranch(branch);
	}
	
	@Override
	public List<Object[]> findFarmerCountByGroupICS(String selectedBranch, String season) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerCountByGroupICS(selectedBranch,season);
	}
	
	

	@Override
	public List<Object[]> populateFarmerCropCountChartByGroup(String seasonCode, String selectedics,
			String selectedBranch) {
		// TODO Auto-generated method stub
		return farmerDAO.populateFarmerCropCountChartByGroup(seasonCode, selectedics, selectedBranch);
	}

	@Override
	public FarmIcsConversion findFarmIcsConversionByFarmSeasonScopeAndInspectionType(long selectedFarm, String season,
			String scope, String insType) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmIcsConversionByFarmSeasonScopeAndInspectionType(selectedFarm, season, scope, insType);
	}
	
	@Override
	public List<Object[]> ListFarmerCropDetails(List<String> mainIds){
		return farmerDAO.ListFarmerCropDetails(mainIds);
	}
	
	public List<Object[]> ListFarmerCropDetailsByFarmer(List<String> mainIds){
		return farmerDAO.ListFarmerCropDetailsByFarmer(mainIds);
	}
	
	public List<Object[]> listMaxTypez(List<Long> collect,String txnType){
		return farmerDAO.listMaxTypez(collect,txnType);
	}
	
	public List<FarmCrops> listOfCropsByFarmIdAndSeason(List<Long> id,long season){
		return farmerDAO.listOfCropsByFarmIdAndSeason(id,season);
	}

	@Override
	public List<Object[]> listOfFarmsByFarmer(Long farmerId) {
		// TODO Auto-generated method stub
		return farmerDAO.listOfFarmsByFarmer(farmerId);
	}
	
	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByRefId(String refId, String txnType,Long farmerDynamicDataId){
		return farmerDAO.listFarmerDynmaicFieldsByRefId(refId,txnType,farmerDynamicDataId);
	}

	@Override
	public List<Object[]> findAmtAndQtyByProcurmentTraceability(String selectedBranch, String selectedState,
			String selectedSeason, Date sDate, Date eDate) {
		// TODO Auto-generated method stub
		return farmerDAO.findAmtAndQtyByProcurmentTraceability(selectedBranch, selectedState, selectedSeason, sDate, eDate);
	}
	
	@Override
	public List<DataLevel> listDataLevelByType(String type) {
		// TODO Auto-generated method stub
		return farmerDAO.listDataLevelByType(type);
	}
	@Override
	public SamithiIcs findSamithiIcsById(Long id){
		return farmerDAO.findSamithiIcsById(id);
	}
	
	public List<Object[]> listFarmInfoLatLon(){
		return farmerDAO.listFarmInfoLatLon();
	}

	@Override
	public ESEAccount findAccountByFarmerLoanProduct(long farmerId) {
		// TODO Auto-generated method stub
		return farmerDAO.findAccountByFarmerLoanProduct(farmerId);
	}

	@Override
	public LoanInterest findLoanPercent(Long amt) {
		// TODO Auto-generated method stub
		return farmerDAO.findLoanPercent(amt);
	}

	@Override
	public LoanInterest findLoanRangeById(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findLoanRangeById(id);
	}
	@Override
	public void removeLoanRange(LoanInterest loanrange) {
		// TODO Auto-generated method stub
		farmerDAO.delete(loanrange);
	}
	public List<Object[]> findCountOfDynamicDataByFarmerId(List<Long> fidLi,String season){
		return farmerDAO.findCountOfDynamicDataByFarmerId(fidLi,season);
	}
	
	public ESEAccount findEseAccountByFarmerId(String farmerId){
		return farmerDAO.findEseAccountByFarmerId(farmerId);
		
	}

	@Override
	public List<Object[]> listLoanLedgerByEseAccountId(String accountId) {
		// TODO Auto-generated method stub
		return farmerDAO.listLoanLedgerByEseAccountId(accountId);
	}

	@Override
	public List<Object[]> listLoanLedgerByEseAccountId(String accountId, int startIndex, int limit) {
		// TODO Auto-generated method stub
		return farmerDAO.listLoanLedgerByEseAccountId(accountId, startIndex, limit);
	}
	@Override
	public ESEAccount findEseAccountById(Long id) {
		// TODO Auto-generated method stub
		return farmerDAO.findEseAccountById(id);
	}
	@Override
	public List<Object[]> listFarmerInfoByProcurementWithTypez() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerInfoByProcurementWithTypez();
	}

	@Override
	public List<Object[]> listGroupInfoByProcurementWithTypez() {
		// TODO Auto-generated method stub
		return farmerDAO.listGroupInfoByProcurementWithTypez();
	}
	@Override
	public List<Object[]> listFarmerStatementByEseAccountId(String accountId) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerStatementByEseAccountId(accountId);
	}
	
	@Override
	public List<Object[]> listFarmerStatementByEseAccountId(String accountId, int startIndex, int limit) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerStatementByEseAccountId(accountId, startIndex, limit);
	}
	
	@Override
	public List<Object[]> listFarmerFilterWithLoanLedger() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerFilterWithLoanLedger();
	}
	
	@Override
	public List<Object[]> listGroupFilterWithLoanLedger() {
		// TODO Auto-generated method stub
		return farmerDAO.listGroupFilterWithLoanLedger();
	}
	
	@Override
	public List<Object[]> listFFBpurchaseAndFFBRepaymentAmt(String farmerId) {
		// TODO Auto-generated method stub
		return farmerDAO.listFFBpurchaseAndFFBRepaymentAmt(farmerId);
	}
	
	@Override
	public Farmer findFarmerInfoByFarmerId(String farmerId) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmerInfoByFarmerId(farmerId);
	}
	
	public List<Object[]> findDateOfLoanLedger(Date startDate,Date endDate){
		return farmerDAO.findDateOfLoanLedger(startDate,endDate);
	}
	
	
	public List<Object[]> findDateOfLoanLedger(Date startDate,Date endDate,int startIndex, int limit){
		return farmerDAO.findDateOfLoanLedger(startDate,endDate,startIndex,limit);
	}
	
	public List<Object[]> listLoanLedgerByDate(String date,String branchId){
		return farmerDAO.listLoanLedgerByDate(date,branchId);
	}
	public List<Object[]> listLoanLedgerByDate(String date, int startIndex, int limit){
		return farmerDAO.listLoanLedgerByDate(date,startIndex,limit);
	}

	@Override
	public List<Farmer> listFarmerWithOutstandLoanBal(String villageCode) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerWithOutstandLoanBal(villageCode);
	}

	@Override
	public List<Object[]> listFarmerByLoanDistribution() {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerByLoanDistribution();
	}
	
	@Override
	public List<byte[]> getImageByQuery(String methodName, Object[] parameter, String branchId) {
		// TODO Auto-generated method stub
		return farmerDAO.getImageByQuery(methodName, parameter, branchId);
	}
	
	@Override
	public List<Long> listFarmerPrimaryId(Farmer f) {
		return farmerDAO.listFarmerPrimaryId(f);
	}

	@Override
	public List<Long> listProfilePrimaryId(Agent agent) {
		return farmerDAO.listProfilePrimaryId(agent);
	}

	@Override
	public List<Long> listWebUsersPrimaryId(User user) {
		return farmerDAO.listWebUsersPrimaryId(user);
	}

	@Override
	public List<Object[]> listFarmerFarmInfoByLotNoFromFarmerTraceabilityData(String selectedLotNo, String branch) {
		// TODO Auto-generated method stub
		return farmerDAO.listFarmerFarmInfoByLotNoFromFarmerTraceabilityData(selectedLotNo,branch);
	}

	@Override
	public Object[] findFarmersCountFromLotTraceByLotNo(String selectedLotNo) {
		// TODO Auto-generated method stub
		return farmerDAO.findFarmersCountFromLotTraceByLotNo(selectedLotNo);
	}
}
