/*
 * IProductDAO.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.Date;
import java.util.List;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.profile.ViewCultivation;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransfer;
import com.sourcetrace.eses.util.profile.Product;

import com.sourcetrace.esesw.entity.profile.CropCalendar;
import com.sourcetrace.esesw.entity.profile.CropCalendarDetail;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Vendor;

/**
 * The Interface IProductDAO.
 */
public interface IProductDAO extends IESEDAO {

	/**
	 * Find product list.
	 * 
	 * @return the list< product>
	 */
	public List<Product> findProductList();

	/**
	 * Find by product code.
	 * 
	 * @param code
	 *            the code
	 * @return the product
	 */
	public Product findByProductCode(String code);

	/**
	 * Find by product name.
	 * 
	 * @param name
	 *            the name
	 * @return the product
	 */
	public Product findByProductName(String name);

	/**
	 * Find product by id.
	 * 
	 * @param id
	 *            the id
	 * @return the product
	 */
	public Product findProductById(long id);

	/**
	 * Find warehouse productby code.
	 * 
	 * @param warehousecode
	 *            the warehousecode
	 * @param productCode
	 *            the product code
	 * @return the warehouse product
	 */
	public WarehouseProduct findWarehouseProductbyCode(String warehousecode, String productCode);

	/**
	 * Find warehouse productby id.
	 * 
	 * @param warehouseId
	 *            the warehouse id
	 * @param productId
	 *            the product id
	 * @return the warehouse product
	 */
	public WarehouseProduct findWarehouseProductbyId(long warehouseId, long productId);

	/**
	 * List warehouse product.
	 * 
	 * @return the list< warehouse product>
	 */
	public List<WarehouseProduct> listWarehouseProduct();

	/**
	 * List product by sub category.
	 * 
	 * @param selectedSubCategory
	 *            the selected sub category
	 * @return the list< product>
	 */
	public List<Product> listProductBySubCategory(String selectedSubCategory);

	/**
	 * Find by warehouse product id.
	 * 
	 * @param id
	 *            the id
	 * @return the warehouse product
	 */
	public WarehouseProduct findByWarehouseProductId(long id);

	/**
	 * Find product by sub category code.
	 * 
	 * @param subCategoryId
	 *            the sub category id
	 * @return the list< product>
	 */
	public List<Product> findProductBySubCategoryCode(String subCategoryId);

	/**
	 * List product by warehouse.
	 * 
	 * @param selectedWarehouse
	 *            the selected warehouse
	 * @return the list< product>
	 */
	public List<Product> listProductByWarehouse(String selectedWarehouse);

	/**
	 * Find warehouse product mapping exist.
	 * 
	 * @param productId
	 *            the product id
	 * @return true, if successful
	 */
	public boolean findWarehouseProductMappingExist(long productId);

	/**
	 * List product name unit by warehouse.
	 * 
	 * @param warehouseCode
	 *            the warehouse code
	 * @return the list< object[]>
	 */
	public List<Object[]> listProductNameUnitByWarehouse(String warehouseCode);

	/**
	 * Find product by name and unit.
	 * 
	 * @param name
	 *            the name
	 * @param selectedUnit
	 *            the selected unit
	 * @return the product
	 */
	public Product findProductByNameAndUnit(String name, String selectedUnit);

	/**
	 * List warehouse product by city id.
	 * 
	 * @param cityId
	 *            the city id
	 * @return the list< warehouse product>
	 */
	public List<WarehouseProduct> listWarehouseProductByCityId(long cityId);

	/**
	 * List product name unit by warehouse agent id.
	 * 
	 * @param warehouseCode
	 *            the warehouse code
	 * @param agentId
	 *            the agent id
	 * @return the list< object[]>
	 */
	public List<Object[]> listProductNameUnitByWarehouseAgentId(String warehouseCode, long agentId);

	/**
	 * List products.
	 * 
	 * @return the list< object[]>
	 */
	public List<Object[]> listProducts();

	/**
	 * List warehouse product by agent.
	 * 
	 * @param agent
	 *            the agent
	 * @return the list< warehouse product>
	 */

	/**
	 * List product name unit.
	 * 
	 * @return the list< object[]>
	 */
	public List<Object[]> listProductNameUnit();

	/**
	 * List product name unit by cooperative manager profile id.
	 * 
	 * @param agentId
	 *            the agent id
	 * @return the list< object[]>
	 */
	public List<Object[]> listProductNameUnitByCooperativeManagerProfileId(String agentId);

	/**
	 * List warehouse product by agent id.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param warehouseId
	 *            the warehouse id
	 * @return the list< warehouse product>
	 */
	public List<WarehouseProduct> listWarehouseProductByAgentId(long agentId, long warehouseId);

	/**
	 * List product by revision no.
	 * 
	 * @param revisionNo
	 *            the revision no
	 * @return the list< product>
	 */
	public List<Product> listProductByRevisionNo(long revisionNo, String branchId);

	/**
	 * List warehouse product by agent id revision no.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param warehouseId
	 *            the warehouse id
	 * @param revisionNo
	 *            the revision no
	 * @return the list< warehouse product>
	 */
	public List<WarehouseProduct> listWarehouseProductByAgentIdRevisionNo(long agentId, long warehouseId,
			long revisionNo);

	/**
	 * List product name unit by warehouse agent.
	 * 
	 * @param agentId
	 *            the agent id
	 * @return the list< object[]>
	 */
	public List<Object[]> listProductNameUnitByWarehouseAgent(long agentId);

	/**
	 * List warehouse stock products.
	 * 
	 * @return the list< object[]>
	 */
	public List<Object[]> listWarehouseStockProducts();

	/**
	 * List warehouse product by agent revision no.
	 * 
	 * @param agentId
	 *            the agent id
	 * @param revisionNo
	 *            the revision no
	 * @return the list< warehouse product>
	 */
	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNo(long agentId, long revisionNo);

	public List<Product> findProductListBasedOnSubCategoryCode(String selectedSubCategoryList,String branchId);

	public Vendor findVendorIdById(long id);

	public Product findProductUnitByProductId(long id);

	public List<SubCategory> findSubCategorybyWarehouseId(String warehouseCode);

	public List<SubCategory> findSubCategorybyAgentId(long agentId);

	public WarehouseProduct findCostPriceForProduct(long id, long warehouseId);

	public Product findProductByProductCode(String code);

	public Warehouse findWarehouseByCode(String code);

	public WarehouseProduct findCostPriceForAgent(long productId, long agentId);

	public Agent findProfileByProfileid(String profileId);

	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStock(long agentId, long revisionNo);

	public List<SubCategory> findCategoryListBasedOnProductCode(long warehouseId);

	public ProcurementGrade findProcurementGradeByCode(String code);

	public ProcurementProduct findProcurementProductByCode(String code);

	public ProcurementVariety findProcurementVarietyByCode(String code);

	public CropHarvest findCropHarvestById(long id);

	public List<CropHarvestDetails> listCropHarvestDetails(long id);

	public List<CropHarvestDetails> listCropHarvestDetails(long id, int startIndex, int limit);

	public CropSupply findCropSupplyById(long id);

	public CropHarvestDetails findCropHarvestDetailsById(long id);

	public List<CropHarvestDetails> listOfCrops();

	public ViewCultivation findViewCultivationById(long id);

	public List<ProcurementProduct> listProcurmentProductsByType(String cropTypeCode);

	public List<ProcurementVariety> listProcurmentVarirtyByProcurementProductId(String cropId);

	public List<ProcurementGrade> listProcurmentGradeByVarityId(String varietyId);

	public List<ProcurementVariety> listProcurementVariety();

	public List<CropHarvestDetails> listCropHarvestDetails();

	public CropHarvestDetails findCropHarvestDetailsbyHarvestIdandtherItems(Long harvestId, int cropType, long cropId,
			Long varietyId, Long gradeId);

	public ProcurementProduct findProcurementProductById(long id);

	public List<ProcurementGrade> listProcurementGrade();

	public ProcurementVariety findProcurementVarietyById(long varietyId);

	public ProcurementGrade findProcurementGradeById(long gradeId);

	public void removeCropHarvestDetails(CropHarvestDetails cropHarvestDetails);

	public CropSupplyDetails findCropSupplyDetailsbySupplyIdandtherItems(long supplyId, int cropType, long cropId,
			long varietyId, long gradeId);

	public void removeCropSupplyDetails(long id);

	public List<CropSupplyDetails> listCropSupplyDetails(long cropSupplyId);

	public List<CropSupply> findCropSupplyByCustomerId(Long id);

	public List<CropSupply> listCropSupplyByFarmCode(String farmCode);

	public List<CropHarvest> listCropHarvestByFarmCode(String farmCode);

	public List<FarmCrops> listFarmCropsBySeasonId(Long id);

	public List<CropHarvestDetails> listCropHarvestDetailsByHarvestId(long id);

	public List<Object> listCropSaleQtyByMoth(Date sDate, Date eDate,String selectedBranch);

	public List<Object> listCropHarvestByMoth(Date sDate, Date eDate,String selectedBranch);

	public List<Object> listDistributionQtyByMoth(Date sDate, Date eDate,String selectedBranch);

	public Product findProductByCodeByTenantId(String code, String tenantId);

	public SubCategory findSubCategoryByCode(String code);

	public Product findProductByProductCodeAndSubCategoryId(String productCode, long subCategoryId);

	public Product findProductByProductNameAndSubCategoryId(String productName, long subCategoryId);

	public List<SubCategory> lisCategoryByWarehouseIdSeasonCode(String warehouseId, String seasonCode);

	public List<Object[]> findFarmerCountCultivationAreaEsiyield(String farmerCode, String cropId, String season,
			String samithi, String icsCode);

	public List<Object[]> listOfHarvestedCrops(String branchId);

	public List<Object[]> listOfProductByStock();

	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStockAndSeasonCode(long id, Long valueOf,
			String seasonCode);

	public Product findProductUnitByProductCode(String code);

	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStockAndSeasonCodeByBatch(long id, Long valueOf,
			String seasonCode);

	List<WarehouseProduct> listWarehouseProductBySeasonCodeByBatch(long id, String seasonCode);

	public HarvestSeason findHarvestSeasonNamebySeasonCode(String code);

	public List<Object[]> listFarmProducts();

	public List<SubCategory> findSubCategorybyAgentIdAndSeason(Long agentId, String selectedSeason);

	public List<Object> listcowMilkByMonth(Date sDate, Date eDate);

	public ProcurementGrade findGradeUnitByGradeId(long id);

	public Object[] findByProdNameAndId(String selectedProductName);

	public Product findProductByProductIdAndSubCategoryId(long productId, long subCategoryId);

	public List<SubCategory> lisCategoryByWarehouseId(String warehouseCode);

	public ProcurementProduct findUnitByCropId(long id);

	public List<Object[]> listProcurementGradeInfo();

	public List<Object[]> listFarmProducts(String branch);

	public List<Object[]> findProcurementCummulativeData();

	public List<Object[]> findProcurementDataByFilter(Long selectedProduct, String selectedDate);

	public List<Object[]> listOfProducts();

	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStockByBatch(long agentId, Long revisionNo);

	public List<WarehouseProduct> listWarehouseProductByBatch(long agentId);

	public List<Object> listProcurementAmtByMoth(Date sDate, Date eDate,String selectedBranch);

	public List<Object> listEnrollmentByMoth(Date sDate, Date eDate);

	public String findProcurementProductUnitByProductCode(String selectedProduct);

	public List<ProcurementProduct> listProcurmentProductsByFarmer(String farmerId);

	List<Object[]> listWarehouseProductSeason();

	public List<Product> findProductListByBranch(String branchId);

	public List<ProcurementVariety> listProcurmentVarietyByProcurementProductIdInProcurement(String code);

	public List<Object> listProcurementQtyByMoth(Date sDate, Date eDate);

	public List<Object> listProcurementGroupByMoth(Integer year, Integer month);
	
	public WarehouseProduct findWarehouseProductByAgentAndProductAndSeason(Long agent_id,Long productId,String seasonCode);
	
	public WarehouseProduct findWarehouseProductbyIdAndSeasonCode(long warehouseId, long productID, String seasonCode);
	
	public List<AgroTransaction> listAgroTransactionByDistributionId(Long id);

	public List<Object[]> listOfProcurementProductByStock();

	public Object[] findTotalQtyAndAmt(String farmerName, String product, String warehouse,String branchIdParma,String ics,String village,String city,String fCooperative,String season, Date startDate, Date endDate);
	
	public List<Object[]> listProductionCenterByTraceabilityStock();
	
	public List<Object[]> listOfProcurementProductByTraceabilityStock();
	
	public Object[] findGradeNameAndVarietyByGradeCode(String grade);
	
	public List<Object[]> listOfICSFromTraceabilityStock();

	public List<Object[]> listFarmerByTraceabilityStock();
	
	public Object[] findTotalQtyAndAmtFromProcurementStock(String farmerName, String product, String warehouse,
			String branchIdParma,String village,String city,String fpo,String ics,String selectedSeason);
	
	public List<Object[]> listOfFarmCrops(String branchId);

	public Warehouse findWarehouseByCode(String warehouseId, String tenantId);
	
	public List<Object[]> listOfVillageFromTraceability();
	
	public List<Object[]> listOfcityFromTraceability();

	public List<Object[]> listProcurementVarietyByCode(String code);

	public List<Object[]> listOfICSFromHeapStock();

	public List<Object[]> listOfProcurementProductFromHeap();

	public List<Object> listOfHeapName();

	public List<Object[]> findSupplierProcurementDataByFilter(long l, String dateRange);

	public List<Object[]> findSupplierProcurementCummulativeData();
	
	public List<Object> listSupplierProcurementQtyByMoth(Date firstDateOfMonth, Date date);

	public List<Object> listSupplierProcurementAmtByMoth(Date firstDateOfMonth, Date date);

	public List<Object[]> listOfFarmCrops();
	
	public List<Object[]> listOfGiningFromBale();
	
	public List<Object> listOfHeapFromBale();
	
	public ProcurementProduct findProcurementProductByCode(String productCode, String tenantId);

	public List<Object> listSupplierProcurementGroupByMoth(Integer year, Integer month);

	public void removeProcurment(String id, String deleteStatus);
	
	public List<Object[]> listCategoriesByWarehouseId(long warehouseId,String season);
	
	public Double findAvailableStockByWarehouseIdAndProduct(String senderWarehouse, String product,String season);
	
	public List<Object> listOfDistributionStockReceiptNo(long warehosueId);
	
	public List<Product> findProductByWarehouseIdAndSubCategoryCode(long warehouseId,String category,String season);
	
	public List<SubCategory> lisCategoryByWarehouseIdSeasonCodeAgent(long agentId, String seasonCode);

	public List<Product> listofProductBySubCategoryAndSelectedAgent(String selectedCategory, long selectedAgent,String seasonCode);

	public List<AgroTransaction> listAgroTransactionByDistributionStockId(long id,String receiptNo,String txnType,String seasonCode);
	
	public List<Object[]> listOfDistributionProductsByType(String pType);
	
	public Product findProuductByProudctNameSubCategoryIdAndManufacture(String selectedProductName, Long category,
			String manufacture);
	
	public CropCalendar findCropCalendarById(long id);

	public CropCalendarDetail findCropCalendarDetailById(Long id);
	
	public ProcurementVariety findProcurementVarietyByCode(String code,String tenantId);

	public List<ColdStorageStockTransfer> findColdStorageStockTransferByCustomerId(Long id);
	
public List<Object[]> findProcurementTraceabilityDataByFilter(Long selectedProduct, String selectedDate);
	
	public List<Object[]> findProcurementTraceabilityCummulativeData();
	
	public List<Object[]> listProcurmentGradeByVarityCode(String code);
}
