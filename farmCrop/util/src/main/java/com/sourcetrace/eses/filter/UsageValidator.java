/*
 * UsageValidator.java
 * Copyright (c) 2013-2014, SourceTrace Systems, All Rights Reserved.
 *
 * This software is the confidential and proprietary information of SourceTrace Systems
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * SourceTrace Systems.
 */
package com.sourcetrace.eses.filter;

import java.util.Date;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.UncategorizedSQLException;

import com.ese.dao.util.ILicenseDAO;
import com.ese.entity.util.ESESystem;
import com.sourcetrace.eses.dao.IESESystemDAO;
import com.sourcetrace.eses.txn.exception.WebConsoleError;
import com.sourcetrace.eses.txn.exception.WebConsoleException;
import com.sourcetrace.eses.util.DateUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.eses.util.StringUtil;
import com.sourcetrace.eses.util.crypto.ICryptoUtil;
import com.sourcetrace.eses.util.entity.License;
import com.sourcetrace.eses.util.entity.LicenseType;

/**
 * The Class UsageValidator.
 * 
 * @author $Author:$
 * @version $Rev:$ $Date:$
 */

public class UsageValidator implements IUsageValidator {

	@Autowired
	private ILicenseDAO licenseDAO;
	@Autowired
	private ICryptoUtil tripleDES;
	@Autowired
	private IESESystemDAO systemDAO;

	/**
	 * Sets the license dao.
	 * 
	 * @param licenseDAO
	 *            the new license dao
	 */
	public void setLicenseDAO(ILicenseDAO licenseDAO) {

		this.licenseDAO = licenseDAO;
	}

	/**
	 * Sets the triple des.
	 * 
	 * @param tripleDES
	 *            the new triple des
	 */
	public void setTripleDES(ICryptoUtil tripleDES) {

		this.tripleDES = tripleDES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.util.filter.IUsageValidator#validate()
	 */
	public void validate() {

		try {
			License license = licenseDAO.find();
			if (!ObjectUtil.isEmpty(license)) {
				license = validateLicense(license);
				licenseDAO.update(license);
			} else {
				throw new WebConsoleException(WebConsoleError.NO_LICENCE);
			}
		} catch (DataAccessResourceFailureException e) {
			throw new WebConsoleException(WebConsoleError.DATABASE_CONNECTION_ERROR);
		} catch (UncategorizedSQLException e) {
			throw new WebConsoleException(WebConsoleError.DATABASE_CONNECTION_ERROR);
		} catch (Exception e) {
			if (e instanceof WebConsoleException) {
				throw (WebConsoleException) e;
			} else {
				throw new WebConsoleException(WebConsoleError.INVALID_LICENCE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sourcetrace.eses.util.filter.IUsageValidator#validate(java.lang.
	 * String)
	 */
	public void validate(String key) {

		try {
			License license = new License();
			license.setKey(key);
			license = validateLicense(license);
			licenseDAO.save(license);
		} catch (Exception e) {
			if (e instanceof WebConsoleException) {
				throw (WebConsoleException) e;
			} else {
				throw new WebConsoleException(WebConsoleError.INVALID_LICENCE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sourcetrace.eses.util.filter.IUsageValidator#validateLicenseForTxn()
	 */
	public void validateLicenseForTxn() {

		try {
			License license = licenseDAO.find();
			if (!ObjectUtil.isEmpty(license)) {
				license = validateLicense(license);
			} else {
				throw new WebConsoleException(WebConsoleError.NO_LICENCE);
			}
		} catch (Exception e) {
			if (e instanceof WebConsoleException) {
				throw (WebConsoleException) e;
			} else {
				throw new WebConsoleException(WebConsoleError.INVALID_LICENCE);
			}
		}
	}

	private License validateLicense(License license) throws WebConsoleException {

		license = parseLicenseKey(license);
		Date today = DateUtil.getDateWithoutTime(new Date());
		if (license.getEnd().compareTo(today) >= 0) {
			ESESystem preferences = systemDAO.findPrefernceById(ESESystem.CLIENT);
			String clientName = preferences.getPreferences().get(ESESystem.CLIENT_NAME);
			if (!StringUtil.isEmpty(clientName) && clientName.equals(license.getClient())) {
				return license;
			} else {
				throw new WebConsoleException(WebConsoleError.UNAUTHORIZED_CLIENT);
			}
		} else {
			throw new WebConsoleException(WebConsoleError.EXPIRED_LICENCE);
		}
	}

	private License parseLicenseKey(License license) {

		String text = tripleDES.decrypt(license.getKey());
		StringTokenizer tokens = new StringTokenizer(text, ":");
		license.setType(LicenseType.valueOf(tokens.nextToken()));
		license.setVersion(tokens.nextToken());
		license.setClient(tokens.nextToken());
		license.setStart(DateUtil.convertStringToDate(tokens.nextToken(), "MMddyyyy"));
		license.setEnd(DateUtil.convertStringToDate(tokens.nextToken(), "MMddyyyy"));
		license.setOwner(tokens.nextToken());
		return license;

	}

	

}
