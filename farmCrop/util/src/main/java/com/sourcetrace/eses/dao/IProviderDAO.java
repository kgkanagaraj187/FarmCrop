/*
 * IProviderDAO.java
 * Copyright (c) 2008-2009, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.Parameter;
import com.sourcetrace.esesw.entity.profile.ParameterType;
import com.sourcetrace.esesw.entity.profile.Provider;
import com.sourcetrace.esesw.entity.profile.ResponseParameter;
import com.sourcetrace.esesw.entity.profile.TxnType;

// TODO: Auto-generated Javadoc
/**
 * The Interface IProviderDAO.
 * 
 * @author $Author: moorthy $
 * @version $Rev: 1413 $ $Date: 2010-09-08 18:51:40 +0530 (Wed, 08 Sep 2010) $
 */
public interface IProviderDAO extends IESEDAO {

	/**
	 * List providers.
	 * 
	 * @return the list< provider>
	 */
	public List<Provider> list();

	/**
	 * List by category.
	 * 
	 * @param categoryCode the category code
	 * 
	 * @return the list< provider>
	 */
	public List<Provider> listByCategory(String categoryCode);

	/**
	 * Find provider.
	 * 
	 * @param id the id
	 * 
	 * @return the provider
	 */
	public Provider find(long id);

	/**
	 * Find provider by prov id.
	 * 
	 * @param provId the prov id
	 * 
	 * @return the provider
	 */
	public Provider findByProviderId(String provId);

	/**
	 * Find with payments.
	 * 
	 * @param provId the prov id
	 * 
	 * @return the provider
	 */
	public Provider findWithPayments(String provId);

	/**
	 * Find provider by provider name.
	 * 
	 * @param providername the providername
	 * 
	 * @return the provider
	 */
	public Provider findProviderByProviderName(String providername);

	/**
	 * Find parameterby id.
	 * 
	 * @param id the id
	 * 
	 * @return the parameter
	 */
	public Parameter findParameterbyId(long id);

	/**
	 * List parameter types.
	 * 
	 * @return the list< parameter type>
	 */
	public List<ParameterType> listParameterTypes();

	/**
	 * Find parameter type byid.
	 * 
	 * @param parameterTypeId the parameter type id
	 * 
	 * @return the parameter type
	 */
	public ParameterType findParameterTypeByid(long parameterTypeId);

	/**
	 * Find txn typeby id.
	 * 
	 * @param valueOf the value of
	 * 
	 * @return the txn type
	 */
	public TxnType findTxnTypebyId(long valueOf);

	/**
	 * List provider txn types.
	 * 
	 * @param providerId the provider id
	 * 
	 * @return the list< txn type>
	 */
	public List<TxnType> listProviderTxnTypes(String providerId);

	/**
	 * List txn types.
	 * 
	 * @return the list< txn type>
	 */
	public List<TxnType> listTxnTypes();

	/**
	 * Delete provider txn types.
	 * 
	 * @param providerId the provider id
	 */
	public void deleteProviderTxnTypes(long providerId);

	/**
	 * Adds the provider txn types.
	 * 
	 * @param providerId the provider id
	 * @param txnTypeId the txn type id
	 */
	public void addProviderTxnTypes(long providerId, long txnTypeId);

	/**
	 * Find parameterby name.
	 * 
	 * @param name the name
	 * 
	 * @return the parameter
	 */
	public Parameter findParameterbyName(String name);

	/**
	 * Find response parameterby id.
	 * 
	 * @param id the id
	 * 
	 * @return the response parameter
	 */
	public ResponseParameter findResponseParameterbyId(Long id);

	/**
	 * Find response parameterby name.
	 * 
	 * @param name the name
	 * 
	 * @return the response parameter
	 */
	public ResponseParameter findResponseParameterbyName(String name);
	
	/**
	 * List by payment day.
	 * 
	 * @param paymentDayCode the payment day code
	 * 
	 * @return the list< provider>
	 */
	public List<Provider> listByPaymentDay(String paymentDayCode);
	
	/**
	 * List by payment type.
	 * 
	 * @param paymentTypeCode the payment type code
	 * 
	 * @return the list< provider>
	 */
	public List<Provider> listByPaymentType(String paymentTypeCode);
	
	/**
	 * List by payment period.
	 * 
	 * @param paymentPeriodCode the payment period code
	 * 
	 * @return the list< provider>
	 */
	public List<Provider> listByPaymentPeriod(String paymentPeriodCode);

    public List<Provider> findProviderByTxnType(String id);
}
