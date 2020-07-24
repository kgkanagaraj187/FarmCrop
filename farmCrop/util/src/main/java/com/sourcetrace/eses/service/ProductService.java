/*
 * ProductService.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.profile.ViewCultivation;
import com.sourcetrace.eses.dao.FarmerDAO;
import com.sourcetrace.eses.dao.IAccountDAO;
import com.sourcetrace.eses.dao.IProductDAO;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransfer;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.VillageWarehouse;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.profile.Product;
import com.sourcetrace.esesw.entity.profile.ESEAccount;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.entity.profile.WarehousePaymentDetails;

/**
 * The Class ProductService.
 */
@Service
public class ProductService implements IProductService {
	@Autowired
	private IProductDAO productDAO;
	@Autowired
	private IAccountDAO accountDAO;

	/**
	 * Sets the product dao.
	 * 
	 * @param productDAO
	 *            the new product dao
	 */
	public void setProductDAO(IProductDAO productDAO) {

		this.productDAO = productDAO;
	}

	/**
	 * Gets the product dao.
	 * 
	 * @return the product dao
	 */
	public IProductDAO getProductDAO() {

		return productDAO;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IProductService#
	 * findProductList ()
	 */
	public List<Product> findProductList() {

		return productDAO.findProductList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductService#addProduct
	 * (com.sourcetrace.eses.order.entity.profile.Product)
	 */
	public void addProduct(Product product) {

		product.setRevisionNo(DateUtil.getRevisionNumber());
		productDAO.save(product);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IProductService#
	 * findProductById (long)
	 */
	public Product findProductById(long id) {

		return productDAO.findProductById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductService#editProduct
	 * (com.sourcetrace.eses.order.entity.profile.Product)
	 */
	public void editProduct(Product existing) {

		existing.setRevisionNo(DateUtil.getRevisionNumber());
		productDAO.update(existing);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductService#removeProduct
	 * (com.sourcetrace.eses.order.entity.profile.Product)
	 */
	public void removeProduct(Product product) {

		productDAO.delete(product);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IProductService#
	 * findProductByCode (java.lang.String)
	 */
	public Product findProductByCode(String code) {

		return this.productDAO.findByProductCode(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * addWarehouseProduct(com.ese.entity.profile.WarehouseProduct)
	 */
	public void addWarehouseProduct(WarehouseProduct warehouseProduct) {

		productDAO.save(warehouseProduct);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * editWarehouseProduct(com.ese.entity.profile.WarehouseProduct)
	 */
	public void editWarehouseProduct(WarehouseProduct warehouseProduct) {

		productDAO.update(warehouseProduct);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * findWarehouseProductbyCode(java.lang.String, java.lang.String)
	 */
	public WarehouseProduct findWarehouseProductbyCode(String warehousecode, String productCode) {

		return productDAO.findWarehouseProductbyCode(warehousecode, productCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * findWarehouseProductbyId(long, long)
	 */
	public WarehouseProduct findWarehouseProductbyId(long warehouseId, long productId) {

		return productDAO.findWarehouseProductbyId(warehouseId, productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listWarehouseProduct()
	 */
	public List<WarehouseProduct> listWarehouseProduct() {

		return productDAO.listWarehouseProduct();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * removeWarehouseProduct(com.ese.entity.profile.WarehouseProduct)
	 */
	public void removeWarehouseProduct(WarehouseProduct warehouseProduct) {

		productDAO.delete(warehouseProduct);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listProductBySubCategory(java.lang.String)
	 */
	public List<Product> listProductBySubCategory(String selectedSubCategory) {

		return productDAO.listProductBySubCategory(selectedSubCategory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.service.service.IProductService#
	 * findByProductName (java.lang.String)
	 */
	public Product findByProductName(String name) {

		return productDAO.findByProductName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * findByWarehouseProductId(long)
	 */
	public WarehouseProduct findByWarehouseProductId(long id) {

		return productDAO.findByWarehouseProductId(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * updateVillageWarehouse
	 * (com.sourcetrace.eses.order.entity.txn.VillageWarehouse)
	 */
	public void updateVillageWarehouse(VillageWarehouse villageWarehouse) {

		productDAO.update(villageWarehouse);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * addVillageWarehouse
	 * (com.sourcetrace.eses.order.entity.txn.VillageWarehouse)
	 */
	public void addVillageWarehouse(VillageWarehouse villageWarehouse) {

		productDAO.save(villageWarehouse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * updateWarehouseProduct
	 * (com.sourcetrace.eses.order.entity.txn.Distribution,
	 * com.ese.entity.profile.Warehouse)
	 */
	public void updateWarehouseProduct(Distribution distribution, Warehouse warehouse) {

		/** UPDATE DISTRIBUTION OBJECT **/
		productDAO.update(distribution);
		/** UPDATE ACCOUNT OBJECT **/
		if (!ObjectUtil.isEmpty(distribution.getAgroTransaction())) {
			ESEAccount account = accountDAO.findAccountByProfileIdAndProfileType(
					distribution.getAgroTransaction().getFarmerId(), ESEAccount.FARMER_ACCOUNT);
			if (!ObjectUtil.isEmpty(account)) {
				account.setBalance(account.getBalance() - distribution.getAgroTransaction().getTxnAmount());
				accountDAO.update(account);
			}
		}
		/** UPDATE WAREHOUSE PRODUCT OBJECT **/
		for (DistributionDetail distributionDetail : distribution.getDistributionDetails()) {
			WarehouseProduct warehouseProduct = productDAO.findWarehouseProductbyId(warehouse.getId(),
					distributionDetail.getProduct().getId());
			warehouseProduct
					.setStock(warehouseProduct.getStock() + new Double(distributionDetail.getQuantity()).longValue());
			productDAO.update(warehouseProduct);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * editWarehouseProductList(java.util.List)
	 */
	public void editWarehouseProductList(List<WarehouseProduct> productList) {

		for (WarehouseProduct product : productList) {
			editWarehouseProduct(product);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * updateVillageWarehouseList(java.util.List)
	 */
	public void updateVillageWarehouseList(List<VillageWarehouse> villageWarehouseList) {

		for (VillageWarehouse villageWarehouse : villageWarehouseList) {
			updateVillageWarehouse(villageWarehouse);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * findProductBySubCategoryCode(java.lang.String)
	 */
	public List<Product> findProductBySubCategoryCode(String subCategoryId) {

		return productDAO.findProductBySubCategoryCode(subCategoryId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listProductByWarehouse(java.lang.String)
	 */
	public List<Product> listProductByWarehouse(String selectedWarehouse) {

		return productDAO.listProductByWarehouse(selectedWarehouse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * findWarehouseProductMappingExist(long)
	 */
	public boolean findWarehouseProductMappingExist(long productId) {

		return productDAO.findWarehouseProductMappingExist(productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listProductNameUnitByWarehouse (java.lang.String)
	 */
	public List<Object[]> listProductNameUnitByWarehouse(String warehouseCode) {

		return productDAO.listProductNameUnitByWarehouse(warehouseCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * findProductByNameAndUnit(java. lang.String, java.lang.String)
	 */
	public Product findProductByNameAndUnit(String name, String selectedUnit) {

		return productDAO.findProductByNameAndUnit(name, selectedUnit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listWarehouseProductByCityId(long)
	 */
	public List<WarehouseProduct> listWarehouseProductByCityId(long cityId) {

		return productDAO.listWarehouseProductByCityId(cityId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listProductNameUnitByWarehouseAgentId (java.lang.String, long)
	 */
	public List<Object[]> listProductNameUnitByWarehouseAgentId(String warehouseCode, long agentId) {

		return productDAO.listProductNameUnitByWarehouseAgentId(warehouseCode, agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.service.service.IProductService#listProducts()
	 */
	public List<Object[]> listProducts() {

		return productDAO.listProducts();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listWarehouseProductByAgent(com .ese.entity.profile.Agent)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listProductNameUnit()
	 */
	public List<Object[]> listProductNameUnit() {

		return productDAO.listProductNameUnit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listProductNameUnitByCooperativeManagerProfileId(java.lang.String)
	 */
	public List<Object[]> listProductNameUnitByCooperativeManagerProfileId(String agentId) {

		return productDAO.listProductNameUnitByCooperativeManagerProfileId(agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listWarehouseProductByAgentId( long)
	 */
	public List<WarehouseProduct> listWarehouseProductByAgentId(long agentId, long warehouseId) {

		return productDAO.listWarehouseProductByAgentId(agentId, warehouseId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listProductByRevisionNo(long)
	 */
	public List<Product> listProductByRevisionNo(long revisionNo, String branchId) {

		return productDAO.listProductByRevisionNo(revisionNo, branchId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listWarehouseProductByAgentIdRevisionNo(long, long, long)
	 */
	public List<WarehouseProduct> listWarehouseProductByAgentIdRevisionNo(long agentId, long warehouseId,
			long revisionNo) {

		return productDAO.listWarehouseProductByAgentIdRevisionNo(agentId, warehouseId, revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listProductNameUnitByWarehouseAgent (long)
	 */
	public List<Object[]> listProductNameUnitByWarehouseAgent(long agentId) {

		// TODO Auto-generated method stub
		return productDAO.listProductNameUnitByWarehouseAgent(agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listWarehouseStockProducts()
	 */
	public List<Object[]> listWarehouseStockProducts() {

		return productDAO.listWarehouseStockProducts();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.service.service.IProductService#
	 * listWarehouseProductByAgentRevisionNo (long, long)
	 */
	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNo(long agentId, long revisionNo) {

		// TODO Auto-generated method stub
		return productDAO.listWarehouseProductByAgentRevisionNo(agentId, revisionNo);
	}

	public List<Product> findProductListBasedOnSubCategoryCode(String selectedSubCategoryList,String branchId) {

		// TODO Auto-generated method stub
		return productDAO.findProductListBasedOnSubCategoryCode(selectedSubCategoryList,branchId);
	}

	public Vendor findVendorIdById(long id) {
		System.out.println("id" + id);

		return productDAO.findVendorIdById(id);
	}

	public Product findProductUnitByProductId(long id) {

		return productDAO.findProductUnitByProductId(id);
	}

	public List<SubCategory> findSubCategorybyWarehouseId(String warehouseCode) {

		return productDAO.findSubCategorybyWarehouseId(warehouseCode);
	}

	public List<SubCategory> findSubCategorybyAgentId(long agentId) {

		return productDAO.findSubCategorybyAgentId(agentId);
	}

	public WarehouseProduct findCostPriceForProduct(long id, long warehouseId) {
		return productDAO.findCostPriceForProduct(id, warehouseId);
	}

	public Product findProductByProductCode(String code) {
		return productDAO.findProductByProductCode(code);
	}

	public Warehouse findWarehouseByCode(String code) {
		return productDAO.findWarehouseByCode(code);
	}

	public WarehouseProduct findCostPriceForAgent(long productId, long AgentId) {
		// TODO Auto-generated method stub
		return productDAO.findCostPriceForAgent(productId, AgentId);
	}

	public Agent findProfileByProfileid(String ProfileId) {
		// TODO Auto-generated method stub
		return productDAO.findProfileByProfileid(ProfileId);
	}

	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStock(long id, Long valueOf) {
		return productDAO.listWarehouseProductByAgentRevisionNoStock(id, valueOf);
	}

	public List<SubCategory> findCategoryListBasedOnProductCode(long warehouseId) {
		return productDAO.findCategoryListBasedOnProductCode(warehouseId);
	}

	public ProcurementGrade findProcurementGradeByCode(String code) {
		// TODO Auto-generated method stub
		return productDAO.findProcurementGradeByCode(code);
	}

	public ProcurementProduct findProcurementProductByCode(String code) {
		// TODO Auto-generated method stub
		return productDAO.findProcurementProductByCode(code);
	}

	public ProcurementVariety findProcurementVarietyByCode(String code) {
		// TODO Auto-generated method stub
		return productDAO.findProcurementVarietyByCode(code);
	}

	public void savecropHarvestAndCropHarvestDetails(CropHarvest cropHarvest) {
		// TODO Auto-generated method stub
		productDAO.save(cropHarvest);
	}

	public CropHarvest findCropHarvestById(long id) {
		// TODO Auto-generated method stub
		return productDAO.findCropHarvestById(id);
	}

	public List<CropHarvestDetails> listCropHarvestDetails(long id) {
		// TODO Auto-generated method stub
		return productDAO.listCropHarvestDetails(id);
	}

	public List<CropHarvestDetails> listCropHarvestDetails(long id, int startIndex, int limit) {
		// TODO Auto-generated method stub
		return productDAO.listCropHarvestDetails(id, startIndex, limit);
	}

	public CropSupply findCropSupplyById(long id) {
		// TODO Auto-generated method stub
		return productDAO.findCropSupplyById(id);
	}

	public ViewCultivation findViewCultivationById(long id) {
		// TODO Auto-generated method stub
		return productDAO.findViewCultivationById(id);
	}

	public CropHarvestDetails findCropHarvestDetailsById(long id) {
		// TODO Auto-generated method stub
		return productDAO.findCropHarvestDetailsById(id);
	}

	public List<CropHarvestDetails> listOfCrops() {
		// TODO Auto-generated method stub
		return productDAO.listOfCrops();
	}

	@Override
	public void updateCropHarvest(CropHarvest temp) {
		// TODO Auto-generated method stub
		productDAO.update(temp);
	}

	@Override
	public List<ProcurementProduct> listProcurmentProductsByType(String cropTypeCode) {
		// TODO Auto-generated method stub
		return productDAO.listProcurmentProductsByType(cropTypeCode);
	}

	@Override
	public List<ProcurementVariety> listProcurmentVarirtyByProcurementProductId(String cropId) {
		// TODO Auto-generated method stub
		return productDAO.listProcurmentVarirtyByProcurementProductId(cropId);
	}

	@Override
	public List<ProcurementGrade> listProcurmentGradeByVarityId(String varietyId) {
		// TODO Auto-generated method stub
		return productDAO.listProcurmentGradeByVarityId(varietyId);
	}

	@Override
	public List<ProcurementVariety> listProcurementVariety() {
		// TODO Auto-generated method stub
		return productDAO.listProcurementVariety();
	}

	@Override
	public List<CropHarvestDetails> listCropHarvestDetails() {
		// TODO Auto-generated method stub
		return productDAO.listCropHarvestDetails();
	}

	@Override
	public CropHarvestDetails findCropHarvestDetailsbyHarvestIdandtherItems(Long harvestId, int cropType, long cropId,
			Long varietyId, Long gradeId) {
		// TODO Auto-generated method stub
		return productDAO.findCropHarvestDetailsbyHarvestIdandtherItems(harvestId, cropType, cropId, varietyId,
				gradeId);
	}

	@Override
	public void updateCropHarvestDetails(CropHarvestDetails cropHarvestDetails) {
		// TODO Auto-generated method stub
		productDAO.update(cropHarvestDetails);
	}

	@Override
	public ProcurementProduct findProcurementProductById(long id) {
		// TODO Auto-generated method stub
		return productDAO.findProcurementProductById(id);
	}

	@Override
	public List<ProcurementGrade> listProcurementGrade() {
		// TODO Auto-generated method stub
		return productDAO.listProcurementGrade();
	}

	@Override
	public ProcurementVariety findProcurementVarietyById(long varietyId) {
		// TODO Auto-generated method stub
		return productDAO.findProcurementVarietyById(varietyId);
	}

	@Override
	public ProcurementGrade findProcurementGradeById(long gradeId) {
		// TODO Auto-generated method stub
		return productDAO.findProcurementGradeById(gradeId);
	}

	@Override
	public void saveCropHarvestDetails(CropHarvestDetails newCropHarvestDetails) {
		// TODO Auto-generated method stub
		productDAO.save(newCropHarvestDetails);
	}

	@Override
	public void removeCropHarvestDetails(CropHarvestDetails cropHarvestDetails) {
		// TODO Auto-generated method stub
		productDAO.removeCropHarvestDetails(cropHarvestDetails);
	}

	@Override
	public void updateCropSupply(CropSupply temp) {
		// TODO Auto-generated method stub
		productDAO.update(temp);
	}

	@Override
	public void updateCropSaleDetails(CropSupplyDetails cropSaleDetails) {
		// TODO Auto-generated method stub
		productDAO.update(cropSaleDetails);
	}

	@Override
	public void saveCropSupplyDetails(CropSupplyDetails newCropSaleDetails) {
		// TODO Auto-generated method stub

		productDAO.save(newCropSaleDetails);

	}

	@Override
	public CropSupplyDetails findCropSupplyDetailsbySupplyIdandtherItems(long supplyId, int cropType, long cropId,
			long varietyId, long gradeId) {
		// TODO Auto-generated method stub
		return productDAO.findCropSupplyDetailsbySupplyIdandtherItems(supplyId, cropType, cropId, varietyId, gradeId);
	}

	@Override
	public void removeCropSupplyDetails(long id) {
		// TODO Auto-generated method stub
		productDAO.removeCropSupplyDetails(id);
	}

	@Override
	public List<CropSupplyDetails> listCropSupplyDetails(long cropSupplyId) {
		// TODO Auto-generated method stub
		return productDAO.listCropSupplyDetails(cropSupplyId);
	}

	@Override
	public void removeCropHarvest(CropHarvest cropHarvest) {
		// TODO Auto-generated method stub
		productDAO.delete(cropHarvest);
	}

	public List<CropSupply> findCropSupplyByCustomerId(Long id) {
		return productDAO.findCropSupplyByCustomerId(id);
	}

	public List<CropSupply> listCropSupplyByFarmCode(String farmCode) {
		return productDAO.listCropSupplyByFarmCode(farmCode);
	}

	public List<CropHarvest> listCropHarvestByFarmCode(String farmCode) {
		return productDAO.listCropHarvestByFarmCode(farmCode);
	}

	public List<FarmCrops> listFarmCropsBySeasonId(Long id) {
		return productDAO.listFarmCropsBySeasonId(id);
	}

	@Override
	public List<CropHarvestDetails> listCropHarvestDetailsByHarvestId(long id) {
		return productDAO.listCropHarvestDetailsByHarvestId(id);
	}

	@Override
	public List<Object> listCropSaleQtyByMoth(Date sDate, Date eDate,String selectedBranch) {
		return productDAO.listCropSaleQtyByMoth(sDate, eDate,selectedBranch);
	}

	@Override
	public List<Object> listCropHarvestByMoth(Date sDate, Date eDate,String selectedBranch) {
		return productDAO.listCropHarvestByMoth(sDate, eDate,selectedBranch);
	}

	@Override
	public List<Object> listDistributionQtyByMoth(Date sDate, Date eDate,String selectedBranch) {
		return productDAO.listDistributionQtyByMoth(sDate, eDate,selectedBranch);
	}

	@Override
	public Product findProductByCodeByTenantId(String code, String tenantId) {

		return productDAO.findProductByCodeByTenantId(code, tenantId);
	}

	@Override
	public SubCategory findSubCategoryByCode(String code) {

		// TODO Auto-generated method stub
		return productDAO.findSubCategoryByCode(code);
	}

	@Override
	public Product findProductByProductCodeAndSubCategoryId(String productCode, long subCategoryId) {

		// TODO Auto-generated method stub
		return productDAO.findProductByProductCodeAndSubCategoryId(productCode, subCategoryId);
	}

	public Product findProductByProductNameAndSubCategoryId(String productName, long subCategoryId) {

		// TODO Auto-generated method stub
		return productDAO.findProductByProductNameAndSubCategoryId(productName, subCategoryId);
	}

	@Override
	public List<SubCategory> lisCategoryByWarehouseIdSeasonCode(String warehouseId, String seasonCode) {
		// TODO Auto-generated method stub
		return productDAO.lisCategoryByWarehouseIdSeasonCode(warehouseId, seasonCode);
	}

	@Override
	public List<Object[]> findFarmerCountCultivationAreaEsiyield(String farmerCode, String cropId, String season,
			String samithi, String icsCode) {
		// TODO Auto-generated method stub
		return productDAO.findFarmerCountCultivationAreaEsiyield(farmerCode, cropId, season, samithi, icsCode);
	}

	@Override
	public List<Object[]> listOfHarvestedCrops(String branchId) {
		// TODO Auto-generated method stub
		return productDAO.listOfHarvestedCrops(branchId);
	}

	@Override
	public List<Object[]> listOfProductByStock() {
		// TODO Auto-generated method stub
		return productDAO.listOfProductByStock();
	}

	@Override
	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStockAndSeasonCode(long id, Long valueOf,
			String seasonCode) {
		// TODO Auto-generated method stub
		return productDAO.listWarehouseProductByAgentRevisionNoStockAndSeasonCode(id, valueOf, seasonCode);
	}

	@Override
	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStockAndSeasonCodeByBatch(long id, Long valueOf,
			String seasonCode) {

		return productDAO.listWarehouseProductByAgentRevisionNoStockAndSeasonCodeByBatch(id, valueOf, seasonCode);
	}

	@Override
	public Product findProductUnitByProductCode(String code) {
		// TODO Auto-generated method stub
		return productDAO.findProductUnitByProductCode(code);
	}

	@Override
	public List<WarehouseProduct> listWarehouseProductBySeasonCodeByBatch(long id, String seasonCode) {
		return productDAO.listWarehouseProductBySeasonCodeByBatch(id, seasonCode);
	}

	@Override
	public HarvestSeason findHarvestSeasonNamebySeasonCode(String code) {
		return productDAO.findHarvestSeasonNamebySeasonCode(code);
	}

	@Override
	public List<Object[]> listFarmProducts() {

		return productDAO.listFarmProducts();
	}

	@Override
	public void updateWarehousePayement(WarehousePayment existingWarehousePayement) {
		// TODO Auto-generated method stub
		productDAO.update(existingWarehousePayement);
	}

	@Override
	public void updatePayementDetails(WarehousePaymentDetails existingWarehousePaymentDetails) {
		// TODO Auto-generated method stub
		productDAO.update(existingWarehousePaymentDetails);
	}

	public List<SubCategory> findSubCategorybyAgentIdAndSeason(Long agentId, String selectedSeason) {
		// TODO Auto-generated method stub
		return productDAO.findSubCategorybyAgentIdAndSeason(agentId, selectedSeason);
	}

	@Override
	public List<Object> listcowMilkByMonth(Date sDate, Date eDate) {
		// TODO Auto-generated method stub

		return productDAO.listcowMilkByMonth(sDate, eDate);
	}

	@Override
	public ProcurementGrade findGradeUnitByGradeId(long id) {
		// TODO Auto-generated method stub
		return productDAO.findGradeUnitByGradeId(id);
	}

	@Override
	public Object[] findByProdNameAndId(String selectedProductName) {
		// TODO Auto-generated method stub
		return productDAO.findByProdNameAndId(selectedProductName);
	}

	@Override
	public Product findProductByProductIdAndSubCategoryId(long productId, long subCategoryId) {

		return productDAO.findProductByProductIdAndSubCategoryId(productId, subCategoryId);
	}

	@Override
	public List<SubCategory> lisCategoryByWarehouseId(String warehouseCode) {

		return productDAO.lisCategoryByWarehouseId(warehouseCode);
	}

	@Override
	public ProcurementProduct findUnitByCropId(long id) {
		return productDAO.findUnitByCropId(id);
	}

	public List<Object[]> listProcurementGradeInfo() {
		return productDAO.listProcurementGradeInfo();
	}

	@Override
	public List<Object[]> listFarmProducts(String branch) {
		return productDAO.listFarmProducts(branch);
	}

	@Override
	public List<Object[]> findProcurementCummulativeData() {
		return productDAO.findProcurementCummulativeData();
	}

	@Override
	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStockByBatch(long agentId, Long revisionNo) {

		return productDAO.listWarehouseProductByAgentRevisionNoStockByBatch(agentId, revisionNo);
	}

	@Override
	public List<WarehouseProduct> listWarehouseProductByBatch(long agentId) {

		return productDAO.listWarehouseProductByBatch(agentId);
	}

	@Override
	public List<Object> listProcurementAmtByMoth(Date sDate, Date eDate,String selectedBranch) {
		return productDAO.listProcurementAmtByMoth(sDate, eDate,selectedBranch);
	}

	@Override
	public List<Object> listEnrollmentByMoth(Date sDate, Date eDate) {
		return productDAO.listEnrollmentByMoth(sDate, eDate);
	}

	@Override
	public List<ProcurementProduct> listProcurmentProductsByFarmer(String farmerId) {
		// TODO Auto-generated method stub
		return productDAO.listProcurmentProductsByFarmer(farmerId);
	}

	@Override
	public String findProcurementProductUnitByProductCode(String selectedProduct) {
		return productDAO.findProcurementProductUnitByProductCode(selectedProduct);
	}

	@Override
	public List<Object[]> listWarehouseProductSeason() {
		return productDAO.listWarehouseProductSeason();
	}

	@Override
	public List<Product> findProductListByBranch(String branchId) {
		// TODO Auto-generated method stub
		return productDAO.findProductListByBranch(branchId);
	}

	@Override
	public List<ProcurementVariety> listProcurmentVarietyByProcurementProductIdInProcurement(String code) {
		return productDAO.listProcurmentVarietyByProcurementProductIdInProcurement(code);
	}

	@Override
	public List<Object[]> findProcurementDataByFilter(Long selectedProduct, String selectedDate) {
		return productDAO.findProcurementDataByFilter(selectedProduct, selectedDate);
	}

	@Override
	public List<Object> listProcurementQtyByMoth(Date sDate, Date eDate) {
		// TODO Auto-generated method stub
		return productDAO.listProcurementQtyByMoth(sDate, eDate);
	}

	@Override
	public List<Object> listProcurementGroupByMoth(Integer year, Integer month) {
		// TODO Auto-generated method stub
		return productDAO.listProcurementGroupByMoth(year, month);
	}

	@Override
	public WarehouseProduct findWarehouseProductByAgentAndProductAndSeason(Long agent_id, Long productId,
			String seasonCode) {
		return productDAO.findWarehouseProductByAgentAndProductAndSeason(agent_id, productId, seasonCode);
	}

	@Override
	public WarehouseProduct findWarehouseProductbyIdAndSeasonCode(long warehouseId, long productID, String seasonCode) {
		return productDAO.findWarehouseProductbyIdAndSeasonCode(warehouseId, productID, seasonCode);
	}

	@Override
	public List<AgroTransaction> listAgroTransactionByDistributionId(Long id) {
		return productDAO.listAgroTransactionByDistributionId(id);
	}

	@Override
	public List<Object[]> listOfProcurementProductByStock() {
		// TODO Auto-generated method stub
		return productDAO.listOfProcurementProductByStock();
	}

	@Override
	public Object[] findTotalQtyAndAmt(String farmerName, String product, String warehouse,String branchIdParma,String ics,String village,String city,String fCooperative,String season, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return productDAO.findTotalQtyAndAmt(farmerName,product,warehouse,branchIdParma,ics,village,city,fCooperative,season, startDate,  endDate);
	}
	@Override
	public List<Object[]> listProductionCenterByTraceabilityStock() {
		return productDAO.listProductionCenterByTraceabilityStock();
	}

	@Override
	public List<Object[]> listOfProcurementProductByTraceabilityStock() {
		return productDAO.listOfProcurementProductByTraceabilityStock();
	}

	@Override
	public Object[] findGradeNameAndVarietyByGradeCode(String grade) {
		return productDAO.findGradeNameAndVarietyByGradeCode(grade);
	}

	@Override
	public List<Object[]> listOfICSFromTraceabilityStock() {
		return productDAO.listOfICSFromTraceabilityStock();
	}

	@Override
	public List<Object[]> listFarmerByTraceabilityStock() {
		return productDAO.listFarmerByTraceabilityStock();
	}

	@Override
	public Object[] findTotalQtyAndAmtFromProcurementStock(String farmerName, String product, String warehouse,
			String branchIdParma,String village,String city,String fpo,String ics,String selectedSeason) {
		return productDAO.findTotalQtyAndAmtFromProcurementStock(farmerName, product, warehouse, branchIdParma,village,city,fpo,ics,selectedSeason);
	}

	@Override
	public List<Object[]> listOfFarmCrops(String branchId) {
		// TODO Auto-generated method stub
		return productDAO.listOfFarmCrops(branchId);
	}

	@Override
	public Warehouse findWarehouseByCode(String warehouseId, String tenantId) {
		return productDAO.findWarehouseByCode(warehouseId,tenantId);
	}
	
	public List<Object[]> listOfVillageFromTraceability(){
		return productDAO.listOfVillageFromTraceability();
	}
	
	public List<Object[]> listOfcityFromTraceability(){
		return productDAO.listOfcityFromTraceability();
	}

	@Override
	public List<Object[]> listProcurementVarietyByCode(String code) {
		// TODO Auto-generated method stub
		//return productDAO.listOfcityFromTraceability();
		return productDAO.listProcurementVarietyByCode(code);
	}

	@Override
	public List<Object[]> listOfICSFromHeapStock() {
		// TODO Auto-generated method stub
		return productDAO.listOfICSFromHeapStock();
	}

	@Override
	public List<Object[]> listOfProcurementProductFromHeap() {
		// TODO Auto-generated method stub
		return productDAO.listOfProcurementProductFromHeap();
	}

	@Override
	public List<Object> listOfHeapName() {
		// TODO Auto-generated method stub
		return productDAO.listOfHeapName();
	}

	@Override
	public List<Object[]> findSupplierProcurementDataByFilter(long l, String dateRange) {
		// TODO Auto-generated method stub
		return productDAO.findSupplierProcurementDataByFilter(l,dateRange);
	}

	@Override
	public List<Object[]> findSupplierProcurementCummulativeData() {
		// TODO Auto-generated method stub
		return productDAO.findSupplierProcurementCummulativeData();
	}

	@Override
	public List<Object> listSupplierProcurementAmtByMoth(Date firstDateOfMonth, Date date) {
		// TODO Auto-generated method stub
		return productDAO.listSupplierProcurementAmtByMoth(firstDateOfMonth,date);
	}

	@Override
	public List<Object> listSupplierProcurementQtyByMoth(Date firstDateOfMonth, Date date) {
		// TODO Auto-generated method stub
		return productDAO.listSupplierProcurementQtyByMoth(firstDateOfMonth,date);
	}

	@Override
	public List<Object[]> listOfFarmCrops() {
		// TODO Auto-generated method stub
		return productDAO.listOfFarmCrops();
	}

	@Override
	public List<Object[]> listOfGiningFromBale() {
		return productDAO.listOfGiningFromBale();
	}

	@Override
	public List<Object> listOfHeapFromBale() {
		return productDAO.listOfHeapFromBale();
	}

	@Override
	public ProcurementProduct findProcurementProductByCode(String productCode, String tenantId) {
		return productDAO.findProcurementProductByCode(productCode, tenantId);
	}

	@Override
	public List<Object> listSupplierProcurementGroupByMoth(Integer year, Integer month) {
		// TODO Auto-generated method stub
		return productDAO.listSupplierProcurementGroupByMoth(year, month);
	}

	@Override
	public List<Object[]> listOfProducts() {
		// TODO Auto-generated method stub
		return productDAO.listOfProducts();
	}

	@Override
	public void removeProcurment(String id, String deleteStatus) {
		// TODO Auto-generated method stub
		productDAO.removeProcurment(id,deleteStatus);
	}

	@Override
	public List<Object[]> listCategoriesByWarehouseId(long warehouseId,String season) {
		return productDAO.listCategoriesByWarehouseId(warehouseId,season);
	}

	@Override
	public Double findAvailableStockByWarehouseIdAndProduct(String senderWarehouse, String product,String season) {
		return productDAO.findAvailableStockByWarehouseIdAndProduct(senderWarehouse,product,season);
	}

	@Override
	public List<Object> listOfDistributionStockReceiptNo(long warehosueId) {
		return productDAO.listOfDistributionStockReceiptNo(warehosueId);
	}

	@Override
	public List<Product> findProductByWarehouseIdAndSubCategoryCode(long warehouseId,String category,String season) {
		return productDAO.findProductByWarehouseIdAndSubCategoryCode(warehouseId,category,season);
	}
	public List<SubCategory> lisCategoryByWarehouseIdSeasonCodeAgent(long agentId, String seasonCode) {
		return productDAO.lisCategoryByWarehouseIdSeasonCodeAgent(agentId,seasonCode);
	}

	@Override
	public List<Product> listofProductBySubCategoryAndSelectedAgent(String selectedCategory, long selectedAgent,String seasonCode) {
		// TODO Auto-generated method stub
		return productDAO.listofProductBySubCategoryAndSelectedAgent(selectedCategory,selectedAgent,seasonCode);
	}

	@Override
	public List<AgroTransaction> listAgroTransactionByDistributionStockId(long id,String receiptNo,String txnType,String seasonCode) {
		// TODO Auto-generated method stub
		return productDAO.listAgroTransactionByDistributionStockId(id,receiptNo,txnType,seasonCode);
	}

	@Override
	public List<Object[]> listOfDistributionProductsByType(String pType) {
		return productDAO.listOfDistributionProductsByType(pType);
	}

	@Override
	public Product findProuductByProudctNameSubCategoryIdAndManufacture(String selectedProductName, Long category,
			String manufacture) {
		return productDAO.findProuductByProudctNameSubCategoryIdAndManufacture(selectedProductName,category,manufacture);
	}

	@Override
	public ProcurementVariety findProcurementVarietyByCode(String code, String tenantId) {
		// TODO Auto-generated method stub
		return productDAO.findProcurementVarietyByCode(code, tenantId);
	}
	
	public List<ColdStorageStockTransfer> findColdStorageStockTransferByCustomerId(Long id) {
		return productDAO.findColdStorageStockTransferByCustomerId(id);
	}

	@Override
	public List<Object[]> findProcurementTraceabilityDataByFilter(Long selectedProduct, String selectedDate) {
		// TODO Auto-generated method stub
		return productDAO.findProcurementTraceabilityDataByFilter(selectedProduct, selectedDate);
	}

	@Override
	public List<Object[]> findProcurementTraceabilityCummulativeData() {
		// TODO Auto-generated method stub
		return productDAO.findProcurementTraceabilityCummulativeData();
	}
	
	@Override
	public List<Object[]> listProcurmentGradeByVarityCode(String code){
		return productDAO.listProcurmentGradeByVarityCode(code);
	}
}
