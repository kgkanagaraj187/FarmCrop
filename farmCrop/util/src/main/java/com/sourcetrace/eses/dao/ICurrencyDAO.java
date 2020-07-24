/*
 * ICurrencyDAO.java
 * Copyright (c) 2008-2009, Source Trace Systems
 * ALL RIGHTS RESERVED
 */
package com.sourcetrace.eses.dao;

import java.util.List;

import com.sourcetrace.esesw.entity.profile.Currency;

/**
 * The Interface ICurrencyDAO.
 * 
 * @author $Author: aravind $
 * @version $Rev: 59 $, $Date: 2009-08-04 17:10:51 +0530 (Tue, 04 Aug 2009) $
 */
public interface ICurrencyDAO extends IESEDAO {

	/**
	 * Find currency.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the currency
	 */
	public Currency findCurrency(long id);

	/**
	 * Currency by code.
	 * 
	 * @param currencyCode
	 *            the currency code
	 * 
	 * @return the list< currency>
	 */
	public Currency currencyByCode(String currencyCode);

	/**
	 * List currency.
	 * 
	 * @return the list< currency>
	 */
	public List<Currency> listCurrency();

}
