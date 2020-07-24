/*
 * IAffiliateDAO.java
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

import com.sourcetrace.esesw.entity.profile.Affiliate;
import com.sourcetrace.esesw.entity.profile.AffiliateSwitchTxnCommission;
import com.sourcetrace.esesw.entity.txn.Balance;
import com.sourcetrace.esesw.entity.txn.DayCreditBalance;

// TODO: Auto-generated Javadoc
/**
 * The Interface IAffiliateDAO.
 */
public interface IAffiliateDAO extends IESEDAO {

    /**
     * List.
     * @return the list< affiliate>
     */
    public List<Affiliate> list();

    /**
     * Find affiliate by affiliate name.
     * @param affiliatename the affiliatename
     * @return the affiliate
     */
    public Affiliate findAffiliateByAffiliateName(String affiliatename);

    /**
     * List affiliate ids by federation.
     * @param federation the federation
     * @return the list< string>
     */
    public List<String> listAffiliateIdsByFederation(String federation);

    /**
     * Find by affiliate id.
     * @param affiliateId the affiliate id
     * @return the affiliate
     */
    public Affiliate findByAffiliateId(String affiliateId);

    /**
     * Find affiliate.
     * @param id the id
     * @return the affiliate
     */
    public Affiliate findAffiliate(long id);

    /**
     * Find by account number.
     * @param accountNumber the account number
     * @return the affiliate
     */
    public Affiliate findByAccountNumber(String accountNumber);

    /**
     * Find with txns.
     * @param affiliateId the affiliate id
     * @return the affiliate
     */
    public Affiliate findWithTxns(String affiliateId);

    /**
     * Find with stores.
     * @param affiliateId the affiliate id
     * @return the affiliate
     */
    public Affiliate findWithStores(String affiliateId);

    /**
     * Find with service point users.
     * @param affiliateId the affiliate id
     * @return the affiliate
     */
    public Affiliate findWithServicePointUsers(String affiliateId);

    /**
     * Find with payments.
     * @param affiliateId the affiliate id
     * @return the affiliate
     */
    public Affiliate findWithPayments(String affiliateId);

    /**
     * List affiliate ids by federation.
     * @param federation the federation
     * @return the list< affiliate>
     */
    public List<Affiliate> listAffiliateIdsByFederation(long federation);

    /**
     * List by category.
     * @param categoryCode the category code
     * @return the list< affiliate>
     */
    public List<Affiliate> listByCategory(String categoryCode);

    /**
     * List by payment day.
     * @param paymentDayCode the payment day code
     * @return the list< affiliate>
     */
    public List<Affiliate> listByPaymentDay(String paymentDayCode);

    /**
     * List by payment type.
     * @param paymentTypeCode the payment type code
     * @return the list< affiliate>
     */
    public List<Affiliate> listByPaymentType(String paymentTypeCode);

    /**
     * List by payment period.
     * @param paymentPeriodCode the payment period code
     * @return the list< affiliate>
     */
    public List<Affiliate> listByPaymentPeriod(String paymentPeriodCode);

    /**
     * Find last txn credit balance.
     * @param affiliateId the affiliate id
     * @return the double
     */
    public double findLastTxnCreditBalance(String affiliateId);

    /**
     * Find last txn credit balance.
     * @param affiliateId the affiliate id
     * @param txnTime the txn time
     * @return the double
     */
    public double findLastTxnCreditBalance(String affiliateId, Date txnTime);

    /**
     * Find day credit balance.
     * @param affiliateId the affiliate id
     * @param date the date
     * @return the day credit balance
     */
    public DayCreditBalance findDayCreditBalance(String affiliateId, Date date);

    /**
     * Save daily closing balance.
     */
    public void saveDailyClosingBalance();

    /**
     * List affiliates by federation.
     * @param federation the federation
     * @return the list< affiliate>
     */
    public List<Affiliate> listAffiliatesByFederation(String federation);

    /**
     * Find affiliate txn commission.
     * @param affiliateId the affiliate id
     * @param switchTxnId the switch txn id
     * @return the affiliate switch txn commission
     */
    public AffiliateSwitchTxnCommission findAffiliateTxnCommission(String affiliateId,
            String switchTxnId);

    /**
     * List affiliate txn commission.
     * @param selectedAffiliate the selected affiliate
     * @return the list< affiliate switch txn commission>
     */
    public List<AffiliateSwitchTxnCommission> listAffiliateTxnCommission(String selectedAffiliate);

    /**
     * List day credit balance.
     * @return the list< day credit balance>
     */
    public List<DayCreditBalance> listDayCreditBalance();

    public Balance findBalanceByAffiliate(long id);

}
