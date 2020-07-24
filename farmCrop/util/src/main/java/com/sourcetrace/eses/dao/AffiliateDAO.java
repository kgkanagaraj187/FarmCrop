/*
 * AffiliateDAO.java
 * Copyright (c) 2008-2010, SourceTrace Systems, All Rights Reserved.
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
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.Affiliate;
import com.sourcetrace.esesw.entity.profile.AffiliateSwitchTxnCommission;
import com.sourcetrace.esesw.entity.txn.Balance;
import com.sourcetrace.esesw.entity.txn.DayCreditBalance;
import com.sourcetrace.esesw.excep.SwitchException;

/**
 * The Class AffiliateDAO.
 * 
 * @author $Author: ganesh $
 * @version $Rev: 1275 $ $Date: 2010-06-15 16:29:01 +0530 (Tue, 15 Jun 2010) $
 */
@Repository
@Transactional
public class AffiliateDAO extends ESEDAO implements IAffiliateDAO {
	
	@Autowired
	public AffiliateDAO(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	/**
	 * List.
	 * 
	 * @return the list< affiliate>
	 */
	private static final Logger LOGGER = Logger.getLogger(AffiliateDAO.class.getName());

	public List<Affiliate> list() {

		return list("FROM Affiliate");
	}

	/**
	 * List affiliate ids by federation.
	 * 
	 * @param federation
	 *            the federation
	 * @return the list< string>
	 */
	public List<String> listAffiliateIdsByFederation(String federation) {

		return list("SELECT a.affiliateId FROM Affiliate a where a.federation.name = ? ", federation);
	}

	/**
	 * List affiliate ids by federation.
	 * 
	 * @param federation
	 *            the federation
	 * @return the list< affiliate>
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#listAffiliateIdsByFederation(long)
	 */
	public List<Affiliate> listAffiliateIdsByFederation(long federation) {

		return list("FROM Affiliate a where a.federation.id = ? ", federation);

	}

	/**
	 * List by category.
	 * 
	 * @param categoryCode
	 *            the category code
	 * @return the list< affiliate>
	 */
	public List<Affiliate> listByCategory(String categoryCode) {

		List<Affiliate> affiliates = (List<Affiliate>) list("FROM Affiliate a WHERE a.category.code=?", categoryCode);
		return affiliates;
	}

	/**
	 * List by payment day.
	 * 
	 * @param paymentDayCode
	 *            the payment day code
	 * @return the list< affiliate>
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#listByPaymentDay(java.lang.String)
	 */
	public List<Affiliate> listByPaymentDay(String paymentDayCode) {

		List<Affiliate> affiliates = (List<Affiliate>) list("FROM Affiliate a WHERE a.paymentDay.code=?",
				paymentDayCode);
		return affiliates;
	}

	/**
	 * List by payment type.
	 * 
	 * @param paymentTypeCode
	 *            the payment type code
	 * @return the list< affiliate>
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#listByPaymentType(java.lang.String)
	 */
	public List<Affiliate> listByPaymentType(String paymentTypeCode) {

		List<Affiliate> affiliates = (List<Affiliate>) list("FROM Affiliate a WHERE a.paymentType.code=?",
				paymentTypeCode);
		return affiliates;
	}

	/**
	 * List by payment period.
	 * 
	 * @param paymentPeriodCode
	 *            the payment period code
	 * @return the list< affiliate>
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#listByPaymentPeriod(java.lang.String)
	 */
	public List<Affiliate> listByPaymentPeriod(String paymentPeriodCode) {

		List<Affiliate> affiliates = (List<Affiliate>) list("FROM Affiliate a WHERE a.paymentPeriod.code=?",
				paymentPeriodCode);
		return affiliates;
	}

	/**
	 * Find.
	 * 
	 * @param affiliateId
	 *            the affiliate id
	 * @return the affiliate
	 */
	public Affiliate findWithTxns(String affiliateId) {

		Affiliate affiliate = (Affiliate) find("FROM Affiliate a LEFT JOIN FETCH a.txns WHERE a.affiliateId = ?",
				affiliateId);
		return affiliate;
	}

	/**
	 * Find affiliate by affiliate name.
	 * 
	 * @param affiliatename
	 *            the affiliatename
	 * @return the affiliate
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#findAffiliateByAffiliateName(java.lang.String)
	 */
	public Affiliate findAffiliateByAffiliateName(String affiliatename) {

		Affiliate affiliate = (Affiliate) find("from Affiliate a where a.name = ?", affiliatename);
		return affiliate;
	}

	/**
	 * Find by affiliate id.
	 * 
	 * @param affiliateId
	 *            the affiliate id
	 * @return the affiliate
	 */
	public Affiliate findByAffiliateId(String affiliateId) {

		Affiliate affiliate = (Affiliate) find("FROM Affiliate a WHERE a.affiliateId = ?", affiliateId);
		return affiliate;
	}

	/**
	 * Find affiliate.
	 * 
	 * @param id
	 *            the id
	 * @return the affiliate
	 */
	public Affiliate findAffiliate(long id) {

		Affiliate affiliate = (Affiliate) find("FROM Affiliate a LEFT JOIN FETCH a.stores WHERE a.id = ?", id);
		return affiliate;

	}

	/**
	 * Find with service point users.
	 * 
	 * @param affiliateId
	 *            the affiliate id
	 * @return the affiliate
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#findWithServicePointUsers(long)
	 */
	public Affiliate findWithServicePointUsers(String affiliateId) {

		Affiliate affiliate = (Affiliate) find(
				"FROM Affiliate a LEFT JOIN FETCH a.servicePointUsers WHERE a.affiliateId = ?", affiliateId);
		return affiliate;
	}

	/**
	 * Find with stores.
	 * 
	 * @param affiliateId
	 *            the affiliate id
	 * @return the affiliate
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#findWithStores(long)
	 */
	public Affiliate findWithStores(String affiliateId) {

		Affiliate affiliate = (Affiliate) find("FROM Affiliate a LEFT JOIN FETCH a.stores WHERE a.affiliateId = ?",
				affiliateId);
		return affiliate;
	}

	/**
	 * Find by account number.
	 * 
	 * @param accountNumber
	 *            the account number
	 * @return the affiliate
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#findByAccountNumber(java.lang.String)
	 */
	public Affiliate findByAccountNumber(String accountNumber) {

		Affiliate affiliate = (Affiliate) find(
				"FROM Affiliate a LEFT JOIN FETCH a.balance WHERE a.balance.accountNumber = ?", accountNumber);
		return affiliate;
	}

	/**
	 * Find with payments.
	 * 
	 * @param affiliateId
	 *            the affiliate id
	 * @return the affiliate
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#findWithPayments(java.lang.String)
	 */
	public Affiliate findWithPayments(String affiliateId) {

		Affiliate affiliate = (Affiliate) find("FROM Affiliate a LEFT JOIN FETCH a.payments WHERE a.affiliateId = ?",
				affiliateId);
		return affiliate;
	}

	/**
	 * Find last txn credit balance.
	 * 
	 * @param affiliateId
	 *            the affiliate id
	 * @return the double
	 */
	public double findLastTxnCreditBalance(String affiliateId) {

		double balance = 0.00;
		Object object = currentSession()
				.createSQLQuery(
						"select tcb.CR_BAL FROM TXN_CR_BAL tcb WHERE (select max(t.TXN_CR_BAL_ID) from TXN t where t.AFF_ID = ?) IS NOT NULL && tcb.ID = (select max(t.TXN_CR_BAL_ID) from TXN t where t.AFF_ID = ?)")
				.setString(0, affiliateId).setString(1, affiliateId).uniqueResult();

		if (object != null) {
			balance = (Double) object;
		} else {
			throw new SwitchException();
		}

		return balance;
	}

	/**
	 * Find last txn credit balance.
	 * 
	 * @param affiliateId
	 *            the affiliate id
	 * @param txnTime
	 *            the txn time
	 * @return the double
	 */

	public double findLastTxnCreditBalance(String affiliateId, Date txnTime) {

		double balance = 0.00;
		Object object = null;
		if (ObjectUtil.isEmpty(txnTime)) {
			object = currentSession()
					.createSQLQuery(
							"select tcb.CR_BAL FROM TXN_CR_BAL tcb WHERE (select max(t.TXN_CR_BAL_ID) from TXN t where t.AFF_ID = ? ) IS NOT NULL && tcb.ID = (select max(t.TXN_CR_BAL_ID) from TXN t where t.AFF_ID = ? )")
					.setString(0, affiliateId).setString(1, affiliateId).uniqueResult();
		} else {
			object = currentSession()
					.createSQLQuery(
							"select tcb.CR_BAL FROM TXN_CR_BAL tcb WHERE (select max(t.TXN_CR_BAL_ID) from TXN t where t.AFF_ID = ? and t.TXN_TIME<=?) IS NOT NULL && tcb.ID = (select max(t.TXN_CR_BAL_ID) from TXN t where t.AFF_ID = ? and t.TXN_TIME<=?)")
					.setString(0, affiliateId).setDate(1, txnTime).setString(2, affiliateId).setDate(3, txnTime)
					.uniqueResult();
		}

		if (object != null) {
			balance = (Double) object;
		} else {
			throw new SwitchException();
		}

		return balance;
	}

	/**
	 * Save daily closing balance.
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#saveDailyClosingBalance()
	 */
	public void saveDailyClosingBalance() {

		StatelessSession session = getSessionFactory().openStatelessSession();
		LOGGER.info("Accessing Affiliate data");
		Query query = session.createQuery("FROM Affiliate a");
		ScrollableResults affiliates = query.scroll(ScrollMode.FORWARD_ONLY);
		while (affiliates.next()) {
			Affiliate affiliate = (Affiliate) affiliates.get(0);
			DayCreditBalance dayBalance = new DayCreditBalance();
			dayBalance.setDate(new Date());
			dayBalance.setAffiliate(affiliate);
			Balance balance = affiliate.getBalance();
			dayBalance.setDeposit(balance.getPrepaymentAmount());
			dayBalance.setCredit(balance.getCreditAmount());
			dayBalance.setTrust(balance.getTrustAmount());
			dayBalance.setAccumulatedBalance(balance.getAccumulatedBalance());
			dayBalance.setCreditBalance(balance.getCreditBalance());

			try {
				double txnBalance = findLastTxnCreditBalance(affiliate.getAffiliateId());
				if (dayBalance.getCreditBalance() == txnBalance) {
					dayBalance.setStatus(DayCreditBalance.TALLY);
				} else if (dayBalance.getCreditBalance() > txnBalance) {
					dayBalance.setStatus(DayCreditBalance.MORE);
				} else if (dayBalance.getCreditBalance() < txnBalance) {
					dayBalance.setStatus(DayCreditBalance.LESS);
				}
			} catch (SwitchException se) {
				dayBalance.setStatus(DayCreditBalance.TALLY);
			}

			save(dayBalance);
		}

		session.close();
		LOGGER.info("done Affiliate data processing");
	}

	/**
	 * Find day credit balance.
	 * 
	 * @param affiliateId
	 *            the affiliate id
	 * @param date
	 *            the date
	 * @return the day credit balance
	 */
	public DayCreditBalance findDayCreditBalance(String affiliateId, Date date) {

		Object[] bindValues = { affiliateId, date };
		DayCreditBalance dayCreditBalance = (DayCreditBalance) find(
				"FROM DayCreditBalance dcb WHERE dcb.affiliate.affiliateId = ? and dcb.date<?", bindValues);
		return dayCreditBalance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#
	 * listAffiliatesByFederation(java.lang.String)
	 */
	public List<Affiliate> listAffiliatesByFederation(String federation) {

		return list("FROM Affiliate a where a.federation.name = ? ", federation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#
	 * findAffiliateTxnCommission(java.lang.String, java.lang.String)
	 */
	public AffiliateSwitchTxnCommission findAffiliateTxnCommission(String affiliateId, String switchTxnId) {

		Object[] bindValues = { affiliateId, switchTxnId };
		AffiliateSwitchTxnCommission affiliateTxnCommission = (AffiliateSwitchTxnCommission) find(
				"FROM AffiliateSwitchTxnCommission a WHERE a.affiliateId = ? and a.switchtxnId=?", bindValues);
		return affiliateTxnCommission;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.IAffiliateDAO#
	 * listAffiliateTxnCommission(java.lang.String)
	 */
	public List<AffiliateSwitchTxnCommission> listAffiliateTxnCommission(String selectedAffiliate) {

		return list("FROM AffiliateSwitchTxnCommission a WHERE a.affiliateId = ? ", selectedAffiliate);
	}

	/**
	 * List credit balance.
	 * 
	 * @return the list< day credit balance>
	 */
	public List<DayCreditBalance> listDayCreditBalance() {

		return list("FROM DayCreditBalance");
	}

	public Balance findBalanceByAffiliate(long id) {

		Balance balance = (Balance) find("FROM Balance a WHERE a.id = ?", id);
		return balance;
	}

}
