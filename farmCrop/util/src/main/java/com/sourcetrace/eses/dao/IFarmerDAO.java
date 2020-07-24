/*
 * IFarmerDAO.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.profile.ViewFarmerActivity;
import com.ese.entity.traceability.FarmerTraceability;
import com.ese.entity.txn.mfi.InterestCalcConsolidated;
import com.ese.entity.util.FarmCropsField;
import com.ese.entity.util.FarmField;
import com.ese.entity.util.FarmerField;
import com.ese.entity.util.FarmerLocationMapField;
import com.ese.entity.util.LoanInterest;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.ColdStorageDetail;
import com.sourcetrace.eses.entity.DynamicFeildMenuConfig;
import com.sourcetrace.eses.entity.DynamicFieldConfig;
import com.sourcetrace.eses.entity.DynamicFieldReportConfig;
import com.sourcetrace.eses.entity.DynamicFieldReportJoinMap;
import com.sourcetrace.eses.entity.DynamicFieldScoreMap;
import com.sourcetrace.eses.entity.DynamicMenuFieldMap;
import com.sourcetrace.eses.entity.DynamicSectionConfig;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.FarmerFeedbackEntity;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.SamithiIcs;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseStorageMap;
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
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Calf;
import com.sourcetrace.esesw.entity.profile.CottonPrice;
import com.sourcetrace.esesw.entity.profile.Cow;
import com.sourcetrace.esesw.entity.profile.DataLevel;
import com.sourcetrace.esesw.entity.profile.DistributionBalance;
import com.sourcetrace.esesw.entity.profile.DocumentUpload;
import com.sourcetrace.esesw.entity.profile.DynamicConstants;
import com.sourcetrace.esesw.entity.profile.DynamicImageData;
import com.sourcetrace.esesw.entity.profile.DynamicReportFieldsConfig;
import com.sourcetrace.esesw.entity.profile.DynamicReportTableConfig;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.Expenditure;
import com.sourcetrace.esesw.entity.profile.Farm;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
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
import com.sourcetrace.esesw.entity.profile.OfflineFarmerEnrollment;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.ResearchStation;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.TreeDetail;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.entity.Agent;

/**
 * The Interface IFarmerDAO.
 */
public interface IFarmerDAO extends IESEDAO {

	/**
	 * Find farmer by farmer id.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @return the farmer
	 */
	public Farmer findFarmerByFarmerId(String farmerId);

	/**
	 * Find farmer by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farmer
	 */
	public Farmer findFarmerById(Long id);

	/**
	 * Find farm by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farm
	 */
	public Farm findFarmById(Long id);

	/**
	 * Find farm by farm code.
	 * 
	 * @param farmCode
	 *            the farm code
	 * @return the farm
	 */
	public Farm findFarmByFarmCode(String farmCode);

	/**
	 * Find farm by farm name.
	 * 
	 * @param farmName
	 *            the farm name
	 * @return the farm
	 */
	public Farm findFarmByFarmName(String farmName);

	/**
	 * Removes the farm by farmer id.
	 * 
	 * @param id
	 *            the id
	 */
	public void removeFarmByFarmerId(long id);

	/**
	 * Find farm by code.
	 * 
	 * @param code
	 *            the code
	 * @return the farm
	 */
	public Farm findFarmByCode(String code);

	/**
	 * List farm by farmer id.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @return the list< farm>
	 */
	public List<Farm> listFarmByFarmerId(long farmerId);

	/**
	 * Checks if is city mappingexist.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is city mappingexist
	 */
	public boolean isCityMappingexist(long id);

	/**
	 * List farmer by rev no and city.
	 * 
	 * @param revisionNo
	 *            the revision no
	 * @param id
	 *            the id
	 * @return the list< farmer>
	 */
	public List<Farmer> listFarmerByRevNoAndCity(Long revisionNo, long id);

	/**
	 * Checks if is village mapping exist.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is village mapping exist
	 */
	public boolean isVillageMappingExist(long id);

	/**
	 * Find farmer by farmer name.
	 * 
	 * @param selectedFarmer
	 *            the selected farmer
	 * @return the farmer
	 */
	public Farmer findFarmerByFarmerName(String selectedFarmer);

	/**
	 * Find farmer by village code.
	 * 
	 * @param villageCode
	 *            the village code
	 * @return the list< farmer>
	 */
	public List<Farmer> findFarmerByVillageCode(String villageCode);

	/**
	 * List farmer.
	 * 
	 * @return the list< farmer>
	 */
	public List<Farmer> listFarmer();

	/**
	 * List offline farmer enrollment by status code.
	 * 
	 * @param statusCode
	 *            the status code
	 * @return the list< offline farmer enrollment>
	 */
	public List<OfflineFarmerEnrollment> listOfflineFarmerEnrollmentByStatusCode(int statusCode);

	/**
	 * List offline farmer enrollment by status enrollment type.
	 * 
	 * @param statusCode
	 *            the status code
	 * @param txnType
	 *            the txn type
	 * @return the list< offline farmer enrollment>
	 */
	public List<OfflineFarmerEnrollment> listOfflineFarmerEnrollmentByStatusEnrollmentType(int statusCode,
			String txnType);

	/**
	 * Find farm photo by id.
	 * 
	 * @param id
	 *            the id
	 * @return the byte[]
	 */
	public byte[] findFarmPhotoById(Long id);

	/**
	 * List seasons.
	 * 
	 * @return the list< season>
	 */
	public List<Season> listSeasons();

	/**
	 * Find season by id.
	 * 
	 * @param id
	 *            the id
	 * @return the season
	 */
	public Season findSeasonById(Long id);

	/**
	 * List farmer by city id.
	 * 
	 * @param id
	 *            the id
	 * @return the list< farmer>
	 */
	public List<Farmer> listFarmerByCityId(long id);

	/**
	 * List farmer by village id.
	 * 
	 * @param id
	 *            the id
	 * @return the list< farmer>
	 */
	public List<Farmer> listFarmerByVillageId(long id);

	/**
	 * Find farmer account statu by id.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @return the string
	 */
	public String findFarmerAccountStatuById(String farmerId);

	/**
	 * List active farmers by village code.
	 * 
	 * @param villageCode
	 *            the village code
	 * @return the list< farmer>
	 */
	public List<Farmer> listActiveFarmersByVillageCode(String villageCode);

	/**
	 * List active farmer by city code.
	 * 
	 * @param cityCode
	 *            the city code
	 * @return the list< farmer>
	 */
	public List<Farmer> listActiveFarmerByCityCode(String cityCode);

	/**
	 * Find contract by farmer id season code procurement product.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @param seasonCode
	 *            the season code
	 * @param procurementProductCode
	 *            the procurement product code
	 * @return the contract
	 */
	public Contract findContractByFarmerIdSeasonCodeProcurementProduct(String farmerId, String seasonCode,
			String procurementProductCode);

	/**
	 * List active contract farmers by village code season code.
	 * 
	 * @param villageCode
	 *            the village code
	 * @param seasonCode
	 *            the season code
	 * @return the list< farmer>
	 */
	public List<Farmer> listActiveContractFarmersByVillageCodeSeasonCode(String villageCode, String seasonCode);

	/**
	 * Find account by seasson procurment product farmer.
	 * 
	 * @param seasonId
	 *            the season id
	 * @param procurementProductId
	 *            the procurement product id
	 * @param farmerId
	 *            the farmer id
	 * @return the eSE account
	 */
	public ESEAccount findAccountBySeassonProcurmentProductFarmer(long seasonId, long procurementProductId,
			long farmerId);

	/**
	 * Find contract by seasson procurment product farmer.
	 * 
	 * @param seasonId
	 *            the season id
	 * @param procurementProductId
	 *            the procurement product id
	 * @param farmerId
	 *            the farmer id
	 * @return the contract
	 */
	public Contract findContractBySeassonProcurmentProductFarmer(long seasonId, long procurementProductId,
			long farmerId);

	/**
	 * Find contract by account no.
	 * 
	 * @param accountNo
	 *            the account no
	 * @return the contract
	 */
	public Contract findContractByAccountNo(String accountNo);

	/**
	 * Checks if is farmer contract mapping exist.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @return true, if is farmer contract mapping exist
	 */
	public boolean isFarmerContractMappingExist(String farmerId);

	/**
	 * List active contract farmers by season rev no and city.
	 * 
	 * @param cityId
	 *            the city id
	 * @param seasonCode
	 *            the season code
	 * @param revisionNo
	 *            the revision no
	 * @return the list< farmer>
	 */
	public List<Farmer> listActiveContractFarmersBySeasonRevNoAndCity(Long cityId, String seasonCode, Long revisionNo);

	/**
	 * List active contract farmers by season procurement product.
	 * 
	 * @param seasonId
	 *            the season id
	 * @param procurementProductId
	 *            the procurement product id
	 * @return the list< farmer>
	 */
	public List<Farmer> listActiveContractFarmersBySeasonProcurementProduct(long seasonId, long procurementProductId);

	/**
	 * List active contract farmers by season procurement product village.
	 * 
	 * @param seasoncode
	 *            the seasoncode
	 * @param productCode
	 *            the product code
	 * @param selectedVillage
	 *            the selected village
	 * @return the list< farmer>
	 */
	public List<Farmer> listActiveContractFarmersBySeasonProcurementProductVillage(String seasoncode,
			String productCode, String selectedVillage);

	/**
	 * Find active contract by farmer id procurement product code not in season
	 * code.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @param procurementProductCode
	 *            the procurement product code
	 * @param seasonCode
	 *            the season code
	 * @return the contract
	 */
	public Contract findActiveContractByFarmerIdProcurementProductCodeNotInSeasonCode(String farmerId,
			String procurementProductCode, String seasonCode);

	/**
	 * Find active contract by farmer id not in season code.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @param procurementProductCode
	 *            the procurement product code
	 * @param seasonCode
	 *            the season code
	 * @return the contract
	 */
	public Contract findActiveContractByFarmerIdNotInSeasonCode(String farmerId, String procurementProductCode,
			String seasonCode);

	/**
	 * Find farmer current contract status by id.
	 * 
	 * @param id
	 *            the id
	 * @param seasonId
	 *            the season id
	 * @return true, if successful
	 */
	public boolean findFarmerCurrentContractStatusById(Long id, Long seasonId);

	/**
	 * Find season by season code.
	 * 
	 * @param seasonCode
	 *            the season code
	 * @return the long
	 */
	public Long findSeasonBySeasonCode(String seasonCode);

	/**
	 * List farm.
	 * 
	 * @return the list< farm>
	 */
	public List<Farm> listFarm();

	/**
	 * Update farmer modify time.
	 * 
	 * @param id
	 *            the id
	 * @param tempDate
	 *            the temp date
	 */
	public void updateFarmerModifyTime(long id, Date tempDate);

	/**
	 * List active contract farmers by season rev no and samithi.
	 * 
	 * @param id
	 *            the id
	 * @param currentSeasonCode
	 *            the current season code
	 * @param revisionNo
	 *            the revision no
	 * @return the list< farmer>
	 */
	public List<Farmer> listActiveContractFarmersBySeasonRevNoAndSamithi(long id, String currentSeasonCode,
			Long revisionNo);

	/**
	 * Removes the contract by farmer id.
	 * 
	 * @param farmerId
	 *            the farmer id
	 */
	public void removeContractByFarmerId(long farmerId);

	/**
	 * List active contract farmers by village code season code samithi id.
	 * 
	 * @param villageCode
	 *            the village code
	 * @param seasonCode
	 *            the season code
	 * @param samithiId
	 *            the samithi id
	 * @return the list< farmer>
	 */
	public List<Farmer> listActiveContractFarmersByVillageCodeSeasonCodeSamithiId(String villageCode, String seasonCode,
			long samithiId);

	/**
	 * List active contract farmers by village code season code samithi code.
	 * 
	 * @param villageCode
	 *            the village code
	 * @param seasonCode
	 *            the season code
	 * @param samithiCode
	 *            the samithi code
	 * @return the list< farmer>
	 */
	public List<Farmer> listActiveContractFarmersByVillageCodeSeasonCodeSamithiCode(String villageCode,
			String seasonCode, String samithiCode);

	/**
	 * List farmer by city and village id.
	 * 
	 * @param cityId
	 *            the city id
	 * @param villageId
	 *            the village id
	 * @return the list< farmer>
	 */
	public List<Farmer> listFarmerByCityAndVillageId(long cityId, long villageId);

	/**
	 * List farmer with account.
	 * 
	 * @return the list< object[]>
	 */
	public List<Object[]> listFarmerWithAccount();

	/**
	 * List farmer by cooperative certificate standard village.
	 * 
	 * @param selectedCooperative
	 *            the selected cooperative
	 * @param selectedCertificateStandard
	 *            the selected certificate standard
	 * @param selectedVillage
	 *            the selected village
	 * @return the list< object[]>
	 */
	public List<Object[]> listFarmerByCooperativeCertificateStandardVillage(String selectedCooperative,
			String selectedCertificateStandard, String selectedVillage);

	/**
	 * Find head of family by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farmer family
	 */
	public FarmerFamily findHeadOfFamilyById(long id);

	/**
	 * Find farmer family by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farmer family
	 */
	public FarmerFamily findFarmerFamilyById(long id);

	/**
	 * Find farmer family by name.
	 * 
	 * @param name
	 *            the name
	 * @param farmerId
	 *            the farmer id
	 * @return the farmer family
	 */
	public FarmerFamily findFarmerFamilyByName(String name, long farmerId);

	/**
	 * Removes the farmer economy mapping sql.
	 * 
	 * @param farmerEconomy
	 *            the farmer economy
	 */
	public void removeFarmerEconomyMappingSQL(FarmerEconomy farmerEconomy);

	/**
	 * Find harvest season by code.
	 * 
	 * @param code
	 *            the code
	 * @return the harvest season
	 */
	public HarvestSeason findHarvestSeasonByCode(String code);

	/**
	 * Find harvest season by name.
	 * 
	 * @param name
	 *            the name
	 * @return the harvest season
	 */
	public HarvestSeason findHarvestSeasonByName(String name);

	/**
	 * Find harvest season by id.
	 * 
	 * @param id
	 *            the id
	 * @return the harvest season
	 */
	public HarvestSeason findHarvestSeasonById(Long id);

	/**
	 * Find harvest data by id.
	 * 
	 * @param id
	 *            the id
	 * @return the harvest data
	 */
	public HarvestData findHarvestDataById(Long id);

	/**
	 * Find farm inventory by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farm inventory
	 */
	public FarmInventory findFarmInventoryById(String id);

	/**
	 * Find farm inventory item.
	 * 
	 * @param farmId
	 *            the farm id
	 * @param inventoryItem
	 *            the inventory item
	 * @return the farm inventory
	 */
	public FarmInventory findFarmInventoryItem(long farmId, int inventoryItem);

	/**
	 * Find animal husbandary by id.
	 * 
	 * @param id
	 *            the id
	 * @return the animal husbandary
	 */
	public AnimalHusbandary findAnimalHusbandaryById(String id);

	/**
	 * Checks if is farm mapping farm inventory.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is farm mapping farm inventory
	 */
	public boolean isFarmMappingFarmInventory(long id);

	/**
	 * Checks if is farm mapping animal husbandry.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is farm mapping animal husbandry
	 */
	public boolean isFarmMappingAnimalHusbandry(long id);

	/**
	 * Checks if is farm mapped farm crops.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is farm mapped farm crops
	 */
	public boolean isFarmMappedFarmCrops(long id);

	/**
	 * List of certified farmers.
	 * 
	 * @return the list< farmer>
	 */
	public List<Farmer> listOfCertifiedFarmers();

	/**
	 * List farmer with account by revision date.
	 * 
	 * @param revisionDate
	 *            the revision date
	 * @return the list< object[]>
	 */
	public List<Object[]> listFarmerWithAccountByRevisionDate(Date revisionDate);

	/**
	 * Find active contract farmers latest revision no by agent and season.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param seasonCode
	 *            the season code
	 * @return the long
	 */
	public Long findActiveContractFarmersLatestRevisionNoByAgentAndSeason(long agentId, String seasonCode);

	/**
	 * List active contract farmers account by agent and season.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param seasonCode
	 *            the season code
	 * @param revisionDate
	 *            the revision date
	 * @return the list< object[]>
	 */
	public List<Object[]> listActiveContractFarmersAccountByAgentAndSeason(long agentId, String seasonCode,
			Date revisionDate);

	/**
	 * Find interest calc consolidated byfarmer profile id.
	 * 
	 * @param farmerProfileId
	 *            the farmer profile id
	 * @return the interest calc consolidated
	 */
	public InterestCalcConsolidated findInterestCalcConsolidatedByfarmerProfileId(String farmerProfileId);

	/**
	 * Update interest calc consolidated.
	 * 
	 * @param interestCalcConsolidated
	 *            the interest calc consolidated
	 */
	public void updateInterestCalcConsolidated(InterestCalcConsolidated interestCalcConsolidated);

	/**
	 * List interest calc history by date.
	 * 
	 * @param formerId
	 *            the former id
	 * @param date
	 *            the date
	 * @return the list< interest calc history>
	 */
	public List<InterestCalcHistory> listInterestCalcHistoryByDate(String formerId, Date date);

	/**
	 * Update farmer.
	 * 
	 * @param farmer
	 *            the farmer
	 */
	public void updateFarmer(Farmer farmer);

	/**
	 * Update farm.
	 * 
	 * @param farm
	 *            the farm
	 */
	public void updateFarm(Farm farm);

	/**
	 * Find farm byfarm id.
	 * 
	 * @param farmId
	 *            the farm id
	 * @return the farm
	 */
	public Farm findFarmByfarmId(String farmId);

	/**
	 * Removes the unmapped farm crop object.
	 */
	public void removeUnmappedFarmCropObject();

	/**
	 * Update farmer revision no.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @param revisionNo
	 *            the revision no
	 */
	public void updateFarmerRevisionNo(Long farmerId, Long revisionNo);

	/**
	 * Find farm by farm name and farmer id.
	 * 
	 * @param farmName
	 *            the farm name
	 * @param id
	 *            the id
	 * @return the farm
	 */
	public Farm findFarmByFarmNameAndFarmerId(String farmName, Long id);

	/**
	 * List of farmers count.
	 * 
	 * @return the long
	 */
	public long listOfFarmersCount();

	/**
	 * Find farmer list.
	 * 
	 * @return the list< farmer>
	 */
	public List<Farmer> findFarmerList();

	/**
	 * Deleteelemetby id.
	 * 
	 * @param fileid
	 *            the fileid
	 */
	public void deleteelemetbyId(Long fileid);

	public List<FarmCatalogue> listFarmAnimalBasedOnType();

	public FarmCrops findFarmCropsByFarmCode(Long farmId);

	public FarmDetailedInfo findTotalLandHoldingById(Long id);

	public List<String[]> listOfFarmerCode();

	public List<String[]> listOfFarmerName();

	public List<Cultivation> findCostOfCultivationsByFarmerCode(String farmerCode, String farmCode);

	public List<CultivationDetail> findCultivationDetailsByCultivationId(long id);

	public List<Object[]> findCultivationCost(String farmerCode, String farmCode);

	public Object findCottonIncomeByFarmerCode(String farmerCode, String farmCode);

	public Farmer findFarmerByFarmerCode(String farmerCode);

	public List<String[]> listFarmerName();

	public List<String[]> listFatherName();

	public List<String[]> listFarmerCode();

	public double findCottonIncomeByFarmerCodeAndFarmCode(String farmerId, String farmCode);

	public List<HarvestSeason> listHarvestSeasons();

	public Farm findFarmByfarmId(Long farmId);

	public List<AnimalHusbandary> listAnimalHusbandaryList(Long farmId);

	public void updateAnimalInventory(AnimalHusbandary animalHusbandary);

	public List<CropHarvest> findCropHarvestByFarmCode(String farmCode);

	public List<CropSupply> findCropSupplyByFarmCode(String farmCode);

	public byte[] findfarmerVerificationFarmerVoiceById(Long id);

	public List<FarmCatalogue> listFarmEquipmentBasedOnType();

	public void updateFarmInventory(FarmInventory fm);

	public FarmInventory findFarmInventoryItem(long farmId, String inventoryItem);

	public List<FarmInventory> listFarmInventryList(Long farmerId);

	public Object findAnimalHusbandaryByFarmAndOtherItems(long farmId, long animalStr, String revenueStr, long housStr);

	public List<FarmCatalogue> listFodderBasedOnType();

	public List<FarmCatalogue> listAnimalHousingBasedOnType();

	public List<FarmCatalogue> listRevenueBasedOnType();

	public CropSupply findcropSupplyId(String id);

	public List<CropSupplyDetails> findCropSupplyDetailId(long id);

	public List<Farm> listFarmerFarmByFarmerId(String farmerId);

	public List<Farm> listfarmbyVillageId(Long id);

	public List<Object[]> listPeriodicInspectionFarmer();

	public List<Object[]> listPeriodicInspectionFarm();

	public List<FarmCrops> listOfCrops(long id);

	public List<Farmer> listOfFarmers(String selectedVillage);

	public List<FarmCrops> listOfCropNames(String selectedCropType, long farmId);

	public List<FarmCrops> listOfVariety(String selectedCropName);

	public List<ProcurementGrade> listOfGrade(String selectedVariety);

	public Double findGradePrice(String selectedGrade);

	public List<String[]> listIcsName();

	public List<Warehouse> listSamithiName();

	public List<Farm> listFarmName(String branchId);

	public List<BankInformation> findFarmerBankinfo(long id);

	public long listOfFarmersCountBasedOnBranch(String branchIdValue);

	public boolean findCurrentSeason();

	public List<java.lang.Object[]> listFertilizerAppliedAndPestAppliedWithQty(String farmCode, String cropCode);

	public Cultivation findByCultivationId(String id);

	public List<Object[]> listCropSupplyChartDetails();

	public List<Object[]> listCropSupplyQtyChartDetails();

	public List<Object[]> listCropHarvestQtyChartDetails();

	public List<Object[]> listCropHarvestBuyerChartDetails();

	public List<FarmCatalogue> listCatelogueType(String type);

	public List<String[]> listFarmerId();

	public Integer findFarmerCountByMonth(Date sDate, Date eDate);

	public List<Object> listFarmerCountByGroup();

	public List<FarmCatalogue> listMachinary();

	public List<FarmCatalogue> listPolyHouse();

	public FarmElement findFarmElementById(Long templateId);

	public List<FarmElement> listFarmElementList(Long farmId);

	public FarmElement findFarmElementItem(Long farmId, String machStr);

	public void updateFarmElement(FarmElement element);

	public List<FarmElement> listMachinaryList(Long farmId, String machType);

	public MasterData findMasterDataIdByCode(String farmerId);

	public Integer findFarmersCount();

	public FarmCrops findByFarmCropsId(Long cropId);

	public Farmer findFarmerByFarmerId(String farmerId, String tenantId);

	public ESEAccount findAccountBySeassonProcurmentProductFarmer(long seasonId, long procurementProductId,
			long farmerId, String tenantId);

	public InterestCalcConsolidated findInterestCalcConsolidatedByfarmerProfileId(String farmerProfileId,
			String tenantId);

	public void updateInterestCalcConsolidated(InterestCalcConsolidated intCalConsolidated, String tenantId);

	public List<Object[]> listFarmCropsByVillage(String selectedVillage, String branchId);

	public List<Object[]> listFarmCropsByVillage(String selectedVillage, int startIndex, int limit, String branchId);

	public List<Object[]> listFarmCropsDetailsByVillageCode(String selectedVillage, int startIndex, int limit,
			String branchId);

	public List<Object[]> listFarmCropsByCropCode(String selectedCropCode, int startIndex, int limit, String branchId);

	public List<Object[]> listFarmCropsByCropCode(String selectedCropCode, String branchId);

	public List<Object[]> listFarmCropsDetailsByCropCode(String selectedCropCode, String branchId);

	public List<Object[]> listFarmCropsDetailsByCropCode(String selectedCropCode, int startIndex, int limit,
			String branchId);

	public List<FarmCatalogue> listSeedTreatmentDetailsBasedOnType();

	public FarmCatalogue findfarmcatalogueById(String farmCatalogueId);

	public List<Object[]> listFarmCropsDetailsByVillageCode(String selectedVillage, String branchId);

	public void updateFarmerImageInfo(long id, long imageInfoId);

	public List<Object> listPeriodicInsoectionFatherName();

	public List<String[]> listByFatherNameList();

	public List<Object[]> listHarvestSeasonFromToPeriod();

	public List<Object[]> listFarmerCodeIdNameByVillageCode(String villageCode);

	public List<Object[]> listSeasonCodeAndName();

	public HarvestSeason findSeasonNameByCode(String seasonCode);

	public List<Object[]> listFarmerInfo();

	public List<FarmerSoilTesting> listFarmerSoilTestingByFarmId(String farmId);

	public List<Object[]> listfarmingseasonlist();

	public String findHarvestSeasonBycodeusingname(String name);

	public List<FarmerLandDetails> listFarmingSystemByFarmId(long id);

	public FarmerLandDetails findFarmerLandDetailsById(long id);

	public void deleteFarmerLandDetailById(long farmerLandId);

	public String findSanghamTypeFromWarehouseByWarehouseCode(String warehouseCode);

	public List<FarmerSourceIncome> listFarmSourceIncomeByFarmerId(String farmerId);

	public List<FarmerIncomeDetails> listFarmerIncomeDetailsBySourceIncomeId(String id);

	/* public void deleteFarmerIncomeDetails(String id); */

	public void deleteFarmerIncomeDetails(String tableName, String columnName);

	public void updateFarmerIdInFarmerSourceIncome(String sourceIncomeIds, String farmerId);

	public List<Expenditure> listExpentitureListByFarmId(Long id);

	public HarvestSeason findHarvestSeasosnByName(String name);

	public Warehouse findWareHouseByFarmerId(String farmerId);

	public DocumentUpload findDocumentById(Long id);

	public String findBySeasonCode(String seasonCode);

	public List<FarmCatalogue> listCatelogueTypeWithOther(String type);

	public List<FarmerCropProdAnswers> listFarmerwithCategoryCode(String categoryCode);

	public List<FarmersQuestionAnswers> listQuestionBySection(long id);

	public List<Object> listFarmerCountByBranch();

	public List<Object> listTotalFarmAcreByBranch(String branchId);

	public List<Object> listSeedTypeCount();

	public List<Object> listSeedSourceCount();

	public List<Object> listSeedSourceCountBySource(String selectedSeedSource);

	public Integer findFarmersCountByStatus();

	public List<Object[]> listFarmInfo();

	public Object[] findFarmerAndFatherNameByFarmerId(String farmerId);

	public List<Farmer> listFarmerByFarmerId(long id);

	public List<Object> listFarmerCountByGroupAndBranch(String branchId, Integer selectedYear);

	public Integer findFarmersCountByBranch(String branchId);

	public List<Object> findTotalFarmAcreByBranch(String selectedBranch);

	public Long findInputCostByBranch(String branchId);

	public Long findInputCost();

	public Integer findTotalCultivationProdLandByBranch(String selectedBranch, Integer selectedYear);

	// public Integer findTotalCultivationProdLand();

	public Long findTotalIncomeFromCottonByBranch(String selectedBranch, Integer selectedYear);

	// public Long findTotalIncomeFromCotton();

	public Long findCultivatedFarmersCount();

	public Long findCultivatedFarmersCountByBranch(String branchId);

	public List<PeriodicInspectionData> findPeriodicDataByType(String value, long id);

	public List<PeriodicInspectionSymptom> findPeriodicSymptomsByType(String value, long id);

	public List<PeriodicInspectionData> listPestRecomentedById(Long inspId);

	public List<PeriodicInspectionData> listFungicideById(Long inspId);

	public List<PeriodicInspectionData> listManureById(Long inspId);

	public List<PeriodicInspectionData> listFertilizerById(Long inspId);

	public List<Object[]> listFarmerFarmInfo();

	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm, String selectedOrganicStatus, String selectedFarmer);

	public void updateFarmerUtzStatus(String farmId, String certificationStatus);

	public void updateSamithiUtzStatus(String farmerId, String certificationStatus);

	public void editFarmerStatus(long id, int statusCode, String statusMsg);

	public List<Object[]> listPeriodicInspectionVillage();

	public int findLandPreparationDetailsByFarmCode(String farmCode, String seasonCode);

	public PeriodicInspection findperiodicInspectionByBranchIdAndFarmId(String branchId, String farmId);

	public int findWeedingStatusByCode(String branchId, String Code, String farmCode, String seasonCode);

	public int findUsageCountforFertilizerFromCultivationDetails(String branchId, String Code, String farmCode,
			Long type, String seasonCode);

	public void updateFarmerRevisionNoAndBasicInfo(long farmerId, Long revisionNo, int basicInfo);

	public int findPickingStatusByCode(String branchId, String Code, String farmCode, String seasonCode);

	public Farm findFarmByFarmerId(long farmerId);

	public List<Object[]> listFarmByFarmerrId(String farmerId);

	public List<Object[]> listFarmerInfoByCultivation(String branch);

	public List<Object[]> listFarmerInfoByDistribution();

	public List<Object[]> listFarmerInfoByProcurement();

	public List<Object[]> listFarmerInfoByCropHarvest();

	public List<Object[]> listFarmerInfoByCropSale();

	public ResearchStation findResearchStation(long id);

	public List<ResearchStation> listResearchStationByRevNo(Long revNo);

	public List<CultivationDetail> findCultivationDetailsByCultivationIdAndSession(long id);

	public List<Cultivation> findCostOfCultivationsByFarmerCodeAndSeason(String farmerId, String farmCode,
			String seasonCode);

	public List<Object[]> listFarmInfoByCropHarvest();

	public List<Object[]> listOfVillageByCultivation();

	public Integer findFarmCountByMonth(Date sDate, Date eDate);

	public Integer findFarmsCountByBranch(String branchId);

	public Integer findFarmsCount();

	public Integer findFarmCropCountByMonth(Date sDate, Date eDate);

	public Integer findFarmsCropCountByBranch(String branchId);

	public Integer findFarmCropCount();

	public String findFarmTotalLandAreaCount();

	public Cow findCowByCowId(long id);

	public List<CostFarming> listCostFarming();

	public List<Cow> findByCows(String farmCode);

	public ResearchStation findResearchStationId(String researchStationId);

	public Cow findCowId(long cowId);

	public List<Calf> listOfCalfs(long cowId);

	public HousingInfo findByHousingInfo(long farmId);

	public HousingInfo findByHousingId(Long housingId);

	public Cow findByCowId(String cowId);

	public Date findByLastInspDate(String cowId);

	public List<Object[]> findByCowInspFarmer();

	public List<Object[]> findByCowInspFarm();

	public List<Object[]> findByResearchStation();

	public List<String> findByCowList();

	public CowInspection findCowInspectionById(Long id);

	public byte[] findCowInspectionCowVoiceById(Long id);

	public Integer findCowCount();

	public Integer findCowCountByMonth(Date firstDateOfMonth, Date date);

	public void updateResearchStatRevisionNo(long id, long revisionNumber);

	public List<Object[]> findByResearchStationList();

	public List<CostFarming> findByCowList(long cowId);

	public List<Object[]> findFarmInfoByFarmerId(Long id);

	public List<Object> findMilkingCountByCow(String isMilking);

	public List<Object> listCowCountByVillage();

	public List<Object> listCowCountByRS();

	public List<Object> listCowCountByDiseaseName();

	public List<Object> listTotalFarmAcreByVillage();

	public List<Object> findByCowCost();

	public List<CowInspection> findByCowList(String id);

	public Farm findFarmByFarmId(String farmId);

	public List<Object[]> listActiveContractFarmersFieldsBySeasonRevNoAndSamithi(long id, String currentSeasonCode,
			Long revisionNo);

	public List<Object[]> listFarmFieldsByFarmerId(List<Long> farmerId);

	public List<Object[]> listFarmCropsFieldsByFarmerId(Long farmId);

	public List<Cow> listCowFieldsByFarmId(Long farmId);

	public List<Object[]> listFarmIcsByFarmId();

	public InterestCalcConsolidated findInterestCalcConsolidatedByfarmerProfileIdOpt(String farmerProfileId);

	public long listOfFarmerCountBySamithiId(long id);

	public Integer findMinVarietyIntervalDays();

	public List<Object[]> findFarmCropProductList();

	public List<Object[]> findSowingDateAndInterval(String vCode);

	public Object[] findFarmFarmerAndYieldCount(String varietyCode);

	public List<Cultivation> findCOCByFarmerIdFarmIdCropCodeSeason(String farmerId, String farmId, String cropCode,
			String seasonCode);

	public List<Object[]> findCropProductBySeason(String seasonCode, String varietyCode);

	public Object[] findFarmFarmerAndYieldCountBySeason(String seasonCode);

	public List<FarmCrops> listOfVarietyByCropTypeFarmCodeAndCrop(String selectedCropType, String selectedFarm,
			String selectedCropName);

	public String findFarmTotalLandAreaCount(String BranchId);

	public List<PeriodicInspectionData> listDiseaseById(Long inspId);

	public long findFarmerCropProdAnswerByFarmerIdAndCategoryCode(String farmerId, String code);

	public List<Cultivation> listCultivationExpenses();

	public List<Object[]> findFarmerCountWithPhotos(String branchId);

	public List<Object[]> findFarmCountWithPhotos(String branchId);

	public List<Object[]> findFarmCountWithGPSTag(String branchId);

	public List<Object> findFarmerCountByICconversion(String selectedBranch, Integer selectedYear,
			String selectedSeason, String selectedGender, String selectedState);

	public List<Object> findTotalFarmAcreAndYieldByBranch(String selectedBranch, Integer selectedYear);

	public Integer findTotalFarmerCount(String selectedBranch, String selectedStaple, String selectedSeason,
			String selectedGender, String selectedState);

	public Integer findTotalFarmCount(String selectedBranch, Integer selectedYear);

	public List<Object[]> listOfCropNamesByCropTypeAndFarm(String selectedCropType, String selectedFarm);

	public String findCropHarvestByYearAndBranch(String selectedBranch, Integer selectedYear);

	public Double findCropSupplyByYearAndBranch(String selectedBranch, Integer selectedYear);

	public Integer findFarmerCountByState(String selectedBranch, Integer selectedState);

	public List<Object> findTotalFarmAcreAndYieldByState(String selectedBranch, Long selectedState);

	public Long findTotalIncomeFromCottonByState(String selectedBranch, String selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender);

	public Integer findTotalCultivationProdLandByState(String selectedBranch, String selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender);

	public List<FarmerField> listRemoveFarmerFields();

	public List<FarmField> listRemoveFarmFields();

	public List<Object[]> listFarmInfoByFarmerId(long id);

	public List<Object[]> findCountOfFarmerEnrollment(List<String> agent);

	public List<FarmerField> findFarmerFieldByType(String type);

	public List<FarmerField> listFarmerFields();

	public Object[] findFarmerCodeNameVillageSamithibyFarmerId(String farmerId);

	public List<Object[]> listFarmerInfoByProductReturn();

	public Warehouse findSamithiByFarmerId(String farmerId);

	public List<Object> listTotalFarmAcreByFpo();

	public List<PMT> lisReceiptNumberList();

	public Cultivation findCultivationByCultivationId(long cultiId);

	public ProcurementProduct findCropByCropCode(String cropCode);

	public List<Object[]> listFarmerIDAndNameByFarmerID(String farmerId);

	public List<FarmCrops> findFarmCropsByFarmerIdAndProcId(long id, long proId);

	public void updatePeriodicInspectionData(String farmId, String cropCode, String category, String type);

	public List<Object[]> listActiveContractFarmersFieldsBySeasonRevNoAndSamithi(long id, String currentSeasonCode,
			Long revisionNo, List<String> branch);

	/** DASHBOARD MIGRATION */
	public List<Object> findTotalFarmAcreAndYieldAndHarvstByBranch(String selectedBranch, int selectedYear);

	public String findTotalQtyByFarm(List<String> farmCode);

	public Object[] listObjectCultivationExpenses(String selectedBranch, String selectedSeason, String selectedYear);

	public List<String> findFarmerIdsByfarmCode(List<String> farmCodes);

	public Double findCottonIncByFarmerCode(List<String> farmerIds, List<String> farmCodes);

	public String findTotalLandByFarmCode(List<String> farmerIds, List<String> farmCodes);

	public List<Object> findTotalFarmAcreAndYieldAndStapleByBranch(String selectedBranch, int parseInt,
			List<String> selectedStapleLen);

	public List<String> findTotalFarmCodeByBranch(String selectedBranch, int selectedYear, String selectedSeason);

	public List<String> findTotalFarmCodeByBranch(String selectedBranch, int selectedYear, List<String> stapleCode);

	public String findTotalLandAcre(String selectedBranch, int selelctedYear, String selectedSeason);

	public List<String> findFarmCodesByCultivation(String selectedBranch, String selectedSeason, String selectedYear);

	public String findByFarmCode(String farmCode);

	public double findTotalCottonAreaCount();

	public double findTotalCottonAreaCountByMonth(Date sDate, Date eDate);

	public List<Object> findFarmerDetailsByStateAndCrop(String selectedBranch, Long selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender);

	public Integer findFarmerCountByStateAndCrop(String selectedBranch, int selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender);

	public void updateFarmerStatus(long id);

	public List<Object> listFarmerCountByGroupAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedGender, String selectedCooperative, String selectedCrop, String typez,
			String selectedVillage);

	public FarmCatalogue findfarmcatalogueByCode(String code);

	public Integer findTotalFarmerCountByStapleLength(String selectedBranch, int selectedYear,
			List<String> selectedStapleLen);

	public Double findTotalYieldByBranch(String selectedBranch, String selectedStaple, String selectedSeason,
			String selectedState);

	public List<Object> listFarmerCountByFpoGroup();

	public Double findCocCostByCropBranchCooperativeAndGender(String branchId, String selectedCooperative,
			String selectedCrop, String selectedGender, String selectedYear, String selectedState);

	public List<Object> listSeedTypeCountByBranch(String selectedSeedType, String selectedCrop,
			String selectedCooperative, String selectedGender);

	public List<Object> listSeedSourceCountBySource(String selectedBranch, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedVariety, String selectedState);

	public List<Object> listTotalFarmAcreByBranch();

	public List<Object[]> listFarmerFarmInfoByVillageIdImg(Long villageId, Long farmerId, Long farmId);

	public List<Object[]> findFarmerByVillageCodeAndProcurement(String villageCode);

	public List<Object[]> findFarmerByFpo(String fpo);

	public String findComponentNameByDynamicField(String compName);

	public List<DynamicSectionConfig> findDynamicFieldsBySectionId(String section);

	public Object[] findDynamicValueByFarmerId(String componentName, long farmerId);

	public List<DynamicFieldConfig> listDynamicFields();

	public List<DynamicSectionConfig> listDynamicSections();

	public List<FarmerFeedbackEntity> findFamerFromFarmerFeedback();

	public List<Object[]> listFarmerBySamithi(Long samithiIds);

	public AnimalHusbandary findAnimalHusbandaryByFarmerIdAndId(Long farmerId, Long animalId);

	public FarmIcsConversion findFarmIcsConversionByFarmId(Long farmId);

	public FarmCrops findFarmCropsByFarmCodeAndCategory(long farmId);

	public List<Object[]> listCropsByFarmIdBasedOnCategory(Long farmId);

	public Object[] findTotalQtyByCropHarvest(String selectedBranch, String selectedSeason, String selectedStaple,
			String selectedState);

	public Integer findCropCount();

	public Double findSaleCottonByCoc(List<String> farmerIds, List<String> farmCodes, String selectedSeason);

	public String findTotalLandByPostHarvest(String selectedBranch, int selectedYear, String selectedSeason);

	public Long findFarmCountByFarmerId(Long farmerId);

	public List<Object[]> listFarmerIdAndName();

	public FarmerField findFarmerFieldByFieldName(String entityColumn);

	public List<Cultivation> listCultivation();

	public List<CropHarvest> listCropHarvest();

	List<Object[]> listFarmerByCooperativeCode(List<String> samithiIds);

	public List<Object[]> findCocCostByFarmAndFarmer(String farmerId, String farmCode);

	public Object findCottonIncomeByFarmerAndFarm(String farmerId, String farmCode);

	public List<Object[]> findFarmerByGroup(long fpo);

	public MasterData findMasterDataIdById(long farmerId);

	public MasterData findMasterDataIdByCode(String farmerId, String tenantId);

	public Farm findFarmByCode(String code, String tenantId);

	public Farmer findFarmerbyFarmerIdAndSeason(String farmerId, String season);

	public List<Cultivation> listCultivationBySeason(String currentSeasonsCode);

	public List<Object[]> findCultivationCostBySeason(String farmerId, String farmCode, String season);

	public Farm findFarmByfarmIdAndSeason(String farmId, String cSeasonCode);

	public Farm findFarmByfarmCodeAndSeason(String farmId, String cSeasonCode);

	public List<Object> listFarmerCountByGroupByType();

	public Integer findFarmersCountByStatusByTypez();

	public Farmer findFarmerById(Long id, String branchId);

	public List<Object[]> listUomType(String id);

	public List<Object[]> listQtyByUomAndType(String id, String uom);

	public List<PeriodicInspection> listPeriodicInspection();

	public List<CultivationDetail> listCultivationDetail();

	public Farmer findFarmerByTraceId(String traceId);

	public List<Cultivation> findCOCByFarmerIdFarmIdSeason(String farmerId, String farmId, String seasonCode);

	String findCropNameByCropCode(String cropCode);

	public List<ViewFarmerActivity> listActivityReport(String sord, String sidx, int startIndex, int limit,
			Date getsDate, Date geteDate, ViewFarmerActivity filter, int page, Object object);

	public List<ViewFarmerActivity> listActivityReport(String startDate, String endDate, String farmer);

	List<Object[]> listFcpawithCategoryCode(String categoryCode);

	public List<Object> findICSFarmerCountByGroup(String selectedBranch, int selectedYear, String selectedSeason,
			int icsTyp);

	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByFarmerId(Long farmerId, String txnType);

	public List<Object[]> ListOfFarmerHealthAssessmentByFarmerId();

	public List<Object[]> ListOfFarmerSelfAssesmentByFarmerId();

	public List<FarmerDynamicFieldsValue> listDynmaicFieldsInfo();

	public List<Object[]> listFarmCropsFieldsByFarmerId(List<Long> farmIds);

	public Integer findFarmerSowingCount(String selectedBranch, int selectedYear, String selectedSeason);

	public Integer findFarmerPreHarvestCount(String selectedBranch, int selectedYear, String selectedSeason,
			String selectedGender, String selectedState);

	public Integer findFarmerPostHarvestCount(String selectedBranch, int selectedYear, String selectedSeason,
			String selectedGender, String selectedState);

	public List<Object[]> findComponentListVal(String text);

	public int findMaxOrderNo();

	public String findSectionCodeByReferenceId(String listType);

	public void updateFarmerStatusByFarmerId(String farmerId);

	public Integer findFarmersCountByStatusAndSeason(String currentSeason);

	public Object[] findTotalQtyAndAmt(String location, String farmerId, String product, String seasonCode,
			String stateName, String fpo, String icsName, String branchIdParma, String subBranchIdParma,
			String selectedFieldStaff);

	public void updateDynamicFarmerFieldComponentType();

	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByRefId(String refId, String txnType);

	public Object[] findTotalQtyAndAmt(String location, String farmerId, String product, String seasonCode,
			String stateName, String fpo, String icsName, String branchIdParma, String subBranchIdParma,
			String selectedFieldStaff, Date startDate, Date endDate, String branch);

	public Object[] findTotalQty(String warehouse, String order, String selectedProduct, String vendor, String receipt,
			String seasonCode, String branchIdParma, Date startDate, Date endDate);

	public Object[] findTotalAmtAndweightByProcurement(String filterList, Map<String, Object> params);

	public Object[] findTotalNoBagsAndNetWeg(String farmerId, String selectedCoOperative, String branchIdParma,
			String branch, String productId);

	public List<Object[]> listIcNameByFarmer(String selectedFarmer);

	public Object[] findTotalQtyInAgent(String location, String farmerId, String productId, String seasonCode,
			String stateName, String fpo, String icsName, String branchIdParma, String subBranchIdParma, String agentId,
			Date convertStringToDate, Date convertStringToDate2, String branch);

	public List<Object> findCropHavestQuantity(String farmerId, String seasonCode, String stateName, String farmCode,
			String crop, String icsName, String branchIdParma, Date convertStringToDate, Date convertStringToDate2,
			String agentId);

	public Object[] findCropSaleQtyAmt(String farmerId, String seasonCode, String buyerInfo, String stateName,
			String fpo, String icsName, String branchIdParma, Date convertStringToDate, Date convertStringToDate2,
			String agentId, String subBranchIdParam);

	public List<Object[]> listVillageByGroup(long id);

	public List<Object[]> listIcsStatusByFarmer(String selectedFarmer);

	public List<Object[]> listICSByGroup(long id);

	public List<Object[]> findDynamicSectionFieldsByTxnType(String txnType);

	public List<Object[]> findDynamicSectionFields();

	public List<DynamicSectionConfig> listDynamicSectionByTxnType(String txnType);

	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByTxnId(String selectedObject);

	public FarmerDynamicData findFarmerDynamicData(String id);

	public Integer findFarmersCountFarmerLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus);

	public Object[] findTotalAcreAndEstYield(String selectedCrop, String selectedTaluk, String selectedVillage,
			String selectedSeason);

	public Object[] findPeriodicInsDateByFarmCode(String farmCode);

	public Farmer findFarmerByMsgNo(String msgNo);

	public List<Object[]> listFarmCropsFieldsByFarmerIdByAgentIdAndSeason(long id, Long revisionNo);

	public List<Object[]> listFarmFieldsByFarmerIdByAgentIdAndSeason(long id, Long revisionNo);

	Long listFarmFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(long id);

	Long listFarmCropsFieldsByFarmerIdByAgentIdAndSeasonRevisionNo(long id);

	public List<Object[]> findTotalAreaProdByOrg(String selectedSeason, String selectedGender, String selectedState,
			String branchId);

	public List<Object[]> findTotalAreaProdByIcs(String selectedSeason);

	public List<MasterData> listMasterType();

	public GMO findGMOById(Long gmoId);

	public int findMaxTypeId();

	public GinnerQuantitySold findGinnerQtySoldById(Long id);

	public List<Object[]> findTotalGinnerQty(String selectedSeason, String selectedBranch);

	public List<Object[]> findGmoPercentage(String selectedSeason, String selectedBranch);

	public void updateDynmaicImage(FarmerDynamicFieldsValue fdfv, String parentId);

	public DynamicImageData findDynamicImageDataById(Long id);

	public List<Object> listOfICS();

	public List<Object[]> listProcurementTraceabilityCity();

	public List<Object[]> listProcurementTraceabilityVillage();

	public List<Object> listFarmerFpo();

	public List<Object[]> listcooperativeByFarmer(String selectedFarmer);

	public List<Object[]> findfpoByFarmerID(long id);

	public double findTotalStockInHeap(String gining, String procurementProduct, String ics, String heapDataName,
			String branchIdParma, String season);

	public List<Object[]> findFarmerDatasByFarmerID(long id);

	public List<DynamicFeildMenuConfig> listDynamicMenus();

	public List<FarmerDynamicFieldsValue> listFarmerDynamicFieldsValuePhotoByRefTxnType(String refId, String txnType);

	public List<DynamicFieldConfig> getListComponent(String sectionCode);

	public DynamicFieldConfig findDynamicFieldConfigById(Long id);

	public CottonPrice findProdPriceById(Long id);

	public String findMspByStapleLen(String selectedSeason, String selectedStapleLen);

	public GMO findGMOType(String type, String season);

	public CottonPrice findStapleByCottonPrice(String staple, String seasonCode);

	public List<DynamicFeildMenuConfig> findDynamicMenusByType(String type);

	public List<Object> listFarmerFpoFromTraceabilityStock();

	public List<Object[]> listFarmerIDAndName();

	public List<Object[]> listFarmIDAndName();

	public void updateFarmICSStatusByFarmId(long farmId, String icsType);

	public List<CropSupplyImages> listCropSupplyImages(long id);

	public CropSupplyImages loadCropSupplyImages(Long id);

	public List<DynamicFieldConfig> listDynamicFieldsBySectionId(List<String> selectedSection);

	public List<DynamicSectionConfig> listDynamicSectionsBySectionId(List<String> selectedSection);

	public List<DynamicFieldConfig> listDynamicFieldsByCodes(List<String> selectedSection);

	public DynamicFeildMenuConfig findDynamicMenuConfigById(Long id);

	public List<Object[]> findDynamicSectionByMenuId(Long menuId);

	public List<Object[]> findDynamicFieldsByMenuId(Long menuId);

	public DynamicSectionConfig findDynamicSectionByName(String sectionName);

	public List<String> listOfDynamicSections();

	public List<Object[]> listOfDynamicFields(List<String> selectedSections);

	public List<String> listOfDynamicFields();

	public Object[] listFarmInfoByCode(String code);

	public List<DynamicFieldReportJoinMap> findDynamicFieldReportJoinMapByEntityAndTxn(String entity, String txnType);

	public List<DynamicFieldReportConfig> findDynamicFieldReportConfigByEntityAndTxn(String entity, String txnType);

	public List<String> findFarmCodsByStapleLen(String selectedSeason, String selectedStapleLen, String branch);

	public List<Object[]> findSalePriceByFarmCodes(List<String> farmCodes);

	public Farm findFarmByID(Long id);

	public List<Object[]> getParentMenus();

	public List<Object[]> findSubMenusByParentMenuId(String parentMenuId);

	public void delete_subMenu(Long id, String name);

	public void save_subMenu(String parentId, String menuName, String menuDes, String menuUrl, String menuOrder,
			String ese_ent_name, String ese_action_actionId, String role_id);

	public List<Object[]> getAction();

	public List<Object[]> getRole();

	public List<FarmCatalogue> listHeapData();

	public List<Object> listFarmerCountByGroupAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedCooperative, String selectedGender, String typez);

	public Long findCropIdByFarmCode(String farmCode);

	public void removeFarmCoordinates(Long id);

	public Farm listOfFarmCoordinateByFarmId(long id);

	public List<FarmCrops> listFarmCropsByFarmId(long farmId);

	public List<Object[]> listFarmerBySamithiId(long id);

	public void removeFarmCropCoordinates(Long id);

	public List<Object[]> listFarmerInformationByCultivation();

	public List<FarmerLocationMapField> listRemoveFarmerLocMapFields();

	public void deleteListFields(Long id);

	public DynamicSectionConfig findDynamicSectionConfigById(Long id);

	FarmerDynamicData findFarmerDynamicData(String txnType, String referenceId);

	public List<FarmerDynamicFieldsValue> processFormula(FarmerDynamicData farmerDynamicData,
			Map<String, List<FarmerDynamicFieldsValue>> fdMap, LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap);

	public void deleteChildObjects(String txnType);

	public FarmerDynamicData findFarmerDynamicDataBySeason(String txnType, String id, String season);

	public List<DynamicFeildMenuConfig> findDynamicMenusByMType(String txnType);

	List<DynamicFieldReportConfig> findDynamicFieldReportConfigByEntity(String reportType);

	List<DynamicFeildMenuConfig> findDynamicMenusByEntityType(String reportType);

	List<DynamicFieldReportJoinMap> findDynamicFieldReportJoinMapByEntity(String reportType);

	List<Object[]> listOfFarmInfo();

	public void addWeatherForeCast(WeatherForeCast foreCast);

	public List<Object[]> findLatAndLongByFarm();

	public String findLatAndLong(String lat, String lon);

	public List<WeatherForeCast> findForecastByCity(String selectedCity, List<String> daysList);

	public void removeForecastData();

	public String findFarmIcsTypByFarmId(Long farmId);

	public String findCropNameByCropId(Long farmId);

	public Object[] findFarmInfoById(Long id);

	public Object[] findFarmerInfoById(Long id);

	public List<Object[]> listActiveContractFarmersFieldsBySeasonRevNoAndSamithiWithGramp(long id,
			String currentSeasonCode, Long revisionNo, List<String> branch);

	public List<DynamicFieldConfig> getLitsComponentsBySectionCodeAndListId(String sectionCode, String id);

	public void updateListComponentsOrder(Long id);

	public void updateOrderForSubMenus(Long id, int order);

	public List<Object[]> isAlreadyAvailableMenuByParentId(Long parentMenuId, String menuName);

	public List<Object[]> listFarmListByFarmerId(Long farmerId);

	public boolean isPlottingExist(Long id);

	public List<Object[]> listFarmerAddressByFarmerId(String farmerId);

	public List<DynamicFieldConfig> getFieldsListForFormula();

	public ForecastAdvisoryDetails findforeCastAdvisoryDetailsById(Long id);

	public ForecastAdvisory findforeCastAdvisoryById(Long id);

	public List<Object[]> findAdvisoryList();

	public List<Object[]> findForeCastList(Long cropId);

	public void addForeCast(Forecast foreCast);

	public void removeForecast();

	public void updateDependencyKeyForDependentFieldsOfFormula(String dependentFieldCode, String formula_label_code);

	public List<DynamicFieldConfig> availableFieldsForList();

	public void updateFieldsReferenceId(Long listId, List<Long> fieldsIdList);

	public void updateListButtonOrder(Long referenceId);

	List<Object[]> listFarmFieldsByFarmerId(long id);

	public List<Object[]> listCropNamesWithFarm();

	public List<Farmer> findExistingFarmerByFarmerId(String farmerId);

	public void updateExistingFarmerFlagById(List<Farmer> existing_farmer);

	public List<Object[]> findFarmerTraceDetailsByFarmCode(String farmCode, String tenantId);

	public Farmer findFarmerByID(Long id, String tenantId);

	List<Object[]> listDistributionBalanceDownload(long id, String strevisionNo);

	public String findTotalAcreAndEstYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedSeason);

	public DistributionBalance findDistributionBalanceItem(Long id, String procurementProduct);

	public void updateDistributionBalance(DistributionBalance db);

	public List<DistributionBalance> listDistributionBalanceList(Long id);

	public DistributionBalance findDistributionBalanceById(long templateId);

	public List<Object[]> listCultivationByFarmerIncome(String branchId);

	public List<FarmerDynamicData> listFarmerDynamicDataByTxnId(String txnId);

	public void updateFarmIcsConversionByFarmId(long farmId);

	public FarmIcsConversion findFarmIcsConversionByFarmIdWithActive(Long farmId);

	public FarmIcsConversion findFarmIcsConversionByFarmIdAndInspectionTypeAndScopeOperationWithInspectionDate(
			long selectedFarm, String insType, String scope, int inspectionDateYear);

	public void updateFarmICSStatusByFarmIdInsTypeAndIcsType(Long farmId, String insType, String icsType);

	public FarmIcsConversion findFarmIcsConversionById(Long id);

	public List<Farmer> listFarmerByFarmerIdByIdList(List<String> id);

	public FarmIcsConversion findFarmIcsConversionByFarmIcsConvId(Long id);

	public Object[] findTotalAcreAndEstimatedYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedSeason, String selectedFarmer,
			String selectedOrgnicStatus);

	public void insertDistBalance(String stringCellValue, String string, Double valueOf);

	public Object[] findFarmerCountByStateName(String stateName);

	public List<Object[]> populateStateAndFarmerCountList(String selectedBranch);

	public List<DynamicFeildMenuConfig> listDynamicMenusByRevNo(String revisionNo);

	public List<Object[]> populateDistrictAndFarmerCountList(String selectedBranch);

	public List<Object[]> populateDistrictAndactiveFarmers(String selectedBranch);

	public BigInteger[] getPramidChartDetails();

	public List<Object[]> findTotalWeightAmtNameByProcurement(String hqlQueryAppnd, Map<String, Object> params);

	public List<DynamicFieldConfig> listDynamicFieldsBySectionCode(String sectionCode);

	public void addNewComponentIntoList(Long component_id, Long list_id);

	public List<Object[]> findSeasonCodeList(String selectedBranch);

	public List<Object[]> findProductsByWarehouse(String warehouse, String chartType);

	public List<Object[]> listDistributionWarehouse(String branch, String season, String chartType);

	public List<Object[]> findAmtAndQtyByProcurment(String selectedBranch, String selectedState, String selectedSeason,
			Date sDate, Date eDate);

	public Object[] findSaleIncomeByFarmer(List<String> farmerIds, List<String> farmCodes);

	public List<Object[]> getCertifiedFarmerCount();

	public List<Object[]> getNonCertifiedFarmerCount();

	public List<Object[]> getFarmDetailsAndProposedPlantingArea();

	public List<Object[]> listOfFarmersByFarmCrops();

	public Object[] findTotalAmtAndweightByProcurementWithDate(String filterList, Map<String, Object> params,
			Date convertStringToDate, Date convertStringToDate2, String branch);

	public double findStockAdded(Long distId, Long warehouseId);

	public List<Object[]> farmersByBranch();

	public List<Object[]> farmersByState(String selectedBranch);

	public List<Object[]> farmersByCountry(String selectedBranch);

	public List<Object[]> farmersByLocality(String selectedBranch);

	public List<Object[]> farmersByMunicipality(String selectedBranch);

	public List<Object[]> farmersByGramPanchayat(String selectedBranch);

	public List<Object[]> farmersByVillageWithGramPanchayat(String selectedBranch);

	public List<Object[]> farmersByVillageWithOutGramPanchayat(String selectedBranch);

	public List<Object[]> farmerDetailsByVillage(String selectedBranch);

	public List<Object[]> getFarmDetailsAndProposedPlantingArea(String locationLevel, String selectedBranch,
			String gramPanchayatEnable);

	public List<Object[]> populateFarmerLocationCropChart(String codeForCropChart);

	public List<Object[]> estimatedAndActualYield(String locationCode);

	public List<Object[]> listfarmerwithoutorganic();

	public List<Object[]> listFarmFieldsByFarmerIdAndNonOrganic(long id);

	public List<Object[]> listFarmFieldsByFarmerIdNonCertified(long id);

	public List<DynamicImageData> listDynamicImageByIds(List<Long> isList);

	public void updateDistributionStatus(int deleteStatus, long distId);

	public List<Object[]> estimatedYield(String locationCode);

	public List<Object[]> actualYield(String locationCode);

	List<Farmer> listFarmerByIds(List<String> ids);

	public List<DynamicConstants> listDynamicConstants();

	public String getFieldValueByContant(String entityId, String referenceId, String group);

	public List<Object[]> warehouseToMobileUserChart(String branch);

	public List<Object[]> warehouseToMobileUser_AgentChart(String branch, String warehouse);

	public List<Object[]> warehouseToMobileUser_ProductChart(String branch, String warehouse, String agentId);

	public List<Object[]> populateMobileUserToFarmer_AgentChart(String branch, String season);

	public List<Object[]> populateMobileUserToFarmer_FarmerChart(String branch, String agent);

	public List<Object[]> populateMobileUserToFarmer_ProductChart(String branch, String agent, String farmerId);

	public List<Object[]> populateWarehouseToFarmer_ProductChart(String branch, String warehouse, String farmerId);

	public List<Object[]> populateWarehouseToFarmer_FarmerChart(String branch, String warehouse);

	public List<Object[]> populateWarehouseToFarmer_WarehouseChart(String branch);

	public List<Object[]> productChartByWarehouseToMobileUser(String branch, String warehouse);

	public List<Object[]> productChartByMobileUserToFarmer(String branch, String agent, String selectedSeason);

	public List<Object[]> productChartByWarehouseToFarmer(String branch, String warehouse);

	public List<Object[]> listParentFields();

	public List<Object[]> populateAvailableColumns();

	public List<Object[]> ListFarmerDynamicDataAgentByTxnType(String txnType, String branchId);

	public List<Object[]> getGridData(String query);

	public List<FarmerDynamicData> ListFarmerDynamicDatas(long l);

	public FarmerDynamicData findFarmerDynamicDataByTxnUniquId(String txnUniqueId);

	public List<Object[]> cropHarvestByFarmerId(String farmerId, String startDate, String endDate, String seasonCode);

	public List<Object[]> distributionToFarmerByFarmerId(String farmerId, String startDate, String endDate,
			String seasonCode);

	public List<Object[]> productReturnByFarmerId(String farmerId, String startDate, String endDate, String seasonCode);

	public List<Object[]> trainingStatusReportByFarmerId(String farmerId, String startDate, String endDate);

	public List<Object[]> farmerBalanceReportByFarmerId(String farmerId, String startDate, String endDate);

	public List<Object[]> procurementTransactionsByFarmerId(String farmerId, String startDate, String endDate,
			String seasonCode);

	public void saveDynamicReport(String reportTitle, String reportDescription, String outPutQuery,
			String header_fields, String filter_data, String entity, String selectedFields, String groupByField,
			String id);

	public void deleteDynamicReportById(String id);

	public void updateDynamicReportById(String id, String title, String des, String query, String header_fields,
			String filter_data, String entity, String fields, String groupByField);

	public List<TreeDetail> findTreeDetailByFarmId(long id);

	public List<Object[]> periodicInspectionsByFarmerId(String farmCode, String startDate, String endDate,
			String seasonCode);

	public List<Object[]> periodicNeedBasedInspectionsByFarmerId(String farmCode, String startDate, String endDate,
			String seasonCode);

	public List<Object> ListFarmerDynamicDatasByIds(List<Long> mainIds);

	public List<DynamicReportFieldsConfig> populateDynamicReportFields();

	public List<Object[]> listFarmerHavingPlots();

	public List<Object[]> listFarmByFarmerIds(long id);

	public DynamicReportTableConfig findDyamicReportTableConfigById(Long enity);

	public List<FarmCropsField> listFarmCropsFields();

	public FarmerDynamicData findFarmerDynamicDataByReferenceId(long referenceId);

	public int findDynamicMenuMaxOrderNo();

	public Farmer findFarmerByFirstNameAndSurName(String firstName, String surName);

	public Farmer findFarmerByFirstNameLastNameAndSurName(String firstName, String lastName, String surName);

	public List<Object[]> listColdStorageNameDynamic();

	public FarmCrops findFarmCropById(Long caseId);

	public boolean isFarmMappingFarm(long id);

	public WarehouseStorageMap findWarehouseStorageMapByWarehouseId(Long id);

	public List<WarehouseStorageMap> listOfWarehouseStorageMap(long id);

	public List<Object[]> listFarmCropsFieldsByFarmerIdAgentIdAndSeason(long agentId, Long revisionNo,
			String seasonCode);

	public void processCustomisedFormula(FarmerDynamicData farmerDynamicData,
			Map<String, DynamicFieldConfig> fieldConfigMap);

	public List<Object[]> getFarmDetailsAndCultivationArea(String locationLevel, String selectedBranch,
			String gramPanchayatEnable);

	public FarmCrops findFarmCropByCropIdAndFarmId(long farmId, long varietyId);

	public List<Object[]> listValuesbyQuery(String methodName, Object[] parameter, String branchId);

	public List<Object[]> plottingAreaByBranch();

	public List<Object[]> plottingByWarehouse(String selectedBranch);

	public List<Object[]> plottingAreaByBranch(String selectedSeason);

	public List<Object[]> plottingByMobileUser(String selectedBranch);

	public List<Object[]> trainingsByBranch();

	public List<Object[]> findAgentTrainingData(long warehouseId,String selectedFinYear);

	public List<Object[]> populateWarehouseMobileUserToFarmer_AgentChart(String branch, String season, String selectedFinYear);

	public EventCalendar findEventByMessageNumber(String eventId);

	public List<EventCalendar> listEventByRevisionNo(Long revisionNo, String agentId);

	public String getValueByQuery(String methodName, Object[] parameter, String branchId);

	public DynamicFieldScoreMap findDynamicFieldScoreByFieldIdAndComponentCode(String id, String code);

	public List<Object[]> getValueListByQuery(String methodName, Object[] parameter, String branchId);

	public List<Object[]> listFarmerFarmInfoFarmCropsByVillageId(FarmCrops crops);

	public List<Object[]> listOfFarmersByVillageAndFarmCrops(long villageId,String plottingType);

	public Integer findFarmersCountSowingLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer);

	public Object[] findTotalAcreAndEstimatedYieldSwoingLoca(String selectedCrop, String selectedState,
			String selectedLocality, String selectedTaluk, String selectedVillage, String selectedFarmer);

	public List<Object[]> listFarmerFarmCropInfoByVillageIdImg(String fType, Long cropId, Long farmId);

	public void saveSectionBatch(List<DynamicSectionConfig> dscList);

	public long findDynamicFieldsCountByCode(String code);

	public long findDynamicSectionCountByCode(String code);

	public void saveFieldsBatch(List<DynamicFieldConfig> dfcList);

	public List<Object[]> listOfFarmersByVillageAndFarm(long villageId);

	public long findDynamicMenuCountByCode(String code);

	public List<DynamicReportFieldsConfig> listDynamicColumnsByEntity(String enities);

	public List<Object[]> updateLotStatus(String selectedBales);

	public FarmerTraceability findTraceabilityDataById(Long id);

	public String findQrCodeParameterByKey(String farmCode, String tenantId);

	public Farm findFarmByID(Long id, String tenantId);

	public List<Object[]> listFarmDetailsInfo();

	public List<DataLevel> listDataLevel();

	public CropYield findCropYieldById(Long id);

	public List<CropYieldDetail> findCropYieldDetailById(Long id);

	public List<FarmerDynamicFieldsValue> listFarmerDynamicFieldByFdIds(List<Long> ids);

	public void updateFDVS(List<FarmerDynamicFieldsValue> fdvs);

	public List<Object[]> listFDVSForFolloUp(String agentId, String revisionNo);

	public List<Object[]> findFarmerByListOfFarmerId(List<String> farmerList);
	
	public Farmer findFarmerByMSISDN(String msisdn);

	public String findComponentNameByDynamicFieldCode(String fieldCode);

	public List<Object[]> listFarmerDynamicFieldsValuesByFarmIdList(List<String> farmIdList,List<String> selectedDynamicFieldCodeList);

	public List<Object[]> listDynamicFieldsCodeAndName();

	public List<DynamicMenuFieldMap> listDynamisMenubyscoreType(int i);

	List<Object[]> listFarmerCodeIdNameByStateCode(String stateCode);
	public List<DynamicFeildMenuConfig> listDynamicMenusByRevNo(String revisionNo, String branchId,String tenantId);
	
	public List<DynamicFeildMenuConfig> findDynamicMenusByType(String txnTypez, String branchId);
	
	public List<DynamicFeildMenuConfig> findDynamicMenusByTypeForOCP(String txnTypez, String branchId);

	public List<Object[]> listValueByFieldName(String field, String branchId);

	public FarmCrops findFarmCropFullObjectById(String id);

	public FarmerDynamicFieldsValue findFarmerDynamicFieldsValueById(String id);

	public FarmerDynamicData processCustomisedFormula(FarmerDynamicData farmerDynamicData, Map<String, DynamicFieldConfig> fieldConfigMap,
			Map<String, List<FarmerDynamicFieldsValue>> fdMap);

	public ColdStorage findColdStorageById(String id);

	public ColdStorageDetail findColdStorageDetailById(Long valueOf);

	public List<DynamicMenuFieldMap> listDynamisMenubyscoreType();
	
	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm, String selectedOrganicStatus,String selectedFarmer,List<Long> yieldEstimationDoneFarmers);

	public FarmerDynamicData findFarmerDynamicDataByReferenceIdAndTxnType(long id);
	
	public List<Object[]> listfarmerDynamicData(List<Long> fidLi);
	
	public FarmCrops findFarmCropByCropIdAndFarmIdAndSeason(long farmId, long varietyId, long seasonId);
	
	public List<Object> listFarmerCountByGroupTraderAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedGender, String selectedCooperative, String selectedCrop, String typez,
			String selectedVillage);
	
	public List<Object> listFarmerCountByFarmInspection(Date sDate, Date eDate);
	
	public FarmerDynamicFieldsValue findLotCodeFromFarmerDynamicFieldsValue(Long id,String fieldName);

	public List<Object[]> populateTrainingChart(String selectedBranch, String selectedFinYear);

	public Integer findFarmerCountByStateAndCrop(String selectedBranch, int i, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedFinYear);

	public List<Object> findFarmerDetailsByStateAndCrop(String selectedBranch, Long selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedFinYear);

	public List<Object[]> productChartByMobileUserToFarmer(String selectedBranch, String agentId, String selectedSeason,
			String selectedFinYear);
	
	public List<FarmCropsField> listRemoveFarmCropFields();
	
	public List<Object[]> listFarmsLastInspectionDate();
	
	public CropYield findMoleculeDateByLotCode(String lotCode);
	
	public Farmer findOlivadoFarmerByFarmerCode(String farmerCode);
	
	public List<Object[]> listFarmerAddressById(Long farmerId);

	public List<Object[]> populateWarehouseMobileUserToFarmer_AgentChart(String selectedBranch, String selectedSeason);
	
	public List<Object[]> findDyamicReportTableConfigParentIdsById(String entity);
	
	public List<String> listFarmerByCreatedUser();
	
	public List<Object[]> listFarmersWithFarmParcelIds();
	
	public List<Object[]> listFarmParcelIdsByFarmer(String farmerId);
	
	public List<Object[]> listFarmCropsWithGcParcelIdsByFarm(String farmId);
	
	public List<Object[]> listFarmersWithFarmCropParcelIds();
	
	public List<Object[]> listFarmWithoutCropParcelIdsByFarmer(String farmerId);
	
	public List<Object[]> listFarmersWithIdAndCode();
	
	public List<Object[]> listFarmsOfFarmerId(long farmerId);
	
	public boolean findIfFarmHasParcelId(String farmId);
		
	public List<Object[]> listFarmCropsByFarm(String farmId);
	
	public List<Object[]> listFarmersWithFarmCoordinates();
	
	public List<Object[]> listFarmersWithFarmCropCoordinates();
	
	public List<Object[]> listFarmWithCropParcelIdsByFarmer(String farmerId);
	
	public List<Object[]> listFarmCropByFarmId(String farmId);
	
	public List<Object[]> listFarmCropsWithFarmId(long farmId);

	public List<Object[]> listValueByFieldName(String localeProperty);
	
public List<Object[]> listOfProofNo();
	
	public List<Object[]> listVendorByLoanStatusAndFarmerAndVendor(Long farmerId, String vendorId);
	
	public List<Object[]> listFarmerCodeIdNameByFarmerTypezAndLoanApplication(String branchId);

	public List<Object[]> listActiveContractFarmersByLoanStatusAndSamithiAndVendor(Long id, String vendorId);
	
	public LoanDistribution findFarmerLatestLoanYear(Long farmerId);
	
	public LoanDistribution findGroupLatestLoanYear(Long farmerId);
	
	public LoanApplication findFarmerLatestLoanApplication(Long farmerId);
	
	public LoanApplication findGroupLatestLoanApplication(Long farmerId);
	
	public List<Farmer> listGroup();
	
	public Farmer findGroupById(Long id);

	public List<Object> findFarmerDetailsByStateAndCropbyFarmerStatus(String selectedBranch, Long selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender,String selectedStatus);
	
	public Integer findFarmerCountByStateAndCropbyFarmerStatus(String selectedBranch, int selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender,String selectedStatus);

	public List<Object[]> listFarmscountInfo();

	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm,String selectedOrganicStatus,String selectedFarmer,List<Long> yieldEstimationDoneFarmers,String selectedStatus );

	public Integer findFarmersCountFarmerLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus,String selectedStatus);


	public Object[] findTotalAcreAndEstimatedYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus,String selectedStatus);

	
	public List<Object[]> farmersByGroup(String selectedBranch);
	
	public List<Object[]> getFarmDetailsAndProposedPlantingAreaByGroup(String locationLevel1, String selectedBranch,String gramPanchayatEnable);

	List<Object[]> findFarmerCountByGroupICS(String selectedBranch);
	
	public List<Object[]> populateFarmerLocationCropChartByGroup(String codeForCropChart, String selectedBranch,String seasonCode);

	
	public List<Object[]> listFarmerFarmInfoFarmCropsByVillageId(Object obj,String selectedStatus,String plottingType) ;

	public Integer findFarmersCountSowingLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer,String selectedStatus);


	public Object[] findTotalAcreAndEstimatedYieldSwoingLoca(String selectedCrop, String selectedState,
			String selectedLocality, String selectedTaluk, String selectedVillage, String selectedFarmer,String selectedStatus, String selectedFarm);

	public List<DynamicFeildMenuConfig> findDynamicMenus();
	
	public List<Object[]> farmersByBranchandSeason(String season);
	
	public List<Object[]> farmersByBranchandSeason(String branch,String season);
	
	public List<Object[]> farmersByCountryAndSeason(String selectedBranch,String seasonCode);
	
	public List<Object[]> farmersByGroupAndSeason(String selectedBranch,String seasonCode);
	
	public List<Object[]> farmersByBranch(String branch);
	
	List<Object[]> findFarmerCountByGroupICS(String selectedBranch, String season);
	
	public List<Object[]> populateFarmerCropCountChartByGroup(String seasonCode,String selectedics,String selectedBranch);

	public FarmIcsConversion findFarmIcsConversionByFarmSeasonScopeAndInspectionType(long selectedFarm, String season,
			String scope, String insType);
	
	public List<Object[]> ListFarmerCropDetails(List<String> mainIds);
	
	public List<Object[]> ListFarmerCropDetailsByFarmer(List<String> mainIds);
	
	public List<Object[]> listMaxTypez(List<Long> collect,String txnType);
	
	public List<FarmCrops> listOfCropsByFarmIdAndSeason(List<Long> id,long season);

	public List<Object[]> listOfFarmsByFarmer(Long farmerId);
	
	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByRefId(String refId, String txnType,Long farmerDynamicDataId);
	
	public List<Object[]> findAmtAndQtyByProcurmentTraceability(String selectedBranch, String selectedState, String selectedSeason,
			Date sDate, Date eDate);
	
	public List<DataLevel> listDataLevelByType(String type);
	
	public SamithiIcs findSamithiIcsById(Long id);
	
	public List<Object[]> listFarmInfoLatLon();
	
	public ESEAccount findAccountByFarmerLoanProduct(long farmerId);

	public LoanInterest findLoanPercent(Long amt);

	public LoanInterest findLoanRangeById(Long id);

	public List<Object[]> findCountOfDynamicDataByFarmerId(List<Long> fidLi,String season);
	
	public ESEAccount findEseAccountByFarmerId(String farmerId);
	
	public List<Object[]> listLoanLedgerByEseAccountId(String accountId);
	
	public List<Object[]> listLoanLedgerByEseAccountId(String accountId, int startIndex, int limit);
	
	public ESEAccount findEseAccountById(Long id);
	
public List<Object[]> listFarmerInfoByProcurementWithTypez();
	
	public List<Object[]> listGroupInfoByProcurementWithTypez();
	
public List<Object[]> listFarmerStatementByEseAccountId(String accountId);
	
	public List<Object[]> listFarmerStatementByEseAccountId(String accountId, int startIndex, int limit);
	
public List<Object[]> listFarmerFilterWithLoanLedger();
	
	public List<Object[]> listGroupFilterWithLoanLedger();
	
	public List<Object[]> listFFBpurchaseAndFFBRepaymentAmt(String farmerId);
	
	public Farmer findFarmerInfoByFarmerId(String farmerId);
	
	public List<Object[]> findDateOfLoanLedger(Date startDate,Date endDate);
	
	public List<Object[]> findDateOfLoanLedger(Date startDate,Date endDate,int startIndex, int limit);
	
public List<Object[]> listLoanLedgerByDate(String date,String branchId);
	
	public List<Object[]> listLoanLedgerByDate(String date, int startIndex, int limit);
	
	public List<Farmer> listFarmerWithOutstandLoanBal(String villageCode);
	
	public List<Object[]> listFarmerByLoanDistribution();

	public List<byte[]> getImageByQuery(String methodName, Object[] parameter, String branchId);
	
	public List<Long> listFarmerPrimaryId(Farmer f);

	public List<Long> listProfilePrimaryId(Agent agent);

	public List<Long> listWebUsersPrimaryId(User user);

	Object[] findFarmersCountFromLotTraceByLotNo(String selectedLotNo);

	public List<Object[]> listFarmerFarmInfoByLotNoFromFarmerTraceabilityData(String selectedLotNo, String branch);

}



