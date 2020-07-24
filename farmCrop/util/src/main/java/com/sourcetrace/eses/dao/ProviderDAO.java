/*
 * ProviderDAO.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sourcetrace.esesw.entity.profile.Parameter;
import com.sourcetrace.esesw.entity.profile.ParameterType;
import com.sourcetrace.esesw.entity.profile.Provider;
import com.sourcetrace.esesw.entity.profile.ResponseParameter;
import com.sourcetrace.esesw.entity.profile.TxnType;

// TODO: Auto-generated Javadoc
/**
 * The Class ProviderDAO.
 * 
 * @author $Author: moorthy $
 * @version $Rev: 1299 $ $Date: 2010-06-28 19:39:26 +0530 (Mon, 28 Jun 2010) $
 */
@Repository
@Transactional
public class ProviderDAO extends ESEDAO implements IProviderDAO {
	
	@Autowired
	public ProviderDAO(SessionFactory sessionFactory) {
		// TODO Auto-generated constructor stub
		this.setSessionFactory(sessionFactory);
	}

	/**
	 * List.
	 * 
	 * @return the list< provider>
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.IProviderDAO#list()
	 */
	public List<Provider> list() {

		return list("FROM Provider");
	}

	/**
	 * Find provider.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the provider
	 */
	public Provider find(long id) {

		return (Provider) find("FROM Provider p WHERE p.id=?", id);
	}

	/**
	 * Find provider by provider id.
	 * 
	 * @param provId
	 *            the prov id
	 * 
	 * @return the provider
	 */
	public Provider findByProviderId(String provId) {

		return (Provider) find("FROM Provider p WHERE p.providerId=?", provId);
	}

	/**
	 * List by category.
	 * 
	 * @param categoryCode
	 *            the category code
	 * 
	 * @return the list< provider>
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.IProviderDAO#listByCategory(java.lang.String)
	 */
	public List<Provider> listByCategory(String categoryCode) {

		List<Provider> providers = (List<Provider>) list("FROM Provider p WHERE p.category.code=?", categoryCode);
		return providers;
	}

	/**
	 * List by payment day.
	 * 
	 * @param paymentDayCode
	 *            the payment day code
	 * 
	 * @return the list< provider>
	 */
	public List<Provider> listByPaymentDay(String paymentDayCode) {

		List<Provider> providers = (List<Provider>) list("FROM Provider p WHERE p.paymentDay.code=?", paymentDayCode);
		return providers;
	}

	/**
	 * List by payment type.
	 * 
	 * @param paymentTypeCode
	 *            the payment type code
	 * 
	 * @return the list< provider>
	 */
	public List<Provider> listByPaymentType(String paymentTypeCode) {

		List<Provider> providers = (List<Provider>) list("FROM Provider p WHERE p.paymentType.code=?", paymentTypeCode);
		return providers;
	}

	/**
	 * List by payment period.
	 * 
	 * @param paymentPeriodCode
	 *            the payment period code
	 * 
	 * @return the list< provider>
	 */
	public List<Provider> listByPaymentPeriod(String paymentPeriodCode) {

		List<Provider> providers = (List<Provider>) list("FROM Provider p WHERE p.paymentPeriod.code=?",
				paymentPeriodCode);
		return providers;
	}

	/**
	 * Find provider by provider name.
	 * 
	 * @param providername
	 *            the providername
	 * 
	 * @return the provider
	 */
	public Provider findProviderByProviderName(String providername) {

		Provider provider = (Provider) find("from Provider p where p.name = ?", providername);
		return provider;
	}

	/**
	 * Find with payments.
	 * 
	 * @param provId
	 *            the prov id
	 * 
	 * @return the provider
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.IProviderDAO#findWithPayments(java.lang.String)
	 */
	public Provider findWithPayments(String provId) {

		Provider provider = (Provider) find("FROM Provider p LEFT JOIN FETCH p.payments WHERE p.providerId = ?",
				provId);
		return provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.IProviderDAO#findParameterbyId(long)
	 */
	public Parameter findParameterbyId(long id) {

		return (Parameter) find("FROM Parameter p WHERE p.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.IProviderDAO#listParameterTypes()
	 */
	public List<ParameterType> listParameterTypes() {

		return list("FROM ParameterType");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.IProviderDAO#findParameterTypeByid(
	 * long)
	 */
	public ParameterType findParameterTypeByid(long parameterTypeId) {

		return (ParameterType) find("FROM ParameterType p WHERE p.id=?", parameterTypeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.IProviderDAO#findTxnTypebyId(long)
	 */
	public TxnType findTxnTypebyId(long valueOf) {

		return (TxnType) find("FROM TxnType p WHERE p.id=?", valueOf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.IProviderDAO#listProviderTxnTypes(java
	 * .lang.String)
	 */
	public List<TxnType> listProviderTxnTypes(String providerId) {

		List<TxnType> types = (List<TxnType>) currentSession().createSQLQuery("SELECT * FROM TXN_TYPE t WHERE t.ID IN "
				+ "(SELECT TXN_TYPE_ID FROM PROV_TXN_TYPE " + "WHERE  PROV_ID =?)").

				addEntity("p", TxnType.class).setParameter(0, providerId).list();
		return types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.IProviderDAO#listTxnTypes()
	 */
	public List<TxnType> listTxnTypes() {

		return list("FROM TxnType");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.IProviderDAO#addProviderTxnTypes(long,
	 * long)
	 */
	public void addProviderTxnTypes(long providerId, long txnTypeId) {

		if (txnTypeId != 0) {
			// delete operation before update
			currentSession().createSQLQuery("INSERT INTO PROV_TXN_TYPE (TXN_TYPE_ID,PROV_ID)" + " VALUES (?,?)")
					.setParameter(0, txnTypeId).setParameter(1, providerId).executeUpdate();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.IProviderDAO#deleteProviderTxnTypes
	 * (long)
	 */
	public void deleteProviderTxnTypes(long providerId) {

		currentSession().createSQLQuery("DELETE FROM PROV_TXN_TYPE WHERE PROV_ID=?").setParameter(0, providerId)
				.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.IProviderDAO#findParameterbyName(java
	 * .lang.String)
	 */
	public Parameter findParameterbyName(String name) {

		return (Parameter) find("FROM Parameter p WHERE p.name=?", name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.esesw.dao.profile.IProviderDAO#findResponseParameterbyId(
	 * java.lang.Long)
	 */
	public ResponseParameter findResponseParameterbyId(Long id) {

		return (ResponseParameter) find("FROM ResponseParameter p WHERE p.id=?", id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.esesw.dao.profile.IProviderDAO#
	 * findResponseParameterbyName(java.lang.String)
	 */
	public ResponseParameter findResponseParameterbyName(String name) {

		return (ResponseParameter) find("FROM ResponseParameter p WHERE p.name=?", name);
	}

	public List<Provider> findProviderByTxnType(String id) {

		List<Provider> providers = list("FROM Provider p where p.txnTypes.id=?", id);

		return providers;
	}

}
