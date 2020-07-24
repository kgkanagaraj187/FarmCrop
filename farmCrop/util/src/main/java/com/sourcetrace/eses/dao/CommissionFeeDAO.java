/*
 * 
 */
package com.sourcetrace.eses.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ese.entity.txn.fee.CommissionFeeGroup;
import com.ese.entity.txn.fee.CommissionPayment;
import com.ese.entity.txn.fee.TxnCommissionFee;
import com.sourcetrace.eses.entity.Profile;

// TODO: Auto-generated Javadoc
/**
 * The Class CommissionFeeDAO.
 * 
 * @author $Author: antronivan $
 * @version $Rev: 422 $, $Date: 2009-11-23 08:19:13 +0530 (Mon, 23 Nov 2009) $
 */

@Repository
@Transactional
public class CommissionFeeDAO extends ESEDAO implements ICommissionFeeDAO {

	@Autowired
	public CommissionFeeDAO(SessionFactory sessionFactory) {

		this.setSessionFactory(sessionFactory);
	}
	
    /*
     * (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#findCommissionFeeGroup(long)
     */
    public CommissionFeeGroup findCommissionFeeGroup(long id) {

        CommissionFeeGroup commfeeGroup = (CommissionFeeGroup) find(
                "FROM CommissionFeeGroup a WHERE a.id = ?", id);

        return commfeeGroup;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#listCommissionFeeGroup()
     */
    public List<CommissionFeeGroup> listCommissionFeeGroup() {

        return list("FROM CommissionFeeGroup");
    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#findCommissionFeeGroupByName (java.lang.String)
     */
    public CommissionFeeGroup findCommissionFeeGroupByName(String name) {

        CommissionFeeGroup commfeeGroup = (CommissionFeeGroup) find(
                "FROM CommissionFeeGroup a WHERE a.name = ?", name);

        return commfeeGroup;
    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#findCommissionFeeGroupByName (java.lang.String)
     */
    public List<TxnCommissionFee> listPendingCommissionFees(String profileId) {

        return list(
                "FROM TxnCommissionFee tcf WHERE  tcf.status = 1 AND tcf.profile.profileId = ?",
                profileId);

    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#findAgent(com.ese.entity.profile .Profile,
     * java.util.Date, java.util.Date, int, int)
     */
    public TxnCommissionFee findAgent(Profile profile, Date startDate, Date endDate,
            int onlineTxnType) {

        Object[] bindValues = { profile, startDate, endDate, onlineTxnType };
        TxnCommissionFee txnCommissionFee = (TxnCommissionFee) find(
                "FROM TxnCommissionFee a WHERE a.profile = ? and (a.commDate between ? and ?) and (a.txnType.id=? ) ",
                bindValues);
        return txnCommissionFee;

    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#listCommissionFeesTxn()
     */
    public List<TxnCommissionFee> listCommissionFeesTxn() {

        return list("FROM TxnCommissionFee");

    }

    /*
     * (non-Javadoc)v
     * @see com.ese.dao.credential.ICommissionFeeDAO#commissionCountByTxn()
     */
    public List commissionCountByTxn(int status) {

        return list("select distinct a.txnType,sum(a.txnCount),sum(a.commAmount),ca.currency from TxnCommissionFee a join a.profile.commissionAccounts ca where a.status=?  group by a.txnType,ca.currency ",status);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#commissionByAgentAndTxn(com.
     * ese.entity.profile.Profile)
     */
    public List commissionByAgentAndTxn(Profile p,int status) {
    	 Object[] bindValues = { p.getId(), status };
        return list(
                "select  a.txnType,sum(a.txnCount),sum(a.commAmount),a.status,a.commDate from TxnCommissionFee a where a.profile.id=? and a.status=?  group by a.txnType",
                bindValues);
    }

    /*
     * (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#listAgentsHaveFee()
     */
    public List<Object> listAgentsHaveFee(int status) {

        return list("select distinct c.profile FROM TxnCommissionFee c where c.status=?",status);
    }

    /* (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#listCommissionFeesCurrentTxn()
     */
    public List<TxnCommissionFee> listCommissionFeesCurrentTxn() {

        return list("FROM TxnCommissionFee a where  a.status= 0");

    }

    /* (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#listCommissionFeesPendingTxn()
     */
    public List<TxnCommissionFee> listCommissionFeesPendingTxn() {

        return list("FROM TxnCommissionFee a where  a.status= 1");

    }

    /* (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#listAllPendingCommissionPayments()
     */
    public List<CommissionPayment> listAllPendingCommissionPayments() {

        return list("from CommissionPayment o where  o.status !=0");
    }

    /* (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#findCommissionPaymentById(long)
     */
    public CommissionPayment findCommissionPaymentById(long key) {

        return (CommissionPayment) find("from CommissionPayment o where  o.id=?", key);
    }

    /* (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#findCommissionPaymentByPaymentRefernceId(long)
     */
    public CommissionPayment findCommissionPaymentByPaymentRefernceId(long key) {

        return (CommissionPayment) find(
                "from CommissionPayment o where  o.status !=0 and o.paymentRefernceId=?", key);
    }

    /* (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#listCommissionFeesTxnByCommissionPayment(com.ese.entity.txn.fee.CommissionPayment)
     */
    public List<TxnCommissionFee> listCommissionFeesTxnByCommissionPayment(
            CommissionPayment commissionPayment) {

        return list("FROM TxnCommissionFee a where a.payment=?", commissionPayment);
    }

    /* (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#commissionPaymentByAgentAndTxn(com.ese.entity.profile.Profile, int)
     */
    public List<Object[]> commissionPaymentByAgentAndTxn(Profile p, int status) {

        // TODO Auto-generated method stub
        Object[] bindValues = { p.getId(), status };
        return list(
                "select  a.txnType,sum(a.txnCount),sum(a.commAmount),a.status,a.commDate from TxnCommissionFee a where a.profile.id=? and a.status=?  group by a.txnType",
                bindValues);
    }
    
    /* (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#commissionPaymentByAgentAndDate(long, java.util.Date, java.util.Date)
     */
    public List<Object[]> commissionPaymentByAgentAndDate(String agentId, Date startDate, Date endDate){

        // TODO Auto-generated method stub
        Object[] bindValues = { agentId,startDate,endDate};
        return list(
                "select  a.txnType,sum(a.txnCount),sum(a.commAmount),a.commDate from TxnCommissionFee a where a.profile.personalInfo.identityNumber=? and (a.commDate between ? and ?) and a.status=3 group by a.commDate",
                bindValues);
    }

    /* (non-Javadoc)
     * @see com.ese.dao.credential.ICommissionFeeDAO#listAllPendingCommissionPaymentsToProcess()
     */
    public List<CommissionPayment> listAllPendingCommissionPaymentsToProcess() {

        return list("from CommissionPayment o where  o.status !=0 and o.retry=1");
    }

	/* (non-Javadoc)
	 * @see com.ese.dao.credential.ICommissionFeeDAO#listFailedCommissionPayments(java.util.Date, java.util.Date)
	 */
	public List<CommissionPayment> listFailedCommissionPayments(Date sDate,Date eDate) {
		Object[] bindValues = {sDate,eDate};		
		 return list("from CommissionPayment o where  o.status !=0 and (o.txnDate between ? and ?)",bindValues);
	}

	/* (non-Javadoc)
	 * @see com.ese.dao.credential.ICommissionFeeDAO#listSuccessCommissionPayments(java.util.Date, java.util.Date)
	 */
	public List<CommissionPayment> listSuccessCommissionPayments(Date sDate,Date eDate) {
		Object[] bindValues = {sDate,eDate};
		 return list("from CommissionPayment o where  o.status =0 and (o.txnDate between ? and ?)",bindValues);
	}

	/* (non-Javadoc)
	 * @see com.ese.dao.credential.ICommissionFeeDAO#listCommissionPaymentsByCommerceID(java.lang.String)
	 */
	public List<CommissionPayment> listCommissionPaymentsByCommerceID(
			String commerceId) {
		 return list("from CommissionPayment o where  o.status !=0 and o.commerceId=? ",commerceId);
	}
	
	/* (non-Javadoc)
	 * @see com.ese.dao.credential.ICommissionFeeDAO#listSuccessCommissionPayments(java.util.Date, java.util.Date, java.lang.String)
	 */
	public List<Object[]> listSuccessCommissionPayments(Date sDate,Date eDate,String commerceId) {
		Object[] bindValues = {sDate,eDate,commerceId};
		return list("select distinct month(o.txnDate),year(o.txnDate),count(o.id),concat(o.txnCode),sum(o.amount),o.currency from CommissionPayment o where  o.status =0 and (o.txnDate between ? and ?) and o.commerceId=?  group by month(o.txnDate), year(o.txnDate)",bindValues);
	}

}
