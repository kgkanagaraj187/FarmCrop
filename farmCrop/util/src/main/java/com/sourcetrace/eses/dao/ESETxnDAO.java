/*
 * ESETxnDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ese.entity.txn.mfi.MFITransaction;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.Distribution;
import com.sourcetrace.eses.order.entity.txn.DistributionDetail;
import com.sourcetrace.eses.order.entity.txn.InventoryDetail;
import com.sourcetrace.eses.order.entity.txn.OrderDetail;
import com.sourcetrace.eses.order.entity.txn.OrderTaking;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.esesw.entity.txn.ESEMTTxn;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnHeader;
import com.sourcetrace.esesw.entity.txn.ESETxnType;
import com.sourcetrace.esesw.entity.txn.TxnBulkImport;

/**
 * The Class TxnDAO.
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
@Repository
@Transactional
public class ESETxnDAO extends ESEDAO implements IESETxnDAO {
	
	@Autowired
	  public ESETxnDAO(SessionFactory sessionFactory) {

	    this.setSessionFactory(sessionFactory);
	   }

	  private static final Logger LOGGER = Logger.getLogger(ESETxnDAO.class.getName());

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#findTxnTypeByCode(java.lang. String)
	     */
	    public ESETxnType findTxnTypeByCode(String code) {

	        return (ESETxnType) find("From ESETxnType eseTxnType where eseTxnType.code=?", code);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#findDuplication(java.util.Date,
	     * java.lang.String, long)
	     */
	    public ESETxn findDuplication(Date txnTime, String serialNo, String msgNo) {

	        Object[] bind = { txnTime, serialNo, msgNo };
	        return (ESETxn) find(
	                "FROM ESETxn eseTxn WHERE eseTxn.txnTime = ? and  eseTxn.header.serialNo=? and eseTxn.header.msgNo=? ",
	                bind);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#findTxnBySerialNumber(java.lang .String)
	     */
	    public boolean findTxnBySerialNumber(String serialNumber) {

	        List<ESETxnHeader> txnHeaderList = list("From ESETxnHeader eth where eth.serialNo=?",
	                serialNumber);
	        boolean isTxnHeaderExist = false;
	        if (txnHeaderList.size() > 0) {
	            isTxnHeaderExist = true;
	        }
	        return isTxnHeaderExist;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listEseMTTxnByAccountNo(java .lang.String)
	     */
	    public List<ESEMTTxn> listEseMTTxnByAccountNo(String accountNo) {

	        return (List<ESEMTTxn>) list("From ESEMTTxn ca where ca.accountNo=?", accountNo);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listEseMTTxnByAccountNo(java .lang.String, int,
	     * int)
	     */
	    public List<ESEMTTxn> listEseMTTxnByAccountNo(String accountNo, int startIndex, int limit) {

	        Session session = getSessionFactory().openSession();
	        String queryString = "select * FROM ese_mt_txn WHERE ACCOUNT_NO = '" + accountNo
	                + "' LIMIT " + startIndex + "," + limit;
	        Query query = session.createSQLQuery(queryString).addEntity(ESEMTTxn.class);
	        List<ESEMTTxn> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#findTxnExistForServicePoint( java.lang.String)
	     */
	    @SuppressWarnings("unchecked")
	    public boolean findTxnExistForServicePoint(String columnTxnValue) {

	        List<ESETxnHeader> txnHeaderList = list("From ESETxnHeader eth where eth.servPointId = ?",
	                columnTxnValue);
	        boolean isTxnHeaderExist = false;
	        if (txnHeaderList.size() > 0) {
	            isTxnHeaderExist = true;
	        }
	        return isTxnHeaderExist;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#findTxnExistForClient(java.lang .String)
	     */
	    public boolean findTxnExistForClient(String profileId) {

	        List<MFITransaction> clientTxnList = list(
	                "From MFITransaction mfi where mfi.profileId = ?", profileId);
	        boolean isClientTxnExist = false;
	        if (clientTxnList.size() > 0) {
	            isClientTxnExist = true;
	        }
	        return isClientTxnExist;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#findTxnIdWithStatusMsg(java. lang.String)
	     */
	    public TxnBulkImport findTxnBulkImportByTxnId(String txnId) {

	        return (TxnBulkImport) find("FROM TxnBulkImport import WHERE import.txnId = ?", txnId);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#findTxnByTxnId(java.lang.String)
	     */
	    public TxnBulkImport findTxnByTxnId(String txnId) {

	        TxnBulkImport txnBulkImport = (TxnBulkImport) find(
	                "FROM TxnBulkImport import WHERE import.txnId=?", txnId);
	        return txnBulkImport;

	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#findBulkUpload()
	     */
	    public List<TxnBulkImport> listBulkUpload() {

	        List<TxnBulkImport> txnBulkImport = list("From TxnBulkImport import where import.statusCode = 2");
	        return txnBulkImport;

	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listOrderDeatail(long)
	     */
	    @SuppressWarnings("unchecked")
	    public List<OrderDetail> listOrderDeatail(long id) {

	        return list("From OrderDetail orders Where orders.orderTaking.id = ?", id);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listOrderDeatail(long, int, int)
	     */
	    public List<OrderDetail> listOrderDeatail(long id, int startIndex, int limit) {

	        Session session = getSessionFactory().openSession();
	        String queryString = "select * FROM order_detail WHERE ORDER_TAKING_ID = '" + id
	                + "' LIMIT " + startIndex + "," + limit;
	        Query query = session.createSQLQuery(queryString).addEntity(OrderDetail.class);
	        List<OrderDetail> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listStoreNameFromOrderTaking()
	     */
	    public List<String> listStoreNameFromOrderTaking() {

	        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
	                .createCriteria(OrderTaking.class);
	        criteria.setProjection(Projections.distinct(Projections.property("storeName")));
	        List<String> storeNames = criteria.list();
	        return storeNames;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listTopProducts()
	     */
	    public List<Object[]> listTopProducts() {

	        Session session = getSessionFactory().openSession();
	        String queryString = "SELECT  OD.PRODUCT_NAME , COALESCE(SUM(OD.SUB_TOTAL),0 ) AS TOTAL_PRICE FROM order_detail AS OD GROUP BY OD.CATEGORY_NAME, OD.PRODUCT_NAME ORDER BY TOTAL_PRICE DESC LIMIT 5";
	        Query query = session.createSQLQuery(queryString);
	        List<Object[]> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listTopAgents()
	     */
	    public List<Object[]> listTopAgents() {

	        Session session = getSessionFactory().openSession();
	        String queryString = "SELECT ORDER.AGENT_NAME, COALESCE(Sum(`ORDER`.TOTAL_ORDER_PRICE),0 ) AS TOTAL_ORDER_PRICE FROM order_taking AS `ORDER` GROUP BY `ORDER`.AGENT_ID ORDER BY TOTAL_ORDER_PRICE DESC LIMIT 5";
	        Query query = session.createSQLQuery(queryString);
	        List<Object[]> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listTopAgentsByOrder()
	     */
	    public List<Object[]> listTopAgentsByOrder() {

	        Session session = getSessionFactory().openSession();
	        String queryString = "SELECT INV.AGENT_NAME,INV.AGENT_ID,COALESCE(SUM(INV.TOTAL_QTY), 0) AS TOTAL_ORDER_QTY,COALESCE(SUM(INV.GRAND_TOTAL), 0) AS TOTAL_ORDER_PRICE FROM inventory AS INV WHERE INV.TYPES <> 1 GROUP BY INV.AGENT_ID ORDER BY TOTAL_ORDER_PRICE DESC LIMIT 5";
	        Query query = session.createSQLQuery(queryString);
	        List<Object[]> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listTopShopDealersByOrder()
	     */
	    public List<Object[]> listTopShopDealersByOrder() {

	        Session session = getSessionFactory().openSession();
	        String queryString = "SELECT INV.SHOP_DEALER_NAME,INV.SHOP_DEALER_ID,COALESCE(SUM(INV.TOTAL_QTY), 0) AS TOTAL_ORDER_QTY,COALESCE(SUM(INV.GRAND_TOTAL), 0) AS TOTAL_ORDER_PRICE FROM inventory AS INV WHERE INV.TYPES <> 1 GROUP BY INV.SHOP_DEALER_ID ORDER BY TOTAL_ORDER_PRICE DESC LIMIT 5";
	        Query query = session.createSQLQuery(queryString);
	        List<Object[]> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listTopProductsByOrder()
	     */
	    public List<Object[]> listTopProductsByOrder() {

	        Session session = getSessionFactory().openSession();
	        String queryString = "SELECT p.NAME,p.CODE,COALESCE(SUM(inv_det.QUANTITY), 0) AS TOTAL_ORDER_QTY,COALESCE((SUM(inv_det.QUANTITY)*inv_det.PRICE_PER_UNIT), 0) TOTAL_ORDER_PRICE FROM inventory_detail inv_det LEFT JOIN inventory inv ON inv.id = inv_det.INVENTORY_ID LEFT JOIN product p ON p.id = inv_det.PRODUCT_ID WHERE inv.TYPES <> 1 GROUP BY inv_det.PRODUCT_ID ORDER BY TOTAL_ORDER_PRICE DESC LIMIT 5";
	        Query query = session.createSQLQuery(queryString);
	        List<Object[]> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#findTxnExistForProductByProductId (long)
	     */
	    @SuppressWarnings("unchecked")
	    public boolean findTxnExistForProductByProductId(long id) {

	        List<InventoryDetail> inventoryDetailList = list(
	                "From InventoryDetail ind where ind.product.id = ?", id);
	        if (!ObjectUtil.isListEmpty(inventoryDetailList) && inventoryDetailList.size() > 0) {
	            return true;
	        } else {
	            List<DistributionDetail> distributionDetailList = list(
	                    "From DistributionDetail dd where dd.product.id = ?", id);
	            if (!ObjectUtil.isListEmpty(distributionDetailList)
	                    && distributionDetailList.size() > 0) {
	                return true;
	            }
	        }
	        return false;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#findTxnExistForFarmer(java.lang .String)
	     */
	    public boolean findTxnExistForFarmer(String farmerId) {

	        List<AgroTransaction> txnList = list("From AgroTransaction atn WHERE atn.farmerId = ? ",
	                farmerId);
	        if (!ObjectUtil.isListEmpty(txnList)) {
	            return true;
	        }
	        return false;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listTopAgentsByProcurement(int)
	     */
	    @SuppressWarnings("unchecked")
	    public List<Object[]> listTopAgentsByProcurement(String seasonCode, int limitValue) {

	        String queryString = "SELECT trxn_agro.AGENT_NAME , SUM( procurement_detail.NUMBER_OF_BAGS) as QUANTITY,SUM(trxn_agro.TXN_AMT) AS AMOUNT FROM trxn_agro "
	                + "INNER JOIN procurement ON trxn_agro.id = procurement.TRXN_AGRO_ID "
	                + "INNER JOIN procurement_detail ON procurement.id = procurement_detail.PROCUREMENT_ID "
	                + "INNER JOIN season_master ON procurement.SEASON_ID=season_master.ID "
	                + "WHERE season_master.CODE=:seasonCode "
	                + "GROUP BY procurement_detail.PROCUREMENT_PRODUCT_ID,trxn_agro.AGENT_ID ORDER BY AMOUNT DESC,QUANTITY DESC LIMIT :limitValue ";
	        Session session = getSessionFactory().openSession();
	        Query query = session.createSQLQuery(queryString).setParameter("seasonCode", seasonCode)
	                .setParameter("limitValue", limitValue);
	        List<Object[]> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listTopFarmersByProcurement(int)
	     */
	    @SuppressWarnings("unchecked")
	    public List<Object[]> listTopFarmersByProcurement(String seasonCode, int limitValue) {

	        String queryString = "SELECT trxn_agro.FARMER_NAME ,SUM(procurement_detail.NUMBER_OF_BAGS) AS QUANTITY,SUM(trxn_agro.TXN_AMT) AS AMOUNT FROM trxn_agro "
	                + "INNER JOIN procurement ON trxn_agro.id = procurement.TRXN_AGRO_ID "
	                + "INNER JOIN procurement_detail ON procurement.id = procurement_detail.PROCUREMENT_ID "
	                + "INNER JOIN season_master ON procurement.SEASON_ID=season_master.ID "
	                + "WHERE season_master.CODE=:seasonCode "
	                + "GROUP BY procurement_detail.PROCUREMENT_PRODUCT_ID,trxn_agro.FARMER_ID ORDER BY AMOUNT DESC,QUANTITY DESC LIMIT :limitValue ";
	        Session session = getSessionFactory().openSession();
	        Query query = session.createSQLQuery(queryString).setParameter("seasonCode", seasonCode)
	                .setParameter("limitValue", limitValue);
	        List<Object[]> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.esesw.dao.txn.IESETxnDAO#listEnrollmentDataByYearAgent(java.lang.String[],
	     * java.lang.String, int)
	     */
	    @SuppressWarnings("unchecked")
	    public List<Object[]> listEnrollmentDataByYearAgent(String agentId, String year, int month) {

	        String filterCondition1 = " ";
	        String filterCondition2 = " ";
	        if (!StringUtil.isEmpty(agentId)) {
	            filterCondition1 = " AND  ese_txn_header.AGENT_ID='" + agentId + "' ";
	            filterCondition2 = " AND offline_farmer_enrollment.ENROLLED_AGENT_ID='" + agentId
	                    + "' ";
	        }
	        // Getting count from ese_txn_header for Farmer Enrollment with online mode
	        // Getting count from offline_farmer_enrollment for Farmer Enrollment with offline mode
	        // Left joining with months_table to get all month data
	        String queryString = "SELECT months_table.month_name AS MONTH_NAME,IFNULL(table1.ENROLLMENT_COUNT, 0) AS ENROLLMENT_COUNT FROM months_table "
	                + "LEFT JOIN "
	                + "(SELECT  MONTH_NAME, SUM(ENROLLMENT_COUNT) AS ENROLLMENT_COUNT  FROM "
	                + "(SELECT DATE_FORMAT(ese_txn.TXN_TIME, '%b') AS MONTH_NAME,COUNT(*) AS ENROLLMENT_COUNT  FROM ese_txn "
	                + " INNER JOIN ese_txn_header ON ese_txn.ESE_TXN_HEADER_ID = ese_txn_header.ID "
	                + " INNER JOIN ese_txn_type ON ese_txn_header.ESE_TXN_TYPE_ID = ese_txn_type.ID AND ese_txn_type.ID = '8' "
	                + " WHERE YEAR(ese_txn.TXN_TIME) = :year AND ese_txn.`STATUS` = 0 AND ese_txn_header.mode='01' "
	                + filterCondition1
	                + "  GROUP BY DATE_FORMAT(ese_txn.TXN_TIME, '%Y%m') "
	                + " UNION "
	                + "SELECT DATE_FORMAT(offline_farmer_enrollment.ENROLL_DATE, '%b') AS MONTH_NAME,COUNT(*) AS ENROLLMENT_COUNT  FROM offline_farmer_enrollment "
	                + " WHERE YEAR(offline_farmer_enrollment.ENROLL_DATE) = :year AND offline_farmer_enrollment.TXN_TYPE = '308' AND offline_farmer_enrollment.STATUS_CODE = 0 "
	                + filterCondition2
	                + " GROUP BY DATE_FORMAT(offline_farmer_enrollment.ENROLL_DATE,'%Y%m' )) AS tab1 "
	                + " GROUP BY MONTH_NAME ) AS table1 "
	                + "ON months_table.month_name = table1.month_name LIMIT :month";

	        Session session = getSessionFactory().openSession();
	        Query query = session.createSQLQuery(queryString).setParameter("year", year).setParameter(
	                "month", month);
	        List<Object[]> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.dao.txn.IESETxnDAO#listDistributionData(java.lang.String, int)
	     */
	    @SuppressWarnings("unchecked")
	    public List<Object[]> listDistributionData(String agentId, String seasonCode, int limitValue) {

	        String filterCondition = " ";
	        if (!StringUtil.isEmpty(agentId)) {
	            filterCondition = " AND trxn_agro.AGENT_ID='" + agentId + "' ";
	        }
	        String queryString = "SELECT  CONCAT(SUBSTR(product.NAME,1,8),' ',SUBSTR(product.unit,1,4)) ,SUM(distribution_detail.QUANTITY) AS QUANTITY , SUM(trxn_agro.TXN_AMT) AS AMOUNT FROM trxn_agro "
	                + "INNER JOIN distribution ON trxn_agro.id = distribution.TRXN_AGRO_ID "
	                + "INNER JOIN distribution_detail ON distribution.ID = distribution_detail.DISTRIBUTION_ID "
	                + "INNER JOIN product ON distribution_detail.PRODUCT_ID = product.ID "
	                + "INNER JOIN season_master ON distribution.SEASON_ID=season_master.ID "
	                + "WHERE  trxn_agro.TXN_TYPE='"
	                + Distribution.PRODUCT_DISTRIBUTION_TO_FARMER
	                + "' AND trxn_agro.OPER_TYPE=1 AND  season_master.CODE='"
	                + seasonCode
	                + "'"
	                + filterCondition
	                + "GROUP BY distribution_detail.PRODUCT_ID ORDER BY AMOUNT DESC,QUANTITY DESC limit :limitValue";
	        Session session = getSessionFactory().openSession();
	        Query query = session.createSQLQuery(queryString).setParameter("limitValue", limitValue);
	        List<Object[]> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.esesw.dao.txn.IESETxnDAO#listTopDailyProductOrderByDate(java.lang.String,
	     * int)
	     */
	    @SuppressWarnings("unchecked")
	    public List<Object[]> listTopDailyProductOrderByDate(String selectedDate, int limitValue) {

	        String quertString = "SELECT p.NAME,COALESCE(SUM(inv_det.QUANTITY), 0) AS TOTAL_ORDER_QTY ,p.CODE "
	                + "FROM inventory_detail inv_det "
	                + "LEFT JOIN inventory inv ON inv.id = inv_det.INVENTORY_ID "
	                + "LEFT JOIN product p ON p.id = inv_det.PRODUCT_ID "
	                + "WHERE inv.OPER_TYPE=1 and inv.TYPES IN (0,2) and DATE_FORMAT(inv.ORDER_DATE,'%Y-%m-%d')=:selectedDate "
	                + "GROUP BY inv_det.PRODUCT_ID ORDER BY TOTAL_ORDER_QTY DESC limit :limitValue";
	        Session session = getSessionFactory().openSession();
	        Query query = session.createSQLQuery(quertString)
	                .setParameter("selectedDate", selectedDate).setParameter("limitValue", limitValue);
	        List<Object[]> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.esesw.dao.txn.IESETxnDAO#listTopDailyProductOrderSummaryByDate(java.lang.
	     * String, int)
	     */
	    @SuppressWarnings("unchecked")
	    public List<Object[]> listTopDailyProductOrderSummaryByDate(String orderDate, int limitValue) {

	        String quertString = "SELECT SUBSTR(PRODUCT_NAME,1,12),SUM(ORDER_QUANTITY),SUM(DELEIVERY_QUANTITY),SUM(ORDER_QUANTITY)-SUM(DELEIVERY_QUANTITY) AS PENDING_QUANTITY FROM "
	                + " ( SELECT P.`NAME` AS PRODUCT_NAME, "
	                + " SUM(IF((INV.TYPES=0 OR INV.TYPES=2 ),IDETAIL.QUANTITY,0)) AS ORDER_QUANTITY, "
	                + " 0 AS DELEIVERY_QUANTITY, "
	                + " P.`CODE` AS PRODUCT_CODE "
	                + " FROM INVENTORY INV INNER JOIN INVENTORY_DETAIL IDETAIL ON INV.ID=IDETAIL.INVENTORY_ID INNER JOIN PRODUCT P ON IDETAIL.PRODUCT_ID=P.ID "
	                + " WHERE INV.OPER_TYPE=1 AND INV.TYPES IN (0,2) AND  DATE_FORMAT(INV.ORDER_DATE,'%Y-%m-%d')=:orderDate GROUP BY P.`CODE` "
	                + " UNION "
	                + " SELECT P.`NAME` AS PRODUCT_NAME, "
	                + " 0 AS ORDER_QUANTITY, "
	                + " SUM(IDETAIL.QUANTITY) AS DELEIVERY_QUANTITY, "
	                + " P.`CODE` AS PRODUCT_CODE "
	                + " FROM INVENTORY I INNER JOIN INVENTORY_DETAIL IDETAIL ON I.ID=IDETAIL.INVENTORY_ID INNER JOIN PRODUCT P ON IDETAIL.PRODUCT_ID=P.ID "
	                + " WHERE I.ORDER_NO IN "
	                + " (SELECT INV.ORDER_NO FROM INVENTORY INV WHERE INV.TYPES IN (0,2) AND INV.OPER_TYPE='1' AND DATE_FORMAT(INV.ORDER_DATE,'%Y-%m-%d')=:orderDate ) "
	                + " AND I.TYPES IN (1,2)  AND I.OPER_TYPE='1' GROUP BY P.`CODE`) "
	                + " T1 GROUP BY PRODUCT_CODE ORDER BY ORDER_QUANTITY DESC limit :limitValue";
	        Session session = getSessionFactory().openSession();
	        Query query = session.createSQLQuery(quertString).setParameter("orderDate", orderDate)
	                .setParameter("limitValue", limitValue);
	        List<Object[]> list = query.list();
	        session.flush();
	        session.close();
	        return list;
	    }

   }
