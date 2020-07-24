/*
 * ESETxnService.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.service;



import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.IESETxnDAO;
import com.sourcetrace.eses.dao.ISequenceDAO;
import com.sourcetrace.eses.order.entity.txn.OrderDetail;
import com.sourcetrace.eses.order.entity.txn.OrderTaking;
import com.sourcetrace.esesw.entity.txn.ESEMTTxn;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnType;
import com.sourcetrace.esesw.entity.txn.TxnBulkImport;


/**
 * TxnService implements ITxnService and is used to manage txn.
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
@Service
@Transactional
public class ESETxnService implements IESETxnService {

	  private static final Logger LOGGER = Logger.getLogger(ESETxnService.class.getName());
	  @Autowired
	    private IESETxnDAO txnDAO;
	  @Autowired
	    private ISequenceDAO sequenceDAO;

	   

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#addTxn(com.sourcetrace
	     * .esesw.entity.txn.ESETxn)
	     */
	    public void addTxn(ESETxn txn) {

	        // set unique txn id for the txn
	        txn.setTxnId(Long.toString(sequenceDAO.getTxnSequence()));
	        txnDAO.save(txn);
	        txnDAO.update(txn);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#editTxn(com.sourcetrace
	     * .esesw.entity.txn.ESETxn)
	     */
	    public void editTxn(ESETxn txn) {

	        txnDAO.update(txn);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#findTxnTypeByCode(java .lang.String)
	     */
	    public ESETxnType findTxnTypeByCode(String code) {

	        return txnDAO.findTxnTypeByCode(code);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#findTxnBySerialNumber
	     * (java.lang.String)
	     */
	    public boolean findTxnBySerialNumber(String serialNumber) {

	        return txnDAO.findTxnBySerialNumber(serialNumber);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listEseMTTxnByAccountNo
	     * (java.lang.String)
	     */
	    public List<ESEMTTxn> listEseMTTxnByAccountNo(String accountNo) {

	        return txnDAO.listEseMTTxnByAccountNo(accountNo);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listEseMTTxnByAccountNo
	     * (java.lang.String, int, int)
	     */
	    public List<ESEMTTxn> listEseMTTxnByAccountNo(String accountNo, int startIndex, int limit) {

	        return txnDAO.listEseMTTxnByAccountNo(accountNo, startIndex, limit);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#findTxnExistForServicePoint
	     * (java.lang.String)
	     */
	    public boolean findTxnExistForServicePoint(String columnTxnValue) {

	        return txnDAO.findTxnExistForServicePoint(columnTxnValue);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#findTxnExistForClient
	     * (java.lang.String)
	     */
	    public boolean findTxnExistForClient(String profileId) {

	        return txnDAO.findTxnExistForClient(profileId);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#save(com.ese.entity. txn.TxnBulkImport)
	     */
	    public void save(TxnBulkImport txnBulkImport) {

	        txnDAO.save(txnBulkImport);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#findTxnIdWithStatusMsg
	     * (java.lang.String)
	     */
	    public TxnBulkImport findTxnBulkImportByTxnId(String txnId) {

	        return txnDAO.findTxnBulkImportByTxnId(txnId);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#update(com.ese.entity
	     * .txn.TxnBulkImport)
	     */
	    public void update(TxnBulkImport txnBulkImport) {

	        txnDAO.update(txnBulkImport);

	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#findTxnByTxnId(java. lang.String)
	     */
	    public TxnBulkImport findTxnByTxnId(String txnId) {

	        return txnDAO.findTxnByTxnId(txnId);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#findBulkUpload()
	     */
	    public List<TxnBulkImport> listBulkUpload() {

	        return txnDAO.listBulkUpload();
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.esesw.service.txn.IESETxnService#addOrderTaking(com.
	     * sourcetrace.eses.order.entity.txn.OrderTaking)
	     */
	    public void saveOrderTaking(OrderTaking orderTaking) {

	        txnDAO.save(orderTaking);
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.esesw.service.txn.IESETxnService#addorderDetail(com.
	     * sourcetrace.eses.order.entity.txn.OrderDetail)
	     */
	    public void saveOrderDetail(OrderDetail orderDetail) {

	        txnDAO.save(orderDetail);

	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listOrderDeatail(long)
	     */
	    public List<OrderDetail> listOrderDeatail(long id) {

	        return txnDAO.listOrderDeatail(id);

	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listOrderDeatail(long, int, int)
	     */
	    public List<OrderDetail> listOrderDeatail(long id, int startIndex, int limit) {

	        return txnDAO.listOrderDeatail(id, startIndex, limit);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listStoreNameFromOrderTaking ()
	     */
	    public List<String> listStoreNameFromOrderTaking() {

	        return txnDAO.listStoreNameFromOrderTaking();
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listTopProducts()
	     */
	    public List<Object[]> listTopProducts() {

	        return txnDAO.listTopProducts();
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listTopAgents()
	     */
	    public List<Object[]> listTopAgents() {

	        return txnDAO.listTopAgents();
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listTopAgentsByOrder()
	     */
	    public List<Object[]> listTopAgentsByOrder() {

	        return txnDAO.listTopAgentsByOrder();
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listTopShopDealersByOrder ()
	     */
	    public List<Object[]> listTopShopDealersByOrder() {

	        return txnDAO.listTopShopDealersByOrder();
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listTopProductsByOrder()
	     */
	    public List<Object[]> listTopProductsByOrder() {

	        return txnDAO.listTopProductsByOrder();
	    }

	    /*
	     * (non-Javadoc)
	     * @seecom.sourcetrace.esesw.service.txn.IESETxnService# findTxnExistForProductByProductId(long)
	     */
	    public boolean findTxnExistForProductByProductId(long id) {

	        return txnDAO.findTxnExistForProductByProductId(id);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#findESETxnexist(java .util.Date,
	     * java.lang.String, java.lang.String)
	     */
	    public ESETxn findESETxnexist(Date txnTime, String serialNo, String msgNo) {

	        return txnDAO.findDuplication(txnTime, serialNo, msgNo);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#findTxnExistForFarmer
	     * (java.lang.String)
	     */
	    public boolean findTxnExistForFarmer(String farmerId) {

	        return txnDAO.findTxnExistForFarmer(farmerId);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listTopAgentsByProcurement(int)
	     */
	    public List<Object[]> listTopAgentsByProcurement(String seasonCode, int limitValue) {

	        return txnDAO.listTopAgentsByProcurement(seasonCode, limitValue);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listTopFarmersByProcurement(int)
	     */
	    public List<Object[]> listTopFarmersByProcurement(String seasonCode, int limitValue) {

	        return txnDAO.listTopFarmersByProcurement(seasonCode, limitValue);
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.esesw.service.txn.IESETxnService#listEnrollmentDataByYearAgent(java.lang.
	     * String[], java.lang.String, int)
	     */
	    public List<Object[]> listEnrollmentDataByYearAgent(String agentId, String year, int month) {

	        return txnDAO.listEnrollmentDataByYearAgent(agentId, year, month);
	    }

	    /*
	     * (non-Javadoc)
	     * @see com.sourcetrace.esesw.service.txn.IESETxnService#listDistributionData(java.lang.String,
	     * int)
	     */
	    public List<Object[]> listDistributionData(String agentId, String seasonCode, int limitValue) {

	        return txnDAO.listDistributionData(agentId, seasonCode, limitValue);
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.esesw.service.txn.IESETxnService#listTopDailyProductOrderByDate(java.lang
	     * .String, int)
	     */
	    public List<Object[]> listTopDailyProductOrderByDate(String date, int limitValue) {

	        return txnDAO.listTopDailyProductOrderByDate(date, limitValue);
	    }

	    /*
	     * (non-Javadoc)
	     * @see
	     * com.sourcetrace.esesw.service.txn.IESETxnService#listTopDailyProductOrderSummaryByDate(java
	     * .lang.String, int)
	     */
	    public List<Object[]> listTopDailyProductOrderSummaryByDate(String dailyProductOrderDate,
	            int limitValue) {

	        return txnDAO.listTopDailyProductOrderSummaryByDate(dailyProductOrderDate, limitValue);
	    }


 }
