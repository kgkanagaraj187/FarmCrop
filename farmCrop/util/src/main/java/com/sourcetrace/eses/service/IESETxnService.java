/*
 * IESETxnService.java
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

import com.sourcetrace.eses.order.entity.txn.OrderDetail;
import com.sourcetrace.eses.order.entity.txn.OrderTaking;
import com.sourcetrace.esesw.entity.txn.ESEMTTxn;
import com.sourcetrace.esesw.entity.txn.ESETxn;
import com.sourcetrace.esesw.entity.txn.ESETxnType;
import com.sourcetrace.esesw.entity.txn.TxnBulkImport;

/**
 * ITxnService is the interface for managing transactions.
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */
public interface IESETxnService
{

	  /**
     * Adds the txn.
     * @param txn the txn
     */
    public void addTxn(ESETxn txn);

    /**
     * Edits the txn.
     * @param txn the txn
     */
    public void editTxn(ESETxn txn);

    /**
     * Find txn type by code.
     * @param code the code
     * @return the eSE txn type
     */
    public ESETxnType findTxnTypeByCode(String code);

    /**
     * Find txn by serial number.
     * @param serialNumber the serial number
     * @return true, if successful
     */
    public boolean findTxnBySerialNumber(String serialNumber);

    /**
     * List ese mt txn by account no.
     * @param accountNo the account no
     * @return the list< esemt txn>
     */
    public List<ESEMTTxn> listEseMTTxnByAccountNo(String accountNo);

    /**
     * List ese mt txn by account no.
     * @param accountNo the account no
     * @param startIndex the start index
     * @param limit the limit
     * @return the list< esemt txn>
     */
    public List<ESEMTTxn> listEseMTTxnByAccountNo(String accountNo, int startIndex, int limit);

    /**
     * Find txn exist for service point.
     * @param columnTxn the column txn
     * @return true, if successful
     */
    public boolean findTxnExistForServicePoint(String columnTxn);

    /**
     * Find txn exist for client.
     * @param profileId the profile id
     * @return true, if successful
     */
    public boolean findTxnExistForClient(String profileId);

    /**
     * Save.
     * @param txnBulkImport the txn bulk import
     */
    public void save(TxnBulkImport txnBulkImport);

    /**
     * Find txn bulk import by txn id.
     * @param txnId the txn id
     * @return the txn bulk import
     */
    public TxnBulkImport findTxnBulkImportByTxnId(String txnId);

    /**
     * Update.
     * @param txnBulkImport the txn bulk import
     */
    public void update(TxnBulkImport txnBulkImport);

    /**
     * Find txn by txn id.
     * @param txnId the txn id
     * @return the txn bulk import
     */
    public TxnBulkImport findTxnByTxnId(String txnId);

    /**
     * List bulk upload.
     * @return the list< txn bulk import>
     */
    public List<TxnBulkImport> listBulkUpload();

    /**
     * Save order taking.
     * @param orderTaking the order taking
     */
    public void saveOrderTaking(OrderTaking orderTaking);

    /**
     * Save order detail.
     * @param orderDetail the order detail
     */
    public void saveOrderDetail(OrderDetail orderDetail);

    /**
     * List order deatail.
     * @param id the id
     * @return the list< order detail>
     */
    public List<OrderDetail> listOrderDeatail(long id);

    /**
     * List order deatail.
     * @param id the id
     * @param startIndex the start index
     * @param limit the limit
     * @return the list< order detail>
     */
    public List<OrderDetail> listOrderDeatail(long id, int startIndex, int limit);

    /**
     * List store name from order taking.
     * @return the list< string>
     */
    public List<String> listStoreNameFromOrderTaking();

    /**
     * List top products.
     * @return the list< object[]>
     */
    public List<Object[]> listTopProducts();

    /**
     * List top agents.
     * @return the list< object[]>
     */
    public List<Object[]> listTopAgents();

    /**
     * List top agents by order.
     * @return the list< object[]>
     */
    public List<Object[]> listTopAgentsByOrder();

    /**
     * List top shop dealers by order.
     * @return the list< object[]>
     */
    public List<Object[]> listTopShopDealersByOrder();

    /**
     * List top products by order.
     * @return the list< object[]>
     */
    public List<Object[]> listTopProductsByOrder();

    /**
     * Find txn exist for product by product id.
     * @param id the id
     * @return true, if successful
     */
    public boolean findTxnExistForProductByProductId(long id);

    /**
     * Find ese txnexist.
     * @param txnTime the txn time
     * @param serialNo the serial no
     * @param msgNo the msg no
     * @return the eSE txn
     */
    public ESETxn findESETxnexist(Date txnTime, String serialNo, String msgNo);

    /**
     * Find txn exist for farmer.
     * @param farmerId the farmer id
     * @return true, if successful
     */
    public boolean findTxnExistForFarmer(String farmerId);

    /**
     * List top agents by procurement.
     * @param limitValue the limit value
     * @return the list< object[]>
     */
    public List<Object[]> listTopAgentsByProcurement(String seasonCode, int limitValue);

    /**
     * List top farmers by procurement.
     * @param limitValue the limit value
     * @return the list< object[]>
     */
    public List<Object[]> listTopFarmersByProcurement(String seasonCode, int limitValue);

    /**
     * List enrollment data by year agent.
     * @param agentId the agent id
     * @param selectedYear the selected year
     * @param month the month
     * @return the list< object[]>
     */
    public List<Object[]> listEnrollmentDataByYearAgent(String agentId, String selectedYear,
            int month);

    /**
     * List distribution data.
     * @param filterConditions the filter conditions
     * @param limitValue the limit value
     * @return the list< object[]>
     */
    public List<Object[]> listDistributionData(String agentId, String seasonCode, int limitValue);

    /**
     * List top daily product order by date.
     * @param date the date
     * @param limitValue the limit value
     * @return the list< object[]>
     */
    public List<Object[]> listTopDailyProductOrderByDate(String date, int limitValue);

    /**
     * List top daily product order summary by date.
     * @param dailyProductOrderDate the daily product order date
     * @param i the i
     * @return the list< object[]>
     */
    public List<Object[]> listTopDailyProductOrderSummaryByDate(String dailyProductOrderDate, int i);
	
}
