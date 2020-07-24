/*
 * IAccountService.java
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
import java.util.Set;

import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.esesw.entity.profile.AgentCashFlow;
import com.sourcetrace.esesw.entity.profile.ESEAccount;

public interface IAccountService {

	public List<ESEAccount> listOfHomePOSClientByRevisionNumberAndServicePlaceId(long revisionNumber,
			String servicePlaceId);

	public void createAccount(ESEAccount account);

	public ESEAccount findAccountByProfileId(String profileId);

	public ESEAccount findAccountByAccountId(long id);

	public void update(ESEAccount tempAccount);

	public void updateAccountStatus(String profileId, int status, int type);

	public String findAccountNoByProfileId(String profileId);

	public void removeAccountByProfileId(String profileId);

	public ESEAccount findAccountByAccountNo(String acctNumber);

	public List<String> getAccountBalanceList(String value);

	public void editAccount(ESEAccount account);

	public AgentCashFlow findAgentCashFlowById(long id);

	public void createAgroTransactionAndEditAccount(AgroTransaction agroTransaction, ESEAccount account);

	public ESEAccount findAccountByProfileIdAndProfileType(String profileId, int type);

	public void removeAccountByProfileIdAndProfileType(String profileId, int type);

	public void processCashReceivedTxn(ESEAccount eseAccount, AgroTransaction agroTxnTransaction);

	public void updateEseAccount(ESEAccount eseAccount);

	public void createAccount(String profileId, String accountNo, Date txnTime, int type,String branchId);

	public ESEAccount findAccountByVendorIdAndType(String vendorId, int type);
	
	public ESEAccount findAccountByProfileId(String profileId, String tenantId);
	
	//public ESEAccount findAccountByProfileIdAndProfileType1(String profileId, int farmerAccount, String tenantId);

    public ESEAccount findAccountByProfileIdAndProfileType(String profileId, int agentAccount,
            String tenantId);

    public void update(ESEAccount agentAccount, String tenantId);
    
    public void updateESEAccountCashBalById(long id, double cashBalance);
    
    public void updateCashBal(long id, double cashBalance, double creditBalance);
    
    public void SaveByTenant(Object obj,String tenantId);
    
    public void updateByTenant(Object obj,String tenantId);
    
    public void SaveOrUpdateByTenant(Object obj,String tenantId);

	public AgroTransaction findEseAccountByTransaction(long id);

	public ESEAccount findEseAccountByBuyerIdAndTypee(String customerId, int clientAccount);
	
	public ESEAccount findEseAccountByProfileId(String profileId);


    
   }
