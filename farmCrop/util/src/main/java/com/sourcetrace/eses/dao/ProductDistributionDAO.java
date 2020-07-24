/*
 * ProductDistributionDAO.java
 * Copyright (c) 2015-2016, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
import com.sourcetrace.eses.entity.Profile;
import com.sourcetrace.eses.entity.Warehouse;
import com.sourcetrace.eses.entity.WarehouseProduct;
import com.sourcetrace.eses.entity.WarehouseProductDetail;
import com.sourcetrace.eses.filter.ISecurityFilter;
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
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
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
import com.sourcetrace.esesw.entity.profile.Vendor;
import com.sourcetrace.esesw.entity.profile.WarehousePayment;
import com.sourcetrace.esesw.entity.profile.WarehousePaymentDetails;
import com.sourcetrace.esesw.entity.profile.WarehouseStockReturn;
import com.sourcetrace.esesw.entity.txn.ESETxnStatus;


/**
 * The Class ProductDistributionDAO.
 */
@Repository
@Transactional
public class ProductDistributionDAO extends ESEDAO implements IProductDistributionDAO {
	@Autowired
	public ProductDistributionDAO(SessionFactory sessionFactory) {

		this.setSessionFactory(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findDistributionByRecNo(java.lang.String)
	 */
	public Distribution findDistributionByRecNo(String receiptNo) {

		Distribution distribution = (Distribution) find("FROM Distribution dn WHERE dn.agroTransaction.receiptNo = ?",
				receiptNo);

		return distribution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listDistributionDetail(long)
	 */
	public List<DistributionDetail> listDistributionDetail(long id) {

		return list("FROM DistributionDetail dd WHERE dd.distribution.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listDistributionDetail(long, int, int)
	 */
	public List<DistributionDetail> listDistributionDetail(long id, int startIndex, int limit) {

		Session session = getSessionFactory().openSession();
		String queryString = "select * FROM distribution_detail WHERE DISTRIBUTION_ID = '" + id + "' LIMIT "
				+ startIndex + "," + limit;
		Query query = session.createSQLQuery(queryString).addEntity(DistributionDetail.class);
		List<DistributionDetail> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementByRecNo(java.lang.String)
	 */
	public Procurement findProcurementByRecNo(String receiptNo) {

		Procurement procurement = (Procurement) find("FROM Procurement pt WHERE pt.agroTransaction.receiptNo = ?",
				receiptNo);

		return procurement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementProductByCode(java.lang.String)
	 */
	public ProcurementProduct findProcurementProductByCode(String productCode) {

		ProcurementProduct procurementProduct = (ProcurementProduct) find(
				"FROM ProcurementProduct ppt WHERE ppt.code = ?", productCode);

		return procurementProduct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementProductByCode(java.lang.String)
	 */
	public ProcurementVariety findProcurementVariertyByCode(String productCode) {

		ProcurementVariety procurementVariety = (ProcurementVariety) find(
				"FROM ProcurementVariety ppv WHERE ppv.code = ?", productCode);

		return procurementVariety;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementVariertyById (java.lang.Long)
	 */
	public ProcurementVariety findProcurementVariertyById(Long id) {

		ProcurementVariety procurementVariety = (ProcurementVariety) find(
				"FROM ProcurementVariety ppv WHERE ppv.id = ?", id);

		return procurementVariety;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementVariertyByName (java.lang.String)
	 */
	public ProcurementVariety findProcurementVariertyByName(String name) {

		ProcurementVariety procurementVariety = (ProcurementVariety) find(
				"FROM ProcurementVariety ppv WHERE ppv.name = ?", name);

		return procurementVariety;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementGradeById(java .lang.Long)
	 */
	public ProcurementGrade findProcurementGradeById(Long id) {

		ProcurementGrade procurementGrade = (ProcurementGrade) find("FROM ProcurementGrade pg WHERE pg.id=?", id);
		return procurementGrade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementGradeByName (java.lang.String)
	 */
	public ProcurementGrade findProcurementGradeByName(String name) {

		ProcurementGrade procurementGrade = (ProcurementGrade) find("FROM ProcurementGrade pg WHERE pg.name=?", name);
		return procurementGrade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementGradeByPrice (java.lang.Double)
	 */
	public ProcurementGrade findProcurementGradeByPrice(Double rate) {

		ProcurementGrade procurementGrade = (ProcurementGrade) find("FROM ProcurementGrade pg WHERE pg.price=?", rate);
		return procurementGrade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementGradeByCode (java.lang.String)
	 */
	public ProcurementGrade findProcurementGradeByCode(String productCode) {

		ProcurementGrade procurementGrade = (ProcurementGrade) find("FROM ProcurementGrade pg WHERE pg.code=?",
				productCode);
		return procurementGrade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findMTNTByReceiptNo(java.lang.String)
	 */
	public MTNT findMTNTByReceiptNo(String receiptNo) {

		MTNT mtnt = (MTNT) find("FROM MTNT mtnt WHERE mtnt.receiptNo = ?", receiptNo);

		return mtnt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listMTNTDetailList(long)
	 */
	public List<MTNTDetail> listMTNTDetailList(long mtntId) {

		return list("FROM MTNTDetail md WHERE md.mtnt.id = ?", mtntId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listMTNTDetailList(long, int, int)
	 */
	public List<MTNTDetail> listMTNTDetailList(long mtntId, int startIndex, int limit) {

		Session session = getSessionFactory().openSession();
		String queryString = "select * FROM mtnt_detail WHERE MTNT_ID = '" + mtntId + "' LIMIT " + startIndex + ","
				+ limit;
		Query query = session.createSQLQuery(queryString).addEntity(MTNTDetail.class);
		List<MTNTDetail> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findVillageWarehouseByVillageCode(long)
	 */
	public List<VillageWarehouse> findVillageWarehouseByVillageCode(long id) {

		return list("FROM VillageWarehouse vw WHERE vw.village.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findVillageWarehouseByVillageAndProduct(long, long)
	 */
	public VillageWarehouse findVillageWarehouseByVillageAndProduct(long villageId, long productId) {

		Object[] values = { villageId, productId };
		VillageWarehouse villageWarehouse = (VillageWarehouse) find(
				"FROM VillageWarehouse vw WHERE vw.village.id = ? AND vw.procurementProduct.id = ? AND vw.isDelete = 0",
				values);
		return villageWarehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * removeMTNTDetail(long)
	 */
	public void removeMTNTDetail(long id) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("DELETE MTNTDetail det WHERE det.mtnt.id = :id");
		query.setParameter("id", id);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * removeDistributionDetail(long)
	 */
	public void removeDistributionDetail(long id) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("DELETE DistributionDetail det WHERE det.distribution.id = :id");
		query.setParameter("id", id);
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * removeProcurementDetail(long)
	 */
	public void removeProcurementDetail(long procurementId) {

		String queryString = "DELETE FROM procurement_detail WHERE PROCUREMENT_ID =  '" + procurementId + "'";
		Session sessions = getSessionFactory().openSession();
		Query querys = sessions.createSQLQuery(queryString);
		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findVillageWarehouseByVillageCodeAndAgentId(long, java.lang.String)
	 */
	public List<VillageWarehouse> findVillageWarehouseByVillageCodeAndAgentId(long id, String agentId) {

		Object[] values = { id, agentId };
		return list(
				"FROM VillageWarehouse vw WHERE vw.village.id = ? AND vw.agentId = ? AND isDelete = 0 AND numberOfBags > 0 AND grossWeight> 0 ",
				values);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findVillageWarehouse(long, long, java.lang.String)
	 */
	public VillageWarehouse findVillageWarehouse(long villageId, long productId, String agentId) {

		Object[] values = { villageId, productId, agentId };
		VillageWarehouse villageWarehouse = (VillageWarehouse) find(
				"FROM VillageWarehouse vw WHERE vw.village.id = ? AND vw.procurementProduct.id = ? AND vw.agentId = ? AND vw.isDelete = 0",
				values);
		return villageWarehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * isPendingMTNTExistsForAgent(java.lang.String)
	 */
	public boolean isPendingMTNTExistsForAgent(String agentId) {

		return (listPendingMTNTExistsByAgentId(agentId).size() > 0) ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPendingMTNTExistsByAgentId(java.lang.String)
	 */
	public List<VillageWarehouse> listPendingMTNTExistsByAgentId(String agentId) {

		return list(
				"FROM VillageWarehouse vw WHERE vw.agentId = ? AND vw.numberOfBags > 0 AND vw.grossWeight> 0 AND vw.isDelete = 0",
				agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listVillageWarehouseForAgent(java.lang.String)
	 */
	public List<VillageWarehouse> listVillageWarehouseForAgent(String agentId) {

		return list("FROM VillageWarehouse vw WHERE vw.agentId = ? AND vw.isDelete = 0", agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * removePendingMTNTStockByAgentId(java.lang.String)
	 */
	public void removePendingMTNTStockByAgentId(String agentId) {

		// getHibernateTemplate().update("update VillageWarehouse vw set
		// vw.isDelete = 1 where vw.agentId=? and vw.isDelete = 0",
		// agentId);
		Session sessions = getSessionFactory().openSession();
		String queryStrings = "UPDATE VILLAGE_WAREHOUSE SET IS_DELETE=1 WHERE AGENT_ID = '" + agentId
				+ "' AND IS_DELETE=0";
		Query querys = sessions.createSQLQuery(queryStrings);

		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * isVillageMappindExistForDistributionAndProcurement(long)
	 */
	public String isVillageMappindExistForDistributionAndProcurement(long id) {

		List<Distribution> distributionList = list("FROM Distribution d WHERE d.village.id = ?", id);
		if (!ObjectUtil.isListEmpty(distributionList)) {
			return "distribution.exist";
		} else {
			List<Procurement> procurementList = list("FROM Procurement p WHERE p.village.id = ?", id);
			if (!ObjectUtil.isListEmpty(procurementList)) {
				return "procurement.exist";
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findVillageWarehouseStock(long, long, java.lang.String, long, double)
	 */
	public VillageWarehouse findVillageWarehouseStock(long villageId, long procurementProductId, String agentId,
			long numberOfBags, double grossWeight) {

		Object[] values = { villageId, procurementProductId, agentId, numberOfBags, grossWeight };
		VillageWarehouse villageWarehouse = (VillageWarehouse) find(
				"FROM VillageWarehouse vw WHERE vw.village.id = ? AND vw.procurementProduct.id = ? AND vw.agentId = ? AND vw.isDelete = 0 AND vw.numberOfBags >= ? AND vw.grossWeight >= ?",
				values);
		return villageWarehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPendingOfflineProcurementList(java.lang.String)
	 */
	public List<OfflineProcurement> listPendingOfflineProcurementList(String tenantID) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantID).openSession();
		List<OfflineProcurement> result = session.createQuery("FROM OfflineProcurement op WHERE op.statusCode=2")
				.list();
		session.flush();
		session.close();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listOfflineDistribution()
	 */
	public List<OfflineDistribution> listOfflineDistribution(String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		List<OfflineDistribution> result = session.createQuery("FROM OfflineDistribution od WHERE od.statusCode = 2")
				.list();

		session.flush();
		session.close();
		return result;

		// return list("FROM OfflineDistribution od WHERE od.statusCode = 2");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPendingOfflineMTNTAndMTNR()
	 */
	public List<OfflineMTNT> listPendingOfflineMTNTAndMTNR() {

		return list("From OfflineMTNT offlineMTNT WHERE offlineMTNT.statusCode=2");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findMTNByReceiptNoAndType(java.lang.String, int)
	 */
	public MTNT findMTNByReceiptNoAndType(String receiptNo, int type) {

		Object[] values = { receiptNo, type };
		MTNT mtnt = (MTNT) find("FROM MTNT mtnt WHERE mtnt.receiptNo = ? AND mtnt.type = ?", values);
		return mtnt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findDistributionByRecNoAndOperType(java.lang.String, int)
	 */
	public Distribution findDistributionByRecNoAndOperType(String receiptNo, int operationType) {

		Object[] values = { receiptNo, operationType };
		Distribution distribution = (Distribution) find(
				"FROM Distribution dn WHERE dn.agroTransaction.receiptNo = ? AND dn.agroTransaction.operType = ?",
				values);
		return distribution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementByRecNoAndOperType(java.lang.String, int,
	 * java.lang.String)
	 */
	public Procurement findProcurementByRecNoAndOperType(String receiptNo, int operationType, String tenantId) {

		/*
		 * Object[] values = { receiptNo, operationType }; Procurement
		 * procurement = (Procurement)
		 * find("FROM Procurement pt WHERE pt.agroTransaction.receiptNo = ? AND pt.agroTransaction.operType=?"
		 * , values); return procurement;
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM Procurement pt WHERE pt.agroTransaction.receiptNo = :receiptNo AND pt.agroTransaction.operType = :operationType");
		query.setParameter("receiptNo", receiptNo);
		query.setParameter("operationType", operationType);

		List<Procurement> procurementList = query.list();
		Procurement procurement = null;
		if (procurementList.size() > 0) {
			procurement = (Procurement) procurementList.get(0);
		}

		session.flush();
		session.close();
		return procurement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findMTNByReceiptNoTypeAndOperType(java.lang.String, int)
	 */
	public MTNT findMTNByReceiptNoTypeAndOperType(String receiptNo, int type) {

		Object[] values = { receiptNo, type };
		MTNT mtnt = (MTNT) find("FROM MTNT mtnt WHERE mtnt.receiptNo = ? AND mtnt.type = ? AND mtnt.operationType = 1",
				values);
		return mtnt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findOfflineDistributionById(long)
	 */
	public OfflineDistribution findOfflineDistributionById(long id) {

		OfflineDistribution offlineDistribution = (OfflineDistribution) find(
				"FROM OfflineDistribution odn WHERE odn.id = ?", id);
		return offlineDistribution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findOfflineProcurementById(long)
	 */
	public OfflineProcurement findOfflineProcurementById(long id) {

		OfflineProcurement offlineProcurement = (OfflineProcurement) find("FROM OfflineProcurement op WHERE op.id = ?",
				id);
		return offlineProcurement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findOfflineMTNTById(long)
	 */
	public OfflineMTNT findOfflineMTNTById(long id) {

		OfflineMTNT offlineMTNT = (OfflineMTNT) find("FROM OfflineMTNT omtnt WHERE omtnt.id = ?", id);
		return offlineMTNT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementProductByType(int)
	 */
	public List<ProcurementProduct> listProcurementProductByType(int type) {

		return list("FROM ProcurementProduct ppt WHERE ppt.type = ? ORDER BY ppt.name ASC", type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findAvailableStock(long, long)
	 */
	public WarehouseProduct findAvailableStock(long warehouseId, long productId) {

		Object[] values = { warehouseId, productId };
		WarehouseProduct warehouseProduct = (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.agent.id=null", values);
		return warehouseProduct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findAgroTxnByReceiptNo(java.lang.String)
	 */
	public List<AgroTransaction> findAgroTxnByReceiptNo(String receiptNo) {

		return list("FROM AgroTransaction at WHERE at.receiptNo = ? AND at.operType = 1 ", receiptNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementProductById(long)
	 */
	public ProcurementProduct findProcurementProductById(long id) {

		ProcurementProduct procurementProduct = (ProcurementProduct) find(
				"FROM ProcurementProduct ppt WHERE ppt.id = ?", id);

		return procurementProduct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listFarmerTransactionHistory (java.lang.String, java.lang.String,
	 * java.lang.String, int)
	 */
	public List<AgroTransaction> listFarmerTransactionHistory(String farmerId, String[] transactionArray, int limit) {

		Session session = getSessionFactory().openSession();
		String queryString = "FROM AgroTransaction agTran WHERE agTran.farmerId=:farmerId AND agTran.txnType IN (:transactionArray) AND agTran.profType=:profType ORDER BY agTran.id DESC ";
		Query query = session.createQuery(queryString).setParameter("farmerId", farmerId)
				.setParameterList("transactionArray", transactionArray).setParameter("profType", Profile.CLIENT)
				.setMaxResults(limit);
		List<AgroTransaction> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findTruckStockByTruckId(java.lang.String)
	 */
	public TruckStock findTruckStockByTruckId(String truckId) {

		TruckStock truckStock = (TruckStock) find("FROM TruckStock ts WHERE ts.truckId = ? AND ts.reason = null ",
				truckId);
		return truckStock;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findTruckStockById(java.lang .Long)
	 */
	public TruckStock findTruckStockById(Long id) {

		TruckStock truckStock = (TruckStock) find("FROM TruckStock ts WHERE ts.id = ? ", id);
		return truckStock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listSeasons ()
	 */
	public List<Season> listSeasons() {

		return list("FROM Season sn");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPaymentMode()
	 */
	public List<PaymentMode> listPaymentMode() {

		return list("FROM PaymentMode pm");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findGradeById (long)
	 */
	public GradeMaster findGradeById(long id) {

		GradeMaster gradeMaster = (GradeMaster) find("FROM GradeMaster gm WHERE gm.id = ?", id);
		return gradeMaster;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listGradeMaster()
	 */
	public List<GradeMaster> listGradeMaster() {

		return list("FROM GradeMaster gm ORDER BY gm.name");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listGradeMasterByRevisionNo (long)
	 */
	public List<GradeMaster> listGradeMasterByRevisionNo(long revisionNo) {

		return list("FROM GradeMaster gm WHERE gm.revisionNo>? ORDER BY gm.revisionNo DESC", revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findVillageWarehouse(long, long, java.lang.String, java.lang.String)
	 */
	public VillageWarehouse findVillageWarehouse(long villageId, long productId, String agentId, String qualityCode) {

		Object[] values = { villageId, productId, agentId, qualityCode };
		VillageWarehouse villageWarehouse = (VillageWarehouse) find(
				"FROM VillageWarehouse vw WHERE vw.village.id = ? AND vw.procurementProduct.id = ? AND vw.agentId = ? AND vw.quality = ? AND vw.isDelete = 0",
				values);
		return villageWarehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPaymentModeByCode(java .lang.String)
	 */
	public PaymentMode findPaymentModeByCode(String paymentType) {

		PaymentMode paymentMode = (PaymentMode) find("FROM PaymentMode pm WHERE pm.code = ? ", paymentType);
		return paymentMode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findSeasonBySeasonCode(java .lang.String)
	 */
	public Season findSeasonBySeasonCode(String seasonCode) {

		Season season = (Season) find("FROM Season sn WHERE sn.code = ? ", seasonCode);
		return season;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findTripSheetByCityDateChartNo (long, java.util.Date, java.lang.String)
	 */
	public TripSheet findTripSheetByCityDateChartNo(long cityId, Date date, String chartNo) {

		Object[] constraint = { cityId, date, chartNo };
		return (TripSheet) find(
				"SELECT ts FROM TripSheet ts INNER JOIN ts.city c WHERE c.id = ? AND ts.date = ? AND ts.chartNo = ?",
				constraint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findGradeByCode(java.lang. String)
	 */
	public GradeMaster findGradeByCode(String code) {

		return (GradeMaster) find("FROM GradeMaster gm WHERE gm.code = ?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementProduct()
	 */
	public List<ProcurementProduct> listProcurementProduct() {

		return list("FROM ProcurementProduct pp ORDER BY pp.name");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findGradePricingExist(long, long, long, long)
	 */
	public GradeMasterPricing findGradePricingExist(long cityId, long seasonId, long productId, long gradeMasterId) {

		Object[] values = { cityId, seasonId, productId, gradeMasterId };
		GradeMasterPricing pricing = (GradeMasterPricing) find(
				"FROM GradeMasterPricing gmp WHERE gmp.area.id = ? AND gmp.season.id = ? AND gmp.product.id = ? AND gmp.gradeMaster.id = ?",
				values);
		return pricing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPMTReceiptNumberByStatus (int)
	 */
	@SuppressWarnings("unchecked")
	public List<String> listPMTReceiptNumberByStatus(int status) {

		return list("Select mtntReceiptNumber From PMT pmt WHERE pmt.statusCode=?", status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPMTByReceiptNumber(java .lang.String, int)
	 */
	public PMT findPMTByReceiptNumber(String receiptNumber, int status) {

		Object[] values = { receiptNumber, status };
		return (PMT) find("From PMT pmt WHERE pmt.mtntReceiptNumber=? AND pmt.statusCode=?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCentralWarehouseByQuality (java.lang.String)
	 */
	public CityWarehouse findCentralWarehouseByQuality(String gradeCode) {

		return (CityWarehouse) find("From CityWarehouse cw WHERE cw.city=null and cw.quality=?", gradeCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPMTByReceiptNumber(java .lang.String)
	 */
	public PMT findPMTByReceiptNumber(String receiptNumber) {

		return (PMT) find("From PMT pmt WHERE pmt.mtntReceiptNumber=?", receiptNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findSeasonById (long)
	 */
	public Season findSeasonById(long id) {

		return (Season) find("From Season s WHERE s.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCityWarehouse(long, long, java.lang.String, java.lang.String)
	 */
	public CityWarehouse findCityWarehouse(long cityId, long productId, String agentId, String quality) {

		Object[] values = { cityId, productId, agentId, quality };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.city.id = ? AND cw.procurementProduct.id = ? AND cw.agentId = ? AND cw.quality = ? AND cw.isDelete = 0",
				values);
		return cityWarehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listGradeMasterPricingByCityIdSeasonIdProductId(long, long, long)
	 */
	public List<GradeMasterPricing> listGradeMasterPricingByCityIdSeasonIdProductId(long cityId, long seasonId,
			long productId) {

		return list(
				"Select gmp From GradeMasterPricing gmp INNER JOIN gmp.gradeMaster gm INNER JOIN gmp.product p INNER JOIN gmp.area c INNER JOIN gmp.season s WHERE c.id=? AND s.id=? AND p.id=?",
				new Object[] { cityId, seasonId, productId });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listMunicipalityFromCityWarehouse ()
	 */
	public List<Municipality> listMunicipalityFromCityWarehouse() {

		return list(
				"select cw.city From CityWarehouse cw WHERE cw.isDelete=0 AND (cw.numberOfBags>0 OR cw.grossWeight>0) GROUP BY cw.city");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listTripSheetByCityIdTransitStatus (long, int)
	 */
	public List<TripSheet> listTripSheetByCityIdTransitStatus(long cityId, int transitStatus) {

		return list("SELECT ts FROM TripSheet ts INNER JOIN ts.city c WHERE c.id=? AND ts.transitStatus=?",
				new Object[] { cityId, transitStatus });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listGradeInformationByTripSheetIdProductId(long, long) Return Object[] 0
	 * : grade master id, 1 : no of bags, 2 : gross weight 3 : tare weight, 4 :
	 * net weight, 5 : grade master code
	 */
	public List<Object[]> listGradeInformationByTripSheetIdProductId(long tripSheetId, long procurementProductId) {

		List<Object[]> result = new ArrayList<Object[]>();
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "SELECT gm.id ID,SUM(pd.NUMBER_OF_BAGS) NUMBER_OF_BAGS,SUM(pd.GROSS_WEIGHT) GROSS_WEIGHT,SUM(pd.TARE_WEIGHT) TARE_WEIGHT,SUM(pd.NET_WEIGHT) NET_WEIGHT, gm.CODE FROM grade_master gm "
				+ "LEFT JOIN procurement_detail pd ON pd.QUALITY=gm.CODE "
				+ "LEFT JOIN procurement_product pp ON pp.id = pd.PROCUREMENT_PRODUCT_ID "
				+ "LEFT JOIN procurement p ON p.ID = pd.PROCUREMENT_ID "
				+ "LEFT JOIN trip_sheet ts ON ts.ID = p.TRIP_SHEET_ID "
				+ "WHERE ts.ID = :tripSheetId AND pp.id = :procurementProductId " + "GROUP BY pd.QUALITY";
		Query query = session.createSQLQuery(queryString).setParameter("tripSheetId", tripSheetId)
				.setParameter("procurementProductId", procurementProductId);
		result = query.list();
		// session.flush();
		// session.close();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findTripSheetById(long)
	 */
	public TripSheet findTripSheetById(long id) {

		return (TripSheet) find("FROM TripSheet ts WHERE ts.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findAvailableStockByAgentId(long, long, long)
	 */
	public WarehouseProduct findAvailableStockByAgentId(long warehouseId, long productId, long agentId) {

		Object[] values = { warehouseId, productId, agentId };
		WarehouseProduct warehouseProduct = (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.agent.id=?", values);
		return warehouseProduct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findAgentAvailableStock(java .lang.String, long)
	 */
	public WarehouseProduct findAgentAvailableStock(String agentId, long productId) {

		Object[] bindValues = { agentId, productId, agentId };
		return (WarehouseProduct) find(
				"From WarehouseProduct wp WHERE wp.agent.profileId=? and wp.product.id=? and wp.warehouse.id =(SELECT Distinct w.refCooperative.id FROM Agent a INNER JOIN a.wareHouses w WHERE a.profileId=?)",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findWarehouseProductAvailableStock (java.lang.String, long)
	 */
	public WarehouseProduct findWarehouseProductAvailableStock(String agentId, long productId) {

		Session session = getSessionFactory().getCurrentSession();
		String queryString = "SELECT * FROM WAREHOUSE_PRODUCT WHERE WAREHOUSE_PRODUCT.WAREHOUSE_ID=(SELECT WAREHOUSE.ID FROM PROF INNER JOIN SERV_POINT ON PROF.SERVICE_POINT_ID=SERV_POINT.ID INNER JOIN WAREHOUSE ON WAREHOUSE.`CODE`=SERV_POINT.CODE WHERE PROF.PROF_ID='"
				+ agentId + "') AND WAREHOUSE_PRODUCT.PRODUCT_ID='" + productId
				+ "' AND  WAREHOUSE_PRODUCT.AGENT_ID IS NULL;";
		Query query = session.createSQLQuery(queryString).addEntity(WarehouseProduct.class);
		List<WarehouseProduct> list = query.list();
		// session.flush();
		// session.close();
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findMTNTReceiptNoByReceiverWarehouse(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<DMT> findMTNTReceiptNoByReceiverWarehouse(String selectedWarehouse) {

		return list("FROM DMT dmt WHERE dmt.receiverWarehouse.code = ? AND dmt.status='1'", selectedWarehouse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findMTNTDetails(long, java.lang.String)
	 */
	public DMT findMTNTDetails(String receiverWarehouseId, String selectedMTNTReceiptNo) {

		Object[] values = { receiverWarehouseId, selectedMTNTReceiptNo };
		DMT mtntDetails = (DMT) find("FROM DMT dmt WHERE dmt.receiverWarehouse.code=? AND dmt.mtntReceiptNo=?", values);

		return mtntDetails;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findMTNTProductDetails(long)
	 */
	public List<DMTDetail> findMTNTProductDetails(long dmtId) {

		return list("FROM DMTDetail dd WHERE dd.dmt.id=?", dmtId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findDMTByMTNTReceiptNo(java.lang.String)
	 */
	public DMT findDMTByMTNTReceiptNo(String selectedMTNTReceiptNo) {

		return (DMT) find("FROM DMT dmt where dmt.mtntReceiptNo=? AND dmt.status = '1'", selectedMTNTReceiptNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findDMTProductByProductId(long, long)
	 */
	public DMTDetail findDMTProductByProductId(long productId, long dmtId) {

		Object[] values = { productId, dmtId };
		return (DMTDetail) find("FROM DMTDetail dd where dd.product.id=? AND dd.dmt.id=?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * isAgentWarehouseProductStockExist (java.lang.String)
	 */
	public boolean isAgentWarehouseProductStockExist(String profileId) {

		Long warehoseProductCount = (Long) find(
				"SELECT Count(wp) From WarehouseProduct wp WHERE wp.agent.profileId=? AND wp.stock>0", profileId);
		if (warehoseProductCount > 0) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listCentralWarehouse()
	 */
	public List<Object[]> listCentralWarehouse() {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery(
				"SELECT cw.quality, SUM(cw.numberOfBags), SUM(cw.grossWeight) FROM CityWarehouse cw WHERE cw.city IS NULL AND cw.isDelete = 0 GROUP BY cw.quality");
		List list = query.list();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listDMTDetailProductList(long, long)
	 */
	public List<DMTDetail> listDMTDetailProductList(long productId, long dmtId) {

		Object[] values = { productId, dmtId };
		return list("FROM DMTDetail dd where dd.product.id=? AND dd.dmt.id=?", values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listWarehouseProductByAgentId (long)
	 */
	@SuppressWarnings("unchecked")
	public List<WarehouseProduct> listWarehouseProductByAgentId(long agentId) {

		return list("From WarehouseProduct wp WHERE wp.agent.id = ? ", agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listWarehouseProductByCityCode (java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<WarehouseProduct> listWarehouseProductByCityCode(String code) {

		return list("From WarehouseProduct wp WHERE wp.warehouse.code=?", code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listWarehouseProductByProductId (long)
	 */
	public List<WarehouseProduct> listWarehouseProductByProductId(long productId) {

		return list("From WarehouseProduct wp WHERE wp.product.id=?", productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPendingPaymentTxn()
	 */
	@SuppressWarnings("unchecked")
	public List<OfflinePayment> listPendingPaymentTxn() {

		return list("FROM OfflinePayment op WHERE op.statusCode = ?", ESETxnStatus.PENDING.ordinal());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listDMTDetail (long)
	 */
	@SuppressWarnings("unchecked")
	public List<DMTDetail> listDMTDetail(long id) {

		return list("FROM DMTDetail dd WHERE dd.dmt.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listSenderWarehouse()
	 */
	@SuppressWarnings("unchecked")
	public List<Warehouse> listSenderWarehouse() {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT * FROM WAREHOUSE W" + " INNER JOIN DMT D ON W.ID = D.SENDER_WAREHOUSE_ID"
				+ " WHERE D.STATUS= 1 OR D.STATUS= 2";
		Query query = session.createSQLQuery(queryString).addEntity(Warehouse.class);
		List<Warehouse> list = query.list();
		session.flush();
		session.close();
		if (!ObjectUtil.isListEmpty(list))
			return list;
		else
			return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listMTNRReceiverWarehouse()
	 */
	@SuppressWarnings("unchecked")
	public List<Warehouse> listMTNRReceiverWarehouse() {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT * FROM WAREHOUSE W" + " INNER JOIN DMT D ON W.ID = D.RECEIVER_WAREHOUSE_ID"
				+ " WHERE D.STATUS= 2";
		Query query = session.createSQLQuery(queryString).addEntity(Warehouse.class);
		List<Warehouse> list = query.list();
		session.flush();
		session.close();
		if (!ObjectUtil.isListEmpty(list))
			return list;
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listMTNTReceiverWarehouse()
	 */
	@SuppressWarnings("unchecked")
	public List<Warehouse> listMTNTReceiverWarehouse() {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT * FROM WAREHOUSE W" + " INNER JOIN DMT D ON W.ID = D.RECEIVER_WAREHOUSE_ID"
				+ " WHERE D.STATUS = 1 OR D.STATUS = 2";
		Query query = session.createSQLQuery(queryString).addEntity(Warehouse.class);
		List<Warehouse> list = query.list();
		session.flush();
		session.close();
		if (!ObjectUtil.isListEmpty(list))
			return list;
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listWarehouseProductForAgentWarehouse(long)
	 */
	public List<WarehouseProduct> listWarehouseProductForAgentWarehouse(long cityId) {

		return list("FROM WarehouseProduct wp WHERE wp.warehouse.city.id = ? AND wp.stock > 0 AND wp.agent is NULL",
				cityId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findMTNTByAgentWarehouse(long)
	 */
	public List<DMT> findMTNTByAgentWarehouse(long cityId) {

		List<DMT> dmtList = list(
				"FROM DMT dmt WHERE dmt.receiverWarehouse.code = (SELECT wh.code FROM Warehouse wh WHERE wh.city.id = ? ) AND dmt.status='1'",
				cityId);
		return dmtList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findDMTByMTNRReceiptNo(java .lang.String)
	 */
	public DMT findDMTByMTNRReceiptNo(String mtnrReceiptNo) {

		return (DMT) find("FROM DMT dmt where dmt.mtnrReceiptNo=?", mtnrReceiptNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listOfPMTNTReportbyPMTId(long, java.lang.String)
	 */
	/**
	 * List of pmtnt reportby pmt id.
	 * 
	 * @param id
	 *            the id
	 * @param tripsheetIds
	 *            the tripsheet ids
	 * @return the list< object[]>
	 */
	public List<Object[]> listOfPMTNTReportbyPMTId(long id, String tripsheetIds) {

		List<Object[]> result = new ArrayList<Object[]>();
		Session session = getSessionFactory().openSession();
		String queryString = "SELECT  pd.ID,ts.ID,ts.CHART_NO,pd.QUALITY,SUM(pd.NUMBER_OF_BAGS),"
				+ " SUM(pd.GROSS_WEIGHT) FROM pmt procurmentMT"
				+ " INNER JOIN pmt_ts_map conf ON conf.PMT_ID = procurmentMT.id INNER JOIN trip_sheet ts ON ts.ID = conf.TRIP_SHEET_ID"
				+ " INNER JOIN procurement p ON p.TRIP_SHEET_ID = ts.ID INNER JOIN procurement_detail pd ON pd.procurement_id = p.ID"
				+ " WHERE procurmentMT.id = :pmtId AND ts.ID in (" + tripsheetIds
				+ ")  GROUP BY ts.CHART_NO,pd.QUALITY";

		Query query = session.createSQLQuery(queryString).setParameter("pmtId", id);// .setParameter("tripSheetIds",
		// tripsheetIds);

		result = query.list();
		session.flush();
		session.close();
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listTripSheetsIdsByPMTId(long, int, int)
	 */
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
	public List<BigInteger> listTripSheetsIdsByPMTId(long id, int startIndex, int endIndex) {

		List<BigInteger> result = new ArrayList<BigInteger>();
		Session session = getSessionFactory().openSession();
		String queryString = "SELECT PTM.TRIP_SHEET_ID FROM PMT LEFT JOIN PMT_TS_MAP PTM ON PTM.PMT_ID = PMT.ID"
				+ " WHERE PMT.ID=:pmtId LIMIT " + startIndex + "," + endIndex;

		Query query = session.createSQLQuery(queryString).setParameter("pmtId", id);

		result = query.list();
		session.flush();
		session.close();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPMTById (java.lang.Long)
	 */
	/**
	 * Find pmt by id.
	 * 
	 * @param id
	 *            the id
	 * @return the pMT
	 */
	public PMT findPMTById(Long id) {

		PMT pmt = (PMT) find("FROM PMT pmt WHERE pmt.id=?", id);
		return pmt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementMTNT()
	 */
	public List<PMT> listProcurementMTNT() {

		return list("From PMT pmt WHERE pmt.statusCode='1'");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listDMTDetail (long, int, int)
	 */
	public List<DMTDetail> listDMTDetail(long id, String type, int startIndex, int limit) {

		Session session = getSessionFactory().openSession();
		String queryString = null;
		if ("dmtnt".equalsIgnoreCase(type)) {
			queryString = "select * FROM dmt_detail WHERE dmt_ID = '" + id + "' AND TRANSFERED_QTY >0 LIMIT "
					+ startIndex + "," + limit;
		} else if ("dmtnr".equalsIgnoreCase(type)) {
			queryString = "select * FROM dmt_detail WHERE dmt_ID = '" + id + "' AND RECEIVED_QTY >0 LIMIT " + startIndex
					+ "," + limit;
		}
		Query query = session.createSQLQuery(queryString).addEntity(DMTDetail.class);
		List<DMTDetail> list = query.list();
		session.flush();
		session.close();
		return list;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPMTByMTNRReceiptNumber (java.lang.String)
	 */
	public PMT findPMTByMTNRReceiptNumber(String mtnrReceiptNo) {

		return (PMT) find("From PMT pmt WHERE pmt.mtnrReceiptNumber=?", mtnrReceiptNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPricePatternByRevNoAndSeason (java.lang.Long, java.lang.String)
	 */
	public List<PricePattern> listPricePatternByRevNoAndSeason(Long revisionNo, String seasonCode) {

		Object[] values = { revisionNo, seasonCode };
		return list("FROM PricePattern pp WHERE pp.revisionNo > ? AND pp.season.code = ? ORDER BY pp.revisionNo DESC",
				values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findAgentProductDetails(long)
	 */
	public List<WarehouseProduct> findAgentProductDetails(long id) {

		return list("FROM WarehouseProduct wp WHERE wp.agent.id=? AND wp.stock > 0", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPricePatternById(long)
	 */
	public PricePattern findPricePatternById(long id) {

		return (PricePattern) find("FROM PricePattern pp WHERE pp.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPricePatternByName(java .lang.String)
	 */
	public PricePattern findPricePatternByName(String name) {

		return (PricePattern) find("FROM PricePattern pp WHERE pp.name=?", name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * removePricePatternDetail(long)
	 */
	public void removePricePatternDetail(long id) {

		Session session = getSessionFactory().openSession();
		Query query = session.createQuery("DELETE PricePatternDetail ppd WHERE ppd.pricePattern.id = :id");
		query.setParameter("id", id);
		int result = query.executeUpdate();
		session.flush();
		session.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPricePatternBySeasonProcurementProduct(long, long)
	 */
	public List<PricePattern> listPricePatternBySeasonProcurementProduct(long seasonId, long procurementProductId) {

		return (List<PricePattern>) list("FROM PricePattern pp WHERE pp.season.id=? AND pp.procurementProduct.id=?",
				new Object[] { seasonId, procurementProductId });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPricePatternDetailByPricePattern(long)
	 */
	public List<PricePatternDetail> listPricePatternDetailByPricePattern(long pricePatternId) {

		return (List<PricePatternDetail>) list("FROM PricePatternDetail ppd WHERE ppd.pricePattern.id=?",
				pricePatternId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findDistributionById(java. lang.Long)
	 */
	public Distribution findDistributionById(Long id) {

		Distribution distribution = (Distribution) find("FROM Distribution dt WHERE dt.id = ?", id);
		return distribution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPMTSenderWarehouse()
	 */
	@SuppressWarnings("unchecked")
	public List<Municipality> listPMTSenderCity() {

		return (List<Municipality>) (list(
				"SELECT c FROM PMT pmt INNER JOIN pmt.tripSheets ts INNER JOIN ts.city c GROUP BY c"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPMTReceiverCities()
	 */
	@SuppressWarnings("unchecked")
	public List<Municipality> listPMTReceiverCities() {

		return (List<Municipality>) (list(
				"SELECT c FROM PMT pmt INNER JOIN pmt.tripSheets ts INNER JOIN ts.city c WHERE pmt.statusCode!=1 GROUP BY c"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCooperativeAvailableStockByFieldStaff(java.lang.String, long)
	 */
	public WarehouseProduct findCooperativeAvailableStockByFieldStaff(String agentId, long productId) {

		Object[] bindValues = { agentId, productId };
		return (WarehouseProduct) find(
				"From WarehouseProduct wp WHERE wp.warehouse.id=(SELECT Distinct wh.refCooperative.id FROM Agent a INNER JOIN a.wareHouses wh WHERE  a.profileId=?) AND wp.product.id=? AND wp.agent=null",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findDistributionByReceiptNoTxnType (java.lang.String, java.lang.String)
	 */
	public Distribution findDistributionByReceiptNoTxnType(String receiptNo, String txnType, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM Distribution dn WHERE dn.agroTransaction.receiptNo = :receiptNo AND dn.agroTransaction.txnType= :txnType");
		query.setParameter("receiptNo", receiptNo);
		query.setParameter("txnType", txnType);

		// Distribution distributionList = (Distribution) query.list();

		List<Distribution> distributionList = query.list();
		Distribution distribution = null;
		if (distributionList.size() > 0) {
			distribution = (Distribution) distributionList.get(0);
		}

		session.flush();
		session.close();
		return distribution;

		/*
		 * Object[] bindValues = { receiptNo, txnType }; Distribution
		 * distribution = (Distribution) find(
		 * "FROM Distribution dn WHERE dn.agroTransaction.receiptNo = ? and dn.agroTransaction.txnType=?"
		 * , bindValues); return distribution;
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCityWarehouseByAgentId (java.lang.String)
	 */
	public List<CityWarehouse> findCityWarehouseByAgentId(String agentId) {

		return list(
				"FROM CityWarehouse cw WHERE cw.agentId = ? AND cw.coOperative is NULL AND cw.isDelete = 0 AND cw.grossWeight> 0 ",
				agentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listCityWarehouseByAgentIdRevisionNo(java.lang.String, long)
	 */
	public List<CityWarehouse> listCityWarehouseByAgentIdRevisionNo(String agentId, long revisionNo) {

		Session session = getSessionFactory().openSession();
		// String queryString = "SELECT * FROM `city_warehouse` WHERE `AGENT_ID`
		// = '" + agentId
		// + "' AND `CO_OPERATIVE_ID` IS NULL AND `IS_DELETE` = '0' AND
		// `REVISION_NO` > '" + revisionNo
		// + "' AND `VILLAGE_ID` IN ((SELECT VILLAGE_ID FROM
		// `warehouse_village_map` WHERE `WAREHOUSE_ID` IN ((SELECT WAREHOUSE_ID
		// FROM `agent_warehouse_map` WHERE `AGENT_ID` = (SELECT ID FROM `prof`
		// WHERE `PROF_ID` = '"
		// + agentId + "'))))) ORDER BY `REVISION_NO` DESC";

		String queryString1 = "FROM CityWarehouse cw WHERE cw.agentId=:agentId and cw.coOperative IS NULL AND cw.isDelete=0 AND cw.revisionNo > :revNo)";
		// Query query =
		// session.createSQLQuery(queryString).addEntity(CityWarehouse.class);
		Query query = session.createQuery(queryString1);
		query.setParameter("agentId", agentId);
		query.setParameter("revNo", revisionNo);
		List<CityWarehouse> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPricePattern()
	 */
	public List<PricePattern> listPricePattern() {

		return list("FROM PricePattern pp");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementStocksForAgent (java.lang.String)
	 */
	public List<CityWarehouse> listProcurementStocksForAgent(String agentId) {

		return (List<CityWarehouse>) (list(
				"FROM CityWarehouse cw WHERE cw.numberOfBags > 0 AND cw.grossWeight> 0  AND cw.agentId=?", agentId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCityWarehouseByVillage (long, long, java.lang.String,
	 * java.lang.String)
	 */
	public CityWarehouse findCityWarehouseByVillage(long villageId, long productId, String agentId, String quality) {

		Object[] values = { villageId, productId, agentId, quality };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.village.id = ? AND cw.procurementProduct.id = ? AND cw.agentId = ? AND cw.quality = ? AND cw.isDelete = 0",
				values);
		return cityWarehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCityWarehouseByCoOperative (long, long, java.lang.String,
	 * java.lang.String)
	 */
	public CityWarehouse findCityWarehouseByCoOperative(long coOperativeId, long productId, String agentId,
			String quality) {

		Object[] values = { coOperativeId, productId, quality };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.coOperative.id = ? AND cw.procurementProduct.id = ? AND cw.quality = ? AND cw.isDelete = 0",
				values);
		return cityWarehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCityWarehouseByCoOperative (long, long, java.lang.String)
	 */
	public CityWarehouse findCityWarehouseByCoOperative(long coOperativeId, long productId, String agentId) {

		Object[] values = { coOperativeId, productId, agentId };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.coOperative.id = ? AND cw.procurementProduct.id = ? AND cw.agentId = ? AND cw.isDelete = 0",
				values);
		return cityWarehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCityWarehouseByVillage (long, long, java.lang.String)
	 */
	public CityWarehouse findCityWarehouseByVillage(long villageId, long productId, String agentId) {

		Object[] values = { villageId, productId, agentId };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.village.id = ? AND cw.procurementProduct.id = ? AND cw.agentId = ? AND cw.isDelete = 0",
				values);
		return cityWarehouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPricePatternDetailByProductFarmerSeasonGrade(long, long, long,
	 * java.lang.String)
	 */
	public PricePatternDetail findPricePatternDetailByProductFarmerSeasonGrade(long farmerId, long seasonId,
			long procurementProductId, String gradeCode) {

		Object[] values = { farmerId, seasonId, procurementProductId, gradeCode };
		return (PricePatternDetail) find(
				"SELECT ppd FROM Contract c INNER JOIN c.pricePatterns pp INNER JOIN pp.pricePatternDetails ppd WHERE c.farmer.id=? AND c.season.id=? AND pp.procurementProduct.id=? AND ppd.gradeMaster.code=?",
				values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPricePatternDetailByProductSeason(long, long)
	 */
	public List<PricePatternDetail> listPricePatternDetailByProductSeason(long productId, long seasonId) {

		Object[] values = { productId, seasonId };
		/*
		 * return (List<PricePatternDetail>) list(
		 * "From PricePatternDetail ppd WHERE ppd.pricePattern.id = ? AND ppd.pricePattern.season.id=?"
		 * , values);
		 */
		return (List<PricePatternDetail>) list(
				"From PricePatternDetail ppd WHERE ppd.gradeMaster.product.id = ? AND ppd.pricePattern.season.id=?",
				values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCooperativeAvailableStockByCooperativeManager(java.lang.String, long)
	 */
	public WarehouseProduct findCooperativeAvailableStockByCooperativeManager(String agentId, long productId) {

		Object[] bindValues = { agentId, productId };
		return (WarehouseProduct) find(
				"From WarehouseProduct wp WHERE wp.warehouse.id=(SELECT Distinct wh.id FROM Agent a INNER JOIN a.wareHouses wh WHERE  a.profileId=?) AND wp.product.id=? AND wp.agent=null",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPricePatternDetailById (long)
	 */
	public PricePatternDetail findPricePatternDetailById(long id) {

		return (PricePatternDetail) find("FROM PricePatternDetail ppd WHERE ppd.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listGradeMasterByProcurementProductId(java.lang.Long)
	 */
	public List<GradeMaster> listGradeMasterByProcurementProductId(Long productId) {

		return list("FROM GradeMaster gm WHERE gm.product.id = ? ORDER BY gm.name", productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementProductByName (java.lang.String)
	 */
	public ProcurementProduct findProcurementProductByName(String name) {

		ProcurementProduct product = (ProcurementProduct) find("FROM ProcurementProduct pp WHERE pp.name = ?", name);
		return product;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findGradeByName(java.lang. String)
	 */
	public GradeMaster findGradeByName(String gradeName) {

		GradeMaster grade = (GradeMaster) find("FROM GradeMaster gm WHERE gm.name = ?", gradeName);
		return grade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findAgroTransactionByRecNoProfTypeTxnDescAndDate(java.lang.String,
	 * java.lang.String, java.lang.String, java.util.Date)
	 */
	public AgroTransaction findAgroTransactionByRecNoProfTypeTxnDescAndDate(String recNo, String profType,
			String txnDesc, Date date) {

		return (AgroTransaction) find(
				"FROM AgroTransaction at WHERE at.receiptNo=? AND at.profType=? AND at.txnDesc=? AND at.txnTime=?",
				new Object[] { recNo, profType, txnDesc, date });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPaymentAgroTransactionByRecNoProfType(java.lang.String,
	 * java.lang.String)
	 */
	public AgroTransaction findPaymentAgroTransactionByRecNoProfType(String recNo, String profType) {

		return (AgroTransaction) find(
				"FROM AgroTransaction at WHERE at.receiptNo=? AND at.profType=? AND (at.txnType=? OR at.txnType=?)",
				new Object[] { recNo, profType, PaymentMode.PROCURMENT_PAYMENT_TXN,
						PaymentMode.DISTIBUTION_PAYMENT_TXN });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementProductByRevisionNo (long)
	 */
	public List<ProcurementProduct> listProcurementProductByRevisionNo(long revisionNo) {

		return list("FROM ProcurementProduct pp WHERE pp.revisionNo>? ORDER BY pp.revisionNo DESC", revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findLogoImageById(java.lang .Long)
	 */
	public byte[] findLogoImageById(Long id) {

		return (byte[]) find("SELECT image FROM ESELogo logo WHERE logo.id = ?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listSeasonByRevisionNo(long)
	 */
	public List<Season> listSeasonByRevisionNo(long revisionNo) {

		return list("FROM Season sn WHERE sn.revisionNo>? ORDER BY sn.revisionNo DESC", revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProductsByCoOperative( long)
	 */
	public List<CityWarehouse> listProductsByCoOperative(long coOperativeId) {

		// TODO Auto-generated method stub
		return list("from CityWarehouse cw where cw.coOperative.id=?", coOperativeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCityWareHouseByGrade(java .lang.String, long, long, java.lang.String)
	 */
	public CityWarehouse findCityWareHouseByGrade(String agentId, long coOperativeId, long productId,
			String gradeCode) {

		// TODO Auto-generated method stub
		return (CityWarehouse) find(
				"from CityWarehouse cw where cw.agentId=? and cw.coOperative.id=? and cw.procurementProduct.id=? and cw.quality=?",
				new Object[] { agentId, coOperativeId, productId, gradeCode });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPrecurementProductStock (java.lang.String, long, long)
	 */
	public double findPrecurementProductStock(String agentId, long coOperativeId, long productId) {

		// TODO Auto-generated method stub
		long a = (Long) find(
				"select sum(cw.numberOfBags) from CityWarehouse cw where cw.agentId=? and cw.coOperative.id=? and cw.procurementProduct.id=?",
				new Object[] { agentId, coOperativeId, productId });
		return Double.valueOf(a);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPrecurementProductStockNetWht (java.lang.String, long, long)
	 */
	public double findPrecurementProductStockNetWht(String agentId, long coOperativeId, long productId) {

		double a = (Double) find(
				"select sum(cw.grossWeight) from CityWarehouse cw where cw.agentId=? and cw.coOperative.id=? and cw.procurementProduct.id=?",
				new Object[] { agentId, coOperativeId, productId });
		return a;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findDistributionByRecNoAndTxnType (java.lang.String, java.lang.String)
	 */
	public Distribution findDistributionByRecNoAndTxnType(String receiptNumber, String txnType) {

		Distribution distribution = (Distribution) find(
				"FROM Distribution dn WHERE dn.agroTransaction.receiptNo = ? AND dn.agroTransaction.txnType=?",
				new Object[] { receiptNumber, txnType });

		return distribution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findTxnAgroAudioFileByTxnAgroId (long)
	 */
	public byte[] findTxnAgroAudioFileByTxnAgroId(long id) {

		return (byte[]) find("SELECT at.audioFile FROM AgroTransaction at WHERE at.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findAgentMovementById(long)
	 */
	public AgentMovement findAgentMovementById(long id) {

		return (AgentMovement) find("FROM AgentMovement at WHERE at.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCooperativeAvailableStockByCooperative(long, long)
	 */
	public WarehouseProduct findCooperativeAvailableStockByCooperative(long warehouseId, long productId) {

		Object[] bindValues = { warehouseId, productId };
		return (WarehouseProduct) find(
				"From WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.agent=null", bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findAvailableStocks(java.lang .String, long)
	 */
	public WarehouseProduct findAvailableStocks(String warehouseCode, long productId) {

		Object[] values = { warehouseCode, productId };
		WarehouseProduct warehouseProduct = (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.warehouse.code=? AND wp.product.id=?", values);

		return warehouseProduct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findFieldStaffAvailableStock (java.lang.String, long)
	 */
	public WarehouseProduct findFieldStaffAvailableStock(String agentId, long productId) {

		Object[] bindValues = { agentId, productId };
		return (WarehouseProduct) find("From WarehouseProduct wp WHERE wp.agent.profileId=? and wp.product.id=?)",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementProductGradeByRevisionNo(java.lang.Long)
	 */
	public List<ProcurementGrade> listProcurementProductGradeByRevisionNo(Long revisionNo) {

		return list("FROM ProcurementGrade pg WHERE pg.revisionNo>? ORDER BY pg.revisionNo DESC", revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementProductVarietyByRevisionNo(java.lang.Long)
	 */
	public List<ProcurementVariety> listProcurementProductVarietyByRevisionNo(Long revisionNo) {

		return list("FROM ProcurementVariety pv WHERE pv.revisionNo>? ORDER BY pv.revisionNo DESC", revisionNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementProductVarietyByProcurementProductId(long, java.lang.Long)
	 */
	public List<ProcurementVariety> listProcurementProductVarietyByProcurementProductId(long id, Long revisionNo) {

		Object[] bindValues = { id, revisionNo };

		return list(
				"FROM ProcurementVariety pv WHERE  pv.procurementProduct.id=? AND pv.revisionNo>? ORDER BY pv.revisionNo DESC",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementProductGradeByProcurementProductVarietyId(java.lang.Long,
	 * java.lang.Long)
	 */
	public List<ProcurementGrade> listProcurementProductGradeByProcurementProductVarietyId(Long id, Long revisionNo) {

		Object[] bindValues = { id, revisionNo };

		return list(
				"FROM ProcurementGrade pg WHERE  pg.procurementVariety.id=? AND pg.revisionNo>? ORDER BY pg.revisionNo DESC",
				bindValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findProcurementVariertyByCropCode (java.lang.String)
	 */
	public List<ProcurementVariety> findProcurementVariertyByCropCode(String selectedCrop) {

		return list("FROM ProcurementVariety pv WHERE pv.procurementProduct.code= ? ORDER BY pv.name ASC",
				selectedCrop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementVarietyByProcurementProductId(java.lang.Long)
	 */
	public List<ProcurementVariety> listProcurementVarietyByProcurementProductId(Long id) {

		return list("FROM ProcurementVariety pv WHERE pv.procurementProduct.id=? ORDER BY pv.name ASC", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementGradeByProcurementVarietyId(java.lang.Long)
	 */
	public List<ProcurementGrade> listProcurementGradeByProcurementVarietyId(Long id) {

		return list("FROM ProcurementGrade pg WHERE pg.procurementVariety.id=? ORDER BY pg.name ASC", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listPriceByVarAndGrade(long, long, long)
	 */
	public ProcurementGrade listPriceByVarAndGrade(long procurementProductId, long procurementVarId,
			long procurementGradeId) {

		Object[] values = { procurementProductId, procurementVarId, procurementGradeId };
		return (ProcurementGrade) find(
				"From ProcurementGrade pg WHERE pg.procurementVariety.procurementProduct.id=?  AND  pg.procurementVariety.id=? AND pg.id=?",
				values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementVariety()
	 */
	@SuppressWarnings("unchecked")
	public List<ProcurementVariety> listProcurementVariety() {

		return list("FROM ProcurementVariety pv ORDER BY pv.name");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementGrade()
	 */
	@SuppressWarnings("unchecked")
	public List<ProcurementGrade> listProcurementGrade(int cooperative, int procurementproduct) {
		Object[] values = { cooperative, procurementproduct };

		return list(
				"SELECT pd.mtnrNumberOfBags, pd.mtnrGrossWeight,pd.procurementProduct,pd.coOperative,pg.name,pv.name,   FROM PMT pt INNER JOIN PMTDetail pd ON pd.PMT_ID = pt.ID INNER JOIN ProcurementGrade pg ON pd.PROCUREMENT_GRADE_ID = pg.ID INNER JOIN ProcurementVariety pv ON pg.PROCUREMENT_VARIETY_ID = pv.ID WHERE pd.COOPRATIVE_ID = ? AND pd.PROCUREMENT_PRODUCT_ID = ?",
				values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * listProcurementGradeByProcurementProductId(long)
	 */
	public List<ProcurementGrade> listProcurementGradeByProcurementProductId(long productId) {

		return list("FROM ProcurementGrade pg WHERE pg.procurementVariety.procurementProduct.id=?", productId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
	 * long, java.lang.String, long)
	 */
	public CityWarehouse findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(long warehouseId,
			String gradeCode, long productId) {

		Object[] values = { warehouseId, gradeCode, productId };
		return (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.coOperative.id=? AND cw.quality=? AND cw.procurementProduct.id=? ",
				values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.sourcetrace.eses.order.dao.service.IProductDistributionDAO#
	 * findPrecurementProductStockNetWhtByWarehouseIdAndProductId(long, long)
	 */
	public double findPrecurementProductStockNetWhtByWarehouseIdAndProductId(long warehouseId, long productId) {

		Object[] values = { warehouseId, productId };
		double a = (Double) find(
				"select sum(cw.grossWeight) from CityWarehouse cw where  cw.coOperative.id=? and cw.procurementProduct.id=?",
				values);
		return a;
	}

	public List<Vendor> listVendor() {

		return list("FROM Vendor ve ORDER BY ve.vendorName ASC");

	}

	public WarehousePayment findWarehouseStockByRecNo(String receiptNumber) {
		return (WarehousePayment) find("FROM WarehousePayment wp WHERE wp.receiptNo=?", receiptNumber);
	}

	/**
	 * List by all vendor names for warehouse payment
	 * 
	 * @return List
	 */

	@SuppressWarnings("unchecked")
	public List<WarehousePayment> selectVendorList() {
		// TODO Auto-generated method stub
		return list("FROM WarehousePayment w ORDER BY w.vendor.vendorName ASC");
	}

	/**
	 * List by all order No for warehousepayment
	 * 
	 * @return List
	 */

	@SuppressWarnings("unchecked")
	public List<WarehousePayment> selectOrderNoList() {
		// TODO Auto-generated method stub
		return list("FROM WarehousePayment wp  ORDER BY wp.orderNo ASC");
	}

	/**
	 * Find by vendorId and orderno for warehousepayment
	 * 
	 * @param selected
	 *            vendor Id
	 * @param selectedOrderNo
	 *            warehousepayment
	 */

	public WarehousePayment findVendorAndOrderNo(Long selectedVendor, String selectedOrderNo) {
		Object[] values = { selectedVendor, selectedOrderNo };
		return (WarehousePayment) find(
				"FROM WarehousePayment wp WHERE wp.vendor.id=? AND wp.orderNo=? AND wp.totalDamagedStock!='0' ",
				values);
	}

	/**
	 * Find by warehouse paymentId for warehouse payment details
	 * 
	 * @param payementId
	 */

	@SuppressWarnings("unchecked")
	public List<WarehousePaymentDetails> listWarehousePaymentDetails(long warehousePaymentId) {
		// TODO Auto-generated method stub
		return list("FROM WarehousePaymentDetails wpd WHERE wpd.warehousePayment.id=?", warehousePaymentId);
	}

	/**
	 * List by all warehouse names
	 * 
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public List<Warehouse> listWarehouse() {
		// TODO Auto-generated method stub
		return list("FROM Warehouse w");

	}

	/**
	 * Edit Warehouse Damaged Stock Quantity For Warehouse Payment
	 * 
	 * @param selectedOrderNo
	 * @param selectedVendor
	 * @param updateDamagedStock
	 */

	public void editWarehouseDamagedStock(String selectedOrderNo, String selectedVendor, long updateDamagedStock) {
		// TODO Auto-generated method stub

		/*
		 * String hql =
		 * "UPDATE FROM WAREHOUSE_PAYMENT wp SET TOTAL_DAMAGED_STOCK = :TOTAL_DAMAGED_STOCK "
		 * + "WHERE wb.vendor.id = : VENDOR_ID and ORDER_NO=: ORDER_NO"; Session
		 * session = getHibernateTemplate().getSessionFactory().openSession();
		 * Query query = session.createQuery(hql);
		 * query.setParameter("TOTAL_DAMAGED_STOCK", updateDamagedStock);
		 * query.setParameter("VENDOR_ID", selectedVendor);
		 * query.setParameter("ORDER_NO", selectedOrderNo); int result =
		 * query.executeUpdate();
		 */

		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery("UPDATE FROM WAREHOUSE_PAYMENT SET TOTAL_DAMAGED_STOCK="
				+ updateDamagedStock + " WHERE VENDOR_ID=" + selectedVendor + " AND ORDER_NO=" + selectedOrderNo + ",");
		query.executeUpdate();
		session.flush();
		session.close();

	}

	/**
	 * Find Warehouse Id and ProductId for Warehouse Payment Deatils
	 * 
	 * @param Warehouseid
	 * @param damagedProducts
	 * @return
	 */

	public WarehousePaymentDetails findWarehousePaymentIdAndProduct(long wareHouseId, String damagedProducts) {
		// TODO Auto-generated method stub
		Object[] values = { wareHouseId, Long.valueOf(damagedProducts) };
		return (WarehousePaymentDetails) find(
				"FROM WarehousePaymentDetails wp WHERE wp.warehousePayment.id=? AND wp.product.id=? AND wp.damagedStock!='0' ",
				values);
	}

	/**
	 * Edit Warehouse Damaged Stock Quantity for Warehouse Payment Details
	 */

	public void editWarehousePaymentDetails(long id, String damagedProducts, long damagedStockQty) {
		// TODO Auto-generated method stub
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery("UPDATE FROM WAREHOUSE_PAYMENT_DETAILS SET DAMAGED_STOCK="
				+ damagedStockQty + " WHERE WAREHOUSE_PAYMENT_ID=" + id + " AND PRODUCT_ID=" + damagedProducts + ",");
		query.executeUpdate();
		session.flush();
		session.close();

	}

	public Distribution findDistributionFarmerByRecNo(String receiptNumber) {
		return (Distribution) find("FROM Distribution d WHERE d.receiptNumber=?", receiptNumber);

	}

	public List<WarehousePayment> selectOrderNoList(long selectedVendor) {

		return list(
				"FROM WarehousePayment wp  WHERE wp.vendor.id=? AND wp.totalDamagedStock!='0' ORDER BY wp.orderNo ASC",
				selectedVendor);
	}

	public List<WarehousePayment> warehouseByvendorAndOrderNo(long selectedVendor, String selectedOrderNo) {

		Object[] values = { selectedVendor, selectedOrderNo };
		return list("FROM WarehousePayment wp  WHERE wp.vendor.id=? AND wp.orderNo=? ORDER BY wp.orderNo ASC", values);
	}

	public List<WarehousePayment> listAllWarehouse() {

		return list("FROM WarehousePayment w ORDER BY w.warehouse.code ASC");
	}

	public List<WarehousePayment> loadOrderNobasedOnVendorAndQty(long selectedVendor) {

		return list("FROM WarehousePayment wp  WHERE wp.vendor.id=? AND wp.totalGoodStock!='0' ORDER BY wp.orderNo ASC",
				selectedVendor);
	}

	public WarehousePayment warehousePaymentByVendorAndOrderNo(long selectedVendor, String selectedOrderNo) {

		Object[] values = { selectedVendor, selectedOrderNo };
		return (WarehousePayment) find("FROM WarehousePayment wp WHERE wp.vendor.id=? AND wp.orderNo=?", values);
	}

	public WarehousePaymentDetails findWarehousePaymentDetail(long id, String damagedProducts) {
		Object[] values = { id, Long.valueOf(damagedProducts) };
		return (WarehousePaymentDetails) find(
				"FROM WarehousePaymentDetails wp WHERE wp.warehousePayment.id=? AND wp.product.id=?", values);
	}

	public List<String> listStockReturnType() {
		// TODO Auto-generated method stub
		return list("FROM WarehouseStockReturn wsr GROUP BY wsr.returnType");

	}

	public List<String> listOfOrderNo() {
		// TODO Auto-generated method stub
		return list("FROM WarehouseStockReturn wsr GROUP BY wsr.orderNo");

	}

	public List<WarehouseProduct> listwarehouseProductByWarehouseId(long selectedWarehouseId) {
		// TODO Auto-generated method stub
		return list("FROM WarehouseProduct wp where wp.warehouse.id=?", selectedWarehouseId);
	}

	public WarehousePaymentDetails WarehousePaymentDetailsByWarehouseProductIds(long productId,
			long warehousePaymentId) {
		// TODO Auto-generated method stub
		Object[] values = { productId, Long.valueOf(warehousePaymentId) };
		return (WarehousePaymentDetails) find(
				"WarehousePaymentDetails wpd where wpd.product.id=? AND wpd.warehousePayment.id=?", values);
	}

	public Product findProductByProductId(long productId) {
		// TODO Auto-generated method stub
		return (Product) find("FROM Product p WHERE p.id=?", productId);
	}

	public WarehousePayment isOrderNoExists(String orderNo) {
		// TODO Auto-generated method stub
		return (WarehousePayment) find("FROM WarehousePayment wp WHERE wp.orderNo=?", orderNo);
	}

	public List<HarvestSeason> listHarvestSeason() {

		return list("from HarvestSeason");
	}

	public List<HarvestSeason> listHarvestSeasonByRevisionNo(Long revNo) {

		return list("FROM HarvestSeason sn WHERE sn.revisionNo>? ORDER BY sn.revisionNo DESC", revNo);
	}

	public List<FarmCatalogue> listCatalogueByRevisionNo(Long revNo) {
		Object[] values = { revNo, FarmCatalogue.ACTIVE };
		return list("FROM FarmCatalogue sn WHERE sn.revisionNo>? AND sn.status=? ORDER BY sn.revisionNo DESC", values);
	}

	public byte[] findLogoCultivateImageById(Long id) {

		return (byte[]) find("SELECT image FROM ESELogo logo WHERE logo.id = ?", id);
	}

	public List<Customer> listCustomerByRevisionNo(Long revNo) {

		return list("FROM Customer sn WHERE sn.revisionNo>? ORDER BY sn.revisionNo DESC", revNo);
	}

	@Override
	public Object findTotalYieldPriceByHarvestId(long harvestId) {
		// TODO Auto-generated method stub
		return find("SELECT SUM(chd.subTotal) FROM CropHarvestDetails chd WHERE  chd.cropHarvest.id=?", harvestId);
	}

	@Override
	public WarehouseProduct findAgentAvailableStock(long agentId, long prodId) {
		// TODO Auto-generated method stub
		Object[] values = { agentId, prodId };
		WarehouseProduct warehouseProduct = (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.agent.id=? AND wp.product.id=? AND wp.warehouse.id=null", values);
		return warehouseProduct;
	}

	public Boolean isProductDistributionExist(Long id) {
		Long distributionDetailCount = (Long) find(
				"SELECT Count(dd) From DistributionDetail dd WHERE dd.product.subcategory.id=?", id);
		if (distributionDetailCount > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CropHarvestDetails> listCropHarvestDetailsByProcurementProductId(Long id) {
		Object[] values = { id };
		return list("FROM CropHarvestDetails chd WHERE chd.variety.procurementProduct.id=?", values);
	}

	@Override
	public List<CropSupplyDetails> listCropSaleDetailsByProcurementProductId(Long id) {
		Object[] values = { id };
		return list("FROM CropSupplyDetails csd WHERE csd.variety.procurementProduct.id=?", values);
	}

	@Override
	public List<CropHarvestDetails> listCropHarvestDetailsByProcurementVarietyId(Long id) {
		Object[] values = { id };
		return list("FROM CropHarvestDetails chd WHERE chd.variety.id=?", values);
	}

	@Override
	public List<CropSupplyDetails> listCropSaleDetailsByProcurementVarietyId(Long id) {
		Object[] values = { id };
		return list("FROM CropSupplyDetails csd WHERE csd.variety.id=?", values);
	}

	@Override
	public List<CropHarvestDetails> listCropHarvestDetailsByProcurementGradeId(Long id) {
		Object[] values = { id };
		return list("FROM CropHarvestDetails chd WHERE chd.grade.id=?", values);
	}

	@Override
	public List<CropSupplyDetails> listCropSaleDetailsByProcurementGradeId(Long id) {
		Object[] values = { id };
		return list("FROM CropSupplyDetails csd WHERE csd.grade.id=?", values);
	}

	@Override
	public ProcurementVariety findProcurementVariertyByNameAndBranch(String varietyName, String branchId_F) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		org.hibernate.Filter branchFilter = session.getEnabledFilter(ISecurityFilter.BRANCH_FILTER);
		ProcurementVariety variety = null;
		if (!ObjectUtil.isEmpty(branchFilter)) {
			session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		}
		if (StringUtil.isEmpty(branchId_F)) {
			variety = (ProcurementVariety) find(
					"from ProcurementVariety var where var.name = ? AND var.procurementProduct.branchId IS NULL",
					varietyName);
		} else {

			variety = (ProcurementVariety) find(
					"from ProcurementVariety var where var.name = ? AND var.procurementProduct.branchId = ?",
					new Object[] { varietyName, branchId_F });
		}
		return variety;
	}

	@Override
	public ProcurementGrade findProcurementGradeByNameAndBranch(String gradeName, String branchId_F) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		org.hibernate.Filter branchFilter = session.getEnabledFilter(ISecurityFilter.BRANCH_FILTER);
		ProcurementGrade grade = null;
		if (!ObjectUtil.isEmpty(branchFilter)) {
			session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		}
		if (StringUtil.isEmpty(branchId_F)) {
			grade = (ProcurementGrade) find(
					"from ProcurementGrade gr where gr.name = ? AND gr.procurementVariety.procurementProduct.branchId IS NULL",
					gradeName);
		} else {

			grade = (ProcurementGrade) find(
					"from ProcurementGrade gr where gr.name = ? AND gr.procurementVariety.procurementProduct.branchId = ?",
					new Object[] { gradeName, branchId_F });
		}
		return grade;
	}

	@Override
	public ProcurementProduct findProcurementProductByNameAndBranch(String cropName, String branchId_F) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		org.hibernate.Filter branchFilter = session.getEnabledFilter(ISecurityFilter.BRANCH_FILTER);
		ProcurementProduct prod = null;
		if (!ObjectUtil.isEmpty(branchFilter)) {
			session.disableFilter(ISecurityFilter.BRANCH_FILTER);
		}
		if (StringUtil.isEmpty(branchId_F)) {
			prod = (ProcurementProduct) find("from ProcurementProduct pr where pr.name = ? AND pr.branchId IS NULL",
					cropName);
		} else {

			prod = (ProcurementProduct) find("from ProcurementProduct pr where pr.name = ? AND pr.branchId = ?",
					new Object[] { cropName, branchId_F });
		}
		return prod;
	}

	@Override
	public List<DistributionDetail> listDistributionDetailBySubCategory(Long id) {
		// TODO Auto-generated method stub
		Object[] values = { id };
		return list("FROM DistributionDetail dd where dd.product.subcategory.id=?", values);
	}

	public List<MasterData> listMasterDataByRevisionNo(Long revNo) {

		return list("FROM MasterData md WHERE md.revisionNo>? ORDER BY md.revisionNo DESC", revNo);
	}

	public List<MasterData> listMasterDataByMasterTypeAndRevisionNo(Long masterType, Long revNo) {
		return list("FROM MasterData md WHERE md.revisionNo>? and md.masterType = ? ORDER BY md.revisionNo DESC",
				new Object[] { revNo, String.valueOf(masterType) });
	}

	@Override
	public List<MasterData> listMandiTraderSupplier() {
		// TODO Auto-generated method stub
		return list("FROM MasterData md WHERE md.masterType='1'");
	}

	@Override
	public List<MasterData> listMandiAggregatorSupplier() {
		// TODO Auto-generated method stub
		return list("FROM MasterData md WHERE md.masterType='2'");
	}

	@Override
	public List<MasterData> listFarmAggregator() {
		// TODO Auto-generated method stub
		return list("FROM MasterData md WHERE md.masterType='3'");
	}

	@Override
	public List<MasterData> listSupplierFpo() {
		// TODO Auto-generated method stub
		return list("FROM MasterData md WHERE md.masterType='4'");
	}

	@Override
	public List<MasterData> listSupplierCig() {
		// TODO Auto-generated method stub
		return list("FROM MasterData md WHERE md.masterType='5'");
	}

	@Override
	public List<MasterData> listSupplierFig() {
		// TODO Auto-generated method stub
		return list("FROM MasterData md WHERE md.masterType='6'");
	}

	@Override
	public List<MasterData> listSupplierProducerImporter() {
		// TODO Auto-generated method stub
		return list("FROM MasterData md WHERE md.masterType='7'");
	}

	public Season findSeasonBySeasonCode(String seasonCode, String tenantId) {

		/*
		 * Season season = (Season) find("FROM Season sn WHERE sn.code = ? ",
		 * seasonCode); return season;
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM Season sn WHERE sn.code = :seasonCode");
		query.setParameter("seasonCode", seasonCode);

		List<Season> seasonList = query.list();
		Season season = null;
		if (seasonList.size() > 0) {
			season = (Season) seasonList.get(0);
		}

		session.flush();
		session.close();
		return season;
	}

	public ProcurementGrade findProcurementGradeByCode(String productCode, String tenantId) {

		/*
		 * ProcurementGrade procurementGrade = (ProcurementGrade)
		 * find("FROM ProcurementGrade pg WHERE pg.code=?", productCode); return
		 * procurementGrade;
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM ProcurementGrade pg left join fetch pg.procurementVariety pv left join fetch pv.procurementProduct pp WHERE pg.code =:productCode ");
		query.setParameter("productCode", productCode);

		List<ProcurementGrade> procurementGradeList = query.list();
		ProcurementGrade procurementGrade = null;
		if (procurementGradeList.size() > 0) {
			procurementGrade = (ProcurementGrade) procurementGradeList.get(0);
		}

		session.flush();
		session.close();
		return procurementGrade;
	}

	@Override
	public void saveAgroTransaction(AgroTransaction agroTransaction, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(agroTransaction);
		sessions.flush();
		sessions.close();
	}

	@Override
	public void saveProcurement(Procurement procurement, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(procurement);
		sessions.flush();
		sessions.close();
	}

	public CityWarehouse findCityWarehouseByCoOperative(long coOperativeId, long productId, String agentId,
			String quality, String tenantId) {

		/*
		 * Object[] values = { coOperativeId, productId, agentId, quality };
		 * CityWarehouse cityWarehouse = (CityWarehouse) find(
		 * "FROM CityWarehouse cw WHERE cw.coOperative.id = ? AND cw.procurementProduct.id = ? AND cw.agentId = ? AND cw.quality = ? AND cw.isDelete = 0"
		 * , values); return cityWarehouse;
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM CityWarehouse cw WHERE cw.coOperative.id = :coOperativeId AND cw.procurementProduct.id = :productId "
						+ " AND cw.quality = :quality  AND cw.agentId = :agentId AND cw.isDelete = 0");
		query.setParameter("coOperativeId", coOperativeId);
		query.setParameter("productId", productId);
		query.setParameter("agentId", agentId);
		query.setParameter("quality", quality);

		List<CityWarehouse> cityWarehouseList = query.list();
		CityWarehouse cityWarehouse = null;
		if (cityWarehouseList.size() > 0) {
			cityWarehouse = (CityWarehouse) cityWarehouseList.get(0);
		}

		session.flush();
		session.close();
		return cityWarehouse;
	}

	public CityWarehouse findCityWarehouseByVillage(long villageId, long productId, String agentId, String quality,
			String tenantId) {

		/*
		 * Object[] values = { villageId, productId, agentId, quality };
		 * CityWarehouse cityWarehouse = (CityWarehouse) find(
		 * "FROM CityWarehouse cw WHERE cw.village.id = ? AND cw.procurementProduct.id = ? AND cw.agentId = ? AND cw.quality = ? AND cw.isDelete = 0"
		 * , values); return cityWarehouse;
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM CityWarehouse cw WHERE cw.village.id = :villageId AND cw.procurementProduct.id = :productId "
						+ " AND cw.agentId = :agentId AND cw.quality = :quality AND cw.isDelete = 0");
		query.setParameter("villageId", villageId);
		query.setParameter("productId", productId);
		query.setParameter("agentId", agentId);
		query.setParameter("quality", quality);

		List<CityWarehouse> cityWarehouseList = query.list();
		CityWarehouse cityWarehouse = null;
		if (cityWarehouseList.size() > 0) {
			cityWarehouse = (CityWarehouse) cityWarehouseList.get(0);
		}

		session.flush();
		session.close();
		return cityWarehouse;
	}

	@Override
	public void saveCityWarehouseDetail(CityWarehouseDetail cityWarehouseDetail, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(cityWarehouseDetail);
		sessions.flush();
		sessions.close();
	}

	@Override
	public void saveOrUpdateCityWarehouse(CityWarehouse cityWarehouse, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.saveOrUpdate(cityWarehouse);
		sessions.flush();
		sessions.close();
	}

	public CityWarehouse findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(long warehouseId,
			String gradeCode, long productId, String tenantId) {

		/*
		 * Object[] values = { warehouseId, gradeCode, productId }; return
		 * (CityWarehouse) find(
		 * "FROM CityWarehouse cw WHERE cw.coOperative.id=? AND cw.quality=? AND cw.procurementProduct.id=? "
		 * , values);
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session
				.createQuery("FROM CityWarehouse cw WHERE cw.coOperative.id=:warehouseId AND cw.quality=:gradeCode AND "
						+ " cw.procurementProduct.id=:productId ");
		query.setParameter("warehouseId", warehouseId);
		query.setParameter("gradeCode", gradeCode);
		query.setParameter("productId", productId);

		List<CityWarehouse> cityWarehouseList = query.list();
		CityWarehouse cityWarehouse = null;
		if (cityWarehouseList.size() > 0) {
			cityWarehouse = (CityWarehouse) cityWarehouseList.get(0);
		}

		session.flush();
		session.close();
		return cityWarehouse;
	}

	@Override
	public void updateOfflineProcurement(OfflineProcurement offlineProcurement, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(offlineProcurement);
		sessions.flush();
		sessions.close();
	}

	public Season findSeasonBySeasonCodeByTenantId(String seasonCode, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM Season sn WHERE sn.code = :seasonCode");
		query.setParameter("seasonCode", seasonCode);

		// Season seasonList = (Season) query.list();

		List<Season> seasonList = query.list();
		Season season = null;
		if (seasonList.size() > 0) {
			season = (Season) seasonList.get(0);
		}

		session.flush();
		session.close();
		return season;
	}

	public WarehouseProduct findFieldStaffAvailableStockByTenantId(String agentId, long productId, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"From WarehouseProduct wp WHERE wp.agent.profileId = :agentId AND wp.product.id = :productId");
		query.setParameter("agentId", agentId);
		query.setParameter("productId", productId);

		// WarehouseProduct warehouseProductList = (WarehouseProduct)
		// query.list();

		List<WarehouseProduct> warehouseProductList = query.list();
		WarehouseProduct warehouseProduct = null;
		if (warehouseProductList.size() > 0) {
			warehouseProduct = (WarehouseProduct) warehouseProductList.get(0);
		}

		session.flush();
		session.close();
		return warehouseProduct;
	}

	@Override
	public void saveDistribution(Distribution distribution, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(distribution);
		sessions.flush();
		sessions.close();
	}

	public WarehouseProduct findAvailableStock(long warehouseId, long productId, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM WarehouseProduct wp WHERE wp.warehouse.id = :warehouseId AND wp.product.id :productId AND wp.agent.id=null");
		query.setParameter("warehouseId", warehouseId);
		query.setParameter("productId", productId);

		List<WarehouseProduct> warehouseProductList = query.list();
		WarehouseProduct warehouseProduct = null;
		if (warehouseProductList.size() > 0) {
			warehouseProduct = (WarehouseProduct) warehouseProductList.get(0);
		}
		session.flush();
		session.close();
		return warehouseProduct;

	}

	public WarehouseProduct findFieldStaffAvailableStock(String agentId, long productId, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"From WarehouseProduct wp WHERE wp.agent.profileId= :agentId AND wp.product.id= :productId");
		query.setParameter("agentId", agentId);
		query.setParameter("productId", productId);

		// WarehouseProduct warehouseProductList = (WarehouseProduct)
		// query.list();

		List<WarehouseProduct> warehouseProductList = query.list();
		WarehouseProduct warehouseProduct = null;
		if (warehouseProductList.size() > 0) {
			warehouseProduct = (WarehouseProduct) warehouseProductList.get(0);
		}

		session.flush();
		session.close();
		return warehouseProduct;

	}

	@Override
	public WarehouseProduct findAvailableStocks(String servicePointId, long id, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM WarehouseProduct wp WHERE wp.warehouse.code= :servicePointId AND wp.product.id= :id");
		query.setParameter("servicePointId", servicePointId);
		query.setParameter("id", id);

		List<WarehouseProduct> warehouseProductList = query.list();
		WarehouseProduct warehouseProduct = null;
		if (warehouseProductList.size() > 0) {
			warehouseProduct = (WarehouseProduct) warehouseProductList.get(0);
		}
		session.flush();
		session.close();
		return warehouseProduct;

	}

	@Override
	public void saveOrUpdate(Object warehouseProduct, String tenantId) {

		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.saveOrUpdate(warehouseProduct);
		sessions.flush();
		sessions.close();

	}

	@Override
	public WarehouseProduct findCooperativeAvailableStockByCooperative(long agentId, long productId, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"From WarehouseProduct wp WHERE wp.warehouse.id=(SELECT Distinct wh.id FROM Agent a INNER JOIN a.wareHouses wh WHERE  a.profileId=:agentId) AND wp.product.id= :productId AND wp.agent=null ");
		query.setParameter("agentId", agentId);
		query.setParameter("productId", productId);

		List<WarehouseProduct> warehouseProductList = query.list();
		WarehouseProduct warehouseProduct = null;
		if (warehouseProductList.size() > 0) {
			warehouseProduct = (WarehouseProduct) warehouseProductList.get(0);
		}
		session.flush();
		session.close();
		return warehouseProduct;

	}

	public WarehouseProduct findAgentAvailableStock(String agentId, long productId, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"From WarehouseProduct wp WHERE wp.agent.profileId= :agentId and wp.product.id= :productId and wp.warehouse.id =(SELECT Distinct w.refCooperative.id FROM Agent a INNER JOIN a.wareHouses w WHERE a.profileId= :agentId)");
		query.setParameter("agentId", agentId);
		query.setParameter("productId", productId);

		List<WarehouseProduct> warehouseProductList = query.list();
		WarehouseProduct warehouseProduct = null;
		if (warehouseProductList.size() > 0) {
			warehouseProduct = (WarehouseProduct) warehouseProductList.get(0);
		}
		session.flush();
		session.close();
		return warehouseProduct;

	}

	@Override
	public void updateOfflineDistribution(OfflineDistribution offlineDistribution, String tenantId) {

		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(offlineDistribution);
		sessions.flush();
		sessions.close();

	}

	@Override
	public Distribution findDistributionByReceiptNoTxnType(String receiptNo, String txnType) {
		Object[] bindValues = { receiptNo, txnType };
		Distribution distribution = (Distribution) find(
				"FROM Distribution dn WHERE dn.agroTransaction.receiptNo = ? and dn.agroTransaction.txnType=?",
				bindValues);
		return distribution;
	}

	@Override
	public void save(WarehouseProductDetail warehouseProductDetail, String tenantId) {

		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(warehouseProductDetail);
		sessions.flush();
		sessions.close();

	}

	@Override
	public List<ProcurementProduct> listFarmCrop() {

		return list("FROM ProcurementProduct pp ORDER BY pp.code ASC");
	}

	@Override
	public WarehouseStockReturn findStockReturnByRecNo(String receiptNo) {
		// TODO Auto-generated method stub
		return (WarehouseStockReturn) find("FROM WarehouseStockReturn wp WHERE wp.receiptNo=?", receiptNo);
	}

	@Override
	public void saveByTenantId(Object object, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(object);
		sessions.flush();
		sessions.close();

	}

	@Override
	public List<OfflineMTNT> listPendingOfflineMTNTAndMTNR(String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		List<OfflineMTNT> result = session.createQuery("From OfflineMTNT offlineMTNT WHERE offlineMTNT.statusCode=2")
				.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public MTNT findMTNByReceiptNoTypeAndOperType(String receiptNo, int type, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM MTNT mtnt WHERE mtnt.receiptNo = :receiptNo AND mtnt.type = :type AND mtnt.operationType = 1");
		query.setParameter("receiptNo", receiptNo);
		query.setParameter("type", type);

		List<MTNT> mtnts = query.list();
		MTNT mtnt = null;
		if (mtnts.size() > 0) {
			mtnt = (MTNT) mtnts.get(0);
		}
		session.flush();
		session.close();
		return mtnt;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findpmtdetail(int cooperative, int procurementproduct) {
		// Object[] values={cooperative,procurementproduct};

		// return list("SELECT pd.mtnrNumberOfBags,
		// pd.mtnrGrossWeight,pd.procurementProduct,pd.coOperative,pg.name,pv.name
		// FROM PMT pt INNER JOIN PMTDetail pd ON pd.PMT_ID = pt.ID INNER JOIN
		// ProcurementGrade pg ON pd.PROCUREMENT_GRADE_ID = pg.ID INNER JOIN
		// ProcurementVariety pv ON pg.PROCUREMENT_VARIETY_ID = pv.ID WHERE
		// pd.COOPRATIVE_ID = ? AND pd.PROCUREMENT_PRODUCT_ID = ?",values);
		String Query = "SELECT pv.name PRODUCT_NAME, pg.NAME PRODUCT_GRADE, pd.MTNR_NO_OF_BAGS PD_BAG, pd.MTNR_GROSS_WEIGHT PD_WEIGHT FROM PMT pt INNER JOIN PMT_Detail pd ON pd.PMT_ID = pt.ID "
				+ " INNER JOIN Procurement_Grade pg ON pd.PROCUREMENT_GRADE_ID = pg.ID "
				+ " INNER JOIN Procurement_Variety pv ON pg.PROCUREMENT_VARIETY_ID = pv.ID WHERE pd.COOPRATIVE_ID = :cooperative "
				+ " AND pd.PROCUREMENT_PRODUCT_ID = :procurementproduct";

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		return session.createSQLQuery(Query).addScalar("PRODUCT_NAME", StringType.INSTANCE)
				.addScalar("PRODUCT_GRADE", StringType.INSTANCE).addScalar("PD_BAG", StringType.INSTANCE)
				.addScalar("PD_WEIGHT", StringType.INSTANCE).setString("cooperative", String.valueOf(cooperative))
				.setString("procurementproduct", String.valueOf(procurementproduct)).list();
	}

	@SuppressWarnings("unchecked")
	public List<ProcurementGrade> listProcurementGrade() {

		return list("FROM ProcurementGrade pg ORDER BY pg.name");

	}

	@Override
	public void save(PMT pmt, String tenantId) {

		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(pmt);
		sessions.flush();
		sessions.close();
	}

	@Override
	public List<Object[]> listPMTReceiptNumberByWarehouseI(long warehouseId) {
		// TODO Auto-generated method stub
		return (List<Object[]>) list(
				"select pmtd.id, pmtd.pmt.mtntReceiptNumber from PMTDetail pmtd where pmtd.mtntNumberOfBags>0 AND pmtd.coOperative.id=?",
				warehouseId);
	}

	@Override
	public List<ProcurementProduct> listProcurementProductByReceiptNo(Long pmtdId) {
		// TODO Auto-generated method stub
		return list("SELECT pp FROM PMTDetail pmtd  INNER JOIN pmtd.procurementProduct pp  where pmtd.id=?", pmtdId);
	}

	@Override
	public PMTDetail findpmtdetailById(long id) {
		// TODO Auto-generated method stub
		return (PMTDetail) find("from PMTDetail pd where pd.id=?", id);
	}

	@Override
	public List<AgroTransaction> findAgroTxnByReceiptNo(String receiptNo, String tenantId) {

		// return list("FROM AgroTransaction at WHERE at.receiptNo = ? AND
		// at.operType = 1 ", receiptNo);
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session
				.createQuery("FROM AgroTransaction at WHERE at.receiptNo = :receiptNo AND at.operType = 1");
		query.setParameter("receiptNo", receiptNo);
		List<AgroTransaction> agroTransactionList = query.list();
		session.flush();
		session.close();
		return agroTransactionList;
	}

	@Override
	public PaymentMode findPaymentModeByCode(String paymentType, String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM PaymentMode pm WHERE pm.code = :pType");
		query.setParameter("pType", paymentType);
		List<PaymentMode> paymentList = query.list();
		PaymentMode paymentMode = null;
		if (paymentList.size() > 0) {
			paymentMode = (PaymentMode) paymentList.get(0);
		}
		session.flush();
		session.close();
		return paymentMode;
	}

	@Override
	public List<OfflinePayment> listPendingPaymentTxn(String tenantId) {
		// return list("FROM OfflinePayment op WHERE op.statusCode = ?",
		// ESETxnStatus.PENDING.ordinal());
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM OfflinePayment op WHERE op.statusCode = :status");
		query.setParameter("status", ESETxnStatus.PENDING.ordinal());
		List<OfflinePayment> payment = query.list();
		session.flush();
		session.close();
		return payment;
	}

	@Override
	public CityWarehouse findCityWarehouseBySamithi(long samithiId, long productId, String quality) {

		Object[] values = { samithiId, productId, quality };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.coOperative.id = ? AND cw.procurementProduct.id = ? AND cw.quality = ? AND cw.isDelete = 0",
				values);
		return cityWarehouse;
	}

	@Override
	public CityWarehouse findCityWarehouseByVillageId(long villageId, long productId, String quality) {

		Object[] values = { villageId, productId, quality };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.village.id = ? AND cw.procurementProduct.id = ? AND cw.quality = ? AND cw.isDelete = 0",
				values);
		return cityWarehouse;
	}

	public List<CityWarehouse> listFarmersByCoOperative(long coOperativeId) {

		return list("from CityWarehouse cw where cw.coOperative.id=?", coOperativeId);
	}

	public double findFarmerStockNetWgtByWarehouseIdAndFarmerId(long warehouseId, String farmerId) {

		Object[] values = { warehouseId, farmerId };
		double a = (Double) find(
				"select sum(cw.grossWeight) from CityWarehouse cw where  cw.coOperative.id=? and cw.farmer.farmerId=?",
				values);
		return a;
	}

	public List<CityWarehouse> listProductsByFarmerIdAndCooperativeId(long farmerId, long coOperativeId) {

		return (List<CityWarehouse>) list(
				"from CityWarehouse cw where cw.farmer.id=? AND cw.coOperative.id=? AND cw.grossWeight!=0",
				new Object[] { farmerId, coOperativeId });
	}

	public List<Procurement> findProcurementByFarmerId(long farmerId) {

		return list("from Procurement pt WHERE pt.farmer.id=?", farmerId);
	}

	public List<ProcurementDetail> findProcurementDetailByProcurementId(long procurementId) {

		return list("from ProcurementDetail pd WHERE pd.procurement.id=?", procurementId);

	}

	public ProcurementGrade findProcurementGradeByProcurementGradeId(Long id) {

		ProcurementGrade procurementGrade = (ProcurementGrade) find("FROM ProcurementGrade pg WHERE pg.id=?", id);

		return procurementGrade;
	}

	public List<CityWarehouseDetail> listCityWarehouseDetailsByCityWarehouseId(long cityWarehouseId) {

		// TODO Auto-generated method stub
		return list("from CityWarehouseDetail cwd where cw.cityWarehouse.id=?", cityWarehouseId);
	}

	public CityWarehouse findCityWarehouseByCoOperativeAndFarmer(long coOperativeId, long productId, long farmerId,
			String quality) {

		Object[] values = { coOperativeId, productId, farmerId, quality };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.coOperative.id = ? AND cw.procurementProduct.id = ? AND cw.farmer.id = ? AND cw.quality = ? AND cw.isDelete = 0",
				values);
		return cityWarehouse;
	}

	public CityWarehouse findCityWarehouseByVillageAndFarmer(long villageId, long productId, long farmerId,
			String quality) {

		Object[] values = { villageId, productId, farmerId, quality };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.village.id = ? AND cw.procurementProduct.id = ? AND cw.farmerId = ? AND cw.quality = ? AND cw.isDelete = 0",
				values);
		return cityWarehouse;
	}

	@Override
	public List<ProcurementGrade> listProcurementVarietyByGradeCode(String gradeCode) {

		return list("from ProcurementGrade pg where pg.code=?", gradeCode);
	}

	@Override
	public List<CityWarehouse> listProductsByProductId(long coOperativeId, long farmerId, long productId) {

		return (List<CityWarehouse>) list(
				"from CityWarehouse cw where cw.coOperative.id=? AND cw.farmer.id=? AND cw.procurementProduct.id=? ",
				new Object[] { coOperativeId, farmerId, productId });

		// return list("from CityWarehouse cw where cw.procurementProduct.id=?",
		// productId);
	}

	public CityWarehouse listStockByFarmerIdProductIdGradeCodeAndCooperativeId(long cooperativeId, long farmerId,
			long procurementProductId, String gradeCode) {

		Object[] values = { cooperativeId, farmerId, procurementProductId, gradeCode };
		return (CityWarehouse) find(
				"From CityWarehouse cw WHERE cw.coOperative.id =? AND cw.farmer.id = ? AND cw.procurementProduct.id = ? AND cw.quality=?",
				values);
	}

	public ProcurementVariety findProcurementVariertyByProductCode(String productCode) {

		ProcurementVariety procurementVariety = (ProcurementVariety) find(
				"FROM ProcurementVariety ppv WHERE ppv.procurementProduct.code = ?", productCode);

		return procurementVariety;
	}

	@Override
	public CityWarehouse listCityWarehouseProductsByProductCode(String productCode) {

		Object[] values = { productCode };
		return (CityWarehouse) find("from CityWarehouse cw where cw.procurementProduct.code=?", values);

	}

	@Override
	public CityWarehouse listCityWareHouseByCooperativeIdFarmerIdAndProductId(long farmerId, long procurementProductId,
			long cooperativeId) {

		Object[] values = { farmerId, procurementProductId, cooperativeId };
		return (CityWarehouse) find(
				"From CityWarehouse cw WHERE cw.farmer.id = ? AND cw.procurementProduct.id = ? AND cw.coOperative.id =?",
				values);
	}

	public CityWarehouse findCityWareHouseByWarehouseIdProcrmentGradeCodeAndProcurementProductIdAndFarmerId(
			long farmerId, long warehouseId, String gradeCode, long productId) {

		Object[] values = { farmerId, warehouseId, gradeCode, productId };
		return (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.farmer.id=? AND cw.coOperative.id=? AND cw.quality=? AND cw.procurementProduct.id=? ",
				values);
	}

	@Override
	public Procurement findProcurementById(Long id) {
		// TODO Auto-generated method stub
		Procurement procurement = (Procurement) find("FROM Procurement pt WHERE pt.id = ?", id);

		return procurement;
	}

	@Override
	public List<ProcurementVariety> listProcurementProductVarietyByvarietyCode(String code) {

		return list("FROM ProcurementVariety pv WHERE pv.code=?", code);
	}

	@Override
	public List<ProcurementProduct> listProcurementProductByProductId(long id) {

		return list("FROM ProcurementProduct ppt WHERE ppt.id = ?", id);
	}

	@Override
	public List<ProcurementVariety> listProcurementProductVarietyByVarietyId(long id) {

		return list("FROM ProcurementVariety pv WHERE pv.id=?", id);
	}

	public HarvestSeason findHarvestSeasonBySeasonCode(String seasonCode) {

		HarvestSeason harvestSeason = (HarvestSeason) find("FROM HarvestSeason hsn WHERE hsn.code = ? ", seasonCode);
		return harvestSeason;
	}

	public Object findNoOfBagByByWarehouseIdProductIdAndFarmerId(Object warehouseId, Object productId,
			Object farmerId) {

		Object[] values = { warehouseId, productId, farmerId };
		return find(
				"SELECT SUM(cw.numberOfBags) FROM CityWarehouse cw WHERE cw.coOperative.id=? AND cw.procurementProduct.id=? AND cw.farmer.id=? ",
				values);
	}

	public Object findNetWeightByWarehouseIdProductIdAndFarmerId(Object warehouseId, Object productId,
			Object farmerId) {

		Object[] values = { warehouseId, productId, farmerId };
		return find(
				"SELECT SUM(cw.grossWeight) FROM CityWarehouse cw WHERE cw.coOperative.id=? AND cw.procurementProduct.id=? AND cw.farmer.id=? ",
				values);
	}

	@Override
	public List<CityWarehouse> listGradeByWarehouseIdFarmerIdProductIdAndVarietyCode(long coOperativeId, long farmerId,
			long productId, String varietyCode) {

		return (List<CityWarehouse>) list(
				"from CityWarehouse cw where cw.coOperative.id=? AND cw.farmer.id=? AND cw.procurementProduct.id=? AND cw.quality=? ",
				new Object[] { coOperativeId, farmerId, productId, varietyCode });

		// return list("from CityWarehouse cw where cw.procurementProduct.id=?",
		// productId);
	}

	@Override
	public CityWarehouse findByProdIdAndGradeCode(long prodId, String gradeCode, String tenantID) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantID).openSession();
		Query query = session.createQuery("FROM CityWarehouse cw WHERE cw.procurementProduct.id=" + prodId
				+ " AND cw.quality='" + gradeCode + "'");
		List list = query.list();
		CityWarehouse cityWarehouse = null;
		if (list.size() > 0) {
			cityWarehouse = (CityWarehouse) list.get(list.size() - 1);
		}
		session.flush();
		session.close();
		return cityWarehouse;
	}

	@Override
	public ProcurementDetail findByProcurementDetailId(Long procuDetailId) {
		ProcurementDetail procurementDetail = (ProcurementDetail) find("FROM ProcurementDetail pd WHERE pd.id = ?",
				procuDetailId);
		return procurementDetail;
	}

	@Override
	public List<CityWarehouse> listProductsByFarmerId(long farmerId) {

		return (List<CityWarehouse>) list("from CityWarehouse cw where cw.farmer.id=? AND cw.coOperative.id=?",
				farmerId);
	}

	@Override
	public List<WarehouseProductDetail> findWarehouseproductDetailByWarehouseproductIdAndReceiptNo(long id,
			String receiptNumber) {
		Object[] values = { id, receiptNumber };
		// TODO Auto-generated method stub
		return list("FROM WarehouseProductDetail wd WHERE wd.warehouseProduct.id=? AND wd.receiptNo=?", values);
	}

	@Override
	public DistributionDetail findDistributionDetailById(Long id) {
		// TODO Auto-generated method stub
		return (DistributionDetail) find("FROM DistributionDetail dd left join fetch dd.distribution where dd.id=?",
				id);
	}

	@Override
	public List<Object[]> findWarehouseProductAvailableStockByAgentIdProductId(long id, String agentId) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery(
				"SELECT wp.STOCK as stock,wp.ID as wpId FROM warehouse_product wp INNER JOIN product p ON p.ID = wp.PRODUCT_ID WHERE p.ID = "
						+ id + " AND wp.AGENT_ID = (SELECT ID    FROM prof pf WHERE pf.PROF_ID =" + agentId + ")");
		List<Object[]> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public void updateStockById(Long warehouseProductId, String currentQuantity) {
		// TODO Auto-generated method stub
		Double currentStock = Double.valueOf(currentQuantity);
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery(
				"UPDATE warehouse_product wp set wp.STOCK=" + currentStock + " WHERE wp.ID=" + warehouseProductId + "");
		query.executeUpdate();
		session.flush();
		session.close();

	}

	@Override
	public String findPrefernceByName(String enableApproved) {
		// TODO Auto-generated method stub
		String queryString = "SELECT VAL FROM pref WHERE pref.NAME ='" + enableApproved + "' ";
		Session sessions = getSessionFactory().openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object> list = query.list();
		sessions.flush();
		sessions.close();
		if (!ObjectUtil.isListEmpty(list))
			return (String) list.get(0);
		return null;
	}

	@Override
	public List<WarehousePayment> selectVendorListByBranchId(String branchId) {
		return list("FROM WarehousePayment w where w.branchId=? ORDER BY w.vendor.vendorName ASC", branchId);
	}

	@Override
	public WarehouseProduct findAvailableStockByWarehouseIdSelectedProductSeason(Long warehouseId, Long productId,
			String selectedSeason) {
		Object[] values = { warehouseId, productId, selectedSeason };
		WarehouseProduct warehouseProduct = (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.seasonCode=? AND wp.agent.id=null",
				values);
		return warehouseProduct;
	}

	public ESEAccount findESEAccountByProfileId(String profileId, int type) {
		Object[] values = { profileId, type };
		ESEAccount account = (ESEAccount) find("from ESEAccount account where account.profileId = ? AND type=?",
				values);
		return account;
	}

	@Override
	public List<Object[]> listOfWarehouseByStockEntry() {
		// TODO Auto-generated method stub
		List<Object[]> result = list(
				"SELECT DISTINCT wpd.warehousePayment.vendor.vendorId,wpd.warehousePayment.vendor.vendorName from WarehousePaymentDetails wpd)");

		return result;
	}

	@Override
	public void updateESEAccount(ESEAccount farmerAccount, String tenantId) {
		// TODO Auto-generated method stub
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(farmerAccount);
		sessions.flush();
		sessions.close();

	}

	public Product findProductBySubCategoryId(long subCategoryId) {
		// TODO Auto-generated method stub
		return (Product) find("FROM Product p WHERE p.subcategory.id=?", subCategoryId);
	}

	@Override
	public WarehouseProduct findAvailableStockByWarehouseIdSelectedProductSeasonBatchNo(long warehouseId,
			long productId, String selectedSeason, String batchNo) {
		Object[] values = { warehouseId, productId, selectedSeason, batchNo };
		WarehouseProduct warehouseProduct = (WarehouseProduct) find(
				"FROM WarehouseProduct wpd WHERE wpd.warehouse.id=? AND wpd.product.id=? AND wpd.seasonCode=? AND wpd.agent.id=null AND wpd.batchNo=?",
				values);
		return warehouseProduct;
	}

	@Override
	public List<Object[]> findBatchNoListByWarehouseIdProductIdSeason(long warehouseId, long productId,
			String selectedSeason) {
		// TODO Auto-generated method stub
		Object[] values = { warehouseId, productId, selectedSeason };
		return (List<Object[]>) list(
				"SELECT wp.id,wp.batchNo FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.seasonCode=? AND wp.agent.id=null and wp.stock  > 0",
				values);
	}

	@Override
	public WarehouseProduct findAvailableStockByWarehouseIdProductIdBatchNo(long warehouseId, long productId,
			String batchNo) {
		Object[] values = { warehouseId, productId, batchNo };
		// TODO Auto-generated method stub
		return (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.agent.id=null AND wp.batchNo=?",
				values);
	}

	@Override
	public WarehouseProduct findFieldStaffAvailableStockByAgentIdproductIdBatchNo(String agentId, long productId,
			String batchNo) {
		Object[] bindValues = { agentId, productId, batchNo };
		return (WarehouseProduct) find(
				"From WarehouseProduct wp WHERE wp.agent.profileId=? and wp.product.id=? and wp.batchNo=?", bindValues);
	}

	@Override
	public List<Object[]> findBatchNoListByAgentIdProductIdSeason(String agentId, long productId,
			String selectedSeason) {
		Object[] bindValues = { agentId, productId, selectedSeason };
		// TODO Auto-generated method stub
		return list(
				"SELECT wp.id,wp.batchNo From WarehouseProduct wp WHERE wp.agent.profileId=? and wp.product.id=? and wp.seasonCode=? and wp.stock > 0",
				bindValues);
	}

	@Override
	public WarehouseProduct findAvailableStockByAgentIdProductIdBatchNoSeason(String agentId, long productId,
			String selectedSeason, String batchNo) {
		Object[] values = { agentId, productId, selectedSeason, batchNo };

		WarehouseProduct warehouseProduct = (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.agent.profileId=? AND wp.product.id=? AND wp.seasonCode=? AND wp.warehouse.id=null AND wp.batchNo=?",
				values);
		return warehouseProduct;
	}

	@Override
	public WarehouseProduct findAvailableStocksBySeasonAndBatch(String servicePointId, long productId, String seasonId,
			String batch, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM WarehouseProduct wp WHERE wp.warehouse.code= :servicePointId AND wp.product.id= :id AND wp.seasonCode=:seasonCode AND wp.batchNo=:batchNo");
		query.setParameter("servicePointId", servicePointId);
		query.setParameter("id", productId);
		query.setParameter("seasonCode", seasonId);
		query.setParameter("batchNo", batch);

		List<WarehouseProduct> warehouseProductList = query.list();
		WarehouseProduct warehouseProduct = null;
		if (warehouseProductList.size() > 0) {
			warehouseProduct = (WarehouseProduct) warehouseProductList.get(0);
		}
		session.flush();
		session.close();
		return warehouseProduct;
	}

	@Override
	public WarehouseProduct findFieldStaffAvailableStockBySeasonAndBatch(String agentId, long productId,
			String seasonId, String batch, String tenantId,String branchId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"From WarehouseProduct wp WHERE wp.agent.profileId= :agentId AND wp.product.id= :productId AND wp.seasonCode=:seasonCode AND wp.batchNo=:batchNo AND wp.branchId=:branchId AND wp is not null");
		query.setParameter("agentId", agentId);
		query.setParameter("productId", productId);
		query.setParameter("seasonCode", seasonId);
		query.setParameter("batchNo", batch);
		query.setParameter("branchId", branchId);

		// WarehouseProduct warehouseProductList = (WarehouseProduct)
		// query.list();

		List<WarehouseProduct> warehouseProductList = query.list();
		WarehouseProduct warehouseProduct = null;
		if (warehouseProductList.size() > 0) {
			warehouseProduct = (WarehouseProduct) warehouseProductList.get(0);
		}

		session.flush();
		session.close();
		return warehouseProduct;
	}

	@Override
	public AgroTransaction findAgentByAgentId(String agentId) {
		AgroTransaction agroTxn = (AgroTransaction) find("FROM AgroTransaction ag WHERE ag.agentId=?", agentId);
		return agroTxn;
	}

	@Override
	public WarehouseProduct findAvailableStockByWarehouseIdProductIdBatchNoAndSeason(long warehouseId, long productId,
			String batchNo, String seasonCode) {
		Object[] values = { warehouseId, productId, batchNo, seasonCode };
		// TODO Auto-generated method stub
		return (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.agent.id=null AND wp.batchNo=? AND wp.seasonCode=?",
				values);
	}

	@Override
	public double findAvailableStockByWarehouseIdProductIdSeasonBatchNo(long warehouseId, long productId,
			String selectedSeason, String batchNo) {

		Object[] values = { warehouseId, productId, selectedSeason, batchNo };
		Double warehouseProduct = (Double) find(
				"SELECT COALESCE(SUM(wp.stock),0) FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.seasonCode=? AND wp.agent.id=null AND wp.batchNo=? and wp is not null",
				values);
		return warehouseProduct;
	}

	@Override
	public double findAvailableStockByAgentIdProductIdSeasonBatchNo(String agentId, long productId,
			String selectedSeason, String batchNo) {
		Object[] values = { agentId, productId, selectedSeason, batchNo };

		Double warehouseProduct = (Double) find(
				"SELECT COALESCE(SUM(wp.stock),0) FROM WarehouseProduct wp WHERE wp.agent.profileId=? AND wp.product.id=? AND wp.seasonCode=? AND wp.warehouse.id=null AND wp.batchNo=? and wp is not null",
				values);
		return warehouseProduct;
	}

	@Override
	public Distribution findDistributionFarmerByRecNoAndTxnType(String receiptNumber, String txnType) {
		Object[] values = { receiptNumber, txnType };
		return (Distribution) find("FROM Distribution d WHERE d.receiptNumber=? AND d.txnType=?", values);
	}

	@Override
	public String findPrefernceByName(String enableApproved, String tenantId) {

		String queryString = "SELECT VAL FROM pref WHERE pref.NAME ='" + enableApproved + "' ";
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = sessions.createSQLQuery(queryString);
		List<Object> list = query.list();
		sessions.flush();
		sessions.close();
		if (!ObjectUtil.isListEmpty(list))
			return (String) list.get(0);
		return null;

	}

	public WarehouseProduct findwarehouseProductById(Long id) {
		// TODO Auto-generated method stub
		return (WarehouseProduct) find("FROM WarehouseProduct wp where wp.id=?", id);
	}

	@Override
	public WarehousePayment findwarehousePaymentById(Long id) {
		// TODO Auto-generated method stub
		return (WarehousePayment) find("FROM WarehousePayment wp WHERE wp.id=?", id);
	}

	@Override
	public WarehousePaymentDetails findwarehousePaymentDetailById(long id) {
		// TODO Auto-generated method stub
		return (WarehousePaymentDetails) find("FROM WarehousePaymentDetails wpd WHERE wpd.id=?", id);
	}

	@Override
	public Object[] findAvailableStockAndDamagedStockByWarehouseIdProductIdSeasonBatchNo(long warehouseId,
			long productId, String selectedSeason, String batchNo) {
		Object[] values = { warehouseId, productId, selectedSeason, batchNo };
		// TODO Auto-generated method stub
		return (Object[]) find(
				"SELECT wp.stock,wp.damagedQty,wp.id FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.seasonCode=? AND wp.agent.id=null AND wp.batchNo=?",
				values);
	}

	@Override
	public WarehouseProductDetail findwarehouseProductDetailByWarehouseProductIdReceiptNo(long id, String receiptNo) {
		Object[] values = { id, receiptNo };
		// TODO Auto-generated method stub
		return (WarehouseProductDetail) find(
				"FROM WarehouseProductDetail wpd where wpd.warehouseProduct.id=? and wpd.receiptNo=?", values);
	}

	@Override
	public WarehouseProduct findAvailableStockByAgentIdProductIdSeason(String profileId, long productId,
			String selectedSeason) {

		Object[] values = { profileId, productId, selectedSeason };

		WarehouseProduct warehouseProduct = (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.agent.profileId=? AND wp.product.id=? AND wp.seasonCode=? AND wp.warehouse.id=null",
				values);
		return warehouseProduct;
	}

	public Object findNoOfBagByByWarehouseIdProductIdAndGradeCode(Object warehouseId, Object productId,
			Object gradeCode) {

		Object[] values = { warehouseId, productId, gradeCode };
		return find(
				"SELECT SUM(cw.numberOfBags) FROM CityWarehouse cw WHERE cw.coOperative.id=? AND cw.procurementProduct.id=? AND cw.quality=? ",
				values);
	}

	public Object findNetWeightByWarehouseIdProductIdAndGradeCode(Object warehouseId, Object productId,
			Object gradeCode) {

		Object[] values = { warehouseId, productId, gradeCode };
		return find(
				"SELECT SUM(cw.grossWeight) FROM CityWarehouse cw WHERE cw.coOperative.id=? AND cw.procurementProduct.id=? AND cw.quality=? ",
				values);
	}

	public Object findNoOfBagByByWarehouseIdProductId(Object warehouseId, Object productId) {

		Object[] values = { warehouseId, productId };
		return find(
				"SELECT SUM(cw.numberOfBags) FROM CityWarehouse cw WHERE cw.coOperative.id=? AND cw.procurementProduct.id=?",
				values);
	}

	public Object findNetWeightByWarehouseIdProductId(Object warehouseId, Object productId) {

		Object[] values = { warehouseId, productId };
		return find(
				"SELECT SUM(cw.grossWeight) FROM CityWarehouse cw WHERE cw.coOperative.id=? AND cw.procurementProduct.id=? ",
				values);
	}

	@Override
	public List<Object[]> listPMTReceiptNumberByWarehouse(long warehouseId) {
		Object[] args = { warehouseId, PMT.Status.COMPLETE.ordinal() };
		/*return (List<Object[]>) list(
				"select DISTINCT pmtd.pmt.mtntReceiptNumber,pmtd.pmt.statusCode from PMTDetail pmtd where pmtd.coOperative.id=? AND pmtd.pmt.mtntReceiptNumber!=null and pmtd.mtnrGrossWeight>0 and pmtd.status!=?",
				args);*/
		return (List<Object[]>) list(
				"select DISTINCT pmtd.pmt.mtntReceiptNumber,pmtd.pmt.statusCode from PMTDetail pmtd where pmtd.coOperative.id=? AND pmtd.pmt.mtntTransferInfo.id!=null and pmtd.mtnrGrossWeight>0 and pmtd.status!=?",
				args);
	}

	@Override
	public List<ProcurementProduct> listProcurementProductByPMTReceiptNo(String receiptNo) {
		// TODO Auto-generated method stub
		return list(
				"SELECT DISTINCT pp FROM PMTDetail pmtd  INNER JOIN pmtd.pmt pt  INNER JOIN pmtd.procurementProduct pp  where pt.mtntReceiptNumber=?",
				receiptNo);
	}

	@Override
	public List<ProcurementGrade> listPMTProcurementGradeByVarietyId(String receiptNo) {
		// TODO Auto-generated method stub
		return list(
				"SELECT DISTINCT pg FROM PMTDetail pmtd  INNER JOIN pmtd.pmt pt  INNER JOIN pmtd.procurementGrade pg  where pt.mtntReceiptNumber=?",
				receiptNo);
	}

	@Override
	public PMTDetail findpmtdetailByProcurementGrade(Long gradeId) {
		// TODO Auto-generated method stub
		return (PMTDetail) find("from PMTDetail pd where pd.procurementGrade.id=?", gradeId);
	}

	@Override
	public List<Object[]> listProcurementProductByCultivation() {
		Session session = getSessionFactory().openSession();
		String queryString = "SELECT DISTINCT pp.code,pp.NAME from procurement_product pp INNER JOIN cultivation c on c.CROP_CODE=pp.CODE";
		Query query = session.createSQLQuery(queryString);
		List list = query.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public ProcurementGrade findProcurementGradeByVarityId(long varietyId) {
		ProcurementGrade procurementGrade = (ProcurementGrade) find(
				"FROM ProcurementGrade ppv WHERE ppv.procurementVariety.id = ?", varietyId);
		return procurementGrade;
	}

	@Override
	public WarehouseProduct findAvailableStockByWarehouseIdSelectedProductBatchNo(long warehouseId, long productId,
			String batchNo) {
		Object[] values = { warehouseId, productId, batchNo };
		WarehouseProduct warehouseProduct = (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.agent.id=null AND wp.batchNo=?",
				values);
		return warehouseProduct;
	}

	@Override
	public double findAvailableStockByWarehouseIdProductIdBatchNum(long warehouseId, long productId, String batchNo) {

		Object[] values = { warehouseId, productId, batchNo };
		Double warehouseProduct = (Double) find(
				"SELECT COALESCE(SUM(wp.stock),0) FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.agent.id=null AND wp.batchNo=? and wp is not null",
				values);
		return warehouseProduct;
	}

	@Override
	public double findAvailableStockByAgentIdProductIdBatchNum(String agentId, long productId, String batchNo) {

		Object[] values = { agentId, productId, batchNo };

		Double warehouseProduct = (Double) find(
				"SELECT COALESCE(SUM(wp.stock),0) FROM WarehouseProduct wp WHERE wp.agent.profileId=? AND wp.product.id=? AND wp.warehouse.id=null AND wp.batchNo=? and wp is not null",
				values);
		return warehouseProduct;
	}

	@Override
	public WarehouseProduct findAvailableStockByAgentIdProductIdBatchNo(String agentId, long productId,
			String batchNo) {

		Object[] values = { agentId, productId, batchNo };

		WarehouseProduct warehouseProduct = (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.agent.profileId=? AND wp.product.id=? AND wp.warehouse.id=null AND wp.batchNo=?",
				values);
		return warehouseProduct;
	}

	@Override
	public WarehouseProduct findFieldStaffAvailableStockByBatch(String profileId, long productId, String batchNo,
			String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"From WarehouseProduct wp WHERE wp.agent.profileId= :profileId AND wp.product.id= :productId AND wp.batchNo=:batchNo");
		query.setParameter("profileId", profileId);
		query.setParameter("productId", productId);
		query.setParameter("batchNo", batchNo);

		// WarehouseProduct warehouseProductList = (WarehouseProduct)
		// query.list();

		List<WarehouseProduct> warehouseProductList = query.list();
		WarehouseProduct warehouseProduct = null;
		if (warehouseProductList.size() > 0) {
			warehouseProduct = (WarehouseProduct) warehouseProductList.get(0);
		}

		session.flush();
		session.close();
		return warehouseProduct;
	}

	@Override
	public WarehouseProduct findAvailableStocksByBatch(String servicePointId, long productId, String batch,
			String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM WarehouseProduct wp WHERE wp.warehouse.code= :servicePointId AND wp.product.id= :id AND wp.batchNo=:batchNo");
		query.setParameter("servicePointId", servicePointId);
		query.setParameter("id", productId);
		query.setParameter("batchNo", batch);

		List<WarehouseProduct> warehouseProductList = query.list();
		WarehouseProduct warehouseProduct = null;
		if (warehouseProductList.size() > 0) {
			warehouseProduct = (WarehouseProduct) warehouseProductList.get(0);
		}
		session.flush();
		session.close();
		return warehouseProduct;
	}

	@Override
	public List<Object[]> listProcurementProductFarmersByPMTReceiptNo(String receiptNoId) {
		return list(
				"SELECT DISTINCT f.id,f.firstName,COALESCE(f.lastName,'') FROM PMTDetail pmtd  INNER JOIN pmtd.pmt pt  INNER JOIN pmtd.farmer f  where pt.mtntReceiptNumber=?",
				receiptNoId);
	}

	@Override
	public List<ProcurementProduct> listProcurementProductByPMTReceiptNoAndFarmer(String receiptNoId, Long farmerid) {
		return list(
				"SELECT DISTINCT pp FROM PMTDetail pmtd  INNER JOIN pmtd.pmt pt  INNER JOIN pmtd.procurementProduct pp  where pt.mtntReceiptNumber=? and pmtd.farmer.id=?",
				new Object[] { receiptNoId, farmerid });
	}

	@Override
	public PMTDetail findpmtdetailByProcurementGradeAndReceiptNo(Long gradeId, String receiptNoId) {
		return (PMTDetail) find(
				"from PMTDetail pd where pd.procurementGrade.id=? and pd.pmt.mtntReceiptNumber=? order by pd.id desc",
				new Object[] { gradeId, receiptNoId });
	}

	@Override
	public List<ProcurementGrade> listPMTProcurementGradeByVarietyIdAndProduct(String receiptNoId,
			String selectedProduct) {
		return list(
				"SELECT DISTINCT pg FROM PMTDetail pmtd  INNER JOIN pmtd.pmt pt  INNER JOIN pmtd.procurementGrade pg  where pt.mtntReceiptNumber=? and pmtd.procurementProduct.code=?",
				new Object[] { receiptNoId, selectedProduct });
	}

	@Override
	public List<AgroTransaction> findAgroTxnByFarmerIdTXnTypesSeasonCode(String farmerId, String seasonCode,
			String txnTypes) {
		Object[] values = { farmerId, txnTypes, seasonCode };
		return list("FROM AgroTransaction a where a.farmerId=" + "'" + farmerId.trim() + "'" + " AND a.txnType IN("
				+ txnTypes + ") AND a.seasonCode=" + "'" + seasonCode.trim() + "'" + " AND a.profType=" + "'"
				+ ESEAccount.FARMER_ACC + "'" + " ORDER BY a.id DESC");
	}

	@Override
	public List<Object[]> listProcurementProductByVariety(Long procurementProdId) {
		// TODO Auto-generated method stub
		return list("SELECT pv.id,pv.code,pv.name FROM ProcurementVariety pv WHERE pv.procurementProduct.id=?",
				procurementProdId);
	}

	public CityWarehouse findCityWarehouseIdByFarmerAndProductIdAndGradeCode(long farmerId, long productId,
			String gradeCode) {

		Object[] values = { farmerId, productId, gradeCode };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.farmer.id = ? AND cw.procurementProduct.id = ? AND cw.quality = ?",
				values);
		return cityWarehouse;
	}

	public CityWarehouse findCityWarehouseIdByAgentAndProductIdAndGradeCode(String agentId, long productId,
			String gradeCode) {

		Object[] values = { agentId, productId, gradeCode };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.agentId = ? AND cw.procurementProduct.id = ? AND cw.quality = ?",
				values);
		return cityWarehouse;
	}

	@Override
	public List<AgroTransaction> listAgroTransactionByReceiptNoAndProfType(String receiptNumber, String profType) {
		// TODO Auto-generated method stub
		Object[] values = { receiptNumber, profType };
		return list("FROM AgroTransaction a where a.receiptNo=? AND a.profType=?", values);
	}

	@Override
	public AgroTransaction findAgrotxnByReceiptNoAndProfType(String receiptNumber, String profType) {
		// TODO Auto-generated method stub
		Object[] values = { receiptNumber, profType };
		return (AgroTransaction) find("FROM AgroTransaction a where a.receiptNo=? AND a.profType=?", values);
	}

	@Override
	public List<WarehousePaymentDetails> listWarehousePaymentDetailsByWarehousePaymentId(long id) {
		// TODO Auto-generated method stub
		return list("FROM WarehousePaymentDetails wpd WHERE wpd.warehousePayment.id = ?", id);
	}

	@Override
	public ProductReturnDetail findProductReturnDetailById(Long id) {
		// TODO Auto-generated method stub
		return (ProductReturnDetail) find(
				"FROM ProductReturnDetail prd left join fetch prd.productReturn where prd.id=?", id);
	}

	public List<ProductReturnDetail> listProductReturnDetail(long id) {

		return list("FROM ProductReturnDetail prd WHERE prd.productReturn.id = ?", id);
	}

	public List<ProductReturnDetail> listProductReturnDetail(long id, int startIndex, int limit) {

		Session session = getSessionFactory().openSession();
		String queryString = "select * FROM product_return_detail WHERE product_return_ID = '" + id + "' LIMIT "
				+ startIndex + "," + limit;
		Query query = session.createSQLQuery(queryString).addEntity(ProductReturnDetail.class);
		List<ProductReturnDetail> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public ProductReturn findProductReturnById(Long id) {
		ProductReturn productReturn = (ProductReturn) find("FROM ProductReturn pr WHERE pr.id = ?", id);
		return productReturn;
	}

	public ProductReturn findProductReturnByRecNoAndTxnType(String receiptNumber, String txnType) {

		ProductReturn productReturn = (ProductReturn) find(
				"FROM ProductReturn pr WHERE pr.agroTransaction.receiptNo = ? AND pr.agroTransaction.txnType=?",
				new Object[] { receiptNumber, txnType });

		return productReturn;
	}

	@Override
	public Object[] findAvailableStockByFarmer(String farmerId, long productId, String selectedSeason, String batchNo) {
		Object[] values = { farmerId, productId, selectedSeason, batchNo };
		Object[] farmerProduct = (Object[]) find(
				"SELECT COALESCE(SUM(dd.quantity),0),dd.sellingPrice,d.tax,d.paymentAmount,d.finalAmount FROM Distribution d INNER JOIN d.distributionDetails dd WHERE d.farmerId=? AND dd.product.id=? AND d.seasonCode=? AND d.agentId=null AND dd.batchNo=?",
				values);
		return farmerProduct;
	}

	@Override
	public DistributionDetail findByDistributionDetails(String farmerId, long productId, String batchNo,
			String seasonCode) {
		// TODO Auto-generated method stub
		Object[] values = { farmerId, productId, seasonCode, batchNo };
		return (DistributionDetail) find(
				"FROM DistributionDetail dd  WHERE dd.distribution.farmerId=? AND dd.product.id=? AND dd.distribution.seasonCode=? AND dd.distribution.agentId=null AND dd.batchNo=?",
				values);
	}

	@Override
	public ProductReturn findProductReturnFarmerByRecNoAndTxnType(String receiptNumber, String txnType) {
		// TODO Auto-generated method stub
		Object[] values = { receiptNumber, txnType };
		return (ProductReturn) find("FROM ProductReturn pr WHERE pr.receiptNumber=? AND pr.txnType=?", values);
	}

	@Override
	public ProductReturn findProductReturnByReceiptNoTxnType(String receiptNo, String txnType) {
		Object[] bindValues = { receiptNo, txnType };
		ProductReturn productReturn = (ProductReturn) find(
				"FROM ProductReturn pr WHERE pr.agroTransaction.receiptNo = ? and pr.agroTransaction.txnType=?",
				bindValues);
		return productReturn;
	}

	public List<OfflineProductReturn> listOfflineProductReturn(String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		List<OfflineProductReturn> result = session
				.createQuery("FROM OfflineProductReturn opr WHERE opr.statusCode = 2").list();

		session.flush();
		session.close();
		return result;

	}

	public ProductReturn findProductReturnByReceiptNoTxnType(String receiptNo, String txnType, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM ProductReturn pr WHERE pr.agroTransaction.receiptNo = :receiptNo AND pr.agroTransaction.txnType= :txnType");
		query.setParameter("receiptNo", receiptNo);
		query.setParameter("txnType", txnType);

		List<ProductReturn> productReturnList = query.list();
		ProductReturn productReturn = null;
		if (productReturnList.size() > 0) {
			productReturn = (ProductReturn) productReturnList.get(0);
		}

		session.flush();
		session.close();
		return productReturn;

	}

	@Override
	public void saveProductReturn(ProductReturn productReturn, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(productReturn);
		sessions.flush();
		sessions.close();
	}

	@Override
	public void updateOfflineProductReturn(OfflineProductReturn offlineProductReturn, String tenantId) {

		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(offlineProductReturn);
		sessions.flush();
		sessions.close();

	}

	public ESEAccount findESEAccountByProfileId(String profileId, int type, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session
				.createQuery("from ESEAccount account where account.profileId = :profileId AND type=:type");
		query.setParameter("profileId", profileId);
		query.setParameter("type", type);
		List<ESEAccount> eseAccountList = query.list();

		ESEAccount eseAccount = null;
		if (eseAccountList.size() > 0) {
			eseAccount = (ESEAccount) eseAccountList.get(0);
		}

		session.flush();
		session.close();
		return eseAccount;
	}

	@Override
	public DistributionDetail findByDistributionDetails(String farmerId, long productId, String batchNo,
			String seasonCode, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM DistributionDetail dd  WHERE dd.distribution.farmerId=:farmerId AND dd.product.id=:productId AND dd.distribution.seasonCode=:seasonCode AND dd.distribution.agentId=null AND dd.batchNo=:batchNo");
		query.setParameter("farmerId", farmerId);
		query.setParameter("productId", productId);
		query.setParameter("seasonCode", seasonCode);
		query.setParameter("batchNo", batchNo);

		// Distribution distributionList = (Distribution) query.list();

		List<DistributionDetail> distributionList = query.list();
		DistributionDetail distribution = null;
		if (distributionList.size() > 0) {
			distribution = (DistributionDetail) distributionList.get(0);
		}

		session.flush();
		session.close();
		return distribution;
	}

	@Override
	public WarehouseProduct findAvailableStockByAgentIdProductIdBatchNoSeason(String agentId, long productId,
			String seasonId, String batch, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"From WarehouseProduct wp WHERE wp.agent.profileId= :agentId AND wp.product.id= :productId AND wp.seasonCode=:seasonCode AND wp.warehouse.id=null AND wp.batchNo=:batchNo");
		query.setParameter("agentId", agentId);
		query.setParameter("productId", productId);
		query.setParameter("seasonCode", seasonId);
		query.setParameter("batchNo", batch);

		// WarehouseProduct warehouseProductList = (WarehouseProduct)
		// query.list();

		List<WarehouseProduct> warehouseProductList = query.list();
		WarehouseProduct warehouseProduct = null;
		if (warehouseProductList.size() > 0) {
			warehouseProduct = (WarehouseProduct) warehouseProductList.get(0);
		}

		session.flush();
		session.close();
		return warehouseProduct;
	}

	public WarehouseProduct isVarietyExists(String variety) {
		return (WarehouseProduct) find("FROM WarehouseProduct wpd WHERE wpd.batchNo=?", variety);
	}

	@Override
	public List<WarehousePaymentDetails> listOfWarehousePaymentDetailsByWarehousePaymentId(long id) {
		// TODO Auto-generated method stub
		return list("FROM WarehousePaymentDetails wpd WHERE wpd.warehousePaymentDetails.id = ?", id);
	}

	@Override
	public List<Object> findWarehouseProductDetailsVarityByProduct(long productId, long coOperativeId,
			String seasonCode) {

		Object[] values = { productId, coOperativeId, seasonCode };

		// return list("FROM WarehouseProduct wpd where wpd.product.id =? AND
		// wpd.warehouse.id = ? AND wpd.seasonCode= ? GROUP BY wpd.batchNo" ,
		// values);

		return list(
				"SELECT Distinct wpd.batchNo FROM WarehouseProduct wpd where wpd.product.id =? AND wpd.warehouse.id = ? AND wpd.seasonCode= ? ",
				values);

	}

	@Override
	public WarehousePaymentDetails findWarehousePaymentDetailsByProduct(long productId) {
		// TODO Auto-generated method stub
		return (WarehousePaymentDetails) find("FROM WarehousePaymentDetails wp WHERE wp.product.id=?", productId);
	}

	@Override
	public List<WarehouseProduct> listProductsByWarehouseAndSeason(long coOperativeId, String seasonCode) {
		// TODO Auto-generated method stub

		Object[] values = { seasonCode, coOperativeId };

		return list(
				"from WarehouseProduct wpd where wpd.seasonCode =? AND wpd.warehouse.id =? AND wpd.stock > 0 group by wpd.product.id  ",
				values);
	}

	@Override
	public WarehouseProductDetail findwarehouseProductDetailById(long id) {
		// TODO Auto-generated method stub
		return (WarehouseProductDetail) find("FROM WarehouseProductDetail wpd WHERE wpd.id=?", id);
	}

	@Override
	public WarehouseProductDetail findwarehouseProductDetailByWarehouseProductId(long productID) {
		// TODO Auto-generated method stub
		return (WarehouseProductDetail) find("FROM WarehouseProductDetail wp WHERE wp.warehouseProduct.product.id=?",
				productID);
	}

	@Override
	public ProcurementGrade findProcurementGradeByNameAndVarietyId(String name, long id) {
		// TODO Auto-generated method stub
		Object[] values = { name, id };
		ProcurementGrade procurementGrade = (ProcurementGrade) find(
				"FROM ProcurementGrade pg WHERE pg.name=? and pg.procurementVariety.id=?", values);
		return procurementGrade;
	}

	@Override
	public ProcurementVariety findProcurementVariertyByNameAndCropId(String varietyName, long procurementProductId) {
		// TODO Auto-generated method stub
		Object[] values = { varietyName, procurementProductId };
		ProcurementVariety procurementVariety = (ProcurementVariety) find(
				"FROM ProcurementVariety pv WHERE pv.name=? and pv.procurementProduct.id=?", values);
		return procurementVariety;
	}

	@Override
	public List<ProcurementProduct> listProcurementProductBasedOnCropCat() {

		return list("FROM ProcurementProduct pp  WHERE pp.cropCategory = 0 ORDER BY pp.name");
	}

	@Override
	public List<ProcurementProduct> listProcurementProductBasedOnInterCropCat(String cropCategory) {
		// TODO Auto-generated method stub
		return list("FROM ProcurementProduct pp  WHERE pp.cropCategory = ? ORDER BY pp.name", cropCategory);
	}

	@Override
	public List<PMTDetail> listPMTDetailByRecNoAndProduct(Long productId, String receiptNo) {
		Session session = getSessionFactory().openSession();
		String queryString = "FROM PMTDetail pd WHERE pd.procurementGrade.id=:productId and pd.pmt.mtntReceiptNumber=:receiptNo order by pd.id desc";
		Query query = session.createQuery(queryString);
		query.setParameter("productId", productId);
		query.setParameter("receiptNo", receiptNo);
		List<PMTDetail> pmtDetailList = query.list();
		session.flush();
		session.close();
		return pmtDetailList;
	}

	@Override
	public List<PMTDetail> listPMTDetailByProductIdAndReceiptNo(Long warehouseId, String receiptNoId) {
		Object[] values = { warehouseId, receiptNoId };
		return list(
				"FROM PMTDetail pd WHERE pd.coOperative.id=? AND pd.pmt.mtntReceiptNumber=? AND pd.mtnrGrossWeight>0",
				values);
	}

	public ProcurementVariety findProcurementVariertyByNameAndProductId(String name, long id) {
		Object[] values = { name, id };
		ProcurementVariety procurementVariety = (ProcurementVariety) find(
				"FROM ProcurementVariety ppv WHERE ppv.name = ? AND ppv.procurementProduct.id =?", values);

		return procurementVariety;
	}

	@Override
	public List<Distribution> findDistributionByFarmerId(String farmerId) {
		// TODO Auto-generated method stub
		return list("from Distribution dis WHERE dis.farmerId=?", farmerId);
	}

	@Override
	public List<DistributionDetail> findDistributionDetailByDistributionId(long id) {
		// TODO Auto-generated method stub
		return list("from DistributionDetail dd WHERE dd.distribution.id=?", id);
	}

	@Override
	public List<Object[]> findDistributionCount() {
		return list(
				"SELECT d.farmerId,count(d.id) from Distribution d WHERE d.farmerId IS NOT NULL GROUP BY d.farmerId");
	}

	@Override
	public List<Object[]> findProcurementCount() {
		// TODO Auto-generated method stub
		return list(
				"SELECT p.farmer.id,count(p.id) from Procurement p WHERE p.farmer.id IS NOT NULL GROUP BY p.farmer.id");
	}

	@Override
	public List<Object[]> findDistributionAndProcurmentCountByFarmers() {
		// TODO Auto-generated method stub
		return list(
				"SELECT f.id,f.farmerId,f.firstName,f.branchId,(SELECT count(d.id) from Distribution d where d.farmerId=f.farmerId),(SELECT count(p.id) from Procurement p where p.farmer.id=f.id) from Farmer f");
	}

	@Override
	public double findWarehouseAvaliableStock(String warehouseCode, long productId, String selectedSeason,
			String batchNo) {

		Object[] values = { warehouseCode, productId, selectedSeason, batchNo };
		Double warehouseProduct = (Double) find(
				"SELECT COALESCE(SUM(wp.stock),0) FROM WarehouseProduct wp WHERE wp.warehouse.code=? AND wp.product.id=? AND wp.seasonCode=? AND wp.agent.id=null AND wp.batchNo=? and wp is not null",
				values);
		return warehouseProduct;
	}

	@Override
	public ProcurementVariety findProcurementVariertyByProductAndVarietyName(String product, String variety) {
		Object[] params = { product, variety };
		ProcurementVariety procurementVariety = (ProcurementVariety) find(
				"FROM ProcurementVariety ppv WHERE ppv.procurementProduct.name=? and ppv.name = ?", params);
		return procurementVariety;
	}

	public List<OfflineSupplierProcurement> listPendingOfflineSupplierProcurementList(String tenantID) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantID).openSession();
		List<OfflineSupplierProcurement> result = session
				.createQuery("FROM OfflineSupplierProcurement op WHERE op.statusCode=2").list();
		session.flush();
		session.close();
		return result;
	}

	public SupplierProcurement findSupplierProcurementByRecNoAndOperType(String receiptNo, int operationType,
			String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM SupplierProcurement pt WHERE pt.agroTransaction.receiptNo = :receiptNo AND pt.agroTransaction.operType = :operationType");
		query.setParameter("receiptNo", receiptNo);
		query.setParameter("operationType", operationType);

		List<SupplierProcurement> supplierProcurementList = query.list();
		SupplierProcurement supplierProcurement = null;
		if (supplierProcurementList.size() > 0) {
			supplierProcurement = (SupplierProcurement) supplierProcurementList.get(0);
		}

		session.flush();
		session.close();
		return supplierProcurement;
	}

	@Override
	public void saveSupplierProcurement(SupplierProcurement supplierProcurement, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(supplierProcurement);
		sessions.flush();
		sessions.close();
	}

	@Override
	public void updateOfflineSupplierProcurement(OfflineSupplierProcurement offlineSupplierProcurement,
			String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(offlineSupplierProcurement);
		sessions.flush();
		sessions.close();
	}

	public CityWarehouse findSupplierWarehouseByCoOperative(long coOperativeId, long productId, String quality,
			String agentId, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM CityWarehouse cw WHERE cw.coOperative.id = :coOperativeId AND cw.procurementProduct.id = :productId "
						+ " AND cw.quality = :quality AND cw.agentId= :agentId AND cw.isDelete = 0");
		query.setParameter("coOperativeId", coOperativeId);
		query.setParameter("productId", productId);
		query.setParameter("agentId", agentId);
		query.setParameter("quality", quality);

		List<CityWarehouse> supplierWarehouseList = query.list();
		CityWarehouse supplierWarehouse = null;
		if (supplierWarehouseList.size() > 0) {
			supplierWarehouse = (CityWarehouse) supplierWarehouseList.get(0);
		}

		session.flush();
		session.close();
		return supplierWarehouse;
	}

	public SupplierWarehouse findSupplierWarehouseByVillage(long villageId, long productId, String agentId,
			String quality, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM SupplierWarehouse sw WHERE sw.village.id = :villageId AND sw.procurementProduct.id = :productId "
						+ " AND sw.agentId = :agentId AND sw.quality = :quality AND sw.isDelete = 0");
		query.setParameter("villageId", villageId);
		query.setParameter("productId", productId);
		query.setParameter("agentId", agentId);
		query.setParameter("quality", quality);

		List<SupplierWarehouse> supplierWarehouseList = query.list();
		SupplierWarehouse supplierWarehouse = null;
		if (supplierWarehouseList.size() > 0) {
			supplierWarehouse = (SupplierWarehouse) supplierWarehouseList.get(0);
		}

		session.flush();
		session.close();
		return supplierWarehouse;
	}

	public SupplierWarehouse findSupplierWarehouseByWarehouseIdProcrmentGradeCodeAndProcurementProductId(
			long warehouseId, String gradeCode, long productId, String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM SupplierWarehouse sw WHERE sw.coOperative.id=:warehouseId AND sw.quality=:gradeCode AND "
						+ " sw.procurementProduct.id=:productId ");
		query.setParameter("warehouseId", warehouseId);
		query.setParameter("gradeCode", gradeCode);
		query.setParameter("productId", productId);

		List<SupplierWarehouse> supplierWarehouseList = query.list();
		SupplierWarehouse supplierWarehouse = null;
		if (supplierWarehouseList.size() > 0) {
			supplierWarehouse = (SupplierWarehouse) supplierWarehouseList.get(0);
		}

		session.flush();
		session.close();
		return supplierWarehouse;
	}

	@Override
	public void saveSupplierWarehouseDetail(SupplierWarehouseDetail supplierWarehouseDetail, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(supplierWarehouseDetail);
		sessions.flush();
		sessions.close();
	}

	@Override
	public void saveOrUpdateSupplierWarehouse(SupplierWarehouse supplierWarehouse, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.saveOrUpdate(supplierWarehouse);
		sessions.flush();
		sessions.close();
	}

	@Override
	public SupplierProcurement findSupplierProcurementById(Long id) {
		// TODO Auto-generated method stub
		SupplierProcurement supplierProcurement = (SupplierProcurement) find(
				"FROM SupplierProcurement sp WHERE sp.id = ?", id);

		return supplierProcurement;
	}

	public CityWarehouse findSupplierWarehouseByCoOperative(long coOperativeId, long productId, String quality,
			String agentId) {
		Object[] values = { coOperativeId, productId, quality, agentId };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.coOperative.id = ? AND cw.procurementProduct.id = ? AND cw.quality = ? AND cw.agentId= ? AND cw.isDelete = 0",
				values);
		return cityWarehouse;
	}

	@Override
	public SupplierProcurementDetail findSupplierProcurementDetailById(Long id) {
		SupplierProcurementDetail supplierProcurementDetail = (SupplierProcurementDetail) find(
				"FROM SupplierProcurementDetail sp WHERE sp.id = ?", id);
		return supplierProcurementDetail;
	}

	public List<CityWarehouse> listProcurementStockByAgentIdRevisionNo(String agentId, Long revisionNo) {

		Session session = getSessionFactory().openSession();
		String queryString1 = "FROM CityWarehouse cw WHERE cw.agentId=:agentId AND cw.isDelete=0 AND cw.revisionNo > :revNo)";
		// Query query =
		// session.createSQLQuery(queryString).addEntity(CityWarehouse.class);
		Query query = session.createQuery(queryString1);
		query.setParameter("agentId", agentId);
		query.setParameter("revNo", revisionNo);
		List<CityWarehouse> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	public CityWarehouse findSupplierWarehouseByCoOperativeProductAndGrade(long coOperativeId, long productId,
			String quality) {
		Object[] values = { coOperativeId, productId, quality };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.coOperative.id = ? AND cw.procurementProduct.id = ? AND cw.quality = ? AND cw.isDelete = 0",
				values);
		return cityWarehouse;
	}

	@Override
	public List<SupplierProcurement> listSupplierProcurement() {
		return list("FROM SupplierProcurement");
	}

	@Override
	public List<SupplierProcurementDetail> listSupplierProcurementProducts() {
		return list("FROM SupplierProcurementDetail spd GROUP BY spd.procurementProduct");
	}

	@Override
	public List<SupplierProcurementDetail> listSupplierProcurementVariety() {
		return list(
				"FROM SupplierProcurementDetail spd JOIN FETCH spd.procurementGrade pg JOIN FETCH pg.procurementVariety pv GROUP BY pv.id");
	}

	@Override
	public List<SupplierProcurementDetail> listSupplierProcurementDetails() {
		return list("FROM SupplierProcurementDetail spd");
	}

	@Override
	public ProcurementTraceability findProcurementTraceabtyByRecNo(String receiptNo) {
		// TODO Auto-generated method stub
		ProcurementTraceability procurementTrace = (ProcurementTraceability) find(
				"FROM ProcurementTraceability pt WHERE pt.agroTransaction.receiptNo = ?", receiptNo);

		return procurementTrace;
	}

	@Override
	public void saveProcurementTracebty(ProcurementTraceability procurementTrace, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(procurementTrace);
		sessions.flush();
		sessions.close();
	}

	@Override
	public void updateOfflineProcurementTrace(OfflineProcurementTraceability offlineProcurementTrace, String tenantId) {
		// TODO Auto-generated method stub
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(offlineProcurementTrace);
		sessions.flush();
		sessions.close();
	}

	@Override
	public List<OfflineProcurementTraceability> listPendingOfflineProcurementTraceList(String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		List<OfflineProcurementTraceability> result = session
				.createQuery("FROM OfflineProcurementTraceability op WHERE op.statusCode=2").list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public ProcurementProduct findProcurementProductPrice(Long selectedCrop) {
		ProcurementProduct procurementProduct = (ProcurementProduct) find(
				"FROM ProcurementProduct ppt WHERE ppt.id = ?", selectedCrop);
		return procurementProduct;
	}

	@Override
	public ProcurementTraceabilityStock findProcurementStockByIcs(String icsName, long productId, String quality,
			long warehouseId, String season) {
		Object[] values = { icsName, productId, quality, warehouseId,season };
		ProcurementTraceabilityStock traceabilityStock = (ProcurementTraceabilityStock) find(
				"FROM ProcurementTraceabilityStock pts WHERE pts.ics = ? AND pts.procurementProduct.id = ? AND pts.grade = ? AND pts.coOperative.id=? and pts.season=?",
				values);
		return traceabilityStock;
	}

	public ProcurementTraceability findProcurementTraceabtyByRecNoAndOperType(String receiptNo, int operationType,
			String tenantId) {

		/*
		 * Object[] values = { receiptNo, operationType }; Procurement
		 * procurement = (Procurement)
		 * find("FROM Procurement pt WHERE pt.agroTransaction.receiptNo = ? AND pt.agroTransaction.operType=?"
		 * , values); return procurement;
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM ProcurementTraceability pt WHERE pt.agroTransaction.receiptNo = :receiptNo AND pt.agroTransaction.operType = :operationType");
		query.setParameter("receiptNo", receiptNo);
		query.setParameter("operationType", operationType);

		List<ProcurementTraceability> procurementList = query.list();
		ProcurementTraceability procurement = null;
		if (procurementList.size() > 0) {
			procurement = (ProcurementTraceability) procurementList.get(0);
		}

		session.flush();
		session.close();
		return procurement;
	}

	@Override
	public List<Object[]> listProcurementTraceabilityDetailsByVillageAndProcurementCenter(
			ProcurementTraceabilityStockDetails procurementStockDetails, Map criteriaMap) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(ProcurementTraceabilityStockDetails.class);
		ProjectionList list = Projections.projectionList();

		String groupPropertyString = (String) criteriaMap.get(IReportDAO.PROJ_GROUP);
		String sumPropertyString = (String) criteriaMap.get(IReportDAO.PROJ_SUM);
		String avgPropertyString = (String) criteriaMap.get(IReportDAO.PROJ_AVG);
		String otherPropertyString = (String) criteriaMap.get(IReportDAO.PROJ_OTHERS);

		if (!ObjectUtil.isEmpty(procurementStockDetails.getProcurementTraceabilityStock())) {
			criteria.createAlias("farmer", "f");
			criteria.createAlias("f.village", "v");
			criteria.createAlias("v.city", "c");
			criteria.createAlias("f.samithi", "s");
			criteria.createAlias("procurementTraceabilityStock", "ps");
			criteria.createAlias("ps.coOperative", "w");
			criteria.createAlias("ps.procurementProduct", "pr");
			if (!StringUtil.isEmpty(procurementStockDetails.getProcurementTraceabilityStock().getIcs())) {
				criteria.add(Restrictions.in("ps.ics",
						Arrays.asList(procurementStockDetails.getProcurementTraceabilityStock().getIcs().split(","))));
			}
			if (!ObjectUtil.isEmpty(procurementStockDetails.getProcurementTraceabilityStock().getCoOperative())
					&& !StringUtil.isEmpty(
							procurementStockDetails.getProcurementTraceabilityStock().getCoOperative().getId())) {
				criteria.add(Restrictions.eq("w.id",
						procurementStockDetails.getProcurementTraceabilityStock().getCoOperative().getId()));
			}
			if(!StringUtil.isEmpty(procurementStockDetails.getProcurementTraceabilityStock().getSeason())){
				criteria.add(Restrictions.eqOrIsNull("ps.season",procurementStockDetails.getProcurementTraceabilityStock().getSeason()));
			}

			// criteria.add(Restrictions.gt("totalNumberOfBags", 0l));

			criteria.add(Restrictions.gt("totalstock", 0.0d));
		}

		if (!StringUtil.isEmpty(otherPropertyString)) {
			String[] otherProperties = otherPropertyString.split(IReportDAO.SEPARATOR);
			for (String property : otherProperties) {
				list.add(Projections.property(property));
			}
		}
		if (!StringUtil.isEmpty(sumPropertyString)) {
			String[] sumProperties = sumPropertyString.split(IReportDAO.SEPARATOR);
			for (String property : sumProperties) {
				list.add(Projections.sum(property));
			}
		}

		if (!StringUtil.isEmpty(avgPropertyString)) {
			String[] avgProperties = avgPropertyString.split(IReportDAO.SEPARATOR);
			for (String property : avgProperties) {
				list.add(Projections.avg(property));
			}
		}

		if (!StringUtil.isEmpty(groupPropertyString)) {
			String[] groupProperties = groupPropertyString.split(IReportDAO.SEPARATOR);
			for (String property : groupProperties) {
				list.add(Projections.groupProperty(property));
			}
		}

		if (!StringUtil.isEmpty(groupPropertyString) || !StringUtil.isEmpty(sumPropertyString)
				|| !StringUtil.isEmpty(avgPropertyString)) {
			criteria.setProjection(list);
		}

		List crList = criteria.list();
		session.flush();
		session.close();
		return crList;
	}

	@Override
	public ProcurementTraceabilityStockDetails findProcurementStockDetailByIdAndFarmer(long procurementStockId,
			long farmerId) {
		return (ProcurementTraceabilityStockDetails) find(
				"From ProcurementTraceabilityStockDetails ptsd where ptsd.procurementTraceabilityStock.id=? and ptsd.farmer.id=?",
				new Object[] { procurementStockId, farmerId });
	}

	@Override
	public ProcurementTraceabilityStockDetails findProcurementTracabiltiyDetailsStockById(Long id) {
		ProcurementTraceabilityStockDetails procurementTraceabilityStock = (ProcurementTraceabilityStockDetails) find(
				"FROM ProcurementTraceabilityStockDetails pts WHERE pts.id=?", id);
		return procurementTraceabilityStock;
	}

	@Override
	public List<Object[]> listTransferProCenters() {
		return list("SELECT Distinct p.coOperative.code,p.coOperative.name FROM PMT p where p.statusCode=1");
	}

	@Override
	public List<Object[]> listReceiverGinning() {
		return list(
				"SELECT Distinct pd.coOperative.code,pd.coOperative.name FROM PMTDetail pd where pd.pmt.statusCode=1");
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> listProductTransferReceiptNumber() {
		Object[] values = { PMT.Status.MTNT.ordinal() };
		return (List<Object[]>) list(
				"select pmtd.id, pmtd.pmt.mtntReceiptNumber from PMTDetail pmtd where pmtd.mtntNumberOfBags>0 AND pmtd.pmt.statusCode=?",
				values);
	}

	@Override
	public List<PMTDetail> listPMTDetailByProductIdAndReceiptNo(String receiptNoId) {
		Object[] values = { receiptNoId, PMT.Status.MTNT.ordinal() };
		return list("FROM PMTDetail pd WHERE pd.pmt.mtntReceiptNumber=? AND pd.pmt.statusCode=?", values);
	}

	@Override
	public List<Object[]> listWarehouseByPMTReceiptNo(String receiptNoId) {
		return list(
				"SELECT Distinct cop.id,cop.code,cop.name FROM PMTDetail pmtd  INNER JOIN pmtd.pmt pt INNER JOIN pt.coOperative cop where pt.mtntReceiptNumber=?",
				receiptNoId);
	}

	public ProcurementTraceabilityStock findProcurementTraceabilityStockByCoOperative(long coOperativeId,
			long productId, String quality, String icsCode,String season) {

		Object[] values = { coOperativeId, productId, quality, icsCode,season };
		ProcurementTraceabilityStock procurementTraceabilityStock = (ProcurementTraceabilityStock) find(
				"FROM ProcurementTraceabilityStock pts WHERE pts.coOperative.id = ? AND pts.procurementProduct.id = ? AND pts.grade = ? AND pts.ics = ? AND pts.season=?",
				values);
		return procurementTraceabilityStock;
	}

	@Override
	public void saveProcurementTraceability(ProcurementTraceability procurement, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(procurement);
		sessions.flush();
		sessions.close();

	}

	@Override
	public ProcurementTraceabilityStock findProcurementStockByIcs(String icsName, long productId, String grade,
			long warehouseId, String tenantId, String season) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM ProcurementTraceabilityStock pts WHERE pts.coOperative.id=:coOperativeId AND pts.procurementProduct.id=:productId AND pts.grade=:grade AND pts.ics=:icsName and pts.season=:season");
		query.setParameter("coOperativeId", warehouseId);
		query.setParameter("productId", productId);
		query.setParameter("icsName", icsName);
		query.setParameter("grade", grade);
		query.setParameter("season", season);

		List<ProcurementTraceabilityStock> stockList = query.list();
		ProcurementTraceabilityStock procurementTraceabilityStock = null;
		if (stockList.size() > 0) {
			procurementTraceabilityStock = (ProcurementTraceabilityStock) stockList.get(0);
		}
		session.flush();
		session.close();
		return procurementTraceabilityStock;
	}

	@Override
	public ProcurementTraceabilityStockDetails findProcurementStockDetailByIdAndFarmer(long pid, long farmerId,
			String tenantId) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"From ProcurementTraceabilityStockDetails ptsd where ptsd.procurementTraceabilityStock.id=:pid and ptsd.farmer.id=:farmerId");
		query.setParameter("pid", pid);
		query.setParameter("farmerId", farmerId);

		List<ProcurementTraceabilityStockDetails> stockList = query.list();
		ProcurementTraceabilityStockDetails procurementTraceabilityStockDetail = null;
		if (stockList.size() > 0) {
			procurementTraceabilityStockDetail = (ProcurementTraceabilityStockDetails) stockList.get(0);
		}
		session.flush();
		session.close();
		return procurementTraceabilityStockDetail;
	}

	@Override
	public void save(Object obj, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.saveOrUpdate(obj);
		sessions.flush();
		sessions.close();
	}

	public List<Object[]> listSupplierProcurementDetailProperties(String ssDate, String eeDate) {
		List list = new LinkedList<>();
		/*
		 * 1=Id,2=Txn time,3=invoice,4=season,5=warehouseId,6=procMasterType,7=
		 * procMasterTypeId,8=Farmer Name,9=Group,10=Village 11=City,12=product
		 * name,13=grade
		 * name,14=numberOfBags,15=ratePerUnit,16=grossWeight,17=isRegSupplier,
		 * 18=Un Reg farmer name
		 */
		String[] otherProperties = { "id", "agroTxn.txnTime", "supplierProcurement.invoiceNo",
				"supplierProcurement.seasonCode", "supplierProcurement.warehouseId",
				"supplierProcurement.procMasterType", "supplierProcurement.procMasterTypeId", "f.firstName", "s.name",
				"v.name", "c.name", "pp.name", "pg.name", "numberOfBags", "ratePerUnit", "grossWeight",
				"supplierProcurement.isRegSupplier", "farmerName","f.id" ,"supplierProcurement.invoiceValue"};

		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("id"));
		for (String property : otherProperties) {
			pList.add(Projections.property(property));
		}
		 Date from = DateUtil.convertStringToDate(ssDate, DateUtil.DATE_FORMAT_1);
	     Date to = DateUtil.convertStringToDate(eeDate, DateUtil.DATE_FORMAT_1);

	     Session session = getSessionFactory().openSession();
		Criteria criteria = getSessionFactory().openSession().createCriteria(SupplierProcurementDetail.class);
		criteria.createAlias("supplierProcurement", "supplierProcurement");
		criteria.createAlias("supplierProcurement.agroTransaction", "agroTxn");
		criteria.createAlias("farmer", "f",Criteria.LEFT_JOIN);
		criteria.createAlias("f.village", "v",Criteria.LEFT_JOIN);
		criteria.createAlias("f.samithi", "s",Criteria.LEFT_JOIN);
		criteria.createAlias("v.city", "c",Criteria.LEFT_JOIN);
		criteria.createAlias("procurementProduct", "pp");
		criteria.createAlias("procurementGrade", "pg");

		criteria.add(Restrictions.isNotNull("procurementGrade"));
		criteria.add(Restrictions.isNotNull("supplierProcurement"));
		criteria.add(Restrictions.isNotNull("supplierProcurement.agroTransaction"));
		criteria.add(Restrictions.between("agroTxn.txnTime",from, to));

		criteria.addOrder(Order.desc("id"));

		// procurementProduct
		criteria.setProjection(pList);
		list = criteria.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public List<PMT> findTransferStockByGinner(Integer ginner, Long warhouseId,String season) {
		// TODO Auto-generated method stub
		Object[] values = { ginner, warhouseId,season };
		return list(
				"select distinct pmt From PMT pmt inner join pmt.pmtDetails pd "
						+ "WHERE pd.coOperative.typez=? AND pd.coOperative.id=? AND pmt.mtntReceiptNumber IS NOT NULL "
						+ "and pd.mtntGrossWeight > 0 and pmt.statusCode in ('1','2') and pd.status in ('0','1','2') and pmt.seasonCode=?",
				values);

	}

	@Override
	public List listPendingOfflineMTNR(String tenantId) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		List<OfflineProcurement> result = session.createQuery("FROM OfflinePMTNR offPMTNR WHERE offPMTNR.statusCode=2").list();
		session.flush();
		session.close();
		return result;

	}

	@Override
	public PMT findPMTNRByReceiptNoAndType(String receiptNo, String trnType, String tenantId) {
		// TODO Auto-generated method stub
		Object[] values = { receiptNo, trnType };
		PMT pmt = (PMT) find("FROM PMT pmt WHERE pmt.mtnrReceiptNumber = ? AND pmt.trnType = ?", values);
		return pmt;
	}

	@Override
	public void savePMTNR(PMT pmtnr, String tenantId) {
		// TODO Auto-generated method stub
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.saveOrUpdate(pmtnr);
		sessions.flush();
		sessions.close();
	}

	@Override
	public void updateOfflinePMTNR(OfflinePMTNR offlinePMTNR, String tenantId) {
		// TODO Auto-generated method stub
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(offlinePMTNR);
		sessions.flush();
		sessions.close();
	}

	public List<Object[]> listProcurementTraceabilityStockbyAgent(String agentId, long revNo,String season) {
		List<Object[]> result = new ArrayList<Object[]>();
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "select f.farmer_id,pp.CODE,ptsd.TOTAL_NO_OF_BAGS,ptsd.TOTAL_STOCK,pts.GRADE from procurement_traceability_stock pts "
				+ "INNER JOIN procurement_product pp on pp.ID=pts.PROCUREMENT_PRODUCT_ID "
				+ "INNER JOIN procurement_traceability_stock_details ptsd on ptsd.PROCUREMENT_TRACEABILITY_STOCK_ID=pts.ID "
				+ "INNER JOIN farmer f on f.ID=ptsd.FARMER_ID " + "INNER JOIN warehouse w on w.ID=pts.CO_OPERATIVE_ID "
				+ "INNER JOIN prof p on p.WAREHOUSE_ID=w.ID WHERE p.PROF_ID=:agentId AND pts.SEASON=:season AND pts.REVISION_NO >=:revNo "
				+ "GROUP BY ptsd.FARMER_ID, pts.PROCUREMENT_PRODUCT_ID,pts.GRADE";
		Query query = session.createSQLQuery(queryString).setParameter("agentId", agentId).setParameter("revNo", revNo).setParameter("season", season);
		result = query.list();
		// session.flush();
		// session.close();
		return result;
	}

	public ProcurementTraceabilityStock findProcurementTraceabilityStockByCoOperative(long coOperativeId,
			long productId, String quality, String icsCode, String tenantId,String season) {
		/*
		 * Session session =
		 * getSessionFactory().withOptions().tenantIdentifier(tenantId).
		 * openSession(); Object[] values = { coOperativeId, productId, quality,
		 * icsCode }; ProcurementTraceabilityStock procurementTraceabilityStock
		 * = (ProcurementTraceabilityStock)
		 * find("FROM ProcurementTraceabilityStock pts WHERE pts.coOperative.id ='"
		 * +coOperativeId+"' AND pts.procurementProduct.id='"
		 * +productId+"' AND pts.grade='"+quality+"' AND pts.ics='"+icsCode+"'")
		 * ; session.flush(); session.close(); return
		 * procurementTraceabilityStock;
		 */

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM ProcurementTraceabilityStock pts WHERE pts.coOperative.id=:coOperativeId AND pts.procurementProduct.id=:productId AND pts.grade=:quality AND pts.ics=:icsCode AND pts.season=:season");
		query.setParameter("coOperativeId", coOperativeId);
		query.setParameter("quality", quality);
		query.setParameter("productId", productId);
		query.setParameter("icsCode", icsCode);
		query.setParameter("season", season);
		List<ProcurementTraceabilityStock> procurementTraceStockList = query.list();
		ProcurementTraceabilityStock procurementTraceStock = null;
		if (procurementTraceStockList.size() > 0) {
			procurementTraceStock = (ProcurementTraceabilityStock) procurementTraceStockList.get(0);
		}

		session.flush();
		session.close();
		return procurementTraceStock;
	}

	public CityWarehouse findSupplierWarehouseByCoOperativeProductAndGrade(long coOperativeId, long productId,
			String quality, String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Object[] values = { coOperativeId, productId, quality };
		CityWarehouse cityWarehouse = (CityWarehouse) find(
				"FROM CityWarehouse cw WHERE cw.coOperative.id = ? AND cw.procurementProduct.id = ? AND cw.quality = ? AND cw.isDelete = 0",
				values);
		session.flush();
		session.close();
		return cityWarehouse;
	}

	@Override
	public List<OfflineMTNT> listPendingOfflineTransferTraceability(String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		List<OfflineMTNT> result = session.createQuery("From OfflineMTNT offlineMTNT WHERE offlineMTNT.statusCode=3")
				.list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public List<Object> listPMTImageDetailsIdByPmtId(long pmtId) {

		return list("SELECT pimg.id  FROM PMTImageDetails pimg where pimg.pmt=?", pmtId);
	}

	@Override
	public PMTImageDetails findPMTImageDetailById(Long id) {
		return (PMTImageDetails) find("FROM PMTImageDetails pimg where pimg.id=?", id);
	}

	@Override
	public List<Object[]> findpmtdetailByPmtId(long pmtId, long pmtDetailId) {
		Object[] values = { pmtDetailId };
		return list(
				"select pd.mtntNumberOfBags,pd.mtntGrossWeight,pd.farmer FROM PMTFarmerDetail pd INNER JOIN pd.pmt p INNER JOIN p.pmtDetails pmd where pmd.id=?",
				values);
	}

	@Override
	public List<Object[]> findpmtdetailByPmt(Long id, String product, String icsName) {
		// TODO Auto-generated method stub
		return list(
				"select pd.mtntNumberOfBags,pd.mtntGrossWeight,pd.farmer,pd.procurementProduct.name,pd.procurementGrade.name,pd.procurementGrade.procurementVariety.name FROM PMTFarmerDetail pd INNER JOIN pd.pmt p where p.id=? and pd.procurementProduct.code=? and pd.ics=?",
				new Object[] { id, product, icsName });
	}

	@Override
	public PMT findDriverAndTransporterByReceiptNo(String receiptNoId, String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM PMT pmt where pmt.mtntReceiptNumber = :receiptNo");
		query.setParameter("receiptNo", receiptNoId);
		List<PMT> pmtList = query.list();
		PMT pmt = null;
		if (pmtList.size() > 0) {
			pmt = (PMT) pmtList.get(0);
		}
		session.flush();
		session.close();
		return pmt;
	}

	@Override
	public HeapData findHeapDataByGinnerProductIcsAndHeapCode(long coOperative, long product, String heap, String season) {

		return (HeapData) find("FROM HeapData hd where hd.ginning.id=? and hd.procurementProduct.id=?  and hd.code=? and hd.season=?",
				new Object[] { coOperative, product, heap, season});
	}

	@Override
	public List<HeapData> findHeapDataByGinner(Integer ginnerType, long ginnerId,String season) {
		return list("FROM HeapData hd where hd.ginning.id=? and hd.ginning.typez=? and hd.season=?",
				new Object[] { ginnerId, ginnerType,season });
	}

	@Override
	public HeapData findHeapDataByGinnerProductIcsAndHeapCode(long coOperative, long product, String heap,
			String tenantId, String season) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery(
				"FROM HeapData hd where hd.ginning.id=:coOperative and hd.procurementProduct.id=:product  and hd.code=:heap and hd.season=:season");
		query.setParameter("coOperative", coOperative);
		query.setParameter("product", product);
		// query.setParameter("ics", ics);
		query.setParameter("heap", heap);
		query.setParameter("season", season);

		List<HeapData> procurementList = query.list();
		HeapData procurement = null;
		if (procurementList.size() > 0) {
			procurement = (HeapData) procurementList.get(0);
		}

		session.flush();
		session.close();
		return procurement;

	}

	@Override
	public LedgerData findLedgerByGinnerProductIcsAndHeapCode(long ginning, long product, String ics, String heap,
			String tenantId) {
		return (LedgerData) find(
				"FROM LedgerData ld where ld.ginning.id=? and hd.product.id=? and hd.ics=? and hd.heap=? and hd.branchId=?",
				new Object[] { ginning, product, ics, heap, tenantId });
	}

	@Override
	public List listPendingOfflineGinningProcess(String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		List<OfflineProcurement> result = session
				.createQuery("FROM OfflineGinningProcess ofline WHERE ofline.statusCode=2").list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public void updateOfflineGinningProcess(OfflineGinningProcess offlineProcess, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(offlineProcess);
		sessions.flush();
		sessions.close();

	}

	@Override
	public List listPendingOfflingBaleGeneration(String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		List<OfflineProcurement> result = session
				.createQuery("FROM OfflineBaleGeneration ofline WHERE ofline.statusCode=2").list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public void updateOfflineBaleGeneration(OfflineBaleGeneration offlineProcess, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(offlineProcess);
		sessions.flush();
		sessions.close();

	}

	@Override
	public List listPendingOfflineSpinningTransfer(String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		List<OfflineProcurement> result = session
				.createQuery("FROM OfflineSpinningTransfer ofline WHERE ofline.statusCode=2").list();
		session.flush();
		session.close();
		return result;
	}

	@Override
	public void updateOfflineSpinningTransfer(OfflineSpinningTransfer offlineProcess, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(offlineProcess);

		sessions.flush();
		sessions.close();

	}

	@Override
	public GinningProcess findGinningProcessByDateHeapAndProduct(long ginning, String ginDate, String heap,
			long product, String tenantId, String season) {
		// return (GinningProcess) find ("FROM GinningProcess gp where
		// gp.processDate=? and gp.heapCode=?",new Object[]{ginDate,heap});

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		String queryString = "select * FROM ginning_process gp WHERE gp.GINNING_ID='" + ginning
				+ "' and gp.PROCESS_DATE='" + ginDate + "' and gp.HEAP_CODE='" + heap + "' and gp.PRODUCT='" + product
				+"' and gp.SEASON='"+season+"'";
		Query query = session.createSQLQuery(queryString).addEntity(GinningProcess.class);
		List<GinningProcess> list = query.list();
		session.flush();
		session.close();
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	@Override
	public void updateGinningProcess(GinningProcess ginningProcess, String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		String queryString = "UPDATE ginning_process gp SET gp.TOT_LINT_COTTON='" + ginningProcess.getTotlintCotton()
				+ "' ,gp.LINT_PER='" + ginningProcess.getLintPer() + "' ,gp.BALE_COUNT = '"
				+ ginningProcess.getBaleCount() + "',gp.FARMER='" + ginningProcess.getFarmer()
				+ "' WHERE gp.ID='" + ginningProcess.getId() + "'";
		Query query = session.createSQLQuery(queryString).addEntity(GinningProcess.class);
		query.executeUpdate();
		session.flush();
		session.close();
	}

	@Override
	public List<BaleGeneration> findBaleGenerationByGinningProcessId(String id) {
		return list("FROM BaleGeneration bg where bg.ginningProcess.id=?", Long.parseLong(id));
	}

	@Override
	public List<Object[]> listOfGinningFromSpinningTransfer() {

		return list("SELECT st.ginning.code,st.ginning.name FROM SpinningTransfer st");
	}

	@Override
	public List<Object[]> listOfSpinningFromSpinningTransfer() {
		return list("SELECT st.spinning.code,st.spinning.name FROM SpinningTransfer st");
	}

	@Override
	public List<Object[]> listOfLotNoPrNoAndTypeFromSpinningTransfer() {
		return list(
				"SELECT st.lotNo,st.prNo,st.type,(SELECT DISTINCT fc.name from FarmCatalogue fc where fc.code=st.type) from SpinningTransfer st");
	}

	@Override
	public HeapData findHeapDataByHeapCode(long ginning, String heap, String tenant,String season) {

		Session session = getSessionFactory().withOptions().tenantIdentifier(tenant).openSession();
		Query query = session.createQuery("FROM HeapData hd left join fetch hd.procurementProduct pp where hd.code=:heap and hd.ginning.id=:ginning and hd.season=:season");
		query.setParameter("heap", heap);
		query.setParameter("ginning", ginning);
		query.setParameter("season", season);
		List<HeapData> procurementList = query.list();
		HeapData procurement = null;
		if (procurementList.size() > 0) {
			procurement = (HeapData) procurementList.get(0);
		}

		session.flush();
		session.close();
		return procurement;
	}

	@Override
	public List<Object[]> listPMTDetailByProductIdReceiptNoAndICSName(String receiptNoId) {
		Object[] values = { receiptNoId, PMT.Status.MTNT.ordinal() };
		return list(
				"SELECT pd.id,pd.ics,pd.procurementProduct.id,sum(pd.mtnrNumberOfBags),sum(pd.mtntQuintalWeight),sum(pd.mtnrGrossWeight) FROM PMTDetail pd WHERE pd.pmt.mtntReceiptNumber=? AND pd.pmt.statusCode=? GROUP BY pd.procurementProduct,pd.ics",
				values);
	}

	@Override
	public SpinningTransfer findSpinningTransferById(long id) {
		return (SpinningTransfer) find("FROM SpinningTransfer st where st.id=?", id);
	}

	@Override
	public List<BaleGeneration> findBaleGenerationByGinningId(long ginningId,int status) {
		return list("FROM BaleGeneration bg where bg.ginning.id=? and bg.status=?", new Object[]{ginningId,status});
	}

	@Override
	public void updateBaleStatusById(String selectedBales,long id) {
		Session sessions = getSessionFactory().openSession();
		String queryStrings = "UPDATE BALE_GENERATION SET STATUS=1,SPINNING_TRANSFER_ID=:id WHERE ID IN ("+selectedBales+")";
		Query querys = sessions.createSQLQuery(queryStrings);
		querys.setParameter("id",id);
		int results = querys.executeUpdate();
		sessions.flush();
		sessions.close();

	}


	public List<Object[]> listGinningProcessByHeapProductAndGinning(String heap, long product, long ginning,String startDate, String endDate,String season) {
		Session sessions = getSessionFactory().openSession();
		String queryString = "";
		List<Object[]> list = new ArrayList<>();
		if (!StringUtil.isEmpty(heap) && !StringUtil.isEmpty(product)) {
			queryString = "SELECT gp.processDate,sum(gp.processQty) FROM GinningProcess gp where gp.heapCode=:heap AND gp.product.id=:product and gp.ginning.id=:ginning AND gp.season=:season AND gp.processDate BETWEEN :sDate AND :eDate GROUP BY gp.processDate";
			Query query = sessions.createQuery(queryString);
			query.setParameter("heap", heap);
			query.setParameter("ginning", ginning);
			query.setParameter("product", product);
			query.setParameter("season", season);
			query.setParameter("sDate", startDate);
			query.setParameter("eDate", endDate);
			
			list = query.list();
			}
		sessions.flush();
		sessions.close();
		return list;					
	}
	@Override
	public List<Object> listPMTImageDetailById(Long id,List<Integer> typeList) {
		Session sessions = getSessionFactory().openSession();
		String queryString="SELECT pimg.id  FROM PMTImageDetails pimg where pimg.pmt=:id and pimg.type in (:typeList)";
		Query query = sessions.createQuery(queryString);
		query.setParameter("id", id);
		query.setParameterList("typeList", typeList);
		List<Object> list = query.list();
		sessions.flush();
		sessions.close();
		return list;
	}

	@Override
	public List<PMTFarmerDetail> findPmtFarmerDetailByPmtId(long pmtId) {
		return list("FROM PMTFarmerDetail pd where pd.pmt.id=?",pmtId);
	}

	@Override
	public List<PMTDetail> findpmtdetailByPmtId(long pmtId) {
		return list("FROM PMTDetail pd where pd.pmt.id=?",pmtId);
	}

	
	public DistributionBalance findDistributionBalanceByFarmerAndProduct(long farmerId, long productId,String tenantId) {
		//return  (DistributionBalance) find("FROM DistributionBalance pd where pd.farmer.id=? and pd.product.id=? ",new Object[]{farmerId,productId,tenantId});
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session
				.createQuery("FROM DistributionBalance pd WHERE  pd.farmer.id = :farmerId AND pd.product.id = :productId");
		query.setParameter("farmerId", farmerId);
		query.setParameter("productId", productId);

		List<DistributionBalance> disList = query.list();

		DistributionBalance distBal = null;
		if (disList.size() > 0) {
			distBal = (DistributionBalance) disList.get(0);
		}

		session.flush();
		session.close();
		return distBal;
	}
	
	@Override
	public void update(DistributionBalance db,
			String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(db);
		sessions.flush();
		sessions.close();
	}

	@Override
	public List<ProcurementProduct> listProcurementProductFromFarmCrops() {
		  return list("SELECT DISTINCT pp from FarmCrops fc inner JOIN fc.procurementVariety pf inner join pf.procurementProduct pp where pp.id=pf.procurementProduct.id");
	}
	
	public AgroTransaction findAgroTransactionByProcurementId(long procurementId,long agrotxnId){
		Object[] values = { procurementId,agrotxnId};
		AgroTransaction agroTransaction = (AgroTransaction) find(
				"FROM AgroTransaction a WHERE a.procurement.id=? AND a.id=? ", values);
		return agroTransaction;
	}
	
	
	public PaymentLedger findPaymenyLedgerByProcurementId(String Id){
		Object[] values = { Id};
		PaymentLedger paymentLedger = (PaymentLedger) find(
				"FROM PaymentLedger p WHERE p.refId=? ", values);
		return paymentLedger;
	}
	
	
	public AgroTransaction findAgroTransactionByRecAndTxnDesc(String receiptno, String txnDesc){
		Object[] values = { receiptno,txnDesc};
		AgroTransaction agroTransaction = (AgroTransaction) find(
				"FROM AgroTransaction a WHERE a.receiptNo=? AND a.txnDesc=? ", values);
		return agroTransaction;
	}

	@Override
	public List<PMTImageDetails> findPMTImageDetailByDistributionId(long id) {
		 return list("FROM PMTImageDetails pd where pd.pmt=?",id);
	}

	@Override
	public void updateDistribution(Distribution distribution, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(distribution);
		sessions.flush();
		sessions.close();		
	}

	@Override
	public Double findAvailableStockByWarehouseIdAndProductId(long warehouseId, long productId) {
		Object[] values = { warehouseId, productId };
		Double warehouseProduct = (Double) find(
				"SELECT COALESCE(SUM(wp.stock),0) FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp is not null",
				values);
		return warehouseProduct;
	}

	@Override
	public WarehouseProduct findWarehouseStockByWarehouseIdAndProductId(long warehouseId, long productId,String season) {
		Object[] values = { warehouseId, productId, season };
		return (WarehouseProduct) find(
				"FROM WarehouseProduct wp WHERE wp.warehouse.id=? AND wp.product.id=? AND wp.seasonCode=?",
				values);
	}

	@Override
	public DistributionStock findDistributionStockById(long distributionStockId) {
		return (DistributionStock) find("FROM DistributionStock ds where ds.id=?",distributionStockId);
	}

	@Override
	public List<DistributionStockDetail> listDistributionStockDetailByReceiveWarehouseIdAndReceiptNo(
			Long receiverWarehouseId, String receiptNo) {
		return list("FROM DistributionStockDetail dsd where dsd.distributionStock.receiverWarehouse.id=? and dsd.distributionStock.receiptNo=?",new Object[]{receiverWarehouseId,receiptNo});
	}

	@Override
	public DistributionStock findTransferDistributionStockByReceiptNumber(String receiptNo) {
		return (DistributionStock) find("FROM DistributionStock ds where ds.receiptNo=? and ds.txnType=? and ds.status=?",new Object[]{receiptNo,DistributionStock.DISTRIBUTION_STOCK_TRANSFER,0});
	}

	@Override
	public DistributionStockDetail findDistributionStockDetailById(Long distDetID) {
		return (DistributionStockDetail) find("FROM DistributionStockDetail dsd where dsd.id=? and dsd.distributionStock.txnType=? and dsd.distributionStock.status=?",new Object[]{distDetID,DistributionStock.DISTRIBUTION_STOCK_TRANSFER,0});
	}

	@Override
	public List<AgroTransaction> findAgroTxnByFarmerIdTXnTypes(String farmerId) {
		// TODO Auto-generated method stub
		return list("FROM AgroTransaction at WHERE at.farmerId = ?", farmerId);
	}

	@Override
	public List<AgroTransaction> findAgroTxnByFarmerIdTXnTypesSeasonCode(String farmerId, String seasonCode,
			String[] txnTypes, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		
		
		Session session = getSessionFactory().getCurrentSession();

		String queryString = "from AgroTransaction ta where ta.farmerId=:FARMER_ID AND ta.seasonCode=:SEASON_CODE AND ta.txnType in (:TXN_TYPE) AND ta.profType=02 AND ta.txnTime BETWEEN :startDate AND :endDate ORDER BY ta.id DESC";
		
		Query query = session.createQuery(queryString);
		
		query.setParameter("FARMER_ID",farmerId);
		query.setParameter("SEASON_CODE",seasonCode);
		query.setParameterList("TXN_TYPE", txnTypes);
		query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		
		List<AgroTransaction> result = (List<AgroTransaction>) query.list();
			return result;
		
	}
	
	public boolean isAgentDistributionExist(String profileId){
		Long distributionCount = (Long) find(
				"SELECT Count(d) From Distribution d WHERE d.agentId=? AND d.txnType=514", profileId);
		//Long distributionCount = (Long) find("SELECT Count(d) From AgentAccessLogDetail d Where d.agentAccessLog.profileId=? AND d.txnType=314",profileId);
		if (distributionCount > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<DistributionStockDetail> findDistributionStOckDetailByDistributionId(long id) {
		// TODO Auto-generated method stub
		return list("From DistributionStockDetail dsd where dsd.distributionStock.id = ? ",id);
	}

	@Override
	public WarehouseProduct findWarehouseProductBySenderWarehouseIdAndSeasonCode(long id, String season) {
		// TODO Auto-generated method stub
		Object[] values = {id,season};
		return (WarehouseProduct) find ("From WarehouseProduct wp where wp.warehouse.id=? AND wp.seasonCode=?",values);
	}

	@Override
	public void deleteAgroTxnById(long id) {
		// TODO Auto-generated method stub
		/*Session session = getSessionFactory().openSession();
		String hql = "DELETE FROM AgroTransaction " + "WHERE id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		int result = query.executeUpdate();
		session.flush();
		session.close();*/
		
		  Session sessions = getSessionFactory().getCurrentSession();
		  sessions.disableFilter(ISecurityFilter.BRANCH_FILTER);
		  String hql = "DELETE FROM AgroTransaction " + "WHERE id = :id";
		  Query query = sessions.createQuery(hql);
		  query.setParameter("id", id);
		  int result = query.executeUpdate();
			
	}

	@Override
	public List<Object[]> listTreeDetails(long farmId) {
		// TODO Auto-generated method stub
		Session sessions = getSessionFactory().getCurrentSession();
		 String hql = "select sum(td.NO_OF_TREES),pv.name,td.PRODUCT_STATUS from tree_detail td inner join procurement_variety pv on pv.code=td.VARIETY "
		 		+ " where td.farm_id=:farmId GROUP BY pv.name,td.PRODUCT_STATUS";
		  Query query = sessions.createSQLQuery(hql);
		  query.setParameter("farmId", farmId);
		
		return query.list();
	}

	@Override
	public String findConvnVartyByFarm(long farmId, String conVartyCode) {
		// TODO Auto-generated method stub
		String result=null;
		Session sessions = getSessionFactory().openSession();
		String queryString="select  getVarietyTreeCount("+farmId+",'"+conVartyCode+"')";
		Query query = sessions.createSQLQuery(queryString);
		List list = query.list();
		if(list.size()>0)
		{
			result=(String) query.list().get(0);
		}
		sessions.flush();
		sessions.close();
		return result;
	}

	@Override
	public String findOrganicVartyByFarm(long farmId, String orgVartyCode) {
		// TODO Auto-generated method stub
		String result=null;
		Session sessions = getSessionFactory().openSession();
		String queryString="select  getVarietyTreeCount("+farmId+",'"+orgVartyCode+"')";
		Query query = sessions.createSQLQuery(queryString);
		List list = query.list();
		if(list.size()>0)
		{
			result=(String) query.list().get(0);
		}
		sessions.flush();
		sessions.close();
		return result;
	}
	@Override
	public void updateProductReturn(ProductReturn productReturn, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(productReturn);
		sessions.flush();
		sessions.close();		
	}
	
	public double findAvailableQtyByWarehouseColdStorageNameGradeBlockFloorBay(Long warehouseId, String coldStorageName, 
			String blockName, String floorName, String bayNumber, Long productId) {

	double array = 0.0;
		Session sessions = getSessionFactory().openSession();
		String queryString = "SELECT IFNULL(SUM( cw.GROSS_WEIGHT ) ,0) from city_warehouse cw where cw.CO_OPERATIVE_ID = '"+warehouseId+"' AND cw.COLD_STORAGE_NAME = '"+coldStorageName+"' AND cw.ID IN(SELECT DISTINCT(cwd.CITY_WAREHOUSE_ID) from city_warehouse_detail cwd WHERE cwd.BLOCK_NAME = '"+blockName+"' AND cwd.FLOOR_NAME = '"+floorName+"' AND cwd.BAY_NUMBER = '"+bayNumber+"')  ";
		Query query = sessions.createSQLQuery(queryString);
		if (!StringUtil.isEmpty(query)) {
			if(!ObjectUtil.isListEmpty(query.list())){
			array = (Double) query.list().get(0);
			}
		}
		sessions.flush();
		sessions.close();
		return array;
		
		
		
		
	}
	
	public CityWarehouse findCityWarehouseByWarehouseProductBatchNoGrade(long warehouseId, long productId,String batchNo,String grade){

		Session session = getSessionFactory().getCurrentSession();
		String queryString = "SELECT * from city_warehouse cw JOIN city_warehouse_detail cwd ON cwd.CITY_WAREHOUSE_ID=cw.ID WHERE cw.CO_OPERATIVE_ID='"+warehouseId+"' AND cw.PROCUREMENT_PRODUCT_ID='"+productId+"' AND cwd.BATCH_NO='"+batchNo+"' AND cw.quality ='"+grade+"' AND cw.IS_DELETE='0';";
		Query query = session.createSQLQuery(queryString).addEntity(CityWarehouse.class);
		List<CityWarehouse> list = query.list();
		if (list.size() > 0){
			return list.get(0);
			}
		else{
			return null;
			}
			
	}

	@Override
	public List<SupplierProcurementDetail> listSupplierProcurementDetailById(Long id) {
		Object[] values = { id };
		return list("FROM SupplierProcurementDetail spd WHERE spd.procurementGrade.id=?", values);
	}

	@Override
	public CityWarehouse findCityWarehouseByWarehouseProductBatchNoGradeFarmer(long warehouseId, long productId,
			String batchNo, String grade, String coldStorageName, String blockName, String floorName, String bayNum,long farmerId) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "SELECT * from city_warehouse cw JOIN city_warehouse_detail cwd ON cwd.CITY_WAREHOUSE_ID=cw.ID WHERE cw.CO_OPERATIVE_ID='"+warehouseId+"' AND cw.PROCUREMENT_PRODUCT_ID='"+productId+"' AND cwd.BATCH_NO='"+batchNo+"' AND cw.quality ='"+grade+"' AND cw.COLD_STORAGE_NAME='"+coldStorageName+"' AND cwd.BLOCK_NAME='"+blockName+"' AND cwd.FLOOR_NAME='"+floorName+"' AND cwd.BAY_NUMBER='"+bayNum+"' AND cw.FARMER_ID='"+farmerId+"' AND cw.IS_DELETE='0';";
		Query query = session.createSQLQuery(queryString).addEntity(CityWarehouse.class);
		List<CityWarehouse> list = query.list();
		if (list.size() > 0){
			return list.get(0);
			}
		else{
			return null;
			}
	}
	
	@Override
	public CityWarehouse findCityWarehouseByWarehouseProductBatchNoGrade(long warehouseId, long productId,
			String batchNo, String grade, String coldStorageName, String blockName, String floorName, String bayNum) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().getCurrentSession();
		String queryString = "SELECT * from city_warehouse cw JOIN city_warehouse_detail cwd ON cwd.CITY_WAREHOUSE_ID=cw.ID WHERE cw.CO_OPERATIVE_ID='"+warehouseId+"' AND cw.PROCUREMENT_PRODUCT_ID='"+productId+"' AND cwd.BATCH_NO='"+batchNo+"' AND cw.quality ='"+grade+"' AND cw.COLD_STORAGE_NAME='"+coldStorageName+"' AND cwd.BLOCK_NAME='"+blockName+"' AND cwd.FLOOR_NAME='"+floorName+"' AND cwd.BAY_NUMBER='"+bayNum+"' AND cw.IS_DELETE='0';";
		Query query = session.createSQLQuery(queryString).addEntity(CityWarehouse.class);
		List<CityWarehouse> list = query.list();
		if (list.size() > 0){
			return list.get(0);
			}
		else{
			return null;
			}
	}

	@Override
	public List<Object[]> listProductByCityWarehouseAndColdStorage(Long warehouseId, String selectedColdStorageName) {
		Object [] values={warehouseId,selectedColdStorageName};
		// TODO Auto-generated method stub
		return list("SELECT Distinct cw.procurementProduct.id,cw.procurementProduct.name FROM CityWarehouse cw WHERE cw.coOperative.id=? AND cw.coldStorageName=?", values);
				
		
	}
	
	@Override
	public List<String> listBatchNoByCityWarehouseAndColdStorageAndProduct(Long warehouseId, String selectedColdStorageName,Long productId) {
		Object [] values={warehouseId,selectedColdStorageName,productId};
		// TODO Auto-generated method stub
		return list("SELECT Distinct cwd.batchNo FROM CityWarehouseDetail cwd WHERE cwd.cityWarehouse.coOperative.id=? AND cwd.cityWarehouse.coldStorageName=? AND cwd.cityWarehouse.procurementProduct.id=? AND cwd.cityWarehouse.grossWeight>0 AND cwd.cityWarehouse.numberOfBags>0", values);
				
		
	}
	@Override
	public List<CityWarehouseDetail> listCityWareHouseDetailByWarehouseIdGradeCodeAndProductIdAndColdStorageNameAndBatchNo(long warehouseId,
			long productId,String coldStorageName,String batchNo) {

		Object[] values = { warehouseId,  productId, coldStorageName, batchNo};
		return list(
				"FROM CityWarehouseDetail cwd WHERE cwd.cityWarehouse.coOperative.id=? AND  cwd.cityWarehouse.procurementProduct.id=? AND cwd.cityWarehouse.coldStorageName=?AND cwd.batchNo=? AND cwd.cityWarehouse.grossWeight>0 AND cwd.cityWarehouse.numberOfBags>0 GROUP BY cwd.cityWarehouse.id ",
				values);
	}

	@Override
	public List<BaleGeneration> findBaleGenerationByLotNo(String lotNo, String tenantId) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("From BaleGeneration bg left join fetch bg.ginningProcess gp where bg.lotNo= :lotNo group by bg.lotNo,bg.heap");
		query.setParameter("lotNo", lotNo);
		List<BaleGeneration> baleList = query.list();
		session.flush();
		session.close();
		return baleList;
		
	}
	
	public List<CropCalendarDetail> listCropCalendarDetailByProcurementVarietyId(long id,String currentSeasonCode) {

		Object[] bindValues = { id, currentSeasonCode};

		return list(
				"FROM CropCalendarDetail ccd WHERE ccd.cropCalendar.procurementVariety.id=? AND ccd.cropCalendar.seasonCode=? ORDER BY ccd.cropCalendar.revisionNo DESC",
				bindValues);
	}

	
	@Override
	public void updateProcurementVarietyRevisionNo(long varietyId,long revisionNo) {
		// TODO Auto-generated method stub
		
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(
				"UPDATE ProcurementVariety pv set pv.revisionNo=" + revisionNo + " WHERE pv.id=" + varietyId + "");
		
		int result = query.executeUpdate();
		session.flush();
		session.close();

	}
	
	public List<Object[]> listActivityByCalendarIdAndVarietyId(long varietyId,String seasonCode) {
		Object [] values={varietyId,seasonCode};
		// TODO Auto-generated method stub
		return list("SELECT ccd.activityMethod,SUM(ccd.noOfDays) FROM CropCalendarDetail ccd where ccd.cropCalendar.procurementVariety.id=? AND ccd.cropCalendar.seasonCode=? GROUP BY ccd.activityMethod ORDER BY ccd.noOfDays ASC", values);
				
		
	}
	
	public List<OfflineFarmCropCalendar> listPendingOfflineFarmCropCalendarList(String tenantID) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantID).openSession();
		List<OfflineFarmCropCalendar> result = session.createQuery("FROM OfflineFarmCropCalendar ofcc WHERE ofcc.statusCode=2")
				.list();
		session.flush();
		session.close();
		return result;
	}
	
	@Override
	public void updateOfflineFarmCropCalendar(OfflineFarmCropCalendar offlineFarmCropCalendar, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(offlineFarmCropCalendar);
		sessions.flush();
		sessions.close();
	}
	
	@Override
	public void saveFarmCropCalendar(FarmCropCalendar farmCropCalendar, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(farmCropCalendar);
		sessions.flush();
		sessions.close();
	}
	
	public List<Object[]> listFarmCropCalendarByFarmAndSeason(long farmId,String seasonCode) {
		Object [] values={farmId,seasonCode};
		// TODO Auto-generated method stub
		return list("SELECT pp.name,pv.name,fccd.activityMethod,fccd.status,fccd.date,fccd.remarks FROM FarmCropCalendarDetail fccd LEFT JOIN fccd.farmCropCalendar fcc LEFT JOIN fcc.variety pv LEFT JOIN pv.procurementProduct pp where fcc.farm.id=? AND fcc.seasonCode=? GROUP BY fccd.id", values);
				
		
	}
	
	@Override
	public ColdStorage findColdStorageByFarmerAndBatchNo(Long farmerId, String batchNo) {
		Object [] values={farmerId,batchNo};
		ColdStorage coldStorage = (ColdStorage) find("FROM ColdStorage cs WHERE cs.farmer.id=? AND cs.batchNo=?", values);
		return coldStorage;
		
	}
	
	@Override
	public ColdStorage findColdStorageByBatchNo(String batchNo) {
		Object [] values={batchNo};
		ColdStorage coldStorage = (ColdStorage) find("FROM ColdStorage cs WHERE cs.batchNo=?", values);
		return coldStorage;
		
	}
	
	@Override
	public GinningProcess findGinningByGinningId(long ginningId) {
		// TODO Auto-generated method stub
		return (GinningProcess) find ("From GinningProcess gp where gp.id=?",ginningId);
	}
	
	@Override
	public String findIcsNameByIcsCode(String ics) {
		// TODO Auto-generated method stub
		Session session = getSessionFactory().openSession();
		Query query = session.createSQLQuery("SELECT w.name FROM catalogue_value w  WHERE FIND_IN_SET(code,:ics)");
		query.setParameter("ics", ics);
		List<String> codeList = query.list();
		String codeVal = String.join(", ", codeList); 
		session.flush();
		session.close();
		return codeVal;
		
		
	}

	@Override
	public List<Object[]> findFarmerNameByFarmerId(String farmer) {
		// TODO Auto-generated method stub
				Session session = getSessionFactory().openSession();
				Query query = session.createSQLQuery("select CONCAT(f.FIRST_NAME,'  ',IFNULL(f.LAST_NAME,'')),v.`NAME` as villge_Name,cv.name as ICS_NAME from farmer f "
						+ "join village v on v.id = f.VILLAGE_ID join catalogue_value cv on cv.`CODE` = f.ICS_NAME WHERE FIND_IN_SET(f.ID,:farmer)");
				query.setParameter("farmer", farmer);
				List<Object[]> codeList = query.list();
				//String codeVal = String.join(", ", codeList); 
				session.flush();
				session.close();
				return codeList;
				
	}

	@Override
	public HeapData findHeapDataByGinningHeapCodeAndProduct(String heapCode, long ginningId, long prodId,
			String currentTenantId,String season) {
		Session session = getSessionFactory().withOptions().tenantIdentifier(currentTenantId).openSession();
		Query query = session.createQuery("FROM HeapData hd where hd.code=:heap and hd.ginning.id=:ginning and hd.procurementProduct.id=:product and hd.season=:season");
		query.setParameter("heap", heapCode);
		query.setParameter("ginning", ginningId);
		query.setParameter("product", prodId);
		query.setParameter("season", season);
		List<HeapData> procurementList = query.list();
		HeapData procurement = null;
		if (procurementList.size() > 0) {
			procurement = (HeapData) procurementList.get(0);
		}

		session.flush();
		session.close();
		return procurement;
	}
	
	@Override
	public List<Object[]> listWarehouseProductByWarehouseId(String warehouseId,String season) {
		// TODO Auto-generated method stub
		Object[] values = { Long.valueOf(warehouseId) ,season};
		return list("SELECT DISTINCT g.heapCode,(SELECT DISTINCT fc.name from FarmCatalogue fc where fc.code=g.heapCode) from GinningProcess g WHERE g.ginning.id=? and g.season=?", values);
	}

	@Override
	public List<Object[]> listOfGinningDateByHeap(String ginningId ,String heapCode,String season) {
		// TODO Auto-generated method stub
		Object[] values = {Long.valueOf(ginningId), heapCode,season};
		return list("SELECT g,id,g.processDate FROM GinningProcess g where g.ginning.id=? AND g.heapCode=? AND g.season=?", values);

	}
	@Override
	public List<FarmCrops> listfarmCropsByProcurementVarietyId(Long id){
		return list("FROM FarmCrops f WHERE f.procurementVariety.id=? ", id);
	}

	@Override
	public List<Object[]> listPMTReceiptNumberByWarehouseAndSeason(long warehouseId, String season) {
		Object[] args = { warehouseId, PMT.Status.COMPLETE.ordinal(),season };
		return (List<Object[]>) list(
				"select DISTINCT pmtd.pmt.mtntReceiptNumber,pmtd.pmt.statusCode from PMTDetail pmtd where pmtd.coOperative.id=? AND pmtd.pmt.mtntReceiptNumber!=null and pmtd.mtnrGrossWeight>0 and pmtd.status!=? and pmtd.pmt.seasonCode=?",
				args);
	}

	@Override
	public OfflineGinningProcess findGinningProcessByMessageNo(String messageNo) {
		return (OfflineGinningProcess) find("FROM OfflineGinningProcess f WHERE f.messageNo=? AND f.statusCode in (0,2)", messageNo);
	}

	@Override
	public OfflineBaleGeneration findBaleGenerationByMessageNo(String messageNo) {
		return (OfflineBaleGeneration) find("FROM OfflineBaleGeneration f WHERE f.messageNo=? AND f.statusCode in (0,2)", messageNo);
	}

	@Override
	public OfflineSpinningTransfer findSpinningTransferByMessageNo(String messageNo) {
		return (OfflineSpinningTransfer) find("FROM OfflineSpinningTransfer f WHERE f.messageNo=? AND f.statusCode in (0,2)", messageNo);
	}

	@Override
	public OfflineMTNT findOfflineMTNTByMessageNo(String messageNo) {
		return (OfflineMTNT) find("FROM OfflineMTNT f WHERE f.messageNo=? AND f.statusCode in (0,2)", messageNo);
	}

	@Override
	public OfflinePMTNR findOfflinePMTNRByMessageNo(String messageNo) {
		return (OfflinePMTNR) find("FROM OfflinePMTNR f WHERE f.messageNo=? AND f.statusCode in (0,2)", messageNo);
	}

	@Override
	public OfflineProcurementTraceability findOfflineProcurementTraceabilityByMessageNo(String messageNo) {
		return (OfflineProcurementTraceability) find("FROM OfflineProcurementTraceability f WHERE f.messageNo=? AND f.statusCode in (0,2)", messageNo);
	}

	@Override
	public List<BaleGeneration> findBaleGenerationByGinningIdAndHeap(long ginning, String heap, int status) {
		Object[] parms={ginning,status};
		return list("FROM BaleGeneration bg where bg.ginning.id=? and bg.status=? and bg.heap in("+heap+")", parms);
	}

	@Override
	public List<String> listOfLotNoFromBaleGeneration() {
		return list("SELECT DISTINCT bg.lotNo from BaleGeneration bg");
	}
	
	
	@Override
	public List<Object[]> listFarmerByICS(String farmerId) {		
		Session session = getSessionFactory().openSession();
		String queryString = "select cv.NAME,GROUP_CONCAT((CONCAT(f.FIRST_NAME,' ',f.LAST_NAME))) from farmer f LEFT JOIN catalogue_value cv on cv.CODE=f.ICS_NAME where f.id IN("+farmerId+") GROUP BY f.ICS_NAME";
		Query query = session.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		session.flush();
		session.close();
		return list;
	}

	@Override
	public List<ColdStorageStockTransferDetail> listColdStorageStockTransferByLotCode(String lotCode) {
	
		return list("FROM ColdStorageStockTransferDetail cstd WHERE cstd.batchNo=?", lotCode);
	}
	
	
	@Override
	public LoanDistribution findLoanDistributionById(long id) {
		// TODO Auto-generated method stub
		return (LoanDistribution) find("FROM LoanDistribution p WHERE p.id=?", id);
	}

	@Override
	public List<DistributionBalance> findDistributionBalanceByFarmerAndProductIdAndVendorId(Long id, String pCode, Long vendorId) {
		// TODO Auto-generated method stub
		Object[] values = { id,vendorId, pCode };
		return list("FROM DistributionBalance db where db.farmer.id=" + "'" + id+ "'" + " AND db.vendor.id=" + "'" + vendorId+ "'" + " AND db.product.id IN("
				+ pCode + ")");
	}

	@Override
	public List<SubCategory> listSubCategory() {
		// TODO Auto-generated method stub
		return list("From SubCategory sc");
	}

	@Override
	public List<Product> listProductsBySubCategoryId(Long categoryId) {
		// TODO Auto-generated method stub
		 return list("FROM Product p WHERE p.subcategory.id = ? ORDER BY p.name ASC", categoryId);
	}



	@Override
	public OfflineProcurement findOfflineProcurementByReceipotNo(String receipt) {
		OfflineProcurement offlineProcurement = (OfflineProcurement) find("FROM OfflineProcurement op WHERE op.receiptNo = ?",
				receipt);
		return offlineProcurement;
	}
	@Override
	public List<LoanLedger> findFarmerLoanByFarmerIdInLoanLedger(String id, String tenantId){
		
	Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
	Query query = session.createQuery("FROM LoanLedger ll WHERE ll.farmerId=:id AND ll.txnType=:txnType AND ll.loanStatus!=:loanStatus ORDER BY DATE(ll.txnTime) ASC");
	query.setParameter("id", id);
	query.setParameter("txnType", "701");
	query.setParameter("loanStatus", 0);
	
	List<LoanLedger> loanLedgerList = query.list();
	session.flush();
    session.close();
	return loanLedgerList;
	}
	
	@Override
	public List<Vendor> findVendorById(String vendorId) {	
			List<String> vlist = new ArrayList<String>(Arrays.asList(vendorId.split(",")));
			Session session = getSessionFactory().openSession();  
		    String queryString="FROM Vendor v where v.vendorId in (:vid)";
			Query query = session.createQuery(queryString).setParameterList("vid",vlist);		
			List<Vendor> vendor= query.list();
			session.flush();
	        session.close();
			return vendor;
			
		
	}
	
	
	public List<Vendor> listPriorityVendor() {

		return list("FROM Vendor ve WHERE ve.priority IS NOT NULL ORDER BY ve.priority ASC");

	}

	@Override
	public List<AgroTransaction> findAgroTxnByVendorIdTXnTypesSeasonCode(String farmerId, String seasonCode,
			String[] txnTypes, Date startDate, Date endDate) {
		Session session = getSessionFactory().getCurrentSession();

		String queryString = "from AgroTransaction ta where ta.vendorId=:FARMER_ID AND ta.txnType in (:TXN_TYPE) AND ta.profType=02 AND ta.txnTime BETWEEN :startDate AND :endDate ORDER BY ta.id DESC";
		
		Query query = session.createQuery(queryString);
		
		query.setParameter("FARMER_ID",farmerId);
		//query.setParameter("SEASON_CODE",seasonCode);
		query.setParameterList("TXN_TYPE", txnTypes);
		query.setParameter("startDate", startDate).setParameter("endDate", endDate);
		
		List<AgroTransaction> result = (List<AgroTransaction>) query.list();
			return result;
	}

	@Override
	public LoanDistributionDetail findLoanDistributionDetailByIdd(long id) {
		// TODO Auto-generated method stub
		return (LoanDistributionDetail) find("FROM LoanDistributionDetail p WHERE p.id=?", id);
	}
	
	@Override
	public List<LoanDistributionDetail> findLoanDistributionDetailById(long pmtId) {
		return list("FROM LoanDistributionDetail pd where pd.loanDistribution.id=?",pmtId);
	}

	@Override
	public CityWarehouse findCityWarehouseByProductGradeWarehouse(long prodId, String grade, String tenantId,
			Long warehouseId) {
		
		Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM CityWarehouse cw WHERE cw.procurementProduct.id=" + prodId+ " AND cw.quality='" + grade + "'AND cw.coOperative.id='" + warehouseId + "'");
		List list = query.list();
		CityWarehouse cityWarehouse = null;
		if (list.size() > 0) {
			cityWarehouse = (CityWarehouse) list.get(list.size() - 1);
		}
		session.flush();
		session.close();
		return cityWarehouse;
	}
	
	@Override
	public List<CityWarehouseDetail> listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(long warehouseId,
			long productId,String coldStorageName,String[] batchNo) {

		Session session = getSessionFactory().getCurrentSession();

		String queryString = "FROM CityWarehouseDetail cwd INNER JOIN cwd.cityWarehouse cw INNER JOIN cw.procurementProduct pp INNER JOIN cw.coOperative co WHERE cw.coOperative.id=:warehouseId AND pp.id=:productId AND cw.coldStorageName=:coldStorageName AND cwd.batchNo IN (:batchNo) ";
		
		Query query = session.createQuery(queryString);
		
		query.setParameter("warehouseId",warehouseId);
		query.setParameter("productId",productId);
		query.setParameter("coldStorageName",coldStorageName);
		query.setParameterList("batchNo", batchNo);
		
		List<CityWarehouseDetail> result = query.list();
		
			return result;
	
	}
	
	public List<Object[]> listCityWareHouseDetailByWarehouseIdAndProductIdAndColdStorageNameAndBatchNo(long warehouseId,
			long productId,String coldStorageName,String batchNo) {

		Session session = getSessionFactory().openSession();
		String queryString = "SELECT cwd.id AS ID,f.ID AS FARMER_ID,f.FIRST_NAME AS FIRST_NAME,pp.id AS PRODUCT_ID,pp.NAME AS PRODUCT_NAME,pp.UNIT AS UNIT,pv.id AS VARIETY_ID,pv.NAME AS VARIETY_NAME,cwd.BLOCK_NAME AS BLOCK_CODE,(SELECT catalogue_value.NAME FROM catalogue_value WHERE ( catalogue_value.CODE = cwd.BLOCK_NAME )) AS BLOCK_NAME,cwd.FLOOR_NAME AS FLOOR_CODE,(SELECT catalogue_value.NAME FROM catalogue_value WHERE ( catalogue_value.CODE = cwd.FLOOR_NAME )) AS FLOOR_NAME,cwd.BAY_NUMBER AS BAY_CODE,(SELECT catalogue_value.NAME FROM catalogue_value WHERE ( catalogue_value.CODE = cwd.BAY_NUMBER )) AS BAY_NUMBER,cw.NUMBER_OF_BAGS,cw.GROSS_WEIGHT,cwd.BATCH_NO AS BATCH_NO  FROM city_warehouse_detail cwd LEFT JOIN city_warehouse cw ON cw.id = cwd.CITY_WAREHOUSE_ID LEFT JOIN procurement_variety pv ON cw.QUALITY = pv.CODE LEFT JOIN procurement_product pp ON pp.id = cw.PROCUREMENT_PRODUCT_ID LEFT JOIN farmer f ON f.id = cw.FARMER_ID WHERE cw.CO_OPERATIVE_ID = '"+warehouseId+"' AND cw.PROCUREMENT_PRODUCT_ID = '"+productId+"' AND cw.COLD_STORAGE_NAME = '"+coldStorageName+"' AND cwd.BATCH_NO IN ("+batchNo+") AND cw.GROSS_WEIGHT > 0 AND cw.NUMBER_OF_BAGS > 0 GROUP BY cwd.CITY_WAREHOUSE_ID";
		Query query = session.createSQLQuery(queryString);
		List<Object[]> list = query.list();
		session.flush();
		session.close();
		return list;
	
	}
	
	public List<ProcurementProduct> listProcurementProductByBranch(String branch) {

		return list("FROM ProcurementProduct pp where pp.branchId=? ORDER BY pp.name", branch);
	}
	
	
	public List<ProcurementProduct> listProcurementProductByRevisionNoByBranch(Long revisionNo,String branch) {
		Object[] values = { revisionNo, branch };
		return list("FROM ProcurementProduct pp where pp.revisionNo>? and pp.branchId=? ORDER BY pp.revisionNo DESC", values);
	}
	
	public List<ProcurementVariety> listProcurementProductVarietyByRevisionNoByBranch(Long revisionNo,String branch) {
		Object[] values = { revisionNo, branch };
		return list("FROM ProcurementVariety pv where pv.revisionNo>? and pv.branchId=? ORDER BY pv.revisionNo DESC", values);
	}
	@Override
	public List<ColdStorageStockTransferDetail> listColdStorageStockTransferByLotCodeAndColdStorageCode(String lotCode,String coldStorageCode,String warehouseCode,String chamberCode,String floorCode,String bayCode) {
		Object[] values = { lotCode, coldStorageCode ,warehouseCode,chamberCode,floorCode,bayCode};
		return list("FROM ColdStorageStockTransferDetail cstd WHERE cstd.batchNo=? AND cstd.coldStorageStockTransfer.coldStorageName=? AND cstd.coldStorageStockTransfer.warehouse.code=? AND cstd.blockName=? AND cstd.floorName=? AND cstd.bayNumber=?", values);
	}

	@Override
	public List<String> listLotNoFromFarmerTraceabilityData() {
		// TODO Auto-generated method stub
		return list("SELECT Distinct lt.lotNo from LotTraceabilityData lt order by cast(lt.lotNo as integer) asc");
	}

}
