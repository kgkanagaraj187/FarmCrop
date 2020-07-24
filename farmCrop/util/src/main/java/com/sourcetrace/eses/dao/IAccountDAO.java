/*
 * IAccountDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.esesw.entity.profile.AgentCashFlow;
import com.sourcetrace.esesw.entity.profile.ESEAccount;

@SuppressWarnings("unused")
public interface IAccountDAO extends IESEDAO {

	public List<ESEAccount> listOfHomePOSClientByRevisionNumberAndServicePlaceId(long revisionNumber,
			String servicePlaceId);

	public ESEAccount findAccountByProfileId(String profileId);
	
	public ESEAccount findAccountByProfileId(long Id);

	public ESEAccount findAccountByAccountId(long id);

	public void updateAccountStatus(String profileId, int status, int type);

	public String findAccountNoByProfileId(String profileId);

	public void removeAccountByProfileId(String profileId);

	public ESEAccount findAccountByAccountNo(String acctNumber);

	public List<String> findAccountBalanceList(String value);

	public AgentCashFlow findAgentCashFlowById(long id);

	public ESEAccount findAccountByProfileIdAndProfileType(String profileId, int type);

	public void removeAccountByProfileIdAndProfileType(String profileId, int type);

	public ESEAccount findAccountByVendorIdAndType(String vendorId, int type);

	public void updateCashBal(long id, double cashBalance, double creditBalance);

	public ESEAccount findAccountByProfileIdAndProfileType(String profileId, int type, String tenantId);

	public ESEAccount findAccountByProfileId(String profileId, String tenantId);

	public void update(ESEAccount tempAccount, String tenantId);

	public void updateESEAccountCashBalById(long id, double cashBalance);

	public void SaveByTenant(Object obj, String tenantId);

	public void updateByTenant(Object obj, String tenantId);

	public void SaveOrUpdateByTenant(Object obj, String tenantId);

	public AgroTransaction findEseAccountByTransaction(long id);
	
	public ESEAccount findEseAccountByBuyerIdAndTypee(String customerId, int clientAccount);
	
	public ESEAccount findEseAccountByProfileId(String profileId);
	
	public void updateESEAccountOutStandingBalById(long id, double balAmount);

}
