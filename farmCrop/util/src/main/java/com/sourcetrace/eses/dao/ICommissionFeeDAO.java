/*
 * ICommissionFeeDAO.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.Date;
import java.util.List;

import com.ese.entity.txn.fee.CommissionFeeGroup;
import com.ese.entity.txn.fee.CommissionPayment;
import com.ese.entity.txn.fee.TxnCommissionFee;
import com.sourcetrace.eses.entity.Profile;

// TODO: Auto-generated Javadoc
/**
 * The Interface ICommissionFeeDAO.
 */
public interface ICommissionFeeDAO extends IESEDAO {

	/**
	 * Find commission fee group.
	 * 
	 * @param id the id
	 * 
	 * @return the commission fee group
	 */
	public CommissionFeeGroup findCommissionFeeGroup(long id);

	/**
	 * List commission fee group.
	 * 
	 * @return the list< commission fee group>
	 */
	public List<CommissionFeeGroup> listCommissionFeeGroup();

	/**
	 * Find commission fee group by name.
	 * 
	 * @param name the name
	 * 
	 * @return the commission fee group
	 */
	public CommissionFeeGroup findCommissionFeeGroupByName(String name);

	/**
	 * List pending commission fees.
	 * 
	 * @param profileId the profile id
	 * 
	 * @return the list< txn commission fee>
	 */
	public List<TxnCommissionFee> listPendingCommissionFees(String profileId);

	/**
	 * List commission fees txn.
	 * 
	 * @return the list< txn commission fee>
	 */
	public List<TxnCommissionFee> listCommissionFeesTxn();

	/**
	 * Commission count by txn.
	 * 
	 * @param status the status
	 * 
	 * @return the list
	 */
	public List commissionCountByTxn(int status);

	/**
	 * Find agent.
	 * 
	 * @param profile the profile
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param onlineTxnType the online txn type
	 * 
	 * @return the txn commission fee
	 */
	public TxnCommissionFee findAgent(Profile profile, Date startDate,
			Date endDate, int onlineTxnType);

	/**
	 * Commission by agent and txn.
	 * 
	 * @param p the p
	 * @param status the status
	 * 
	 * @return the list
	 */
	public List commissionByAgentAndTxn(Profile p,int status);

	/**
	 * List agents have fee.
	 * 
	 * @param status the status
	 * 
	 * @return the list< object>
	 */
	public List<Object> listAgentsHaveFee(int status);

	/**
	 * List commission fees current txn.
	 * 
	 * @return the list< txn commission fee>
	 */
	public List<TxnCommissionFee> listCommissionFeesCurrentTxn();

	/**
	 * List all pending commission payments.
	 * 
	 * @return the list< commission payment>
	 */
	public List<CommissionPayment> listAllPendingCommissionPayments();

	/**
	 * List all pending commission payments to process.
	 * 
	 * @return the list< commission payment>
	 */
	public List<CommissionPayment> listAllPendingCommissionPaymentsToProcess();

	/**
	 * Find commission payment by id.
	 * 
	 * @param key the key
	 * 
	 * @return the commission payment
	 */
	public CommissionPayment findCommissionPaymentById(long key);

	/**
	 * Find commission payment by payment refernce id.
	 * 
	 * @param key the key
	 * 
	 * @return the commission payment
	 */
	public CommissionPayment findCommissionPaymentByPaymentRefernceId(long key);

	/**
	 * List commission fees txn by commission payment.
	 * 
	 * @param commissionPayment the commission payment
	 * 
	 * @return the list< txn commission fee>
	 */
	public List<TxnCommissionFee> listCommissionFeesTxnByCommissionPayment(
			CommissionPayment commissionPayment);

	/**
	 * Commission payment by agent and txn.
	 * 
	 * @param p the p
	 * @param status the status
	 * 
	 * @return the list< object[]>
	 */
	public List<Object[]> commissionPaymentByAgentAndTxn(Profile p, int status);

	/**
	 * List commission fees pending txn.
	 * 
	 * @return the list< txn commission fee>
	 */
	public List<TxnCommissionFee> listCommissionFeesPendingTxn();
	
	/**
	 * Commission payment by agent and date.
	 * 
	 * @param id the id
	 * @param startDate the start date
	 * @param endDate the end date
	 * 
	 * @return the list< object[]>
	 */
	public List<Object[]> commissionPaymentByAgentAndDate(String id, Date startDate, Date endDate);

	/**
	 * List success commission payments.
	 * 
	 * @param sDate the s date
	 * @param eDate the e date
	 * 
	 * @return the list< commission payment>
	 */
	public List<CommissionPayment>  listSuccessCommissionPayments(Date sDate,Date eDate);

	/**
	 * List failed commission payments.
	 * 
	 * @param sDate the s date
	 * @param eDate the e date
	 * 
	 * @return the list< commission payment>
	 */
	public List<CommissionPayment> listFailedCommissionPayments(Date sDate,Date eDate);
	
	/**
	 * List commission payments by commerce id.
	 * 
	 * @param commerceId the commerce id
	 * 
	 * @return the list< commission payment>
	 */
	public List<CommissionPayment> listCommissionPaymentsByCommerceID(String commerceId);

	/**
	 * List success commission payments.
	 * 
	 * @param sDate the s date
	 * @param eDate the e date
	 * @param commerceId the commerce id
	 * 
	 * @return the list< commission payment>
	 */
	public List<Object[]> listSuccessCommissionPayments(Date sDate,
			Date eDate, String commerceId);

}
