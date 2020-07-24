/*
 * AccountService.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcetrace.eses.dao.IAccountDAO;
import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.esesw.entity.profile.AgentCashFlow;
import com.sourcetrace.esesw.entity.profile.ESEAccount;


// TODO: Auto-generated Javadoc
@Service
@Transactional
public class AccountService implements IAccountService {

	@Autowired
    private IAccountDAO accountDAO;
	@Autowired
    public IAccountDAO getAccountDAO() {

        return accountDAO;
    }

    
    public void setAccountDAO(IAccountDAO accountDAO) {

        this.accountDAO = accountDAO;
    }

    public List<ESEAccount> listOfHomePOSClientByRevisionNumberAndServicePlaceId(
            long revisionNumber, String servicePlaceId) {

        return accountDAO.listOfHomePOSClientByRevisionNumberAndServicePlaceId(revisionNumber,
                servicePlaceId);
    }

    public void createAccount(ESEAccount account) {
        accountDAO.save(account);

    }

    public ESEAccount findAccountByProfileId(String profileId) {

        return accountDAO.findAccountByProfileId(profileId);
    }

    public ESEAccount findAccountByAccountId(long id) {

        return accountDAO.findAccountByAccountId(id);
    }

    public void update(ESEAccount tempAccount) {

        accountDAO.update(tempAccount);

    }

    public void updateAccountStatus(String profileId, int status, int type) {

        accountDAO.updateAccountStatus(profileId, status, type);

    }

    public String findAccountNoByProfileId(String profileId) {

        return accountDAO.findAccountNoByProfileId(profileId);
    }

    public void removeAccountByProfileId(String profileId) {

        accountDAO.removeAccountByProfileId(profileId);

    }

    public ESEAccount findAccountByAccountNo(String acctNumber) {

        return accountDAO.findAccountByAccountNo(acctNumber);
    }

    public List<String> getAccountBalanceList(String value) {

        return accountDAO.findAccountBalanceList(value);
    }

    public void editAccount(ESEAccount account) {

        accountDAO.update(account);
    }

    public AgentCashFlow findAgentCashFlowById(long id) {

        return accountDAO.findAgentCashFlowById(id);
    }

    public void createAgroTransactionAndEditAccount(AgroTransaction agroTransaction,
            ESEAccount account) {

        accountDAO.save(agroTransaction);
        accountDAO.update(account);

    }

    public ESEAccount findAccountByProfileIdAndProfileType(String profileId, int type) {

        return accountDAO.findAccountByProfileIdAndProfileType(profileId, type);
    }

    public void removeAccountByProfileIdAndProfileType(String profileId, int type) {

        accountDAO.removeAccountByProfileIdAndProfileType(profileId, type);

    }

    public void processCashReceivedTxn(ESEAccount eseAccount, AgroTransaction agroTxnTransaction) {

        accountDAO.update(eseAccount);
        accountDAO.save(agroTxnTransaction);

    }

    public void updateEseAccount(ESEAccount eseAccount) {

        accountDAO.update(eseAccount);
    }

    public void createAccount(String profileId, String accountNo, Date txnTime, int type,String branchId) {

        ESEAccount account = new ESEAccount();
        account.setProfileId(profileId);
        account.setAccountNo(accountNo);
        account.setType(type);
        account.setAccountOpenDate(txnTime);
        if (ESEAccount.AGENT_ACCOUNT == type) {
            account.setAccountType(ESEAccount.OPERATOR_ACCOUNT);
        } else if (ESEAccount.FARMER_ACCOUNT == type) {
            account.setAccountType(ESEAccount.SAVING_BANK_ACCOUNT);
        } else if ((ESEAccount.CLIENT_ACCOUNT == type) || (ESEAccount.VENDOR_ACCOUNT == type)) {
            account.setAccountType(ESEAccount.SAVING_BANK_ACCOUNT);
        }
        account.setCashBalance(0.00);
        account.setCreditBalance(0.00);
        account.setBranchId(branchId);
        account.setBalance(0.00);
        account.setDistributionBalance(0.00);
        account.setSavingAmount(0.00);
        account.setShareAmount(0.00);
        account.setStatus(ESEAccount.ACTIVE);
        account.setCreateTime(txnTime);
        createAccount(account);
    }

    public ESEAccount findAccountByVendorIdAndType(String vendorId, int type) {

        return accountDAO.findAccountByVendorIdAndType(vendorId, type);
    }

    public ESEAccount findAccountByProfileId(String profileId, String tenantId) {

        return accountDAO.findAccountByProfileId(profileId, tenantId);
    }
    
    public ESEAccount findAccountByProfileIdAndProfileType(String profileId, int type, String tenantId) {

        return accountDAO.findAccountByProfileIdAndProfileType(profileId, type, tenantId);
    }


    @Override
    public void update(ESEAccount agentAccount, String tenantId) {

        accountDAO.update(agentAccount,tenantId);
        
    }

    public void updateESEAccountCashBalById(long id, double cashBalance){
    	accountDAO.updateESEAccountCashBalById(id, cashBalance);
    }
    
    public void updateCashBal(long id, double cashBalance, double creditBalance){
    	accountDAO.updateCashBal(id, cashBalance, creditBalance);
    }


	@Override
	public void updateByTenant(Object obj, String tenantId) {
		accountDAO.updateByTenant(obj, tenantId);
		
	}


	@Override
	public void SaveByTenant(Object obj, String tenantId) {
		accountDAO.SaveByTenant(obj, tenantId);
		
	}


	@Override
	public void SaveOrUpdateByTenant(Object obj, String tenantId) {
		accountDAO.SaveOrUpdateByTenant(obj, tenantId);
	}


	@Override
	public AgroTransaction findEseAccountByTransaction(long id) {
		 return accountDAO.findEseAccountByTransaction(id);
		
	}


	@Override
	public ESEAccount findEseAccountByBuyerIdAndTypee(String customerId, int clientAccount) {
		// TODO Auto-generated method stub
		return accountDAO.findEseAccountByBuyerIdAndTypee(customerId, clientAccount);
	}


	@Override
	public ESEAccount findEseAccountByProfileId(String profileId) {
		// TODO Auto-generated method stub
		return accountDAO.findEseAccountByProfileId(profileId);
	}
}
