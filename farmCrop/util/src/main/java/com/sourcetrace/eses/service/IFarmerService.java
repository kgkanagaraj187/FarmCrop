/*
 * IFarmerService.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.profile.ViewFarmerActivity;
import com.ese.entity.traceability.FarmerTraceability;
import com.ese.entity.txn.mfi.InterestCalcConsolidated;
import com.ese.entity.txn.mfi.InterestRateHistory;
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
import com.sourcetrace.eses.util.entity.ImageInfo;
import com.sourcetrace.esesw.entity.profile.AnimalHusbandary;
import com.sourcetrace.esesw.entity.profile.BankInformation;
import com.sourcetrace.esesw.entity.profile.Calf;
import com.sourcetrace.esesw.entity.profile.CashReceived;
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
 * The Interface IFarmerService.
 */
public interface IFarmerService {

	/**
	 * Find farmer by farmer id.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @return the farmer
	 */
	public Farmer findFarmerByFarmerId(String farmerId);

	/**
	 * Adds the farmer.
	 * 
	 * @param farmer
	 *            the farmer
	 */
	public void addFarmer(Farmer farmer);

	/**
	 * Find farmer by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farmer
	 */
	public Farmer findFarmerById(Long id);

	/**
	 * Edits the farmer.
	 * 
	 * @param existing
	 *            the existing
	 */
	public void editFarmer(Farmer existing);

	/**
	 * Removes the farmer.
	 * 
	 * @param farmer
	 *            the farmer
	 */
	public void removeFarmer(Farmer farmer);

	/**
	 * Adds the farm.
	 * 
	 * @param farm
	 *            the farm
	 */
	public void addFarm(Farm farm);

	/**
	 * Find farm by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farm
	 */
	public Farm findFarmById(Long id);

	/**
	 * Edits the farm.
	 * 
	 * @param existing
	 *            the existing
	 */
	public void editFarm(Farm existing);

	/**
	 * Removes the farm.
	 * 
	 * @param farm
	 *            the farm
	 */
	public void removeFarm(Farm farm);

	/**
	 * Removes the farm by farmer id.
	 * 
	 * @param id
	 *            the id
	 */
	public void removeFarmByFarmerId(long id);

	/**
	 * Save farmer farm and farm crops detail.
	 * 
	 * @param farmer
	 *            the farmer
	 * @param farmList
	 *            the farm list
	 * @param farmCropsList
	 *            the farm crops list
	 */
	public void saveFarmerFarmAndFarmCropsDetail(Farmer farmer, List<Farm> farmList, List<FarmCrops> farmCropsList);

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
	 * Find farmer by village code.
	 * 
	 * @param string
	 *            the string
	 * @return the list< farmer>
	 */
	public List<Farmer> findFarmerByVillageCode(String string);

	/**
	 * List farmer.
	 * 
	 * @return the list< farmer>
	 */
	public List<Farmer> listFarmer();

	/**
	 * Adds the offline farmer enrollment.
	 * 
	 * @param offlineFarmerEnrollment
	 *            the offline farmer enrollment
	 */
	public void addOfflineFarmerEnrollment(OfflineFarmerEnrollment offlineFarmerEnrollment);

	/**
	 * List offline farmer enrollment by status code.
	 * 
	 * @param statusCode
	 *            the status code
	 * @return the list< offline farmer enrollment>
	 */
	public List<OfflineFarmerEnrollment> listOfflineFarmerEnrollmentByStatusCode(int statusCode);

	/**
	 * Process offline farmer enrollment.
	 */
	public void processOfflineFarmerEnrollment();

	/**
	 * Edits the offline farmer enrollment.
	 * 
	 * @param offlineFarmerEnrollmens
	 *            the offline farmer enrollmens
	 */
	public void editOfflineFarmerEnrollment(OfflineFarmerEnrollment offlineFarmerEnrollmens);

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
	 * Process offline biometric upload.
	 */
	public void processOfflineBiometricUpload();

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
	 * Adds the contract.
	 * 
	 * @param contract
	 *            the contract
	 * @return the contract
	 */
	public Contract addContract(Contract contract);

	/**
	 * Edits the contract.
	 * 
	 * @param contract
	 *            the contract
	 */
	public void editContract(Contract contract);

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
	 * @param procurementProductId
	 *            the procurement product id
	 * @param farmerId
	 *            the farmer id
	 * @return the eSE account
	 */
	public ESEAccount findAccountBySeassonProcurmentProductFarmer(long procurementProductId, long farmerId);

	/**
	 * Find contract by season procurment product farmer.
	 * 
	 * @param procurementProductId
	 *            the procurement product id
	 * @param farmerId
	 *            the farmer id
	 * @return the contract
	 */
	public Contract findContractBySeasonProcurmentProductFarmer(long procurementProductId, long farmerId);

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
	 * @param procurementProductId
	 *            the procurement product id
	 * @return the list< farmer>
	 */
	public List<Farmer> listActiveContractFarmersBySeasonProcurementProduct(long procurementProductId);

	/**
	 * List active contract farmers by season procurement product village.
	 * 
	 * @param seasonCode
	 *            the season code
	 * @param productCode
	 *            the product code
	 * @param selectedVillage
	 *            the selected village
	 * @return the list< farmer>
	 */
	public List<Farmer> listActiveContractFarmersBySeasonProcurementProductVillage(String seasonCode,
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
	 * Process contract.
	 * 
	 * @param contract
	 *            the contract
	 */
	public void processContract(Contract contract);

	/**
	 * Find season by season code.
	 * 
	 * @param seasonCode
	 *            the season code
	 * @return the long
	 */
	public Long findSeasonBySeasonCode(String seasonCode);

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
	 * List farm.
	 * 
	 * @return the list< farm>
	 */
	public List<Farm> listFarm();

	/**
	 * Adds the contract for farmer and harvest data.
	 * 
	 * @param farmer
	 *            the farmer
	 * @param harvestDatas
	 *            the harvest datas
	 * @param createUserName
	 *            the create user name
	 */
	public void addContractForFarmerAndHarvestData(Farmer farmer, String createUserName);

	/**
	 * Adds the contract for farmer.
	 * 
	 * @param farmer
	 *            the farmer
	 * @param createUserName
	 *            the create user name
	 */
	public void addContractForFarmer(Farmer farmer, String createUserName);

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
	 * @param id
	 *            the id
	 */
	public void removeContractByFarmerId(long id);

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
	 * Adds the farmer family.
	 * 
	 * @param farmerFamily
	 *            the farmer family
	 */
	public void addFarmerFamily(FarmerFamily farmerFamily);

	/**
	 * Find farmer family by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farmer family
	 */
	public FarmerFamily findFarmerFamilyById(long id);

	/**
	 * Edits the farmer family.
	 * 
	 * @param farmerFamily
	 *            the farmer family
	 */
	public void editFarmerFamily(FarmerFamily farmerFamily);

	/**
	 * Removes the farmer family.
	 * 
	 * @param farmerFamily
	 *            the farmer family
	 */
	public void removeFarmerFamily(FarmerFamily farmerFamily);

	/**
	 * Adds the farmer economy.
	 * 
	 * @param farmerEconomy
	 *            the farmer economy
	 */
	public void addFarmerEconomy(FarmerEconomy farmerEconomy);

	/**
	 * Edits the farmer economy.
	 * 
	 * @param farmerEconomy
	 *            the farmer economy
	 */
	public void editFarmerEconomy(FarmerEconomy farmerEconomy);

	/**
	 * Removes the farmer economy.
	 * 
	 * @param farmerEconomy
	 *            the farmer economy
	 */
	public void removeFarmerEconomy(FarmerEconomy farmerEconomy);

	/**
	 * Removes the farmer economy mapping sql.
	 * 
	 * @param farmerEconomy
	 *            the farmer economy
	 */
	public void removeFarmerEconomyMappingSQL(FarmerEconomy farmerEconomy);

	/**
	 * Adds the harvest season.
	 * 
	 * @param harvestSeason
	 *            the harvest season
	 */
	public void addHarvestSeason(HarvestSeason harvestSeason);

	/**
	 * Edits the harvest season.
	 * 
	 * @param existing
	 *            the existing
	 */
	public void editHarvestSeason(HarvestSeason existing);

	/**
	 * Removes the harvest season.
	 * 
	 * @param harvestSeason
	 *            the harvest season
	 */
	public void removeHarvestSeason(HarvestSeason harvestSeason);

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
	 * @param valueOf
	 *            the value of
	 * @return the harvest data
	 */
	public HarvestData findHarvestDataById(Long valueOf);

	/**
	 * Adds the harvest.
	 * 
	 * @param harvestData
	 *            the harvest data
	 */
	public void addHarvest(HarvestData harvestData);

	/**
	 * Edits the harvest.
	 * 
	 * @param existing
	 *            the existing
	 */
	public void editHarvest(HarvestData existing);

	/**
	 * Removes the harvest.
	 * 
	 * @param harvestData
	 *            the harvest data
	 */
	public void removeHarvest(HarvestData harvestData);

	/**
	 * Adds the farm inventory.
	 * 
	 * @param farmInventory
	 *            the farm inventory
	 */
	public void addFarmInventory(FarmInventory farmInventory);

	/**
	 * Find farm inventory by id.
	 * 
	 * @param id
	 *            the id
	 * @return the farm inventory
	 */
	public FarmInventory findFarmInventoryById(String id);

	/**
	 * Edits the farm inventory.
	 * 
	 * @param farmInventory
	 *            the farm inventory
	 */
	public void editFarmInventory(FarmInventory farmInventory);

	/**
	 * Removes the farm inventory.
	 * 
	 * @param farmInventory
	 *            the farm inventory
	 */
	public void removeFarmInventory(FarmInventory farmInventory);

	/**
	 * Adds the farm detailed info.
	 * 
	 * @param farmDetailedInfo
	 *            the farm detailed info
	 */
	public void addFarmDetailedInfo(FarmDetailedInfo farmDetailedInfo);

	/**
	 * Removes the farm detailed info.
	 * 
	 * @param farmDetailedInfo
	 *            the farm detailed info
	 */
	public void removeFarmDetailedInfo(FarmDetailedInfo farmDetailedInfo);

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
	 * Adds the animal husbandary.
	 * 
	 * @param animalHusbandary
	 *            the animal husbandary
	 */
	public void addAnimalHusbandary(AnimalHusbandary animalHusbandary);

	/**
	 * Find animal husbandary by id.
	 * 
	 * @param id
	 *            the id
	 * @return the animal husbandary
	 */
	public AnimalHusbandary findAnimalHusbandaryById(String id);

	/**
	 * Edits the animal husbandary.
	 * 
	 * @param animalHusbandary
	 *            the animal husbandary
	 */
	public void editAnimalHusbandary(AnimalHusbandary animalHusbandary);

	/**
	 * Removes the animal husbandary.
	 * 
	 * @param animalHusbandary
	 *            the animal husbandary
	 */
	public void removeAnimalHusbandary(AnimalHusbandary animalHusbandary);

	/**
	 * Checks if is farm mappingexist.
	 * 
	 * @param id
	 *            the id
	 * @return the string
	 */
	public String isFarmMappingexist(long id);

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
	 * @param revisionDate
	 *            the revision date
	 * @return the list< object[]>
	 */
	public List<Object[]> listActiveContractFarmersAccountByAgentAndSeason(long agentId, Date revisionDate);

	/**
	 * Find interest calc consolidated byfarmer profile id.
	 * 
	 * @param farmerProfileId
	 *            the farmer profile id
	 * @return the interest calc consolidated
	 */
	public InterestCalcConsolidated findInterestCalcConsolidatedByfarmerProfileId(String farmerProfileId);

	/**
	 * Find current rate of interest.
	 * 
	 * @return the interest rate history
	 */
	public InterestRateHistory findCurrentRateOfInterest();

	/**
	 * Process daily interest calc.
	 */
	public void processDailyInterestCalc();

	/**
	 * Removes the image info.
	 * 
	 * @param imageInfo
	 *            the image info
	 */
	public void removeImageInfo(ImageInfo imageInfo);

	/**
	 * Update image info.
	 * 
	 * @param imageInfo
	 *            the image info
	 */
	public void updateImageInfo(ImageInfo imageInfo);

	/**
	 * Update contract for farmer and harvest datas.
	 * 
	 * @param farmer
	 *            the farmer
	 * @param harvestDatas
	 *            the harvest datas
	 * @param createUserName
	 *            the create user name
	 */
	public void updateContractForFarmerAndHarvestDatas(Farmer farmer, Set<HarvestData> harvestDatas,
			String createUserName);

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
	 * @param l
	 *            the farmer id
	 * @param revisionNo
	 *            the revision no
	 */
	public void updateFarmerRevisionNo(long farmerId, Long revisionNo);

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
	 * Adds the cash received.
	 * 
	 * @param cashReceived
	 *            the cash received
	 */
	public void addCashReceived(CashReceived cashReceived);

	/**
	 * Deleteelemetby id.
	 * 
	 * @param fileid
	 *            the fileid
	 */
	public void deleteelemetbyId(Long fileid);

	public void saveCultivation(Cultivation cultivation);

	public List<FarmCatalogue> listFarmAnimalBasedOnType();

	public FarmCrops findFarmCropsByFarmCode(Long farmId);

	public FarmDetailedInfo findTotalLandHoldingById(Long id);

	public List<String[]> listOfFarmerCode();

	public List<String[]> listOfFarmerName();

	public List<Cultivation> findCostOfCultivationsByFarmerCode(String farmerId, String farmCode);

	public List<CultivationDetail> findCultivationDetailsByCultivationId(long id);

	public List<Object[]> findCultivationCost(String farmerCode, String farmCode);

	public Farmer findFarmerByFarmerCode(String farmerCode);

	public Object findCottonIncomeByFarmerCode(String farmerCode, String string);

	public List<String[]> listFarmerName();

	public List<String[]> listFatherName();

	public List<String[]> listFarmerCode();

	public double findCottonIncomeByFarmerCodeAndFarmCode(String farmerId, String farmCode);

	public List<HarvestSeason> listHarvestSeasons();

	public HarvestSeason findHarvestSeasonByCode(String seasonCode);

	public Farm findFarmByfarmId(Long farmId);

	public List<AnimalHusbandary> listAnimalHusbandaryList(Long farmId);

	public void updateAnimalInventory(AnimalHusbandary animalHusbandary);

	public void addAnimalInventory(AnimalHusbandary animalHusbandary);

	public List<CropHarvest> findCropHarvestByFarmCode(String farmCode);

	public List<CropSupply> findCropSupplyByFarmCode(String farmCode);

	public byte[] findfarmerVerificationFarmerVoiceById(Long id);

	public List<FarmCatalogue> listFarmEquipmentBasedOnType();

	public void updateFarmInventory(FarmInventory fm);

	public FarmInventory findFarmInventoryItem(long farmId, String string);

	public List<FarmInventory> listFarmInventryList(Long farmId);

	public void removeFarmInventry(FarmInventory inventry);

	public Object findAnimalHusbandaryByFarmAndOtherItems(long farmId, long animalStr, String revenueStr, long housStr);

	public List<FarmCatalogue> listFodderBasedOnType();

	public List<FarmCatalogue> listAnimalHousingBasedOnType();

	public List<FarmCatalogue> listRevenueBasedOnType();

	public CropSupply findcropSupplyId(String id);

	public List<CropSupplyDetails> findCropSupplyDetailId(long id);

	public List<Farm> listFarmerFarmByFarmerId(String farmerId);

	public List<Farm> listfarmbyVillageId(Long id);

	public List<FarmCrops> listOfCrops(long id);

	public List<Object[]> listPeriodicInspectionFarmer();

	public List<Object[]> listPeriodicInspectionFarm();

	public void removeCropSupply(CropSupply cropSupply);

	public List<Farmer> listOfFarmers(String selectedVillage);

	public List<FarmCrops> listOfCropNames(String selectedCropType, long farmId);

	public List<FarmCrops> listOfVariety(String selectedCropName);

	public List<ProcurementGrade> listOfGrade(String selectedVariety);

	public Double findGradePrice(String selectedGrade);

	public void saveCropSupply(CropSupply cropSupply);

	public void saveCropHarvest(CropHarvest cropHarvest);

	public List<String[]> listIcsName();

	public List<Warehouse> listSamithiName();

	public List<Farm> listFarmName(String branchId);

	public List<BankInformation> findFarmerBankinfo(long id);

	public void addBankInformation(BankInformation bankInformation);

	public long listOfFarmersCountBasedOnBranch(String branchIdValue);

	public List<java.lang.Object[]> listFertilizerAppliedAndPestAppliedWithQty(String farmCode, String cropCode);

	public Cultivation findByCultivationId(String id);

	public List<FarmCatalogue> listCatelogueType(String Type);

	public List<Object[]> listCropSupplyChartDetails();

	public List<Object[]> listCropSupplyQtyChartDetails();

	public List<Object[]> listCropHarvestQtyChartDetails();

	public List<Object[]> listCropHarvestBuyerChartDetails();

	public List<String[]> listFarmerId();

	public Integer findFarmerCountByMonth(Date sDate, Date eDate);

	public List<Object> listFarmerCountByGroup();

	public List<Object> listFarmerCountByGroupByType();

	public List<FarmCatalogue> listMachinary();

	public List<FarmCatalogue> listPolyHouse();

	public FarmElement findFarmElementById(Long templateId);

	public void removeFarmElement(FarmElement farmElement);

	public List<FarmElement> listFarmElementList(Long farmId);

	public FarmElement findFarmElementItem(Long farmId, String machStr);

	public void addFarmElement(FarmElement element);

	public void updateFarmElement(FarmElement element);

	public List<FarmElement> listMachinaryList(Long farmId, String machType);

	public MasterData findMasterDataIdByCode(String farmerId);

	public Integer findFarmersCount();

	public FarmCrops findByFarmCropsId(Long valueOf);

	public Farmer findFarmerByFarmerId(String farmerId, String tenantId);

	public ESEAccount findAccountBySeassonProcurmentProductFarmer(long procurementProductId, long farmerId,
			String tenantId);

	public List<Object[]> listFarmCropsByVillage(String selectedVillage, String branchId);

	public List<Object[]> listFarmCropsByVillage(String selectedVillage, int startIndex, int limit, String branchId);

	public List<Object[]> listFarmCropsDetailsByVillageCode(String selectedVillage, String branchId);

	public List<Object[]> listFarmCropsDetailsByVillageCode(String selectedVillage, int startIndex, int limit,
			String branchId);

	public List<Object[]> listFarmCropsByCropCode(String selectedCropCode, int startIndex, int limit, String branchId);

	public List<Object[]> listFarmCropsByCropCode(String selectedCropCode, String branchId);

	public List<Object[]> listFarmCropsDetailsByCropCode(String selectedCropCode, String branchId);

	public List<Object[]> listFarmCropsDetailsByCropCode(String selectedCropCode, int startIndex, int limit,
			String branchId);

	public List<FarmCatalogue> listSeedTreatmentDetailsBasedOnType();

	public FarmCatalogue findfarmcatalogueById(String farmCatalogueId);

	public void addImageInfo(ImageInfo imageInfo);

	public void updateFarmerImageInfo(long farmerId, long imageInfoId);

	public List<Object> listPeriodicInsoectionFatherName();

	public List<String[]> listByFatherNameList();

	public List<Object[]> listFarmerCodeIdNameByVillageCode(String villageId);

	public List<Object[]> listSeasonCodeAndName();

	public HarvestSeason findSeasonNameByCode(String seasonCode);

	public List<Object[]> listHarvestSeasonFromToPeriod();

	public List<Object[]> listFarmerInfo();

	public List<FarmerSoilTesting> listFarmerSoilTestingByFarmId(String farmId);

	public String findHarvestSeasonBycodeusingname(String name);

	public void addfarmerlanddetail(FarmerLandDetails farmlandList);

	public List<Object[]> listfarmingseasonlist();

	public String findSanghamTypeFromWarehouseByWarehouseCode(String warehouseCode);

	public List<FarmerLandDetails> listFarmingSystemByFarmId(long id);

	public FarmerLandDetails findFarmerLandDetailsById(long id);

	public List<FarmerSourceIncome> listFarmSourceIncomeByFarmerId(String farmerId);

	public List<FarmerIncomeDetails> listFarmerIncomeDetailsBySourceIncomeId(String id);

	public void removeFarmerSourceIncome(FarmerSourceIncome farmerSourceIncome);

	/* public void deleteFarmerIncomeDetails(String id); */

	public void deleteFarmerIncomeDetails(String tableName, String columnName);

	public void updateFarmerIdInFarmerSourceIncome(String sourceIncomeIds, String farmerId);

	public void deleteFarmerLandDetailById(long farmerLandId);

	public List<Expenditure> listExpentitureListByFarmId(Long valueOf);

	public HarvestSeason findHarvestSeasosnByName(String name);

	public Warehouse findWareHouseByFarmerId(String farmerId);

	public DocumentUpload findDocumentById(Long id);

	public String findBySeasonCode(String seasonCode);

	public List<FarmCatalogue> listCatelogueTypeWithOther(String type);

	public List<FarmerCropProdAnswers> listFarmerwithCategoryCode(String categoryCode);

	public List<FarmersQuestionAnswers> listQuestionBySection(long id);

	public List<Object> listFarmerCountByBranch();

	public List<Object> listTotalFarmAcreByBranch(String selectedBranch);

	public List<Object> listSeedTypeCount();

	public List<Object> listSeedSourceCount();

	public List<Object> listSeedSourceCountBySource(String selectedSeedSource);

	public void deleteObject(Object object);

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

	public void editPeriodicInspection(PeriodicInspection existing);

	public List<PeriodicInspectionData> listPestRecomentedById(Long inspId);

	public List<PeriodicInspectionData> listFungicideById(Long inspId);

	public List<PeriodicInspectionData> listManureById(Long inspId);

	public List<PeriodicInspectionData> listFertilizerById(Long inspId);

	public List<PeriodicInspectionData> listDiseaseById(Long inspId);

	public List<Object[]> listFarmerFarmInfo();

	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm, String selectedOrganicStatus, String selectedFarmer);

	public void updateFarmerUtzStatus(String farmId, String certificationStatus);

	public void updateSamithiUtzStatus(String farmerId, String certificationStatus);

	public void editFarmerStatus(long id, int statusCode, String statusMsg);

	public List<Object[]> listPeriodicInspectionVillage();

	public int findLandPreparationDetailsByFarmCode(String farmCode, String seasonCode);

	public PeriodicInspection findperiodicInspectionByBranchIdAndFarmId(String branchId, String farmId);

	public int findWeedingStatusByCode(String branchId, String Code, String farmCode, String seasonCode);

	public int findUsageCountforFertilizerFromCultivationDetails(String branchId, String Code, String farmCode, Long l,
			String seasonCode);

	public void updateFarmerRevisionNoAndBasicInfo(long farmerId, Long revisionNo, int basicInfo);

	public int findPickingStatusByCode(String branchId, String Code, String farmCode, String seasonCode);

	public Farm findFarmByFarmerId(long farmerId);

	public List<Object[]> listFarmByFarmerrId(String farmerId);

	public List<Object[]> listFarmerInfoByCultivation(String branch);

	public List<Object[]> listFarmerInfoByDistribution();

	public List<Object[]> listFarmerInfoByProcurement();

	public List<Object[]> listFarmerInfoByCropHarvest();

	public List<Object[]> listFarmerInfoByCropSale();

	public void save(Object obj);

	public void update(Object obj);

	public ResearchStation findResearchStation(long id);

	public void addResearchStation(ResearchStation researchStation);

	public void editResearchStation(ResearchStation researchStation);

	public void removeResearchStation(ResearchStation researchStation);

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

	public ResearchStation findResearchStationId(String researchStationId);

	public void addCow(Cow cow);

	public HousingInfo findByHousingInfo(long farmId);

	public Cow findByCowId(String cowId);

	public List<Calf> listOfCalfs(long id);

	public void updateCow(Cow cow);

	public void removeCow(Cow cow);

	public void addCostFarming(CostFarming costFarming);

	public void addCowInspection(CowInspection cowInspection);

	public Date findByLastInspDate(String cowId);

	public void addHousingInfo(HousingInfo housingInfo);

	public HousingInfo findByHousingId(Long valueOf);

	public void removeHousingInfo(HousingInfo housingInfo);

	public CowInspection findCowInspectionById(Long valueOf);

	public List<Object[]> findByCowInspFarmer();

	public List<Object[]> findByResearchStation();

	public List<String> findByCowList();

	public byte[] findCowInspectionCowVoiceById(Long valueOf);

	public Integer findCowCount();

	public Integer findCowCountByMonth(Date firstDateOfMonth, Date date);

	public void updateResearchStatRevisionNo(long id, long revisionNumber);

	public List<Object[]> findFarmInfoByFarmerId(Long id);

	public List<Object[]> findByResearchStationList();

	public List<CostFarming> findByCowList(long cowId);

	public List<Object> findMilkingCountByCow(String iS_MILKING);

	public List<Object> listCowCountByVillage();

	public List<Object> listCowCountByRS();

	public List<Object> listCowCountByDiseaseName();

	public List<Object> listTotalFarmAcreByVillage();

	public List<Object> findByCowCost();

	public List<CowInspection> findByCowList(String id);

	public Farm findFarmByFarmId(String farmId);

	public List<Object[]> listActiveContractFarmersFieldsBySeasonRevNoAndSamithi(long id, String currentSeasonCode,
			Long revisionNo);

	public List<Object[]> listFarmFieldsByFarmerId(List<Long> farmerIds);

	public List<Object[]> listFarmCropsFieldsByFarmerId(Long farmId);

	public List<Cow> listCowFieldsByFarmId(Long farmId);

	public List<Object[]> listFarmIcsByFarmId();

	public InterestCalcConsolidated findInterestCalcConsolidatedByfarmerProfileIdOpt(String farmerProfileId);

	public long listOfFarmerCountBySamithiId(long id);

	public Integer findMinVarietyIntervalDays();

	public List<Object[]> findFarmCropProductList();

	public Object[] findFarmFarmerAndYieldCount(String code);

	public List<Object[]> findSowingDateAndInterval(String code);

	public List<Cultivation> findCOCByFarmerIdFarmIdCropCodeSeason(String farmerId, String farmId, String cropCode,
			String seasonCode);

	public List<Object[]> findCropProductBySeason(String code, String varietyCode);

	public Object[] findFarmFarmerAndYieldCountBySeason(String code);

	public List<FarmCrops> listOfVarietyByCropTypeFarmCodeAndCrop(String selectedCropType, String selectedFarm,
			String selectedCropName);

	public String findFarmTotalLandAreaCount(String BranchId);

	public long findFarmerCropProdAnswerByFarmerIdAndCategoryCode(String farmerId, String code);

	public List<Cultivation> listCultivationExpenses();

	public List<Object[]> findFarmerCountWithPhotos(String branchId);

	public List<Object[]> findFarmCountWithPhotos(String branchId);

	public List<Object[]> findFarmCountWithGPSTag(String branchId);

	public List<Object> findFarmerCountByICconversion(String selectedBranch, Integer selectedYear,
			String selectedSeason, String selectedGender, String selectedState);

	public List<Object> findTotalFarmAcreAndYieldByBranch(String selectedBranch, Integer selectedYear);

	public List<Object[]> listOfCropNamesByCropTypeAndFarm(String selectedCropType, String selectedFarm);

	public Integer findTotalFarmerCount(String selectedBranch, String selectedStaple, String selectedSeason,
			String selectedGender, String selectedState);

	public Integer findTotalFarmCount(String selectedBranch, Integer selectedYear);

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

	public Integer findTotalFarmerCountByStapleLength(String selectedBranch, int i, List<String> stapleCode);

	public List<String> findFarmerIdsByfarmCode(List<String> farmCodes);

	public List<Object> findTotalFarmAcreAndYieldAndHarvstByBranch(String selectedBranch, int parseInt);

	public String findTotalQtyByFarm(List<String> farmCodes);

	public Object[] listObjectCultivationExpenses(String selectedBranch, String selectedSeason, String selectedYear);

	public Double findCottonIncByFarmerCode(List<String> farmerIds, List<String> farmCodes);

	public String findTotalLandByFarmCode(List<String> farmerIds, List<String> farmCodes);

	public List<Object> findTotalFarmAcreAndYieldAndStapleByBranch(String selectedBranch, int parseInt,
			List<String> stapleCode);

	public List<String> findTotalFarmCodeByBranch(String selectedBranch, int selectedYear, String selectedSeason);

	public List<String> findTotalFarmCodeByBranch(String selectedBranch, int selectedYear, List<String> stapleCode);

	public String findTotalLandAcre(String selectedBranch, int parseInt, String selectedSeason);

	public List<String> findFarmCodesByCultivation(String selectedBranch, String selectedSeason, String selectedYear);

	public String findByFarmCode(String farmCode);

	public double findTotalCottonAreaCount();

	public double findTotalCottonAreaCountByMonth(Date sDate, Date eDate);

	public void updateFarmerStatus(long id);

	public List<Object> findFarmerDetailsByStateAndCrop(String selectedBranch, Long selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender);

	public Integer findFarmerCountByStateAndCrop(String selectedBranch, int i, String selectedCrop,
			String selectedCooperative, String selectedGender);

	public List<Object> listFarmerCountByGroupAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedCooperative, String selectedGender, String selectedCrop, String typez,
			String selectedVillage);

	public Farm findFarmByFarmCode(String farmId);

	public FarmCatalogue findfarmcatalogueByCode(String code);

	public Double findTotalYieldByBranch(String selectedBranch, String selectedStaple, String selectedSeason,
			String selectedState);

	public List<Object> listFarmerCountByFpoGroup();

	public Double findCocCostByCropBranchCooperativeAndGender(String branchId, String selectedCooperative,
			String selectedCrop, String selectedGender, String selectedYear, String selectedState);

	public Integer findFarmerCountByState(String selectedBranch, Integer selectedState);

	public List<Object> findTotalFarmAcreAndYieldByState(String selectedBranch, Long selectedState);

	public Long findTotalIncomeFromCottonByState(String selectedBranch, String selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender);

	public Integer findTotalCultivationProdLandByState(String selectedBranch, String selectedState, String selectedCrop,
			String selectedCooperative, String selectedGender);

	public String findCropHarvestByYearAndBranch(String selectedBranch, Integer selectedYear);

	public Double findCropSupplyByYearAndBranch(String selectedBranch, Integer selectedYear);

	public List<Object> listSeedTypeCountByBranch(String selectedSeedType, String selectedCrop,
			String selectedCooperative, String selectedGender);

	public List<Object> listSeedSourceCountBySource(String selectedBranch, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedVariety, String selectedState);

	public List<Object> listTotalFarmAcreByBranch();

	public String findComponentNameByDynamicField(String compName);

	public void addDynamicComponents(DynamicFieldConfig dynamicFieldConfig);

	public List<DynamicSectionConfig> findDynamicFieldsBySectionId(String sectionId);

	public Object[] findDynamicValueByFarmerId(String componentName, long farmerId);

	public List<Object[]> listFarmerFarmInfoByVillageIdImg(Long villageId, Long farmerId, Long farmId);

	public List<Object[]> findFarmerByVillageCodeAndProcurement(String villageCode);

	public List<Object[]> findFarmerByFpo(String fpo);

	/** END */

	public List<DynamicFieldConfig> listDynamicFields();

	public List<DynamicSectionConfig> listDynamicSections();

	public List<FarmerFeedbackEntity> findFamerFromFarmerFeedback();

	public AnimalHusbandary findAnimalHusbandaryByFarmerIdAndId(Long farmerId, Long animalId);

	public FarmIcsConversion findFarmIcsConversionByFarmId(Long farmId);

	public List<Object[]> listFarmerBySamithi(Long valueOf);

	public FarmCrops findFarmCropsByFarmCodeAndCategory(long farmId);

	public List<java.lang.Object[]> listCropsByFarmIdBasedOnCategory(Long farmId);

	public Integer findCropCount();

	public Object[] findTotalQtyByCropHarvest(String selectedBranch, String selectedSeason, String selectedStaple,
			String selectedState);

	public List<Object[]> listFarmerIdAndName();

	public Double findSaleCottonByCoc(List<String> farmerIds, List<String> farmCodes, String selectedSeason);

	public String findTotalLandByPostHarvest(String selectedBranch, int i, String selectedSeason);

	public Long findFarmCountByFarmerId(Long farmerId);

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

	public Farm findFarmByFarmCodeAndSeason(String farmId, String season);

	public Integer findFarmersCountByStatusByTypez();

	public Farmer findFarmerById(Long id, String branchId);

	public List<Object[]> listUomType(String id);

	public List<Object[]> listQtyByUomAndType(String id, String uom);

	public Farmer findFarmerByFarmerName(String farmerName);

	public List<CultivationDetail> listCultivationDetail();

	public List<PeriodicInspection> listPeriodicInspection();

	public Farmer findFarmerByTraceId(String traceId);

	public List<Cultivation> findCOCByFarmerIdFarmIdSeason(String farmerId, String farmId, String seasonCode);

	String findCropNameByCropCode(String cropCode);

	public Map listActivityReport(String sord, String sidx, int startIndex, int limit, Date getsDate, Date geteDate,
			ViewFarmerActivity filter, int page, Object object);

	List<Object[]> listFcpawithCategoryCode(String categoryCode);

	public List<Object> findICSFarmerCountByGroup(String selectedBranch, int selectedYear, String selectedSeason,
			int icsType);

	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByFarmerId(Long farmerId, String txnType);

	public List<Object[]> ListOfFarmerHealthAssessmentByFarmerId();

	public List<Object[]> ListOfFarmerSelfAssesmentByFarmerId();

	public List<FarmerDynamicFieldsValue> listDynmaicFieldsInfo();

	public List<java.lang.Object[]> listFarmCropsFieldsByFarmerId(List<Long> farmIds);

	public Integer findFarmerSowingCount(String selectedBranch, int i, String selectedSeason);

	public Integer findFarmerPreHarvestCount(String selectedBranch, int i, String selectedSeason, String selectedGender,
			String selectedState);

	public Integer findFarmerPostHarvestCount(String selectedBranch, int i, String selectedSeason,
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

	public Object[] findTotalQty(String warehouse, String order, String selectedProduct, String vendor, Date startDate,
			Date endDate, String receipt, String seasonCode, String branchIdParma);

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

	public List<Object[]> listICSByGroup(long id);

	public List<Object[]> listIcsStatusByFarmer(String selectedFarmer);

	public List<Object[]> findDynamicSectionFieldsByTxnType(String txnType);

	List<DynamicSectionConfig> listDynamicSectionByTxnType(String txnType);

	public List<Object[]> findDynamicSectionFields();

	public List<FarmerDynamicFieldsValue> listFarmerDynmaicFieldsByTxnId(String selectedObject);

	FarmerDynamicData findFarmerDynamicData(String id);

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
			String string);

	public List<Object[]> findTotalAreaProdByIcs(String selectedSeason);

	public GMO findGMOById(Long valueOf);

	public void editGMO(GMO gmo);

	public void removeGMO(GMO gmo);

	public void saveGMO(GMO gmo);

	public int findMaxTypeId();

	public GinnerQuantitySold findGinnerQtySoldById(Long valueOf);

	public void editGinnerQtySold(GinnerQuantitySold ginnerQtySold);

	public void removeGinnerQtySold(GinnerQuantitySold ginnerQtySold);

	public void saveGinnerQtySold(GinnerQuantitySold ginnerQuantitySold);

	public List<Object[]> findTotalGinnerQty(String selectedSeason, String selectedBranch);

	public List<Object[]> findGmoPercentage(String selectedSeason, String selectedBranch);

	public List<MasterData> listMasterType();

	public void updateDynmaicImage(FarmerDynamicFieldsValue fdfv, String parentId);

	public DynamicImageData findDynamicImageDataById(Long id);

	public List<Object> listOfICS();

	public List<Object[]> listProcurementTraceabilityCity();

	public List<Object[]> listProcurementTraceabilityVillage();

	public List<Object> listFarmerFpo();

	public List<Object[]> listcooperativeByFarmer(String selectedFarmer);

	public List<Object[]> findfpoByFarmerID(long id);

	public double findTotalStockInHeap(String gining, String procurementProduct, String ics, String heapDataName,
			String branchIdParmam, String season);

	public List<Object[]> findFarmerDatasByFarmerID(long id);

	public List<DynamicFeildMenuConfig> listDynamicMenus();

	public List<FarmerDynamicFieldsValue> listFarmerDynamicFieldsValuePhotoByRefTxnType(String refId, String txnType);

	public void saveDynamicSection(DynamicSectionConfig dsc);

	public void saveDynamicField(DynamicFieldConfig dfc);

	public void updateDynamicField(DynamicFieldConfig dfc);

	public List<DynamicFieldConfig> getListComponent(String sectionCode);

	public void updateDynamicSection(DynamicSectionConfig dsc);

	public DynamicFieldConfig findDynamicFieldConfigById(Long id);

	public CottonPrice findProdPriceById(Long id);

	public void editProdPrice(CottonPrice productPrice);

	public void saveCottonPrice(CottonPrice cottonPrice);

	public void removeCottonPrice(CottonPrice cottonPrice);

	public String findMspByStapleLen(String selectedSeason, String selectedStapleLen);

	public GMO findGMOType(String type, String season);

	public CottonPrice findStapleByCottonPrice(String string, String seasonCode);

	public List<DynamicFeildMenuConfig> findDynamicMenusByType(String txnTypez);

	public List<Object> listFarmerFpoFromTraceabilityStock();

	public List<Object[]> listFarmerIDAndName();

	public List<Object[]> listFarmIDAndName();

	public void updateFarmICSStatusByFarmId(long farmId, String icsType);

	public List<CropSupplyImages> listCropSupplyImages(long id);

	public CropSupplyImages loadCropSupplyImages(Long id);

	public void addcropSupplyImages(CropSupplyImages cropSupplyImages);

	public List<DynamicFieldConfig> listDynamicFieldsBySectionId(List<String> selectedSection);

	public List<DynamicSectionConfig> listDynamicSectionsBySectionId(List<String> selectedSection);

	public List<DynamicFieldConfig> listDynamicFieldsByCodes(List<String> selectedSection);

	public void addDynamicFeildMenuConfig(DynamicFeildMenuConfig dynamicFeildMenuConfig);

	public DynamicFeildMenuConfig findDynamicMenuConfigById(Long id);

	public void editDynamicFeildMenuConfig(DynamicFeildMenuConfig tempDynamicMenuConfig);

	public List<Object[]> findDynamicSectionByMenuId(Long menuId);

	public List<Object[]> findDynamicFieldsByMenuId(Long menuId);

	public DynamicSectionConfig findDynamicSectionByName(String sectionName);

	public List<String> listOfDynamicSections();

	public List<Object[]> listOfDynamicFields(List<String> selectedSections);

	public List<String> listOfDynamicFields();

	public void removedynamicFeildMenuConfig(DynamicFeildMenuConfig dynamicFeildMenuConfig);

	public Object[] listFarmInfoByCode(String code);

	public List<DynamicFieldReportJoinMap> findDynamicFieldReportJoinMapByEntityAndTxn(String entity, String txnType);

	public List<DynamicFieldReportConfig> findDynamicFieldReportConfigByEntityAndTxn(String entity, String txnType);

	public Farm findFarmByID(Long id);

	public List<String> findFarmCodsByStapleLen(String selectedSeason, String selectedStapleLen, String branch);

	public List<Object[]> findSalePriceByFarmCodes(List<String> farmCodes);

	public List<Object[]> getParentMenus();

	public List<Object[]> findSubMenusByParentMenuId(String parentMenuId);

	public void delete_subMenu(Long id, String name);

	public void save_subMenu(String parentId, String menuName, String menuDescription, String menuUrl, String menuOrder,
			String ese_ent_name, String ese_action_actionId, String role_id);

	public List<Object[]> getAction();

	public List<Object[]> getRole();

	public List<FarmCatalogue> listHeapData();

	public List<Object> listFarmerCountByGroupAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedCooperative, String selectedGender, String typez);

	public void removeFarmCoordinates(Long id);

	public Farm listOfFarmCoordinateByFarmId(long id);

	public List<FarmCrops> listFarmCropsByFarmId(long farmId);

	public List<Object[]> listFarmerBySamithiId(long id);

	public void removeFarmCropCoordinates(Long id);

	public Long findCropIdByFarmCode(String farmCode);

	public List<Object[]> listFarmerInformationByCultivation();

	public List<FarmerLocationMapField> listRemoveFarmerLocMapFields();

	public void deleteField(DynamicFieldConfig dfc);

	public void deleteListFields(Long id);

	public DynamicSectionConfig findDynamicSectionConfigById(Long valueOf);

	public void deleteSection(DynamicSectionConfig dsc);

	public FarmerDynamicData findFarmerDynamicData(String txnType, String referenceId);

	public void saveOrUpdate(FarmerDynamicData farmerDynamicData, Map<String, List<FarmerDynamicFieldsValue>> fdMap,
			LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap);

	public FarmerDynamicData findFarmerDynamicDataBySeason(String txnType, String id, String season);

	public List<DynamicFeildMenuConfig> findDynamicMenusByMType(String txnType);

	public void saveOrUpdate(FarmerDynamicData fyd);

	public void deleteChildObjects(String txnType);

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

	public String findCropNameByCropId(Long valueOf);

	public Object[] findFarmInfoById(Long id);

	public Object[] findFarmerInfoById(Long id);

	public List<java.lang.Object[]> listActiveContractFarmersFieldsBySeasonRevNoAndSamithiWithGramp(long id,
			String currentSeasonCode, Long revisionNo, List<String> branch);

	public List<DynamicFieldConfig> getLitsComponentsBySectionCodeAndListId(String sectionCode, String id);

	public void updateListComponentsOrder(Long id);

	public void updateOrderForSubMenus(Long valueOf, int order);

	public List<Object[]> isAlreadyAvailableMenuByParentId(Long parentId, String menuName);

	public boolean isPlottingExist(Long id);

	public List<Object[]> listFarmListByFarmerId(Long farmerId);

	public List<Object[]> listFarmerAddressByFarmerId(String farmerId);

	public List<DynamicFieldConfig> getFieldsListForFormula();

	public ForecastAdvisoryDetails findforeCastAdvisoryDetailsById(Long id);

	public ForecastAdvisory findforeCastAdvisoryById(Long id);

	public List<Object[]> findAdvisoryList();

	public List<Object[]> findForeCastList(Long cropId);

	public void addForeCast(Forecast foreCast);

	public void removeForecast();

	public void updateDependencyKeyForDependentFieldsOfFormula(String string, String formula_label_code);

	public List<DynamicFieldConfig> availableFieldsForList();

	public void updateFieldsReferenceId(Long listId, List<Long> fieldsIdList);

	public void updateListButtonOrder(Long listId);

	List<Object[]> listFarmFieldsByFarmerId(long id);

	public List<Object[]> listCropNamesWithFarm();

	public List<Farmer> findExistingFarmerByFarmerId(String valueOf);

	public void updateExistingFarmerFlagById(List<Farmer> existing_farmer);

	public List<Object[]> findFarmerTraceDetailsByFarmCode(String farmCode, String tenantId);

	public Farmer findFarmerByID(Long id, String tenantId);

	public List<java.lang.Object[]> listDistributionBalanceDownload(long id, String strevisionNo);

	public String findTotalAcreAndEstYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedSeason);

	public DistributionBalance findDistributionBalanceItem(Long id, String procurementProduct);

	public void updateDistributionBalance(DistributionBalance db);

	public void addDistributionBalance(DistributionBalance distBal);

	public List<DistributionBalance> listDistributionBalanceList(Long id);

	public DistributionBalance findDistributionBalanceById(long templateId);

	public void removeDistributionBalance(DistributionBalance distBal);

	public void editDistributionBalance(DistributionBalance distBal);

	public List<Object[]> listCultivationByFarmerIncome(String branchId);

	public FarmIcsConversion findFarmIcsConversionByFarmIdWithActive(Long farmId);

	public FarmIcsConversion findFarmIcsConversionByFarmIdAndInspectionTypeAndScopeOperationWithInspectionDate(
			long selectedFarm, String insType, String scope, int inspectionDateYear);

	public void updateFarmICSStatusByFarmIdInsTypeAndIcsType(Long farmId, String insType, String icsType);

	public void updateFarmIcsConversionByFarmId(long farmId, FarmIcsConversion existing);

	public FarmIcsConversion findFarmIcsConversionById(Long id);

	public void saveFarmIcsConversionByFarmId(long farmId, FarmIcsConversion farmIcsconversion);

	public List<Farmer> listFarmerByFarmerIdByIdList(List<String> ids);

	public void addFarmIcsConversion(FarmIcsConversion existing);

	public FarmIcsConversion findFarmIcsConversionByFarmIcsConvId(Long id);

	public Object[] findTotalAcreAndEstimatedYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedSeason, String selectedFarmer,
			String selectedOrganicStatus);

	public void insertDistBalance(String stringCellValue, String string, Double valueOf);

	public List<Object[]> populateStateAndFarmerCountList(String selectedBranch);

	public List<Object[]> populateDistrictAndFarmerCountList(String selectedBranch);

	public List<Object[]> populateDistrictAndactiveFarmers(String selectedBranch);

	public Object[] findFarmerCountByStateName(String stateName);

	public BigInteger[] getPramidChartDetails();

	public List<DynamicFieldConfig> listDynamicFieldsBySectionCode(String sectionCode);

	public void addNewComponentIntoList(Long component_id, Long list_id);

	public List<DynamicFeildMenuConfig> listDynamicMenusByRevNo(String revisionNo);

	public List<Object[]> findTotalWeightAmtNameByProcurement(String hqlQueryAppnd, Map<String, Object> params);

	public List<Object[]> findSeasonCodeList(String selectedBranch);

	public List<Object[]> findProductsByWarehouse(String warehouse_chart, String chartType);

	public List<Object[]> listDistributionWarehouse(String branch, String season, String chartType);

	public List<Object[]> findAmtAndQtyByProcurment(String selectedBranch, String selectedState, String selectedSeason,
			Date sDate, Date eDate);

	public Object[] findSaleIncomeByFarmer(List<String> farmerIds, List<String> farmCodes);

	public List<Object[]> getCertifiedFarmerCount();

	public List<Object[]> getNonCertifiedFarmerCount();

	public List<Object[]> listOfFarmersByFarmCrops();

	public Object[] findTotalAmtAndweightByProcurementWithDate(String filterList, Map<String, Object> params,
			Date convertStringToDate, Date convertStringToDate2, String branch);

	public double findStockAdded(Long distId, Long warehouseId);

	public List<Object[]> farmersByBranch();

	public List<Object[]> farmersByCountry(String selectedBranch);

	public List<Object[]> farmersByState(String selectedBranch);

	public List<Object[]> farmersByLocality(String selectedBranch);

	public List<Object[]> farmersByMunicipality(String selectedBranch);

	public List<Object[]> farmersByGramPanchayat(String selectedBranch);

	public List<Object[]> farmersByVillageWithGramPanchayat(String selectedBranch);

	public List<Object[]> farmersByVillageWithOutGramPanchayat(String selectedBranch);

	public List<Object[]> farmerDetailsByVillage(String selectedBranch);

	public List<Object[]> getFarmDetailsAndProposedPlantingArea(String locationLevel, String selectedBranch,
			String gramPanchayatEnable);

	public List<Object[]> populateFarmerLocationCropChart(String codeForCropChart);

	public List<Object[]> estimatedAndActualYield(String codeForCropChart);

	public List<Object[]> listfarmerwithoutorganic();

	public List<Object[]> listFarmFieldsByFarmerIdAndNonOrganic(long id);

	public List<Object[]> listFarmFieldsByFarmerIdNonCertified(long id);

	public List<DynamicImageData> listDynamicImageByIds(List<Long> isList);

	public void updateDistributionStatus(int deleteStatus, long distId);

	public void addAgroTxn(AgroTransaction agroTxn);

	public List<Object[]> estimatedYield(String codeForCropChart);

	public List<Object[]> actualYield(String codeForCropChart);

	List<Farmer> listFarmerByIds(List<String> ids);

	public List<DynamicConstants> listDynamicConstants();

	public String getFieldValueByContant(String entityId, String referenceId, String group);

	public List<Object[]> warehouseToMobileUserChart(String selectedBranch);

	public List<Object[]> warehouseToMobileUser_AgentChart(String selectedBranch, String selectedWarehouse);

	public List<Object[]> warehouseToMobileUser_ProductChart(String selectedBranch, String selectedWarehouse,
			String agentId);

	public List<Object[]> populateMobileUserToFarmer_AgentChart(String selectedBranch, String season);

	public List<Object[]> populateMobileUserToFarmer_FarmerChart(String selectedBranch, String agentId);

	public List<Object[]> populateMobileUserToFarmer_ProductChart(String selectedBranch, String agentId,
			String farmerId);

	public List<Object[]> populateWarehouseToFarmer_ProductChart(String selectedBranch, String selectedWarehouse,
			String farmerId);

	public List<Object[]> populateWarehouseToFarmer_FarmerChart(String selectedBranch, String selectedWarehouse);

	public List<Object[]> populateWarehouseToFarmer_WarehouseChart(String selectedBranch);

	public List<Object[]> productChartByWarehouseToMobileUser(String selectedBranch, String selectedWarehouse);

	public List<Object[]> productChartByMobileUserToFarmer(String selectedBranch, String agentId,
			String selectedSeason);

	public List<Object[]> productChartByWarehouseToFarmer(String selectedBranch, String selectedWarehouse);

	public List<Object[]> listParentFields();

	public List<Object[]> ListFarmerDynamicDataAgentByTxnType(String txnType, String branchId);

	public List<Object[]> populateAvailableColumns();

	public List<Object[]> getGridData(String query);

	public List<FarmerDynamicData> ListFarmerDynamicDatas(long l);

	public FarmerDynamicData findFarmerDynamicDataByTxnUniquId(String txnUniqueId);

	public List<Object[]> cropHarvestByFarmerId(String farmerId, String startDate, String endDate, String seasonCode);

	public List<Object[]> distributionToFarmerByFarmerId(String farmerId, String startDate, String endDate,
			String seasonCode);

	public List<Object[]> productReturnByFarmerId(String farmerId, String startDate, String endDate, String seasonCode);

	public List<Object[]> trainingStatusReportByFarmerId(String farmerId, String startDate, String endDate);

	public List<Object[]> farmerBalanceReportByFarmerId(String farmerId, String startDate, String endDate);

	public List<Object[]> procurementTransactionsByFarmerId(String farmerId, String start, String end,
			String seasonCode);

	public void saveDynamicReport(String reportTitle, String reportDescription, String outPutQuery,
			String header_fields, String filter_data, String entity, String selectedFields, String groupByField,
			String id);

	public void deleteDynamicReportById(String saved_dynamic_report_id);

	public void updateDynamicReportById(String saved_dynamic_report_id, String reportTitle, String reportDescription,
			String query, String header_fields, String filter_data, String entity, String selectedFields,
			String groupByField);

	public void addTreeDetails(TreeDetail treeDetail);

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

	void saveCropCalendar(CropCalendar cropCalendar);

	public Farmer findFarmerByFirstNameLastNameAndSurName(String firstName, String lastName, String surName);

	void updateCropCalendarDetails(CropCalendarDetail cropCalendarDetails);

	void removeCropCalendar(CropCalendar cropCalendar);

	public List<Object[]> listColdStorageNameDynamic();

	public String isFarmInventoryMappingexist(long id);

	public String isFarmerMappingexist(long id);

	public WarehouseStorageMap findWarehouseStorageMapByWarehouseId(Long id);

	public List<WarehouseStorageMap> listOfwarehouseStorageMap(long id);

	public List<Object[]> listFarmCropsFieldsByFarmerIdAgentIdAndSeason(long agentId, Long revisionNo,
			String seasonCode);

	void processCustomisedFormula(FarmerDynamicData farmerDynamicData, Map<String, DynamicFieldConfig> fieldConfigMap);

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

	public String getValueByQuery(String methodName, Object[] arr, String branchId);

	public DynamicFieldScoreMap findDynamicFieldScoreByFieldIdAndComponentCode(String id, String code);

	public List<Object[]> getValueListByQuery(String methodName, Object[] parameter, String branchId);

	public List<Object[]> listFarmerFarmInfoFarmCropsByVillageId(FarmCrops crops);

	public List<Object[]> listOfFarmersByVillageAndFarmCrops(long villageId,String plottingType);

	public Integer findFarmersCountSowingLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer);

	public Object[] findTotalAcreAndEstimatedYieldSwoingLoca(String selectedCrop, String selectedState,
			String selectedLocality, String selectedTaluk, String selectedVillage, String selectedFarmer);

	public List<Object[]> listFarmerFarmCropInfoByVillageIdImg(String cropId, Long farmCropId, Long farmId);

	void saveSectionBatch(List<DynamicSectionConfig> dscList);

	public long findDynamicFieldsCountByCode(String code);

	public long findDynamicSectionCountByCode(String sectionCode);

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

	public List<CropYieldDetail> findCropYieldDetailById(Long valueOf);

	public List<FarmerDynamicFieldsValue> listFarmerDynamicFieldByFdIds(List<Long> ids);

	public void updateFDVS(List<FarmerDynamicFieldsValue> fdvs);

	public List<Object[]> listFDVSForFolloUp( String agentId, String folloRev);

	public void updateFarmerDynamicData(FarmerDynamicData farmerDynamicData,
			LinkedHashMap<String, DynamicFieldConfig> fieldConfigMap,Map<String, List<FarmerDynamicFieldsValue>> fdMap);

	public List<Object[]> findFarmerByListOfFarmerId(List<String> farmerList);

	public String findComponentNameByDynamicFieldCode(String fieldCode);

	public List<Object[]> listFarmerDynamicFieldsValuesByFarmIdList(List<String> farmIdList,List<String> selectedDynamicFieldCodeList);
	
	public Farmer findFarmerByMSISDN(String msisdn);

	public List<java.lang.Object[]> listDynamicFieldsCodeAndName();

	public List<DynamicMenuFieldMap> listDynamisMenubyscoreType(int i);

	public List<Object[]> listFarmerCodeIdNameByStateCode(String selectedState);

	public List<DynamicFeildMenuConfig> listDynamicMenusByRevNo(String revisionNo, String branchId,String tenantId);
	
	public List<DynamicFeildMenuConfig> findDynamicMenusByType(String txnTypez, String branchId);
	
	public List<DynamicFeildMenuConfig> findDynamicMenusByTypeForOCP(String txnTypez, String branchId);

	public List<Object[]> listValueByFieldName(String field, String branchId);

	public FarmCrops findFarmCropFullObjectById(String id);

	public FarmerDynamicFieldsValue findFarmerDynamicFieldsValueById(String id);

	public FarmerDynamicData processCustomisedFormula(FarmerDynamicData farmerDynamicData, Map<String, DynamicFieldConfig> fieldConfigMap,
			Map<String, List<FarmerDynamicFieldsValue>> fdMap);

	ColdStorage findColdStorageById(String id);

	public ColdStorageDetail findColdStorageDetailById(Long valueOf);

	public List<DynamicMenuFieldMap> listDynamisMenubyscoreType();
	
	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm, String selectedOrganicStatus,String selectedFarmer,List<Long> yieldEstimationDoneFarmers);

	public List<FarmerDynamicData> listFarmerDynamicDataByTxnId(String txnType);
	
	public List<Object[]> listfarmerDynamicData(List<Long> fidLi);
	
	public FarmCrops findFarmCropByCropIdAndFarmIdAndSeason(long farmId, long varietyId, long seasonId);
	
	public List<Object> listFarmerCountByGroupTraderAndBranchStateCoop(String selectedBranch, String selectedState,
			String selectedCooperative, String selectedGender, String selectedCrop, String typez,
			String selectedVillage);
	
	public List<Object> listFarmerCountByFarmInspection(Date sDate, Date eDate);

	public FarmerDynamicFieldsValue findLotCodeFromFarmerDynamicFieldsValue(Long id,String fieldName);
	
	public List<Object[]> populateTrainingChart(String selectedBranch, String selectedFinYear);

	public Integer findFarmerCountByStateAndCrop(String selectedBranch, int i, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedFinYear);

	public List<Object> findFarmerDetailsByStateAndCrop(String selectedBranch, Long valueOf, String selectedCrop,
			String selectedCooperative, String selectedGender, String selectedFinYear);
	
	public List<Object[]> productChartByMobileUserToFarmer(String selectedBranch, String agentId,
			String selectedSeason,String selectedFinYear);
	
	public List<FarmCropsField> listRemoveFarmCropFields();
	public CropYield findMoleculeDateByLotCode(String lotCode);

	public List<Object[]> listFarmsLastInspectionDate();
	
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
	
	
	public List<Object[]> listVendorByLoanStatusAndFarmerAndVendor(Long farmerId, String vendorId);

	public List<Object[]> listOfProofNo();
	
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
	
	public List<Object[]> listFarmerFarmInfoByVillageId(Farm farm,String selectedOrganicStatus,String selectedFarmer,List<Long> yieldEstimationDoneFarmers,
			String selectedStatus );

	public Integer findFarmersCountFarmerLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus,String selectedStatus);
	
	public Object[] findTotalAcreAndEstimatedYield(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer, String selectedSeason,
			String selectedOrganicStatus,String selectedStatus);

	
	public List<Object[]> farmersByGroup(String selectedBranch);
	
	public List<Object[]> populateFarmerLocationCropChartByGroup(String codeForCropChart,String selectedBranch,String seasonCode);
	
	public List<Object[]> getFarmDetailsAndProposedPlantingAreaByGroup(String locationLevel1, String selectedBranch,String gramPanchayatEnable);
	
	List<Object[]> findFarmerCountByGroupICS(String selectedBranch);

	
	public List<Object[]> listFarmerFarmInfoFarmCropsByVillageId(Object obj,String selectedStatus,String plottingType);
	
	public Integer findFarmersCountSowingLoca(String selectedCrop, String selectedState, String selectedLocality,
			String selectedTaluk, String selectedVillage, String selectedFarmer,String selectedStatus);

	public Object[] findTotalAcreAndEstimatedYieldSwoingLoca(String selectedCrop, String selectedState,
			String selectedLocality, String selectedTaluk, String selectedVillage, String selectedFarmer,String selectedStatus,String selectedFarm);

	public List<DynamicFeildMenuConfig> findDynamicMenus();
	
	public List<Object[]> farmersByBranchandSeason(String season);
	
	public List<Object[]> farmersByBranchandSeason(String branch,String season);
	
	public List<Object[]>farmersByCountryAndSeason(String selectedBranch,String seasonCode);
	
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

	public void processProfileUpdates(Map<String, Object> profileUpdateFields,
			Map<String, DynamicFieldConfig> fieldConfigMap, FarmerDynamicData farmerDynamicData);
	
	public List<DataLevel> listDataLevelByType(String type);
	
	public SamithiIcs findSamithiIcsById(Long id);
	
	public List<Object[]> listFarmInfoLatLon();
	
	public List<Object[]> findCountOfDynamicDataByFarmerId(List<Long> fidLi,String season);
	
    public ESEAccount findAccountByFarmerLoanProduct(long farmerId);
	
	public LoanInterest findLoanPercent(Long amt);

	public LoanInterest findLoanRangeById(Long id);

	public void removeLoanRange(LoanInterest loanrange);
	
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
	
	public List<byte[]> getImageByQuery(String methodName, Object[] arr, String branchId);
	
	public List<Long> listFarmerPrimaryId(Farmer f);

	public List<Long> listProfilePrimaryId(Agent agent);

	public List<Long> listWebUsersPrimaryId(User user);

	public List<Object[]> listFarmerFarmInfoByLotNoFromFarmerTraceabilityData(String selectedLotNo, String branchId);

	public Object[] findFarmersCountFromLotTraceByLotNo(String selectedLotNo);


}