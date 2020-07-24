/*
 * AccountDAO.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.eses.order.entity.txn.AgroTransaction;
import com.sourcetrace.eses.order.entity.txn.CityWarehouse;
import com.sourcetrace.esesw.entity.profile.AgentCashFlow;
import com.sourcetrace.esesw.entity.profile.Client;
import com.sourcetrace.esesw.entity.profile.ESEAccount;

@SuppressWarnings("unused")
@Repository
public class AccountDAO extends ESEDAO implements IAccountDAO {
    @Autowired
	public AccountDAO(SessionFactory sessionFactory){
		this.setSessionFactory(sessionFactory);
	}
    @SuppressWarnings("unchecked")
    public List<ESEAccount> listOfHomePOSClientByRevisionNumberAndServicePlaceId(
            long revisionNumber, String servicePlaceId) {

        Object[] bind = { Client.CLIENT, revisionNumber, servicePlaceId };
        List<ESEAccount> clientCardList = list(
                "select eseAcct FROM Client client, ESEAccount eseAcct WHERE client.profileId = eseAcct.profileId AND client.profileType = ? AND client.revisionNumber>? AND client.servicePoint.code = ? ORDER BY client.revisionNumber",
                bind);

        return clientCardList;
    }

    public ESEAccount findAccountByProfileId(String profileId) {

        ESEAccount account = (ESEAccount) find("from ESEAccount account where account.profileId = ?", profileId);
        return account;
    }

    public ESEAccount findAccountByAccountId(long id) {

        return (ESEAccount) find("FROM ESEAccount c WHERE c.id = ?", id);
    }

    public void updateAccountStatus(String profileId, int status, int type) {

        Session session = getSessionFactory().openSession();
        Query query = session.createQuery("update ESEAccount set status = :status"
                + " where profileId = :profileId AND type = :type");
        query.setParameter("status", status);
        query.setParameter("profileId", profileId);
        query.setParameter("type", type);
        int result = query.executeUpdate();
        session.flush();
        session.close();

    }

    public String findAccountNoByProfileId(String profileId) {

        return (String) find("SELECT acct.accountNo FROM ESEAccount acct WHERE acct.profileId = ?",
                profileId);
    }

    public void removeAccountByProfileId(String profileId) {

        Session session = getSessionFactory().openSession();
        Query query = session.createQuery("delete ESEAccount where profileId = :profileId");
        query.setParameter("profileId", profileId);
        int result = query.executeUpdate();
        session.flush();
        session.close();
    }

    
    public ESEAccount findAccountByAccountNo(String acctNumber) {

        return (ESEAccount) find("From ESEAccount where accountNo = ?", acctNumber);
    }

    public List<String> findAccountBalanceList(String value) {

        Session session = getSessionFactory().openSession();
        String queryString = "SELECT CONCAT_WS('-', ese_account.ACC_NO, pers_info.FIRST_NAME, pers_info.LAST_NAME, ese_account.BALANCE) "
                + "FROM pers_info INNER JOIN prof ON prof.PERS_INFO_ID = pers_info.ID "
                + "INNER JOIN ese_account ON ese_account.PROFILE_ID = prof.PROF_ID WHERE pers_info.FIRST_NAME LIKE '"
                + value + "%' OR ese_account.ACC_NO LIKE '" + value + "%'";
        Query query = session.createSQLQuery(queryString);
        List list = query.list();
        session.flush();
        session.close();
        return list;
    }

    public AgentCashFlow findAgentCashFlowById(long id) {

        return (AgentCashFlow) find("From AgentCashFlow where id = ?", id);
    }

    public ESEAccount findAccountByProfileIdAndProfileType(String profileId, int type) {

        Object[] values = { profileId, type };
        return (ESEAccount) find(
                "FROM ESEAccount esea WHERE esea.profileId = ? AND esea.type = ? ", values);
    }

    public void removeAccountByProfileIdAndProfileType(String profileId, int type) {

        Session session = getSessionFactory().openSession();
        Query query = session
                .createQuery("delete ESEAccount where profileId = :profileId AND type = :type");
        query.setParameter("profileId", profileId);
        query.setParameter("type", type);
        int result = query.executeUpdate();
        session.flush();
        session.close();

    }

    public ESEAccount findAccountByVendorIdAndType(String vendorId, int type) {

        Object[] values = { vendorId, type };
        return (ESEAccount) find("FROM ESEAccount esea WHERE esea.profileId = ? AND esea.type = ? ", values);
    }

    public void updateCashBal(long id, double cashBalance,double creditBalance){
        
    	String queryString="UPDATE ESE_ACCOUNT SET CASH_BALANCE ='"+cashBalance+"',CREDIT_BALANCE ='"+creditBalance+"' WHERE ID='"+id+"'";
        Session session = getSessionFactory().getCurrentSession();
        Query query = session
                .createSQLQuery(queryString);
        /*query.setParameter("CASH_BAL", cashBalance);
        query.setParameter("CREDIT_BAL", creditBalance);
        query.setParameter("ID", id);
        */
        int result = query.executeUpdate();
       
        
    }

    public ESEAccount findAccountByProfileIdAndProfileType(String profileId, int type, String tenantId) {

        /*Object[] values = { profileId, type };
        return (ESEAccount) find("FROM ESEAccount esea WHERE esea.profileId = ? AND esea.type = ? ", values);*/
    	
    	Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("FROM ESEAccount esea WHERE esea.profileId = :profileId AND esea.type = :type");
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
    
    public ESEAccount findAccountByProfileId(String profileId, String tenantId) {

        /*ESEAccount account = (ESEAccount) find("from ESEAccount account where account.profileId = ?", profileId);
        return account;*/
    	
    	Session session = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		Query query = session.createQuery("from ESEAccount account where account.profileId = :profileId");
		query.setParameter("profileId", profileId);
		
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
    public void update(ESEAccount tempAccount, String tenantId) {

        Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
        sessions.update(tempAccount);
        sessions.flush();
        sessions.close();
        
    }
   
    public void updateESEAccountCashBalById(long id, double cashBalance)
    {
    	String queryString="UPDATE ESE_ACCOUNT SET CASH_BALANCE ='"+cashBalance+"' WHERE ID='"+id+"'";
        Session session = getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(queryString);
        /*query.setParameter("CASH_BAL", cashBalance);
        query.setParameter("CREDIT_BAL", creditBalance);
        query.setParameter("ID", id);
        */
        int result = query.executeUpdate();
    }
	@Override
	public void SaveByTenant(Object obj, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.save(obj);
		sessions.flush();
		sessions.close();
		
	}
	@Override
	public void updateByTenant(Object obj, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.update(obj);
		sessions.flush();
		sessions.close();
		
	}
	@Override
	public void SaveOrUpdateByTenant(Object obj, String tenantId) {
		Session sessions = getSessionFactory().withOptions().tenantIdentifier(tenantId).openSession();
		sessions.saveOrUpdate(obj);
		sessions.flush();
		sessions.close();
		
	}
    @Override
    public ESEAccount findAccountByProfileId(long Id) {
        ESEAccount account = (ESEAccount) find("from ESEAccount account where account.id = ?",Id);
        return account;
    }
	@Override
	public AgroTransaction findEseAccountByTransaction(long id) {
		AgroTransaction agroTransaction=(AgroTransaction) find("from AgroTransaction ag where ag.account.id=?",id);
		return agroTransaction;
	}
   public ESEAccount findEseAccountByBuyerIdAndTypee(String profileId, int type){
	   
	   Session session = getSessionFactory().getCurrentSession();
		String queryString = "SELECT * from ese_account ea WHERE ea.profile_id='"+profileId+"' AND ea.typee='"+type+"';";
		Query query = session.createSQLQuery(queryString).addEntity(ESEAccount.class);
		List<ESEAccount> list = query.list();
		if (list.size() > 0){
			return list.get(0);
			}
		else{
			return null;
			}
   }
   
 public ESEAccount findEseAccountByProfileId(String profileId){
	   
	   Session session = getSessionFactory().getCurrentSession();
		String queryString = "SELECT * from ese_account ea WHERE ea.profile_id='"+profileId+"';";
		Query query = session.createSQLQuery(queryString).addEntity(ESEAccount.class);
		List<ESEAccount> list = query.list();
		if (list.size() > 0){
			return list.get(0);
			}
		else{
			return null;
			}
   }
 
 public void updateESEAccountOutStandingBalById(long id, double loanBalance)
 {
 	String queryString="UPDATE ESE_ACCOUNT SET OUTSTANDING_AMOUNT ='"+loanBalance+"' WHERE ID='"+id+"'";
     Session session = getSessionFactory().getCurrentSession();
     Query query = session.createSQLQuery(queryString);
     /*query.setParameter("CASH_BAL", cashBalance);
     query.setParameter("CREDIT_BAL", creditBalance);
     query.setParameter("ID", id);
     */
     int result = query.executeUpdate();
 }
}
