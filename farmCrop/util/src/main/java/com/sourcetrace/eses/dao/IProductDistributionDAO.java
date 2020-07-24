/*
\ * IProductDistributionDAO.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.traceability.BaleGeneration;
import com.ese.entity.traceability.GinningProcess;
import com.ese.entity.traceability.HeapData;
import com.ese.entity.traceability.LedgerData;
import com.ese.entity.traceability.ProcurementTraceability;
import com.ese.entity.traceability.ProcurementTraceabilityStock;
import com.ese.entity.traceability.ProcurementTraceabilityStockDetails;
import com.ese.entity.traceability.SpinningTransfer;
import com.ese.entity.util.LoanLedger;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.ColdStorageDetail;
import com.sourcetrace.eses.entity.FarmCatalogue;
import com.sourcetrace.eses.entity.MasterData;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.entity.WarehouseProductDetail;
import com.sourcetrace.eses.order.entity.profile.GradeMaster;
import com.sourcetrace.eses.order.entity.profile.GradeMasterPricing;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.order.entity.txn.AgentMovement;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.eses.order.entity.txn.CityWarehouseDetail;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransfer;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransferDetail;
import com.sourcetrace.eses.order.entity.txn.DMT;
import com.sourcetrace.eses.order.entity.txn.DMTDetail;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.DistributionStock;
import com.sourcetrace.eses.order.entity.txn.DistributionStockDetail;
import com.sourcetrace.eses.order.entity.txn.FarmCropCalendar;
import com.sourcetrace.eses.order.entity.txn.LoanDistribution;
import com.sourcetrace.eses.order.entity.txn.LoanDistributionDetail;
import com.sourcetrace.eses.order.entity.txn.MTNT;
import com.sourcetrace.eses.order.entity.txn.MTNTDetail;
import com.sourcetrace.eses.order.entity.txn.OfflineBaleGeneration;
import com.sourcetrace.eses.order.entity.txn.OfflineDistribution;
import com.sourcetrace.eses.order.entity.txn.OfflineFarmCropCalendar;
import com.sourcetrace.eses.order.entity.txn.OfflineGinningProcess;
import com.sourcetrace.eses.order.entity.txn.OfflineMTNT;
import com.sourcetrace.eses.order.entity.txn.OfflinePMTNR;
import com.sourcetrace.eses.order.entity.txn.OfflinePayment;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurement;
import com.sourcetrace.eses.order.entity.txn.OfflineProcurementTraceability;
import com.sourcetrace.eses.order.entity.txn.OfflineProductReturn;
import com.sourcetrace.eses.order.entity.txn.OfflineSpinningTransfer;
import com.sourcetrace.eses.order.entity.txn.OfflineSupplierProcurement;
import com.sourcetrace.eses.order.entity.txn.PMT;
import com.sourcetrace.eses.order.entity.txn.PMTDetail;
import com.sourcetrace.eses.order.entity.txn.PMTFarmerDetail;
import com.sourcetrace.eses.order.entity.txn.PMTImageDetails;
import com.sourcetrace.eses.order.entity.txn.PaymentLedger;
import com.sourcetrace.eses.order.entity.txn.PaymentMode;
import com.sourcetrace.eses.order.entity.txn.PricePattern;
import com.sourcetrace.eses.order.entity.txn.PricePatternDetail;
import com.sourcetrace.eses.order.entity.txn.Procurement;
import com.sourcetrace.eses.order.entity.txn.ProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.ProductReturn;
import com.sourcetrace.eses.order.entity.txn.ProductReturnDetail;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurement;
import com.sourcetrace.eses.order.entity.txn.SupplierProcurementDetail;
import com.sourcetrace.eses.order.entity.txn.SupplierWarehouse;
import com.sourcetrace.eses.order.entity.txn.SupplierWarehouseDetail;
import com.sourcetrace.eses.order.entity.txn.TripSheet;
import com.sourcetrace.eses.order.entity.txn.TruckStock;
import com.sourcetrace.eses.order.entity.txn.VillageWarehouse;
import com.sourcetrace.eses.util.profile.Product;

import com.sourcetrace.esesw.entity.profile.CropCalendarDetail;
import com.sourcetrace.esesw.entity.profile.Customer;
import com.sourcetrace.esesw.entity.profile.DistributionBalance;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.Municipality;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Season;
import com.sourcetrace.esesw.entity.profile.TreeDetail;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.entity.profile.WarehousePaymentDetails;
import com.sourcetrace.esesw.entity.profile.WarehouseStockReturn;

/**
 * The Interface IProductDistributionDAO.
 */
public interface IProductDistributionDAO extends IESEDAO {

	/**
	 * Find distribution by rec no.
	 * 
	 * @param receiptNo
	 *            the receipt no
	 * @return the distribution
	 */
	public Distribution findDistributionByRecNo(String receiptNo);

	/**
	 * List distribution detail.
	 * 
	 * @param id
	 *            the id
	 * @return the list< distribution detail>
	 */
	public List<DistributionDetail> listDistributionDetail(long id);

	/**
	 * List distribution detail.
	 * 
	 * @param id
	 *            the id
	 * @param startIndex
	 *            the start index
	 * @param limit
	 *            the limit
	 * @return the list< distribution detail>
	 */
	public List<DistributionDetail> listDistributionDetail(long id, int startIndex, int limit);

	/**
	 * Find procurement by rec no.
	 * 
	 * @param receiptNo
	 *            the receipt no
	 * @return the procurement
	 */
	public Procurement findProcurementByRecNo(String receiptNo);

	/**
	 * Find procurement product by code.
	 * 
	 * @param productCode
	 *            the product code
	 * @return the procurement product
	 */
	public ProcurementProduct findProcurementProductByCode(String productCode);

	/**
	 * Find procurement varierty by id.
	 * 
	 * @param id
	 *            the id
	 * @return the procurement variety
	 */
	public ProcurementVariety findProcurementVariertyById(Long id);

	/**
	 * Find procurement varierty by code.
	 * 
	 * @param productCode
	 *            the product code
	 * @return the procurement variety
	 */
	public ProcurementVariety findProcurementVariertyByCode(String productCode);

	/**
	 * Find procurement varierty by name.
	 * 
	 * @param name
	 *            the name
	 * @return the procurement variety
	 */
	public ProcurementVariety findProcurementVariertyByName(String name);

	/**
	 * Find procurement grade by id.
	 * 
	 * @param id
	 *            the id
	 * @return the procurement grade
	 */

	public ProcurementGrade findProcurementGradeById(Long id);

	/**
	 * Find procurement grade by code.
	 * 
	 * @param productCode
	 *            the product code
	 * @return the procurement grade
	 */

	public ProcurementGrade findProcurementGradeByCode(String productCode);

	/**
	 * Find procurement grade by name.
	 * 
	 * @param name
	 *            the name
	 * @return the procurement grade
	 */

	public ProcurementGrade findProcurementGradeByName(String name);

	/**
	 * Find procurement grade by price.
	 * 
	 * @param price
	 *            the price
	 * @return the procurement grade
	 */

	public ProcurementGrade findProcurementGradeByPrice(Double price);

	/**
	 * Find mtnt by receipt no.
	 * 
	 * @param receiptNo
	 *            the receipt no
	 * @return the mTNT
	 */
	public MTNT findMTNTByReceiptNo(String receiptNo);

	/**
	 * List mtnt detail list.
	 * 
	 * @param mtntId
	 *            the mtnt id
	 * @return the list< mtnt detail>
	 */
	public List<MTNTDetail> listMTNTDetailList(long mtntId);

	/**
	 * List mtnt detail list.
	 * 
	 * @param mtntId
	 *            the mtnt id
	 * @param startIndex
	 *            the start index
	 * @param limit
	 *            the limit
	 * @return the list< mtnt detail>
	 */
	public List<MTNTDetail> listMTNTDetailList(long mtntId, int startIndex, int limit);

	/**
	 * Find village warehouse by village code.
	 * 
	 * @param id
	 *            the id
	 * @return the list< village warehouse>
	 */
	public List<VillageWarehouse> findVillageWarehouseByVillageCode(long id);

	/**
	 * Find village warehouse by village and product.
	 * 
	 * @param villageId
	 *            the village id
	 * @param productId
	 *            the product id
	 * @return the village warehouse
	 */
	public VillageWarehouse findVillageWarehouseByVillageAndProduct(long villageId, long productId);

	/**
	 * Removes the mtnt detail.
	 * 
	 * @param id
	 *            the id
	 */
	public void removeMTNTDetail(long id);

	/**
	 * Removes the distribution detail.
	 * 
	 * @param id
	 *            the id
	 */
	public void removeDistributionDetail(long id);

	/**
	 * Removes the procurement detail.
	 * 
	 * @param procurementId
	 *            the procurement id
	 */
	public void removeProcurementDetail(long procurementId);

	/**
	 * Find village warehouse by village code and agent id.
	 * 
	 * @param id
	 *            the id
	 * @param agentId
	 *            the agent id
	 * @return the list< village warehouse>
	 */
	public List<VillageWarehouse> findVillageWarehouseByVillageCodeAndAgentId(long id, String agentId);

	/**
	 * Find village warehouse.
	 * 
	 * @param villageId
	 *            the village id
	 * @param productId
	 *            the product id
	 * @param agentId
	 *            the agent id
	 * @return the village warehouse
	 */
	public VillageWarehouse findVillageWarehouse(long villageId, long productId, String agentId);

	/**
	 * Checks if is pending mtnt exists for agent.
	 * 
	 * @param agentId
	 *            the agent id
	 * @return true, if is pending mtnt exists for agent
	 */
	public boolean isPendingMTNTExistsForAgent(String agentId);

	/**
	 * List village warehouse for agent.
	 * 
	 * @param agentId
	 *            the agent id
	 * @return the list< village warehouse>
	 */
	public List<VillageWarehouse> listVillageWarehouseForAgent(String agentId);

	/**
	 * List pending mtnt exists by agent id.
	 * 
	 * @param agentId
	 *            the agent id
	 * @return the list< village warehouse>
	 */
	public List<VillageWarehouse> listPendingMTNTExistsByAgentId(String agentId);

	/**
	 * Removes the pending mtnt stock by agent id.
	 * 
	 * @param agentId
	 *            the agent id
	 */
	public void removePendingMTNTStockByAgentId(String agentId);

	/**
	 * Checks if is village mappind exist for distribution and procurement.
	 * 
	 * @param id
	 *            the id
	 * @return the string
	 */
	public String isVillageMappindExistForDistributionAndProcurement(long id);

	/**
	 * Find village warehouse stock.
	 * 
	 * @param villageId
	 *            the village id
	 * @param procurementProductId
	 *            the procurement product id
	 * @param agentId
	 *            the agent id
	 * @param numberOfBags
	 *            the number of bags
	 * @param grossWeight
	 *            the gross weight
	 * @return the village warehouse
	 */
	public VillageWarehouse findVillageWarehouseStock(long villageId, long procurementProductId, String agentId,
			long numberOfBags, double grossWeight);

	/**
	 * List pending offline procurement list.
	 * 
	 * @return the list< offline procurement>
	 */
	public List<OfflineProcurement> listPendingOfflineProcurementList(String tenantId);

	/**
	 * List offline distribution.
	 * 
	 * @return the list< offline distribution>
	 */
	public List<OfflineDistribution> listOfflineDistribution(String tenantId);

	/**
	 * List pending offline mtnt and mtnr.
	 * 
	 * @return the list< offline mtn t>
	 */
	public List<OfflineMTNT> listPendingOfflineMTNTAndMTNR();

	/**
	 * Find mtn by receipt no and type.
	 * 
	 * @param receiptNo
	 *            the receipt no
	 * @param type
	 *            the type
	 * @return the mTNT
	 */
	public MTNT findMTNByReceiptNoAndType(String receiptNo, int type);

	/**
	 * Find distribution by rec no and oper type.
	 * 
	 * @param receiptNo
	 *            the receipt no
	 * @param operationType
	 *            the operation type
	 * @return the distribution
	 */
	public Distribution findDistributionByRecNoAndOperType(String receiptNo, int operationType);

	/**
	 * Find procurement by rec no and oper type.
	 * 
	 * @param receiptNo
	 *            the receipt no
	 * @param operationType
	 *            the operation type
	 * @return the procurement
	 */
	public Procurement findProcurementByRecNoAndOperType(String receiptNo, int operationType, String tenantId);

	/**
	 * Find mtn by receipt no type and oper type.
	 * 
	 * @param receiptNo
	 *            the receipt no
	 * @param type
	 *            the type
	 * @return the mTNT
	 */
	public MTNT findMTNByReceiptNoTypeAndOperType(String receiptNo, int type);

	/**
	 * Find offline distribution by id.
	 * 
	 * @param id
	 *            the id
	 * @return the offline distribution
	 */
	public OfflineDistribution findOfflineDistributionById(long id);

	/**
	 * Find offline procurement by id.
	 * 
	 * @param id
	 *            the id
	 * @return the offline procurement
	 */
	public OfflineProcurement findOfflineProcurementById(long id);

	/**
	 * Find offline mtnt by id.
	 * 
	 * @param id
	 *            the id
	 * @return the offline mtnt
	 */
	public OfflineMTNT findOfflineMTNTById(long id);

	/**
	 * List procurement product by type.
	 * 
	 * @param type
	 *            the type
	 * @return the list< procurement product>
	 */
	public List<ProcurementProduct> listProcurementProductByType(int type);

	/**
	 * Find available stock.
	 * 
	 * @param warehouseId
	 *            the warehouse id
	 * @param productId
	 *            the product id
	 * @return the warehouse product
	 */
	public WarehouseProduct findAvailableStock(long warehouseId, long productId);

	/**
	 * Find agro txn by receipt no.
	 * 
	 * @param receiptNo
	 *            the receipt no
	 * @return the list< agro transaction>
	 */
	public List<AgroTransaction> findAgroTxnByReceiptNo(String receiptNo);

	/**
	 * Find procurement product by id.
	 * 
	 * @param id
	 *            the id
	 * @return the procurement product
	 */
	public ProcurementProduct findProcurementProductById(long id);

	/**
	 * List farmer transaction history.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @param transactionArray
	 *            the transaction array
	 * @param limit
	 *            the limit
	 * @return the list< agro transaction>
	 */
	public List<AgroTransaction> listFarmerTransactionHistory(String farmerId, String[] transactionArray, int limit);

	/**
	 * Find truck stock by truck id.
	 * 
	 * @param truckId
	 *            the truck id
	 * @return the truck stock
	 */
	public TruckStock findTruckStockByTruckId(String truckId);

	/**
	 * Find truck stock by id.
	 * 
	 * @param id
	 *            the id
	 * @return the truck stock
	 */
	public TruckStock findTruckStockById(Long id);

	/**
	 * List seasons.
	 * 
	 * @return the list< season>
	 */
	public List<Season> listSeasons();

	/**
	 * List payment mode.
	 * 
	 * @return the list< payment mode>
	 */
	public List<PaymentMode> listPaymentMode();

	/**
	 * Find grade by id.
	 * 
	 * @param id
	 *            the id
	 * @return the grade master
	 */
	public GradeMaster findGradeById(long id);

	/**
	 * List grade master.
	 * 
	 * @return the list< grade master>
	 */
	public List<GradeMaster> listGradeMaster();

	/**
	 * Find village warehouse.
	 * 
	 * @param villageId
	 *            the village id
	 * @param productId
	 *            the product id
	 * @param agentId
	 *            the agent id
	 * @param qualityCode
	 *            the quality code
	 * @return the village warehouse
	 */
	public VillageWarehouse findVillageWarehouse(long villageId, long productId, String agentId, String qualityCode);

	/**
	 * Find payment mode by code.
	 * 
	 * @param paymentType
	 *            the payment type
	 * @return the payment mode
	 */
	public PaymentMode findPaymentModeByCode(String paymentType);

	/**
	 * Find season by season code.
	 * 
	 * @param seasonCode
	 *            the season code
	 * @return the season
	 */
	public Season findSeasonBySeasonCode(String seasonCode);

	/**
	 * Find trip sheet by city date chart no.
	 * 
	 * @param cityId
	 *            the city id
	 * @param date
	 *            the date
	 * @param chartNo
	 *            the chart no
	 * @return the trip sheet
	 */
	public TripSheet findTripSheetByCityDateChartNo(long cityId, Date date, String chartNo);

	/**
	 * Find grade by code.
	 * 
	 * @param code
	 *            the code
	 * @return the grade master
	 */
	public GradeMaster findGradeByCode(String code);

	/**
	 * List procurement product.
	 * 
	 * @return the list< procurement product>
	 */
	public List<ProcurementProduct> listProcurementProduct();

	/**
	 * Find grade pricing exist.
	 * 
	 * @param cityId
	 *            the city id
	 * @param seasonId
	 *            the season id
	 * @param productId
	 *            the product id
	 * @param gradeMasterId
	 *            the grade master id
	 * @return the grade master pricing
	 */
	public GradeMasterPricing findGradePricingExist(long cityId, long seasonId, long productId, long gradeMasterId);

	/**
	 * List pmt receipt number by status.
	 * 
	 * @param status
	 *            the status
	 * @return the list< string>
	 */
	public List<String> listPMTReceiptNumberByStatus(int status);

	/**
	 * Find pmt by receipt number.
	 * 
	 * @param receiptNumber
	 *            the receipt number
	 * @param status
	 *            the status
	 * @return the pMT
	 */
	public PMT findPMTByReceiptNumber(String receiptNumber, int status);

	/**
	 * Find central warehouse by quality.
	 * 
	 * @param gradeCode
	 *            the grade code
	 * @return the city warehouse
	 */
	public CityWarehouse findCentralWarehouseByQuality(String gradeCode);

	/**
	 * Find pmt by receipt number.
	 * 
	 * @param receiptNumber
	 *            the receipt number
	 * @return the pMT
	 */
	public PMT findPMTByReceiptNumber(String receiptNumber);

	/**
	 * Find season by id.
	 * 
	 * @param id
	 *            the id
	 * @return the season
	 */
	public Season findSeasonById(long id);

	/**
	 * Find city warehouse.
	 * 
	 * @param cityId
	 *            the city id
	 * @param productId
	 *            the product id
	 * @param agentId
	 *            the agent id
	 * @param quality
	 *            the quality
	 * @return the city warehouse
	 */
	public CityWarehouse findCityWarehouse(long cityId, long productId, String agentId, String quality);

	/**
	 * List grade master pricing by city id season id product id.
	 * 
	 * @param cityId
	 *            the city id
	 * @param seasonId
	 *            the season id
	 * @param productId
	 *            the product id
	 * @return the list< grade master pricing>
	 */
	public List<GradeMasterPricing> listGradeMasterPricingByCityIdSeasonIdProductId(long cityId, long seasonId,
			long productId);

	/**
	 * List municipality from city warehouse.
	 * 
	 * @return the list< municipality>
	 */
	public List<Municipality> listMunicipalityFromCityWarehouse();

	/**
	 * List trip sheet by city id transit status.
	 * 
	 * @param cityId
	 *            the city id
	 * @param transitStatus
	 *            the transit status
	 * @return the list< trip sheet>
	 */
	public List<TripSheet> listTripSheetByCityIdTransitStatus(long cityId, int transitStatus);

	/**
	 * List grade information by trip sheet id product id.
	 * 
	 * @param tripSheetId
	 *            the trip sheet id
	 * @param procurementProductId
	 *            the procurement product id
	 * @return the list< object[]>
	 */
	public List<Object[]> listGradeInformationByTripSheetIdProductId(long tripSheetId, long procurementProductId);

	/**
	 * Find trip sheet by id.
	 * 
	 * @param id
	 *            the id
	 * @return the trip sheet
	 */
	public TripSheet findTripSheetById(long id);

	/**
	 * Find available stock by agent id.
	 * 
	 * @param warehouseId
	 *            the warehouse id
	 * @param productId
	 *            the product id
	 * @param agentId
	 *            the agent id
	 * @return the warehouse product
	 */
	public WarehouseProduct findAvailableStockByAgentId(long warehouseId, long productId, long agentId);

	/**
	 * Find agent available stock.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param productId
	 *            the product id
	 * @return the warehouse product
	 */
	public WarehouseProduct findAgentAvailableStock(String agentId, long productId);

	/**
	 * Find warehouse product available stock.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param productId
	 *            the product id
	 * @return the warehouse product
	 */
	public WarehouseProduct findWarehouseProductAvailableStock(String agentId, long productId);

	/**
	 * Find mtnt receipt no by receiver warehouse.
	 * 
	 * @param selectedWarehouse
	 *            the selected warehouse
	 * @return the list< dm t>
	 */
	public List<DMT> findMTNTReceiptNoByReceiverWarehouse(String selectedWarehouse);

	/**
	 * Find mtnt details.
	 * 
	 * @param receiverWarehouseId
	 *            the receiver warehouse id
	 * @param selectedMTNTReceiptNo
	 *            the selected mtnt receipt no
	 * @return the dMT
	 */
	public DMT findMTNTDetails(String receiverWarehouseId, String selectedMTNTReceiptNo);

	/**
	 * Find mtnt product details.
	 * 
	 * @param dmtId
	 *            the dmt id
	 * @return the list< dmt detail>
	 */
	public List<DMTDetail> findMTNTProductDetails(long dmtId);

	/**
	 * Find dmt by mtnt receipt no.
	 * 
	 * @param selectedMTNTReceiptNo
	 *            the selected mtnt receipt no
	 * @return the dMT
	 */
	public DMT findDMTByMTNTReceiptNo(String selectedMTNTReceiptNo);

	/**
	 * Find dmt product by product id.
	 * 
	 * @param productId
	 *            the product id
	 * @param dmtId
	 *            the dmt id
	 * @return the dMT detail
	 */
	public DMTDetail findDMTProductByProductId(long productId, long dmtId);

	/**
	 * Checks if is agent warehouse product stock exist.
	 * 
	 * @param profileId
	 *            the profile id
	 * @return true, if is agent warehouse product stock exist
	 */
	public boolean isAgentWarehouseProductStockExist(String profileId);

	/**
	 * List central warehouse.
	 * 
	 * @return the list< object[]>
	 */
	public List<Object[]> listCentralWarehouse();

	/**
	 * List dmt detail product list.
	 * 
	 * @param productId
	 *            the product id
	 * @param dmtId
	 *            the dmt id
	 * @return the list< dmt detail>
	 */
	public List<DMTDetail> listDMTDetailProductList(long productId, long dmtId);

	/**
	 * List warehouse product by agent id.
	 * 
	 * @param agentId
	 *            the agent id
	 * @return the list< warehouse product>
	 */
	public List<WarehouseProduct> listWarehouseProductByAgentId(long agentId);

	/**
	 * List warehouse product by city code.
	 * 
	 * @param code
	 *            the code
	 * @return the list< warehouse product>
	 */
	public List<WarehouseProduct> listWarehouseProductByCityCode(String code);

	/**
	 * List warehouse product by product id.
	 * 
	 * @param productId
	 *            the product id
	 * @return the list< warehouse product>
	 */
	public List<WarehouseProduct> listWarehouseProductByProductId(long productId);

	/**
	 * List pending payment txn.
	 * 
	 * @return the list< offline payment>
	 */
	public List<OfflinePayment> listPendingPaymentTxn();

	/**
	 * List dmt detail.
	 * 
	 * @param id
	 *            the id
	 * @return the list< dmt detail>
	 */
	public List<DMTDetail> listDMTDetail(long id);

	/**
	 * List sender warehouse.
	 * 
	 * @return the list< warehouse>
	 */
	public List<Warehouse> listSenderWarehouse();

	/**
	 * List mtnt receiver warehouse.
	 * 
	 * @return the list< warehouse>
	 */
	public List<Warehouse> listMTNTReceiverWarehouse();

	/**
	 * List mtnr receiver warehouse.
	 * 
	 * @return the list< warehouse>
	 */
	public List<Warehouse> listMTNRReceiverWarehouse();

	/**
	 * List warehouse product for agent warehouse.
	 * 
	 * @param cityId
	 *            the city id
	 * @return the list< warehouse product>
	 */
	public List<WarehouseProduct> listWarehouseProductForAgentWarehouse(long cityId);

	/**
	 * Find mtnt by agent warehouse.
	 * 
	 * @param cityId
	 *            the city id
	 * @return the list< dm t>
	 */
	public List<DMT> findMTNTByAgentWarehouse(long cityId);

	/**
	 * Find dmt by mtnr receipt no.
	 * 
	 * @param mtnrReceiptNo
	 *            the mtnr receipt no
	 * @return the dMT
	 */
	public DMT findDMTByMTNRReceiptNo(String mtnrReceiptNo);

	/**
	 * List of pmtnt reportby pmt id.
	 * 
	 * @param id
	 *            the id
	 * @param tripsheetIds
	 *            the tripsheet ids
	 * @return the list< object[]>
	 */
	public List<Object[]> listOfPMTNTReportbyPMTId(long id, String tripsheetIds);

	/**
	 * List trip sheets ids by pmt id.
	 * 
	 * @param id
	 *            the id
	 * @param startIndex
	 *            the start index
	 * @param endIndex
	 *            the end index
	 * @return the list< big integer>
	 */
	public List<BigInteger> listTripSheetsIdsByPMTId(long id, int startIndex, int endIndex);

	/**
	 * Find pmt by id.
	 * 
	 * @param id
	 *            the id
	 * @return the pMT
	 */
	public PMT findPMTById(Long id);

	/**
	 * List procurement mtnt.
	 * 
	 * @return the list< pm t>
	 */
	public List<PMT> listProcurementMTNT();

	/**
	 * Find pmt by mtnr receipt number.
	 * 
	 * @param mtnrReceiptNo
	 *            the mtnr receipt no
	 * @return the pMT
	 */
	public PMT findPMTByMTNRReceiptNumber(String mtnrReceiptNo);

	/**
	 * List dmt detail.
	 * 
	 * @param id
	 *            the id
	 * @param type
	 *            the type
	 * @param startIndex
	 *            the start index
	 * @param limit
	 *            the limit
	 * @return the list< dmt detail>
	 */
	public List<DMTDetail> listDMTDetail(long id, String type, int startIndex, int limit);

	/**
	 * List price pattern by rev no and season.
	 * 
	 * @param revisionNo
	 *            the revision no
	 * @param seasonCode
	 *            the season code
	 * @return the list< price pattern>
	 */
	public List<PricePattern> listPricePatternByRevNoAndSeason(Long revisionNo, String seasonCode);

	/**
	 * Find agent product details.
	 * 
	 * @param id
	 *            the id
	 * @return the list< warehouse product>
	 */
	public List<WarehouseProduct> findAgentProductDetails(long id);

	/**
	 * Find price pattern by id.
	 * 
	 * @param id
	 *            the id
	 * @return the price pattern
	 */
	public PricePattern findPricePatternById(long id);

	/**
	 * Find price pattern by name.
	 * 
	 * @param name
	 *            the name
	 * @return the price pattern
	 */
	public PricePattern findPricePatternByName(String name);

	/**
	 * Removes the price pattern detail.
	 * 
	 * @param id
	 *            the id
	 */
	public void removePricePatternDetail(long id);

	/**
	 * List price pattern by season procurement product.
	 * 
	 * @param seasonId
	 *            the season id
	 * @param procurementProductId
	 *            the procurement product id
	 * @return the list< price pattern>
	 */
	public List<PricePattern> listPricePatternBySeasonProcurementProduct(long seasonId, long procurementProductId);

	/**
	 * List price pattern detail by price pattern.
	 * 
	 * @param pricePatternId
	 *            the price pattern id
	 * @return the list< price pattern detail>
	 */
	public List<PricePatternDetail> listPricePatternDetailByPricePattern(long pricePatternId);

	/**
	 * Find distribution by id.
	 * 
	 * @param id
	 *            the id
	 * @return the distribution
	 */
	public Distribution findDistributionById(Long id);

	/**
	 * List pmt sender city.
	 * 
	 * @return the list< municipality>
	 */
	public List<Municipality> listPMTSenderCity();

	/**
	 * Find city warehouse by agent id.
	 * 
	 * @param agentId
	 *            the agent id
	 * @return the list< city warehouse>
	 */
	public List<CityWarehouse> findCityWarehouseByAgentId(String agentId);

	/**
	 * List pmt receiver cities.
	 * 
	 * @return the list< municipality>
	 */
	public List<Municipality> listPMTReceiverCities();

	/**
	 * Find cooperative available stock by field staff.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param id
	 *            the id
	 * @return the warehouse product
	 */
	public WarehouseProduct findCooperativeAvailableStockByFieldStaff(String agentId, long id);

	/**
	 * Find distribution by receipt no txn type.
	 * 
	 * @param receiptNo
	 *            the receipt no
	 * @param txnType
	 *            the txn type
	 * @return the distribution
	 */
	public Distribution findDistributionByReceiptNoTxnType(String receiptNo, String txnType, String tenantId);

	/**
	 * List price pattern.
	 * 
	 * @return the list< price pattern>
	 */
	public List<PricePattern> listPricePattern();

	/**
	 * List procurement stocks for agent.
	 * 
	 * @param agentId
	 *            the agent id
	 * @return the list< city warehouse>
	 */
	public List<CityWarehouse> listProcurementStocksForAgent(String agentId);

	/**
	 * Find city warehouse by village.
	 * 
	 * @param villageId
	 *            the village id
	 * @param productId
	 *            the product id
	 * @param agentId
	 *            the agent id
	 * @param quality
	 *            the quality
	 * @return the city warehouse
	 */
	public CityWarehouse findCityWarehouseByVillage(long villageId, long productId, String agentId, String quality);

	/**
	 * Find city warehouse by co operative.
	 * 
	 * @param coOperativeId
	 *            the co operative id
	 * @param productId
	 *            the product id
	 * @param agentId
	 *            the agent id
	 * @param quality
	 *            the quality
	 * @return the city warehouse
	 */
	public CityWarehouse findCityWarehouseByCoOperative(long coOperativeId, long productId, String agentId,
			String quality);

	/**
	 * Find city warehouse by co operative.
	 * 
	 * @param coOperativeId
	 *            the co operative id
	 * @param productId
	 *            the product id
	 * @param agentId
	 *            the agent id
	 * @return the city warehouse
	 */
	public CityWarehouse findCityWarehouseByCoOperative(long coOperativeId, long productId, String agentId);

	/**
	 * Find city warehouse by village.
	 * 
	 * @param villageId
	 *            the village id
	 * @param productId
	 *            the product id
	 * @param agentId
	 *            the agent id
	 * @return the city warehouse
	 */
	public CityWarehouse findCityWarehouseByVillage(long villageId, long productId, String agentId);

	/**
	 * Find price pattern detail by product farmer season grade.
	 * 
	 * @param farmerId
	 *            the farmer id
	 * @param seasonId
	 *            the season id
	 * @param procurementProductId
	 *            the procurement product id
	 * @param gradeCode
	 *            the grade code
	 * @return the price pattern detail
	 */
	public PricePatternDetail findPricePatternDetailByProductFarmerSeasonGrade(long farmerId, long seasonId,
			long procurementProductId, String gradeCode);

	/**
	 * List price pattern detail by product season.
	 * 
	 * @param productId
	 *            the product id
	 * @param seasonId
	 *            the season id
	 * @return the list< price pattern detail>
	 */
	public List<PricePatternDetail> listPricePatternDetailByProductSeason(long productId, long seasonId);

	/**
	 * Find cooperative available stock by cooperative manager.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param productId
	 *            the product id
	 * @return the warehouse product
	 */
	public WarehouseProduct findCooperativeAvailableStockByCooperativeManager(String agentId, long productId);

	/**
	 * Find price pattern detail by id.
	 * 
	 * @param id
	 *            the id
	 * @return the price pattern detail
	 */
	public PricePatternDetail findPricePatternDetailById(long id);

	/**
	 * List grade master by procurement product id.
	 * 
	 * @param productId
	 *            the product id
	 * @return the list< grade master>
	 */
	public List<GradeMaster> listGradeMasterByProcurementProductId(Long productId);

	/**
	 * Find procurement product by name.
	 * 
	 * @param name
	 *            the name
	 * @return the procurement product
	 */
	public ProcurementProduct findProcurementProductByName(String name);

	/**
	 * Find grade by name.
	 * 
	 * @param gradeName
	 *            the grade name
	 * @return the grade master
	 */
	public GradeMaster findGradeByName(String gradeName);

	/**
	 * Find agro transaction by rec no prof type txn desc and date.
	 * 
	 * @param recNo
	 *            the rec no
	 * @param profType
	 *            the prof type
	 * @param txnDesc
	 *            the txn desc
	 * @param date
	 *            the date
	 * @return the agro transaction
	 */
	public AgroTransaction findAgroTransactionByRecNoProfTypeTxnDescAndDate(String recNo, String profType,
			String txnDesc, Date date);

	/**
	 * Find payment agro transaction by rec no prof type.
	 * 
	 * @param recNo
	 *            the rec no
	 * @param profType
	 *            the prof type
	 * @return the agro transaction
	 */
	public AgroTransaction findPaymentAgroTransactionByRecNoProfType(String recNo, String profType);

	/**
	 * List procurement product by revision no.
	 * 
	 * @param revisionNo
	 *            the revision no
	 * @return the list< procurement product>
	 */
	public List<ProcurementProduct> listProcurementProductByRevisionNo(long revisionNo);

	/**
	 * List city warehouse by agent id revision no.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param revisionNo
	 *            the revision no
	 * @return the list< city warehouse>
	 */
	public List<CityWarehouse> listCityWarehouseByAgentIdRevisionNo(String agentId, long revisionNo);

	/**
	 * List grade master by revision no.
	 * 
	 * @param revisionNo
	 *            the revision no
	 * @return the list< grade master>
	 */
	public List<GradeMaster> listGradeMasterByRevisionNo(long revisionNo);

	/**
	 * Find logo image by id.
	 * 
	 * @param id
	 *            the id
	 * @return the byte[]
	 */
	public byte[] findLogoImageById(Long id);

	/**
	 * List season by revision no.
	 * 
	 * @param revisionNo
	 *            the revision no
	 * @return the list< season>
	 */
	public List<Season> listSeasonByRevisionNo(long revisionNo);

	/**
	 * List products by co operative.
	 * 
	 * @param coOperativeId
	 *            the co operative id
	 * @return the list< city warehouse>
	 */
	public List<CityWarehouse> listProductsByCoOperative(long coOperativeId);

	/**
	 * Find city ware house by grade.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param coOperativeId
	 *            the co operative id
	 * @param productId
	 *            the product id
	 * @param gradeCode
	 *            the grade code
	 * @return the city warehouse
	 */
	public CityWarehouse findCityWareHouseByGrade(String agentId, long coOperativeId, long productId, String gradeCode);

	/**
	 * Find precurement product stock.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param coOperativeId
	 *            the co operative id
	 * @param productId
	 *            the product id
	 * @return the double
	 */
	public double findPrecurementProductStock(String agentId, long coOperativeId, long productId);

	/**
	 * Find precurement product stock net wht.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param coOperativeId
	 *            the co operative id
	 * @param productId
	 *            the product id
	 * @return the double
	 */
	public double findPrecurementProductStockNetWht(String agentId, long coOperativeId, long productId);

	/**
	 * Find distribution by rec no and txn type.
	 * 
	 * @param receiptNumber
	 *            the receipt number
	 * @param txnType
	 *            the txn type
	 * @return the distribution
	 */
	public Distribution findDistributionByRecNoAndTxnType(String receiptNumber, String txnType);

	/**
	 * Find txn agro audio file by txn agro id.
	 * 
	 * @param id
	 *            the id
	 * @return the byte[]
	 */
	public byte[] findTxnAgroAudioFileByTxnAgroId(long id);

	/**
	 * Find agent movement by id.
	 * 
	 * @param id
	 *            the id
	 * @return the agent movement
	 */
	public AgentMovement findAgentMovementById(long id);

	/**
	 * Find cooperative available stock by cooperative.
	 * 
	 * @param warehouseId
	 *            the warehouse id
	 * @param productId
	 *            the product id
	 * @return the warehouse product
	 */
	public WarehouseProduct findCooperativeAvailableStockByCooperative(long warehouseId, long productId);

	/**
	 * Find available stocks.
	 * 
	 * @param warehouseCode
	 *            the warehouse code
	 * @param productId
	 *            the product id
	 * @return the warehouse product
	 */
	public WarehouseProduct findAvailableStocks(String warehouseCode, long productId);

	/**
	 * Find field staff available stock.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param productId
	 *            the product id
	 * @return the warehouse product
	 */
	public WarehouseProduct findFieldStaffAvailableStock(String agentId, long productId);

	/**
	 * List procurement product grade by revision no.
	 * 
	 * @param revisionNo
	 *            the revision no
	 * @return the list< procurement grade>
	 */
	public List<ProcurementGrade> listProcurementProductGradeByRevisionNo(Long revisionNo);

	/**
	 * List procurement product variety by revision no.
	 * 
	 * @param revisionNo
	 *            the revision no
	 * @return the list< procurement variety>
	 */
	public List<ProcurementVariety> listProcurementProductVarietyByRevisionNo(Long revisionNo);

	/**
	 * List procurement product grade by procurement product variety id.
	 * 
	 * @param id
	 *            the id
	 * @param revisionNo
	 *            the revision no
	 * @return the list< procurement grade>
	 */
	public List<ProcurementGrade> listProcurementProductGradeByProcurementProductVarietyId(Long id, Long revisionNo);

	/**
	 * List procurement product variety by procurement product id.
	 * 
	 * @param id
	 *            the id
	 * @param revisionNo
	 *            the revision no
	 * @return the list< procurement variety>
	 */
	public List<ProcurementVariety> listProcurementProductVarietyByProcurementProductId(long id, Long revisionNo);

	/**
	 * Find procurement varierty by crop code.
	 * 
	 * @param selectedCrop
	 *            the selected crop
	 * @return the list< procurement variety>
	 */
	public List<ProcurementVariety> findProcurementVariertyByCropCode(String selectedCrop);

	/**
	 * List procurement variety by procurement product id.
	 * 
	 * @param id
	 *            the id
	 * @return the list< procurement variety>
	 */
	public List<ProcurementVariety> listProcurementVarietyByProcurementProductId(Long id);

	/**
	 * List procurement grade by procurement variety id.
	 * 
	 * @param id
	 *            the id
	 * @return the list< procurement grade>
	 */
	public List<ProcurementGrade> listProcurementGradeByProcurementVarietyId(Long id);

	/**
	 * List price by var and grade.
	 * 
	 * @param procurementProductId
	 *            the procurement product id
	 * @param procurementVarId
	 *            the procurement var id
	 * @param procurementGradeId
	 *            the procurement grade id
	 * @return the procurement grade
	 */
	public ProcurementGrade listPriceByVarAndGrade(long procurementProductId, long procurementVarId,
			long procurementGradeId);

	/**
	 * List procurement variety.
	 * 
	 * @return the list< procurement variety>
	 */
	public List<ProcurementVariety> listProcurementVariety();

	/**
	 * List procurement grade.
	 * 
	 * @return the list< procurement grade>
	 */
	public List<ProcurementGrade> listProcurementGrade();

	/**
	 * List procurement grade by procurement product id.
	 * 
	 * @param productId
	 *            the product id
	 * @return the list< procurement grade>
	 */
	public List<ProcurementGrade> listProcurementGradeByProcurementProductId(long productId);

	/**
	 * Find city ware house by warehouse id procrment grade code and procurement
	 * product id.
	 * 
	 * @param warehouseId
	 *            the warehouse id
	 * @param gradeCode
	 *            the grade code
	 * @param productId
	 *            the product id
	 * @return the city warehouse
	 */
	public CityWarehouse findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(long warehouseId,
			String gradeCode, long productId);

	/**
	 * Find precurement product stock net wht by warehouse id and product id.
	 * 
	 * @param warehouseId
	 *            the warehouse id
	 * @param productId
	 *            the product id
	 * @return the double
	 */
	public double findPrecurementProductStockNetWhtByWarehouseIdAndProductId(long warehouseId, long productId);

	public List<Vendor> listVendor();

	public WarehousePayment findWarehouseStockByRecNo(String receiptNumber);

	/**
	 * List by all vendor names for warehouse payment
	 * 
	 * @return List
	 */

	public List<WarehousePayment> selectVendorList();

	/**
	 * List by all order No for warehousepayment
	 * 
	 * @return List
	 */

	public List<WarehousePayment> selectOrderNoList();

	/**
	 * Find by vendorId and orderno for warehousepayment
	 * 
	 * @param selected
	 *            vendor Id
	 * @param selectedOrderNo
	 *            warehousepayment
	 */

	public WarehousePayment findVendorAndOrderNo(Long long1, String selectedOrderNo);

	/**
	 * Find by warehouse paymentId for warehouse payment details
	 * 
	 * @param payementId
	 */

	public List<WarehousePaymentDetails> listWarehousePaymentDetails(long id);

	/**
	 * List by all warehouse names
	 * 
	 * @return
	 */
	public List<Warehouse> listWarehouse();

	public void editWarehouseDamagedStock(String selectedOrderNo, String selectedVendor, long updateDamagedStock);

	public WarehousePaymentDetails findWarehousePaymentIdAndProduct(long id, String damagedProducts);

	public void editWarehousePaymentDetails(long id, String damagedProducts, long damagedStockQty);

	public Distribution findDistributionFarmerByRecNo(String receiptNumber);

	public List<WarehousePayment> selectOrderNoList(long selectedVendor);

	public List<WarehousePayment> warehouseByvendorAndOrderNo(long selectedVendor, String selectedOrderNo);

	public List<WarehousePayment> listAllWarehouse();

	public List<WarehousePayment> loadOrderNobasedOnVendorAndQty(long selectedVendor);

	public WarehousePayment warehousePaymentByVendorAndOrderNo(long selectedVendor, String selectedOrderNo);

	public WarehousePaymentDetails findWarehousePaymentDetail(long id, String damagedProducts);

	public List<String> listStockReturnType();

	public List<String> listOfOrderNo();

	public List<WarehouseProduct> listwarehouseProductByWarehouseId(long selectedWarehouseId);

	public WarehousePaymentDetails WarehousePaymentDetailsByWarehouseProductIds(long productId,
			long warehousePaymentId);

	public Product findProductByProductId(long productId);

	public WarehousePayment isOrderNoExists(String orderNo);

	public List<HarvestSeason> listHarvestSeason();

	public byte[] findLogoCultivateImageById(Long id);

	public List<HarvestSeason> listHarvestSeasonByRevisionNo(Long revNo);

	public List<FarmCatalogue> listCatalogueByRevisionNo(Long revNo);

	public List<Customer> listCustomerByRevisionNo(Long revNo);

	public Object findTotalYieldPriceByHarvestId(long id);

	public WarehouseProduct findAgentAvailableStock(long agentId, long prodId);

	public Boolean isProductDistributionExist(Long id);

	public List<CropHarvestDetails> listCropHarvestDetailsByProcurementProductId(Long id);

	public List<CropSupplyDetails> listCropSaleDetailsByProcurementProductId(Long id);

	public List<CropHarvestDetails> listCropHarvestDetailsByProcurementVarietyId(Long id);

	public List<CropSupplyDetails> listCropSaleDetailsByProcurementVarietyId(Long id);

	public List<CropHarvestDetails> listCropHarvestDetailsByProcurementGradeId(Long id);

	public List<CropSupplyDetails> listCropSaleDetailsByProcurementGradeId(Long id);

	public ProcurementVariety findProcurementVariertyByNameAndBranch(String varietyName, String branchId_F);

	public ProcurementGrade findProcurementGradeByNameAndBranch(String gradeName, String branchId_F);

	public ProcurementProduct findProcurementProductByNameAndBranch(String cropName, String branchId_F);

	public List<DistributionDetail> listDistributionDetailBySubCategory(Long id);

	public List<MasterData> listMasterDataByRevisionNo(Long revNo);

	public List<MasterData> listMasterDataByMasterTypeAndRevisionNo(Long masterType, Long revNo);

	public List<MasterData> listMandiTraderSupplier();

	public List<MasterData> listMandiAggregatorSupplier();

	public List<MasterData> listFarmAggregator();

	public List<MasterData> listSupplierFpo();

	public List<MasterData> listSupplierCig();

	public List<MasterData> listSupplierFig();

	public List<MasterData> listSupplierProducerImporter();

	public Season findSeasonBySeasonCodeByTenantId(String seasonCode, String tenantId);

	public WarehouseProduct findFieldStaffAvailableStockByTenantId(String agentId, long productId, String tenantId);

	public Season findSeasonBySeasonCode(String seasonCode, String tenantId);

	public ProcurementGrade findProcurementGradeByCode(String productCode, String tenantId);

	public void saveAgroTransaction(AgroTransaction agroTransaction, String tenantId);

	public void saveProcurement(Procurement procurement, String tenantId);

	public CityWarehouse findCityWarehouseByCoOperative(long coOperativeId, long productId, String agentId,
			String quality, String tenantId);

	public CityWarehouse findCityWarehouseByVillage(long villageId, long productId, String agentId, String quality,
			String tenantId);

	public void saveCityWarehouseDetail(CityWarehouseDetail cityWarehouseDetail, String tenantId);

	public void saveOrUpdateCityWarehouse(CityWarehouse cityWarehouse, String tenantId);

	public CityWarehouse findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(long warehouseId,
			String gradeCode, long productId, String tenantId);

	public void updateOfflineProcurement(OfflineProcurement offlineProcurement, String tenantId);

	public void saveDistribution(Distribution distribution, String tenantId);

	public WarehouseProduct findAvailableStock(long warehouseId, long productId, String tenanatId);

	public void saveOrUpdate(Object obj, String tenantId);

	public WarehouseProduct findAvailableStocks(String servicePointId, long id, String tenantId);

	public WarehouseProduct findFieldStaffAvailableStock(String agentId, long id, String tenantId);

	public WarehouseProduct findCooperativeAvailableStockByCooperative(long agentId, long productId, String tenantId);

	public WarehouseProduct findAgentAvailableStock(String agentId, long id, String tenantId);

	public void updateOfflineDistribution(OfflineDistribution offlineDistribution, String tenantId);

	public Distribution findDistributionByReceiptNoTxnType(String receiptNo, String txnType);

	public void save(WarehouseProductDetail warehouseProductDetail, String tenantId);

	public List<ProcurementProduct> listFarmCrop();

	public WarehouseStockReturn findStockReturnByRecNo(String receiptNo);

	public void saveByTenantId(Object object, String tenantId);

	public List<OfflineMTNT> listPendingOfflineMTNTAndMTNR(String tenantId);

	public MTNT findMTNByReceiptNoTypeAndOperType(String receiptNo, int type, String tenantId);

	public void save(PMT pmt, String tenantId);

	public List<Object[]> findpmtdetail(int cooperative, int procurementproduct);

	public List<Object[]> listPMTReceiptNumberByWarehouseI(long warehouseId);

	public List<ProcurementProduct> listProcurementProductByReceiptNo(Long receiptNoId);

	public PMTDetail findpmtdetailById(long productId);

	public List<AgroTransaction> findAgroTxnByReceiptNo(String receiptNo, String tenantId);

	public PaymentMode findPaymentModeByCode(String paymentType, String tenantId);

	public List<OfflinePayment> listPendingPaymentTxn(String tenantId);

	public CityWarehouse findCityWarehouseBySamithi(long samithiId, long productId, String quality);

	public CityWarehouse findCityWarehouseByVillageId(long villageId, long productId, String quality);

	public List<CityWarehouse> listFarmersByCoOperative(long coOperativeId);

	public double findFarmerStockNetWgtByWarehouseIdAndFarmerId(long warehouseId, String farmerId);

	public List<CityWarehouse> listProductsByFarmerIdAndCooperativeId(long farmerId, long coOperativeId);

	public List<Procurement> findProcurementByFarmerId(long farmerId);

	public List<ProcurementDetail> findProcurementDetailByProcurementId(long procurementId);

	public ProcurementGrade findProcurementGradeByProcurementGradeId(Long id);

	public List<CityWarehouseDetail> listCityWarehouseDetailsByCityWarehouseId(long cityWarehouseId);

	public CityWarehouse findCityWarehouseByCoOperativeAndFarmer(long coOperativeId, long productId, long farmerId,
			String quality);

	public CityWarehouse findCityWarehouseByVillageAndFarmer(long villageId, long productId, long farmerId,
			String quality);

	public List<ProcurementGrade> listProcurementVarietyByGradeCode(String gradeCode);

	public List<CityWarehouse> listProductsByProductId(long coOperativeId, long farmerId, long productId);

	public CityWarehouse listStockByFarmerIdProductIdGradeCodeAndCooperativeId(long cooperativeId, long farmerId,
			long procurementProductId, String gradeCode);

	public ProcurementVariety findProcurementVariertyByProductCode(String productCode);

	public CityWarehouse listCityWarehouseProductsByProductCode(String productCode);

	public CityWarehouse listCityWareHouseByCooperativeIdFarmerIdAndProductId(long farmerId, long procurementProductId,
			long cooperativeId);

	public CityWarehouse findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductIdAndFarmerId(
			long farmerId, long warehouseId, String gradeCode, long productId);

	public Procurement findProcurementById(Long id);

	public List<ProcurementVariety> listProcurementProductVarietyByvarietyCode(String code);

	public List<ProcurementProduct> listProcurementProductByProductId(long id);

	public List<ProcurementVariety> listProcurementProductVarietyByVarietyId(long id);

	public HarvestSeason findHarvestSeasonBySeasonCode(String seasonCode);

	public Object findNoOfBagByByWarehouseIdProductIdAndFarmerId(Object warehouseId, Object productId, Object farmerId);

	public Object findNetWeightByWarehouseIdProductIdAndFarmerId(Object warehouseId, Object productId, Object farmerId);

	public List<CityWarehouse> listGradeByWarehouseIdFarmerIdProductIdAndVarietyCode(long coOperativeId, long farmerId,
			long productId, String varietyCode);

	public CityWarehouse findByProdIdAndGradeCode(long prodId, String gradeCode, String tenantId);

	public ProcurementDetail findByProcurementDetailId(Long procuDetailId);

	public List<CityWarehouse> listProductsByFarmerId(long farmerId);

	public DistributionDetail findDistributionDetailById(Long id);

	public List<Object[]> findWarehouseProductAvailableStockByAgentIdProductId(long id, String agentId);

	public void updateStockById(Long warehouseProductId, String currentQuantity);

	public List<WarehouseProductDetail> findWarehouseproductDetailByWarehouseproductIdAndReceiptNo(long id,
			String receiptNumber);

	public String findPrefernceByName(String enableApproved);

	public List<WarehousePayment> selectVendorListByBranchId(String branchId);

	public WarehouseProduct findAvailableStockByWarehouseIdSelectedProductSeason(Long warehouseId, Long productId,
			String selectedSeason);

	public ESEAccount findESEAccountByProfileId(String agentId, int agentAccount);

	public List<Object[]> listOfWarehouseByStockEntry();

	public void updateESEAccount(ESEAccount farmerAccount, String tenantId);

	public Product findProductBySubCategoryId(long subCategoryId);

	public WarehouseProduct findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(long warehouseId,
			long productId, String selectedSeason, String batchNo);

	public List<Object[]> findBatchNoListByWarehouseIdProductIdSeason(long warehouseId, long productId,
			String selectedSeason);

	public WarehouseProduct findAvailableStockByWarehouseIdProductIdBatchNo(long warehouseId, long productId,
			String batchNo);

	public WarehouseProduct findFieldStaffAvailableStockByAgentIdproductIdBatchNo(String agentId, long id,
			String batchNo);

	public List<Object[]> findBatchNoListByAgentIdProductIdSeason(String agentId, long productId,
			String selectedSeason);

	public WarehouseProduct findAvailableStockByAgentIdProductIdBatchNoSeason(String agentId, long productId,
			String selectedSeason, String batchNo);

	public WarehouseProduct findAvailableStocksBySeasonAndBatch(String servicePointId, long productId, String seasonId,
			String batch, String tenantId);

	public WarehouseProduct findFieldStaffAvailableStockBySeasonAndBatch(String agentId, long productId,
			String seasonId, String batch, String tenantId,String branchId);

	public AgroTransaction findAgentByAgentId(String agentId);

	public WarehouseProduct findAvailableStockByWarehouseIdProductIdBatchNoAndSeason(long warehouseId, long productId,
			String batchNo, String seasonCode);

	public double findAvailableStockByWarehouseIdProductIdSeasonBatchNo(long warehouseId, long productId,
			String selectedSeason, String batchNo);

	public double findAvailableStockByAgentIdProductIdSeasonBatchNo(String agentId, long productId,
			String selectedSeason, String batchNo);

	public Distribution findDistributionFarmerByRecNoAndTxnType(String receiptNumber, String txnType);

	public String findPrefernceByName(String enableApproved, String tenantId);

	public WarehouseProduct findwarehouseProductById(Long id);

	public WarehousePayment findwarehousePaymentById(Long id);

	public WarehousePaymentDetails findwarehousePaymentDetailById(long id);

	public Object[] findAvailableStockAndDamagedStockByWarehouseIdProductIdSeasonBatchNo(long warehouseId,
			long productId, String selectedSeason, String batchNo);

	public WarehouseProductDetail findwarehouseProductDetailByWarehouseProductIdReceiptNo(long id, String receiptNo);

	public WarehouseProduct findAvailableStockByAgentIdProductIdSeason(String profileId, long productId,
			String selectedSeason);

	public Object findNoOfBagByByWarehouseIdProductId(Object warehouseId, Object productId);

	public Object findNetWeightByWarehouseIdProductId(Object warehouseId, Object productId);

	public Object findNetWeightByWarehouseIdProductIdAndGradeCode(Object warehouseId, Object productId,
			Object gradeCode);

	public Object findNoOfBagByByWarehouseIdProductIdAndGradeCode(Object warehouseId, Object productId,
			Object gradeCode);

	public List<Object[]> listPMTReceiptNumberByWarehouse(long warehouseId);

	public List<ProcurementProduct> listProcurementProductByPMTReceiptNo(String receiptNo);

	public List<ProcurementGrade> listPMTProcurementGradeByVarietyId(String receiptNo);

	public PMTDetail findpmtdetailByProcurementGrade(Long gradeId);

	public List<Object[]> listProcurementProductByCultivation();

	public ProcurementGrade findProcurementGradeByVarityId(long varietyId);

	public List<Object[]> listProcurementProductByVariety(Long procurementProdId);

	public CityWarehouse findCityWarehouseIdByFarmerAndProductIdAndGradeCode(long farmerId, long productId,
			String gradeCode);

	public List<AgroTransaction> findAgroTxnByFarmerIdTXnTypesSeasonCode(String farmerId, String seasonCode,
			String txnTypes);

	public WarehouseProduct findAvailableStockByWarehouseIdSelectedProductBatchNo(long warehouseId, long productId,
			String batchNo);

	public WarehouseProduct findAvailableStockByAgentIdProductIdBatchNo(String agentId, long productId, String batchNo);

	public AgroTransaction findAgrotxnByReceiptNoAndProfType(String receiptNumber, String agentAcc);

	public List<AgroTransaction> listAgroTransactionByReceiptNoAndProfType(String receiptNumber, String farmerAcc);

	public double findAvailableStockByWarehouseIdProductIdBatchNum(long warehouseId, long productId, String batchNo);

	public double findAvailableStockByAgentIdProductIdBatchNum(String agentId, long productId, String batchNo);

	public WarehouseProduct findFieldStaffAvailableStockByBatch(String profileId, long productId, String batchNo,
			String tenantId);

	public WarehouseProduct findAvailableStocksByBatch(String servicePointId, long productId, String batch,
			String tenantId);

	public List<ProcurementProduct> listProcurementProductByPMTReceiptNoAndFarmer(String receiptNoId, Long farmerid);

	public List<Object[]> listProcurementProductFarmersByPMTReceiptNo(String receiptNoId);

	public PMTDetail findpmtdetailByProcurementGradeAndReceiptNo(Long gradeId, String receiptNoId);

	public List<ProcurementGrade> listPMTProcurementGradeByVarietyIdAndProduct(String receiptNoId,
			String selectedProduct);

	public CityWarehouse findCityWarehouseIdByAgentAndProductIdAndGradeCode(String agentId, long productId,
			String gradeCode);

	public ProductReturnDetail findProductReturnDetailById(Long id);

	public List<WarehousePaymentDetails> listWarehousePaymentDetailsByWarehousePaymentId(long id);

	public List<ProductReturnDetail> listProductReturnDetail(long parseLong);

	public List<ProductReturnDetail> listProductReturnDetail(long parseLong, int startIndex, int limit);

	public ProductReturn findProductReturnById(Long id);

	public ProductReturn findProductReturnByRecNoAndTxnType(String receiptNumber, String txnType);

	public Object[] findAvailableStockByFarmer(String farmerId, long productId, String selectedSeason, String batchNo);

	public DistributionDetail findByDistributionDetails(String farmerId, long id, String batchNo, String seasonCode);

	public ProductReturn findProductReturnFarmerByRecNoAndTxnType(String receiptNumber, String distTxnType);

	public ProductReturn findProductReturnByReceiptNoTxnType(String receiptNo, String txnType);

	public List<OfflineProductReturn> listOfflineProductReturn(String tenantId);

	public ProductReturn findProductReturnByReceiptNoTxnType(String receiptNo, String txnType, String tenantId);

	public void saveProductReturn(ProductReturn productReturn, String tenantId);

	public void updateOfflineProductReturn(OfflineProductReturn offlineProductReturn, String tenantId);

	public ESEAccount findESEAccountByProfileId(String agentId, int agentAccount, String tenantId);

	public DistributionDetail findByDistributionDetails(String farmerId, long productId, String batchNo,
			String seasonCode, String tenantId);

	public WarehouseProduct findAvailableStockByAgentIdProductIdBatchNoSeason(String agentId, long productId,
			String selectedSeason, String batchNo, String tenantId);

	public WarehouseProduct isVarietyExists(String variety);

	public List<WarehousePaymentDetails> listOfWarehousePaymentDetailsByWarehousePaymentId(long id);

	public List<Object> findWarehouseProductDetailsVarityByProduct(long productId, long coOperativeId,
			String seasonCode);

	public WarehousePaymentDetails findWarehousePaymentDetailsByProduct(long productId);

	public List<WarehouseProduct> listProductsByWarehouseAndSeason(long coOperativeId, String seasonCode);

	public WarehouseProductDetail findwarehouseProductDetailById(long id);

	public WarehouseProductDetail findwarehouseProductDetailByWarehouseProductId(long productID);

	public ProcurementGrade findProcurementGradeByNameAndVarietyId(String name, long id);

	public ProcurementVariety findProcurementVariertyByNameAndCropId(String varietyName, long procurementProductId);

	public List<ProcurementProduct> listProcurementProductBasedOnCropCat();

	public List<ProcurementProduct> listProcurementProductBasedOnInterCropCat(String cropCategory);

	public List<PMTDetail> listPMTDetailByRecNoAndProduct(Long productId, String receiptNo);

	public List<PMTDetail> listPMTDetailByProductIdAndReceiptNo(Long procProdId, String receiptNoId);

	public ProcurementVariety findProcurementVariertyByNameAndProductId(String name, long id);

	public List<Distribution> findDistributionByFarmerId(String farmerId);

	public List<DistributionDetail> findDistributionDetailByDistributionId(long id);

	public List<Object[]> findDistributionCount();

	public List<Object[]> findProcurementCount();

	public List<Object[]> findDistributionAndProcurmentCountByFarmers();

	public ProcurementVariety findProcurementVariertyByProductAndVarietyName(String product, String variety);

	public double findWarehouseAvaliableStock(String warehouseId, long productId, String selectedSeason,
			String batchNo);

	public List<OfflineSupplierProcurement> listPendingOfflineSupplierProcurementList(String tenantId);

	public SupplierProcurement findSupplierProcurementByRecNoAndOperType(String receiptNo, int operationType,
			String tenantId);

	public void saveSupplierProcurement(SupplierProcurement supplierProcurement, String tenantId);

	public void updateOfflineSupplierProcurement(OfflineSupplierProcurement offlineSupplierProcurement,
			String tenantId);

	public CityWarehouse findSupplierWarehouseByCoOperative(long coOperativeId, long productId, String quality,
			String agentId, String tenantId);

	public SupplierWarehouse findSupplierWarehouseByVillage(long villageId, long productId, String agentId,
			String quality, String tenantId);

	public SupplierWarehouse findSupplierWarehouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
			long warehouseId, String gradeCode, long productId, String tenantId);

	public void saveSupplierWarehouseDetail(SupplierWarehouseDetail supplierWarehouseDetail, String tenantId);

	public void saveOrUpdateSupplierWarehouse(SupplierWarehouse SupplierWarehouse, String tenantId);

	public SupplierProcurement findSupplierProcurementById(Long id);

	public CityWarehouse findSupplierWarehouseByCoOperative(long coOperativeId, long productId, String quality,
			String agentId);

	public SupplierProcurementDetail findSupplierProcurementDetailById(Long id);

	public List<CityWarehouse> listProcurementStockByAgentIdRevisionNo(String agentId, Long revNo);

	public CityWarehouse findSupplierWarehouseByCoOperativeProductAndGrade(long coOperativeId, long productId,
			String quality);

	public List<SupplierProcurement> listSupplierProcurement();

	public List<SupplierProcurementDetail> listSupplierProcurementProducts();

	public List<SupplierProcurementDetail> listSupplierProcurementVariety();

	public List<SupplierProcurementDetail> listSupplierProcurementDetails();

	public ProcurementTraceability findProcurementTraceabtyByRecNo(String receiptNo);

	public void saveProcurementTracebty(ProcurementTraceability procurementTraceabty, String tenantId);

	public void updateOfflineProcurementTrace(OfflineProcurementTraceability offlineProcurementTrace, String tenantId);

	public List<OfflineProcurementTraceability> listPendingOfflineProcurementTraceList(String tenantId);

	public ProcurementProduct findProcurementProductPrice(Long selectedCrop);

	public ProcurementTraceabilityStock findProcurementStockByIcs(String icsName, long id, String agentId,long warehouseId, String season);

	public ProcurementTraceability findProcurementTraceabtyByRecNoAndOperType(String receiptNo, int onLine,
			String tenantId);

	public List<Object[]> listProcurementTraceabilityDetailsByVillageAndProcurementCenter(
			ProcurementTraceabilityStockDetails procurementStockDetails, Map criteriaMap);

	public ProcurementTraceabilityStockDetails findProcurementStockDetailByIdAndFarmer(long procurementStockId , long farmerId);
	
	public ProcurementTraceabilityStockDetails findProcurementTracabiltiyDetailsStockById(Long id);


	public List<Object[]> listProductTransferReceiptNumber();

	public List<PMTDetail> listPMTDetailByProductIdAndReceiptNo(String receiptNoId);

	public List<Object[]> listWarehouseByPMTReceiptNo(String receiptNoId);
	public ProcurementTraceabilityStock findProcurementTraceabilityStockByCoOperative(long coOperativeId, long productId,String quality,String icsCode,String season);

	public List<Object[]> listTransferProCenters();

	public List<Object[]> listReceiverGinning();

	public void saveProcurementTraceability(ProcurementTraceability procurement, String tenantId);

	public ProcurementTraceabilityStock findProcurementStockByIcs(String icsName, long id, String code, long id2,
			String tenantId, String season);

	public ProcurementTraceabilityStockDetails findProcurementStockDetailByIdAndFarmer(long pid, long farmerId,
			String tenantId);

	public void save(Object obj, String tenantId);
	
	public List<Object[]> listSupplierProcurementDetailProperties(String ssDate, String eeDate);

	
	public List<Object[]> listProcurementTraceabilityStockbyAgent(String agentId, long revisionNo,String season);
	
	public ProcurementTraceabilityStock findProcurementTraceabilityStockByCoOperative(long coOperativeId, long productId,String quality,String icsCode,String tenantId,String season);
	
	public CityWarehouse findSupplierWarehouseByCoOperativeProductAndGrade(long coOperativeId, long productId,
			String quality,String tenantId);

	
	public List<PMT> findTransferStockByGinner(Integer ginner, Long warhouseId,String season);

	public List listPendingOfflineMTNR(String tenantId);

	public PMT findPMTNRByReceiptNoAndType(String receiptNo, String trnType, String tenantId);

	public void savePMTNR(PMT pmtnr, String tenantId);

	public void updateOfflinePMTNR(OfflinePMTNR offlinePMTNR, String tenantId);

	public List<OfflineMTNT> listPendingOfflineTransferTraceability(String tenantId);

	public PMTImageDetails findPMTImageDetailById(Long id);
	
	public List<Object> listPMTImageDetailsIdByPmtId(long pmtId);
	
	public List<Object[]> findpmtdetailByPmtId(long pmtId,long pmtDetailId);

	public List<Object[]> findpmtdetailByPmt(Long id, String product, String icsName);

	public PMT findDriverAndTransporterByReceiptNo(String receiptNoId,String tenantId);

	public HeapData findHeapDataByGinnerProductIcsAndHeapCode(long coOperative, long product,  String heap, String season);

	public List<HeapData> findHeapDataByGinner(Integer ginnerType, long ginnerId,String season);

	public HeapData findHeapDataByGinnerProductIcsAndHeapCode(long coOperative, long product,String heap, String tenantId,String season);

	public LedgerData findLedgerByGinnerProductIcsAndHeapCode(long ginning, long product, String ics, String heap, String tenantId);
	
	public List listPendingOfflineGinningProcess(String tenantId);
	
	public void updateOfflineGinningProcess(OfflineGinningProcess offlineProcess, String tenantId);

	public List listPendingOfflingBaleGeneration(String tenantId); 

	public void updateOfflineBaleGeneration(OfflineBaleGeneration offlineProcess, String tenantId); 

	public List listPendingOfflineSpinningTransfer(String tenantId);
	
	public void updateOfflineSpinningTransfer(OfflineSpinningTransfer offlineProcess, String tenantId);
	
	public GinningProcess findGinningProcessByDateHeapAndProduct(long ginning, String ginDate, String heap,long product, String tenantId, String season);

	public void updateGinningProcess(GinningProcess ginningProcess,String tenantId);
	
	public List<BaleGeneration> findBaleGenerationByGinningProcessId(String id);

	public List<Object[]> listPMTDetailByProductIdReceiptNoAndICSName(String receiptNoId);
	
	public List<Object[]> listOfGinningFromSpinningTransfer();
	
	public List<Object[]> listOfSpinningFromSpinningTransfer(); 

	public List<Object[]> listOfLotNoPrNoAndTypeFromSpinningTransfer();
	
	public HeapData findHeapDataByHeapCode(long ginning,String heap, String tenant,String season);
	
	public List<Object[]> listGinningProcessByHeapProductAndGinning(String heap, long product, long ginning, String startDate, String endDate,String season);
	
	public SpinningTransfer findSpinningTransferById(long id);
	
	public List<BaleGeneration> findBaleGenerationByGinningId(long ginningId,int status);
	
	public void updateBaleStatusById(String selectedBales,long id);

	public List<Object> listPMTImageDetailById(Long id, List<Integer> typeList);
	
	public List<PMTFarmerDetail> findPmtFarmerDetailByPmtId(long pmtId);
	
	public List<PMTDetail> findpmtdetailByPmtId(long pmtId);

	public DistributionBalance findDistributionBalanceByFarmerAndProduct(long farmerId, long productId, String tenantId);
	
	public void update(DistributionBalance db, String tenantId);
	
	public List<ProcurementProduct> listProcurementProductFromFarmCrops();
	
	public AgroTransaction findAgroTransactionByProcurementId(long procurementId,long agrotxnId);
	

	public PaymentLedger findPaymenyLedgerByProcurementId(String id);
	
	
	public AgroTransaction findAgroTransactionByRecAndTxnDesc(String receiptno, String txnDesc);
	
	public List<PMTImageDetails> findPMTImageDetailByDistributionId(long id);

	public void updateDistribution(Distribution distribution, String tenantId);
	
	public Double findAvailableStockByWarehouseIdAndProductId(long warehouseId, long productId);

	public WarehouseProduct findWarehouseStockByWarehouseIdAndProductId(long warehouseId, long productId,String season);

	public DistributionStock findDistributionStockById(long distributionStockId);
	
	public List<DistributionStockDetail> listDistributionStockDetailByReceiveWarehouseIdAndReceiptNo(Long receiverWarehouseId,
			String receiptNo);

	public DistributionStock findTransferDistributionStockByReceiptNumber(String receiptNo);
	
	public DistributionStockDetail findDistributionStockDetailById(Long distDetID);
	
	public List<AgroTransaction> findAgroTxnByFarmerIdTXnTypes(String farmerId);

	public List<AgroTransaction> findAgroTxnByFarmerIdTXnTypesSeasonCode(String farmerId, String seasonCode,
			String[] txnTypes, Date startDate, Date endDate);
	
	public boolean isAgentDistributionExist(String profileId);

	public List<DistributionStockDetail> findDistributionStOckDetailByDistributionId(long id);

	public WarehouseProduct findWarehouseProductBySenderWarehouseIdAndSeasonCode(long id, String season);

	public void deleteAgroTxnById(long id);

	public List<Object[]> listTreeDetails(long farmId);

	public String findConvnVartyByFarm(long farmId, String conVartyCode);

	public String findOrganicVartyByFarm(long farmId, String orgVartyCode);

	public void updateProductReturn(ProductReturn productReturn, String tenantId);

	public double findAvailableQtyByWarehouseColdStorageNameGradeBlockFloorBay(Long warehouseId, String coldStorageName, 
			String blockName, String floorName, String bayNumber, Long productId);

	public CityWarehouse findCityWarehouseByWarehouseProductBatchNoGradeFarmer(long warehouseId, long productId,String batchNo,String grade,String coldStorageName,String blockName,String floorName,String bayNum,long farmerId);

	public CityWarehouse findCityWarehouseByWarehouseProductBatchNoGrade(long warehouseId, long productId,String batchNo,String grade,String coldStorageName,String blockName,String floorName,String bayNum);
	
	public List<SupplierProcurementDetail> listSupplierProcurementDetailById(Long id);
	
	public List<Object[]> listProductByCityWarehouseAndColdStorage(Long valueOf, String selectedColdStorageName);
	
	public List<String> listBatchNoByCityWarehouseAndColdStorageAndProduct(Long warehouseId, String selectedColdStorageName,Long productId);
	
	public List<CityWarehouseDetail> listCityWareHouseDetailByWarehouseIdGradeCodeAndProductIdAndColdStorageNameAndBatchNo(
			long parseLong, long longValue, String selectedColdStorageName, String selectedBatchNo);
	
	public List<CropCalendarDetail> listCropCalendarDetailByProcurementVarietyId(long id,String currentSeasonCode);
	
	public List<BaleGeneration> findBaleGenerationByLotNo(String lotNo,String tenantId);	

	public List<Object[]> listActivityByCalendarIdAndVarietyId(long varietyId,String seasonCode);
	
	public void updateProcurementVarietyRevisionNo(long varietyId,long revisionNo);

	public List<OfflineFarmCropCalendar> listPendingOfflineFarmCropCalendarList(String tenantID);
	
	public void updateOfflineFarmCropCalendar(OfflineFarmCropCalendar offlineFarmCropCalendar, String tenantId);
	
	public void saveFarmCropCalendar(FarmCropCalendar farmCropCalendar, String tenantId);
	
	public List<Object[]> listFarmCropCalendarByFarmAndSeason(long farmId,String seasonCode);

	public ColdStorage findColdStorageByFarmerAndBatchNo(Long farmerId, String batchNo);

	public ColdStorage findColdStorageByBatchNo(String batchNo);
	
	public GinningProcess findGinningByGinningId(long ginningId);

	public String findIcsNameByIcsCode(String ics);

	public List<Object[]> findFarmerNameByFarmerId(String farmer);

	public HeapData findHeapDataByGinningHeapCodeAndProduct(String heapCode, long ginningId, long prodId,
			String currentTenantId,String season);
	public List<Object[]> listWarehouseProductByWarehouseId(String warehouseId,String season);

	public List<Object[]> listOfGinningDateByHeap(String ginningId, String heapCode,String season);
	
	public List<FarmCrops> listfarmCropsByProcurementVarietyId(Long id);

	public List<Object[]> listPMTReceiptNumberByWarehouseAndSeason(long warehouseId, String season);
	
	public OfflineGinningProcess findGinningProcessByMessageNo(String messageNo);
	
	public OfflineBaleGeneration findBaleGenerationByMessageNo(String messageNo);
	
	public OfflineSpinningTransfer findSpinningTransferByMessageNo(String messageNo);
	
	public OfflineMTNT findOfflineMTNTByMessageNo(String messageNo);
	
	public OfflinePMTNR findOfflinePMTNRByMessageNo(String messageNo);
	
	public OfflineProcurementTraceability findOfflineProcurementTraceabilityByMessageNo(String messageNo);
	
	public List<BaleGeneration> findBaleGenerationByGinningIdAndHeap(long ginning, String heap, int status);
	
	public List<String> listOfLotNoFromBaleGeneration();
	
	public List<Object[]> listFarmerByICS(String farmerId);
	
	public List<ColdStorageStockTransferDetail> listColdStorageStockTransferByLotCode(String lotCode);
	
	public LoanDistribution findLoanDistributionById(long id);

	public List<DistributionBalance> findDistributionBalanceByFarmerAndProductIdAndVendorId(Long id, String pCode,Long vendorId);

	public List<SubCategory> listSubCategory();

	public List<Product> listProductsBySubCategoryId(Long categoryId);


	public OfflineProcurement findOfflineProcurementByReceipotNo(String receipt);

	public List<LoanLedger> findFarmerLoanByFarmerIdInLoanLedger(String id, String tenantId);

	public List<Vendor> findVendorById(String vendorId);


	
	public List<Vendor> listPriorityVendor();

	public List<AgroTransaction> findAgroTxnByVendorIdTXnTypesSeasonCode(String farmerId, String seasonCode,
			String[] txnTypes, Date startDate, Date endDate);

	public LoanDistributionDetail findLoanDistributionDetailByIdd(long id);
	
	public List<LoanDistributionDetail> findLoanDistributionDetailById(long pmtId);

	public CityWarehouse findCityWarehouseByProductGradeWarehouse(long prodId, String grade, String tenantId,
			Long warehouseId);

	List<CityWarehouseDetail> listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(
			long warehouseId, long productId, String coldStorageName, String[] batchNo);
	
	public List<Object[]> listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(long warehouseId,
			long productId,String coldStorageName,String batchNo);

	public List<ProcurementProduct> listProcurementProductByBranch(String branch);
	
	public List<ProcurementProduct> listProcurementProductByRevisionNoByBranch(Long revisionNo,String branch);
	
	public List<ProcurementVariety> listProcurementProductVarietyByRevisionNoByBranch(Long revisionNo,String branch);
	
	public List<ColdStorageStockTransferDetail> listColdStorageStockTransferByLotCodeAndColdStorageCode(String lotCode,String coldStorageCode,String warehouseCode,String chamberCode,String floorCode,String bayCode);

	public List<String> listLotNoFromFarmerTraceabilityData();

	
}

