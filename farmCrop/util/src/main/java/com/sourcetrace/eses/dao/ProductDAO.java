/*
 * ProductDAO.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ese.entity.profile.CropHarvest;
import com.ese.entity.profile.CropHarvestDetails;
import com.ese.entity.profile.CropSupply;
import com.ese.entity.profile.CropSupplyDetails;
import com.ese.entity.profile.ViewCultivation;
import com.sourcetrace.eses.entity.Agent;
import com.sourcetrace.eses.entity.ColdStorage;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.filter.ISecurityFilter;
import com.sourcetrace.eses.order.entity.profile.SubCategory;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.ColdStorageStockTransfer;
import com.sourcetrace.eses.umgmt.entity.User;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.ReflectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.profile.Product;

import com.sourcetrace.esesw.entity.profile.CropCalendar;
import com.sourcetrace.esesw.entity.profile.CropCalendarDetail;
import com.sourcetrace.esesw.entity.profile.FarmCrops;
import com.sourcetrace.esesw.entity.profile.HarvestSeason;
import com.sourcetrace.esesw.entity.profile.ProcurementGrade;
import com.sourcetrace.esesw.entity.profile.ProcurementProduct;
import com.sourcetrace.esesw.entity.profile.ProcurementVariety;
import com.sourcetrace.esesw.entity.profile.Vendor;

@Repository
@Transactional
public class ProductDAO extends ESEDAO implements IProductDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDAO#findProductList()
	 */
	@Autowired
	public ProductDAO(SessionFactory sessionFactory) {

		this.setSessionFactory(sessionFactory);
	}

	public List<Product> findProductList() {

		return list("FROM Product");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IProductDAO#findByProductCode(
	 * java.lang.String)
	 */
	public Product findByProductCode(String code) {

		Product product = (Product) find("FROM Product p WHERE p.code = ?", code);
		return product;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IProductDAO#findByProductName(
	 * java.lang.String)
	 */
	public Product findByProductName(String name) {

		Product product = (Product) find("FROM Product p WHERE p.name = ?", name);
		return product;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IProductDAO#findProductById(long)
	 */
	public Product findProductById(long id) {

		Product product = (Product) find("FROM Product p WHERE p.id = ?", id);
		return product;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDAO#
	 * findWarehouseProductbyCode (java.lang.String, java.lang.String)
	 */
	public WarehouseProduct findWarehouseProductbyCode(String warehousecode, String productCode) {

		Object[] values = { warehousecode, productCode };
		return (WarehouseProduct) find("FROM WarehouseProduct wp WHERE wp.warehouse.code = ? AND wp.product.code = ?",
				values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDAO#
	 * findWarehouseProductbyId (long, long)
	 */
	public WarehouseProduct findWarehouseProductbyId(long warehouseId, long productId) {

		Object[] values = { warehouseId, productId };
		return (WarehouseProduct) find("FROM WarehouseProduct wp WHERE wp.warehouse.id = ? AND wp.product.id = ?",
				values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IProductDAO#listWarehouseProduct()
	 */
	public List<WarehouseProduct> listWarehouseProduct() {

		return list("FROM WarehouseProduct wp ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listProductBySubCategory (java.lang.String)
	 */
	public List<Product> listProductBySubCategory(String selectedSubCategory) {

		return list("FROM Product p  WHERE p.subcategory.name = ? ORDER BY p.name ASC", selectedSubCategory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDAO#
	 * findByWarehouseProductId (long)
	 */
	public WarehouseProduct findByWarehouseProductId(long id) {

		return (WarehouseProduct) find("FROM WarehouseProduct wp WHERE wp.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDAO#
	 * findProductBySubCategoryCode(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Product> findProductBySubCategoryCode(String subCategoryId) {

		return list("FROM Product p WHERE p.subcategory.code=?", subCategoryId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IProductDAO#listProductByWarehouse
	 * (java.lang.String)
	 */
	public List<Product> listProductByWarehouse(String selectedWarehouse) {

		String queryString = "SELECT * FROM product INNER JOIN warehouse_product ON warehouse_product.PRODUCT_ID = product.ID WHERE warehouse_product.WAREHOUSE_ID = (SELECT id FROM warehouse where `CODE`='"
				+ selectedWarehouse + "')";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString).addEntity(Product.class);
		List<Product> list = query.list();
		sessions.flush();
		sessions.close();
		return list;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDAO#
	 * findWarehouseProductMappingExist(long)
	 */
	public boolean findWarehouseProductMappingExist(long productId) {

		Long productCount = (Long) find(
				"Select Count(wp) From WarehouseProduct wp where wp.product.id = ? AND wp.stock>0", productId);
		boolean isMappingExist = false;
		if (productCount > 0) {
			isMappingExist = true;
		}
		return isMappingExist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listProductNameUnitByWarehouse(java.lang .String)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> listProductNameUnitByWarehouse(String warehouseCode) {

		String queryString = "SELECT PRODUCT.`NAME`,GROUP_CONCAT(PRODUCT.UNIT SEPARATOR '~' ) FROM product p INNER JOIN warehouse_product ON warehouse_product.PRODUCT_ID = product.ID WHERE warehouse_product.WAREHOUSE_ID = (SELECT id FROM warehouse where `CODE`='"
				+ warehouseCode
				+ "' ) AND WAREHOUSE_PRODUCT.AGENT_ID IS NULL AND WAREHOUSE_PRODUCT.STOCK >0 GROUP BY PRODUCT.NAME ORDER BY p.name ASC";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDAO#
	 * findProductByNameAndUnit (java.lang.String, java.lang.String)
	 */
	public Product findProductByNameAndUnit(String name, String selectedUnit) {

		Object[] values = { name, selectedUnit };
		return (Product) find("From Product p where p.name=? AND p.unit=?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listWarehouseProductByCityId(long)
	 */
	public List<WarehouseProduct> listWarehouseProductByCityId(long cityId) {

		return list("FROM WarehouseProduct wp WHERE wp.warehouse.city.id = ? AND wp.stock > 0", cityId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listProductNameUnitByWarehouseAgentId( java.lang.String, long)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> listProductNameUnitByWarehouseAgentId(String warehouseCode, long agentId) {

		String queryString = "SELECT PRODUCT.`NAME`,GROUP_CONCAT(PRODUCT.UNIT SEPARATOR '~' ) FROM product INNER JOIN warehouse_product ON warehouse_product.PRODUCT_ID = product.ID WHERE warehouse_product.WAREHOUSE_ID = (SELECT id FROM warehouse where `CODE`='"
				+ warehouseCode + "' ) AND WAREHOUSE_PRODUCT.AGENT_ID=" + agentId
				+ " AND WAREHOUSE_PRODUCT.STOCK >0 GROUP BY PRODUCT.NAME";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDAO#listProducts()
	 */
	public List<Object[]> listProducts() {

		String queryString = "select product.NAME, GROUP_CONCAT(PRODUCT.UNIT SEPARATOR '~' ),product.ID,product.code,product.manufacture from product group by product.name";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listWarehouseProductByAgent(com.ese.entity .profile.Agent)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.order.dao.service.IProductDAO#listProductNameUnit()
	 */
	public List<Object[]> listProductNameUnit() {

		String queryString = "SELECT PRODUCT.`NAME`,GROUP_CONCAT(PRODUCT.UNIT SEPARATOR '~' ) FROM product GROUP BY PRODUCT.NAME";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listProductNameUnitByCooperativeManagerProfileId(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> listProductNameUnitByCooperativeManagerProfileId(String agentId) {

		String queryString = "select pr.NAME,GROUP_CONCAT(pr.UNIT SEPARATOR '~' ) FROM product pr INNER JOIN warehouse_product wp ON pr.ID=wp.PRODUCT_ID INNER JOIN warehouse co ON wp.WAREHOUSE_ID=co.ID WHERE co.REF_WAREHOUSE_ID IS NULL AND wp.STOCK>0 AND co.ID = (SELECT wh.ID FROM Warehouse wh INNER JOIN agent_warehouse_map mp ON wh.ID=mp.WAREHOUSE_ID INNER JOIN agent_prof a ON a.PROF_ID=mp.AGENT_ID INNER JOIN prof p ON p.id=a.PROF_ID WHERE p.PROF_ID=:agentId) and wp.agent_id IS NULL GROUP BY pr.NAME ORDER BY pr.NAME";
		Session session = getSessionFactory().openSession();
		Query query = session.createSQLQuery(queryString).setParameter("agentId", agentId);
		List<Object[]> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listWarehouseProductByAgentId(long)
	 */
	@SuppressWarnings("unchecked")
	public List<WarehouseProduct> listWarehouseProductByAgentId(long agentId, long warehouseid) {

		Object[] bindValues = { agentId, warehouseid };
		return list("FROM WarehouseProduct wp WHERE wp.agent.id = ? AND wp.stock > 0 AND wp.warehouse.id=?",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listProductByRevisionNo (long)
	 */
	public List<Product> listProductByRevisionNo(long revisionNo, String branchId) {

		// return list("FROM Product p WHERE p.revisionNo>? and
		// p.subcategory.branchId=? ORDER BY p.revisionNo DESC", revisionNo);
		Object[] values = { revisionNo };
		return list("FROM Product p WHERE p.revisionNo>? ORDER BY p.revisionNo DESC", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listWarehouseProductByAgentIdRevisionNo (long, long, long)
	 */
	public List<WarehouseProduct> listWarehouseProductByAgentIdRevisionNo(long agentId, long warehouseId,
			long revisionNo) {

		Object[] bindValues = { agentId, warehouseId, revisionNo };
		return list(
				"FROM WarehouseProduct wp WHERE wp.agent.id = ? AND wp.stock > 0 AND wp.warehouse.id=? AND wp.revisionNo>? ORDER BY wp.revisionNo DESC",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listProductNameUnitByWarehouseAgent(long)
	 */
	public List<Object[]> listProductNameUnitByWarehouseAgent(long agentId) {

		String queryString = " SELECT PRODUCT.`NAME`,GROUP_CONCAT(PRODUCT.UNIT SEPARATOR '~' ) FROM product INNER JOIN warehouse_product ON warehouse_product.PRODUCT_ID = product.ID WHERE WAREHOUSE_PRODUCT.AGENT_ID="
				+ agentId + " AND WAREHOUSE_PRODUCT.STOCK >0 GROUP BY PRODUCT.NAME ";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	/*
	 * public List<WarehouseProduct> listProductByWarehouseProductId(long id) {
	 * returnlist(
	 * "FROM WarehouseProduct wp WHERE wp.warehouse.typez=1 AND wp.product.id=?"
	 * ,id); }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listWarehouseStockProducts ()
	 */
	public List<Object[]> listWarehouseStockProducts() {

		return list(
				"select distinct(wp.product.code),wp.product.name FROM WarehouseProduct wp WHERE wp.warehouse.typez=0 AND wp.warehouse IS NOT NULL ORDER BY wp.product.name ASC ");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDAO#
	 * listWarehouseProductByAgentRevisionNo( long, long)
	 */
	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNo(long agentId, long revisionNo) {

		Object[] bindValues = { agentId, revisionNo };
		return list(
				"FROM WarehouseProduct wp WHERE wp.agent.id = ? AND wp.stock > 0 AND wp.revisionNo>? ORDER BY wp.revisionNo DESC",
				bindValues);
	}

	public List<Product> findProductListBasedOnSubCategoryCode(String selectedSubCategoryList, String branchId) {
		Object[] bindValues = { selectedSubCategoryList, branchId };

		return list(
				"SELECT pts FROM SubCategory sc INNER JOIN sc.products pts WHERE sc.name=? AND sc.branchId=? ORDER BY pts.name ASC",
				bindValues);
	}

	public Vendor findVendorIdById(long id) {

		return (Vendor) find("FROM Vendor vn WHERE vn.id= ?", id);
	}

	public Product findProductUnitByProductId(long id) {

		return (Product) find("FROM Product pd WHERE pd.id= ?", id);
	}

	public List<SubCategory> findSubCategorybyWarehouseId(String warehouseCode) {

		return list(
				"select distinct(wp.product.subcategory) FROM WarehouseProduct wp WHERE wp.warehouse IS NOT NULL AND wp.warehouse.code = ? ORDER BY wp.product.subcategory.name ASC",
				warehouseCode);
	}

	public List<SubCategory> findSubCategorybyAgentId(long agentId) {
		return list(
				"select distinct(wp.product.subcategory) FROM WarehouseProduct wp WHERE wp.warehouse IS NULL AND wp.stock>0 AND wp.agent.id = ?",
				agentId);
	}

	public WarehouseProduct findCostPriceForProduct(long id, long warehouseId) {

		Object[] values = { id, warehouseId };

		return (WarehouseProduct) find("from WarehouseProduct wp where wp.product.id = ? AND wp.warehouse.id=?",
				values);
	}

	public Product findProductByProductCode(String code) {

		Product product = (Product) find("FROM Product p WHERE p.code = ?", code);
		return product;
	}

	public Warehouse findWarehouseByCode(String code) {
		Warehouse warehouse = (Warehouse) find("FROM Warehouse w WHERE w.code = ?", code);
		return warehouse;

	}

	public WarehouseProduct findCostPriceForAgent(long productId, long agentId) {
		Object[] values = { productId, agentId };

		return (WarehouseProduct) find("from WarehouseProduct wp inner join fetch wp.product p where wp.product.id = ? AND wp.agent.id=?", values);
	}

	public Agent findProfileByProfileid(String profileId) {
		// TODO Auto-generated method stub
		return (Agent) find("FROM Agent p WHERE p.profileId=?", profileId);
	}

	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStock(long agentId, long revisionNo) {
		Object[] bindValues = { agentId, revisionNo };
		return list("FROM WarehouseProduct wp WHERE wp.agent.id = ? AND wp.revisionNo>? ORDER BY wp.revisionNo DESC",
				bindValues);
	}

	public List<SubCategory> findCategoryListBasedOnProductCode(long warehouseId) {

		return list(
				"select distinct(wp.product.subcategory) FROM WarehouseProduct wp WHERE wp.warehouse IS NOT NULL AND wp.warehouse.id = ?",
				warehouseId);
	}

	public ProcurementGrade findProcurementGradeByCode(String code) {
		// TODO Auto-generated method stub
		return (ProcurementGrade) find("FROM ProcurementGrade pg WHERE pg.code=?", code);
	}

	public ProcurementProduct findProcurementProductByCode(String code) {
		// TODO Auto-generated method stub
		return (ProcurementProduct) find("FROM ProcurementProduct pp WHERE pp.code=?", code);
	}

	public ProcurementVariety findProcurementVarietyByCode(String code) {
		// TODO Auto-generated method stub
		return (ProcurementVariety) find("FROM ProcurementVariety pv WHERE pv.code=?", code);
	}

	public CropHarvest findCropHarvestById(long id) {
		// TODO Auto-generated method stub
		CropHarvest cropHarvest = (CropHarvest) find("FROM CropHarvest c WHERE c.id = ?", id);
		return cropHarvest;
	}

	public List<CropHarvestDetails> listCropHarvestDetails(long id) {
		// TODO Auto-generated method stub
		return list("FROM CropHarvestDetails cd WHERE cd.id = ?", id);
	}

	public List<CropHarvestDetails> listCropHarvestDetails(long id, int startIndex, int limit) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		String queryString = "select * FROM crop_harvest_details WHERE CROP_HARVEST_ID = '" + id + "' LIMIT "
				+ startIndex + "," + limit;
		Query query = session.createSQLQuery(queryString).addEntity(CropHarvestDetails.class);
		List<CropHarvestDetails> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	public CropSupply findCropSupplyById(long id) {
		CropSupply cropSupply = (CropSupply) find("FROM CropSupply c WHERE c.id = ?", id);
		return cropSupply;
	}

	public ViewCultivation findViewCultivationById(long id) {
		ViewCultivation viewCultivation = (ViewCultivation) find("FROM ViewCultivation c WHERE c.id = ?", id);
		return viewCultivation;
	}

	public CropHarvestDetails findCropHarvestDetailsById(long id) {
		// TODO Auto-generated method stub
		return (CropHarvestDetails) find("FROM CropHarvestDetails cd WHERE cd.id=?", id);
	}

	public List<CropHarvestDetails> listOfCrops() {
		// TODO Auto-generated method stub
		return list("FROM CropHarvestDetails ");
	}

	@Override
	public List<ProcurementProduct> listProcurmentProductsByType(String cropTypeCode) {
		// TODO Auto-generated method stub
		return list("FROM ProcurementProduct p WHERE p.type=?", Integer.valueOf(cropTypeCode));
	}

	@Override
	public List<ProcurementVariety> listProcurmentVarirtyByProcurementProductId(String cropId) {
		// TODO Auto-generated method stub
		return list("FROM ProcurementVariety pv WHERE pv.procurementProduct.id=?", Integer.valueOf(cropId));
	}

	@Override
	public List<ProcurementGrade> listProcurmentGradeByVarityId(String varietyId) {
		// TODO Auto-generated method stub
		return list(" FROM ProcurementGrade pg WHERE pg.procurementVariety.id=?", Integer.valueOf(varietyId));
	}

	@Override
	public List<ProcurementVariety> listProcurementVariety() {
		// TODO Auto-generated method stub
		return list("FROM ProcurementVariety pv");
	}

	@Override
	public List<CropHarvestDetails> listCropHarvestDetails() {
		// TODO Auto-generated method stub
		return list("FROM CropHarvestDetails cd");
	}

	@Override
	public CropHarvestDetails findCropHarvestDetailsbyHarvestIdandtherItems(Long harvestId, int cropType, long cropId,
			Long varietyId, Long gradeId) {
		// TODO Auto-generated method stub
		Object[] values = { cropType, harvestId, cropId, varietyId, gradeId };
		// String query="FROM CropHarvestDetails cd where cd.cropType=:cropType
		// cd.cropHarvest.id=:harvestId cd.crop.id=:cropId
		// cd.variety.id=:varietyId cd.grade.id=:gradeId";
		return (CropHarvestDetails) find(
				"FROM CropHarvestDetails cd where cd.cropType=? and cd.cropHarvest.id=? and cd.crop.id=? and cd.variety.id=? and cd.grade.id=?",
				values);
	}

	@Override
	public ProcurementProduct findProcurementProductById(long id) {
		// TODO Auto-generated method stub
		return (ProcurementProduct) find("from ProcurementProduct pp where pp.id=?", id);

	}

	@Override
	public List<ProcurementGrade> listProcurementGrade() {
		// TODO Auto-generated method stub
		return list("FROM  ProcurementGrade pg");
	}

	@Override
	public ProcurementVariety findProcurementVarietyById(long varietyId) {
		// TODO Auto-generated method stub
		return (ProcurementVariety) find("from ProcurementVariety pv where pv.id=?", varietyId);
	}

	@Override
	public ProcurementGrade findProcurementGradeById(long gradeId) {
		// TODO Auto-generated method stub
		return (ProcurementGrade) find("from ProcurementGrade pg where pg.id=?", gradeId);
	}

	@Override
	public void removeCropHarvestDetails(CropHarvestDetails cropHarvestDetails) {
		// TODO Auto-generated method stub

		Session session = getSessionFactory().openSession();
		String queryString = "DElETE FROM crop_harvest_details where ID ='" + cropHarvestDetails.getId() + "'";

		Query query = session.createSQLQuery(queryString);
		query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public CropSupplyDetails findCropSupplyDetailsbySupplyIdandtherItems(long supplyId, int cropType, long cropId,
			long varietyId, long gradeId) {
		// TODO Auto-generated method stub
		Object[] values = { cropType, supplyId, cropId, varietyId, gradeId };
		// String query="FROM CropHarvestDetails cd where cd.cropType=:cropType
		// cd.cropHarvest.id=:harvestId cd.crop.id=:cropId
		// cd.variety.id=:varietyId cd.grade.id=:gradeId";
		return (CropSupplyDetails) find(
				"FROM CropSupplyDetails cd where cd.cropType=? and cd.cropSupply.id=? and cd.crop.id=? and cd.variety.id=? and cd.grade.id=?",
				values);
	}

	@Override
	public void removeCropSupplyDetails(long id) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("DElETE FROM CropSupplyDetails csd WHERE csd.id=:ID");
		query.setParameter("ID", id);
		int value = query.executeUpdate();

		session.flush();
		session.close();

	}

	@Override
	public List<CropSupplyDetails> listCropSupplyDetails(long cropSupplyId) {
		// TODO Auto-generated method stub
		return list("FROM CropSupplyDetails cs WHERE cs.cropSupply.id = ?", cropSupplyId);
	}

	public List<CropSupply> findCropSupplyByCustomerId(Long id) {
		return list("FROM CropSupply cs WHERE cs.buyerInfo.id = ?", id);
	}

	public List<CropSupply> listCropSupplyByFarmCode(String farmCode) {
		return list("FROM CropSupply cs WHERE cs.farmCode = ?", farmCode);
	}

	public List<CropHarvest> listCropHarvestByFarmCode(String farmCode) {
		return list("FROM CropHarvest ch WHERE ch.farmCode = ?", farmCode);
	}

	public List<FarmCrops> listFarmCropsBySeasonId(Long id) {
		return list("FROM FarmCrops fc WHERE fc.cropSeason.id = ?", id);
	}

	@Override
	public List<CropHarvestDetails> listCropHarvestDetailsByHarvestId(long id) {
		return list("FROM CropHarvestDetails cd WHERE cd.cropHarvest.id = ?", id);
	}

	/*
	 * @Override public List<Object> listCropSaleQtyByMoth(Date sDate, Date
	 * eDate) { Session session = getSessionFactory().getCurrentSession(); Query
	 * query = session.createQuery(
	 * "select year(cs.dateOfSale),month(cs.dateOfSale),SUM(cs.totalSaleValu) from CropSupply cs where cs.dateOfSale BETWEEN :sDate AND :eDate group by year(cs.dateOfSale),month(cs.dateOfSale)"
	 * ); query.setParameter("sDate", sDate).setParameter("eDate", eDate);
	 * List<Object> resultSet = query.list(); return resultSet; }
	 */

	@Override
	public List<Object> listCropSaleQtyByMoth(Date sDate, Date eDate,String selectedBranch) {
		Session session = getSessionFactory().openSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String sqlString= "SELECT YEAR(cs.DATE_SALE ),MONTH (cs.DATE_SALE ),sum(csd.QTY ) FROM CROP_SUPPLY cs INNER JOIN CROP_SUPPLY_DETAILS csd "
				+ "ON cs.ID = csd.CROP_SUPPLY_ID INNER JOIN farm fa on fa.FARM_CODE=cs.FARM_CODE INNER JOIN farmer f on f.FARMER_ID=cs.FARMER_ID "
				+ "WHERE cs.DATE_SALE BETWEEN :sDate AND :eDate AND fa.status=1 AND f.status=1 AND f.STATUS_CODE=0 GROUP BY YEAR (cs.DATE_SALE),MONTH (cs.DATE_SALE)";
		
		if (!StringUtil.isEmpty(selectedBranch)) {
			sqlString += " AND f.branch_Id =:branch";
			params.put("branch", selectedBranch);
		}
		params.put("sDate", sDate);
		params.put("eDate", eDate);
		SQLQuery query = session.createSQLQuery(sqlString);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
						
		List<Object> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public List<Object> listCropHarvestByMoth(Date sDate, Date eDate,String selectedBranch) {
		Session session = getSessionFactory().openSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String sqlString= "select year(ch.HARVEST_DATE),month(ch.HARVEST_DATE),sum(chd.QTY) from CROP_HARVEST ch inner join CROP_HARVEST_DETAILS chd "
				+ "on ch.ID=chd.CROP_HARVEST_ID INNER JOIN farm fa ON fa.FARM_CODE = ch.FARM_CODE INNER JOIN farmer f "
				+ "ON f.FARMER_ID = ch.FARMER_ID where ch.HARVEST_DATE between  :sDate AND :eDate AND fa.status=1 AND f.status=1 AND f.STATUS_CODE=0 "
				+ "group by year(ch.HARVEST_DATE),month(ch.HARVEST_DATE)";
				
		if (!StringUtil.isEmpty(selectedBranch)) {
			sqlString += " AND f.branch_Id =:branch";
			params.put("branch", selectedBranch);
		}
		params.put("sDate", sDate);
		params.put("eDate", eDate);
		SQLQuery query = session.createSQLQuery(sqlString);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
						
		List<Object> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public List<Object> listDistributionQtyByMoth(Date sDate, Date eDate,String selectedBranch) {
		Session session = getSessionFactory().openSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String sqlString= "select year(d.TXN_TIME),month(d.TXN_TIME),sum(dd.QUANTITY) from DISTRIBUTION d inner join DISTRIBUTION_DETAIL dd "
				+ "on d.id=dd.DISTRIBUTION_ID INNER JOIN farmer f "
				+ "ON f.FARMER_ID = d.FARMER_ID where d.TXN_TIME between  :sDate AND :eDate AND f.status=1 AND f.STATUS_CODE=0 "
				+ "group by year(d.TXN_TIME),month(d.TXN_TIME)";
				
				
		if (!StringUtil.isEmpty(selectedBranch)) {
			sqlString += " AND f.branch_Id =:branch";
			params.put("branch", selectedBranch);
		}
		params.put("sDate", sDate);
		params.put("eDate", eDate);
		SQLQuery query = session.createSQLQuery(sqlString);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
						
		List<Object> result = query.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public Product findProductByCodeByTenantId(String code, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM Product p WHERE p.code = :code");
		query.setParameter("code", code);

		// Product productList = (Product) query.list();

		List<Product> productList = query.list();

		Product product = null;
		if (productList.size() > 0) {
			product = (Product) productList.get(0);
		}

		session.flush();
		session.close();
		return product;
	}

	@Override
	public SubCategory findSubCategoryByCode(String code) {

		SubCategory subCategory = (SubCategory) find("FROM SubCategory sc WHERE sc.code = ?", code);
		return subCategory;
	}

	@Override
	public Product findProductByProductCodeAndSubCategoryId(String productCode, long subCategoryId) {
		Object[] values = { productCode, subCategoryId };
		return (Product) find("FROM Product p Where p.code=? AND p.subcategory.id=?", values);

	}

	public Product findProductByProductNameAndSubCategoryId(String productName, long subCategoryId) {
		Object[] values = { productName, subCategoryId };
		return (Product) find("FROM Product p Where p.name=? AND p.subcategory.id=?", values);

	}

	@Override
	public List<SubCategory> lisCategoryByWarehouseIdSeasonCode(String warehouseCode, String seasonCode) {
		// TODO Auto-generated method stub
		Object[] values = { warehouseCode, seasonCode };
		return list(
				"select distinct(wp.product.subcategory) FROM WarehouseProduct wp WHERE wp.warehouse IS NOT NULL AND wp.warehouse.code = ? AND wp.seasonCode=? AND wp.stock > 0 ORDER BY wp.product.subcategory.name ASC",
				values);
	}

	@Override
	public List<Object[]> findFarmerCountCultivationAreaEsiyield(String farmerId, String cropId, String season,
			String samithi, String icsCode) {

		Session session = getSessionFactory().getCurrentSession();
		Map<String, Object> params = new HashMap<String, Object>();

		String hqlQuery = "select SUM(fc.estimatedYield) as Total,cast(SUM(fc.cultiArea) as int),count(DISTINCT f.id) FROM FarmCrops fc INNER JOIN fc.farm fa INNER JOIN fa.farmer f WHERE f.status=1";

		if (!StringUtil.isEmpty(icsCode)) {
			hqlQuery += " AND f.icsName =:icsCode ";
			params.put("icsCode", icsCode);
		}

		if (!StringUtil.isEmpty(farmerId)) {
			hqlQuery += " AND f.farmerId =:farmerId ";
			params.put("farmerId", farmerId);
		}
		if (!StringUtil.isEmpty(season)) {
			hqlQuery += " AND fc.cropSeason.code =:season";
			params.put("season", season);
		}

		if (!StringUtil.isEmpty(samithi)) {
			hqlQuery += " AND f.samithi.id=:samithi";
			params.put("samithi", Long.valueOf(samithi));
		}
		if (!StringUtil.isEmpty(cropId)) {
			hqlQuery += "  AND fc.procurementVariety.procurementProduct.id=:selectedCrop";
			params.put("selectedCrop", Long.valueOf(cropId));
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		return query.list();

	}

	@Override
	public List<Object[]> listOfHarvestedCrops(String branchId) {
		// TODO Auto-generated method stub

		if (!StringUtil.isEmpty(branchId)) {
			return list(
					"SELECT DISTINCT chd.crop.name,chd.crop.code from CropHarvestDetails chd INNER JOIN chd.cropHarvest ch WHERE ch.branchId=?)",
					branchId);
		} else {
			return list(
					"SELECT DISTINCT chd.crop.name,chd.crop.code from CropHarvestDetails chd INNER JOIN chd.cropHarvest ch)");
		}
	}

	@Override
	public List<Object[]> listOfProductByStock() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT DISTINCT wp.product.code,wp.product.name,wp.product.subcategory.name, wp.product.id from WarehouseProduct wp )");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStockAndSeasonCode(long id, Long valueOf,
			String seasonCode) {

		Object[] bindValues = { id, seasonCode, valueOf };
		return list(
				"FROM WarehouseProduct wp WHERE wp.agent.id = ? AND wp.seasonCode = ? AND wp.revisionNo>? ORDER BY wp.revisionNo DESC",
				bindValues);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStockAndSeasonCodeByBatch(long id, Long valueOf,
			String seasonCode) {

		Object[] bindValues = { id, seasonCode, valueOf };
		return list(
				"FROM WarehouseProduct wp WHERE wp.agent.id = ? AND wp.seasonCode = ? AND wp.revisionNo>? GROUP BY wp.batchNo,wp.warehouse.id,wp.product.id,wp.agent.id ORDER BY wp.revisionNo DESC",
				bindValues);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WarehouseProduct> listWarehouseProductBySeasonCodeByBatch(long id, String seasonCode) {
		Object[] bindValues = { id, seasonCode };
		return list(
				"FROM WarehouseProduct wp WHERE wp.agent.id = ? AND wp.seasonCode = ?  GROUP BY wp.batchNo,wp.warehouse.id,wp.product.id,wp.agent.id ORDER BY wp.warehouse.id DESC",
				bindValues);
	}

	@Override
	public Product findProductUnitByProductCode(String code) {
		// TODO Auto-generated method stub
		Product product = (Product) find("FROM Product pr WHERE pr.code = ?", code);
		return product;
	}

	@Override
	public HarvestSeason findHarvestSeasonNamebySeasonCode(String code) {
		HarvestSeason seasonName = (HarvestSeason) find("From HarvestSeason hs WHERE hs.code=?", code);
		return seasonName;
	}

	public List<Object[]> listFarmProducts() {

		String queryString = "Select DISTINCT p.name,p.id from Product p";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	@Override
	public List<SubCategory> findSubCategorybyAgentIdAndSeason(Long agentId, String selectedSeason) {
		return list(
				"select distinct(wp.product.subcategory) FROM WarehouseProduct wp WHERE wp.warehouse IS NULL AND wp.agent.id = ? and wp.seasonCode = ?",
				new Object[] { agentId, selectedSeason });
	}

	@Override
	public List<Object> listcowMilkByMonth(Date sDate, Date eDate) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"select year(ci.currentInspDate),month(ci.currentInspDate),SUM(ci.totalMilkPerDay) from CowInspection ci where ci.currentInspDate BETWEEN :sDate AND :eDate group by year(ci.currentInspDate),month(ci.currentInspDate)");
		query.setParameter("sDate", sDate).setParameter("eDate", eDate);
		List<Object> resultSet = query.list();
		return resultSet;
	}

	@Override
	public ProcurementGrade findGradeUnitByGradeId(long id) {
		// TODO Auto-generated method stub
		return (ProcurementGrade) find("FROM ProcurementGrade pg WHERE pg.id= ?", id);
	}

	@Override
	public Object[] findByProdNameAndId(String selectedProductName) {
		// TODO Auto-generated method stub
		return (Object[]) find("select p.id,p.name from Product p where p.name=?", selectedProductName);
	}

	@Override
	public Product findProductByProductIdAndSubCategoryId(long productId, long subCategoryId) {
		Object[] values = { productId, subCategoryId };
		return (Product) find("FROM Product p Where p.id=? AND p.subcategory.id=?", values);

	}

	@Override
	public List<SubCategory> lisCategoryByWarehouseId(String warehouseCode) {

		// TODO Auto-generated method stub
		Object[] values = { warehouseCode };
		return list(
				"select distinct(wp.product.subcategory) FROM WarehouseProduct wp WHERE wp.warehouse IS NOT NULL AND wp.warehouse.code = ? AND wp.stock > 0 ORDER BY wp.product.subcategory.name ASC",
				values);
	}

	@Override
	public ProcurementProduct findUnitByCropId(long id) {
		return (ProcurementProduct) find("FROM ProcurementProduct pp WHERE pp.id= ?", id);
	}

	@Override
	public List<Object[]> listProcurementGradeInfo() {
		return list(
				"SELECT pg.id,pg.code,pg.name,pv.id,pv.code,pv.name,pp.id,pp.code,pp.name,pp.unit FROM ProcurementGrade pg INNER JOIN pg.procurementVariety pv INNER JOIN pv.procurementProduct pp ORDER BY pg.id ASC");
	}

	@Override
	public List<Object[]> listFarmProducts(String branch) {
		String queryString = "Select DISTINCT p.name,p.id from Product p inner join sub_category sc where sc.BRANCH_ID='"
				+ branch + "' and p.SUB_CATEGORY_ID=sc.ID";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	@Override
	public List<Object[]> findProcurementCummulativeData() {
		String queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.NetWeight),SUM(pd.subTotal) FROM Procurement p INNER JOIN p.procurementDetails pd";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	@Override
	public List<Object[]> findProcurementDataByFilter(Long selectedProduct, String selectedDate) {
		Session sessions = getSessionFactory().openSession();
		String queryString = "";
		List<Object[]> list = new ArrayList<>();
		if (!StringUtil.isEmpty(selectedProduct) && selectedProduct > 0L && !StringUtil.isEmpty(selectedDate)) {
			String[] dateSplit = selectedDate.split("-");
			Date sDate = DateUtil.convertStringToDate(dateSplit[0], DateUtil.DATE_FORMAT);
			Date eDate = DateUtil.convertStringToDate(dateSplit[1], DateUtil.DATE_FORMAT);

			queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.NetWeight),SUM(pd.subTotal) FROM Procurement p INNER JOIN p.procurementDetails pd WHERE pd.procurementProduct.id=:selectedProduct AND p.createdDate BETWEEN :sDate AND :eDate";
			Query query = sessions.createQuery(queryString);
			query.setParameter("selectedProduct", selectedProduct);
			query.setParameter("sDate", sDate);
			query.setParameter("eDate", eDate);

			list = query.list();

		} else if (!StringUtil.isEmpty(selectedProduct) && selectedProduct > 0L) {

			queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.NetWeight),SUM(pd.subTotal) FROM Procurement p INNER JOIN p.procurementDetails pd WHERE pd.procurementProduct.id=:selectedProduct";
			Query query = sessions.createQuery(queryString);
			query.setParameter("selectedProduct", selectedProduct);
			list = query.list();

		} else if (!StringUtil.isEmpty(selectedDate)) {

			String[] dateSplit = selectedDate.split("-");
			Date sDate = DateUtil.convertStringToDate(dateSplit[0], DateUtil.DATE_FORMAT);
			Date eDate = DateUtil.convertStringToDate(dateSplit[1], DateUtil.DATE_FORMAT);
			queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.NetWeight),SUM(pd.subTotal) FROM Procurement p INNER JOIN p.procurementDetails pd WHERE p.createdDate BETWEEN :sDate AND :eDate";
			Query query = sessions.createQuery(queryString);
			query.setParameter("sDate", sDate);
			query.setParameter("eDate", eDate);
			list = query.list();

		}
		sessions.flush();
		sessions.close();
		return list;
	}

	@Override
	public List<Object[]> listOfProducts() {

		return list("SELECT p.id,p.code,p.name,p.subcategory.name,p.subcategory.id FROM Product p ORDER BY p.id ASC");
	}

	@Override
	public List<WarehouseProduct> listWarehouseProductByAgentRevisionNoStockByBatch(long agentId, Long revisionNo) {

		Object[] bindValues = { agentId, revisionNo };
		return list(
				"FROM WarehouseProduct wp WHERE wp.agent.id = ? AND wp.revisionNo>? GROUP BY wp.batchNo,wp.warehouse.id,wp.product.id,wp.agent.id,wp.seasonCode ORDER BY wp.revisionNo DESC",
				bindValues);
	}

	@Override
	public List<WarehouseProduct> listWarehouseProductByBatch(long agentId) {

		Object[] bindValues = { agentId };
		return list(
				"FROM WarehouseProduct wp WHERE wp.agent.id = ? GROUP BY wp.batchNo,wp.warehouse.id,wp.product.id,wp.agent.id ORDER BY wp.warehouse.id DESC",
				bindValues);
	}

	@Override
	public List<Object> listProcurementAmtByMoth(Date sDate, Date eDate,String selectedBranch) {

		Session session = getSessionFactory().openSession();
		Map<String, Object> params = new HashMap<String, Object>();
		String sqlString= "select year(proc.created_Date),month(proc.created_date),SUM(proc.TOTAL_AMOUNT),f.fpo as fpo FROM Procurement  proc inner join "
				+ "farmer f on f.id=proc.FARMER_ID where proc.created_date BETWEEN :sDate AND :eDate AND f.status=1 AND f.STATUS_CODE=0 "
				+ " group by year(proc.created_Date),month(proc.created_Date)";
												
		if (!StringUtil.isEmpty(selectedBranch)) {
			sqlString += " AND f.branch_Id =:branch";
			params.put("branch", selectedBranch);
		}
		params.put("sDate", sDate);
		params.put("eDate", eDate);
		SQLQuery query = session.createSQLQuery(sqlString);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
						
		List<Object> result = query.list();
		session.flush();
		session.close();
		return result;						
	}

	@Override
	public List<Object> listEnrollmentByMoth(Date sDate, Date eDate) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"select year(f.createdDate),month(f.createdDate),COUNT(f) FROM Farmer f WHERE f.statusCode=0 AND f.status=1 AND f.createdDate between :sDate AND :eDate group by year(f.createdDate),month(f.createdDate)");
		query.setParameter("sDate", sDate).setParameter("eDate", eDate);
		List<Object> resultSet = query.list();
		return resultSet;
	}

	@Override
	public String findProcurementProductUnitByProductCode(String selectedProduct) {
		return (String) find("SELECT pp.unit FROM ProcurementProduct pp WHERE pp.code= ?", selectedProduct);
	}

	@Override
	public List<ProcurementProduct> listProcurmentProductsByFarmer(String farmerId) {
		String queryString = "SELECT distinct p.procurementProduct  FROM ProcurementDetail p  where p.procurement.farmer.farmerId=:farmerId";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createQuery(queryString);
		query.setParameter("farmerId", farmerId);
		List<ProcurementProduct> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	@Override
	public List<Object[]> listWarehouseProductSeason() {
		return list(
				"SELECT DISTINCT wp.seasonCode, (SELECT hs.name from HarvestSeason hs where hs.code=wp.seasonCode) From WarehouseProduct wp where wp.warehouse.typez=1");
	}

	@Override
	public List<Product> findProductListByBranch(String branchId) {
		if (StringUtil.isEmpty(branchId)) {
			return findProductList();
		} else {
			return list("FROM Product p WHERE p.subcategory.branchId=?", branchId);
		}
	}

	@Override
	public List<ProcurementVariety> listProcurmentVarietyByProcurementProductIdInProcurement(String code) {
		String queryString = "SELECT distinct p.procurementGrade.procurementVariety   FROM ProcurementDetail p  where p.procurementProduct.code=:code";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createQuery(queryString);
		query.setParameter("code", code);
		List<ProcurementVariety> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	@Override
	public List<Object> listProcurementQtyByMoth(Date sDate, Date eDate) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"select year(proc.createdDate),month(proc.createdDate),SUM(pd.NetWeight) FROM Procurement proc INNER JOIN proc.procurementDetails pd  WHERE proc.createdDate between :sDate AND :eDate group by year(proc.createdDate),month(proc.createdDate)");
		query.setParameter("sDate", sDate).setParameter("eDate", eDate);
		List<Object> resultSet = query.list();
		return resultSet;
	}

	@Override
	public List<Object> listProcurementGroupByMoth(Integer year, Integer month) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"select year(proc.createdDate),month(proc.createdDate),proc.totalProVal,pd.NetWeight,proc.farmer.fpo as fpo FROM Procurement proc INNER JOIN proc.procurementDetails pd  WHERE Year(proc.createdDate)=:year AND Month(proc.createdDate)=:month group by proc.farmer.fpo");
		query.setParameter("year", year).setParameter("month", month);
		List<Object> resultSet = query.list();
		return resultSet;
	}

	@Override
	public List<Object> listSupplierProcurementGroupByMoth(Integer year, Integer month) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"select year(sp.createdDate),month(sp.createdDate),sp.totalProVal,spd.NetWeight,spd.farmer.fpo as fpo FROM SupplierProcurement sp INNER JOIN sp.supplierProcurementDetails spd  WHERE Year(sp.createdDate)=:year AND Month(sp.createdDate)=:month group by spd.farmer.fpo");
		query.setParameter("year", year).setParameter("month", month);
		List<Object> resultSet = query.list();
		return resultSet;
	}

	@Override
	public WarehouseProduct findWarehouseProductByAgentAndProductAndSeason(Long agent_id, Long productId,
			String seasonCode) {
		Object[] values = { agent_id, productId, seasonCode };
		return (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.agent.id = ? AND wp.product.id = ? AND wp.seasonCode=?", values);
	}

	@Override
	public WarehouseProduct findWarehouseProductbyIdAndSeasonCode(long warehouseId, long productID, String seasonCode) {
		Object[] values = { warehouseId, productID, seasonCode };
		return (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.warehouse.id = ? AND wp.product.id = ? AND wp.seasonCode=?", values);
	}

	@Override
	public List<AgroTransaction> listAgroTransactionByDistributionId(Long id) {
		return list("FROM AgroTransaction at WHERE at.distribution.id=? ORDER BY at.id ASC", id);
	}

	@Override
	public List<Object[]> listOfProcurementProductByStock() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT DISTINCT cw.procurementProduct.id,cw.procurementProduct.name from CityWarehouse cw )");
		return result;
	}

	public Object[] findTotalQtyAndAmt(String farmerName, String product, String warehouse, String branchIdParma,
			String ics, String village, String city, String fCooperative,String season, Date startDate, Date endDate) {

		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = null;
		Map<String, Object> params = new HashMap<String, Object>();
		// String hqlQuery = "select
		// sum(dd.quantity),sum(d.totalAmount),sum(d.paymentAmount) from
		// DistributionDetail dd inner join dd.distribution d where d.id is not
		// null";
		String hqlQuery = "select sum(ptd.netWeight),sum(ptd.totalPricepremium),sum(ptd.numberOfBags) from ProcurementTraceabilityDetails ptd inner join ptd.procurementTraceability pt where pt.id is not null";

		if (!StringUtil.isEmpty(farmerName) && farmerName != null) {
			hqlQuery += " AND pt.farmer.farmerId =:farmerId";
			params.put("farmerId", farmerName);
		}

		if (!StringUtil.isEmpty(product) && product != null) {
			hqlQuery += " AND ptd.procurementProduct.name =:product";
			params.put("product", product);
		}

		if (!StringUtil.isEmpty(warehouse) && warehouse != null) {
			hqlQuery += " AND pt.warehouse.code =:warehouse";
			params.put("warehouse", warehouse);
		}

		if (!StringUtil.isEmpty(branchIdParma) && branchIdParma != null) {
			hqlQuery += " AND pt.branchId =:branchId";
			params.put("branchId", branchIdParma);
		}

		if (!StringUtil.isEmpty(ics) && ics != null) {
			hqlQuery += " AND pt.farmer.icsName =:ics";
			params.put("ics", ics);
		}
		if (!StringUtil.isEmpty(village) && village != null) {
			hqlQuery += " AND pt.farmer.village.code =:village";
			params.put("village", village);
		}
		if (!StringUtil.isEmpty(city) && city != null) {
			hqlQuery += " AND pt.farmer.city.code =:city";
			params.put("city", city);
		}
		if (!StringUtil.isEmpty(fCooperative) && fCooperative != null) {
			hqlQuery += " AND pt.farmer.fpo =:fCooperative";
			params.put("fCooperative", fCooperative);
		}
		if (!StringUtil.isEmpty(startDate) && endDate != null) {
			hqlQuery += " AND pt.procurementDate BETWEEN :startDate AND :endDate";
			params.put("startDate", startDate);
			params.put("endDate", endDate);

		}
		hqlQuery+=" AND pt.season=:season";
		params.put("season", season);
		
		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);
		}

		return obj;
	}

	@Override
	public List<Object[]> listProductionCenterByTraceabilityStock() {
		List<Object[]> result = list(
				"SELECT DISTINCT ps.coOperative.id, ps.coOperative.name FROM ProcurementTraceabilityStock ps where ps.coOperative.typez=2");
		return result;
	}

	@Override
	public List<Object[]> listOfProcurementProductByTraceabilityStock() {
		List<Object[]> result = list(
				"SELECT DISTINCT ps.procurementProduct.id, ps.procurementProduct.name FROM ProcurementTraceabilityStock ps");
		return result;
	}

	@Override
	public Object[] findGradeNameAndVarietyByGradeCode(String grade) {
		Object[] result = (Object[]) find(
				"SELECT DISTINCT pg.name,pg.procurementVariety.name FROM ProcurementGrade pg where pg.code=?", grade);
		return result;
	}

	@Override
	public List<Object[]> listOfICSFromTraceabilityStock() {
		List<Object[]> result = list(
				"SELECT DISTINCT ps.ics,(SELECT DISTINCT fc.name from FarmCatalogue fc where fc.code=ps.ics) FROM ProcurementTraceabilityStock ps");
		return result;
	}

	@Override
	public List<Object[]> listFarmerByTraceabilityStock() {
		List<Object[]> result = list(
				"SELECT DISTINCT ps.farmer.farmerId,ps.farmer.firstName FROM ProcurementTraceabilityStockDetails ps");
		return result;
	}

	@Override
	public Object[] findTotalQtyAndAmtFromProcurementStock(String farmerName, String product, String warehouse,
			String branchIdParma, String village, String city, String fpo, String ics,String selectedSeason) {

		Session session = getSessionFactory().getCurrentSession();
		Object[] obj = null;
		Map<String, Object> params = new HashMap<String, Object>();
		// String hqlQuery = "select
		// sum(dd.quantity),sum(d.totalAmount),sum(d.paymentAmount) from
		// DistributionDetail dd inner join dd.distribution d where d.id is not
		// null";
		String hqlQuery = "select sum(ptd.totalstock),sum(ptd.totalNumberOfBags) from ProcurementTraceabilityStockDetails ptd inner join ptd.procurementTraceabilityStock pt where pt.id is not null";

		if (!StringUtil.isEmpty(farmerName) && farmerName != null) {
			hqlQuery += " AND ptd.farmer.farmerId =:farmerId";
			params.put("farmerId", farmerName);
		}

		if (!StringUtil.isEmpty(product) && product != null) {
			hqlQuery += " AND pt.procurementProduct.id =:product";
			params.put("product", Long.parseLong(product));
		}

		if (!StringUtil.isEmpty(warehouse) && warehouse != null) {
			hqlQuery += " AND pt.coOperative.id =:warehouse";
			params.put("warehouse", Long.parseLong(warehouse));
		}

		if (!StringUtil.isEmpty(branchIdParma) && branchIdParma != null) {
			hqlQuery += " AND ptd.branchId =:branchId";
			params.put("branchId", branchIdParma);
		}

		if (!StringUtil.isEmpty(city) && city != null) {
			hqlQuery += " AND ptd.farmer.city.id=:city";
			params.put("city", Long.parseLong(city));
		}

		if (!StringUtil.isEmpty(village) && village != null) {
			hqlQuery += " AND ptd.farmer.village.id=:village";
			params.put("village", Long.parseLong(village));
		}

		if (!StringUtil.isEmpty(fpo) && fpo != null) {
			hqlQuery += " AND ptd.farmer.fpo=:fpo";
			params.put("fpo", fpo);
		}

		if (!StringUtil.isEmpty(ics) && ics != null) {
			hqlQuery += " AND pt.ics=:ics";
			params.put("ics", ics);
		}
		
		if (!StringUtil.isEmpty(selectedSeason) && selectedSeason != null) {
			hqlQuery += " AND pt.season=:selectedSeason";
			params.put("selectedSeason", selectedSeason);
		}

		Query query = session.createQuery(hqlQuery);
		for (String str : query.getNamedParameters()) {
			query.setParameter(str, params.get(str));
		}
		if (!ObjectUtil.isEmpty(query) && query.list().size() > 0) {
			obj = (Object[]) query.list().get(0);
		}

		return obj;
	}

	@Override
	public List<Object[]> listOfFarmCrops(String branchId) {
		// TODO Auto-generated method stub
		if (!StringUtil.isEmpty(branchId)) {
			return list("SELECT DISTINCT pp.id,pp.name from ProcurementProduct pp  WHERE pp.branchId=?)", branchId);
		} else {
			return list("SELECT DISTINCT pp.id,pp.name from ProcurementProduct pp)");
		}
	}

	@Override
	public Warehouse findWarehouseByCode(String warehouseId, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM Warehouse p WHERE p.code = :code");
		query.setParameter("code", warehouseId);

		// Product productList = (Product) query.list();

		List<Warehouse> productList = query.list();

		Warehouse product = null;
		if (productList.size() > 0) {
			product = (Warehouse) productList.get(0);
		}

		session.flush();
		session.close();
		return product;

	}

	public List<Object[]> listOfVillageFromTraceability() {
		List<Object[]> result = list(
				"SELECT DISTINCT ps.village.id, ps.village.name FROM ProcurementTraceabilityStock ps");
		return result;
	}

	public List<Object[]> listOfcityFromTraceability() {
		List<Object[]> result = list("SELECT DISTINCT ps.city.id, ps.city.name FROM ProcurementTraceabilityStock ps");
		return result;
	}

	@Override
	public List<Object[]> listProcurementVarietyByCode(String code) {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT pv.code, pv.name FROM ProcurementVariety pv where pv.procurementProduct.code=?", code);
		return result;
	}

	@Override
	public List<Object[]> listOfICSFromHeapStock() {
		List<Object[]> result = list(
				"SELECT DISTINCT hd.ics,(SELECT DISTINCT fc.name from FarmCatalogue fc where fc.code=hd.ics) FROM HeapData hd");
		return result;
	}

	@Override
	public List<Object[]> listOfProcurementProductFromHeap() {
		List<Object[]> result = list(
				"SELECT DISTINCT ps.procurementProduct.id, ps.procurementProduct.name FROM HeapData ps");
		return result;
	}

	@Override
	public List<Object> listOfHeapName() {
		List<Object> result = list("SELECT DISTINCT ps.name FROM HeapData ps");
		return result;
	}

	@Override
	public List<Object[]> findSupplierProcurementDataByFilter(long selectedProduct, String selectedDate) {
		Session sessions = getSessionFactory().openSession();
		String queryString = "";
		List<Object[]> list = new ArrayList<>();
		if (!StringUtil.isEmpty(selectedProduct) && selectedProduct > 0L && !StringUtil.isEmpty(selectedDate)) {
			String[] dateSplit = selectedDate.split("-");
			Date sDate = DateUtil.convertStringToDate(dateSplit[0], DateUtil.DATE_FORMAT);
			Date eDate = DateUtil.convertStringToDate(dateSplit[1], DateUtil.DATE_FORMAT);

			queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.NetWeight),SUM(pd.subTotal) FROM SupplierProcurement p INNER JOIN p.supplierProcurementDetails pd WHERE pd.procurementProduct.id=:selectedProduct AND p.createdDate BETWEEN :sDate AND :eDate";
			Query query = sessions.createQuery(queryString);
			query.setParameter("selectedProduct", selectedProduct);
			query.setParameter("sDate", sDate);
			query.setParameter("eDate", eDate);

			list = query.list();

		} else if (!StringUtil.isEmpty(selectedProduct) && selectedProduct > 0L) {

			queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.NetWeight),SUM(pd.subTotal) FROM SupplierProcurement p INNER JOIN p.supplierProcurementDetails pd WHERE pd.procurementProduct.id=:selectedProduct";
			Query query = sessions.createQuery(queryString);
			query.setParameter("selectedProduct", selectedProduct);
			list = query.list();

		} else if (!StringUtil.isEmpty(selectedDate)) {

			String[] dateSplit = selectedDate.split("-");
			Date sDate = DateUtil.convertStringToDate(dateSplit[0], DateUtil.DATE_FORMAT);
			Date eDate = DateUtil.convertStringToDate(dateSplit[1], DateUtil.DATE_FORMAT);
			queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.NetWeight),SUM(pd.subTotal) FROM SupplierProcurement p INNER JOIN p.supplierProcurementDetails pd WHERE p.createdDate BETWEEN :sDate AND :eDate";
			Query query = sessions.createQuery(queryString);
			query.setParameter("sDate", sDate);
			query.setParameter("eDate", eDate);
			list = query.list();

		}
		sessions.flush();
		sessions.close();
		return list;
	}

	@Override
	public List<Object[]> findSupplierProcurementCummulativeData() {
		String queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.NetWeight),SUM(pd.subTotal) FROM SupplierProcurement p INNER JOIN p.supplierProcurementDetails pd";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	@Override
	public List<Object> listSupplierProcurementQtyByMoth(Date sDate, Date eDate) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"select year(proc.createdDate),month(proc.createdDate),SUM(pd.NetWeight) FROM SupplierProcurement proc INNER JOIN proc.supplierProcurementDetails pd  WHERE proc.createdDate between :sDate AND :eDate group by year(proc.createdDate),month(proc.createdDate)");
		query.setParameter("sDate", sDate).setParameter("eDate", eDate);
		List<Object> resultSet = query.list();
		return resultSet;
	}

	@Override
	public List<Object> listSupplierProcurementAmtByMoth(Date sDate, Date eDate) {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"select year(proc.createdDate),month(proc.createdDate),SUM(proc.totalProVal) FROM SupplierProcurement proc WHERE proc.createdDate between :sDate AND :eDate group by year(proc.createdDate),month(proc.createdDate)");
		query.setParameter("sDate", sDate).setParameter("eDate", eDate);
		List<Object> resultSet = query.list();
		return resultSet;
	}

	@Override
	public List<Object[]> listOfFarmCrops() {
		// TODO Auto-generated method stub
		  Session sessions = getSessionFactory().getCurrentSession();
		  Query query = sessions
					.createQuery("SELECT DISTINCT pp.id,pp.name from ProcurementProduct pp");
		  List<Object[]> list = query.list();
		  return list;	
	
	}

	@Override
	public List<Object[]> listOfGiningFromBale() {
		return list("SELECT DISTINCT bg.ginning.code,bg.ginning.name from BaleGeneration bg");
	}

	@Override
	public List<Object> listOfHeapFromBale() {

		return list(
				"SELECT DISTINCT bg.heap,(SELECT fc.name from FarmCatalogue fc where fc.code=bg.heap) from BaleGeneration bg");
	}

	@Override
	public ProcurementProduct findProcurementProductByCode(String productCode, String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM ProcurementProduct pp WHERE pp.code = :code");
		query.setParameter("code", productCode);

		List<ProcurementProduct> productList = query.list();

		ProcurementProduct product = null;
		if (productList.size() > 0) {
			product = (ProcurementProduct) productList.get(0);
		}

		session.flush();
		session.close();
		return product;

	}

	@Override
	public void removeProcurment(String id, String deleteStatus) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("update Procurement p set p.status=:deleteStatus where id=:proId");
		query.setParameter("deleteStatus", Integer.valueOf(deleteStatus));
		query.setParameter("proId", Long.valueOf(id));
		int result = query.executeUpdate();
	}

	@Override
	public List<Object[]> listCategoriesByWarehouseId(long warehouseId,String season) {

		return list("select distinct wp.product.subcategory.code,wp.product.subcategory.name  FROM WarehouseProduct wp WHERE wp.warehouse.id=? and wp.seasonCode=?", new Object[]{warehouseId,season});	
	}

	@Override
	public Double findAvailableStockByWarehouseIdAndProduct(String senderWarehouse, String product,String season) {
		Object[] args={Long.parseLong(senderWarehouse.toString()),Long.parseLong(product.toString()),season};
		return (Double)find("Select wp.stock from WarehouseProduct wp where wp.warehouse.id=? and wp.product.id=? and wp.seasonCode=?",args);
	}

	@Override
	public List<Object> listOfDistributionStockReceiptNo(long warehosueId) {
		return list("select ds.receiptNo FROM DistributionStock ds where ds.receiverWarehouse.id=? and ds.status=0",warehosueId);
	}
	
	@Override
	public List<Product> findProductByWarehouseIdAndSubCategoryCode(long warehouseId,String category,String season) {
		return list("select wp.product from WarehouseProduct wp left join wp.product.subcategory s where wp.warehouse.id=? and s.code=? and wp.seasonCode=? and wp.stock>0",new Object[]{warehouseId,category,season});
	}
	
	@Override
	public List<SubCategory> lisCategoryByWarehouseIdSeasonCodeAgent(long agentId, String seasonCode) {
		// TODO Auto-generated method stub
		Object[] values = { agentId, seasonCode };
		return list(
				"select distinct(wp.product.subcategory) FROM WarehouseProduct wp WHERE wp.agent.id = ? AND wp.seasonCode=? AND wp.stock > 0 ORDER BY wp.product.subcategory.name ASC",
				values);
	}

	@Override
	public List<Product> listofProductBySubCategoryAndSelectedAgent(String selectedCategory, long selectedAgent,String seasonCode) {
		// TODO Auto-generated method stub
		Object[] values = { selectedCategory, selectedAgent,seasonCode };
		return list("select distinct(wp.product) FROM WarehouseProduct wp WHERE  wp.product.subcategory.code=? AND wp.agent.id = ?  AND wp.seasonCode=? ORDER BY wp.product.name ASC",values);
	}

	@Override
	public List<AgroTransaction> listAgroTransactionByDistributionStockId(long id,String receiptNo,String txnType,String seasonCode) {
		// TODO Auto-generated method stub
	//	HttpSession httpSession = ReflectUtil.getCurrentHttpSession();
		  //HttpServletRequest request = ReflectUtil.getCurrentHttpRequest();
		//  String tenantId = !StringUtil.isEmpty(ISecurityFilter.DEFAULT_TENANT_ID) ? ISecurityFilter.DEFAULT_TENANT_ID : "";
		//  Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		  
		  Session sessions = getSessionFactory().getCurrentSession();
		  sessions.disableFilter(ISecurityFilter.BRANCH_FILTER);
		  Query query = sessions
					.createQuery("From AgroTransaction at WHERE at.distributionStock.id=:id AND at.receiptNo=:receiptNo AND at.txnType=:txnType AND at.seasonCode=:seasonCode ORDER BY at.id ASC");

		  query.setParameter("id", id);
		  query.setParameter("receiptNo", receiptNo);
		  query.setParameter("txnType", txnType);
		  query.setParameter("seasonCode", seasonCode);
			List<AgroTransaction> list = query.list();
			//sessions.flush();
			//sessions.close();
			return list;	
		
		
//			Object[] values = {id,receiptNo,txnType,seasonCode};
		//return list("From AgroTransaction at WHERE at.distributionStock.id=? AND at.receiptNo=? AND at.txnType=? AND at.seasonCode=? ORDER BY at.id ASC", new Object[]{id,receiptNo,txnType,seasonCode});
		
		
		
	}
	@Override
	public CropCalendarDetail findCropCalendarDetailById(Long id) {
		CropCalendarDetail CropCalendarDetail = (CropCalendarDetail) find("FROM CropCalendarDetail ccd WHERE ccd.id = ?",
				id);
		return CropCalendarDetail;
	}

	@Override
	public List<Object[]> listOfDistributionProductsByType(String pType) {
		return list("select distinct ds.product.id,ds.product.name FROM DistributionStockDetail ds where ds.distributionStock.txnType=?",pType);
	}

	@Override
	public Product findProuductByProudctNameSubCategoryIdAndManufacture(String selectedProductName, Long category,
			String manufacture) {
		Object[] values = { selectedProductName, category,manufacture};
		return (Product) find("FROM Product p Where p.name=? AND p.subcategory.id=? AND p.manufacture=?", values);
	}

	@Override
	public CropCalendar findCropCalendarById(long id) {
		// TODO Auto-generated method stub
		CropCalendar CropCalendar = (CropCalendar) find("FROM CropCalendar ccd WHERE ccd.id = ?",
				id);
		return CropCalendar;
	}

	@Override
	public ProcurementVariety findProcurementVarietyByCode(String code, String tenantId) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM ProcurementVariety pv WHERE pv.code = :code");
		query.setParameter("code", code);

		// Product productList = (Product) query.list();

		List<ProcurementVariety> varietyList = query.list();

		ProcurementVariety variety = null;
		if (varietyList.size() > 0) {
			variety = (ProcurementVariety) varietyList.get(0);
		}

		session.flush();
		session.close();
		return variety;
	}
	
	public List<ColdStorageStockTransfer> findColdStorageStockTransferByCustomerId(Long id) {
		return list("FROM ColdStorageStockTransfer cst WHERE cst.buyer.id = ?", id);
	}
	
	@Override
	public List<Object[]> findProcurementTraceabilityDataByFilter(Long selectedProduct, String selectedDate) {
		Session sessions = getSessionFactory().openSession();
		String queryString = "";
		List<Object[]> list = new ArrayList<>();
		if (!StringUtil.isEmpty(selectedProduct) && selectedProduct > 0L && !StringUtil.isEmpty(selectedDate)) {
			String[] dateSplit = selectedDate.split("-");
			Date sDate = DateUtil.convertStringToDate(dateSplit[0], DateUtil.DATE_FORMAT);
			Date eDate = DateUtil.convertStringToDate(dateSplit[1], DateUtil.DATE_FORMAT);

			queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.netWeight),SUM(pd.totalPricepremium) FROM ProcurementTraceability p INNER JOIN p.procurmentTraceabilityDetails pd WHERE pd.procurementProduct.id=:selectedProduct AND p.createdDate BETWEEN :sDate AND :eDate";
			Query query = sessions.createQuery(queryString);
			query.setParameter("selectedProduct", selectedProduct);
			query.setParameter("sDate", sDate);
			query.setParameter("eDate", eDate);

			list = query.list();

		} else if (!StringUtil.isEmpty(selectedProduct) && selectedProduct > 0L) {

			queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.netWeight),SUM(pd.totalPricepremium) FROM ProcurementTraceability p INNER JOIN p.procurmentTraceabilityDetails pd WHERE pd.procurementProduct.id=:selectedProduct";
			Query query = sessions.createQuery(queryString);
			query.setParameter("selectedProduct", selectedProduct);
			list = query.list();

		} else if (!StringUtil.isEmpty(selectedDate)) {

			String[] dateSplit = selectedDate.split("-");
			Date sDate = DateUtil.convertStringToDate(dateSplit[0], DateUtil.DATE_FORMAT);
			Date eDate = DateUtil.convertStringToDate(dateSplit[1], DateUtil.DATE_FORMAT);
			queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.netWeight),SUM(pd.totalPricepremium) FROM ProcurementTraceability p INNER JOIN p.procurmentTraceabilityDetails pd WHERE p.createdDate BETWEEN :sDate AND :eDate";
			Query query = sessions.createQuery(queryString);
			query.setParameter("sDate", sDate);
			query.setParameter("eDate", eDate);
			list = query.list();

		}
		sessions.flush();
		sessions.close();
		return list;
	}
	@Override
	public List<Object[]> findProcurementTraceabilityCummulativeData() {
		String queryString = "SELECT SUM(pd.numberOfBags),SUM(pd.netWeight),SUM(pd.totalPricepremium) FROM ProcurementTraceability p INNER JOIN p.procurmentTraceabilityDetails pd";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createQuery(queryString);
		List<Object[]> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}
	
	@Override
	public List<Object[]> listProcurmentGradeByVarityCode(String code) {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT pp.code, pp.name FROM ProcurementProduct pp where pp.ProcurementVariety.code=?", code);
		return result;
	}
}


